<h:panelGroup id="lkdrq7FaultsQuickAdd" 
				  rendered="#{SimplexBean.currentEditProjectForm.renderFaultsQuickAdd}">
  <f:verbatim>
	 <fieldset style="width:100%">
	 <legend class="portlet-form-label">Project Importer </legend>
  </f:verbatim>
  <h:form id="faultQuickAdd">
	 <f:verbatim>
		Project import form: use this if you are importing a command-line Simplex problem.<br/>
		The first line must be the project's latitude and longitude. <br/>
		The second and subsequent lines are faults (one per line) in the following required format:<br/>
		X Y Strike Dip Depth Width Length StrikeSlip <br/>
	 </f:verbatim>
	 
	 <h:panelGrid id="QuickFaultPG" columns="1">
		<h:inputTextarea id="QuickFaultTextArea"
							  rows="10" 
							  cols="50"
							  value="#{SimplexBean.currentEditProjectForm.faultQuickAddTextArea}"/>
		<h:commandButton id="addFaultQuick" value="select"
							  actionListener="#{SimplexBean.toggleAddQuickSimplexFaults}" />
	 </h:panelGrid>
  </h:form>
  <f:verbatim></fieldset></f:verbatim>
</h:panelGroup>
