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
echo "Installing Generic Bean Code"
echo "######################################"
cd $GENERIC_PROJECT_HOME
if mvn clean install
then
    echo Install complete
else
    echo Install failed
    exit 1
fi





