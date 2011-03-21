<h:panelGrid id="DashboradPanelgrid"
				 columnClasses="alignTop"
				 columns="1" 
				 border="0">		
  <h:panelGroup id="simplexDashPanelGrouper">
	 <f:verbatim>
		<fieldset style="width:100%">
		  <legend class="portlet-form-label"> Project Dashboard </legend>
		</f:verbatim>
	
  <h:outputText id="instructionezzze" 
					 escape="false"
					 value="You must add at least one observation point and one fault 
							  before you can run Simplex."/>
  
  <h:form id="selectprojSimplex">  
	 <%-- This is the first row --%>
	 <h:panelGrid id="simplexDashboardMenu" 
					  cellspacing="3"
					  columns="4" 
					  border="0">
		<h:outputText id="lkdrq4" escape="false"
							 value="<b>Project Name:</b> #{SimplexBean.projectName}" />
		<h:outputText id="lkdrq5" escape="false"
							 value="<b>Starting Temperature:</b> #{SimplexBean.currentProjectEntry.startTemp}"/>
		<h:outputText id="lkdrq6" escape="false"
							 value="<b>Maximum Iterations:</b> #{SimplexBean.currentProjectEntry.maxIters}"/>
		<h:outputText id="lkj3034f" escape="false"
						  value="<b>Project Lat/Lon Origin</b>: (#{SimplexBean.currentProjectEntry.origin_lat}, #{SimplexBean.currentProjectEntry.origin_lon})"/>
	 </h:panelGrid>
	 
	 <%-- This is the second row --%>
	 <h:selectOneMenu id="SimplexSelectionMenu"
							title="Use this drop down to add faults and observation points to 
									 your project."
							value="#{SimplexBean.currentEditProjectForm.projectSelectionCode}">
		<f:selectItem id="itemSimplexUnavco"
						  itemLabel="Add UNAVCO GSRM North America GPS Observation Point (Preferred)"
						  itemValue="ShowUnavcoGPSObsv" />

		<f:selectItem id="item021"
						  itemLabel="Add GPS Observation Point (Preferred)"
						  itemValue="ShowGPSObsv" />		
		
		<f:selectItem id="item1"
						  itemLabel="Add Observation Point"
						  itemValue="ShowObservation" />
		
		<f:selectItem id="item0"
						  itemLabel="Add Observation List (Advanced)"
						  itemValue="ShowObsvCutPaste" />

		<f:selectItem id="ariaDisplacementForm"
						  itemLabel="Import ARIA Data Files"
						  itemValue="ShowAriaObsvCutPaste" />
		
		<f:selectItem id="item33221"
						  itemLabel="Add Fault from Map (Preferred)"
						  itemValue="ShowFaultMap" />
		
		<f:selectItem id="item2"
						  itemLabel="Create New Fault"
						  itemValue="CreateNewFault" />

		<f:selectItem id="itemProjectMap"
						  itemLabel="Show Project Map"
						  itemValue="ShowProjectMap" />
	 </h:selectOneMenu>
	 <h:commandButton id="button1" value="Make Selection"
							actionListener="#{SimplexBean.currentEditProjectForm.toggleProjectSelection}">
	 </h:commandButton>
  </h:form>
  
  
  <h:form id="dflelerkljk185" 
			 rendered="#{!empty SimplexBean.myObservationEntryForProjectList 
						  and !empty SimplexBean.myFaultEntryForProjectList}">
	 <h:outputText id="simplexSubmitSeparator" 
						escape="false"
						value="<hr/>"/>
	 <h:outputText value="Simplex is ready to run."/>
	 <h:commandButton rendered="#{!empty SimplexBean.myObservationEntryForProjectList
										 and !empty SimplexBean.myFaultEntryForProjectList}"
							id="runSimplex2" value="Run Simplex"
							action="#{SimplexBean.toggleRunSimplex2}" />
  </h:form>	
	 <f:verbatim></fieldset></f:verbatim>
  </h:panelGroup>
</h:panelGrid>
