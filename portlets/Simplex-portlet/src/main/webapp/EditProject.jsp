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
		font: bold;
	}
</style>

<head>
<link rel="stylesheet" type="text/css"
	href='<%= request.getContextPath() + "/stylesheet.css" %>'>

<title>Edit Project</title>
 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBQozjQdf4FEuMBqpopduISAOADS4xTilRYX9d1ZU0uvBJwyY4gerC4Gog"
      type="text/javascript"></script>

<%/*
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here"
type="text/javascript"></script>


  <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here"
type="text/javascript"></script>      
*/%>

</head>
<body onload="initialize()" onunload="GUnload()">
<f:view>
<script language="JavaScript">

		  //These are various gmap definitions.
	 var geocoder=null;
	 var map;
	 var geoXml;
	 
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
      var theFactor = d2r* Math.cos(d2r * latStart.value)
        * 6378.139 * (1.0 - Math.sin(d2r * lonStart.value) * Math.sin(d2r * lonStart.value) * flatten);

  var x=(lonEnd.value-lonStart.value)*theFactor;
  var y=(latEnd.value-latStart.value)*111.32;
  var lengthVal=Math.sqrt(x*x+y*y);

  length.value=Math.round(lengthVal*1000)/1000;

  var strikeValue=Math.atan2(x,y)/d2r;
  strike.value=Math.round(strikeValue*1000)/1000;
} 

function initialize() {
 map=new GMap2(document.getElementById("map"));
 map.addMapType(G_PHYSICAL_MAP);
 map.setCenter(new GLatLng(33,-117),7);
 map.addControl(new GLargeMapControl());
 map.addControl(new GMapTypeControl());
 map.addControl(new GScaleControl());

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

        //Add the markers
	var marker=new Array(<%= stationList.size() %>);

	//Set up the icon marker
	var baseIcon=new GIcon(G_DEFAULT_ICON);
	baseIcon.iconSize=new GSize(15,20);
	baseIcon.shadowSize = new GSize(10, 10);
	baseIcon.iconAnchor = new GPoint(1, 10);
	baseIcon.infoWindowAnchor = new GPoint(5, 1);
	baseIcon.infoShadowAnchor = new GPoint(5, 5);
	baseIcon.image = "http://labs.google.com/ridefinder/images/mm_20_green.png";
	markerOptions={ icon:baseIcon };
	
	var html=new Array(<%=stationList.size()%>);
	
<%
	//Display the markers
	for(int i=0;i<stationList.size();i++){
%>
		var lon=<%=lonArray[i] %>;
		var lat=<%=latArray[i] %>;
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

		});
		map.addOverlay(marker[<%=i%>]);
<%
        }
%>

