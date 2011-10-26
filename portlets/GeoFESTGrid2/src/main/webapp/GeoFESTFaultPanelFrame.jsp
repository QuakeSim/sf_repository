			<h:form id="Faultform" rendered="#{MGBean.renderCreateNewFaultForm}">
				<h:panelGrid id="FaultTable" columns="2" footerClass="subtitle"
					headerClass="subtitlebig" styleClass="medium"
					columnClasses="subtitle,medium">

					<f:facet name="header">
						<h:outputFormat id="output3" escape="false"
							value="<b>Input Fault Geometry </b>" />
					</f:facet>

					<h:outputText value="Fault Name:" id="kld1"/>
					<h:panelGroup id="kld2">
						<h:inputText id="FaultName"
							value="#{MGBean.currentFault.faultName}" required="true" />
						<h:message for="FaultName" showDetail="true" showSummary="true"
									  id="stuff45"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText  id="kld3" value="Location X:" />
					<h:panelGroup  id="kld4">
						<h:inputText id="FaultLocationX"
							value="#{MGBean.currentFault.faultLocationX}" required="true" />
						<h:message for="FaultLocationX" showDetail="true"
												id="stuff55"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Location Y:"  id="kld5"/>
					<h:panelGroup  id="kld6">
						<h:inputText id="FaultLocationY"
							value="#{MGBean.currentFault.faultLocationY}" required="true" />
						<h:message for="FaultLocationY" showDetail="true"
									  id="stuff56"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Length:"  id="kld8"/>
					<h:panelGroup id="kld7">
						<h:inputText id="FaultLength"
							value="#{MGBean.currentFault.faultLength}" required="true" />
						<h:message for="FaultLength" showDetail="true" showSummary="true"
									 id="stuff57"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Width:"  id="kld9"/>
					<h:panelGroup  id="kld10">
						<h:inputText id="FaultWidth"
							value="#{MGBean.currentFault.faultWidth}" required="true" />
						<h:message for="FaultWidth" showDetail="true" showSummary="true"
												id="stuff57"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Depth:"  id="kld11"/>
					<h:panelGroup  id="kld21">
						<h:inputText id="FaultDepth"
							value="#{MGBean.currentFault.faultDepth}" required="true" />
						<h:message for="FaultDepth" showDetail="true" showSummary="true"
									  id="stuff58"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Dip Angle:"  id="kld22"/>
					<h:panelGroup  id="kld23">
						<h:inputText id="FaultDipAngle"
							value="#{MGBean.currentFault.faultDipAngle}" required="true" />
						<h:message for="FaultDipAngle" showDetail="true"
									  id="stuff59"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Strike Angle:"  id="kld24"/>
					<h:panelGroup  id="kld25">
						<h:inputText id="FaultStrikeAngle"
							value="#{MGBean.currentFault.faultStrikeAngle}" required="true" />
						<h:message for="FaultStrikeAngle" showDetail="true"
									  id="stuff60"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Slip:"  id="kld26"/>
					<h:panelGroup  id="kld27">
						<h:inputText id="FaultSlip"
							value="#{MGBean.currentFault.faultSlip}" required="true" />
						<h:message for="FaultSlip" showDetail="true" showSummary="true"
									   id="stuff61"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Rake Angle:"  id="kld28"/>
					<h:panelGroup  id="kld29">
						<h:inputText id="FaultRakeAngle"
							value="#{MGBean.currentFault.faultRakeAngle}" required="true" />
						<h:message for="FaultRakeAngle" showDetail="true"
									   id="stuff62"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>
					<h:commandButton id="addfault" value="select"
						actionListener="#{MGBean.toggleAddFaultForProject}" />
				</h:panelGrid>
			</h:form>
