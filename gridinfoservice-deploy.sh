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
echo "Deploying GridInfo Service"
echo "######################################"
cd $GRIDINFO_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME 
then
    echo GridInfo compile complete.
else
    exit 1
fi
if cp -r target/gridinfo $CATALINA_HOME/webapps
then 
    echo GridInfo install complete.
else
	 exit 1
fi


