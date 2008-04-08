<html>
<head>
  <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBQozjQdf4FEuMBqpopduISAOADS4xTilRYX9d1ZU0uvBJwyY4gerC4Gog"
      type="text/javascript"></script>
</head>
<body onload="initialize()" onunload="GUnload()">
<script language="JavaScript"> 

	 var map=null;
	 var geocoder=null;
	 
	 function initialize() {
	     map=new GMap2(document.getElementById("map"));
    	  map.setCenter(new GLatLng(32,-118),7);
    	  map.addControl(new GLargeMapControl());
    	  map.addControl(new GMapTypeControl());

		  geocoder=new GClientGeocoder();
    }

	 function showAddress(address) {
      if (geocoder) {
        geocoder.getLatLng(
          address,
          function(point) {
            if (!point) {
              alert(address + " not found");
            } else {
              map.setCenter(point, 12);
              var marker = new GMarker(point);
              map.addOverlay(marker);
              marker.openInfoWindowHtml(address+"<br>"+"Latitude: "+point.lat()
				  									 +"<br>"+"Longitude: "+point.lng());
            }
          }
        );
      }
    }
 

</script>

    <form action="#" onsubmit="showAddress(this.address.value); return false">
      <p>
        <input type="text" size="60" name="address" value="4800 Oak Grove Drive, Pasadena, CA 90089-8099" />
        <input type="submit" value="Go!" />
      </p>
      <div id="map" style="width: 800px; height: 800px"></div>
    </form>  
</body>
</html>
