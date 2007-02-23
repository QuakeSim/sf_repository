#!/bin/bash 
#set -x

#---------------------------------------------------------------------
# Note this build depends on a compiled version of GeoFEST to be 
# located in GEOFEST_BIN_HOME.  
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
echo "Deploying GeoFEST Execution Service"
echo "######################################"
cd $GEOFEST_EXECUTION_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME -Dgeofest.service.url=$PORTAL_SERVER_URL/geofestexec
then
    echo GeoFEST exec install complete.
else
    exit 1
fi
cp -r target/geofestexec $CATALINA_HOME/webapps

# Copy over the GeoFEST and related executable
if 
    mkdir -p $CATALINA_HOME/webapps/geofestexec/WEB-INF/binaryexec
    cp $GEOFEST_BIN_HOME/* $CATALINA_HOME/webapps/geofestexec/WEB-INF/binaryexec
    chmod +x $CATALINA_HOME/webapps/geofestexec/WEB-INF/binaryexec/*
then 
    echo "GeoFEST binary installed"
else 
    echo Could not install GeoFEST binary.
    exit 1
fi


