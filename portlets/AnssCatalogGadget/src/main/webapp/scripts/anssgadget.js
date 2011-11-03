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
	 var amp="&";
	 //These are parameters needed by the anss service.
	 var OUTPUT_TYPE="output=kml";
	 var OUTPUT_FORMAT="format=cnss";
	 var MINTIME="mintime=";
	 var MAXTIME="maxtime=";
	 var MINMAG="minmag=";
	 var MAXMAG="maxmag=";
	 var ETYPE="etype=E";
	 var OUTPUT_LOC="outputloc=web";
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

	 function submitMapRequest(minmag,maxmag,mindate,maxdate) {
		  //Note this assumes the AnssCatalogService is co-located.
		  console.log("Submitting request");
		  var mintime=MINTIME+mindate.value+","+DEFAULT_MIN_WALLTIME;//"00:00:00";
		  var maxtime=MAXTIME+maxdate.value+","+DEFAULT_MAX_WALLTIME;//"00:00:00";
		  var minmag=MINMAG+minmag.value;
		  var maxmag=MAXMAG+maxmag.value;
		  var minlon=MINLON+"-120";
		  var maxlon=MAXLON+"-116";
		  var minlat=MINLAT+"31";
		  var maxlat=MAXLAT+"35";
		  var finalUrl=urlBase+"?"+OUTPUT_TYPE+amp+OUTPUT_FORMAT+amp+mintime+amp+maxtime+amp+minmag+amp+maxmag+amp+ETYPE+amp+OUTPUT_LOC+amp+minlon+amp+maxlon+amp+minlat+amp+maxlat;
		  console.log(finalUrl);

		  var results=$.ajax({url:finalUrl,async:false}).responseText;
		  console.log(results);

 		  google.earth.fetchKml(map,results,function(kmlObject){
				if(kmlObject) {
					 map.getFeatures().appendChild(kmlObject);
				}
		  });
		  
//		  var kmlMapOpts={map:map};
//		  var seismicCatalogLayer=new google.maps.KmlLayer(results,kmlMapOpts);
	 }

	 /**
	  * This is the public API
	  */
	 return {
		  createMap:createMap,
		  submitMapRequest:submitMapRequest,
	 }
})();