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
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here"
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
	<h:outputText styleClass="header2" value="Project Input"/> 
	<h:outputText id="message" value=""/>
	<h:inputHidden id="faultKmlUrl" value="#{DislocBean2.faultKmlUrl}"/>
	<h:inputHidden id="obsvKmlUrl" value="#{DislocBean2.obsvKmlUrl}"/>
        <h:outputText escape="false" 
					  value="<p>Create your geometry out of observation points and faults.  
                        <br/> The project origin 
	                     will be the starting lat/lon of the first fault.</p>"/> 
	<h:panelGrid id="EditProject"  
		columnClasses="alignTop,alignTop" 
		columns="2" border="1"> 

   <%@include file="DashboardPanel.jsp" %>
   <%@include file="ObsvStyle.jsp" %>
   <%@include file="ScatterMap.jsp" %>
 
			<h:panelGroup id="stuff4">
	       <h:form id="obsvform" rendered="#{DislocBean2.renderDislocGridParamsForm}"> 
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
							value="#{DislocBean2.currentParams.originLat}" required="true" /> 
					</h:panelGroup> 

 					<h:outputText  id="stuff2434" value="Project Origin Lon:" /> 
					<h:panelGroup  id="stuff24sx5"> 
						<h:inputText id="origin_lonlkd" 
							value="#{DislocBean2.currentParams.originLon}" required="true" /> 
					</h:panelGroup> 
					
 					<h:outputText  id="stuf334" value="Observation Style:" /> 
					<h:panelGroup>
					<h:outputText id="stylejkrejonlkd" 
					              value="#{DislocBean2.currentParams.observationPointStyle}"/>
					</h:panelGroup>	      
				</h:panelGrid>

				<h:panelGrid id="erep912e3" rendered="#{!DislocBean2.usesGridPoints}">
					<f:facet name="header"> 
						<h:outputFormat id="outputere2" escape="false" 
							value="<b>Scatter-Style Observation Points </b>" /> 
					</f:facet> 
					<h:dataTable value="#{DislocBean2.myPointObservationList}"
									 binding="#{DislocBean2.myScatterPointsTable}"
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
						<h:column>
						   <h:commandButton value="Delete" id="delxypointr4ero"
												  actionListener="DislocBean2.deleteScatterPoint"/> 
						</h:column>
					</h:dataTable>
            </h:panelGrid>

				<h:panelGrid id="eiurojd93" columns="2" rendered="#{DislocBean2.usesGridPoints}">
					<f:facet name="header"> 
						<h:outputFormat id="outputere2" escape="false" 
							value="<b>Define Grid of Observation Points </b>" /> 
					</f:facet> 

 					<h:outputText  id="stuff2" value="Grid Minimum X Value:" /> 
					<h:panelGroup  id="stuff5"> 
						<h:inputText id="minx" 
							value="#{DislocBean2.currentParams.gridMinXValue}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff3" value="X Spacing:" /> 
					<h:panelGroup  id="stuff6"> 
						<h:inputText id="xspacing" 
							value="#{DislocBean2.currentParams.gridXSpacing}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff7" value="X Iterations" /> 
					<h:panelGroup  id="stuff8"> 
						<h:inputText id="xiterations" 
							value="#{DislocBean2.currentParams.gridXIterations}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff9" value="Grid Minimum Y Value:" /> 
					<h:panelGroup  id="stuff10"> 
						<h:inputText id="miny" 
							value="#{DislocBean2.currentParams.gridMinYValue}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff11" value="Y Spacing:" /> 
					<h:panelGroup   id="pg2"> 
						<h:inputText id="yspacing" 
							value="#{DislocBean2.currentParams.gridYSpacing}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff12" value="Y Iterations" /> 
					<h:panelGroup  id="stuff13"> 
						<h:inputText id="yiterations" 
							value="#{DislocBean2.currentParams.gridYIterations}" required="true" /> 
					</h:panelGroup> 
				</h:panelGrid>
 
					<h:commandButton id="addobservation" value="select" 
						actionListener="#{DislocBean2.toggleAddObservationsForProject}" /> 

			</h:form> 
 
			<h:form id="Faultform" rendered="#{DislocBean2.renderCreateNewFaultForm}"> 
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
							value="#{DislocBean2.currentFault.faultName}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff17" value="Location X:" /> 
					<h:panelGroup  id="stuff18"> 
						<h:inputText id="FaultLocationX" 
							value="#{DislocBean2.currentFault.faultLocationX}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff19" value="Location Y:" /> 
					<h:panelGroup  id="stuff20"> 
						<h:inputText id="FaultLocationY" 
							value="#{DislocBean2.currentFault.faultLocationY}" required="true" /> 
					</h:panelGroup> 

					<h:outputText value="Fault Origin Latitude:" />
			      <h:panelGroup>
						<h:inputText id="faultLat" value="#{DislocBean2.currentFault.faultLatStart}"
										 required="true" />
					</h:panelGroup>

					<h:outputText value="Fault Origin Longitude" />
					<h:panelGroup>
					<h:inputText id="faultLon" value="#{DislocBean2.currentFault.faultLonStart}"
									 required="true" />
					</h:panelGroup>

					<h:outputText value="Fault End Latitude:" />
			      <h:panelGroup>
						<h:inputText id="faultLatendere" value="#{DislocBean2.currentFault.faultLatEnd}"
										 required="true" />
					</h:panelGroup>

					<h:outputText value="Fault End Longitude" />
					<h:panelGroup>
					<h:inputText id="faultLonende3r" value="#{DislocBean2.currentFault.faultLonEnd}"
									 required="true" />
					</h:panelGroup>

 
					<h:outputText  id="stuff21" value="Length:" /> 
					<h:panelGroup  id="stuff22"> 
						<h:inputText id="FaultLength" 
							value="#{DislocBean2.currentFault.faultLength}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff23" value="Width:" /> 
					<h:panelGroup  id="stuff24"> 
						<h:inputText id="FaultWidth" 
							value="#{DislocBean2.currentFault.faultWidth}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff25" value="Depth:" /> 
					<h:panelGroup  id="stuff26"> 
						<h:inputText id="FaultDepth" 
							value="#{DislocBean2.currentFault.faultDepth}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff27" value="Dip Angle:" /> 
					<h:panelGroup  id="stuff28"> 
						<h:inputText id="FaultDipAngle" 
							value="#{DislocBean2.currentFault.faultDipAngle}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff29" value="Dip Slip:" /> 
					<h:panelGroup  id="stuff30"> 
						<h:inputText id="FaultSlip" 
							value="#{DislocBean2.currentFault.faultDipSlip}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff31" value="Strike Angle:" /> 
					<h:panelGroup  id="stuff32"> 
						<h:inputText id="FaultStrikeAngle" 
							value="#{DislocBean2.currentFault.faultStrikeAngle}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff33" value="Strike Slip:" /> 
					<h:panelGroup  id="stuff35"> 
						<h:inputText id="FaultStrikeSlip" 
							value="#{DislocBean2.currentFault.faultStrikeSlip}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff34" value="Tensile Slip:" /> 
					<h:panelGroup  id="stuff36"> 
						<h:inputText id="FaultTensileSlip" 
							value="#{DislocBean2.currentFault.faultTensileSlip}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff37" value="Lame Lambda:" /> 
					<h:panelGroup  id="stuff38"> 
						<h:inputText id="LameLambda" 
							value="#{DislocBean2.currentFault.faultLameLambda}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff39" value="Lame Mu:" /> 
					<h:panelGroup  id="stuff40"> 
						<h:inputText id="LameMu" 
							value="#{DislocBean2.currentFault.faultLameMu}" required="true" /> 
					</h:panelGroup> 
 
					<h:commandButton id="addfault" value="Set Values" 
						actionListener="#{DislocBean2.toggleAddFaultForProject}" /> 
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
 
			<h:form id="faultselection" 
				rendered="#{DislocBean2.renderAddFaultSelectionForm}"> 
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
							value="#{DislocBean2.faultSelectionCode}"> 
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
							actionListener="#{DislocBean2.toggleFaultSelection}" /> 
					</h:panelGroup> 
				</h:panelGrid> 
			</h:form> 
 
			<h:form id="faultsearchByNameform" 
				rendered="#{DislocBean2.renderSearchByFaultNameForm}"> 
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
							<h:inputText id="Fault_Name" value="#{DislocBean2.forSearchStr}" 
								required="true" /> 
							<h:message for="Fault_Name" showDetail="true" showSummary="true" 
							     id="stuff50" 
								errorStyle="color: red" /> 
							<h:commandButton  id="stuff51" value="Query" 
								actionListener="#{DislocBean2.toggleFaultSearchByName}" /> 
						</h:panelGroup> 
					</h:panelGroup> 
				</h:panelGrid> 
			</h:form> 
 
   <%@ include file="FaultLatLonSearchPanel.jsp" %>  
 
   <%@ include file="FaultAuthorSearchPanel.jsp" %>  
 
   <%@ include file="FaultSearchResultsPanel.jsp" %>  
		</h:panelGroup> 
	</h:panelGrid> 

   <%@ include file="ProjectComponentPanel.jsp" %>  
 
   <%@ include file="footer.jsp" %>
 
</f:view> 
 
</body> 
</html> 
