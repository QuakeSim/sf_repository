#!/usr/local/python2.6/bin/python
"""
SARImage_server generated line-of-sight disloc interferograms 

Usage:
SARImage_server [Options] [dislocfile] 

    Options:
        -h display help on options  
        -e elevation azimuth in degree (default: 60, -5)
        -r radar_frequency in GHZ (default: 1.29)
        -u URL_path_to_output_image (default "")
        -n average_GPS_altitude average_terrain_height peg_heading in meter, meter, degree
        -m UAVSAR RPI metadata file
    
    notes:
        -m will always surpass -n

Outputs:
    [dislocfile].png
    [dislocfile].kml
    
"""

#===============================================================================
# History:
#   2010/09/07: fix nan problem
#   2010/09/20: fix white stripes
#   2010/09/25: add elevation angle function
#   2011/01/25: load elevation angle parameters from UAVSAR RPI metadata file
#   2011/01/25: rewrite the parameter-inputs as options
#   2011/02/15: keep the parameters in kml
#===============================================================================

import csv
import sys
import os
import math
import string
from optparse import OptionParser

try:
    import numpy as np
    #import matplotlib.cm as cm
    #import matplotlib.mlab as mlab
    import matplotlib.pyplot as plt
    #from matplotlib.backends.backend_agg import FigureCanvasAgg
except ImportError:
    sys.exit("Import matplotlib failed ")



def dxy2lonlat(xy, ref):
    """convert dx, dy to lon, lat
        parameters: dx, dy, reflonlat
    """

    flattening = 1.0/298.247
    yfactor = 111.32

    lon1,lat1 = ref

    xfactor = 111.32*math.cos(math.radians(lat1))*(1.0 - flattening*(math.sin(math.radians(lat1))**2))
    lon2 = xy[0]/xfactor + lon1
    lat2 = xy[1]/yfactor + lat1

    return [lon2,lat2]

