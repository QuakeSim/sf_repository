#!/bin/sh

cat $1 | awk '{print $4,$5,$6}'  > $1.xyz
cat $1 | awk '{print 1}' > $1.Q
cat $1 | awk '{print $3}' | sed -n '1p'| sed '/^$/d' > $1.xyz.starttime
cat $1 | awk '{print $3}' | sed -n '$p'| sed '/^$/d' > $1.xyz.endtime

#PLOT_TEMPLATE_DIR="/home/zhigang/tmp/ss/plotXYZ/"

./plot_go.sh $1.xyz $1.Q

