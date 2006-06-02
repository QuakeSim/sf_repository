#!/bin/bash 
#set -x
source ./env.sh

echo "Build home is $BUILD_DIR"

cd $BUILD_DIR
source ./unpack-tars.sh

cd $BUILD_DIR
source ./build-gridsphere.sh

cd $BUILD_DIR
source ./customize-portal.sh

cd $BUILD_DIR
source ./install-web-services.sh

cd $BUILD_DIR
source ./simplex-sa-install.sh

#cd $BUILD_DIR
#source ./simplex-portlet-install.sh
