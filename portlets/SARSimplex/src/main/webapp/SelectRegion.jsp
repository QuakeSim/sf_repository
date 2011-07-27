<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%> 
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%> 

<html>
  <head>
  <head> 
	 <link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/quakesim_style.css"/>	 
	 <!--Google and related APIs are imported here -->
	 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" 
				type="text/javascript"></script>
  </head>
  <body>
	 <div id="InSARMap" style="width: 600px; height: 400px;"></div>
	 <script>
		var insarMap=new GMap2(document.getElementById("InSARMap"));
		insarMap.addMapType(G_HYBRID_MAP);
		insarMap.addMapType(G_PHYSICAL_MAP);
		insarMap.addMapType(G_SATELLITE_MAP);
		
		insarMap.setMapType(G_PHYSICAL_MAP);
		
		insarMap.addControl(new GSmallMapControl());
		insarMap.addControl(new GMapTypeControl());
		insarMap.setCenter(new GLatLng(37.0, -119),6);
		
		var projectSARKml="http://gf19.ucs.indiana.edu:9898/uavsar-data/SanAnd_26501_09083-010_10028-000_0174d_s01_L090_01/SanAnd_26501_09083-010_10028-000_0174d_s01_L090HH_01.amp2.kml";
		var sarOverlay=new GGeoXml(projectSARKml);
		insarMap.addOverlay(sarOverlay);
		sarOverlay.gotoDefaultViewport(insarMap);
		
	 </script>
  </body>
</html>