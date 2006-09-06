#!/bin/bash 
#set -x

#
# This script short-circuits the tar unpacking, portlet container building
# and web service installation steps.
#
source ./env.sh

echo "Build home is $BUILD_DIR"


cd $BUILD_DIR
source ./genericproject-install.sh

#cd $BUILD_DIR
#source ./simplex-sa-install.sh

cd $BUILD_DIR
source ./rdahmm-sa-install.sh

cd $BUILD_DIR
source ./rdahmm-portlet-install.sh

cd $BUILD_DIR
source ./stfilter-sa-install.sh

cd $BUILD_DIR
source ./stfilter-portlet-install.sh


#cd $BUILD_DIR
#source ./stfilter-portlet-install.sh


#cd $BUILD_DIR
#source ./simplex-portlet-install.sh
