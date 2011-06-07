<h:form id="UpdateSelectedParamsForm" 
		  rendered="#{!empty DislocBean2.myObsvEntryForProjectList}"> 
  <h:panelGrid columns="1" border="1"  id="stuff84"> 
	 <h:panelGroup  id="lid9"> 
		<h:panelGrid  id="stuff85" columns="1"> 
		  <h:outputText  id="stuff86" 
							  escape="false" value="<b>Observations</b>"/> 
		</h:panelGrid> 
		
		<h:dataTable border="1"  id="stuff87" 
						 value="#{DislocBean2.myObsvEntryForProjectList}" var="myentry4"> 
		  <h:column  id="lid110"> 
			 <f:facet name="header"> 
				<h:outputText  id="lkj1" escape="false" value="<b>Name</b>"> 
			 </h:outputText> 
		  </f:facet> 
		  <h:outputText  id="lkj2" value="Observations" /> 
		</h:column> 
		<h:column  id="lkj11"> 
		  <f:facet name="header"> 
			 <h:outputText  id="lkj3" escape="false" value="<b>View</b>" /> 
		  </f:facet> 
		  <h:selectBooleanCheckbox value="#{myentry4.view}" 
											id="stuff88" 
											onchange="selectOne(this.form,this)" 
									onclick="selectOne(this.form,this)" /> 
		  
		</h:column> 
		<h:column  id="lkj4"> 
		  <f:facet name="header"> 
			 <h:outputText escape="false" value="<b>Remove</b>" /> 
		  </f:facet> 
		  <h:selectBooleanCheckbox value="#{myentry4.delete}" 
											onchange="selectOne(this.form,this)" 
											id="lkj5" 
											onclick="selectOne(this.form,this)" /> 
		</h:column> 
		
	 </h:dataTable> 
  </h:panelGroup> 
 
</h:panelGrid> 
<h:commandButton id="updateObsv" value="Update Observations" 
					actionListener="#{DislocBean2.toggleUpdateProjectObservations}" /> 

</h:form>
