<h:form id="Disloc3Faultform" rendered="#{DislocBean2.renderCreateNewFaultForm}"> 
  <h:inputHidden id="projectOriginLatFPP" value="#{DislocBean2.currentParams.originLat}"/>
  <h:inputHidden id="projectOriginLonFPP" value="#{DislocBean2.currentParams.originLon}"/>

	 <f:verbatim>
		<fieldset><legend class="portlet-form-label">Fault Geometry Parameters</legend>
	 </f:verbatim>	 
  
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
						 onchange="fppCalc.updateLat0Lon0Lat1Lon1(this)"
						 value="#{DislocBean2.currentFault.faultLocationX}" required="true" /> 
	 </h:panelGroup> 
	 <h:outputText  id="lkdjre3234" value="N/A" /> 

	 <h:outputText  id="stuff19" value="Location Y:" /> 
	 <h:panelGroup  id="stuff20"> 
		<h:inputText id="FaultLocationY" 
						 onchange="fppCalc.updateLat0Lon0Lat1Lon1(this)"
						 value="#{DislocBean2.currentFault.faultLocationY}" required="true" /> 
	 </h:panelGroup> 
	 <h:outputText  id="lkdjre3122" value="N/A" /> 
	 
	 <h:outputText id="stufflat32" value="Fault Origin Latitude:" />
	 <h:panelGroup id="stufflat32we">
		<h:inputText id="FaultLatStartDisloc3" value="#{DislocBean2.currentFault.faultLatStart}"							
						 onchange="fppCalc.updateXYLengthStrike(this)"
						 required="true" />
	 </h:panelGroup>
	 <h:outputText  id="lkdj21re32" value="N/A" /> 
	 
	 <h:outputText id="stufflon32" value="Fault Origin Longitude" />
	 <h:panelGroup id="stufflat323432">
		<h:inputText id="FaultLonStartDisloc3" 
						 value="#{DislocBean2.currentFault.faultLonStart}"
						 onchange="fppCalc.updateXYLengthStrike(this)"
						 required="true" />
	 </h:panelGroup>
	 <h:outputText  id="lkdj121re3dlkj2" value="N/A" /> 
	 
	 <h:outputText id="stufflonend32" value="Fault End Latitude:" />
	 <h:panelGroup id="stufflat3q1432">
		<h:inputText id="FaultLatEndDisloc3" 
						 onchange="fppCalc.updateXYLengthStrike(this)"
						 value="#{DislocBean2.currentFault.faultLatEnd}"
						 required="true" />
	 </h:panelGroup>
	 <h:outputText  id="lkdj23oi232" value="N/A" /> 
	 
	 <h:outputText id="stufflonend308" value="Fault End Longitude" />
	 <h:panelGroup id="stufflatewe3w32">
		<h:inputText id="FaultLonEndDisloc3" 
						 onchange="fppCalc.updateXYLengthStrike(this)"
						 value="#{DislocBean2.currentFault.faultLonEnd}"
						 required="true" />
	 </h:panelGroup>
	 <h:outputText  id="lkdjr1212e32" value="N/A" /> 
	 
	 <h:outputText  id="stuff21" value="Length:" /> 
	 <h:panelGroup  id="stuff22"> 
		<h:inputText id="FaultLength" 
						 value="#{DislocBean2.currentFault.faultLength}" 
						 onchange="fppCalc.updateLat0Lon0Lat1Lon1(this)"
						 required="true" /> 
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
	 <h:outputText  id="lk67v342djre32" value="mm"/> 
	 
	 <h:outputText  id="stuff31" value="Strike Angle:" /> 
	 <h:panelGroup  id="stuff32"> 
		<h:inputText id="FaultStrikeAngle" 
						 value="#{DislocBean2.currentFault.faultStrikeAngle}" 
						 onchange="fppCalc.updateLat0Lon0Lat1Lon1(this)"
						 required="true" /> 
	 </h:panelGroup> 
	 <h:outputText  id="r54r4djre32" value="degrees"/> 
	 
	 <h:outputText  id="stuff33" value="Strike Slip:" /> 
	 <h:panelGroup  id="stuff35"> 
		<h:inputText id="FaultStrikeSlip" 
						 value="#{DislocBean2.currentFault.faultStrikeSlip}" required="true" /> 
	 </h:panelGroup> 
	 <h:outputText  id="er9e2djre32" value="mm"/> 
	 
	 <h:outputText  id="stuff34" value="Tensile Slip:" /> 
	 <h:panelGroup  id="stuff36"> 
		<h:inputText id="FaultTensileSlip" 
						 value="#{DislocBean2.currentFault.faultTensileSlip}" required="true" /> 
	 </h:panelGroup> 
	 <h:outputText  id="ee3e33c3" value="mm"/> 
	 
	 
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
	 
  </h:panelGrid>		 
	 <h:commandButton id="addfault" value="Set Values" 
							actionListener="#{DislocBean2.toggleAddFaultForProject}" /> 
	 <f:verbatim></fieldset></f:verbatim>
	 
	 <f:verbatim>
		<script src="/Disloc3/script/calculators.js"></script>		
		<script>
		  //TODO: The functions in this script are used by Disloc3, Simplex3 and possibly other components.
		  //If updated here, they must be updated in other locations.  It would be better to put these
		  //in a global library location.
		  
		  var fppCalc=fppCalc || (function() {
		  
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
		  var origLat_fpp=document.getElementById("Disloc3Faultform:projectOriginLatFPP");
		  console.log("Project origin lat:"+origLat_fpp.value);
		  var origLon_fpp=document.getElementById("Disloc3Faultform:projectOriginLonFPP");
		  console.log("Project origin lon:"+origLon_fpp.value);
		  
		  function updateXYLengthStrike(source) {
		  setUpStuff(source);
		  calculators.updateXYLengthStrike(origLat_fpp,origLon_fpp,xstart_fpp, ystart_fpp,latStart_fpp, lonStart_fpp, latEnd_fpp, lonEnd_fpp, strike_fpp, dipAngle_fpp, length_fpp);
		  }
		  
		  function updateLat0Lon0Lat1Lon1(source) {
		  setUpStuff(source);
		  calculators.updateLat0Lon0Lat1Lon1(origLat_fpp,origLon_fpp,xstart_fpp, ystart_fpp,latStart_fpp, lonStart_fpp, latEnd_fpp, lonEnd_fpp, strike_fpp, dipAngle_fpp, length_fpp);
		  }
		  
		  //--------------------------------------------------
		  //
		  //--------------------------------------------------
		  function setUpStuff(source){
		  var parts=source.getAttribute("id").split(":");
		  //var rowName=parts[0]+":"+parts[1];
		  var rowName=parts[0];
		  //Set up all the values from forms
		  setFormValues(rowName);
		  console.log(xstart_fpp.value+" "+ystart_fpp.value+" "+latStart_fpp.value+" "+lonStart_fpp.value+" "+latEnd_fpp.value+" "+lonEnd_fpp.value+" "+strike_fpp.value+" "+dipAngle_fpp.value+" "+length_fpp.value);
		  
		  }

		  //--------------------------------------------------
		  // Sets a bunch of values for the given row name
		  // These have to correspond to the HTML ID attributes, so this may not
		  // be portable. 
		  //--------------------------------------------------
		  function setFormValues(rowName) {
		  console.log(rowName);
		  //Here we get the values that we need from the form fields
		  xstart_fpp=document.getElementById(rowName+":"+"FaultLocationX");
		  console.log(xstart_fpp.value);
		  ystart_fpp=document.getElementById(rowName+":"+"FaultLocationY");
		  console.log(ystart_fpp.value);
		  latStart_fpp=document.getElementById(rowName+":"+"FaultLatStartDisloc3");
		  console.log(latStart_fpp.value);
		  lonStart_fpp=document.getElementById(rowName+":"+"FaultLonStartDisloc3");
		  console.log(lonStart_fpp.value);
		  latEnd_fpp=document.getElementById(rowName+":"+"FaultLatEndDisloc3");
		  lonEnd_fpp=document.getElementById(rowName+":"+"FaultLonEndDisloc3");
		  strike_fpp=document.getElementById(rowName+":"+"FaultStrikeAngle");
		  dipAngle_fpp=document.getElementById(rowName+":"+"FaultDipAngle");
		  length_fpp=document.getElementById(rowName+":"+"FaultLength");	 
		  
		  }
		  
		  return {
		  updateLat0Lon0Lat1Lon1:updateLat0Lon0Lat1Lon1,
		  updateXYLengthStrike:updateXYLengthStrike	   
		  }
		  })();
  </script>

	 </f:verbatim>
</h:form> 
