
###################################################################
#                                                                 #
#     $0 : Combined XYZ file; format: <state> <date> <x> <y> <z>  #
#     $1 : OUTPUT png file                                        #
#     $2 : 3(Plot for X) or 4(Plot for Y) or 5(Plot for Z)        #
#     $3 : Base value for the vertical coordinate                 #
#     $4 : beginning date                                         #
#     $5 : ending date                                            #
###################################################################

      set terminal png
      set output "$1"
      set grid
      set xdata time
      set timefmt "%Y-%m-%d"
      set format y "%+-5.8f"
      if ($2==3) set ylabel "Longitude(degree)"
      if ($2==4) set ylabel "Latitude(degree)"
      if ($2==5) set ylabel "Elevation(m)"
      set xlabel "Time(days [$4 $5])"
      show label
      set size 1.7,0.8
      set rmargin 8
      set pointsize 0.2
      #set key left
      set nokey
      set yrange [y_min:y_max]
      #set xrange [x_min:x_max]
      set xrange ["$4":"$5"]
      set format x "%Y-%m-%d"
      set multiplot
      # linetype 49 and 64 are light gray
      if ($2==3) plot "$0" using 2:($$3 - y_base) with lines linetype 64;
      if ($2==4) plot "$0" using 2:($$4 - y_base) with lines linetype 64;
      if ($2==5) plot "$0" using 2:($$5 - y_base) with lines linetype 64;      
      call 'INPUT.xyzplot_func.plt' '$0' '$2'
      set key
      plot 0 title "Base value is:$3" with lines linetype 30;
      set nomultiplot
      reset

