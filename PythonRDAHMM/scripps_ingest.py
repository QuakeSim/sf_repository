#!/usr/bin/python
#==========================================================================
# Ingest all scripps datasets downloaded into proper directories and databases
# Original download directory path is hard-coded. Use subprocess to invoke
# multiple scripps_ingest_single.py for parallel processing
#
# usage: scripps_ingest.py
#
#===========================================================================
import os, glob, subprocess
from threading import Thread
#import time

scripps_data = "/home/yuma/RDAHMM/Download/WesternNorthAmerica/*.tar"

class ThreadJob(Thread):

    def __init__(self, dataset):
        Thread.__init__(self)
        self.dataset = dataset

    def run(self):
        cmd = "/home/yuma/RDAHMM/Scripts/scripps_ingest_single.py"
        # start = time.time()
        print "+++Starting process ", dataset, " ..."
        p = subprocess.Popen([cmd, self.dataset], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        (stdout, stderr) = p.communicate()
        # end = time.time()
        if p.returncode != 0:
            print p.stderr        
        print "+++Finished process ", dataset

for dataset in glob.glob(scripps_data):
    t = ThreadJob(dataset)
    t.start()
