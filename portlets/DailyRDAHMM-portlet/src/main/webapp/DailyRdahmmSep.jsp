<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<%@page import="java.util.*, java.net.URL, java.io.*, java.lang.*, org.dom4j.*, cgl.sensorgrid.common.*, org.dom4j.io.*"%>

<jsp:scriptlet>
  //TODO: We need a better way of handling this.

   //--------------------------------------------------
   // This loads the content of the windowcontent.htm file, which embeds the RDAHMM flash '
	// plotting object. This is 
	// read into the strTabContent String, which is passed to markerWinHtmlStr below.
   //--------------------------------------------------
	String strTabContent = new String();
	java.io.BufferedInputStream bis=null;
	try{
		bis =new java.io.BufferedInputStream(new java.io.FileInputStream(config.getServletContext().getRealPath("form/windowcontent.htm")));
		BufferedReader br = new BufferedReader(new InputStreamReader(bis));
		String line = null;
		while ((line = br.readLine()) != null) {
			strTabContent = strTabContent+line;
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	finally {
		if (bis != null)bis.close();
	}


	//--------------------------------------------------
	// The code below sets various parameters that need to be determined at load time, based on
	// the value of the dataSource parameter provided.
   //
	// The dataSource has a value like jplFill, sopacFill, etc.  See the if block below.
	// This is a required parameter to render the page.
	//--------------------------------------------------
	String dataSource = request.getParameter("dataSource");
	if (dataSource == null) {
		dataSource = "sopacFill";
	}
	//These will all be overridden if not sopacFill.  The string xml.access.hostname will be 
	//overridden by Maven property filtering during deployment.  These values will be used 
	//far below when we get the results and parse them.
	String xmlUrl = "http://xml.access.hostname/daily_rdahmmexec/daily/SOPAC_FILL/station-status-change-SOPAC_FILL.xml";
	String xmlFileName = "station-status-change-SOPAC_FILL.xml";
	String contextGroup = "SOPAC GLOBK";
	String swfName = "dailyRdahmmPlotLLH.swf";
	double defSwLat = 31.2973;
	double defSwLng = -127.9907;
	double defNeLat = 41.8532;
	double defNeLng = -111.5552;
	if (dataSource.equalsIgnoreCase("jplFill")) {
		xmlUrl = "http://xml.access.hostname/daily_rdahmmexec/daily/JPL_FILL/station-status-change-JPL_FILL.xml";
		xmlFileName = "station-status-change-JPL_FILL.xml";
		contextGroup = "JPL GIPSY";
		swfName = "dailyRdahmmPlotLLH.swf";
		defSwLat = 31.2973;
		defSwLng = -127.9907;
		defNeLat = 41.8532;
		defNeLng = -111.5552;
	} else if (dataSource.equalsIgnoreCase("unavcoPboFill")) {
		xmlUrl = "http://xml.access.hostname/daily_rdahmmexec/daily/unavcoPboFill/station-status-change-unavcoPboFill.xml";
		xmlFileName = "station-status-change-unavcoPboFill.xml";
		contextGroup = "UNAVCO PBO";
		swfName = "dailyRdahmmPlotLlhDisp.swf";
		defSwLat = 36.9323;
		defSwLng = -125.9473;
		defNeLat = 46.7248;
		defNeLng = -109.5117;
	} else if (dataSource.equalsIgnoreCase("unavcoNucleusFill")) {
		xmlUrl = "http://xml.access.hostname/daily_rdahmmexec/daily/unavcoNucleusFill/station-status-change-unavcoNucleusFill.xml";
		xmlFileName = "station-status-change-unavcoNucleusFill.xml";
		contextGroup = "UNAVCO Nucleus";
		swfName = "dailyRdahmmPlotLlhDisp.swf";
		defSwLat = 38.8739;
		defSwLng = -128.8257;
		defNeLat = 48.3854;
		defNeLng = -112.3901;
	}

</jsp:scriptlet>

<html>
	<head>
   <!-- TODO: these javascript imports can be unified and moved lower down for faster rendering -->
	<script src="http://maps.google.com/maps/api/js?sensor=false" type="text/javascript"></script>
	<script src="http://local.hostname/DailyRDAHMM-portlet/NmapAPI.js" type="text/javascript"></script>
	<script src="http://local.hostname/DailyRDAHMM-portlet/dateUtil.js" type="text/javascript"></script>
	<script src="http://danvk.org/dygraphs/dygraph-combined.js" type="text/javascript"></script>
	</head>

	<body>
	<!-- Initiating the yui controls -->
	<script type="text/javascript" src="/yui_0.12.2/build/yahoo/yahoo.js"></script>
	<script type="text/javascript" src="/yui_0.12.2/build/event/event.js"></script>
	<script type="text/javascript" src="/yui_0.12.2/build/dom/dom.js"></script>
	<script type="text/javascript" src="/yui_0.12.2/build/calendar/calendar.js"></script>
	<script type="text/javascript" src="/yui_0.12.2/build/slider/slider.js"></script>
	<script type="text/javascript" src="/yui_0.12.2/build/yahoo-dom-event/yahoo-dom-event.js"></script>
	<script type="text/javascript" src="/yui_0.12.2/build/dragdrop/dragdrop-min.js"></script>
	<script type="text/javascript" src="/yui_0.12.2/build/slider/slider-min.js"></script>
	<link type="text/css" rel="stylesheet" href="/yui_0.12.2/build/calendar/assets/calendar.css"/>
 
   <!-- These style declarations should be moved to a separate CSS file. -->
	<style>
		#cal1Container {position:relative; width:160px;}
		#slider-bg {
			position: relative;
			background:url(/yui_0.12.2/build/slider/assets/horizBg.png) 5px 0;
			height:26px;
			width:693px;
		}
		#slider-thumb {
			position: absolute;
			top: 4px;
		}
	</style>

   <!-- These style declarations should be moved to a separate CSS file. -->
	<style>
		td      { font-size: 9pt;}
		.ooib { border-width: 1px; border-style: none solid solid; border-color: #CC3333; background-color: #EDEDED;}
		.ooih td { border-width: 1px; padding: 0 5; }
		.ooihj { color: #CC3333; background-color: #EDEDED; border-style: solid solid none; border-color: #CC3333; cursor: pointer}
		.ooihs { color: #6600CC; background-color: #ccccFF; border-style: solid; border-color: #6600CC #6600CC #CC3333; cursor: pointer}
		.ooihx { border-style: none none solid; border-color: #CC3333; }
	</style>

	<!-- Script should be moved to a separate JS file -->
	<script>
	//for manipulating the tabs
	function ghbq(td) {
		var tr = document.getElementById("tabRow");
		var ob = document.getElementById("obody").rows;
		for(var ii=0; ii < tr.cells.length-1; ii++){
			tr.cells[ii].className = (td.cellIndex==ii ? "ooihj":"ooihs");
			ob[ii].style.display = (td.cellIndex==ii ? "":"none");
		}
		if (td.cellIndex > 0 && scnPlotGraph == null) {
			scnPlotGraph = new Dygraph(document.getElementById("scnPlotDiv"), 
							scnJsiLink, 
							{
								colors:['#007FFF'],
								strokeWidth:0.7,
								pixelsPerXLabel:50,
								rightGap:2,
								stepPlot:true,
								fillGraph:true,
								fillAlpha:0.8
							});
		}
	}

	//TODO: switch this to JQuery for future maintainability.
	YAHOO.namespace("example.calendar");
	
	var slider;
	var slider_range = 665;
	var denotedDate = new Date();
	var modelStartDate = new Date(1994, 0, 1, 0, 0, 0);
	
	var tmpNoActOnCalSelect = false;
	//This function handles all date change and display events.
	function myShowDateHandler(type,args,obj) {
		if (tmpNoActOnCalSelect) {
			tmpNoActOnCalSelect = false;
			return;
		}
		var dates=args[0];
		var date=dates[0];
		var year=date[0],month=date[1],day=date[2];
		if (month[0] == '0')
			month = "" + month[1];
		if (day[0] == '0')
			day = "" + day[1];
		var showDate = year + "-" + month + "-" + day;
		var dateToShow = document.getElementById("dateText");
	 	if (showDate != dateToShow.value) {
			var tmpDate = getDateFromString(showDate);
			var today = new Date();
			if (tmpDate > today || tmpDate < modelStartDate) {
				alert("Please choose a date between " + getDateString(modelStartDate) + " and " + getDateString(today));
				return;
			}
			dateToShow.setAttribute("value",showDate);
			dateToShow.value = showDate;
			tmpNoActOnSlideChange = true;
			setSliderValByDate(tmpDate);
			overlayMarkers();
		}
	}
  
	function setSliderValByDate(theDate) {
		var today = new Date();
		slider.setValue( (theDate.getTime() - modelStartDate.getTime()) * slider_range / (today.getTime() - modelStartDate.getTime()) );
	}
  
	var tmpNoActOnSlideChange = false;
	function onSlideChange() {
		// if the slider changes because user presses "enter" in the date text box or click the calendar, don't change the date and do everything again
		if (tmpNoActOnSlideChange) {
			return;
		}
		var slider_val = slider.getValue();
		var endDate = new Date();
		var timeSinceStart = slider_val * (endDate.getTime() - modelStartDate.getTime()) / slider_range;
		denotedDate.setTime(modelStartDate.getTime() + timeSinceStart);
		var dateToShow = document.getElementById("dateText");
		var str = getDateString(denotedDate);
		dateToShow.setAttribute("value", str);
		dateToShow.value = str;
	}

	function onSlideEnd() {
		//alert("onSlideEnd " + tmpNoActOnSlideChange);
		if (tmpNoActOnSlideChange) {
			tmpNoActOnSlideChange = false;
			return;
		}
		setCalByDate(denotedDate);
		overlayMarkers();
	}
 
	// when the left arrow clicked, go to one day before; go to one day after for right arrow click
	function sliderArrowClick(obj) {
		var n;
		if (obj == document.getElementById("leftSliderArrow")) {
			n = 1;
		} else if (obj == document.getElementById("rightSliderArrow")) {
			n = -1;
		} else {
			return;
		}

		var str = document.getElementById("dateText").value;
		var tmpDate = getDateFromString(str);
		if (isNaN(tmpDate.getDate())) {
			var slider_val = slider.getValue();
			var endDate = new Date();
			var timeSinceStart = slider_val * (endDate.getTime() - modelStartDate.getTime()) / slider_range;
			tmpDate.setTime(modelStartDate.getTime() + timeSinceStart);
		}

		tmpDate = nDaysBefore(n, tmpDate);
		today = new Date();
		if (tmpDate > today || tmpDate < modelStartDate )
			return;
		// since the slider bar is not precise, we use the date text to adjust the date
		str = getDateString(tmpDate);
		var dateTxt = document.getElementById("dateText");
		dateTxt.value = str;
		dateTxt.setAttribute("value", str);
		tmpNoActOnSlideChange = true;
		setSliderValByDate(tmpDate);
		setCalByDate(tmpDate);
		overlayMarkers();
	}

	function setCalByDate(theDate) {
		YAHOO.example.calendar.cal1.setYear(theDate.getFullYear());
		YAHOO.example.calendar.cal1.setMonth(theDate.getMonth());
		tmpNoActOnCalSelect = true;
		YAHOO.example.calendar.cal1.select(theDate);
		YAHOO.example.calendar.cal1.render();
	}
	</script>

<!-- inline functions about map api -->
	<script type="text/javascript">
	//This value is set at the top of the page.  It is only a template. The values for a
	//specific station will be set for each station.
	var markerWinHtmlStr = '<%=strTabContent%>';
	var selectedStation;

	// Create the marker and corresponding information window 
	function createInfoWinMarker(point, icon1, idx) {
		var markerOpts = {
			position: point,
			icon: icon1,
			clickable: true,
			title: stationArray[idx][0]
		};
		var marker = new google.maps.Marker(markerOpts);
		google.maps.event.addListener(marker, "click", function(mouseEvt) {
				selectedStation = stationArray[idx];
				markerClickBody(marker, selectedStation);
			});
		return marker;
	}

	//Creates the info window associated with the marker.  See "click" listener above.
	function markerClickBody(marker, selectedStation) {
		var stationId = selectedStation[0];
		var lat = "" + selectedStation[1];
		var long = "" + selectedStation[2];
		var preFix = urlPattern.replace(/{!station-id!}/g, stationId) + "/" + dirPattern.replace(/{!station-id!}/g, stationId) + "/";
		var swfURL = preFix + swfInputPattern.replace(/{!station-id!}/g, stationId);
		var dygraphsURL = preFix + dygraphsInputPattern.replace(/{!station-id!}/g, stationId);
		var modelLink = urlPattern.replace(/{!station-id!}/g, stationId) + "/" + modelPattern.replace(/{!station-id!}/g, stationId);
		//TODO: SWF inputs are commented out but should be deleted
		var outputTable = "<table border='0'><tr><td align='center'><b>Output files</b></td></tr>"
							+ "<tr><td><a target='_blank' href='" + preFix + inputPattern.replace(/{!station-id!}/g, stationId) + "'>Input File</a></td></tr>"
							+ "<tr><td><a target='_blank' href='" +  preFix + rawInputPattern.replace(/{!station-id!}/g, stationId) + "'>All Raw Input Data</a></td></tr>"
							+ "<tr><td><a target='_blank' href='" +  preFix + rangePattern.replace(/{!station-id!}/g, stationId) + "'>Range</a></td></tr>"
							+ "<tr><td><a target='_blank' href='" +  preFix + qPattern.replace(/{!station-id!}/g, stationId) + "'>Optimal State Sequence File (Q)</a></td></tr>"
							+ "<tr><td><a target='_blank' href='" +  preFix + aPattern.replace(/{!station-id!}/g, stationId) + "'>Model Transition Probability (A)</a></td></tr>"
							+ "<tr><td><a target='_blank' href='" +  preFix + bPattern.replace(/{!station-id!}/g, stationId) + "'>Model Output Distribution (B)</a></td></tr>"
							+ "<tr><td><a target='_blank' href='" +  preFix + lPattern.replace(/{!station-id!}/g, stationId) + "'>Model Log Likelihood (L)</a></td></tr>"
							+ "<tr><td><a target='_blank' href='" +  preFix + piPattern.replace(/{!station-id!}/g, stationId) + "'>Model Initial State Probability (PI)</a></td></tr>"
							+ "<tr><td><a target='_blank' href='" +  preFix + minPattern.replace(/{!station-id!}/g, stationId) + "'>Minimum Value</a></td></tr>"
							+ "<tr><td><a target='_blank' href='" +  preFix + maxPattern.replace(/{!station-id!}/g, stationId) + "'>Maximum Value</a></td></tr>"
							+ "<tr><td><a target='_blank' href='" +  preFix + xPattern.replace(/{!station-id!}/g, stationId) + "'>Plot of North Values</a></td></tr>"
							+ "<tr><td><a target='_blank' href='" +  preFix + yPattern.replace(/{!station-id!}/g, stationId) + "'>Plot of East Values</a></td></tr>"
							+ "<tr><td><a target='_blank' href='" +  preFix + zPattern.replace(/{!station-id!}/g, stationId) + "'>Plot of Up Values</a></td></tr>"
							+ "<!-- <tr><td><a target='_blank' href='" +  swfURL + "'>Plot Component Input</a></td></tr>-->"							+ "<tr><td><a target='_blank' href='" +  dygraphsURL + "'>Plot Component Input</a></td></tr>"
							+ "<tr><td><b><a target='_blank' href='" +  modelLink + "'>Get all model files</a></b></td></tr></table>";
          
		var changeTable = "<table border='1'><tr><td>Date</td><td nowrap='nowrap'>Old State</td> <td nowrap='nowrap'>New State</td></tr>";
		var dateTmp = new Date();
		if (selectedStation[5] != null) {
			var changeIdx = 0;
			var changeCount = selectedStation[5].length / 3;
			for (var i=0; i<changeCount && i<10; i++) {
				dateTmp.setTime((selectedStation[5][changeIdx++] + timeDiff) * DAY_MILLI + 3600000);
				dateTmp.setUTCHours(12);
				var dateStr = getUTCDateString(dateTmp);
				var oldState = selectedStation[5][changeIdx++];
				var newState = selectedStation[5][changeIdx++];
				changeTable += "<tr><td>" + dateStr + "</td><td>" + oldState + "</td><td>" + newState + "</td></tr>";
			}
		} 
		changeTable += "</table>";

		//TODO: Older SWF plot button is commented out but should be deleted.
		var htmlStr2 = "<div align='center'><table border='0'><tr><td colspan='2'><b>Station ID</b>: " + stationId + "</td></tr>" +
						"<tr><td><b>Longitude</b>: " + long + "</td><td><b>Latitude</b>: " + lat + "</td></tr>" +
						"<tr><td colspan='2'><hr/></td></tr>" +
						"<tr valign='top'><td width='230'>" +
						"<table><tr><td align='center'><b>State Changes</b></td></tr>" +
						"<tr><td>" + changeTable + "</td></tr>" +
						"<!-- <tr><td><button id='showTsBtn' style='background-color:lightgreen' onClick='showTsBtnClick(this)'>View Time Series</button></td></tr> -->" +						
						"<tr><td><button id='showTS2Btn' style='background-color:lightgreen' onClick='showTS2BtnClick(this)'>View Time Series</button></td></tr>" +
						"</table></td><td width='220'>" + outputTable + "</td></tr>" +
						"</table></div>";
		
		//marker.openInfoWindowHtml(htmlStr2, {suppressMapPan:true});
		var infoWin = new google.maps.InfoWindow();
		infoWin.setContent(htmlStr2);
		infoWin.open(map, marker);
	}

	//This is the "View Time Series" button action and opens the plotting window.  
	//The data actually comes from the urlPatten variable, which is populated from the xmlUrl 
	//variable for the appropriate data set. 
	//
	//TODO: this is obsolete and can be deleted.
	function showTsBtnClick(obj) {
		var stationId = selectedStation[0];
		var lat = "" + selectedStation[1];
		var long = "" + selectedStation[2];
		var hgt = "" + selectedStation[4];
		var preFix = urlPattern.replace(/{!station-id!}/g, stationId) + "/" + dirPattern.replace(/{!station-id!}/g, stationId) + "/";
		var swfURL = preFix + swfInputPattern.replace(/{!station-id!}/g, stationId);
		var tsHtmlStr = markerWinHtmlStr;
		tsHtmlStr = tsHtmlStr.replace(/{!swfName!}/g, '<%=swfName%>');
		tsHtmlStr = tsHtmlStr.replace(/{!swfInputURL!}/g, swfURL);
		tsHtmlStr = tsHtmlStr.replace(/{!stationId!}/g, stationId);
		tsHtmlStr = tsHtmlStr.replace(/{!latitude!}/g, lat);
		tsHtmlStr = tsHtmlStr.replace(/{!longitude!}/g, long);
		tsHtmlStr = tsHtmlStr.replace(/{!height!}/g, hgt);
		var newWin = window.open("", stationId, "width=800,height=600");
		newWin.document.writeln(tsHtmlStr);
		newWin.document.title = stationId;
		newWin.document.close();
	}

	//TODO: this function is very hard to modify. Need a better way of generating the plot page.
	function showTS2BtnClick(obj) {
	   var dataSourceType = '<%=dataSource %>';
		console.log("Data Source Type: "+dataSourceType);
		var stationId = selectedStation[0];
		var lat = "" + selectedStation[1];
		var lon = "" + selectedStation[2];
		var hgt = "" + selectedStation[4];
		var preFix = urlPattern.replace(/{!station-id!}/g, stationId) + "/" + dirPattern.replace(/{!station-id!}/g, stationId) + "/";
		var dygraphsURL = preFix + dygraphsInputPattern.replace(/{!station-id!}/g, stationId);
		var dygraphsHtml="<html><body>";
		dygraphsHtml+="<div><b>Station ID:</b> "+stationId+" <b>Lat:</b> "+lat+" <b>Lon:</b> "+lon+"<\/div>";
		dygraphsHtml+="<br/>";
		dygraphsHtml+="<script src='http://danvk.org/dygraphs/dygraph-combined.js' type='text/javascript'>";
		dygraphsHtml+="<\/script>";
		dygraphsHtml+="<script src='"+dygraphsURL+"'><\/script>";
		dygraphsHtml+="<script src='http://dygraphs.com/tests/data.js'><\/script>";
		dygraphsHtml+="<div id='plotDiv1' style='width:800px;height:150px'><\/div>";
		dygraphsHtml+="<br/>";
		dygraphsHtml+="<div id='plotDiv2' style='width:800px;height:150px'><\/div>";
		dygraphsHtml+="<br/>";
		dygraphsHtml+="<div id='plotDiv3' style='width:800px;height:150px'><\/div>";
		//Use mm displacements for UNAVCO data types.  Note significant figures change from below
		dygraphsHtml+="<script type='text/javascript'>";
		dygraphsHtml+="var graphs=[]\;"
		dygraphsHtml+="var plot1, plot2, plot3\;";
		if(dataSourceType=='unavcoPboFill' || dataSourceType=='unavcoNucleusFill') {
		   dygraphsHtml+="plot1=new Dygraph(document.getElementById('plotDiv1'),data_east_disp,{drawPoints:true, strokeWidth:0.0, zoomCallback:zoomCallback, title:\"East Displacement (m)\",yAxisLabelWidth:150,sigFigs:6})\;";
		   dygraphsHtml+="plot2=new Dygraph(document.getElementById('plotDiv2'),data_north_disp,{drawPoints:true, strokeWidth:0.0, zoomCallback:zoomCallback, title:\"North Displacement (m)\",yAxisLabelWidth:150,sigFigs:6})\;";
		   dygraphsHtml+="plot3=new Dygraph(document.getElementById('plotDiv3'),data_up_disp,{drawPoints:true, strokeWidth:0.0, zoomCallback:zoomCallback, title:\"Height Displacement (m)\", yAxisLabelWidth:150,sigFigs:6})\;";
		}
		else {
		   //All other cases, use lat/lon.
			dygraphsHtml+="plot1=new Dygraph(document.getElementById('plotDiv1'),data_east,{drawPoints:true, strokeWidth:0.0, zoomCallback:zoomCallback, title:\"Longitude (degrees)\",yAxisLabelWidth:150,sigFigs:14})\;";
			dygraphsHtml+="plot2=new Dygraph(document.getElementById('plotDiv2'),data_north,{drawPoints:true, strokeWidth:0.0, zoomCallback:zoomCallback, title:\"Latitude (degrees)\",yAxisLabelWidth:150,sigFigs:14})\;";
			dygraphsHtml+="plot3=new Dygraph(document.getElementById('plotDiv3'),data_up,{drawPoints:true, strokeWidth:0.0, zoomCallback:zoomCallback, title:\"Height (meters)\", yAxisLabelWidth:150,sigFigs:14})\;";
		}
		dygraphsHtml+="graphs.push(plot1)\;";
		dygraphsHtml+="graphs.push(plot2)\;";
		dygraphsHtml+="graphs.push(plot3)\;";
		dygraphsHtml+="function zoomCallback(minDate,maxDate){for (var i=0\;i<graphs.length\;i++){graphs[i].updateOptions({dateWindow:[minDate,maxDate]})}}\;";
		dygraphsHtml+="<\/script>";
		dygraphsHtml+="<\/body><\/html>";

		 var windowName=stationId+"-Dygraphs";
		 var newWin = window.open("", windowName, "width=850,height=600");
		newWin.document.writeln(dygraphsHtml);
		newWin.document.title = stationId;
		newWin.document.close();


	}
	</script>

	<!-- This is the all-purpose waiting screen.  Its display is toggled by javascript functions -->
	<DIV ID="waitScreen" STYLE="position:absolute;z-index:5;top:30%;left:20%;visibility:hidden;">
		<TABLE BGCOLOR="#CCCCFF" BORDER=1 BORDERCOLOR="#6600CC" CELLPADDING=0 CELLSPACING=0 HEIGHT=150 WIDTH=300>
			<TR>
				<TD WIDTH="100%" HEIGHT="100%" BGCOLOR="#CCCCFF" ALIGN="CENTER" VALIGN="MIDDLE">
					<BR><BR>   
            	<FONT FACE="Helvetica,Verdana,Arial" SIZE=2 COLOR="#6600CC"><B>Calculating.  Please wait...</B></FONT>
					<BR><BR>
				</TD>
			</TR>
		</TABLE>
	</DIV>

	<!-- This organizes the main display.  Probably should reorganize code so that this comes earlier -->
	<table align="left">
		<tr>
			<td width="936" colspan="2">
				<b><font size="4" face="Verdana">Daily RDAHMM GPS Data Analysis - <%=contextGroup%> Context Group</font></b><p>
				<font face="Verdana" size="2">Note:The default date is set to the latest day when GPS data is available. Click on a station symbol for more information.</font><p></p>
			</td>
		</tr>
		<tr>
			<td valign="top" width="176">
				<font face="Verdana" size="2">
				<div id="networksDiv"> State changes and Colors:    </div>
				<div id="cal1Container" align="center"> </div>
				<div>    Or choose a date by dragging the slider under the map. </div>
				<div> 
					<table border="1" width="172">
						<tr border="0"><td colspan="2">Get KML For Date Range:</td></tr>
						<tr border="0"><td>From: </td><td><input type="text" id="getKmlFromDateText" size="10"/> </td></tr>
						<tr border="0"><td>To:   </td><td><input type="text" id="getKmlToDateText" size="10" onkeydown="onKmlToDateTextKeyDown(event)"/> </td></tr>
						<tr border="0">
							<td  align="center" colspan="2">
								<button id="getKmlBtn" onClick="getKmlBtnClick(this)" style="width:100px;height:20px\">Get KML</button>
							</td>
						</tr>
					</table>
				</div>
				<div>
					<table><tr>
						<td><select id="stationSelect" style="width:70" selectedIndex="0"/></td>
						<td><button id="findStationBtn" onClick="findStationBtnClick(this)">Find Station</button></td>
					</tr></table>
				</div>
				</font>
			</td>
			<td valign="top" width="750">
				<font face="Verdana" size="2">
				<table class="ooih" border="0" cellspacing="0" cellpadding="0" width="750" height="19">
					<tr id="tabRow">
						<td class="ooihj" nowrap onclick="ghbq(this)">View Map</td>
						<td class="ooihs" nowrap onclick="ghbq(this)">State Change Number vs. Time Plot</td>
						<td class="ooihx" style="width:100%">&nbsp;</td>
					</tr>
				</table>
				<table class="ooib" id="obody" border="0" cellspacing="0" cellpadding="0" width="750" height="635">
					<tr valign="top">
						<td valign="top">
							<div id="mapDiv" style="width: 748px; height: 600px">      </div>
							<table border='0'>
								<tr>
									<td>
										<img id="leftSliderArrow" src="/yui_0.12.2/build/slider/assets/arrow_left.png" style="cursor: pointer" onClick="sliderArrowClick(this)">
									</td>
									<td>
										<div id="slider-bg" tabindex="-1">
										<div id="slider-thumb"><img src="/yui_0.12.2/build/slider/assets/horizSlider.png" style="cursor: pointer"></div>
										</div>
									</td>
									<td>
										<img id="rightSliderArrow" src="/yui_0.12.2/build/slider/assets/arrow_right.png" style="cursor: pointer" onClick="sliderArrowClick(this)">
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr valign="top" style="display: none">
						<td valign="top" align="center">
						<table>
							<tr>
								<td colspan="2"><div id="scnPlotDiv" style="width: 742px; height: 539px"></div></td>
							</tr>
							<tr align="center">
								<td colspan="2">(Select an area to zoom in, and double click to zoom out. Use the following "Plot" button to plot for a bounded area.)</td>
							</tr>
							<tr>
								<td>
									<table>
										<tr>
											<td width="70">Latitude: </td>
											<td width="30">from</td>
											<td width="80"><input type="text" id="scnLatFromText" size="9"/></td> 
											<td width="15"> to </td>
											<td colspan="3" width="80"><input type="text" id="scnLatToText" size="9"/></td>
										</tr>
										<tr>
											<td width="70">Longitude: </td>
											<td width="30">from</td>
											<td width="80"><input type="text" id="scnLongFromText" size="9"/></td>
											<td width="15"> to </td>
											<td width="80"><input type="text" id="scnLongToText" size="9"/></td>
											<td><button id="scnPlotBtn" onClick="scnPlotBtnClick(this)">Plot</button></td>
											<td><button id="scnWholeAreaBtn" onClick="scnWholeAreaBtnClick(this)">Whole network</button></td>
										</tr>
									</table>
								</td>
								<td valign="top">
									<table valign="top" align="center">
									<tr><td>
										<a id="scnTxtLink" target="_blank" href="">Text data source for the plot.</a>
										<br/>
										<a id="videoLink" target="_blank" href="">Video for the whole time since 1994.</a>
										<br/>
										<a id="allInputLink" target="_blank" href="">Big input file for all stations.</a>
									</td></tr>
									</table>
								</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</font>
			</td>
		</tr>
	</table>


	<!-- fail nicely if the browser has no Javascript -->
	<noscript><b>JavaScript must be enabled in order for you to use Google Maps.</b>
		However, it seems JavaScript is either disabled or not supported by your browser.
		To view Google Maps, enable JavaScript by changing your browser options, and then
		try again.
	</noscript>

	<script type="text/javascript">
    
	// Create common icon elements for all markers
	var iconSize = new google.maps.Size(18, 18);
	var iconAnchor = new google.maps.Point(1, 10);
	var iconDefImgUrl = "http://maps.google.com/mapfiles/ms/micons/green.png";
	var iconOrigin = new google.maps.Point(0, 0);

	var networkInfo = new Array(5);
	for (i = 0; i < networkInfo.length; ++i){
		networkInfo[i] = new Array(2);
	}

	//This is used by the upper left state lexicon.  Why is this done in JavaScript?
	networkInfo[0][0] = "no state change:";
	networkInfo[0][1] = "http://maps.google.com/mapfiles/ms/micons/green.png";
	networkInfo[1][0] = "state changes on selected date:";
	networkInfo[1][1] = "http://maps.google.com/mapfiles/ms/micons/red.png";
	networkInfo[2][0] = "state changed in last 30 days before selected date:";
	networkInfo[2][1] = "http://maps.google.com/mapfiles/ms/micons/yellow.png";
	networkInfo[3][0] = "no data on selected date:";
	networkInfo[3][1] = "http://maps.google.com/mapfiles/ms/micons/lightblue.png";
	networkInfo[4][0] = "no data on selected date, state changed in last 30 days before selected date:";
	networkInfo[4][1] = "http://maps.google.com/mapfiles/ms/micons/blue.png";

	//Creates the network information display. This is static information so why use javascript?
	function printNetworkColors(array) {
		var html = "<table border='1'><tr><td><b>State</b></td><td nowrap><b>Color<b></td></tr>";
		var row;

		for (row = 0; row < array.length; ++ row) {
			html = html + " <tr>";
			var col;
			for (col = 0; col < array [row] . length; ++ col){
				if(col==0)
					html = html + "  <td>" + array [row] [col] + "</td>";
				if(col==1)
					html = html + "  <td align='center'><img border=0 width=18 height=18 src=" + array [row] [col] + "></td>";
			}
			html = html + " </tr>";
		}
		html = html + " </table> <br/>" 
					+ "Select a date for changes on that day:"
					+ "<table border='0'><tr><td><input type='text' id='dateText' size='11' value='' onkeydown='onDateTextKeyDown(event)'/></td>"
					+ "<td> <button id='clearDateBtn' onClick='clearBtnClick(this)' style='width:70px;height:22px'>Latest</button> </td></tr></table>";
		var idiv = window.document.getElementById("networksDiv");
		idiv.innerHTML = html;
	}
	
	//This calls the previously defined function.  This is the only place that function gets executed.
	//TODO: Remove this and just make it HTML.
	printNetworkColors(networkInfo);

	//What to do when the "Clear" Button is clicked. This is "Latest" button in the left-side 
	//data navigation. Takes you back to the latest available data.
	function clearBtnClick(btn) {
		var dateToShow=document.getElementById("dateText");
		if (dateToShow.value != strLatestDate) {
			dateToShow.value = strLatestDate;
			dateToShow.setAttribute("value", strLatestDate);
			overlayMarkers();
			setCalByDate(dataLatestDate);
			tmpNoActOnSlideChange = true;
			setSliderValByDate(dataLatestDate);
		}
	}
    
	// plot state change number vs time for a bounded area
	function scnPlotBtnClick(btn) {
		document.getElementById("waitScreen").style.visibility="visible";
		var latFrom = document.getElementById("scnLatFromText").value;
		var latTo = document.getElementById("scnLatToText").value;
		var longFrom = document.getElementById("scnLongFromText").value;
		var longTo = document.getElementById("scnLongToText").value;
		
		// little trick learnt from internet about checking validity of string representations of floats
		if (parseFloat(latFrom) != latFrom - 0) {
			alert("invalid minimum latitude!");
			return;
		}
		if (parseFloat(latTo) != latTo - 0) {
			alert("invalid maximum latitude!");
			return;
		}
		if (parseFloat(longFrom) != longFrom - 0) {
			alert("invalid minimum longitude!");
			return;
		}
		if (parseFloat(longTo) != longTo - 0) {
			alert("invalid maximum longitude!");
			return;
		}

		//Note local.hostname will get replaced by Maven.  Also note this service will need to 
		//be deployed separately (mvn clean install -f ExecutionServices/DailyRDAHMMResultsService/pom.xml).
		var url = "http://local.hostname/axis2/services/DailyRdahmmResultService/getStateChangeNumberTrace?resUrl="
					+ xmlResultUrl + "&minLat=" + latFrom + "&maxLat=" + latTo + "&minLong=" + longFrom + "&maxLong=" + longTo;
		var link = callHttpService(url);
		scnPlotGraph = new Dygraph(document.getElementById("scnPlotDiv"), link,
									{
										colors:['#007FFF'],
										strokeWidth:0.7,
										pixelsPerXLabel:50,
										rightGap:2,
										stepPlot:true,
										fillGraph:true,
										fillAlpha:0.8
									});
		document.getElementById("scnTxtLink").href = link;
		document.getElementById("waitScreen").style.visibility="hidden";
	}

	// show the state change number vs. time plot for the whole area
	function scnWholeAreaBtnClick(btn) {
		if (document.getElementById("scnTxtLink").href == scnJsiLink) {
			return;
		}
		scnPlotGraph = new Dygraph(document.getElementById("scnPlotDiv"), 
							scnJsiLink, 
							{
								colors:['#007FFF'],
								strokeWidth:0.7,
								pixelsPerXLabel:50,
								rightGap:2,
								stepPlot:true,
								fillGraph:true,
								fillAlpha:0.8
							});
		document.getElementById("scnLatFromText").value = minLat;
		document.getElementById("scnLatToText").value = maxLat;
		document.getElementById("scnLongFromText").value = minLon;
		document.getElementById("scnLongToText").value = maxLon;
		document.getElementById("scnTxtLink").href = scnJsiLink;
	}

	// what to do when the user pressed a key in the input textbox for the end date of the get kml service
	function onKmlToDateTextKeyDown(e) {
		var keynum, targ;
		if(window.event) {   // IE
			keynum = e.keyCode;
		} else if (e.which) { // Netscape/Firefox/Opera
			keynum = e.which;
		} else if (e.keyCode) {
			keynum = e.keyCode;
		}
  				
		if (e.target) {
			targ=e.target;
		} else if (e.srcElement) {
			targ=e.srcElement;
		}
        	
		var dateTxt = document.getElementById("getKmlToDateText");
		if(keynum == 13 && targ == dateTxt) {
			getKmlBtnClick(document.getElementById("getKmlBtn"));
		}
	}

	// get kml for the selected date
	function getKmlBtnClick(btn) {
		var fromDateStr = document.getElementById("getKmlFromDateText").value;
		var toDateStr = document.getElementById("getKmlToDateText").value;
		var modelStartDate = new Date(1994, 0, 1, 0, 0, 0);
		var today = new Date();
		var fromDate = getDateFromString(fromDateStr);
		var toDate = getDateFromString(toDateStr);

		if (isNaN(fromDate.getDate()) || isNaN(toDate.getDate())) {
			alert("Please input the dates in the format of 'yyyy-mm-dd', 'yyyy/MM/dd', 'MM/dd/yyyy', 'MMMM dd, yyyy', or 'MMM dd, yyyy'");
			return;
		}
		if (fromDate < modelStartDate || fromDate > today || toDate < modelStartDate || toDate > today) {
			alert("Please input dates between 1994-01-01 and today!");
			return;
		}
		fromDateStr = getDateString(fromDate);
		toDateStr = getDateString(toDate);
		var url = "http://local.hostname/axis2/services/DailyRdahmmResultService/getKmlForDateRange2?"
				  + "fromDateStr=" + fromDateStr + "&toDateStr=" + toDateStr + "&resUrl=" + xmlResultUrl;
		document.getElementById("waitScreen").style.visibility="visible";
		var link = callHttpService(url);
		window.open(link);
		document.getElementById("waitScreen").style.visibility="hidden";
	}

	// what to do when the user pressed a key in the date text box
	function onDateTextKeyDown(e) {
		var keynum, targ;
		if(window.event) {   // IE
			keynum = e.keyCode;
		} else if (e.which) { // Netscape/Firefox/Opera
			keynum = e.which;
		} else if (e.keyCode) {
			keynum = e.keyCode;
		}
 
		if (e.target) {
			targ=e.target;
		} else if (e.srcElement) {
			targ=e.srcElement;
		}
 
		var dateTxt = document.getElementById("dateText");
		if(keynum == 13 && targ == dateTxt) {
			var str = dateTxt.value;
			if (str != dateTxt.getAttribute("value")) {
				var tmpDate = getDateFromString(str);
				if (isNaN(tmpDate.getDate())) {
					alert("Please input the dates in the format of 'yyyy-mm-dd', 'yyyy/MM/dd', 'MM/dd/yyyy', 'MMMM dd, yyyy', or 'MMM dd, yyyy'");
					return;
				}
				var today = new Date();
				today.setHours(12);
				today.setMinutes(0);
				today.setSeconds(0);
				today.setMilliseconds(0);
				if (tmpDate > today || tmpDate < modelStartDate) {
					alert("Please input a date between " + getDateString(modelStartDate) + " and " + getDateString(today));
					return;
				}
				dateTxt.setAttribute("value", str);
				tmpNoActOnSlideChange = true;
				setSliderValByDate(tmpDate);
				setCalByDate(tmpDate);
				overlayMarkers();
			}
		}
	}

	// if the date is like '2007-02-02', remove the '0's before '2's
	function simplifyDateStr(dateStr) {
		var str;
		var i1, i2;
		str = dateStr;
		i1 = str.indexOf('-');
		if (str.charAt(i1+1) == '0')
			str = str.substring(0, i1+1) + str.substring(i1+2);
 
		i2 = str.indexOf('-', i1+1);
		if (str.charAt(i2+1) == '0')
			str = str.substring(0, i2+1) + str.substring(i2+2);

		return str;
	}

	function findStationBtnClick(obj) {
		var stationIdx = stSelect.options[stSelect.selectedIndex].value;
		var station = stationArray[stationIdx];
		if (!station[3] || station[6] == null) {
			var color = "green";
			switch(globalColorStr.charAt(2 * stationIdx)) {
				case '1':
					color = "red";
					break;
				case '2':
					color = "yellow";
					break;
				case '3':
					color = "lightblue";
					break;
				case '4':
					color = "blue";
					break;
			}
		   	
			if (station[6] != null)
				station[6].getIcon().url = "http://maps.google.com/mapfiles/ms/micons/" + color + ".png";
			else {
				icon = new google.maps.MarkerImage(iconDefImgUrl, iconSize, iconOrigin, iconAnchor, iconSize);
				icon.url = "http://maps.google.com/mapfiles/ms/micons/" + color + ".png";
				station[6] = createInfoWinMarker(new google.maps.LatLng(station[1], station[2]), icon, stationIdx);
			}
			station[6].setMap(map);
			station[3] = true;
			nMarkerDoneForNewDate++;
		}

		//var mapBounds = map.getBounds();
		//var sw = mapBounds.getSouthWest();
		//var ne = mapBounds.getNorthEast();
		//if (station[1] < sw.lat() || station[1] > ne.lat() || station[2] < sw.lng() || station[2] > ne.lng()) {
			//map.centerAndZoom(new GPoint(station[2], station[1]), 11);
		//}
		selectedStation = station;
		markerClickBody(station[6], selectedStation);
	}

	/* The status change xml file is formated like (note this is obsolete):
	<xml>
		<output-pattern>
		<server-url>http://156-56-104-131.dhcp-bl.indiana.edu:8080//daily_rdahmmexec</server-url>
		<pro-dir>daily_project_{!station-id!}_2007-12-18</pro-dir>
		<AFile>daily_project_{!station-id!}.A</AFile>
		<BFile>daily_project_{!station-id!}.B</BFile>
		<InputFile>daily_project_{!station-id!}_2007-12-18.input</InputFile>
		<LFile>daily_project_{!station-id!}.L</LFile>
		<XPngFile>daily_project_{!station-id!}_2007-12-18.all.input.X.png</XPngFile>
		<YPngFile>daily_project_{!station-id!}_2007-12-18.all.input.Y.png</YPngFile>
		<ZPngFile>daily_project_{!station-id!}_2007-12-18.all.input.Z.png</ZPngFile>
		<PiFile>daily_project_{!station-id!}.pi</PiFile>
		<QFile>daily_project_{!station-id!}_2007-12-18.Q</QFile>
		<MaxValFile>daily_project_{!station-id!}.maxval</MaxValFile>
		<MinValFile>daily_project_{!station-id!}.minval</MinValFile>
		<RangeFile>daily_project_{!station-id!}.range</RangeFile>
		</output-pattern>
		<station-count>442</station-count>
		<station>
			<id>7odm</id>
			<lat>30.3421</lat>
			<long>-144.3342</long>
			<status-changes>2006-10-15:1to5; </status-changes>
			<change-count>15</change-count>
		</station>
		<station>
			<id>arm1</id>
			<lat>30.3421</lat>
			<long>-144.3342</long>
			<status-changes>2006-11-8:2to3; </status-changes>
			<change-count>8</change-count>
		</station>
	</xml>
	*/
<%
   //This seems to be used to parse the XML above into a consumable form. Several parameters
	//like xmlUrl are defined at the top.  It looks like these remote files are downloaded and 
	//grokked each time the page is loaded but only once.
	//		  
   //TODO: the Java below probably should be placed in a backing bean.
	Document statusDoc = null;
	Element eleXml = null;
	Element eleOutput = null;
	try {
		// if the file is old or does not exist, copy it from xmlUrl
		File localFile = new File(config.getServletContext().getRealPath(xmlFileName));
		if (localFile.exists()) {		
			localFile.delete();
		}
		InputStream inUrl = new URL(xmlUrl).openStream();
		OutputStream outLocalFile = new FileOutputStream(localFile);
		byte[] buf = new byte[1024];
		int length;
		while((length = inUrl.read(buf))>0) {
			outLocalFile.write(buf,0,length);
		}
		inUrl.close();
		outLocalFile.close();

		BufferedReader br = new BufferedReader(new FileReader(localFile));
		StringBuffer sb = new StringBuffer();
		while (br.ready()) {
			sb.append(br.readLine());
		}
		SAXReader reader = new SAXReader();
		statusDoc = reader.read( new StringReader(sb.toString()) );
		eleXml = statusDoc.getRootElement();
		eleOutput = eleXml.element("output-pattern");

		// download the state change number input file for the plotting javascript
		String outputUrlPattern = eleOutput.element("server-url").getText();
		String scnJsiFileName = eleOutput.element("stateChangeNumJsInput").getText();                  
		String scnJsiUrl = outputUrlPattern + "/" + scnJsiFileName;                                    
		
		File localScnJsiFile = new File(config.getServletContext().getRealPath(scnJsiFileName));
		InputStream inScnJsiUrl = new URL(scnJsiUrl).openStream();
		OutputStream outLocalScnJsiFile = new FileOutputStream(localScnJsiFile);
		byte[] buf2 = new byte[1024];
		int length2;
		while((length2 = inScnJsiUrl.read(buf2))>0) {
			outLocalScnJsiFile.write(buf2,0,length2);
		}
        inScnJsiUrl.close();
		outLocalScnJsiFile.close();
	} catch (DocumentException ex) {
		ex.printStackTrace();
	}
%>
	var xmlResultUrl = "<%=xmlUrl%>"; 
	var urlPattern = '<%=eleOutput.element("server-url").getText()%>';
	var scnPattern = '<%=eleOutput.element("stateChangeNumJsInput").getText()%>';
	var scnTxtPattern= '<%=eleOutput.element("stateChangeNumTxtFile").getText()%>';
	var scnWholeAreaPngUrl = urlPattern + "/" + scnTxtPattern + ".png";
	var videoUrl = '<%=eleOutput.element("video-url").getText()%>';
	var allInputPattern = '<%=eleOutput.element("allStationInputName").getText()%>';
	var scnJsiLink = "http://local.hostname/DailyRDAHMM-portlet/" + scnPattern;
	var scnPlotGraph = null; 
	document.getElementById("scnTxtLink").href = scnJsiLink;
	document.getElementById("videoLink").href = videoUrl;
	document.getElementById("allInputLink").href = urlPattern + "/" + allInputPattern;

	var dirPattern = '<%=eleOutput.element("pro-dir").getText()%>';
	var aPattern = '<%=eleOutput.element("AFile").getText()%>';
	var bPattern = '<%=eleOutput.element("BFile").getText()%>';
	var inputPattern = '<%=eleOutput.element("InputFile").getText()%>';
	var rawInputPattern = '<%=eleOutput.element("RawInputFile").getText()%>';
	var swfInputPattern = '<%=eleOutput.element("SwfInputFile").getText()%>';
	var dygraphsInputPattern = '<%=eleOutput.element("DygraphsInputFile").getText()%>';
	var lPattern = '<%=eleOutput.element("LFile").getText()%>';
	var xPattern = '<%=eleOutput.element("XPngFile").getText()%>';
	var yPattern = '<%=eleOutput.element("YPngFile").getText()%>';
	var zPattern = '<%=eleOutput.element("ZPngFile").getText()%>';
	var piPattern = '<%=eleOutput.element("PiFile").getText()%>';
	var qPattern = '<%=eleOutput.element("QFile").getText()%>';
	var maxPattern = '<%=eleOutput.element("MaxValFile").getText()%>';
	var minPattern = '<%=eleOutput.element("MinValFile").getText()%>';
	var rangePattern = '<%=eleOutput.element("RangeFile").getText()%>';
	var modelPattern = '<%=eleOutput.element("ModelFiles").getText()%>';
<%
   //TODO: probably this java doesn't need 
	List lStations = eleXml.elements("station");
%>
	// every station in the station array has 7 attributes: id, lat, long, null(for state change table), null(for output table), state change details, marker
	// "state change details" is an array of 3*[state change count] elements; so there are 3 elements in the array for every change count:
	// the millisecond time value for the date of the change, the old state, and the new state
	// we create the infoWindowTabs on the fly, cause they are eating up too much memory if created here
	var stationArray = new Array(<%=lStations.size()%>);
	var icon;
	var dateTmp = new Date();
	var stSelect = document.getElementById("stationSelect");
<%
   //More java code.  Loops over stations, dynamically populates javascript arrays.  
	//TODO: interleaving Java and JavaScript here is tricky. Probably we can separate out and
   //make a REST service call for retreiving all of this.
	double mapcenter_y, mapcenter_x, xmin = 0, xmax = 0, ymin = 0, ymax = 0;
	int changeCount = 0;
	int nodataCount = 0;
	Calendar tmpCaldr = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
	tmpCaldr.set(Calendar.HOUR_OF_DAY, 12);
	tmpCaldr.set(Calendar.MINUTE, 0);
	tmpCaldr.set(Calendar.SECOND, 0);
	tmpCaldr.set(Calendar.MILLISECOND, 0);
	long DAY_MILLI = 86400000;
	// now we load state change information from the xml file, and create markers for all stations
	for (int i=0; i<lStations.size(); i++) {
		Element eleStation = (Element)lStations.get(i);
		double x = Double.parseDouble(eleStation.element("lat").getText());
		double y = Double.parseDouble(eleStation.element("long").getText());
		double h = Double.parseDouble(eleStation.element("height").getText());
		if (xmin == 0 || x < xmin)
			xmin = x;
		if (xmax == 0 || x > xmax)
			xmax = x;
		if (ymin == 0 || y < ymin)
			ymin = y;
		if (ymax == 0 || y > ymax)
			ymax = y;
		changeCount = Integer.parseInt(eleStation.element("change-count").getText());
		nodataCount = Integer.parseInt(eleStation.element("nodata-count").getText());
%>
		stationArray[<%=i%>] = new Array(7);	stationArray[<%=i%>][0] = '<%=eleStation.element("id").getText()%>';
		stationArray[<%=i%>][1] = <%=x%>;	stationArray[<%=i%>][2] = <%=y%>; stationArray[<%=i%>][4] = <%=h%>;
		icon = new google.maps.MarkerImage(iconDefImgUrl, iconSize, iconOrigin, iconAnchor, iconSize);
		stationArray[<%=i%>][6] = createInfoWinMarker(new google.maps.LatLng(<%=x%>, <%=y%>), icon, <%=i%>);
<%
		if (changeCount == 0) {
			out.write("stationArray[" + i + "][5] = null;");
		} else {
			int changeIdx = 0;
			List lChanges = eleStation.elements("status-changes");
			int usefulChange = 10;
			if (lChanges.size() < usefulChange)
				usefulChange = lChanges.size();
			out.write("stationArray[" + i + "][5] = new Array(" + usefulChange + " * 3); ");
			for (int j=0; j<lChanges.size() &&  j<usefulChange; j++) {
				String changeStr = ((Element)lChanges.get(j)).getText();
				int idx1 = 0;
				int idx2 = changeStr.indexOf(';');
				while (idx2 >= 0) {
					String oneChange = changeStr.substring(idx1, idx2).trim();
					int idxCollon = oneChange.indexOf(':');
					int idxTo = oneChange.indexOf("to");
					String changeDate = oneChange.substring(0, idxCollon);
					String oldStatus = oneChange.substring(idxCollon + 1, idxTo);
					String newStatus = oneChange.substring(idxTo + 2);

					String str = changeDate;
					String year, month, day;
					int i1, i2;
					i1 = str.indexOf("-");
					i2 = str.indexOf("-", i1+1);
					year = str.substring(0, i1);
					month = str.substring(i1+1, i2);
					day = str.substring(i2+1);
					tmpCaldr.set(Calendar.YEAR, Integer.parseInt(year, 10));
					tmpCaldr.set(Calendar.MONTH, Integer.parseInt(month, 10)-1);
					tmpCaldr.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day, 10));
%>
					stationArray[<%=i%>][5][<%=changeIdx++%>] = <%=tmpCaldr.getTime().getTime() / DAY_MILLI%>; stationArray[<%=i%>][5][<%=changeIdx++%>] = <%=oldStatus%>; stationArray[<%=i%>][5][<%=changeIdx++%>] = <%=newStatus%>;
<%				
					if (changeIdx >= 30)
						break;
					idx1 = idx2 + 1;
					if (idx1 >= changeStr.length())
						break;
					else {
						idx2 = changeStr.indexOf(';', idx1);
						// if we set idx2 to changeStr.length(), idx1 will be larger than changeStr.length(), so we can break anyway, and this is just 
						// for dealing with the case where there is not a ';' at the end of the changeStr
						if (idx2 < 0)
							idx2 = changeStr.length();
					}
				}
				if (changeIdx >= 30)
					break;
			}
		}
	}
	mapcenter_x = xmin + (xmax - xmin)/2;
	mapcenter_y = ymin + (ymax - ymin)/2;
	Element eleLon = eleXml.element("center-longitude");
	Element eleLat = eleXml.element("center-latitude");
	if (eleLon != null) {
		mapcenter_y = Double.valueOf(eleLon.getText());
	}
	if (eleLat != null) {
		mapcenter_x = Double.valueOf(eleLat.getText());
	}
	tmpCaldr.set(Calendar.YEAR, 1970);
	tmpCaldr.set(Calendar.MONTH, 0);
	tmpCaldr.set(Calendar.DAY_OF_MONTH, 2);
%>
	for (var i=0; i<stationArray.length; i++) {
		stSelect.options[stSelect.length]=new Option(stationArray[i][0], i);
	}
	var DAY_MILLI = 86400000;
	var timeDiff = parseInt(Date.UTC(1970,0,2)/DAY_MILLI) - <%=(tmpCaldr.getTime().getTime()/DAY_MILLI)%>;
	var mapCenterY = <%=mapcenter_y%>;
	var mapCenterX = <%=mapcenter_x%>;	
	var minLon = <%=ymin%>;
	var maxLon = <%=ymax%>;
	var minLat = <%=xmin%>;
	var maxLat = <%=xmax%>;

	document.getElementById("scnLatFromText").value = minLat;
	document.getElementById("scnLatToText").value = maxLat;
	document.getElementById("scnLongFromText").value = minLon;
	document.getElementById("scnLongToText").value = maxLon;
	
	// Create the map
	var mapCenter = new google.maps.LatLng(mapCenterX, mapCenterY);
	var mapOpts = {
		zoom: 6,
		center: mapCenter,
		mapTypeId: google.maps.MapTypeId.ROADMAP
	};
	var map = new google.maps.Map(document.getElementById("mapDiv"), mapOpts);
	var mapZoomLevel = map.getZoom();
	google.maps.event.addListener(map, "dragend", function(){ onMapMove(); });
	google.maps.event.addListener(map, "zoom_changed", function() {
		var oldLevel = mapZoomLevel;
		var newLevel = map.getZoom();
		mapZoomLevel = newLevel;
		// zoomin: newLevel > oldLevel; zoomout: newLevel < oldLevel
		if (newLevel < oldLevel)
			onMapMove();
	} );
	var quakeSimDiv = document.createElement('div');
	quakeSimDiv.innerHTML = "<img src='http://local.hostname/DailyRDAHMM-portlet/QuakeSimLogoGrayEmboss.png'/>";
	map.controls[google.maps.ControlPosition.BOTTOM_RIGHT].push(quakeSimDiv);

	// every station in the station array has 7 attributes: id, lat, long, hasColored(for the selected date), null(for output table), state change details, marker
	var globalColorStr = "";
	function overlayMarkers() {
		document.getElementById("waitScreen").style.visibility="visible";
		window.setTimeout('overlayMarkersBody()',1);
	}

	function clearMapMarkers() {
		for (var i=0; i<stationArray.length; i++) {
			if (stationArray[i][6] != null) {
				stationArray[i][6].setMap(null);
			}
		}
	}

	function overlayMarkersBody(){
		var dateShowText = document.getElementById("dateText");
		var showDateStr = dateShowText.getAttribute("value");
		var icon;
		var sw = new google.maps.LatLng(<%=defSwLat%>, <%=defSwLng%>);
		var ne = new google.maps.LatLng(<%=defNeLat%>, <%=defNeLng%>);
		var mapBounds = map.getBounds();
		if (mapBounds != null) {
			sw = mapBounds.getSouthWest();
			ne = mapBounds.getNorthEast();
		}
		// convert the showDateStr to the format of "yyyy-mm-dd"
		showDateStr = getDateString(getDateFromString(showDateStr));
		if (showDateStr != "") {
			url = "http://local.hostname/axis2/services/DailyRdahmmResultService/calcStationColors?date=" + showDateStr + "&resUrl=" + xmlResultUrl;
			var colorStr = callHttpService(url);
			if (colorStr.length != 0) {
				globalColorStr = colorStr;
				clearMapMarkers();
				nMarkerDoneForNewDate = 0;
				for (var i=0; i<stationArray.length; i++) {
					stationArray[i][3] = false;									
					if (stationArray[i][1] <= sw.lat() || stationArray[i][1] >= ne.lat() || stationArray[i][2] <= sw.lng() || stationArray[i][2] >= ne.lng())
						continue;
					var color = "green";
					switch(colorStr.charAt(2 * i)) {
						case '1':
							color = "red";
							break;
						case '2':
							color = "yellow";
							break;
						case '3':
							color = "lightblue";
							break;
						case '4':
							color = "blue";
							break;
					}
			    	
					if (stationArray[i][6] != null)
						stationArray[i][6].getIcon().url = "http://maps.google.com/mapfiles/ms/micons/" + color + ".png";
					else {
						icon = new google.maps.MarkerImage(iconDefImgUrl, iconSize, iconOrigin, iconAnchor, iconSize);
						icon.url = "http://maps.google.com/mapfiles/ms/micons/" + color + ".png";
						stationArray[i][6] = createInfoWinMarker(new google.maps.LatLng(stationArray[i][1], stationArray[i][2]), icon, i);
					}
					stationArray[i][6].setMap(map);
					stationArray[i][3] = true;
					nMarkerDoneForNewDate++;
				}
			} else {    		
				alert("calling http service at " + url + " Returns empty string");
			}
		}
		document.getElementById("waitScreen").style.visibility="hidden";
	}

	var nMarkerDoneForNewDate = 0;
	function onMapMove() {
		if (nMarkerDoneForNewDate < stationArray.length * 0.7) {
			document.getElementById("waitScreen").style.visibility="visible";
		}
		window.setTimeout('onMapMoveBody()',30);
	}

	function onMapMoveBody() {
		var dateShowText = document.getElementById("dateText");
		var showDateStr = dateShowText.getAttribute("value");
		var icon;
		var mapBounds = map.getBounds();
		var sw = mapBounds.getSouthWest();
		var ne = mapBounds.getNorthEast();
		if (showDateStr != "") {
			if (globalColorStr.length != 0) {
				for (var i=0; i<stationArray.length; i++) {					
					if (stationArray[i][3] || stationArray[i][1] <= sw.lat() || stationArray[i][1] >= ne.lat() || stationArray[i][2] <= sw.lng() || stationArray[i][2] >= ne.lng())
						continue;
					var color = "green";
					switch(globalColorStr.charAt(2 * i)) {
						case '1':
							color = "red";
							break;
						case '2':
							color = "yellow";
							break;
						case '3':
							color = "lightblue";
							break;
						case '4':
							color = "blue";
							break;
					}
			    	
					if (stationArray[i][6] != null)
						stationArray[i][6].getIcon().url = "http://maps.google.com/mapfiles/ms/micons/" + color + ".png";
					else {
						icon = new google.maps.MarkerImage(iconDefImgUrl, iconSize, iconOrigin, iconAnchor, iconSize);
						icon.url = "http://maps.google.com/mapfiles/ms/micons/" + color + ".png";
						stationArray[i][6] = createInfoWinMarker(new google.maps.LatLng(stationArray[i][1], stationArray[i][2]), icon, i);
					}
					stationArray[i][6].setMap(map);
					stationArray[i][3] = true;
					nMarkerDoneForNewDate++;
				}
			}
		}
		document.getElementById("waitScreen").style.visibility="hidden";
	}

	// call a web service with http binding
	function callHttpService(url) {
		var xmlhttp=new XMLHttpRequest();
		xmlhttp.open("GET",url,false);
		xmlhttp.send(null);
		var str = "return>";
		var idx = xmlhttp.responseText.indexOf(str);
		if (idx < 0)
			return "";
		var idx2 = xmlhttp.responseText.indexOf("</", idx);
		return xmlhttp.responseText.substring(idx + str.length, idx2);
	}

	// call the daily RDAHMM result service to get data latest date
	var dataLatestDate = new Date();
	var strLatestDate = getDateString(dataLatestDate);
	function queryDataLatestDate() {
		var url = "http://local.hostname/axis2/services/DailyRdahmmResultService/getDataLatestDate?resUrl=" + xmlResultUrl;
		var res = callHttpService(url);
		if (res.length > 0) {
			setDateBySpecialString(dataLatestDate, res);
			strLatestDate = getDateString(dataLatestDate);
		}
	}

	YAHOO.example.calendar.cal1=new YAHOO.widget.Calendar("cal1","cal1Container");
	YAHOO.example.calendar.cal1.selectEvent.subscribe(myShowDateHandler,YAHOO.example.calendar.cal1, true);
	YAHOO.example.calendar.cal1.Style.CSS_CELL_TODAY = null;
	YAHOO.example.calendar.cal1.render();
	
	slider = YAHOO.widget.Slider.getHorizSlider("slider-bg", "slider-thumb", 0, slider_range, 1);
	slider.subscribe("change", onSlideChange);
	slider.subscribe("slideEnd", onSlideEnd);
	queryDataLatestDate();
	document.getElementById("dateText").value = "";
	clearBtnClick(document.getElementById("clearDateBtn"));
</script>
</body>
</html>
