var anssgadget=anssgadget || (function() {
	 //These are "global" variables
	 var map;
	 var markers=new Array();
	 var markerNE, markerSW;
	 var polyPoints;
	 var polyShape;
    var polyLineColor = "#3355ff";
    var polyFillColor = "#335599";
	 var urlBase="http://localhost:8080/AnssCatalogService/catalog";
	 var amp="&";
	 //These are parameters needed by the anss service.
	 var OUTPUT_TYPE="output=kml";
	 var OUTPUT_FORMAT="format=cnss";
	 var MINTIME="mintime=";
	 var MAXTIME="maxtime=";
	 var MINMAG="minmag=";
	 var MAXMAG="maxmag=";
	 var ETYPE="etype=E";
	 var OUTPUT_LOC="outputloc=web";
	 var MINLON="minlon=";
	 var MAXLON="maxlon=";
	 var MINLAT="minlat=";
	 var MAXLAT="maxlat=";
	 

	 function createMap(mapDiv) {
		  var latlng=new google.maps.LatLng(33.3,-118.0);
		  var myOpts={zoom:5, center: latlng, mapTypeId: google.maps.MapTypeId.ROADMAP};
		  map=new google.maps.Map(mapDiv, myOpts);
		  

//		  var kmlMapOpts={map:map, clickable:false, preserveViewport:true};
//		  var forecastKml=new google.maps.KmlLayer("http://yodubuntu.physics.ucdavis.edu/quakemaps/scorecardKML.kml",kmlMapOpts);
	 }

	 function setupSelectionBox(lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3, bboxDiv) {
		  google.maps.event.addListener(map,"click",function(event){
				rectangleLeftClick(event,lat0, lon0, lat1, lon1, lat2, lon2, lat3, lon3, bboxDiv);
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
				var offset=new google.maps.LatLng(event.latLng.lat()-1.0,event.latLng.lng()-1.0);
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

	 function submitMapRequest() {
		  //Note this assumes the AnssCatalogService is co-located.
		  console.log("Submitting request");
		  var mintime=MINTIME+"2002/01/01,00:00:00";
		  var maxtime=MAXTIME+"2010/01/01,00:00:00";
		  var minmag=MINMAG+"4.0";
		  var maxmag=MAXMAG+"10.0";
		  var minlon=MINLON+"-120";
		  var maxlon=MAXLON+"-116";
		  var minlat=MINLAT+"31";
		  var maxlat=MAXLAT+"35";
		  var finalUrl=urlBase+"?"+OUTPUT_TYPE+amp+OUTPUT_FORMAT+amp+mintime+amp+maxtime+amp+minmag+amp+maxmag+amp+ETYPE+amp+OUTPUT_LOC+amp+minlon+amp+maxlon+amp+minlat+amp+maxlat;
		  console.log(finalUrl);
		  console.log(finalUrl.value);

		  var kmlMapOpts={map:map};
		  var seismicCatalogLayer=new google.maps.KmlLayer(finalUrl,kmlMapOpts);
		  google.maps.event.addListener(seismicCatalogLayer,"metadata_changed",function(){
				console.log("Metadata changed");
		  });

//		  var results=$.ajax({url:finalUrl,async:false}).responseText;
	 }

	 /**
	  * This is the public API
	  */
	 return {
		  createMap:createMap,
		  setupSelectionBox:setupSelectionBox,
		  submitMapRequest:submitMapRequest,
	 }
})();