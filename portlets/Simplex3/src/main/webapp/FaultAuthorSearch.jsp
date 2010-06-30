         <h:panelGroup id="doierl323" rendered="#{SimplexBean.currentEditProjectForm.renderSearchByAuthorForm}">
				<h:form id="FaultAuthorSearchform">
					
					<h:panelGrid id="FaultAuthorSearch" columns="1"
						footerClass="subtitle" headerClass="subtitlebig"
						styleClass="medium" columnClasses="subtitle,medium">
						<b><h:outputFormat id="dflelerkljk141" escape="false"
							value="Search Fault DB by Author"/></b><br><br>
						<h:panelGroup id="dflelerkljk142">
							<h:panelGroup id="dflelerkljk143">
								<h:outputText escape="false" id="dflelerkljk144"
									value="Enter the last name of the principal author of the desired fault descriptions. The search will do partial matches." />
								<h:outputText id="dflelerkljk145" escape="false" value=" " /><br>
							</h:panelGroup>

							<h:panelGroup id="dflelerkljk146">
								<h:inputText id="FaultAuthorForSearch"
									value="#{SimplexBean.currentEditProjectForm.forSearchStr}"
									required="true" />
								<h:message id="dflelerkljk147" for="FaultAuthorForSearch" showDetail="true"
									showSummary="true" errorStyle="color: red" />
								<h:commandButton id="dflelerkljk148" value="Query"
									actionListener="#{SimplexBean.currentEditProjectForm.toggleFaultSearchByAuthor}" />
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGrid>
				</h:form>
			</h:panelGroup>
