<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Google Maps Example</title>
 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBQozjQdf4FEuMBqpopduISAOADS4xTilRYX9d1ZU0uvBJwyY4gerC4Gog"
      type="text/javascript"></script>
  <script src="egeoxml.js" type="text/javascript"></script>
  </head>
  <body>
    <div id="map" style="width: 800px; height: 600px; "></div>

    <script type="text/javascript">
    //<![CDATA[

    var map=new GMap2(document.getElementById("map"));
    map.setCenter(new GLatLng(32,-118),7);
    map.addControl(new GLargeMapControl());
    map.addControl(new GMapTypeControl());


    // == create the manager ==
    var mgr = new GMarkerManager(map);

    var mypoints=[];

    function addMark(marker,name,desc,imagefile,n) {
        mgr.addMarker(marker,0,17);
    }
   
    var exml = new EGeoXml("exml", map, ["network0.kml"], {nozoom:true,addmarker:addMark});
    exml.parse();

    //]]>
    </script>



  </body>
</html>
