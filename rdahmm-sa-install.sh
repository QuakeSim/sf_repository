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
cd $RDAHMM_SA_HOME
if mvn clean package -Dportal.server.url=$PORTAL_SERVER_URL -Dbuild.dir=$BUILD_DIR -Dtomcat.base.dir=$CATALINA_HOME -Dportal.host.name=$PORTAL_HOST_NAME

then
    echo Install complete
else
    exit 1
fi
cp -r target/RDAHMM $CATALINA_HOME/webapps



