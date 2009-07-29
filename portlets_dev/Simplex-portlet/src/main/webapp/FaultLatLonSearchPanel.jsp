			<h:panelGroup id="erre3454" rendered="#{SimplexBean.currentEditProjectForm.renderSearchByLatLonForm}">
				<h:form id="faultlatlonsearchform">

					<h:panelGrid id="FaultLatLonSearch" columns="1"
						footerClass="subtitle" headerClass="subtitlebig"
						styleClass="medium" columnClasses="subtitle,medium">
						<h:outputFormat escape="false" id="dflelerkljk123"
							value="<b>Search Fault DB by Bounding Latitude and Longitude</b><br><br>" />
						<h:outputFormat escape="false" id="dflelerkljk124"
							value="Enter the starting and ending latitude and longitude values (in decimal degrees) of the search bounding box. All faults completely within the bounding box will be returned.<br><br>" />


						<h:panelGrid columns="2" border="0" id="dflelerkljk125">
							<h:outputText id="dflelerkljk126" value="Starting Latitude: " />
							<h:panelGroup id="dflelerkljk127">
								<h:inputText id="StartingLatitude"
									value="#{SimplexBean.currentEditProjectForm.faultLatStart}"
									required="true" />
								<h:message id="dflelerkljk128" for="StartingLatitude" showDetail="true"
									showSummary="true" errorStyle="color: red" />
							</h:panelGroup>

							<h:outputText id="dflelerkljk129" value="Ending Latitude: " />
							<h:panelGroup id="dflelerkljk131">
								<h:inputText id="EndingLatitude"
									value="#{SimplexBean.currentEditProjectForm.faultLatEnd}"
									required="true" />
								<h:message id="dflelerkljk132" for="EndingLatitude" showDetail="true"
									showSummary="true" errorStyle="color: red" />
							</h:panelGroup>
							<h:outputText id="dflelerkljk133" value="Starting Longitude: " />
							<h:panelGroup id="dflelerkljk134">
								<h:inputText id="StartingLongitude"
									value="#{SimplexBean.currentEditProjectForm.faultLonStart}"
									required="true" />
								<h:message id="dflelerkljk135" for="StartingLongitude" showDetail="true"
									showSummary="true" errorStyle="color: red" />
							</h:panelGroup>
							<h:outputText id="dflelerkljk136" value="Ending Longitude: " />
							<h:panelGroup id="dflelerkljk137">
								<h:inputText id="EndingLongitude"
									value="#{SimplexBean.currentEditProjectForm.faultLonEnd}"
									required="true" />
								<h:message id="dflelerkljk138" for="EndingLongitude" showDetail="true"
									showSummary="true" errorStyle="color: red" />
							</h:panelGroup>
							<h:panelGroup id="dflelerkljk139">
								<h:commandButton id="dflelerkljk140" value="Query"
									actionListener="#{SimplexBean.currentEditProjectForm.toggleFaultSearchByLonLat}" />
							</h:panelGroup>
						</h:panelGrid>

					</h:panelGrid>
				</h:form>
         </h:panelGroup>
