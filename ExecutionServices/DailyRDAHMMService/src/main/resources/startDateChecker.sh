#!/bin/sh

WORKDIR=$1
OUTPUTPATH=$2

echo $WORKDIR
echo $OUTPUTPATH

JARS="${tomcat.base.dir}/webapps/${pom.artifactId}//WEB-INF/lib/ant-1.6.5.jar";

for i in ${tomcat.base.dir}/webapps/${pom.artifactId}/WEB-INF/lib/*.jar;
do
        JARS=$JARS:$i;
done;

java -classpath $JARS:$CLASSPATH cgl.webservices.StartDateChecker $WORKDIR $OUTPUTPATH &

