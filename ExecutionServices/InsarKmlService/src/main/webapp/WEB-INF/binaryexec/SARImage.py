#!/usr/local/python2.6/bin/python

#============================================
# Generate line-of-sight interferograms  
#
# input: disclocOutput
# output: image 
#
# usage:
#   python SARImage.py
#   python SARIMage.py dislocOutput imageURL
#   python SARImage.py dislocOutput elevation(degree) azimuth(degree) radarFrequency(in GHz) imageURL
#
# output:
#   [dislocOutput].png
#   [dislocOutput].kml
#============================================

import csv, math, sys, os

import numpy as np
import matplotlib.cm as cm
import matplotlib.mlab as mlab
import matplotlib.pyplot as plt
from matplotlib.backends.backend_agg import FigureCanvasAgg

def dxy2lonlat(xy, ref):
    """
        convert dx, dy to lon, lat
        parameters: dx, dy, reflonlat
    """
    flattening = 1.0/298.247
    yfactor = 111.32

    lon1,lat1 = ref

    #xfactor = equard*Pi/180*Cos[lat1 Degree]*(1.0 - (flattening*Sin[lat1 Degree])^2)
    #eqrad = 6378.139
    #equard*Pi/180 = 111.32
    xfactor = 111.32*math.cos(math.radians(lat1))*(1.0 - flattening*(math.sin(math.radians(lat1))**2))
    lon2 = xy[0]/xfactor + lon1
    lat2 = xy[1]/yfactor + lat1
    return [lon2,lat2]

def generateKML(extent, outputname, imageurl):
    """
       generate KML  
    """
    kml = """<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://earth.google.com/kml/2.2">
  <Folder>
    <name>Ground Overlays</name>
    <description>Examples of ground overlays</description>
    <GroundOverlay>
      <Icon>
        <href>%s</href>
      </Icon>
      <LatLonBox>
        <north>%s</north>
        <south>%s</south>
        <east>%s</east>
        <west>%s</west>
      </LatLonBox>
    </GroundOverlay>
  </Folder>
</kml>
"""
    west = str(extent[0])
    east = str(extent[1])
    south = str(extent[2])
    north = str(extent[3])
    href = imageurl + "/" + outputname + ".png"

    #print kml % (north,south,east,west)
    kml = kml % (href, north,south,east,west)
    kmlf = open(outputname + ".kml",'w')
    kmlf.write(kml)
    kmlf.close()
    
def drawimage(datatable,lonlatgrid, outputname, imageurl):
    """
       produece image  
    """
    xy=[]
    data=[]
    for row in datatable:
        xy.append(row[0:2])
        data.append(row[2])

    xy0=min(xy)
    xy1=max(xy)

    z = np.array(data)
    z = z.reshape(lonlatgrid[0],lonlatgrid[1])

    fig = plt.figure()
    fig.subplots_adjust(left=0.0,bottom=0.0,top=1.0,right=1.0)

    im = plt.imshow(z,cmap=cm.jet,
                origin='lower', alpha=0.875,aspect="auto",interpolation=None, extent=[xy0[0],xy1[0],xy0[1],xy1[1]])

    plt.axis("off")
    plt.savefig(outputname + ".png", format="PNG",transparent=False)
    #print xy0, xy1
    generateKML([xy0[0],xy1[0],xy0[1],xy1[1]],outputname, imageurl)

    
    
    
def lineofsight (ele,azi,radarWL,disO,url):
    """
        caculate line of sight
        parameters:elevation,azimuth,radarWaveLength,disclocOutput
    """
    outputReader = csv.reader(open(disO), delimiter = ' ')
    rawdata=[]
    header = 0
    for row in outputReader:
        # remove ''
        line = [i for i in row if i !='']
        if not ('x' in line):
            rawdata.append(map(float,line))
        else:
            # locate the position of x y ux uy uz exx exy eyy
            header = len(rawdata)

    del outputReader
    
    # 30  30  32.237000  -115.083000
    gridsize = map(int,rawdata[0][:2])
    reflonlat = [rawdata[0][3], rawdata[0][2]]
    data = rawdata[header:]

    #print reflonlat
    #print len(data)
    #print gridsize

    # unite verctor
    # g = {Sin[Azimuth] Cos[Elevation], Cos[Azimuth] Cos[Elevation], Sin[Elevation]}
    azimuth, elevation = math.radians(azi),math.radians(ele)
    g = [math.sin(azimuth)*math.cos(elevation),math.cos(azimuth)*math.cos(elevation),math.sin(elevation)]
    #print g
    
    datatable = []
    for entry in data:
        # conver x,y to lon,lat
        lonlat = dxy2lonlat(entry[:2],reflonlat)
        ux,uy,uv = entry[2:5]
        # line of sight displacement
        losd = g[0]*ux + g[1]*uy + g[2]*uv
        # fringe
        # fringe = abs(math.modf(2*losd / radarWL)[0])
        fringe = 2*losd / radarWL - math.floor(2*losd / radarWL)
        datatable.append([lonlat[0],lonlat[1],fringe])

    # output for test
    #writer = csv.writer(open(disO+"_table", "wb"))
    #writer.writerows(datatable)
    #del writer

    outputname = os.path.basename(disO)
    
    drawimage(datatable,gridsize, outputname, url)

    
if __name__ == "__main__":


    numargv = len(sys.argv)

    if numargv == 1:
        ## ----- testing case ---##
        elevation = 60
        azimuth = 0
        radarFrequency = 1.26*10**9
        radarWaveLength = 299792458.0/radarFrequency * 100.0
        disclocOutput = "ShakeOutRuptureDetail2.output"
	imageURL = "file://" + os.getcwd()
        print "testing plot function with ShakeOutRuptureDetail2.output ..."
    elif numargv == 3:
        elevation = 60
        azimuth = 0
        radarFrequency = 1.26*10**9
        radarWaveLength = 299792458.0/radarFrequency * 100.0
        disclocOutput = sys.argv[1]
	imageURL = sys.argv[2]
    elif numargv == 6:
        disclocOutput = sys.argv[1]
        elevation = float(sys.argv[2])
        azimuth = float(sys.argv[3])
        radarFrequency = float(sys.argv[4])*10**9 
        radarWaveLength = 299792458.0/radarFrequency * 100.0
	imageURL = sys.argv[5]

    else:        
        sys.exit("not enough parameters!")

        
    lineofsight(elevation, azimuth,radarWaveLength,disclocOutput, imageURL)
