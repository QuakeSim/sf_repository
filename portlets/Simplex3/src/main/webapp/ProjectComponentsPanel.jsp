		    <h:panelGrid id="ProjectComponentList" columns="1" border="0">
			<h:panelGroup id="dflelerkljk161">
				<h:form id="UpdateSelectFaultsForm"
					rendered="#{!empty SimplexBean.myFaultEntryForProjectList}">
		<h:outputText value="<br/>" escape="false"/>
		<h:outputText value="1) Please check a box to allow a value to vary during the simplex optimization. " escape="false"/>
		<h:outputText value="2) Please click the 'update' button after value changes." escape="false"/>
		<h:outputText value="2) Please click the checkbox next to the 'name' to draw the fault on the map" escape="false"/>
					<h:panelGrid id="dflelerkljk162" columns="1" border="1" cellpadding="0" cellspacing="0">
						  <h:outputFormat id="dflelerkljk165" escape="false" value="<b>Fault Components</b>">
						  </h:outputFormat>

						  <h:panelGrid id="dflelerkljg162" columns="2" border="0"  cellpadding="0" cellspacing="0">

							<h:dataTable border="1" cellpadding="0" cellspacing="0" id="dflelerh966" headerClass="componentstableh" columnClasses="componentstablec"
							      value="#{SimplexBean.myFaultsForProjectList}" var="myentry31">
							    <h:column>
							      <f:facet name="header">
								<h:outputText id="dflelerh967" escape="false" value="<b>Name</b>" />
							      </f:facet>

							      <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
								<h:inputText id="dflelerklh968" style="text-align:right;width:60px" value="#{myentry31.faultName}" required="true" />
								<h:selectBooleanCheckbox id="faultnamec" onclick="togglefaultname(this)" value="" />
							      </h:panelGrid>
							    </h:column>

							    <h:column>
							      <f:facet name="header">
								<h:outputText id="lkdrh119" value="Location X" />
							      </f:facet>

							      <h:panelGrid columns="3" cellpadding="0" cellspacing="0">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultLocationX2" style="text-align:right;width:60px"
								  value="#{myentry31.faultLocationX}" required="true" />
								<h:message id="lkdrh1182" for="FaultLocationX2" showDetail="true"
								  showSummary="true" errorStyle="color: red" />
								<h:selectBooleanCheckbox id="faultOriginXVary2"
								  value="#{myentry31.faultOriginXVary}" />
							      </h:panelGrid>
							    </h:column>

							    <h:column>
							      <f:facet name="header">
								<h:outputText id="lkdrqh1183" value="Location Y" />
							      </f:facet>

							      <h:panelGrid columns="3" cellpadding="0" cellspacing="0">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultLocationY2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultLocationY}"
								  required="true" />
								<h:message id="lkdrqh185" for="FaultLocationY2" showDetail="true"
								  showSummary="true" errorStyle="color: red" />								
								<h:selectBooleanCheckbox id="faultOriginYVary2"
								  value="#{myentry31.faultOriginYVary}" />
							      </h:panelGrid>
							    </h:column>

							    <h:column>
							      <f:facet name="header">
								<h:outputText id="lkdrqh1186" value="Length" />
							      </f:facet>
							      <h:panelGrid columns="3" cellpadding="0" cellspacing="0">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultLength2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultLength}"
								  required="true" />
								<h:message id="lkdrae188" for="FaultLength2" showDetail="true" showSummary="false"
								  errorStyle="color: red" />
								<h:selectBooleanCheckbox id="faultLengthVary2"
								  value="#{myentry31.faultLengthVary}" />
							      </h:panelGrid>
							    </h:column>

							    <h:column>
							      <f:facet name="header">
								<h:outputText id="lkdrh1189" value="Width" />
							      </f:facet>
							      <h:panelGrid columns="3" cellpadding="0" cellspacing="0">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultWidth2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultWidth}"
								  required="true" />
								<h:message id="lkdrqh11812" for="FaultWidth2" showDetail="true" showSummary="true"
								  errorStyle="color: red" />
								<h:selectBooleanCheckbox id="faultWidthVary2"
								  value="#{myentry31.faultWidthVary}" />
							      </h:panelGrid>
							    </h:column>						
							
							    <h:column>
							      <f:facet name="header">
								<h:outputText id="lkdrq11813" value="Depth" />
							      </f:facet>
							      <h:panelGrid columns="3" cellpadding="0" cellspacing="0">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultDepth2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultDepth}"
								  required="true" />
								<h:message id="lkdrqr11815" for="FaultDepth2" showDetail="true" showSummary="true"
								  errorStyle="color: red" />
								<h:selectBooleanCheckbox id="faultDepthVary2"
								  value="#{myentry31.faultDepthVary}" />
							      </h:panelGrid>
							    </h:column>

							    <h:column>
							      <f:facet name="header">
								<h:outputText id="lkdrqj11816" value="Dip Angle" />
							      </f:facet>
							      <h:panelGrid columns="3" cellpadding="0" cellspacing="0">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultDipAngle2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultDipAngle}"
								  required="true" />
								<h:message id="lkdrqj11818" for="FaultDipAngle2" showDetail="true"
								  showSummary="true" errorStyle="color: red" />
								<h:selectBooleanCheckbox id="faultDipAngleVary2"
								  value="#{myentry31.faultDipAngleVary}" />
							      </h:panelGrid>
							    </h:column>						

							    <h:column>
							      <f:facet name="header">
								<h:outputText id="lkdrq11819" value="Strike Angle" />
							      </f:facet>
							      <h:panelGrid columns="3" cellpadding="0" cellspacing="0">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultStrikeAngle2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultStrikeAngle}"
								  required="false" />
								<h:message id="dflelerkljj2" for="FaultStrikeAngle2" showDetail="true"
								  showSummary="true" errorStyle="color: red" />
								<h:selectBooleanCheckbox id="faultStrikeAngleVary2"
								  value="#{myentry31.faultStrikeAngleVary}" />
							      </h:panelGrid>
							    </h:column>

							    <h:column>
							      <f:facet name="header">
								<h:outputText id="dflelerkljj3" value="Dip Slip" />
							      </f:facet>
							      <h:panelGrid columns="3" cellpadding="0" cellspacing="0">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultSlip2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultSlip}"
								  required="true" />
								<h:message for="FaultSlip2" showDetail="true" showSummary="true"
								  errorStyle="color: red" />
								<h:selectBooleanCheckbox id="faultDipSlipVary2"
								  value="#{myentry31.faultDipSlipVary}" />
							      </h:panelGrid>
							    </h:column>

							    <h:column>
							      <f:facet name="header">
								<h:outputText id="dflelerkljj5" value="Strike Slip" />
							      </f:facet>
							      <h:panelGrid columns="3" cellpadding="0" cellspacing="0">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultRakeAngle2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultRakeAngle}"
								  required="true" />
								<h:message  id="dflelerkljj7" for="FaultRakeAngle2" showDetail="true"
								  showSummary="true" errorStyle="color: red" />
								<h:selectBooleanCheckbox id="faultStrikeSlipVary2"
								  value="#{myentry31.faultStrikeSlipVary}" />
							      </h:panelGrid>
							    </h:column>						
						
							    <h:column>
							      <f:facet name="header">
								<h:outputText id="dflelerkljj8" value="Fault Lon Starts(optional)" />
							      </f:facet>
							      <h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="centered">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultLonStarts2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultLonStarts}" />
							      </h:panelGrid>
							    </h:column>

							    <h:column>
							      <f:facet name="header">
								<h:outputText id="dflelerkljj11" value="Fault Lat Starts(optional)" />
							      </f:facet>
							      <h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="centered">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultLatStarts2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultLatStarts}" />
							      </h:panelGrid>
							    </h:column>

							    <h:column>
							      <f:facet name="header">
								<h:outputText  id="dflelerkljj14" value="Fault Lon Ends(optional)" />
							      </f:facet>
							      <h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="centered">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultLonEnds2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultLonEnds}" />
							      </h:panelGrid>
							    </h:column>

							    <h:column>
							      <f:facet name="header">
								<h:outputText id="dflelerkljj17" value="Fault Lat Ends(optional)" />
							      </f:facet>
							      <h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="centered">
								<f:facet name="header">								
								</f:facet>
								<h:inputText id="FaultLatEnds2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultLatEnds}" />
							      </h:panelGrid>
							    </h:column>


						      </h:dataTable>


							<h:dataTable border="1" cellpadding="0" cellspacing="0" id="dflelerh966" headerClass="componentstableh2" columnClasses="componentstablec"
											  id="dflelerkljk451"
											  value="#{SimplexBean.myFaultEntryForProjectList}" var="myentry3">

								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk454" escape="false" value="<b>Update</b>" />
									</f:facet>

							      <h:panelGrid columns="2" cellpadding="0" cellspacing="0" styleClass="centered">
								<f:facet name="header">								
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry3.update}" id="dflelerkljk455"/>
								<h:outputText style="text-align:right;width:0px" value ="" />
							      </h:panelGrid>								
								</h:column>

								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk456" escape="false" value="<b>Remove</b>" />
									</f:facet>									

							      <h:panelGrid columns="2" cellpadding="0" cellspacing="0" styleClass="centered">
								<f:facet name="header">								
								</f:facet>
								<h:selectBooleanCheckbox id="dflelerkljk457" value="#{myentry3.delete}"/>
								<h:outputText style="text-align:right;width:0px" value ="" />
							      </h:panelGrid>
								</h:column>
							</h:dataTable>

						</h:panelGrid>						
					</h:panelGrid>
					<h:commandButton id="SelectFault4projj" value="UpdateFault"
						actionListener="#{SimplexBean.toggleUpdateFaults}" />
				</h:form>




				<h:form id="UpdateSelectObservationForm"
					rendered="#{!empty SimplexBean.myObservationsForProjectList}">
				<h:outputText value="<br/>" escape="false"/>				
				<h:outputText value="1) Please click the 'update' button after value changes." escape="false"/>
						<h:panelGroup id="dflelerkljk174">
						  <h:panelGrid id="obsvpanelgrid" columns="1" border="1">
								<h:outputFormat escape="false" id="dflelerkljk176"
									value="<b>Observation Components</b>">
								</h:outputFormat>
								<h:commandButton id="viewSimplexObsv" value="Display/Hide"
									actionListener="#{SimplexBean.currentEditProjectForm.toggleShowObsvEntries}"/>
							<h:panelGrid id="aerbasd1" columns="2" border="0" cellpadding="0" cellspacing="0">    

							<h:dataTable  border="1" cellpadding="0" cellspacing="0" id="dflelerkljk177" headerClass="componentstableh2" columnClasses="componentstablec"
							   rendered="#{SimplexBean.currentEditProjectForm.renderObsvEntries}"
								value="#{SimplexBean.myObservationsForProjectList}"
								var="myentry4">
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk178" escape="false" value="<b>Name</b>" />
									</f:facet>
									<h:inputText id="dflelerkljk179" style="text-align:right;width:60px"
									  value="#{myentry4.obsvName}" required="true" />
								</h:column>

								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk538" escape="false" value="<b>Type</b>" />
									</f:facet>									
									<h:selectOneMenu id="obsvType2"
										value="#{myentry4.obsvType}">
										<f:selectItem id="obsvTypeitem1" itemLabel="Displacement East"
											itemValue="1" />
										<f:selectItem id="obsvTypeitem2" itemLabel="Displacement North"
											itemValue="2" />
										<f:selectItem id="obsvTypeitem3" itemLabel="Displacement Up"
											itemValue="3" />
									</h:selectOneMenu>
								</h:column>

								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk548" escape="false" value="<b>Value</b>" />
									</f:facet>
									<h:inputText id="dflelerklj71" style="text-align:right;width:110px;" value="#{myentry4.obsvValue}" />
								</h:column>

								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk558" escape="false" value="<b>Error</b>" />
									</f:facet>
									<h:inputText id="dflelerklj72" style="text-align:right;width:110px;" value="#{myentry4.obsvError}" />
								</h:column>

								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk568" escape="false" value="<b>Location East</b>" />
									</f:facet>
									<h:inputText id="dflelerklj73" style="text-align:right;" value="#{myentry4.obsvLocationEast}" />
								</h:column>

								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk578" escape="false" value="<b>Location North</b>" />
									</f:facet>
									<h:inputText id="dflelerklj74" style="text-align:right;" value="#{myentry4.obsvLocationNorth}" />
								</h:column>
								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk388" escape="false" value="<b>Ref Site</b>" />
									</f:facet>
									<h:selectOneMenu id="obsvRefSite2"
										value="#{myentry4.obsvRefSite}">
										<f:selectItem id="obsvRefSiteitem1" itemLabel="unselected"
											itemValue="1" />
										<f:selectItem id="obsvRefSiteitem2" itemLabel="selected"
											itemValue="-1" />
									</h:selectOneMenu>
								</h:column>
							</h:dataTable>

							<h:dataTable border="1" cellpadding="0" cellspacing="0" id="dflelerh966" headerClass="componentstableh2" columnClasses="componentstablec"
											  id="dflelerkljk951"
											  rendered="#{SimplexBean.currentEditProjectForm.renderObsvEntries}"
											  value="#{SimplexBean.myObservationEntryForProjectList}" var="myentry7">

								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk954" escape="false" value="<b>Update</b>" />
									</f:facet>
									
							      <h:panelGrid columns="2" cellpadding="0" cellspacing="0" styleClass="centered">
								<f:facet name="header">								
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry7.update}" id="dflelerkljk955" />
								<h:outputText style="text-align:right;width:0px" value ="" />
							      </h:panelGrid>

								</h:column>

								<h:column>
									<f:facet name="header">
										<h:outputText id="dflelerkljk956" escape="false" value="<b>Remove</b>" />
									</f:facet>
									

							      <h:panelGrid columns="2" cellpadding="0" cellspacing="0" styleClass="centered">
								<f:facet name="header">								
								</f:facet>
								<h:selectBooleanCheckbox id="dflelerkljk957" value="#{myentry7.delete}"/>
								<h:outputText style="text-align:right;width:0px" value ="" />
							      </h:panelGrid>


								</h:column>
							</h:dataTable>
						    </h:panelGrid>


						</h:panelGrid>
						</h:panelGroup>	
					<h:commandButton id="SelectObservations4proj"					   
						value="Update Observations"
						actionListener="#{SimplexBean.toggleUpdateObservations}" />
				
				</h:form>
			</h:panelGroup>
		</h:panelGrid>