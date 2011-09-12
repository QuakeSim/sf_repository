<h:panelGrid id="DashboradPanelgrid"
				 columnClasses="alignTop"
				 columns="1" 
				 border="0">		
  <h:panelGroup id="simplexDashPanelGrouper">
	 <f:verbatim>
		<fieldset style="width:100%">
		  <legend class="portlet-form-label"> Project Dashboard </legend>
		</f:verbatim>
	
		<f:verbatim>
		  <p>You must add at least one observation point and one fault 
		  before you can run Simplex.</p>
		</f:verbatim>
  
	 <%-- This is the first row --%>
	 <h:panelGrid id="simplexDashboardMenu" 
					  cellspacing="3"
					  columns="4" 
					  border="1">
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
  <h:form id="selectprojSimplex">  
	 <f:verbatim>
		<p><b>Step 1:</b> Add fault models to your project.<br/>
	 </f:verbatim>
	 <h:selectOneMenu id="SimplexSelectionMenu"
							title="Use this drop down to add faults points to 
									 your project."
							value="#{SimplexBean.currentEditProjectForm.projectSelectionCode}">
		<f:selectItem id="item33221"
						  itemLabel="Add Fault from Map (Preferred)"
						  itemValue="ShowFaultMap" />
		
		<f:selectItem id="item2"
						  itemLabel="Create New Fault"
						  itemValue="CreateNewFault" />

		<f:selectItem id="item3"
						  itemLabel="Quick Fault Add Form"
						  itemValue="ShowFaultQuickAddForm" />

	 </h:selectOneMenu>
	 <h:commandButton id="simplexSelectButton1" value="Make Selection"
							actionListener="#{SimplexBean.currentEditProjectForm.toggleProjectSelection1}">
	 </h:commandButton>
  <f:verbatim></p></f:verbatim>
  </h:form>

  <h:form id="simplexSelection2">
	 <f:verbatim>
		<p><b>Step 2:</b> Input your observations. <br/>
	 </f:verbatim>
	 <h:selectOneMenu id="SimplexSelectionMenu2"
							title="Use this drop down to add observation points to 
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
						  itemLabel="Import ARIA Data File"
						  itemValue="ShowAriaObsvCutPaste" />

		<f:selectItem id="coseismicDisplacementForm"
						  itemLabel="Import Coseismic Data File"
						  itemValue="ShowCoseismicCutPaste" />

		<f:selectItem id="SAR2SimplexDisplacementForm"
						  itemLabel="Import SAR2Simplex Data File"
						  itemValue="ShowSARObsvCutPaste" />

		<f:selectItem id="SAR2SimplexSelectionMap"
						  itemLabel="Create Simplex Input from SAR Image"
						  itemValue="ShowSARSelectionMap" />
	 </h:selectOneMenu>
	 <h:commandButton id="simplexSelectButton2" value="Make Selection"
							actionListener="#{SimplexBean.currentEditProjectForm.toggleProjectSelection2}">
	 </h:commandButton>
	 <f:verbatim></p></f:verbatim>
  </h:form>
  <h:form id="simplexSelection3">
	 <f:verbatim>
		<p><b>Step 3:</b> (Optional) View a map of your project inputs and reset parameters. <br/>
	 </f:verbatim>
		
	 <h:selectOneMenu id="SimplexSelectionMenu3"
							title="Use this drop down to add observation points to 
									 your project."
							value="#{SimplexBean.currentEditProjectForm.projectSelectionCode}">

		<f:selectItem id="itemProjectParams"
						  itemLabel="Edit Project Params"
						  itemValue="ShowProjectParams" />
		<f:selectItem id="itemProjectMap"
						  itemLabel="Show Project Map"
						  itemValue="ShowProjectMap" />
	 </h:selectOneMenu>
	 <h:commandButton id="simplexSelectButton3" value="Make Selection"
							actionListener="#{SimplexBean.currentEditProjectForm.toggleProjectSelection3}">
	 </h:commandButton>
	 <f:verbatim></p></f:verbatim>
  </h:form>
  
  <h:form id="dflelerkljk185" 
			 rendered="#{!empty SimplexBean.myObservationEntryForProjectList 
						  and !empty SimplexBean.myFaultEntryForProjectList}">
	 <f:verbatim>
		<hr/>
		<p>
		Simplex is ready to run.  This may take several minutes, so provide your email
		address here to get an email notice when you job completes.  Then hit the "Run Simplex" button.
		</p>
	 </f:verbatim>
	 <f:verbatim>
		<b>Email Address:</b>
	 </f:verbatim>
	 <h:inputText id="simplexEmailNotification"
					  required="true"
					  value="#{SimplexBean.userEmailAddress}"/>
						
	 <h:commandButton rendered="#{!empty SimplexBean.myObservationEntryForProjectList
										 and !empty SimplexBean.myFaultEntryForProjectList}"
							id="runSimplex2" 
							onclick="showLoading()"
							value="Run Simplex"
							action="#{SimplexBean.toggleRunSimplex2}" />
  </h:form>	
	 <f:verbatim></fieldset></f:verbatim>
  </h:panelGroup>
</h:panelGrid>
