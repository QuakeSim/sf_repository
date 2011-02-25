<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="java.util.*, java.io.*, java.util.*, java.net.URL, java.lang.*, org.dom4j.*, org.dom4j.io.*, cgl.sensorgrid.common.*, cgl.sensorgrid.sopac.gps.GetStationsRSS,cgl.sensorgrid.gui.google.MapBean, cgl.quakesim.simplex.*, javax.faces.context.ExternalContext, javax.servlet.http.HttpServletRequest, javax.portlet.PortletRequest, javax.faces.context.FacesContext"%>

<%
//Code below probably belongs in GPSMapPanel (removed)
%>

<!-- These styles should be in a separate stylesheet file -->
<style type="text/css">
.alignTop {
	vertical-align: top;
}

.header2 {
	font-family: Arial, sans-serif;
	font-size: 18pt;
	font-weight: bold;
}
</style>

<head>
<link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/quakesim_style.css">

<title>Edit Project</title>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" type="text/javascript"></script>
    <!-- These are needed by the fault map panel and are repeated there.  Remove redundancies.-->
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
    <script type="text/javascript" src="@host.base.url@@artifactId@/lib/jquery.cookie.js"></script>
    <script type="text/javascript" src="@host.base.url@@artifactId@/jquery.treeview.js"></script>
</head>

<body onload="myInit()" onunload="GUnload()">
  <script type="text/javascript">
	 //<![CDATA[

	 //Any "onload" operations for subcomponent includes need to go here.
	 function myInit() {
	 //Script below initializes the tree view for the FaultMapPanelFrame sidebar.
  	$("#browser").treeview({
	animated:"normal",
	persist: "cookie"
	});
	
	//This initializes the UNAVCO map
	initializeUnavcoMap();
	}
	//]]
