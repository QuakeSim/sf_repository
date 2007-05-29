#####################################################
COUNTER=0
while [ $COUNTER -lt 361 ]
do

COUNTER=`expr $COUNTER + 1`
convert -background none -rotate $COUNTER base_red.png base_red$COUNTER.png

done
