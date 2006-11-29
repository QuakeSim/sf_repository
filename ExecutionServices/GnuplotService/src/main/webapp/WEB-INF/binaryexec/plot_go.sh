#!/bin/sh
#plot input.xyz and input.Q
#$1 input XYZ file name
#$2 input Q file name

#PLOT_TEMPLATE_DIR="/home/zhigang/tmp/ss/plotXYZ/"

echo $PLOT_TEMPLATE_DIR

usage()
{
  echo "Usage : 'basename $0' XYZ-file Q-file"
  echo "try 'basename $0' -help for more info"
}

if [ $# != 2 ]; then
  usage
fi

if [ $# == 2 ]; then
  tmp1=`echo -E $1|sed 's/\//\\\\\//g'`
  tmp2=`echo -E $2|sed 's/\//\\\\\//g'`
  echo -E $tmp1
  echo -E $tmp2

  sed -e 's/INPUT.xyz/'$tmp1'/g;s/INPUT.Q/'$tmp2'/g' plot_go.plt > $1.plt
  sed -e 's/INPUT.xyz/'$tmp1'/g;s/INPUT.Q/'$tmp2'/g' plot_xyz.plt > $1plot.plt
  sed -e 's/INPUT.xyz/'$tmp1'/g;s/INPUT.Q/'$tmp2'/g' plot_func.plt > $1plot_func.plt
  gnuplot < $1.plt
  rm $1.plt
  rm $1plot.plt
  rm $1plot_func.plt
fi