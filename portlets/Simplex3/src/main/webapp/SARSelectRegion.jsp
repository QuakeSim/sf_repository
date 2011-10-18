<h:panelGroup id="SAR-Simplex-SelectionMap"
				  rendered="#{SimplexBean.currentEditProjectForm.renderSARSelectionMap}">
  <f:verbatim><fieldset style="width:960px"><legend><b>Interferogram Map Selection</b></legend></f:verbatim>
  <f:verbatim>Click on the map to select the region you want to use.</f:verbatim>
  <h:panelGrid id="InSAR-View-All" columns="2" columnClasses="alignTop,alignTop">
	 
	 <f:verbatim>
		<div id="InSAR-All-Map" style="width: 600px; height: 400px;"></div>
	 </f:verbatim>
	 <f:verbatim>
		<div id="dynatable"></div>
	 </f:verbatim>
  </h:panelGrid>
  <f:verbatim></fieldset></f:verbatim>
  <f:verbatim>
	 <script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
	 <script src="@host.base.url@@artifactId@/script/sarselect.js"></script>			 
	 
	 <script>
		//Rendear the InSAR overlays.
		var insarMapDiv=document.getElementById("InSAR-All-Map");
		var tableDivName="dynatable";
		sarselect.setMasterMap(insarMapDiv,tableDivName);
	 </script>
  </f:verbatim>
</h:panelGroup>