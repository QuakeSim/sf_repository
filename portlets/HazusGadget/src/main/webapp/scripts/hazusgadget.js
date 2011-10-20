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
		  var myOpts={zoom:6, center: latlng, mapTypeId: google.maps.MapTypeId.ROADMAP};
		  map=new google.maps.Map(mapDiv, myOpts);
	 }

	 function setupSelectionBox(actionButton) {
		  google.maps.event.addListener(map,"click",function(event){
				rectangleLeftClick(event);
				activateSubmitButton(actionButton);
		  });
	 }

	 function rectangleLeftClick(event) {
		  //If the marker doesn't exist, create it.

		  if(!markerNE && !markerSW) {
				//console.log("No markers found");
				markerNE=new google.maps.Marker({map: map, 
															position: event.latLng, 
															visible: true, 
															draggable: true});
				var offset=new google.maps.LatLng(event.latLng.lat()-0.05,event.latLng.lng()-0.05);
				markerSW=new google.maps.Marker({map: map, 
															position: offset, 
															visible: true, 
															draggable: true});
				// Make markers draggable			 
				google.maps.event.addListener(markerNE, "drag", function() {
					 drawRectangle(map);
				});
				google.maps.event.addListener(markerSW, "drag", function() {
					 drawRectangle(map);
				});

				//Update the selection
				google.maps.event.addListener(markerNE, "dragend", function() {
					 
				});
				google.maps.event.addListener(markerSW, "dragend", function() {

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
		  

	 /**
	  * This is the public API
	  */
	 return {
		  createMap:createMap,
		  setupSelectionBox:setupSelectionBox,
		  activateSubmitButton:activateSubmitButton
	 }
})();