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
<h:inputText id="faultsStars" value="Some fault"/>
</h:form>
</f:view>

<a id="Northridge" href="javascript:GEvent.trigger(document.getElementById('faultKMLSelectorForm:faultsStars'),'click','Northridge')">blah</a>

<script language="JavaScript">
	 var faultMap=null;
	 var faultField=document.getElementById("faultKMLSelectorForm:faultsStars")

    //These are used by the fault map 
    faultMap=new GMap2(document.getElementById("faultMap"));
    faultMap.setCenter(new GLatLng(35.0,-118.5),5);
    faultMap.addControl(new GLargeMapControl());
    faultMap.addControl(new GMapTypeControl());
   
		var exmlFMap = new EGeoXml("exmlFMap", faultMap, ["allQTFaults.kml"], {sidebarfn:myside,nozoom:true,sidebarid:"the_side_bar",iwwidth:200});
    exmlFMap.parse();

	 	 GEvent.addDomListener(faultField,"click",function(param1){
			         var newElement1=document.getElementById("faultKMLSelectorForm:faultsStars");
		  				newElement1.setAttribute("value",param1);
	 					});

	 function myside(myvar,name,type,i,graphic) {

	   if(type=="polyline") {
        return '<a id="'+name+'" href="javascript:GEvent.trigger(document.getElementById(\'faultKMLSelectorForm:faultsStars\'),\'click\',\''+name+'\')">' 
					+ name + '</a><br>';

//        return '<a id="'+name+'" href="javascript:GEvent.trigger('
//          + myvar+ '.gpolylines['+i+'],\'click\')">' + name + '</a><br>';
	   }
		return "";
    }
</script>
</body>
</html>
