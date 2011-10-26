			<h:form id="faultsearchByNameform"
				rendered="#{MGBean.renderSearchByFaultNameForm}">
				<h:panelGrid id="FaultSearchName" columns="1" footerClass="subtitle"
					headerClass="subtitlebig" styleClass="medium"
					columnClasses="subtitle,medium">
					<h:outputFormat escape="false"
					    id="der11"
						value="<b>Search Fault DB by Fault Name</b><br><br>" />
					<h:panelGroup   id="der12">
						<h:panelGroup   id="der13">
							<h:outputText escape="false"   id="der14"
								value="Enter the name of the fault. The search will return partial matches." />
							<h:outputText escape="false" value="<br>"  id="der15"/>
						</h:panelGroup>

						<h:panelGroup  id="der16">
							<h:inputText id="Fault_Name" value="#{MGBean.forSearchStr}"
								required="true" />
							<h:message for="Fault_Name" showDetail="true" showSummary="true"
										   id="stuff63"
								errorStyle="color: red" />
							<h:commandButton id="cb6" value="Query"
								actionListener="#{MGBean.toggleFaultSearchByName}" />
						</h:panelGroup>
					</h:panelGroup>
				</h:panelGrid>
			</h:form>
