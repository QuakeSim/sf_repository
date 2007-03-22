#!/bin/sh
#plot input.xyz and input.Q
#$1 input XYZ file name
#$2 input Q file name

usage()
{
  echo "Usage : 'basename $0' XYZ-file Q-file base-directory"
  echo "try 'basename $0' -help for more info"
}

if [ $# != 2 ]; then
  usage
fi

if [ $# == 2 ]; then
  sed 's/INPUT.xyz/'$1'/g;s/INPUT.Q/'$2'/g' plot_go.plt > $1.plt
  sed 's/INPUT.xyz/'$1'/g;s/INPUT.Q/'$2'/g' plot_xyz.plt > plot$1.plt
  sed 's/INPUT.xyz/'$1'/g;s/INPUT.Q/'$2'/g' plot_func.plt > plot$1_func.plt

  `which gnuplot` < $1.plt
  rm *$1*.plt
fi
