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
echo "Customizing portal"
echo "######################################"
cd $RDAHMM_SA_HOME
mvn clean install
cp -r target/RDAHMM $CATALINA_HOME/webapps



