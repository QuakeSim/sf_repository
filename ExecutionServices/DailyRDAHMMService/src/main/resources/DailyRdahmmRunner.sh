#!/bin/sh

rm -f -r ${tomcat.base.dir}/webapps/${pom.artifactId}/daily/*
rm -f -r ${tomcat.base.dir}/webapps/DailyRDAHMM-portlet/WEB-INF/config/*tmp*
rm -f -r ${user.home}/RDAHMM_WDIR/*2008*

JARS="${tomcat.base.dir}/webapps/${pom.artifactId}/WEB-INF/lib/ant-1.6.5.jar";

for i in ${tomcat.base.dir}/webapps/${pom.artifactId}/WEB-INF/lib/*.jar;
do
        JARS=$JARS:$i;
done;

java -classpath $JARS:$CLASSPATH cgl.webservices.DailyRDAHMMRunner 8 5 1994-01-01 2006-09-30 2006-10-01 &

