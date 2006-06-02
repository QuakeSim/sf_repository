#!/bin/bash
#set -x

# SET ENVIRONMENT
source ./env.sh

echo "#################################"
echo "Shutting down the tomcat servers"
echo "#################################"
source ./tomcat-shutdown.sh
sleep 3

# CLEAN VENDOR SOFTWARE
echo "#################################"
echo "#  Cleaning vendor packages...  #"
echo "#################################"
echo ""
cd $PORTAL_DEPLOY_DIR
find . -type d -exec rm -fr {} \;

cd $THIRD_PARTY_TOOLS
find . -type d -exec rm -fr {} \;


#Install web services
cd $WEB_SERVICES_HOME
mvn clean

cd $SIMPLEX_PORTLET_HOME
mvn clean