</script>
<script type="text/javascript">
  //<![CDATA[
  //--------------------------------------------------
  //Following two math functions should probably go into a separate script file.
  //--------------------------------------------------
  //This is used to calculate the length and strike angle.
  function calculatelength(){
  var lonStart = document.getElementById("Faultform:FaultLonStarts");
  var lonEnd = document.getElementById("Faultform:FaultLonEnds");
  var latStart = document.getElementById("Faultform:FaultLatStarts");
  var latEnd = document.getElementById("Faultform:FaultLatEnds");
  
  var length = document.getElementById("Faultform:FaultLength");
  var strike = document.getElementById("Faultform:FaultStrikeAngle");
  
  if ((latStart.value.length == 0) || (latStart.value == null)) { alert("Fault Origin Latitude is empty!"); }
  else if ((lonStart.value.length == 0) || (lonStart.value == null)) { alert("Fault Origin Longititude is empty!"); }
  else if ((latEnd.value.length == 0) || (latEnd.value == null)) { alert("Fault End Latitude is empty!"); }
  else if ((lonEnd.value.length == 0) || (lonEnd.value == null)) { alert("Fault End Longitude is empty!"); }
  else {
  var d2r = Math.acos(-1.0) / 180.0;
  var flatten=1.0/298.247;
  //var theFactor = d2r* Math.cos(d2r * latStart.value)
  //        * 6378.139 * (1.0 - Math.sin(d2r * lonStart.value) * Math.sin(d2r * lonStart.value) * flatten);
  var theFactor = d2r* Math.cos(d2r * latStart.value) * 6378.139 * (1.0 - Math.sin(d2r * latStart.value) * Math.sin(d2r * latStart.value) * flatten);
  
  var x=(lonEnd.value-lonStart.value)*theFactor;
  var y=(latEnd.value-latStart.value)*111.32;
  var lengthVal=Math.sqrt(x*x+y*y);
  length.value=Math.round(lengthVal*1000)/1000;
  
  var strikeValue=Math.atan2(x,y)/d2r;
  if (strikeValue < 0) { strikeVaule = strikeValue + 360; }
  strike.value=Math.round(strikeValue*1000)/1000;
  }
  }
  
  //This calculates the endpoint, given other parameters.
  function calculateendpoint(){
  // alert("debug");
  var lonStart = document.getElementById("Faultform:FaultLonStarts");
  var lonEnd = document.getElementById("Faultform:FaultLonEnds");
  var latStart = document.getElementById("Faultform:FaultLatStarts");
  var latEnd = document.getElementById("Faultform:FaultLatEnds");
  
  var length = document.getElementById("Faultform:FaultLength");
  var strike = document.getElementById("Faultform:FaultStrikeAngle");
  
  if ((latStart.value.length == 0) || (latStart.value == null)) { alert("Fault Origin Latitude is empty!"); }
  else if ((lonStart.value.length == 0) || (lonStart.value == null)) { alert("Fault Origin Longititude is empty!"); }
  else if ((length.value.length == 0) || (length.value == null)) { alert("Length is empty!"); }
  else if ((strike.value.length == 0) || (strike.value == null)) { alert("Strike Angle is empty!"); }
  else {
  
  var d2r = Math.acos(-1.0) / 180.0;
  var flatten=1.0/298.247;
  var theFactor = d2r* Math.cos(d2r * latStart.value) * 6378.139 * (1.0 - Math.sin(d2r * latStart.value) * Math.sin(d2r * latStart.value) * flatten);
  
  //Massive math calculation starts here. 
  if (strike.value == 0) {
  var answer = confirm("Strike Angle is 0, Are you sure?");
  if (answer) { xval = 0; yval = length.value;}
  else { return; }
  }
  else if (strike.value == 90) { xval = length.value; yval = 0;}
  else if (strike.value == 180) { xval = 0; yval = (-1.0) * length.value;}
  else if (strike.value == 270) { xval = (-1.0) * length.value; yval = 0;}
  else {
  var sval = 90 - strike.value;
  var thetan = Math.tan(sval*d2r);
  var xval = length.value/Math.sqrt(1 + thetan*thetan);
  var yval = Math.sqrt(length.value*length.value - xval*xval);
  
  if (strike.value > 0 && strike.value < 90) { xval = xval*1.0; yval = yval*1.0;}
  else if (strike.value > 90 && strike.value < 180) { xval = xval*1.0; yval = yval* (-1.0);}
  else if (strike.value > 180 && strike.value < 270) { xval = xval*(-1.0); yval = yval*(-1.0);}
  else if (strike.value > 270 && strike.value < 360) { xval = xval*(-1.0); yval = yval*1.0;}
  }
  
  lonEnd.value = (xval*1.0)/theFactor + (lonStart.value*1.0);
  latEnd.value = yval/111.32 + (latStart.value*1.0);
  
  lonEnd.value = Math.round(lonEnd.value*100)/100.0;
  latEnd.value = Math.round(latEnd.value*100)/100.0;
  }
  }
  //--------------------------------------------------
  
  //--------------------------------------------------
  // These are selection control functions. Should be moved to
  // a separate script file.  May also want to verify that they are 
  // still needed.
  //--------------------------------------------------
  function selectOne(form , button) {
  turnOffRadioForForm(form);
  button.checked = true;
  }
  
  function turnOffRadioForForm(form) {
  for(var i=0; i<form.elements.length; i++)
  {
  form.elements[i].checked = false;
  
  }
  }
  
  function dataTableSelectOneRadio(radio) {
  var id = radio.name.substring(radio.name.lastIndexOf(':'));
  var el = radio.form.elements;
  // alert (el.length);
  for (var i = 0; i < el.length; i++) {
		if (el[i].name.substring(el[i].name.lastIndexOf(':')) == id) {
		// alert (el[i].checked)
		el[i].checked = false;
		el[i].checked = false;
		}
		}
		radio.checked = true;
		}       
		
		
		function printNetworkColors (array) {
		var html = "<table border='0'><tr><td><b>Network</b></td><td nowrap><b>Icon Color<b></td></tr>";
		
		var row;
		for (row = 0; row < array.length; ++ row)
		{
		html = html + " <tr>";
		var col;
		for (col = 0; col < array [row] . length; ++ col){
		if(col==0)
		html = html + "  <td>" + array [row] [col] + "</td>";
		if(col==1)
		html = html + "  <td align='center'><img border=0 src=" + array [row] [col] + "></td>";
		
		}
		html = html + " </tr>";
		}
		html = html + "</table>";
		var idiv = window.document.getElementById("networksDiv");
		idiv.innerHTML = html;
		}

		
		// Needed for Firefox 2.0 compatibility
		function getScrolling() {
		var x = 0; var y = 0;
		if (document.body && document.body.scrollLeft && !isNaN(document.body.scrollLeft)) {
		x = document.body.scrollLeft;
		} else if (window.pageXOffset && !isNaN(window.pageXOffset)) {
		x = window.pageXOffset;
		}
		if (document.body && document.body.scrollTop && !isNaN(document.body.scrollTop)) {
		y = document.body.scrollTop;
		} else if (window.pageYOffset && !isNaN(window.pageYOffset)) {
		y = window.pageYOffset;
		}
		return x + "," + y;
		}
		//]]
	 </script>
	 <f:view>
		<h:messages id="simplexMessagesDynamicFault" 
						showDetail="true"
						showSummary="true"
					  errorStyle="color: red"/>
		
		<h:outputText id="lkdrq1" styleClass="header2"
						  value="Project Component Manager" />
		<%-- <h:inputHidden id="faultKmlUrl" value="#{SimplexBean.faultKmlUrl}" /> --%>
		<h:outputText id="lkdrq2" escape="false" value="<br>"/>
		<h:outputText id="lkdrq2" escape="false"
						  value="You must provide at least one fault and one observation point before you can run Simplex" />
		<%/* This is the main grid container */%>
		<h:panelGrid id="EditProject" 
						 columnClasses="alignTop,alignTop" 
						 columns="1" 
						 border="0">
		  <%@include file="DashboardPanel.jsp"%>
		  <%@include file="ObservationPanel.jsp"%>
		  <%@include file="GPSMapPanel.jsp"%>
		  <%@include file="ObsvListPanel.jsp"%>
		  <%@include file="FaultParamsPanel.jsp"%>
		  <%@include file="FaultSelectionPanel.jsp"%>
		  <%@include file="FaultNameSearchPanel.jsp"%>
		  <%@include file="FaultLatLonSearchPanel.jsp"%>
		  <%@include file="FaultAuthorSearchPanel.jsp"%>
		  <%@include file="FaultSearchResultsPanel.jsp"%>
		  <%@include file="FaultMapPanelFrame.jsp"%>
		  <%@include file="UnavcoGPSMapPanel.jsp"%>
		</h:panelGrid>
		
		<%@include file="ProjectComponentsPanel.jsp"%>
		
		<h:outputText id="simplexHorizontalLine" escape="false" value="<hr/>"/>
	<h:form id="dflelerkljk186">
	  <h:commandLink id="dflelerkljk187" action="Simplex2-back">
		 <h:outputText id="Simplex3NavigationLink" value="#{SimplexBean.codeName} Main Menu" />
	  </h:commandLink>
	</h:form>
	
 </f:view>
 
</body>
</html>
