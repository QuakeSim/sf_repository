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
echo "Deploying Ant Execution Service"
echo "######################################"
cd $ANT_EXECUTION_HOME
mvn clean package
cp -r target/antexec $CATALINA_HOME/webapps



