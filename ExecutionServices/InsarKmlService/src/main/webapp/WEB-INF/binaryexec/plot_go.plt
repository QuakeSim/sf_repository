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
      system " sed = INPUT.xyz | sed 'N;s/\n/\t/' > INPUT.xyz.combine.txt "
      system " paste INPUT.Q INPUT.xyz.combine.txt >  INPUT.xyz.combine.tmp.txt"
      system " mv INPUT.xyz.combine.tmp.txt INPUT.xyz.combine.txt"
      system " sed '/^$/d' INPUT.Q | sort -u > INPUT.Q.class.txt "
      
      class_total =  ` wc -l INPUT.Q.class.txt | awk '{print $1}' `
      class_start = 0
      y_max =` cat INPUT.xyz | awk '{print $1}'|sort -n|sed -e '/^$/d'|sed -n '$p' `
      y_min =` cat INPUT.xyz | awk '{print $1}'|sort -n|sed -e '/^$/d'|sed -n '1p' `
      x_max =` wc -l INPUT.xyz | awk '{print $1}' `
      x_min =0

#########################################################
#  Draw for x                                           #
#                                                       #
#                                                       #
#########################################################
      print "plotting ... INPUT.xyz.X.png"
      call 'plotINPUT.xyz.plt' 'INPUT.xyz.combine.txt' 'INPUT.xyz.X.png' '3'

#########################################################
#  pre treatment shell command                          #
#                                                       #
#                                                       #
#########################################################
      system " sed '/^$/d' INPUT.Q | sort -u > INPUT.Q.class.txt "
      class_total =  ` wc -l INPUT.Q.class.txt | awk '{print $1}' `
      class_start = 0
      y_max =` cat INPUT.xyz | awk '{print $2}'|sort -n|sed -e '/^$/d'|sed -n '$p' `
      y_min =` cat INPUT.xyz | awk '{print $2}'|sort -n|sed -e '/^$/d'|sed -n '1p' `
#########################################################
#  Draw for y                                           #
#                                                       #
#                                                       #
#########################################################
      print "plotting ... INPUT.xyz.Y.png"
      call 'plotINPUT.xyz.plt' 'INPUT.xyz.combine.txt' 'INPUT.xyz.Y.png' '4'

#########################################################
#  pre treatment shell command                          #
#                                                       #
#                                                       #
#########################################################
      system " sed '/^$/d' INPUT.Q | sort -u > INPUT.Q.class.txt "
      class_total =  ` wc -l INPUT.Q.class.txt | awk '{print $1}' `
      class_start = 0
      y_max =` cat INPUT.xyz | awk '{print $3}'|sort -n|sed -e '/^$/d'|sed -n '$p' `
      y_min =` cat INPUT.xyz | awk '{print $3}'|sort -n|sed -e '/^$/d'|sed -n '1p' `
#########################################################
#  Draw for z                                           #
#                                                       #
#                                                       #
#########################################################
      print "plotting ... INPUT.xyz.Z.png"
      call 'plotINPUT.xyz.plt' 'INPUT.xyz.combine.txt' 'INPUT.xyz.Z.png' '5'
      

#########################################################
#  post treatment shell command                         #
#                                                       #
#                                                       #
#########################################################
      #system " rm INPUT.xyz.combine.txt "
      system " rm INPUT.Q.class.txt "
