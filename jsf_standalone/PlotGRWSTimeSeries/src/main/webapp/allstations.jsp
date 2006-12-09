<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<html>
  <head>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBTCdqtmO6Kma7uYZgpagQkNe17MQhRS93QdZFchZ2Vy9IpcH0W3nbN34g"
      type="text/javascript"></script>
  </head>
  <body>
   
   <!--This is an outer bounding table to make columns.-->
   <table border="1">  
   <tr>
   <td>
    <table>
      <tr>
        <td width="550" colspan="2">
          <b><font size="4">SOPAC Real Time GPS Networks</font></b><p>
            <font size="2">Click on a station symbol for more
              information on a particular station.  Then click the "Query 
              Selected Station" link below the map.
             </font><p></p></td>
            </tr>
      <tr>
        <td>
          <div id="map" style="width: 500px; height: 500px">      </div>
        </td>
       </tr>
    </table>

  <f:view>
   <h:outputText value="#{stfilterBean.mapCenterX}"/>
 
    <!-- fail nicely if the browser has no Javascript -->
    <noscript><b>JavaScript must be enabled in order for you to use Google Maps.</b>
      However, it seems JavaScript is either disabled or not supported by your browser.
      To view Google Maps, enable JavaScript by changing your browser options, and then
      try again.
    </noscript>


    <script type="text/javascript">
	document.write("test");
        var req;
        var baseIcon = new GIcon();
        baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
        baseIcon.iconSize = new GSize(15, 20);
        baseIcon.shadowSize = new GSize(10, 10);
        baseIcon.iconAnchor = new GPoint(1, 10);
        baseIcon.infoWindowAnchor = new GPoint(5, 1);
        baseIcon.infoShadowAnchor = new GPoint(5, 5);

        // Create the map
        var map = new GMap2(document.getElementById("map"));
        map.addControl(new GLargeMapControl());
        map.addControl(new GMapTypeControl());
        map.addControl(new GScaleControl());
        map.setCenter(new GLatLng(${stfilterBean.mapCenterX},
			${stfilterBean.mapCenterY}), 7);

	for(i=0;i<${stfilterBean.networkBeanArraySize};i++) {
 
          var icon = new GIcon(baseIcon);
 	  var junk=${stfilterBean.networkBeanArray[0].stationBeanArraySize};
          icon.image = "http://labs.google.com/ridefinder/images/mm_20_green.png";
          var stations = new Array(${stfilterBean.networkBeanArray[0].stationBeanArraySize});
          var Markers = new Array(${stfilterBean.networkBeanArray[0].stationBeanArraySize});

          for (j = 0; j < Markers.length; j++){         

	      Markers[j]=createMarker(${stfilterBean.networkBeanArray[0].networkName},                         "world",
                          "-118","37",icon);
              map.addOverlay(Markers[j]);
	  }


        }
	
        function createMarker(networkName, name, lon, lat, icon) {
          var marker = new GMarker(new GLatLng(lat,lon),icon);
          // Show this marker's name in the info window when it is clicked
          var html = "<b>Station Name= </b>" + name + "<br><b>Lat=</b>" + lat + "<br><b>Lon= </b>" + lon + "<br><b>Network= </b>" + networkName;


          GEvent.addListener(marker, "click", function() {
            marker.openInfoWindowHtml(html);});

          GEvent.addListener(marker, "click", function() {
		var newElement=document.getElementById("form1:station_name");
		newElement.setAttribute("value",name);
	  });
          return marker;
        }


      </script>

      </td>
     </table>
      </f:view>

     </body>
</html>
