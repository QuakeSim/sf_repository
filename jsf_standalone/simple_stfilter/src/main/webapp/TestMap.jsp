<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<%@page import="java.util.*, cgl.sensorgrid.sopac.gps.GetStationsRSS, java.io.*, java.lang.*"%>

<%
	String str_tabcontent = new String();
	java.io.BufferedInputStream bis=null;
	try{
		bis =new java.io.BufferedInputStream(new java.io.FileInputStream(config.getServletContext().getRealPath("form/tabcontent.htm")));
		BufferedReader br = new BufferedReader(new InputStreamReader(bis));
		String line = null;
		while ((line = br.readLine()) != null) {
			str_tabcontent=str_tabcontent+line;
		}
	}
	catch(Exception e){
		e.printStackTrace();
	}
	finally {
		if (bis != null)bis.close();
	}

%>

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
          <div id="map" style="width: 750px; height: 600px">      </div>
        </td>
        <td valign="middle" width="50">
          <div id="networksDiv"> Network Names and Colors     </div>
        </td>
      </tr>
    </table>

   <f:view>

   <%
   String mapCenterX="37.0";
   String mapCenterY="-117.0";
   
   %>
	
   <h:outputText value="#{stfilterBean.resOption}"/>


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
        map.centerAndZoom(new GPoint(${stfilterBean.mapCenterY}, ${stfilterBean.mapCenterZ}), 10);

        var colors = new Array (6);
        colors[0]="red";
        colors[1]="green";
        colors[2]="blue";
        colors[3]="black";
        colors[4]="white";
        colors[5]="yellow";
        colors[6]="purple";
        colors[7]="brown";
    </script>

          <p><font face="Verdana" size="2">More information about California Real Time Network (CRTN) is available at<a href="http://sopac.ucsd.edu/projects/realtime/">SOPAC Web Page</a></font></p>

    </f:view>
	</body>
</html>
