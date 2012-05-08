#!/usr/bin/python
#==========================================================================
# Given a RDAHMM Model dataset, generate necessary input files and 
# execute RDAHMM evaluation command for each station, as well as produce
# necessary files for front end plotting.
# To be invoked by the overall rdahmm_eval.py using subprocess, 
# Ingested data, model and destination evaluation directories are defined 
# in properties.
# 
# input: scripps dataset name, e.g WNAM_Clean_DetrendNeuTimeSeries_comb
# output: RDAHMM input files and evaluation results for each station
#
# usage:
#   rdahmm_eval_single.py scripps_dataset_name
#
# output:
#   /path/to/rdahmm/eval/daily_project_stationID_eval_date/*
#   Besides rdahmm evaluation result files: 
#       - stationID.sqlite is copied from scripps ingested data directory, 
#         which was used to generate daily_project_stationID.input
#       - daily_project_stationID.input.[start|end]time specifying model
#         training range
#===========================================================================
import os, sys, string 
import sqlite3 as db
import datetime, csv
from properties import properties

numargv = len(sys.argv)
if numargv == 1:
    sys.exit("usage: rdahmm_eval_single.py scripps_dataset_name")
elif numargv == 2:
    dataset = sys.argv[1]
else:
    sys.exit("Invalid number of parameters!")

data_path = properties('data_path') + "/" + dataset + "/"
model_path = properties('model_path') + "/" + dataset + "/"
eval_path = properties('eval_path') + "/" + dataset + "/"
rdahmm_bin = properties('rdahmm_bin')
rdahmm_eval_parm = properties('rdahmm_eval_parm')

today = datetime.date.today().isoformat()

if not os.path.exists(eval_path):
    cmd = "mkdir -p " + eval_path
    os.system(cmd)

for station in os.listdir(model_path):
    stationID = station[-4:]
    # station eval directory is named as: daily_project_twhl_2012-05-08
    stationDir = eval_path + "daily_project_" + stationID + "_" + today + "/"
    if not os.path.exists(stationDir):
        cmd = "mkdir -p " + stationDir
        os.system(cmd)
    # use station eval directory as current working directory
    os.chdir(stationDir)

    # copy station dbfile from data_path to eval directory
    dbfile = data_path + stationID + ".sqlite"
    cmd = "cp -p " + dbfile + " ."
    os.system(cmd)
    stationdb = stationDir + stationID + ".sqlite"

    # copy model results from model_path to eval directory
    modelfiles = model_path + "daily_project_" + stationID + "/daily_project_*"
    cmd = "cp -p " + modelfiles + " ."
    os.system(cmd)

    # connect to the station database to generate evaluation input data file
    conn = db.connect(stationdb)
    cur = conn.cursor()
    # record start and end time of current evaluation
    sql = "SELECT MIN(Timestamp) FROM StationGPSTimeSeries"
    start_epoch = cur.execute(sql).fetchone()[0]
    sql = "SELECT MAX(Timestamp) FROM StationGPSTimeSeries"
    end_epoch = cur.execute(sql).fetchone()[0]

    # generate eval input file using all data up to date
    sql = "SELECT North, East, Up FROM StationGPSTimeSeries" 
    rows = cur.execute(sql).fetchall()
    dataCount = str(len(rows))
    evalfile = stationDir + "daily_project_" + stationID + "_" + today + ".all.input"
    csvWriter = csv.writer(open(evalfile, 'w'), delimiter = ' ')
    csvWriter.writerows(rows)
    cur.close()
    conn.close()
    del csvWriter

    # write start and end time of current evaluation in files
    startfile = evalfile + ".starttime"
    endfile = evalfile + ".endtime"
    with (open(startfile, 'w')) as f:
        f.write(start_epoch)
    f.close
    with (open(endfile, 'w')) as f:
        f.write(end_epoch)
    f.close
   
    # execute RDAHMM model command with properly replaced parameters
    dimensionCount = "3"
    proBaseName = "daily_project_" + stationID + "_" + today
    modelBaseName = "daily_project_" + stationID
    rdahmm_eval_parm = string.replace(rdahmm_eval_parm, "<proBaseName>", proBaseName)
    rdahmm_eval_parm = string.replace(rdahmm_eval_parm, "<dataCount>", dataCount)
    rdahmm_eval_parm = string.replace(rdahmm_eval_parm, "<dimensionCount>", dimensionCount) 
    rdahmm_eval_parm = string.replace(rdahmm_eval_parm, "<modelBaseName>", modelBaseName) 
    rdahmm_eval_cmd = rdahmm_bin + " " + rdahmm_eval_parm
    #print rdahmm_eval_cmd
    #os.system can be replaced with other non-blocking invocation method.
    os.system(rdahmm_eval_cmd)
  
    sys.exit(0)
