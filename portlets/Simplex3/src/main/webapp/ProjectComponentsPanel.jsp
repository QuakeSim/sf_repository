<h:panelGrid id="ProjectComponentList" 
				 rendered="#{!empty SimplexBean.myFaultEntryForProjectList 
							  || !empty SimplexBean.myObservationsForProjectList}"
				 columns="1" 
				 border="0">
  <f:verbatim>
	 <script type="text/javascript">
		//<![CDATA[
      //--------------------------------------------------
      // This toggles displays of fieldsets.
		//--------------------------------------------------
		function toggleDisplay1(fieldset) {
		var panelGrid1=document.getElementById("UpdateSelectFaultsForm:dflelerkljg162");
		
		if(panelGrid1.style.display=='none') {
		panelGrid1.style.display='block';
		}
		else if (panelGrid1.style.display!='none'){
		panelGrid1.style.display='none';
		}
		else {
		panelGrid1.style.display='block';
		}
		}
		
		function toggleDisplay2(fieldset) {
		var panelGrid=document.getElementById("UpdateSelectObservationForm:aerbasd1");
		
		if(panelGrid.style.display=='none') {
		panelGrid.style.display='block';
		}
		else if (panelGrid.style.display!='none'){
		panelGrid.style.display='none';
		}
		else {
		panelGrid.style.display='block';
		}
		}
		
		  //]]>
	 </script>
  </f:verbatim>
  <h:panelGroup id="dflelerkljk161">
	 <h:form id="UpdateSelectFaultsForm"
				rendered="#{!empty SimplexBean.myFaultEntryForProjectList}">
		<f:verbatim>
		  <fieldset ondblclick="toggleDisplay1(this)">
			 <legend class="portlet-form-label">Fault Components</legend>
			 <b>To update values: </b>Please click the 'update' button after value changes. 
			 Click the checkbox if you want to allow Simplex to vary the value during
			 optimization. 
			 <u>Toggle the fault display on/off by double clicking anywhere in this fieldset.</u>
			 <p/>
			 
		  </f:verbatim>

		  <h:panelGrid id="dflelerkljg162" columns="2" border="0"  cellpadding="0" cellspacing="0">
			 <h:dataTable border="1" 
							  cellpadding="0" 
							  cellspacing="0" 
							  id="dflelegq24" 
							  headerClass="componentstableh2" 
							  columnClasses="componentstablec"											  
							  value="#{SimplexBean.myFaultEntryForProjectList}" 
							  var="myentry3">
				<h:column>
				  <f:facet name="header">
					 <f:verbatim><b>Update</b></f:verbatim>
				  </f:facet>
				  <h:selectBooleanCheckbox value="#{myentry3.update}" 
													style="text-align:center"
													id="dflelerkljk455"/>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <f:verbatim><b>Remove</b></f:verbatim>
				  </f:facet>													  
				  <h:selectBooleanCheckbox id="dflelerkljk457" 
													style="text-align:center"
													value="#{myentry3.delete}"/>
				</h:column>
			 </h:dataTable>

			 <h:dataTable id="dflelerh966"  
							  border="1" 
							  cellpadding="0" 
							  cellspacing="0"  
							  headerClass="componentstableh" 
							  columnClasses="componentstablec"
							  value="#{SimplexBean.myFaultsForProjectList}" 
							  var="myentry31">
				<h:column>
				  <f:facet name="header">
					 <f:verbatim><b>Name</b></f:verbatim>
				  </f:facet>
				  <h:panelGrid  id="spxpcp5" columns="2" cellpadding="0" cellspacing="0">
					 <h:inputText id="dflelerklh968" 
									  size="15"
									  style="text-align:right" 
									  value="#{myentry31.faultName}" 
									  required="true" />
				  </h:panelGrid>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="lkdrh119" value="X" />
				  </f:facet>
				  <h:panelGrid  id="spxpcp6" columns="2" cellpadding="0" cellspacing="0">
					 <h:inputText id="FaultLocationX2" 
									  size="7"
									  style="text-align:right"
									  value="#{myentry31.faultLocationX}" 
									  required="true" />
					 <h:selectBooleanCheckbox id="faultOriginXVary2"
													  value="#{myentry31.faultOriginXVary}" />
				  </h:panelGrid>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="lkdrqh1183" value="Y" />
				  </f:facet>
				  
				  <h:panelGrid  id="spxpcp7" columns="2" cellpadding="0" cellspacing="0">
					 <h:inputText id="FaultLocationY2" 
									  size="7"
									  style="text-align:right" 
									  value="#{myentry31.faultLocationY}"
									  required="true" />
					 <h:selectBooleanCheckbox id="faultOriginYVary2"
													  value="#{myentry31.faultOriginYVary}" />
				  </h:panelGrid>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="lkdrqh1186" value="Length" />
				  </f:facet>
				  <h:panelGrid  id="spxpcp8" columns="2" cellpadding="0" cellspacing="0">
					 <h:inputText id="FaultLength2" 
									  size="7"
									  style="text-align:right" 
									  value="#{myentry31.faultLength}"
									  required="true" />
					 <h:selectBooleanCheckbox id="faultLengthVary2"
													  value="#{myentry31.faultLengthVary}" />
				  </h:panelGrid>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="lkdrh1189" value="Width" />
				  </f:facet>
				  <h:panelGrid  id="spxpcp9" columns="2" cellpadding="0" cellspacing="0">
					 
					 <h:inputText id="FaultWidth2" 
									  size="7"
									  style="text-align:right" 
									  value="#{myentry31.faultWidth}"
									  required="true" />
					 <h:selectBooleanCheckbox id="faultWidthVary2"
													  value="#{myentry31.faultWidthVary}" />
				  </h:panelGrid>
				</h:column>						
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="lkdrq11813" value="Depth" />
				  </f:facet>
				  <h:panelGrid id="spxpcp10" columns="2" cellpadding="0" cellspacing="0">
					 <f:facet name="header">								
					 </f:facet>
					 <h:inputText id="FaultDepth2" 
									  size="7"
									  style="text-align:right" 
									  value="#{myentry31.faultDepth}"
									  required="true" />
					 <h:selectBooleanCheckbox id="faultDepthVary2"
													  value="#{myentry31.faultDepthVary}" />
				  </h:panelGrid>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="lkdrqj11816" value="Dip Angle" />
				  </f:facet>
				  <h:panelGrid id="spxpcp11" columns="2" cellpadding="0" cellspacing="0">
					 <h:inputText id="FaultDipAngle2" 
									  size="7"
									  style="text-align:right" 
									  value="#{myentry31.faultDipAngle}"
									  required="true" />
					 <h:selectBooleanCheckbox id="faultDipAngleVary2"
													  value="#{myentry31.faultDipAngleVary}" />
				  </h:panelGrid>
				</h:column>						
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="lkdrq11819" value="Strike Angle" />
				  </f:facet>
				  <h:panelGrid id="spxpcp12" columns="2" cellpadding="0" cellspacing="0">
					 <f:facet name="header">								
					 </f:facet>
					 <h:inputText id="FaultStrikeAngle2" 
									  size="7"
									  style="text-align:right" 
									  value="#{myentry31.faultStrikeAngle}"
									  required="false" />
					 <h:selectBooleanCheckbox id="faultStrikeAngleVary2"
													  value="#{myentry31.faultStrikeAngleVary}" />
				  </h:panelGrid>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljj3" value="Dip Slip" />
				  </f:facet>
				  <h:panelGrid id="spxpcp13" columns="2" cellpadding="0" cellspacing="0">
					 <h:inputText id="FaultSlip2" 
									  size="7"
									  style="text-align:right" 
									  value="#{myentry31.faultSlip}"
									  required="true" />
					 <h:selectBooleanCheckbox id="faultDipSlipVary2"
													  value="#{myentry31.faultDipSlipVary}" />
				  </h:panelGrid>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljj5" value="Strike Slip" />
				  </f:facet>
				  <h:panelGrid id="spxpcp14" columns="2" cellpadding="0" cellspacing="0">
					 <f:facet name="header">								
					 </f:facet>
					 <h:inputText id="FaultRakeAngle2" 
									  size="7"
									  style="text-align:right" 
									  value="#{myentry31.faultRakeAngle}"
									  required="true" />
					 <h:selectBooleanCheckbox id="faultStrikeSlipVary2"
													  value="#{myentry31.faultStrikeSlipVary}" />
				  </h:panelGrid>
				</h:column>						
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljj8" value="Fault Lon Starts" />
				  </f:facet>
				  <h:panelGrid id="spxpcp15" columns="1" 
									cellpadding="0" cellspacing="0" styleClass="centered">
					 <h:inputText id="FaultLonStarts2" 
									  size="7"
									  style="text-align:right" 
									  value="#{myentry31.faultLonStarts}" />
				  </h:panelGrid>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljj11" value="Fault Lat Starts" />
				  </f:facet>
				  <h:panelGrid id="spxpcp16" 
									columns="1" 
									cellpadding="0" 
									cellspacing="0" 
									styleClass="centered">
					 <f:facet name="header">								
					 </f:facet>
					 <h:inputText id="FaultLatStarts2" 
									  size="7"
									  style="text-align:center" 
									  value="#{myentry31.faultLatStarts}" />
				  </h:panelGrid>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText  id="dflelerkljj14" value="Fault Lon Ends" />
				  </f:facet>
				  <h:panelGrid id="spxpcp17" 
									columns="1" 
									cellpadding="0" 
									cellspacing="0" 
									styleClass="centered">
					 <h:inputText id="FaultLonEnds2" 
									  size="7"
									  style="text-align:center" 
									  value="#{myentry31.faultLonEnds}" />
				  </h:panelGrid>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljj17" value="Fault Lat Ends" />
				  </f:facet>
				  <h:panelGrid id="spxpcp18" 
									columns="1" 
									cellpadding="0" 
									cellspacing="0" 
									styleClass="centered">
					 <h:inputText id="FaultLatEnds2" 
									  size="7"
									  style="text-align:center" 
									  value="#{myentry31.faultLatEnds}" />
				  </h:panelGrid>
				</h:column>
			 </h:dataTable>
		</h:panelGrid>
		<h:commandButton id="SelectFault4projj" value="UpdateFault"
							  actionListener="#{SimplexBean.toggleUpdateFaults}" />
	 <f:verbatim></fieldset></f:verbatim>		  
	 </h:form>
	 
	 <h:form id="UpdateSelectObservationForm"
				rendered="#{!empty SimplexBean.myObservationsForProjectList}">
		<f:verbatim>
		  <fieldset ondblclick="toggleDisplay2(this)">
			 <legend class="portlet-form-label">Observation Components</legend>
			 Please click the 'update' button after value changes.
			 <u>Toggle the fault display on/off by double clicking anywhere in this fieldset.</u>
		</f:verbatim>
		
		<h:commandButton id="SelectObservations4proj1"					   
							  value="Update Observations"
							  actionListener="#{SimplexBean.toggleUpdateObservations}" />
		
		<h:panelGrid id="aerbasd1" 
						 style="display:none"
						 columns="2" 
						 border="0" 
						 cellpadding="0" 
						 cellspacing="0">    
		  
		  <h:dataTable border="1" 
							cellpadding="0" 
							cellspacing="0" 
							id="dflelerz166" 
							headerClass="componentstableh2" columnClasses="componentstablec"						
							value="#{SimplexBean.myObservationEntryForProjectList}" 
							var="myentry7">
			 
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljk954" 
										style="font:bold"
										escape="false" 
										value="Update" />
				  </f:facet>
				  
				  <h:panelGrid id="spxpcp31" columns="2" cellpadding="0" 
									cellspacing="0" styleClass="centered">
					 <h:selectBooleanCheckbox value="#{myentry7.update}" id="dflelerkljk955" />
					 <h:outputText id="spxpcp32"style="text-align:right;width:0px" value ="" />
				  </h:panelGrid>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljk956" 
										style="font:bold"
										escape="false" 
										value="Remove" />
				  </f:facet>
				  
				  <h:panelGrid id="spxpcp35" 
									columns="2" 
									cellpadding="0" 
									cellspacing="0" 
									styleClass="centered">
					 <h:selectBooleanCheckbox id="dflelerkljk957" 
													  value="#{myentry7.delete}"/>
					 <h:outputText id="spxpcp36" style="text-align:right;width:0px" value ="" />
				  </h:panelGrid>
				</h:column>
			 </h:dataTable>

			 <h:dataTable border="1" 
							  cellpadding="0" 
							  cellspacing="0" 
							  id="dflelerkljk1277" 
							  headerClass="componentstableh2" 
							  columnClasses="componentstablec"
							  value="#{SimplexBean.myObservationsForProjectList}"
							  var="myentry4">
				<h:column>
				  <f:facet name="header">
					 <f:verbatim><b>Name</b></f:verbatim>
				  </f:facet>
				  <h:outputText id="dflelerkljk179" 
									 value="#{myentry4.obsvName}" />
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <f:verbatim><b>Displacement Type</b></f:verbatim>
				  </f:facet>									
				  <h:outputText id="obsvTypeEast"
									 rendered="#{myentry4.obsvType==1}"
									 value="East"/>
				  <h:outputText id="obsvTypeNorth"
									 rendered="#{myentry4.obsvType==2}"
									 value="North"/>
				  <h:outputText id="obsvTypeUp"
									 rendered="#{myentry4.obsvType==3}"
									 value="Up"/>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljk548" 
										escape="false" 
										value="<b>Velocity Value</b>" />
				  </f:facet>
				  <h:inputText id="dflelerklj71" 
									size="7"
									value="#{myentry4.obsvValue}" />
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljk558" 
										escape="false" 
										value="<b>Error</b>" /></b>
				  </f:facet>
				  <h:inputText id="dflelerklj72" 
									size="7"
									value="#{myentry4.obsvError}" />
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljk568" 
										escape="false" 
										value="<b>Location East</b>" />
				  </f:facet>
				  <h:inputText id="dflelerklj73" 
									size="7"
									value="#{myentry4.obsvLocationEast}" />
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <f:verbatim><b>Location North</b></f:verbatim>
				  </f:facet>
				  <h:inputText id="dflelerklj74" 
									size="7"
									value="#{myentry4.obsvLocationNorth}" />
				</h:column>
				<h:column>
				  <f:facet name="header">
					 <f:verbatim><b>Ref Site</b></f:verbatim>
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
			 
		</h:panelGrid>
	 <f:verbatim></fieldset></f:verbatim>		  
  </h:form>
</h:panelGroup>
</h:panelGrid>