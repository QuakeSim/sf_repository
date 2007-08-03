#!/bin/bash 
#set -x

#---------------------------------------------------------------------
# Note this build depends on a compiled version of Queue to be 
# located in QUEUE_BIN_HOME.  
#---------------------------------------------------------------------

#----------------------------------------------------------------------
# SET ENVIRONMENT
echo "######################################"
echo "#  Setting up the build environment  #"
echo "######################################"
echo ""
source ./env.sh

echo ""
echo "######################################"
echo "Deploying Queue Execution Service"
echo "######################################"
cd $QUEUE_EXECUTION_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME -Dqueue.service.url=$PORTAL_SERVER_URL/queueservice
then
    echo Queue exec install complete.
else
    exit 1
fi
cp -r target/queueservice $CATALINA_HOME/webapps



