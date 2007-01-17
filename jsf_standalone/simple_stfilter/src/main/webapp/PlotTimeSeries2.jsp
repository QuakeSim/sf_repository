<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<%@page import="java.util.*, cgl.sensorgrid.sopac.gps.GetStationsRSS, java.io.*, java.lang.*"%>

<%

%>

<html>
  <head>
    <script src="http://maps.google.com/maps?file=api&v=2&key=ABQIAAAAKcOFua2izv3xh_wg-gvdExTAQYcFNijgb9J3KVaLw6xnNh3fkBS1ZczEcqaeg0P3s-wHzWuBR40ipw " type="text/javascript"></script>
    <script language="JavaScript" src="NmapAPI.js" type="text/javascript"></script>

  </head>
  <body>
    <table>
      <tr>
        <td width="650" colspan="2">
          <b><font size="4" face="Verdana">SOPAC Real Time GPS Networks</font></b><p>
            <font face="Verdana" size="2">Click on a station symbol for more
              information.</font><p></p></td>
            </tr>
      <tr>
        <td width="600">
          <div id="map" style="width: 750px; height: 600px">      </div>
        </td>
        <td valign="middle" width="50">
          <div id="networksDiv"> Network Names and Colors     </div>
        </td>
      </tr>
    </table>


    <!-- fail nicely if the browser has no Javascript -->
    <noscript>
       <b>JavaScript must be enabled in order for you to use Google 
       Maps.</b>.  However, it seems JavaScript is either disabled or not 
       supported by your browser. To view Google Maps, enable JavaScript by 
       changing your browser options, and then try again.
    </noscript>

    <script type="text/javascript">
        var req;
        var baseIcon = new GIcon();
        baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
        baseIcon.iconSize = new GSize(15, 20);
        baseIcon.shadowSize = new GSize(10, 10);
        baseIcon.iconAnchor = new GPoint(1, 10);
        baseIcon.infoWindowAnchor = new GPoint(5, 1);
        baseIcon.infoShadowAnchor = new GPoint(5, 5);

        // Create the map
        var map = new GMap(document.getElementById("map"));
        map.addControl(new GLargeMapControl());
        map.addControl(new GMapTypeControl());
        map.addControl(new GScaleControl());
        map.centerAndZoom(new GPoint(${stfilterBean.mapCenterY, 
                                     ${stfilterBean.mapCenterX}), 10);

        var colors = new Array (6);
        colors[0]="red";
        colors[1]="green";
        colors[2]="blue";
        colors[3]="black";
        colors[4]="white";
        colors[5]="yellow";
        colors[6]="purple";
        colors[7]="brown";

        var networkInfo = new Array (<%=networkNames.size()%>);
        for (i = 0; i < networkInfo.length; ++ i){
          networkInfo [i] = new Array (2);
        }

        function overlayNetworks(){
          var icon = new GIcon(baseIcon);
          icon.image = "http://labs.google.com/ridefinder/images/mm_20_green.png";

          <%
          for (int j = 0; j < networkNames.size(); j++) {
            String networkName = (String)networkNames.get(j);
            Vector stationsVec = RSSBeanID.getStationsVec(networkName);
            %>
            networkInfo [<%=j%>] [0] = "<%=networkName%>";

            var k;
            if(<%=j%>>=colors.length)
            k = 0;
            else
            k=<%=j%>;
            icon.image = "http://labs.google.com/ridefinder/images/mm_20_" + colors[k] + ".png";
            networkInfo [<%=j%>] [1] = "http://labs.google.com/ridefinder/images/mm_20_" + colors[k] + ".png";

            var stationCount = <%=stationsVec.size()%>;
            var stations = new Array (stationCount);
            var Markers = new Array (stationCount);
            for (i = 0; i < stations.length; ++ i){
              stations [i] = new Array (3);
            }
              stations [<%=i%>] [0] = "<%=name%>";
              stations [<%=i%>] [1] = "<%=lat%>";
              stations [<%=i%>] [2] = "<%=lon%>";

              var html = "<b>Station Name= </b>" + "<%=name%>" + "<br><b>Lat=</b>" + "<%=lat%>" + "<br><b>Lon= </b>" + "<%=lon%>" + "<br><b>Network= </b>" + "<%=networkName%>";

	     var infoTabs = [new GInfoWindowTab("Current X", '<%=x_tabcontent%>'),new GInfoWindowTab("Current Y", '<%=y_tabcontent%>'),new GInfoWindowTab("Current Z", '<%=z_tabcontent%>')];  

              Markers[<%=i%>] = createTabsInfoMarker(new GPoint("<%=lon%>", "<%=lat%>") , infoTabs , icon);

              map.addOverlay(Markers[<%=i%>]);
              <%
            }
          }
          %>
        }

        function createMarker(networkName, name, lon, lat, icon) {
          var marker = new GMarker(new GPoint(lon, lat),icon);
          // Show this marker's name in the info window when it is clicked
          var html = "<b>Station Name= </b>" + name + "<br><b>Lat=</b>" + lat + "<br><b>Lon= </b>" + lon + "<br><b>Network= </b>" + networkName;
          GEvent.addListener(marker, "click", function() {
            marker.openInfoWindowHtml(html);});
            return marker;
          }
        overlayNetworks();
        function printNetworkColors (array)
        {
          var html = "<table border='0'><tr><td><b>Network</b></td><td nowrap><b>Icon Color<b></td></tr>";

          var row;
          for (row = 0; row < array.length; ++ row)
          {
            html = html + " <tr>";
            var col;
            for (col = 0; col < array [row] . length; ++ col){
              if(col==0)
              html = html + "  <td>" + array [row] [col] + "</td>";
              if(col==1)
              html = html + "  <td align='center'><img border=0 src=" + array [row] [col] + "></td>";

            }
            html = html + " </tr>";
          }
           html = html + "</table>";
           var idiv = window.document.getElementById("networksDiv");
           idiv.innerHTML = html;
         }
         printNetworkColors(networkInfo);
          </script>
          <p><font face="Verdana" size="2">More information about California Real Time Network (CRTN) is available at<a href="http://sopac.ucsd.edu/projects/realtime/">SOPAC Web Page</a></font></p>
	</body>
</html>