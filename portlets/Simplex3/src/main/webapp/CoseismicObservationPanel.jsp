<h:panelGroup id="lkdrq7Coseis" 
				  rendered="#{SimplexBean.currentEditProjectForm.renderCoseismicDisp}">
  <h:form id="obsvCoseismicCutPaste">
	 <f:verbatim>
		<b>Import Coseismic Displacements: </b>Enter one observation point per line in 
		following format:<br/>
		#Comments begin with a * on each line.<br/>
		#Colmns are LON LAT East(mm) North(mm) E+/-(mm) N +/-(mm) Up(mm) U+/-(mm) StationName<br/>
		#Example values below<br/>
		 243.57062  34.59428    -0.11    -3.03     1.02     1.11      2.89     2.24 AGMT <br/>
	 </f:verbatim>
	 
	 <h:panelGrid id="ObsvCoseismicTextArea" columns="1">
		<h:inputTextarea id="obsvCoseismicTextArea"
							  rows="20" cols="100"
							  value="#{SimplexBean.currentEditProjectForm.coseismicTextArea}"/>
		<h:commandButton id="addCoseismicTextArea" value="select"
							  actionListener="#{SimplexBean.toggleAddCoseismicObsvForProject}" />
	 </h:panelGrid>
  </h:form>
</h:panelGroup>
