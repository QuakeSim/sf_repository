<h:panelGrid id="ProjectComponentList" 
				 rendered="#{!empty DislocBean2.myFaultEntryForProjectList 
							  || !empty DislocBean2.myObsvEntryForProjectList}"
				 columns="1" 
				 border="0">
  <h:panelGroup id="dislocFaultContainerPanel">
  <h:form id="UpdateSelectFaultsForm" 
			 rendered="#{!empty DislocBean2.myFaultEntryForProjectList 
						  || !empty DislocBean2.myObsvEntryForProjectList}"> 
	 <h:outputText id="dflelerkljk165" 
						escape="false" 
						styleClass="h3"
						value="Fault Components"/>
	 <h:panelGrid id="dislocdirtyworkaroundgroup"
					  columns="2">
		<h:outputText id="lierlk3" 
						  value="1) Please click the 'update' button after value changes." 
						  escape="false"/>
		<h:outputText id="elirelh" 
						  value="2) Please click the checkbox next to the 'name' to 
									draw the fault on the map" 
						  escape="false"/>
	 </h:panelGrid>
	 <h:panelGrid id="dislocFaultPanelGridOuter" 
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
				 <h:outputText id="dflelerh967" escape="false" value="<b>Name</b>" />
			  </f:facet>
			  
			  <h:panelGrid columns="2" cellpadding="0" cellspacing="0" id="kljerje22">
				 <h:inputText id="dflelerklh968" 
								  style="text-align:right;width:60px" 
								  value="#{myentry31.faultName}" 
								  required="true" />
				 <h:selectBooleanCheckbox id="faultnamec" onclick="togglefaultname(this)" value="" />
			  </h:panelGrid>
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrh119" value="Location X" />
			  </f:facet>
			  <h:panelGrid columns="1" cellpadding="0" cellspacing="0" id="ljerlke12j">
				 <h:inputText id="FaultLocationX2" style="text-align:right;width:60px"
								  value="#{myentry31.faultLocationX}" required="true" />
			  </h:panelGrid>
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrqh1183" value="Location Y" />
			  </f:facet>
			  <h:panelGrid columns="1" cellpadding="0" cellspacing="0" id="ljlejre122">
				 <h:inputText id="FaultLocationY2" style="text-align:right;width:60px" 
								  value="#{myentry31.faultLocationY}"
								  required="true" />
			  </h:panelGrid>
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrqh1351" value="Fault Origin Lat" />
			  </f:facet>
			  <h:inputText id="FaultLatStart" style="text-align:right;width:60px" 
								value="#{myentry31.faultLatStart}"
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrqh1352" value="Fault Origin Lon" />
			  </f:facet>
				 <h:inputText id="FaultLonStart" style="text-align:right;width:60px" 
								  value="#{myentry31.faultLonStart}"
								  required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrqh1186" value="Fault End Lat" />
			  </f:facet>
			  <h:inputText id="FaultLatEnd" style="text-align:right;width:60px" 
								value="#{myentry31.faultLatEnd}"
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrh1189" value="Fault End Lon" />
			  </f:facet>
				 <h:inputText id="FaultLonEnd" style="text-align:right;width:60px" 
								  value="#{myentry31.faultLonEnd}"
								  required="true" />
			</h:column>						
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrq11842" value="Length" />
			  </f:facet>
			  <h:inputText id="FaultLength" style="text-align:right;width:60px" 
								  value="#{myentry31.faultLength}"
								  required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrq11813" value="Width" />
			  </f:facet>
			  <h:inputText id="FaultWidth" style="text-align:right;width:60px" 
								value="#{myentry31.faultWidth}"
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrq11213" value="Depth" />
			  </f:facet>
			  <h:inputText id="FaultDepth" style="text-align:right;width:60px" 
								value="#{myentry31.faultDepth}"
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrqj11816" value="Dip Angle" />
			  </f:facet>
			  <h:inputText id="FaultDipAngle" style="text-align:right;width:60px" 
								value="#{myentry31.faultDipAngle}"
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="dflelerk123" value="Dip Slip" />
			  </f:facet>
			  <h:inputText id="FaultSlip" style="text-align:right;width:60px" 
								value="#{myentry31.faultDipSlip}"
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="lkdrq1521" value="Strike Angle" />
			  </f:facet>
			  <h:inputText id="FaultStrikeAngle" style="text-align:right;width:60px" 
								value="#{myentry31.faultStrikeAngle}"
								required="false" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="dflelerkljj61" value="Strike Slip" />
			  </f:facet>
			  <h:inputText id="FaultStrikeSlip" style="text-align:right;width:60px" 
								value="#{myentry31.faultStrikeSlip}"
								required="true" />
			</h:column>						
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="dflelerklj1252" value="Tensile Slip" />
			  </f:facet>
			  <h:inputText id="FaultTensileSlip" style="text-align:right;width:60px" 
								value="#{myentry31.faultTensileSlip}" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText id="dflelerkljj81" value="Lame Lambda" />
			  </f:facet>
			  <h:inputText id="LameLambda" style="text-align:right;width:60px" 
								value="#{myentry31.faultLameLambda}" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <h:outputText  id="dflelerkljj171" value="Lame Mu" />
			  </f:facet>
				 <h:inputText id="LameMu" style="text-align:right;width:60px" 
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
</h:panelGroup>
</h:panelGrid>
