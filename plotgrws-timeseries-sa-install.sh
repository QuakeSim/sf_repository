#!/bin/bash 
#set -x

#----------------------------------------------------------------------
# SET ENVIRONMENT
echo "######################################"
echo "#  Setting up the build environment  #"
echo "######################################"
echo ""
source ./env.sh


echo ""
echo "######################################"
echo "Installing STFILTER"
echo "######################################"
cd $PLOT_GRWS_TIMESERIES_SA_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME
then 
    echo Install complete
else
    echo Install failed
    exit 1
fi

cp -r target/PlotGRWSTimeSeries $CATALINA_HOME/webapps



