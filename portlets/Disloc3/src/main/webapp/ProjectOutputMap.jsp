<h:form id="DislocDisplayProjectResults" rendered="#{DislocBean2.renderProjectOutputMap}">
  <h:inputHidden id="myDislocOutput" value="#{DislocBean2.myKmlUrl}"/>
  <h:inputHidden id="myDislocInsar" value="#{DislocBean2.insarKmlUrl}"/>
  <f:verbatim>
	 <fieldset style="width:920px"><legend class="portlet-form-label">Simulation Output Plots</legend>
  </f:verbatim>
  <h:panelGroup id="emptyDislocProjectOutputPanelGroup"
					 rendered="#{(DislocBean2.insarKmlUrl==null) 
								  || (DislocBean2.myKmlUrl==null)}">
	 <f:verbatim>
		No simulation outputs to plot.  You must run Disloc first.
	 </f:verbatim>
  </h:panelGroup>
  <h:panelGroup id="projectOutputPanelGroup" 
					 rendered="#{(DislocBean2.insarKmlUrl!=null) 
								  || (DislocBean2.myKmlUrl!=null)}">
	 <h:panelGrid id="disloc3OuputDiv" columns="2" columnClasses="alignTop,alignTop">
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
				<tr><td>
				  <input type="checkbox" 
							id="insarKmlCheckbox" 
							value="InSAR KML" 
							onclick="toggleKmlDisplay(this)"
							checked/>InSAR KML
				  <input type="button" value="Make Transparent" class="b_opacity"/>
				</td></tr>
			 </table>
			 </f:verbatim>
		  <h:panelGrid id="outputLinksPanelGroup" columns="1">
			 <h:outputText id="outputLinkLable" 
								style="font-weight:bold" 
								value="Click links to download output KML"/>
			 <h:outputLink id="myDislocOutputLink" target="_blank" value="#{DislocBean2.myKmlUrl}">
				<h:outputText id="dislocOutputText" value="Disloc Output KML"/>
			 </h:outputLink>
			 <h:outputLink id="myDislocInsarLink" target="_blank" value="#{DislocBean2.insarKmlUrl}">
				<h:outputText id="dislocInSArText" value="Disloc InSAR KML"/>
			 </h:outputLink>
			 <h:outputLink id="myDislocInputLink" target="_blank" value="#{DislocBean2.dislocInputUrl}">
				<h:outputText id="dislocInputText" value="Disloc Input File"/>
			 </h:outputLink>
			 <h:outputLink id="myDislocOutputFileUrl" target="_blank" value="#{DislocBean2.dislocOutputUrl}">
				<h:outputText id="dislocOutputFileText" value="Disloc Output File"/>
			 </h:outputLink>
		  </h:panelGrid>
		</h:panelGrid>
	 </h:panelGrid>
	 <f:verbatim>
		<script type="text/javascript">
		  //Fade the png
		  $(document).ready(function(){
		    $(".b_opacity").click(function() {
			    $("#projectResultsDiv").find("img[src*='anonymousProject']").fadeTo("fast","0.4");			 
			  });
			  });
		  var projectResultsDiv=new GMap2(document.getElementById("projectResultsDiv"));
		  var projectFaultKml="@host.base.url@@artifactId@/QuakeTables_CGS_2002.kml";
		  var projectDislocOutput=document.getElementById("DislocDisplayProjectResults:myDislocOutput");
		  var projectDislocInsar=document.getElementById("DislocDisplayProjectResults:myDislocInsar");
		  
		  projectResultsDiv.addMapType(G_HYBRID_MAP);
		  projectResultsDiv.addMapType(G_PHYSICAL_MAP);
		  projectResultsDiv.addMapType(G_SATELLITE_MAP);
		  
		  projectResultsDiv.setMapType(G_PHYSICAL_MAP);
		  
		  projectResultsDiv.addControl(new GSmallMapControl());
		  projectResultsDiv.addControl(new GMapTypeControl());
		  projectResultsDiv.setCenter(new GLatLng(37.0, -119),6);
		  
		  //Decorate the map with the KML files: faults, arrows, and insar.
		  var faultOverlay=new GGeoXml(projectFaultKml);
		  projectResultsDiv.addOverlay(faultOverlay);
		  
		  //Note: use .value here since we are getting values from HTML elements.
		  var insarOverlay=new GGeoXml(projectDislocInsar.value);
		  projectResultsDiv.addOverlay(insarOverlay);
		  
		  var arrowOverlay=new GGeoXml(projectDislocOutput.value);
		  projectResultsDiv.addOverlay(arrowOverlay);
		  
		  //Set the viewport. 
		  insarOverlay.gotoDefaultViewport(projectResultsDiv);
		  
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
		    else if(displaySource.id=="insarKmlCheckbox" && displaySource.checked) {
			     projectResultsDiv.addOverlay(insarOverlay);
			 }
		    else if(displaySource.id=="insarKmlCheckbox" && !displaySource.checked) {
			     projectResultsDiv.removeOverlay(insarOverlay);
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