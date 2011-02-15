
			<h:panelGroup id="fredfd23" rendered="#{SimplexBean.currentEditProjectForm.renderAddFaultSelectionForm}">
				<h:form id="faultselection">
					<h:panelGrid id="AddFaultSelection" columns="1"
						footerClass="subtitle" headerClass="subtitlebig"
						styleClass="medium" columnClasses="subtitle,medium">
						<h:panelGroup id="dflelerkljk111">
							<h:outputFormat  id="dflelerkljk112" escape="false"
								value="Fault Database Selection<br/>" />
							<h:outputFormat  id="dflelerkljk113" escape="false"
								value="You may select faults from the Fault Database using author search, latitude/longitude bounding box, or by viewing the master list (long).<br/>" />
							<h:outputFormat  id="dflelerkljk114" escape="false"
								value="Please choose a radio button and click Select.<br/>"/>
						</h:panelGroup>

						<h:panelGroup id="dflelerkljk115">
							<h:selectOneRadio layout="pageDirection" id="subscriptionssss"
								value="#{SimplexBean.currentEditProjectForm.faultSelectionCode}">
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
								actionListener="#{SimplexBean.currentEditProjectForm.toggleFaultSelection}" />
						</h:panelGroup>
					</h:panelGrid>
				</h:form>
			</h:panelGroup>

