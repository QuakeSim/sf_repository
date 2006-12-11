#!/bin/bash
#set -x

# SET THE ENVIRONMENT
export PORTAL_SERVER_URL=http://localhost:8080/

export BUILD_DIR=`pwd`
export THIRD_PARTY_TOOLS=$BUILD_DIR/third_party_tools
export ANT_HOME=$THIRD_PARTY_TOOLS/apache-ant-1.6.5
export MAVEN_HOME=$THIRD_PARTY_TOOLS/maven-2.0.4

export PORTAL_DEPLOY_DIR=$BUILD_DIR/portal_deploy
export CATALINA_HOME=$PORTAL_DEPLOY_DIR/apache-tomcat-5.5.12
export GRIDSPHERE_HOME=$PORTAL_DEPLOY_DIR/gridsphere-2.1.4
#export GRIDSPHERE_HOME=$PORTAL_DEPLOY_DIR/gridsphere-2.1
export PORTAL_WEBAPP_DIR=$CATALINA_HOME/webapps/gridsphere/

# These are maven steps.

# Here are the "portal" services.
export WEB_SERVICES_HOME=$BUILD_DIR/WebServices

# Here is the AntVisco execution service.
export ANT_EXECUTION_HOME=$BUILD_DIR/ExecutionServices/AntVisco
export RDAHMM_EXECUTION_HOME=$BUILD_DIR/ExecutionServices/RDAHMMService
export STFILTER_EXECUTION_HOME=$BUILD_DIR/ExecutionServices/STFilterService
export GNUPLOT_EXECUTION_HOME=$BUILD_DIR/ExecutionServices/GnuplotService
export ANALYZE_TSERI_EXECUTION_HOME=$BUILD_DIR/ExecutionServices/AnalyzeTseriService

# These are needed to use the rdahmm service.
export RDAHMM_BIN_HOME=$HOME/GEMCodes/RDAHMM2/bin/

#Generic project code
export GENERIC_PROJECT_HOME=$BUILD_DIR/GenericQuakeSimProject

#Standalone JSF (for development).
export SIMPLEX_SA_HOME=$BUILD_DIR/jsf_standalone/Simplex
export RDAHMM_SA_HOME=$BUILD_DIR/jsf_standalone/RDAHMM
export STFILTER_SA_HOME=$BUILD_DIR/jsf_standalone/st_filter
export SENSORGRID_SA_HOME=$BUILD_DIR/jsf_standalone/sensorgrid
export SIMPLE_RDAHMM_SA_HOME=$BUILD_DIR/jsf_standalone/SimpleRDAHMMClient/
export STATION_MONITOR_SA_HOME=$BUILD_DIR/jsf_standalone/StationMonitor/
export REALTIME_RDAHMM_SA_HOME=$BUILD_DIR/jsf_standalone/RealTimeRDAHMM
export SIMPLE_STFILTER_SA_HOME=$BUILD_DIR/jsf_standalone/simple_stfilter
export PLOT_GRWS_TIMESERIES_SA_HOME=$BUILD_DIR/jsf_standalone/PlotGRWSTimeSeries

#Portlets
export SIMPLEX_PORTLET_HOME=$BUILD_DIR/portlets/Simplex-portlet
export RDAHMM_PORTLET_HOME=$BUILD_DIR/portlets/RDAHMM-portlet
export STFILTER_PORTLET_HOME=$BUILD_DIR/portlets/STFILTER-portlet
export STATION_MONITOR_PORTLET_HOME=$BUILD_DIR/portlets/StationMonitor-portlet
export REALTIME_RDAHMM_PORTLET_HOME=$BUILD_DIR/portlets/RealTimeRdahmm-portlet

export PATH=$ANT_HOME/bin:$MAVEN_HOME/bin:$PATH
