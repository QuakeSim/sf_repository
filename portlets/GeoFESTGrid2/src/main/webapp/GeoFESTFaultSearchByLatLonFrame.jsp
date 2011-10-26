			<h:form id="faultlatlonsearchform"
				rendered="#{MGBean.renderSearchByLatLonForm}">
				<h:panelGrid id="FaultLatLonSearch" columns="1"
					footerClass="subtitle" headerClass="subtitlebig"
					styleClass="medium" columnClasses="subtitle,medium">
					<h:outputFormat escape="false"
					  id="der17"
						value="<b>Search Fault DB by Bounding Latitude and Longitude</b><br><br>" />
					<h:outputFormat escape="false"  id="der18"
						value="Enter the starting and ending latitude and longitude values (in decimal degrees) of the search bounding box. All faults completely within the bounding box will be returned.<br><br>" />

					<h:panelGrid id="pgfaultlatlon" columns="2" border="0">

						<h:outputText value="Starting Latitude: "   id="erd19"/>
						<h:panelGroup  id="erd1">
							<h:inputText id="StartingLatitude"
								value="#{MGBean.faultLatStart}" required="true" />
							<h:message for="StartingLatitude" showDetail="true"
										   id="stuff70"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="Ending Latitude: "   id="erd2"/>
						<h:panelGroup  id="erd3">
							<h:inputText id="EndingLatitude" value="#{MGBean.faultLatEnd}"
								required="true" />
							<h:message for="EndingLatitude" showDetail="true"
										   id="stuff71"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:outputText value="Starting Longitude: "   id="erd4"/>
						<h:panelGroup   id="erd5">
							<h:inputText id="StartingLongitude"
								value="#{MGBean.faultLonStart}" required="true" />
							<h:message for="StartingLongitude" showDetail="true"
										   id="stuff72"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:outputText value="Ending Longitude: "   id="erd6"/>
						<h:panelGroup  id="erd7">
							<h:inputText id="EndingLongitude" value="#{MGBean.faultLonEnd}"
								required="true" />
							<h:message for="EndingLongitude" showDetail="true"
										   id="stuff73"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:panelGroup  id="erd8">
							<h:commandButton id="cb7" value="Query"
								actionListener="#{MGBean.toggleFaultSearchByLonLat}" />
						</h:panelGroup>
					</h:panelGrid>

				</h:panelGrid>
			</h:form>
