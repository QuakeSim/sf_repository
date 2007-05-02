#!/bin/bash
#set -x

# SET THE ENVIRONMENT

#--------------------------------------------------
# The following variables must be set for your 
# individual installation.
#--------------------------------------------------
export PORTAL_HOST_NAME=gf19.ucs.indiana.edu 
export PORTAL_SERVER_URL=http://$PORTAL_HOST_NAME:8080/
export GOOGLE_KEY="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBSPTG4M0VeHMNoDOKS2I0IJH6WVtRT4Ye3YMLMmOKDiX0HJAySVruBU5g"

#--------------------------------------------------
# None of the variables below should be changed.
#--------------------------------------------------
export BUILD_DIR=`pwd`
export THIRD_PARTY_TOOLS=$BUILD_DIR/third_party_tools
export ANT_HOME=$THIRD_PARTY_TOOLS/apache-ant-1.6.5
export MAVEN_HOME=$THIRD_PARTY_TOOLS/maven-2.0.4

export PORTAL_DEPLOY_DIR=$BUILD_DIR/portal_deploy
#export CATALINA_HOME=$PORTAL_DEPLOY_DIR/apache-tomcat-5.5.12
export CATALINA_HOME=$PORTAL_DEPLOY_DIR/apache-tomcat-5.5.20
export GRIDSPHERE_HOME=$PORTAL_DEPLOY_DIR/gridsphere-2.1.4
export YUI_SOURCE_DIR=$PORTAL_DEPLOY_DIR/yui_0.12.2
export PORTAL_WEBAPP_DIR=$CATALINA_HOME/webapps/gridsphere/
export YUI_DEPLOY_DIR=$CATALINA_HOME/webapps/ROOT

# These are maven steps.

# Here are the "portal" services.
export WEB_SERVICES_HOME=$BUILD_DIR/WebServices

# Here is the AntVisco execution service.
export ANT_EXECUTION_HOME=$BUILD_DIR/ExecutionServices/AntVisco
export RDAHMM_EXECUTION_HOME=$BUILD_DIR/ExecutionServices/RDAHMMService
export STFILTER_EXECUTION_HOME=$BUILD_DIR/ExecutionServices/STFilterService
export GNUPLOT_EXECUTION_HOME=$BUILD_DIR/ExecutionServices/GnuplotService
export ANALYZE_TSERI_EXECUTION_HOME=$BUILD_DIR/ExecutionServices/AnalyzeTseriService
export GEOFEST_EXECUTION_HOME=$BUILD_DIR/ExecutionServices/GeoFESTService
export DISLOC_EXECUTION_HOME=$BUILD_DIR/ExecutionServices/DislocService

# These are directory locations for binaries that we should not put in
# SourceForge
export RDAHMM_BIN_HOME=$HOME/GEMCodes/RDAHMM2/bin/
export GEOFEST_BIN_HOME=$HOME/GEMCodes/GeoFEST/bin/
export DISLOC_BIN_HOME=$HOME/GEMCodes/Disloc/

#Generic project code
export GENERIC_PROJECT_HOME=$BUILD_DIR/GenericQuakeSimProject

#Standalone JSF (for development).
export SIMPLEX_SA_HOME=$BUILD_DIR/jsf_standalone/Simplex
export RDAHMM_SA_HOME=$BUILD_DIR/jsf_standalone/RDAHMM/
export STFILTER_SA_HOME=$BUILD_DIR/jsf_standalone/st_filter/
export SENSORGRID_SA_HOME=$BUILD_DIR/jsf_standalone/sensorgrid
export SIMPLE_RDAHMM_SA_HOME=$BUILD_DIR/jsf_standalone/SimpleRDAHMMClient/
export STATION_MONITOR_SA_HOME=$BUILD_DIR/jsf_standalone/StationMonitor/
export REALTIME_RDAHMM_SA_HOME=$BUILD_DIR/jsf_standalone/RealTimeRDAHMM
export SIMPLE_STFILTER_SA_HOME=$BUILD_DIR/jsf_standalone/simple_stfilter
export PLOT_GRWS_TIMESERIES_SA_HOME=$BUILD_DIR/jsf_standalone/PlotGRWSTimeSeries
export MESHGENERATOR_SA_HOME=$BUILD_DIR/jsf_standalone/MeshGenerator
export NEW_MESHGENERATOR_SA_HOME=$BUILD_DIR/jsf_standalone/NewMeshGen2
export DISLOC_SA_HOME=$BUILD_DIR/jsf_standalone/NewDisloc

#Portlets
export SIMPLEX_PORTLET_HOME=$BUILD_DIR/portlets/Simplex-portlet
export RDAHMM_PORTLET_HOME=$BUILD_DIR/portlets/RDAHMM-portlet
export STFILTER_PORTLET_HOME=$BUILD_DIR/portlets/STFILTER-portlet
export STATION_MONITOR_PORTLET_HOME=$BUILD_DIR/portlets/StationMonitor-portlet
export REALTIME_RDAHMM_PORTLET_HOME=$BUILD_DIR/portlets/RealTimeRdahmm-portlet
export PLOT_GRWS_TIMESERIES_PORTLET_HOME=$BUILD_DIR/portlets/PlotGRWSTimeSeries-portlet
export MESHGENERATOR_PORTLET_HOME=$BUILD_DIR/portlets/MeshGenerator-portlet
export NEW_MESHGENERATOR_PORTLET_HOME=$BUILD_DIR/portlets/NewMeshGen-portlet
export DISLOC_PORTLET_HOME=$BUILD_DIR/portlets/Disloc-portlet

#Set the path to include ant and maven.
export PATH=$ANT_HOME/bin:$MAVEN_HOME/bin:$PATH
