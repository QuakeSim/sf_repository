<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
	pageEncoding="ISO-8859-1"%> 
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
 
 
<html> 
<head> 
<link rel="stylesheet" type="text/css" 
	href='<%= request.getContextPath() + "/stylesheet.css" %>'> 
 
<title>Edit Project</title>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBRxYpIIOz9ynlSKjbx-4JMuN5JjrhR5gSOcKdieYppOZ4_yzZc_Ti15qw"
      type="text/javascript"></script>
</head> 

<body onload="initialize()" onunload="GUnload()">

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
	<h:outputText styleClass="header2" value="Project Input"/> 
	<h:outputText id="message" value=""/>
	<h:inputHidden id="faultKmlUrl" value="#{DislocBean.faultKmlUrl}"/>
	<h:inputHidden id="obsvKmlUrl" value="#{DislocBean.obsvKmlUrl}"/>
        <h:outputText escape="false" 
					  value="<p>Create your geometry out of observation points and faults.  
                        <br/> The project origin 
	                     will be the starting lat/lon of the first fault.</p>"/> 
	<h:panelGrid id="EditProject"  
		columnClasses="alignTop,alignTop" 
		columns="2" border="1"> 
		<h:form id="selectproj"> 
			<h:panelGroup id="pg1"> 
 
				<h:outputFormat id="stuff1" escape="false" 
					value="<b>Project Name: #{DislocBean.projectName} </b><br>" /> 
				<h:outputFormat id="stuffe3io4" escape="false" 
					value="<b>Project Origin (lat/lon):</b> (#{DislocBean.currentParams.originLat}, 
											        #{DislocBean.currentParams.originLon}) <br>" /> 
				<h:outputFormat id="stufw3f1" escape="false" 
					value="<b>Observation Style: #{DislocBean.currentParams.observationPointStyle} </b><br>" /> 
 
				<h:selectOneRadio layout="pageDirection" id="subscriptions" 
					value="#{DislocBean.projectSelectionCode}"> 
				<f:selectItem id="item0w3" 
						itemLabel="Set Observation Style: Choose between grid and scatter points." 
						itemValue="ChooseObsvStyleForm"/> 

					<f:selectItem id="item1" 
						itemLabel="View Project Info: Verify and update project information." 
						itemValue="CreateObservationGrid" /> 
					<f:selectItem id="item2" 
						itemLabel="Create New Fault: Click to specify geometry for a fault segment." 
						itemValue="CreateNewFault" /> 

					<f:selectItem id="item4" 
						itemLabel="Add Fault from DB: Click to select a fault segment from the database." 
						itemValue="AddFaultSelection" /> 

					<f:selectItem id="item021"
						itemLabel="Use Map: View faults and pick observation points on an interactive map."
						itemValue="ShowMap" />
				</h:selectOneRadio> 
				<h:commandButton id="button1" value="Make Selection" 
					actionListener="#{DislocBean.toggleProjectSelection}"> 
				</h:commandButton> 
			</h:panelGroup> 
		</h:form> 
		
		<h:panelGroup id="lck0ere93ks"
					rendered="#{DislocBean.renderChooseObsvStyleForm}">
		      <h:form id="obsvStyleForm">
                	<h:outputText id="clrr33asz3" escape="false"
					    value="<b>Select Sites:</b>Click to choose scatter point."/>
					  <h:selectOneRadio layout="pageDirection" id="ere34ionssss" 
							value="#{DislocBean.obsvStyleSelectionCode}"> 
							<f:selectItem id="item021" 
											  itemLabel="Rectangular grid of observation points" 
											  itemValue="GridStyle" /> 
							<f:selectItem id="item0332" 
								itemLabel="Scattered observation points." 
								itemValue="ScatterStyle" /> 
					  </h:selectOneRadio> 
					  <h:commandButton id="chooseAStyle" value="Choose Style"
						 		actionListener="#{DislocBean.toggleSetObsvStyle}"/>
					 </h:form>
		        </h:panelGroup>

			<h:panelGroup id="lck093ks"
					rendered="#{DislocBean.renderMap}">
					 <h:form id="obsvGPSMap">
                <h:outputText id="clrlc093" escape="false"
					    value="<b>Select Sites:</b>Click to choose scatter point."/>
						 <h:panelGrid id="mapsAndCrap" columns="2" columnClasses="alignTop,alignTop">
						    <h:panelGroup id="mapncrap1">
						 <f:verbatim>
						 <div id="map" style="width: 600px; height: 400px"></div>
						 </f:verbatim>
                      </h:panelGroup>
                      <h:panelGroup id="mapncrap2">
							<h:panelGrid id="dfjdlkj" columns="2" 
							   rendered="#{empty DislocBean.usesGridPoints
											 || !DislocBean.usesGridPoints}">
						 <h:outputText id="dkljr3rf" value="Latitude:"/>
						 <h:inputText id="stationLat" value="#{DislocBean.gpsStationLat}"/>
						 <h:outputText id="dkljfer4" value="Longitude:"/>
						 <h:inputText id="stationLon" value="#{DislocBean.gpsStationLon}"/>
						 <h:commandButton id="addGPSObsv" value="Add Observation Point"
						 		actionListener="#{DislocBean.toggleAddPointObsvForProject}"/>

						 <h:commandButton id="closeMap" value="Close Map"
						 		actionListener="#{DislocBean.toggleCloseMap}"/>
								</h:panelGrid>
						   </h:panelGroup>
							</h:panelGrid>
					 </h:form>
			</h:panelGroup>
 
			<h:panelGroup id="stuff4">
	          	   <h:form id="obsvform" rendered="#{DislocBean.renderDislocGridParamsForm}"> 
				<h:panelGrid id="ObsvTable" columns="2" footerClass="subtitle" 
					headerClass="subtitlebig" styleClass="medium" 
					columnClasses="subtitle,medium"> 
 
					<f:facet name="header"> 
						<h:outputFormat id="output2" escape="false" 
							value="<b>Project Origin and Style</b>" /> 
					</f:facet> 

 					<h:outputText  id="stuff223" value="Project Origin Lat:" /> 
					<h:panelGroup  id="stuff543"> 
						<h:inputText id="origin_latlieew" 
							value="#{DislocBean.currentParams.originLat}" required="true" /> 
					</h:panelGroup> 

 					<h:outputText  id="stuff2434" value="Project Origin Lon:" /> 
					<h:panelGroup  id="stuff24sx5"> 
						<h:inputText id="origin_lonlkd" 
							value="#{DislocBean.currentParams.originLon}" required="true" /> 
					</h:panelGroup> 
					
 					<h:outputText  id="stuf334" value="Observation Style:" /> 
					<h:panelGroup>
					<h:outputText id="stylejkrejonlkd" 
					              value="#{DislocBean.currentParams.observationPointStyle}"/>
					</h:panelGroup>	      
          			</h:panelGrid>

				<h:panelGrid id="erep912e3" rendered="#{!DislocBean.usesGridPoints}">
					<f:facet name="header"> 
						<h:outputFormat id="outputere2" escape="false" 
							value="<b>Scatter-Style Observation Points </b>" /> 
					</f:facet> 
					<h:dataTable value="#{DislocBean.myPointObservationList}"
									 binding="#{DislocBean.myScatterPointsTable}"
									 var="xypoints"
									 id="xypointsq3u">
 						<h:column>
					     <f:facet name="header"> 
						   <h:outputFormat id="outputere2" escape="false" 
							   value="<b>Lat</b>" /> 
					     </f:facet> 

						  <h:outputText id="akjlatelkr34je" value="#{xypoints.latPoint}"/>
    					</h:column>
						<h:column>
					     <f:facet name="header"> 
						   <h:outputFormat id="outputere2" escape="false" 
							   value="<b>Lon</b>" /> 
					     </f:facet> 
						  <h:outputText id="rerdad62lon" value="#{xypoints.lonPoint}"/>
    					</h:column>
					</h:dataTable>
            </h:panelGrid>

				<h:panelGrid id="eiurojd93" columns="2" rendered="#{DislocBean.usesGridPoints}">
					<f:facet name="header"> 
						<h:outputFormat id="outputere2" escape="false" 
							value="<b>Define Grid of Observation Points </b>" /> 
					</f:facet> 

 					<h:outputText  id="stuff2" value="Grid Minimum X Value:" /> 
					<h:panelGroup  id="stuff5"> 
						<h:inputText id="minx" 
							value="#{DislocBean.currentParams.gridMinXValue}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff3" value="X Spacing:" /> 
					<h:panelGroup  id="stuff6"> 
						<h:inputText id="xspacing" 
							value="#{DislocBean.currentParams.gridXSpacing}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff7" value="X Iterations" /> 
					<h:panelGroup  id="stuff8"> 
						<h:inputText id="xiterations" 
							value="#{DislocBean.currentParams.gridXIterations}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff9" value="Grid Minimum Y Value:" /> 
					<h:panelGroup  id="stuff10"> 
						<h:inputText id="miny" 
							value="#{DislocBean.currentParams.gridMinYValue}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff11" value="Y Spacing:" /> 
					<h:panelGroup   id="pg2"> 
						<h:inputText id="yspacing" 
							value="#{DislocBean.currentParams.gridYSpacing}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff12" value="Y Iterations" /> 
					<h:panelGroup  id="stuff13"> 
						<h:inputText id="yiterations" 
							value="#{DislocBean.currentParams.gridYIterations}" required="true" /> 
					</h:panelGroup> 
				</h:panelGrid>
 
					<h:commandButton id="addobservation" value="select" 
						actionListener="#{DislocBean.toggleAddObservationsForProject}" /> 

			</h:form> 
 
			<h:form id="Faultform" rendered="#{DislocBean.renderCreateNewFaultForm}"> 
				<h:panelGrid id="FaultTable" columns="2" footerClass="subtitle" 
					headerClass="subtitlebig" styleClass="medium" 
					columnClasses="subtitle,medium"> 
 
					<f:facet name="header"> 
						<h:outputFormat id="output3" escape="false" 
							value="<b>Input Fault Geometry </b>" /> 
					</f:facet> 
 
					<h:outputText  id="stuff15" value="Fault Name:" /> 
					<h:panelGroup  id="stuff16"> 
						<h:inputText id="FaultName" 
							value="#{DislocBean.currentFault.faultName}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff17" value="Location X:" /> 
					<h:panelGroup  id="stuff18"> 
						<h:inputText id="FaultLocationX" 
							value="#{DislocBean.currentFault.faultLocationX}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff19" value="Location Y:" /> 
					<h:panelGroup  id="stuff20"> 
						<h:inputText id="FaultLocationY" 
							value="#{DislocBean.currentFault.faultLocationY}" required="true" /> 
					</h:panelGroup> 

					<h:outputText value="Fault Origin Latitude:" />
			      <h:panelGroup>
						<h:inputText id="faultLat" value="#{DislocBean.currentFault.faultLatStart}"
										 required="true" />
					</h:panelGroup>

					<h:outputText value="Fault Origin Longitude" />
					<h:panelGroup>
					<h:inputText id="faultLon" value="#{DislocBean.currentFault.faultLonStart}"
									 required="true" />
					</h:panelGroup>

					<h:outputText value="Fault End Latitude:" />
			      <h:panelGroup>
						<h:inputText id="faultLatendere" value="#{DislocBean.currentFault.faultLatEnd}"
										 required="true" />
					</h:panelGroup>

					<h:outputText value="Fault End Longitude" />
					<h:panelGroup>
					<h:inputText id="faultLonende3r" value="#{DislocBean.currentFault.faultLonEnd}"
									 required="true" />
					</h:panelGroup>

 
					<h:outputText  id="stuff21" value="Length:" /> 
					<h:panelGroup  id="stuff22"> 
						<h:inputText id="FaultLength" 
							value="#{DislocBean.currentFault.faultLength}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff23" value="Width:" /> 
					<h:panelGroup  id="stuff24"> 
						<h:inputText id="FaultWidth" 
							value="#{DislocBean.currentFault.faultWidth}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff25" value="Depth:" /> 
					<h:panelGroup  id="stuff26"> 
						<h:inputText id="FaultDepth" 
							value="#{DislocBean.currentFault.faultDepth}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff27" value="Dip Angle:" /> 
					<h:panelGroup  id="stuff28"> 
						<h:inputText id="FaultDipAngle" 
							value="#{DislocBean.currentFault.faultDipAngle}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff29" value="Dip Slip:" /> 
					<h:panelGroup  id="stuff30"> 
						<h:inputText id="FaultSlip" 
							value="#{DislocBean.currentFault.faultDipSlip}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff31" value="Strike Angle:" /> 
					<h:panelGroup  id="stuff32"> 
						<h:inputText id="FaultStrikeAngle" 
							value="#{DislocBean.currentFault.faultStrikeAngle}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff33" value="Strike Slip:" /> 
					<h:panelGroup  id="stuff35"> 
						<h:inputText id="FaultStrikeSlip" 
							value="#{DislocBean.currentFault.faultStrikeSlip}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff34" value="Tensile Slip:" /> 
					<h:panelGroup  id="stuff36"> 
						<h:inputText id="FaultTensileSlip" 
							value="#{DislocBean.currentFault.faultTensileSlip}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff37" value="Lame Lambda:" /> 
					<h:panelGroup  id="stuff38"> 
						<h:inputText id="LameLambda" 
							value="#{DislocBean.currentFault.faultLameLambda}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff39" value="Lame Mu:" /> 
					<h:panelGroup  id="stuff40"> 
						<h:inputText id="LameMu" 
							value="#{DislocBean.currentFault.faultLameMu}" required="true" /> 
					</h:panelGroup> 
 
					<h:commandButton id="addfault" value="select" 
						actionListener="#{DislocBean.toggleAddFaultForProject}" /> 
				</h:panelGrid> 
			</h:form> 
 
			<h:form id="faultselection" 
				rendered="#{DislocBean.renderAddFaultSelectionForm}"> 
				<h:panelGrid id="AddFaultSelection" columns="1" 
					footerClass="subtitle" headerClass="subtitlebig" 
					styleClass="medium" columnClasses="subtitle,medium"> 
					<h:panelGroup   id="stuff41"> 
						<h:outputFormat escape="false" 
						    id="stuff42" 
							value="<b>Fault Database Selection</b><br><br>" /> 
						<h:outputFormat escape="false" 
						     id="stuff43" 
							value="You may select faults from the Fault Database using author search, latitude/longitude bounding box, or by viewing the master list (long).<br><br>" /> 
						<h:outputFormat escape="false" 
						    id="pg2" 
							value="Please choose a radio button and click <b>Select</b>.<br><br>" /> 
					</h:panelGroup> 
 
					<h:panelGroup id="stuff44"> 
						<h:selectOneRadio layout="pageDirection" id="subscriptionssss" 
							value="#{DislocBean.faultSelectionCode}"> 
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
							actionListener="#{DislocBean.toggleFaultSelection}" /> 
					</h:panelGroup> 
				</h:panelGrid> 
			</h:form> 
 
			<h:form id="faultsearchByNameform" 
				rendered="#{DislocBean.renderSearchByFaultNameForm}"> 
				<h:panelGrid id="FaultSearchName" columns="1" footerClass="subtitle" 
					headerClass="subtitlebig" styleClass="medium" 
					columnClasses="subtitle,medium"> 
					<h:outputFormat escape="false" 
					     id="stuff44" 
						value="<b>Search Fault DB by Fault Name</b><br><br>" /> 
					<h:panelGroup   id="stuff45"> 
						<h:panelGroup   id="stuff46"> 
							<h:outputText escape="false" 
							     id="stuff47" 
								value="Enter the name of the fault. The search will return partial matches." /> 
							<h:outputText id="stuff48" escape="false" value="<br>" /> 
						</h:panelGroup> 
 
						<h:panelGroup  id="stuff49"> 
							<h:inputText id="Fault_Name" value="#{DislocBean.forSearchStr}" 
								required="true" /> 
							<h:message for="Fault_Name" showDetail="true" showSummary="true" 
							     id="stuff50" 
								errorStyle="color: red" /> 
							<h:commandButton  id="stuff51" value="Query" 
								actionListener="#{DislocBean.toggleFaultSearchByName}" /> 
						</h:panelGroup> 
					</h:panelGroup> 
				</h:panelGrid> 
			</h:form> 
 
			<h:form id="faultlatlonsearchform" 
				rendered="#{DislocBean.renderSearchByLatLonForm}"> 
				<h:panelGrid id="FaultLatLonSearch" columns="1" 
					footerClass="subtitle" headerClass="subtitlebig" 
					styleClass="medium" columnClasses="subtitle,medium"> 
					<h:outputFormat escape="false"  id="stuff52" 
						value="<b>Search Fault DB by Bounding Latitude and Longitude</b><br><br>" /> 
					<h:outputFormat escape="false"  id="stuff53" 
						value="Enter the starting and ending latitude and longitude values (in decimal degrees) of the search bounding box. All faults completely within the bounding box will be returned.<br><br>" /> 
 
					<h:panelGrid columns="2" border="0"  id="stuff54"> 
						<h:outputText value="Starting Latitude: "  id="stuff55"/> 
						<h:panelGroup  id="stuff56"> 
							<h:inputText id="StartingLatitude" 
							    id="stuff57" 
								value="#{DislocBean.faultLatStart}" required="true" /> 
							<h:message for="StartingLatitude" showDetail="true" 
							    id="stuff58" 
								showSummary="true" errorStyle="color: red" /> 
						</h:panelGroup> 
 
						<h:outputText  id="stuff59" value="Ending Latitude: " /> 
						<h:panelGroup  id="stuff60"> 
							<h:inputText id="EndingLatitude" value="#{DislocBean.faultLatEnd}" 
								required="true" /> 
							<h:message for="EndingLatitude" showDetail="true" 
							     id="stuff61" 
								showSummary="true" errorStyle="color: red" /> 
						</h:panelGroup> 
						<h:outputText value="Starting Longitude: "   id="stuff62"/> 
						<h:panelGroup  id="stuff63"> 
							<h:inputText id="StartingLongitude" 
							     id="stuff64" 
								value="#{DislocBean.faultLonStart}" required="true" /> 
							<h:message for="StartingLongitude" showDetail="true" 
							     id="stuff65" 
								showSummary="true" errorStyle="color: red" /> 
						</h:panelGroup> 
						<h:outputText value="Ending Longitude: "   id="stuff66"/> 
						<h:panelGroup   id="stuff67"> 
							<h:inputText id="EndingLongitude" value="#{DislocBean.faultLonEnd}" 
								required="true" /> 
							<h:message for="EndingLongitude" showDetail="true" 
							    id="stuff68" 
								showSummary="true" errorStyle="color: red" /> 
						</h:panelGroup> 
						<h:panelGroup> 
 
							<h:commandButton value="Query" 
							     id="stuff69" 
								actionListener="#{DislocBean.toggleFaultSearchByLonLat}" /> 
						</h:panelGroup> 
					</h:panelGrid> 
				</h:panelGrid> 
			</h:form> 
 
			<h:form id="FaultAuthorSearchform" 
				rendered="#{DislocBean.renderSearchByAuthorForm}"> 
				<h:panelGrid id="FaultAuthorSearch" columns="1" 
					footerClass="subtitle" headerClass="subtitlebig" 
					styleClass="medium" columnClasses="subtitle,medium"> 
					<h:outputFormat escape="false" 
						value="<b>Search Fault DB by Author</b><br><br>" /> 
					<h:panelGroup  id="pg3"> 
						<h:panelGroup  id="pg4"> 
							<h:outputText escape="false" 
							     id="stuff70" 
								value="Enter the last name of the principal author of the desired fault descriptions. The search will do partial matches." /> 
							<h:outputText  id="stuff71" escape="false" value="<br>" /> 
						</h:panelGroup> 
 
						<h:panelGroup  id="pg5"> 
							<h:inputText id="FaultAuthorForSearch" 
								value="#{DislocBean.forSearchStr}" required="true" /> 
							<h:message for="FaultAuthorForSearch" showDetail="true" 
							    id="stuff72" 
								showSummary="true" errorStyle="color: red" /> 
							<h:commandButton value="Query" 
							    id="stuff73" 
								actionListener="#{DislocBean.toggleFaultSearchByAuthor}" /> 
						</h:panelGroup> 
					</h:panelGroup> 
				</h:panelGrid> 
			</h:form> 
 
			<h:form id="SelectFaultDBEntryForm" 
				rendered="#{DislocBean.renderAddFaultFromDBForm}"> 
				<h:dataTable value="#{DislocBean.myFaultDBEntryList}" var="myentry1" 
				    id="stuff74" 
					binding="#{DislocBean.myFaultDataTable}"> 
 
					<h:column  id="pg6"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>FaultName</b>" /> 
						</f:facet> 
						<h:selectOneRadio layout="pageDirection" 
						    id="stuff75" 
							valueChangeListener="#{DislocBean.handleFaultsRadioValueChange}" 
							onchange="dataTableSelectOneRadio(this)" 
							onclick="dataTableSelectOneRadio(this)"> 
							<f:selectItems value="#{myentry1.faultName}" /> 
						</h:selectOneRadio> 
					</h:column> 
 
					<h:column  id="pg7"> 
						<f:facet name="header"> 
							<h:outputText  id="pg8" escape="false" value="<b>SegmentName</b>" /> 
						</f:facet> 
						<h:outputText value="#{myentry1.faultSegmentName}" /> 
					</h:column> 
 
					<h:column  id="pg9"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>Author1</b>" /> 
						</f:facet> 
						<h:outputText  id="pg10" value="#{myentry1.faultAuthor}" /> 
					</h:column> 
 
					<h:column  id="lereid1"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>Segment Coordinates</b>" /> 
						</f:facet> 
						<h:outputText  id="liered1" value="#{myentry1.faultSegmentCoordinates}" /> 
					</h:column> 
					<h:column  id="lieraed1"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>Action</b>" /> 
						</f:facet> 
						<h:commandLink id="stuff76" 
							actionListener="#{DislocBean.handleFaultEntryEdit}"> 
							<h:outputText value="Get" /> 
						</h:commandLink> 
					</h:column> 
				</h:dataTable> 
				<h:commandButton id="SelectFaultDBEntry" value="SelectFaultDBEntry" 
					actionListener="#{DislocBean.toggleSelectFaultDBEntry}" /> 
			</h:form> 
 
		</h:panelGroup> 
	</h:panelGrid> 
 
	<h:panelGroup  id="stuff77" 
		rendered="#{!empty DislocBean.myFaultEntryForProjectList 
					   || !empty DislocBean.myObsvEntryForProjectList}"> 
 
	<h:outputText  id="stuff78" styleClass="header2" value="Current Project Components"
						rendered="#{!empty DislocBean.myFaultEntryForProjectList 
					   || !empty DislocBean.myObsvEntryForProjectList}"/> 
 
	<h:panelGrid id="ProjectComponentList" columns="2" border="1" 
			columnClasses="alignTop, alignTop"> 
 
		<h:panelGroup id="stuff79" rendered="#{!empty DislocBean.myFaultEntryForProjectList}"> 
			<h:form id="UpdateSelectFaultsForm">
				<h:panelGrid columns="1" border="1"  id="stuff80"> 
					<h:panelGroup  id="lid2"> 
						<h:panelGrid  id="lid3" columns="1"> 
							<h:outputText escape="false" value="<b>Faults</b>"/> 
						</h:panelGrid> 
 
						<h:dataTable border="1" id="stuff81" 
							value="#{DislocBean.myFaultEntryForProjectList}" var="myentry3"> 
							<h:column  id="lid4"> 
								<f:facet name="header"> 
									<h:outputText  id="lid5" escape="false" value="<b>Name</b>"> 
                           </h:outputText> 
								</f:facet> 
								<h:outputText value="#{myentry3.faultName}" 
								    id="stuff82"/> 
							</h:column> 
							<h:column  id="lid6"> 
								<f:facet name="header"> 
									<h:outputText escape="false" value="<b>View</b>" /> 
								</f:facet> 
								<h:selectBooleanCheckbox value="#{myentry3.view}" 
                            id="stuff83" 
									onchange="selectOne(this.form,this)" 
									onclick="selectOne(this.form,this)" /> 
 
							</h:column> 
							<h:column  id="lid7"> 
								<f:facet name="header"> 
									<h:outputText escape="false" value="<b>Remove</b>" /> 
								</f:facet> 
								<h:selectBooleanCheckbox value="#{myentry3.delete}" 
								    id="lid8" 
									onchange="selectOne(this.form,this)" 
									onclick="selectOne(this.form,this)" /> 
							</h:column> 
 
						</h:dataTable> 
					</h:panelGroup> 
 
				</h:panelGrid> 
				<h:commandButton id="SelectFault4proj" value="UpdateFault" 
					actionListener="#{DislocBean.toggleUpdateFaultProjectEntry}" /> 
 
			</h:form> 
 
			<h:form id="UpdateSelectedParamsForm" 
				rendered="#{!empty DislocBean.myObsvEntryForProjectList}"> 
				<h:panelGrid columns="1" border="1"  id="stuff84"> 
					<h:panelGroup  id="lid9"> 
						<h:panelGrid  id="stuff85" columns="1"> 
							<h:outputText  id="stuff86" 
                       escape="false" value="<b>Observations</b>"/> 
						</h:panelGrid> 
 
						<h:dataTable border="1"  id="stuff87" 
							value="#{DislocBean.myObsvEntryForProjectList}" var="myentry4"> 
							<h:column  id="lid110"> 
								<f:facet name="header"> 
									<h:outputText  id="lkj1" escape="false" value="<b>Name</b>"> 
                           </h:outputText> 
								</f:facet> 
								<h:outputText  id="lkj2" value="Observations" /> 
							</h:column> 
							<h:column  id="lkj11"> 
								<f:facet name="header"> 
									<h:outputText  id="lkj3" escape="false" value="<b>View</b>" /> 
								</f:facet> 
								<h:selectBooleanCheckbox value="#{myentry4.view}" 
                            id="stuff88" 
									onchange="selectOne(this.form,this)" 
									onclick="selectOne(this.form,this)" /> 
 
							</h:column> 
							<h:column  id="lkj4"> 
								<f:facet name="header"> 
									<h:outputText escape="false" value="<b>Remove</b>" /> 
								</f:facet> 
								<h:selectBooleanCheckbox value="#{myentry4.delete}" 
									onchange="selectOne(this.form,this)" 
									 id="lkj5" 
									onclick="selectOne(this.form,this)" /> 
							</h:column> 
 
						</h:dataTable> 
					</h:panelGroup> 
 
				</h:panelGrid> 
				<h:commandButton id="updateObsv" value="Update Observations" 
					actionListener="#{DislocBean.toggleUpdateProjectObservations}" /> 
 
			</h:form> 
		</h:panelGroup> 
 
		<h:form id="RunDisloc"
				rendered="#{!(empty DislocBean.myFaultEntryForProjectList) 
							   && !(empty DislocBean.myObsvEntryForProjectList)}" > 
			<h:panelGrid columns="1"  
             id="stuff89" 
				footerClass="subtitle" 
				headerClass="subtitlebig" styleClass="medium" 
				columnClasses="subtitle,medium"> 
 
				<h:outputFormat escape="false"  id="stuff90" 
					value="<b>Run Disloc</b><br><br>" /> 
				<h:outputFormat escape="false"   id="stuff91" 
					value="Click the button below to run Disloc.<br><br>" /> 
 
					<h:commandButton value="Run Disloc" 
						action="#{DislocBean.runBlockingDislocJSF}" /> 
			</h:panelGrid> 
		</h:form> 
	</h:panelGrid> 
   </h:panelGroup> 
 
   <%@ include file="footer.jsp" %>
 
</f:view> 
 
</body> 
</html> 
