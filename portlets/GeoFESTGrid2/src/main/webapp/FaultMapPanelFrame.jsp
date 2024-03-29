<h:form id="faultKMLSelectorForm" rendered="#{MGBean.renderFaultMap}">
<h:inputHidden id="faultName" value="#{MGBean.mapFaultName}"/>



<h:panelGrid id="faultKmlploter" columns="1" border="1">
<h:panelGrid id="gridforbutton" columns="1" border="0" style="vertical-align:top;">

	<f:verbatim>
		Click the button below to get chosen fault params
	</f:verbatim>
	<h:commandButton id="queryDBFromMap" value="Get Fault Params" actionListener="#{MGBean.toggleSetFaultFromMap}"/> 

</h:panelGrid>

<f:verbatim>
<link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/quakesim_style.css">
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

</h:panelGrid>


<f:verbatim>


<script type="text/javascript">


	// These are used by the fault map 	
	var faultMap=null;
	faultMap=new GMap2(document.getElementById("faultMap"));

	// The gridsphere container doesn't work with urls. That should be solved
	// var kmllist = ["@host.base.url@@artifactId@/geo_000520-001216-sim_HDR_4rlks.unw.kml","@host.base.url@@artifactId@/QuakeTables_CGS_1996.kml","@host.base.url@@artifactId@/QuakeTables_CGS_2002.kml"];
	var kmllist = ["geo_000520-001216-sim_HDR_4rlks.unw.kml","QuakeTables_CGS_1996.kml","QuakeTables_CGS_2002.kml","QuakeTables_UCERF_2.4.kml"];	
	
	exmlFMap = new EGeoXml("exmlFMap", faultMap, kmllist, {sidebarfn:myside,nozoom:true,sidebarid:"faultMapside",parentformofsidebarid:"faultKMLSelectorForm",clickpolyobjfn:clickpolyobj,iwwidth:200});       
	exmlFMap.parse();

		

	faultMap.addMapType(G_PHYSICAL_MAP);
	faultMap.setMapType(G_PHYSICAL_MAP);
	faultMap.setCenter(new GLatLng(35.0,-118.5),6);
	faultMap.addControl(new GLargeMapControl());
	faultMap.addControl(new GMapTypeControl());

	
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
					return '<a id="'+name+'" href="javascript:GEvent.trigger(document.getElementById(\'faultKMLSelectorForm:faultName\'),\'click\',\''+name+'\','+myvar+'.gpolyobjs['+i+'], \'script\', '+myvar+'.gpolyobjs_desc['+i+'])">' + shortName + '</a>';					
				}

				return "";
		}

	 // This overrides the default clickpolyobjfn of egeoxml.js
	 function clickpolyobj(p, name, desc) {
	     GEvent.trigger(document.getElementById('faultKMLSelectorForm:faultName'),'click', name, p, 'frommap', desc);	    
	  }
  
</script>
</f:verbatim>

</h:form>
