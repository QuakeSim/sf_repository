#!/usr/local/python2.6/bin/python 
 
#============================================ 
# Generate line-of-sight interferograms   
# 
# input: disclocOutput 
# output: image  
# 
# usage: 
#   python SARImage.py (testing with default data set and parameters) 
#   python SARIMage.py dislocOutput imageURL 
#   python SARImage.py dislocOutput elevation(degree) azimuth(degree) radarFrequency(in GHz) imageURL 
# 
# output: 
#   [dislocOutput].png 
#   [dislocOutput].kml 
#============================================ 
 
#===================================================== 
# History: 
#   2010/09/07: fix nan problem 
#   2010/09/20: fix white stripes
#   2011/05/09: add QuakeSim logo
#   2011/05/09: record parameters in kml files
#   2011/05/10: temporary fix for QuakeSim logo on small image 
#===================================================== 
 
import csv, math, sys, os, math, string 
 
try:
    import numpy as np 
    import matplotlib.cm as cm 
    import matplotlib.mlab as mlab 
    import matplotlib.pyplot as plt 
    #from matplotlib.backends.backend_agg import FigureCanvasAgg 
    import matplotlib.image as mpimg
except ImportError:
    sys.exit("Import matplotlib failed ")


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
 
def generateKML(extent, outputname, imageurl, params): 
    """ 
       generate KML   
    """ 
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
    href = imageurl + "/" + outputname + ".png" 
    if imageurl == "": 
        href = outputname + ".png" 

    description = "<![CDATA[%s]]>"
    desc = string.join(["Disloc :", str(params[0]), '<br>',
                       "Elevation (degree): ", str(params[1]), '<br>',
                       "Azimuth (degree): ", str(params[2]), '<br>',
                       "Radar Wavelength (cm): ", "%.3f" % params[3],'<br>'
                       ])
 
    #print kml % (north,south,east,west) 
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
     
def drawimage(datatable,lonlatgrid, outputname, imageurl, params): 
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
 
##    z = np.array(data) 
##    z = z.reshape(lonlatgrid[1],lonlatgrid[0]) 
## 
##    fig = plt.figure() 
##    fig.subplots_adjust(left=0.0,bottom=0.0,top=1.0,right=1.0) 
## 
##    im = plt.imshow(z,cmap=cm.jet, 
##                origin='lower', alpha=0.875,aspect="auto",interpolation=None, extent=[xy0[0],xy1[0],xy0[1],xy1[1]]) 
## 
##    plt.axis("off") 
##    plt.savefig(outputname + ".png", format="PNG",transparent=True) 
##    #print xy0, xy1 
##    generateKML([xy0[0],xy1[0],xy0[1],xy1[1]],outputname, imageurl) 
 
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
    p=14 
    colm = np.array(colormatrx[(p - 1)*16:p*16]) 
    newimg = colm[z] 
    newimg = newimg.reshape(lonlatgrid[1],lonlatgrid[0],3) 
    # figsize 
    fig = plt.figure(figsize=(lonlatgrid[0]/12.0,lonlatgrid[1]/12.0)) 
    fig.subplots_adjust(left=0.0,bottom=0.0,top=1.0,right=1.0) 
    im = plt.imshow(newimg,interpolation='spline16',origin='lower') 
    plt.axis("off")

    # add QuakeSim logo
    #if (lonlatgrid[1] < 100) or (lonlatgrid[0] < 100):
    #    logo = mpimg.imread('QuakeSimLogoGrayEmbossSmall.png')
    #    fig.figimage(logo, xo=fig.bbox.xmax, yo=2,zorder=1)
    #else:
    #    logo = mpimg.imread('QuakeSimLogoGrayEmboss.png')
    #    fig.figimage(logo, xo=fig.bbox.xmax, yo=2,zorder=1)
    
    
    plt.savefig(outputname + ".png", format="PNG",aspect="auto",transparent=True,dpi=(96)) 

    generateKML([xy0[0],xy1[0],xy0[1],xy1[1]],outputname, imageurl, params) 
     
def lineofsight (ele,azi,radarWL,disO,url): 
    """ 
        caculate line of sight 
        parameters:elevation,azimuth,radarWaveLength,disclocOutput 
    """ 
    params = [disO, ele, azi,radarWL] 

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

    drawimage(datatable,gridsize, outputname, url, params) 
 
     
if __name__ == "__main__": 
 
 
    numargv = len(sys.argv) 
 
    if numargv == 1: 
        ## ----- testing case ---## 
        elevation = 60 
        azimuth = -5 
        radarFrequency = 1.26*10**9 
        radarWaveLength = 299792458.0/radarFrequency * 100.0 
        disclocOutput = "M61.output" 
        imageURL = "file://" + os.getcwd() 
        imageURL = "" 
        print "testing plot function with " + disclocOutput 
    elif numargv == 3: 
        elevation = 60 
        azimuth = -5 
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
 
