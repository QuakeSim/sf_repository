#!/bin/bash 
#set -x
source ./env.sh

echo "Build home is $BUILD_DIR"
#----------------------------------------------------------------------
# Unpack various third party jars for tomcat, gridsphere, etc.
#----------------------------------------------------------------------
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

#----------------------------------------------------------------------
# Build the Ant execution services
#----------------------------------------------------------------------
# Compile the parent service class.
cd $BUILD_DIR
source ./antvisco-deploy.sh

cd $BUILD_DIR
source ./rdahmmexec-deploy.sh

cd $BUILD_DIR
source ./gnuplotexec-deploy.sh

# Not installed, probably obsolete.
#cd $BUILD_DIR
#source ./stfilterexec-deploy.sh

cd $BUILD_DIR
source ./analyze-tseri-exec-deploy.sh

cd $BUILD_DIR
source ./geofestexec-deploy.sh

cd $BUILD_DIR
source ./dislocexec-deploy.sh

cd $BUILD_DIR
source ./simplexexec-deploy.sh

cd $BUILD_DIR
source ./kmlgenerator-deploy.sh

#----------------------------------------------------------------------
# Build the generic project classes.
#----------------------------------------------------------------------
cd $BUILD_DIR
source ./genericproject-install.sh

#----------------------------------------------------------------------
# Buid the standalone (SA) web applications.
#----------------------------------------------------------------------
#cd $BUILD_DIR
#source ./simplex-sa-install.sh

# Uncommented since we only need for development.
#cd $BUILD_DIR
#source ./rdahmm-sa-install.sh

#cd $BUILD_DIR
#source ./simple-rdahmm-sa-install.sh

# Development version
#cd $BUILD_DIR
#source ./simple-stfilter-sa-install.sh

# Development version
#cd $BUILD_DIR
#source ./stfilter-sa-install.sh

cd $BUILD_DIR
source ./plotgrws-timeseries-sa-install.sh

#cd $BUILD_DIR
#source ./meshgenerator-sa-install.sh

#----------------------------------------------------------------------
# Build the portlets.
#----------------------------------------------------------------------
cd $BUILD_DIR
source ./stfilter-portlet-install.sh

cd $BUILD_DIR
source ./rdahmm-portlet-install.sh

cd $BUILD_DIR
source ./simplex-portlet-install.sh

cd $BUILD_DIR
source ./newmeshgen-portlet-install.sh

cd $BUILD_DIR
source ./realtime-rdahmm-portlet-install.sh

cd $BUILD_DIR
source ./daily-rdahmm-portlet-install.sh

cd $BUILD_DIR
source ./stationmonitor-portlet-install.sh

cd $BUILD_DIR
source ./disloc-portlet-install.sh

cd $BUILD_DIR
source ./geofestgrid-portlet-install.sh

#cd $BUILD_DIR
#source ./simplex-portlet-install.sh

# Portlet version has bugs, so don't install
#cd $BUILD_DIR
#source ./plotgrws-timeseries-portlet-install.sh


