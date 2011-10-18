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
	 var insarKml;
	 var lowResSARLayer=null;
	 var dygraphLOSOpts={width:300,height:300,title:'InSAR Line of Sight Values',xlabel:'Distance',ylabel:'LOS Value'};
	 var dygraphHgtOpts={width:300,height:300,title:'InSAR Height Values',xlabel:'Distance',ylabel:'Height'};

	 function setMasterMap(insarMapDiv,tableDivName) {
		  var latlng=new google.maps.LatLng(32.3,-118.0);
		  var myOpts={zoom:6, center: latlng, mapTypeId: google.maps.MapTypeId.ROADMAP};
		  masterMap=new google.maps.Map(insarMapDiv, myOpts);
		  		  
		  var kmlMapOpts={map:masterMap, suppressInfoWindows:true, preserveViewport:true};
		  insarKml = new google.maps.KmlLayer("http://quaketables.quakesim.org/kml?uid=all&ov=0",kmlMapOpts);
		  
		  //Find out where we are.
		  google.maps.event.addListener(insarKml,"click",function(event) {
				var finalUrl=constructWmsUrl(masterMap,event);
				var results=$.ajax({url:finalUrl,async:false}).responseText;
				var parsedResults=jQuery.parseJSON(results);
				createTable(parsedResults,tableDivName);
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

	 //Activates the low-res insar layer.  
	 function activateLayerMap(insarMap,overlayUrl,drawFunctionType,uid) {
        //Add the KML Layer
		  console.log("Draw function type is "+drawFunctionType);
		  if(lowResSARLayer) lowResSARLayer.setMap(null);  //Remove any previous layers.
		  lowResSARLayer=new google.maps.KmlLayer(overlayUrl,{suppressInfoWindows: true, map: insarMap, clickable: false});
		  
		  google.maps.event.addListener(insarMap,"click",function(event) {
			if(drawFunctionType=="polygon") {
				 polygonLeftClick(insarMap,event);
			}
				else if(drawFunctionType=="rectangle") {
					 rectangleLeftClick(insarMap,event,uid);
				}
				else if(drawFunctionType=="line") {
					 lineLeftClick(insarMap,event,uid);
				}
				else {
					 alert("Invalid draw method provided. Should be either 'polygon' or 'rectangle'. Using 'rectangle' by default.");
					 rectangleLeftClick(isarMap,event,uid);
				}
		  });
	 }
    
	 function lineLeftClick(insarMap,event,uid) {
		  console.log("Uid is "+uid);
		  //If the marker doesn't exist, create it.
		  if(!markerNE && !markerSW) {
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
		  		
				getInSarValues(uid);

				// Make markers draggable			 
				google.maps.event.addListener(markerNE, "drag", function() {
					 drawLine(insarMap);
				});
				google.maps.event.addListener(markerSW, "drag", function() {
					 drawLine(insarMap);
				});

				google.maps.event.addListener(markerNE, "dragend", function() {
					 getInSarValues(uid);
				});
				google.maps.event.addListener(markerSW, "dragend", function() {
					 getInSarValues(uid);
				});
		  }

		  //This is called after a drag event.
		  drawLine(insarMap);

	 }

	 function rectangleLeftClick(insarMap,event,uid) {
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

				getSimplexValues(uid);
		  		
				// Make markers draggable			 
				google.maps.event.addListener(markerNE, "drag", function() {
					 drawRectangle(insarMap);
				});
				google.maps.event.addListener(markerSW, "drag", function() {
					 drawRectangle(insarMap);
				});

				//Update the selection
				google.maps.event.addListener(markerNE, "dragend", function() {
					 getSimplexValues(uid);
					 console.log("NE Drag End");
				});
				google.maps.event.addListener(markerSW, "dragend", function() {
					 getSimplexValues(uid);
					 console.log("SW Drag End");
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
														  strokeColor:polyLineColor});
		  polyShape.setMap(insarMap);
		  
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
														 strokeColor:polyLineColor});
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

	 function getSimplexValues(uid) {
		  var cornerSE=new google.maps.LatLng((markerNE.getPosition()).lat(),(markerSW.getPosition()).lng());
		  var cornerNW=new google.maps.LatLng((markerSW.getPosition()).lat(),(markerNE.getPosition()).lng());

		  var swMarkerLat=markerSW.getPosition().lat();
		  var swMarkerLon=markerSW.getPosition().lng();
		  var seMarkerLat=cornerSE.lat();
		  var seMarkerLon=cornerSE.lng();
		  var neMarkerLat=markerNE.getPosition().lat();
		  var neMarkerLon=markerNE.getPosition().lng();
		  var nwMarkerLat=cornerNW.lat();
		  var nwMarkerLon=cornerNW.lng();

		  //This must be clockwise. 
		  var restUrl="/InSAR-LOS-REST/insarsimplex/"+uid+"/"+nwMarkerLon+"/"+nwMarkerLat+"/"+neMarkerLon+"/"+neMarkerLat+"/"+seMarkerLon+"/"+seMarkerLat+"/"+swMarkerLon+"/"+swMarkerLat;
		  console.log(restUrl);
		  var csv=$.ajax({
				url:restUrl,
				async:false
		  }).responseText;

		  console.log(csv);
	 }

	 function getInSarValues(uid) {
		  getLosInSarValues(uid);
		  getHgtInSarValues(uid);
	 }

	 function getLosInSarValues(uid) {
		  console.log("Uid is "+uid);
		  var westMarkerLat=markerSW.getPosition().lat();
		  var westMarkerLon=markerSW.getPosition().lng();
		  var eastMarkerLat=markerNE.getPosition().lat();
		  var eastMarkerLon=markerNE.getPosition().lng();

		  var restUrl="/InSAR-LOS-REST/insarlos/csv/"+uid+"/"+westMarkerLon+"/"+westMarkerLat+"/"+eastMarkerLon+"/"+eastMarkerLat;
		  var csv=$.ajax({
				url:restUrl,
				async:false
		  }).responseText;
		  var g1=new Dygraph(document.getElementById("outputGraph1"),csv,dygraphLOSOpts);		  
	 }

	 function getHgtInSarValues(uid) {
		  console.log("Uid is "+uid);
		  var westMarkerLat=markerSW.getPosition().lat();
		  var westMarkerLon=markerSW.getPosition().lng();
		  var eastMarkerLat=markerNE.getPosition().lat();
		  var eastMarkerLon=markerNE.getPosition().lng();

		  var restUrl="/InSAR-LOS-REST/insarhgt/csv/"+uid+"/"+westMarkerLon+"/"+westMarkerLat+"/"+eastMarkerLon+"/"+eastMarkerLat;
		  var csv=$.ajax({
				url:restUrl,
				async:false
		  }).responseText;
		  var g2=new Dygraph(document.getElementById("outputGraph2"),csv,dygraphHgtOpts);		  
	 }
		  
	 function createTable(parsedResults,tableDivName) {
		var dynatable='<table border="1">';
		//Create the header row.
		dynatable+='<tr>';
		for(var index1 in parsedResults[0]) {
		dynatable+='<th>'+index1+'</th>';
		}
		dynatable+='</tr>';
		//Fill in the table.
		for (var index1 in parsedResults) {
		dynatable+='<tr onmouseover="sarselect.selectedRow(this)" onmouseout="sarselect.unselectedRow(this)" onclick="sarselect.selectRowAction(this)">';
		for(var index2 in parsedResults[index1]) {
		dynatable+='<td>'+parsedResults[index1][index2]+'</td>';
		}
		dynatable+='</tr>'
		}
		dynatable+='</table>';
		document.getElementById(tableDivName).innerHTML=dynatable;
	 }
	 
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
	    row.style.backgroundColor="gray";
		 row.style.cursor="pointer";
	 }
	 function unselectedRow(row) {
	    row.style.backgroundColor="white";
		 row.style.cursor="default";
	 }
	 function selectRowAction(row){
	     //Find the ID of the row
		  var uid=extractRowId(row);

	   //Call REST service
		var callResults=getImageMetadata(uid);
		
		//Extract overlayUrl
		var overlayUrl=extractOverlayUrl(callResults);

		  //Turn off the master overlayer
		  insarKml.setMap(null);
		  
		  //Turn on the new overlayer
		  activateLayerMap(masterMap,overlayUrl,"rectangle",uid);
		//Redirect to next page
		//parent.location="./SARSelectRegion.faces?overlayUrl="+overlayUrl;
	 }
	 
	 function extractOverlayUrl(callResults){
	   var overlayUrl;
		for(var index in callResults) {
		    if(callResults[index].datatype=="int") {
			    overlayUrl=callResults[index].kml;
			 }
		}
	   return overlayUrl;
	 }

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
		  activateLayerMap: activateLayerMap
	 }
	 
})();