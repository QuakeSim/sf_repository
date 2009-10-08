#!/bin/sh

SUBDIR=$1
PROPFILE=$2

echo $SUBDIR
echo $PROPFILE

rm -f -r /globalhome/xgao/projects/crisisgrid/QuakeSim2/portal_deploy/apache-tomcat-5.5.20/webapps/rdahmmexec/daily/$SUBDIR/*-*
mkdir /globalhome/xgao/projects/crisisgrid/QuakeSim2/portal_deploy/apache-tomcat-5.5.20/webapps/rdahmmexec/daily/$SUBDIR
rm -f -r /globalhome/xgao/projects/crisisgrid/QuakeSim2/portal_deploy/apache-tomcat-5.5.20/webapps/DailyRDAHMM-portlet/WEB-INF/config/*tmp*
rm -f -r /globalhome/xgao/RDAHMM_WDIR/$SUBDIR/*-*

JARS="/globalhome/xgao/projects/crisisgrid/QuakeSim2/portal_deploy/apache-tomcat-5.5.20/webapps/rdahmmexec/WEB-INF/lib/ant-1.6.5.jar";

for i in /globalhome/xgao/projects/crisisgrid/QuakeSim2/portal_deploy/apache-tomcat-5.5.20/webapps/rdahmmexec/WEB-INF/lib/*.jar;
do
        JARS=$JARS:$i;
done;

java -classpath $JARS:$CLASSPATH cgl.webservices.DailyRDAHMMRunner $PROPFILE 1 5 1994-01-01 2006-09-30 2006-10-01 &

