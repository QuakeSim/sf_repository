#!/usr/bin/python
#==========================================================================
# Reads the list of stations and the available processed data for each station
# within a particular type (ie, WNAM_Filter_DetrendNeuTimeSeries_jpl, 
# WNAM_Clean_DetrendNeuTimeSeries_jpl, etc.
#===========================================================================
import os, sys, string, re
from datetime import date
from datetime import timedelta
from properties import properties

# Some useful global constants
xmlDeclaration='<?xml version="1.0" encoding="UTF-8"?>)'
serverName="gf16.ucs.indiana.edu"
updateTime="2012-07-13T06:22:0"
beginDate="1994-01-01"
endDate="2012-07-13"
centerLng="-119.7713889"
centerLat="36.7477778"
stationCount="1532"

# Used to separate parts of the station name
SEPARATOR_CHARACTER="_"

xmlHeaderTemplate="""  <update-time>%s</update-time>
  <data-source>%s</data-source>
  <begin-date>%s</begin-date>
  <end-date>%s</end-date>
  <center-longitude>%s</center-longitude>
  <center-latitude>%s</center-latitude>
  <output-pattern>
    <server-url>http://%s/daily_rdahmmexec/daily/%s</server-url>
    <stateChangeNumTxtFile>stateChangeNums.txt</stateChangeNumTxtFile>
    <stateChangeNumJsInput>stateChangeNums.txt.jsi</stateChangeNumJsInput>
    <allStationInputName>all_stations.all.input</allStationInputName>
    <Filters>Fill-Missing</Filters>
    <pro-dir>daily_project_{!station-id!}_%s</pro-dir>
    <AFile>daily_project_{!station-id!}.A</AFile>
    <BFile>daily_project_{!station-id!}.B</BFile>
    <InputFile>daily_project_{!station-id!}_%s.all.input</InputFile>
    <RawInputFile>daily_project_{!station-id!}_%s.all.raw</RawInputFile>
    <SwfInputFile>daily_project_{!station-id!}_%s.plotswf.input</SwfInputFile>
    <DygraphsInputFile>daily_project_{!station-id!}_%s.dygraphs.js</DygraphsInputFile>
    <LFile>daily_project_{!station-id!}.L</LFile>
    <XPngFile>daily_project_{!station-id!}_%s.all.input.X.png</XPngFile>
    <YPngFile>daily_project_{!station-id!}_%s.all.input.Y.png</YPngFile>
    <ZPngFile>daily_project_{!station-id!}_%s.all.input.Z.png</ZPngFile>
    <XTinyPngFile>daily_project_{!station-id!}_%s.all.input.X_tiny.png</XTinyPngFile>
    <YTinyPngFile>daily_project_{!station-id!}_%s.all.input.Y_tiny.png</YTinyPngFile>
    <ZTinyPngFile>daily_project_{!station-id!}_%s.all.input.Z_tiny.png</ZTinyPngFile>
    <PiFile>daily_project_{!station-id!}.pi</PiFile>
    <QFile>daily_project_{!station-id!}_%s.all.Q</QFile>
    <MaxValFile>daily_project_{!station-id!}.maxval</MaxValFile>
    <MinValFile>daily_project_{!station-id!}.minval</MinValFile>
    <RangeFile>daily_project_{!station-id!}.range</RangeFile>
    <ModelFiles>daily_project_{!station-id!}.zip</ModelFiles>
    <video-url></video-url>
  </output-pattern>
  <station-count>%s</station-count>
"""

# Read the content of sites_file_path into an array.
def readStationsList(sites_file_path):
    stations_array = []
    infile=open(sites_file_path,"r") 
    for line in infile:
        stations_array.append(line)
    infile.close()
    return stations_array
    
# Read the list of files in the eval_dir_path into another array
def readProjectDirsList(eval_dir_path):
    datasets_array=[]
    for dirname in os.listdir(eval_dir_path):
        datasets_array.append(dirname)
    return datasets_array

