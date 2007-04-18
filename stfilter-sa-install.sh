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
echo "Installing STFILTER"
echo "######################################"
cd $STFILTER_SA_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME -Danalyze_tseri.service.url=$PORTAL_SERVER_URL/analyze-tseri-exec -Dportal.host.name=$PORTAL_HOST_NAME -Dportal.server.url=$PORTAL_SERVER_URL -Dgoogle.key=$GOOGLE_KEY
then 
    echo Install complete
else
    echo Install failed
    exit 1
fi

cp -r target/STFILTER $CATALINA_HOME/webapps



