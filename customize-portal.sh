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
echo "Customizing portal"
echo "######################################"
cd $BUILD_DIR
cp -r tomcat-configure/conf/* $CATALINA_HOME/conf
cp -r lnf4portal/CustomPortal $PORTAL_WEBAPP_DIR/WEB-INF
cp -r lnf4portal/images/* $PORTAL_WEBAPP_DIR/images
cp -r lnf4portal/html/* $PORTAL_WEBAPP_DIR/html

# Use the monochrome theme.
mv $PORTAL_WEBAPP_DIR/themes/default $PORTAL_WEBAPP_DIR/themes/default_`date +%m_%d_%Y`
cp -r $PORTAL_WEBAPP_DIR/themes/monochrome $PORTAL_WEBAPP_DIR/themes/default


