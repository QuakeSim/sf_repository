<h:panelGroup id="lkdrq7Aria" 
				  rendered="#{SimplexBean.currentEditProjectForm.renderAriaObsvCutPaste}">
  <h:form id="obsvAriaCutPaste">
	 <f:verbatim>
		<b>Import ARIA Coseismic Displacements: </b>Enter one observation point per line in 
		following format:<br/>
		#Comments begin with a # on each line.<br/>
		#Colmns are LON LAT East(m) North(m) Vert(m) Sigma(m) Site, separated by space<br/>
		#Example values below<br/>
		136.8511277530  .46538729501  0.0294  0.02  0.0081  0.0355012675830033 0065<br/>
	 </f:verbatim>
	 
	 <h:panelGrid id="ObsvAriaTextArea" columns="1">
		<h:inputTextarea id="obsvAriaTextArea"
							  rows="20" cols="100"
							  value="#{SimplexBean.currentEditProjectForm.obsvAriaTextArea}"/>
		<h:commandButton id="addAriaObsvTextArea" value="select"
							  actionListener="#{SimplexBean.toggleAddAriaObsvForProject}" />
	 </h:panelGrid>
  </h:form>
</h:panelGroup>
