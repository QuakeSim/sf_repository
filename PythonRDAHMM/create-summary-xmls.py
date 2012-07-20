#!/usr/bin/python
#==========================================================================
# Reads the list of stations and the available processed data for each station
# within a particular type (ie, WNAM_Filter_DetrendNeuTimeSeries_jpl, 
# WNAM_Clean_DetrendNeuTimeSeries_jpl, etc.
#===========================================================================
import os, sys, string, re
from datetime import date, datetime, timedelta, time
from properties import properties

# Some useful global constants
xmlDeclaration='<?xml version="1.0" encoding="UTF-8"?>'
serverName="gf13.ucs.indiana.edu"
updateTime="2012-07-20T06:12:05"
beginDate="1994-01-01"
endDate="2012-07-20"
centerLng="-119.7713889"
centerLat="36.7477778"
stationCount="1532"

# Used to separate parts of the station name
SEPARATOR_CHARACTER="_"
NO_DATA_TIME_STAMP="22:22:22"

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
    xmlFile.write(xmlDeclaration+"\n");
    xmlFile.write("<xml>\n")
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
    if refFileName:
        with open(refFileName,"r") as refFile:
            refParts=refFile.readline().split(" ")
            refLat=refParts[0]
            refLon=refParts[1]
            refHgt=refParts[2].rstrip() # Have to chomp off the final \n
        refFile.close()
    else:
        refLat="1.0"
        refLon="2.0"
        refHgt="-1.0"
      
    xmlFile.write("\t\t<lat>"+refLat+"</lat>\n");
    xmlFile.write("\t\t<long>"+refLon+"</long>\n");
    xmlFile.write("\t\t<height>"+refHgt+"</height>\n");

def setStatusChanges(stationDir,xmlFile):
    stationFiles=os.listdir(stationDir);
    # Open the .all.Q and the .all.raw files.  We get the state from the first and
    # the data from the second. 
    # TODO: for now, we assume these files always exist
    qFileName=""
    rawFileName=""
    qFileSet=False
    rawFileSet=False
    #TODO: we could break out after both files are found.
    for file in stationFiles:
        if(file.endswith(".all.Q")):
            qFileName=file
            qFileSet=True
        if(file.endswith(".all.raw")):
            rawFileName=file
            rawFileSet=True

    # Bail out if the required files don't exist
    if((not qFileSet) or (not rawFileSet)): return 
    qFile=open(stationDir+"/"+qFileName,"r")
    rawFile=open(stationDir+"/"+rawFileName,"r")


    stateChangeString=""
    # Now step through the Q file looking for state changes
    # If we find a state change, get the date from the raw file
    # We will save these to the string stateChangeArray since we
    # need to record in latest-first order
    qline1=qFile.readline()
    rline1=rawFile.readline()        
    while True:
        qline2=qFile.readline()
        rline2=rawFile.readline()
        if not qline2: break
        
        # See if qline1 and qline2 are the same.  If so, extract the dates from rline1 and rline2
        # The line splits below are specific to the raw file line format.
        if (qline1.rstrip()!=qline2.rstrip()):
            eventdate=rline2.split(" ")[1] 
            eventdate=eventdate.split("T")[0]
            stateChangeString=eventdate+":"+qline1.rstrip()+"to"+qline2.rstrip()+";"+stateChangeString

        # Make the previous "next" lines the "first" lines for the next comparison
        qline1=qline2
        rline1=rline2

    #Status changes are now collected into groups of 20. The earliest (ie last listed) group will have the 
    # remainder.
    stateChangeList=stateChangeString.split(";")
    stateChangeFrag=""
    index=0
    partition=0
    while index<len(stateChangeList):
        stateChangeFrag+=stateChangeList[index]+";" # Need to reinsert the separating ";"
        index+=1
        partition+=1
        if(partition==20):
            # Write the fragment
            xmlFile.write("\t\t<status-changes>")
            xmlFile.write(stateChangeFrag[:-1])  # Remove the trailing ";" at the end  
            xmlFile.write("</status-changes>\n")
            
            # Reset the partition
            partition=0
            stateChangeFrag=""
    #Write out any remaining stations
    xmlFile.write("\t\t<status-changes>")
    xmlFile.write(stateChangeFrag[:-1])  # Remove the trailing ";" at the end  
    xmlFile.write("</status-changes>\n")

    #Print the change-count
    xmlFile.write("\t\t<change-count>"+str(len(stateChangeList)-1)+"</change-count>\n")

    # Clean up
    qFile.close
    rawFile.close

