<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="java.util.*, java.io.*, java.util.*, java.net.URL, java.lang.*, org.dom4j.*, org.dom4j.io.*, cgl.sensorgrid.common.*, cgl.sensorgrid.sopac.gps.GetStationsRSS,cgl.sensorgrid.gui.google.MapBean, cgl.quakesim.simplex.*, javax.faces.context.ExternalContext, javax.servlet.http.HttpServletRequest, javax.portlet.PortletRequest, javax.faces.context.FacesContext"%>


<jsp:useBean id="RSSBeanID" scope="session"
	class="cgl.sensorgrid.sopac.gps.GetStationsRSS" />
<jsp:useBean id="MapperID" scope="session"
	class="cgl.sensorgrid.gui.google.Mapper" />




<%
Vector networkNames = RSSBeanID.networkNames();

// Vector stationsVec = RSSBeanID.getAllStationsVec();
String mapcenter_x = "33.036";
String mapcenter_y = "-117.24";

String [] center_xy = RSSBeanID.getMapCenter();
mapcenter_x = center_xy[0];
mapcenter_y = center_xy[1];
%>

<%
File localFile = new File(config.getServletContext().getRealPath("stations-rss-new.xml"));
BufferedReader br=new BufferedReader(new FileReader(localFile));
StringBuffer sb = new StringBuffer();
while (br.ready()) {
	sb.append(br.readLine());
}
SAXReader reader = new SAXReader();
Document statusDoc = reader.read( new StringReader(sb.toString()) );
Element eleXml = (Element)statusDoc.getRootElement();
List stationList = eleXml.elements("station");

KMLdescriptionparser kdp = new KMLdescriptionparser();


kdp.parseXml(config.getServletContext().getRealPath("perm.xml").split("perm.xml")[0], "perm.kml");
System.out.println("[getPlacemarkSize] " + config.getServletContext().getRealPath("perm.xml").split("perm.xml")[0]);
System.out.println("[getPlacemarkSize] " + kdp.getPlacemarkSize());

int rssnewsize = stationList.size();
int permsize = kdp.getPlacemarkSize();
int totalstations = rssnewsize + permsize;

String[] latArray=new String[rssnewsize+permsize];
String[] lonArray=new String[rssnewsize+permsize];
String[] nameArray=new String[rssnewsize+permsize];

// Set upt the arrays
for(int i=0;i<stationList.size();i++) {
	Element station=(Element)stationList.get(i);
	latArray[i]=station.element("latitude").getText();
	lonArray[i]=station.element("longitude").getText();
	nameArray[i]=station.element("id").getText().toLowerCase();
}

for(int i=0;i<permsize;i++) {
	kdp.getDesc(i);
// System.out.println(kdp.getDesc(i));

	latArray[i+rssnewsize]=kdp.getEle("</b>", "<b>Latitude:").trim();
	lonArray[i+rssnewsize]=kdp.getEle("</b>", "<b>Longitude:").trim();
	nameArray[i+rssnewsize]=kdp.getEle("</b>", "<b>Monument Code:").trim().toLowerCase();
	// System.out.println(i + " " + nameArray[i+rssnewsize]);
}






%>



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
<link rel="stylesheet" type="text/css" href='<%= request.getContextPath() + "/stylesheet.css" %>'>
<link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/quakesim_style.css">

<title>Edit Project</title>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" type="text/javascript"></script>
</head>


<body onload="" onunload="GUnload()">

<f:view>
	<script language="JavaScript" type="text/javascript">
//<![CDATA[
// These are various gmap definitions.
var map;
var geoXml;
var selectedGPSstationlist = new Array();

var searcharea;

var marker_NE;
var marker_SW;

var border;

var icon_NE;
var icon_SW;
var icon_move;

// Add the markers
var pinmarker = new Array(2);
var pinmarkervalue = new Array(2);
var pin_index = -1;

var marker = new Array(<%=totalstations%>);

var markerlonlist = new Array(<%=totalstations%>);
var markerlatlist = new Array(<%=totalstations%>);
var markernamelist = new Array(<%= totalstations %>);
var markedmarkernamelist = new Array(<%= totalstations %>);
var unmarkedmarkernamelist = new Array(<%= totalstations %>);

var html = new Array(<%=totalstations%>);

