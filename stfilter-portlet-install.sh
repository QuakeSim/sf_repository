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
echo "Installing STFILTER Portlet"
echo "######################################"
cd $STFILTER_PORTLET_HOME
export PORTLET_NAME=STFILTER-portlet
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME -Dgoogle.key=$GOOGLE_KEY -Dportal.host.name=$PORTAL_HOST_NAME 
then
    echo Install complete
else
    echo Install failed
    exit 1
fi

cp -r target/$PORTLET_NAME $CATALINA_HOME/webapps
touch $PORTAL_WEBAPP_DIR/WEB-INF/CustomPortal/portlets/$PORTLET_NAME


