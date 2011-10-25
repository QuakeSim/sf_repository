<h:form id="SimplexDisplayProjectResults" 
		  rendered="#{SimplexBean.currentEditProjectForm.renderProjectOutputMap}">
  <f:verbatim>
	 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" 
				type="text/javascript"></script>
  </f:verbatim>
  <%--
  <h:inputHidden id="mySimplexOutput" value="#{SimplexBean.myKmlUrl}"/>
  --%>
  <f:verbatim>
	 <fieldset><legend class="portlet-form-label">Simulation Output Plots</legend>
  </f:verbatim>
  <h:panelGroup id="emptySimplexProjectOutputPanelGroup"
					 rendered="#{(SimplexBean.myKmlUrl==null)}"> 
	 <f:verbatim>
		No simulation outputs to plot.  You must run Simplex first.
	 </f:verbatim>
  </h:panelGroup>
  <h:panelGroup id="projectOutputPanelGroup" 
					 rendered="#{(SimplexBean.myKmlUrl!=null)}"> 
	 <h:panelGrid id="simplex3OuputDiv" columns="2" columnClasses="alignTop,alignTop">
		<f:verbatim>
		  <div id="projectResultsDiv" style="width: 600px; height: 400px;"></div>
		</f:verbatim>
		<h:panelGrid id="outputDisplayControllerPG" columns="1">
		  <f:verbatim>
			 <table>
				<tr><td>
				  <b>Click the checkbox to toggle KML display</b>
				</td></tr>
				<tr><td>
				  <input type="checkbox" 
							id="faultKmlCheckbox" 
							value="Fault KML" 
							onclick="toggleKmlDisplay(this)"
							checked/>Fault KML
				</td></tr>
				<tr><td>
				  <input type="checkbox" 
							id="arrowsKmlCheckbox" 
							value="Vector Output KML" 
							onclick="toggleKmlDisplay(this)"
							checked/>Vector Output KML
				</td></tr>
			 </table>
			 </f:verbatim>
		  <h:panelGrid id="outputLinksPanelGroup" columns="1">
			 <h:outputText id="outputLinkLable" 
								style="font-weight:bold" 
								value="Click links to download output KML"/>
			 <h:outputLink id="mySimplexOutputLink" target="_blank" value="#{SimplexBean.myKmlUrl}">
				<h:outputText id="simplexOutputText" value="Simplex Output KML"/>
			 </h:outputLink>
			 <h:outputLink id="mySimplexInputLink" target="_blank" value="#{SimplexBean.simplexInputUrl}">
				<h:outputText id="simplexInputText" value="Simplex Input File"/>
			 </h:outputLink>
			 <h:outputLink id="mySimplexOutputFileUrl" target="_blank" value="#{SimplexBean.simplexOutputUrl}">
				<h:outputText id="simplexOutputFileText" value="Simplex Output File"/>
			 </h:outputLink>
		  </h:panelGrid>
		</h:panelGrid>
	 </h:panelGrid>
	 <f:verbatim>
		<script type="text/javascript">
		  //Fade the png
		  $(document).ready(function(){
		    $(".faderButton").click(function() {
			    $("#projectResultsDiv").find("img[src*='output']").fadeTo("fast","0.75");			 
			  });
		    $(".resetButton").click(function() {
			    $("#projectResultsDiv").find("img[src*='output']").fadeTo("fast","1.0");			 
			  });
			  });
		  var projectResultsDiv=new GMap2(document.getElementById("projectResultsDiv"));
		  //This is a not very good workaround to just fade the insar overlay.
		  var projectFaultKml="@host.base.url@@artifactId@/QuakeTables_CGS_2002.kml";
		  var projectSimplexOutput=document.getElementById("SimplexDisplayProjectResults:mySimplexOutputLink");
		  projectResultsDiv.addMapType(G_HYBRID_MAP);
		  projectResultsDiv.addMapType(G_PHYSICAL_MAP);
		  projectResultsDiv.addMapType(G_SATELLITE_MAP);
		  
		  projectResultsDiv.setMapType(G_PHYSICAL_MAP);
		  
		  projectResultsDiv.addControl(new GSmallMapControl());
		  projectResultsDiv.addControl(new GMapTypeControl());
		  projectResultsDiv.setCenter(new GLatLng(37.0, -119),6);
		  
		  //Decorate the map with the KML files: faults and arrows.
		  var faultOverlay=new GGeoXml(projectFaultKml);
		  projectResultsDiv.addOverlay(faultOverlay);
		  
		  var arrowOverlay=new GGeoXml(projectSimplexOutput);
		  projectResultsDiv.addOverlay(arrowOverlay);
		  
		  //Set the viewport. 
		  arrowOverlay.gotoDefaultViewport(projectResultsDiv);
		  
		  //This function is used to toggle the overlay display on and off.
		  function toggleKmlDisplay(displaySource) {
		    if(displaySource.id=="faultKmlCheckbox" && displaySource.checked) {
			     projectResultsDiv.addOverlay(faultOverlay);
			 }
		    else if(displaySource.id=="faultKmlCheckbox" && !displaySource.checked) {
			     projectResultsDiv.removeOverlay(faultOverlay);
			 }
		    else if(displaySource.id=="arrowsKmlCheckbox" && displaySource.checked) {
			     projectResultsDiv.addOverlay(arrowOverlay);
			 }
		    else if(displaySource.id=="arrowsKmlCheckbox" && !displaySource.checked) {
			     projectResultsDiv.removeOverlay(arrowOverlay);
			 }
			 else {
			    alert("Error encountered:"+displaySource.id);
			 }
		  }

		</script>
	 </f:verbatim>
  </h:panelGroup>
  <f:verbatim></fieldset></f:verbatim>			 
</h:form>
