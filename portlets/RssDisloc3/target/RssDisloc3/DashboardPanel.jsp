	<h:panelGrid id="DashboradPanelgrid"
		columnClasses="alignTop,alignTop"
		columns="2" border="0">

	   <h:panelGroup id="pg1"> 
			<h:form id="selectproj">
				<h:outputFormat id="stuff1" escape="false" 
					value="<b>Project Name: #{DislocBean2.projectName} </b><br>" /> 
				<h:outputFormat id="stuffe3io4" escape="false" 
					value="<b>Project Origin (lat/lon):</b> (#{DislocBean2.currentParams.originLat}, #{DislocBean2.currentParams.originLon}) <br>" /> 
				<h:outputFormat id="stufw3f1" escape="false" 
					value="<b>Observation Style: #{DislocBean2.currentParams.observationPointStyle} </b><br>" /> 
 
				<h:selectOneRadio layout="pageDirection" id="subscriptions" 
					value="#{DislocBean2.projectSelectionCode}"> 
				<f:selectItem id="item0w3" 
						itemLabel="Set Observation Style: Choose between grid and scatter points." 
						itemValue="ChooseObsvStyleForm"/> 

					<f:selectItem id="item1" 
						itemLabel="View Project Info: Verify and update project information." 
						itemValue="CreateObservationGrid" /> 

					<f:selectItem id="item2" 
						itemLabel="Create New Fault: Click to specify geometry for a fault segment." 
						itemValue="CreateNewFault" /> 

					<f:selectItem id="item02132"
						itemLabel="Use Fault Map: Interactively select faults." 
						itemValue="ShowFaultMap" />

					<f:selectItem id="item021"
						itemLabel="View Project Map: View project faults and pick observation points on an interactive map."
						itemValue="ShowMap" />

				</h:selectOneRadio> 
				<h:commandButton id="button1" value="Make Selection" 
					actionListener="#{DislocBean2.toggleProjectSelection}"> 
			</h:commandButton> 
		</h:form> 

		<h:form id="RunDisloc" rendered="#{!(empty DislocBean2.myFaultEntryForProjectList) && !(empty DislocBean2.myObsvEntryForProjectList)}" > 
		  <h:outputFormat escape="false"  id="stuff90"  value="Disloc is ready to run.  Click the button below to launch." /> 
		  <h:commandButton id="rundisloc" value="Run Disloc" action="#{DislocBean2.runBlockingDislocJSF}" /> 
		</h:form>
	    </h:panelGroup> 


</h:panelGrid>
