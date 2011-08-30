<h:panelGrid id="ProjectComponentList" 
				 rendered="#{!empty DislocBean2.myFaultEntryForProjectList}"
				 columns="1" 
				 border="0">
  <h:panelGroup id="dislocFaultContainerPanel">
	 <f:verbatim>
		<fieldset style="width:920px"><legend class="portlet-form-label">Fault Components</legend>
	 </f:verbatim>	 
  <h:form id="UpdateSelectFaultsForm" 
			 rendered="#{!empty DislocBean2.myFaultEntryForProjectList 
						  || !empty DislocBean2.myObsvEntryForProjectList}"> 
	 <h:panelGrid id="dislocdirtyworkaroundgroup"
					  columns="2">
		<f:verbatim>
		  Below are the faults you have added to your project.  You can update the
		  fault model parameters by 1) editing the value in the box, 2) clicking the "Update"
		  checkbox, and 3) clicking the "Update Fault" button.
		</f:verbatim>
	 </h:panelGrid>
	 <h:panelGrid id="dislocFaultPanelGridOuter" 
					  style="width:920px" 
					  columns="1" border="0" cellpadding="0" cellspacing="0">
		<h:panelGrid id="dflelerkljg162" 
						 columns="2" 
						 border="0"  
						 cellpadding="0" 
						 cellspacing="0" 
						 columnClasses="alignTop,alignTop">
		  
	    <h:dataTable border="1" 
						  cellpadding="0" 
						  cellspacing="0" 
						  id="dflelerh966" 
						  headerClass="componentstableh" 
						  columnClasses="componentstablec"
						  value="#{DislocBean2.myFaultsForProjectList}" var="myentry31">
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="dflelerh967" escape="false" value="Name" />
			  </f:facet>
			  <h:panelGrid columns="2" cellpadding="0" cellspacing="0" id="kljerje22">
				 <h:inputText id="dflelerklh968" 
								  style="text-align:right;width:35px" 
								  value="#{myentry31.faultName}" 
								  required="true" />
			  </h:panelGrid>
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrh119" value="X" />
			  </f:facet>
			  <h:panelGrid columns="1" cellpadding="0" cellspacing="0" id="ljerlke12j">
				 <h:inputText id="FaultLocationX2" style="text-align:right;width:30px"
								  value="#{myentry31.faultLocationX}" required="true" />
			  </h:panelGrid>
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrqh1183" value="Y" />
			  </f:facet>
			  <h:panelGrid columns="1" cellpadding="0" cellspacing="0" id="ljlejre122">
				 <h:inputText id="FaultLocationY2" style="text-align:right;width:30px" 
								  value="#{myentry31.faultLocationY}"
								  required="true" />
			  </h:panelGrid>
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrqh1351" value="Start Lat" />
			  </f:facet>
			  <h:inputText id="FaultLatStart" style="text-align:right;width:30px" 
								value="#{myentry31.faultLatStart}"
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrqh1352" value="Start Lon" />
			  </f:facet>
				 <h:inputText id="FaultLonStart" style="text-align:right;width:30px" 
								  value="#{myentry31.faultLonStart}"
								  required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrqh1186" value="End Lat" />
			  </f:facet>
			  <h:inputText id="FaultLatEnd" style="text-align:right;width:30px" 
								value="#{myentry31.faultLatEnd}"
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrh1189" value="End Lon" />
			  </f:facet>
				 <h:inputText id="FaultLonEnd" style="text-align:right;width:30px" 
								  value="#{myentry31.faultLonEnd}"
								  required="true" />
			</h:column>						

			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrq1521" value="Strike Angle" />
			  </f:facet>
			  <h:inputText id="FaultStrikeAngle" style="text-align:right;width:30px" 
								value="#{myentry31.faultStrikeAngle}"
								required="false" />
			</h:column>

			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrqj11816" value="Dip Angle" />
			  </f:facet>
			  <h:inputText id="FaultDipAngle" style="text-align:right;width:30px" 
								value="#{myentry31.faultDipAngle}"
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrq11213" value="Depth" />
			  </f:facet>
			  <h:inputText id="FaultDepth" style="text-align:right;width:30px" 
								value="#{myentry31.faultDepth}"
								required="true" />
			</h:column>

			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrq11813" value="Width" />
			  </f:facet>
			  <h:inputText id="FaultWidth" style="text-align:right;width:30px" 
								value="#{myentry31.faultWidth}"
								required="true" />
			</h:column>
						
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrq11842" value="Length" />
			  </f:facet>
			  <h:inputText id="FaultLength" style="text-align:right;width:30px" 
								  value="#{myentry31.faultLength}"
								  required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="dflelerkljj61" value="Strike Slip" />
			  </f:facet>
			  <h:inputText id="FaultStrikeSlip" style="text-align:right;width:30px" 
								value="#{myentry31.faultStrikeSlip}"
								required="true" />
			</h:column>						

			<h:column>
			  <f:facet name="header">
				 <h:outputText id="dflelerk123" value="Dip Slip" />
			  </f:facet>
			  <h:inputText id="FaultSlip" style="text-align:right;width:30px" 
								value="#{myentry31.faultDipSlip}"
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="dflelerkljj81" value="Lame Lambda" />
			  </f:facet>
			  <h:inputText id="LameLambda" style="text-align:right;width:30px" 
								value="#{myentry31.faultLameLambda}" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText  id="dflelerkljj171" value="Lame Mu" />
			  </f:facet>
				 <h:inputText id="LameMu" style="text-align:right;width:30px" 
								  value="#{myentry31.faultLameMu}" />
			</h:column>
		 </h:dataTable>

		 <h:dataTable 
			  border="1" 
			  cellpadding="0" 
			  cellspacing="0" 
			  headerClass="componentstableh2" 
			  columnClasses="componentstablec"
			  id="dflelerkljk451"
			  value="#{DislocBean2.myFaultEntryForProjectList}" 
			  var="myentry3">
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="dflelerkljk454" value="Update Values" />
			  </f:facet>
			  <h:selectBooleanCheckbox value="#{myentry3.update}" id="dflelerkljk455"/>
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="dflelerkljk456" value="Remove Fault" />
			  </f:facet>									
			  <h:selectBooleanCheckbox id="dflelerkljk457" value="#{myentry3.delete}"/>
			</h:column>
		 </h:dataTable>

	</h:panelGrid>		
 </h:panelGrid>				
 
 <h:commandButton id="SelectFault4projj" value="UpdateFault"
						actionListener="#{DislocBean2.toggleUpdateFaults}" />
</h:form>
<f:verbatim></fieldset></f:verbatim>
</h:panelGroup>
</h:panelGrid>
