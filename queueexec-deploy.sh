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
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME -Dqueue.service.url=$PORTAL_SERVER_URL/queueexec
then
    echo Queue exec install complete.
else
    exit 1
fi
cp -r target/queueexec $CATALINA_HOME/webapps

# Copy over the Queue and related executable
if 
    mkdir -p $CATALINA_HOME/webapps/queueexec/WEB-INF/binaryexec
    cp $QUEUE_BIN_HOME/queue $CATALINA_HOME/webapps/queueexec/WEB-INF/binaryexec
    chmod +x $CATALINA_HOME/webapps/queueexec/WEB-INF/binaryexec/*
then 
    echo "Queue binary installed"
else 
    echo Could not install Queue binary.
    exit 1
fi


