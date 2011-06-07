<h:panelGrid id="DashboradPanelgrid"
				 columnClasses="alignTop"
				 columns="1" 
				 border="0">
  <h:panelGroup id="pg1"> 
	 <%-- Open the fieldset.  This will not be valid xml, since we must close it below. --%>
	 <f:verbatim>
		<fieldset style="width:920px">
		  <legend class="portlet-form-label"> Project Dashboard </legend>
		</f:verbatim>

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
						  rendered="#{DislocBean2.currentParams.observationPointStyle==1}"
						  escape="false" 
						  value="<b>Observation Style:</b> Grid" />
		
		<h:outputText id="stufw3f234" 
						  rendered="#{DislocBean2.currentParams.observationPointStyle==0}"
						  escape="false" 
						  value="<b>Observation Style:</b> Scatter" />
	 </h:panelGrid>
	 <h:form id="dislocDashboardMain1">
		<f:verbatim>
		  <p> <b> Step 1: </b>Use this drop down to add faults and observation points to your project.<br/>
		</f:verbatim>
		<h:selectOneMenu id="DislocFaultSelectionMenu" 
							  title="Use this drop down to add faults and observation points to your project."
							  value="#{DislocBean2.projectSelectionCode}"> 
		  <f:selectItem id="item02132"
							 itemLabel="Interactively select faults from a map." 
							 itemValue="ShowFaultMap" />		  
		  <f:selectItem id="item2" 
							 itemLabel="Specify geometry for a new fault segment." 
							 itemValue="CreateNewFault" /> 

		</h:selectOneMenu>
		<h:commandButton id="button1" 
							  value="Make Selection" 
							  actionListener="#{DislocBean2.toggleProjectSelectionAnon1}"> 
		</h:commandButton> 
	 </h:form>
	 <h:form id="dislocDashboardMain2">
		<f:verbatim>
		  <b> Step 2: </b>Use this drop down to choose the output style you want: a grid or scattered surface points. Use this also to change default settings for the interferogram calculations <br/>
		</f:verbatim>
		<h:selectOneMenu id="DislocParamsSelectionMenu" 
							  title="Use this drop down to choose the output style you want: a grid or scattered surface points"
							  value="#{DislocBean2.projectSelectionCode}"> 
		  <f:selectItem id="item0w3" 
							 itemLabel="Choose between grid and scatter point observation styles." 
							 itemValue="ChooseObsvStyleForm"/> 
		  
		  <f:selectItem id="item1" 
							 itemLabel="Update your observation grid or scatter points." 
							 itemValue="CreateObservationGrid" /> 

		  <f:selectItem id="item-insar-anon"
							 itemLabel="Change default interferogram plotting frequency, elevation, and azimuth."
							 itemValue="ShowInsarForm" />


		  <f:selectItem id="item021"
							 itemLabel="Select observation points on an interactive map (scatter style only)."
							 itemValue="ShowMap" />

		</h:selectOneMenu> 
		<h:commandButton id="button2" 
							  value="Make Selection" 
							  actionListener="#{DislocBean2.toggleProjectSelectionAnon2}"> 
		</h:commandButton> 
	 </h:form> 
	 
	 <h:form id="RunDisloc" 
				rendered="#{!(empty DislocBean2.myFaultEntryForProjectList) 
							 && !(empty DislocBean2.myObsvEntryForProjectList)}" > 
		<h:outputFormat escape="false"  
							 id="stuff90"  
							 value="<br/><b>Step 3: </b> Click the button to launch: " /> 
		<h:commandButton id="rundisloc" 
							  value="Run Disloc" 
							  onclick="showLoading()"
							  action="#{DislocBean2.runBlockingDislocJSFAnon}" /> 
	 </h:form>
  <f:verbatim></fieldset></f:verbatim>  
</h:panelGroup> 
</h:panelGrid>
