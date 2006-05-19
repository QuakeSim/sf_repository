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
cp -r lnf4portal/CustomPortal $PORTAL_WEBAPP_DIR/WEB-INF
cp -r lnf4portal/images/* $PORTAL_WEBAPP_DIR/images

