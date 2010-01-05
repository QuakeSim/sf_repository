<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
	pageEncoding="ISO-8859-1"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%> 
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%> 


<%@page import="java.util.*, cgl.sensorgrid.sopac.gps.GetStationsRSS,cgl.sensorgrid.gui.google.MapBean, java.io.*"%>
<%@page import="cgl.quakesim.disloc.*"%>

<jsp:useBean id="RSSBeanID" scope="session" class="cgl.sensorgrid.sopac.gps.GetStationsRSS"/>

<jsp:useBean id="MapperID" scope="session" class="cgl.sensorgrid.gui.google.Mapper"/>
<%
Vector networkNames = RSSBeanID.networkNames();

//Vector stationsVec = RSSBeanID.getAllStationsVec();
String mapcenter_x = "33.036";
String mapcenter_y = "-117.24";

String [] center_xy = RSSBeanID.getMapCenter();
mapcenter_x = center_xy[0];
mapcenter_y = center_xy[1];

%>

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
 
 
<html> 
<head> 
<link rel="stylesheet" type="text/css" 
	href='<%= request.getContextPath() + "/stylesheet.css" %>'> 
 
<title>Edit Project</title>
<!--Google and related APIs are imported here -->

<script src="@host.base.url@@artifactId@/egeoxml.js" type="text/javascript"></script>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" type="text/javascript"></script>      
</head> 

<body onload="" onunload="GUnload()">

<script language="JavaScript">

		  //These are various gmap definitions.
	 var geocoder=null;
	 var map;
	 var geoXml;
	 var geoObsvXml;


        var req;
        var baseIcon = new GIcon();
        baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
        baseIcon.iconSize = new GSize(15, 20);
        baseIcon.shadowSize = new GSize(10, 10);
        baseIcon.iconAnchor = new GPoint(1, 10);
        baseIcon.infoWindowAnchor = new GPoint(5, 1);
        baseIcon.infoShadowAnchor = new GPoint(5, 5);

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


//This is used to calculate the length and strike angle.
function doMath(){
var lonStart=document.getElementById("Faultform:faultLon");
var lonEnd=document.getElementById("Faultform:faultLonende3r");
var latStart=document.getElementById("Faultform:faultLat");
var latEnd=document.getElementById("Faultform:faultLatendere");

var length=document.getElementById("Faultform:FaultLength");
var strike=document.getElementById("Faultform:FaultStrikeAngle");


var d2r = Math.acos(-1.0) / 180.0;
var flatten=1.0/298.247;
      var theFactor = d2r* Math.cos(d2r * latStart.value)
        * 6378.139 * (1.0 - Math.sin(d2r * lonStart.value) * Math.sin(d2r * lonStart.value) * flatten);

var x=(lonEnd.value-lonStart.value)*theFactor;
var y=(latEnd.value-latStart.value)*111.32;
var lengthVal=Math.sqrt(x*x+y*y);

length.value=Math.round(lengthVal*1000)/1000;

var strikeValue=Math.atan2(x,y)/d2r;
strike.value=Math.round(strikeValue*1000)/1000;
} 

function dataTableSelectOneRadio(radio) { 
    var id = radio.name.substring(radio.name.lastIndexOf(':')); 
    var el = radio.form.elements; 
     //alert (el.length); 
    for (var i = 0; i < el.length; i++) { 
        if (el[i].name.substring(el[i].name.lastIndexOf(':')) == id) { 
        //alert (el[i].checked) 
            el[i].checked = false; 
            el[i].checked = false; 
        } 
    } 
    radio.checked = true; 
}

function selectOne(form , button) 
{ 
  turnOffRadioForForm(form); 
  button.checked = true; 
} 
 
function turnOffRadioForForm(form) 
{ 
  for(var i=0; i<form.elements.length; i++) 
  { 
   form.elements[i].checked = false; 
  } 
} 