def generateKML(extent, params):
    """generate KML"""
    
    kml = """<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://earth.google.com/kml/2.2">
  <Folder>
    <name>Disloc interferograms</name>
    <description>%s</description>
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
    # get disloc file name
    disloc = params['disloc']
    outputname = os.path.basename(disloc)
    imageurl = params['url']
    href = imageurl + "/" + outputname + ".png"
    if imageurl == "":
        href = outputname + ".png"

    description = "<![CDATA[%s]]>"
    desc = string.join(["Disloc :", str(params['disloc']), '<br>',
                       "Elevation (degree): ", str(params['elevation']), '<br>',
                       "Azimuth (degree): ", str(params['azimuth']), '<br>',
                       "Radar Wavelength (cm): ", "%.3f" % params['radar'],'<br>'
                       ])
    
    # add flight parameters 
    if 'flight' in params:
        aga, ath, ph = params['flight']
        fs = string.join(['Average GPS Altitude (meter): ', "%.3f" % aga,'<br>',
                          'Average Terrain Height (meter): ', "%.3f" % ath,'<br>',
                          'Peg Heading: ', "%.3f" % ph])
        desc = desc + fs
        
    description = description % (desc)
    kml = kml % (description, href, north,south,east,west)
    kmlf = open(outputname + ".kml",'w')
    kmlf.write(kml)
    kmlf.close()


def color_wheel(fcw):
    """ make goldstein color wheel """

    #fcw=1 flips order of colors
    wheel = [[None]*16 for i in range(3)]
    for i in range(16):
        if i==0:
            ib=255
            ir=ib
            ig=ib
        elif i>=1 and i<=5:
            ib = 255
            ir = (i-1)*51
            ig = 255 - 51*(i-1)
        elif i>=6 and i<=10:
            ir = 255
            ig = (i-6)*51
            ib = 255 - (i-6)*51
        elif i>=11 and i<=15:
            ig = 255
            ib = (i-11)*51
            ir = 255 -(i-11)*51
        else:
            print "something wrong!"
        wheel[0][i]=math.floor(ir/255.0*225)+30
        wheel[1][i]=math.floor(ig/255.0*225)+30
        wheel[2][i]=math.floor(ib/255.0*225)+30

    if fcw == 1:
        #flip the color wheel
        for i in range(3):
            junk = wheel[i][1:]
            junk.reverse()
            wheel[i][1:]=junk
            
    colormatrx = []
    for j in range(256):
        mag = math.floor(j/16)
        i = j % 16
        red = math.floor(mag/16.0*wheel[0][i]*16/15)
        green = math.floor(mag/16.0*wheel[1][i]*16/15)
        blue = math.floor(mag/16.0*wheel[2][i]*16/15)
        colormatrx.append([red/255.0,green/255.0,blue/255.0])

    return colormatrx
    
def drawimage(datatable,lonlatgrid,  params):
    """
       produece image  
    """
    # get disloc file name
    disloc = params['disloc']
    outputname = os.path.basename(disloc)

    
    xy=[]
    data=[]
    for row in datatable:
        xy.append(row[0:2])
        data.append(row[2])

    xy0=min(xy)
    xy1=max(xy)

    # new color wheel
    z = np.array(data)
    minz, maxz = min(z), max(z)
    #scale it to [1~15]
    z = np.fix((z-minz)/(maxz-minz)*14+ 0.001) + 1
    z = z.astype("int")
    #handle nan number nan is colored by colm[0]
    nan = np.where(z<0)
    z[nan]=0
    colormatrx = color_wheel(0)
    # the brightness of colorwheel
    p=15
    colm = np.array(colormatrx[(p - 1)*16:p*16])
    newimg = colm[z]
    newimg = newimg.reshape(lonlatgrid[1],lonlatgrid[0],3)
    # figsize
    fig = plt.figure(figsize=(lonlatgrid[0]/12.0,lonlatgrid[1]/12.0))
    fig.subplots_adjust(left=0.0,bottom=0.0,top=1.0,right=1.0)
    plt.imshow(newimg,interpolation='spline16',origin='lower')
    plt.axis("off")
    plt.savefig(outputname + ".png", format="PNG",aspect="auto",transparent=True,dpi=(96))

    generateKML([xy0[0],xy1[0],xy0[1],xy1[1]],params)
    
def setpar(s,line,valtype):
    """
    from Jay's coder
    """
    
    if line[0] != ';' and s in line[:40] and '=' in line:
        line = line[line.index('=')+1:]
        if valtype == 'float':
            value = float(line[:16])
        if valtype == 'int':
            value = int(line[:16])
        if valtype == 'string':
            value = line[:16].strip() # strip removes leading, trailing spaces
        return value

def assign_metadata_values(fname):
    """
       Function assign_metadata_values:
          fname: filename to open and use to extract values
       from Jay's code
    """
    aga, ath, ph = None, None, None
    af = open(fname,'r')
    for line in af.readlines():
        if aga == None: aga = setpar('Average GPS Altitude',line,'float')
        if ath == None: ath = setpar('Average Terrain Height',line,'float')
        if ph == None: ph = setpar('Peg Heading',line,'float')
    
    af.close()
    del af

    return ([aga,ath,ph])

def lineofsight (params):
    """caculate line of sight
    """
    
    # get disloc file name
    disloc = params['disloc']
    
    # load data
    outputReader = csv.reader(open(disloc), delimiter = ' ')
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
    
    # grid paramters
    gridsize = map(int,rawdata[0][:2])
    reflonlat = [rawdata[0][3], rawdata[0][2]]
    data = rawdata[header:]

    # decide methods
    # methods 1: unite vector    
    # methods 2: with UAVSAR flight information
    # load flight information from meta file
    
    unitflag = True  # method 2
    if "RPIfile" in params:
        flight = assign_metadata_values(params['RPIfile'])
        params['flight'] = flight
    elif "flight" in params:
        flight = params['flight']
    else:
        unitflag = False
            
    
    # unite verctor
    # g = {Sin[Azimuth] Cos[Elevation], Cos[Azimuth] Cos[Elevation], Sin[Elevation]}
    azimuth, elevation = math.radians(params['azimuth']),math.radians(params['elevation'])
    # method I: without UAVSAR flight information
    g = [math.sin(azimuth)*math.cos(elevation),math.cos(azimuth)*math.cos(elevation),math.sin(elevation)]
    
    radarWL = params['radar']
    
    # methods II: with UAVSAR flight information
    # elevation for each pixel is different, move g into loop
    if unitflag:        
        # caculate elevation for each pixel
        # code source: def elmap(xpt,ypt,af_val): in sar2simplex.py     
        M_PER_KM = 1000.
        aga, ath, ph = flight
        
        # So the average height of the craft above ground is:
        delta_height_in_m = aga - ath
        delta_height_in_km = delta_height_in_m/M_PER_KM
        # Heading unit vector, as x, y (East, North) components:
        #hvec = (geofunc.sino(ph),geofunc.coso(ph))
        ph = math.radians(ph)    
        hvec = (math.sin(ph),math.cos(ph))    
    # end of method II 

    datatable = []
    for entry in data:
        # conver x,y to lon,lat
        lonlat = dxy2lonlat(entry[:2],reflonlat)
        ux,uy,uv = entry[2:5]

        # methods II
        if unitflag:
            # Craft peg point is above (0,0); 
            # horizontal part of craft to pixel vector is (xpt,ypt)
            # and so projection is x_ = x_ - p dot h h_
            # (pp is horix. distance from pegged heading line to pixel)
            xpt, ypt = entry[:2]        
            pdoth = xpt*hvec[0]+ypt*hvec[1]
            pp = (xpt-pdoth*hvec[0],ypt-pdoth*hvec[1])
            magpp = math.sqrt(pp[0]*pp[0]+pp[1]*pp[1])
            # elevation angle from right triangle: ht is delta ht,
            # base is mag pp
            elevation = math.atan2(delta_height_in_km, magpp)
            g = [math.sin(azimuth)*math.cos(elevation),math.cos(azimuth)*math.cos(elevation),math.sin(elevation)]
        # end of method II #    
         

        # line of sight displacement
        losd = g[0]*ux + g[1]*uy + g[2]*uv
        # fringe
        # fringe = abs(math.modf(2*losd / radarWL)[0])
        fringe = 2*losd / radarWL - math.floor(2*losd / radarWL)
        datatable.append([lonlat[0],lonlat[1],fringe])
    
    drawimage(datatable,gridsize,params)

def usage():
    sys.exit(__doc__)
    
def main():

    # build option parser
    parser = OptionParser("usage: %prog [options] dislocfile")
    # -e: elevation azimuth in degree
    parser.add_option("-e", nargs=2, type="float", dest="eleazi", help="elevation(degree) and azimuth(degree)", default=(60, -5)) 
    # -r: radar frequency in GHZ
    parser.add_option("-r", nargs=1, type="float", dest="radar", help="radar frequency in GHz", default=1.29)
    # -u: url prefix to image
    parser.add_option("-u", nargs=1, type="string", dest="url", help="URL path to output image", default="")
    # -n altitude height peg
    parser.add_option("-n", nargs=3, type="float", dest="flight", help="altitude(meter) height(meter) peg(degree)")
    # -m UAVSAR RPI metadata file
    parser.add_option("-m", nargs=1, type="string", dest="RPIfile", help="UAVSAR metadata file")   
    
    (options, args) = parser.parse_args()
    
    if not (len(args) == 1):
        parser.error("incorrect number of arguments")
        usage()
    
    
    elevation, azimuth = options.eleazi
    radarFrequency = options.radar * 10**9
    radarWaveLength = 299792458.0/radarFrequency * 100.0
    disclocOutput = args[0]
    imageURL = options.url

    params = {'disloc': disclocOutput,'elevation':elevation, 'azimuth':azimuth, 
              'radar':radarWaveLength,'url':imageURL}
        
    if options.flight: 
        params['flight'] = options.flight
        
    if options.RPIfile:
        RPIfile = options.RPIfile
        params['RPIfile'] = RPIfile
    
    # call image generator 
    lineofsight(params)    

if __name__ == "__main__":
    sys.exit(main())
