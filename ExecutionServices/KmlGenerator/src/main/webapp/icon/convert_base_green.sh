#####################################################
COUNTER=-1
while [ $COUNTER -lt 361 ]
do

COUNTER=`expr $COUNTER + 1`

    convert  -size 32x32 xc:none -gravity Center \
             -pointsize 10 \
             -fill none -stroke green -strokewidth 2 -draw "affine 1,0,0,-1,16,16 rotate -$COUNTER line 0,0 0,16" \
             -fill green -draw 'circle 16,16,22,16 ' \
             -fill blue -stroke none -draw "rotate 0 text 0,0 '$COUNTER'" \
             base_green$COUNTER.png

done
