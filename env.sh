#!/bin/bash
#set -x

# SET THE ENVIRONMENT
export BUILD_DIR=$PWD
export PORTAL_DEPLOY_DIR=$BUILD_DIR/portal_deploy
export THIRD_PARTY_TOOLS=$BUILD_DIR/third_party_tools

export CATALINA_HOME=$PORTAL_DEPLOY_DIR/apache-tomcat-5.5.12
export GRIDSPHERE_HOME=$PORTAL_DEPLOY_DIR/gridsphere-2.1.4
export PORTAL_WEBAPP_DIR=$CATALINA_HOME/webapps/gridsphere/

export ANT_HOME=$THIRD_PARTY_TOOLS/apache-ant-1.6.5