function initialize() {
	 map=null;
	 geoXml=null;
	 geoObsvXml=null;
	 map=new GMap2(document.getElementById("map"));
	 	  //This is the default center.
    	  map.setCenter(new GLatLng(33,-117),7);
		  map.addMapType(G_PHYSICAL_MAP);
    	  map.addControl(new GLargeMapControl());
    	  map.addControl(new GMapTypeControl());
        map.addControl(new GScaleControl());

	  //Create the network.
	  var faultKmlUrl=document.getElementById("faultKmlUrl");
//		  alert(faultKmlUrl.value);	
		  
	  //This is the kml of the faults
	  geoXml=new GGeoXml(faultKmlUrl.value, function() {
		  	var message=document.getElementById("message");
        while (!geoXml.hasLoaded()) {
			message.innerHTML="Loading...";
		  }
			message.innerHTML="";
         geoXml.gotoDefaultViewport(map);
			//Show the map.
	  		map.addOverlay(geoXml);
//        	overlayNetworks();
//         printNetworkColors(networkInfo);
      });

	  var obsvKmlUrl=document.getElementById("obsvKmlUrl");
	  geoObsvXml=new GGeoXml(obsvKmlUrl.value, function() {
		  	var message=document.getElementById("message");
        while (!geoObsvXml.hasLoaded()) {
			message.innerHTML="Loading...";
		  }
			message.innerHTML="";
         geoObsvXml.gotoDefaultViewport(map);
			//Show the map.
	  		map.addOverlay(geoObsvXml);
      });

		//Listen for user clicks
	   GEvent.addListener(map,"click",addObsvMarker);
}

function addObsvMarker(overlay,point) {
			var message="Lat: "+point.lat()+"<br>"+"Lon:"+point.lng();
	 		var newElement2=document.getElementById("obsvGPSMap:stationLat");
			newElement2.setAttribute("value",point.lat());
	 		var newElement3=document.getElementById("obsvGPSMap:stationLon");
			newElement3.setAttribute("value",point.lng());
			
			map.openInfoWindow(point,message);			    
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
            marker.openInfoWindowHtml(html);;
			 		var newElement=document.getElementById("obsvGPSMap:stationName");
					newElement.setAttribute("value",name);
			 		var newElement2=document.getElementById("obsvGPSMap:stationLat");
					newElement2.setAttribute("value",lat);
			 		var newElement3=document.getElementById("obsvGPSMap:stationLon");
					newElement3.setAttribute("value",lon);
	       });

          return marker;
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


//Needed for Firefox 2.0 compatibility
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
 
</script> 

<f:view>
	<h:outputText id="epjf1" styleClass="header2" value="Project Input"/> 
	<h:outputText id="message" value=""/>
	<h:inputHidden id="faultKmlUrl" value="#{DislocBean2.faultKmlUrl}"/>
	<h:inputHidden id="obsvKmlUrl" value="#{DislocBean2.obsvKmlUrl}"/>
        <h:outputText id="epjf1_text1" escape="false"
		value="<p>Create your geometry out of observation points and faults.<br/>The project origin will be the starting lat/lon of the first fault.</p>"/>
	<h:panelGrid id="EditProject"
		columnClasses="alignTop,alignTop"
		columns="1" border="1">

   <%@include file="DashboardPanel.jsp" %>
   <%@include file="ObsvStyle.jsp" %>
   <%@include file="ObservationParamsPanel.jsp" %>
   <%@include file="FaultParamPanel.jsp" %>
   <%@include file="FaultSearchOptionPanel.jsp" %>
   <%@include file="SearchFaultNamePanel.jsp" %>
   <%@include file="SearchFaultLatLonPanel.jsp" %>
   <%@include file="SearchFaultAuthorPanel.jsp" %>
   <%@include file="FaultDisplaySearchResultsPanel.jsp" %>


	</h:panelGrid>
   <%@include file="ProjectComponentPanel.jsp" %>
   <%@include file="footer.jsp" %>

</f:view>

</body>
</html>

