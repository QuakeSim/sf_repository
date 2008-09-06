#!/bin/sh
#plot stateChangeNum.txt
#$1 mencoder path
#$2 input file 1 path
#$3 input file 2 path
#$4 output file path

usage()
{
  echo "Usage : 'basename $0' <mencoder path> <input file 1 path> <input file 2 path> <output file path>"
  echo "try 'basename $0' -help for more info"
}

if [ $# != 4 ]; then
  usage
fi

if [ $# == 4 ]; then
	MENCODER=$1
	INPUT1=$2
	INPUT2=$3
	OUTPUT=$4
	
	$MENCODER -ovc lavc -oac lavc $INPUT1 $INPUT2 -o $OUTPUT
fi
