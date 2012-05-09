#!/usr/bin/python
#==========================================================================
# Execute rdahmm modeling for all ingested scripps datasets.
# Use subprocess to invoke multiple rdahmm_model_single.py for 
# parallel processing
#
# usage: rdahmm_model.py
#
#===========================================================================
import os, glob, subprocess, sys
from threading import Thread
from properties import properties

data_path = properties('data_path') 
model_cmd = properties('script_path') + "/rdahmm_model_single.py"

class ThreadJob(Thread):

    def __init__(self, dataset):
        Thread.__init__(self)
        self.dataset = dataset

    def run(self):
        cmd = model_cmd
        # start = time.time()
        print "+++Starting process ", dataset, " ..."
        p = subprocess.Popen([cmd, self.dataset], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        (stdout, stderr) = p.communicate()
        # end = time.time()
        if p.returncode != 0:
            print p.stderr        
        print "+++Finished process ", dataset

for dataset in os.listdir(data_path):
    t = ThreadJob(dataset)
    t.start()
