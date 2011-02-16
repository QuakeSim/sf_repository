			<h:panelGroup id="lck093ks"
					rendered="#{SimplexBean.currentEditProjectForm.renderGPSStationMap}">
					 <h:form id="obsvGPSMap">
                <h:outputText id="clrlc093" escape="false"
					    value="Select Stations from Map: Select the stations that you want to use as observation points."/>
						 <h:panelGrid id="mapsAndCrap" columns="3" columnClasses="alignTop,alignTop">
						    <h:panelGroup id="mapncrap1">
						 <f:verbatim>
						 <div id="defaultmap" style="width: 600px; height: 400px"></div>

						 </f:verbatim>
                      </h:panelGroup>
                      <h:panelGroup id="mapncrap2">
						 <h:panelGrid id="dfjdlkj" columns="2">
						  
						<h:panelGrid id="nploe" columns="2">
						  <h:outputText id="dkb2" value="stations.xml"/>
						  <h:selectBooleanCheckbox id="gpssource1" onchange="togglesource('box1')" value="#{SimplexBean.gpssource1}" />
						</h:panelGrid>

						<h:panelGrid id="npbbv" columns="2">
						  <h:outputText id="dkb3" value="perm.kml"/>
						  <h:selectBooleanCheckbox id="gpssource2" onchange="togglesource('box2')" value="#{SimplexBean.gpssource2}"/>  
						</h:panelGrid>

						 <h:outputText id="dkljrabd2" value="Station:"/> 
						 <h:inputText id="stationName" value="#{SimplexBean.gpsStationName}" style="text-align:center;width:45px" readonly="true"/>

						 <h:outputText id="dkljr3dssraea" value="Selected GPS list:"/> 
						 <h:inputText id="GPSStationList" value="#{SimplexBean.selectedGpsStationName}" readonly="true"/>
						 <h:outputText id="dkljr3dssabfd" value="Selected GPS Number:"/> 
						 <h:inputText id="GPSStationNum" value="" readonly="true"/>


						  <h:commandButton id="dummysubmit" value="Get values"
						 		actionListener="#{SimplexBean.getvalues}"/>

						<h:panelGrid id="npbas12" columns="2">												  
						 <h:inputHidden id="stationLat" value="#{SimplexBean.gpsStationLat}"/>
						 <h:inputHidden id="stationLon" value="#{SimplexBean.gpsStationLon}"/>
						</h:panelGrid>
						

						</h:panelGrid>

						<h:panelGrid id="mnauw1" columns="1" border="0" cellpadding="0" cellspacing="0">
						  <h:dataTable  border="1" cellpadding="0" cellspacing="0" id="dflezzz277" headerClass="componentstableh2" columnClasses="componentstablec"
								    rendered="#{!empty SimplexBean.mycandidateObservationsForProjectList}" value="#{SimplexBean.mycandidateObservationsForProjectList}" var="myentry5">

							    <h:column>
							      <f:facet name="header">
								<h:outputText id="bawee21" value="Station" />
							      </f:facet>
							      <h:panelGrid id="gplgmpp2" columns="1" cellpadding="0" cellspacing="0" styleClass="centered">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="bawee22" style="text-align:right;width:60px" 
								  value="#{myentry5.stationName}" />
							      </h:panelGrid>
							    </h:column>

							    <h:column>
							      <f:facet name="header">
								<h:outputText id="bawee23" value="Sources" />
							      </f:facet>
							      <h:panelGrid id="gplgmpp3" columns="1" cellpadding="0" cellspacing="0" styleClass="centered">
								<f:facet name="header">								
								</f:facet>
							      <h:selectOneListbox id="sselectl" value="#{myentry5.selectedSource}">
							      <f:selectItems value="#{myentry5.stationSources}" />
							      </h:selectOneListbox> 
							      </h:panelGrid>
							    </h:column>


						      </h:dataTable>
						</h:panelGrid>


						<h:panelGrid id="nploebba" columns="2">

             					 <h:outputText id="dkljr3dssrf" value="Ref Station?:"/>
						 <h:selectBooleanCheckbox id="gpsRefStation23211s"
							value="#{SimplexBean.gpsRefStation}" />
						 <h:outputText id="dkljr3dssra" value="Use search area:"/>
						 <h:selectBooleanCheckbox id="gpsRefStation23211b" onclick="toggleBorder()" 
							value="#{SimplexBean.searcharea}"/>
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
						 <div id="networksDiv"></div>
						 </f:verbatim>
						   </h:panelGroup>
							</h:panelGrid>

<f:verbatim>
<script type="text/javascript">

	initialize();

function toggleoff(form){
form.checked = false;
}

function stationsourcechange(){

alert("aa");
document.getElementById("obsvGPSMap:dummysubmit").click();
}

function togglesource(s){

var box1 = document.getElementById("obsvGPSMap:gpssource1");
var box2 = document.getElementById("obsvGPSMap:gpssource2");

var rssnewsize=<%=rssnewsize%>
var permsize=<%=permsize%>

if (s == "box1") {

if (box1.checked == false) {
for (var nA = 0 ; nA < rssnewsize ; nA++)
  map.removeOverlay(marker[nA]);
}

if (box1.checked == true) {
for (var nA = 0 ; nA < rssnewsize ; nA++)
  map.addOverlay(marker[nA]);
}
}

if (s == "box2") {

if (box2.checked == false) {
for (var nA = 0 ; nA < permsize ; nA++)
  map.removeOverlay(marker[nA+rssnewsize]);
}

if (box2.checked == true) {
for (var nA = 0 ; nA < permsize ; nA++)
  map.addOverlay(marker[nA+rssnewsize]);
}
}


}
	
</script>
</f:verbatim>

					 </h:form>








			</h:panelGroup>