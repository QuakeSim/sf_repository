<h:panelGrid id="DashboradPanelgrid"
				 columnClasses="alignTop"
				 columns="1" 
				 border="0">
  
  <h:panelGroup id="pg1"> 
	 <h:form id="selectprojDisloc">
		<h:panelGrid id="projectMetadataDisloc"
						 border="1"
						 cellspacing="3"
						 cellpadding="3"
						 columns="3">
		  <h:outputText id="stuff1" 
							 escape="false" 
							 value="<b>Project Name:</b> #{DislocBean2.projectName}" /> 
		  <h:outputText id="stuffe3io4" 
							 escape="false" 
							 value="<b>Project Origin (lat/lon):</b> (#{DislocBean2.currentParams.originLat}, #{DislocBean2.currentParams.originLon})" /> 
		  <h:outputText id="stufw3f1" 
							 escape="false" 
							 value="<b>Observation Style:</b> #{DislocBean2.currentParams.observationPointStyle}" /> 
		</h:panelGrid>
		<h:selectOneMenu id="DislocSelectionMenu" 
							  title="Use this drop down to add faults and observation points to 
										your project."
							  value="#{DislocBean2.projectSelectionCode}"> 
		  <f:selectItem id="item02132"
							 itemLabel="Interactively select faults from a map." 
							 itemValue="ShowFaultMap" />		  
		  <f:selectItem id="item021"
							 itemLabel="Pick observation points on an interactive map."
							 itemValue="ShowMap" />

		  <f:selectItem id="item2" 
							 itemLabel="Specify geometry for a new fault segment." 
							 itemValue="CreateNewFault" /> 

		  <f:selectItem id="item0w3" 
							 itemLabel="Choose between grid and scatter point observation styles." 
							 itemValue="ChooseObsvStyleForm"/> 
		  
		  <f:selectItem id="item1" 
							 itemLabel="Verify and update project information." 
							 itemValue="CreateObservationGrid" /> 
		  		  
		</h:selectOneMenu> 
		<h:commandButton id="button1" 
							  value="Make Selection" 
							  actionListener="#{DislocBean2.toggleProjectSelection}"> 
		</h:commandButton> 
	 </h:form> 
	 
	 <h:form id="RunDisloc" 
				rendered="#{!(empty DislocBean2.myFaultEntryForProjectList) 
							 && !(empty DislocBean2.myObsvEntryForProjectList)}" > 
		<h:outputFormat escape="false"  
							 id="stuff90"  
							 value="Disloc is ready to run.  Click the button below to launch." /> 
		<h:commandButton id="rundisloc" 
							  value="Run Disloc" 
							  action="#{DislocBean2.runBlockingDislocJSF}" /> 
	 </h:form>
  </h:panelGroup> 
  
</h:panelGrid>
