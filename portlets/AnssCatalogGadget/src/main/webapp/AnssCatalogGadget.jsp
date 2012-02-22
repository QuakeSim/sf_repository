<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<html>
  <head>
	 <link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/styles/quakesim_style.css"/>
	 <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
	 <script type="text/javascript" src="https://www.google.com/jsapi?key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBToCwzmKPcju0WT8bJ7uVS8tITbtRTTsrqBp0FU9RF-p-xe-lBo6_wm4A"></script>
	 <%--gf4 key: ABQIAAAAxOZ1VuCkrWUtft6jtubycBToCwzmKPcju0WT8bJ7uVS8tITbtRTTsrqBp0FU9RF-p-xe-lBo6_wm4A--%>
	 <%--Localhost key is ABQIAAAAxOZ1VuCkrWUtft6jtubycBRHVVQ-3gUyVPTBA35K-1FKDOM5_hRFZSpddzaJbaPYJ4oXfR2X8O_0Jg--%>
	 <script src="@host.base.url@@artifactId@/scripts/anssgadget.js"></script>			 
  </head>
  <body>
	 <f:view>
		<h:messages id="acgMessagesLoadProject" 
						showDetail="true"
						showSummary="true"
						errorStyle="color: red"/>	 
		
		<h:panelGrid id="acgMainPanel" columns="1" columnClasses="alignTopFixWidth">
		  <f:verbatim>
			 Click the map to select your region of interest, then click "Fetch ANSS Catalog".  This service
			 harvests data from <a target="blank" href="http://www.ncedc.org/anss/catalog-search.html">http://www.ncedc.org/anss/catalog-search.html</a>
		  </f:verbatim>
		  <h:panelGrid id="anssgadgetlayoutgrid" columns="2" columnClasses="alignTop,alignTop">
			 <h:panelGroup id="anssgadgetleftpanel">
				<f:verbatim>
				  <div id="mapDiv" style="width: 600px; height: 500px;"></div>
				</f:verbatim>
			 </h:panelGroup>
			 <h:panelGroup id="anssgridrightpanel">
				<h:inputHidden id="acglat0" value=""/>
				<h:inputHidden id="acglon0" value=""/>
				<h:inputHidden id="acglat1" value=""/>
				<h:inputHidden id="acglon1" value=""/>
				<h:inputHidden id="acglat2" value=""/>
				<h:inputHidden id="acglon2" value=""/>
				<h:inputHidden id="acglat3" value=""/>
				<h:inputHidden id="acglon3" value=""/>
				
				<f:verbatim>
				  <b>Bounding Box:</b> 
				</f:verbatim>
				
				<%-- Retain this for draggable placemarks in future versions.
				<f:verbatim>
				  <table>
					 <tr>
						<td valign="top"><b>Bounding Box:</b></td>
						<td valign="top"><div id="acgBBoxDiv">None selected, will return for the entire earth by default. Click the map to select a specific bounding box to limit queries.</div></td>
					 </tr>
				  </table>
				</f:verbatim>
				--%>
				<h:panelGrid id="acgBBoxStuff" columns="2">
				  <f:verbatim>Min Lat:</f:verbatim>
				  <h:inputText id="minlatbbox" value="31.0"/>
				  
				  <f:verbatim>Min Lon:</f:verbatim>
				  <h:inputText id="minlonbbox" value="-120.0"/>
				  
				  <f:verbatim>Max Lat:</f:verbatim>
				  <h:inputText id="maxlatbbox" value="35.0"/>
				  
				  <f:verbatim>Max Lon:</f:verbatim>
				  <h:inputText id="maxlonbbox" value="-116.0"/>
				  
				</h:panelGrid>
				
				<h:panelGrid id="acgStuff" columns="2">
				  <f:verbatim>Starting Date:</f:verbatim>
				  <h:inputText id="acgMinDate" required="true" value="2002/01/01" title="Provide a starting date in the format yyyy/mm/dd">
				  </h:inputText>
				  <%-- Will default to 00:00:00 for now --%>
				  <%--
				  <f:verbatim>Starting Time:</f:verbatim>
				  <h:inputText id="acgMinTime" required="true" value="00:00:00" title="Provide a starting time in the format hh:mm:ss">
				  </h:inputText>
				  --%>
				  
				  <f:verbatim>Ending Date:</f:verbatim>
				  <h:inputText id="acgMaxDate" required="true" value="2011/06/01" title="Provide an ending date in the format yyyy/mm/dd">
				  </h:inputText>
				  
				  <%-- Will default to 00:00:00 for now --%>
				  <%--
				  <f:verbatim>Ending Time:</f:verbatim>
				  <h:inputText id="acgMaxTime" required="true" value="00:00:00" title="Provide an ending time in the format hh:mm:ss">
				  </h:inputText>
				  --%>
				  <f:verbatim>Minimum Magnitude:</f:verbatim>
				  <h:inputText id="acgMinMagnitude" required="true" value="4.0" title="Please provide a magnitude value between 3-10">
				  </h:inputText>
				  
				  <f:verbatim>Maximum Magnitude:</f:verbatim>
				  <h:inputText id="acgMaxMagnitude" required="true" value="10.0" title="Please provide a magnitude value between 3-10">
				  </h:inputText>
				</h:panelGrid>
				<f:verbatim>
				  <button id="RunAnssAction" onclick='anssgadget.submitMapRequest(minmag,maxmag,mindate,maxdate,minlat,minlon,maxlat,maxlon)'>
					 Fetch ANSS Catalog
				  </button>
				</f:verbatim>
				<f:verbatim>
				  <hr/>
				</f:verbatim>
				<h:panelGrid id="acgOutputPanelGrid" columns="2">
				  <f:verbatim><b>Status:</b><span id="acgResultKml">Unsubmitted</span></f:verbatim>
				</h:panelGrid>
			 </h:panelGroup>
		  </h:panelGrid>
		</h:panelGrid>
	 </f:view>
	 <script src="http://code.jquery.com/jquery-latest.js"></script>
	 <script>
		$(function() {
		$("#acgMinDate").datepicker({dateFormat:'yy/mm/dd',changeMonth: true, changeYear: true});
		});
		$(function() {
		$("#acgMaxDate").datepicker({dateFormat:'yy/mm/dd'});
		});
	 </script>
	 <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.16/jquery-ui.min.js" type="text/javascript"></script>
	 <script>
		var mapDiv=document.getElementById("mapDiv");
		var actionButton=document.getElementById("RunAnssForm:RunAnssAction");
		var lat0=document.getElementById("RunAnssForm:acglat0");
		var lat1=document.getElementById("RunAnssForm:acglat1");
		var lat2=document.getElementById("RunAnssForm:acglat2");
		var lat3=document.getElementById("RunAnssForm:acglat3");
		
		var lon0=document.getElementById("RunAnssForm:acglon0");
		var lon1=document.getElementById("RunAnssForm:acglon1");
		var lon2=document.getElementById("RunAnssForm:acglon2");
		var lon3=document.getElementById("RunAnssForm:acglon3");

		var bboxDiv=document.getElementById("acgBBoxDiv");

		var minmag=document.getElementById("acgMinMagnitude");
		var maxmag=document.getElementById("acgMaxMagnitude");
		var mindate=document.getElementById("acgMinDate");
		var mintime=document.getElementById("acgMinTime");
		var maxdate=document.getElementById("acgMaxDate");
		var maxtime=document.getElementById("acgMaxTime");
		var minlat=document.getElementById("minlatbbox");
		var minlon=document.getElementById("minlonbbox");
		var maxlat=document.getElementById("maxlatbbox");
		var maxlon=document.getElementById("maxlonbbox");

		var resultKmlDiv=document.getElementById("acgResultKml");

		anssgadget.createMap(mapDiv);
		//		anssgadget.setupSelectionBox(lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3, bboxDiv);
	 </script>
  </body>
</html>
