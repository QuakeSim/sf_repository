#!/bin/bash
# name:
# to call:plot_multisite [-w dest-workdir] [-q qfile] [-x xyzfile] [-s stationfile] [-l stationlist] [-h help] 

#PLOT_TEMPLATE_DIR=/home/zhigang/tmp/ss/plotXYZ_0.16/

STATIONFILE=""
QFILE=""
XYZFILE=""
STATIONLIST=""
WORKDIR=""

usage()
{
    echo "Usage:`basename $0` [-w dest-workdir] [-q qfile] [-x xyzfile] [-s stationfile] [-l stationlist] [-h help]"
    exit 1
}

if [ $# -eq 0 ]
then
    usage
fi

while getopts :q:s:x:hl:w: OPTION 
do
    case $OPTION in 
    s)  STATIONFILE=$OPTARG
        ;;
    q)  QFILE=$OPTARG
        ;;
    x)  XYZFILE=$OPTARG
        ;;
    l)  STATIONLIST=$OPTARG
        ;;
    w)  WORKDIR=$OPTARG
        ;;
    h)  echo echo "plot a multi column file to some picture files"
        echo "specified command option."
        echo "Where option is"
        echo "-x the specified xyz file"
        echo "-q the specified q file"
        echo "-w the dest work dir"
        echo "-s the file which stores station list"
        echo "-l the list of station name"
        echo "-h help"
        echo "Usage:`basename $0` [-w dest-workdir] [-q qfile] [-x xyzfile] [-s stationsfile] [-l stationlist] [-h help]"
        exit 0
        ;;
    \?) usage
        ;;
    esac
done

if [ -z $XYZFILE ]
then
    echo "error: $(basename $0) does not specify xyzfile"
    echo "try `basename $0` -h"
    exit 1
else
    if [ -f $XYZFILE ]
    then :
    else
        echo "`basename $0`:Error cannot find the xyz file $XYZFILE"
        exit 1
    fi
fi

if [ -z $QFILE ]
then
    echo "error: $(basename $0) does not specify qfile"
    echo "try `basename $0` -h"
    exit 1
else
    if [ -f $QFILE ]
    then :
    else
        echo "`basename $0`:Error cannot find the q file $QFILE"
        exit 1
    fi
fi

if [ -z $STATIONFILE ] && [ -z "`echo $STATIONLIST`" ]
then
    echo "error: $(basename $0) does not specify stationsfile or stationslist"
    echo "try `basename $0` -h"
    exit 1
else
    if [ -f $STATIONFILE ]
    then :
    else
        echo "`basename $0`:Error cannot find the stations file $STATIONFILE"
        exit 1
    fi
fi


echo "you chose the following options..I can process these"
echo "XYZFILE:$XYZFILE"
echo "QFILE:$QFILE"
echo "STATIONFILE:$STATIONFILE"
echo "STATIONLIST:$STATIONLIST"
echo "WORKDIR:$WORKDIR"


tmp_stations=""
if [ -n "`echo $STATIONLIST`" ]
then
    tmp_stations=$STATIONLIST
else
    tmp_stations=`cat ${STATIONFILE}| sed '1q' `
fi

stationsarray=($(echo $tmp_stations|tr ' ' ' '|tr -s ' '))
arraysize=${#stationsarray[*]}
echo "How many stations:"$arraysize

COUNTER=1
I=0
while [ $I -lt $arraysize ]
do

x=`expr $COUNTER`
COUNTER=`expr $COUNTER + 1`
y=`expr $COUNTER`
COUNTER=`expr $COUNTER + 1`
z=`expr $COUNTER`
COUNTER=`expr $COUNTER + 1`

cat $XYZFILE | awk '{print $'$x' ,$'$y' ,$'$z' }'  > ${WORKDIR}/${stationsarray[$I]}.xyz

#cat $XYZFILE | awk '{print 1}' > ${stationsarray[1]}.Q
cat $XYZFILE | awk '{print $3}' | sed -n '1p'| sed '/^$/d' > ${WORKDIR}/${stationsarray[$I]}.xyz.starttime
cat $XYZFILE | awk '{print $3}' | sed -n '$p'| sed '/^$/d' > ${WORKDIR}/${stationsarray[$I]}.xyz.endtime
plot_go.sh ${WORKDIR}/${stationsarray[$I]}.xyz $QFILE
I=`expr $I + 1`

done
