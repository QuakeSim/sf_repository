<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%> 
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%> 

<html>
<body>
 <script src="http://156.56.104.143:8080/Disloc2/egeoxml.js" type="text/javascript"></script>
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBRxYpIIOz9ynlSKjbx-4JMuN5JjrhR5gSOcKdieYppOZ4_yzZc_Ti15qw"
      type="text/javascript"></script>

<f:view>
<h:form id="faultKMLSelectorForm">
<h:panelGrid id="faultKmlploter" columns="2">
   <f:verbatim>
    <div id="faultMap" style="width: 500px; height: 400px;"></div>
	 </f:verbatim>
	<f:verbatim>
	 <div id="the_side_bar" style="width:300px; height:400px;overflow:auto;"></div>
   </f:verbatim>
</h:panelGrid>
<h:outputText escape="false" value="Fault Name:" /><h:inputText id="faultName" value="Some fault"/>
<h:outputText escape="false" value="Segment Name:" /><h:inputText id="segmentName" value="Some segment"/>
</h:form>
</f:view>

<a id="Northridge" href="javascript:GEvent.trigger(document.getElementById('faultKMLSelectorForm:faultName'),'click','Northridge',exmlFMap.gpolylines[0])">blah</a>

<script language="JavaScript">
	 var faultMap=null;

    //These are used by the fault map 
    faultMap=new GMap2(document.getElementById("faultMap"));
	 faultMap.addMapType(G_PHYSICAL_MAP);
	 faultMap.setMapType(G_PHYSICAL_MAP);
    faultMap.setCenter(new GLatLng(35.0,-118.5),5);
    faultMap.addControl(new GLargeMapControl());
    faultMap.addControl(new GMapTypeControl());
   
		var exmlFMap = new EGeoXml("exmlFMap", faultMap, ["allQTFaults.kml"], {sidebarfn:myside,nozoom:true,sidebarid:"the_side_bar",iwwidth:200});
    exmlFMap.parse();

       //Handle sidebar events.  Param1 is the fault+segment name, param2 is the polyline.
    	 var faultField=document.getElementById("faultKMLSelectorForm:faultName")
	 	 GEvent.addDomListener(faultField,"click",function(param1,param2){
					   var faultName,segmentName;
						//Parse out the segment name
						if(param1.indexOf("-") > -1) {
							faultName=param1.substring(0,param1.indexOf("-"));
							segmentName=param1.substring(param1.indexOf("-")+1,param1.length);
						}
						else {
						   //Fault name and segment name are the same.
							faultName=param1;
							segmentName=param1;
						}

					   //Now show the values.
			         var newElement1=document.getElementById("faultKMLSelectorForm:faultName");
		  				newElement1.setAttribute("value",faultName);
			         var newElement2=document.getElementById("faultKMLSelectorForm:segmentName");
		  				newElement2.setAttribute("value",segmentName);

						//Trigger the polyline click event to show the popup window.
						GEvent.trigger(param2,'click');
	 					});

	 //This function overrides the default side panel.
	 function myside(myvar,name,type,i,graphic) {

	   if(type=="polyline") {
        return '<a id="'+name+'" href="javascript:GEvent.trigger(document.getElementById(\'faultKMLSelectorForm:faultName\'),\'click\',\''+name+'\','+myvar+'.gpolylines['+i+'])">' + name + '</a><br>';

//        return '<a id="'+name+'" href="javascript:GEvent.trigger('
//          + myvar+ '.gpolylines['+i+'],\'click\')">' + name + '</a><br>';
	   }
		return "";
    }
</script>
</body>
</html>
