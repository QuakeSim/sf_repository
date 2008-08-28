<html xmlns="http://www.w3.org/1999/xhtml">  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Google Maps Example</title>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBRxYpIIOz9ynlSKjbx-4JMuN5JjrhR5gSOcKdieYppOZ4_yzZc_Ti15qw"
      type="text/javascript"></script>
  <script src="http://gf7.ucs.indiana.edu:8080/Disloc-portlet/egeoxml.js" type="text/javascript"></script>
  </head>
  <body>
   <table>
   <tr>
   <td valign="top">
    <div id="map" style="width: 500px; height: 400px; "></div>
    <div id="dropboxdiv"></div>
   </td>
   <td>
	 <div id="the_side_bar" style="height:400px;overflow:auto;"></div>
    </td>
    </tr>
	 </table>	 


    <script type="text/javascript">
    //<![CDATA[

    var map=new GMap2(document.getElementById("map"));
    map.setCenter(new GLatLng(35.0,-118.5),5);
    map.addControl(new GLargeMapControl());
    map.addControl(new GMapTypeControl());
   
		var exml = new EGeoXml("exml", map, ["allQTFaults.kml"], {nozoom:true,sidebarid:"the_side_bar",iwwidth:200});
    exml.parse();

    //]]>
    </script>

</html>
