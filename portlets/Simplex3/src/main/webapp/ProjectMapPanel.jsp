<h:panelGroup id="ProjectPanelFrame"
				  rendered="#{SimplexBean.currentEditProjectForm.renderProjectMap}">
  <f:verbatim>
	 <link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/jquery.treeview.css">
		
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
		<script type="text/javascript" src="@host.base.url@@artifactId@/lib/jquery.cookie.js"></script>
		<script type="text/javascript" src="@host.base.url@@artifactId@/jquery.treeview.js"></script>
		<script type="text/javascript" src="@host.base.url@@artifactId@/egeoxml.js"></script>
	 </f:verbatim>	
	 
	 <h:form id="toggleProjectMapForm">
		
		<h:panelGrid id="projectMapPanelGrid" columns="1" border="0">
		  <f:verbatim>
			 <div id="projectMapDiv" style="width: 800px; height: 600px;"></div>
		  </f:verbatim>
		</h:panelGrid>
	 </h:form>  
  
  <f:verbatim>
	 <script type="text/javascript">
		  
		  var faultMap=null;
		  faultMap=new GMap2(document.getElementById("projectMapDiv"));
		  
		  var flistsize = document.getElementById("faultKMLSelectorForm:faultlistsize");
		  var flistpolyline = new Array();
		  for (var nA = 0 ; nA < flistsize.value ; nA++){
		  flistpolyline[nA] = null;
		  }
		  
		  // The gridsphere container doesn't work with urls. That should be solved
		  // var kmllist = ["@host.base.url@@artifactId@/geo_000520-001216-sim_HDR_4rlks.unw.kml","@host.base.url@@artifactId@/QuakeTables_CGS_1996.kml","@host.base.url@@artifactId@/QuakeTables_CGS_2002.kml"];
		  // var kmllist = ["geo_000520-001216-sim_HDR_4rlks.unw.kml","QuakeTables_CGS_1996.kml","QuakeTables_CGS_2002.kml"];
		  var kmllist = ["QuakeTables_CGS_1996.kml","QuakeTables_CGS_2002.kml"];	
		  
		  exmlFMap = new EGeoXml("exmlFMap", faultMap, kmllist, {sidebarfn:myside,nozoom:true,sidebarid:"faultMapside",parentformofsidebarid:"faultKMLSelectorForm",clickpolyobjfn:clickpolyobj,iwwidth:200});       
		  exmlFMap.parse();
		  
		  faultMap.addMapType(G_PHYSICAL_MAP);
		  faultMap.setMapType(G_PHYSICAL_MAP);
		  faultMap.setCenter(new GLatLng(35.0,-118.5),6);
		  faultMap.addControl(new GSmallMapControl());
		  //	faultMap.addControl(new GMapTypeControl());  //Don't show; takes up too much space.
		  
		  var faultdrawing = document.getElementById("faultKMLDrawingForm:dflab2");
		  faultdrawing.checked = false;
		  
		  var faultField=document.getElementById("faultKMLSelectorForm:faultName");
		  GEvent.addDomListener(faultField,"click",function(param1,param2,param3,param4){
		  
		  var interpHead=" (InterpId:";
		  var faultName,segmentName;
		  var segmentNamePlusId, interpId;
		  
		  var newElement1=document.getElementById("faultKMLSelectorForm:faultName");
		  
		  // Parse out the segment name
		  if((param1 != null) && (param2 != null) && (param3 != null)){
		  if(param1.indexOf("-") > -1) {
		  faultName=param1.substring(0,param1.indexOf(" - "));
		  segmentName=param1.substring(param1.indexOf(" - ")+4,param1.indexOf(interpHead));
		  interpId=param1.substring(param1.indexOf(interpHead)+interpHead.length+1,param1.length-1);
		  }
		  else {
		  // No segment name
		  faultName=param1.substring(0,param1.indexOf(interpHead));
		  segmentName="N/A";
		  interpId=param1.substring(param1.indexOf(interpHead)+interpHead.length+1,param1.length-1);
		  }
		  
		  faultName=faultName+"@"+segmentName+"%"+interpId;						
		  
		  // Now show the values.
		  
		  // alert(param4);
		  
		  newElement1.value = param1
		  
		  // Trigger the polyline click event to show the popup window.
		  if(param3 != 'frommap')
		  GEvent.trigger(param2,'click');						
		  }
		  //Update the displayed fault name
		  document.getElementById('faultKMLSelectorForm:CurrentSelectedFaultValue').innerHTML="<b>Current Selected Fault:</b> "+newElement1.value;
		  
		  });
		  
		  // This function overrides the default side panel.
		  function myside(myvar,name,type,i,graphic) {
		  if((type == "polyline" || type == "polygon") || type == "GroundOverlay") {
		  // shortName=name.substring(0,name.indexOf("(InterpId:"));
		  shortName=name;
		  return '<a id="'+name+'" href="javascript:GEvent.trigger(document.getElementById(\'faultKMLSelectorForm:faultName\'),\'click\',\''+name+'\','+myvar+'.gpolyobjs['+i+'], \'script\', '+myvar+'.gpolyobjs_desc['+i+'])">' + shortName + '</a>';					
		  }
		  
		  return "";
		  }
		  
		  // This overrides the default clickpolyobjfn of egeoxml.js
		  function clickpolyobj(p, name, desc) {
		  GEvent.trigger(document.getElementById('faultKMLSelectorForm:faultName'),'click', name, p, 'frommap', desc);	    
		  }
		  
		  function togglefaultdrawing() {
		  if (faultdrawing.checked == false) {  
		  
		  faultMap.removeOverlay(border);
		  faultMap.removeOverlay(marker_NE);
		  faultMap.removeOverlay(marker_SW);  
		  }
		  
		  else {    
		  initialFaultdrawing();
		  }
		  }
		  
		  function togglefaultname(t) {
		  
		  var bits = t.id;
		  bits = bits.split(":");    
		  var prefix = bits[1].split("_");
		  
		  if (flistpolyline[prefix[1]]) {
		  faultMap.removeOverlay(flistpolyline[prefix[1]]);
		  flistpolyline[prefix[1]] = null;
		  }
		  
		  else {
		  
		  var p = "UpdateSelectFaultsForm:dflelerh966_"+prefix[1];
		  var n;	  
		  var html = "<div style='font-weight: bold; font-size: medium; margin-bottom: 0em;'>"+document.getElementById(p+":dflelerklh968").value+"</div>"
		  
		  html += "<div style='font-family: Arial, sans-serif;font-size: small;width:"+this.iwwidth+"px'>";	  
		  html += "<b>Fault Name</b>:" + document.getElementById(p+":dflelerklh968").value + "<br/>";
		  html += "<b>Location X</b>:" + document.getElementById(p+":FaultLocationX2").value + "<br/>";
		  html += "<b>Location X Vary</b>:" + document.getElementById(p+":faultOriginXVary2").checked + "<br/>";
		  html += "<b>Location Y</b>:" + document.getElementById(p+":FaultLocationY2").value + "<br/>";
		  html += "<b>Location Y Vary</b>:" + document.getElementById(p+":faultOriginYVary2").checked + "<br/>";
		  html += "<b>Length</b>:" + document.getElementById(p+":FaultLength2").value + "<br/>";
		  html += "<b>Length Vary</b>:" + document.getElementById(p+":faultLengthVary2").checked + "<br/>";
		  html += "<b>Width</b>:" + document.getElementById(p+":FaultWidth2").value + "<br/>";
		  html += "<b>Width Vary</b>:" + document.getElementById(p+":faultWidthVary2").checked + "<br/>";
		  html += "<b>Depth</b>:" + document.getElementById(p+":FaultDepth2").value + "<br/>";
		  html += "<b>Depth Vary</b>:" + document.getElementById(p+":faultDepthVary2").checked + "<br/>";
		  html += "<b>Dip Angle</b>:" + document.getElementById(p+":FaultDipAngle2").value + "<br/>";
		  html += "<b>Dip Angle Vary</b>:" + document.getElementById(p+":faultDipAngleVary2").checked + "<br/>";
		  html += "<b>Strike Angle</b>:" + document.getElementById(p+":FaultStrikeAngle2").value + "<br/>";
		  html += "<b>Strike Angle Vary</b>:" + document.getElementById(p+":faultStrikeAngleVary2").checked + "<br/>";
		  html += "<b>Dip Slip</b>:" + document.getElementById(p+":FaultSlip2").value + "<br/>";
		  html += "<b>Dip Slip Vary</b>:" + document.getElementById(p+":faultDipSlipVary2").checked + "<br/>";
		  html += "<b>Strike Slip</b>:" + document.getElementById(p+":FaultRakeAngle2").value + "<br/>";
		  html += "<b>Strike Slip Vary</b>:" + document.getElementById(p+":faultStrikeSlipVary2").checked + "<br/>";
		  html += "<b>Fault Lon Starts(optional)</b>:" + document.getElementById(p+":FaultLonStarts2").value + "<br/>";
		  html += "<b>Fault Lat Starts(optional)</b>:" + document.getElementById(p+":FaultLatStarts2").value + "<br/>";
		  html += "<b>Fault Lon Ends(optional)</b>:" + document.getElementById(p+":FaultLonEnds2").value + "<br/>";
		  html += "<b>Fault Lat Ends(optional)</b>:" + document.getElementById(p+":FaultLatEnds2").value + "<br/>";
		  html += "</div>";
		  
		  n = p+":FaultLatEnds2";
		  var faultdrawLatEnds = document.getElementById(n);	  
		  n = p+":FaultLatStarts2";	  
		  
		  var faultdrawLatStarts = document.getElementById(n);	  
		  n = p+":FaultLonEnds2";
		  var faultdrawLonEnds = document.getElementById(n);
		  n = p+":FaultLonStarts2";
		  var faultdrawLonStarts = document.getElementById(n);
		  
		  var points = [new GLatLng(faultdrawLatEnds.value, faultdrawLonEnds.value),
		  new GLatLng(faultdrawLatStarts.value, faultdrawLonStarts.value)];
		  
		  flistpolyline[prefix[1]] = new GPolyline(points, "#FF0000", 5, 0.45);
		  
		  GEvent.addListener(flistpolyline[prefix[1]],"click", function() {
		  faultMap.openInfoWindowHtml(flistpolyline[prefix[1]].getVertex(Math.floor(flistpolyline[prefix[1]].getVertexCount()/2)),html,{});
		  
		  });
		  
		  faultMap.addOverlay(flistpolyline[prefix[1]]);
		  }
		  }
				
		  function initialFaultdrawing() {
		  
		  var bounds = faultMap.getBounds();
		  var span = bounds.toSpan();
		  var newSW = new GLatLng(bounds.getSouthWest().lat() + span.lat()/3, 
		  bounds.getSouthWest().lng() + span.lng()/3);
		  var newNE = new GLatLng(bounds.getNorthEast().lat() - span.lat()/3, 
		  bounds.getNorthEast().lng() - span.lng()/3);
		  
		  var newBounds = new GLatLngBounds(newSW, newNE);
		  
		  var border;
		  var icon_NE, icon_SW;
		  icon_NE = new GIcon(); 
		  icon_NE.image = 'http://maps.google.com/mapfiles/ms/micons/red-pushpin.png';
		  icon_NE.shadow = '';
		  icon_NE.iconSize = new GSize(32, 32);
		  icon_NE.shadowSize = new GSize(22, 20);
		  icon_NE.iconAnchor = new GPoint(10, 32);
		  icon_NE.dragCrossImage = '';

		  icon_SW = icon_NE;

		  marker_NE = new GMarker(newBounds.getNorthEast(), {draggable: true, icon: icon_NE});
		  GEvent.addListener(marker_NE, 'dragend', function() {
		  updateFaultline();
		  updateFaultdrawn();
		  });
		  
		  marker_SW = new GMarker(newBounds.getSouthWest(), {draggable: true, icon: icon_SW});
		  GEvent.addListener(marker_SW, 'dragend', function() {
		  updateFaultline();
		  updateFaultdrawn();
		  });  
		  
		  faultMap.addOverlay(marker_NE);
		  faultMap.addOverlay(marker_SW);
		  updateFaultdrawn();
		  updateFaultline();
		  }
		  
		  function updateFaultdrawn() {
		  
		  var faultdrawLatEnds = document.getElementById("faultKMLDrawingForm:dflelerh93_0:faultdrawLatEndst");	
		  var faultdrawLatStarts = document.getElementById("faultKMLDrawingForm:dflelerh93_0:faultdrawLatStartst");     
		  var faultdrawLonEnds = document.getElementById("faultKMLDrawingForm:dflelerh93_0:faultdrawLonEndst");
		  var faultdrawLonStarts = document.getElementById("faultKMLDrawingForm:dflelerh93_0:faultdrawLonStartst");
		  
		  faultdrawLatEnds.value = marker_SW.getPoint().lat();
		  faultdrawLonEnds.value = marker_SW.getPoint().lng();
		  faultdrawLatStarts.value = marker_NE.getPoint().lat();
		  faultdrawLonStarts.value = marker_NE.getPoint().lng();
		  }
		  
		  
		  function updateFaultline() {
		  if (border) {
		  faultMap.removeOverlay(border);
		  border = null;
		  }
		  
		  var points = [new GLatLng(marker_SW.getPoint().lat(), marker_SW.getPoint().lng()),
		  new GLatLng(marker_NE.getPoint().lat(), marker_NE.getPoint().lng())
		  ];
		  border = new GPolyline(points, "#FF0000", 5, 0.45);
		  
		  faultMap.addOverlay(border);
		  }
		  
		</script>
	 </f:verbatim>
  </h:panelGroup>
