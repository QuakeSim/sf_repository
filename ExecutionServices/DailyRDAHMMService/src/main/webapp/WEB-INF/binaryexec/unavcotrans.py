#!/usr/local/python2.6/bin/python

#============================================
# Download UNAVCO data file and convert into GRWS input format
#
# input: station ID, destination directory, begin date, end date
# output: original unavco station data file, and converted GRWS raw input format
#
# usage:
#   python unavcotrans.py stationID destdir beginDate endDate
#
# output:
#   stationID.pbo.igs05.pos [daily_project_stationID].unavco.raw
#============================================

import csv, sys, os, subprocess, urllib
from datetime import datetime, timedelta

numargv = len(sys.argv)
unavco_client_path = "./unavcoClients"

if numargv == 1:
    sys.exit("usage: unavcotrans.py stationID destdir beginDate endDate")
elif numargv == 5:
    stationID = sys.argv[1]
    destdir = sys.argv[2]
    bdate = sys.argv[3]
    edate = sys.argv[4]

    bdate = datetime.strptime(bdate, "%Y-%m-%d")
    edate = datetime.strptime(edate, "%Y-%m-%d")


    # change current working directory
    # os.chdir(destdir)

    # query UNAVCO for station data file in .pbo.igs05.csv format
    cmd = "java -jar " + unavco_client_path + "/unavcoFiles.jar -4charEqu " + stationID.upper() + " -position"
    filelist = subprocess.Popen([cmd], shell=True, stdout=subprocess.PIPE).communicate()[0].split("\n")
    fileloc = None
    for item in filelist:
        ind = item.find(".pbo.igs05.pos")
        if not ind == -1:
            fileloc = item[:ind] + ".pbo.igs05.pos"
    if fileloc == None:
        sys.exit("No data available for station " + stationID)

    # download the original UNAVCO data file
    unavcofile = destdir + "/" + stationID + ".pbo.igs05.pos"
    urllib.urlretrieve(fileloc, unavcofile)

    # convert UNAVCO data file into GRWS raw format
    rawfile = destdir + "/daily_project_" + stationID + ".unavco.raw"
    inputReader = csv.reader(open(unavcofile, 'r'), delimiter = ' ')
    outputWriter = csv.writer(open(rawfile, 'w'), delimiter = ' ')
    for row in inputReader:
        row = filter(None, row)
        if len(row) >= 25: 
            if (row[1][-2:] == '60'):
                row[1] = row[1][:-2] + '00'
                rdate = datetime.strptime(row[0]+" "+row[1], "%Y%m%d %H%M%S") + timedelta(seconds=60)
            else:
                rdate = datetime.strptime(row[0]+" "+row[1], "%Y%m%d %H%M%S")
            if rdate >= bdate and rdate <= edate: 
                date = rdate.strftime("%Y-%m-%dT%H:%M:%S")
                lat, lon, vert = map(float, row[12:15])
		if lon > 180:
		    lon = lon - 360
                dlat, dlon, dvert = map(float, row[18:21])
                outputWriter.writerow([stationID, date, lon, lat, vert, dlon, dlat, dvert])
    del inputReader, outputWriter
    print rawfile
else:
    sys.exit("Invalid number of parameters!")
