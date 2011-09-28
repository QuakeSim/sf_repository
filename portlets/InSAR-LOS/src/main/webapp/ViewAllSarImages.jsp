<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<body>
  <f:view>
	 <f:verbatim><fieldset><legend><b>Interferogram Map Selection</b></legend></f:verbatim>
	 <h:panelGrid id="InSAR-View-All">
		<f:verbatim>
		  Click on the map to select the region you want to use.
		  <div id="InSAR-All-Map" style="width: 800px; height: 600px;"></div>
		</f:verbatim>
		<f:verbatim>
		  <div id="dynatable"></div>
		</f:verbatim>
		<f:verbatim></fieldset></f:verbatim>
	 </h:panelGrid>
  </f:view>
  <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>  
  <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
  <script src="script/sarselect.js"></script>			 
  
  <script>
	 //Rendear the InSAR overlays.
	 var insarMapDiv=document.getElementById("InSAR-All-Map");
	 var tableDivName="dynatable";
	 setMasterMap(insarMapDiv,tableDivName);

	 function setMasterMap(insarMapDiv,tableDivName) {
		  var latlng=new google.maps.LatLng(32.3,-118.0);
		  var myOpts={zoom:6, center: latlng, mapTypeId: google.maps.MapTypeId.ROADMAP};
		  var map=new google.maps.Map(insarMapDiv, myOpts);
		  
		  
		  var kmlMapOpts={map:map, suppressInfoWindows:true, preserveViewport:true};
		  var insarKml = new google.maps.KmlLayer("http://quaketables.quakesim.org/kml?uid=all&ov=0",kmlMapOpts);

		  //Find out where we are.
		  google.maps.event.addListener(insarKml,"click",function(event) {
				var finalUrl=constructWmsUrl(map,event);
				console.log(finalUrl);
				var results=$.ajax({url:finalUrl,async:false}).responseText;
				var parsedResults=jQuery.parseJSON(results);
				createTable(parsedResults,tableDivName);
		  });
	 }
		  
	 function createTable(parsedResults,tableDivName) {
		var dynatable='<table border="1">';
		//Create the header row.
		dynatable+='<tr>';
		for(var index1 in parsedResults[0]) {
		dynatable+='<th>'+index1+'</th>';
		}
		dynatable+='</tr>';
		//Fill in the table.
		for (var index1 in parsedResults) {
		dynatable+='<tr onmouseover="selectedRow(this)" onmouseout="unselectedRow(this)" onclick="selectRowAction(this)">';
		for(var index2 in parsedResults[index1]) {
		dynatable+='<td>'+parsedResults[index1][index2]+'</td>';
		}
		dynatable+='</tr>'
		}
		dynatable+='</table>';
		document.getElementById(tableDivName).innerHTML=dynatable;
	 }
	 
	 function constructWmsUrl(map,event) {
	   var scale=Math.pow(2,map.getZoom());
	   var nw=new google.maps.LatLng(map.getBounds().getNorthEast().lat(),map.getBounds().getSouthWest().lng());
		var worldCoordNW=map.getProjection().fromLatLngToPoint(nw);
		var worldCoord=map.getProjection().fromLatLngToPoint(event.latLng);
		xpix=Math.floor((worldCoord.x-worldCoordNW.x)*scale);
		ypix=Math.floor((worldCoord.y-worldCoordNW.y)*scale);

		var width=map.getDiv().style.width;
		width=width.substring(0,width.indexOf("px"));
		var height=map.getDiv().style.height;
		height=height.substring(0,height.indexOf("px"));

		var slash="/";
	   var urlToCall="/InSAR-LOS-REST/insarnav";
	   urlToCall+=slash+map.getBounds().getSouthWest().lng();
		urlToCall+=slash+map.getBounds().getSouthWest().lat();
		urlToCall+=slash+map.getBounds().getNorthEast().lng();
		urlToCall+=slash+map.getBounds().getNorthEast().lat();
		urlToCall+=slash+width;
		urlToCall+=slash+height;
		urlToCall+=slash+xpix;
		urlToCall+=slash+ypix;
		
		return urlToCall;
    }
	 function selectedRow(row) {
	    row.style.backgroundColor="gray";
		 row.style.cursor="pointer";
	 }
	 function unselectedRow(row) {
	    row.style.backgroundColor="white";
		 row.style.cursor="default";
	 }
	 function selectRowAction(row){
	   //Find the ID of the row
		var id=extractRowId(row);

	   //Call REST service
		var callResults=getImageMetadata(id);
		
		//Extract overlayUrl
		var overlayUrl=extractOverlayUrl(callResults);

		//Redirect to next page
		parent.location="./SARSelectRegion.faces?overlayUrl="+overlayUrl;
	 }
	 
	 function extractOverlayUrl(callResults){
	   var overlayUrl;
		for(var index in callResults) {
		    if(callResults[index].datatype=="int") {
			    overlayUrl=callResults[index].kml;
			 }
		}
	   return overlayUrl;
	 }

	 function extractRowId(row) {
	    //Get the first td from the tr and return its innerHTML.
	    //The ID is the value of the first <td> element.
		 var td=row.firstChild;
	    var id=td.innerHTML;
		 return id;
	 }
	 
	 function getImageMetadata(id) {
	   var urlToCall="/InSAR-LOS-REST/insarid/"+id;
		var tmpResults=$.ajax({url:urlToCall,async:false}).responseText;
		jsonResults=jQuery.parseJSON(tmpResults);
	   return jsonResults;
	 }


  </script>
</body>
