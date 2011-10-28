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
				  <h:inputHidden id="hgmlat0" value="#{HazusGadgetBean.lat0}"/>
				  <h:inputHidden id="hgmlon0" value="#{HazusGadgetBean.lon0}"/>
				  <h:inputHidden id="hgmlat1" value="#{HazusGadgetBean.lat1}"/>
				  <h:inputHidden id="hgmlon1" value="#{HazusGadgetBean.lon1}"/>
				  <h:inputHidden id="hgmlat2" value="#{HazusGadgetBean.lat2}"/>
				  <h:inputHidden id="hgmlon2" value="#{HazusGadgetBean.lon2}"/>
				  <h:inputHidden id="hgmlat3" value="#{HazusGadgetBean.lat3}"/>
				  <h:inputHidden id="hgmlon3" value="#{HazusGadgetBean.lon3}"/>
				  <f:verbatim>
					 <table>
						<tr>
						  <td valign="top"><b>Bounding Box:</b></td>
						  <td valign="top"><div id="hgmBBoxDiv">None selected.  Click the map.</div></td>
						</tr>
					 </table>
				  </f:verbatim>
				  
				  <h:panelGrid id="hgmFaultInputs" columns="2">
					 <f:verbatim>Fault Rake (deg):</f:verbatim>
					 <h:inputText id="hgmFaultRake" 
									  required="true"
									  value="#{HazusGadgetBean.faultRake}">
						<f:validateDoubleRange minimum="-180.0" maximum="180.0"/>
					 </h:inputText>

					 <f:verbatim>Fault Depth:</f:verbatim>
					 <h:inputText id="hgmFaultDepth" 
									  required="true"
									  value="#{HazusGadgetBean.faultDepth}">
						<f:validateDoubleRange maximum="0.0"/>					 
					 </h:inputText>

					 <f:verbatim>Magnitude:</f:verbatim>
					 <h:inputText id="hgmMagnitude" required="true" value="#{HazusGadgetBean.magnitude}" title="Please provide a magnitude value between 5-10">
						<f:validateDoubleRange minimum="5.0" maximum="10.0"/>
					 </h:inputText>

					 <f:verbatim>Grid Spacing (deg):</f:verbatim>
					 <h:inputText id="hgmGridSpacing" required="true" value="#{HazusGadgetBean.gridSpacing}" title="Please provide a grid spacing value between 0.01-1.0">
						<f:validateDoubleRange minimum="0.01" maximum="1.0"/>
					 </h:inputText>
				  </h:panelGrid>
				  <h:commandButton id="RunHazusAction" 
										 value="Run Hazus" 
										 disabled="true"
										 action="#{HazusGadgetBean.invokeHazusService}"/>
				  <f:verbatim>
					 <hr/>
				  </f:verbatim>
				  <h:panelGrid id="hgmOutputPanelGrid" columns="2">
					 <f:verbatim><b>Results:</b></f:verbatim>
					 <h:outputText rendered="#{(empty HazusGadgetBean.resultsLink)}"
										value="No results.  Run the simulation first."/>
					 <h:outputLink rendered="#{!(empty HazusGadgetBean.resultsLink)}"
										id="hgmOutputLink" 
										target="null"
										value="#{HazusGadgetBean.resultsLink}">
						<h:outputText id="hgmOutputText" 
										  value="#{HazusGadgetBean.resultsLink}"/>
					 </h:outputLink>
				  </h:panelGrid>
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

		var bboxDiv=document.getElementById("hgmBBoxDiv");

		hazusgadget.createMap(mapDiv);
		hazusgadget.setupSelectionBox(actionButton, lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3, bboxDiv);
	 </script>
  </body>
</html>
