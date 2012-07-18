<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ page import="java.util.*, java.io.*, java.util.*, java.net.URL, java.lang.*, org.dom4j.*, org.dom4j.io.*, cgl.quakesim.disloc.*, javax.faces.context.ExternalContext, javax.servlet.http.HttpServletRequest, javax.portlet.PortletRequest, javax.faces.context.FacesContext, javax.faces.model.SelectItem, com.db4o.*"%>

<html>
 <head>
 <style>
	.alignTop {
	vertical-align:top;
	}
	
	.header2 {
	font-family: Arial, sans-serif;
	font-size: 18pt;
	font-weight: bold;
	}
	
	.display-toggle-on {
	background-image: url(/RssDisloc3/images/demo-spindown-open.gif);
	background-repeat: no-repeat;
	background-position: left center;
	padding-left:10px;
	}

	.display-toggle-off {
	background-image: url(/RssDisloc3/images/demo-spindown-closed.gif);
	background-repeat: no-repeat;
	background-position: left center;
	padding-left:10px;
	}
 </style>

	<title>RSS Disloc Results</title>
	<link rel="stylesheet" type="text/css" href="http://quakesim-iu.appspot.com/stylesheets/quakesim_style.css"/>
   <link rel="stylesheet" type="text/css" href="http://quakesim-iu.appspot.com/stylesheets/jquery.treeview.css"/>
	 <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
	 <script type="text/javascript" src="http://quakesim-iu.appspot.com/scripts/jquery.cookie.js"></script>
	 <script type="text/javascript" src="http://quakesim-iu.appspot.com/scripts/jquery.treeview.js"></script>

	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
 </head>
 
 <body onload="myInit()">
	 	
	<f:view>
	  
	  <h:outputText id="abdv1" styleClass="header2" value="Disloc Results Map"/>   
	  <f:verbatim>
		 <p>
		 <a class="display-toggle-on" id="ToggleView" href="#">View/Hide Instructions</a>
		 <div id="ViewInstructions">
		 This map shows results of surface deformation calculations for earthquakes
		 from the USGS RSS feed for M>5. See <a href="http://earthquake.usgs.gov/earthquakes/catalogs/7day-M5.xml">http://earthquake.usgs.gov/earthquakes/catalogs/7day-M5.xml</a>.</p>  
		 <p>Four different scenarios are calculated for each event.  
		 <ol>
			<li>Dip Angle=90 degrees, Strike Angle=0 degrees, strike slip, no dip slip</li>
			<li>Dip Angle=90 degrees, Strike Angle=45 degrees, strike slip, no dip slip</li>
			<li>Dip Angle=45 degrees, Strike Angle=0 degrees, dip slip, no strike slip</li>
			<li>Dip Angle=45 degrees, Strike Angle=90 degrees, dip slip, no strike slip</li>
		 </ol>
		 Interferograms are calculated with elevation angle=60, azimuth=0, and frequency 1.26 GHz. 
		 <p><b>Usage Instructions</b>
		 <ul>
			<li>Click the "+" or "-" icon to expand or contract the listings for an event.
			<li>Click the earthquake scenario link to go its location. </li>
			<li>Click the checkbox next to "InSAR Plot" or "Surface Displacement" to toggle results display on/off.</li>
			<li>Click the "InSAR Plot" or "Surface Diplacement" links to download the source KML.
		 </ul>
	  </div>
	</p>
	  </f:verbatim>
	  <script>
		 //This is a script to toggle the instructions' display
		 $('#ToggleView').click(function() {
           if($('#ViewInstructions').css('display')=='none') {
		        $('#ToggleView').removeClass('display-toggle-off').addClass('display-toggle-on');
		     }
		     else {
		        $('#ToggleView').removeClass('display-toggle-on').addClass('display-toggle-off');
		     }
		     $('#ViewInstructions').toggle();

	    });
	  </script>
	  <h:form id="refreshPage1">
		 <h:commandLink id="lrilehdk239" action="disloc-this">
			<h:outputText id="feo0re0" value="Refresh Page" />
		 </h:commandLink>
	  </h:form>
	  
	  <h:form id="RssFaultsMap">
		 <h:inputHidden id="faultName" value=""/> 
		 
		 <h:inputHidden id="projectsource" value="#{DislocBean2.projectsource}"/>   
		 <h:inputHidden id="faultlistsize" value="#{DislocBean2.myFaultsForProjectListsize}"/>
		 <h:panelGrid id="gridforbutton" columns="1" border="0" style="vertical-align:top;">
		 </h:panelGrid>
		 <%
	 //<![CDATA[
	 //This is some crappy code for getting the disloc bean object.
	 //Needs to be improved and is probably conceptually flawed.
		 ExternalContext context = null;
		 FacesContext facesContext=FacesContext.getCurrentInstance();
		 try {
		 context=facesContext.getExternalContext();
		 }
		 catch(Exception ex) {
		 out.println(ex.getMessage());
		 }
		 Object requestObj=null;
		 requestObj=context.getRequest();
		 
		 DislocBean dsb = null;
		 
		 if(requestObj instanceof PortletRequest) {
		 dsb = (DislocBean)((PortletRequest)requestObj).getPortletSession().getAttribute("DislocBean2");
		 }
		 else if(requestObj instanceof HttpServletRequest) {
		 dsb = (DislocBean)request.getSession().getAttribute("DislocBean2");
		 }
		 //And this code calls the function that conditionally runs Disloc.
		 HashMap hm = dsb.getDbProjectNameList();
		 //]]>
		 %>
		 
		 <h:panelGrid id="faultMapsideGrid" columns="1" border="1">
			<f:verbatim>
			  <div id="faultMap" style="width: 600px; height: 600px;"></div>
			</f:verbatim> 
			<f:verbatim>
			  <div id="navbar" style="width: 600px; height: 300px; overflow:auto;"></div>
			</f:verbatim>
		 </h:panelGrid>
		 <f:verbatim>
			
			<script src="@host.base.url@@artifactId@/scripts/rssdisloc.js"></script>			 
			<script type="text/javascript">
			  
			  //<![CDATA[
			//Any "onload" operations for subcomponent includes need to go here.
			function myInit() {
			//Script below initializes the tree view for the FaultMapPanelFrame sidebar.
			$("#browser").treeview({
			animated:"normal",
			persist: "cookie",
			collapsed: true
			});
			}
			
			//Set the fault map
			var faultMapDiv=document.getElementById('faultMap');
			rssdisloc.setMap(faultMapDiv);

			//This is the div for the navbar
			var navbar=document.getElementById('navbar');        
			rssdisloc.setNavbar(navbar);
			
			//]]> //Keep for formatting
		 </script>
		 </f:verbatim>
		 
	  </h:form>
	  <h:form id="refreshMe2">
		 <h:commandLink id="jdflkdjkjfd" action="disloc-this">
			<h:outputText id="kjdkjfdlkjdfldjk" value="Refresh Page" />
		 </h:commandLink>
	  </h:form>
	  
	</f:view>
 </body>
</html>
