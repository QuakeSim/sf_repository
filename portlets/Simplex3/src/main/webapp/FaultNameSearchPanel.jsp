
			<h:panelGroup id="erea3412" rendered="#{SimplexBean.currentEditProjectForm.renderSearchByFaultNameForm}">
				<h:form id="faultsearchByNameform">
					<h:panelGrid id="FaultSearchName" columns="1"
						footerClass="subtitle" headerClass="subtitlebig"
						styleClass="medium" columnClasses="subtitle,medium">
						<b><h:outputFormat  id="dfleleawjk116" escape="false"
							value="Search Fault DB by Fault Name" /></b><br><br>
						<h:panelGroup id="dfleaagljk117">
							<h:panelGroup id="dflbrkljk118">
								<h:outputText escape="false"  id="dflelerkljk119"
									value="Enter the name of the fault. The search will return partial matches." />
								<h:outputText escape="false" value="" /><br>
							</h:panelGroup>

							<h:panelGroup id="dflelbljk120">
								<h:inputText id="Fault_Name"
									value="#{SimplexBean.currentEditProjectForm.forSearchStr}"
									required="true" />
								<h:message id="dflelealjk121" for="Fault_Name" showDetail="true" showSummary="true"
									errorStyle="color: red" />
								<h:commandButton id="dfleleeao122" value="Query"
									actionListener="#{SimplexBean.currentEditProjectForm.toggleFaultSearchByName}" />
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGrid>
				</h:form>
			</h:panelGroup>
