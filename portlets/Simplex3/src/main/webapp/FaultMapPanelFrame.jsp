<h:form id="faultKMLSelectorForm" rendered="#{SimplexBean.currentEditProjectForm.renderFaultMap}">
<h:inputHidden id="faultName" value="#{SimplexBean.currentEditProjectForm.mapFaultName}"/>


<h:panelGrid id="faultKmlploter" columns="1" border="1">
  <h:panelGrid id="gridforbutton" columns="1" border="0" style="vertical-align:top;">
    <f:verbatim>
      Click the button below to get chosen fault params
    </f:verbatim>
    <h:commandButton id="queryDBFromMap" value="Get Fault Params" actionListener="#{SimplexBean.currentEditProjectForm.toggleSetFaultFromMap}"/> 
  </h:panelGrid>

  <f:verbatim>
    <link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/jquery.treeview.css">

    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
    <script type="text/javascript" src="@host.base.url@@artifactId@/lib/jquery.cookie.js"></script>
    <script type="text/javascript" src="@host.base.url@@artifactId@/jquery.treeview.js"></script>
    <script type="text/javascript" src="@host.base.url@@artifactId@/egeoxml.js"></script>

    <h:panelGrid id="faultMapsideGrid" columns="2" border="1">
      <f:verbatim>
	<div id="faultMapside" style="width: 200px; height: 400px; overflow:auto;"></div>
      </f:verbatim>
      <f:verbatim>
	<div id="faultMap" style="width: 600px; height: 400px;"></div>
      </f:verbatim>
    </h:panelGrid>
  </f:verbatim>	

  <h:panelGrid columns="2" border="0">
      <h:outputText id="daflelerkl513" escape="false" value="Drawing a fault : "/>
      <h:selectBooleanCheckbox id="dflab2" onclick="togglefaultdrawing()" value="#{SimplexBean.faultdrawing}"/>
   </h:panelGrid>

  <h:dataTable border="1" cellpadding="0" cellspacing="0" id="dflelerh93" headerClass="componentstableh"
		value="#{SimplexBean.currentEditProjectForm}" var="myentry32">
    <h:column>
      <f:facet name="header">
	<h:outputText id="nfa1" escape="false" value="<b>Name</b>" />
      </f:facet>
      <h:inputText id="faultnamet" style="text-align:right;width:60px" value="#{myentry32.faultName}" required="true" />
    </h:column>
    <h:column>
      <f:facet name="header">
	<h:outputText id="nfa3" escape="false" value="<b>Lat End</b>" />
      </f:facet>
      <h:inputText id="faultdrawLatEndst" value="#{myentry32.faultLatEnd}" required="true" />
    </h:column>
    <h:column>
      <f:facet name="header">
	<h:outputText id="nfa5" escape="false" value="<b>Lon End</b>" />
      </f:facet>
      <h:inputText id="faultdrawLonEndst" value="#{myentry32.faultLonEnd}" required="true" />
    </h:column>
    <h:column>
      <f:facet name="header">
	<h:outputText id="nfa7" escape="false" value="<b>Lat Start</b>" />
      </f:facet>
      <h:inputText id="faultdrawLatStartst" value="#{myentry32.faultLatStart}" required="true" />
    </h:column>
    <h:column>
      <f:facet name="header">
	<h:outputText id="nfa9" escape="false" value="<b>Lon Start</b>" />
      </f:facet>
      <h:inputText id="faultdrawLonStartst" value="#{myentry32.faultLonStart}" required="true" />
    </h:column>
  </h:dataTable>
  <h:commandButton id="addfaultsd" value="Add a new fault" actionListener="#{SimplexBean.toggleDrawFaultFromMap}"/>
  
</h:panelGrid>


<f:verbatim>
<script language="JavaScript">


	var faultMap=null;
	faultMap=new GMap2(document.getElementById("faultMap"));
	
	

	// The gridsphere container doesn't work with urls. That should be solved
	// var kmllist = ["@host.base.url@@artifactId@/geo_000520-001216-sim_HDR_4rlks.unw.kml","@host.base.url@@artifactId@/QuakeTables_CGS_1996.kml","@host.base.url@@artifactId@/QuakeTables_CGS_2002.kml"];
	// var kmllist = ["geo_000520-001216-sim_HDR_4rlks.unw.kml","QuakeTables_CGS_1996.kml","QuakeTables_CGS_2002.kml"];
	var kmllist = ["QuakeTables_CGS_1996.kml","QuakeTables_CGS_2002.kml"];		
	
	exmlFMap = new EGeoXml("exmlFMap", faultMap, kmllist, {sidebarfn:myside,nozoom:true,sidebarid:"faultMapside",parentformofsidebarid:"faultKMLSelectorForm",clickpolyobjfn:clickpolyobj,iwwidth:200});       
	

	exmlFMap.parse();
	
	
	faultMap.addMapType(G_PHYSICAL_MAP);
	faultMap.setMapType(G_PHYSICAL_MAP);
	faultMap.setCenter(new GLatLng(35.0,-118.5),6);
	faultMap.addControl(new GLargeMapControl());
	faultMap.addControl(new GMapTypeControl());
   
	var faultdrawing = document.getElementById("faultKMLSelectorForm:dflab2");
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

</h:form>




