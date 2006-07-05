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
echo "Installing SensorGrid"
echo "######################################"
cd $SENSORGRID_SA_HOME
mvn clean package
cp -r target/sensorgrid $CATALINA_HOME/webapps



