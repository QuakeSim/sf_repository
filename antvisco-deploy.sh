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
if mvn clean install
then
    echo Ant exec install complete.
else
    exit 1
fi
cp -r target/AntVisco $CATALINA_HOME/webapps



