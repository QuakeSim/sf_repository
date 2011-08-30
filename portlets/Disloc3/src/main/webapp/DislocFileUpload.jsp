<h:form id="dislocInputForm" rendered="#{DislocBean2.renderDislocInputUploadForm}"> 
  <f:verbatim>
	 <fieldset><legend class="portlet-form-label">Upload Fault File</legend>
	 Use this form to upload one or more faults that are already in Disloc input file format.  The following example shows formatting:
    Line 1: 32.904255 -115.526449 1   (this is the lat, lon of the origin; and "1" signifies use of a grid).<br/>
    Line 2: -75 1 151 -40 1 41 (the grid: x0, x_delta, x_number, y0, y_delta, y_number) <br/>
    Line 3: 20.489759271 -80.624111128 355.0 (first fault patch: x, y (km) from origin and strike (degrees)<br/>
    Line 4: 0 1.21 45.0 1.0 1.0 -0.0 -0.0 0.0 3.0 3.0 (fault_type —0 for point dislocation — depth, dip (degrees), lambda, mu,u1,u2,u3, length, width) <br/>
	 Repeat the formats for Lines 3 and 4 for each additional fault.
	 <p/>
  </f:verbatim>
  <h:panelGroup id="df09r3u8188">
	 <h:inputTextarea id="dislocFaultTextArea"
							rows="20" cols="100"
							value="#{DislocBean2.dislocFaultTextArea}"/>
  </h:panelGroup>
  <f:verbatim><br/></f:verbatim>
  <h:panelGroup id="oi43o7c8q">
	 <h:commandButton id="importDislocFaultTextArea" value="Import Faults"
							actionListener="#{DislocBean2.toggleImportFaultsForProject}" />
  </h:panelGroup>
  
  <f:verbatim></fieldset></f:verbatim>			 
</h:form>