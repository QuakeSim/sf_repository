#!/bin/bash 
#set -x

#---------------------------------------------------------------------
# Note this build depends on a compiled version of ANALYZE_TSERI to be 
# located in ANALYZE_TSERI_EXECUTION_HOME.  
#---------------------------------------------------------------------

#----------------------------------------------------------------------
# SET ENVIRONMENT
#----------------------------------------------------------------------
echo "######################################"
echo "#  Setting up the build environment  #"
echo "######################################"
echo ""
source ./env.sh

echo ""
echo "######################################"
echo "Deploying ANALYZETSERI Execution Service"
echo "######################################"
cd $ANALYZE_TSERI_EXECUTION_HOME
if mvn clean package -Dtomcat.base.dir=$CATALINA_HOME -Danalyze_tseri.service.url=$PORTAL_SERVER_URL/analyze-tseri-exec
then
    echo Ant exec install complete.
else
    exit 1
fi
cp -r target/analyze-tseri-exec $CATALINA_HOME/webapps

# Copy over the ANALYZE_TSERI Executable
if 
    chmod +x $CATALINA_HOME/webapps/analyze-tseri-exec/WEB-INF/binaryexec/*
then 
    echo "ANALYZE_TSERI scripts installed."
else 
    echo Could not install gnuplot binary.
    exit 1
fi