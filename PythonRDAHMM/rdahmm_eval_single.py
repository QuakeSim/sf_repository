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
import zipfile

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
    # sql = "SELECT MIN(Timestamp) FROM StationGPSTimeSeries"
    # start_epoch = cur.execute(sql).fetchone()[0]
    # sql = "SELECT MAX(Timestamp) FROM StationGPSTimeSeries"
    # end_epoch = cur.execute(sql).fetchone()[0]

    # generate eval input file using all data up to date
    sql = "SELECT North, East, Up FROM StationGPSTimeSeries ORDER BY Timestamp ASC" 
    rows = cur.execute(sql).fetchall()
    dataCount = str(len(rows))
    evalfile = stationDir + "daily_project_" + stationID + "_" + today + ".all.input"
    csvWriter = csv.writer(open(evalfile, 'w'), delimiter = ' ')
    csvWriter.writerows(rows)
    del csvWriter

    # following is commented out, since it's handled by plotting part
    # write start and end time of current evaluation in files
    # startfile = evalfile + ".starttime"
    # endfile = evalfile + ".endtime"
    # with (open(startfile, 'w')) as f:
    #    f.write(start_epoch)
    # f.close
    # with (open(endfile, 'w')) as f:
    #     f.write(end_epoch)
    # f.close
   
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
    # os.system can be replaced with other non-blocking invocation method 
    # for parallelism of individual stations, but synchronization will get
    # much more complicated.
    os.system(rdahmm_eval_cmd)
    # check if results .Q file contains 0, if yes rerun with -addstate option
    qfile = proBaseName + ".Q"
    with open(qfile, 'r') as qf:
        qlines = qf.read()
        if "0" in qlines:
            rdahmm_eval_cmd = rdahmm_eval_cmd + " -addstate"
            #print rdahmm_eval_cmd
            os.system(rdahmm_eval_cmd)
    qf.close
 
    # start to produce plotting related files
    # 1. zip file of the model results if not already exists
    modelzip = eval_path + "daily_project_" + stationID  + ".zip"
    if not os.path.exists(modelzip):
        modelname = "daily_project_" + stationID + "/"
        myzip = zipfile.ZipFile(modelzip, 'w')
        for filename in os.listdir(model_path+modelname):
            myzip.write(filename, modelname+filename, zipfile.ZIP_DEFLATED)
        myzip.close()
    # 2. eval result .Q file is renamed (use copy here for now) to .all.Q
    cmd = "cp -p " + proBaseName + ".Q " + proBaseName + ".all.Q"
    os.system(cmd)
    # 3. required .all.raw file for plot_go.sh, this is pretty silly
    sql = "SELECT Timestamp, North, East, Up, Nsig, Esig, Usig FROM StationGPSTimeSeries ORDER BY Timestamp ASC" 
    rows = cur.execute(sql).fetchall()
    rawfile = stationDir + "daily_project_" + stationID + "_" + today + ".all.raw"
    csvWriter = csv.writer(open(rawfile, 'w'), delimiter = ' ')
    newrows = []
    for row in rows:
        newrow = list(row)
        newrow.insert(0, stationID)
        newrows.append(newrow)
    csvWriter.writerows(newrows)
    del csvWriter
    # 4. required .plotswf.input file for plotting, again silly   
    plotswffile = stationDir + "daily_project_" + stationID + "_" + today + ".plotswf.input"
    allQfile = stationDir + "daily_project_" + stationID + "_" + today + ".all.Q"
    with open(allQfile, 'r') as qfile:
        qrows = qfile.read()
    qfile.close
    qrows = string.split(qrows)

    csvWriter = csv.writer(open(plotswffile, 'w'), delimiter = ' ')
    newrows = []
    for i in range(len(rows)):
        newrow = list(rows[i])
        newrow.insert(0, qrows[i])
        newrows.append(newrow)
    csvWriter.writerows(newrows)
    del csvWriter
 
    cur.close()
    conn.close()
    sys.exit(0)
