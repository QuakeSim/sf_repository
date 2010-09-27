
#==============================================
# This program read ROI_PAC image (*.unw)
#   and convert it to a png image
#
# 
#
#
#==============================================

import sys, os, string, getopt, math
import numpy as np
import matplotlib.pyplot as plt
import pylab


options = {
'-i':'input',
'-o':'output',
'-a': 'Amplitude only',
'-p': 'Phase only',
'-s': 'Show',
'-g': 'Geocoded',
'-d': 'Image data only',
'-h': 'help',
'-v':'Silent'}

V = {
'opts': {'-s':''},
'args': [],
'infile': '',
'outfile': '',
'width': 0,
'length': 0, 
'easting': 0.,
'northing':0., 
'stepx': 1.,
'stepy': 1.,
'file_type': '', 
'geocoded': False,
'rdata': np.array([]), 
'rlayer': '',
'idata': np.array([]),
'ilayer': '',
'rdata_vmin': 0.,
'rdata_vmax': 0.,
'idata_vmin': 0.,
'idata_vmax': 0.,
}

def readrsc():
    """ read infile.rsc """
    try:
        rsc = dict(np.loadtxt(V['infile']+".rsc", dtype=str))
    except:
        sys.exit("Can't read hear file: " + V['infile']+".rsc")
    try:
        V['width'] = int(rsc['WIDTH'])
        V['length'] = int(rsc['FILE_LENGTH'])
        V['easting'] = 0.0
        V['northing'] = 0.0
        V['stepx'] = 1.
        V['stepy'] = 1.  
    except KeyError:
        sys.exit('Problem reading header file'+V['infile']+'.rsc')
    try:
        V['easting'] = float(rsc['X_FIRST'])
        V['northing'] = float(rsc['Y_FIRST'])
        V['stepx'] = float(rsc['X_STEP'])
        V['stepy'] = float(rsc['Y_STEP'])
    except KeyError:
        V['geocoded']  = False
    else:
        V['geocoded']  = True
    print "File size: " + str(V['width'] )+"x"+str(V['length'])

def readdata():
    """ load infile """

    try:
      data = np.fromfile(V['infile'] ,dtype="float32", count=V['length'] *2*V['width']).reshape(V['length'] *2,V['width'] )
    except IOError:
      sys.exit("Error while trying to open the file: "+V['infile'] )
    oddindices = np.where(np.arange(V['length']*2)&1)[0]
    # magnitude
    V['rdata']  = data.take(oddindices-1,axis=0)
    # phase
    V['idata']  = data.take(oddindices,axis=0)

def lower8bit(a):
    """ return lower 8 bits of a """

    #test code
    #a= np.array([range(-10,10,1)]*3)
    #b=lower8bit(a)
    #print a
    #print b

    b = np.fix(a % 256)
    bc = np.copy(b)
    meng = np.where(bc<0)
    mn,nn = len(meng[0]),len(meng[1])
    if mn > 0:
        bc[mneg] = 256*np.ones(mn,nn)+bc[mneg]
        b = np.reshape(bc,b.shape)
    return b

def da2imgw(phase,amp,wrap,ex):
    """ combine the displacements and amplitude """

    phc = np.copy(phase)
    m0 = (phc == 0).nonzero()
    phase = lower8bit(np.fix(phase/wrap*255))
    phase = lower8bit(phase/255*14.99999) + 1
    phc = np.copy(phase)

    phc[m0]= 0
    phase = np.reshape(phc,phase.shape)
    phase = phase +1

    amp = amp ** ex
    stdv = np.std(np.std(amp,axis = 0 ), axis = 0)
    mn = np.mean(np.mean(amp,axis = 0 ), axis = 0)
    if mn ==0 and stdv ==0:
        mn = 0.5
        stdv = 0.5
    amp1 = amp /(mn+stdv)*240
    amp1c = np.copy(amp1)

    mbig = (amp1c > 255).nonzero()
    amp1c[mbig]=255
    amp2 = np.reshape(amp1c,amp.shape)
    amp = lower8bit(amp2)
    amp = lower8bit(amp /16.0)
    amp = lower8bit(amp * 16)
    # -1 shift t 0~255 for the color wheel
    image = amp + phase - 1
    image = image.astype("int")
    return image

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
    

def gen_image(image):
    """ generate image"""

    # normal color wheel
    colm = np.array(color_wheel(0))
    imgc = np.reshape(image,V['width']*V['length'])
    newimg = colm[imgc]
    print newimg.shape,image.shape,V['width']*V['length']
    newimg = np.reshape(newimg,(V['length'],V['width'],3))

    # figsize
    fig = plt.figure(figsize=(V['width']/96.0,V['length']/96.0))
    fig.subplots_adjust(left=0.0,bottom=0.0,top=1.0,right=1.0)
    im = plt.imshow(newimg)
    plt.axis("off")
    plt.savefig(V['outfile'],dpi=(96))
           
def main():

    try:
        V['infile']  = V['opts']['-i']
    except KeyError:
        sys.exit("Input file is required!")
        
    if V['infile']:
        if not os.path.exists(V['infile'] ):
            sys.exit("Error: Can't fined input file!")
        if not os.path.exists(V['infile'] +'.rsc'):
            sys.exit("Error: Can't fined input header file (.rsc)!")
    try:
        V['outfile']  = V['opts']['-o']
    except KeyError:
        V['outfile'] = V['infile'] + ".png"

    readrsc()
    readdata()
    image = da2imgw(V['idata'],V['rdata'],6.28,0.6)
    gen_image(image)
    
if __name__=="__main__":
    # get options and arguments
    
    try:
        V['opts'] ,V['args']  = getopt.getopt(sys.argv[1:],'i:o:apdsghv')
        V['opts'] = dict(V['opts'] )
    except getopt.error, err:
        print str(err)
        sys.exit()
    main()
    print "Done"
    sys.exit()

