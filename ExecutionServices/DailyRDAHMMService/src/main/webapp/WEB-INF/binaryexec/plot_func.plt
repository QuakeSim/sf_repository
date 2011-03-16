      # $0 : Combined XYZ file; format: <state> <date> <x> <y> <z>
      # $1 : 3(Plot for X) or 4(Plot for Y) or 5(Plot for Z)
      
      line_style = ` sed '1q' INPUT.Q.class.txt `
      ;system " sed '1d' INPUT.Q.class.txt > INPUT.Q.class.tmp.txt ";
      ;system " mv INPUT.Q.class.tmp.txt INPUT.Q.class.txt";
      line_color = line_style;
      if (line_style == 0) line_color = 64;
      # pointtype 1 is '+'
      if ($1==3) plot "$0" using 2:($$1==line_style ? $$3 - y_base : 1/0) with lines linetype line_color;
      if ($1==3) plot "$0" using 2:($$1==line_style ? $$3 - y_base : 1/0) with points pointtype 1 linetype line_color;
      if ($1==4) plot "$0" using 2:($$1==line_style ? $$4 - y_base : 1/0) with lines linetype line_color;
      if ($1==4) plot "$0" using 2:($$1==line_style ? $$4 - y_base : 1/0) with points pointtype 1 linetype line_color;
      if ($1==5) plot "$0" using 2:($$1==line_style ? $$5 - y_base : 1/0) with lines linetype line_color;
      if ($1==5) plot "$0" using 2:($$1==line_style ? $$5 - y_base : 1/0) with points pointtype 1 linetype line_color;
      class_start = class_start+1;
      if(class_start <  class_total) reread
