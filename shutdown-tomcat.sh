#!/bin/bash 
#set -x

# SET ENVIRONMENT
echo "######################################"
echo "#  Setting up the build environment  #"
echo "######################################"
echo ""

source ./env.sh

# SHUTDOWN TOMCAT
echo "####################################"
echo "#  Shutting down Portal Tomcat...  #"
echo "####################################"

$CATALINA_HOME/bin/shutdown.sh
