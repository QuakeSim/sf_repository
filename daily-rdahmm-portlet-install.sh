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
cd $DAILY_RDAHMM_PORTLET_HOME
export PORTLET_NAME=DailyRDAHMM-portlet
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME -Dgoogle.key=$GOOGLE_KEY -Dportal.host.name=$PORTAL_HOST_NAME

then
    echo Install complete
else
    exit 1
fi
cp -r target/$PORTLET_NAME $CATALINA_HOME/webapps
touch $PORTAL_WEBAPP_DIR/WEB-INF/CustomPortal/portlets/$PORTLET_NAME

mkdir $CATALINA_HOME/webapps/ROOT/yui_0.12.2/build/slider/assets
cp -r yui-assets-plus/* $CATALINA_HOME/webapps/ROOT/yui_0.12.2/build/slider/assets/

