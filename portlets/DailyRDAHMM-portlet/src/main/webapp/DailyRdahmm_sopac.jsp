<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<%@page import="java.util.*, java.net.URL, java.io.*, java.lang.*, org.dom4j.*, cgl.sensorgrid.common.*, org.dom4j.io.*"%>

<%
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
%>

<html>
	<head>
	<script
    src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" type="text/javascript"></script>
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
	<script type="text/javascript" src="/yui_0.12.2/build/yahoo-dom-event/yahoo-dom-event.js" ></script> 
	<script type="text/javascript" src="/yui_0.12.2/build/dragdrop/dragdrop-min.js" ></script>
	<script type="text/javascript" src="/yui_0.12.2/build/slider/slider-min.js" ></script>
	<link type="text/css" rel="stylesheet" href="/yui_0.12.2/build/calendar/assets/calendar.css">
  
	<style>
		#cal1Container {position:relative; width:210px;}
		#slider-bg { 
			position: relative; 
			background:url(/yui_0.12.2/build/slider/assets/horizBg.png) 5px 0; 
			height:26px; 
			width:710px;  
		}
		#slider-thumb { 
			position: absolute; 
			top: 4px; 
		} 
	</style>

	<style>
		td      { font-size: 9pt;}
		.ooib { border-width: 1px; border-style: none solid solid; border-color: #CC3333; background-color: #EDEDED;}
		.ooih td { border-width: 1px; padding: 0 5; }
		.ooihj { color: #CC3333; background-color: #EDEDED; border-style: solid solid none; border-color: #CC3333; cursor: pointer}
		.ooihs { color: #6600CC; background-color: #ccccFF; border-style: solid; border-color: #6600CC #6600CC #CC3333; cursor: pointer}
		.ooihx { border-style: none none solid; border-color: #CC3333; }
	</style>

	<script type="text/javascript">

	//for manipulating the tabs
	function ghbq(td) {
		var tr = document.getElementById("tabRow");
		var ob = document.getElementById("obody").rows;
		for(var ii=0; ii < tr.cells.length-1; ii++){
			tr.cells[ii].className = (td.cellIndex==ii ? "ooihj":"ooihs");
			ob[ii].style.display = (td.cellIndex==ii ? "":"none");
		}
	}

	YAHOO.namespace("example.calendar");
	
	var slider;
	var slider_range = 682;
	var denotedDate = new Date();
	var modelStartDate = new Date(1994, 0, 1, 0, 0, 0);
	
	//Add an alert window.
	function myShowDateHandler(type,args,obj) {
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
		YAHOO.example.calendar.cal1.render();
	}
	</script> 

<!-- inline functions about map api -->	
	<script type="text/javascript">
	var markerWinHtmlStr = '<%=strTabContent%>';
	// Create the marker and corresponding information window
	function createTabsInfoMarker(point, infoTabs ,icon1, idx, sel) {
		var marker = new GMarker(point, {icon: icon1, clickable: true, title:stationArray[idx][0]});
		GEvent.addListener(marker, "click", function() {
				var stationId = stationArray[idx][0];
				var lat = "" + stationArray[idx][1];
				var long = "" + stationArray[idx][2];
				var urlPattern2 = urlPattern, dirPattern2 = dirPattern, swfInputPattern2 = swfInputPattern;
				var preFix = urlPattern2.replace(/{!station-id!}/g, stationId) + "/" + dirPattern2.replace(/{!station-id!}/g, stationId) + "/";
				var swfURL = preFix + swfInputPattern2.replace(/{!station-id!}/g, stationId);
				var htmlStr = markerWinHtmlStr;
				htmlStr = htmlStr.replace(/{!swfInputURL!}/g, swfURL);
				htmlStr = htmlStr.replace(/{!stationId!}/g, stationId);
				htmlStr = htmlStr.replace(/{!latitude!}/g, lat);
				htmlStr = htmlStr.replace(/{!longitude!}/g, long);
				marker.openInfoWindowHtml(htmlStr, {suppressMapPan:true});
				sel.selectedIndex = idx;
				sltChange(sel);
			});
		return marker;
	}
	</script>

	<DIV ID="waitScreen" STYLE="position:absolute;z-index:5;top:30%;left:35%;visibility:hidden;">
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

	<table>
		<tr>
			<td width="650" colspan="2">
				<b><font size="4" face="Verdana">Daily RDAHMM GPS Data Analysis - SOPAC GLOBK Context Group</font></b><p>
				<font face="Verdana" size="2">Note:The default date is set to the latest day when GPS data is available. Click on a station symbol for more information.</font><p></p>
			</td>
		</tr>
		<tr>
			<td valign="top" width="220">
				<div id="loadInfo"> </div>
				<div id="networksDiv"> Status changes and Colors:    </div>
				<div id="cal1Container" align="center"> </div>          
				<div>    Or choose a date by dragging the slider under the map. </div>
				<div>
					<table border="1" width="220">
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
			</td>
			<td valign="top" width="600">
				<table class="ooih" border="0" cellspacing="0" cellpadding="0" width="767" height="19">
					<tr id="tabRow">
						<td class="ooihj" nowrap onclick="ghbq(this)">View Map</td>
						<td class="ooihs" nowrap onclick="ghbq(this)">State Change Number vs. Time Plot</td>
						<td class="ooihs" nowrap onclick="ghbq(this)">Static State Change Number Plot</td>
						<td class="ooihx" style="width:100%">&nbsp;</td>
					</tr>
				</table>
				<table class="ooib" id="obody" border="0" cellspacing="0" cellpadding="0" width="767" height="635">
					<tr valign="top">
						<td valign="top">
							<div id="map" style="width: 765px; height: 600px">      </div>
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
								<td><div id="scnPlotDiv" style="width: 759px; height: 539px"></div></td>
							</tr>
							<tr align="center">
								<td>(Usage: move mouse to see value, select an area to zoom in, and double click to zoom out.)</td>
							</tr>
							<tr><td><br/></td></tr>
							<tr>
								<td>
								<table valign="top" align="center" width="759px">
								<tr>
									<td>
										<a id="scnTxtLink" target="_blank" href="">Click here to view the detailed data.</a>
										<br/>
										<a id="videoLink" target="_blank" href="">Click here to get a video of the whole time since 1994.</a>
										<br/>
										<a id="allInputLink" target="_blank" href="">Click here to get a file containing the input of all stations.</a>
									</td>
								</tr>
								</table>
								</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr valign="top" style="display: none">
						<td>
						<table align="left">
							<tr><td colspan="7"><img id="scnPngImg" style="width:100%;height:100%;"></td></tr>
							<tr><td colspan="7"><br/></td></tr>
							<tr><td colspan="7">Get the plot for a bounded area:<br/></td></tr>
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
								<td width="85"><button id="scnPlotBtn" onClick="scnPlotBtnClick(this)" style="width:70px;height:25px">Plot</button></td>
								<td><button id="scnWholeAreaBtn" onClick="scnWholeAreaBtnClick(this)" style="width:200px;height:25px">Plot for the whole network</button></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
			</td>
			<td valign="top" width = "240">
				<div id="changeListDiv"> Station List:	</div>
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
    
	// Create a base icon for all of our markers
	var req;
	var baseIcon = new GIcon();
	baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
	baseIcon.iconSize = new GSize(15, 20);
	baseIcon.shadowSize = new GSize(10, 10);
	baseIcon.iconAnchor = new GPoint(1, 10);
	baseIcon.infoWindowAnchor = new GPoint(5, 1);
	baseIcon.infoShadowAnchor = new GPoint(5, 5);
	baseIcon.image = "http://labs.google.com/ridefinder/images/mm_20_green.png";

	// Create the map
	var map = new GMap(document.getElementById("map"));
	map.addControl(new GLargeMapControl());
	map.addControl(new GMapTypeControl());
	map.addControl(new GScaleControl());

	var networkInfo = new Array(5);
	for (i = 0; i < networkInfo.length; ++i){
		networkInfo[i] = new Array(2);
	}
	networkInfo[0][0] = "no status change:";
	networkInfo[0][1] = "http://labs.google.com/ridefinder/images/mm_20_green.png";	networkInfo[1][0] = "status changes on selected date:";
	networkInfo[1][1] = "http://labs.google.com/ridefinder/images/mm_20_red.png";
	networkInfo[2][0] = "status changed in last 30 days before selected date:";
	networkInfo[2][1] = "http://labs.google.com/ridefinder/images/mm_20_yellow.png";	networkInfo[3][0] = "no data on selected date:";	networkInfo[3][1] = "http://labs.google.com/ridefinder/images/mm_20_gray.png";
	networkInfo[4][0] = "no data on selected date, status changed in last 30 days before selected date:";
	networkInfo[4][1] = "http://labs.google.com/ridefinder/images/mm_20_blue.png";

	function printNetworkColors(array) {
		var html = "<table border='1'><tr><td><b>Status</b></td><td nowrap><b>Icon Color<b></td></tr>";
      var row;

		for (row = 0; row < array.length; ++ row) {
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
		html = html + " </table> <br/> <br/>" 
					+ "Select a date for changes on that day:"
					+ "<table border='0' align='center'> <tr> <td> <input type='text' id='dateText' size='15' value='' onkeydown='onDateTextKeyDown(event)'/> </td>"
					+ "<td> <button id='clearDateBtn' onClick='clearBtnClick(this)' style='width:70px;height:22px'>Today</button>  </td> </tr> </table>";
		var idiv = window.document.getElementById("networksDiv");
		idiv.innerHTML = html;
	}
	
	printNetworkColors(networkInfo);
        
	// what to do when the "Clear" Button is clicked
	function clearBtnClick(btn) {
		var dateToShow=document.getElementById("dateText");
		var d = new Date();
		var ds = getDateString(d);
		if (dateToShow.value != ds) {
			dateToShow.value = ds;
			overlayMarkers();
			YAHOO.example.calendar.cal1.setYear(d.getFullYear());
			YAHOO.example.calendar.cal1.setMonth(d.getMonth());
			YAHOO.example.calendar.cal1.render();
			slider.setValue(slider_range);
		}	
	}

	// plot state change number vs time for a bounded area
	function scnPlotBtnClick(btn) {
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

		var url = "http://local.hostname/axis2/services/DailyRdahmmResultService/proxyCallHttpService?serviceUrl="
					+ "http%3A%2F%2Fresult.service.hostname%2Faxis2%2Fservices%2FDailyRdahmmResultService%2FgetStateChangeNumberPlot%3F"
					+ "dataSource%3DSOPAC%26minLat%3D" + latFrom + "%26maxLat%3D" + latTo + "%26minLong%3D" + longFrom + "%26maxLong%3D" + longTo;
		var link = callHttpService(url);
		document.getElementById("scnPngImg").src = link;
		//window.open(link);
	}

	// show the state change number vs. time plot for the whole area
	function scnWholeAreaBtnClick(btn) {
		document.getElementById("scnPngImg").src = scnWholeAreaPngUrl;
		document.getElementById("scnLatFromText").value = minLat;
		document.getElementById("scnLatToText").value = maxLat;
		document.getElementById("scnLongFromText").value = minLon;
		document.getElementById("scnLongToText").value = maxLon;
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
		var url = "http://local.hostname/axis2/services/DailyRdahmmResultService/proxyCallHttpService?serviceUrl="
				  + "http%3A%2F%2Fresult.service.hostname%2Faxis2%2Fservices%2FDailyRdahmmResultService%2FgetKmlForDateRange%3FfromDateStr%3D"
				  + fromDateStr + "%26toDateStr%3D" + toDateStr 
				  + "%26resUrl%3Dhttp%3A%2F%2Fxml.access.hostname%2F%2Fdaily_rdahmmexec%2Fdaily%2FSOPAC_FILL%2Fstation-status-change-SOPAC_FILL.xml";
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
        
	/* The status change xml file is formated like:
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
	Document statusDoc = null;
	String xmlUrl = "http://xml.access.hostname//daily_rdahmmexec/daily/SOPAC_FILL/station-status-change-SOPAC_FILL.xml";
	Element eleXml = null;
	Element eleOutput = null;
	try {
		// if the file is old or does not exist, copy it from xmlUrl
		boolean shouldCopy = false;		
		File localFile = new File(config.getServletContext().getRealPath("station-status-change-SOPAC_FILL.xml"));
		if (localFile.exists()) {		
			Calendar calFile1 = Calendar.getInstance();
			Calendar calFile2 = Calendar.getInstance();
			calFile2.setTimeInMillis(localFile.lastModified());
			shouldCopy = !( calFile1.get(Calendar.YEAR) == calFile2.get(Calendar.YEAR) && calFile1.get(Calendar.MONTH) == calFile2.get(Calendar.MONTH) && calFile1.get(Calendar.DATE) == calFile2.get(Calendar.DATE) && calFile2.get(Calendar.HOUR_OF_DAY) > 5);
			if (shouldCopy) {
				//shouldCopy = false;
				localFile.delete();
			}
		} else {
			shouldCopy = true;		
		}
		if (shouldCopy) {
			InputStream inUrl = new URL(xmlUrl).openStream();
			OutputStream outLocalFile = new FileOutputStream(localFile);
			byte[] buf = new byte[1024];
			int length;
			while((length = inUrl.read(buf))>0) {
				outLocalFile.write(buf,0,length);
			}
			inUrl.close();
			outLocalFile.close();
		}

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
	
	var scnPlotGraph = new Dygraph(document.getElementById("scnPlotDiv"), 
							"http://local.hostname/DailyRDAHMM-portlet/" + scnPattern, 
							{
								colors:['#007FFF'],
								strokeWidth:0.7,
								pixelsPerXLabel:50,
								rightGap:2,
								stepPlot:true,
								fillGraph:true,
								fillAlpha:0.8
							});
	document.getElementById("scnTxtLink").href = urlPattern + "/" + scnPattern;
	document.getElementById("scnPngImg").src = scnWholeAreaPngUrl;
	document.getElementById("videoLink").href = videoUrl;
	document.getElementById("allInputLink").href = urlPattern + "/" + allInputPattern;

	var dirPattern = '<%=eleOutput.element("pro-dir").getText()%>';
	var aPattern = '<%=eleOutput.element("AFile").getText()%>';
	var bPattern = '<%=eleOutput.element("BFile").getText()%>';
	var inputPattern = '<%=eleOutput.element("InputFile").getText()%>';
	var rawInputPattern = '<%=eleOutput.element("RawInputFile").getText()%>';
	var swfInputPattern = '<%=eleOutput.element("SwfInputFile").getText()%>';
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
	List lStations = eleXml.elements("station");
%>
	// every station in the station array has 7 attributes: id, lat, long, null(for status change table), null(for output table), status change details, marker
	// "status change details" is an array of 3*[status change count] elements; so there are 3 elements in the array for every change count: 
	// the millisecond time value for the date of the change, the old status, and the new status
	// we create the infoWindowTabs on the fly, cause they are eating up too much memory if created here
	var stationArray = new Array(<%=lStations.size()%>);
	var icon; var dateTmp = new Date();
<%
	float mapcenter_y, mapcenter_x, xmin = 0, xmax = 0, ymin = 0, ymax = 0;
	int changeCount = 0;
	int nodataCount = 0;
	Calendar tmpCaldr = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
	tmpCaldr.set(Calendar.HOUR_OF_DAY, 12);
	tmpCaldr.set(Calendar.MINUTE, 0);
	tmpCaldr.set(Calendar.SECOND, 0);
	tmpCaldr.set(Calendar.MILLISECOND, 0);
	long DAY_MILLI = 86400000;
	// now we load status change information from the xml file, and create markers for all stations
	for (int i=0; i<lStations.size(); i++) {
		Element eleStation = (Element)lStations.get(i);
		float x = Float.parseFloat(eleStation.element("lat").getText());
		float y = Float.parseFloat(eleStation.element("long").getText());
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
		stationArray[<%=i%>][1] = <%=x%>;	stationArray[<%=i%>][2] = <%=y%>;
		var icon = new GIcon(baseIcon);
		stationArray[<%=i%>][6] = createTabsInfoMarker(new GPoint('<%=y%>', '<%=x%>') , null, icon, <%=i%>, document.getElementById("stationSelect"));
		/*stationArray[<%=i%>][6] = null; */
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
	tmpCaldr.set(Calendar.YEAR, 1970);
	tmpCaldr.set(Calendar.MONTH, 0);
	tmpCaldr.set(Calendar.DAY_OF_MONTH, 2);				
%>
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

	map.centerAndZoom(new GPoint(mapCenterY, mapCenterX), 10);
	GEvent.addListener(map, "moveend", function() {	onMapMove(); } );
	GEvent.addListener(map, "zoomend", function(oldLevel, newLevel) {
		// zoomin: newLevel > oldLevel; zoomout: newLevel < oldLevel
		if (newLevel < oldLevel)
			onMapMove(); 
	} );
				
	// show the list for staus changes
	function printChangedStations() {
		var idiv = window.document.getElementById("changeListDiv");
		var html = "<table id=\"statusChangeTable\" border='0'> <tr><b>Stations with status changes:</b></tr> "
						+ "<tr>(Select one for details)</tr> <tr> <select id=\"stationSelect\" style=\"width:240px\" onChange=\"sltChange(this)\" selectedIndex=0 >";
		for (var i=0; i<stationArray.length; i++) {
			html += "<option>" + stationArray[i][0] + "</option>";
		}
		html += "</tr> <tr align='center'>Last 10 Status Changes:</tr> <tr> </tr> <tr> </tr> <tr> </tr> <tr> </tr> </table>";
		idiv.innerHTML = html;
	}
        
	function sltChange(stationSlt) {
		var stationId = stationArray[stationSlt.selectedIndex][0];
		var urlPattern2 = urlPattern, inputPattern2 = inputPattern, rangePattern2 = rangePattern, qPattern2 = qPattern, aPattern2 = aPattern;
		var bPattern2 = bPattern, lPattern2 = lPattern, piPattern2 = piPattern, minPattern2 = minPattern, maxPattern2 = maxPattern;
		var xPattern2 = xPattern, yPattern2 = yPattern, zPattern2 = zPattern, dirPattern2 = dirPattern; modelPattern2 = modelPattern;
          
		var idx = inputPattern2.lastIndexOf(".");
		var rawInputPattern2 = rawInputPattern; 

		var preFix = urlPattern2.replace(/{!station-id!}/g, stationId) + "/" + dirPattern2.replace(/{!station-id!}/g, stationId) + "/";
		var modelLink = urlPattern2.replace(/{!station-id!}/g, stationId) + "/" + modelPattern2.replace(/{!station-id!}/g, stationId);
		var outputTable = "<table border='0'> <tr><td> <b>Output Values</b> </td></tr>" + "<tr><td><a target=\"_blank\" href=\"" 
							+  preFix + inputPattern2.replace(/{!station-id!}/g, stationId) + "\">Input File</a></td></tr>"
							+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + rawInputPattern2.replace(/{!station-id!}/g, stationId) + "\">All Raw Input Data</a></td></tr>"
							+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + rangePattern2.replace(/{!station-id!}/g, stationId) + "\">Range</a></td></tr>"
							+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + qPattern2.replace(/{!station-id!}/g, stationId) + "\">Optimal State Sequence File (Q)</a></td></tr>"
							+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + aPattern2.replace(/{!station-id!}/g, stationId) + "\">Model Transition Probability (A)</a></td></tr>"
							+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + bPattern2.replace(/{!station-id!}/g, stationId) + "\">Model Output Distribution (B)</a></td></tr>"
							+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + lPattern2.replace(/{!station-id!}/g, stationId) + "\">Model Log Likelihood (L)</a></td></tr>"
							+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + piPattern2.replace(/{!station-id!}/g, stationId) + "\">Model Initial State Probability (PI)</a></td></tr>"
							+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + minPattern2.replace(/{!station-id!}/g, stationId) + "\">Minimum Value</a></td></tr>"
							+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + maxPattern2.replace(/{!station-id!}/g, stationId) + "\">Maximum Value</a></td></tr>"
							+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + xPattern2.replace(/{!station-id!}/g, stationId) + "\">Plot of North Values</a></td></tr>"
							+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + yPattern2.replace(/{!station-id!}/g, stationId) + "\">Plot of East Values</a></td></tr>"
							+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + zPattern2.replace(/{!station-id!}/g, stationId) + "\">Plot of Up Values</a></td></tr>" 
							+ "<tr><td><b><a target=\"_blank\" href=\"" +  modelLink + "\">Get all model files</a></b></td></tr></table>";
          
		var changeTable = "<table border='1'> <tr> <td><b>Date</b></td> <td><b>Old Status</b></td> <td><b>New Status</b></td> </tr>";
		var dateTmp = new Date();
		if (stationArray[stationSlt.selectedIndex][5] != null) {
			var changeIdx = 0; 
			var changeCount = stationArray[stationSlt.selectedIndex][5].length / 3;
			for (var i=0; i<changeCount && i<10; i++) {
				dateTmp.setTime((stationArray[stationSlt.selectedIndex][5][changeIdx++] + timeDiff) * DAY_MILLI + 3600000);
				dateTmp.setUTCHours(12);
				var dateStr = getUTCDateString(dateTmp);
				var oldStatus = stationArray[stationSlt.selectedIndex][5][changeIdx++];
				var newStatus = stationArray[stationSlt.selectedIndex][5][changeIdx++];
				changeTable += "<tr><td>" + dateStr + "</td><td>" + oldStatus + "</td><td>" + newStatus + "</td></tr>";
			}
		} else {
			//alert("stationArray[stationSlt.selectedIndex][5] == null");
		}
		changeTable += "</table>";
          
		var tRows = document.getElementById("statusChangeTable").rows;
		tRows[tRows.length - 3].innerHTML = "<td>" + changeTable + "</td>";
		tRows[tRows.length - 1].innerHTML = "<td>" + outputTable + "</td>";
	}
       
	printChangedStations();
	sltChange(document.getElementById("stationSelect"));
	
	// every station in the station array has 7 attributes: id, lat, long, hasColored(for the selected date), null(for output table), status change details, marker
	var globalColorStr = "";
	function overlayMarkers() {
		document.getElementById("waitScreen").style.visibility="visible";
		window.setTimeout('overlayMarkersBody()',1);
	}

	function overlayMarkersBody(){        	
		var dateShowText = document.getElementById("dateText");	
		var showDateStr = dateShowText.getAttribute("value");
		var icon;
		var mapBounds = map.getBounds();
		var sw = mapBounds.getSouthWest();
		var ne = mapBounds.getNorthEast();
		// convert the showDateStr to the format of "yyyy-mm-dd"
		showDateStr = getDateString(getDateFromString(showDateStr));
		if (showDateStr != "") {
			url = "http://local.hostname/axis2/services/DailyRdahmmResultService/calcStationColors?date=" + showDateStr + "&resUrl=" + xmlResultUrl;
			var colorStr = callHttpService(url);
			if (colorStr.length != 0) {
				map.clearOverlays();
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
							color = "gray";
							break;
						case '4':
							color = "blue";
							break;
					}
			    	 		
					if (stationArray[i][6] != null)
						stationArray[i][6].getIcon().image = "http://labs.google.com/ridefinder/images/mm_20_" + color + ".png";
					else {
						icon = new GIcon(baseIcon);	
						icon.image = "http://labs.google.com/ridefinder/images/mm_20_" + color + ".png";			
						stationArray[i][6] = createTabsInfoMarker(new GPoint(stationArray[i][2], stationArray[i][1]) , null, icon, i, document.getElementById("stationSelect"));
					}
					map.addOverlay(stationArray[i][6]);
					stationArray[i][3] = true;
					nMarkerDoneForNewDate++;
				}
				globalColorStr = colorStr;
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
							color = "gray";
							break;
						case '4':
							color = "blue";
							break;
					}
			    	 		
					if (stationArray[i][6] != null)
						stationArray[i][6].getIcon().image = "http://labs.google.com/ridefinder/images/mm_20_" + color + ".png";
					else {
						icon = new GIcon(baseIcon);	
						icon.image = "http://labs.google.com/ridefinder/images/mm_20_" + color + ".png";			
						stationArray[i][6] = createTabsInfoMarker(new GPoint(stationArray[i][2], stationArray[i][1]) , null, icon, i, document.getElementById("stationSelect"));
					}
					map.addOverlay(stationArray[i][6]);
					stationArray[i][3] = true;
					nMarkerDoneForNewDate++;
				}
			}
		}
		document.getElementById("waitScreen").style.visibility="hidden";
	}

	function createMarker(networkName, name, lon, lat, icon) {
		var marker = new GMarker(new GPoint(lon, lat),icon);
		// Show this marker's name in the info window when it is clicked
		var html = "<b>Station Name= </b>" + name + "<br><b>Lat=</b>" + lat + "<br><b>Lon= </b>" + lon + "<br><b>Network= </b>" + networkName;
		GEvent.addListener(marker, "click", function() {
			marker.openInfoWindowHtml(html);});
		return marker;
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

	YAHOO.example.calendar.cal1=new YAHOO.widget.Calendar("cal1","cal1Container");
	YAHOO.example.calendar.cal1.selectEvent.subscribe(myShowDateHandler,YAHOO.example.calendar.cal1, true);
	YAHOO.example.calendar.cal1.Style.CSS_CELL_TODAY = null;
	YAHOO.example.calendar.cal1.render();
		
	slider = YAHOO.widget.Slider.getHorizSlider("slider-bg", "slider-thumb", 0, slider_range, 1); 
	slider.subscribe("change", onSlideChange);
	slider.subscribe("slideEnd", onSlideEnd);
	// set the date to 18 days ago, the latest date that we get data for all stations
	var url = "http://local.hostname/axis2/services/DailyRdahmmResultService/getDataLatestDate?resUrl=" + xmlResultUrl;
	var str = callHttpService(url);
	var tmpDate = getDateFromString(str);
	document.getElementById("dateText").setAttribute("value", "");
	YAHOO.example.calendar.cal1.setYear(tmpDate.getFullYear());
	YAHOO.example.calendar.cal1.setMonth(tmpDate.getMonth());
	YAHOO.example.calendar.cal1.select(tmpDate);
	YAHOO.example.calendar.cal1.render();

	</script>

	<hr/>
	<f:view>
		<h:form>
			<h:commandLink action="go-to-jpl">
				<h:outputText value="See Results for the JPL GIPSY Context Group"/>
			</h:commandLink>
		</h:form>
	</f:view>

	<p><font face="Verdana" size="2">More information about California Real Time Network (CRTN) is available at	<a href="http://sopac.ucsd.edu/projects/realtime/">SOPAC Web Page</a></font></p>
	</body>
</html>
