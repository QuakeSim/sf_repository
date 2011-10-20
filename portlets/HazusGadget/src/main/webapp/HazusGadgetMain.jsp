<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<html>
  <head>
	 <link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/quakesim_style.css"/>
	 <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
  </head>
  <body>
	 <f:view>
		<h:messages id="hgmMessagesLoadProject" 
						showDetail="true"
						showSummary="true"
						errorStyle="color: red"/>	 

		<h:panelGrid id="hgmMainPanel" columns="1" columnClasses="alignTopFixWidth">
		  <h:form id="RunHazusForm">
			 <f:verbatim>
				Click the map to select your region of interest, then click "Run Hazus".
			 </f:verbatim>
			 <h:panelGrid id="hazusgadgetlayoutgrid" columns="2" columnClasses="alignTop,alignTop">
				<h:panelGroup id="hazusgadgetleftpanel">
				  <f:verbatim>
					 <div id="mapDiv" style="width: 550px; height: 400px;"></div>
				  </f:verbatim>
				</h:panelGroup>
				<h:panelGroup id="hazusgridrightpanel">
				  <f:verbatim>Bounding Box:</f:verbatim>
				  <h:inputText id="hgmlat0" value="#{HazusGadgetBean.lat0}"/>
				  <h:inputText id="hgmlon0" value="#{HazusGadgetBean.lon0}"/>
				  <h:inputText id="hgmlat1" value="#{HazusGadgetBean.lat1}"/>
				  <h:inputText id="hgmlon1" value="#{HazusGadgetBean.lon1}"/>
				  <h:inputText id="hgmlat2" value="#{HazusGadgetBean.lat2}"/>
				  <h:inputText id="hgmlon2" value="#{HazusGadgetBean.lon2}"/>
				  <h:inputText id="hgmlat3" value="#{HazusGadgetBean.lat3}"/>
				  <h:inputText id="hgmlon3" value="#{HazusGadgetBean.lon3}"/>
				  
				  <h:panelGrid id="hgmFaultInputs" columns="2">
					 <f:verbatim>Fault Type:</f:verbatim>
						<h:selectOneMenu id="hgmFaultTypeMenu" 
											  required="true"
											  value="#{HazusGadgetBean.faultType}">
						  <f:selectItem id="type1" 
												 itemLabel="#{HazusGadgetBean.leftLateralFault}"
												 itemValue="#{HazusGadgetBean.leftLateralFault}"/>
						  <f:selectItem id="type2" 
												 itemLabel="#{HazusGadgetBean.thrustFault}"
												 itemValue="#{HazusGadgetBean.thrustFault}"/>
						  <f:selectItem id="type3" 
												 itemLabel="#{HazusGadgetBean.rightLateralFault}"
												 itemValue="#{HazusGadgetBean.rightLateralFault}"/>
						  <f:selectItem id="type4" 
												 itemLabel="#{HazusGadgetBean.normalFault}"
												 itemValue="#{HazusGadgetBean.normalFault}"/>
						</h:selectOneMenu>

					 <f:verbatim>Magnitude:</f:verbatim>
					 <h:inputText id="hgmMagnitude" required="true" value="#{HazusGadgetBean.magnitude}" title="Please provide a magnitude value between 5-10">
						<f:validateDoubleRange minimum="5.0" maximum="10.0"/>
					 </h:inputText>

					 <f:verbatim>Grid Spacing (km):</f:verbatim>
					 <h:inputText id="hgmGridSpacing" required="true" value="#{HazusGadgetBean.gridSpacing}" title="Please provide a grid spacing value between 0.01-1.0">
						<f:validateDoubleRange minimum="0.01" maximum="1.0"/>
					 </h:inputText>
				  </h:panelGrid>
				  <h:commandButton id="RunHazusAction" 
										 value="Run Hazus" 
										 disabled="true"
										 action="#{HazusGadgetBean.invokeHazusService}"/>
				  
				</h:panelGroup>
			 </h:panelGrid>
		  </h:form>
		</h:panelGrid>
	 </f:view>
	 <script src="@host.base.url@@artifactId@/scripts/hazusgadget.js"></script>			 
	 <script>
		var mapDiv=document.getElementById("mapDiv");
		var actionButton=document.getElementById("RunHazusForm:RunHazusAction");
		var lat0=document.getElementById("RunHazusForm:hgmlat0");
		var lat1=document.getElementById("RunHazusForm:hgmlat1");
		var lat2=document.getElementById("RunHazusForm:hgmlat2");
		var lat3=document.getElementById("RunHazusForm:hgmlat3");

		var lon0=document.getElementById("RunHazusForm:hgmlon0");
		var lon1=document.getElementById("RunHazusForm:hgmlon1");
		var lon2=document.getElementById("RunHazusForm:hgmlon2");
		var lon3=document.getElementById("RunHazusForm:hgmlon3");
		
		hazusgadget.createMap(mapDiv);
		hazusgadget.setupSelectionBox(actionButton, lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3);
	 </script>
  </body>
</html>