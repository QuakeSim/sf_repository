
########################################################
#                                                      #
#     $0 :XYZ file                                     #
#     $1 :OUTPUT png file                              #
#     $2 :X or Y or Z                                  #
########################################################

      set terminal png
      set output "$1"
      set grid
      set ylabel "Coordinate(m)"
      set xlabel "Time(days)"
      show label
      set size 1.7,0.65
      #set key left
      set nokey
      set yrange [y_min:y_max]
      set xrange [x_min:x_max]
      set multiplot
      call 'plotINPUT.xyz_func.plt' '$0' '$2'
      set nomultiplot
      reset

