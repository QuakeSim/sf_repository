<h:panelGroup id="ProjectMapPanelFrame"
				  rendered="#{SimplexBean.currentEditProjectForm.renderProjectMap}">
  <f:verbatim>
	 <script type="text/javascript" src="@host.base.url@@artifactId@/egeoxml.js"></script>
  </f:verbatim>	
  
  <h:form id="toggleProjectMapForm">
	 <h:inputHidden id="projectObsvKml" 
						 value="#{SimplexBean.obsvKmlUrl}"/>
	 <h:inputHidden id="projectFaultKml" 
						 value="#{SimplexBean.faultKmlUrl}"/>
	 <h:panelGrid id="projectMapPanelGrid" columns="1" border="0">
		<f:verbatim>
		  <div id="projectMapDiv" style="width: 800px; height: 600px;"></div>
		  <script type="text/javascript">
			 var projectMapDiv=new GMap2(document.getElementById("projectMapDiv"));
			 var projectFaultKml=document.getElementById("toggleProjectMapForm:projectFaultKml");
			 var projectObsvKml=document.getElementById("toggleProjectMapForm:projectObsvKml");
			 projectMapDiv.addMapType(G_HYBRID_MAP);
			 projectMapDiv.addMapType(G_PHYSICAL_MAP);
			 projectMapDiv.addMapType(G_SATELLITE_MAP);
			 
			 projectMapDiv.setMapType(G_PHYSICAL_MAP);
//			 projectMapDiv.setCenter(new GLatLng(35.0,-118.5),6);
			 projectMapDiv.addControl(new GSmallMapControl());
			 projectMapDiv.addControl(new GMapTypeControl());
			 
			 //Decorate the map with the KML.

			 var obsvOverlay=new GGeoXml(projectObsvKml.value, function(){
			    while(!obsvOverlay.hasLoaded()) {;}
			    projectMapDiv.addOverlay(obsvOverlay);
			 });

			 var faultOverlay=new GGeoXml(projectFaultKml.value,function(){
			    while(!faultOverlay.hasLoaded()) {;}
				 faultOverlay.gotoDefaultViewport(projectMapDiv);
			    projectMapDiv.addOverlay(faultOverlay);
			 });

			
		  </script>
		</f:verbatim>
	 </h:panelGrid>
  </h:form>  
</h:panelGroup>
