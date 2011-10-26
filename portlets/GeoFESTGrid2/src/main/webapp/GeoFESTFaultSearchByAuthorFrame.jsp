			<h:form id="FaultAuthorSearchform"
				rendered="#{MGBean.renderSearchByAuthorForm}">
				<h:panelGrid id="FaultAuthorSearch" columns="1"
					footerClass="subtitle" headerClass="subtitlebig"
					styleClass="medium" columnClasses="subtitle,medium">
					<h:outputFormat escape="false"   id="erd9"
						value="<b>Search Fault DB by Author</b><br><br>" />
					<h:panelGroup   id="erd10">
						<h:panelGroup  id="erd11">
							<h:outputText escape="false"  id="erd12"
								value="Enter the last name of the principal author of the desired fault descriptions. The search will do partial matches." />
							<h:outputText escape="false" value="<br>"   id="erd13"/>
						</h:panelGroup>

						<h:panelGroup  id="erd14">
							<h:inputText id="FaultAuthorForSearch"
								value="#{MGBean.forSearchStr}" required="true" />
							<h:message for="FaultAuthorForSearch" showDetail="true"
										   id="stuff74"
								showSummary="true" errorStyle="color: red" />
							<h:commandButton id="cb7" value="Query"
								actionListener="#{MGBean.toggleFaultSearchByAuthor}" />
						</h:panelGroup>
					</h:panelGroup>
				</h:panelGrid>
			</h:form>
