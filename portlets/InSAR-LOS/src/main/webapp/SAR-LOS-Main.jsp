<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>
  <link rel="stylesheet" href="@host.base.url@/InSAR-LOS/css/default.css"/>
</head>
<body>
  <f:view>
	 <h:panelGrid id="insarlospanelgrid" columns="1" columnClasses="alignTopFixWidth">
		<h:panelGroup id="bigoldlospanelgroup">
		<f:verbatim><fieldset><legend><b>Interferogram Map Selection</b></legend></f:verbatim>
		<f:verbatim>Click on the map to select the region you want to use.</f:verbatim>
		<h:panelGrid id="InSAR-View-All" columns="2" columnClasses="alignTop,alignTop">
		  <h:panelGroup id="Sar-LOS-Panel-Left">
			 <f:verbatim>
				<div id="InSAR-All-Map" style="width: 500px; height: 400px;"></div>
			 </f:verbatim>
			 <f:verbatim>				
				<div id="InSAR-Map-Messages" style="width: 500px; height: 400px;"></div>
			 </f:verbatim>
		  </h:panelGroup>
		  <h:panelGroup id="Sar-LOS-Panel-Right">
			 <f:verbatim>
				<div id="dynatable"></div>
			 </f:verbatim>
			 <f:verbatim><p/></f:verbatim>
			 <h:panelGrid id="insar-results-table" columns="2" columnClasses="alignTop,alignTop">
				<f:verbatim>
				  <div id="outputGraph1"></div>
				</f:verbatim>
				<f:verbatim>
				  <div id="outputGraph2"></div>
				</f:verbatim>
		  </h:panelGrid>
		  </h:panelGroup>
		</h:panelGrid>
		<f:verbatim></fieldset></f:verbatim>
	 </h:panelGroup>
	 </h:panelGrid>
  </f:view>
  <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>  
  <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
  <script src="script/sarselect.js"></script>			 
  <script src="//dygraphs.com/dygraph-combined.js"></script>
  
  <script>
	 //Rendear the InSAR overlays.
	 var insarMapDiv=document.getElementById("InSAR-All-Map");
	 var tableDivName="dynatable";
	 var messageDiv=document.getElementById("InSAR-Map-Messages");
	 sarselect.setMasterMap(insarMapDiv,tableDivName,messageDiv);
  </script>
</body>
</html>
