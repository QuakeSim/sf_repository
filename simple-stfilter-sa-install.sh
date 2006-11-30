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
cd $SIMPLE_STFILTER_SA_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME
then
    echo Install complete
else
    exit 1
fi
cp -r target/simple_stfilter $CATALINA_HOME/webapps



