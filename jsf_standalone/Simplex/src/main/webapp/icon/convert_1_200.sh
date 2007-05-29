#####################################################
COUNTER=-50
while [ $COUNTER -lt 361 ]
do

COUNTER=`expr $COUNTER + 1`
#convert -size 20x18 -background  none -pointsize 18 -fill blue label:$COUNTER $COUNTER.png
convert -size 20x15 -background none -box dodgerblue -pointsize 18 -fill red label:$COUNTER $COUNTER.png

done
