#!/bin/bash 
#set -x

#---------------------------------------------------------------------
# Note this build depends on a compiled version of RDAHMM to be 
# located in RDAHMM_BIN_HOME.  
#---------------------------------------------------------------------

#----------------------------------------------------------------------
# SET ENVIRONMENT
echo "######################################"
echo "#  Setting up the build environment  #"
echo "######################################"
echo ""
source ./env.sh

echo ""
echo "######################################"
echo "Deploying DAILY RDAHMM Execution Service"
echo "######################################"
cd $DAILY_RDAHMM_EXECUTION_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME -Ddaily_rdahmm.service.url=$PORTAL_SERVER_URL/daily_rdahmmexec
then
    echo Ant exec install complete.
else
    exit 1
fi
cp -r target/daily_rdahmmexec $CATALINA_HOME/webapps

# Copy over the RDAHMM Executable
if 
    mkdir -p $CATALINA_HOME/webapps/daily_rdahmmexec/WEB-INF/binaryexec
    cp $RDAHMM_BIN_HOME/rdahmm $CATALINA_HOME/webapps/daily_rdahmmexec/WEB-INF/binaryexec
    chmod +x $CATALINA_HOME/webapps/daily_rdahmmexec/WEB-INF/binaryexec/*
then 
    echo "DAILY RDAHMM binary installed"
else 
    echo Could not install rdahmm binary.
    exit 1
fi


