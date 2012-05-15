#!/usr/bin/python

# Check if everything in data_path is processed

import os, sys, string, glob
from properties import properties

data_path = properties('data_path')
model_path = properties('model_path')
eval_path = properties('eval_path')

for dataset in os.listdir(data_path):
    src = data_path + "/" + dataset + "/"
    for dbfile in glob.glob(src +"/????.sqlite"):
        stationID = string.split(dbfile, "/")[-1][:-7]
        model_dest = model_path + "/" + dataset + "/" + "daily_project_" + stationID
        eval_dest = eval_path + "/" + dataset + "/" + "daily_project_" + stationID + "*"
        if not os.path.exists(model_dest):
            print model_dest
        #if glob.glob(eval_dest) == []:
        #    print eval_dest
