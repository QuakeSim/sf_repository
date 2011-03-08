#!/bin/bash

#============================================
# Convert DailyRDAHMM input file coordinates from xyz to lon-lat-vert
# using PROJ.4 http://trac.osgeo.org/proj/
#
# input: daily_rdahmm .input file
# output: coodinates converted .input.llh file
#
# usage:
#   llhtrans.sh daily_project_station.input
#
# output:
#   [daily_project_station].input.llh
#============================================

input=$1
output=$input.llh
CS2CSBIN=./cs2cs

$CS2CSBIN -f '%.10f' +proj=geocent +datum=WGS84 +to +proj=latlong +datum=WGS84 $input | sed "s/\t/ /g" > $output
echo $output
