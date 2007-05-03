#!/bin/bash

#--------------------------------------------------
# Download build dir
#--------------------------------------------------
echo "Downloading build directory"
if svn co https://crisisgrid.svn.sourceforge.net/svnroot/crisisgrid/QuakeSim2/portal_deploy/
then 
	 echo "Deployment directory downloaded"
else
	 echo "Deployment directory could not be downloaded."
	 echo "BUILD FAILED"
    exit
fi

#--------------------------------------------------
# Download Execution Services
#--------------------------------------------------
echo "Downloading execution services directory"
if svn co https://crisisgrid.svn.sourceforge.net/svnroot/crisisgrid/QuakeSim2/ExecutionServices
then 
	 echo "Execution services directory downloaded"
else
	 echo "Execution services directory could not be downloaded."
	 echo "BUILD FAILED"
    exit
fi

#--------------------------------------------------
# Download third party tools
#------------------------------------------------
echo "Downloading third party tools directory"
if svn co https://crisisgrid.svn.sourceforge.net/svnroot/crisisgrid/QuakeSim2/third_party_tools
then 
	 echo "Third party tools directory downloaded"
else
	 echo "Third party tools directory could not be downloaded."
	 echo "BUILD FAILED"
    exit
fi

#--------------------------------------------------
# Get top level directory shell scripts
#-------------------------------------------------
echo "Downloading top level scripts"
if svn co -N https://crisisgrid.svn.sourceforge.net/svnroot/crisisgrid/QuakeSim2/ .
then 
	 echo "Installation scripts downloaded"
else
	 echo "Installation scripts could not be downloaded."
	 echo "BUILD FAILED"
    exit
fi

#--------------------------------------------------
# Run the scripts
#--------------------------------------------------
source ./env.sh

echo "Build home is $BUILD_DIR"

#----------------------------------------------------------------------
# Unpack various third party jars for tomcat, gridsphere, etc.
#----------------------------------------------------------------------
cd $BUILD_DIR
echo "Unpacking tars"
if source ./unpack-tars.sh
then
	 echo "done..."
else
	 echo "Build failed"
fi

cd $BUILD_DIR
echo "Building RDAHMM"
if source ./rdahmmexec-deploy.sh
then
	 echo "done..."
else
	 echo "Build failed"
fi

cd $BUILD_DIR
echo "Building Geofest"
if source ./geofestexec-deploy.sh
then
	 echo "done..."
else
	 echo "Build failed"
fi

cd $BUILD_DIR
echo "Building Disloc"
if source ./dislocexec-deploy.sh
then
	 echo "done..."
else
	 echo "Build failed"
fi

cd $BUILD_DIR
echo "Starting Tomcat"
if source ./tomcat-startup.sh
then
	 echo "done..."
else
	 echo "Build failed"
fi
