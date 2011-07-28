<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%> 
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%> 

<html>
  <head>
  <head> 
	 <link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/quakesim_style.css"/>	 
	 <!--Google and related APIs are imported here -->
	 <!--
	 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" 
				type="text/javascript"></script>
	 -->
	 <script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script> 
  </head>
  <body>
	 <div id="InSARMap" style="width: 800px; height: 600px;"></div>
	 
	 <script src="script/sarselect.js">
	 $(function() {
		  sarselect.setMap("InSARMap");
	 });
	 </script>
  </body>
</html>