#!/usr/local/python2.6/bin/python

#============================================
# Download UNAVCO data file and convert into GRWS input format
#
# input: station ID, destination directory
# output: original unavco station data file, and converted GRWS raw input format
#
# usage:
#   python unavcotrans.py stationID destdir
#
# output:
#   stationID.pbo.igs05.csv [daily_project_stationID].unavco.raw
#============================================

import csv, sys, os, subprocess, urllib

numargv = len(sys.argv)
unavco_client_path = "./unavcoClients"
cs2cs_exec = "./cs2cs"

if numargv == 1:
    sys.exit("usage: unavcotrans.py stationID destdir")
elif numargv == 3:
    stationID = sys.argv[1]
    destdir = sys.argv[2]

    # change current working directory
    # os.chdir(destdir)

    # query UNAVCO for station data file in .pbo.igs05.csv format
    cmd = "java -jar " + unavco_client_path + "/unavcoFiles.jar -4charEqu " + stationID.upper() + " -positionCSV"
    filelist = subprocess.Popen([cmd], shell=True, stdout=subprocess.PIPE).communicate()[0].split("\n")
    fileloc = None
    for item in filelist:
        ind = item.find(".pbo.igs05.csv")
        if not ind == -1:
            fileloc = item[:ind] + ".pbo.igs05.csv"
    if fileloc == None:
        sys.exit("No data available for station " + stationID)

    # download the original UNAVCO data file
    unavcofile = destdir + "/" + stationID + ".pbo.igs05.csv"
    urllib.urlretrieve(fileloc, unavcofile)

    # convert UNAVCO data file into GRWS raw format
    rawfile = destdir + "/daily_project_" + stationID + ".unavco.raw"
    inputReader = csv.reader(open(unavcofile, 'r'), delimiter = ',')
    outputWriter = csv.writer(open(rawfile, 'w'), delimiter = ' ')
    for row in inputReader:
        if row[0] == "Reference position":
            lat = row[1].split()[0]
            long = row[2].split()[0]
            vert = row[3].split()[0]
            cmd = cs2cs_exec + " -f '%.4f' +proj=latlong +datum=WGS84 +to +proj=geocent +datum=WGS84 <<EOF \n" + long + " " + lat + " " + vert
            x, y, z = map(float, subprocess.Popen([cmd], shell=True, stdout=subprocess.PIPE).communicate()[0].split())
        if len(row) >= 8 and row[0][:4].isdigit():
            date = row[0] + "T12:00:00"
            xx, yy, zz, dx, dy, dz = [val/1000.0 for val in map(float, row[1:7])]
            ux = x + xx
            uy = y + yy
            uz = z + zz
            outputWriter.writerow([stationID, date, ux, uy, uz, dx, dy, dz])
    del inputReader, outputWriter
    print rawfile
else:
    sys.exit("Invalid number of parameters!")
