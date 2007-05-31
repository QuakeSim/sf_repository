<%@page import="java.util.*, cgl.sensorgrid.sopac.gps.GetStationsRSS, java.io.*, java.lang.*"%>
<jsp:useBean id="RSSBeanID" scope="session" class="cgl.sensorgrid.sopac.gps.GetStationsRSS"/>
<%
	Vector networkNames = RSSBeanID.networkNames();

	//Vector stationsVec = RSSBeanID.getAllStationsVec();
	String mapcenter_x = "33.036";
	String mapcenter_y = "-117.24";

	String [] center_xy = RSSBeanID.getMapCenter();
	mapcenter_x = center_xy[0];
	mapcenter_y = center_xy[1];
%>
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
 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBQozjQdf4FEuMBqpopduISAOADS4xTilRYX9d1ZU0uvBJwyY4gerC4Gog"
      type="text/javascript"></script>
  </head>
  <body>

<!--
This is the end of the function inlines.
-->
<script type="text/javascript">
//N_map_API 0.1
//Load Javascript function.
function K_LoadJS(src,handle)
{
  var jsFile= document.createElement("script");
  jsFile.src=src;
  jsFile.onreadystatechange=function(){if(this.readyState=="complete" || this.readyState=="loaded"){if(handle)handle.call()}};
  document.body.appendChild(jsFile);
}
//read URL function
function K_GetQueryString(key)
{
  var returnValue =""; 
  var sURL = window.document.URL;
  if (sURL.indexOf("?") > 0)
  {
    var arrayParams = sURL.split("?");
    var arrayURLParams = arrayParams[1].split("&");
    for (var i = 0; i < arrayURLParams.length; i++)
    {
      var sParam =  arrayURLParams[i].split("=");

      if ((sParam[0] ==key) && (sParam[1] != ""))
        returnValue=sParam[1];
    }
  }
  return returnValue;
}
//read cookie function.
function K_GetCookieVal(offset)
{
  var endstr = document.cookie.indexOf (";", offset);
  if (endstr == -1)
    endstr = document.cookie.length;
  return unescape(document.cookie.substring(offset, endstr));
}
function K_SetCookie(name, value)
{
  var expdate = new Date();
  var argv = K_SetCookie.arguments;
  var argc = K_SetCookie.arguments.length;
  var expires = (argc > 2) ? argv[2] : null;
  var path = (argc > 3) ? argv[3] : null;
  var domain = (argc > 4) ? argv[4] : null;
  var secure = (argc > 5) ? argv[5] : false;
  if(expires!=null) expdate.setTime(expdate.getTime() + ( expires * 1000 ));
  document.cookie = name + "=" + escape (value) +((expires == null) ? "" : ("; expires="+ expdate.toGMTString()))+((path == null) ? "" : ("; path=" + path)) +((domain == null) ? "" : ("; domain=" + domain))+((secure == true) ? "; secure" : "");
}
function K_DelCookie(name)
{
  var exp = new Date();
  exp.setTime(exp.getTime() - 1);
  var cval =K_GetCookie(name);
  document.cookie = name + "=" + cval + "; expires="+ exp.toGMTString();
}
function K_GetCookie(name)
{
  var arg = name + "=";
  var alen = arg.length;
  var clen = document.cookie.length;
  var i = 0;
  while (i < clen)
  {
    var j = i + alen;
    if (document.cookie.substring(i, j) == arg)
      return K_GetCookieVal(j);
    i = document.cookie.indexOf(" ", i) + 1;
    if (i == 0) break;
  }
  return null;
}
//callback function.
function K_GetCallBack(obj,func)
{
  return function(){return func.apply(obj,arguments)};
}

// Create the marker and corresponding information window 
function createInfoMarker(point, address, icon) {
  var marker = new GMarker(point,icon);
  GEvent.addListener(marker, "click", function()
  {
    marker.openInfoWindowHtml(address);
  } );
  return marker;
}

// Create the marker and corresponding information window 
function createTabsInfoMarker(point, infoTabs ,icon) {
  var marker = new GMarker(point,icon);
  GEvent.addListener(marker, "click", function() {
    marker.openInfoWindowTabsHtml(infoTabs);
  });
  return marker;
}

