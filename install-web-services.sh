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
echo "Make the war for deployment in tomcat"
echo "######################################"
cd $WEB_SERVICES_HOME
mvn clean install -Dpackaging=war
cp -r $WEB_SERVICES_HOME/target/WebServices $CATALINA_HOME/webapps


