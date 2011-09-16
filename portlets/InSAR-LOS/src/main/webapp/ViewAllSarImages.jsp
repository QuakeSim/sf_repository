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
<script type="text/javascript" src="http://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script src="script/sarselect.js"></script>			 
  
  <script>
	 var insarMapDiv=document.getElementById("InSAR-All-Map");
	 var latlng=new google.maps.LatLng(32.3,-118.0);
	 var myOpts={zoom:4, center: latlng, mapTypeId: google.maps.MapTypeId.ROADMAP};
	 var map=new google.maps.Map(insarMapDiv, myOpts);
	 
	 var kmlMapOpts={map:map, preserveViewport:true};
	 var insarKml = new google.maps.KmlLayer("http://quaketables.quakesim.org/kml?uid=all&ov=0",kmlMapOpts);

  </script>
</body>
