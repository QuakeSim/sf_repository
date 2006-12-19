#!/bin/bash
#set -x

# SET ENVIRONMENT
source ./env.sh

echo "#################################"
echo "Shutting down the tomcat servers"
echo "#################################"
source ./tomcat-shutdown.sh
sleep 5

#--------------------------------------------------
# Clean up core stuff
#--------------------------------------------------
echo "#######################################"
echo "#  Cleaning generic and legacy stuff  #"
echo "#######################################"
echo ""

cd $GENERIC_PROJECT_HOME
mvn clean

cd $WEB_SERVICES_HOME
mvn clean

#--------------------------------------------------
# Clean up standalone stuff
#--------------------------------------------------
echo "#################################"
echo "#  Cleaning standalone webapps  #"
echo "#################################"
echo ""

cd $SIMPLEX_SA_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

cd $RDAHMM_SA_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

cd $SIMPLE_RDAHMM_SA_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

cd $SIMPLE_STFILTER_SA_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

cd $SENSORGRID_SA_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

cd $STFILTER_SA_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

cd $SIMPLE_RDAHMM_SA_HOME
mvn clean 

cd $REALTIME_RDAHMM_SA_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

cd $STATION_MONITOR_SA_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

cd $PLOT_GRWS_TIMESERIES_SA_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME
 
#Clean up portlet stuff
#cd $SIMPLEX_PORTLET_HOME
#mvn clean

#--------------------------------------------------
# Clean up the portlets.
#--------------------------------------------------
echo "#################################"
echo "#  Cleaning portlets            #"
echo "#################################"
echo ""

cd $RDAHMM_PORTLET_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

cd $STFILTER_PORTLET_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

cd $REALTIME_RDAHMM_PORTLET_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

cd $STATION_MONITOR_PORTLET_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

cd $PLOT_GRWS_TIMESERIES_PORTLET_HOME
mvn clean -Dtomcat.base.dir=$CATALINA_HOME

#--------------------------------------------------
# Clean up the execution services.
#--------------------------------------------------
echo "#################################"
echo "#  Cleaning execution services  #"
echo "#################################"
echo ""

cd $ANT_EXECUTION_HOME
mvn clean

cd $RDAHMM_EXECUTION_HOME
mvn clean -Dtomcat.base.dir=/tmp -Drdahmm.service.url=/tmp

cd $GNUPLOT_EXECUTION_HOME
mvn clean -Dtomcat.base.dir=/tmp -Dgnuplot.service.url=/tmp

cd $STFILTER_EXECUTION_HOME
mvn clean

cd $ANALYZE_TSERI_EXECUTION_HOME
mvn clean -Dtomcat.base.dir=/tmp -Danalyze_tseri.service.url=/tmp

#--------------------------------------------------
# CLEAN VENDOR SOFTWARE
#--------------------------------------------------
echo "#################################"
echo "#  Cleaning vendor packages...  #"
echo "#################################"
echo ""
cd $PORTAL_DEPLOY_DIR
rm -rf apache-tomcat-5.5.12
rm -rf gridsphere-2.1.4 
#find . -type d -exec rm -fr {} \;

cd $THIRD_PARTY_TOOLS
rm -rf maven-2.0.4
rm -rf apache-ant-1.6.5


#--------------------------------------------------
# Clean up any junk.
#--------------------------------------------------
cd $BUILD_DIR
echo "#################################"
echo "#  Removing emacs backup files #"
echo "#################################"
rm `find . -name "*~"`

