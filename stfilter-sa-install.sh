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
cd $STFILTER_SA_HOME
mvn clean package
cp -r target/STFILTER $CATALINA_HOME/webapps



