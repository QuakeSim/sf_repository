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
echo "Installing REALTIME_RDAHMM"
echo "######################################"
cd $REALTIME_RDAHMM_SA_HOME
if mvn clean package 

then
    echo Install complete
else
    exit 1
fi
cp -r target/RealTimeRDAHMM $CATALINA_HOME/webapps



