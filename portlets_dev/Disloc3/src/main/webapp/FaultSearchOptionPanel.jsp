			<h:form id="faultselection" 
				rendered="#{DislocBean2.renderAddFaultSelectionForm}">							
				<h:panelGrid id="AddFaultSelection" columns="1" 
					footerClass="subtitle" headerClass="subtitlebig" 
					styleClass="medium" columnClasses="subtitle,medium"> 
					<h:panelGroup   id="stuff41"> 
						<h:outputFormat escape="false" 
						    id="stuff42" 
							value="<b>Fault Database Selection</b><br><br>" /> 
						<h:outputFormat escape="false" 
						     id="stuff43" 
							value="You may select faults from the Fault Database using author search, latitude/longitude bounding box, or by viewing the master list (long).<br><br>" /> 
						<h:outputFormat escape="false" 
						    id="pg211" 
							value="Please choose a radio button and click <b>Select</b>.<br><br>" /> 
					</h:panelGroup> 
 
					<h:panelGroup id="stuff44"> 
						<h:selectOneRadio layout="pageDirection" id="subscriptionssss" 
							value="#{DislocBean2.faultSelectionCode}"> 
							<f:selectItem id="item01" itemLabel="Search by fault name." 
								itemValue="SearchByFaultName" /> 
							<f:selectItem id="item02" 
								itemLabel="Search by Lat/Lon bounding box." 
								itemValue="SearchByLatLon" /> 
							<f:selectItem id="item03" itemLabel="Search by author." 
								itemValue="SearchByAuthor" /> 
							<f:selectItem id="item04" itemLabel="View all faults." 
								itemValue="ViewAllFaults" /> 
						</h:selectOneRadio> 
						<h:commandButton id="button122" value="Make Selection" 
							actionListener="#{DislocBean2.toggleFaultSelection}" /> 
					</h:panelGroup> 
				</h:panelGrid> 
			</h:form> 
