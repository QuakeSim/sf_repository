<h:panelGroup id="lck093ks"
				  rendered="#{SimplexBean.currentEditProjectForm.renderGPSStationMap}">
  <h:inputHidden id="faultKmlUrl" value="#{SimplexBean.faultKmlUrl}" />
  
  <%
  //<![CDATA[
  //Set the map center. Hard-coded, need a better way.
  String mapcenter_x = "33.036";
  String mapcenter_y = "-117.24";
  
  //Read and parse the stations list from the old XML file.
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
  
  //This is a KML file we got from somewhere. Parse it to extract stations.
  KMLdescriptionparser kdp = new KMLdescriptionparser();
  kdp.parseXml(config.getServletContext().getRealPath("perm.xml").split("perm.xml")[0], "perm.kml");
  
  //The total number of stations.  We need a more general way to handle this
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
  //]]>
  %>
  
  <f:verbatim>
	 <script type="text/javascript">
		//<![CDATA[
		//These are variables needed for the GPS station selection features.
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

// Are these used?
var pinmarker = new Array(2);
var pinmarkervalue = new Array(2);
var pin_index = -1;

//Arrays to handle GPS stations, both marked and unmarked.
var marker = new Array(<%=totalstations%>);
var markerlonlist = new Array(<%=totalstations%>);
var markerlatlist = new Array(<%=totalstations%>);
var markernamelist = new Array(<%= totalstations %>);
var markedmarkernamelist = new Array(<%= totalstations %>);
var unmarkedmarkernamelist = new Array(<%= totalstations %>);

//This is an array to handle HTML in the GPS marker popup window.
var html = new Array(<%=totalstations%>);

//Obsolete?
var req;
var colors = new Array (6);
colors[0] = "red";
colors[1] = "green";
colors[2] = "blue";
colors[3] = "black";
colors[4] = "white";
colors[5] = "yellow";
colors[6] = "purple";
colors[7] = "brown";

//Styling for the GPS marker icons.  This is repeated below, so one should be deleted.
var baseIcon = new GIcon();
baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
baseIcon.iconSize = new GSize(15, 20);
baseIcon.shadowSize = new GSize(10, 10);
baseIcon.iconAnchor = new GPoint(1, 10);
baseIcon.infoWindowAnchor = new GPoint(5, 1);
baseIcon.infoShadowAnchor = new GPoint(5, 5);
//Now done with all the initialization work.

