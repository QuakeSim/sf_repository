<h:form id="dislocInputForm" rendered="#{DislocBean2.renderDislocInputUploadForm}"> 
  <f:verbatim>
	 <fieldset><legend class="portlet-form-label">Upload Fault File</legend>
	 Use this form to upload one or more faults that are already in Disloc input file format.<p/>
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