var req;
var baseIcon = new GIcon();
baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
baseIcon.iconSize = new GSize(15, 20);
baseIcon.shadowSize = new GSize(10, 10);
baseIcon.iconAnchor = new GPoint(1, 10);
baseIcon.infoWindowAnchor = new GPoint(5, 1);
baseIcon.infoShadowAnchor = new GPoint(5, 5);

var colors = new Array (6);
colors[0] = "red";
colors[1] = "green";
colors[2] = "blue";
colors[3] = "black";
colors[4] = "white";
colors[5] = "yellow";
colors[6] = "purple";
colors[7] = "brown";

var networkInfo = new Array (<%=networkNames.size()%>);
for (i = 0; i < networkInfo.length; ++ i){
	networkInfo [i] = new Array (2);
}



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
//var xv = document.getElementById("Faultform:FaultLocationX");
//var yv = document.getElementById("Faultform:FaultLocationY");
//xv.value = Math.round(x*1000)/1000;
//yv.value = Math.round(y*1000)/1000;

//alert("x : " + x + " y : " + y);

var lengthVal=Math.sqrt(x*x+y*y);

length.value=Math.round(lengthVal*1000)/1000;

var strikeValue=Math.atan2(x,y)/d2r;
if (strikeValue < 0) { strikeVaule = strikeValue + 360; }
strike.value=Math.round(strikeValue*1000)/1000;
}
}


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
//var theFactor = d2r* Math.cos(d2r * latStart.value)
//        * 6378.139 * (1.0 - Math.sin(d2r * lonStart.value) * Math.sin(d2r * lonStart.value) * flatten);

var theFactor = d2r* Math.cos(d2r * latStart.value) * 6378.139 * (1.0 - Math.sin(d2r * latStart.value) * Math.sin(d2r * latStart.value) * flatten);

//var x = document.getElementById("Faultform:FaultLocationX");
//var y = document.getElementById("Faultform:FaultLocationY");

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

//x.value = parseInt(x.value);
//y.value = parseInt(y.value);
//alert(x.value/theFactor + " is " + typeof(x.value/theFactor));
lonEnd.value = (xval*1.0)/theFactor + (lonStart.value*1.0);
latEnd.value = yval/111.32 + (latStart.value*1.0);

lonEnd.value = Math.round(lonEnd.value*100)/100.0;
latEnd.value = Math.round(latEnd.value*100)/100.0;

//x.value=(lonEnd.value-lonStart.value)*theFactor;
//y.value=(latend.value-latStart.value)*111.32;

//var lengthVal=Math.sqrt((x.value)*(x.value)+(y.value)*(y.value));
// alert("x :" + x.value + " y :" + y.value);
//length.value=Math.round(lengthVal*1000)/1000;

//var strikeValue=Math.atan2(x.value,y.value)/d2r;
//strike.value=Math.round(strikeValue*1000)/1000;
}
}

