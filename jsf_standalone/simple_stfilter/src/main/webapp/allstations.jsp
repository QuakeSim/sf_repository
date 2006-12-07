<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<%@page import="java.util.*, cgl.sensorgrid.sopac.gps.GetStationsRSS,
cgl.sensorgrid.gui.google.MapBean, java.io.*"%>
<jsp:useBean id="RSSBeanID" scope="session" class="cgl.sensorgrid.sopac.gps.GetStationsRSS"/>
<jsp:useBean id="MapperID" scope="session" class="cgl.sensorgrid.gui.google.Mapper"/>

<%
Vector networkNames = RSSBeanID.networkNames();

//Vector stationsVec = RSSBeanID.getAllStationsVec();
String mapcenter_x = "33.6";
String mapcenter_y = "-117.06";

//String [] center_xy = RSSBeanID.getMapCenter();
//mapcenter_x = center_xy[0];
//mapcenter_y = center_xy[1];
%>
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
 
 
    <!-- fail nicely if the browser has no Javascript -->
    <noscript><b>JavaScript must be enabled in order for you to use Google Maps.</b>
      However, it seems JavaScript is either disabled or not supported by your browser.
      To view Google Maps, enable JavaScript by changing your browser options, and then
      try again.
    </noscript>

    <script type="text/javascript">
      //<![CDATA[
      // Check to see if this browser can run the Google API

      //    if (GBrowserIsCompatible()) {
        // Create a base icon for all of our markers
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
        map.centerAndZoom(new GPoint(<%=mapcenter_y%>, <%=mapcenter_x%>), 10);

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

            <%
            for (int i = 0; i < stationsVec.size(); i++) {
              String name = (String)stationsVec.get(i);
              String lat = RSSBeanID.getStationInfo(name)[0];
              String lon = RSSBeanID.getStationInfo(name)[1];
              %>
              stations [<%=i%>] [0] = "<%=name%>";
              stations [<%=i%>] [1] = "<%=lat%>";
              stations [<%=i%>] [2] = "<%=lon%>";

              Markers[<%=i%>] = createMarker("<%=networkName%>", "<%=name%>", "<%=lon%>", "<%=lat%>", icon);
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

          GEvent.addListener(marker, "click", function() {
		var newElement=document.getElementById("form1:station_name");
		newElement.setAttribute("value",name);
	  });

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
              html = html + "  " + array [row] [col] + " ";
              if(col==1)
              html = html + "  <img border=0 src=" + array [row] [col] + "/>";

            }
            html = html + " </tr>";
          }
           html = html + "</table>";
           var idiv = window.document.getElementById("networksDiv");
           idiv.innerHTML = html;
         }
         printNetworkColors(networkInfo);
      </script>

      </td>
      <td valign="top">
      <b><font size="4">Query GPS archive and run ST Filter </font></b>
      <f:view>
       <h:form id="form1">
       <h:panelGrid columns="2">
       <h:outputText value="Site Code"/>
       <h:inputText id="station_name" value="#{stfilterBean.siteCode}"/>
   
       <h:outputText value="Begin Date"/>
       <h:inputText id="begin_date" value="#{stfilterBean.beginDate}"/>


       <h:outputText value="End Date"/>
       <h:inputText id="end_date" value="#{stfilterBean.endDate}"/>

       </h:panelGrid>

       <h:commandLink action="#{stfilterBean.runBlockingAnalyzeTseri}">
            <h:outputText value="Query Selected Station"/>
       </h:commandLink>
       </h:form>
      </f:view>

     </td>
     </table>


     </body>
</html>