function refimg(elementId,url){
	objimg = document.getElementById(elementId);
	objimg.setAttribute('src',url,0);
}

function load() {
  if (GBrowserIsCompatible()) {
    var map = new GMap2(document.getElementById("map"));
    map.addControl(new GSmallMapControl());
    map.addControl(new GMapTypeControl());
    map.setCenter(new GLatLng(37.4419, -122.1419), 13);
    // Our info window content
    var infoTabs = [
      new GInfoWindowTab("Tab #1", "This is tab #1 content"),
        new GInfoWindowTab("Tab #2", "This is tab #2 content")
    ];

    // Place a marker in the center of the map and open the info window
    // automatically
    var marker = new GMarker(map.getCenter());
    GEvent.addListener(marker, "click", function() {
      marker.openInfoWindowTabsHtml(infoTabs);
    });
    map.addOverlay(marker);
    marker.openInfoWindowTabsHtml(infoTabs);
  }
}  
</script>
<!--
This is the end of the function inlines.
-->

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
            
              String x_tabcontent=new String(str_tabcontent);
              String y_tabcontent=new String(str_tabcontent);
              String z_tabcontent=new String(str_tabcontent);
              x_tabcontent=x_tabcontent.replace("{!name!}",name);
              x_tabcontent=x_tabcontent.replace("{!networkName!}",networkName);
              x_tabcontent=x_tabcontent.replace("{!lon!}",lon);
              x_tabcontent=x_tabcontent.replace("{!lat!}",lat);
              x_tabcontent=x_tabcontent.replace("{!output_png!}","http://darya.ucs.indiana.edu:23080/sensorgrid/rdahmm/eval/" + name + "/" + name + ".xyz.xyz.X.png");
//              x_tabcontent=x_tabcontent.replace("{!output_png!}","http://darya.ucs.indiana.edu:23080/sensorgrid/rdahmm/eval/" + networkName.toUpperCase() + "/" + name + "/" + name + ".xyz.xyz.X.png");
              y_tabcontent=y_tabcontent.replace("{!name!}",name);
              y_tabcontent=y_tabcontent.replace("{!networkName!}",networkName);
              y_tabcontent=y_tabcontent.replace("{!lon!}",lon);
              y_tabcontent=y_tabcontent.replace("{!lat!}",lat);
              y_tabcontent=y_tabcontent.replace("{!output_png!}","http://darya.ucs.indiana.edu:23080/sensorgrid/rdahmm/eval/" + name + "/" + name + ".xyz.xyz.Y.png");
              z_tabcontent=z_tabcontent.replace("{!name!}",name);
              z_tabcontent=z_tabcontent.replace("{!networkName!}",networkName);
              z_tabcontent=z_tabcontent.replace("{!lon!}",lon);
              z_tabcontent=z_tabcontent.replace("{!lat!}",lat);
              z_tabcontent=z_tabcontent.replace("{!output_png!}","http://darya.ucs.indiana.edu:23080/sensorgrid/rdahmm/eval/" + name + "/" + name + ".xyz.xyz.Z.png");
              
              %>
              stations [<%=i%>] [0] = "<%=name%>";
              stations [<%=i%>] [1] = "<%=lat%>";
              stations [<%=i%>] [2] = "<%=lon%>";

          		var html = "<b>Station Name= </b>" + "<%=name%>" + "<br><b>Lat=</b>" + "<%=lat%>" + "<br><b>Lon= </b>" + "<%=lon%>" + "<br><b>Network= </b>" + "<%=networkName%>";
							var infoTabs = [
							new GInfoWindowTab("Current X", '<%=x_tabcontent%>'),
							new GInfoWindowTab("Current Y", '<%=y_tabcontent%>'),
							new GInfoWindowTab("Current Z", '<%=z_tabcontent%>')
							];  

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
          <p><font face="Verdana" size="2">More information about California Real Time Network (CRTN) is available at	<a href="http://sopac.ucsd.edu/projects/realtime/">SOPAC Web Page</a></font></p>
	</body>
</html>
