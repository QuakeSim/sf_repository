<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" 
xmlns:v="urn:schemas-microsoft-com:vml">
  <head>
    <script src="http://maps.google.com/maps?file=api&amp;v=2.x&amp;key=put.google.map.key.here"
      type="text/javascript"></script>


  </head>
  


  <body onload="onLoad()">
<f:view>

<!--***************************************************************************************-->  	
    <style type="text/css">
    body {
      font-family: Arial, sans serif;
      font-size: 11px;
    }
    v\:* {
      behavior:url(#default#VML);
    }
    </style>
    <script type="text/javascript"> 
var map;
var userAdded = 1;

var observkml='<h:outputText value="#{SimplexBean.projectSimpleXOutput.kmlUrls[1]}"/>';
var calckml='<h:outputText value="#{SimplexBean.projectSimpleXOutput.kmlUrls[2]}"/>';
var o_ckml='<h:outputText value="#{SimplexBean.projectSimpleXOutput.kmlUrls[3]}"/>';
var lon='<h:outputText value="#{SimplexBean.currentProjectEntry.origin_lon}"/>';
var lat='<h:outputText value="#{SimplexBean.currentProjectEntry.origin_lat}"/>';

var layers = {
 "ObservedDisplacements":
 {"url": observkml,
  "name": "Observed Displacements",  
  "zoom": 8,
  "lat": lat,
  "lng": lon},
 "CalculatedDisplacements": 
 {"url": calckml,
  "name": "Calculated Displacements",
  "zoom": 8,
  "lat": lat,
  "lng": lon},
 "ResidualDisplacements": 
 {"url": o_ckml,
  "name": "Residual Displacements",
  "zoom": 8,
  "lat": lat,
  "lng": lon}
};


function onLoad() {
  map = new GMap2(document.getElementById("map")); 

  map.setCenter(new GLatLng(37.422341, -122.085018), 5);
  map.addControl(new GSmallMapControl());

  document.getElementById("url").value = "http://";
  
  for(var layer in layers) {
    addTR(layer, layers[layer].name);
  }
  document.getElementById(layer).checked = true;
  toggleGeoXML(layer, true);
} 



function addGeoXML() {
  var theUrl = document.getElementById("url").value;
  theUrl = theUrl.replace(/^\s+/, "");
  theUrl = theUrl.replace(/\s+$/, "");
  if (theUrl.indexOf(' ') != -1) {
    alert('Error - that address has a space in it');
  } else {
    var id = "userAdded" + userAdded;
    layers[id] = {};
    layers[id].url = theUrl;
    layers[id].name = "User Layer " + userAdded;

    addTR(id);
    document.getElementById(id).checked = true;
    toggleGeoXML(id, true);
    userAdded++;
  }
}

function addTR(id) {
  var layerTR = document.createElement("tr");

  var inputTD = document.createElement("td");
  var input = document.createElement("input");
  input.type = "checkbox";
  input.id = id;
  input.onclick = function () { toggleGeoXML(this.id, this.checked) };
  inputTD.appendChild(input);

  var nameTD = document.createElement("td");
  var nameA = document.createElement("a");
  nameA.href = layers[id].url;
  var name = document.createTextNode(layers[id].name);
  nameA.appendChild(name);
  nameTD.appendChild(nameA);

  layerTR.appendChild(inputTD);
  layerTR.appendChild(nameTD);
  document.getElementById("sidebarTBODY").appendChild(layerTR);
}

function toggleGeoXML(id, checked) {
  if (checked) {
    var geoXml = new GGeoXml(layers[id].url);
    layers[id].geoXml = geoXml;

    if (layers[id].zoom) {
      map.setZoom(layers[id].zoom);
    } else {
      map.setZoom(1);
    }
    if (layers[id].lat && layers[id].lng) {
      map.setCenter(new GLatLng(layers[id].lat, layers[id].lng));
    } else {
      map.setCenter(new GLatLng(39.909736,-35.859375));
    }
    map.addOverlay(geoXml);
  } else if (layers[id].geoXml) {
    map.removeOverlay(layers[id].geoXml);
  }
}

  </script>  
    	
<!--***************************************************************************************-->  	
  	
    <input id="url" value="" size="60"/>
    <input type="button" value="Add" onClick="addGeoXML();"/>
    <br/>
    <br/>
    <div id="map" style="width:80%; height: 500px; float:left; border: 1px solid black;"></div>
    <div id="sidebar" style="float:left; overflow-vertical:scroll; height: 500px; width:15%; border:1px solid black">
    <table id="sidebarTABLE">

    <tbody id="sidebarTBODY">
    </tbody>
    </table>
    </div>
    <div id="send_kml_googlemap" style="width:100%;height:20px ;float:left;border: 1px solidblack;">
						<h:outputLink id="link6" value="http://maps.google.com/maps?q=#{SimplexBean.projectSimpleXOutput.kmlUrls[0]}" target="_blank">
							<h:outputText value="[<font size=1px>View In Google map</font>]" escape="false" />
						</h:outputLink>
  </div>
  
 <div style="width:100%;height:20px ;float:left;border: 1px solidblack;">
	<h:form>
		<hr />
		<h:commandLink action="Simplex2-back">
			<h:outputText value="#{SimplexBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>
</div>
</f:view>  
  </body>
</html>



