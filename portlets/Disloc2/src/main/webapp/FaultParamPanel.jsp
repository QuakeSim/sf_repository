			<h:form id="Faultform" rendered="#{DislocBean2.renderCreateNewFaultForm}"> 
		  <h:outputText id="output3" escape="false" 
							value="<b>Fault Geometry Parameters</b>" /> 

				<h:panelGrid id="FaultTable" columns="3" footerClass="subtitle" 
					headerClass="subtitlebig" styleClass="medium" 
					columnClasses="subtitle,medium"> 
 

			      <h:outputText id="outputer3" escape="false" 
							  		value="<b>Name</b>" /> 

					<h:outputText id="valcoler09" escape="false" 
							  		value="<b>Value</b>" /> 

				   <h:outputText id="unitcol3908" escape="false" 
							  		value="<b>Units</b>" /> 

 
					<h:outputText  id="stuff15" value="Fault Name:" /> 
					<h:panelGroup  id="stuff16"> 
						<h:inputText id="FaultName" 
							value="#{DislocBean2.currentFault.faultName}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lkdjre32" value="N/A" /> 
 
					  <f:verbatim>
					    <a class="tooltip" href="#">
						 Location X:
							  <span>Location of the grid origin in Cartesian coordinates</span>
					     </a>
				  </f:verbatim>
					<h:panelGroup  id="stuff18"> 
						<h:inputText id="FaultLocationX" 
							value="#{DislocBean2.currentFault.faultLocationX}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lkdjre3234" value="N/A" /> 
 
					<f:verbatim>
					    <a class="tooltip" href="#">
						 Location Y:
							  <span>Location of the grid origin in Cartesian coordinates</span>
					     </a>
				  </f:verbatim>
					<h:panelGroup  id="stuff20"> 
						<h:inputText id="FaultLocationY" 
							value="#{DislocBean2.currentFault.faultLocationY}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lkdjre3122" value="N/A" /> 

					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault Origin Latitude:
							  <span>
							  Latitude of the fault's origin.
							  </span>
					     </a>
				  </f:verbatim>
			      <h:panelGroup>
						<h:inputText id="faultLat" value="#{DislocBean2.currentFault.faultLatStart}"
										 required="true" />
					</h:panelGroup>
					<h:outputText  id="lkdj21re32" value="N/A" /> 


					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault Origin Longitude:
							  <span>
							  Longitude of the fault's origin.
							  </span>
					     </a>
				  </f:verbatim>
					<h:panelGroup>
					<h:inputText id="faultLon" value="#{DislocBean2.currentFault.faultLonStart}"
									 required="true" />
					</h:panelGroup>
					<h:outputText  id="lkdj121re32" value="N/A" /> 

					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault End-Point Latitude:
							  <span>
							  Latitude of the fault's end point.
							  </span>
					     </a>
				  </f:verbatim>
			      <h:panelGroup>
						<h:inputText id="faultLatendere" value="#{DislocBean2.currentFault.faultLatEnd}"
										 required="true" />
					</h:panelGroup>
					<h:outputText  id="lkdj23oi232" value="N/A" /> 

					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault End-Point Longitude:
							  <span>
							  Longitude of the fault's end point.
							  </span>
					     </a>
				  </f:verbatim>
					<h:panelGroup>
					<h:inputText id="faultLonende3r" value="#{DislocBean2.currentFault.faultLonEnd}"
									 required="true" />
					</h:panelGroup>
					<h:outputText  id="lkdjr1212e32" value="N/A" /> 
 
					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault Length:
							  <span>
							  Length of the fault in kilometers
							  </span>
					     </a>
				  </f:verbatim>
					<h:panelGroup  id="stuff22"> 
						<h:inputText id="FaultLength" 
							value="#{DislocBean2.currentFault.faultLength}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lkdj1re1132" value="km" /> 
 
					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault Width:
							  <span>
							  Width of the fault in kilometers
							  </span>
					     </a>
				  </f:verbatim>
					<h:panelGroup  id="stuff24"> 
						<h:inputText id="FaultWidth" 
							value="#{DislocBean2.currentFault.faultWidth}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lkdjr121e32" value="km"/> 
 
					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault Depth:
							  <span>
							  Depth of the fault in kilometers
							  </span>
					     </a>
				  </f:verbatim>
					<h:panelGroup  id="stuff26"> 
						<h:inputText id="FaultDepth" 
							value="#{DislocBean2.currentFault.faultDepth}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lkdj2321re32" value="km"/> 
 
					<f:verbatim>
					    <a class="tooltip" href="#">
						 Fault Dip Angle:
							  <span>
							  Dip angle of the fault
							  </span>
					     </a>
				  </f:verbatim>					
					<h:panelGroup  id="stuff28"> 
						<h:inputText id="FaultDipAngle" 
							value="#{DislocBean2.currentFault.faultDipAngle}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lk34342djre32" value="degrees"/> 
 
					<f:verbatim>
					    <a class="tooltip" href="#">
						 Dip Slip:
							  <span>
							  Dip slip of the fault in centimeters. 
							  </span>
					     </a>
				  </f:verbatim>					
					<h:panelGroup  id="stuff30"> 
						<h:inputText id="FaultSlip" 
							value="#{DislocBean2.currentFault.faultDipSlip}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lk67v342djre32" value="cm"/> 
 
					<f:verbatim>
					    <a class="tooltip" href="#">
						 Strike Angle: 
							  <span>
							  Strike angle of the fault in degrees (not radians).
							  </span>
					     </a>
				  </f:verbatim>					
					<h:panelGroup  id="stuff32"> 
						<h:inputText id="FaultStrikeAngle" 
							value="#{DislocBean2.currentFault.faultStrikeAngle}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="r54r4djre32" value="degrees"/> 
 
					<f:verbatim>
					    <a class="tooltip" href="#">
						 Strike Slip: 
							  <span>
							  Strike slip of the fault in centimeters.
							  </span>
					     </a>
				  </f:verbatim>					
					<h:panelGroup  id="stuff35"> 
						<h:inputText id="FaultStrikeSlip" 
							value="#{DislocBean2.currentFault.faultStrikeSlip}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="er9e2djre32" value="cm"/> 
 
					<f:verbatim>
					    <a class="tooltip" href="#">
						 Tensile Slip: 
							  <span>
							  Tensile slip of the fault in centimeters.
							  </span>
					     </a>
				  </f:verbatim>					
					<h:panelGroup  id="stuff36"> 
						<h:inputText id="FaultTensileSlip" 
							value="#{DislocBean2.currentFault.faultTensileSlip}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="ee3e33c3" value="cm"/> 
 
					<f:verbatim>
					    <a class="tooltip" href="#">
						    Lame Lambda:
							  <span>
							  Lambda value of the Lame parameter.
							  </span>
					     </a>
				  </f:verbatim>
					<h:panelGroup  id="stuff38"> 
						<h:inputText id="LameLambda" 
							value="#{DislocBean2.currentFault.faultLameLambda}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="e34343" value="N/A"/> 
 
					<f:verbatim>
					    <a class="tooltip" href="#">
						 	 Lame Mu:
							  <span>
							  Mu value of the Lame parameter
							  </span>
					     </a>
				  </f:verbatim>
					<h:panelGroup  id="stuff40"> 
						<h:inputText id="LameMu" 
							value="#{DislocBean2.currentFault.faultLameMu}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="e3434dff3d" value="N/A"/> 
 
					<f:facet name="footer"> 
					   <h:outputFormat id="output2" escape="false" 
						value="Click 'Do Math' to udpate length and strike. 
						       Click 'Set Values' when you are done." /> 
					</f:facet> 
				</h:panelGrid>		 
 				<h:panelGrid id="faultbuttons93" columns="2">
					<h:commandButton id="addfault" value="Set Values" 
						actionListener="#{DislocBean2.toggleAddFaultForProject}" /> 
				   <f:verbatim>
				      <input id="domath" type="button" name="Update" value="Calculate length" onclick="calculatelength()"/>
				      <input id="domath2" type="button" name="Update2" value="Calculate endpoint" onclick="calculateendpoint()"/>
 				   </f:verbatim>
				</h:panelGrid>
			</h:form> 
