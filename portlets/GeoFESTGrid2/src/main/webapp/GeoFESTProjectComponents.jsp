

		<h:panelGroup   id="ldop10">
			<h:form id="UpdateSelectFaultsForm"
				rendered="#{!empty MGBean.myFaultEntryForProjectList}">
				<h:panelGrid columns="1" border="1"  id="ldop11">
					<h:panelGroup  id="ldop12">
						<h:panelGrid columns="1"  id="ldop13">
							<h:outputText   id="ldop14" escape="false" value="<b>Faults</b>"/>
						</h:panelGrid>

						<h:dataTable border="1"  id="ldop15"
							value="#{MGBean.myFaultEntryForProjectList}" var="myentry3">
							<h:column  id="ldop16">
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Name</b>">
                           </h:outputText>
								</f:facet>
								<h:outputText   id="ldop17" value="#{myentry3.faultName}" />
							</h:column>
							<h:column  id="ldop18">
								<f:facet name="header">
									<h:outputText   id="ldop19" escape="false" value="<b>View</b>" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry3.view}"
								     id="cjd1"
									onchange="selectOne(this.form,this)"
									onclick="selectOne(this.form,this)" />

							</h:column>
							<h:column  id="cjd2">
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Remove</b>" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry3.delete}"
								     id="cjd3"
									onchange="selectOne(this.form,this)"
									onclick="selectOne(this.form,this)" />
							</h:column>

						</h:dataTable>
					</h:panelGroup>

				</h:panelGrid>
				<h:commandButton id="SelectFault4proj" value="UpdateFault"
					actionListener="#{MGBean.toggleUpdateFaultProjectEntry}" />

			</h:form>
			<h:form id="UpdateSelectLayersForm"
				rendered="#{!empty MGBean.myLayerEntryForProjectList}">
				<h:panelGrid id="updateSelectpg" columns="1" border="1">
					<h:panelGroup id="pg21">
						<h:panelGrid id="pg22" columns="1">
							<h:outputText   id="cjd5" escape="false" value="<b>Layers</b>">
                     </h:outputText>
						</h:panelGrid>

						<h:dataTable border="1"
										 id="pg23"
							value="#{MGBean.myLayerEntryForProjectList}" var="myentry4">
							<h:column  id="cjd6">
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Name</b>" />
								</f:facet>
								<h:outputText value="#{myentry4.layerName}" />
							</h:column>
							<h:column  id="cjd7">
								<f:facet name="header">
									<h:outputText escape="false" value="<b>View</b>" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry4.view}"
										id="pg24"
									onchange="selectOne(this.form,this)"
									onclick="selectOne(this.form,this)" />

							</h:column>
							<h:column  id="cjd8">
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Remove</b>" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry4.delete}"
								   id="pg25"
									onchange="selectOne(this.form,this)"
									onclick="selectOne(this.form,this)" />
							</h:column>

						</h:dataTable>
					</h:panelGroup>

				</h:panelGrid>
				<h:commandButton id="SelectFault4proj" value="UpdateLayer"
					actionListener="#{MGBean.toggleUpdateLayerProjectEntry}" />

			</h:form>
		</h:panelGroup>

