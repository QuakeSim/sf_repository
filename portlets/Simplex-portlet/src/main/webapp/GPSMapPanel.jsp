			<h:panelGroup id="lck093ks"
					rendered="#{SimplexBean.currentEditProjectForm.renderGPSStationMap}">
					 <h:form id="obsvGPSMap">
                <h:outputText id="clrlc093" escape="false"
					    value="<b>Select Stations from Map:</b> Select the stations that you want to use as observation points."/>
						 <h:panelGrid id="mapsAndCrap" columns="3" columnClasses="alignTop,alignTop">
						    <h:panelGroup id="mapncrap1">
						 <f:verbatim>
						 <div id="map" style="width: 600px; height: 400px"></div>
						 </f:verbatim>
                      </h:panelGroup>
                      <h:panelGroup id="mapncrap2">
							<h:panelGrid id="dfjdlkj" columns="2">
						 <h:outputText id="dkl34rtjf" value="Station:"/>
						 <h:inputText id="stationName" value="#{SimplexBean.gpsStationName}"/>
						 <h:outputText id="dkljr3rf" value="Latitude:"/>
						 <h:inputText id="stationLat" value="#{SimplexBean.gpsStationLat}"/>
						 <h:outputText id="dkljfer4" value="Longitude:"/>
						 <h:inputText id="stationLon" value="#{SimplexBean.gpsStationLon}"/>
             					 <h:outputText id="dkljr3dssrf" value="Ref Station?:"/>
						<h:selectBooleanCheckbox id="gpsRefStation23211s"
							value="#{SimplexBean.gpsRefStation}" />

						 <h:commandButton id="addGPSObsv" value="Add Station"
						 		actionListener="#{SimplexBean.toggleAddGPSObsvForProject}"/>
						 <h:commandButton id="closeMap" value="Close Map"
						 		actionListener="#{SimplexBean.toggleCloseMap}"/>
								</h:panelGrid>
                    <f:verbatim>
						 <div id="networksDiv">
						 </f:verbatim>
						   </h:panelGroup>
							</h:panelGrid>
					 </h:form>
			</h:panelGroup>
			
