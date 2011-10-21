var hazusgadget=hazusgadget || (function() {
	 //These are "global" variables
	 var map;
	 var markers=new Array();
	 var markerNE, markerSW;
	 var polyPoints;
	 var polyShape;
    var polyLineColor = "#3355ff";
    var polyFillColor = "#335599";


	 function createMap(mapDiv) {
		  var latlng=new google.maps.LatLng(33.3,-118.0);
		  var myOpts={zoom:7, center: latlng, mapTypeId: google.maps.MapTypeId.ROADMAP};
		  map=new google.maps.Map(mapDiv, myOpts);
		  
		  var kmlMapOpts={map:map, clickable:false, preserveViewport:true};
		  var forecastKml=new google.maps.KmlLayer("http://yodubuntu.physics.ucdavis.edu/quakemaps/scorecardKML.kml",kmlMapOpts);
	 }

	 function setupSelectionBox(actionButton,lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3, bboxDiv) {
		  google.maps.event.addListener(map,"click",function(event){
				rectangleLeftClick(event,lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3, bboxDiv);
				activateSubmitButton(actionButton);
		  });
	 }

	 function rectangleLeftClick(event, lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3, bboxDiv) {
		  //If the marker doesn't exist, create it.

		  if(!markerNE && !markerSW) {
				//console.log("No markers found");
				markerNE=new google.maps.Marker({map: map, 
															position: event.latLng, 
															visible: true, 
															draggable: true});
				var offset=new google.maps.LatLng(event.latLng.lat()-0.2,event.latLng.lng()-0.2);
				markerSW=new google.maps.Marker({map: map, 
															position: offset, 
															visible: true, 
															draggable: true});
				setBoundingBox(markerSW, markerNE, lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3,bboxDiv);
				drawRectangle(map);

				// Make markers draggable			 
				google.maps.event.addListener(markerNE, "drag", function() {
					 drawRectangle(map);
				});
				google.maps.event.addListener(markerSW, "drag", function() {
					 drawRectangle(map);
				});

				//Update the selection
				google.maps.event.addListener(markerNE, "dragend", function() {
					 setBoundingBox(markerSW, markerNE, lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3, bboxDiv);
				});
				google.maps.event.addListener(markerSW, "dragend", function() {
					 setBoundingBox(markerSW, markerNE, lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3, bboxDiv);
				});

		  }
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
	 
	 function activateSubmitButton(actionButton) {
		  actionButton.disabled=false;
	 }
		  
	 function setBoundingBox(markerSW, markerNE, lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3) {
		  var cornerSE=new google.maps.LatLng((markerNE.getPosition()).lat(),(markerSW.getPosition()).lng());
		  var cornerNW=new google.maps.LatLng((markerSW.getPosition()).lat(),(markerNE.getPosition()).lng());

		  lat0.value=markerSW.getPosition().lat();
		  lon0.value=markerSW.getPosition().lng();

		  lat1.value=cornerNW.lat();
		  lon1.value=cornerNW.lng();

		  lat2.value=markerNE.getPosition().lat();
		  lon2.value=markerNE.getPosition().lng();

		  lat3.value=cornerSE.lat();
		  lon3.value=cornerSE.lng();

		  bboxDiv.innerHTML="("+lat0.value+","+lon0.value+")"+"("+lat1.value+","+lon1.value+")"+"("+lat2.value+","+lon2.value+")"+"("+lat3.value+","+lon3.value+")";		  
	 }

	 /**
	  * This is the public API
	  */
	 return {
		  createMap:createMap,
		  setupSelectionBox:setupSelectionBox,
		  activateSubmitButton:activateSubmitButton
	 }
})();