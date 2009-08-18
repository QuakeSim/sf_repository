			<h:panelGroup id="kherl189"
							  rendered="#{SimplexBean.currentEditProjectForm.renderCreateNewFaultForm}">
				<h:form id="Faultform">

					<h:panelGrid id="FaultTable" columns="3" footerClass="subtitle"
						headerClass="subtitlebig" styleClass="medium"
						columnClasses="subtitle,medium">

						<f:facet name="header">
							<h:outputFormat id="output3" escape="false"
								value="<b>Input Fault Geometry</b>" />
						</f:facet>


							<h:outputFormat id="output34343" escape="false"
								value="<b>Parameter Name</b>" />

							<h:outputFormat id="output343" escape="false"
								value="<b>Parameter Value</b>" />

							<h:outputFormat id="output3433" escape="false"
								value="<b>Allow to Vary (Check for Yes)</b>" />

					  <f:verbatim>
					    <a class="tooltip" href="#">
						    Fault Name:
							  <span>
							  Canonical name of the fault segment. 
							  </span>
					     </a>
				      </f:verbatim>
						<h:panelGroup id="lkdrq118">
							<h:inputText id="FaultName"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultName}"
								required="true" />
							<h:message for="FaultName" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:outputText value=""/>


					  <f:verbatim>
					    <a class="tooltip" href="#">
						 Location X:
							  <span>Location of the grid origin in Cartesian coordinates</span>
					     </a>
				      </f:verbatim>
						<h:panelGroup id="lkdrq1181">
							<h:inputText id="FaultLocationX"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLocationX}"
								required="true" />
							<h:message id="lkdrq1182" for="FaultLocationX" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultOriginXVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultOriginXVary}" />


					<f:verbatim>
					    <a class="tooltip" href="#">
						 Location Y:
							  <span>Location of the grid origin in Cartesian coordinates</span>
					     </a>
				   </f:verbatim>
						<h:panelGroup id="lkdrq1184">
							<h:inputText id="FaultLocationY"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLocationY}"
								required="true" />
							<h:message id="lkdrq1185" for="FaultLocationY" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultOriginYVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultOriginYVary}" />

					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault Length:
							  <span>
							  Length of the fault in kilometers
							  </span>
					     </a>
				  </f:verbatim>
						<h:panelGroup id="lkdrq1187">
							<h:inputText id="FaultLength"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLength}"
								required="true" />
							<h:message id="lkdrq1188" for="FaultLength" showDetail="true" showSummary="false"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultLengthVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLengthVary}" />


					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault Width:
							  <span>
							  Width of the fault in kilometers
							  </span>
					     </a>
				  </f:verbatim>
						<h:panelGroup id="lkdrq11811">
							<h:inputText id="FaultWidth"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultWidth}"
								required="true" />
							<h:message id="lkdrq11812" for="FaultWidth" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultWidthVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultWidthVary}" />

					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault Depth:
							  <span>
							  Depth of the fault in kilometers
							  </span>
					     </a>
				  </f:verbatim>
						<h:panelGroup id="lkdrq11814">
							<h:inputText id="FaultDepth"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultDepth}"
								required="true" />
							<h:message id="lkdrq11815" for="FaultDepth" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultDepthVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultDepthVary}" />

					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault Dip Angle:
							  <span>
							  Dip angle of the fault
							  </span>
					     </a>
				  </f:verbatim>	
						<h:panelGroup id="lkdrq11817">
							<h:inputText id="FaultDipAngle"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultDipAngle}"
								required="true" />
							<h:message id="lkdrq11818" for="FaultDipAngle" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultDipAngleVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultDipAngleVary}" />

					<f:verbatim>
					    <a class="tooltip" href="#">
						 Strike Angle: 
							  <span>
							  Strike angle of the fault in degrees (not radians).
							  </span>
					     </a>
				  </f:verbatim>					
						<h:panelGroup id="dflelerkljk1">
							<h:inputText id="FaultStrikeAngle"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultStrikeAngle}"
								required="false" />
							<h:message id="dflelerkljk2" for="FaultStrikeAngle" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultStrikeAngleVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultStrikeAngleVary}" />


					<f:verbatim>
					    <a class="tooltip" href="#">
						 Dip Slip:
							  <span>
							  Dip slip of the fault in centimeters. 
							  </span>
					     </a>
				  </f:verbatim>					
						<h:panelGroup id="dflelerkljk4">
							<h:inputText id="FaultSlip"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultSlip}"
								required="true" />
							<h:message for="FaultSlip" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultDipSlipVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultDipSlipVary}" />

					<f:verbatim>
					    <a class="tooltip" href="#">
						 Strike Slip: 
							  <span>
							  Strike slip of the fault in centimeters.
							  </span>
					     </a>
				  </f:verbatim>					
						<h:panelGroup id="dflelerkljk6">
							<h:inputText id="FaultStrikeSlip"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultRakeAngle}"
								required="true" />
							<h:message  id="dflelerkljk7" for="FaultStrikeSlip" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultStrikeSlipVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultStrikeSlipVary}" />

					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault Origin Longitude:
							  <span>
							  Longitude of the fault's origin.
							  </span>
					     </a>
				  </f:verbatim>
						<h:panelGroup id="dflelerkljk9">
							<h:inputText id="FaultLonStarts"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLonStarts}" />
						</h:panelGroup>
						<h:outputText  id="dflelerkljk10" value="optional" />

					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault Origin Latitude:
							  <span>
							  Longitude of the fault's origin.
							  </span>
					     </a>
				  </f:verbatim>
						<h:panelGroup id="dflelerkljk12">
							<h:inputText id="FaultLatStarts"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLatStarts}" />
						</h:panelGroup>
						<h:outputText  id="dflelerkljk13" value="optional" />

					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault End-Point Longitude:
							  <span>
							  Longitude of the fault's end point.
							  </span>
					     </a>
				  </f:verbatim>
						<h:panelGroup id="dflelerkljk15">
							<h:inputText id="FaultLonEnds"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLonEnds}" />
						</h:panelGroup>
						<h:outputText  id="dflelerkljk16" value="optional" />

					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault End-Point Latitude:
							  <span>
							  Latitude of the fault's end point.
							  </span>
					     </a>
				  </f:verbatim>
						<h:panelGroup id="dflelerkljk18">
							<h:inputText id="FaultLatEnds"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLatEnds}" />
						</h:panelGroup>
						<h:outputText  id="dflelerkljk19" value="optional" />

						<h:commandButton id="addfault" value="Set Values"
							actionListener="#{SimplexBean.toggleAddFaultForProject}" />
				      <f:verbatim>
				         <input type="button" name="Update"
					 				 value="Do Math"
									 onclick="doMath()"/>
 				      </f:verbatim>
					<f:facet name="footer"> 
					   <h:outputFormat id="output2" escape="false" 
						value="Click 'Do Math' to udpate length and strike. 
						       Click 'Set Values' when you are done." /> 
					</f:facet> 

					</h:panelGrid>
				</h:form>
         </h:panelGroup>
