	       <h:form id="obsvform" rendered="#{DislocBean2.renderDislocGridParamsForm}"> 

	  <h:panelGrid id="dflelerkljk11662" columns="1" border="1" cellpadding="0" cellspacing="0" styleClass="centered">
		<h:outputFormat id="dflelerkal512" escape="false" value="<b>Observation Component</b>">
		</h:outputFormat>

				<h:panelGrid id="ObsvTable" columns="2" footerClass="subtitle" 
					headerClass="subtitlebig" styleClass="medium" 
					columnClasses="subtitle,medium">   
 
					<f:facet name="header"> 
						<h:outputFormat id="output2" escape="false" 
							value="<b>Project Origin and Style</b>" /> 
					</f:facet> 

 					<h:outputText  id="stuff223" value="Project Origin Lat:" /> 
					<h:panelGroup  id="stuff543"> 
						<h:inputText id="origin_latlieew" 
							value="#{DislocBean2.currentParams.originLat}" required="true" /> 
					</h:panelGroup> 

 					<h:outputText  id="stuff2434" value="Project Origin Lon:" /> 
					<h:panelGroup  id="stuff24sx5"> 
						<h:inputText id="origin_lonlkd" 
							value="#{DislocBean2.currentParams.originLon}" required="true" /> 
					</h:panelGroup> 
					
 					<h:outputText  id="stuf334" value="Observation Style:" /> 
					<h:panelGroup>
					<h:outputText id="stylejkrejonlkd" 
					              value="#{DislocBean2.currentParams.observationPointStyle}"/>
					</h:panelGroup>	      
				</h:panelGrid>

				<h:panelGrid id="erep912e3" rendered="#{!DislocBean2.usesGridPoints}">
					<f:facet name="header"> 
						<h:outputFormat id="outputere2" escape="false" 
							value="<b>Scatter-Style Observation Points </b>" /> 
					</f:facet> 
					<h:dataTable value="#{DislocBean2.myPointObservationList}"
									 binding="#{DislocBean2.myScatterPointsTable}"
									 var="xypoints"
									 id="xypointsq3u">
 						<h:column>
					     <f:facet name="header"> 
						   <h:outputFormat id="outputere32" escape="false" 
							   value="<b>Lat</b>" /> 
					     </f:facet> 

						  <h:outputText id="akjlatelkr34je" value="#{xypoints.latPoint}"/>
    					</h:column>
						<h:column>
					     <f:facet name="header"> 
						   <h:outputFormat id="outputere42" escape="false" 
							   value="<b>Lon</b>" /> 
					     </f:facet> 
						  <h:outputText id="rerdad62lon" value="#{xypoints.lonPoint}"/>
    					</h:column>
						<h:column>
					     <f:facet name="header"> 
						   <h:outputFormat id="outputere62" escape="false" 
							   value="<b>Remove</b>" /> 
					     </f:facet> 
						   <h:commandButton value="Delete" id="delxypointr4ero"
												  actionListener="#{DislocBean2.deleteScatterPoint}"/> 
						</h:column>
					</h:dataTable>
            </h:panelGrid>

				<h:panelGrid id="eiurojd93" columns="2" rendered="#{DislocBean2.usesGridPoints}">
					<f:facet name="header"> 
						<h:outputFormat id="outputere72" escape="false" 
							value="<b>Define Grid of Observation Points </b>" /> 
					</f:facet> 

 					<h:outputText  id="stuff2" value="Grid Minimum X Value:" /> 
					<h:panelGroup  id="stuff5"> 
						<h:inputText id="minx" 
							value="#{DislocBean2.currentParams.gridMinXValue}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff3" value="X Spacing:" /> 
					<h:panelGroup  id="stuff6"> 
						<h:inputText id="xspacing" 
							value="#{DislocBean2.currentParams.gridXSpacing}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff7" value="X Iterations" /> 
					<h:panelGroup  id="stuff8"> 
						<h:inputText id="xiterations" 
							value="#{DislocBean2.currentParams.gridXIterations}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff9" value="Grid Minimum Y Value:" /> 
					<h:panelGroup  id="stuff10"> 
						<h:inputText id="miny" 
							value="#{DislocBean2.currentParams.gridMinYValue}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff11" value="Y Spacing:" /> 
					<h:panelGroup   id="pg2"> 
						<h:inputText id="yspacing" 
							value="#{DislocBean2.currentParams.gridYSpacing}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff12" value="Y Iterations" /> 
					<h:panelGroup  id="stuff13"> 
						<h:inputText id="yiterations" 
							value="#{DislocBean2.currentParams.gridYIterations}" required="true" /> 
					</h:panelGroup> 
				</h:panelGrid>
 
				<h:panelGrid id="eiurojd93124" columns="2">
					<h:commandButton id="addobservation" value="update" 
						actionListener="#{DislocBean2.toggleAddObservationsForProject}" /> 
					<h:commandButton id="deleteobservation" value="delete" 
						actionListener="#{DislocBean2.deleteObsv}" /> 
				</h:panelGrid>
</h:panelGrid>
			</h:form> 
