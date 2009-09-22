<h:form id="faultKMLSelectorForm" rendered="#{SimplexBean.currentEditProjectForm.renderFaultMap}">
<h:panelGrid id="faultKmlploter" columns="1">
<f:verbatim>

<div id = "the_kmlselection_bar" style="width:200px; height:100px;overflow:yes">

<link rel="stylesheet" type="text/css" href='<%= request.getContextPath() + "/stylesheet.css"%>'>
<link rel="stylesheet" type="text/css" href="quakesim_style.css">


<ul id="sddm">

    <li><a href="#" 
        onmouseover="mopen('m1')" 
        onmouseout="mclosetime()">KML
Add
/Delete</a>
	<div id="m1" 
            onmouseover="mcancelclosetime()" 
            onmouseout="mclosetime()">
        <a href="#" onClick="">Add a KML file</a>
        <a href="#" onClick="">Delete a KML file</a>
        </div>
    </li>



    <li><a href="#" 
        onmouseover="mopen('m2')" 
        onmouseout="mclosetime()">KML
Show
/Remove</a>
        <div id="m2" 
            onmouseover="mcancelclosetime()" 
            onmouseout="mclosetime()">
	<a href="#" onClick="overlay_clear()">Clear</a>
        <a href="#" onClick="showKML('allQTFaults.kml')">Show allQTFaults.kml</a>
        <a href="#" onClick="">Show KML2</a>
        <a href="#" onClick="">Show KML3</a>
        <a href="#" onClick="">Show KML4</a>
        </div>
    </li>
</ul>




<div style="clear:both"></div>

</div>
<div id="faultMap" style="width: 1200px; height: 700px;"></div>
</f:verbatim>



<link rel="stylesheet" type="text/css" href="quakesim_style.css">


<f:verbatim>
	 <div id="the_side_bar" style="width:1200px; height:200px;overflow:auto;"></div>
</f:verbatim>

</h:panelGrid>

<h:outputText escape="false" value="<b>Fault Name:</b>" /><h:inputText id="faultName" value="#{SimplexBean.currentEditProjectForm.mapFaultName}"/>
<h:commandButton id="queryDBFromMap" value="Get Fault Params" actionListener="#{SimplexBean.currentEditProjectForm.toggleSetFaultFromMap}"/> 


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


	function showKML(filename){
	  faultMap.clearOverlays();
	  // var exmlFMap = new EGeoXml("exmlFMap", faultMap, ["allQTFaults.kml"], {sidebarfn:myside,nozoom:true,sidebarid:"the_side_bar",iwwidth:200});
	  var exmlFMap = new EGeoXml("exmlFMap", faultMap, filename, {sidebarfn:myside,nozoom:true,sidebarid:"the_side_bar",iwwidth:200});
	  exmlFMap.parse();
	}

	function overlay_clear(){
	  faultMap.clearOverlays();
	}



      // === Create a custom Control ===
      var labelContainer;

      function LabelControl() {  }
      LabelControl.prototype = new GControl();

      LabelControl.prototype.initialize = function(map) {
        labelContainer = document.getElementById("the_kmlselection_bar");

        map.getContainer().appendChild(labelContainer);
        return labelContainer;
      }

      LabelControl.prototype.getDefaultPosition = function() {
        return new GControlPosition(G_ANCHOR_TOP_LEFT, new GSize(5, 400));
      }

//
	var faultMap=null;
	//These are used by the fault map 

	faultMap=new GMap2(document.getElementById("faultMap"));
	faultMap.addControl(new LabelControl());
	faultMap.addMapType(G_PHYSICAL_MAP);
	faultMap.setMapType(G_PHYSICAL_MAP);
	faultMap.setCenter(new GLatLng(35.0,-118.5),5);
	faultMap.addControl(new GLargeMapControl());
	faultMap.addControl(new GMapTypeControl());
   
//	var exmlFMap = new EGeoXml("exmlFMap", faultMap, ["allQTFaults.kml"], {sidebarfn:myside,nozoom:true,sidebarid:"the_side_bar",iwwidth:200});
//	exmlFMap.parse();

	//Handle sidebar events.  Param1 is the fault+segment name, param2 is the polyline.
	var faultField=document.getElementById("faultKMLSelectorForm:faultName")
	GEvent.addDomListener(faultField,"click",function(param1,param2){
	var interpHead=" (InterpId:";
	var faultName,segmentName;
	var segmentNamePlusId, interpId;
	
	//Parse out the segment name
	if(param1.indexOf("--") > -1) {
						  
		faultName=param1.substring(0,param1.indexOf(" -- "));
		segmentName=param1.substring(param1.indexOf(" -- ")+4,param1.indexOf(interpHead));
		interpId=param1.substring(param1.indexOf(interpHead)+interpHead.length+1,param1.length-1);
						}
						else {
						   //No segment name
							faultName=param1.substring(0,param1.indexOf(interpHead));
							segmentName="N/A";
							interpId=param1.substring(param1.indexOf(interpHead)+interpHead.length+1,param1.length-1);
						}
						faultName=faultName+"@"+segmentName+"%"+interpId;

					   //Now show the values.
			         var newElement1=document.getElementById("faultKMLSelectorForm:faultName");
		  				newElement1.setAttribute("value",faultName);

						//Trigger the polyline click event to show the popup window.
						GEvent.trigger(param2,'click');
	 					});

	 //This function overrides the default side panel.
	 function myside(myvar,name,type,i,graphic) {

	 if(type=="polyline") {
		shortName=name.substring(0,name.indexOf("(InterpId:"));
        return '<a id="'+name+'" href="javascript:GEvent.trigger(document.getElementById(\'faultKMLSelectorForm:faultName\'),\'click\',\''+name+'\','+myvar+'.gpolylines['+i+'])">' + shortName + '</a><br>';

			 var message=document.getElementById("faultKMLSelectorForm:messageBox");
			 message.setAttribute("value",message);
	   }
		return "";
    }
</script>


</f:verbatim>







</h:form>