//        overlayNetworks();
//        printNetworkColors(networkInfo);
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
	<h:panelGrid id="panelgrid" columns="2" border="0"
					 columnClasses="alignTop, alignTop">

      <%/* This contains the left column */%>
		<h:panelGrid id="EditProject" columns="1" border="1">

		   <%/* This panel is the main control board */%>
			<%@include file="DashboardPanel.jsp" %>

			<%/* This is the observation panel */%>
			<h:panelGroup id="lkdrq7"
				  rendered="#{SimplexBean.currentEditProjectForm.renderCreateObsvCutPaste}">
					 <h:form id="obsvCutPaste">
                <h:outputText id="cutinstruct1" escape="false"
					    value="<b>Mass Observation Import:</b> Enter one observation point per line in following format: <br> ObservationType LocationEast LocationNorth Value Uncertainty <br> Values can be either space or comma separated."/>
					   <h:panelGrid id="ObsvTextArea" columns="1">
						  <h:inputTextarea id="obsvTextArea"
							   rows="20" cols="50"
							 	value="#{SimplexBean.currentEditProjectForm.obsvTextArea}"/>
					     <h:commandButton id="addObsvTextArea" value="select"
							   actionListener="#{SimplexBean.toggleAddObsvTextAreaForProject}" />
						</h:panelGrid>
					 </h:form>
			</h:panelGroup>
			
			<%/* This is GPS station map */%>
			<h:panelGroup id="lck093ks"
					rendered="#{SimplexBean.currentEditProjectForm.renderGPSStationMap}">
					 <h:form id="obsvGPSMap">
                <h:outputText id="clrlc093" escape="false"
					    value="<b>Select Stations from Map:</b> Select the stations that you want to use as observation points."/>
						 <h:panelGrid id="mapsAndCrap" columns="3" columnClasses="alignTop,alignTop">
						    <h:panelGroup id="mapncrap1">
						 <f:verbatim>
						 <div id="map" style="width: 600px; height: 400px"></div>
						 </f:verbatim>
                      </h:panelGroup>
                      <h:panelGroup id="mapncrap2">
							<h:panelGrid id="dfjdlkj" columns="2">
						 <h:outputText id="dkl34rtjf" value="Station:"/>
						 <h:inputText id="stationName" value="#{SimplexBean.gpsStationName}"/>
						 <h:outputText id="dkljr3rf" value="Latitude:"/>
						 <h:inputText id="stationLat" value="#{SimplexBean.gpsStationLat}"/>
						 <h:outputText id="dkljfer4" value="Longitude:"/>
						 <h:inputText id="stationLon" value="#{SimplexBean.gpsStationLon}"/>
             					 <h:outputText id="dkljr3dssrf" value="Ref Station?:"/>
						<h:selectBooleanCheckbox id="gpsRefStation23211s"
							value="#{SimplexBean.gpsRefStation}" />

						 <h:commandButton id="addGPSObsv" value="Add Station"
						 		actionListener="#{SimplexBean.toggleAddGPSObsvForProject}"/>
						 <h:commandButton id="closeMap" value="Close Map"
						 		actionListener="#{SimplexBean.toggleCloseMap}"/>
								</h:panelGrid>
                    <f:verbatim>
						 <div id="networksDiv">
						 </f:verbatim>
						   </h:panelGroup>
							</h:panelGrid>
					 </h:form>
			</h:panelGroup>
			
			<%/* This is the individual observations panel */%>
			<h:panelGroup id="lkdrq8"
					rendered="#{SimplexBean.currentEditProjectForm.renderCreateObservationForm}">
				<h:form id="observationform">
					<h:panelGrid id="LayerTable" columns="2" footerClass="subtitle"
						headerClass="subtitlebig" styleClass="medium"
						columnClasses="subtitle,medium">

						<f:facet name="header">
							<h:outputFormat id="output2" escape="false"
								value="<b>Input Observation Parameters</b>" />
						</f:facet>

						<h:outputText id="lkdrq9" value="Observation Name:" />
						<h:panelGroup id="lkdrq10">
							<h:inputText id="obsvName"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvName}"
								required="true" />
							<h:message id="lkdrq11" for="obsvName" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText id="lkdrq12" value="Observation Type:" />
						<h:selectOneMenu id="obsvType"
							value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvType}">
							<f:selectItem id="obsvTypeitem1" itemLabel="Displacement East"
								itemValue="1" />
							<f:selectItem id="obsvTypeitem2" itemLabel="Displacement North"
								itemValue="2" />
							<f:selectItem id="obsvTypeitem3" itemLabel="Displacement Up"
								itemValue="3" />
						</h:selectOneMenu>

						<h:outputText id="lkdrq13" value="Observation Value:" />
						<h:panelGroup id="lkdrq14">
							<h:inputText id="obsvValue"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvValue}"
								required="true" />
							<h:message id="lkdrq15" for="obsvValue" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText id="lkdrq16" value="Observation Error:" />
						<h:panelGroup id="lkdrq17">
							<h:inputText id="obsvError"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvError}"
								required="true" />
							<h:message id="lkdrq18" for="obsvError" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText id="lkdrq19" value="Location East:" />
						<h:panelGroup id="lkdrq111">
							<h:inputText id="obsvLocationEast"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvLocationEast}"
								required="true" />
							<h:message id="lkdrq112" for="obsvLocationEast" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText id="lkdrq113" value="Location North:" />
						<h:panelGroup id="lkdrq114">
							<h:inputText id="obsvLocationNorth"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvLocationNorth}"
								required="true" />
							<h:message for="obsvLocationNorth" showDetail="true"
										   id="lkdrq115"
											showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText id="lkdrq116" value="Reference Site?" />
						<h:selectOneMenu id="obsvRefSite"
							value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvRefSite}">
							<f:selectItem id="obsvRefSiteitem1" itemLabel="unselected"
								itemValue="1" />
							<f:selectItem id="obsvRefSiteitem2" itemLabel="selected"
								itemValue="-1" />
						</h:selectOneMenu>

						<h:commandButton id="addObservation" value="select"
							actionListener="#{SimplexBean.toggleAddObservationForProject}" />
					</h:panelGrid>
				</h:form>
			</h:panelGroup>

			<%/* This shows the fault form */%>
			<h:panelGroup id="kherl189"
							  rendered="#{SimplexBean.currentEditProjectForm.renderCreateNewFaultForm}">
				<h:form id="Faultform">

					<h:panelGrid id="FaultTable" columns="3" footerClass="subtitle"
						headerClass="subtitlebig" styleClass="medium"
						columnClasses="subtitle,medium">

						<f:facet name="header">
							<h:outputFormat id="output3" escape="false"
								value="<b>Input Fault Geometry</b>" />
						</f:facet>

						<h:outputText id="lkdrq117" value="Fault Name:" />
						<h:panelGroup id="lkdrq118">
							<h:inputText id="FaultName"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultName}"
								required="true" />
							<h:message for="FaultName" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:outputText value=""/>

						<h:outputText id="lkdrq119" value="Location X:" />
						<h:panelGroup id="lkdrq1181">
							<h:inputText id="FaultLocationX"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLocationX}"
								required="true" />
							<h:message id="lkdrq1182" for="FaultLocationX" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultOriginXVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultOriginXVary}" />

						<h:outputText id="lkdrq1183" value="Location Y:" />
						<h:panelGroup id="lkdrq1184">
							<h:inputText id="FaultLocationY"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLocationY}"
								required="true" />
							<h:message id="lkdrq1185" for="FaultLocationY" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultOriginYVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultOriginYVary}" />

						<h:outputText id="lkdrq1186" value="Length:" />
						<h:panelGroup id="lkdrq1187">
							<h:inputText id="FaultLength"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLength}"
								required="true" />
							<h:message id="lkdrq1188" for="FaultLength" showDetail="true" showSummary="false"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultLengthVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLengthVary}" />
						<h:outputText id="lkdrq1189" value="Width:" />
						<h:panelGroup id="lkdrq11811">
							<h:inputText id="FaultWidth"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultWidth}"
								required="true" />
							<h:message id="lkdrq11812" for="FaultWidth" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultWidthVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultWidthVary}" />

						<h:outputText id="lkdrq11813" value="Depth:" />
						<h:panelGroup id="lkdrq11814">
							<h:inputText id="FaultDepth"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultDepth}"
								required="true" />
							<h:message id="lkdrq11815" for="FaultDepth" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultDepthVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultDepthVary}" />

						<h:outputText id="lkdrq11816" value="Dip Angle:" />
						<h:panelGroup id="lkdrq11817">
							<h:inputText id="FaultDipAngle"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultDipAngle}"
								required="true" />
							<h:message id="lkdrq11818" for="FaultDipAngle" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultDipAngleVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultDipAngleVary}" />

						<h:outputText id="lkdrq11819" value="Strike Angle:" />
						<h:panelGroup id="dflelerkljk1">
							<h:inputText id="FaultStrikeAngle"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultStrikeAngle}"
								required="false" />
							<h:message id="dflelerkljk2" for="FaultStrikeAngle" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultStrikeAngleVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultStrikeAngleVary}" />


						<h:outputText id="dflelerkljk3" value="Dip Slip:" />
						<h:panelGroup id="dflelerkljk4">
							<h:inputText id="FaultSlip"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultSlip}"
								required="true" />
							<h:message for="FaultSlip" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultDipSlipVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultDipSlipVary}" />

						<h:outputText  id="dflelerkljk5" value="Strike Slip:" />
						<h:panelGroup id="dflelerkljk6">
							<h:inputText id="FaultRakeAngle"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultRakeAngle}"
								required="true" />
							<h:message  id="dflelerkljk7" for="FaultRakeAngle" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultStrikeSlipVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultStrikeSlipVary}" />

						<h:outputText id="dflelerkljk8" value="Fault Lon Starts:" />
						<h:panelGroup id="dflelerkljk9">
							<h:inputText id="FaultLonStarts"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLonStarts}" />
						</h:panelGroup>
						<h:outputText  id="dflelerkljk10" value="optional" />

						<h:outputText id="dflelerkljk11" value="Fault Lat Starts:" />
						<h:panelGroup id="dflelerkljk12">
							<h:inputText id="FaultLatStarts"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLatStarts}" />
						</h:panelGroup>
						<h:outputText  id="dflelerkljk13" value="optional" />

						<h:outputText  id="dflelerkljk14" value="Fault Lon Ends:" />
						<h:panelGroup id="dflelerkljk15">
							<h:inputText id="FaultLonEnds"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLonEnds}" />
						</h:panelGroup>
						<h:outputText  id="dflelerkljk16" value="optional" />

						<h:outputText id="dflelerkljk17" value="Fault Lat Ends:" />
						<h:panelGroup id="dflelerkljk18">
							<h:inputText id="FaultLatEnds"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLatEnds}" />
						</h:panelGroup>
						<h:outputText  id="dflelerkljk19" value="optional" />

						<h:commandButton id="addfault" value="Set Values"
							actionListener="#{SimplexBean.toggleAddFaultForProject}" />
				      <f:verbatim>
				         <input type="button" name="Update"
					 				 value="Do Math"
									 onclick="doMath()"/>
 				      </f:verbatim>
					<f:facet name="footer"> 
					   <h:outputFormat id="output2" escape="false" 
						value="Click 'Do Math' to udpate length and strike. 
						       Click 'Set Values' when you are done." /> 
					</f:facet> 

					</h:panelGrid>
				</h:form>
         </h:panelGroup>

         <%/* Fault selection control board panel */%>
			<h:panelGroup id="fredfd23" rendered="#{SimplexBean.currentEditProjectForm.renderAddFaultSelectionForm}">
				<h:form id="faultselection">
					<h:panelGrid id="AddFaultSelection" columns="1"
						footerClass="subtitle" headerClass="subtitlebig"
						styleClass="medium" columnClasses="subtitle,medium">
						<h:panelGroup id="dflelerkljk111">
							<h:outputFormat  id="dflelerkljk112" escape="false"
								value="<b>Fault Database Selection</b><br><br>" />
							<h:outputFormat  id="dflelerkljk113" escape="false"
								value="You may select faults from the Fault Database using author search, <br>latitude/longitude bounding box, or by viewing the master list (long).<br><br>" />
							<h:outputFormat  id="dflelerkljk114" escape="false"
								value="Please choose a radio button and click <b>Select</b>.<br><br>" />
						</h:panelGroup>

						<h:panelGroup id="dflelerkljk115">
							<h:selectOneRadio layout="pageDirection" id="subscriptionssss"
								value="#{SimplexBean.currentEditProjectForm.faultSelectionCode}">
								<f:selectItem id="item01" itemLabel="Search by fault name."
									itemValue="SearchByFaultName" />
								<f:selectItem id="item02"
									itemLabel="Search by Lat/Lon bounding box."
									itemValue="SearchByLatLon" />
								<f:selectItem id="item03" itemLabel="Search by author."
									itemValue="SearchByAuthor" />
								<f:selectItem id="item04" itemLabel="View all faults."
									itemValue="ViewAllFaults" />
							</h:selectOneRadio>
							<h:commandButton id="button122" value="Make Selection"
								actionListener="#{SimplexBean.currentEditProjectForm.toggleFaultSelection}" />
						</h:panelGroup>
					</h:panelGrid>
				</h:form>
			</h:panelGroup>

			<%/* This is the fault name search */%>
			<h:panelGroup id="erea3412" rendered="#{SimplexBean.currentEditProjectForm.renderSearchByFaultNameForm}">
				<h:form id="faultsearchByNameform">
					<h:panelGrid id="FaultSearchName" columns="1"
						footerClass="subtitle" headerClass="subtitlebig"
						styleClass="medium" columnClasses="subtitle,medium">
						<h:outputFormat  id="dflelerkljk116" escape="false"
							value="<b>Search Fault DB by Fault Name</b><br><br>" />
						<h:panelGroup id="dflelerkljk117">
							<h:panelGroup id="dflelerkljk118">
								<h:outputText escape="false"  id="dflelerkljk119"
									value="Enter the name of the fault. The search will return partial matches." />
								<h:outputText escape="false" value="<br>" />
							</h:panelGroup>

							<h:panelGroup id="dflelerkljk120">
								<h:inputText id="Fault_Name"
									value="#{SimplexBean.currentEditProjectForm.forSearchStr}"
									required="true" />
								<h:message id="dflelerkljk121" for="Fault_Name" showDetail="true" showSummary="true"
									errorStyle="color: red" />
								<h:commandButton id="dflelerkljk122" value="Query"
									actionListener="#{SimplexBean.currentEditProjectForm.toggleFaultSearchByName}" />
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGrid>
				</h:form>
			</h:panelGroup>
			
			<%/* This is the lat/lon search */%>
			<h:panelGroup id="erre3454" rendered="#{SimplexBean.currentEditProjectForm.renderSearchByLatLonForm}">
				<h:form id="faultlatlonsearchform">

					<h:panelGrid id="FaultLatLonSearch" columns="1"
						footerClass="subtitle" headerClass="subtitlebig"
						styleClass="medium" columnClasses="subtitle,medium">
						<h:outputFormat escape="false" id="dflelerkljk123"
							value="<b>Search Fault DB by Bounding Latitude and Longitude</b><br><br>" />
						<h:outputFormat escape="false" id="dflelerkljk124"
							value="Enter the starting and ending latitude and longitude values (in decimal degrees) of the search bounding box. All faults completely within the bounding box will be returned.<br><br>" />


						<h:panelGrid columns="2" border="0" id="dflelerkljk125">
							<h:outputText id="dflelerkljk126" value="Starting Latitude: " />
							<h:panelGroup id="dflelerkljk127">
								<h:inputText id="StartingLatitude"
									value="#{SimplexBean.currentEditProjectForm.faultLatStart}"
									required="true" />
								<h:message id="dflelerkljk128" for="StartingLatitude" showDetail="true"
									showSummary="true" errorStyle="color: red" />
							</h:panelGroup>

							<h:outputText id="dflelerkljk129" value="Ending Latitude: " />
							<h:panelGroup id="dflelerkljk131">
								<h:inputText id="EndingLatitude"
									value="#{SimplexBean.currentEditProjectForm.faultLatEnd}"
									required="true" />
								<h:message id="dflelerkljk132" for="EndingLatitude" showDetail="true"
									showSummary="true" errorStyle="color: red" />
							</h:panelGroup>
							<h:outputText id="dflelerkljk133" value="Starting Longitude: " />
							<h:panelGroup id="dflelerkljk134">
								<h:inputText id="StartingLongitude"
									value="#{SimplexBean.currentEditProjectForm.faultLonStart}"
									required="true" />
								<h:message id="dflelerkljk135" for="StartingLongitude" showDetail="true"
									showSummary="true" errorStyle="color: red" />
							</h:panelGroup>
							<h:outputText id="dflelerkljk136" value="Ending Longitude: " />
							<h:panelGroup id="dflelerkljk137">
								<h:inputText id="EndingLongitude"
									value="#{SimplexBean.currentEditProjectForm.faultLonEnd}"
									required="true" />
								<h:message id="dflelerkljk138" for="EndingLongitude" showDetail="true"
									showSummary="true" errorStyle="color: red" />
							</h:panelGroup>
							<h:panelGroup id="dflelerkljk139">
								<h:commandButton id="dflelerkljk140" value="Query"
									actionListener="#{SimplexBean.currentEditProjectForm.toggleFaultSearchByLonLat}" />
							</h:panelGroup>
						</h:panelGrid>

					</h:panelGrid>
				</h:form>
         </h:panelGroup>

			<%/* Fault author search */%>
         <h:panelGroup id="doierl323" rendered="#{SimplexBean.currentEditProjectForm.renderSearchByAuthorForm}">
				<h:form id="FaultAuthorSearchform">
					
					<h:panelGrid id="FaultAuthorSearch" columns="1"
						footerClass="subtitle" headerClass="subtitlebig"
						styleClass="medium" columnClasses="subtitle,medium">
						<h:outputFormat id="dflelerkljk141" escape="false"
							value="<b>Search Fault DB by Author</b><br><br>" />
						<h:panelGroup id="dflelerkljk142">
							<h:panelGroup id="dflelerkljk143">
								<h:outputText escape="false" id="dflelerkljk144"
									value="Enter the last name of the principal author of the desired fault descriptions. The search will do partial matches." />
								<h:outputText id="dflelerkljk145" escape="false" value="<br>" />
							</h:panelGroup>

							<h:panelGroup id="dflelerkljk146">
								<h:inputText id="FaultAuthorForSearch"
									value="#{SimplexBean.currentEditProjectForm.forSearchStr}"
									required="true" />
								<h:message id="dflelerkljk147" for="FaultAuthorForSearch" showDetail="true"
									showSummary="true" errorStyle="color: red" />
								<h:commandButton id="dflelerkljk148" value="Query"
									actionListener="#{SimplexBean.currentEditProjectForm.toggleFaultSearchByAuthor}" />
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGrid>
				</h:form>
			</h:panelGroup>

			<%/* Fault search results */%>
			<h:panelGroup id="ere43dr342d" rendered="#{SimplexBean.currentEditProjectForm.renderAddFaultFromDBForm}">
				<h:form id="SelectFaultDBEntryForm">

				<h:outputText id="dbisdownklj" escape="false"
								  value="<b>Error:</b> Data base returned no response.  Contact portal administrator."
								  rendered="#{empty SimplexBean.currentEditProjectForm.myFaultDBEntryList}"/>

					<h:dataTable  id="dflelerkljk149"
					   rendered="#{!empty SimplexBean.currentEditProjectForm.myFaultDBEntryList}"
						value="#{SimplexBean.currentEditProjectForm.myFaultDBEntryList}"
						var="myentry1"
						binding="#{SimplexBean.currentEditProjectForm.myFaultDataTable}">

						<h:column>
							<f:facet name="header">
								<h:outputText id="dflelerkljk150" escape="false" value="<b>FaultName</b>" />
							</f:facet>
							<h:selectOneRadio id="dflelerkljk151" layout="pageDirection"
								valueChangeListener="#{SimplexBean.currentEditProjectForm.handleFaultsRadioValueChange}"
								onchange="dataTableSelectOneRadio(this)"
								onclick="dataTableSelectOneRadio(this)">
								<f:selectItems value="#{myentry1.faultName}" />
							</h:selectOneRadio>
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText id="dflelerkljk152" escape="false" value="<b>SegmentName</b>" />
							</f:facet>
							<h:outputText id="dflelerkljk153" value="#{myentry1.faultSegmentName}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText id="dflelerkljk154" escape="false" value="<b>Author1</b>" />
							</f:facet>
							<h:outputText id="dflelerkljk155" value="#{myentry1.faultAuthor}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<h:outputText id="dflelerkljk156" escape="false" value="<b>Segment Coordinates</b>" />
							</f:facet>
							<h:outputText id="dflelerkljk157" value="#{myentry1.faultSegmentCoordinates}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<h:outputText id="dflelerkljk158" escape="false" value="<b>Action</b>" />
							</f:facet>
							<h:commandLink  id="dflelerkljk159"
								actionListener="#{SimplexBean.currentEditProjectForm.handleFaultEntryEdit}">
								<h:outputText id="dflelerkljk160" value="Get" />
							</h:commandLink>
						</h:column>
					</h:dataTable>
					<h:commandButton rendered="#{!empty SimplexBean.currentEditProjectForm.myFaultDBEntryList}"
										  id="SelectFaultDBEntry" value="SelectFaultDBEntry"
										  actionListener="#{SimplexBean.currentEditProjectForm.toggleSelectFaultDBEntry}" />
				</h:form>
			</h:panelGroup>
		</h:panelGrid>

		<%/*This is the right hand column */%>
		<h:panelGrid id="ProjectComponentList" columns="1" border="0">
			<h:panelGroup id="dflelerkljk161">
				<h:form id="UpdateSelectFaultsForm"
					rendered="#{!empty SimplexBean.myFaultEntryForProjectList}">
					<h:panelGrid id="dflelerkljk162" columns="1" border="1">
						<h:panelGroup id="dflelerkljk163">
								<h:outputFormat id="dflelerkljk165" escape="false"
									value="<b>Fault Components</b>">
								</h:outputFormat>

							<h:dataTable border="1"
											  id="dflelerkljk166"
											  value="#{SimplexBean.myFaultEntryForProjectList}" var="myentry3">
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk167" escape="false" value="<b>Name</b>" />
									</f:facet>
									<h:outputText id="dflelerkljk168" value="#{myentry3.faultName}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk169" escape="false" value="<b>View</b>" />
									</f:facet>
									<h:selectBooleanCheckbox value="#{myentry3.view}"
																	  id="dflelerkljk170"
																	  onchange="selectOne(this.form,this)"
																	  onclick="selectOne(this.form,this)" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk171" escape="false" value="<b>Remove</b>" />
									</f:facet>
									<h:selectBooleanCheckbox id="dflelerkljk172" value="#{myentry3.delete}"/>
								</h:column>
							</h:dataTable>
						</h:panelGroup>
					</h:panelGrid>
					<h:commandButton id="SelectFault4proj" value="UpdateFault"
						actionListener="#{SimplexBean.toggleUpdateFaultProjectEntry}" />
				</h:form>

				<h:form id="UpdateSelectObservationForm"
					rendered="#{!empty SimplexBean.myObservationEntryForProjectList}">
						<h:panelGroup id="dflelerkljk174">
						  <h:panelGrid id="obsvpanelgrid" columns="1" border="1">
								<h:outputFormat escape="false" id="dflelerkljk176"
									value="<b>Observation Components</b>">
								</h:outputFormat>
								<h:commandButton id="viewSimplexObsv" value="Display/Hide"
									actionListener="#{SimplexBean.currentEditProjectForm.toggleShowObsvEntries}"/>

							<h:dataTable border="1" id="dflelerkljk177"
							   rendered="#{SimplexBean.currentEditProjectForm.renderObsvEntries}"
								value="#{SimplexBean.myObservationEntryForProjectList}"
								var="myentry4">
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk178" escape="false" value="<b>Name</b>" />
									</f:facet>
									<h:outputText id="dflelerkljk179" value="#{myentry4.observationName}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk180" escape="false" value="<b>View</b>" />
									</f:facet>
									<h:selectBooleanCheckbox value="#{myentry4.view}"
																	  id="dflelerkljk181"
																	  onchange="selectOne(this.form,this)"
																	  onclick="selectOne(this.form,this)" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk182" escape="false" value="<b>Remove</b>" />
									</f:facet>
									<h:selectBooleanCheckbox  id="dflelerkljk183" value="#{myentry4.delete}"
										onchange="selectOne(this.form,this)"
										onclick="selectOne(this.form,this)" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflel332" escape="false" value="<b>Ref Site</b>" />
									</f:facet>
									<h:outputText id="dfl33rejk183" value="#{myentry4.refSite}"/>
								</h:column>
							</h:dataTable>
						</h:panelGrid>
						</h:panelGroup>
					<h:commandButton id="SelectObservation4proj"
					   rendered="#{SimplexBean.currentEditProjectForm.renderObsvEntries}"
						value="Update Observation"
						actionListener="#{SimplexBean.toggleUpdateObservationProjectEntry}" />
				</h:form>
			</h:panelGroup>
		</h:panelGrid>
	</h:panelGrid>

		<hr />
	<h:form id="dflelerkljk186">
		<h:commandLink id="dflelerkljk187" action="Simplex2-back">
			<h:outputText value="#{SimplexBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>

</f:view>

</body>
</html>