function initialize() {
  
	searcharea = document.getElementById("obsvGPSMap:gpsRefStation23211b");
	icon_NE = new GIcon(); 
	icon_NE.image = 'http://maps.google.com/mapfiles/ms/micons/red-pushpin.png';
	icon_NE.shadow = '';
	icon_NE.iconSize = new GSize(32, 32);
	icon_NE.shadowSize = new GSize(22, 20);
	icon_NE.iconAnchor = new GPoint(10, 32);
	icon_NE.dragCrossImage = '';

	icon_SW = icon_NE;
	// icon_SW.image = 'http://maps.google.com/mapfiles/ms/micons/red-pushpin.png';
	// icon_SW.shadow = '';
	// icon_SW.iconSize = new GSize(32, 32);
	// icon_SW.shadowSize = new GSize(22, 20);
	// icon_SW.iconAnchor = new GPoint(10, 32);
	// icon_SW.dragCrossImage = '';


	
	map=new GMap2(document.getElementById("defaultmap"));
	map.addMapType(G_PHYSICAL_MAP);
	map.setCenter(new GLatLng(33,-117),7);
	map.addControl(new GLargeMapControl());
	map.addControl(new GMapTypeControl());
	map.addControl(new GScaleControl());





// Show the faults
	var faultKmlUrl=document.getElementById("faultKmlUrl");
	geoXml=new GGeoXml(faultKmlUrl.value, function() {

		while (!geoXml.hasLoaded()) {
// message.innerHTML="Loading...";
		}
// message.innerHTML="";
		geoXml.gotoDefaultViewport(map);
// Show the map.
		map.addOverlay(geoXml);
// overlayNetworks();
	});

	map.addOverlay(geoXml);

	var gpslist=document.getElementById("obsvGPSMap:GPSStationList");
	

	GEvent.addListener(gpslist,"click",function(e){
		var a = new Array();
		if (gpslist.value != "")
			a = gpslist.value.split(",");

		var c = 0;
		var es = e.split("/");
		for (var nA = 0; nA < markedmarkernamelist.length ; nA++)
		{
			if (markedmarkernamelist[nA] == es[0])
				c = 1;

		}

		if (c==0)
		{
			togglemarker(a,e,"none");
			gpslist.value=a;			
		}
		document.getElementById("obsvGPSMap:GPSStationNum").value = a.length;
	})



	<%
// Display the markers


	ExternalContext context = null;

	FacesContext facesContext=FacesContext.getCurrentInstance();
	if(facesContext==null)
		System.out.println("[EditProject.jsp] a null facesContext error");
	else
		System.out.println("[EditProject.jsp] the facesContext isn't null");


	try {
		context=facesContext.getExternalContext();
	}
	catch(Exception ex) {
		ex.printStackTrace();
	}	

	Object requestObj=null;
	requestObj=context.getRequest();


	SimplexBean SB = null;
	List l = null;	
	List l2 = null;

	if(requestObj instanceof PortletRequest) {
		System.out.println("[EditProject.jsp] requestObj is an instance of PortletRequest");
		SB = (SimplexBean)((PortletRequest)requestObj).getPortletSession().getAttribute("SimplexBean");
	}

	else if(requestObj instanceof HttpServletRequest) {
		System.out.println("[EditProject.jsp] requestObj is an instance of HttpServletRequest");
		SB = (SimplexBean)request.getSession().getAttribute("SimplexBean");
	}


	l = SB.getMyObservationEntryForProjectList();
	l2 = SB.getMycandidateObservationsForProjectList();


	for(int i=0;i<totalstations;i++){

		String color = "http://labs.google.com/ridefinder/images/mm_20_green.png";
		int check = 0;

		for (int nA = 0; nA < l.size() ; nA++)
		{
			if (((observationEntryForProject)l.get(nA)).getObservationName().contains(nameArray[i].toLowerCase()))
			{
				color = "http://labs.google.com/ridefinder/images/mm_20_red.png";
				check = 1;
				%>
				markedmarkernamelist[<%=nA%>]="<%=nameArray[i].toLowerCase()%>";
				unmarkedmarkernamelist[<%=i%>]="marked";

				<%
			}
		}

		for (int nA = 0 ; nA < l2.size() ; nA++)
		{
		    if (((CandidateObservation)l2.get(nA)).getStationName().contains(nameArray[i].toLowerCase())) {
		    color = "http://labs.google.com/ridefinder/images/mm_20_yellow.png";
%>

<%
		    } 
		} 

		if (check == 0) {		
			%>
			unmarkedmarkernamelist[<%=i%>] = "<%=nameArray[i].toLowerCase()%>";
			<%	

		}
		%>

		var baseIcon = new GIcon();
		baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
		baseIcon.iconSize = new GSize(15, 20);
		baseIcon.shadowSize = new GSize(10, 10);
		baseIcon.iconAnchor = new GPoint(1, 10);
		baseIcon.infoWindowAnchor = new GPoint(5, 1);
		baseIcon.infoShadowAnchor = new GPoint(5, 5);
		baseIcon.image = "<%=color%>";
		var markerOptions={ icon:baseIcon };

		var lon=<%=lonArray[i] %>;
		var lat=<%=latArray[i] %>;
		markerlonlist[<%=i%>] = lon;
		markerlatlist[<%=i%>] = lat;

		markernamelist[<%=i%>]="<%=nameArray[i]%>";
		marker[<%=i%>]=new GMarker(new GLatLng(lat,lon),markerOptions);

		html[<%=i%>]="<b>Station Name=</b>"+"<%=nameArray[i] %> <br>";
		html[<%=i%>]+="<b>Latitude:</b> "+lat+"<br>";
		html[<%=i%>]+="<b>Longitude:</b> "+lon+"<br>";
		GEvent.addListener(marker[<%=i%>],"click",function() {

			var newElement=document.getElementById("obsvGPSMap:stationName");
			newElement.setAttribute("value","<%= nameArray[i] %>");
	    
		      
			var newElement2=document.getElementById("obsvGPSMap:stationLat");
			newElement2.setAttribute("value","<%= latArray[i] %>");
			var newElement3=document.getElementById("obsvGPSMap:stationLon");
			newElement3.setAttribute("value","<%= lonArray[i] %>");

			var newElement4="<%= nameArray[i] + "/" + latArray[i] + "/" + lonArray[i]%>";
			GEvent.trigger(document.getElementById("obsvGPSMap:GPSStationList"),'click', newElement4);
			marker[<%=i%>].openInfoWindow(html[<%=i%>]);



		});
		map.addOverlay(marker[<%=i%>]);
		<%
	}
	%>

// overlayNetworks();
// printNetworkColors(networkInfo);


}

