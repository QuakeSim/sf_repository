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
 
					<h:outputText  id="stuff17" value="Location X:" /> 
					<h:panelGroup  id="stuff18"> 
						<h:inputText id="FaultLocationX" 
							value="#{DislocBean2.currentFault.faultLocationX}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lkdjre3234" value="N/A" /> 
 
					<h:outputText  id="stuff19" value="Location Y:" /> 
					<h:panelGroup  id="stuff20"> 
						<h:inputText id="FaultLocationY" 
							value="#{DislocBean2.currentFault.faultLocationY}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lkdjre3122" value="N/A" /> 

					<h:outputText value="Fault Origin Latitude:" />
			      <h:panelGroup>
						<h:inputText id="faultLat" value="#{DislocBean2.currentFault.faultLatStart}"
										 required="true" />
					</h:panelGroup>
					<h:outputText  id="lkdj21re32" value="N/A" /> 

					<h:outputText value="Fault Origin Longitude" />
					<h:panelGroup>
					<h:inputText id="faultLon" value="#{DislocBean2.currentFault.faultLonStart}"
									 required="true" />
					</h:panelGroup>
					<h:outputText  id="lkdj121re32" value="N/A" /> 

					<h:outputText value="Fault End Latitude:" />
			      <h:panelGroup>
						<h:inputText id="faultLatendere" value="#{DislocBean2.currentFault.faultLatEnd}"
										 required="true" />
					</h:panelGroup>
					<h:outputText  id="lkdj23oi232" value="N/A" /> 

					<h:outputText value="Fault End Longitude" />
					<h:panelGroup>
					<h:inputText id="faultLonende3r" value="#{DislocBean2.currentFault.faultLonEnd}"
									 required="true" />
					</h:panelGroup>
					<h:outputText  id="lkdjr1212e32" value="N/A" /> 
 
					<h:outputText  id="stuff21" value="Length:" /> 
					<h:panelGroup  id="stuff22"> 
						<h:inputText id="FaultLength" 
							value="#{DislocBean2.currentFault.faultLength}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lkdj1re1132" value="km" /> 
 
					<h:outputText  id="stuff23" value="Width:" /> 
					<h:panelGroup  id="stuff24"> 
						<h:inputText id="FaultWidth" 
							value="#{DislocBean2.currentFault.faultWidth}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lkdjr121e32" value="km"/> 
 
					<h:outputText  id="stuff25" value="Depth:" /> 
					<h:panelGroup  id="stuff26"> 
						<h:inputText id="FaultDepth" 
							value="#{DislocBean2.currentFault.faultDepth}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lkdj2321re32" value="km"/> 
 
					<h:outputText  id="stuff27" value="Dip Angle:" /> 
					<h:panelGroup  id="stuff28"> 
						<h:inputText id="FaultDipAngle" 
							value="#{DislocBean2.currentFault.faultDipAngle}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lk34342djre32" value="degrees"/> 
 
					<h:outputText  id="stuff29" value="Dip Slip:" /> 
					<h:panelGroup  id="stuff30"> 
						<h:inputText id="FaultSlip" 
							value="#{DislocBean2.currentFault.faultDipSlip}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="lk67v342djre32" value="cm"/> 
 
					<h:outputText  id="stuff31" value="Strike Angle:" /> 
					<h:panelGroup  id="stuff32"> 
						<h:inputText id="FaultStrikeAngle" 
							value="#{DislocBean2.currentFault.faultStrikeAngle}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="r54r4djre32" value="degrees"/> 
 
					<h:outputText  id="stuff33" value="Strike Slip:" /> 
					<h:panelGroup  id="stuff35"> 
						<h:inputText id="FaultStrikeSlip" 
							value="#{DislocBean2.currentFault.faultStrikeSlip}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="er9e2djre32" value="cm"/> 
 
					<h:outputText  id="stuff34" value="Tensile Slip:" /> 
					<h:panelGroup  id="stuff36"> 
						<h:inputText id="FaultTensileSlip" 
							value="#{DislocBean2.currentFault.faultTensileSlip}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="ee3e33c3" value="cm"/> 

 
					<h:outputText  id="stuff37" value="Lame Lambda:" /> 
					<h:panelGroup  id="stuff38"> 
						<h:inputText id="LameLambda" 
							value="#{DislocBean2.currentFault.faultLameLambda}" required="true" /> 
					</h:panelGroup> 
					<h:outputText  id="e34343" value="N/A"/> 
 
					<h:outputText  id="stuff39" value="Lame Mu:" /> 
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
				      <input id="domath" type="button" name="Update" value="Do Math" onclick="doMath()"/>
 				   </f:verbatim>
				</h:panelGrid>
			</h:form> 
