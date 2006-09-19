#!/bin/bash 
#set -x

#----------------------------------------------------------------------
# SET ENVIRONMENT
echo "######################################"
echo "#  Setting up the build environment  #"
echo "######################################"
echo ""

unset ANT_HOME
source ./env.sh

#----------------------------------------------------------------------

# UNPACK THE VENDOR SOFTWARE
echo "######################################"
echo "Unpacking Tomcat and GridSphere..."
echo "######################################"
cd $PORTAL_DEPLOY_DIR
for i in *.tar.gz 
do
     echo "unpacking $i"; tar xzf $i
done
for i in *.tgz
do
     echo "unpacking $i"; tar xzf $i
done

echo ""
echo "######################################"
echo "Unpacking third party tools."
echo "######################################"
echo "Unpacking Ant and Maven..."
cd $THIRD_PARTY_TOOLS
for i in *.tar.gz
  do echo "unpacking $i"; tar xzf $i
done

