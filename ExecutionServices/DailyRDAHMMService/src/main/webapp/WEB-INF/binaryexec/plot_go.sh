#!/bin/sh
#plot input.xyz and input.Q
#$1 input XYZ file name
#$2 input Q file name
#$3 input raw file name that containing the dates

usage()
{
  echo "Usage : 'basename $0' XYZ-file Q-file raw-file"
  echo "try 'basename $0' -help for more info"
}

if [ $# != 3 ]; then
  usage
fi

if [ $# == 3 ]; then
  tmp1=`echo -E $1|sed 's/\//\\\\\//g'`
  tmp2=`echo -E $2|sed 's/\//\\\\\//g'`
  tmp3=`echo -E $3|sed 's/\//\\\\\//g'`
  echo -E $tmp1
  echo -E $tmp2
  echo -E $tmp3

  sed -e 's/INPUT.xyz/'$tmp1'/g;s/INPUT.Q/'$tmp2'/g;s/INPUT.raw/'$tmp3'/g' plot_go.plt > $1.plt
  sed -e 's/INPUT.xyz/'$tmp1'/g;s/INPUT.Q/'$tmp2'/g;s/INPUT.raw/'$tmp3'/g' plot_xyz.plt > $1plot.plt
  sed -e 's/INPUT.xyz/'$tmp1'/g;s/INPUT.Q/'$tmp2'/g;s/INPUT.raw/'$tmp3'/g' plot_func.plt > $1plot_func.plt
  `which gnuplot` < $1.plt
#  /usr/local/bin/gnuplot < $1.plt
  rm $1.plt
  rm $1plot.plt
  rm $1plot_func.plt
fi
