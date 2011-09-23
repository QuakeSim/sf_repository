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
		   var commandButton=document.getElementById("UpdateSelectFaultsForm:SelectFault4projj");
		   doDisplayToggle(panelGrid1,commandButton,'block');
		}

		function toggleDisplay2(fieldset) {
		   var panelGrid=document.getElementById("UpdateSelectObservationForm:aerbasd1");
		   var commandButton=document.getElementById("UpdateSelectObservationForm:SelectObservations4proj1");
		   doDisplayToggle(panelGrid,commandButton,'none');
		}		

		function doDisplayToggle(panelGrid1,commandButton,defaultVal){
		   if(panelGrid1.style.display=='none') {
		     panelGrid1.style.display='block';
		     commandButton.style.display='block';
		   }
		   else if (panelGrid1.style.display!='none'){
			  panelGrid1.style.display='none';
			  commandButton.style.display='none';
			}
		   else {
			  panelGrid1.style.display=defaultVal;
			  commandButton.style.display=defaultVal;
		   }
		}		
		  //]]>
	 </script>
  </f:verbatim>
  <h:panelGroup id="dflelerkljk161">
	 <h:form id="UpdateSelectFaultsForm"
				rendered="#{!empty SimplexBean.myFaultEntryForProjectList}">
		<h:inputHidden id="projectOriginLat" value="#{SimplexBean.currentProjectEntry.origin_lat}"/>
		<h:inputHidden id="projectOriginLon" value="#{SimplexBean.currentProjectEntry.origin_lon}"/>
		
		<f:verbatim>
		  <fieldset style="width:960px" ondblclick="toggleDisplay1(this)">
			 <legend class="portlet-form-label">Fault Components</legend>
			 Click the checkbox if you want to allow Simplex to vary the value during
			 optimization. 
			 <u>Toggle the fault display on/off by double clicking anywhere in this fieldset.</u>
			 <p/>
		  </f:verbatim>

		  <h:panelGrid id="dflelerkljg162" 
							style="width:920px"
							columns="2" 
							border="0"  
							cellpadding="0" 
							cellspacing="0">
			 <h:dataTable id="dflelerh966"  
							  border="1" 
							  cellpadding="0" 
							  cellspacing="0"  
							  headerClass="componentstableh" 
							  columnClasses="componentstablec"
							  binding="#{SimplexBean.myFaultListingsDataTable}"
							  value="#{SimplexBean.myFaultsForProjectList}" 
							  var="myentry31">
				<h:column>
				  <f:facet name="header">
					 <f:verbatim><b>Name</b></f:verbatim>
				  </f:facet>
				  <h:panelGrid  id="spxpcp5" columns="2" cellpadding="0" cellspacing="0">
					 <h:inputText id="dflelerklh968" 
									  size="15"
									  style="text-align:center;width:35px" 
									  value="#{myentry31.faultName}" 
									  onmouseover="expandTextField(this)" 
									  onmouseout="resetTextFieldStyle(this)"
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
									  style="text-align:center;width:35px" 
									  value="#{myentry31.faultLocationX}" 
									  onchange="updateLat0Lon0Lat1Lon1(this)"
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'									  
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
									  style="text-align:center;width:35px" 
									  value="#{myentry31.faultLocationY}"
									  onchange="updateLat0Lon0Lat1Lon1(this)"
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'									  
									  required="true" />
					 <h:selectBooleanCheckbox id="faultOriginYVary2"
													  value="#{myentry31.faultOriginYVary}" />
				  </h:panelGrid>
				</h:column>

				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljj11" value="Start Lat" />
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
									  style="text-align:center;width:35px" 
									  onchange="updateXYLengthStrike(this)"
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'
									  value="#{myentry31.faultLatStarts}" />
				  </h:panelGrid>
				</h:column>
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljj8" value="Start Lon" />
				  </f:facet>
				  <h:panelGrid id="spxpcp15" columns="1" 
									cellpadding="0" cellspacing="0" styleClass="centered">
					 <h:inputText id="FaultLonStarts2" 
									  size="7"
									  style="text-align:center;width:35px" 
									  onchange="updateXYLengthStrike(this)"
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'
									  value="#{myentry31.faultLonStarts}" />
				  </h:panelGrid>
				</h:column>
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljj17" value="End Lat" />
				  </f:facet>
				  <h:panelGrid id="spxpcp18" 
									columns="1" 
									cellpadding="0" 
									cellspacing="0" 
									styleClass="centered">
					 <h:inputText id="FaultLatEnds2" 
									  size="7"
									  style="text-align:center;width:35px" 
									  onchange="updateXYLengthStrike(this)"
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'
									  value="#{myentry31.faultLatEnds}" />
				  </h:panelGrid>
				</h:column>

				
				<h:column>
				  <f:facet name="header">
					 <h:outputText  id="dflelerkljj14" value="End Lon" />
				  </f:facet>
				  <h:panelGrid id="spxpcp17" 
									columns="1" 
									cellpadding="0" 
									cellspacing="0" 
									styleClass="centered">
					 <h:inputText id="FaultLonEnds2" 
									  size="7"
									  style="text-align:center;width:35px" 
									  onchange="updateXYLengthStrike(this)"
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'
									  value="#{myentry31.faultLonEnds}" />
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
									  style="text-align:center;width:35px" 
									  value="#{myentry31.faultStrikeAngle}"
									  onchange="updateLat0Lon0Lat1Lon1(this)"
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'
									  required="false" />
					 <h:selectBooleanCheckbox id="faultStrikeAngleVary2"
													  value="#{myentry31.faultStrikeAngleVary}" />
				  </h:panelGrid>
				</h:column>

				<h:column>
				  <f:facet name="header">
					 <h:outputText id="lkdrqj11816" value="Dip Angle" />
				  </f:facet>
				  <h:panelGrid id="spxpcp11" columns="2" cellpadding="0" cellspacing="0">
					 <h:inputText id="FaultDipAngle2" 
									  size="7"
									  style="text-align:center;width:35px" 
									  value="#{myentry31.faultDipAngle}"
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'
									  required="true" />
					 <h:selectBooleanCheckbox id="faultDipAngleVary2"
													  value="#{myentry31.faultDipAngleVary}" />
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
									  style="text-align:center;width:35px" 
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'
									  value="#{myentry31.faultDepth}"
									  required="true" />
					 <h:selectBooleanCheckbox id="faultDepthVary2"
													  value="#{myentry31.faultDepthVary}" />
				  </h:panelGrid>
				</h:column>
								
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="lkdrh1189" value="Width" />
				  </f:facet>
				  <h:panelGrid  id="spxpcp9" columns="2" cellpadding="0" cellspacing="0">
					 
					 <h:inputText id="FaultWidth2" 
									  size="7"
									  style="text-align:center;width:35px" 
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'
									  value="#{myentry31.faultWidth}"
									  required="true" />
					 <h:selectBooleanCheckbox id="faultWidthVary2"
													  value="#{myentry31.faultWidthVary}" />
				  </h:panelGrid>
				</h:column>						
				
				<h:column>
				  <f:facet name="header">
					 <h:outputText id="lkdrqh1186" value="Length" />
				  </f:facet>
				  <h:panelGrid  id="spxpcp8" columns="2" cellpadding="0" cellspacing="0">
					 <h:inputText id="FaultLength2" 
									  size="7"
									  style="text-align:center;width:35px" 
									  value="#{myentry31.faultLength}"
									  onchange="updateLat0Lon0Lat1Lon1(this)"
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'
									  required="true" />
					 <h:selectBooleanCheckbox id="faultLengthVary2"
													  value="#{myentry31.faultLengthVary}" />
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
									  style="text-align:center;width:35px" 
									  value="#{myentry31.faultRakeAngle}"
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'									  
									  required="true" />
					 <h:selectBooleanCheckbox id="faultStrikeSlipVary2"
													  value="#{myentry31.faultStrikeSlipVary}" />
				  </h:panelGrid>
				</h:column>						

				<h:column>
				  <f:facet name="header">
					 <h:outputText id="dflelerkljj3" value="Dip Slip" />
				  </f:facet>
				  <h:panelGrid id="spxpcp13" columns="2" cellpadding="0" cellspacing="0">
					 <h:inputText id="FaultSlip2" 
									  size="7"
									  style="text-align:center;width:35px" 
									  value="#{myentry31.faultSlip}"
									  onmouseover="expandTextField(this)"
									  onmouseout='resetTextFieldStyle(this)'
									  required="true" />
					 <h:selectBooleanCheckbox id="faultDipSlipVary2"
													  value="#{myentry31.faultDipSlipVary}" />
				  </h:panelGrid>
				</h:column>
				
				

				<h:column>
				  <f:facet name="header">
					 <f:verbatim>Update Fault</f:verbatim>
				  </f:facet>
				  <h:commandButton id="UpdateFaultVals09e" 
										 value="Update"
										 action="#{SimplexBean.toggleUpdateFaults2}" />
				</h:column>
				<h:column>
				  <f:facet name="header">
					 <f:verbatim>Delete Fault</f:verbatim>
				  </f:facet>
				  <h:commandButton id="DeleteFaultVals09e" 
										 value="Delete"
										 action="#{SimplexBean.toggleDeleteFaults2}" />
				</h:column>

			 </h:dataTable>
		</h:panelGrid>
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
				
		<h:panelGrid id="aerbasd1" 
						 style="display:none"
						 columns="2" 
						 border="0" 
						 cellpadding="0" 
						 cellspacing="0">    

			 <h:dataTable border="1" 
							  cellpadding="0" 
							  cellspacing="0" 
							  id="dflelerkljk1277" 
							  headerClass="componentstableh2" 
							  columnClasses="componentstablec"
							  binding="#{SimplexBean.myObsvListingsDataTable}"
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
				  <h:outputText id="obsvTypeSar"
									 rendered="#{myentry4.obsvType==7}"
									 value="SAR"/>
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


				<h:column>
				  <f:facet name="header">
					 <f:verbatim>Update Observation</f:verbatim>
				  </f:facet>
				  <h:commandButton id="UpdateObsvVals09e" 
										 value="Update"
										 action="#{SimplexBean.toggleUpdateObservations2}" />
				</h:column>
				<h:column>
				  <f:facet name="header">
					 <f:verbatim>Delete Observation</f:verbatim>
				  </f:facet>
				  <h:commandButton id="DeleteObsvVals09e" 
										 value="Delete"
										 action="#{SimplexBean.toggleDeleteObservations2}" />
				</h:column>

			 </h:dataTable>
			 
		</h:panelGrid>
	 <f:verbatim></fieldset></f:verbatim>		  
  </h:form>
