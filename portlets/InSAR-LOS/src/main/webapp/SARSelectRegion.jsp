<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<body>
  <f:view>
	 <h:panelGroup id="SAR-LOS-SelectionMap">
		<h:form id="insarlosform">
		  <h:panelGrid id="SAR-Panel-Grid" columns="2">
			 <h:panelGroup id="Sar-LOS-Panel-Left">
			 <h:inputHidden id="insarLosValues" value="#{InSarLOSBean.outputResponse}"/>
			 <h:inputHidden id="westMarkerLat" value="#{InSarLOSBean.lat0}"/>
			 <h:inputHidden id="westMarkerLon" value="#{InSarLOSBean.lon0}"/>
			 <h:inputHidden id="eastMarkerLat" value="#{InSarLOSBean.lat1}"/>
			 <h:inputHidden id="eastMarkerLon" value="#{InSarLOSBean.lon1}"/>
			 <f:verbatim>
				<div id="InSARMap" style="width: 800px; height: 600px;"></div>
			 </f:verbatim>
			 <h:commandButton id="InSARLOSGetCSV" value="Select" actionListener="#{InSarLOSBean.getImageLOSValues}"/>
		  </h:panelGroup>
		  <h:panelGroup id="Sar-LOS-Panel-Right">
			 <f:verbatim>
				  <div id="outputGraph"></div>
				</f:verbatim>
			 </h:panelGroup>
			 </h:panelGrid>
		  </h:form>
		</h:panelGroup>
	 </f:view>
	 <script src="//maps.googleapis.com/maps/api/js?sensor=false"></script> 
	 <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
	 <script src="script/sarselect.js"></script>			 
	 <script src="//dygraphs.com/dygraph-combined.js"></script>
	 
	 <script>

		//This creates the map.
		var insarMapDiv=document.getElementById("InSARMap");
		var westMarkerLat=document.getElementById("insarlosform:westMarkerLat");
		var westMarkerLon=document.getElementById("insarlosform:westMarkerLon");
		var eastMarkerLat=document.getElementById("insarlosform:eastMarkerLat");
		var eastMarkerLon=document.getElementById("insarlosform:westMarkerLon");
		var overlayUrl="http://gf19.ucs.indiana.edu:9898/uavsar-data/SanAnd_08504_10028-001_10057-101_0079d_s01_L090_01/SanAnd_08504_10028-001_10057-101_0079d_s01_L090HH_01.int.kml";
		
		$(function() {
		sarselect.setMap(insarMapDiv,westMarkerLat,westMarkerLon,eastMarkerLat,eastMarkerLon,overlayUrl,"line");
		});
		

		//This creates the plot
		var sarvals=document.getElementById("insarlosform:insarLosValues");
		console.log("CSV list:"+sarvals.value);
		
		if(sarvals.value!="") {
		var g=new Dygraph(document.getElementById("outputGraph"),sarvals.value);
		}
		
  </script>
</body>
