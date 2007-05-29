#!/bin/bash 
#set -x

#---------------------------------------------------------------------
# Note this build depends on a compiled version of Simplex to be 
# located in SIMPLEX_BIN_HOME.  
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
echo "Deploying SimpleX Execution Service"
echo "######################################"
cd $SIMPLEX_EXECUTION_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME -Dsimplex.service.url=$PORTAL_SERVER_URL/simplexexec
then
    echo simplex exec install complete.
else
    exit 1
fi
cp -r target/simplexexec $CATALINA_HOME/webapps

# Copy over the SimpleX and related executable
if
    mkdir -p $CATALINA_HOME/webapps/simplexexec/WEB-INF/binaryexec
    cp $SIMPLEX_BIN_HOME/* $CATALINA_HOME/webapps/simplexexec/WEB-INF/binaryexec
    chmod +x $CATALINA_HOME/webapps/simplexexec/WEB-INF/binaryexec/*
then
    echo "SimpleX binary installed"
else
    echo Could not install SimpleX binary.
    exit 1
fi

