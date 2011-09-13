<h:panelGroup id="lkdrq7FaultsQuickAdd" 
				  rendered="#{SimplexBean.currentEditProjectForm.renderFaultsQuickAdd}">
  <f:verbatim>
	 <fieldset style="width:100%">
	 <legend class="portlet-form-label">Project Importer </legend>
  </f:verbatim>
  <h:form id="faultQuickAdd">
	 <f:verbatim>
		Use this form if you already have your fault parameters. <br/>
		The first line must be the project's latitude and longitude in decimal degrees. <br/>
		The second and subsequent lines are faults (one per line) in the following required format:<br/>
		<i>Location-X Location-Y Strike-Angle Dip-Angle Depth Width Length Strike-Slip Dip-Slip </i><br/>
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
