
		<h:form id="MeshGen">
			<h:panelGrid columns="1" 
			   id="pg30" 
				rendered="#{!empty MGBean.myFaultEntryForProjectList 
							&& !empty MGBean.myLayerEntryForProjectList}"
				footerClass="subtitle"
				headerClass="subtitlebig" styleClass="medium"
				columnClasses="subtitle,medium">

				<h:outputFormat escape="false"
				     id="pg31" 
					value="<b>Create Initial Mesh</b><br><br>" />
				<h:outputFormat escape="false"
                 id="pg32" 
					value="Click the button below to generate a mesh for the geometry.<br><br>" />
			  
           <h:outputText  id="pg33" value="Mesh Refinement Level"/>
			  <h:selectOneListbox title="Mesh Refinement Level"
			  		      size="1"
			  		      id="refinementr43c" 
					      required="true" value="#{MGBean.meshResolution}">
			     <f:selectItem id="rare" itemValue="rare"/>
			     <f:selectItem id="orig" itemValue="original"/>
			     <f:selectItem id="extra" itemValue="extra"/>
		 	  </h:selectOneListbox>
					
           <h:outputText  id="pg33ere" value="Mesh Refinement Host"/>
			  <h:selectOneListbox title="Mesh Refinement Host"
			  		      size="1"
			  		      id="refhost3094" 
					      required="true" value="#{MGBean.gridRefinerHost}">
			     <f:selectItems id="leihrc93" value="#{MGBean.gridHostList}"/>
		 	  </h:selectOneListbox>

				<h:panelGrid   id="pg34" columns="2" border="0">
					<h:commandButton id="meshgencb" value="Generate Mesh"
						action="#{MGBean.runNonBlockingMeshGenerartorJSF}" />
				</h:panelGrid>

			</h:panelGrid>
		</h:form>
