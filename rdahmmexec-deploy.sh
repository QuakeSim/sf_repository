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
cd $RDAHMM_EXECUTION_HOME
if mvn clean package
then
    echo Ant exec install complete.
else
    exit 1
fi
cp -r target/rdahmmexec $CATALINA_HOME/webapps



