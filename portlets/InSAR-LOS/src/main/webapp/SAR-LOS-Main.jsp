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
		  <f:verbatim>Click on the map to select the region you want to use.  If you don't see green rectangles
		  marking the UAVSAR swaths, try refreshing your browser.
		  </f:verbatim>
		  <h:panelGrid id="InSAR-View-All" columns="2" columnClasses="alignTopWidth1,alignTopWidth2">
			 <h:panelGroup id="Sar-LOS-Panel-Left">
				<h:panelGroup id="The-Big-Map">
				  <f:verbatim>
					 <div id="InSAR-All-Map" style="width: 600px; height: 400px;"></div>
				  </f:verbatim>
				  <f:verbatim>				
					 <div id="InSAR-Map-Messages" style="display:none;"></div>
				  </f:verbatim>
				</h:panelGroup>
				<h:panelGroup id="Left-Column-Under-Map" style="display:none;">
				  <f:verbatim>
					 <div id="iconGuide">
						<img src="http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|FF0000"/> 
						is the starting point. 
						<image src="http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|0000FF"/> 
						is the  ending point.
					 </div>
				  </f:verbatim>
				  <h:panelGrid id="plotgridpanel" columns="2" columnClasses="alignTop,alignTop">
					 <f:verbatim>
						<div id="outputGraph1"></div>
					 </f:verbatim>
					 <f:verbatim>
						<div id="outputGraph2"></div>
					 </f:verbatim>
					 <f:verbatim>				
						<div id="LOS-Data-Download"></div>
					 </f:verbatim>
					 <f:verbatim>				
						<div id="HGT-Data-Download"></div>
					 </f:verbatim>
				  </h:panelGrid>
				</h:panelGroup>
			 </h:panelGroup>
			 <h:panelGroup id="Sar-LOS-Panel-Right">
				<f:verbatim>
				  <div id="dynatable"></div>
				</f:verbatim>
				<f:verbatim><p/></f:verbatim>
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
	 sarselect.setMasterMap(insarMapDiv,tableDivName);
  </script>
</body>
</html>
