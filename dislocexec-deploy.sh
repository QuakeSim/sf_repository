#!/bin/bash 
#set -x

#---------------------------------------------------------------------
# Note this build depends on a compiled version of Disloc to be 
# located in DISLOC_BIN_HOME.  
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
echo "Deploying Disloc Execution Service"
echo "######################################"
cd $DISLOC_EXECUTION_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME -Ddisloc.service.url=$PORTAL_SERVER_URL/dislocexec
then
    echo Disloc exec install complete.
else
    exit 1
fi
cp -r target/dislocexec $CATALINA_HOME/webapps

# Copy over the Disloc and related executable
if 
    mkdir -p $CATALINA_HOME/webapps/dislocexec/WEB-INF/binaryexec
    cp $DISLOC_BIN_HOME/disloc $CATALINA_HOME/webapps/dislocexec/WEB-INF/binaryexec
    chmod +x $CATALINA_HOME/webapps/dislocexec/WEB-INF/binaryexec/*
then 
    echo "Disloc binary installed"
else 
    echo Could not install Disloc binary.
    exit 1
fi


