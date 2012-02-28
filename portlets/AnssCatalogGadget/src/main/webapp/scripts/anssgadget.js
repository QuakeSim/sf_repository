var anssgadget=anssgadget || (function() {
	 //These are "global" variables
	 var map;
	 var markers=new Array();
	 var markerNE, markerSW;
	 var polyPoints;
	 var polyShape;
    var polyLineColor = "#3355ff";
    var polyFillColor = "#335599";
	 var urlBase="/AnssCatalogService/catalog";
	 var urlBase2="/AnssCatalogService/csvcatalog";
	 var amp="&";
	 //These are parameters needed by the anss service.
	 var OUTPUT_TYPE="output=kml";
	 var OUTPUT_TYPE2="output=csv";
	 var OUTPUT_FORMAT="format=cnss";
	 var MINTIME="mintime=";
	 var MAXTIME="maxtime=";
	 var MINMAG="minmag=";
	 var MAXMAG="maxmag=";
	 var ETYPE="etype=E";
	 var OUTPUT_LOC="outputloc=ftp";
	 var MINLON="minlon=";
	 var MAXLON="maxlon=";
	 var MINLAT="minlat=";
	 var MAXLAT="maxlat=";
	 var DEFAULT_MIN_WALLTIME="00:00:00";
	 var DEFAULT_MAX_WALLTIME="00:00:00";

	 //Load the earth
	 google.load("earth","1");
//	 function createMap(mapDiv) {
//		  var latlng=new google.maps.LatLng(33.3,-118.0);
//		  var myOpts={zoom:7, center: latlng, mapTypeId: google.maps.MapTypeId.SATELLITE};
//		  map=new google.maps.Map(mapDiv, myOpts);
//	 }

	 function createMap(mapDiv) {
		  google.setOnLoadCallback(init);
	 }

	 function init() {
		  google.earth.createInstance(mapDiv,initGE, failureGE);
	 }
	 
	 function initGE(instance) {
		  map=instance;
		  map.getWindow().setVisibility(true);
		  map.getNavigationControl().setVisibility(map.VISIBILITY_AUTO);
	 }
    
	 function failureGE(errorcode){
		  console.log(errorcode.value);
	 }

	 function submitMapRequest(minmag,maxmag,mindate,maxdate,minlat,minlon,maxlat,maxlon) {
		  //Note this assumes the AnssCatalogService is co-located.
		  console.log("Submitting request");
		  var mintime=MINTIME+mindate.value+","+DEFAULT_MIN_WALLTIME;//"00:00:00";
		  var maxtime=MAXTIME+maxdate.value+","+DEFAULT_MAX_WALLTIME;//"00:00:00";
		  var minmag=MINMAG+minmag.value;
		  var maxmag=MAXMAG+maxmag.value;
		  if(minlat && minlon && maxlat && maxlon) {
				var minlon=MINLON+minlon.value;
				var maxlon=MAXLON+maxlon.value;
				var minlat=MINLAT+minlat.value;
				var maxlat=MAXLAT+maxlat.value;
				var finalUrl=urlBase+"?"+OUTPUT_TYPE+amp+OUTPUT_FORMAT+amp+mintime+amp+maxtime+amp+minmag+amp+maxmag+amp+ETYPE+amp+OUTPUT_LOC+amp+minlon+amp+maxlon+amp+minlat+amp+maxlat;
				var finalUrl2=urlBase2+"?"+OUTPUT_TYPE2+amp+OUTPUT_FORMAT+amp+mintime+amp+maxtime+amp+minmag+amp+maxmag+amp+ETYPE+amp+OUTPUT_LOC+amp+minlon+amp+maxlon+amp+minlat+amp+maxlat;
				console.log(finalUrl);
		  }
		  else {
				var finalUrl=urlBase+"?"+OUTPUT_TYPE+amp+OUTPUT_FORMAT+amp+mintime+amp+maxtime+amp+minmag+amp+maxmag+amp+ETYPE+amp+OUTPUT_LOC;
				var finalUrl2=urlBase2+"?"+OUTPUT_TYPE2+amp+OUTPUT_FORMAT+amp+mintime+amp+maxtime+amp+minmag+amp+maxmag+amp+ETYPE+amp+OUTPUT_LOC;
				console.log(finalUrl);
		  }

		  var request=$.ajax({
				url:finalUrl,
				beforeSend: function() {$('#acgResultKml').html("Request submitted. Please wait.")}
		  });
		  request.done(function(results){
				console.log("Request succeeded:"+results);
 				google.earth.fetchKml(map,results,function(kmlObject){
					 if(kmlObject) {
						  map.getFeatures().appendChild(kmlObject);
						  $("#acgResultKml").html("KML loaded. Download availabe from <a href='"+results+"' target='NULL'>"+results+"</a>");
					 }
					 if(kmlObject.getAbstractView()) {
						  map.getView().setAbstractView(kmlObject.getAbstractView());
					 }
				});
				
		  });
		  
		  request.fail(function(errorMsg) { 
				$('#acgResultKml').html("Request failed: "+errorMsg);
				console.log("Request failed:"+errorMsg)
		  });

		  var request2=$.ajax({
				url:finalUrl2,
		  });
		  request2.done(function(results2){
				console.log("FTP URL:"+results2);
				$('#acgResultCSV').html('CSV Data Available: <a target="NULL" href="'+results2+'">'+results2+'</a>');
		  });
		  
		  request2.fail(function(errorMsg2) { 
				$('#acgResultCSV').html("Request failed: "+errorMsg2);
				console.log("Request failed:"+errorMsg2)
		  });

	 }


	 /**
	  * This is the public API
	  */
	 return {
		  createMap:createMap,
		  submitMapRequest:submitMapRequest,
	 }
})();