<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<%@page import="java.util.*, java.net.URL, java.io.*, java.lang.*, org.dom4j.*, cgl.sensorgrid.common.*, org.dom4j.io.*"%>

<html>
	<head>
	<script src="http://maps.google.com/maps/api/js?sensor=false" type="text/javascript"></script>
	<script src="http://local.hostname/DailyRDAHMM-portlet/NmapAPI.js" type="text/javascript"></script>
	<script src="http://local.hostname/DailyRDAHMM-portlet/dateUtil.js" type="text/javascript"></script>
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
			width:400px;
		}
		#slider-thumb {
			position: absolute;
			top: 1px;
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

	<script>
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
	var slider_range = 385;
	var denotedDate = new Date();
	var modelStartDate = new Date(1994, 0, 1, 0, 0, 0);

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

	var tmpNoActOnCalSelect = false;
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
			//alert(showDate);
			overlayMarkers();
			tmpNoActOnSlideChange = true;
			setSliderValByDate(tmpDate);
		}
	}	
  
	</script>

<!-- inline functions about map api -->
	<script type="text/javascript">
	var markerWinHtmlStr = "id: {!station-id!}";
	// Create the marker and corresponding information window 
	function createInfoWinMarker(point, icon1, idx) {
		var markerOpts = {
			position: point,
			icon: icon1,
			clickable: true,
			title: stationArray[idx][0]
		}
		var marker = new google.maps.Marker(markerOpts);
		google.maps.event.addListener(marker, "click", function(mouseEvt) {
				var stationId = stationArray[idx][0];
				var preFix = urlPattern.replace(/{!station-id!}/g, stationId) + "/" + dirPattern.replace(/{!station-id!}/g, stationId) + "/";
				var pngLink1 = preFix + xPattern.replace(/{!station-id!}/g, stationId);
				var pngLink2 = preFix + yPattern.replace(/{!station-id!}/g, stationId);
				var pngLink3 = preFix + zPattern.replace(/{!station-id!}/g, stationId);
				var htmlStr = "<font face='Verdana' size='2'><img src='" + pngLink1 + "' style='width:100%;'/><br/>";

	        //htmlStr = htmlStr + "See the <a href='http://portal.quakesim.org'>portal</a> for more functionalities.</font>"; 
				htmlStr = htmlStr + "</font>"; 

				var htmlStr2 = "<font face='Verdana' size='2'><img src='" + pngLink2 + "' style='width:100%;'/><br/>";
				var htmlStr3 = "<font face='Verdana' size='2'><img src='" + pngLink3 + "' style='width:100%;'/><br/>";
				htmlStr3 = htmlStr3 + "See the <a href='http://portal.quakesim.org'>portal</a> for more functionalities.</font>";
				
				var winHtml = "<table class='ooih' border='0' cellspacing='0' cellpadding='0' width='300' height='19'>" +
								"<tr id='tabRow'><td class='ooihj' nowrap onclick='ghbq(this)'>Longitude</td>" +
								"<td class='ooihs' nowrap onclick='ghbq(this)'>Latitude</td>" +
								"<td class='ooihs' nowrap onclick='ghbq(this)'>Height</td>" +
								"<td class='ooihx' style='width:100%'>&nbsp;</td></tr></table>" +
								"<table class='ooib' id='obody' border='0' cellspacing='0' cellpadding='0' width='300' height='140'>" +
								"<tr><td>" + htmlStr + "</td></tr>" + 
								"<tr style='display: none'><td>" + htmlStr2 + "</td></tr>" + 
								"<tr style='display: none'><td>" + htmlStr3 + "</td></tr></table>";
				var winOpts = {maxWidth:360};
				var infoWin = new google.maps.InfoWindow(winOpts);
				infoWin.setContent(winHtml);
				infoWin.open(map, marker);
			});
		return marker;
	}
	</script>

	<DIV ID="waitScreen" STYLE="position:absolute;z-index:5;top:100px;left:78px;visibility:hidden;">
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
	<table width="448">
		<tr>
			<td valign="top" width="448" colspan="2">
				<div id="mapDiv" style="width: 448px; height: 400px">      </div>
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
		<tr>
			<td valign="top" width="224">
				<div id="networksDiv"></div>
			</td>
			<td valign="top" width="224">
				<table border='0'>
					<tr>
						<td><input type='text' id='dateText' size='15' value='' onkeydown='onDateTextKeyDown(event)'/></td>
						<td><button id='clearDateBtn' onClick='clearBtnClick(this)' style='width:100px;height:25px'>Go to latest</button></td>
					</tr>
					<tr>
						<td colspan="2" width="224"><div id="cal1Container" align="center"></div></td>
					</tr>
				</table>
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
	networkInfo[0][0] = "no status change:";
	networkInfo[0][1] = "http://maps.google.com/mapfiles/ms/micons/green.png";
	networkInfo[1][0] = "status changes on selected date:";
	networkInfo[1][1] = "http://maps.google.com/mapfiles/ms/micons/red.png";
	networkInfo[2][0] = "status changed in last 30 days before selected date:";
	networkInfo[2][1] = "http://maps.google.com/mapfiles/ms/micons/yellow.png";
	networkInfo[3][0] = "no data on selected date:";
	networkInfo[3][1] = "http://maps.google.com/mapfiles/ms/micons/lightblue.png";
	networkInfo[4][0] = "no data on selected date, status changed in last 30 days before selected date:";
	networkInfo[4][1] = "http://maps.google.com/mapfiles/ms/micons/blue.png";

	function printNetworkColors(array) {
		var html = "<table border='1'><tr><td><b>Status</b></td><td nowrap><b>Color<b></td></tr>";
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
		html = html + " </table>";
		var idiv = window.document.getElementById("networksDiv");
		idiv.innerHTML = html;
	}
	
	printNetworkColors(networkInfo);

	// what to do when the "Clear" Button is clicked
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
				setCalByDate(tmpDate);
				tmpNoActOnSlideChange = true;
				setSliderValByDate(tmpDate);
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
	String xmlUrl = "http://xml.access.hostname//daily_rdahmmexec/daily/JPL_FILL/station-status-change-JPL_FILL.xml";
	Element eleXml = null;
	Element eleOutput = null;
	try {
		// if the file is old or does not exist, copy it from xmlUrl
		File localFile = new File(config.getServletContext().getRealPath("station-status-change-JPL_FILL.xml"));
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
	var dirPattern = '<%=eleOutput.element("pro-dir").getText()%>';
	var aPattern = '<%=eleOutput.element("AFile").getText()%>';
	var bPattern = '<%=eleOutput.element("BFile").getText()%>';
	var inputPattern = '<%=eleOutput.element("InputFile").getText()%>';
	var rawInputPattern = '<%=eleOutput.element("RawInputFile").getText()%>';
	var swfInputPattern = '<%=eleOutput.element("SwfInputFile").getText()%>';
	var lPattern = '<%=eleOutput.element("LFile").getText()%>';
	var xPattern = '<%=eleOutput.element("XTinyPngFile").getText()%>';
	var yPattern = '<%=eleOutput.element("YTinyPngFile").getText()%>';
	var zPattern = '<%=eleOutput.element("ZTinyPngFile").getText()%>';
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
	var icon; 
	var dateTmp = new Date();
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
		icon = new google.maps.MarkerImage(iconDefImgUrl, iconSize, iconOrigin, iconAnchor, iconSize);
		stationArray[<%=i%>][6] = createInfoWinMarker(new google.maps.LatLng(<%=x%>, <%=y%>), icon, <%=i%>);
<%
		
		out.write("stationArray[" + i + "][5] = null;");
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
	var caLat = 36.7477778;
	var caLon = -119.7713889;
	
	// Create the map
	var mapCenter = new google.maps.LatLng(caLat, caLon);
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
	
	// every station in the station array has 7 attributes: id, lat, long, hasColored(for the selected date), null(for output table), status change details, marker
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
		var sw = new google.maps.LatLng(33.1559, -124.6948);
		var ne = new google.maps.LatLng(40.1957, -114.8511);
		var mapBounds = map.getBounds();
		if (mapBounds != null) {
			sw = mapBounds.getSouthWest();
			ne = mapBounds.getNorthEast();
		}
	
		// convert the showDateStr to the format of "yyyy-mm-dd"
		showDateStr = getDateString(getDateFromString(showDateStr));
		//alert(showDateStr);
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
