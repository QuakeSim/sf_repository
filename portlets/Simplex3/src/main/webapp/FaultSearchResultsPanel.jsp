
			<h:panelGroup id="ere43dr342d" rendered="#{SimplexBean.currentEditProjectForm.renderAddFaultFromDBForm}">
				<h:form id="SelectFaultDBEntryForm">

				<b><h:outputText id="dbisdownklj" escape="false"
								  value="Error: Data base returned no response.  Contact portal administrator."
								  rendered="#{empty SimplexBean.currentEditProjectForm.myFaultDBEntryList}"/></b>

					<h:dataTable  id="dflelerkljk149"
					   rendered="#{!empty SimplexBean.currentEditProjectForm.myFaultDBEntryList}"
						value="#{SimplexBean.currentEditProjectForm.myFaultDBEntryList}"
						var="myentry1"
						binding="#{SimplexBean.currentEditProjectForm.myFaultDataTable}">

						<h:column>
							<f:facet name="header">
								<b><h:outputText id="dflelerkljk150" escape="false" value="FaultName" /></b>
							</f:facet>
							<h:selectOneRadio id="dflelerkljk151" layout="pageDirection"
								valueChangeListener="#{SimplexBean.currentEditProjectForm.handleFaultsRadioValueChange}"
								onchange="dataTableSelectOneRadio(this)"
								onclick="dataTableSelectOneRadio(this)">
								<f:selectItems value="#{myentry1.faultName}" />
							</h:selectOneRadio>
						</h:column>

						<h:column>
							<f:facet name="header">
								<b><h:outputText id="dflelerkljk152" escape="false" value="SegmentName" /></b>
							</f:facet>
							<h:outputText id="dflelerkljk153" value="#{myentry1.faultSegmentName}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<b><h:outputText id="dflelerkljk154" escape="false" value="Author1" /></b>
							</f:facet>
							<h:outputText id="dflelerkljk155" value="#{myentry1.faultAuthor}" />
						</h:column>

						<h:column>
							<f:facet name="header">
								<b><h:outputText id="dflelerkljk156" escape="false" value="Segment Coordinates" /></b>
							</f:facet>
							<h:outputText id="dflelerkljk157" value="#{myentry1.faultSegmentCoordinates}" />
						</h:column>
						<h:column>
							<f:facet name="header">
								<b><h:outputText id="dflelerkljk158" escape="false" value="Action" /></b>
							</f:facet>
							<h:commandLink  id="dflelerkljk159"
								actionListener="#{SimplexBean.currentEditProjectForm.handleFaultEntryEdit}">
								<h:outputText id="dflelerkljk160" value="Get" />
							</h:commandLink>
						</h:column>
					</h:dataTable>
					<h:commandButton rendered="#{!empty SimplexBean.currentEditProjectForm.myFaultDBEntryList}"
										  id="SelectFaultDBEntry" value="SelectFaultDBEntry"
										  actionListener="#{SimplexBean.currentEditProjectForm.toggleSelectFaultDBEntry}" />
				</h:form>
			</h:panelGroup>

