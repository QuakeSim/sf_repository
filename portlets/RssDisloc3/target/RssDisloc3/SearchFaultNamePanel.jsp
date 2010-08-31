			<h:form id="faultsearchByNameform" 
				rendered="#{DislocBean2.renderSearchByFaultNameForm}"> 
				<h:panelGrid id="FaultSearchName" columns="1" footerClass="subtitle" 
					headerClass="subtitlebig" styleClass="medium" 
					columnClasses="subtitle,medium"> 
					<h:outputFormat escape="false" 
					     id="stuff44" 
						value="<b>Search Fault DB by Fault Name</b><br><br>" /> 
					<h:panelGroup   id="stuff45"> 
						<h:panelGroup   id="stuff46"> 
							<h:outputText escape="false" 
							     id="stuff47" 
								value="Enter the name of the fault. The search will return partial matches." /> 
							<h:outputText id="stuff48" escape="false" value="<br>" /> 
						</h:panelGroup> 
 
						<h:panelGroup  id="stuff49"> 
							<h:inputText id="Fault_Name" value="#{DislocBean2.forSearchStr}" 
								required="true" /> 
							<h:message for="Fault_Name" showDetail="true" showSummary="true" 
							     id="stuff50" 
								errorStyle="color: red" /> 
							<h:commandButton  id="stuff51" value="Query" 
								actionListener="#{DislocBean2.toggleFaultSearchByName}" /> 
						</h:panelGroup> 
					</h:panelGroup> 
				</h:panelGrid> 
			</h:form> 
