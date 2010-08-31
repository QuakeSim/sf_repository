<h:panelGroup id="lck093ks" rendered="#{DislocBean2.renderMap}">
	<h:form id="obsvGPSMap" style="overflow:auto;"> 
		<h:outputText id="clrlc093" escape="false" value="<b>Select Sites:</b>Click to choose scatter point."/>
		<h:panelGrid id="mapsAndCrap" columns="1" border="1" columnClasses="alignTop,alignTop">
			<h:panelGroup id="mapncrap1">
				<f:verbatim>
					<div id="map" style="width: 600px; height: 400px"></div>
					<script language="JavaScript">
						initialize();
					</script>
				</f:verbatim>	
			</h:panelGroup>
			<h:panelGroup id="mapncrap2">
				<h:panelGrid id="dfjdlkj" columns="2" rendered="#{empty DislocBean2.usesGridPoints || !DislocBean2.usesGridPoints}">
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
</h:panelGroup>
