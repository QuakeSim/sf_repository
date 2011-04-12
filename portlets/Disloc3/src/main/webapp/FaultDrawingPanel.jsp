<h:form id="faultDrawfaultKMLSelectorForm" rendered="#{DislocBean2.renderFaultDrawing}">
  <h:inputHidden id="faultDrawfaultName" value="#{DislocBean2.mapFaultName}"/>
  <h:inputHidden id="faultDrawfaultlistsize" value="#{DislocBean2.myFaultsForProjectListsize}"/>
  <f:verbatim>
	 <fieldset><legend class="portlet-form-label">Fault Map</legend>
  </f:verbatim>
  
  <h:panelGrid id="faultDrawgridforbutton" columns="1" border="0" style="vertical-align:top;">
	 <f:verbatim> Click the button below to get chosen fault params </f:verbatim>
	 <h:commandButton id="faultDrawqueryDBFromMap" 
							value="Get Fault Params" 
							actionListener="#{DislocBean2.toggleSetFaultFromMap}"/> 
  </h:panelGrid>
  <f:verbatim>
	 <script type="text/javascript" src="@host.base.url@@artifactId@/demo.js"></script>
	 <script type="text/javascript" src="@host.base.url@@artifactId@/egeoxml.js"></script>
	 <div style="clear:both"></div>
	 
	 <h:panelGrid id="faultDrawfaultMapsideGrid" columns="1" border="1">
		<f:verbatim>
		  <div id="faultDrawingMap" style="width: 800px; height: 600px;"></div>
		</f:verbatim> 
	 </h:panelGrid>
  </f:verbatim>
  
  <h:panelGroup id="faultDrawdflel235231">
	 <f:verbatim>
		<p/><b>Draw a fault: </b>Instead of selecting a fault from the map, 
		you can draw a new fault on the map.<p/>
	 </f:verbatim>
	 <h:outputText id="faultDrawdaflelerkl513" escape="false" value="Drawing a fault : "/>
	 <h:selectBooleanCheckbox id="faultDrawdflab2" onclick="togglefaultdrawing()" value="#{DislocBean2.faultdrawing}"/>
	 <h:panelGrid columns="3" border="0">
		<h:dataTable border="1" 
						 cellpadding="0" 
						 cellspacing="0" 
						 id="faultDrawdflelerh93" 
						 headerClass="componentstableh"
						 value="#{DislocBean2}" var="myentry32">
		  <h:column>
			 <f:facet name="header">
				<h:outputText id="faultDrawnfa1" escape="false" value="<b>Name</b>" />
			 </f:facet>
			 <h:inputText id="faultDrawfaultnamet" 
							  style="text-align:right;width:60px" 
							  value="#{myentry32.faultName}" 
							  required="true" />								
		  </h:column>
		  
		  <h:column>
			 <f:facet name="header">
				<h:outputText id="faultDrawnfa9" escape="false" value="<b>Lon Start</b>" />
			 </f:facet>
			 <h:inputText id="faultDrawfaultdrawLonStartst" value="#{myentry32.faultLonStart}" required="false" />
		  </h:column>
		  
		  <h:column>
			 <f:facet name="header">
				<h:outputText id="faultDrawnfa7" escape="false" value="<b>Lat Start</b>" />
			 </f:facet>
			 <h:inputText id="faultDrawfaultdrawLatStartst" value="#{myentry32.faultLatStart}" required="false" />
		  </h:column>
		  
		  <h:column>
			 <f:facet name="header">
				<h:outputText id="faultDrawnfa5" escape="false" value="<b>Lon End</b>" />
			 </f:facet>
			 <h:inputText id="faultDrawfaultdrawLonEndst" value="#{myentry32.faultLonEnd}" required="false" />
		  </h:column>
		  <h:column>
			 <f:facet name="header">
				<h:outputText id="faultDrawnfa3" escape="false" value="<b>Lat End</b>" />
			 </f:facet>
			 <h:inputText id="faultDrawfaultdrawLatEndst" value="#{myentry32.faultLatEnd}" required="false" />
		  </h:column>
		  
		</h:dataTable>
	 </h:panelGrid>
  </h:panelGroup>
  <h:commandButton id="faultDrawaddfaultsd" 
						 value="Add a new fault" 
						 actionListener="#{DislocBean2.toggleDrawFaultFromMap}"/>

