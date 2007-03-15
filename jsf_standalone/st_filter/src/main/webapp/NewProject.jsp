<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>STFILTER Project Management</title>
  </head>
 <body>
  <h2>Project Setup Page</h2>
  <p>
  Provide a name for your project and then pick the station from the map. 
<%@page import="java.util.*, cgl.sensorgrid.sopac.gps.GetStationsRSS,
cgl.sensorgrid.gui.google.MapBean, java.io.*"%>
<jsp:useBean id="RSSBeanID" scope="session" class="cgl.sensorgrid.sopac.gps.GetStationsRSS"/>
<jsp:useBean id="MapperID" scope="session" class="cgl.sensorgrid.gui.google.Mapper"/>

<%-- Javascript Toolbox --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/date.js"></script>

<%-- Yahoo UI --%>
<script type="text/javascript" src="/yui_0.12.2/build/yahoo/yahoo.js"></script>
<script type="text/javascript" src="/yui_0.12.2/build/event/event.js"></script>
<script type="text/javascript" src="/yui_0.12.2/build/dom/dom.js"></script>

<script type="text/javascript" src="/yui_0.12.2/build/calendar/calendar.js"></script>
<link type="text/css" rel="stylesheet" href="/yui_0.12.2/build/calendar/assets/calendar.css">

<style>
#beginDateContainer { display:none; position:absolute; }
#endDateContainer { display:none; position:absolute;  }
</style>

<script>
YAHOO.namespace("example.calendar");

function beginDateHandler(type,args,obj) {
	var dates=args[0];
	var date=dates[0];
	var year=date[0],month=date[1],day=date[2];
	var strDate=year+"-"+month+"-"+day;
	var newDateVal=document.getElementById("form1:beginDate");
	newDateVal.setAttribute("value",strDate);
}

function endDateHandler(type,args,obj) {
	var dates=args[0];
	var date=dates[0];
	var year=date[0],month=date[1],day=date[2];
	var strDate=year+"-"+month+"-"+day;
	var newDateVal=document.getElementById("form1:endDate");
	newDateVal.setAttribute("value",strDate);
}

function checkDate() {
	var beginDateObj = Date.parseString(document.forms.form1["form1:beginDate"].value, 'yyyy-MM-dd');
	var endDateObj = Date.parseString(document.forms.form1["form1:endDate"].value, 'yyyy-MM-dd');
	if (beginDateObj.isBefore(endDateObj)) {
		return true;
	} else {
		alert("Begin date should be before end date");
		return false;
	} 
}

function showAndFocus() {
	YAHOO.example.calendar.beginDate.show;
	YAHOO.example.calendar.beginDate.focus;
}

function init() 
{
	var beginDateObj = Date.parseString(document.forms.form1["form1:beginDate"].value, 'yyyy-MM-dd');
	YAHOO.example.calendar.beginDate = new YAHOO.widget.Calendar("beginDate","beginDateContainer", { title:"Choose a start date:", close:true, pagedate:beginDateObj.format('MM/yyyy'), selected:beginDateObj.format('MM/dd/yyyy') } );
	YAHOO.example.calendar.beginDate.selectEvent.subscribe(beginDateHandler, YAHOO.example.calendar.beginDate, true);
	YAHOO.example.calendar.beginDate.render();
	YAHOO.util.Event.addListener("form1:beginDateImg", "mouseover", YAHOO.example.calendar.beginDate.show, YAHOO.example.calendar.beginDate, true); 

	var endDateObj = Date.parseString(document.forms.form1["form1:endDate"].value, 'yyyy-MM-dd');
	YAHOO.example.calendar.endDate = new YAHOO.widget.Calendar("endDate","endDateContainer", { title:"Choose an end date:", close:true, pagedate:endDateObj.format('MM/yyyy'), selected:endDateObj.format('MM/dd/yyyy') } );
	YAHOO.example.calendar.endDate.selectEvent.subscribe(endDateHandler, YAHOO.example.calendar.endDate, true);
	YAHOO.example.calendar.endDate.render();
	YAHOO.util.Event.addListener("form1:endDateImg", "mouseover", YAHOO.example.calendar.endDate.show, YAHOO.example.calendar.endDate, true); 
}

