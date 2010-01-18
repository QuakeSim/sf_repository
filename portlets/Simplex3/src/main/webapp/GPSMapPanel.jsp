			<h:panelGroup id="lck093ks"
					rendered="#{SimplexBean.currentEditProjectForm.renderGPSStationMap}">
					 <h:form id="obsvGPSMap">
                <h:outputText id="clrlc093" escape="false"
					    value="<b>Select Stations from Map:</b> Select the stations that you want to use as observation points."/>
						 <h:panelGrid id="mapsAndCrap" columns="3" columnClasses="alignTop,alignTop">
						    <h:panelGroup id="mapncrap1">
						 <f:verbatim>
						 <div id="defaultmap" style="width: 600px; height: 400px"></div>

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
						 <h:outputText id="dkljr3dssra" value="Use search area:"/>
						 <h:selectBooleanCheckbox id="gpsRefStation23211b" onclick="toggleBorder()" 
							value="#{SimplexBean.searcharea}"/>
						 <h:outputText id="dkljr3dssraea" value="Selected GPS list:"/> 
						 <h:inputText id="GPSStationList" value="#{SimplexBean.selectedGpsStationName}"/>
						 <h:outputText id="dkljr3dssabfd" value="Selected GPS Number:"/> 
						 <h:inputText id="GPSStationNum" value=""/>
						 <h:inputHidden id="minlon" value="#{SimplexBean.selectedminlon}"/>
						 <h:inputHidden id="minlat" value="#{SimplexBean.selectedminlat}"/>
						 <h:inputHidden id="maxlon" value="#{SimplexBean.selectedmaxlon}"/>
						 <h:inputHidden id="maxlat" value="#{SimplexBean.selectedmaxlat}"/>
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

<f:verbatim>
<script language="JavaScript">
	initialize();





	
</script>
</f:verbatim>

					 </h:form>








			</h:panelGroup>