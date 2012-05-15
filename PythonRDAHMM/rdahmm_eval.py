#!/usr/bin/python
#==========================================================================
# Execute rdahmm evaluation for all ingested scripps datasets.
# Use subprocess to invoke multiple rdahmm_eval_single.py for 
# parallel processing
#
# usage: rdahmm_eval.py
#
#===========================================================================
import os, subprocess, sys
from threading import Thread
from properties import properties

model_path = properties('model_path') 
eval_cmd = properties('script_path') + "/rdahmm_eval_single.py"

class ThreadJob(Thread):

    def __init__(self, dataset):
        Thread.__init__(self)
        self.dataset = dataset

    def run(self):
        cmd = eval_cmd
        # start = time.time()
        print "+++Starting process ", dataset, " ..."
        p = subprocess.Popen([cmd, self.dataset], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        (stdout, stderr) = p.communicate()
        # end = time.time()
        if p.returncode != 0:
            print p.stderr        
        print "+++Finished process ", dataset

for dataset in os.listdir(model_path):
    if "Strain" in dataset:
        continue
    t = ThreadJob(dataset)
    t.start()