# Print out the arrays to verify
def printStationList(stations_array):
    print "Station list:\n"
    for item in stations_array:
        print item + "\n"
    return

# Print out the data set array
def printDataSetArray(datasets_array):
    print "Data set list: \n"
    for item in datasets_array:
        print item + "\n"
    return

def printFrontMatter(xmlFile):
    xmlFile.write(xmlDeclaration);
    xmlFile.write("<xml>")
    return

# Print the header part of the output file
def printHeader(xmlFile,dataSource,updateTime, beginDate, endDate, centerLng, centerLat, serverName, stationCount):
    xmlHeaderLines=xmlHeaderTemplate % (updateTime, dataSource,beginDate,endDate,centerLng,centerLat,serverName,dataSource,endDate,endDate,endDate,endDate,endDate,endDate,endDate,endDate,endDate,endDate,endDate,endDate,stationCount)
    xmlFile.write(xmlHeaderLines)
    return

# This just closes out the XML
def writeXmlEndTag(xmlFile):
    xmlFile.write("</xml>")
    return

# Open station XML
def openStationXml(xmlFile):
    xmlFile.write("\t<station>\n");

# Close the <station/> XML tag
def closeStationXml(xmlFile):
    xmlFile.write("\t</station>\n");

# Extract the station name from the directory name. 
def setStationId(stationList,xmlFile):
    #Get the station name.
    stationName=stationList.split(SEPARATOR_CHARACTER)[2];
    xmlFile.write("\t\t<id>"+stationName+"</id>\n");

def setStationRefLatLonHgt(stationDir,xmlFile):
    # Find the .ref file
    stationFiles=os.listdir(stationDir)
    refFileName=""
    refLat=""
    refLon=""
    refHgt=""
    for file in stationFiles:
        if(file.endswith(".ref")):
            refFileName=stationDir+"/"+file
            break
    #Now read the ref file
    with open(refFileName,"r") as refFile:
        refParts=refFile.readline().split(" ")
        refLat=refParts[0]
        refLon=refParts[1]
        refHgt=refParts[2].rstrip() # Have to chomp off the final \n
    refFile.close()
    xmlFile.write("\t\t<lat>"+refLat+"</lat>\n");
    xmlFile.write("\t\t<long>"+refLon+"</long>\n");
    xmlFile.write("\t\t<height>"+refHgt+"</height>\n");

def setStatusChanges():
    pass

def setTimesNoData():
    pass

#--------------------------------------------------
# Now run the script
#--------------------------------------------------
# Set the properties
sites_file_path = properties('download_path')+"/WNAMsites"
eval_dir_path = properties('eval_path')

#Assign the arrays
stations_array=readStationsList(sites_file_path)
datasets_array=readProjectDirsList(eval_dir_path)

# Print out the arrays to make sure they are ok
#printStationList(stations_array)
#printDataSetArray(datasets_array)

# Loop through each data set 
for dataSet in os.listdir(eval_dir_path):
    
    # Open the XML file that will contain the results
    outputPath="./junk-" + dataSet + ".xml"
    xmlFile=open(outputPath,"w");
    
    # Print XML front matter
    printFrontMatter(xmlFile)
    
    # Print out the header parts of the XML file
    printHeader(xmlFile,dataSet,updateTime,beginDate,endDate,centerLng,centerLat,serverName,stationCount)
    
    # Now loop over the station directories for each data set
    projectDir=eval_dir_path+"/"+dataSet
    for stationList in os.listdir(projectDir):
        if os.path.isdir(projectDir+"/"+stationList):
            openStationXml(xmlFile)
            setStationId(stationList,xmlFile)
            setStationRefLatLonHgt(projectDir+"/"+stationList,xmlFile)
            setStatusChanges()
            setTimesNoData()
            closeStationXml(xmlFile)

    # Write the end tag
    writeXmlEndTag(xmlFile)
    
     # Close the XML file
    xmlFile.close()
    
print "we are done"

