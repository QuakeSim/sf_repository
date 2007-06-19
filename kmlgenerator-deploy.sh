#!/bin/bash 
#set -x

#---------------------------------------------------------------------
# Note this build depends on a compiled version of KmlGenerator to be 
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
echo "Deploying KmlGenerator Service"
echo "######################################"
cd $KMLGENERATOR_EXECUTION_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME -DKmlGenerator.service.url=$PORTAL_SERVER_URL/KmlGenerator
then
    echo KmlGenerator web service install complete.
else
    exit 1
fi
cp -r target/KmlGenerator $CATALINA_HOME/webapps


