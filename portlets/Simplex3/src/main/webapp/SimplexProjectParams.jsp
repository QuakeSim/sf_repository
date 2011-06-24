<h:panelGroup id="SimplexProjectParams" 
				  rendered="#{SimplexBean.currentEditProjectForm.renderProjectParams}">
  <h:form id="simplexProjectParamsForm">
	 <f:verbatim>
		<fieldset><legend class="portlet-form-label">Simplex Project Parameters</legend>
	 </f:verbatim>	 
	 
	 <h:panelGrid id="projectParamsGrid" 
					  columns="2">
		<f:verbatim>Project Name:</f:verbatim>
		<h:outputText id="simplexProjectName"
						  value="#{SimplexBean.projectName}"/>
		<f:verbatim>Starting Temperature:</f:verbatim>
		<h:inputText id="simplexStartTemp"
						 required="true"
						 value="#{SimplexBean.currentProjectEntry.startTemp}"/>
		<f:verbatim>Maximum Iterations:</f:verbatim>
		<h:inputText id="simplexMaxIters"
						  required="true"
						 value="#{SimplexBean.currentProjectEntry.maxIters}"/>
		<f:verbatim>Project Origin Lon:</f:verbatim>
		<h:inputText id="simplexOriginLon"						  
						 required="true"
						 value="#{SimplexBean.currentProjectEntry.origin_lon}"/>
		<f:verbatim>Project Origin Lat:</f:verbatim>
		<h:inputText id="simplexOriginLat"
						 required="true"
						 value="#{SimplexBean.currentProjectEntry.origin_lat}"/>
	 </h:panelGrid>
	 <h:commandButton id="addSARObsvTextArea" value="Update Project Parameters"
							actionListener="#{SimplexBean.toggleUpdateSimplexProjectParams}" />
	 <f:verbatim></fieldset></f:verbatim>
  </h:form>
</h:panelGroup>
