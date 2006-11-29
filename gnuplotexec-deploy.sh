#!/bin/bash 
#set -x

#---------------------------------------------------------------------
# Note this build depends on a compiled version of GNUPLOT to be 
# located in GNUPLOT_BIN_HOME.  
#---------------------------------------------------------------------

#----------------------------------------------------------------------
# SET ENVIRONMENT
echo "######################################"
echo "#  Setting up the build environment  #"
echo "######################################"
echo ""
source ./env.sh

echo ""
echo "######################################"
echo "Deploying GNUPLOT Execution Service"
echo "######################################"
cd $GNUPLOT_EXECUTION_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME -Dgnuplot.service.url=$PORTAL_SERVER_URL/gnuplotexec
then
    echo Ant exec install complete.
else
    exit 1
fi
cp -r target/gnuplotexec $CATALINA_HOME/webapps

# Copy over the GNUPLOT Executable
if 
    chmod +x $CATALINA_HOME/webapps/gnuplotexec/WEB-INF/binaryexec/*
then 
    echo "GNUPLOT scripts installed."
else 
    echo Could not install gnuplot binary.
    exit 1
fi
