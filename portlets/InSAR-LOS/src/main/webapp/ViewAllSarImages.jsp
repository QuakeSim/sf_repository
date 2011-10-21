<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<body>
  <f:view>
	 <f:verbatim><fieldset><legend><b>Interferogram Map Selection</b></legend></f:verbatim>
	 <h:panelGrid id="InSAR-View-All">
		<f:verbatim>
		  Click on the map to select the region you want to use.
		  <div id="InSAR-All-Map" style="width: 800px; height: 600px;"></div>
		</f:verbatim>
		<f:verbatim>
		  <div id="dynatable"></div>
		</f:verbatim>
		<f:verbatim></fieldset></f:verbatim>
	 </h:panelGrid>
  </f:view>
  <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>  
  <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
  <script src="script/sarselect.js"></script>			 
  
  <script>
	 //Rendear the InSAR overlays.
	 var insarMapDiv=document.getElementById("InSAR-All-Map");
	 var tableDivName="dynatable";
	 sarselect.setMasterMap(insarMapDiv,tableDivName);

  </script>
</body>