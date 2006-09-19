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
   echo $i
   echo ${i%.tar.gz}
   if [ ! -d ${i%.tar.gz} ]; then
     echo "unpacking $i"; tar xzf $i
  else 
      echo "$i already unpacked."
  fi
done
for i in *.tgz
do
  if [ ! -d ${i%.tgz} ]; then
     echo "unpacking $i"; tar xzf $i
  else 
      echo "$i already unpacked."
  fi
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

