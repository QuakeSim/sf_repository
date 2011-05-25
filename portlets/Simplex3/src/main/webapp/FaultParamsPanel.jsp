<h:panelGrid id="kherl189"
				 columns="1"
				 border="1"
				 rendered="#{SimplexBean.currentEditProjectForm.renderCreateNewFaultForm}">

  <h:form id="Faultform">	 
	 <h:panelGrid id="FaultTable" columns="3" footerClass="subtitle"
					  headerClass="subtitlebig" styleClass="medium"
					  columnClasses="subtitle,medium">
		
		<f:facet name="header">
		  <h:outputFormat id="output3" escape="false"
								value="<b>Input Fault Geometry</b>" />
		</f:facet>
		
		<h:outputText id="lkdmt117" value="Fault Name:" />
		<h:panelGroup id="lkbarq118">
		  <h:inputText id="FaultName"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultName}"
							required="true" />
		  <h:message for="FaultName" showDetail="true" showSummary="true"
						 errorStyle="color: red" />
		</h:panelGroup>
		<h:outputText value=""/>
		
		<h:outputText id="lkdrq119" value="Location X:" />
		<h:panelGroup id="lkdrq1181">
		  <h:inputText id="FaultLocationX"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLocationX}"
							required="true" />
		  <h:message id="lkdrq1182" for="FaultLocationX" showDetail="true"
						 showSummary="true" errorStyle="color: red" />
		</h:panelGroup>
		
		<h:selectBooleanCheckbox id="faultOriginXVary"
										 value="#{SimplexBean.currentEditProjectForm.currentFault.faultOriginXVary}" />
		<h:outputText id="lkdrq1183" value="Location Y:" />
		<h:panelGroup id="lkdrq1184">
		  <h:inputText id="FaultLocationY"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLocationY}"
							required="true" />
		  <h:message id="lkdrq1185" for="FaultLocationY" showDetail="true"
						 showSummary="true" errorStyle="color: red" />
		</h:panelGroup>
		
		<h:selectBooleanCheckbox id="faultOriginYVary"
										 value="#{SimplexBean.currentEditProjectForm.currentFault.faultOriginYVary}" />
		<h:outputText id="lkdrq1186" value="Length:" />
		<h:panelGroup id="lkdrq1187">
		  <h:inputText id="FaultLength"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLength}"
							required="true" />
		  <h:message id="lkdrq1188" for="FaultLength" showDetail="true" showSummary="false"
						 errorStyle="color: red" />
		</h:panelGroup>
		<h:selectBooleanCheckbox id="faultLengthVary"
										 value="#{SimplexBean.currentEditProjectForm.currentFault.faultLengthVary}" />
		<h:outputText id="lkdrq1189" value="Width:" />
		<h:panelGroup id="lkdwao11">
		  <h:inputText id="FaultWidth"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultWidth}"
							required="true" />
		  <h:message id="lkps2" for="FaultWidth" showDetail="true" showSummary="true"
						 errorStyle="color: red" />
		</h:panelGroup>
		<h:selectBooleanCheckbox id="faultWidthVary"
										 value="#{SimplexBean.currentEditProjectForm.currentFault.faultWidthVary}" />
		
		<h:outputText id="lkdrnae813" value="Depth:" />
		<h:panelGroup id="lkoea814">
		  <h:inputText id="FaultDepth"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultDepth}"
							required="true" />
		  <h:message id="lkikkm15" for="FaultDepth" showDetail="true" showSummary="true"
						 errorStyle="color: red" />
		</h:panelGroup>
		<h:selectBooleanCheckbox id="faultDepthVary"
										 value="#{SimplexBean.currentEditProjectForm.currentFault.faultDepthVary}" />
		
		<h:outputText id="lkasdf1" value="Dip Angle:" />
		<h:panelGroup id="lkdnadf317">
		  <h:inputText id="FaultDipAngle"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultDipAngle}"
							required="true" />
		  <h:message id="lafabw8" for="FaultDipAngle" showDetail="true"
						 showSummary="true" errorStyle="color: red" />
		</h:panelGroup>
		<h:selectBooleanCheckbox id="faultDipAngleVary"
										 value="#{SimplexBean.currentEditProjectForm.currentFault.faultDipAngleVary}" />
		
		<h:outputText id="lkdrbas19" value="Strike Angle:" />
		<h:panelGroup id="bae2lerba">
		  <h:inputText id="FaultStrikeAngle"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultStrikeAngle}"
							required="false" />
		  <h:message id="dflelbaa42" for="FaultStrikeAngle" showDetail="true"
						 showSummary="true" errorStyle="color: red" />
		</h:panelGroup>
		<h:selectBooleanCheckbox id="faultStrikeAngleVary"
										 value="#{SimplexBean.currentEditProjectForm.currentFault.faultStrikeAngleVary}" />
		
		<h:outputText id="dbad23" value="Dip Slip:" />
		<h:panelGroup id="dfbt211">
		  <h:inputText id="FaultSlip"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultSlip}"
							required="true" />
		  <h:message for="FaultSlip" showDetail="true" showSummary="true"
						 errorStyle="color: red" />
		</h:panelGroup>
		<h:selectBooleanCheckbox id="faultDipSlipVary"
										 value="#{SimplexBean.currentEditProjectForm.currentFault.faultDipSlipVary}" />
		
		<h:outputText id="dfbaf341" value="Strike Slip:" />
		<h:panelGroup id="dfbad133">
		  <h:inputText id="FaultRakeAngle"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultRakeAngle}"
							required="true" />
		  <h:message id="dflzzz17" for="FaultRakeAngle" showDetail="true"
						 showSummary="true" errorStyle="color: red" />
		</h:panelGroup>
		<h:selectBooleanCheckbox id="faultStrikeSlipVary"
										 value="#{SimplexBean.currentEditProjectForm.currentFault.faultStrikeSlipVary}" />
		
		<h:outputText id="dfadfaa8" value="Fault Lon Starts:" />
		<h:panelGroup id="dnnnak9">
		  <h:inputText id="FaultLonStarts"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLonStarts}" />
		</h:panelGroup>
		<h:outputText  id="dnnasd10" value="optional" />
		
		<h:outputText id="dfleanad11" value="Fault Lat Starts:" />
		<h:panelGroup id="dflelerkljk12">
		  <h:inputText id="FaultLatStarts"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLatStarts}" />
		</h:panelGroup>
		<h:outputText  id="dfadfb43k13" value="optional" />
		
		<h:outputText  id="dflelerkljk14" value="Fault Lon Ends:" />
		<h:panelGroup id="dflelerkljk15">
		  <h:inputText id="FaultLonEnds"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLonEnds}" />
		</h:panelGroup>
		<h:outputText  id="dflelerkljk16" value="optional" />
		
		<h:outputText id="dflelerkljk17" value="Fault Lat Ends:" />
		<h:panelGroup id="dflelerkljk18">
		  <h:inputText id="FaultLatEnds"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLatEnds}" />
		</h:panelGroup>
		<h:outputText  id="dflelerkljk19" value="optional" />
		
		<h:commandButton id="addfault" value="Set Values"
							  actionListener="#{SimplexBean.toggleAddFaultForProject}" />
		<f:verbatim>
		  <input id="domath" type="button" name="Update" value="Calculate length" onclick="calculatelength()"/>
		  <input id="domath2" type="button" name="Update2" value="Calculate endpoint" onclick="calculateendpoint()"/>
		</f:verbatim>
		<f:facet name="footer"> 
		  <h:outputFormat id="output2" escape="false" 
								value="Click 'Do Math' to udpate length and strike. 
										 Click 'Set Values' when you are done." /> 
		</f:facet> 
		
	 </h:panelGrid>
  </h:form>
</h:panelGrid>
