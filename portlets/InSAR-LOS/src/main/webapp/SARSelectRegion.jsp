<body>
  <f:view>
	 <h:panelGroup id="SAR-Simplex-SelectionMap"
						rendered="#{SimplexBean.currentEditProjectForm.renderSARSelectionMap}">
		<f:verbatim>
		  <fieldset>
			 <legend><b>Interferogram Map Selection</b></legend>
			 
			 Click on the map to select the region you want to use.
			 <div id="InSARMap" style="width: 800px; height: 600px;"></div>
		  </fieldset>
		</f:verbatim>
	 </h:panelGroup>
  </f:view>
  <script src="http://maps.googleapis.com/maps/api/js?sensor=false"></script> 
  <script src="//ajax.aspnetcdn.com/ajax/jQuery/jquery-1.6.1.min.js"></script>
  <script src="script/sarselect.js"></script>			 
  
  <script>
	 var insarMapDiv=document.getElementById("InSARMap");
	 var overlayUrl="http://gf19.ucs.indiana.edu:9898/uavsar-data/SanAnd_08504_10028-001_10057-101_0079d_s01_L090_01/SanAnd_08504_10028-001_10057-101_0079d_s01_L090HH_01.int.kml";
	 $(function() {
	 sarselect.setMap(insarMapDiv,overlayUrl,"rectangle");
	 });
  
  </script>
</body>