</h:panelGroup>
</h:panelGrid>
<f:verbatim>
  <script src="script/calculators.js"></script>
  <script>
	 //TODO: The functions in this script are used by Disloc3, Simplex3 and possibly other components.
	 //If updated here, they must be updated in other locations.  It would be better to put these
	 //in a global library location.

	 var d2r = Math.acos(-1.0) / 180.0;
	 var flatten=1.0/298.247;

	 var xstart;
	 var ystart;
	 var latStart;
	 var lonStart;
	 var latEnd;
	 var lonEnd;
	 var strike;
	 var dipAngle;
	 var length;

	 //This is the lat+lon of the project origin.
	 var origLat=document.getElementById("UpdateSelectFaultsForm:projectOriginLat");
	 var origLon=document.getElementById("UpdateSelectFaultsForm:projectOriginLon");
	 
	 function updateXYLengthStrike(source) {
	 setUpStuff(source);
	 calculators.updateXYLengthStrike(origLat,origLon,xstart, ystart,latStart, lonStart, strike, dipAngle, length);
	 }
	 
	 function updateLat0Lon0Lat1Lon1(source) {
	 setUpStuff(source);
	 calculators.updateLat0Lon0Lat1Lon1(origLat, origLon, xstart, ystart,latStart, lonStart, strike, dipAngle, length);
	 }

	 //--------------------------------------------------
	 //
	 //--------------------------------------------------
	 function setUpStuff(source){
		  var parts=source.getAttribute("id").split(":");
		  var rowName=parts[0]+":"+parts[1];
		  console.log("Origin Coordinates:"+origLat.value+" "+origLon.value);
		  
		  //Set up all the values from forms
		  setFormValues(rowName);
		  console.log(xstart.value+" "+ystart.value+" "+latStart.value+" "+lonStart.value+" "+latEnd.value+" "+lonEnd.value+" "+strike.value+" "+dipAngle.value+" "+length.value);

		  }

	 //--------------------------------------------------
	 // Sets a bunch of values for the given row name
	 // These have to correspond to the HTML ID attributes, so this may not
	 // be portable. 
	 //--------------------------------------------------
	 function setFormValues(rowName) {
	 //Here we get the values that we need from the form fields
	 xstart=document.getElementById(rowName+":"+"FaultLocationX2");
	 ystart=document.getElementById(rowName+":"+"FaultLocationY2");
	 latStart=document.getElementById(rowName+":"+"FaultLatStarts2");
	 lonStart=document.getElementById(rowName+":"+"FaultLonStarts2");
	 latEnd=document.getElementById(rowName+":"+"FaultLatEnds2");
	 lonEnd=document.getElementById(rowName+":"+"FaultLonEnds2");
	 strike=document.getElementById(rowName+":"+"FaultStrikeAngle2");
	 dipAngle=document.getElementById(rowName+":"+"FaultDipAngle2");
	 length=document.getElementById(rowName+":"+"FaultLength2");	 
	 
	 }


	 function expandTextField(inputField) {
	 inputField.style.width="70px";
	 }
	 
	 function resetTextFieldStyle(inputField){
	 inputField.style.width="35px";
	 }
  </script>
</f:verbatim>