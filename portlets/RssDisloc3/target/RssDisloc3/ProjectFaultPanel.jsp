			<h:form id="UpdateSelectFaultsForm">
				<h:panelGrid columns="1" border="1"  id="stuff80"> 
					<h:panelGroup  id="lid2"> 
						<h:panelGrid  id="lid3" columns="1"> 
							<h:outputText escape="false" value="<b>Faults</b>"/> 
						</h:panelGrid> 
 
						<h:dataTable border="1" id="stuff81" 
							value="#{DislocBean2.myFaultEntryForProjectList}" var="myentry3"> 
							<h:column  id="lid4"> 
								<f:facet name="header"> 
									<h:outputText  id="lid5" escape="false" value="<b>Name</b>"> 
                           </h:outputText> 
								</f:facet> 
								<h:outputText value="#{myentry3.faultName}" 
								    id="stuff82"/> 
							</h:column> 
							<h:column  id="lid6"> 
								<f:facet name="header"> 
									<h:outputText escape="false" value="<b>Update</b>" /> 
								</f:facet> 
								<h:selectBooleanCheckbox value="#{myentry3.update}" 
                            id="stuff83" 
									onchange="selectOne(this.form,this)" 
									onclick="selectOne(this.form,this)" /> 
 
							</h:column> 
							<h:column  id="lid7"> 
								<f:facet name="header"> 
									<h:outputText escape="false" value="<b>Remove</b>" /> 
								</f:facet> 
								<h:selectBooleanCheckbox value="#{myentry3.delete}" 
								    id="lid8" 
									onchange="selectOne(this.form,this)" 
									onclick="selectOne(this.form,this)" /> 
							</h:column> 
 
						</h:dataTable> 
					</h:panelGroup> 
 
				</h:panelGrid> 
				<h:commandButton id="SelectFault4proj" value="UpdateFault" 
					actionListener="#{DislocBean2.toggleUpdateFaults}" /> 
 
			</h:form> 
