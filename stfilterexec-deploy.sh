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
echo "Deploying STFilter Execution Service"
echo "######################################"
cd $STFILTER_EXECUTION_HOME
if mvn clean package
then
    echo STFilter exec install complete.
else
    exit 1
fi
cp -r target/stfilterexec $CATALINA_HOME/webapps



