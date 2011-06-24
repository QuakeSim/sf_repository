<h:panelGroup id="lkdrq7SAR" 
				  rendered="#{SimplexBean.currentEditProjectForm.renderSARObsvCutPaste}">
  <h:form id="obsvSARCutPaste">
	 <f:verbatim>
		<b>Import SAR2Simplex-Generated Observation Points:</b>Enter one observation point per line in 
		following format:<br/>
		#Comments begin with a # on each line.<br/>
		#Columns are Observation Type, x(km), y(km), obs(cm*), unc(cm*),elev(deg),azimuth(deg)
		#Example values below<br/>
		7 -39.460094 -27.613229 -1.557240 1.000000 27.621950 -5.376566
	 </f:verbatim>
	 
	 <h:panelGrid id="ObsvSARTextArea" columns="1">
		<h:inputTextarea id="obsvSARTextArea"
							  rows="20" cols="100"
							  value="#{SimplexBean.currentEditProjectForm.obsvSARTextArea}"/>
		<h:commandButton id="addSARObsvTextArea" value="select"
							  actionListener="#{SimplexBean.toggleAddSARObsvForProject}" />
	 </h:panelGrid>
  </h:form>
</h:panelGroup>
