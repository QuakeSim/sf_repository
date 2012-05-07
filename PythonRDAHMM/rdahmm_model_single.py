#!/usr/bin/python
#==========================================================================
# Given an ingested scripps dataset, generate necessary input files and 
# execute RDAHMM modeling command for each station. 
# To be invoked by the overall rdahmm_model.py using subprocess, 
# ingested data and destination model directories are defined in properties.
# 
# JIRA QUAKESIM-219: 
# a) All data should be trained for at least 3 years. 
# b) Data with < 3 years should also be processed, 
#    but user is warned that these are trained on insufficient data.  
# c) Data > 3 years should trained up to Dec 31, 2011 or today's date, 
#    which ever is longer. 
# d) Data with < 3 years should be trained on all the available data.  
# e) We will need to retrain data < 3 years old every week.
# f) We will need to let the user know the training period for all data 
#    and when the data training period is changed 
#    (if we change--say, years from now)
#
# input: scripps dataset name, e.g WNAM_Clean_DetrendNeuTimeSeries_comb
# output: RDAHMM input files and modeling results for each station
#
# usage:
#   rdahmm_model_single.py scripps_dataset_name
#
# output:
#   /path/to/rdahmm/model/station/*
#===========================================================================
import os, sys, string, re, glob
import sqlite3 as db
from datetime import date
from datetime import timedelta
from properties import properties

numargv = len(sys.argv)
if numargv == 1:
    sys.exit("usage: scripps_ingest_single.py scripps_dataset_name")
elif numargv == 2:
    dataset = sys.argv[1]
else:
    sys.exit("Invalid number of parameters!")

data_path = properties('data_path') + "/" + dataset + "/"
model_path = properties('model_path') + "/" + dataset + "/"
train_epoch = properties('train_epoch')

if not os.path.exists(model_path):
    cmd = "mkdir -p " + model_path
    os.system(cmd)

for dbfile in glob.glob(data_path+"/????.sqlite"):
    stationID = string.split(dbfile, "/")[-1][:-7]
    # print stationID
    stationDir = model_path + stationID + "/"
    if not os.path.exists(stationDir):
        cmd = "mkdir -p " + stationDir
        os.system(cmd)
    # use station model directory as current working directory
    os.chdir(stationDir)

    # connect to the station database to generate training data file
    conn = db.connect(dbfile)
    cur = conn.cursor()
    # check if there's more than 3 years of data by end of train_epoch 
    sql ="SELECT count(*) FROM StationGPSTimeSeries WHERE Timestamp <= '" + train_epoch + "'"
    modelcounts = cur.execute(sql).fetchone()[0]
    if (modelcounts < 365*3):
        sql = "SELECT MAX(Timestamp) FROM StationGPSTimeSeries"
        end_epoch = cur.execute(sql).fetchone()[0]
    else:
        end_epoch = train_epoch
    sql = "SELECT MIN(Timestamp) FROM StationGPSTimeSeries"
    start_epoch = cur.execute(sql).fetchone()[0]
    sys.exit(0)
