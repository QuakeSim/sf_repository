#!/usr/bin/python
#==========================================================================
# Ingest all scripps datasets downloaded into proper directories and databases
# Original download directory path is defined in properties. Use subprocess to 
# invoke multiple scripps_ingest_single.py for parallel processing
#
# usage: scripps_ingest.py
#
#===========================================================================
import os, glob, subprocess, sys
from threading import Thread
from properties import properties
#import time

#scripps_data = "/home/yuma/RDAHMM/Download/WesternNorthAmerica/*.tar"
scripps_data = properties('download_path') + "/WesternNorthAmerica/*.tar"
scripps_cmd = properties('script_path') + "/scripps_ingest_single.py"
#print scripps_data, scripps_cmd
#sys.exit(0)

class ThreadJob(Thread):

    def __init__(self, dataset):
        Thread.__init__(self)
        self.dataset = dataset

    def run(self):
        #cmd = "/home/yuma/RDAHMM/Scripts/scripps_ingest_single.py"
        cmd = scripps_cmd
        # start = time.time()
        print "+++Starting process ", dataset, " ..."
        p = subprocess.Popen([cmd, self.dataset], stdout=subprocess.PIPE, stderr=subprocess.PIPE)
        (stdout, stderr) = p.communicate()
        # end = time.time()
        if p.returncode != 0:
            print p.stderr        
        print "+++Finished process ", dataset

for dataset in glob.glob(scripps_data):
    if "Strain" in dataset:
        continue
    #print dataset
    t = ThreadJob(dataset)
    t.start()
