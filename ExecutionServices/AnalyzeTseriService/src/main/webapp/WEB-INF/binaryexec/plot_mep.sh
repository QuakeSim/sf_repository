gnuplot <<EOF
set terminal png 
set autoscale
unset title
unset label
set output "$OUTPUT_X" 
plot "$INPUTFILE" using 3 title 'East' with linespoints
set output "$OUTPUT_Y" 
plot "$INPUTFILE" using 4  title 'North' with linespoints
set output "$OUTPUT_Z" 
plot "$INPUTFILE" using 5  title 'Up' with linespoints
EOF
