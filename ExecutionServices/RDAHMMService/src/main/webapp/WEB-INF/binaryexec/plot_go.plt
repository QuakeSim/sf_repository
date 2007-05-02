########################################################
#      input XYZ file name = "INPUT.xyz"                #
#      input Q file name = "INPUT.Q"                    #
#                                                      #
########################################################

#########################################################
#  pre treatment shell command                          #
#                                                       #
#                                                       #
#########################################################
      system " sed = INPUT.xyz | sed 'N;s/\n/ /' > INPUT.xyz.combine.txt "
      system " paste INPUT.Q INPUT.xyz.combine.txt | sed '/^$/d' >  INPUT.xyz.combine.tmp.txt"
      system " mv INPUT.xyz.combine.tmp.txt INPUT.xyz.combine.txt"
      system " sed '/^$/d' INPUT.Q | sort -u > INPUT.Q.class.txt "
      
      class_total =  ` wc -l INPUT.Q.class.txt | awk '{print $1}' `
      class_start = 0
      y_max =` cat INPUT.xyz | awk '$1 != "0.0" {print $1}'|sort -n|sed -e '/^$/d' |sed -n '$p' `
      y_min =` cat INPUT.xyz | awk '$1 != "0.0" {print $1}'|sort -n|sed -e '/^$/d' |sed -n '1p' `
      y_base = y_min
      y_max = y_max-y_base + 0.02
      y_min = y_min-y_base - 0.02
      x_max =` wc -l INPUT.xyz | awk '{print $1}' `
      x_min =0

#########################################################
#  Draw for x                                           #
#                                                       #
#                                                       #
#########################################################
      print "plotting ... INPUT.xyz.X.png"
      call 'INPUT.xyzplot.plt' 'INPUT.xyz.combine.txt' 'INPUT.xyz.X.png' '3' " ` cat INPUT.xyz | awk '$1 != "0.0" {print $1}'|sort -n|sed -e '/^$/d' |sed -n '1p' ` "  "`cat INPUT.xyz.starttime`" "`cat INPUT.xyz.endtime`"

#########################################################
#  pre treatment shell command                          #
#                                                       #
#                                                       #
#########################################################
      system " sed '/^$/d' INPUT.Q | sort -u > INPUT.Q.class.txt "
      class_total =  ` wc -l INPUT.Q.class.txt | awk '{print $1}' `
      class_start = 0
      y_max =` cat INPUT.xyz | awk '$2 != "0.0" {print $2}'|sort -n|sed -e '/^$/d' |sed -n '$p' `
      y_min =` cat INPUT.xyz | awk '$2 != "0.0" {print $2}'|sort -n|sed -e '/^$/d' |sed -n '1p' `
      y_base = y_min
      y_max = y_max-y_base + 0.02
      y_min = y_min-y_base - 0.02

#########################################################
#  Draw for y                                           #
#                                                       #
#                                                       #
#########################################################
      print "plotting ... INPUT.xyz.Y.png"
      call 'INPUT.xyzplot.plt' 'INPUT.xyz.combine.txt' 'INPUT.xyz.Y.png' '4' " ` cat INPUT.xyz | awk '$2 != "0.0" {print $2}'|sort -n|sed -e '/^$/d' |sed -n '1p' ` " "`cat INPUT.xyz.starttime`" "`cat INPUT.xyz.endtime`"

#########################################################
#  pre treatment shell command                          #
#                                                       #
#                                                       #
#########################################################
      system " sed '/^$/d' INPUT.Q | sort -u > INPUT.Q.class.txt "
      class_total =  ` wc -l INPUT.Q.class.txt | awk '{print $1}' `
      class_start = 0
      y_max =` cat INPUT.xyz | awk '$3 != "0.0" {print $3}'|sort -n|sed -e '/^$/d' |sed -n '$p' `
      y_min =` cat INPUT.xyz | awk '$3 != "0.0" {print $3}'|sort -n|sed -e '/^$/d' |sed -n '1p' `
      y_base = y_min
      y_max = y_max-y_base + 0.02
      y_min = y_min-y_base - 0.02

#########################################################
#  Draw for z                                           #
#                                                       #
#                                                       #
#########################################################
      print "plotting ... INPUT.xyz.Z.png"
      call 'INPUT.xyzplot.plt' 'INPUT.xyz.combine.txt' 'INPUT.xyz.Z.png' '5' " ` cat INPUT.xyz | awk '$3 != "0.0" {print $3}'|sort -n|sed -e '/^$/d' |sed -n '1p' ` " "`cat INPUT.xyz.starttime`" "`cat INPUT.xyz.endtime`"
      

#########################################################
#  post treatment shell command                         #
#                                                       #
#                                                       #
#########################################################
      #system " rm INPUT.xyz.combine.txt "
      #system " rm INPUT.Q.class.txt "
