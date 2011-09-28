var sarselect=sarselect || (function() {
    //Global variables
	 var polyShape;
    var polyLineColor = "#3355ff";
    var polyFillColor = "#335599";
    var polyPoints = new Array();
    var markers = new Array();
	 var insarMap;
	 var leftClickOp;
	 var markerNE, markerSW;
 
	 function setLOSMap(insarMapDiv,overlayUrl,drawFunctionType) {

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
		insarMap=new google.maps.Map(insarMapDiv,myOptions);
				
      //Add the KML Layer
		  var lowResSARLayer=new google.maps.KmlLayer(overlayUrl,{suppressInfoWindows: true, map: insarMap, clickable: false});

     google.maps.event.addListener(insarMap,"click",function(event) {
			if(drawFunctionType=="polygon") {
				 polygonLeftClick(event);
			}
			else if(drawFunctionType=="rectangle") {
				 rectangleLeftClick(event);
			}
			else if(drawFunctionType=="line") {
				 lineLeftClick(event);
			}
			else {
				 alert("Invalid draw method provided. Should be either 'polygon' or 'rectangle'. Using 'rectangle' by default.");
				 rectangleLeftClick(event);
			}
	  });
	 }
     
	 function lineLeftClick(event) {
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
		  		
				getLosInSarValues();

				// Make markers draggable			 
				google.maps.event.addListener(markerNE, "drag", function() {
					 drawLine();
				});
				google.maps.event.addListener(markerSW, "drag", function() {
					 drawLine();
				});

				google.maps.event.addListener(markerNE, "dragend", function() {
					 getLosInSarValues();
				});
				google.maps.event.addListener(markerSW, "dragend", function() {
					 getLosInSarValues();
				});
		  }

		  //This is called after a drag event.
		  drawLine();

	 }

	 function rectangleLeftClick(event) {
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
					 drawRectangle();
				});
				google.maps.event.addListener(markerSW, "drag", function() {
					 drawRectangle();
				});
		  }

		  //This is called after a drag event.
		  drawRectangle();
	 }

	 function polygonLeftClick(event) {
		  //Make the marker and add to the map.
		  //console.log("Create marker and add to the map");
		  var marker=new google.maps.Marker({map: insarMap, 
														 position: event.latLng, 
														 visible: true, 
														 draggable: true});
		  markers.push(marker);
		  
		  // Make markers draggable			 
		  google.maps.event.addListener(marker, "drag", function() {
				drawPoly();
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
				drawPoly();
		  });
		  drawPoly();
		  //console.log("Done with leftClick()");
	 }

	 function drawLine() {
		  if(polyShape) polyShape.setMap(null);
		  polyPoints=new Array();
		  polyPoints.push(markerNE.getPosition());
		  polyPoints.push(markerSW.getPosition());
		  polyShape=new google.maps.Polyline({path:polyPoints,
														  strokeColor:polyLineColor});
		  polyShape.setMap(insarMap);
		  
	 }

	 function drawRectangle() {
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
	 
	 function drawPoly() {
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

	 function getLosInSarValues() {
		  var westMarkerLat=markerSW.getPosition().lat();
		  var westMarkerLon=markerSW.getPosition().lng();
		  var eastMarkerLat=markerNE.getPosition().lat();
		  var eastMarkerLon=markerNE.getPosition().lng();
		  
		  var restUrl="/InSAR-LOS-REST/insarlos/csv/"+westMarkerLon+"/"+westMarkerLat+"/"+eastMarkerLon+"/"+eastMarkerLat;
		  console.log(restUrl);
		  var csv=$.ajax({
				url:restUrl,
				async:false
		  }).responseText;
		  var g=new Dygraph(document.getElementById("outputGraph"),csv);		  
	 }


	 /**
	  * Public API for sarselect.js
	  */
	 return {
		  setLOSMap: setLOSMap,
		  getLosInSarValues: getLosInSarValues
	 }
	 
})();