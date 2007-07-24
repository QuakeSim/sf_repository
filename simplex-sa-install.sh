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
echo "Installing Simplex"
echo "######################################"
cd $SIMPLEX_SA_HOME
if mvn clean package -Dportal.host.name=$PORTAL_HOST_NAME -Dbuild.dir=$BUILD_DIR -Dtomcat.base.dir=$CATALINA_HOME

then
    echo Install complete
else
    exit 1
fi
cp -r target/Simplex $CATALINA_HOME/webapps