<f:verbatim>

<script type="text/javascript">


	// These are used by the fault map 	
	var faultMap=null;
	faultMap=new GMap2(document.getElementById("faultDrawingMap"));

	var flistsize = document.getElementById("faultKMLSelectorForm:faultlistsize");
	var flistpolyline = new Array();
	for (var nA = 0 ; nA < flistsize.value ; nA++){
	  flistpolyline[nA] = null;
	}


	// The gridsphere container doesn't work with urls. That should be solved
	// var kmllist = ["@host.base.url@@artifactId@/geo_000520-001216-sim_HDR_4rlks.unw.kml","@host.base.url@@artifactId@/QuakeTables_CGS_1996.kml","@host.base.url@@artifactId@/QuakeTables_CGS_2002.kml"];
	var kmllist = ["QuakeTables_CGS_1996.kml","QuakeTables_CGS_2002.kml"];	
	
	exmlFMap = new EGeoXml("exmlFMap", faultMap, kmllist, {sidebarfn:myside,nozoom:true,sidebarid:"faultMapside",parentformofsidebarid:"faultKMLSelectorForm",clickpolyobjfn:clickpolyobj,iwwidth:200});       
	exmlFMap.parse();

	function jsleep(s){
		s=s*1000;
		var a=true;
		var n=new Date();
		var w;
		var sMS=n.getTime();
		while(a){
			w=new Date();
			wMS=w.getTime();
			if(wMS-sMS>s) a=false;
		}
	}

	jsleep(7);

	faultMap.addMapType(G_PHYSICAL_MAP);
	faultMap.setMapType(G_PHYSICAL_MAP);
	faultMap.setCenter(new GLatLng(35.0,-118.5),6);
	faultMap.addControl(new GLargeMapControl());
	faultMap.addControl(new GMapTypeControl());

	var faultdrawing = document.getElementById("faultKMLSelectorForm:dflab2");
	faultdrawing.checked = false;
	
	// Handle sidebar events.  Param1 is the fault+segment name, param2 is the polyline.
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
						
						// We're going to use just the short name with the new search feature.
						newElement1.value = param1;

						// Trigger the polyline click event to show the popup window.
						if(param3 != 'frommap')
							GEvent.trigger(param2,'click');						
					}
					
				});

	 // This function overrides the default side panel.
	 function myside(myvar,name,type,i,graphic) {
				if((type == "polyline" || type == "polygon") || type == "GroundOverlay") {
					// shortName=name.substring(0,name.indexOf("(InterpId:"));
					shortName=name;
					return '<a id="faultDraw'+name+'" href="javascript:GEvent.trigger(document.getElementById(\'faultKMLSelectorForm:faultName\'),\'click\',\''+name+'\','+myvar+'.gpolyobjs['+i+'], \'script\', '+myvar+'.gpolyobjs_desc['+i+'])">' + shortName + '</a>';					
				}

				return "";
		}

	
	 // This overrides the default clickpolyobjfn of egeoxml.js
	 function clickpolyobj(p, name, desc) {
	     GEvent.trigger(document.getElementById('faultKMLSelectorForm:faultName'),'click', name, p, 'frommap', desc);	    
	  }
  

