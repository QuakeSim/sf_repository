set terminal png
set output "FILE.png"
set grid
set xdata time
set timefmt "%Y-%m-%d"
set ylabel "Number of Stations with state Changes"
set xlabel "Time(days [START_TIME END_TIME])"
show label
set size 1.25,1.2
set rmargin 8
#set key left
set nokey
set yrange [0:446]
set xrange ["START_TIME":"END_TIME"]
set format x "%Y-%m-%d"
set multiplot
set style fill solid 1.0
plot "FILE" using 1:2 with boxes linetype 3
set key
set nomultiplot
reset
