<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@page import="java.util.*, java.net.URL, java.io.*, java.lang.*, org.dom4j.*, cgl.sensorgrid.common.*, org.dom4j.io.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<%@page import="java.util.*, cgl.sensorgrid.sopac.gps.GetStationsRSS,cgl.sensorgrid.gui.google.MapBean, java.io.*"%>
<%@page import="cgl.quakesim.simplex.*"%>

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
String[] latArray=new String[stationList.size()];
String[] lonArray=new String[stationList.size()];
String[] nameArray=new String[stationList.size()];

//Set upt the arrays
for(int i=0;i<stationList.size();i++) {
   Element station=(Element)stationList.get(i);
   latArray[i]=station.element("latitude").getText();
    lonArray[i]=station.element("longitude").getText();
    nameArray[i]=station.element("id").getText();
}
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

<head>
<link rel="stylesheet" type="text/css"
	href='<%= request.getContextPath() + "/stylesheet.css" %>'>

<title>Edit Project</title>
<%/*
 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAgYAii_xZWT_zf_1Dj7VvgBTf0RZ3CvQOmi-GOjEFoiamz50c8BRdcsDMSPvaTAMTVPL7sMxMzuZWCQ"
      type="text/javascript"></script>
*/%>


<script src="http://129.79.49.68:8080/Simplex3/egeoxml.js" type="text/javascript"></script>
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAgYAii_xZWT_zf_1Dj7VvgBTf0RZ3CvQOmi-GOjEFoiamz50c8BRdcsDMSPvaTAMTVPL7sMxMzuZWCQ" type="text/javascript"></script>

</head>
<body onload="" onunload="GUnload()">
<f:view>
<script language="JavaScript">

	//These are various gmap definitions.
	var geocoder=null;
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


        //Add the markers

	var pinmarker = new Array(2);
	var pinmarkervalue = new Array(2);
	var pin_index = -1;

	var marker=new Array(<%=stationList.size()%>);
	
	var markerlonlist=new Array(<%=stationList.size()%>);
	var markerlatlist=new Array(<%=stationList.size()%>);
	var markernamelist= new Array(<%= stationList.size() %>);
	var markedmarkernamelist= new Array(<%= stationList.size() %>);
	var html=new Array(<%=stationList.size()%>);


	 
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
  var lonStart=document.getElementById("Faultform:FaultLonStarts");
  var lonEnd=document.getElementById("Faultform:FaultLonEnds");
  var latStart=document.getElementById("Faultform:FaultLatStarts");
  var latEnd=document.getElementById("Faultform:FaultLatEnds");

  var length=document.getElementById("Faultform:FaultLength");
  var strike=document.getElementById("Faultform:FaultStrikeAngle");

  var d2r = Math.acos(-1.0) / 180.0;
  var flatten=1.0/298.247;
  var theFactor = d2r* Math.cos(d2r * latStart.value) * 6378.139 * (1.0 - Math.sin(d2r * lonStart.value) * Math.sin(d2r * lonStart.value) * flatten);

  var x=(lonEnd.value-lonStart.value)*theFactor;
  var y=(latEnd.value-latStart.value)*111.32;
  var lengthVal=Math.sqrt(x*x+y*y);

  length.value=Math.round(lengthVal*1000)/1000;

  var strikeValue=Math.atan2(x,y)/d2r;
  strike.value=Math.round(strikeValue*1000)/1000;
} 







function initialize() {

//////////////////
  
  searcharea = document.getElementById("obsvGPSMap:gpsRefStation23211b");
  icon_NE = new GIcon(); 
  icon_NE.image = 'http://maps.google.com/mapfiles/ms/micons/red-pushpin.png';
  icon_NE.shadow = '';
  icon_NE.iconSize = new GSize(32, 32);
  icon_NE.shadowSize = new GSize(22, 20);
  icon_NE.iconAnchor = new GPoint(10, 32);
  icon_NE.dragCrossImage = '';

  icon_SW = new GIcon(); 
  icon_SW.image = 'http://maps.google.com/mapfiles/ms/micons/red-pushpin.png';
  icon_SW.shadow = '';
  icon_SW.iconSize = new GSize(32, 32);
  icon_SW.shadowSize = new GSize(22, 20);
  icon_SW.iconAnchor = new GPoint(10, 32);
  icon_SW.dragCrossImage = '';


  geocoder = new GClientGeocoder();




///////////////////

 map=new GMap2(document.getElementById("defaultmap"));
 map.addMapType(G_PHYSICAL_MAP);
 map.setCenter(new GLatLng(33,-117),7);
 map.addControl(new GLargeMapControl());
 map.addControl(new GMapTypeControl());
 map.addControl(new GScaleControl());

if (searcharea.value == true) {
   alert ("searcharea.value : " + searcharea.value);
   initialPosition();
}




 //Show the faults
  var faultKmlUrl=document.getElementById("faultKmlUrl");
  geoXml=new GGeoXml(faultKmlUrl.value, function() {

        		while (!geoXml.hasLoaded()) {
						//message.innerHTML="Loading...";
		  		}
				//message.innerHTML="";
          	geoXml.gotoDefaultViewport(map);
			 	//Show the map.
	  		 	map.addOverlay(geoXml);
        	 	//overlayNetworks();
      });

		  map.addOverlay(geoXml);
	
	
	
	
	
<%
	//Display the markers

	SimplexBean SB = (SimplexBean)request.getSession().getAttribute("SimplexBean");
	List l = SB.getMyObservationEntryForProjectList();



	for(int i=0;i<stationList.size();i++){
	      
	      String color = "http://labs.google.com/ridefinder/images/mm_20_green.png";

	      for (int nA = 0; nA < l.size() ; nA++)
	      {
		  if (((observationEntryForProject)l.get(nA)).getObservationName().contains(nameArray[i].toLowerCase()))
		   {
			color = "http://labs.google.com/ridefinder/images/mm_20_red.png";
%>
			markedmarkernamelist[<%=nA%>]="<%=nameArray[i].toLowerCase()%>";
<%
		   }
	      }
%>

		baseIcon.image = "<%=color%>";
		markerOptions={ icon:baseIcon };

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
	     		marker[<%=i%>].openInfoWindow(html[<%=i%>]);
			var newElement=document.getElementById("obsvGPSMap:stationName");
			newElement.setAttribute("value","<%= nameArray[i] %>");
			var newElement2=document.getElementById("obsvGPSMap:stationLat");
			newElement2.setAttribute("value","<%= latArray[i] %>");
			var newElement3=document.getElementById("obsvGPSMap:stationLon");
			newElement3.setAttribute("value","<%= lonArray[i] %>");
			

			GEvent.trigger(document.getElementById("obsvGPSMap:GPSStationList"),'click', newElement.value);



		});
		map.addOverlay(marker[<%=i%>]);
