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
cd $SIMPLEX_HOME
mvn install
#cp -r target/Simplex $CATALINA_HOME/webapps



