<h:panelGrid id="ProjectComponentList" columns="1" border="0">
  <h:panelGroup id="dflelerkljk161">
    <h:form id="UpdateSelectFaultsForm" rendered="#{!empty DislocBean2.myFaultEntryForProjectList || !empty DislocBean2.myObsvEntryForProjectList}"> 
      <h:outputText value="<br/>" escape="false"/>      
      <h:outputText value="1) Please click the 'update' button after value changes." escape="false"/>
      <h:outputText value="2) Please click the checkbox next to the 'name' to draw the fault on the map" escape="false"/>
	<h:panelGrid id="dflelerkljk162" columns="1" border="1" cellpadding="0" cellspacing="0">
	  <h:outputFormat id="dflelerkljk165" escape="false" value="<b>Fault Components</b>">
	  </h:outputFormat>
	  <h:panelGrid id="dflelerkljg162" columns="2" border="0"  cellpadding="0" cellspacing="0">
	    <h:dataTable border="1" cellpadding="0" cellspacing="0" id="dflelerh966" headerClass="componentstableh" columnClasses="componentstablec"
				value="#{DislocBean2.myFaultsForProjectList}" var="myentry31">
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
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultLocationX2" style="text-align:right;width:60px"
		      value="#{myentry31.faultLocationX}" required="true" />
		    <h:message id="FaultLocationX2" for="FaultLocationX2" showDetail="true"
		      showSummary="true" errorStyle="color: red" />
		  </h:panelGrid>
		</h:column>

		<h:column>
		  <f:facet name="header">
		    <h:outputText id="lkdrqh1183" value="Location Y" />
		  </f:facet>
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultLocationY2" style="text-align:right;width:60px" 
		      value="#{myentry31.faultLocationY}"
		      required="true" />
		    <h:message id="FaultLocationY2" for="FaultLocationY" showDetail="true"
		      showSummary="true" errorStyle="color: red" />	
		  </h:panelGrid>
		</h:column>

		<h:column>
		  <f:facet name="header">
		    <h:outputText id="lkdrqh1351" value="Fault Origin Latitude" />
		  </f:facet>
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultLatStart" style="text-align:right;width:60px" 
		      value="#{myentry31.faultLatStart}"
		      required="true" />
		    <h:message id="lkdrae643" for="FaultLatStart" showDetail="true" showSummary="false"
		      errorStyle="color: red" />
		  </h:panelGrid>
		</h:column>

		<h:column>
		  <f:facet name="header">
		    <h:outputText id="lkdrqh1352" value="Fault Origin Longitude" />
		  </f:facet>
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultLonStart" style="text-align:right;width:60px" 
		      value="#{myentry31.faultLonStart}"
		      required="true" />
		    <h:message id="lkdrae645" for="FaultLonStart" showDetail="true" showSummary="false"
		      errorStyle="color: red" />
		  </h:panelGrid>
		</h:column>


		<h:column>
		  <f:facet name="header">
		    <h:outputText id="lkdrqh1186" value="Fault End Latitude" />
		  </f:facet>
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultLatEnd" style="text-align:right;width:60px" 
		      value="#{myentry31.faultLatEnd}"
		      required="true" />
		    <h:message id="lkdrae188" for="FaultLatEnd" showDetail="true" showSummary="false"
		      errorStyle="color: red" />
		  </h:panelGrid>
		</h:column>

		<h:column>
		  <f:facet name="header">
		    <h:outputText id="lkdrh1189" value="Fault End Longitude" />
		  </f:facet>
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultLonEnd" style="text-align:right;width:60px" 
		      value="#{myentry31.faultLonEnd}"
		      required="true" />
		    <h:message id="lkdrqh11812" for="LonEnd" showDetail="true" showSummary="true"
		      errorStyle="color: red" />
		  </h:panelGrid>
		</h:column>						

		<h:column>
		  <f:facet name="header">
		    <h:outputText id="lkdrq11842" value="Length" />
		  </f:facet>
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultLength" style="text-align:right;width:60px" 
		      value="#{myentry31.faultLength}"
		      required="true" />
		    <h:message id="lkdrqr11855" for="FaultLength" showDetail="true" showSummary="true"
		      errorStyle="color: red" />
		  </h:panelGrid>
		</h:column>

	    
		<h:column>
		  <f:facet name="header">
		    <h:outputText id="lkdrq11813" value="Width" />
		  </f:facet>
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultWidth" style="text-align:right;width:60px" 
		      value="#{myentry31.faultWidth}"
		      required="true" />
		    <h:message id="lkdrqr11119" for="FaultWidth" showDetail="true" showSummary="true"
		      errorStyle="color: red" />
		  </h:panelGrid>
		</h:column>

		<h:column>
		  <f:facet name="header">
		    <h:outputText id="lkdrq11213" value="Depth" />
		  </f:facet>
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultDepth" style="text-align:right;width:60px" 
		      value="#{myentry31.faultWidth}"
		      required="true" />
		    <h:message id="lkdrqr11512" for="FaultDepth" showDetail="true" showSummary="true"
		      errorStyle="color: red" />
		  </h:panelGrid>
		</h:column>

		<h:column>
		  <f:facet name="header">
		    <h:outputText id="lkdrqj11816" value="Dip Angle" />
		  </f:facet>
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultDipAngle" style="text-align:right;width:60px" 
		      value="#{myentry31.faultDipAngle}"
		      required="true" />
		    <h:message id="lkdrqj11421" for="FaultDipAngle" showDetail="true"
		      showSummary="true" errorStyle="color: red" />	
		  </h:panelGrid>
		</h:column>

		<h:column>
		  <f:facet name="header">
		    <h:outputText id="dflelerk123" value="Dip Slip" />
		  </f:facet>
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultSlip" style="text-align:right;width:60px" 
		      value="#{myentry31.faultDipSlip}"
		      required="true" />
		    <h:message for="FaultSlip" showDetail="true" showSummary="true"
		      errorStyle="color: red" />
		  </h:panelGrid>
		</h:column>


		<h:column>
		  <f:facet name="header">
		    <h:outputText id="lkdrq1521" value="Strike Angle" />
		  </f:facet>
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultStrikeAngle" style="text-align:right;width:60px" 
		      value="#{myentry31.faultStrikeAngle}"
		      required="false" />
		    <h:message id="dflelerklj5" for="FaultStrikeAngle" showDetail="true"
		      showSummary="true" errorStyle="color: red" />
		  </h:panelGrid>
		</h:column>


		<h:column>
		  <f:facet name="header">
		    <h:outputText id="dflelerkljj61" value="Strike Slip" />
		  </f:facet>
		  <h:panelGrid columns="2" cellpadding="0" cellspacing="0">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultStrikeSlip" style="text-align:right;width:60px" 
		      value="#{myentry31.faultStrikeSlip}"
		      required="true" />
		    <h:message  id="dflelerkljj7" for="FaultStrikeSlip" showDetail="true"
		      showSummary="true" errorStyle="color: red" />
		  </h:panelGrid>
		</h:column>						
    
		<h:column>
		  <f:facet name="header">
		    <h:outputText id="dflelerklj1252" value="Tensile Slip" />
		  </f:facet>
		  <h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="centered">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="FaultTensileSlip" style="text-align:right;width:60px" 
		      value="#{myentry31.faultTensileSlip}" />
		  </h:panelGrid>
		</h:column>

		<h:column>
		  <f:facet name="header">
		    <h:outputText id="dflelerkljj81" value="LameLambda" />
		  </f:facet>
		  <h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="centered">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="LameLambda" style="text-align:right;width:60px" 
		      value="#{myentry31.faultLameLambda}" />
		  </h:panelGrid>
		</h:column>

		<h:column>
		  <f:facet name="header">
		    <h:outputText  id="dflelerkljj171" value="LameMu" />
		  </f:facet>
		  <h:panelGrid columns="1" cellpadding="0" cellspacing="0" styleClass="centered">
		    <f:facet name="header">								
		    </f:facet>
		    <h:inputText id="LameMu" style="text-align:right;width:60px" 
		      value="#{myentry31.faultLameMu}" />
		  </h:panelGrid>
		</h:column>
	  </h:dataTable>
  
	  <h:dataTable border="1" cellpadding="0" cellspacing="0" id="dflelerh966" headerClass="componentstableh2" columnClasses="componentstablec"
											  id="dflelerkljk451"
											  value="#{DislocBean2.myFaultEntryForProjectList}" var="myentry3">

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
						actionListener="#{DislocBean2.toggleUpdateFaults}" />
				</h:form>


		</h:panelGroup> 
	</h:panelGrid> 