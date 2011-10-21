<h:panelGroup id="SAR-Simplex-SelectionMap"
				  rendered="#{SimplexBean.currentEditProjectForm.renderSARSelectionMap}">
  <f:verbatim><fieldset><legend><b>Interferogram Map Selection</b></legend></f:verbatim>
  <f:verbatim>Click on the map to select the region you want to use.</f:verbatim>
  <h:form id="Simplex3SarSelectForm">
		<h:inputHidden id="Simplex3SarSWLat" value="#{SimplexBean.sarSWLat}"/>
		<h:inputHidden id="Simplex3SarSWLon" value="#{SimplexBean.sarSWLon}"/>
		<h:inputHidden id="Simplex3SarSELat" value="#{SimplexBean.sarSELat}"/>
		<h:inputHidden id="Simplex3SarSELon" value="#{SimplexBean.sarSELon}"/>
		<h:inputHidden id="Simplex3SarNELat" value="#{SimplexBean.sarNELat}"/>
		<h:inputHidden id="Simplex3SarNELon" value="#{SimplexBean.sarNELon}"/>
		<h:inputHidden id="Simplex3SarNWLat" value="#{SimplexBean.sarNWLat}"/>
		<h:inputHidden id="Simplex3SarNWLon" value="#{SimplexBean.sarNWLon}"/>
		<h:inputHidden id="Simplex3SarParamM" value="#{SimplexBean.sarParamM}"/>
		<h:inputHidden id="Simplex3SarParamN" value="#{SimplexBean.sarParamN}"/>
		<h:inputHidden id="Simplex3SarUID" value="#{SimplexBean.sarImageUid}"/>
		
	 <h:panelGrid id="InSAR-View-All" columns="2" columnClasses="alignTop,alignTop">
		
		<f:verbatim>
		  <div id="InSAR-All-Map" style="width: 550px; height: 400px;"></div>
		</f:verbatim>
		<f:verbatim>
		  <div id="dynatable"></div>
		</f:verbatim>
		<h:commandButton id="SarSelectGetSimplexInput" 
							  value="Import Image Points"
							  actionListener="#{SimplexBean.toggleSetObsvFromSarImage}"/>
	 </h:panelGrid>
  </h:form>
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