#!/bin/sh

SUBDIR=$1
PROPFILE=$2

mkdir ${tomcat.base.dir}/webapps/${pom.artifactId}/daily/$SUBDIR
rm -f -r ${tomcat.base.dir}/webapps/${pom.artifactId}/daily/$SUBDIR/*2009*
rm -f -r ${tomcat.base.dir}/webapps/DailyRDAHMM-portlet/*tmp*
rm -f -r ${user.home}/RDAHMM_WDIR/$SUBDIR/*2009*

JARS="";

for i in ${tomcat.base.dir}/webapps/${pom.artifactId}/WEB-INF/lib/*.jar;
do
        JARS=$JARS:$i;
done;

java -classpath $JARS:$CLASSPATH cgl.webservices.DailyRDAHMMRunner $PROPFILE 1 5 1994-01-01 2006-09-30 2006-10-01 &
