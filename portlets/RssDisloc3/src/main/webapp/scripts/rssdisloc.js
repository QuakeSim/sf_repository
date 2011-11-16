var rssdisloc=rssdisloc || (function() {
	 
	 //--------------------------------------------------
	 // The following are function-wide variables.
	 //--------------------------------------------------			
	 var infoWindowArray=new Array();
	 var kmlDispLayerArray=new Array();
	 var kmlInsarLayerArray=new Array();
	 var faultMap;
	 var navbar;
	 
	 function setMap(faultMapDiv) {	 
		  //Create the map
		  var latlng=new google.maps.LatLng(33.3,-118.0);
		  var mapOpts={zoom:7, center: latlng, mapTypeId: google.maps.MapTypeId.ROADMAP};
		  faultMap=new google.maps.Map(faultMapDiv,mapOpts);
	 }
	 
	 function setNavbar(navbarDiv) {
		  navbar=navbarDiv;
		  //This is the XML file containing the results.
		  var myM5List = $.ajax({type:"GET",url:"/overm5.kml",async:false,dataType:"xml"}).responseText;
		  //Parse the list
		  eventList=$.parseXML(myM5List);
        var eventListHtml=navbar.innerHTML+"<ul id='browser'>";
		  //index1 is the scenario number.  index2 is the event index
		  var index1=1; index2=0;
        $(eventList).find('Placemark').each(function(){
			   //We have to work around the poor formatting choices of the KML feed.
				//The faults will be grouped in 4's, each with the same shortname.
				if(index1==1) {
					 eventListHtml+="<li>"+$(this).find('shortName').text();
				}
				if(index1<5) {
					 //Next several lines should go in a helper function.
					 var description=$(this).find('description').text();
					 var content="<div style='font-family: Arial, sans-serif;font-size: small;width:300px'>"+description+"</div>";
					 //Note the coordinates are in lng,lat order.
					 var eventCoord=$(this).find('coordinates').text();
					 eventCoord=eventCoord.split(" ")[0];  //We only keep the first lat,lon coordinates
					 var eventLatLon=new google.maps.LatLng(eventCoord.split(",")[1],eventCoord.split(",")[0]);
					 var infoWindow=new google.maps.InfoWindow({content:content});
					 infoWindow.setPosition(eventLatLon);
					 infoWindowArray.push(infoWindow);
					 eventListHtml+="<ul><li><a id='eventInfoWindow_"+index2+"' onClick='rssdisloc.popupInfoWindow(this)'>Scenario "+index1+"</a>";
				 eventListHtml+="<ul>"
             eventListHtml+="<li><a id='dislocOutput_"+index2+"' target='blank' href='"+$(this).find('DislocOutputURL').text()+"'>Surface Displacement Outputs (text)</a></li>";
				 
				 var kmlDispUrl=$(this).find('DisplacementKmlURL').text();
             kmlDispUrl=kmlDispUrl.replace(/^\s+|\s+$/g, '');
				 kmlDispOpts={map:null};
				 var kmlDispLayer=new google.maps.KmlLayer(kmlDispUrl,kmlDispOpts);
             kmlDispLayerArray.push(kmlDispLayer);
             eventListHtml+="<li>"
				              +"<input id='kmlDispLayer_"+index2+"' type='checkbox' onClick='rssdisloc.toggleDispKml(this)'>"
				              +"<a id='"+index2+"' target='blank' href='"+kmlDispUrl+"'>Surface Displacement Plot</a></li>";

				 var kmlInsarUrl=$(this).find('InsarKmlURL').text();             
				 kmlInsarUrl=kmlInsarUrl.replace(/^\s+|\s+$/g, '');
				 kmlInsarOpts={map:null};
				 var kmlInsarLayer=new google.maps.KmlLayer(kmlInsarUrl,kmlInsarOpts);
				 kmlInsarLayerArray.push(kmlInsarLayer);
             eventListHtml+="<li>"
				              +"<input id='kmlInsarLayer_"+index2+"' type='checkbox' onClick='rssdisloc.toggleInsarKml(this)'>"
				              +"<a id='"+index2+"' target='blank' href='"+kmlInsarUrl+"'>InSAR Plot</a></li>";
             eventListHtml+="</ul></li></ul>";  //Close the inner lists
				 index1++;
				 if(index1==5) index1=1;
				 }
				 index2++;
         });         
           //Close up the navbar's outermost ul.
           eventListHtml+="</ul>";

           navbar.innerHTML=eventListHtml;

			  //Finally, go to the most recent event
			  popupInfoWindow2();
	 }
	 //--------------------------------------------------			
	 
	 //--------------------------------------------------
	 // This function depends on a 'global' array of info windows.  The caller
	 // is the HTMLElement of the corresponding element that generated the call.
	 // Typically this method is called by an onClick event of an anchor tag, and
	 // we expect the id attribute of the tag to correspond to the infowWindow array 
	 // entry that we want.
    //
    // Note also that all of these assume the id attribute comes first (so use [0]).
    // This is fragile. Also the ID extraction depends on convention.
	 //--------------------------------------------------
	 function popupInfoWindow(caller) {
		  var callerId=caller.attributes[0];
		  var index=callerId.value.split("_")[1];
		  var infoWindow=infoWindowArray[index];
		  infoWindow.open(faultMap);
	 }
	 
	 function toggleDispKml(caller) {
		  var callerId=caller.attributes[0];
		  var index=callerId.value.split("_")[1];
		  var kmlDispLayer=kmlDispLayerArray[index];
		  var checkbox=document.getElementById(callerId.value);
		  if(checkbox.checked==true) {
			   kmlDispLayer.setMap(faultMap);
		  }
		  else {
			   kmlDispLayer.setMap(null);
        }
	 }
	 
	 function toggleInsarKml(caller) {
		  var callerId=caller.attributes[0];
		  var index=callerId.value.split("_")[1];
		  var kmlInsarLayer=kmlInsarLayerArray[index];
		  var checkbox=document.getElementById(callerId.value);
		  if(checkbox.checked==true) {
			   kmlInsarLayer.setMap(faultMap);
		  }
		  else {
    			kmlInsarLayer.setMap(null);
        }
	 }
	 
	 //This function opens the most recent event's info window.
	 function popupInfoWindow2() {
        var infoWindow=infoWindowArray[0];
		  infoWindow.open(faultMap);
	 }
	 
	 /**
	  * Public API for rssdisloc.js
	  */
	 return {
		  setMap:setMap,
		  setNavbar:setNavbar,
		  popupInfoWindow:popupInfoWindow,
		  toggleDispKml:toggleDispKml,
		  toggleInsarKml:toggleInsarKml,
		  popupInfoWindow2:popupInfoWindow2
	 }
})();