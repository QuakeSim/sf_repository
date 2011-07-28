<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%> 
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%> 

<html>
  <head>
  <head> 
	 <link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/quakesim_style.css"/>	 
	 <!--Google and related APIs are imported here -->
	 <!--
	 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" 
				type="text/javascript"></script>
	 -->
	 <script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script> 
  </head>
  <body>
	 <div id="InSARMap" style="width: 800px; height: 600px;"></div>
	 
	 <script>
	 
    //Global variables
	 var polyShape;
    var polyLineColor = "#3355ff";
    var polyFillColor = "#335599";
    var polyPoints = new Array();
    var markers = new Array();

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
		var insarMap=new google.maps.Map(document.getElementById("InSARMap"),myOptions);
				
      //Add the KML Layer
var lowResSARLayer=new google.maps.KmlLayer("http://gf19.ucs.indiana.edu:9898/uavsar-data/SanAnd_08504_10028-001_10057-101_0079d_s01_L090_01/SanAnd_08504_10028-001_10057-101_0079d_s01_L090HH_01.int.kml",{suppressInfoWindows: true, map: insarMap, clickable: false});
//var highResSARLayer=new google.maps.KmlLayer("http://gf19.ucs.indiana.edu:9898/uavsar-data/SanAnd_08504_10028-001_10057-101_0079d_s01_L090_01/SanAnd_08504_10028-001_10057-101_0079d_s01_L090HH_01.int.kmz",{suppressInfoWindows:true});

     google.maps.event.addListener(insarMap,"click",function(event) {
			leftClick(event)
	  });

      //Now draw a polygon
      function leftClick(event) {
			 //console.log("leftclick called");
			 // Square marker icons
			 //var square = new GIcon();
			 //square.image = "mapIcons/square.png";
			 //addIcon(square);
			 
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
	 </script>
  </body>
</html>