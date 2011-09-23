<h:panelGroup id="faultsParamPanelSimplex3"
				  rendered="#{SimplexBean.currentEditProjectForm.renderCreateNewFaultForm}">	 
  
<h:form id="Simplex3Faultform">
  <h:inputHidden id="projectOriginLatFPP" value="#{SimplexBean.currentProjectEntry.origin_lat}"/>
  <h:inputHidden id="projectOriginLonFPP" value="#{SimplexBean.currentProjectEntry.origin_lon}"/>
 
<f:verbatim>
  <fieldset style="width:100%">
	 <legend class="portlet-form-label">Input Fault Geometry </legend>
  </f:verbatim>
  
  <h:panelGrid id="FaultTable" columns="3" footerClass="subtitle"
					  headerClass="subtitlebig" styleClass="medium"
					  columnClasses="subtitle,medium">
		
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
							onchange="updateLat0Lon0Lat1Lon1FPP(this)"
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
							onchange="updateLat0Lon0Lat1Lon1FPP(this)"
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
							onchange="updateLat0Lon0Lat1Lon1FPP(this)"
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
							onchange="updateLat0Lon0Lat1Lon1FPP(this)"
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
							onchange="updateXYLengthStrikeFPP(this)"
							required="true"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLonStarts}" />
		</h:panelGroup>
		<h:outputText  id="dnnasd10" value="" />
		
		<h:outputText id="dfleanad11" value="Fault Lat Starts:" />
		<h:panelGroup id="dflelerkljk12">
		  <h:inputText id="FaultLatStarts"
							onchange="updateXYLengthStrikeFPP(this)"
							required="true"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLatStarts}" />
		</h:panelGroup>
		<h:outputText  id="dfadfb43k13" value="" />
		
		<h:outputText  id="dflelerkljk14" value="Fault Lon Ends:" />
		<h:panelGroup id="dflelerkljk15">
		  <h:inputText id="FaultLonEnds"
							onchange="updateXYLengthStrikeFPP(this)"
							required="true"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLonEnds}" />
		</h:panelGroup>
		<h:outputText  id="dflelerkljk16" value="" />
		
		<h:outputText id="dflelerkljk17" value="Fault Lat Ends:" />
		<h:panelGroup id="dflelerkljk18">
		  <h:inputText id="FaultLatEnds"
							onchange="updateXYLengthStrikeFPP(this)"
							required="true"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLatEnds}" />
		</h:panelGroup>
		<h:outputText  id="dflelerkljk19" value="" />
		
		<h:commandButton id="addfault" value="Set Values"
							  actionListener="#{SimplexBean.toggleAddFaultForProject}" />
		<f:facet name="footer">
		  <f:verbatim>
			 Click 'Set Values' when you are ready to add the fault to your project.  All values are required.<br/>
		  </f:verbatim>
		</f:facet>
	 </h:panelGrid>
	 <f:verbatim></fieldset></f:verbatim>
  </h:form>
<f:verbatim>
  <script src="script/calculators.js"></script>
  <script>
	 //TODO: The functions in this script are used by Disloc3, Simplex3 and possibly other components.
	 //If updated here, they must be updated in other locations.  It would be better to put these
	 //in a global library location.

	 var d2r = Math.acos(-1.0) / 180.0;
	 var flatten=1.0/298.247;

	 var xstart_fpp;
	 var ystart_fpp;
	 var latStart_fpp;
	 var lonStart_fpp;
	 var latEnd_fpp;
	 var lonEnd_fpp;
	 var strike_fpp;
	 var dipAngle_fpp;
	 var length_fpp;

	 //This is the lat+lon of the project origin.
	 var origLat_fpp=document.getElementById("Simplex3Faultform:projectOriginLatFPP");
	 console.log("Project origin lat:"+origLat_fpp.value);
	 var origLon_fpp=document.getElementById("Simplex3Faultform:projectOriginLonFPP");
	 console.log("Project origin lon:"+origLon_fpp.value);
	 
	 function updateXYLengthStrikeFPP(source) {
	 setUpStuffFPP(source);
	 calculators.updateXYLengthStrike(origLat_fpp,origLon_fpp,xstart_fpp, ystart_fpp,latStart_fpp, lonStart_fpp, latEnd_fpp, lonEnd_fpp, strike_fpp, dipAngle_fpp, length_fpp);
	 }
	 
	 function updateLat0Lon0Lat1Lon1FPP(source) {
	 setUpStuffFPP(source);
	 calculators.updateLat0Lon0Lat1Lon1(origLat_fpp,origLon_fpp,xstart_fpp, ystart_fpp,latStart_fpp, lonStart_fpp, latEnd_fpp, lonEnd_fpp, strike_fpp, dipAngle_fpp, length_fpp);
	 }

	 //--------------------------------------------------
	 //
	 //--------------------------------------------------
	 function setUpStuffFPP(source){
		  var parts=source.getAttribute("id").split(":");
		  //var rowName=parts[0]+":"+parts[1];
		  var rowName=parts[0];
		  //Set up all the values from forms
		  setFormValuesFPP(rowName);
		  console.log(xstart_fpp.value+" "+ystart_fpp.value+" "+latStart_fpp.value+" "+lonStart_fpp.value+" "+latEnd_fpp.value+" "+lonEnd_fpp.value+" "+strike_fpp.value+" "+dipAngle_fpp.value+" "+length_fpp.value);

		  }

	 //--------------------------------------------------
	 // Sets a bunch of values for the given row name
	 // These have to correspond to the HTML ID attributes, so this may not
	 // be portable. 
	 //--------------------------------------------------
	 function setFormValuesFPP(rowName) {
	 console.log(rowName);
	 //Here we get the values that we need from the form fields
	 xstart_fpp=document.getElementById(rowName+":"+"FaultLocationX");
	 console.log(xstart_fpp.value);
	 ystart_fpp=document.getElementById(rowName+":"+"FaultLocationY");
	 console.log(ystart_fpp.value);
	 latStart_fpp=document.getElementById(rowName+":"+"FaultLatStarts");
	 console.log(latStart_fpp.value);
	 lonStart_fpp=document.getElementById(rowName+":"+"FaultLonStarts");
	 console.log(lonStart_fpp.value);
	 latEnd_fpp=document.getElementById(rowName+":"+"FaultLatEnds");
	 lonEnd_fpp=document.getElementById(rowName+":"+"FaultLonEnds");
	 strike_fpp=document.getElementById(rowName+":"+"FaultStrikeAngle");
	 dipAngle_fpp=document.getElementById(rowName+":"+"FaultDipAngle");
	 length_fpp=document.getElementById(rowName+":"+"FaultLength");	 
	 
	 }

  </script>
</f:verbatim>
</h:panelGroup>