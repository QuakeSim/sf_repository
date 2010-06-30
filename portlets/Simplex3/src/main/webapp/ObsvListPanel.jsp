			<h:panelGroup id="lkdrq8"
					rendered="#{SimplexBean.currentEditProjectForm.renderCreateObservationForm}">
				<h:form id="observationform">
					<h:panelGrid id="LayerTable" columns="2" footerClass="subtitle"
						headerClass="subtitlebig" styleClass="medium"
						columnClasses="subtitle,medium">

						<f:facet name="header">
							<b><h:outputFormat id="output2" escape="false"
								value="Input Observation Parameters" /></b>
						</f:facet>

						<h:outputText id="lkdrq9" value="Observation Name:" />
						<h:panelGroup id="lkdrq10">
							<h:inputText id="obsvName"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvName}"
								required="true" />
							<h:message id="lkdrq11" for="obsvName" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText id="lkdrq12" value="Observation Type:" />
						<h:selectOneMenu id="obsvType"
							value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvType}">
							<f:selectItem id="obsvTypeitem1" itemLabel="Displacement East"
								itemValue="1" />
							<f:selectItem id="obsvTypeitem2" itemLabel="Displacement North"
								itemValue="2" />
							<f:selectItem id="obsvTypeitem3" itemLabel="Displacement Up"
								itemValue="3" />
						</h:selectOneMenu>

						<h:outputText id="lkdrq13" value="Observation Value:" />
						<h:panelGroup id="lkdrq14">
							<h:inputText id="obsvValue"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvValue}"
								required="true" />
							<h:message id="lkdrq15" for="obsvValue" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText id="lkdrq16" value="Observation Error:" />
						<h:panelGroup id="lkdrq17">
							<h:inputText id="obsvError"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvError}"
								required="true" />
							<h:message id="lkdrq18" for="obsvError" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText id="lkdrq19" value="Location East:" />
						<h:panelGroup id="lkdrq111">
							<h:inputText id="obsvLocationEast"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvLocationEast}"
								required="true" />
							<h:message id="lkdrq112" for="obsvLocationEast" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText id="lkdrq113" value="Location North:" />
						<h:panelGroup id="lkdrq114">
							<h:inputText id="obsvLocationNorth"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvLocationNorth}"
								required="true" />
							<h:message for="obsvLocationNorth" showDetail="true"
										   id="lkdrq115"
											showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText id="lkdrq116" value="Reference Site?" />
						<h:selectOneMenu id="obsvRefSite"
							value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvRefSite}">
							<f:selectItem id="obsvRefSiteitem1" itemLabel="unselected"
								itemValue="1" />
							<f:selectItem id="obsvRefSiteitem2" itemLabel="selected"
								itemValue="-1" />
						</h:selectOneMenu>

						<h:commandButton id="addObservation" value="select"
							actionListener="#{SimplexBean.toggleAddObservationForProject}" />
					</h:panelGrid>
				</h:form>
			</h:panelGroup>