function togglefaultdrawing() {
	  if (faultdrawing.checked == false) {  
		  
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
	  html += "<b>Location Y</b>:" + document.getElementById(p+":FaultLocationY2").value + "<br/>";

	  html += "<b>Fault Origin Latitude</b>:" + document.getElementById(p+":FaultLatStart").value + "<br/>";
	  html += "<b>Fault Origin Longitude</b>:" + document.getElementById(p+":FaultLonStart").value + "<br/>";
	  html += "<b>Fault End Latitude</b>:" + document.getElementById(p+":FaultLatEnd").value + "<br/>";
	  html += "<b>Fault End Longitude</b>:" + document.getElementById(p+":FaultLonEnd").value + "<br/>";
	  
	  html += "<b>Length</b>:" + document.getElementById(p+":FaultLength").value + "<br/>";	  
	  html += "<b>Width</b>:" + document.getElementById(p+":FaultWidth").value + "<br/>";	  
	  html += "<b>Depth</b>:" + document.getElementById(p+":FaultDepth").value + "<br/>";	  
	  html += "<b>Dip Angle</b>:" + document.getElementById(p+":FaultDipAngle").value + "<br/>";	  
	  html += "<b>Dip Slip</b>:" + document.getElementById(p+":FaultSlip").value + "<br/>";	  
	  html += "<b>Strike Angle</b>:" + document.getElementById(p+":FaultStrikeAngle").value + "<br/>";	  	  
	  html += "<b>Strike Slip</b>:" + document.getElementById(p+":FaultStrikeSlip").value + "<br/>";	  
	  html += "<b>Tensile Slip</b>:" + document.getElementById(p+":FaultTensileSlip").value + "<br/>";
	  html += "<b>LameLambda</b>:" + document.getElementById(p+":LameLambda").value + "<br/>";
	  html += "<b>LameMu</b>:" + document.getElementById(p+":LameMu").value + "<br/>";	  
	  html += "</div>";

	  n = p+":FaultLatEnd";
	  var faultdrawLatEnds = document.getElementById(n);	  
	  n = p+":FaultLatStart";	  

	  var faultdrawLatStarts = document.getElementById(n);	  
	  n = p+":FaultLonEnd";
	  var faultdrawLonEnds = document.getElementById(n);
	  n = p+":FaultLonStart";
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

	var faultdrawLatEnds = document.getElementById("faultKMLSelectorForm:dflelerh93_0:faultdrawLatEndst");	
	var faultdrawLatStarts = document.getElementById("faultKMLSelectorForm:dflelerh93_0:faultdrawLatStartst");     
	var faultdrawLonEnds = document.getElementById("faultKMLSelectorForm:dflelerh93_0:faultdrawLonEndst");
	var faultdrawLonStarts = document.getElementById("faultKMLSelectorForm:dflelerh93_0:faultdrawLonStartst");

	faultdrawLatEnds.value = marker_SW.getPoint().lat();
	faultdrawLonEnds.value = marker_SW.getPoint().lng();
	faultdrawLatStarts.value = marker_NE.getPoint().lat();
	faultdrawLonStarts.value = marker_NE.getPoint().lng();


/*
	if (marker_SW.getPoint().lat() >= marker_NE.getPoint().lat())
	{
		maxlat.value = marker_SW.getPoint().lat();
		minlat.value = marker_NE.getPoint().lat();
	}

	if (marker_SW.getPoint().lng() >= marker_NE.getPoint().lng())
	{
		maxlon.value = marker_SW.getPoint().lng();
		minlon.value = marker_NE.getPoint().lng();
	}
*/
}




function updateFaultline() {
	if (border) {
		faultMap.removeOverlay(border);
		border = null;
	}


	var points = [

new GLatLng(marker_SW.getPoint().lat(), marker_SW.getPoint().lng()),

new GLatLng(marker_NE.getPoint().lat(), marker_NE.getPoint().lng())
];
	border = new GPolyline(points, "#FF0000", 5, 0.45);

	faultMap.addOverlay(border);
}




</script>
</f:verbatim>
<f:verbatim></fieldset></f:verbatim>			 
</h:form>


