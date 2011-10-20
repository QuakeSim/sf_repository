<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<body>
  <f:view>
	 <f:verbatim>
		<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
	 </f:verbatim>
	 <f:verbatim>
		Click the map to select your region of interest, then click "Run Hazus".
	 </f:verbatim>
	 <f:verbatim>
		<div id="mapDiv" style="width: 550px; height: 400px;"></div>
	 </f:verbatim>

	 <h:form id="RunHazusForm">
		<h:commandButton id="RunHazusAction" 
							  value="Run Hazus" 
							  disabled="true"
							  action="#{HazusGadgetBean.invokeHazusService}"/>
	 </h:form>
  </f:view>
  <script src="@host.base.url@@artifactId@/scripts/hazusgadget.js"></script>			 
  <script>
	 var mapDiv=document.getElementById("mapDiv");
	 var actionButton=document.getElementById("RunHazusForm:RunHazusAction");
	 hazusgadget.createMap(mapDiv);
	 hazusgadget.setupSelectionBox(actionButton);
  </script>
</body>