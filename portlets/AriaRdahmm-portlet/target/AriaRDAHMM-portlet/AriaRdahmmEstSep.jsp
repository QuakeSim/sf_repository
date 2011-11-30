<%@page import="java.util.*, java.net.URL, java.io.*, java.lang.*"%>
<%
	String strTabContent = new String();
	java.io.BufferedInputStream bis=null;
	try{
		bis =new java.io.BufferedInputStream(new java.io.FileInputStream(config.getServletContext().getRealPath("form/windowcontent.htm")));
		BufferedReader br = new BufferedReader(new InputStreamReader(bis));
		String line = null;
		while ((line = br.readLine()) != null) {
			strTabContent=strTabContent+line;
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	finally {
		if (bis != null)bis.close();
	}

	String dataSource = request.getParameter("dataSource");
	if (dataSource == null) {
		dataSource = "30MinMar15";
	}
	String xmlUrl = "http://gf13.ucs.indiana.edu/ariaResEst30MinMar15/station-state-change-ARIA-est.xml";
	String stationListUrl = "http://gf13.ucs.indiana.edu/ariaResEst30MinMar15/stationList.txt";
	if (dataSource.equalsIgnoreCase("30MinMar26")) {
		xmlUrl = "http://gf13.ucs.indiana.edu/ariaResEst30MinMar26/station-state-change-ARIA-estMar26.xml";
		stationListUrl = "http://gf13.ucs.indiana.edu/ariaResEst30MinMar26/stationList.txt";
	} else if (dataSource.equalsIgnoreCase("5MinMar11")) {
		xmlUrl = "http://gf13.ucs.indiana.edu/ariaResEst5MinMar11/station-state-change-ARIA-est5MinMar11.xml";
		stationListUrl = "http://gf13.ucs.indiana.edu/ariaResEst5MinMar11/stationList.txt";
	}
%>

<html>
<head>
	<script src="http://maps.google.com/maps/api/js?sensor=false" type="text/javascript"></script>
	<script src="dateUtil.js" type="text/javascript"></script>
	<script src="NmapAPI.js" type="text/javascript"></script>
</head>

<body>
	
	<!-- Initiating the yui controls -->
	<script type="text/javascript" src="yui_0.12.2/build/yahoo/yahoo.js"></script>
	<script type="text/javascript" src="yui_0.12.2/build/event/event.js"></script>
	<script type="text/javascript" src="yui_0.12.2/build/dom/dom.js"></script>
	<script type="text/javascript" src="yui_0.12.2/build/calendar/calendar.js"></script>
	<script type="text/javascript" src="yui_0.12.2/build/slider/slider.js"></script>
	<script type="text/javascript" src="yui_0.12.2/build/yahoo-dom-event/yahoo-dom-event.js" ></script> 
	<script type="text/javascript" src="yui_0.12.2/build/dragdrop/dragdrop-min.js" ></script>
	<script type="text/javascript" src="yui_0.12.2/build/slider/slider-min.js" ></script>
	<link type="text/css" rel="stylesheet" href="yui_0.12.2/build/calendar/assets/calendar.css"/>
  
	<style>
		#yahooCalContainer {position:relative; width:160px;}
		#slider-bg { 
			position: relative; 
			background:url(yui_0.12.2/build/slider/assets/horizBg.png) 5px 0; 
			height:26px; 
			width:693px;
		}
		#slider-thumb {
			position: absolute; 
			top: 4px; 
		}
	</style>

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

	<!--
	<a href="http://portal.quakesim.org/gridsphere/gridsphere">
		<img src="http://portal.quakesim.org/gridsphere/images/quakesim_banner_portal2.png"/>
	</a>
	-->
	
	<table width="940" cellPadding="0">
		<tr>
			<td width="938" colspan="2">
				<b><font size="4" face="Verdana">Japan GEONET GPS Position Time Series, Tohoku-Oki Earthquake</font></b><p/>
				<font face="Verdana" size="2">Click on a station symbol for more information.</font>
			</td>
		</tr>
		<tr>
			<td valign="top" width="180">
				<table border='0' cellPadding="0">
					<tr><td>
						<table border='1' width='178' cellPadding="0">
							<tr>
								<td><b>Status</b></td>
								<td nowrap><b>Color<b></td>
							</tr>
							<tr>
								<td>no state change</td>
								<td align="center"><img border=0 width=18 height=18 src="http://maps.google.com/mapfiles/ms/micons/green.png"/></td>
							</tr>
							<tr>
								<td>state changed in last 30 minutes before selected time</td>
								<td align="center"><img border=0 width=18 height=18 src="http://maps.google.com/mapfiles/ms/micons/red.png"/></td>
							</tr>
							<tr>
								<td>state changed in last 24 hours before selected time</td>
								<td align="center"><img border=0 width=18 height=18 src="http://maps.google.com/mapfiles/ms/micons/yellow.png"/></td>
							</tr>
							<tr>
								<td>no data at selected time</td>
								<td align="center"><img border=0 width=18 height=18 src="http://maps.google.com/mapfiles/ms/micons/lightblue.png"/></td>
							</tr>
							<tr>
								<td>no data at selected time, state changed in last day before selected time</td>
								<td align="center"><img border=0 width=18 height=18 src="http://maps.google.com/mapfiles/ms/micons/blue.png"/></td>
							</tr>
						</table>
					</td></tr>
					<tr><td>Select a time for changes on map:</td></tr>
					<tr><td><font size="2"><div id="yahooCalContainer" align="center"></div></font></td></tr>
					<tr><td>
						<table border="0" cellPadding="0"><tr>
							<td><input type="text" id="dateText" size="8"/></td> 
							<td><select id="hourSelect"/></td>
							<td>:</td>
							<td><select id="minuteSelect"/></td>
						</tr></table>
					</td></tr>
					<tr><td>
						<table border="0" cellPadding="0"><tr>
							<td><button id="selectTimeBtn" onClick="selectTimeBtnClick(this)">Go</button></td>
							<td><button id="getKmlBtn" onClick="getKmlBtnClick(this)">KML for this date</button></td>
						</tr></table>
					</td></tr>
					<tr><td>
						<table border="0" cellPadding="0"><tr>
							<td><select id="stationSelect" style="width:65px" selectedIndex="0"></td>
							<td><button id="selectStationBtn" onClick="selectStationBtnClick(this)" style="width:100px">Find Station</button></td>
						</tr></table>
					</td></tr>
					<!-- tr><td>Or choose a time by dragging the slider under the map.</td></tr -->
				</table>
			</td>
			<td valign="top">
				<div id="mapDiv" style="width: 744px; height: 620px"></div>
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
	String.prototype.trim = function () {
	    return this.replace(/^\s*/, "").replace(/\s*$/, "");
	}

	var markerWinHtmlStr = '<%=strTabContent%>';
	var selectedStation;
	// Create the marker and corresponding information window
	function createInfoWinMarker(point, icon1, idx) {
		var markerOpts = {
			position: point,
			icon: icon1,
			clickable: true,
			title: stations[idx][0]
		};
		var marker = new google.maps.Marker(markerOpts);
		google.maps.event.addListener(marker, "click", function(mouseEvt) {
			var sltStation = document.getElementById("stationSelect");
			if (sltStation.selectedIndex != idx) {
				sltStation.selectedIndex = idx;
			}
			selectedStation = stations[idx];
			markerClickBody(marker, selectedStation);
		});
		return marker;
	}

	function markerClickBody(marker, selectedStation) {
		var stationId = selectedStation[0];
		var long = "" + selectedStation[1];
		var lat = "" + selectedStation[2];

		var inputUrl = inputLinkPattern.replace(/{!station-id!}/g, stationId);
		var qUrl = qLinkPattern.replace(/{!station-id!}/g, stationId);
		var rawUrl = rawLinkPattern.replace(/{!station-id!}/g, stationId);
		var plotInputUrl = plotInputLinkPattern.replace(/{!station-id!}/g, stationId);

		var htmlStr = "<div align='center'><table border='0'><tr><td><b>Station ID</b>: " + stationId + "</td></tr>" +
						"<tr><td><b>Longitude</b>: " + long + "</td></tr>" + 
						"<tr><td><b>Latitude</b>: " + lat + "</td></tr>" +
						"<tr><td><hr/></td></tr>" +
						"<tr><td><a target=\"_blank\" href=\"" + inputUrl + "\">Input File</a></td></tr>" +
						"<tr><td><a target=\"_blank\" href=\"" + rawUrl + "\">All Raw Input Data</a></td></tr>" +
						"<tr><td><a target=\"_blank\" href=\"" + qUrl + "\">State Sequence File</a></td></tr>" +
						"<tr><td><a target=\"_blank\" href=\"" + plotInputUrl + "\">Plot Input File</a></td></tr>" +
						"<tr><td><button id='showTsBtn' style='background-color:lightgreen' onClick='showTsBtnClick(this)'>View Time Series</button></td></tr>" +
						"</table></div>";
		var infoWin = new google.maps.InfoWindow();
		infoWin.setContent(htmlStr);
		infoWin.open(map, marker);
	}

	function showTsBtnClick(obj) {
		var stationId = selectedStation[0];
		var long = "" + selectedStation[1];
		var lat = "" + selectedStation[2];
		var plotInputUrl = plotInputLinkPattern.replace(/{!station-id!}/g, stationId);

		var htmlStr = markerWinHtmlStr;
		htmlStr = htmlStr.replace(/{!swfInputURL!}/g, plotInputUrl);
		htmlStr = htmlStr.replace(/{!stationId!}/g, stationId);
		htmlStr = htmlStr.replace(/{!latitude!}/g, lat);
		htmlStr = htmlStr.replace(/{!longitude!}/g, long);
		htmlStr = htmlStr.replace(/{!intervalInMin!}/g, intervalInMin+"");
		var newWin = window.open("", stationId, "width=800,height=600");
		newWin.document.writeln(htmlStr);
		newWin.document.title = stationId;
		newWin.document.close();
	}
	
	function selectStationBtnClick(obj) {
		var sltStation = document.getElementById("stationSelect");
		var selectedId = sltStation.options[sltStation.selectedIndex].value;
		var icon = null;
		for (var i=0; i<stations.length; i++) {
			if (stations[i][0] == selectedId) {
				if (stations[i][3] == null || !stations[i][4]) {
					if (globalColorStr.length == 0) {
						return;
					}
					var colorNum = parseInt(globalColorStr.charAt(2*i)+"");
					if (stations[i][3] == null) {
						var icon = new google.maps.MarkerImage(iconImgLinks[colorNum], iconSize, iconOrigin, iconAnchor, iconSize);
						stations[i][3] = createInfoWinMarker(new google.maps.LatLng(stations[i][2], stations[i][1]), icon, i);
					} else {
						stations[i][3].getIcon().url = iconImgLinks[colorNum];
					}
					stations[i][3].setMap(map);
					stations[i][4] = true;
					nMarkerDoneForNewDate++;
				}
				selectedStation = stations[i];
				break;
			}
		}
		markerClickBody(selectedStation[3], selectedStation);
	}

	// Create common icon elements for all markers
	var iconSize = new google.maps.Size(18, 18);
	var iconAnchor = new google.maps.Point(1, 10);
	var iconDefImgUrl = "http://maps.google.com/mapfiles/ms/micons/green.png";
	var iconOrigin = new google.maps.Point(0, 0);

	// Create the map
	var mapCenter = new google.maps.LatLng(38.433255, 139.8999);
	var mapOpts = {
		zoom: 7,
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
	quakeSimDiv.innerHTML = "<img src='http://gf13.ucs.indiana.edu/AriaRDAHMMEst/QuakeSimLogoGrayEmboss.png'/>";
	map.controls[google.maps.ControlPosition.BOTTOM_RIGHT].push(quakeSimDiv);
		
	// read in the station list and create station markers
	var dataSource = "<%=dataSource%>";
	var stationListUrl = "<%=stationListUrl%>";
	var intervalInMin = 30;
	var inputLinkPattern = "";
	var qLinkPattern = "";
	var rawLinkPattern = "";
	var plotInputLinkPattern = "";
	// each element in stations is again array of [<stationId>, <longitude>, <latitude>]
	var stations = new Array();
	var maxLat = 0.0;
	var minLat = 0.0;
	var maxLong = 0.0;
	var minLong = 0.0;
	var dateTimeSelect = null;
	var dateTimeDataStart = null;
	var dateTimeDataEnd = null;
	
	var calSelect = new YAHOO.widget.Calendar("calSelect","yahooCalContainer");
	calSelect.selectEvent.subscribe(myShowDateHandler, calSelect, true);
	calSelect.Style.CSS_CELL_TODAY = null;
	calSelect.MULTI_SELECT = false;
	calSelect.render();
	
	// stationList.txt is like:
	// 30,2011-03-01T00:00:00to2011-03-13T23:30:00
	// http://gf4.ucs.indiana.edu:8080/ariaRes/{!station-id!}/{!station-id!}.input
	// http://gf4.ucs.indiana.edu:8080/ariaRes/{!station-id!}/{!station-id!}.Q
	// http://gf4.ucs.indiana.edu:8080/ariaRes/{!station-id!}/{!station-id!}.raw
	// http://gf4.ucs.indiana.edu:8080/ariaRes/{!station-id!}/{!station-id!}.plotswf.input
	// 0001 141.750435337 45.4029924295
	// 0002 143.224179069 44.4336857788

	var stationListReq = new XMLHttpRequest();
	stationListReq.open("GET", stationListUrl, true);
	stationListReq.onreadystatechange = function() {
		// Makes sure the document is ready to parse.
		if (stationListReq.readyState == 4) {  				
			if (stationListReq.status == 200) {
				onGettingStationList(stationListReq);
			} else {
				alert("Error when getting station list! Request status code: " + stationListReq.status);
			}
		}
	}
	stationListReq.send(null);

	function onGettingStationList(httpReq) {
		var allText = httpReq.responseText;
		var lines = httpReq.responseText.split("\n"); // Will separate each line into an array
		
		// lines[0]
		var idx = lines[0].indexOf(",");
		intervalInMin = parseInt(lines[0].substring(0, idx));
		var idx2 = lines[0].indexOf("to");
		dateTimeDataStart = getDateTimeFromString(lines[0].substring(idx+1, idx2));
		dateTimeDataEnd = getDateTimeFromString(lines[0].substring(idx2+2));
		calSelect.mindate = dateTimeDataStart;
		calSelect.maxdate = dateTimeDataEnd;
		calSelect.render();
		inputLinkPattern = lines[1];
		qLinkPattern = lines[2];
		rawLinkPattern = lines[3];
		plotInputLinkPattern = lines[4];

		var i = 5;
		var stationIdx = 0;
		var sltStation = document.getElementById("stationSelect");
		for (i=5; i<lines.length; i++) {
			var stationLine = lines[i].trim();
			if (stationLine.length == 0) {
				continue;
			}

			var stationElements = stationLine.split(" ");
			// elements for station: id, long, lat, marker, added to map or not
			stations[stationIdx] = new Array(5);
			stations[stationIdx][0] = stationElements[0];
			sltStation.options[stationIdx] = new Option(stationElements[0], stationElements[0]);

			var tmpLong = parseFloat(stationElements[1]);
			var tmpLat =  parseFloat(stationElements[2]);
			stations[stationIdx][1] = tmpLong;
			stations[stationIdx][2] = tmpLat;
			stations[stationIdx][3] = null;
			stations[stationIdx][4] = false;

			if ((i == 5) || (maxLong < tmpLong)) {
				maxLong = tmpLong;
			}
			if ((i == 5) || (minLong > tmpLong)) {
				if (tmpLong > 0) {
					minLong = tmpLong;
				}
			}
			if ((i == 5) || (maxLat < tmpLat)) {
				maxLat = tmpLat;
			}
			if ((i == 5) || (minLat > tmpLat)) {
				minLat = tmpLat;
			}

			stationIdx++;
		}

		var hourSlt = document.getElementById("hourSelect");
		for (i=0; i<24; i++) {
			hourSlt.options[i] = new Option(""+i, i);
		}
		var minuteSlt = document.getElementById("minuteSelect");
		var minCount = 0;
		i = 0;
		while (minCount < 60) {
			minuteSlt.options[i++] = new Option(""+minCount, minCount);
			minCount += intervalInMin;
		}

		overlayMarkers();
	}
		
	// the handler function for the changing event of the Yahoo calendar
	var calSelectDate = null;
	function myShowDateHandler(type,args,obj) {
		var calendarDate = calSelect.getSelectedDates()[0];
		if (calSelectDate != null && calSelectDate.getFullYear() == calendarDate.getFullYear() 
			&& calSelectDate.getMonth() == calendarDate.getMonth() && calSelectDate.getDate() == calendarDate.getDate()) {
			calSelect.render();
			return;
		}
		if (calendarDate < dateTimeDataStart || calendarDate > dateTimeDataEnd) {
			if (calSelectDate == null) {
				calSelectDate = new Date();
				calSelectDate.setFullYear(dateTimeSelect.getFullYear());
				calSelectDate.setMonth(dateTimeSelect.getMonth());
				calSelectDate.setDate(dateTimeSelect.getDate());
			}
			if (calendarDate.getFullYear() != calSelectDate.getFullYear() || calendarDate.getMonth() != calSelectDate.getMonth()) {
				alert("Please choose a time between " + getDateTimeString(dateTimeDataStart)                                                                                   
						+ " and " + getDateTimeString(dateTimeDataEnd) + ".");
			}
			calSelect.select(calSelectDate);
			calSelect.render();
			return;
		}
		if (calSelectDate == null) {
			calSelectDate = new Date();
		}
		calSelectDate.setFullYear(calendarDate.getFullYear());
		calSelectDate.setMonth(calendarDate.getMonth());
		calSelectDate.setDate(calendarDate.getDate());
		var calDateStr = getDateString(calendarDate);
		var textDateStr = document.getElementById("dateText").value;
		if (textDateStr != calDateStr) {
			document.getElementById("dateText").value = calDateStr;
		}
	}

	function selectTimeBtnClick(obj) {
		var dateStr = document.getElementById("dateText").value;
		var hourSlt = document.getElementById("hourSelect");
		var minuteSlt = document.getElementById("minuteSelect");
		var dateTimeStr = dateStr + "T" + hourSlt.options[hourSlt.selectedIndex].value + ":"
							+ minuteSlt.options[minuteSlt.selectedIndex].value + ":00";
		// if the selected date is not changed, do nothing
		if (dateTimeStr == getDateTimeString(dateTimeSelect)) {
			return;
		}

		setDateByString(dateTimeSelect, dateStr);
		if (isNaN(dateTimeSelect.getDate())) {
			alert("Please input a date in the format of 'yyyy-mm-dd'!");
			return;
		}
		dateTimeSelect.setHours(hourSlt.options[hourSlt.selectedIndex].value);
		dateTimeSelect.setMinutes(minuteSlt.options[minuteSlt.selectedIndex].value);
		if (dateTimeSelect < dateTimeDataStart || dateTimeSelect > dateTimeDataEnd) {
			alert("Please choose a time between " + getDateTimeString(dateTimeDataStart)
					+ " and " + getDateTimeString(dateTimeDataEnd) + ".");
			return;
		}
		overlayMarkers();

		if (dateStr != getDateString(calSelect.getSelectedDates()[0])) {
			calSelect.select(dateTimeSelect);
			calSelect.render();
		}
	}
	
	function getKmlBtnClick(obj) {
		var dateStr = document.getElementById("dateText").value;
		var dateTimeStr1 = dateStr + "T00:00:00";
		var dateTimeStr2 = dateStr + "T23:30:00";
		if (dataSource.toLowerCase().indexOf("5min") >= 0) {
			dateTimeStr2 = dateStr + "T23:55:00";
		}
		var dateSelected = getDateTimeFromString(dateTimeStr1);
		if (dateSelected < dateTimeDataStart || dateSelected > dateTimeDataEnd) {
			alert("Please choose a time between " + getDateTimeString(dateTimeDataStart)
					+ " and " + getDateTimeString(dateTimeDataEnd) + ".");
			return;
		}
		
		var url = resServiceUrlPrefix + "getKmlForDateRange?fromDateTimeStr=" + dateTimeStr1 + "&toDateTimeStr=" 
					+ dateTimeStr2 + "&resUrl=" + resXmlUrl;
		document.getElementById("waitScreen").style.visibility="visible";
		var kmlLink = callHttpService(url);
		window.open(kmlLink);
		document.getElementById("waitScreen").style.visibility="hidden";
	}

	var iconImgLinks = new Array();
	for (var i=0; i<5; i++) {
		var color = "green";
		switch (i) {
			case 1:
				color = "red";
				break;
			case 2:
				color = "yellow";
				break;
			case 3:
				color = "lightblue";
				break;
			case 4:
				color = "blue";
				break;
		}
		iconImgLinks[i] = "http://maps.google.com/mapfiles/ms/micons/"  + color + ".png";
	}

	var resXmlUrl = "<%=xmlUrl%>";
	var resServiceUrlPrefix = "http://gf13.ucs.indiana.edu/axis2/services/AriaRdahmmResultService/";
	var dataLatestTimeStr = null;
	function overlayMarkers(){
		document.getElementById("waitScreen").style.visibility="visible";
		window.setTimeout('overlayMarkersBody()',1);
	}

	function clearMapMarkers() {
		for (var i=0; i<stations.length; i++) {
			if (stations[i][3] != null) {
				stations[i][3].setMap(null);
			}
		}
	}

	function overlayMarkersBody() {
		var sw = new google.maps.LatLng(35.85344, 135.50537);
		var ne = new google.maps.LatLng(41.01307, 144.29443);
		var mapBounds = map.getBounds();
		if (mapBounds != null) {
			sw = mapBounds.getSouthWest();
			ne = mapBounds.getNorthEast();
		}
		
		// find the latest time when any data is available
		if (dataLatestTimeStr == null) {
			var url = resServiceUrlPrefix + "getDataLatestDate?resUrl=" + resXmlUrl;
			dataLatestTimeStr = callHttpService(url);
			if (dataSource == "5MinMar11") {
				dataLatestTimeStr = getDateTimeString(dateTimeDataEnd);
			}
			if (dateTimeSelect == null) {
				dateTimeSelect = getDateTimeFromString(dataLatestTimeStr);
				calSelect.setYear(dateTimeSelect.getFullYear());
				calSelect.setMonth(dateTimeSelect.getMonth());
				calSelect.select(dateTimeSelect);
				calSelect.render();
				var hourSlt = document.getElementById("hourSelect");
				var minuteSlt = document.getElementById("minuteSelect");
				for (var i=0; i<hourSlt.options.length; i++) {
					if (hourSlt.options[i].value == dateTimeSelect.getHours()){
						hourSlt.selectedIndex = i;
						break;
					}
				}
				for (var i=0; i<minuteSlt.options.length; i++) {
					if (minuteSlt.options[i].value == dateTimeSelect.getMinutes()){
						minuteSlt.selectedIndex = i;
						break;
					}
				}
			}
		}
	
		var selectedTimeStr = getDateTimeString(dateTimeSelect);
		// overlay markers with different colors
		var url = resServiceUrlPrefix + "calcStationColors?dateTime=" + selectedTimeStr + "&resUrl=" + resXmlUrl;
		var colorStr = callHttpService(url);
		if (colorStr.length == 0) {
			alert("Error when getting icon colors for all stations!");
			document.getElementById("waitScreen").style.visibility="hidden";
			return;
		}
		
		globalColorStr = colorStr;
		clearMapMarkers();
		nMarkerDoneForNewDate = 0;
		for (var i=0; i<stations.length; i++) {
			stations[i][4] = false;
			if (stations[i][2] <= sw.lat() || stations[i][2] >= ne.lat() || stations[i][1] <= sw.lng() || stations[i][1] >= ne.lng())
				continue;

			var colorNum = parseInt(colorStr.charAt(2*i)+"");
			if (stations[i][3] == null) {
				var icon = new google.maps.MarkerImage(iconImgLinks[colorNum], iconSize, iconOrigin, iconAnchor, iconSize);
				stations[i][3] = createInfoWinMarker(new google.maps.LatLng(stations[i][2], stations[i][1]), icon, i);
			} else {
				stations[i][3].getIcon().url = iconImgLinks[colorNum];
			}
			stations[i][3].setMap(map);
			stations[i][4] = true;
			nMarkerDoneForNewDate++;
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
	
	var nMarkerDoneForNewDate = 0;
	var globalColorStr = "";
	function onMapMove() {
		if (nMarkerDoneForNewDate < stations.length * 0.7) {
			document.getElementById("waitScreen").style.visibility="visible";
		}
		if (nMarkerDoneForNewDate >= stations.length) {
			return;
		}
		window.setTimeout('onMapMoveBody()',30);
	}

	function onMapMoveBody() {
		var icon;
		var mapBounds = map.getBounds();
		var sw = mapBounds.getSouthWest();
		var ne = mapBounds.getNorthEast();
		if (globalColorStr.length != 0) {
			for (var i=0; i<stations.length; i++) {					
				if (stations[i][4] || stations[i][2] <= sw.lat() || stations[i][2] >= ne.lat() || stations[i][1] <= sw.lng() || stations[i][1] >= ne.lng())
					continue;
	    	 	
				var colorNum = parseInt(globalColorStr.charAt(2*i)+"");
				if (stations[i][3] == null) {
					icon = new google.maps.MarkerImage(iconImgLinks[colorNum], iconSize, iconOrigin, iconAnchor, iconSize);
					stations[i][3] = createInfoWinMarker(new google.maps.LatLng(stations[i][2], stations[i][1]), icon, i);
				} else {
					stations[i][3].getIcon().url = iconImgLinks[colorNum];
				}
				stations[i][3].setMap(map);
				stations[i][4] = true;
				nMarkerDoneForNewDate++;
			}
		}
		document.getElementById("waitScreen").style.visibility="hidden";
	}

</script>
		<table width="940">
			<tr><td>
				<p><font face="Verdana" size="2">Preliminary GPS time series provided by the ARIA team at JPL and Caltech.
				All original GEONET RINEX data provided to Caltech by the Geospatial Information Authority (GSI) of Japan.
				Time series segmentation performed by the QuakeSim Project using RDAHMM.
				</font></p>
			</td></tr>
		</table>
</body>
</html>
