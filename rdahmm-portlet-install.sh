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
cd $RDAHMM_PORTLET_HOME
export PORTLET_NAME=RDAHMM-portlet
mvn clean package
cp -r target/$PORTLET_NAME $CATALINA_HOME/webapps
touch $PORTAL_WEBAPP_DIR/WEB-INF/CustomPortal/portlets/$PORTLET_NAME