Array.prototype.remove = function(e)
{
	for(var nA = 0; nA < this.length; nA++ )
	{
// alert (this[nA] + " " + e);
		if(this[nA]==e)
			this.splice(nA,1);
	}
}


function togglemarker(array, e, option)
{
	var b = 0;

	var es = e.split("/");
	

	for(var nA = 0; nA < array.length; nA++ )
	{
		var as = array[nA].split("/");
		if(as[0] ==es[0])
			b=1;
	}

	var index=1;
	for (var nA = 0 ; nA < markernamelist.length ; nA++)
	{	
		if (markernamelist[nA] == es[0])
			index = nA;

	}

	map.removeOverlay(marker[index]);

	// Set up the icon marker
	var baseIcon=new GIcon(G_DEFAULT_ICON);
	baseIcon.iconSize=new GSize(15,20);
	baseIcon.shadowSize = new GSize(10, 10);
	baseIcon.iconAnchor = new GPoint(1, 10);
	baseIcon.infoWindowAnchor = new GPoint(5, 1);
	baseIcon.infoShadowAnchor = new GPoint(5, 5);	      


	if (b== 0 || option == "in"){
		array.push(e);
		baseIcon.image = "http://labs.google.com/ridefinder/images/mm_20_yellow.png";

	}

	if (b== 1 || option == "out") {
		array.remove(e);
		baseIcon.image = "http://labs.google.com/ridefinder/images/mm_20_green.png";
	}	


	markerOptions={ icon:baseIcon };

	var lon= markerlonlist[index];
	var lat= markerlatlist[index];
	marker[index]=new GMarker(new GLatLng(lat,lon),markerOptions);		
	GEvent.addListener(marker[index],"click",function() {

		var newElement=document.getElementById("obsvGPSMap:stationName");
		newElement.setAttribute("value",markernamelist[index]);
		var newElement2=document.getElementById("obsvGPSMap:stationLat");
		newElement2.setAttribute("value",markerlatlist[index]);
		var newElement3=document.getElementById("obsvGPSMap:stationLon");
		newElement3.setAttribute("value",markerlonlist[index]);

		var newElement4= markernamelist[index] + '/' + markerlatlist[index] + '/' +  markerlonlist[index];
		GEvent.trigger(document.getElementById("obsvGPSMap:GPSStationList"),'click', newElement4);
		marker[index].openInfoWindow(html[index]);
		



	});
	map.addOverlay(marker[index]);


}




function initialPosition() {
// map.clearOverlays();
	var bounds = map.getBounds();
	var span = bounds.toSpan();
	var newSW = new GLatLng(bounds.getSouthWest().lat() + span.lat()/3, 
			bounds.getSouthWest().lng() + span.lng()/3);
	var newNE = new GLatLng(bounds.getNorthEast().lat() - span.lat()/3, 
			bounds.getNorthEast().lng() - span.lng()/3);

	var newBounds = new GLatLngBounds(newSW, newNE);

	marker_NE = new GMarker(newBounds.getNorthEast(), {draggable: true, icon: icon_NE});
	GEvent.addListener(marker_NE, 'dragend', function() {
		updatePolyline();
		updateGPSinthebox();
	});

	marker_SW = new GMarker(newBounds.getSouthWest(), {draggable: true, icon: icon_SW});
	GEvent.addListener(marker_SW, 'dragend', function() {
		updatePolyline();
		updateGPSinthebox();
	});  

	map.addOverlay(marker_NE);
	map.addOverlay(marker_SW);
// map.addOverlay(marker_move);

	updatePolyline();
}