// Listener 
YAHOO.util.Event.addListener(window, "load", init);
</script>
			
<%
Vector networkNames = RSSBeanID.networkNames();

//Vector stationsVec = RSSBeanID.getAllStationsVec();
String mapcenter_x = "33.036";
String mapcenter_y = "-117.24";

String [] center_xy = RSSBeanID.getMapCenter();
mapcenter_x = center_xy[0];
mapcenter_y = center_xy[1];
%>
<html>
  <head>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAuZXFNqpWwSQD2wTwESuVIBRBrwnbnLbpn3Gq_e9fnTRzhOmcJRQo3xdLzghObGORYbtO3zTTnS6jqg"
      type="text/javascript"></script>
  </head>
  <body>


    <table>
      <tr>
        <td width="750" colspan="3">
          <b><font size="4" face="Verdana">SOPAC Real Time GPS Networks</font></b><p>
        </td>
      </tr>
      <tr>
        <td valign="top" width="50">
          <div id="networksDiv"> Network Names and Colors     </div>
        </td>
        <td width="600">
          <div id="map" style="width: 600px; height: 600px">      </div>
        </td>

    <script type="text/javascript">
      //<![CDATA[
      // Check to see if this browser can run the Google API

      //    if (GBrowserIsCompatible()) {
        // Create a base icon for all of our markers
        var req;
        var baseIcon = new GIcon();
        baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
        baseIcon.iconSize = new GSize(15, 20);
        baseIcon.shadowSize = new GSize(10, 10);
        baseIcon.iconAnchor = new GPoint(1, 10);
        baseIcon.infoWindowAnchor = new GPoint(5, 1);
        baseIcon.infoShadowAnchor = new GPoint(5, 5);

        // Create the map
        var map = new GMap(document.getElementById("map"));
        map.addControl(new GLargeMapControl());
        map.addControl(new GMapTypeControl());
        map.addControl(new GScaleControl());
        map.centerAndZoom(new GPoint(<%=mapcenter_y%>, <%=mapcenter_x%>), 10);

        var colors = new Array (6);
        colors[0]="red";
        colors[1]="green";
        colors[2]="blue";
        colors[3]="black";
        colors[4]="white";
        colors[5]="yellow";
        colors[6]="purple";
        colors[7]="brown";

        var networkInfo = new Array (<%=networkNames.size()%>);
        for (i = 0; i < networkInfo.length; ++ i){
          networkInfo [i] = new Array (2);
        }

        function overlayNetworks(){
          var icon = new GIcon(baseIcon);
          icon.image = "http://labs.google.com/ridefinder/images/mm_20_green.png";

          <%
          for (int j = 0; j < networkNames.size(); j++) {
            String networkName = (String)networkNames.get(j);
            Vector stationsVec = RSSBeanID.getStationsVec(networkName);
            %>
            networkInfo [<%=j%>] [0] = "<%=networkName%>";

            var k;
            if(<%=j%>>=colors.length)
            k = 0;
            else
            k=<%=j%>;
            icon.image = "http://labs.google.com/ridefinder/images/mm_20_" + colors[k] + ".png";
            networkInfo [<%=j%>] [1] = "http://labs.google.com/ridefinder/images/mm_20_" + colors[k] + ".png";

            var stationCount = <%=stationsVec.size()%>;
            var stations = new Array (stationCount);
            var Markers = new Array (stationCount);
            for (i = 0; i < stations.length; ++ i){
              stations [i] = new Array (3);
            }

            <%
            for (int i = 0; i < stationsVec.size(); i++) {
              String name = (String)stationsVec.get(i);
              String lat = RSSBeanID.getStationInfo(name)[0];
              String lon = RSSBeanID.getStationInfo(name)[1];
              %>
              stations [<%=i%>] [0] = "<%=name%>";
              stations [<%=i%>] [1] = "<%=lat%>";
              stations [<%=i%>] [2] = "<%=lon%>";

              Markers[<%=i%>] = createMarker("<%=networkName%>", "<%=name%>", "<%=lon%>", "<%=lat%>", icon);
              map.addOverlay(Markers[<%=i%>]);
	
              <%
            }
          }
          %>
        }

        function createMarker(networkName, name, lon, lat, icon) {
          var marker = new GMarker(new GPoint(lon, lat),icon);
          // Show this marker's name in the info window when it is clicked
          var html = "<b>Station Name= </b>" + name + "<br><b>Lat=</b>" + lat + "<br><b>Lon= </b>" + lon + "<br><b>Network= </b>" + networkName;


          GEvent.addListener(marker, "click", function() {
            marker.openInfoWindowHtml(html);});

          GEvent.addListener(marker, "click", function() {
		var newElement=document.getElementById("form1:station_name");
		newElement.setAttribute("value",name);
	  });

          return marker;
          }

        overlayNetworks();



        function printNetworkColors (array)
        {
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
         printNetworkColors(networkInfo);
      </script>

    <!-- fail nicely if the browser has no Javascript -->
    <noscript><b>JavaScript must be enabled in order for you to use Google Maps.</b>
      However, it seems JavaScript is either disabled or not supported by your browser.
      To view Google Maps, enable JavaScript by changing your browser options, and then
      try again.
    </noscript>
	
	<td valign="top">
	<f:view>
	<h:form id="form1" onsubmit="return checkDate()">
    <h:panelGrid columns="2" border="1">
		<h:outputText value="Project Name:"/>
		<h:panelGroup>
			<h:inputText id="projectName" value="#{stfilterBean.projectName}" required="true"/>
			<h:message for="projectName" showDetail="true" showSummary="true" errorStyle="color: red"/>
		</h:panelGroup>
		
		<h:outputText value="Station Code:"/>
		<h:panelGroup>
			<h:inputText id="station_name" value="#{stfilterBean.siteCode}" required="true"/>
			<h:message for="station_name" showDetail="true" showSummary="true" errorStyle="color: red"/>
		</h:panelGroup>

		<h:outputText value="Begin Date:"/>
		<h:panelGroup>
			<h:inputText id="beginDate" value="#{stfilterBean.beginDate}"/>
			<h:graphicImage id="beginDateImg" value="/calendar.gif"/>
			<f:verbatim>
				<div id="beginDateContainer"></div> 
			</f:verbatim>
		</h:panelGroup>

		<h:outputText value="End Date:"/>
		<h:panelGroup>
			<h:inputText id="endDate" value="#{stfilterBean.endDate}"/>
			<h:graphicImage id="endDateImg" value="/calendar.gif"/>
			<f:verbatim>
				<div id="endDateContainer"></div> 
			</f:verbatim>
		</h:panelGroup>

    </h:panelGrid>
	<h:commandButton value="Query Database" action="#{stfilterBean.createNewAndQuery}"/>
	</h:form>
	<h:form>
	<h:commandButton value="Modify default parameters" action="edit-default-params"/>
	</h:form>

	</td>
    </tr>
    </table>

	<hr/>
	<h:form>
		<h:commandLink action="back">
		<h:outputText value="#{stfilterBean.codeName} Main Menu"/>
		</h:commandLink>
	</h:form>
	</f:view>

 </body>
</html>

<%--
<html>
  <head>
        <title>STFILTER Project Management</title>
  </head>
 <body>
  <h2>Project Setup Page</h2>
  <p>
  Provide a name for your project and then pick the station from the map. 
  <ul>
  <li> Use <i>Pick Station from Map</i> if you want to find your station on 
an interactive map before querying it.
  </li>
  <li> Use <i>Query Station from Databae</i> if you know the station name 
and want to go directly to the database query form.
  </li>
  </ul>
  </p>
  <f:view>        
    <h:form>
    <b>Input Parameter</b>
    <h:panelGrid columns="3" border="1">
       <h:outputText value="Project Name:"/>
       <h:inputText id="projectName" value="#{stfilterBean.projectName}" 
                    required="true"/>
       <h:message for="projectName" showDetail="true" showSummary="true" errorStyle="color: red"/>

    </h:panelGrid>
    <p>
    <h:commandButton value="Pick Station from Map"
                     action="#{stfilterBean.paramsThenMap}"/>

    <h:commandButton value="Query Station from Database"
                     action="#{stfilterBean.paramsThenDB}"/>

    </h:form>
    <h:form>
    <hr/>
    <h:commandLink action="back">
        <h:outputText value="#{stfilterBean.codeName} Main Menu"/>
    </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>
--%>