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
	 var insarKml, ucerfKml;
	 var rowSelected=null;
	 var lowResSARLayer=null;
	 var uid=null;  //This is global because we need to pass it between two unrelated functions. Not good.
	 var dygraphLOSOpts={width:290,height:300,drawPoints:true,pointSize:2,strokeWidth:0.0,title:'InSAR Line of Sight Values',xlabel:'Distance (km)',ylabel:'LOS Value (cm)'};
	 var dygraphHgtOpts={width:290,height:300,drawPoints:true,pointSize:2,strokeWidth:0.0,title:'InSAR Height Values',xlabel:'Distance (km)',ylabel:'Height (m)'};

	 var blueIcon = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + "FF0000",new google.maps.Size(21,34),new google.maps.Point(0,0),new google.maps.Point(10,34));															  
	 var redIcon = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + "0000FF",new google.maps.Size(21,34),new google.maps.Point(0,0),new google.maps.Point(10,34));


	 function setMasterMap(insarMapDiv,tableDivName) {
		  var latlng=new google.maps.LatLng(32.3,-118.0);
		  var myOpts={zoom:6, center: latlng, mapTypeId: google.maps.MapTypeId.TERRAIN};
		  masterMap=new google.maps.Map(insarMapDiv, myOpts);
		  
		  //Add UAVSAR thumb overlay
		  var kmlMapOpts={map:masterMap, suppressInfoWindows:true, preserveViewport:true};
		  insarKml=new google.maps.KmlLayer("http://quakesim.usc.edu/uavsar-data/kml/QuakeTables_UAVSAR_lowres.kmz",kmlMapOpts);
//		  insarKml=new google.maps.KmlLayer("http://quaketables.quakesim.org/kml?uid=all&lowres=1",kmlMapOpts);
//		  insarKml=new google.maps.KmlLayer("@host.base.url@/quakesim_uavsar.kml",kmlMapOpts);
//		  insarKml = new google.maps.KmlLayer("http://quaketables.quakesim.org/kml?uid=all&ov=0",kmlMapOpts);
		  $("#InSAR-Map-Messages").show();
		  $("#InSAR-Map-Messages").html("InSAR Catalog Loading...");

		  //Add a listener for the insarKml map while it is loading.
		  google.maps.event.addListener(insarKml,"metadata_changed",function(event) {
				$("#InSAR-Map-Messages").hide();
		  });
		  
		  //Find out where we are.
		  google.maps.event.addListener(insarKml,"click",function(event) {
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
	 function activateLayerMap(insarMap,overlayUrl,drawFunctionType,uid) {

		  //Remove any previous layers and listeners

		  if(lowResSARLayer) lowResSARLayer.setMap(null);  
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
		  var ucerfMapOpts={map:masterMap, preserveViewport:true};
		  ucerfKml=new google.maps.KmlLayer("@host.base.url@/InSAR-LOS/kml//QuakeTables_UCERF_2.4.kml",ucerfMapOpts);


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
					 lineLeftClick(insarMap,event,uid);
				}
				else {
					 alert("Invalid draw method provided. Should be either 'line', 'polygon', or 'rectangle'. Using 'rectangle' by default.");
					 rectangleLeftClick(isarMap,event);
				}
		  });
	 }
    
	 function lineLeftClick(insarMap,event,uid) {
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
				getInSarValues(uid);
				showEndpoints();

				// Make markers draggable			 
				google.maps.event.addListener(markerNE, "drag", function() {
					 drawLine(insarMap);
				});
				google.maps.event.addListener(markerSW, "drag", function() {
					 drawLine(insarMap);
				});

				google.maps.event.addListener(markerNE, "dragend", function() {
					 getInSarValues(uid);
					 showEndpoints();
				});
				google.maps.event.addListener(markerSW, "dragend", function() {
					 getInSarValues(uid);
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
		  var x=Math.cos(swLat*d2r)*Math.sin(neLat*d2r)-Math.sin(swLat*d2r)*Math.cos(swLat*d2r)*Math.cos(dlon);
		  console.log(x, y, dlon);
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

	 function getInSarValues(uid) {
		  var resolution=$("#resolution-value").val();
		  var averaging=$("#averaging-value").val();
		  var method="native";
		  if($("#average-method").is(':checked')) {
				method="average";
		  }
		  else {
				method="native";
		  }
		  getLosInSarValues(uid,resolution,method,averaging);
		  getHgtInSarValues(uid,resolution,method,averaging);
	 }
	 
	 function getLosInSarValues(uid,resolution,method,averaging) {
		  var westMarkerLat=markerSW.getPosition().lat();
		  var westMarkerLon=markerSW.getPosition().lng();
		  var eastMarkerLat=markerNE.getPosition().lat();
		  var eastMarkerLon=markerNE.getPosition().lng();
		  
		  var restUrl="/InSAR-LOS-REST/insarlos/csv/"+uid+"/"+resolution+"/"+westMarkerLon+"/"+westMarkerLat+"/"+eastMarkerLon+"/"+eastMarkerLat+"/"+method+"/"+averaging;
        console.log(restUrl);

		  var csv=$.ajax({
				url:restUrl,
				async:false
		  }).responseText;
		  var g1=new Dygraph(document.getElementById("outputGraph1"),csv,dygraphLOSOpts);		  
		  $("#LOS-Data-Download").html("<center><a href='"+restUrl+"' target='_blank'>Download LOS Data</a></center>");
	 }

	 function getHgtInSarValues(uid,resolution,method,averaging) {
		  var westMarkerLat=markerSW.getPosition().lat();
		  var westMarkerLon=markerSW.getPosition().lng();
		  var eastMarkerLat=markerNE.getPosition().lat();
		  var eastMarkerLon=markerNE.getPosition().lng();

		  var restUrl="/InSAR-LOS-REST/insarhgt/csv/"+uid+"/"+resolution+"/"+westMarkerLon+"/"+westMarkerLat+"/"+eastMarkerLon+"/"+eastMarkerLat+"/"+method+"/"+averaging;
        console.log(restUrl);

		  var csv=$.ajax({
				url:restUrl,
				async:false
		  }).responseText;
		  var g2=new Dygraph(document.getElementById("outputGraph2"),csv,dygraphHgtOpts);		  
		  $("#HGT-Data-Download").html("<center><a href='"+restUrl+"' target='_blank'>Download HGT Data</a></center>");
	 }
	 
	 function createTable(parsedResults,tableDivName) {
		  //Fill in the table.
		  var dynatable='<table class="sartable-outer">';
		  for (var index1 in parsedResults) {
				dynatable+='<tr onmouseover="sarselect.selectedRow(this)" onmouseout="sarselect.unselectedRow(this)" onclick="sarselect.selectRowAction(this)" id="'+parsedResults[index1]['uid']+'"'+'>';
				dynatable+='<td><table class="sartable-inner" border="1"><tr>';
				dynatable+='<td colspan="2">'+parsedResults[index1]['dataname']+'</td>';
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
		
		  console.log("Constructed URL:"+urlToCall);
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

	   //Call REST service
		var callResults=getImageMetadata(uid);
		
		//Extract overlayUrl
		var overlayUrl=extractOverlayUrl(callResults);

		  //Turn off the master overlayer
		  insarKml.setMap(null);
		  
		  //Turn on the new overlayer
		  activateLayerMap(masterMap,overlayUrl,"line",uid);
		  
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
		  getInSarValues(uid);
	 }

	 //This is a dangerous pattern: UID is a global variable set by a separate function
	 //when the table is clicked. As long as the order of events is preserved, then
	 //UID should be set correctly but changing things will break this function.
	 function plotAverage(){
		  //Enable the "averaging" input field.
		  $("#averaging-value").attr('disabled',false);
		  getInSarValues(uid);
	 }

	 function updateResolution() {
		  getInSarValues(uid);
	 }

	 function updateAveraging() {
		  getInSarValues(uid);
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
		  updateAveraging:updateAveraging
	 }
	 
})();