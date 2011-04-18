<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%> 
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%> 
<html>
<head>
  <!-- Replace this with your google map key -->
  <script
		src="http://maps.google.com/maps?file=api&amp;v=2amp;key=put.google.map.key.here"
		type="text/javascript"></script>
  
</head>
<body>    
  <f:view>	
	 <h:form id="hitTheButtonform">
		<f:verbatim>
		  QuakeSim's elastic forward modeling code calculates surface deformations based on
		  geometric fault models.  Click a fault to see a sample input. 
		  <p/>
		</f:verbatim>
		<f:verbatim>
		  <b>Click here it to run a simulation:</b>
		</f:verbatim>
		<h:commandButton id="DislocAnonProjectInit"
							  value="Create Sample Project"
							  action="#{DislocBean2.NewProjectThenEditProject}"/>
		<f:verbatim>
		  <div id="projectMapDiv" style="width: 800px; height: 600px;"></div>
		</f:verbatim>
		<script type="text/javascript">
		  var projectMapDiv=new GMap2(document.getElementById("projectMapDiv"));
		  var projectFaultKml="@host.base.url@@artifactId@/QuakeTables_CGS_2002.kml";
		
		  projectMapDiv.addMapType(G_HYBRID_MAP);
		  projectMapDiv.addMapType(G_PHYSICAL_MAP);
		  projectMapDiv.addMapType(G_SATELLITE_MAP);
		  
		  projectMapDiv.setMapType(G_PHYSICAL_MAP);
		  
		  projectMapDiv.addControl(new GSmallMapControl());
		  projectMapDiv.addControl(new GMapTypeControl());
		  projectMapDiv.setCenter(new GLatLng(37.0, -119),6);
		  
		  //Decorate the map with the KML.
		  var faultOverlay=new GGeoXml(projectFaultKml);
		  projectMapDiv.addOverlay(faultOverlay);
		</script>
	 </h:form>	 
  </f:view>
</body>
</html>
