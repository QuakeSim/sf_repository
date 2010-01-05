		<h:panelGrid id="ProjectComponentList" columns="1" border="0">
			<h:panelGroup id="dflelerkljk161">
				<h:form id="UpdateSelectFaultsForm"
					rendered="#{!empty SimplexBean.myFaultEntryForProjectList}">
					<h:panelGrid id="dflelerkljk162" columns="1" border="1">
						<h:panelGroup id="dflelerkljk163">
								<h:outputFormat id="dflelerkljk165" escape="false"
									value="<b>Fault Components</b>">
								</h:outputFormat>

							<h:dataTable border="1"
											  id="dflelerkljk166"
											  value="#{SimplexBean.myFaultEntryForProjectList}" var="myentry3">
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk167" escape="false" value="<b>Name</b>" />
									</f:facet>
									<h:outputText id="dflelerkljk168" value="#{myentry3.faultName}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk169" escape="false" value="<b>View</b>" />
									</f:facet>
									<h:selectBooleanCheckbox value="#{myentry3.view}"
																	  id="dflelerkljk170"
																	  onchange="selectOne(this.form,this)"
																	  onclick="selectOne(this.form,this)" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk171" escape="false" value="<b>Remove</b>" />
									</f:facet>
									<h:selectBooleanCheckbox id="dflelerkljk172" value="#{myentry3.delete}"/>
								</h:column>
							</h:dataTable>
						</h:panelGroup>
					</h:panelGrid>
					<h:commandButton id="SelectFault4proj" value="UpdateFault"
						actionListener="#{SimplexBean.toggleUpdateFaultProjectEntry}" />
				</h:form>

				<h:form id="UpdateSelectObservationForm"
					rendered="#{!empty SimplexBean.myObservationEntryForProjectList}">
						<h:panelGroup id="dflelerkljk174">
						  <h:panelGrid id="obsvpanelgrid" columns="1" border="1">
								<h:outputFormat escape="false" id="dflelerkljk176"
									value="<b>Observation Components</b>">
								</h:outputFormat>
								<h:commandButton id="viewSimplexObsv" value="Display/Hide"
									actionListener="#{SimplexBean.currentEditProjectForm.toggleShowObsvEntries}"/>

							<h:dataTable border="1" id="dflelerkljk177"
							   rendered="#{SimplexBean.currentEditProjectForm.renderObsvEntries}"
								value="#{SimplexBean.myObservationEntryForProjectList}"
								var="myentry4">
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk178" escape="false" value="<b>Name</b>" />
									</f:facet>
									<h:outputText id="dflelerkljk179" value="#{myentry4.observationName}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk180" escape="false" value="<b>View</b>" />
									</f:facet>
									<h:selectBooleanCheckbox value="#{myentry4.view}"
																	  id="dflelerkljk181"
																	  onchange="selectOne(this.form,this)"
																	  onclick="selectOne(this.form,this)" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk182" escape="false" value="<b>Remove</b>" />
									</f:facet>
									<h:selectBooleanCheckbox  id="dflelerkljk183" value="#{myentry4.delete}"
										onchange="selectOne(this.form,this)"
										onclick="selectOne(this.form,this)" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflel332" escape="false" value="<b>Ref Site</b>" />
									</f:facet>
									<h:outputText id="dfl33rejk183" value="#{myentry4.refSite}"/>
								</h:column>
							</h:dataTable>
						</h:panelGrid>
						</h:panelGroup>
					<h:commandButton id="SelectObservation4proj"
					   rendered="#{SimplexBean.currentEditProjectForm.renderObsvEntries}"
						value="Update Observation"
						actionListener="#{SimplexBean.toggleUpdateObservationProjectEntry}" />
				</h:form>
			</h:panelGroup>
		</h:panelGrid>
