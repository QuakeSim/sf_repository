			<h:panelGroup id="lkdrq3">
			   <h:form id="selectproj">
					<h:outputText id="lkdrq4" escape="false"
						value="<b>Project Name:</b> #{SimplexBean.projectName} <br>" />
					<h:outputFormat id="lkdrq5" escape="false"
						value="<b>Starting Temperature:</b>"/>
					<h:inputText id="rvfr930391ci" 
									 required="true"		 
									 size="10"					
									 value="#{SimplexBean.currentProjectEntry.startTemp}" />
 					<f:verbatim><br/></f:verbatim>

					<h:outputText id="lkdrq6" escape="false"
						value="<b>Maximum Iterations:</b>"/>
					<h:inputText id="dfe930391ci" 
									 required="true"		 	
									 size="10"									
									 value="#{SimplexBean.currentProjectEntry.maxIters}" />
 					<f:verbatim><br/></f:verbatim>

					<h:outputText id="lkj3034f" escape="false"
					   value="<b>Project Lat Origin:</b>"/>
					<h:inputText id="ere9023"
									 size="10"					
									 value="#{SimplexBean.currentProjectEntry.origin_lat}"
									 required="true"/>
 					<f:verbatim><br/></f:verbatim>

					<h:outputText id="lkr33j3034f" escape="false"
					   value="<b>Project Lon Origin:</b>"/>
					<h:inputText id="ere90eree23"
									 size="10"					
									 value="#{SimplexBean.currentProjectEntry.origin_lon}"
									 required="true"/>
 					<f:verbatim><br/></f:verbatim>
						

					<h:commandButton id="button1121" value="Update Information"
						actionListener="#{SimplexBean.currentEditProjectForm.toggleProjectSelection}">
					</h:commandButton>
						
				</h:form>
						<f:verbatim><hr></f:verbatim>
				<h:form id="uyer092">
					<h:outputText id="instructionezzze" escape="false"
					  				  value="<br>You must add at least one observation point and one fault.  The preferred method is to use the GPS station map option."/>

					<h:selectOneRadio layout="pageDirection" id="subscriptions"
						value="#{SimplexBean.currentEditProjectForm.projectSelectionCode}">
						<f:selectItem id="item021"
							itemLabel="Add GPS Observation Point (Preferred): Use map to choose GPS station input."
							itemValue="ShowGPSObsv" />

						<f:selectItem id="item1"
							itemLabel="Add Observation Point: Click to specify observation point parameters."
							itemValue="ShowObservation" />

						<f:selectItem id="item0"
							itemLabel="Add Observation List (Advanced): Cut and paste a list of observation points. "
							itemValue="ShowObsvCutPaste" />

						<f:selectItem id="item33221"
							itemLabel="Add Fault from Map (Preferred): Use map to choose input fault."
							itemValue="ShowFaultMap" />

						<f:selectItem id="item2"
							itemLabel="Create New Fault: Click to specify geometry for a fault segment."
							itemValue="CreateNewFault" />

						<f:selectItem id="item4"
							itemLabel="Add Fault from DB: Click to select a fault segment from the database."
							itemValue="AddFaultSelection" />
					</h:selectOneRadio>
					<h:commandButton id="button1" value="Make Selection"
						actionListener="#{SimplexBean.currentEditProjectForm.toggleProjectSelection}">
					</h:commandButton>
			   </h:form>

  		      <h:form id="dflelerkljk185" rendered="#{!empty SimplexBean.myObservationEntryForProjectList && !empty SimplexBean.myFaultEntryForProjectList}">
            <h:outputText value="Simplex is ready to run.  Click the button below to launch."/>
		      <h:commandButton rendered="#{!empty SimplexBean.myObservationEntryForProjectList
							  				  && !empty SimplexBean.myFaultEntryForProjectList}"
							  				  id="runSimplex2" value="Run Simplex"
							  				  action="#{SimplexBean.toggleRunSimplex2}" />
			   </h:form>
		   </h:panelGroup>
