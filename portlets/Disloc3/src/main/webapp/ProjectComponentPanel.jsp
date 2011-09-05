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

	 <h:inputHidden id="projectOriginLat" value="#{DislocBean2.currentParams.originLat}"/>
	 <h:inputHidden id="projectOriginLon" value="#{DislocBean2.currentParams.originLon}"/>
	 <h:panelGrid id="dislocdirtyworkaroundgroup"
					  columns="2">
		<f:verbatim>
		  Below are the faults you have added to your project.  You can update the
		  fault model parameters by 1) editing the value in the box and 2) clicking the "Update" button.
		  Changes in fields are linked as follows:
		  <ul>
			 <li>Changes to a fault's (x,y) are translations, and the fault's starting and ending
			 latitude and longitude are updated.</li>
			 <li>Changes to a fault's starting (lat,lon) will also update the (x,y), length, and strike.</li>
			 <li>Changes to a fault's ending (lat,lon) will also update the length and strike </li>
			 <li>Changes to a fault's length or strike will also update the ending latitude and longitude.</li>
		  </ul>
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
						  binding="#{DislocBean2.myFaultListingsDataTable}"
						  value="#{DislocBean2.myFaultsForProjectList}" 
						  var="myentry31">
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Name</f:verbatim>
			  </f:facet>
			  <h:panelGrid columns="2" cellpadding="0" cellspacing="0" id="kljerje22">
				 <h:inputText id="dflelerklh968" 
								  style="text-align:right;width:35px"
								  onmouseover="expandTextField(this)" 
								  onmouseout="resetTextFieldStyle(this)"
								  value="#{myentry31.faultName}" 
								  required="true" />
			  </h:panelGrid>
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>X</f:verbatim>
			  </f:facet>
			  <h:panelGrid columns="1" cellpadding="0" cellspacing="0" id="ljerlke12j">
				 <h:inputText id="FaultLocationX2" 
								  style="text-align:right;width:30px"
								  value="#{myentry31.faultLocationX}" 
								  onchange="updateLat0Lon0Lat1Lon1(this)"
								  onmouseover="expandTextField(this)"
								  onmouseout='resetTextFieldStyle(this)'
								  required="true" />
			  </h:panelGrid>
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Y</f:verbatim>
			  </f:facet>
			  <h:panelGrid columns="1" cellpadding="0" cellspacing="0" id="ljlejre122">
				 <h:inputText id="FaultLocationY2" style="text-align:right;width:30px" 
								  value="#{myentry31.faultLocationY}"
								  onchange="updateLat0Lon0Lat1Lon1(this)"
								  onmouseover="expandTextField(this)"
								  onmouseout='resetTextFieldStyle(this)'

								  required="true" />
			  </h:panelGrid>
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Start Lat</f:verbatim>
			  </f:facet>
			  <h:inputText id="FaultLatStart" style="text-align:right;width:30px" 
								value="#{myentry31.faultLatStart}"
								onchange="updateXYLengthStrike(this)"
								  onmouseover="expandTextField(this)"
								  onmouseout='resetTextFieldStyle(this)'
								  required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Start Lon</f:verbatim>
			  </f:facet>
				 <h:inputText id="FaultLonStart" style="text-align:right;width:30px" 
								  value="#{myentry31.faultLonStart}"
								  onchange="updateXYLengthStrike(this)"
								  onmouseover="expandTextField(this)"
								  onmouseout='resetTextFieldStyle(this)'
								  required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>End Lat</f:verbatim>
			  </f:facet>
			  <h:inputText id="FaultLatEnd" style="text-align:right;width:30px" 
								value="#{myentry31.faultLatEnd}"
								onchange="updateXYLengthStrike(this)"
								onmouseover="expandTextField(this)"
								onmouseout='resetTextFieldStyle(this)'
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>End Lon</f:verbatim>
			  </f:facet>
				 <h:inputText id="FaultLonEnd" style="text-align:right;width:30px" 
								  value="#{myentry31.faultLonEnd}"
								  onchange="updateXYLengthStrike(this)"
								  onmouseover="expandTextField(this)"
								  onmouseout='resetTextFieldStyle(this)'
								  required="true" />
			</h:column>						

			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Strike Angle</f:verbatim>
			  </f:facet>
			  <h:inputText id="FaultStrikeAngle" style="text-align:right;width:30px" 
								value="#{myentry31.faultStrikeAngle}"
								onchange="updateLat0Lon0Lat1Lon1(this)"
								onmouseover="expandTextField(this)"
								onmouseout='resetTextFieldStyle(this)'
								required="false" />
			</h:column>

			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Dip Angle</f:verbatim>
			  </f:facet>
			  <h:inputText id="FaultDipAngle" style="text-align:right;width:30px" 
								value="#{myentry31.faultDipAngle}"
								onmouseover="expandTextField(this)"
								onmouseout='resetTextFieldStyle(this)'
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Depth</f:verbatim>
			  </f:facet>
			  <h:inputText id="FaultDepth" style="text-align:right;width:30px" 
								value="#{myentry31.faultDepth}"
								onmouseover="expandTextField(this)"
								onmouseout='resetTextFieldStyle(this)'
								required="true" />
			</h:column>

			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Width</f:verbatim>
			  </f:facet>
			  <h:inputText id="FaultWidth" style="text-align:right;width:30px" 
								value="#{myentry31.faultWidth}"
								  onmouseover="expandTextField(this)"
								  onmouseout='resetTextFieldStyle(this)'
								required="true" />
			</h:column>
						
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Length</f:verbatim>
			  </f:facet>
			  <h:inputText id="FaultLength" style="text-align:right;width:30px" 
								  value="#{myentry31.faultLength}"
								  onchange="updateLat0Lon0Lat1Lon1(this)"
								  onmouseover="expandTextField(this)"
								  onmouseout='resetTextFieldStyle(this)'
								  required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Strike Slip</f:verbatim>
			  </f:facet>
			  <h:inputText id="FaultStrikeSlip" style="text-align:right;width:30px" 
								value="#{myentry31.faultStrikeSlip}"
								onchange="updateLat0Lon0Lat1Lon1(this)"
								onmouseover="expandTextField(this)"
								onmouseout='resetTextFieldStyle(this)'
								required="true" />
			</h:column>						

			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Dip Slip</f:verbatim>
			  </f:facet>
			  <h:inputText id="FaultSlip" style="text-align:right;width:30px" 
								value="#{myentry31.faultDipSlip}"
								onmouseover="expandTextField(this)"
								onmouseout='resetTextFieldStyle(this)'
								required="true" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Lame Lambda</f:verbatim>
			  </f:facet>
			  <h:inputText id="LameLambda" style="text-align:right;width:30px" 
								  onmouseover="expandTextField(this)"
								  onmouseout='resetTextFieldStyle(this)'
								value="#{myentry31.faultLameLambda}" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Lame Mu</f:verbatim>
			  </f:facet>
				 <h:inputText id="LameMu" style="text-align:right;width:30px" 
								  onmouseover="expandTextField(this)"
								  onmouseout='resetTextFieldStyle(this)'
								  value="#{myentry31.faultLameMu}" />
			</h:column>
			
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Update</f:verbatim>
			  </f:facet>
			  <h:commandButton id="UpdateFaultValues309u" value="Update"
									 action="#{DislocBean2.toggleUpdateFaults2}"/>
			  
			</h:column>
			<h:column>
			  <f:facet name="header">
				 <f:verbatim>Delete</f:verbatim>
			  </f:facet>
			  <h:commandButton id="DeleteFaultValues309" value="Delete"
									 action="#{DislocBean2.toggleDeleteFaults2}"/>
			</h:column>

		 </h:dataTable>

	</h:panelGrid>		
 </h:panelGrid>				
 
