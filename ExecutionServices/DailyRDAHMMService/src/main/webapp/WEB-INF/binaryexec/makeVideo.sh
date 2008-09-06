#!/bin/sh
#plot stateChangeNum.txt
#$1 mencoder path
#$2 list file path
#$3 output file path

usage()
{
  echo "Usage : 'basename $0' <mencoder path> <picture list file path> <output file path>"
  echo "try 'basename $0' -help for more info"
}

if [ $# != 3 ]; then
  usage
fi

if [ $# == 3 ]; then
	MENCODER=$1
	LIST=$2
	OUTPUT=$3
	
	$MENCODER mf://@$LIST -mf w=800:h=600:fps=10:type=jpg -ovc lavc -lavcopts vcodec=mpeg2video:mbd=2:trell -oac copy -o $OUTPUT
fi
