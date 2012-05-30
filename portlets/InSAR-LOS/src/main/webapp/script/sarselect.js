var sarselect=sarselect || (function() {
    //Global variables
	 var polyShape;
    var polyLineColor = "#3355ff";
    var polyFillColor = "#335599";
    var polyPoints = new Array();
    var markers = new Array();
	 var masterMap;
	 var leftClickOp;
	 var markerNE, markerSW;
//	 var insarKml;
	 var ucerfMapOpts={map:null, preserveViewport:true};
	 var ucerfKml=new google.maps.KmlLayer("@host.base.url@/InSAR-LOS/kml//QuakeTables_UCERF_2.4.kml",ucerfMapOpts);
	 var rowSelected=null;
	 var lowResSARLayer=null;
	 var uid=null;  //This is global because we need to pass it between two unrelated functions. Not good.
	 var rpiName=null;
	 var dygraph1, dygraph2;
	 var wmsMapType=null;
	 var faultWmsMapType=null;
	 var faultWmsOptions=null;
	 var azimuth;
	 var losLength;
	 var heading;
	 var radarDirection;
	 var availableDataSets;
	 var mapCenterChangeListener;

	 var dygraphLOSOpts={width:290,
								height:300,
								drawPoints:true,
								pointSize:2,
								strokeWidth:0.0,
								title:'InSAR Line of Sight Values',
								titleHeight:24,
								xlabel:'Distance (km)',
								ylabel:'LOS Value (cm)',
								labelsDiv:document.getElementById('LOS-Data-PointValue')};
	 var dygraphHgtOpts={width:290,
								height:300,
								drawPoints:true,
								pointSize:2,
								strokeWidth:0.0,
								title:'InSAR Height Values',
								titleHeight:24,
								xlabel:'Distance (km)',
								ylabel:'Height (m)',
								labelsDiv:document.getElementById('HGT-Data-PointValue')};

	 var blueIcon = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + "FF0000",new google.maps.Size(21,34),new google.maps.Point(0,0),new google.maps.Point(10,34));															  
	 var redIcon = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + "0000FF",new google.maps.Size(21,34),new google.maps.Point(0,0),new google.maps.Point(10,34));


	 function setMasterMap(insarMapDiv,tableDivName) {
		  var latlng=new google.maps.LatLng(32.3,-118.0);
		  var myOpts={zoom:6, scaleControl:true, center: latlng, mapTypeId: google.maps.MapTypeId.TERRAIN};
		  masterMap=new google.maps.Map(insarMapDiv, myOpts);


		  //Create the WMS overlay for the UAVSAR tiles
		  var wmsOptions= {
            alt: "GeoServer",
            getTileUrl: WMSGetTileUrl2,
            isPng: true,
            maxZoom: 17,
            minZoom: 6,
            name: "Geoserver",
            tileSize: new google.maps.Size(256, 256),
            credit: 'Image Credit: QuakeSim'
		  };
		  wmsMapType=new google.maps.ImageMapType(wmsOptions);

		  //Add the overlay to the master map
		  masterMap.overlayMapTypes.insertAt(0,wmsMapType);
		  
		  //Add the fault layer. We set this up here but don't turn it on until 
		  //later.
		  faultWmsOptions= {
            alt: "GeoServer2",
            getTileUrl: faultsWMSGetTileUrl,
            isPng: true,
            maxZoom: 17,
            minZoom: 6,
            name: "Geoserver2",
            tileSize: new google.maps.Size(256, 256),
            credit: 'Image Credit: QuakeSim'
		  };
		  faultWmsMapType=new google.maps.ImageMapType(faultWmsOptions);

		  
		  //Find out where we are when the map is clicked.
		  google.maps.event.addListener(masterMap,"click",function(event) {
				var finalUrl=constructWmsUrl(masterMap,event);
				var results=$.ajax({url:finalUrl,async:false}).responseText;
				availableDataSets=jQuery.parseJSON(results);
				createTable(availableDataSets,tableDivName);
				$("#Instructions").html("Now click the table entry that you want to plot.");
		  });
	 }
	 
	 //Legacy method.
	 function setLOSMap(insarMapDiv) {

		  //Create the map
		  var myOptions={
				panControl: false,
				zoomControl: true,
				scaleControl: true,
				zoom: 6,
				streetViewControl:false,
				center: new google.maps.LatLng(32.5,-117.0),
				mapTypeId: google.maps.MapTypeId.ROADMAP
		  };
		  var insarMap=new google.maps.Map(insarMapDiv,myOptions);
		  return insarMap;  
	 }

	 //Activates the low-res insar layer for LOS display.
	 function activateLayerMap(insarMap,callResults,drawFunctionType,uid,rpiName) {
		  //Extract overlayUrl
		  var overlayUrl=extractOverlayUrl(callResults);

		  //Remove the drag and zoom listeners on the master map.
		  $("#InSAR-Map-Messages").hide();
		  google.maps.event.clearListeners(masterMap,"dragend");
		  google.maps.event.clearListeners(masterMap,"zoom");

		  //Remove any previous layers and listeners on markers from previous session
		  if(lowResSARLayer) lowResSARLayer.setMap(null);  
		  if(ucerfKml) ucerfKml.setMap(null);
		  
		  if(markerNE) {
				google.maps.event.clearInstanceListeners(markerNE);
				markerNE.setMap(null);
				markerNE=null;
		  }
		  if(markerSW) {
				google.maps.event.clearInstanceListeners(markerSW);
				markerSW.setMap(null);
				markerSW=null;
		  }
		  if(polyShape) polyShape.setMap(null);		  

		  //Add fault overlay (UCERF 2.4)
//		  var ucerfMapOpts={map:masterMap, preserveViewport:true};
//		  ucerfKml=new google.maps.KmlLayer("@host.base.url@/InSAR-LOS/kml//QuakeTables_UCERF_2.4.kml",ucerfMapOpts);
//		  ucerfKml.setMap(masterMap);

		  //Display the fault toggling checkbox
		  $("#FaultToggler").show();
		  $("#FadeDisplay").show();

        //Add the KML Layer
		  lowResSARLayer=new google.maps.KmlLayer(overlayUrl,{suppressInfoWindows: true, map: insarMap, clickable: false});
		  $("#InSAR-Map-Messages").show();
		  $("#InSAR-Map-Messages").html("SAR Image Loading...");
		  //Add a listener for the insarKml map while it is loading.
		  google.maps.event.addListener(lowResSARLayer,"metadata_changed",function(event) {
				$("#InSAR-Map-Messages").hide()
		  });

		  google.maps.event.clearListeners(insarMap,"click");

		  google.maps.event.addListener(insarMap,"click",function(event) {
			if(drawFunctionType=="polygon") {
				 polygonLeftClick(insarMap,event);
			}
				else if(drawFunctionType=="rectangle") {
					 rectangleLeftClick(insarMap,event);
				}
				else if(drawFunctionType=="line") {
					 lineLeftClick(insarMap,event,uid,rpiName);
				}
				else {
					 alert("Invalid draw method provided. Should be either 'line', 'polygon', or 'rectangle'. Using 'rectangle' by default.");
					 rectangleLeftClick(isarMap,event);
				}
		  });
	 }
    
	 function lineLeftClick(insarMap,event,uid,rpiName) {
		  $("#Plot-Parameters").show();

		  $("#Left-Column-Under-Map").show();
		  //If the marker doesn't exist, create it.
		  if(!markerNE && !markerSW) {
				markerNE=new google.maps.Marker({map: insarMap, 
															position: event.latLng, 
															visible: true, 
															icon:redIcon,
															title: "Ending point of LOS measurements",
															draggable: true});
				var offset=new google.maps.LatLng(event.latLng.lat()-0.05,event.latLng.lng()-0.05);
				markerSW=new google.maps.Marker({map: insarMap, 
															position: offset, 
															visible: true, 
															icon:blueIcon,
															title: "Starting point of LOS measurements",
															draggable: true});
		  		drawLine(insarMap);
				getInSarValues(uid,rpiName);
				showEndpoints();
				calculateDistance();
				setEndPointFormValues();

				// Make markers draggable			 
				google.maps.event.addListener(markerNE, "drag", function() {
					 drawLine(insarMap);
					 //Keep map centered while dragging.
					 disableMapDrag(insarMap);
				});
				google.maps.event.addListener(markerSW, "drag", function() {
					 drawLine(insarMap);
					 //Keep map centered while dragging.
					 disableMapDrag(insarMap);
				});

				google.maps.event.addListener(markerNE, "dragend", function() {
					 google.maps.event.clearListeners(insarMap,"center_changed");
					 getInSarValues(uid,rpiName);
					 setEndPointFormValues();
					 showEndpoints();
					 calculateDistance();
				});
				google.maps.event.addListener(markerSW, "dragend", function() {
					 google.maps.event.clearListeners(insarMap,"center_changed");
					 getInSarValues(uid,rpiName);
					 setEndPointFormValues();
					 showEndpoints();
					 calculateDistance();
				});
		  }
	 }

	 //--------------------------------------------------
	 // Calculates the azimuth and displays this along with
	 // the icons under the map.  Also displays the azimuth
	 // in the appropriate form area.
	 //--------------------------------------------------
	 function showEndpoints(){
		  var swLat=markerSW.getPosition().lat().toFixed(5);
		  var swLon=markerSW.getPosition().lng().toFixed(5);
		  var neLat=markerNE.getPosition().lat().toFixed(5);
		  var neLon=markerNE.getPosition().lng().toFixed(5);

		  //Using http://www.movable-type.co.uk/scripts/latlong.html
		  var d2r=Math.PI/180.0;
		  var flatten=1.0/298.247;

		  //This is the old formula.
//		  var dlon=(neLon-swLon)*d2r;
//		  var y=Math.sin(dlon)*Math.cos(neLat*d2r);
//		  var x=Math.cos(swLat*d2r)*Math.sin(neLat*d2r)-Math.sin(swLat*d2r)*Math.cos(neLat*d2r)*Math.cos(dlon);
//		  azimuth=Math.atan2(y,x)/d2r;

		  var theFactor=d2r* Math.cos(d2r * swLat) * 6378.139 * (1.0 - Math.sin(d2r * swLat) * Math.sin(d2r * swLat) * flatten);
		  var x=(neLon-swLon)*theFactor;
		  var y=(neLat-swLat)*111.32;
		  
		  azimuth=Math.atan2(x,y)/d2r;
		  azimuth=azimuth.toFixed(1);
		  if (azimuth < 0) { azimuth = azimuth + 360; }

		  $("#iconGuide").html('<table><tr><td><img src="http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|FF0000"/> <b>Lat, Lon: </b>'+swLat+', '+swLon+'  <image src="http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|0000FF"/>  <b>Lat, Lon:</b> '+neLat+', '+neLon+'  <b>Azimuth:</b> '+ azimuth +'&deg;</td></tr><tr><td>'+'<b>Heading: </b>'+heading+'&deg'+'&nbsp; &nbsp;'+'<b>Radar Direction: </b>'+radarDirection+'</td></tr></table><p/>');
		  
		  $("#azimuth-value").val(azimuth);
	 }


	 function drawLine(insarMap) {
		  if(polyShape) polyShape.setMap(null);
		  polyPoints=new Array();
		  polyPoints.push(markerNE.getPosition());
		  polyPoints.push(markerSW.getPosition());
		  polyShape=new google.maps.Polyline({path:polyPoints,
														  strokeColor:polyLineColor,
														  map:insarMap,
														  zIndex:1});
	 }

	 //--------------------------------------------------
	 //Calls the "get LOS" and "get HGT" rest services based on the provided resolution, method, and 
	 //(optionally) averaging radius. 
	 //--------------------------------------------------
	 function getInSarValues(uid,rpiName) {
		  var resolution=$("#resolution-value").val();
		  var averaging=$("#averaging-value").val();
		  var method="native";
		  if($("#average-method").is(':checked')) {
				method="average";
		  }
		  else {
				method="native";
		  }
		  getLosInSarValues(uid,resolution,method,averaging,rpiName);
		  getHgtInSarValues(uid,resolution,method,averaging,rpiName);
	 }
	 
	 //--------------------------------------------------
	 //Constructs the LOS REST service call, plots the output, and provides a download link.
	 //--------------------------------------------------
	 function getLosInSarValues(uid,resolution,method,averaging,rpiName) {
		  var westMarkerLat=markerSW.getPosition().lat();
		  var westMarkerLon=markerSW.getPosition().lng();
		  var eastMarkerLat=markerNE.getPosition().lat();
		  var eastMarkerLon=markerNE.getPosition().lng();
		  
		  var restUrl="/InSAR-LOS-REST/insarlos/csv/"+uid+"/"+resolution+"/"+westMarkerLon+"/"+westMarkerLat+"/"+eastMarkerLon+"/"+eastMarkerLat+"/"+method+"/"+averaging+"/"+rpiName;

		  var csv=$.ajax({
				url:restUrl,
				beforeSend: function() {if(dygraph1) {dygraph1.destroy(); }; 
											  	$('#LOS-Data-Download').hide(); 
												$('#outputGraph1').html('<center><img src="@host.base.url@/InSAR-LOS/images/processing.gif"/></center>');
											  },
				async:true
		  });
		  csv.done(function(results) {
				dygraph1=new Dygraph(document.getElementById("outputGraph1"),results,dygraphLOSOpts);
				$('#LOS-Data-Download').show();
				$("#LOS-Data-Download").html("<center><a href='"+restUrl+"' target='_blank'>Download LOS Data</a></center>");
		  });
	 }

	 //Constructs the HGT REST service call, plots the output, and provides a download link.
	 function getHgtInSarValues(uid,resolution,method,averaging,rpiName) {
		  var westMarkerLat=markerSW.getPosition().lat();
		  var westMarkerLon=markerSW.getPosition().lng();
		  var eastMarkerLat=markerNE.getPosition().lat();
		  var eastMarkerLon=markerNE.getPosition().lng();

		  var restUrl="/InSAR-LOS-REST/insarhgt/csv/"+uid+"/"+resolution+"/"+westMarkerLon+"/"+westMarkerLat+"/"+eastMarkerLon+"/"+eastMarkerLat+"/"+method+"/"+averaging+"/"+rpiName;

		  var csv=$.ajax({
				url:restUrl,
				beforeSend: function() { if(dygraph2) {dygraph2.destroy();};
												 $('#HGT-Data-Download').hide(); 
												 $('#outputGraph2').html('<center><img src="@host.base.url@/InSAR-LOS/images/processing.gif"/></center>');
											  },

				async:true
		  });
		  csv.done(function(results) {
				dygraph2=new Dygraph(document.getElementById("outputGraph2"),results,dygraphHgtOpts);		  
				$('#HGT-Data-Download').show();
				$("#HGT-Data-Download").html("<center><a href='"+restUrl+"' target='_blank'>Download HGT Data</a></center>");
		  });
		  csv.fail(function(errorMsg){
				console.log("Failed to load HGT values: "+errorMsg);
		  });
	 }

	 //Creates a table of all InSAR images associated with a specific point.
	 function createTable(parsedResults,tableDivName) {
		  //Fill in the table.
		  var dynatable='<table class="sartable-outer">';
		  for (var index1 in parsedResults) {
				dynatable+='<tr onmouseover="sarselect.selectedRow(this)" onmouseout="sarselect.unselectedRow(this)" onclick="sarselect.selectRowAction(this)" id="'+parsedResults[index1]['uid']+'"'+'>';
				dynatable+='<td><table class="sartable-inner" border="1"><tr>';
				dynatable+='<th colspan="2">'+parsedResults[index1]['dataname']+'</th>';
				dynatable+='</tr><tr>';
				dynatable+='<td>'+parsedResults[index1]['time1']+'</td><td>'+parsedResults[index1]['time2']+'</td>';
				dynatable+='</tr></table></td>';
				dynatable+='</tr>';
		  }
		  dynatable+='</table>';
		  $('#dynatable').html(dynatable);
	 }
	 
	 //This function constructs a REST call to a local service, insarnav. The code for this service is 
	 //under QuakeSim's RestServices directory.
	 function constructWmsUrl(map,event) {
	   var scale=Math.pow(2,map.getZoom());
	   var nw=new google.maps.LatLng(map.getBounds().getNorthEast().lat(),map.getBounds().getSouthWest().lng());
		var worldCoordNW=map.getProjection().fromLatLngToPoint(nw);
		var worldCoord=map.getProjection().fromLatLngToPoint(event.latLng);
		xpix=Math.floor((worldCoord.x-worldCoordNW.x)*scale);
		ypix=Math.floor((worldCoord.y-worldCoordNW.y)*scale);

		var width=map.getDiv().style.width;
		width=width.substring(0,width.indexOf("px"));
		var height=map.getDiv().style.height;
		height=height.substring(0,height.indexOf("px"));

		var slash="/";
	   var urlToCall="/InSAR-LOS-REST/insarnav";
	   urlToCall+=slash+map.getBounds().getSouthWest().lng();
		urlToCall+=slash+map.getBounds().getSouthWest().lat();
		urlToCall+=slash+map.getBounds().getNorthEast().lng();
		urlToCall+=slash+map.getBounds().getNorthEast().lat();
		urlToCall+=slash+width;
		urlToCall+=slash+height;
		urlToCall+=slash+xpix;
		urlToCall+=slash+ypix;
		
		return urlToCall;
    }
	 function selectedRow(row) {
		  if(row!=rowSelected){
				row.style.backgroundColor="gray";
				row.style.cursor="pointer";
		  }
	 }
	 function unselectedRow(row) {
		  if(row!=rowSelected) {
				row.style.backgroundColor="white";
				row.style.cursor="default";
		  }
	 }
	 function selectRowAction(row){
		  $("#Left-Column-Under-Map").hide();
		  $("#Plot-Parameters").hide();
		  if(rowSelected!=null) {
				rowSelected.style.backgroundColor="white";
		  }
		  rowSelected=row;
	     rowSelected.style.backgroundColor="lightgreen";
	     //Find the ID of the row
		  //var uid=extractRowId2(row);
		  uid=extractRowId2(row);
		  rpiName=extractRowRpiName(row);
		  heading=extractHeading(rpiName,availableDataSets);
		  radarDirection=extractRadarDirection(rpiName,availableDataSets);

		  //Construct the link to QuakeTables and turn on the display.
		  $("#QuakeTables-Link").html('<p/><a target="_blank" href="http://quakesim.usc.edu/quaketables/uavsar.jsp?uid='+uid+'">Go to download page for selected data set</a>');

	     //Call REST service
		  var callResults=getImageMetadata(uid);
		  
		  //Turn off the thumbnail overlayer
//		  insarKml.setMap(null);
		  masterMap.overlayMapTypes.clear();
//		  masterMap.overlayMapTypes.removeAt(0);
		  

//		  masterMap.overlayMapTypes.insertAt(0,faultWmsMapType);
		  //Turn on the new overlayer
		  activateLayerMap(masterMap,callResults,"line",uid,rpiName);
		  
		  $("#Instructions").html("Now click the map to plot a line.  Move the end points to set the plot.");
	 }
	 
	 function extractOverlayUrl(callResults){
	   var overlayUrl;
		for(var index in callResults) {
		    if(callResults[index].datatype=="unw") {
			    overlayUrl=callResults[index].kml;
			 }
		}
	   return overlayUrl;
	 }

	 //--------------------------------------------------
	 //This is the current preferred method.  It assumes 
	 //the row id attribute has been set to the image uid value.
	 //--------------------------------------------------
	 function extractRowId2(row) {
		  return row.getAttribute('id');
	 }

	 function extractRowRpiName(row) {
		  var td=row.firstChild;
		  var table2=td.firstChild;
		  var thOfTable2=((table2.firstChild).firstChild).firstChild;
		  var productName=thOfTable2.firstChild.nodeValue;
		  return productName;
	 }

	 //This is an obsolete method for extracting the image uid.  It assumes
	 //the image uid values are in a displayed table column
	 function extractRowId(row) {
	    //Get the first td from the tr and return its innerHTML.
	    //The ID is the value of the first <td> element.
		 var td=row.firstChild;
	    var id=td.innerHTML;
		 return id;
	 }
	 
	 function getImageMetadata(id) {
	   var urlToCall="/InSAR-LOS-REST/insarid/"+id;
		var tmpResults=$.ajax({url:urlToCall,async:false}).responseText;
		jsonResults=jQuery.parseJSON(tmpResults);
	   return jsonResults;
	 }

	 //This is a dangerous pattern: UID is a global variable set by a separate function
	 //when the table is clicked. As long as the order of events is preserved, then
	 //UID should be set correctly but changing things will break this function.
	 function plotNative() {
		  //Disable the "averaging" input field.
		  $("#averaging-value").attr('disabled',true);
		  getInSarValues(uid,rpiName);
	 }

	 //--------------------------------------------------
	 //This is a dangerous pattern: UID is a global variable set by a separate function
	 //when the table is clicked. As long as the order of events is preserved, then
	 //UID should be set correctly but changing things will break this function.
	 //--------------------------------------------------
	 function plotAverage(){
		  //Enable the "averaging" input field.
		  $("#averaging-value").attr('disabled',false);
		  getInSarValues(uid,rpiName);
	 }

	 function updateResolution() {
		  getInSarValues(uid,rpiName);
	 }

	 function updateAveraging() {
		  getInSarValues(uid,rpiName);
	 }
	 
	 //--------------------------------------------------
	 //TODO: Note that masterMap and insarMap are synonymous.  Not clear why we need both names.
	 //TODO: Note several functions take insarMap as input but this isn't easily done with the method below.
	 //--------------------------------------------------
	 function toggleFaultKml() {
		  if($("#fault_toggle_id").is(':checked')) {
				//				ucerfKml.setMap(masterMap);
				masterMap.overlayMapTypes.clear();  //Note this will remove all layers, so need a better way.
				masterMap.overlayMapTypes.insertAt(0,faultWmsMapType);
		  }
		  else {
				masterMap.overlayMapTypes.clear();  //Note this will remove all layers, so need a better way.
				//ucerfKml.setMap(null);
		  }
	 }

	 //--------------------------------------------------
    //The code that reads in the WMS file.  To change the WMS layer the user would update the layers 
	 //line.  As this is constructed now you need to have this code for each WMS layer.
	 //Check with your Web Map Server to see what are the required components of the address.  You may 
	 //need to add a couple of segements.  For example, the ArcServer WMS requires
	 //a CRS value which is tacked on to the end of the url.  For an example 
	 //visit http://www.gisdoctor.com/v3/arcserver_wms.html
	 //-------------------------------------------------- 
	 function WMSGetTileUrl2(tile, zoom) {
        var projection = masterMap.getProjection(); //NOTE masterMap is a global var (fix method?)
        var zpow = Math.pow(2, zoom);
        var ul = new google.maps.Point(tile.x * 256.0 / zpow, (tile.y + 1) * 256.0 / zpow);
        var lr = new google.maps.Point((tile.x + 1) * 256.0 / zpow, (tile.y) * 256.0 / zpow);
        var ulw = projection.fromPointToLatLng(ul);
        var lrw = projection.fromPointToLatLng(lr);
        //The user will enter the address to the public WMS layer here.  The data must be in WGS84
		  var baseURL = "http://gf2.ucs.indiana.edu/geoserver/wms?";
        var version = "1.3.0";
        var request = "GetMap";
        var format = "image%2Fpng"; //type of image returned  or image/jpeg
        //The layer ID.  Can be found when using the layers properties tool in ArcMap or from the WMS settings 
        var layers = "thumbnails";
        //projection to display. This is the projection of google map. Don't change unless you know what you are doing.  
        //Different from other WMS servers that the projection information is called by crs, instead of srs
        var crs = "EPSG:4326";
        //With the 1.3.0 version the coordinates are read in LatLon, as opposed to LonLat in previous versions
        var bbox = ulw.lat() + "," + ulw.lng() + "," + lrw.lat() + "," + lrw.lng();
        var service = "WMS";
        //the size of the tile, must be 256x256
        var width = "256";
        var height = "256";
        //Some WMS come with named styles.  The user can set to default.
        var styles = "";
        //Establish the baseURL.  Several elements, including &EXCEPTIONS=INIMAGE and &Service are unique to openLayers addresses.
        var url = baseURL + "Layers=" + layers + "&version=" + version + "&EXCEPTIONS=INIMAGE" + "&Service=" + service + "&request=" + request + "&Styles=" + styles + "&format=" + format + "&CRS=" + crs + "&BBOX=" + bbox + "&width=" + width + "&height=" + height;
        url = url + "&TRANSPARENT=true";
		  return url;
	 }
	 
	 //--------------------------------------------------
	 //This code is used to display the fault layers
	 //TODO: just repeats the code for the UAVSAR tiles. We need to combine these.
	 //--------------------------------------------------
	 	 function faultsWMSGetTileUrl(tile, zoom) {
        var projection = masterMap.getProjection(); //NOTE masterMap is a global var (fix method?)
        var zpow = Math.pow(2, zoom);
        var ul = new google.maps.Point(tile.x * 256.0 / zpow, (tile.y + 1) * 256.0 / zpow);
        var lr = new google.maps.Point((tile.x + 1) * 256.0 / zpow, (tile.y) * 256.0 / zpow);
        var ulw = projection.fromPointToLatLng(ul);
        var lrw = projection.fromPointToLatLng(lr);
        //The user will enter the address to the public WMS layer here.  The data must be in WGS84
		  var baseURL = "http://gf2.ucs.indiana.edu/geoserver/wms?";
        var version = "1.1.0";
        var request = "GetMap";
        var format = "image%2Fpng"; //type of image returned  or image/jpeg
        //The layer ID.  Can be found when using the layers properties tool in ArcMap or from the WMS settings 
        var layers = "InSAR:fault";
        //projection to display. This is the projection of google map. Don't change unless you know what you are doing.  
        //Different from other WMS servers that the projection information is called by crs, instead of srs
        var crs = "EPSG:4326";
        var bbox = ulw.lng() + "," + ulw.lat() + "," + lrw.lng() + "," + lrw.lat();
        var service = "WMS";
        //the size of the tile, must be 256x256
        var width = "256";
        var height = "256";
        //Some WMS come with named styles.  The user can set to default.
        var styles = "";
        //Establish the baseURL.  Several elements, including &EXCEPTIONS=INIMAGE and &Service are unique to openLayers addresses.
        var url = baseURL + "Layers=" + layers + "&version=" + version + "&EXCEPTIONS=INIMAGE" + "&Service=" + service + "&request=" + request + "&Styles=" + styles + "&format=" + format + "&CRS=" + crs + "&BBOX=" + bbox + "&width=" + width + "&height=" + height;
			  url = url + "&TRANSPARENT=true";
			  
		  return url;
	 }

	 function updateStartLat(){
		  var newPos=new google.maps.LatLng($("#startLat-value").val(),markerSW.getPosition().lng());
		  markerSW.setPosition(newPos);
		  google.maps.event.trigger(markerSW,"drag");
		  google.maps.event.trigger(markerSW,"dragend");
	 }

	 function updateStartLon(){
		  var newPos=new google.maps.LatLng(markerSW.getPosition().lat(),$("#startLon-value").val());
		  markerSW.setPosition(newPos);
		  google.maps.event.trigger(markerSW,"drag");
		  google.maps.event.trigger(markerSW,"dragend");
	 }

	 function updateEndLat(){
		  var newPos=new google.maps.LatLng($("#endLat-value").val(),markerNE.getPosition().lng());
		  markerNE.setPosition(newPos);
		  google.maps.event.trigger(markerNE,"drag");
		  google.maps.event.trigger(markerNE,"dragend");
	 }

	 function updateEndLon(){
		  var newPos=new google.maps.LatLng(markerNE.getPosition().lat(),$("#endLon-value").val());
		  markerNE.setPosition(newPos);
		  google.maps.event.trigger(markerNE,"drag");
		  google.maps.event.trigger(markerNE,"dragend");
	 }

	 function setEndPointFormValues() {
		  $("#startLat-value").val(markerSW.getPosition().lat().toFixed(5));
		  $("#startLon-value").val(markerSW.getPosition().lng().toFixed(5));
		  $("#endLat-value").val(markerNE.getPosition().lat().toFixed(5));
		  $("#endLon-value").val(markerNE.getPosition().lng().toFixed(5));
	 }

	 function updateAzimuth() {
		  losLength=$("#losLength-value").val();
		  azimuth=$("#azimuth-value").val();
		  setEndpointsFromAzimuthAndLength();
	 }

	 //Uses the new distance to calculate the new ending lat,lon
	 function updateDistance() {
		  losLength=$("#losLength-value").val();
		  azimuth=$("#azimuth-value").val();
		  setEndpointsFromAzimuthAndLength();
	 }

	 //--------------------------------------------------
	 // Sets the ending (lat,lon) from a provided initial (lat, lon), 
	 // a length, and a strike (or azimuth) angle.
	 //--------------------------------------------------
	 function setEndpointsFromAzimuthAndLength() {
		  var d2r = Math.PI / 180.0;
		  var flatten=1.0/298.247;

		  var latStart=markerSW.getPosition().lat().toFixed(5);
		  var lonStart=markerSW.getPosition().lng().toFixed(5);
		  var latEnd;//=markerNE.getPosition().lat().toFixed(5);
		  var lonEnd;//=markerNE.getPosition().lng().toFixed(5);
		  var xend,yend,sval,thetan;

		  //We'll use the convention that azimuth is -180 to 180. This is what Simplex assumes.  
		  if(azimuth.value>180) azimuth.value=azimuth.value-360;
		  if(azimuth.value<-180) azimuth.value=azimuth.value+360;

		  //Now find the lat/lon values of the translated endpoint.
		  //First, find the Cartesian coordinates of the endpoint.  

		  if (azimuth == 0) {
				xend = 0; 
				yend = losLength;
		  }
		  else if (azimuth == 90) { 
				xend = losLength; yend = 0;
		  }
		  else if (azimuth == 180) { 
				xend = 0; yend = (-1.0) * losLength;
		  }
		  else if (azimuth == -90) { 
				xend = (-1.0) * losLength; yend = 0;
		  }
		  else {
				sval = 90 - azimuth;//.value;
				thetan = Math.tan(sval*d2r);
				xend = losLength/Math.sqrt(1 + thetan*thetan);
				yend = Math.sqrt(losLength*losLength - xend*xend);
				
				if (azimuth > 0 && azimuth < 90) { 
					 xend = xend*1.0; yend = yend*1.0;
				}
				else if (azimuth > 90 && azimuth < 180) { 
					 xend = xend*1.0; yend = yend* (-1.0);
				}
				else if (azimuth > -180 && azimuth < -90) { 
					 xend = xend*(-1.0); yend = yend*(-1.0);
				}
				else if (azimuth > -90 && azimuth < 0) { 
					 xend = xend*(-1.0); yend = yend*1.0;
				}
				else {
					 console.log("Incorrect quadrant determination");
				}
		  }
		  
		  //Note we use the lat, lon of the fault's starting point here, not the origin's lat, lon, because
		  //we are using the fault length (not the distance to the origin from the end point).
		  var theFactor=d2r* Math.cos(d2r * latStart) * 6378.139 * (1.0 - Math.sin(d2r * latStart) * Math.sin(d2r * latStart) * flatten);

		  lonEnd = 1.0*xend/(1.0*theFactor) + lonStart*1.0;
		  lonEnd=lonEnd.toFixed(5);
		  latEnd = 1.0*yend/111.32 + latStart*1.0;
		  latEnd=latEnd.toFixed(5);
		  
		  $("#endLat-value").val(latEnd);
		  $("#endLon-value").val(lonEnd);

		  var newPos=new google.maps.LatLng(latEnd,lonEnd);
		  markerNE.setPosition(newPos);
		  google.maps.event.trigger(markerNE,"drag");
		  google.maps.event.trigger(markerNE,"dragend");
	 }	 

	 //--------------------------------------------------
	 // This function calculates the distance between the starting 
	 // and ending (lat,lon) points.  
	 //--------------------------------------------------
	 function calculateDistance() {
		  var latStart=markerSW.getPosition().lat().toFixed(5);
		  var lonStart=markerSW.getPosition().lng().toFixed(5);
		  var latEnd=markerNE.getPosition().lat().toFixed(5);
		  var lonEnd=markerNE.getPosition().lng().toFixed(5);

		  var d2r = Math.PI / 180.0;
		  var flatten=1.0/298.247;
		  var theFactor=d2r* Math.cos(d2r * latStart) * 6378.139 * (1.0 - Math.sin(d2r * latStart) * Math.sin(d2r * latStart) * flatten);

		  var xdiff=(lonEnd-lonStart)*theFactor;
		  var ydiff=(latEnd-latStart)*111.32;
		  
		  losLength=Math.sqrt(xdiff*xdiff+ydiff*ydiff);
		  losLength=losLength.toFixed(3);
		  
//		  var distance1=Math.sin(dlat/2.0)*Math.sin(dlat/2.)
//				distance1+=Math.sin(dlon/2.)*Math.sin(dlon/2.0)*Math.cos(latStart*d2r)*Math.cos(latEnd*d2r);
//		  var distance2=2.*Math.atan2(Math.sqrt(distance1), Math.sqrt(1.0-distance1));
//		  losLength=R*distance2; //Note this is a global variable.
//		  losLength=losLength.toFixed(5);
		  
		  $("#losLength-value").val(losLength);
	 }

	 function extractHeading(rpiName,availableDataSets) {
		  var myHeading;
		  for(var index1 in availableDataSets) {
				if(availableDataSets[index1]['dataname']==rpiName) {
					 myHeading=availableDataSets[index1]['heading'];
					 break;
				}
		  }
		  return myHeading;
	 }

	 function extractRadarDirection(rpiName,availableDataSets) {
		  var myRadarDirection;
		  for(var index1 in availableDataSets) {
				if(availableDataSets[index1]['dataname']==rpiName) {
					 myRadarDirection=availableDataSets[index1]['radardirection'];
					 break;
				}
		  }
		  return myRadarDirection;
	 }
	 
	 function disableMapDrag(overlayMap) {
		  var lastValidCenter=overlayMap.getCenter();
		  //Use addListenerOnce since the disableMapDrag function is called throughout the drag.
		  //However, it doesn't seem to be working correctly.
		  google.maps.event.addListenerOnce(overlayMap,"center_changed",function(){
				insarMap.panTo(lastValidCenter);
		  });
	 }

	 //--------------------------------------------------
	 // This displays and removes the GeoJSON fault overlays.
	 //--------------------------------------------------
	 currentFeature_or_Features = null;
	 var infowindow = new google.maps.InfoWindow();
	 function clearMap(){
		  if (!currentFeature_or_Features)
				return;
		  if (currentFeature_or_Features.length){
				for (var i = 0; i < currentFeature_or_Features.length; i++){
					 if(currentFeature_or_Features[i].length){
						  for(var j = 0; j < currentFeature_or_Features[i].length; j++){
								currentFeature_or_Features[i][j].setMap(null);
						  }
					 }
					 else{
						  currentFeature_or_Features[i].setMap(null);
					 }
				}
		  }else{
				currentFeature_or_Features.setMap(null);
		  }
		  if (infowindow.getMap()){
				infowindow.close();
		  }
	 }
	 
	 function showFeature(geojson, style, map){
		  clearMap();
		  currentFeature_or_Features = new GeoJSON(geojson, style || null);
		  if (currentFeature_or_Features.type && currentFeature_or_Features.type == "Error"){
				console.log(currentFeature_or_Features.message);
				return;
		  }
		  if (currentFeature_or_Features.length){
				for (var i = 0; i < currentFeature_or_Features.length; i++){
					 if(currentFeature_or_Features[i].length){
						  for(var j = 0; j < currentFeature_or_Features[i].length; j++){
								currentFeature_or_Features[i][j].setMap(map);
								if(currentFeature_or_Features[i][j].geojsonProperties) {
									 setInfoWindow(currentFeature_or_Features[i][j],map);
								}
						  }
					 }
					 else{
						  currentFeature_or_Features[i].setMap(map);
					 }
					 if (currentFeature_or_Features[i].geojsonProperties) {
						  setInfoWindow(currentFeature_or_Features[i],map);
					 }
				}
		  }else{
				currentFeature_or_Features.setMap(map)
				if (currentFeature_or_Features.geojsonProperties) {
					 setInfoWindow(currentFeature_or_Features,map);
				}
		  }			
		}
	 
	 function setInfoWindow (feature,map) {
		  google.maps.event.addListener(feature, "click", function(event) {
				var content = "<div id='infoBox'>";
			   content += this.geojsonProperties['Description'] + "<br />";
				content += "</div>";
				infowindow.setContent(content);
				infowindow.position = event.latLng;
				infowindow.open(map);
		  });
	 }
	 
	 //--------------------------------------------------
	 // This displays the GeoJSON on the map. The fault 
	 // GeoJSON input file is currently hard-coded and
	 // must be supplied by the calling HTML.
	 //--------------------------------------------------
	 function toggleFaultGeoJSON() {
        if($("#fault_toggle_id").is(':checked')) {
				showFeature(fault,null,masterMap);
		  }
		  else {
				clearMap();  //Note this will remove all layers, so need a better way.
		  }
	 }
												 

	 /**
	  * Public API for sarselect.js
	  */
	 return {
		  setMasterMap:setMasterMap,
		  selectedRow:selectedRow,
		  unselectedRow:unselectedRow,
		  selectRowAction:selectRowAction,
		  setLOSMap: setLOSMap,
		  getInSarValues: getInSarValues,
		  getLosInSarValues: getLosInSarValues,
		  getHgtInSarValues: getHgtInSarValues,
		  activateLayerMap: activateLayerMap,
		  plotNative:plotNative,
		  plotAverage:plotAverage,
		  updateResolution:updateResolution,
		  updateAveraging:updateAveraging,
		  toggleFaultKml:toggleFaultKml,
		  updateStartLat:updateStartLat,
		  updateStartLon:updateStartLon,
		  updateEndLat:updateEndLat,
		  updateEndLon:updateEndLon,
		  updateDistance:updateDistance,
		  updateAzimuth:updateAzimuth,
		  toggleFaultGeoJSON:toggleFaultGeoJSON
	 }
	 
})();