<body>
  <f:view>
	 <h:panelGroup id="InSAR-View-All"
						rendered="#{SimplexBean.currentEditProjectForm.renderSARSelectionMap}">
		<f:verbatim>
		  <fieldset>
			 <legend><b>Interferogram Map Selection</b></legend>
			 Click on the map to select the region you want to use.
			 <div id="InSAR-All-Map" style="width: 800px; height: 600px;"></div>
		  </fieldset>
		</f:verbatim>
	 </h:panelGroup>
  </f:view>
  <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" 
			 type="text/javascript"></script>  
  <script src="script/sarselect.js"></script>			 
  
  <script>
	 var insarMapDiv=document.getElementById("InSAR-All-Map");
	 var map = new GMap2(insarMapDiv);
	 
//	 map.addMapType(G_HYBRID_MAP);
//	 map.addMapType(G_PHYSICAL_MAP);
//	 map.addMapType(G_SATELLITE_MAP);
//	 
//	 map.setMapType(G_PHYSICAL_MAP);
	 
//	 map.addControl(new GMapTypeControl());
	 
	 var geoXml = new GGeoXml("http://quaketables.quakesim.org/kml?uid=all&ov=0");   
	 map.addOverlay(geoXml);
	 map.setUIToDefault();

  </script>
</body>
