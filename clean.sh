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

cd $SENSORGRID_SA_HOME
mvn clean

#Clean up portlet stuff
cd $SIMPLEX_PORTLET_HOME
mvn clean

cd $RDAHMM_PORTLET_HOME
mvn clean

cd $STFILTER_SA_HOME
mvn clean


# CLEAN VENDOR SOFTWARE
echo "#################################"
echo "#  Cleaning vendor packages...  #"
echo "#################################"
echo ""
cd $PORTAL_DEPLOY_DIR
rm -rf apache-tomcat-5.5.12
rm -rf gridsphere-2.1.4 
#find . -type d -exec rm -fr {} \;

cd $THIRD_PARTY_TOOLS
rm -rf maven-2.0.4
rm -rf apache-ant-1.6.5


cd $BUILD_DIR
echo "#################################"
echo "#  Removing emacs backup files #"
echo "#################################"
rm `find . -name "*~"`

