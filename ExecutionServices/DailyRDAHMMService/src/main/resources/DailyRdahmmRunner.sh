#!/bin/sh

SUBDIR=$1
PROPFILE=$2

echo $SUBDIR
echo $PROPFILE

rm -f -r ${tomcat.base.dir}/webapps/${pom.artifactId}/daily/$SUBDIR/*-*
mkdirhier ${tomcat.base.dir}/webapps/${pom.artifactId}/daily/$SUBDIR
rm -f -r ${tomcat.base.dir}/webapps/DailyRDAHMM-portlet/WEB-INF/config/*tmp*
rm -f -r ${user.home}/DAILY_RDAHMM_WDIR/$SUBDIR/*-*

JARS="${tomcat.base.dir}/webapps/${pom.artifactId}/WEB-INF/lib/ant-1.6.5.jar";

for i in ${tomcat.base.dir}/webapps/${pom.artifactId}/WEB-INF/lib/*.jar;
do
        JARS=$JARS:$i;
done;

java -classpath $JARS:$CLASSPATH cgl.webservices.DailyRDAHMMRunner $PROPFILE 1 &