<%
        }
%>

//        overlayNetworks();
//        printNetworkColors(networkInfo);



	var gpslist=document.getElementById("obsvGPSMap:GPSStationList");
	
	gpslist.value="";
	GEvent.addListener(gpslist,"click",function(e){
			var a = new Array();
			if (gpslist.value != "")
			  a = gpslist.value.split(",");
			
			var c = 0;
			for (var nA = 0; nA < markedmarkernamelist.length ; nA++)
			{
			    if (markedmarkernamelist[nA] == e)
				 c = 1;
			    
			}
			
			if (c==0)
			{
			    togglemarker(a,e,"none");
			    gpslist.value=a;
			    document.getElementById("obsvGPSMap:GPSStationNum").value = a.length;
			}
	})
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
	    for(var nA = 0; nA < array.length; nA++ )
	    {
		if(array[nA]==e)
		    b=1;
		 
	    }

	      var index=1;
	      for (var nA = 0 ; nA < markernamelist.length ; nA++)
	      {	
		  if (markernamelist[nA] == e)
		    index = nA;

	      }

	      map.removeOverlay(marker[index]);

	      //Set up the icon marker
	      var baseIcon=new GIcon(G_DEFAULT_ICON);
	      baseIcon.iconSize=new GSize(15,20);
	      baseIcon.shadowSize = new GSize(10, 10);
	      baseIcon.iconAnchor = new GPoint(1, 10);
	      baseIcon.infoWindowAnchor = new GPoint(5, 1);
	      baseIcon.infoShadowAnchor = new GPoint(5, 5);	      


	    if (b==0 || option == "in"){
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
	     		marker[index].openInfoWindow(html[index]);
			var newElement=document.getElementById("obsvGPSMap:stationName");
			newElement.setAttribute("value",markernamelist[index]);
			var newElement2=document.getElementById("obsvGPSMap:stationLat");
			newElement2.setAttribute("value",markerlatlist[index]);
			var newElement3=document.getElementById("obsvGPSMap:stationLon");
			newElement3.setAttribute("value",markerlonlist[index]);
			
			GEvent.trigger(document.getElementById("obsvGPSMap:GPSStationList"),'click', newElement.value);
			


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
	    {  // alert ("maxlon.value : " + maxlon.value + " minlon.value : " + minlon.value);
		    if ((markerlonlist[nA] <= maxlon.value && markerlonlist[nA] >= minlon.value) && 
				(markerlatlist[nA] <= maxlat.value && markerlatlist[nA] >= minlat.value))
		      {			
			      
			    
				togglemarker(a, markernamelist[nA], "none");
				b.push(markernamelist[nA]);
			    
		      }
		    else
				togglemarker(a, markernamelist[nA], "out");
		
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
if (searcharea.value == "false") {  
  searcharea.value = true;
  map.removeOverlay(border);
  map.removeOverlay(marker_NE);
  map.removeOverlay(marker_SW);  
}

else {    
    searcharea.value = false;
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

	<h:outputText id="lkdrq1" styleClass="header2" value="Project Component Manager"/>   
	<h:inputHidden id="faultKmlUrl" value="#{SimplexBean.faultKmlUrl}"/>
	<h:outputText id="lkdrq2" escape="false"
					  value="<br>You must provide at least one fault and one observation point before you can run Simplex"/>
   <%/* This is the main grid container */%>					 
	<h:panelGrid id="EditProject"
		columnClasses="alignTop,alignTop"
		columns="2" border="1">
		   
			<%@include file="DashboardPanel.jsp" %>
			<%@include file="ObservationPanel.jsp"%>			
			<%@include file="GPSMapPanel.jsp" %>
			<%@include file="ObsvListPanel.jsp" %>
			<%@include file="FaultParamsPanel.jsp"%>
			<%@include file="FaultSelectionPanel.jsp" %>
			<%@include file="FaultNameSearchPanel.jsp" %>
			<%@include file="FaultLatLonSearchPanel.jsp" %>
			<%@include file="FaultAuthorSearchPanel.jsp" %>
			<%@include file="FaultSearchResultsPanel.jsp" %>			
			<%@include file="FaultMapPanelFrame.jsp"%>				 
		</h:panelGrid>

			<%@include file="ProjectComponentsPanel.jsp" %>	

		<hr />
	<h:form id="dflelerkljk186">
		<h:commandLink id="dflelerkljk187" action="Simplex2-back">
			<h:outputText value="#{SimplexBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>

</f:view>

</body>
</html>
