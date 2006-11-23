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
echo "Installing STATION_MONITOR"
echo "######################################"
cd $STATION_MONITOR_SA_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME

then
    echo Install complete
else
    exit 1
fi
cp -r target/StationMonitor $CATALINA_HOME/webapps



