			<h:form id="faultlatlonsearchform" 
				rendered="#{DislocBean2.renderSearchByLatLonForm}"> 
				<h:panelGrid id="FaultLatLonSearch" columns="1" 
					footerClass="subtitle" headerClass="subtitlebig" 
					styleClass="medium" columnClasses="subtitle,medium"> 
					<h:outputFormat escape="false"  id="stuff52" 
						value="<b>Search Fault DB by Bounding Latitude and Longitude</b><br><br>" /> 
					<h:outputFormat escape="false"  id="stuff53" 
						value="Enter the starting and ending latitude and longitude values (in decimal degrees) of the search bounding box. All faults completely within the bounding box will be returned.<br><br>" /> 
 
					<h:panelGrid columns="2" border="0"  id="stuff54"> 
						<h:outputText value="Starting Latitude: "  id="stuff55"/> 
						<h:panelGroup  id="stuff56"> 
							<h:inputText id="StartingLatitude" 
							    id="stuff57" 
								value="#{DislocBean2.faultLatStart}" required="true" /> 
							<h:message for="StartingLatitude" showDetail="true" 
							    id="stuff58" 
								showSummary="true" errorStyle="color: red" /> 
						</h:panelGroup> 
 
						<h:outputText  id="stuff59" value="Ending Latitude: " /> 
						<h:panelGroup  id="stuff60"> 
							<h:inputText id="EndingLatitude" value="#{DislocBean2.faultLatEnd}" 
								required="true" /> 
							<h:message for="EndingLatitude" showDetail="true" 
							     id="stuff61" 
								showSummary="true" errorStyle="color: red" /> 
						</h:panelGroup> 
						<h:outputText value="Starting Longitude: "   id="stuff62"/> 
						<h:panelGroup  id="stuff63"> 
							<h:inputText id="StartingLongitude" 
							     id="stuff64" 
								value="#{DislocBean2.faultLonStart}" required="true" /> 
							<h:message for="StartingLongitude" showDetail="true" 
							     id="stuff65" 
								showSummary="true" errorStyle="color: red" /> 
						</h:panelGroup> 
						<h:outputText value="Ending Longitude: "   id="stuff66"/> 
						<h:panelGroup   id="stuff67"> 
							<h:inputText id="EndingLongitude" value="#{DislocBean2.faultLonEnd}" 
								required="true" /> 
							<h:message for="EndingLongitude" showDetail="true" 
							    id="stuff68" 
								showSummary="true" errorStyle="color: red" /> 
						</h:panelGroup> 
						<h:panelGroup> 
 
							<h:commandButton value="Query" 
							     id="stuff69" 
								actionListener="#{DislocBean2.toggleFaultSearchByLonLat}" /> 
						</h:panelGroup> 
					</h:panelGrid> 
				</h:panelGrid> 
			</h:form> 
