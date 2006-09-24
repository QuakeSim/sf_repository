#!/bin/bash 
#set -x
source ./env.sh

echo "Build home is $BUILD_DIR"

# Unpack various third party jars for tomcat, gridsphere, etc.
cd $BUILD_DIR
source ./unpack-tars.sh

# Run gridsphere's ant
cd $BUILD_DIR
source ./build-gridsphere.sh

# Apply various customizations.
cd $BUILD_DIR
source ./customize-portal.sh

# Install the portal services
cd $BUILD_DIR
source ./install-web-services.sh

# Build the Ant execution service
cd $BUILD_DIR
source ./antvisco-deploy.sh

# Build the generic project classes.
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
