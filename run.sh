#!/bin/bash
name=`echo $*|sed 's/ /_/g'`
fname=$(mktemp ${name}.csv )
./integrator.py $* 0.001 30 $fname
#gnuplot -e "set datafile separator \",\";set terminal postscript;set output 'plot_$name.ps';plot '$fname' u 2:3 title 'time vs theta' pt 2 pointsize 0.1, '$fname' u 2:4 title 'time vs omega' pt 4 pointsize 0.5, '$fname' u 3:4 title 'theta vs omega' pt 7 pointsize 0.5;"
#convert -density 150x150 -page a4 "plot_$name.ps" +repage +rotate 90 "`echo "plot_$name.ps" | sed -e 's/\.ps$/\.png/'`" &
#echo "plot_$name-3"
