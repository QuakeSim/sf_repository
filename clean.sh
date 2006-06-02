#!/bin/bash
#set -x

# SET ENVIRONMENT
source ./env.sh

echo "#################################"
echo "Shutting down the tomcat servers"
echo "#################################"
source ./tomcat-shutdown.sh
sleep 3

#Clean up the code
cd $WEB_SERVICES_HOME
mvn clean

# Clean up standalone stuff
cd $SIMPLEX_SA_HOME
mvn clean

cd $RDAHMM_SA_HOME
mvn clean


#Clean up portlet stuff
cd $SIMPLEX_PORTLET_HOME
mvn clean

cd $RDAHMM_PORTLET_HOME
mvn clean



# CLEAN VENDOR SOFTWARE
echo "#################################"
echo "#  Cleaning vendor packages...  #"
echo "#################################"
echo ""
cd $PORTAL_DEPLOY_DIR
find . -type d -exec rm -fr {} \;

cd $THIRD_PARTY_TOOLS
find . -type d -exec rm -fr {} \;


