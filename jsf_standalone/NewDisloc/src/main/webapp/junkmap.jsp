<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Fault Maps</title>
  <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBTwM0brOpm-All5BF6PoaKBxRWWERS5kaQBLplD6GDaf1-YuioaBH35uw"
      type="text/javascript"></script>
  </head>
  <body onunload="GUnload()">

    <div id="map" style="width: 500px; height: 500px"></div>

    <noscript><b>JavaScript debe estar activado para que usted pueda usar Google Maps.</b> 
      Sin embargo, parece que JavaScript está desactivado o no es soportado por su navegador. 
      Para ver Google Maps, active JavaScript cambiando su navegador e intente de nuevo.
    </noscript>
 
    <script type="text/javascript">
    //<![CDATA[
        var newWindow=window.open("","","width=800,height=500,scrollbars");

    if (GBrowserIsCompatible()) {

      var polys = [];
      var labels = [];

      //This method checks to see if the selected point falls within the 
      //bounding box defined by the fault segment.  This is not mathematically correct 
      //correct but close enough--otherwise we have to get into pixel 
      //resolutions.
      GPolyline.prototype.Contains = function(point) {
        var j=0;
        var pointInside = false;
        var x = point.lng();
        var y = point.lat();
//        newWindow.document.write(point.lng()+" "+point.lat());
          //See if the point is on the line.         
         var latMin, latMax, lngMin, lngMax;
         if(this.getVertex(0).lat() < this.getVertex(1).lat()) {
              latMin=this.getVertex(0).lat();
              latMax=this.getVertex(1).lat();
         }
         else {
              latMin=this.getVertex(1).lat();
              latMax=this.getVertex(0).lat();              
         }
         if(this.getVertex(0).lng() < this.getVertex(1).lng()) {
              lngMin=this.getVertex(0).lng();
              lngMax=this.getVertex(1).lng();
         }
         else {
              lngMin=this.getVertex(1).lng();
              lngMax=this.getVertex(0).lng();              
         }

          newWindow.document.write(latMin+" "+point.lat()+" "+latMax+" "+lngMin+" "+point.lng()+" "+lngMax);

         if((lngMin < point.lng() <= lngMax) && (latMin < point.lat() <= latMax)) {
             pointInside=true;
         }

        newWindow.document.write(" "+pointInside+"<br>");
        return pointInside;
      }

      // Display the map, with some controls and set the initial location 
      var map = new GMap2(document.getElementById("map"));
      map.addControl(new GLargeMapControl());
      map.addControl(new GMapTypeControl());
      map.setCenter(new GLatLng(34.3,-118.3),10);
		//	   map.setMapType(G_HYBRID_TYPE);

      GEvent.addListener(map, "click", function(overlay,point) {
        if (point) {
          for (var i=0; i<polys.length; i++) {
            if (polys[i].Contains(point)) {
					alert("Clicked!");
				//              map.openInfoWindowHtml(point,"Recorrer "+labels[i]);
              //i = 999; // Jump out of loop
            }
          }
        }
      });
      
      // Read the data from recorridoscallejeros.xml

      var request = GXmlHttp.create();
      request.open("GET", "faults-small.xml", true);
      request.onreadystatechange = function() {
        if (request.readyState == 4) {
          var xmlDoc = request.responseXML;
          
          // ========= Now process the polylines ===========
          var faults = xmlDoc.documentElement.getElementsByTagName("fault");

          // read each line
          for (var a = 0; a < faults.length; a++) {
            // get any fault attributes
            var faultName  = faults[a].getAttribute("faultName");
            var segmentName = faults[a].getAttribute("segmentName");
            // read each point on that line
            var points = [];
               points[0] = new GLatLng(parseFloat(faults[a].getAttribute("latStart")),
                                   parseFloat(faults[a].getAttribute("lonStart")));
               points[1] = new GLatLng(parseFloat(faults[a].getAttribute("latEnd")),
                                   parseFloat(faults[a].getAttribute("lonEnd")));

            var poly = new GPolyline(points,"#000000",2,1,"#000000",1);
            polys.push(poly);
//            labels.push(label);
            map.addOverlay(poly);
          }
          // ================================================           
        }
      }
      request.send(null);

    }
    
    // display a warning if the browser was not compatible
    else {
      alert("Sorry, the Google Maps API is not compatible with this browser");
    }

    //]]>
    </script>
  </body>
</html>




