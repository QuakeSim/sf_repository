<h:panelGroup id="lck093ks" rendered="#{DislocBean2.renderMap}">
  <f:verbatim>
	 <fieldset><legend class="portlet-form-label">Select Sites</legend>
  </f:verbatim>	 
  <h:outputText id="clrlc093" 
					 rendered="#{DislocBean2.usesGridPoints}"
					 escape="false" value="You are currently using grid observation style. 
												  To use the map to pick individual points, change to
												  scatter point observation style."/>
  <h:form id="obsvGPSMap" 
			 rendered="#{empty DislocBean2.usesGridPoints || !DislocBean2.usesGridPoints}"
			 style="overflow:auto;"> 
	 
	 <h:outputText id="clrlc093" escape="false" value="Click to choose scatter point."/>
	 <h:panelGrid id="mapsAndCrap" columns="2" border="0" columnClasses="alignTop,alginTop">
		<h:panelGroup id="mapncrap1">
		  <f:verbatim>
			 <div id="map" style="width: 800px; height: 600px"></div>
			 <script type="text/javascript">
				initialize();
			 </script>
		  </f:verbatim>	
		</h:panelGroup>
		<h:panelGroup id="mapncrap2">
		  <h:panelGrid id="dfjdlkj" columns="2">
			 <h:outputText id="dkljr3rf" value="Latitude:"/>
			 <h:inputText id="stationLat" value="#{DislocBean2.gpsStationLat}"/>
			 <h:outputText id="dkljfer4" value="Longitude:"/>
			 <h:inputText id="stationLon" value="#{DislocBean2.gpsStationLon}"/>
			 <h:commandButton id="addGPSObsv" value="Add Observation Point"
									actionListener="#{DislocBean2.toggleAddPointObsvForProject}"/>
			 <h:commandButton id="closeMap" value="Close Map"
									actionListener="#{DislocBean2.toggleCloseMap}"/>
		  </h:panelGrid>
		</h:panelGroup>
	 </h:panelGrid>
  </h:form>
  <f:verbatim></fieldset></f:verbatim>
</h:panelGroup>

