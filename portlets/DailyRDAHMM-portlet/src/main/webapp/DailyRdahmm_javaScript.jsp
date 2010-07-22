<%@page import="java.util.*, cgl.sensorgrid.sopac.gps.GetStationsRSS, java.io.*, java.lang.*, org.dom4j.*, cgl.sensorgrid.common.*, org.dom4j.io.*"%>

<%
	String strTabContent = new String();
	java.io.BufferedInputStream bis=null;
	try{
		bis =new java.io.BufferedInputStream(new java.io.FileInputStream(config.getServletContext().getRealPath("form/tabcontent.htm")));
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
  <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here"
      type="text/javascript"></script>
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
   #cal1Container {position:relative; width:150px;}
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

	<script>
	YAHOO.namespace("example.calendar");
	
	var slider;
	var slider_range = 682;
	var denotedDate = new Date();
	var modelStartDate = new Date(1994, 1, 1, 0, 0, 0);
	
	//Add an alert window.
	var calClicked = false;
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
	 		
	 		calClicked = true;
	 		dateToShow.setAttribute("value",showDate);
	 		dateToShow.value = showDate;
	 		overlayMarkers();
	 		setSliderValByDate(tmpDate);
	 		calClicked = false;
		}
  }	
  
  function setSliderValByDate(theDate) {
  	var today = new Date();
	 	slider.setValue( (theDate.getTime() - modelStartDate.getTime()) * slider_range / (today.getTime() - modelStartDate.getTime()) );
  }
  
  function onSlideChange() {
  	// if the slider changes because user presses "enter" in the date text box or click the calendar, don't change the date and do everything again
  	if (dateTextKeyDown || calClicked) {
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
  
  // get the date that is n days before thisDate
  function nDaysBefore(n, thisDate) {
  	var before = new Date();
  	before.setTime(thisDate.getTime() - n * 86400 * 1000);
  	return before;
  }
  
  // get the string representation for a date
  function getDateString(d) {
  	return d.getFullYear() + "-" + (d.getMonth()+1) + "-" + d.getDate();
  }
  
  // get date from a string like 2007-03-08
  function getDateFromString(str) { 	
  	var ret = new Date();
  	setDateByString(ret, str);  	
  	return ret;
  }
  
  function setDateByString(theDate, str) {
  	var year, month, day, i1, i2;
  	i1 = str.indexOf("-");
  	i2 = str.indexOf("-", i1+1);
  	year = str.substring(0, i1);
  	month = str.substring(i1+1, i2);
  	day = str.substring(i2+1);
  	
  	theDate.setFullYear(parseInt(year, 10));
  	theDate.setMonth(parseInt(month, 10)-1);
  	theDate.setDate(parseInt(day, 10));
  	theDate.setHours(12);
  	theDate.setMinutes(0);
  	theDate.setSeconds(0);
  	theDate.setMilliseconds(0);
  }
  
  function onSlideEnd() {
  	if (dateTextKeyDown || calClicked)
  		return;
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
  	
  	var tmpDate = null;
  	var str = document.getElementById("dateText").value;
  	if (validDateStr(str)) {
  		tmpDate = getDateFromString(str); 			
  	} else {
  		var slider_val = slider.getValue();
  		var endDate = new Date();
  		var timeSinceStart = slider_val * (endDate.getTime() - modelStartDate.getTime()) / slider_range;
  		tmpDate = new Date();
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
  	dateTextKeyDown = true;
    dateTxt.setAttribute("value", str);
    overlayMarkers();        			
    setSliderValByDate(tmpDate);
    setCalByDate(tmpDate);
    dateTextKeyDown = false;
  }
  
  function setCalByDate(theDate) {
  	YAHOO.example.calendar.cal1.setYear(theDate.getFullYear());
  	YAHOO.example.calendar.cal1.setMonth(theDate.getMonth());
  	YAHOO.example.calendar.cal1.render();
  }
	
	function init() {
		YAHOO.example.calendar.cal1=new YAHOO.widget.Calendar("cal1","cal1Container");
		YAHOO.example.calendar.cal1.selectEvent.subscribe(myShowDateHandler,YAHOO.example.calendar.cal1, true);
		YAHOO.example.calendar.cal1.render();
		
		slider = YAHOO.widget.Slider.getHorizSlider("slider-bg", "slider-thumb", 0, slider_range, 1); 
		slider.subscribe("change", onSlideChange);
		slider.subscribe("slideEnd", onSlideEnd);
		slider.setValue(slider_range);
	}
	YAHOO.util.Event.addListener(window, "load", init);
	</script> 

<!-- inline functions about map api -->	
	<script type="text/javascript">
	
	//N_map_API 0.1
	//Load Javascript function.
	function K_LoadJS(src,handle)
	{
	  var jsFile= document.createElement("script");
	  jsFile.src=src;
	  jsFile.onreadystatechange=function(){if(this.readyState=="complete" || this.readyState=="loaded"){if(handle)handle.call()}};
	  document.body.appendChild(jsFile);
	}
	//read URL function
	function K_GetQueryString(key)
	{
	  var returnValue =""; 
	  var sURL = window.document.URL;
	  if (sURL.indexOf("?") > 0)
	  {
	    var arrayParams = sURL.split("?");
  	  var arrayURLParams = arrayParams[1].split("&");
  	  for (var i = 0; i < arrayURLParams.length; i++)
  	  {
  	    var sParam =  arrayURLParams[i].split("=");
	      if ((sParam[0] ==key) && (sParam[1] != ""))
        returnValue=sParam[1];
  	  }
  	}
  	return returnValue;
	}
	//read cookie function.
	function K_GetCookieVal(offset)
	{
	  var endstr = document.cookie.indexOf (";", offset);
	  if (endstr == -1)
	    endstr = document.cookie.length;
	  return unescape(document.cookie.substring(offset, endstr));
	}
	function K_SetCookie(name, value)
	{
	  var expdate = new Date();
	  var argv = K_SetCookie.arguments;
	  var argc = K_SetCookie.arguments.length;
	  var expires = (argc > 2) ? argv[2] : null;
	  var path = (argc > 3) ? argv[3] : null;
	  var domain = (argc > 4) ? argv[4] : null;
	  var secure = (argc > 5) ? argv[5] : false;
	  if(expires!=null) expdate.setTime(expdate.getTime() + ( expires * 1000 ));
	  document.cookie = name + "=" + escape (value) +((expires == null) ? "" : ("; expires="+ expdate.toGMTString()))+((path == null) ? "" : ("; path=" + path)) +((domain == null) ? "" : ("; domain=" + domain))+((secure == true) ? "; secure" : "");
	}
	function K_DelCookie(name)
	{
	  var exp = new Date();
	  exp.setTime(exp.getTime() - 1);
	  var cval =K_GetCookie(name);
	  document.cookie = name + "=" + cval + "; expires="+ exp.toGMTString();
	}
	function K_GetCookie(name)
	{
	  var arg = name + "=";
	  var alen = arg.length;
	  var clen = document.cookie.length;
	  var i = 0;
	  while (i < clen)
	  {
	    var j = i + alen;
	    if (document.cookie.substring(i, j) == arg)
	      return K_GetCookieVal(j);
	    i = document.cookie.indexOf(" ", i) + 1;
	    if (i == 0) break;
	  }
	  return null;
	}
	//callback function.
	function K_GetCallBack(obj,func)
	{
	  return function(){return func.apply(obj,arguments)};
	}

	// Create the marker and corresponding information window 
	function createInfoMarker(point, address, icon) {
	  var marker = new GMarker(point,icon);
  	GEvent.addListener(marker, "click", function()
  	{
  	  marker.openInfoWindowHtml(address);
  	} );
  	return marker;
	}	

	// Create the marker and corresponding information window 
	function createTabsInfoMarker(point, infoTabs ,icon, idx, sel) {
	  var marker = new GMarker(point,icon);
	  GEvent.addListener(marker, "click", function() {
			var stationId = stationArray[idx][0];
			var urlPattern2 = urlPattern, dirPattern2 = dirPattern;
      var xPattern2 = xPattern, yPattern2 = yPattern, zPattern2 = zPattern;
      var preFix = urlPattern2.replace(/{!station-id!}/g, stationId) + "/" + dirPattern2.replace(/{!station-id!}/g, stationId) + "/";
			
			var str_tabcontent2 = '<%=strTabContent%>';
			var networkName = "unknown";
  		str_tabcontent2 = str_tabcontent2.replace(/{!name!}/g, stationId);
  		str_tabcontent2 = str_tabcontent2.replace(/{!networkName!}/g, networkName);
  		str_tabcontent2 = str_tabcontent2.replace(/{!lon!}/g, "" + stationArray[idx][2]);
  		str_tabcontent2 = str_tabcontent2.replace(/{!lat!}/g, "" + stationArray[idx][1]);
  		var x_tabcontent = str_tabcontent2;
  		x_tabcontent = x_tabcontent.replace( /{!output_png!}/g, preFix + xPattern2.replace(/{!station-id!}/g, stationId));
  		var y_tabcontent = str_tabcontent2;
  		y_tabcontent = y_tabcontent.replace( /{!output_png!}/g, preFix + yPattern2.replace(/{!station-id!}/g, stationId));
  		var z_tabcontent = str_tabcontent2;
  		z_tabcontent = z_tabcontent.replace( /{!output_png!}/g, preFix + zPattern2.replace(/{!station-id!}/g, stationId));
			
			var infoTabs2 = [new GInfoWindowTab("Current X", x_tabcontent),
											new GInfoWindowTab("Current Y", y_tabcontent),
											new GInfoWindowTab("Current Z", z_tabcontent)];
			
			marker.openInfoWindowTabsHtml(infoTabs2);
			sel =	document.getElementById("stationSelect");
			sel.selectedIndex = idx;
			sltChange(sel);
	  });
	  return marker;
	}

	function refimg(elementId,url){
		objimg = document.getElementById(elementId);
		objimg.setAttribute('src',url,0);
	}

	function load() {
	  if (GBrowserIsCompatible()) {
	    var map = new GMap2(document.getElementById("map"));
	    map.addControl(new GSmallMapControl());	
    	map.addControl(new GMapTypeControl());
	    map.setCenter(new GLatLng(37.4419, -122.1419), 13);
  	  // Our info window content
  	  var infoTabs = [ new GInfoWindowTab("Tab #1", "This is tab #1 content"),
  	    							 new GInfoWindowTab("Tab #2", "This is tab #2 content")];

	    // Place a marker in the center of the map and open the info window
 	    // automatically
			var marker = new GMarker(map.getCenter());
	    GEvent.addListener(marker, "click", function() {
	      marker.openInfoWindowTabsHtml(infoTabs);
	    });
	    map.addOverlay(marker);
	    marker.openInfoWindowTabsHtml(infoTabs);
	  }
	}  
	</script>

    <table>
      <tr>
        <td width="650" colspan="2">
          <b><font size="4" face="Verdana">Daily RDAHMM GPS Data Analysis</font></b><p>
            <font face="Verdana" size="2">Click on a station symbol for more
              information.</font><p></p>
        </td>
      </tr>
      <tr>
        <td valign="top" width="150px">
        	<div id="loadInfo"> </div>
          <div id="networksDiv"> Status changes and Colors:    </div>
          <div id="cal1Container"> </div>          
          <br/>
          Or choose a date by dragging the slider under the map.         
        </td>
        <td width="600">
          <div id="map" style="width: 750px; height: 600px">      </div>
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
        <td valign="top" width = "200">
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

        var networkInfo = new Array(3);
        for (i = 0; i < networkInfo.length; ++ i){
          networkInfo[i] = new Array(2);
        }
        networkInfo[0][0] = "no status change:";
        networkInfo[0][1] = "http://labs.google.com/ridefinder/images/mm_20_green.png";
	    	networkInfo[1][0] = "status changes on the selected date:";
        networkInfo[1][1] = "http://labs.google.com/ridefinder/images/mm_20_red.png";
        networkInfo[2][0] = "status changed in the last 30 days before selected date:";
        networkInfo[2][1] = "http://labs.google.com/ridefinder/images/mm_20_yellow.png";

	   		function printNetworkColors (array)
     		{
          var html = "<table border='0'><tr><td><b>Status</b></td><td nowrap><b>Icon Color<b></td></tr>";

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
           html = html + " </table> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/>"
           				+ "Select a date for changes on that day:"
           				+ "<table border='0'> <tr> <td> <input type=\"text\" id=\"dateText\" size=\"10\" value=\"\" onkeydown=\"onDateTextKeyDown(event)\"/> </td>"
           				+ "<td> <button id=\"clearDateBtn\" onClick=\"clearBtnClick(this)\" style=\"width:70px;height:20px\">Today</button>  </td> </tr> </table>";
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
	 					dateToShow.setAttribute("value",ds);
	 					dateToShow.value = ds;
	 					overlayMarkers();
	 					YAHOO.example.calendar.cal1.setYear(d.getFullYear());
  					YAHOO.example.calendar.cal1.setMonth(d.getMonth());
  					YAHOO.example.calendar.cal1.render();
  					slider.setValue(slider_range);
	 				}	
        }
        
        // what to do when the user pressed a key in the date text box
        var dateTextKeyDown = false;
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
        		if (!validDateStr(str)) {
        			alert("Please input the date in the format like 'yyyy-mm-dd'");
        			return;
        		}
        		if (str != dateTxt.getAttribute("value")) {
        			var tmpDate = getDateFromString(str);
        			var today = new Date();
        			if (tmpDate > today || tmpDate < modelStartDate) {
        				alert("Please input a date between " + getDateString(modelStartDate) + " and " + getDateString(today));
        				return;
        			}        			
        			dateTextKeyDown = true;
        			dateTxt.setAttribute("value", str);
        			overlayMarkers();        			
        			setSliderValByDate(tmpDate);
        			setCalByDate(tmpDate);
        			dateTextKeyDown = false;
        		}
        	}
        }
        
        // check if a string is in the right date format
        function validDateStr(dateStr) {
        	var i1, i2;     	
        	
        	i1 = dateStr.indexOf("-");
        	if (i1 < 0 || i1 == dateStr.length - 1)
        		return false;
        	
        	i2 = dateStr.indexOf("-", i1+1);
        	if (i2 < 0 || i2 == dateStr.length - 1 || i2-i1 > 3 || i2-i1 < 2)
        		return false;
        		
        	var j;
        	// charCode: 0:48; 9:57
        	for (j=0; j<dateStr.length; j++) {
        		if (!(dateStr.charCodeAt(j) >= 48 && dateStr.charCodeAt(j) <= 57) && j != i1 && j != i2)
        			return false;
        	}
        			
        	return true;
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
        
        function xmlMicoxLoader(url){
  				//by Micox: micoxjcg@yahoo.com.br.
  				//http://elmicoxcodes.blogspot.com
    			if(window.XMLHttpRequest){
        		var Loader = new XMLHttpRequest();
        		//assyncronous mode to load before the 'return' line
        		Loader.open("GET", url ,false); 
        		Loader.send(null);
        		return Loader.responseXML;
    			}else if(window.ActiveXObject){
        		var Loader = new ActiveXObject("Msxml2.DOMDocument.3.0");
        		//assyncronous mode to load before the 'return' line
        		Loader.async = false;
        		Loader.load(url);
        		return Loader;
    			}
				}

				/* var str_tabcontent = '<%=strTabContent%>'; */
				var str_tabcontent = "";
				//alert("str_tabcontent: " + str_tabcontent);
				// now we load status change information from the xml file, and create markers for all stations
				var changeXml = xmlMicoxLoader("/station-status-change-rss.xml");
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
				var xmlNode = changeXml.childNodes[0];
				var outputNode = xmlNode.childNodes[0];
				var urlPattern = outputNode.childNodes[0].firstChild.nodeValue;
				var dirPattern = "daily/" + outputNode.childNodes[1].firstChild.nodeValue;
				var aPattern = outputNode.childNodes[2].firstChild.nodeValue;
				var bPattern = outputNode.childNodes[3].firstChild.nodeValue;
				var inputPattern = outputNode.childNodes[4].firstChild.nodeValue;
				var lPattern = outputNode.childNodes[5].firstChild.nodeValue;
				var xPattern = outputNode.childNodes[6].firstChild.nodeValue;
				var yPattern = outputNode.childNodes[7].firstChild.nodeValue;
				var zPattern = outputNode.childNodes[8].firstChild.nodeValue;
				var piPattern = outputNode.childNodes[9].firstChild.nodeValue;
				var qPattern = outputNode.childNodes[10].firstChild.nodeValue;
				var maxPattern = outputNode.childNodes[11].firstChild.nodeValue;
				var minPattern = outputNode.childNodes[12].firstChild.nodeValue;
				var rangePattern = outputNode.childNodes[13].firstChild.nodeValue;

				var stationCount = xmlNode.childNodes.length - 2;
				var stationArray = new Array(stationCount);
				var saIdx = 0;
				var mapcenter_y, mapcenter_x, xmin = 0, xmax = 0, ymin = 0, ymax = 0;
				var loadInfoDiv = document.getElementById("loadInfo");
				var stationNode = null;
				var latStr = null;
				var longStr = null;
				var icon = null;
				var changeCount = 0;
				var changeIdx = 0;
				var xmlChildCount = xmlNode.childNodes.length;
				var stationChildCount = 0;
				var changeStr = null;
				var idx1 = -1;
				var idx2 = -1;
				var oneChange = null;
				var idxCollon = -1;
				var idxTo = -1;
				var changeDate = null;
				var oldStatus = null;
				var newStatus = null;
				var dateTmp = new Date();							
				for (var i=2; i<xmlChildCount; i++, saIdx++) {
					stationNode = xmlNode.childNodes[i];
					// every station in the station array has 7 attributes: id, lat, long, status change table, output table, status change details, marker
					// "status change details" is an array of 3*[status change count] elements; so there are 3 elements in the array for every change count: 
					// the millisecond time value for the date of the change, the old status, and the new status
					stationArray[saIdx] = new Array(7);
					stationArray[saIdx][0] = stationNode.childNodes[0].firstChild.nodeValue;
					latStr = stationNode.childNodes[1].firstChild.nodeValue;
					longStr = stationNode.childNodes[2].firstChild.nodeValue;
					stationArray[saIdx][1] = parseFloat(latStr);
					stationArray[saIdx][2] = parseFloat(longStr);
					
					if (xmin == 0) {
						xmin = stationArray[saIdx][1];
					}	else {
						if (stationArray[saIdx][1] < xmin)
							xmin = stationArray[saIdx][1];
					}					
					if (xmax == 0) {
						xmax = stationArray[saIdx][1];
					}	else {
						if (stationArray[saIdx][1] > xmax)
							xmax = stationArray[saIdx][1];
					}					
					if (ymin == 0) {
						ymin = stationArray[saIdx][2];
					}	else {
						if (stationArray[saIdx][2] < ymin)
							ymin = stationArray[saIdx][2];
					}			
					if (ymax == 0) {
						ymax = stationArray[saIdx][2];
					}	else {
						if (stationArray[saIdx][2] > ymax)
							ymax = stationArray[saIdx][2];
					}
					
					// after some tests we decide not to save the tables here, but to create them on the fly. these fields are just reserved
					stationArray[saIdx][3] = null;
					stationArray[saIdx][4] = null;
					  
					icon = new GIcon(baseIcon);
					// we also create the infoWindowTabs on the fly, cause they are eating up too much memory if created here
					stationArray[saIdx][6] = createTabsInfoMarker(new GPoint(longStr, latStr) , null, icon, saIdx, document.getElementById("stationSelect"));
					
					changeCount = parseInt(stationNode.childNodes[stationNode.childNodes.length - 1].firstChild.nodeValue);
					if (changeCount == 0) {
						stationArray[saIdx][5] = null;	
						continue;
					}	
					stationArray[saIdx][5] = new Array(changeCount*3);
					changeIdx = 0;
					stationChildCount = stationNode.childNodes.length;
					for (var j=3; j<stationChildCount - 1; j++) {
						changeStr = stationNode.childNodes[j].firstChild.nodeValue;
						idx1 = 0;
						idx2 = changeStr.indexOf(';');
						while (idx2 >= 0) {
							oneChange = changeStr.substring(idx1, idx2);
							idxCollon = oneChange.indexOf(':');
							idxTo = oneChange.indexOf("to");
							changeDate = oneChange.substring(0, idxCollon);
							oldStatus = oneChange.substring(idxCollon + 1, idxTo);
							newStatus = oneChange.substring(idxTo + 2);
							
							setDateByString(dateTmp, changeDate);
							stationArray[saIdx][5][changeIdx++] = dateTmp.getTime();
							stationArray[saIdx][5][changeIdx++] = parseInt(oldStatus);
							stationArray[saIdx][5][changeIdx++] = parseInt(newStatus);
			
							idx1 = idx2 + 1;
							if (idx1 >= changeStr.length)
									break;
							else {
								idx2 = changeStr.indexOf(';', idx1);
								// if we set idx2 to changeStr.length, idx1 will be larger than changeStr.length, so we can break anyway, and this is just 
								// for dealing with the case where there is not a ';' at the end of the changeStr
								if (idx2 < 0)
									idx2 = changeStr.length;
							}
						}		
					}
					loadInfoDiv.innerHTML = "<b>Loading stations info..." + (saIdx+1) + " done</b>";
				}
				loadInfoDiv.innerHTML = "";
				mapcenter_x = xmin + (xmax - xmin)/2;
				mapcenter_y = ymin + (ymax - ymin)/2;
				map.centerAndZoom(new GPoint(mapcenter_y, mapcenter_x), 10);
        
        // show the list for staus changes
        function printChangedStations() {
        	var idiv = window.document.getElementById("changeListDiv");
        	var html = "<table id=\"statusChangeTable\" border='0'> <tr><b>Stations with status changes:</b></tr> "
        						+ "<tr>(Select one for details)</tr> <tr> <select id=\"stationSelect\" style=\"width:200px\" onChange=\"sltChange(this)\" selectedIndex=0 >";
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
          var xPattern2 = xPattern, yPattern2 = yPattern, zPattern2 = zPattern, dirPattern2 = dirPattern;
          
          var preFix = urlPattern2.replace(/{!station-id!}/g, stationId) + "/" + dirPattern2.replace(/{!station-id!}/g, stationId) + "/";
          var outputTable = "<table border='0'> <tr><td> <b>Output Values</b> </td></tr>" 
                						+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + inputPattern2.replace(/{!station-id!}/g, stationId) + "\">Input File</a></td></tr>"
														+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + rangePattern2.replace(/{!station-id!}/g, stationId) + "\">Range</a></td></tr>"
														+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + qPattern2.replace(/{!station-id!}/g, stationId) + "\">Optimal State Sequence File (Q)</a></td></tr>"
														+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + aPattern2.replace(/{!station-id!}/g, stationId) + "\">Model Transition Probability (A)</a></td></tr>"
														+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + bPattern2.replace(/{!station-id!}/g, stationId) + "\">Model Output Distribution (B)</a></td></tr>"
														+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + lPattern2.replace(/{!station-id!}/g, stationId) + "\">Model Log Likelihood (L)</a></td></tr>"
														+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + piPattern2.replace(/{!station-id!}/g, stationId) + "\">Model Initial State Probability (PI)</a></td></tr>"
														+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + minPattern2.replace(/{!station-id!}/g, stationId) + "\">Minimum Value</a></td></tr>"
														+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + maxPattern2.replace(/{!station-id!}/g, stationId) + "\">Maximum Value</a></td></tr>"
														+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + xPattern2.replace(/{!station-id!}/g, stationId) + "\">Plot of X Values</a></td></tr>"
														+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + yPattern2.replace(/{!station-id!}/g, stationId) + "\">Plot of Y Values</a></td></tr>"
														+ "<tr><td><a target=\"_blank\" href=\"" +  preFix + zPattern2.replace(/{!station-id!}/g, stationId) + "\">Plot of Z Values</a></td></tr> </table>";
          
          var changeTable = "<table border='1'> <tr> <td><b>Date</b></td> <td><b>Old Status</b></td> <td><b>New Status</b></td> </tr>";
          var dateTmp = new Date();
          alert("selected index: " + stationSlt.selectedIndex + "; id: " + stationArray[stationSlt.selectedIndex][0]);
          if (stationArray[stationSlt.selectedIndex][5] != null) {
          	var changeIdx = 0; 
          	var changeCount = stationArray[stationSlt.selectedIndex][5].length / 3;
          	for (var i=0; i<changeCount && i<10; i++) {
          		dateTmp.setTime(stationArray[stationSlt.selectedIndex][5][changeIdx++]);
          		var dateStr = getDateString(dateTmp);
          		var oldStatus = stationArray[stationSlt.selectedIndex][5][changeIdx++];
          		var newStatus = stationArray[stationSlt.selectedIndex][5][changeIdx++];
          		changeTable += "<tr><td>" + dateStr + "</td><td>" + oldStatus + "</td><td>" + newStatus + "</td></tr>";
          	}
        	} else {
        		alert("stationArray[stationSlt.selectedIndex][5] == null");
        	}
          changeTable += "</table>";
          
          var tRows = document.getElementById("statusChangeTable").rows;
          tRows[tRows.length - 3].innerHTML = "<td>" + changeTable + "</td>";
          tRows[tRows.length - 1].innerHTML = "<td>" + outputTable + "</td>";
        }
        
        printChangedStations();
        sltChange(document.getElementById("stationSelect"));
        
        // check if a station's marker should be red or yellow by trying to find a status change with a date between theDate and 30 days before theDate
        // the format of changeArray is like: {chagneTime_1, oldStatus_1, newStatus_1, chagneTime_2, oldStatus_2, newStatus_2, ...}
        function getMarkerColorForChanges(theDate, changeArray) {       	
        	var NDAYS = 30;
        	var firstDate = nDaysBefore(30, theDate);        	
        	var idx1 = 0;
        	var idx2 = changeArray.length / 3 - 1;
        	var color = "green";
        	var startTime = firstDate.getTime();
        	var endTime = theDate.getTime();
        	var midTime;
        	
        	if (idx1 == idx2) {
        		midTime = changeArray[idx1*3];
        		if (midTime == endTime) {
        			color = "red";
        		} else if (midTime >= startTime && midTime < endTime) {
        			color = "yellow";
        		}
        		return color;
        	}
        	
        	// since the changes are desendantly ordered by date, we use binary search to find the date
        	while (idx1 < idx2) { 
        		var mid = parseInt((idx1 + idx2) / 2);
        		midTime = changeArray[mid*3];
        		//alert("startTime: " + startTime + " midTime: " + midTime + " endTime: " + endTime + " startDate: " + getDateString(firstDate) 
        			//		+ " midDate: " + getDateString(changeArray[mid][0]) + " endDate: " + getDateString(theDate) );
        		if (midTime == endTime) {
        			color = "red";
        			break;
        		} else {
        			if (midTime >= startTime && midTime < endTime)
        				color = "yellow";
        			
        			if (mid == idx1) {
        				// this implies that idx1 == idx2-1; so we just check idx2 and get out
        				midTime = changeArray[idx2*3];
        				if (midTime == endTime)
        					color = "red";
        				else if (midTime >= startTime && midTime < endTime)
        					color = "yellow";
        			 	break;
        			}
        			
        			if (midTime > endTime) {
        				idx1 = mid;
        			} else {
        				idx2 = mid;
        			}
        		}
        	}
        	
        	return color;
        }
        
        function overlayMarkers(){        	
   	 			var dateShowText = document.getElementById("dateText");	
		    	var showDateStr = dateShowText.getAttribute("value");
		    	var yellowcount = 0;
		    	var redcount = 0;
		    	if (showDateStr != "") {
		    		map.clearOverlays();		    		
		    		var theDate = getDateFromString(showDateStr);
		    	 	
		    	 	for (var i=0; i<stationArray.length; i++) { 
		    	 		var color = "green";
		    	 		if (stationArray[i][5] != null)
		    	 			color = getMarkerColorForChanges(theDate, stationArray[i][5]);
		    	 		
		    	 		if (color == "yellow")
		    	 			yellowcount++;
		    	 		if (color == "red")
		    	 			redcount++;
		    	 		
		    	 		stationArray[i][6].getIcon().image = "http://labs.google.com/ridefinder/images/mm_20_" + color + ".png";
		    	 		map.addOverlay(stationArray[i][6]);
		    	 	}		
		    	}       
        }

        function createMarker(networkName, name, lon, lat, icon) {
          var marker = new GMarker(new GPoint(lon, lat),icon);
          // Show this marker's name in the info window when it is clicked
          var html = "<b>Station Name= </b>" + name + "<br><b>Lat=</b>" + lat + "<br><b>Lon= </b>" + lon + "<br><b>Network= </b>" + networkName;
          GEvent.addListener(marker, "click", function() {
            marker.openInfoWindowHtml(html);});
            return marker;
        }
        //overlayMarkers();

        </script>
        <p><font face="Verdana" size="2">More information about California Real Time Network (CRTN) is available at	<a href="http://sopac.ucsd.edu/projects/realtime/">SOPAC Web Page</a></font></p>
	</body>
</html>
