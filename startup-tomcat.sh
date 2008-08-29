#!/bin/bash 
#set -x

# SET ENVIRONMENT
echo "######################################"
echo "#  Setting up the build environment  #"
echo "######################################"
echo ""

source ./env.sh

export CATALINA_OPTS="-server -Xms512m -Xmx512m -XX:NewSize=128m -XX:PermSize=16m -XX:MaxPermSize=128m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC"

# SHUTDOWN TOMCAT before starting
echo "####################################"
echo "#  Starting up Portal Tomcat...  #"
echo "####################################"
$CATALINA_HOME/bin/shutdown.sh
sleep 3
$CATALINA_HOME/bin/startup.sh
