#!/bin/sh
#plot stateChangeNum.txt
#$1 input state change file name

usage()
{
  echo "Usage : 'basename $0' stateChange-file"
  echo "try 'basename $0' -help for more info"
}

if [ $# != 1 ]; then
  usage
fi

if [ $# == 1 ]; then
  start_time=`sed -n '1p' $1 | awk '{print $1}' | sed -n '1p'`
  end_time=`sed -n '$p' $1 | awk '{print $1}' | sed -n '1p'`
  y_max=`cat $1 | awk '{print $2}'|sort -g|sed -e '/^$/d' |sed -n '$p' `
  echo -E $start_time
  echo -E $end_time
  echo -E $y_max

  y_max=$y_max+10
  tmp1=`echo -E $1|sed 's/\//\\\\\//g'`
  sed -e 's/FILE/'$tmp1'/g;s/START_TIME/'$start_time'/g;s/END_TIME/'$end_time'/g;s/Y_MAX/'$y_max'/g' plot_stateNum.plt > $1.plt
  `which gnuplot` < $1.plt
#  /usr/local/bin/gnuplot < $1.plt
  rm $1.plt
fi
