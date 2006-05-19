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
echo "Installing Gridsphere"
echo "######################################"
# NEEDED FOR GRIDSPHERE COMPATIBILITY
#cp $ANT_HOME/lib/ant.jar $CATALINA_HOME/shared/lib
#cp $ANT_HOME/lib/ant-launcher.jar $CATALINA_HOME/shared/lib

cd $GRIDSPHERE_HOME
ant install <<EOF
y
n
EOF
