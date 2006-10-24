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
echo "Installing RDAHMM"
echo "######################################"
cd $SIMPLE_RDAHMM_SA_HOME
if mvn clean package
then
    echo Install complete
else
    exit 1
fi
cp -r target/SimpleRDAHMMClient $CATALINA_HOME/webapps



