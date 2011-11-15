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
 </style>

	<title>RSS Disloc Results</title>
	
	<link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/quakesim_style.css"/>
	<link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/jquery.treeview.css"/>
   <%--
	<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" type="text/javascript"></script>      
	--%>
	<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
	<script src="http://code.jquery.com/jquery-latest.js"></script>
	<script type="text/javascript" src="@host.base.url@@artifactId@/lib/jquery.cookie.js"></script>
	<script type="text/javascript" src="@host.base.url@@artifactId@/jquery.treeview.js"></script>
	<%--
	<script type="text/javascript" src="@host.base.url@@artifactId@/egeoxml_for_rssdisloc.js"></script>
	--%>
 </head>
 
 <body onload="myInit()">
	 	
	<f:view>
	  
	  <h:outputText id="abdv1" styleClass="header2" value="Disloc Results Map"/>   
	  <f:verbatim>
		 <p>
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
		 </p>
		 <p><b>Usage Instructions</b>
		 <ul>
			<li>Click the "+" or "-" icon to expand or contract the listings for an event.
			<li>Click the earthquake scenario link to go its location. </li>
			<li>Click the checkbox next to "InSAR Plot" or "Surface Displacement" to toggle results display on/off.</li>
			<li>Click the "InSAR Plot" or "Surface Diplacement" links to download the source KML.
		 </ul>
	  </f:verbatim>
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
				 <div id="faultMap" style="width: 900px; height: 600px;"></div>
			  </f:verbatim> 
			  <f:verbatim>
				 <div id="navbar" style="width: 900px; height: 150px; overflow:auto;"></div>
			  </f:verbatim>
			</h:panelGrid>
		 <f:verbatim>
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
        
			//--------------------------------------------------
			// Everything here should be a function later.
			//--------------------------------------------------			
			var infoWindowArray=new Array();
			var kmlDispLayerArray=new Array();
			var kmlInsarLayerArray=new Array();

         //This is the div for the navbar
         var navbar=document.getElementById('navbar');

			//Create the map
			var latlng=new google.maps.LatLng(33.3,-118.0);
			var mapOpts={zoom:7, center: latlng, mapTypeId: google.maps.MapTypeId.ROADMAP};
			var faultMap=new google.maps.Map(document.getElementById('faultMap'),mapOpts);

			//This is the XML file containing the results.
			var myM5List = $.ajax({type:"GET",url:"/overm5.kml",async:false,dataType:"xml"}).responseText;
			//Parse the list
			eventList=$.parseXML(myM5List);
         var eventListHtml=navbar.innerHTML+"<ul id='browser'>";
			//index1 is the scenario number.  index2 is the event index
			var index1=1; index2=0;
         $(eventList).find('Placemark').each(function(){
			    //We have to work around the poor formatting choices of the KML feed.
				 //The faults will be grouped in 4's, each with the same shortname.
				 if(index1==1) {
				 eventListHtml+="<li>"+$(this).find('shortName').text();
				 }
				 if(index1<5) {
				 //Next several lines should go in a helper function.
				 var description=$(this).find('description').text();
				 var content="<div style='font-family: Arial, sans-serif;font-size: small;width:300px'>"+description+"</div>";
				 //Note the coordinates are in lng,lat order.
				 var eventCoord=$(this).find('coordinates').text();
				 eventCoord=eventCoord.split(" ")[0];  //We only keep the first lat,lon coordinates
				 var eventLatLon=new google.maps.LatLng(eventCoord.split(",")[1],eventCoord.split(",")[0]);
				 var infoWindow=new google.maps.InfoWindow({content:content});
				 infoWindow.setPosition(eventLatLon);
				 infoWindowArray.push(infoWindow);
				 eventListHtml+="<ul><li><a id='eventInfoWindow_"+index2+"' onClick='popupInfoWindow(this)'>Scenario "+index1+"</a>";
				 eventListHtml+="<ul>"
             eventListHtml+="<li><a id='dislocOutput_"+index2+"' target='blank' href='"+$(this).find('DislocOutputURL').text()+"'>Surface Displacement Outputs (text)</a></li>";
				 
				 var kmlDispUrl=$(this).find('DisplacementKmlURL').text();
             kmlDispUrl=kmlDispUrl.replace(/^\s+|\s+$/g, '');
				 kmlDispOpts={map:null};
				 var kmlDispLayer=new google.maps.KmlLayer(kmlDispUrl,kmlDispOpts);
             kmlDispLayerArray.push(kmlDispLayer);
             eventListHtml+="<li>"
				              +"<input id='kmlDispLayer_"+index2+"' type='checkbox' onClick='toggleDispKml(this)'>"
				              +"<a id='"+index2+"' target='blank' href='"+kmlDispUrl+"'>Surface Displacement Plot</a></li>";

				 var kmlInsarUrl=$(this).find('InsarKmlURL').text();             
				 kmlInsarUrl=kmlInsarUrl.replace(/^\s+|\s+$/g, '');
				 kmlInsarOpts={map:null};
				 var kmlInsarLayer=new google.maps.KmlLayer(kmlInsarUrl,kmlInsarOpts);
				 kmlInsarLayerArray.push(kmlInsarLayer);
             eventListHtml+="<li>"
				              +"<input id='kmlInsarLayer_"+index2+"' type='checkbox' onClick='toggleInsarKml(this)'>"
				              +"<a id='"+index2+"' target='blank' href='"+kmlInsarUrl+"'>InSAR Plot</a></li>";
             eventListHtml+="</ul></li></ul>";  //Close the inner lists
				 index1++;
				 if(index1==5) index1=1;
				 }
				 index2++;
         });         
           //Close up the navbar's outermost ul.
           eventListHtml+="</ul>";

           navbar.innerHTML=eventListHtml;

			  //Finally, go to the most recent event
			  popupInfoWindow2();
			//--------------------------------------------------			
			
			//--------------------------------------------------
			// This function depends on a 'global' array of info windows.  The caller
			// is the HTMLElement of the corresponding element that generated the call.
			// Typically this method is called by an onClick event of an anchor tag, and
			// we expect the id attribute of the tag to correspond to the infowWindow array 
			// entry that we want.
         //
         // Note also that all of these assume the id attribute comes first (so use [0]).
         // This is fragile. Also the ID extraction depends on convention.
			//--------------------------------------------------
			function popupInfoWindow(caller) {
			  var callerId=caller.attributes[0];
			  var index=callerId.value.split("_")[1];
			  var infoWindow=infoWindowArray[index];
			  infoWindow.open(faultMap);
			}

			function toggleDispKml(caller) {
			  var callerId=caller.attributes[0];
			  var index=callerId.value.split("_")[1];
			  var kmlDispLayer=kmlDispLayerArray[index];
			  var checkbox=document.getElementById(callerId.value);
			  if(checkbox.checked==true) {
			      kmlDispLayer.setMap(faultMap);
				}
				else {
			      kmlDispLayer.setMap(null);
            }
			}

			function toggleInsarKml(caller) {
			  var callerId=caller.attributes[0];
			  var index=callerId.value.split("_")[1];
			  var kmlInsarLayer=kmlInsarLayerArray[index];
			  var checkbox=document.getElementById(callerId.value);
			  if(checkbox.checked==true) {
			      kmlInsarLayer.setMap(faultMap);
				}
				else {
    			   kmlInsarLayer.setMap(null);
            }
			}

			//This function opens the most recent event's info window.
			function popupInfoWindow2() {
             var infoWindow=infoWindowArray[0];
				 infoWindow.open(faultMap);
			}

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