def setTimesNoData(stationDir,xmlFile):
    stationFiles=os.listdir(stationDir)
    rawFileName=""
    # Find the .all.raw file and open it
    rawFileExists=False
    for file in stationFiles:
        if(file.endswith(".all.raw")):
            rawFileName=file
            rawFileExists=True

    # Required file doesn't exist so bail out
    if(not rawFileExists): return
    rawFile=open(stationDir+"/"+rawFileName,"r")
    
    xmlFile.write("\t\t<time-nodata>")
    noDataString=""

    # We need to set a no-data range from beginDate (for the epoch, 1994-01-01) to the day before
    # our first data point for this station.
    firstDataDateParts=rawFile.readline().split(" ")[1].split("T")[0].split("-");
    #Convert this into a data object
    dayMinusOne=date(int(firstDataDateParts[0]),int(firstDataDateParts[1]),int(firstDataDateParts[2]))
    dayMinusOne-=timedelta(days=1)
    dayMinusOneString=dayMinusOne.isoformat()
    noDataString=dayMinusOneString+"to"+beginDate+";"  # Need the trailing ";" 

    #Reset the "raw" file to the beginning
    rawFile.seek(0)

    # Step through the file to find the starting and ending dates with no data.
    # By convention, this occurs when the line has a timestamp T22:22:22.  Also, by
    # convention, we will record the latest to earliest dates with no data.

    while True:
        nodata=False
        rline1=rawFile.readline()
        if not rline1: break

        # Get the date and timestamp, following format conventions
        fulleventdate1=rline1.split(" ")[1]
        eventdate1=fulleventdate1.split("T")[0]
        timestamp1=fulleventdate1.split("T")[1]

        # See if we have detected a no-data line
        if(timestamp1==NO_DATA_TIME_STAMP):
            nodata=True
            #Keep eventdate1 in case this is an isolated no-data line.
            eventdate_keep=eventdate1

            # We have a no-data line, so step ahead until the 
            # no-data line ends.
            while(nodata):
                rline2=rawFile.readline()
                if not rline2: break
                fulleventdate2=rline2.split(" ")[1]
                eventdate2=fulleventdate2.split("T")[0]
                timestamp2=fulleventdate2.split("T")[1]
                if(timestamp2!=NO_DATA_TIME_STAMP):
                    # Data exists for the second time stamp, so break out
                    # The last no-data line was the previous line
                    nodata=False
                    break
                else:
                    # No data for this line either, so keep this timestamp
                    # and start the while(nodata) loop again
                    eventdate_keep=eventdate2

            # We now know the range of no-data values, so insert this range, latest first
            noDataString=eventdate_keep+"to"+eventdate1+";"+noDataString
            
    # Finally, prepend the data-not-yet-available date range, from the last day of data
    # until today's date.
    today=date.today()
    formattedToday=today.isoformat() 
    
    #Reread the last event
    rawFile.seek(0)
    lastRawLine=rawFile.readlines()[-1]
    lastRawDate=lastRawLine.split(" ")[1].split("T")[0]
    lastDataDateParts=lastRawDate.split("-")  # This is the last date
    #Create a new date object out of the string we get from the file.
    lastDataDatePlus1=date(int(lastDataDateParts[0]),int(lastDataDateParts[1]),int(lastDataDateParts[2]))
    #Now increment this date one day.
    lastDataDatePlus1+=timedelta(days=1)    
    #Now convert to a string
    lastDataDataP1String=lastDataDatePlus1.isoformat()
    
    noDataString=formattedToday+"to"+lastDataDataP1String+";"+noDataString

    xmlFile.write(noDataString)
    # Clean up and close
    xmlFile.write("</time-nodata>\n")
    xmlFile.write("\t\t<nodata-count>"+str(len(noDataString.split(";"))-1)+"</nodata-count>\n")
    rawFile.close

#--------------------------------------------------
# Now run the script
#--------------------------------------------------
# Set the properties
sites_file_path = properties('download_path')+"/WNAMsites"
eval_dir_path = properties('eval_path')

#Assign the arrays
#stations_array=readStationsList(sites_file_path)
#datasets_array=readProjectDirsList(eval_dir_path)

# Loop through each data set 
for dataSet in os.listdir(eval_dir_path):
    projectDir=eval_dir_path+"/"+dataSet
    if(os.path.isdir(projectDir)):
        # Open the XML file that will contain the results
        outputPath="./junk-" + dataSet + ".xml"
        xmlFile=open(outputPath,"w");
        
        # Print XML front matter
        printFrontMatter(xmlFile)
        
        # Print out the header parts of the XML file
        printHeader(xmlFile,dataSet,updateTime,beginDate,endDate,centerLng,centerLat,serverName,stationCount)
    
        # Now loop over the station directories for each data set
        for stationList in os.listdir(projectDir):
            if (os.path.isdir(projectDir+"/"+stationList)):
                openStationXml(xmlFile)
                setStationId(stationList,xmlFile)
                setStationRefLatLonHgt(projectDir+"/"+stationList,xmlFile)
                setStatusChanges(projectDir+"/"+stationList,xmlFile)
                setTimesNoData(projectDir+"/"+stationList,xmlFile)
                closeStationXml(xmlFile)

        # Write the end tag
        writeXmlEndTag(xmlFile)
    
        # Close the XML file
        xmlFile.close()
    


