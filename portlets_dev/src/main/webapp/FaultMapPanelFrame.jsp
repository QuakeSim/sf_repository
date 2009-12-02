<h:form id="faultKMLSelectorForm" rendered="#{DislocBean2.renderFaultMap}">
<h:inputHidden id="faultName" value="#{DislocBean2.mapFaultName}"/>


<h:panelGrid id="faultKmlploter" columns="1" border="1">
<h:panelGrid id="gridforbutton" columns="1" border="0" style="vertical-align:top;">

	<f:verbatim>
		Click the button below to get chosen fault params
	</f:verbatim>
	<h:commandButton id="queryDBFromMap" value="Get Fault Params" actionListener="#{DislocBean2.toggleSetFaultFromMap}"/> 

</h:panelGrid>
 

<f:verbatim>
<link rel="stylesheet" type="text/css" href="@host.base.url@Disloc3/quakesim_style.css">
<link rel="stylesheet" type="text/css" href="@host.base.url@Disloc3/jquery.treeview.css">

<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
<script type="text/javascript" src="@host.base.url@Disloc3/lib/jquery.cookie.js"></script>
<script type="text/javascript" src="@host.base.url@Disloc3/jquery.treeview.js"></script>

<script type="text/javascript" src="@host.base.url@Disloc3/demo.js"></script>


<div style="clear:both"></div>

</div>

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


<script language="JavaScript">


var timeout	= 500;
var closetimer	= 0;
var ddmenuitem	= 0;

// open hidden layer
function mopen(id)
{	
	// cancel close timer
	mcancelclosetime();

	// close old layer
	if(ddmenuitem) ddmenuitem.style.visibility = 'hidden';

	// get new layer and show it
	ddmenuitem = document.getElementById(id);
	ddmenuitem.style.visibility = 'visible';

}
// close showed layer
function mclose()
{
	if(ddmenuitem) ddmenuitem.style.visibility = 'hidden';
}

// go close timer
function mclosetime()
{
	closetimer = window.setTimeout(mclose, timeout);
}

// cancel close timer
function mcancelclosetime()
{
	if(closetimer)
	{
		window.clearTimeout(closetimer);
		closetimer = null;
	}
}

	// close layer when click-out
	document.onclick = mclose; 


	// These are used by the fault map 	
	var faultMap=null;
	faultMap=new GMap2(document.getElementById("faultMap"));
	

//	var kmllist = ["QuakeTables_CGS_1996.kml","QuakeTables_CGS_2002.kml","allQTFaults.kml"];
	var kmllist = ["@host.base.url@Disloc3/geo_000520-001216-sim_HDR_4rlks.unw.kml","@host.base.url@Disloc3/QuakeTables_CGS_1996.kml","@host.base.url@Disloc3/QuakeTables_CGS_2002.kml"];

	
	faultMap.clearOverlays();
	exmlFMap = new EGeoXml("exmlFMap", faultMap, kmllist, {sidebarfn:myside,nozoom:true,sidebarid:"faultMapside",iwwidth:200});       
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
					
						// Now show the values.

						// alert(param4);
						
						newElement1.value = param1+"@@##"+param4;


						// Trigger the polyline click event to show the popup window.
						if(param3 != 'frommap')
							GEvent.trigger(param2,'click');						
					}
					
				});

	 // This function overrides the default side panel.
	 function myside(myvar,name,type,i,graphic) {
				if(type=="polyline" || type=="polygon") {
					// shortName=name.substring(0,name.indexOf("(InterpId:"));
					shortName=name;
					return '<a id="'+name+'" href="javascript:GEvent.trigger(document.getElementById(\'faultKMLSelectorForm:faultName\'),\'click\',\''+name+'\','+myvar+'.gpolylines['+i+'], \'script\', '+myvar+'.gpolylines_desc['+i+'])">' + shortName + '</a>';					

					var message=document.getElementById("faultKMLSelectorForm:messageBox");

					if(type=="polyline")
						message.setAttribute("value",message);
				}

				return "";
		}

  
</script>
</f:verbatim>

</h:form>


