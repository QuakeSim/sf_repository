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

		  //Create the WMS overlay
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
		  
		  
		  //Find out where we are when the map is clicked.
		  google.maps.event.addListener(masterMap,"click",function(event) {
				var finalUrl=constructWmsUrl(masterMap,event);
				var results=$.ajax({url:finalUrl,async:false}).responseText;
				var parsedResults=jQuery.parseJSON(results);
				createTable(parsedResults,tableDivName);
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
	 function activateLayerMap(insarMap,overlayUrl,drawFunctionType,uid,rpiName) {
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
		  ucerfKml.setMap(masterMap);

		  //Display the fault toggling checkbox
		  $("#FaultToggler").show();

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

				setEndPointFormValues();

				// Make markers draggable			 
				google.maps.event.addListener(markerNE, "drag", function() {
					 drawLine(insarMap);
				});
				google.maps.event.addListener(markerSW, "drag", function() {
					 drawLine(insarMap);
				});

				google.maps.event.addListener(markerNE, "dragend", function() {
					 getInSarValues(uid,rpiName);
					 setEndPointFormValues();
					 showEndpoints();
				});
				google.maps.event.addListener(markerSW, "dragend", function() {
					 getInSarValues(uid,rpiName);
					 setEndPointFormValues();
					 showEndpoints();
				});
		  }
	 }

	 function showEndpoints(){
		  var swLat=markerSW.getPosition().lat().toFixed(5);
		  var swLon=markerSW.getPosition().lng().toFixed(5);
		  var neLat=markerNE.getPosition().lat().toFixed(5);
		  var neLon=markerNE.getPosition().lng().toFixed(5);

		  //Using http://www.movable-type.co.uk/scripts/latlong.html
		  var d2r=Math.PI/180.0;
		  var dlon=(neLon-swLon)*d2r;
		  var y=Math.sin(dlon)*Math.cos(neLat*d2r);
		  var x=Math.cos(swLat*d2r)*Math.sin(neLat*d2r)-Math.sin(swLat*d2r)*Math.cos(neLat*d2r)*Math.cos(dlon);
		  var azimuth=Math.atan2(y,x)/d2r;
		  azimuth=azimuth.toFixed(1);

		  $("#iconGuide").html('<img src="http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|FF0000"/> <b>Lat, Lon: </b>'+swLat+', '+swLon+'  <image src="http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|0000FF"/>  <b>Lat, Lon:</b> '+neLat+', '+neLon+'  <b>Azimuth:</b> '+ azimuth +'&deg;<p/>');

	 }

	 function rectangleLeftClick(insarMap,event) {
		  //If the marker doesn't exist, create it.
		  if(!markerNE && !markerSW) {
				//console.log("No markers found");
				markerNE=new google.maps.Marker({map: insarMap, 
															position: event.latLng, 
															visible: true, 
															draggable: true});
				var offset=new google.maps.LatLng(event.latLng.lat()-0.05,event.latLng.lng()-0.05);
				markerSW=new google.maps.Marker({map: insarMap, 
															position: offset, 
															visible: true, 
															draggable: true});
				markers.push(markerNE);
				markers.push(markerSW);
		  		
				// Make markers draggable			 
				google.maps.event.addListener(markerNE, "drag", function() {
					 drawRectangle(insarMap);
				});
				google.maps.event.addListener(markerSW, "drag", function() {
					 drawRectangle(insarMap);
				});
		  }

		  //This is called after a drag event.
		  drawRectangle(insarMap);
	 }

	 function polygonLeftClick(insarMap,event) {
		  //Make the marker and add to the map.
		  //console.log("Create marker and add to the map");
		  var marker=new google.maps.Marker({map: insarMap, 
														 position: event.latLng, 
														 visible: true, 
														 draggable: true});
		  markers.push(marker);
		  
		  // Make markers draggable			 
		  google.maps.event.addListener(marker, "drag", function() {
				drawPoly(insarMap);
		  });
		  
		  // Second click listener to remove the square
		  google.maps.event.addListener(marker, "click", function() {
				// Find out which square to remove
				for(var n = 0; n < markers.length; n++) {
					 if(markers[n] == marker) {
						  markers[n].setMap(null);
						  break;
						}
				}
				markers.splice(n, 1);
				drawPoly(insarMap);
		  });
		  drawPoly(insarMap);
		  //console.log("Done with leftClick()");
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

	 function drawRectangle(insarMap) {
		  //console.log("Drawing rectangle");
		  if(polyShape) polyShape.setMap(null);
		  var cornerSE=new google.maps.LatLng((markerNE.getPosition()).lat(),(markerSW.getPosition()).lng());
		  var cornerNW=new google.maps.LatLng((markerSW.getPosition()).lat(),(markerNE.getPosition()).lng());
		  polyPoints=new Array();
		  polyPoints.push(markerNE.getPosition());		  
		  polyPoints.push(cornerNW);
		  polyPoints.push(markerSW.getPosition());
		  polyPoints.push(cornerSE);
		  
		  polyShape=new google.maps.Polygon({paths:polyPoints,
														 fillColor:polyFillColor,
														 strokeColor:polyLineColor,
														 zindex:1});
		  polyShape.setMap(insarMap);
	 }
	 
	 function drawPoly(insarMap) {
		  //console.log("Redrawing the polyline.");
		  if(polyShape) polyShape.setMap(null);
		  polyPoints.length = 0;	
		  
		  for(i = 0; i < markers.length; i++) {
			 	polyPoints.push(markers[i].getPosition());
		  }
		  // Close the shape with the last line or not
		  polyPoints.push(markers[0].getPosition());
		  polyShape = new google.maps.Polygon({paths: polyPoints, 
															strokeColor: polyLineColor});
		  polyShape.setMap(insarMap);
		}

	 function getInSarValues(uid,rpiName) {
		  console.log(rpiName);
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

		  //Construct the link to QuakeTables and turn on the display.
		  $("#QuakeTables-Link").html('<p/><a target="_blank" href="http://quakesim.usc.edu/quaketables/uavsar.jsp?uid='+uid+'">Go to download page for selected data set</a>');

	   //Call REST service
		var callResults=getImageMetadata(uid);
		  
		//Extract overlayUrl
		var overlayUrl=extractOverlayUrl(callResults);

		  //Turn off the thumbnail overlayer
//		  insarKml.setMap(null);
		  masterMap.overlayMapTypes.clear();
//		  masterMap.overlayMapTypes.removeAt(0);
		  
		  //Turn on the new overlayer
		  activateLayerMap(masterMap,overlayUrl,"line",uid,rpiName);
		  
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

	 //This is the current preferred method.  It assumes 
	 //the row id attribute has been set to the image uid value.
	 function extractRowId2(row) {
		  return row.getAttribute('id');
	 }

	 function extractRowRpiName(row) {
		  var td=row.firstChild;
		  var table2=td.firstChild;
		  var thOfTable2=((table2.firstChild).firstChild).firstChild;
		  var productName=thOfTable2.firstChild.nodeValue;
		  console.log(productName);
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

	 //This is a dangerous pattern: UID is a global variable set by a separate function
	 //when the table is clicked. As long as the order of events is preserved, then
	 //UID should be set correctly but changing things will break this function.
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
	 
	 //TODO: Note that masterMap and insarMap are synonymous.  Not clear why we need both names.
	 //TODO: Note several functions take insarMap as input but this isn't easily done with the method below.
	 function toggleFaultKml() {
		  if($("#fault_toggle_id").is(':checked')) {
				ucerfKml.setMap(masterMap);
		  }
		  else {
				ucerfKml.setMap(null);
		  }
	 }

    //The code that reads in the WMS file.  To change the WMS layer the user would update the layers 
	 //line.  As this is constructed now you need to have this code for each WMS layer.
	 //Check with your Web Map Server to see what are the required components of the address.  You may 
	 //need to add a couple of segements.  For example, the ArcServer WMS requires
	 //a CRS value which is tacked on to the end of the url.  For an example 
	 //visit http://www.gisdoctor.com/v3/arcserver_wms.html 
	 function WMSGetTileUrl2(tile, zoom) {
        var projection = masterMap.getProjection(); //NOTE masterMap is a global var (fix method?)
        var zpow = Math.pow(2, zoom);
        var ul = new google.maps.Point(tile.x * 256.0 / zpow, (tile.y + 1) * 256.0 / zpow);
        var lr = new google.maps.Point((tile.x + 1) * 256.0 / zpow, (tile.y) * 256.0 / zpow);
        var ulw = projection.fromPointToLatLng(ul);
        var lrw = projection.fromPointToLatLng(lr);
        //The user will enter the address to the public WMS layer here.  The data must be in WGS84
        //var baseURL = "http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?";
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
        url = url + "&TRANSPARENT=true"
		  return url;
	 }

	 function updateStartLat(){
		  console.log("Updating the SW marker.");
		  var newPos=new google.maps.LatLng($("#startLat-value").val(),markerSW.getPosition().lng());
		  markerSW.setPosition(newPos);
		  google.maps.event.trigger(markerSW,"drag");
		  google.maps.event.trigger(markerSW,"dragend");
	 }

	 function updateStartLon(){
		  console.log("Updating the SW marker.");
		  var newPos=new google.maps.LatLng(markerSW.getPosition().lat(),$("#startLon-value").val());
		  markerSW.setPosition(newPos);
		  google.maps.event.trigger(markerSW,"drag");
		  google.maps.event.trigger(markerSW,"dragend");
	 }

	 function updateEndLat(){
		  console.log("Updating the NE marker.");
		  var newPos=new google.maps.LatLng($("#endLat-value").val(),markerNE.getPosition().lng());
		  markerNE.setPosition(newPos);
		  google.maps.event.trigger(markerNE,"drag");
		  google.maps.event.trigger(markerNE,"dragend");
	 }

	 function updateEndLon(){
		  console.log("Updating the NE marker.");
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
		  updateEndLon:updateEndLon
	 }
	 
})();