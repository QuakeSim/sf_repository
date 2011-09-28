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
			 <h:inputHidden id="overlayUrl" value="#{InSarLOSBean.overlayUrl}"/>
			 <f:verbatim>
				<div id="InSARMap" style="width: 800px; height: 600px;"></div>
			 </f:verbatim>
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
		var overlayUrl=document.getElementById("insarlosform:overlayUrl");

		console.log("Overlay URL is "+overlayUrl);
		
		$(function() {
		var map=sarselect.setLOSMap(insarMapDiv);
		sarselect.activateLayerMap(map,overlayUrl.value,"line");
		});
		
		
  </script>
</body>
