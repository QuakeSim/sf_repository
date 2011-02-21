<h:panelGrid id="DashboradPanelgrid"
				 columnClasses="alignTop,alignTop"
				 columns="1" 
				 width="300"
				 border="0">			
  
  <h:panelGroup id="lkdrq3">
	 <h:form id="selectproj">  
		<h:outputFormat id="lkdrq4" escape="false"
							 value="<b>Project Name:</b> #{SimplexBean.projectName}<br>" />
		<h:outputFormat id="lkdrq5" escape="false"
							 value="<b>Starting Temperature:</b> #{SimplexBean.currentProjectEntry.startTemp}<br>"/>
		<h:outputFormat id="lkdrq6" escape="false"
							 value="<b>Maximum Iterations:</b> #{SimplexBean.currentProjectEntry.maxIters}<br>"/>
		<h:outputText id="lkj3034f" escape="false"
						  value="<b>Project Lat/Lon Origin</b>: (#{SimplexBean.currentProjectEntry.origin_lat}, #{SimplexBean.currentProjectEntry.origin_lon})<br>"/>
		
		<h:outputText id="instructionezzze" escape="false"
						  value="You must add at least one observation point and one fault.  The preferred method is to use the GPS station map option."/>
		
		<h:selectOneRadio layout="pageDirection" id="subscriptions"
								value="#{SimplexBean.currentEditProjectForm.projectSelectionCode}">
		  <f:selectItem id="item021"
							 itemLabel="Add GPS Observation Point (Preferred): Use map to 
											choose GPS station input."
							 itemValue="ShowGPSObsv" />
		  
		  <f:selectItem id="item1"
							 itemLabel="Add Observation Point: Click to specify observation point parameters."
							 itemValue="ShowObservation" />
		  
		  <f:selectItem id="item0"
							 itemLabel="Add Observation List (Advanced): Cut and paste a list of 
											observation points. "
							 itemValue="ShowObsvCutPaste" />
		  
		  <f:selectItem id="item33221"
							 itemLabel="Add Fault from Map (Preferred): Use map to choose input fault."
							 itemValue="ShowFaultMap" />
		  
		  <f:selectItem id="item2"
							 itemLabel="Create New Fault: Click to specify geometry for a fault segment."
							 itemValue="CreateNewFault" />
		</h:selectOneRadio>
		<h:commandButton id="button1" value="Make Selection"
							  actionListener="#{SimplexBean.currentEditProjectForm.toggleProjectSelection}">
		</h:commandButton>
	 </h:form>
	 
	 <h:form id="dflelerkljk185" 
				rendered="#{!empty SimplexBean.myObservationEntryForProjectList and !empty SimplexBean.myFaultEntryForProjectList}">
		<h:outputText value="Simplex is ready to run.  Click the button below to launch."/>
		<h:commandButton rendered="#{!empty SimplexBean.myObservationEntryForProjectList
											and !empty SimplexBean.myFaultEntryForProjectList}"
							  id="runSimplex2" value="Run Simplex"
							  action="#{SimplexBean.toggleRunSimplex2}" />
	 </h:form>	
  </h:panelGroup>  
</h:panelGrid>
