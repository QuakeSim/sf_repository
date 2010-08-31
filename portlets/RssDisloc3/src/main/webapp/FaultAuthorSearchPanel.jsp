			<h:form id="FaultAuthorSearchform" 
				rendered="#{DislocBean2.renderSearchByAuthorForm}"> 
				<h:panelGrid id="FaultAuthorSearch" columns="1" 
					footerClass="subtitle" headerClass="subtitlebig" 
					styleClass="medium" columnClasses="subtitle,medium"> 
					<h:outputFormat escape="false" 
						value="<b>Search Fault DB by Author</b><br><br>" /> 
					<h:panelGroup  id="pg3"> 
						<h:panelGroup  id="pg4"> 
							<h:outputText escape="false" 
							     id="stuff70" 
								value="Enter the last name of the principal author of the desired fault descriptions. The search will do partial matches." /> 
							<h:outputText  id="stuff71" escape="false" value="<br>" /> 
						</h:panelGroup> 
 
						<h:panelGroup  id="pg5"> 
							<h:inputText id="FaultAuthorForSearch" 
								value="#{DislocBean2.forSearchStr}" required="true" /> 
							<h:message for="FaultAuthorForSearch" showDetail="true" 
							    id="stuff72" 
								showSummary="true" errorStyle="color: red" /> 
							<h:commandButton value="Query" 
							    id="stuff73" 
								actionListener="#{DislocBean2.toggleFaultSearchByAuthor}" /> 
						</h:panelGroup> 
					</h:panelGroup> 
				</h:panelGrid> 
			</h:form> 
