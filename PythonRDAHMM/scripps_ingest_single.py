#!/usr/bin/python
#==========================================================================
# Ingest a given scripps dataset into the corresponding database. 
# To be invoked by the overall scripps_ingest.py using subprocess, 
# destination data directory and temporary working directory are hard-coded.
#
# input: path to the original scripps tar file; 
# output: corresponding sqlite db file with all data ingested; 
#
# usage:
#   scripps_ingest_single.py /path/to/download/scripps_data.tar
#
# output:
#   /path/to/rdahmm/scripps_data.sqlite
#===========================================================================
import os, sys, string, re
import sqlite3 as db
from datetime import date

numargv = len(sys.argv)
if numargv == 1:
    sys.exit("usage: scripps_ingest_single.py /path/to/scripps_data.tar")
elif numargv == 2:
    [scripps_path, tarfile] = os.path.split(sys.argv[1])
    scripps_path += "/"
else:
    sys.exit("Invalid number of parameters!")

rdahmm_path = "/home/yuma/RDAHMM/Data/"
temp_path = "/home/yuma/RDAHMM/TEMP/"

datadir = rdahmm_path + tarfile[:tarfile.rfind("_")] + "/"
dbfile = datadir + tarfile[:-4] + ".sqlite"
workdir = temp_path + tarfile[:tarfile.rfind("_")] + "/"
#print datadir, dbfile

if not os.path.exists(datadir):
    cmd = "mkdir -p " + datadir
    os.system(cmd) 
if not os.path.exists(workdir):
    cmd = "mkdir -p " + workdir
    os.system(cmd) 

#if the same db file exists, drop it
if os.path.isfile(dbfile):
    print "deleting old database"
    os.remove(dbfile)

# creating/connecting the database 
conn = db.connect(dbfile)
# creating a Cursor
cur = conn.cursor()
# creating tables
sql ="""CREATE TABLE GPSTimeSeries (
      StationID CHAR(4), 
      North Num,
      East Num,
      Up  Num,
      Nsig Num,
      Esig Num, 
      Usig Num,
      Timestamp TEXT,
      UNIQUE (StationID, Timestamp))"""
cur.execute(sql)
sql ="""CREATE TABLE ReferencePositions (
      StationID CHAR(4), 
      Latitude Num,
      Longitude Num, 
      Height Num, 
      UNIQUE (StationID))"""
cur.execute(sql)
conn.commit()

# clear working directory
cmd = "rm -f " + workdir + "*"
os.system(cmd)

print "Processing ", tarfile, "..." 
# unpack data 
cmd = "tar xvf " + scripps_path + tarfile + " -C " + workdir
os.system(cmd)
dirlist = os.listdir(workdir)
dirlist.sort()
for datafile in dirlist:
    if datafile[-2:] == ".Z":
        cmd = "unzip " + workdir + datafile + " -d " + workdir
        os.system(cmd)
        datafile = datafile[:-2]
        stationID = datafile[:4]
    with open(workdir + datafile, 'r') as f:
        data = f.readlines()
        for line in data:
            if "Reference position" in line:
                refs = map(float, re.findall("(-?[0-9.]*[0-9]+)", line))
                lat = refs[0] + refs[1]/60.0 + refs[2]/3600.0
                long = -1.0 * (refs[3] + refs[4]/60.0 + refs[5]/3600.0)
                height = refs[6]
                sql = "INSERT INTO ReferencePositions (StationID, Latitude, Longitude, Height) "
                sql += " VALUES ('%s', '%s', '%s', '%s')" % (stationID, lat, long, height)
                cur.execute(sql)
            if not "#" in line:
                record = string.split(line)
                [year, days] = map(int, record[1:3])
                timestamp = date.fromordinal(date(year,1,1).toordinal()+days -1)
                [north, east, up, nsig, esig, usig] = record[3:9]
                sql = "INSERT INTO GPSTimeSeries (StationID, North, East, Up, Nsig, Esig, Usig, Timestamp) "
                sql += " VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')" % (stationID, north, east, up, nsig, esig, usig, timestamp)
                cur.execute(sql)
    conn.commit()
    f.closed

    # create index 
sql = "CREATE INDEX idx_StationID ON GPSTimeSeries(StationID)"
cur.execute(sql)
sql = "CREATE INDEX idx_Timestamp ON GPSTimeSeries(Timestamp)"
cur.execute(sql)
sql = "CREATE INDEX idx_RefStationID ON ReferencePositions(StationID)"
cur.execute(sql)

conn.commit()

cur.close()
conn.close()

# clear working directory
#cmd = "rm -f " + workdir + "*"
#os.system(cmd)
