#!/bin/csh 
set area = -11.625/8.725/-13.68/89.28 #W/E/S/N
set scale = 0.174242424242424

# set up cartesian basemap
psbasemap -R$area -Jx$scale -B10f10:./home/gateway/Simplex_WDIR/mpierce/TestCase/TestCase.output: -K -P > TestCase1126976809321.ps

# plot causative fault(s) in red
#BUT In cartesian plots the strike is not right anywhere except at the equator
#so dont plot faults by default since they are likely to be misleading
#psxy Simplex_fault.gmt -R$area -Jx$scale -K -O -P -W2/255/0/0 >> TestCase1126976809321.ps

# plot velocities
# # psvelo Simplex_calc_vectors.gmt -R$area -Jx$scale -H2 -O -P -Se1.00000001e-08/0.95/8 >> TestCase1126976809321.ps
# psvelo Simplex_resid_vectors.gmt -R$area -Jx$scale -H2 -O -P -Se1.00000001e-08/0.95/8 >> TestCase1126976809321.ps

# Convert postscript to PDF
/usr/bin/ps2pdf TestCase1126976809321.ps TestCase1126976809321.pdf