function updateGPSinthebox() {

	var minlat = document.getElementById("obsvGPSMap:minlat");	
	var minlon = document.getElementById("obsvGPSMap:minlon");     
	var maxlat = document.getElementById("obsvGPSMap:maxlat");
	var maxlon = document.getElementById("obsvGPSMap:maxlon");

	minlat.value = marker_SW.getPoint().lat();
	minlon.value = marker_SW.getPoint().lng();
	maxlat.value = marker_NE.getPoint().lat();
	maxlon.value = marker_NE.getPoint().lng();



	if (marker_SW.getPoint().lat() >= marker_NE.getPoint().lat())
	{
		maxlat.value = marker_SW.getPoint().lat();
		minlat.value = marker_NE.getPoint().lat();
	}

	if (marker_SW.getPoint().lng() >= marker_NE.getPoint().lng())
	{
		maxlon.value = marker_SW.getPoint().lng();
		minlon.value = marker_NE.getPoint().lng();
	}


	var a = new Array();
	var b = new Array();

	if (document.getElementById("obsvGPSMap:GPSStationList").value != "")
		b = document.getElementById("obsvGPSMap:GPSStationList").value.split(",");

	for (var nA = 0 ; nA < markernamelist.length ; nA++)
	{
		if(unmarkedmarkernamelist[nA] != "marked") {


			if ((markerlonlist[nA] <= maxlon.value && markerlonlist[nA] >= minlon.value) && 
					(markerlatlist[nA] <= maxlat.value && markerlatlist[nA] >= minlat.value))
			{			


				togglemarker(a, markernamelist[nA] + '/' + markerlatlist[nA] + '/' +  markerlonlist[nA], "none");
// b.push(markernamelist[nA]);

			}
			else
				togglemarker(a, markernamelist[nA] + '/' + markerlatlist[nA] + '/' +  markerlonlist[nA], "out");
		}


	}

	document.getElementById("obsvGPSMap:GPSStationList").value = a;
	document.getElementById("obsvGPSMap:GPSStationNum").value = a.length;

}




function updatePolyline() {
	if (border) {
		map.removeOverlay(border);
	}


	var points = [
marker_NE.getPoint(),
new GLatLng(marker_SW.getPoint().lat(), marker_NE.getPoint().lng()),
marker_SW.getPoint(),
new GLatLng(marker_NE.getPoint().lat(), marker_SW.getPoint().lng()),
marker_NE.getPoint()];
	border = new GPolyline(points, "#FF0000");

	map.addOverlay(border);
}

function toggleBorder() {
	if (searcharea.checked == false) {  

		map.removeOverlay(border);
		map.removeOverlay(marker_NE);
		map.removeOverlay(marker_SW);  
	}

	else {    

		initialPosition();
	}
}






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

function addFault(latStart, latEnd, lonStart, lonEnd) {
	var polyline = new GPolyline([
new GLatLng(latStart, lonStart),
new GLatLng(latEnd, lonEnd)], "#ff0000", 10);
	map.addOverlay(polyline);	 
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
//]]>
</script>

	<h:outputText id="lkdrq1" styleClass="header2"
		value="Project Component Manager" />
	<h:inputHidden id="faultKmlUrl" value="#{SimplexBean.faultKmlUrl}" />
	<br><h:outputText id="lkdrq2" escape="false"
		value="You must provide at least one fault and one observation point before you can run Simplex" />
	<%/* This is the main grid container */%>
	<h:panelGrid id="EditProject" columnClasses="alignTop,alignTop"
		columns="2" border="1">

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
	</h:panelGrid>

	<%@include file="ProjectComponentsPanel.jsp"%>

	<hr />
	<h:form id="dflelerkljk186">
		<h:commandLink id="dflelerkljk187" action="Simplex2-back">
			<h:outputText value="#{SimplexBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>

</f:view>

</body>
<html></html>
