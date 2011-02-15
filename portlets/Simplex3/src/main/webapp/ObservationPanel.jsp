<h:panelGroup id="lkdrq7" rendered="#{SimplexBean.currentEditProjectForm.renderCreateObsvCutPaste}">
 <h:form id="obsvCutPaste">
  <h:outputText id="cutinstruct1" escape="false" value="<b>Mass Observation Import</b><br>Enter one observation point per line in following format:<br/>" /> 
  <h:outputText id="cutinstruct111" escape="false" value="ObservationType LocationEast LocationNorth Value Uncertainty <br/>" />
  <h:outputText id="cutinstruct112" escape="false" value="Values can be either space or comma separated."/>

     <h:panelGrid id="ObsvTextAreada" columns="1">
  <h:inputTextarea id="obsvTextArea"
							   rows="20" cols="50"
							 	value="#{SimplexBean.currentEditProjectForm.obsvTextArea}"/>
					     <h:commandButton id="addObsvTextArea" value="select"
							   actionListener="#{SimplexBean.toggleAddObsvTextAreaForProject}" />
						</h:panelGrid>
					 </h:form>
			</h:panelGroup>
