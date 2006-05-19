#!/bin/bash 
#set -x
source ./env.sh

cd $BUILD_HOME
source ./unpack-tars.sh

cd $BUILD_HOME
source ./build-gridsphere.sh

cd $BUILD_HOME
source ./customize-portal.sh
