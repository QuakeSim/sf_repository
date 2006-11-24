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
echo "Installing REALTIME_RDAHMM"
echo "######################################"
cd $REALTIME_RDAHMM_PORTLET_HOME
export PORTLET_NAME=RealTimeRDAHMM-portlet
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME

then
    echo Install complete
else
    exit 1
fi
cp -r target/$PORTLET_NAME $CATALINA_HOME/webapps
touch $PORTAL_WEBAPP_DIR/WEB-INF/CustomPortal/portlets/$PORTLET_NAME




