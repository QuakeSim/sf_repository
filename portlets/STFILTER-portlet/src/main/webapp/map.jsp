<jsp:useBean id="HolderBeanID" scope="session" class="cgl.sensorgrid.gui.google.HolderBean"/>
<%
//HolderBeanID.getBean().init();
//String networkName = HolderBeanID.getNetworkName();

//double lat = 33.662 ;
//double lon = -117.9357;
//if(HolderBeanID.getLat()>0)
// lat = HolderBeanID.getLat();
//if(HolderBeanID.getLon()>0)
// lon = HolderBeanID.getLon();
//System.out.println("\n\n\n************************ " + networkName + " " + lat + " " +lon);
//System.out.println(HolderBeanID.getLat() + " " + HolderBeanID.getLon());
%>
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <script src="put.google.map.key.here"
	type="text/javascript"></script><script type="text/javascript">

      //<![CDATA[
      //for gf8:7070/ajax ABQIAAAA68__CB1RSwlTPrJDO7qiqRTy5gaO9GNLTqqmMNUThvEgzxgQPxQtk3Ws9HVeiuwtwLmeYzA9VhjNiQ
      //global variables
      var map;
      var bounds;
      var width;
      var height;
      var isIE = false;
      var req;
      var messageHash = -1;
      var targetId = "1";

      // Initialize the map
      function initMap() {
        if (GBrowserIsCompatible()) {
          map = new GMap( document.getElementById( "map" ));
          map.addControl(new GLargeMapControl()); // zoom etc
          map.addControl(new GMapTypeControl()); //
          //map.centerAndZoom(new GPoint(-117.9357, 33.662), 9);
          map.centerAndZoom(new GPoint(<%=HolderBeanID.getLon()%>, <%=HolderBeanID.getLat()%>), 9);
          //addPoint(-117.997, 33.99, "WHYT");
          // addPolyline();
          bounds = map.getBoundsLatLng();
          width = bounds.maxX - bounds.minX;
          height = bounds.maxY - bounds.minY;
          //addPolyline(bounds, width, height);
        }
        else {
          alert( "GMaps not compatible with this browser, sorry. Please download Mozilla Firefox @ http://www.mozilla.org" );
        }
      }

      function addPoint(lon, lat, name){
        var point = new GPoint(lon, lat);
        var marker = createMarker(point, name, lon, lat);
        map.addOverlay(marker);
      }

      function deleteOverlays(){
        map.clearOverlays();
      }

      // Creates a marker whose info window displays the given number
      function createMarker(point, name, lon, lat) {
        var marker = new GMarker(point);
        // Show this marker's name in the info window when it is clicked
        var html = "<b>Station Name= </b>" + name + "<br><b>Lat=</b>" + lat + "<br><b>Lon= </b>" + lon;
        GEvent.addListener(marker, "click", function() {
          marker.openInfoWindowHtml(html);});
          return marker;
        }

        function initRequest(url) {
          if (window.XMLHttpRequest) {
            req = new XMLHttpRequest();
          } else if (window.ActiveXObject) {
            isIE = true;
            req = new ActiveXObject("Microsoft.XMLHTTP");
          }
        }

        function checkForMessage() {
          window.status = "Checking for update from GPS stations";
          var url = "relay.jsp";
          initRequest(url);
          req.onreadystatechange = processReqChange;
          req.open("GET", url, true);
          req.send(null);
        }

        // callback for the request
        function processReqChange() {
          if (req.readyState == 4) {
            if (req.status == 200) {
              var item = req.responseXML.getElementsByTagName("name")[0];
              var lat = req.responseXML.getElementsByTagName("lat")[0];
              var lon = req.responseXML.getElementsByTagName("lon")[0];

              var names = req.responseXML.getElementsByTagName("name");
              var lats = req.responseXML.getElementsByTagName("lat");
              var lons = req.responseXML.getElementsByTagName("lon");
              var allnames="";
              for (var i=0; i<names.length; i++) {
                var i_name = names[i].firstChild.nodeValue;
                var i_lat = lats[i].firstChild.nodeValue;
                var i_lon = lons[i].firstChild.nodeValue;
                allnames = allnames + " " + i_name + " " + i_lat + " " + i_lon + "<br>";
                addPoint(i_lon, i_lat, i_name);
              }
              showText(allnames);
              window.status = "";
            } else {
              window.status = "No Update for " + messageHash + " status=" + req.status;
            }
            setTimeout("checkForMessage(), deleteOverlays()", 3000);
          }
        }

        // display details retrieved from XML document
        function showText(text) {
          var idiv = window.document.getElementById("msg1");
          idiv.innerHTML = "";
          idiv.innerHTML = "<font face=\"Courier New\" size=\"2\" color=\"#FF0000\">" + text + "</font>";
        }

        function showPos(name, lat, lon) {
          var idiv = window.document.getElementById("message");
          idiv.innerHTML = "";
          idiv.innerHTML = "<font face=\"Courier New\" size=\"2\" color=\"#FF0000\">" + name + "&nbsp" + lat + "&nbsp" + lon + "</font>";
        }
        /*
        function appendToSelect(message) {
          var select = window.document.getElementById("counterSelect");
          var opt = document.createElement("option");
          opt.value = message;
          opt.text = message;
          if (isIE) {
            select.add(opt);
          } else {
            select.appendChild(opt);
          }
        }

        function appendText2TextArea(newtext) {
          var area = window.document.getElementById("textarea");
          area.value = newtext;
        }
        */
        </script><title>Asynchronous Polling using Asynchronous JavaScript and XML (AJAX)</title>
        </head>
<body onload="checkForMessage(),initMap()">
  <p align="left">
    <font face="Verdana" color="#800000">
      <b>
        <br>
          Asynchronous JavaScript and XML
          (AJAX) for Real-Time Streaming Data Display on Google Maps.
          <br>
          </b>
        </font>
        <br>
        </p>
        <table border="1" width="44" id="table2" style="border-style: solid; border-width: 0; ">
          <tr>
            <td valign="top" nowrap>
              <form method="POST" action="demo.jsp">
                <p align="left">
                  <br>
                    <input type="submit" value="Stop Connection" name="stop">
                      <input type="hidden" value="Stop" name="StopMap">
                      </p>
                    </form>
                    <p>        &nbsp;
                      <br>
                        <div id="msg1" style="width: 453px; height: 237px">
              <font color="#FF0000">
                <font face="Verdana" size="2">Wait For the Server Messages</font>
                <br>
                </font>
              </div>
            </td>
          <td width="600">
            <div id="map" style="width: 600px; height: 400px">      </div>
          </td>
        </tr>
      </table>
      <p>&nbsp;</p>
    </body>
  </html>
