<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<%@page import="java.util.*, cgl.sensorgrid.sopac.gps.GetStationsRSS, java.io.*, java.lang.*"%>


<html>
  <head>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBTCdqtmO6Kma7uYZgpagQkNe17MQhRS93QdZFchZ2Vy9IpcH0W3nbN34g"
      type="text/javascript"></script>
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
          <div id="map" style="width: 750px; height: 600px"> </div>
        </td>
      </tr>
    </table>
   <f:view>
   <%-- Just use this to initialize the stfilterBean object. --%>
   <h:outputText value="#{stfilterBean.resOption}" rendered="false"/>


    <script type="text/javascript">
        var req;
        var baseIcon = new GIcon();
        baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
        baseIcon.iconSize = new GSize(15, 20);
        baseIcon.shadowSize = new GSize(10, 10);
        baseIcon.iconAnchor = new GPoint(1, 10);
        baseIcon.infoWindowAnchor = new GPoint(5, 1);
        baseIcon.infoShadowAnchor = new GPoint(5, 5);
        var icon=new GIcon(baseIcon);
        icon.image="http://labs.google.com/ridefinder/images/mm_20_green.png";

        // Create the map
        var map = new GMap2(document.getElementById("map"));

        map.addControl(new GLargeMapControl());
        map.addControl(new GMapTypeControl());
        map.addControl(new GScaleControl());
        map.setCenter(new GLatLng(${stfilterBean.siteCodeLat},${stfilterBean.siteCodeLon}),7);

	var infoTabs=[new GInfoWindowTab("Current X", '${stfilterBean.tabContent[0]}'),
			new GInfoWindowTab("Current Y", '${stfilterBean.tabContent[1]}'),
			new GInfoWindowTab("Current Z", '${stfilterBean.tabContent[2]}')];  



	var marker=new GMarker(new GPoint(${stfilterBean.siteCodeLon}, ${stfilterBean.siteCodeLat}),icon);
    
       map.openInfoWindowTabsHtml(map.getCenter(),infoTabs);
       map.addOverlay(marker);
        
	


    </script>

     </f:view>

	</body>
</html>
