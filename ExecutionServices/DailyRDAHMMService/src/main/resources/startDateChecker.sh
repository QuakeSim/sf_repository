#!/bin/sh

WORKDIR=$1
OUTPUTPATH=$2

echo $WORKDIR
echo $OUTPUTPATH

JARS="/globalhome/xgao/projects/crisisgrid/QuakeSim2/portal_deploy/apache-tomcat-5.5.20/webapps/rdahmmexec/WEB-INF/lib/ant-1.6.5.jar";

for i in /globalhome/xgao/projects/crisisgrid/QuakeSim2/portal_deploy/apache-tomcat-5.5.20/webapps/rdahmmexec/WEB-INF/lib/*.jar;
do
        JARS=$JARS:$i;
done;

java -classpath $JARS:$CLASSPATH cgl.webservices.StartDateChecker $WORKDIR $OUTPUTPATH &