</h:form>
<f:verbatim></fieldset></f:verbatim>
</h:panelGroup>
</h:panelGrid>
<f:verbatim>
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

	 //--------------------------------------------------
	 //This function handles a fault's translation relative to the origin. 
	 //That is, it handles changes to the fault's (x,y)
	 //--------------------------------------------------
	 function updateLat0Lon0Lat1Lon1(source){
	 setUpStuff(source);
	 
	 //Find the new lat/lon value of the modified starting point from (x,y).
	 var theFactor=d2r* Math.cos(d2r * origLat.value) * 6378.139 * (1.0 - Math.sin(d2r * origLat.value) * Math.sin(d2r * origLat.value) * flatten);
	 console.log(theFactor);
	 lonStart.value = (xstart.value*1.0)/theFactor + (origLon.value*1.0);
	 latStart.value = ystart.value/111.32 + (origLat.value*1.0);
	 console.log(lonStart.value+" "+latStart.value);
	 	 
	 //Now find the lat/lon values of the translated endpoint.
	 //First, find the Cartesian coordinates of the endpoint.  

	 if (strike.value == 0) {
	 xend = 0; 
	 yend = length.value;
	 }
	 else if (strike.value == 90) { xend = length.value; yend = 0;}
	 else if (strike.value == 180) { xend = 0; yend = (-1.0) * length.value;}
	 else if (strike.value == 270) { xend = (-1.0) * length.value; yend = 0;}
	 else {
	 var sval = 90 - strike.value;
	 var thetan = Math.tan(sval*d2r);
	 var xend = length.value/Math.sqrt(1 + thetan*thetan);
	 var yend = Math.sqrt(length.value*length.value - xend*xend);
	 

	 if (strike.value > 0 && strike.value < 90) { xend = xend*1.0; yend = yend*1.0;}
	 else if (strike.value > 90 && strike.value < 180) { xend = xend*1.0; yend = yend* (-1.0);}
	 else if (strike.value > 180 && strike.value < 270) { xend = xend*(-1.0); yend = yend*(-1.0);}
	 else if (strike.value > 270 && strike.value < 360) { xend = xend*(-1.0); yend = yend*1.0;}
	 }
	 
	 //Note we use the lat, lon of the fault's starting point here, not the origin's lat, lon, because
	 //we are using the fault length (not the distance to the origin from the end point).
	 var theFactor=d2r* Math.cos(d2r * latStart.value) * 6378.139 * (1.0 - Math.sin(d2r * latStart.value) * Math.sin(d2r * latStart.value) * flatten);
	 console.log(theFactor);
	 lonEnd.value = (xend*1.0)/theFactor + (lonStart.value*1.0);
	 latEnd.value = yend/111.32 + (latStart.value*1.0);
	 
	 lonEnd.value = Math.round(lonEnd.value*100)/100.0;
	 latEnd.value = Math.round(latEnd.value*100)/100.0;
	 console.log(xstart.value+" "+ystart.value+" "+latStart.value+" "+lonStart.value+" "+latEnd.value+" "+lonEnd.value+" "+strike.value+" "+dipAngle.value+" "+length.value);
	 }

	 //--------------------------------------------------
	 //This function handles changes in a fault's starting lat and lon.
	 //This causes a rotation, expansion/contraction, and change in the
	 //fault's Cartesian coordinates.
	 //--------------------------------------------------
	 function updateXYLengthStrike(source) {
	 setUpStuff(source);

	 //First, calculate the new (x,y) of the fault
	 var theFactor=d2r* Math.cos(d2r * origLat.value) * 6378.139 * (1.0 - Math.sin(d2r * origLat.value) * Math.sin(d2r * origLat.value) * flatten);
	 console.log("The factor:"+theFactor);
	 xstart.value=(lonStart.value-origLon.value)*theFactor;
	 ystart.value=(latStart.value-origLat.value)*111.32;
	 console.log("X and Y:"+xstart.value+" "+ystart.value);
	 
	 //Next, calculate the length
	 var theFactor=d2r* Math.cos(d2r * latStart.value) * 6378.139 * (1.0 - Math.sin(d2r * latStart.value) * Math.sin(d2r * latStart.value) * flatten);
	 console.log("The factor:"+theFactor);	
	 console.log(lonEnd.value+" "+lonStart.value+" "+latEnd.value+" "+latStart.value);				
	 xdiff=(lonEnd.value-lonStart.value)*theFactor;
	 ydiff=(latEnd.value-latStart.value)*111.32;
	 console.log("Sqrt:"+Math.sqrt(xdiff*xdiff-ydiff*ydiff));
	 lengthValue=Math.sqrt(xdiff*xdiff+ydiff*ydiff);
	 length.value=Math.round(lengthValue*1000/1000);
	 console.log("xdiff, ydiff, length:"+xdiff+" "+ydiff+" "+length.value);

	 //And the strike
	 strikeValue=Math.atan2(xdiff,ydiff)/d2r;
	 if (strikeValue < 0) { strikeVaule = strikeValue + 360; }
	 strike.value=Math.round(strikeValue*1000)/1000;
	 console.log("Strike:"+strike.value);
	 console.log(xstart.value+" "+ystart.value+" "+latStart.value+" "+lonStart.value+" "+latEnd.value+" "+lonEnd.value+" "+strike.value+" "+dipAngle.value+" "+length.value);
	 }
	 
	 //--------------------------------------------------
	 //This function handles changes in the fault's ending lat and lon.  This 
	 //causes a rotation and expansion/contraction
	 //--------------------------------------------------
	 function updateLengthStrike(source) {
	 setUpStuff(source);

	 }
	 
	 //--------------------------------------------------
	 //This function handles changes in a fault's length and changes 
	 //in the strike angle. The fault's starting (x,y) and (lat,lon) are constant.
	 //--------------------------------------------------
	 function updateLat1Lon1(source) {
	 setUpStuff(source);
	 }


	 //--------------------------------------------------
	 //
	 //--------------------------------------------------
	 function setUpStuff(source){
	 var parts=source.getAttribute("name").split(":");
	 var rowName=parts[0]+":"+parts[1];
	 console.log("Origin Coordinates:"+origLat.value+" "+origLon.value);

	 //Set up all the values from forms
	 setFormValues(rowName);
	 console.log(xstart.value+" "+ystart.value+" "+latStart.value+" "+lonStart.value+" "+latEnd.value+" "+lonEnd.value+" "+strike.value+" "+dipAngle.value+" "+length.value);

	 }

	 //--------------------------------------------------
	 // Sets a bunch of values for the given row name
	 //--------------------------------------------------
	 function setFormValues(rowName) {
	 //Here we get the values that we need from the form fields
	 xstart=document.getElementById(rowName+":"+"FaultLocationX2");
	 ystart=document.getElementById(rowName+":"+"FaultLocationY2");
	 latStart=document.getElementById(rowName+":"+"FaultLatStart");
	 lonStart=document.getElementById(rowName+":"+"FaultLonStart");
	 latEnd=document.getElementById(rowName+":"+"FaultLatEnd");
	 lonEnd=document.getElementById(rowName+":"+"FaultLonEnd");
	 strike=document.getElementById(rowName+":"+"FaultStrikeAngle");
	 dipAngle=document.getElementById(rowName+":"+"FaultDipAngle");
	 length=document.getElementById(rowName+":"+"FaultLength");	 
	 
	 }


	 function expandTextField(inputField) {
	 inputField.style.width="70px";
	 }
	 
	 function resetTextFieldStyle(inputField){
	 inputField.style.width="35px";
	 }
  </script>
</f:verbatim>