//--------------------------------------------------
// This function initializes the area selection feature.
// REVIEW: Probably deserves a more specific name.
//--------------------------------------------------
function initialize() {
	//This is associated with the checkbox.
	searcharea = document.getElementById("obsvGPSMap:gpsRefStation23211b");

	//Create the icons for the search area.
	icon_NE = new GIcon(); 
	icon_NE.image = 'http://maps.google.com/mapfiles/ms/micons/red-pushpin.png';
	icon_NE.shadow = '';
	icon_NE.iconSize = new GSize(32, 32);
	icon_NE.shadowSize = new GSize(22, 20);
	icon_NE.iconAnchor = new GPoint(10, 32);
	icon_NE.dragCrossImage = '';
	icon_SW = icon_NE;

	//Create the map area, center it, etc.
	map=new GMap2(document.getElementById("defaultmap"));
	map.addMapType(G_PHYSICAL_MAP);
	map.setCenter(new GLatLng(33,-117),7);
	map.addControl(new GLargeMapControl());
	map.addControl(new GMapTypeControl());
	map.addControl(new GScaleControl());

	//This centers the map on fault. 
   //REVIEW: this method really looks mangled.
	var faultKmlUrl=document.getElementById("faultKmlUrl");
	geoXml=new GGeoXml(faultKmlUrl.value, function() {
		while (!geoXml.hasLoaded()) {
		}
		geoXml.gotoDefaultViewport(map);
		// Show the map.
		map.addOverlay(geoXml);
	});
	map.addOverlay(geoXml);

	//Get the list of selected GPS stations
	var gpslist=document.getElementById("obsvGPSMap:GPSStationList");

	//Handle some formatting issues for the stations.	They are comman-separated
   //in the above list.
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
	});

	//Note we are still in the initialize() function at this point, althoug we need to break
   //out into so Java code.
	//REVIEW: this is really akward and hard to edit, maintain.

	<%
   // Display the markers
   // Get the context and from it the request object so that we can get the 
   // SimplexBean.  Surely there is an easier way.
	ExternalContext context = null;
	FacesContext facesContext=FacesContext.getCurrentInstance();
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

	//Get the SimplexBean from the session in either the portlet or standalone case.
	if(requestObj instanceof PortletRequest) {
		// System.out.println("[EditProject.jsp] requestObj is an instance of PortletRequest");
		SB = (SimplexBean)((PortletRequest)requestObj).getPortletSession().getAttribute("SimplexBean");
	}

	else if(requestObj instanceof HttpServletRequest) {
		// System.out.println("[EditProject.jsp] requestObj is an instance of HttpServletRequest");
		SB = (SimplexBean)request.getSession().getAttribute("SimplexBean");
	}

	//Finally, get the list of observations already in the project and also
	//candidate observations.
	//REVIEW: these variable names are terrible.
	l = SB.getMyObservationEntryForProjectList();
	l2 = SB.getMycandidateObservationsForProjectList();	

	for(int i=0;i<totalstations;i++){
		String color = "http://labs.google.com/ridefinder/images/mm_20_green.png";
		int check = 0;
		
		//Determine which markers correspond to added observation points and color them red.
		for (int nA = 0; nA < l.size() ; nA++) {
			if (((observationEntryForProject)l.get(nA)).getObservationName().contains(nameArray[i].toLowerCase()))
			{
				color = "http://labs.google.com/ridefinder/images/mm_20_red.png";
				check = 1;
				%>
				//Switch back to javascript and add the marked station to the 
				//appropriate array.
				markedmarkernamelist[<%=nA%>]="<%=nameArray[i].toLowerCase()%>";
				unmarkedmarkernamelist[<%=i%>]="marked";
				//Now back to Java to close the if and for statements.
				<%
			}
		}

		//Determine if the marked station is a candidate for addition.  Candidate stations 
		//have been clicked but not yet added to the project's observation list.  These 
		//are colored yellow.
		for (int nA = 0 ; nA < l2.size() ; nA++) {
		    if (((CandidateObservation)l2.get(nA)).getStationName().contains(nameArray[i].toLowerCase())) {
		    color = "http://labs.google.com/ridefinder/images/mm_20_yellow.png";		    
		    } 
		} 

		//Add the candidate station to the unmarked array list.
		//REVIEW:This if statement is probably unnecessary.
		if (check == 0) {		
			%>
			unmarkedmarkernamelist[<%=i%>] = "<%=nameArray[i].toLowerCase()%>";
			<%	
		}
		%>

		//REVIEW: This is redundant with the earlier baseIcon defs.
		//var baseIcon = new GIcon();
		//baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
		//baseIcon.iconSize = new GSize(15, 20);
		//baseIcon.shadowSize = new GSize(10, 10);
		//baseIcon.iconAnchor = new GPoint(1, 10);
		//baseIcon.infoWindowAnchor = new GPoint(5, 1);
		//baseIcon.infoShadowAnchor = new GPoint(5, 5);
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

document.getElementById("obsvGPSMap:GPSStationNum").value = <%=l2.size()%>;

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


function togglemarker(array, e, option) {
	var b = 0;
	var es = e.split("/");
	
	for(var nA = 0; nA < array.length; nA++ ) {
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


	if (b==0 || option == "in"){
		array.push(e);
		baseIcon.image = "http://labs.google.com/ridefinder/images/mm_20_yellow.png";

	}

	if (b==1 || option == "out") {
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

//This plots the selection area box. 
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
//		updateGPSinthebox();
	});

	marker_SW = new GMarker(newBounds.getSouthWest(), {draggable: true, icon: icon_SW});
	GEvent.addListener(marker_SW, 'dragend', function() {
		updatePolyline();
//		updateGPSinthebox();
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

	if (marker_SW.getPoint().lat() >= marker_NE.getPoint().lat()) {
		maxlat.value = marker_SW.getPoint().lat();
		minlat.value = marker_NE.getPoint().lat();
	}

	if (marker_SW.getPoint().lng() >= marker_NE.getPoint().lng()) {
		maxlon.value = marker_SW.getPoint().lng();
		minlon.value = marker_NE.getPoint().lng();
	}

	//REVIEW: Terrible variable names.
	var a = new Array();
	var b = new Array();

	if (document.getElementById("obsvGPSMap:GPSStationList").value != "")
		b = document.getElementById("obsvGPSMap:GPSStationList").value.split(",");

		
	for (var nA = 0 ; nA < markernamelist.length ; nA++) {
		if(unmarkedmarkernamelist[nA] != "marked") {

			if ((markerlonlist[nA] <= maxlon.value && markerlonlist[nA] >= minlon.value) && 
					(markerlatlist[nA] <= maxlat.value && markerlatlist[nA] >= minlat.value)) {			
					  togglemarker(a, markernamelist[nA] 
					  						+ '/' + markerlatlist[nA] 
											+ '/' +  markerlonlist[nA], "none");

			}
			else
				togglemarker(a, markernamelist[nA] 
									 + '/' + markerlatlist[nA] 
									 + '/' +  markerlonlist[nA], "out");
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
function addFault(latStart, latEnd, lonStart, lonEnd) {
	var polyline = new GPolyline([
   new GLatLng(latStart, lonStart),
   new GLatLng(latEnd, lonEnd)], "#ff0000", 10);
	map.addOverlay(polyline);	 
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

function displayChosenGPS(){
alert("Updating the map");
updatePolyline();
		updateGPSinthebox();
		}
//]]>
	 </script>
  </f:verbatim>
  <%-- Visible part starts here  --%>
  <h:form id="obsvGPSMap">
	 <h:panelGrid id="simplexSelectionMapGrid"
					  columns="1"
					  border="1">
		<h:outputText id="clrlc093" escape="false"
						  value="<b>Select Stations from Map:</b> Select the stations that you want to use as observation points. Then, fetch the values and add them to your project observation list."/>
		<h:panelGrid id="mapsAndCrap" columns="3" columnClasses="alignTop,alignTop">
		  <h:panelGroup id="mapncrap1">
			 <f:verbatim> 
				<div id="defaultmap" style="width: 800px; height: 600px"></div>
			 </f:verbatim>
		  </h:panelGroup>
		  <h:panelGroup id="mapncrap2">
			 <h:panelGrid id="manncraplayoutgrid"
							  border="1"
							  columns="1">
				<h:panelGroup id="mapncrapLayoutGroup1">
				  <h:outputText id="SimplexGPSInstructions1"
									 value="First, select stations to import into your project."/>
				  <h:panelGrid id="dfjdlkj" 
									border="0"
									columns="2">
					 
					 <h:panelGrid id="nploe" columns="2">
						<h:outputText id="dkb2" value="stations.xml"/>
						<h:selectBooleanCheckbox id="gpssource1" 
														 onchange="togglesource('box1')" 
														 value="#{SimplexBean.gpssource1}" />
					 </h:panelGrid>
					 
					 <h:panelGrid id="npbbv" columns="2">
						<h:outputText id="dkb3" value="perm.kml"/>
						<h:selectBooleanCheckbox id="gpssource2" 
													  onchange="togglesource('box2')" 
													  value="#{SimplexBean.gpssource2}"/>  
				  </h:panelGrid>
				
				  <h:outputText id="dkljrabd2" value="Current Station:"/> 
				  <h:inputText id="stationName" 
									value="#{SimplexBean.gpsStationName}" 
									style="text-align:center;width:45px" 
									readonly="true"/>
				  
				  <h:outputText id="dkljr3dssraea" value="Selected GPS list:"/> 
				  <h:inputText id="GPSStationList" 
									value="#{SimplexBean.selectedGpsStationName}" 
									readonly="true"/>
				  <h:outputText id="dkljr3dssabfd" value="Selected GPS Number:"/> 
				  <h:inputText id="GPSStationNum" value="" readonly="true"/>
				
				  <h:outputText id="dkljr3dssrf" value="Ref Station?:"/>
				  <h:selectBooleanCheckbox id="gpsRefStation23211s"
													value="#{SimplexBean.gpsRefStation}" />
				</h:panelGrid>
			 </h:panelGroup>

			 <h:panelGroup id="mapncrapLayoutGroup2">
				<h:outputText id="SimplexGPSInstructions2" 
								  value="Instead of individual stations, you can grab all 
											the stations in a selection area."/>
				<h:panelGrid id="simplexGPSStationSelectionArea" 
								 columns="2">
				  <h:outputText id="dkljr3dssra" value="Use Search Area:"/>
				  <h:selectBooleanCheckbox id="gpsRefStation23211b" onclick="toggleBorder()" 
													value="#{SimplexBean.searcharea}"/>
				  <h:outputText id="SimplexGetGPSSelectionArea" value="Select Stations in Box:"/>
				  <h:commandButton id="SimplexFetchGPSStations"
										 type="button" 
										 onclick="updateGPSinthebox()"
										 value="Select Stations in Box"/>
				  <h:inputHidden id="minlon" value="#{SimplexBean.selectedminlon}"/>
				  <h:inputHidden id="minlat" value="#{SimplexBean.selectedminlat}"/>
				  <h:inputHidden id="maxlon" value="#{SimplexBean.selectedmaxlon}"/>
				  <h:inputHidden id="maxlat" value="#{SimplexBean.selectedmaxlat}"/>
				</h:panelGrid>
			 </h:panelGroup>
			 
			 <h:panelGroup id="mapncrapLayoutGroup3">
				<h:outputText id="SimplexGPSInstructions3" 
								  value="Next, click the button below to retrieve the station values."/>
				
				<h:panelGrid id="simplexGPSStationFetchValues" 
								 columns="2">
				  <h:commandButton id="dummysubmit" 
										 value="Get values"
										 actionListener="#{SimplexBean.getvalues}"/>
				  
				  <h:panelGrid id="npbas12" columns="2">												  
					 <h:inputHidden id="stationLat" value="#{SimplexBean.gpsStationLat}"/>
					 <h:inputHidden id="stationLon" value="#{SimplexBean.gpsStationLon}"/>
				  </h:panelGrid>
				</h:panelGrid>
			 </h:panelGroup>

			 <h:panelGroup id="mapncrapLayoutGroup4">				
				<h:panelGrid id="mnauw1" 
								 rendered="#{!empty SimplexBean.mycandidateObservationsForProjectList}" 
								 columns="1" 
								 border="0" 
								 cellpadding="0" 
								 cellspacing="0">
				  <h:dataTable border="1" 
									cellpadding="0" 
									cellspacing="0" 
									id="dflezzz277" 
									headerClass="componentstableh2" 
									columnClasses="componentstablec"
									value="#{SimplexBean.mycandidateObservationsForProjectList}" 
									var="myentry5">
					 <h:column>
						<f:facet name="header">
						  <h:outputText id="bawee21" value="Station" />
						</f:facet>
						<h:panelGrid id="gplgmpp2" 
										 columns="1" 
										 cellpadding="0" 
										 cellspacing="0" 
										 styleClass="centered">
						  <f:facet name="header">								
						  </f:facet>
						  <h:inputText id="bawee22" style="text-align:right;width:60px" 
											value="#{myentry5.stationName}" />
						</h:panelGrid>
					 </h:column>
					 
					 <h:column>
						<f:facet name="header">
						  <h:outputText id="bawee23" value="Sources" />
						</f:facet>
						<h:panelGrid id="gplgmpp3" 
										 columns="1" 
										 cellpadding="0" 
										 cellspacing="0" 
										 styleClass="centered">
						  <f:facet name="header">								
						  </f:facet>
						  <h:selectOneListbox id="sselectl" value="#{myentry5.selectedSource}">
							 <f:selectItems value="#{myentry5.stationSources}" />
						  </h:selectOneListbox> 
						</h:panelGrid>
					 </h:column>
				  </h:dataTable>
				</h:panelGrid>
			 </h:panelGroup>
			 
			 <h:panelGroup id="mapncrapLayoutGroup5">
				<h:outputText id="simplexStationSelection3"
								  value="Finally, import the selected stations into your project."/>
				
				<h:panelGrid id="nploebba" columns="2">
				  <h:commandButton id="addGPSObsv" value="Add Station(s)"
										 actionListener="#{SimplexBean.toggleAddGPSObsvForProject}"/>
				  <h:commandButton id="closeMap" value="Close Map"
										 actionListener="#{SimplexBean.toggleCloseMap}"/>
				  
				</h:panelGrid>
			 </h:panelGroup>
		  </h:panelGrid>
		  <f:verbatim>
			 <div id="networksDiv"></div>
		  </f:verbatim>
		</h:panelGroup>
	 </h:panelGrid>
	 <h:outputText id="simplexMapKey" 
						escape="false"
						value="<b>Map Key</b><br/>"/>
	 <h:panelGrid id="simplexKeyGrid" 
					  columns="6">
		<h:outputText id="simplexMapKeyRed" 
						  escape="false"
						  value="Imported Station:"/>
		<h:graphicImage id="simplexKeyRedPin"
							 value="http://labs.google.com/ridefinder/images/mm_20_red.png"/>
		<h:outputText id="simplexMapKeyYellow" 
						  escape="false"
						  value="Selected Station:"/>
		<h:graphicImage id="simplexKeyYellowPin"
							 value="http://labs.google.com/ridefinder/images/mm_20_yellow.png"/>
		<h:outputText id="simplexMapKeyGreen" 
						  escape="false"
						  value="Unselected Station:"/>
		<h:graphicImage id="simplexKeyGreenPin"
							 value="http://labs.google.com/ridefinder/images/mm_20_green.png"/>
	 </h:panelGrid>
  </h:panelGrid>
  <f:verbatim>
	 <script type="text/javascript">
		  
		  initialize();

		  function toggleoff(form){
		    form.checked = false;
		  }

		  function stationsourcechange(){
		  alert("aa");
		  document.getElementById("obsvGPSMap:dummysubmit").click();
		  }

		  function togglesource(s){

		  var box1 = document.getElementById("obsvGPSMap:gpssource1");
		  var box2 = document.getElementById("obsvGPSMap:gpssource2");

		  var rssnewsize=<%=rssnewsize%>
		  var permsize=<%=permsize%>

		  if (s == "box1") {

		  if (box1.checked == false) {
		  for (var nA = 0 ; nA < rssnewsize ; nA++)
		  map.removeOverlay(marker[nA]);
		  }

		  if (box1.checked == true) {
		  for (var nA = 0 ; nA < rssnewsize ; nA++)
		  map.addOverlay(marker[nA]);
		  }
		  }

		  if (s == "box2") {

		  if (box2.checked == false) {
		  for (var nA = 0 ; nA < permsize ; nA++)
		  map.removeOverlay(marker[nA+rssnewsize]);
		  }

		  if (box2.checked == true) {
		  for (var nA = 0 ; nA < permsize ; nA++)
		  map.addOverlay(marker[nA+rssnewsize]);
		  }
		  }
		  }
		</script>
	 </f:verbatim>
  </h:form>
</h:panelGroup>