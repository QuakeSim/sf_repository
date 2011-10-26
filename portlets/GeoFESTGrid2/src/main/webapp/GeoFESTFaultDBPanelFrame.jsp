			<h:form id="SelectLayerDBEntryForm"
				rendered="#{MGBean.renderAddLayerFromDBForm}">
				<h:dataTable value="#{MGBean.myLayerDBEntryList}" var="myentry2"
				    id="der1"
					binding="#{MGBean.myLayerDataTable}">
					<h:column   id="der2">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>LayerName</b>" />
						</f:facet>
						<h:selectOneRadio layout="pageDirection"
						     id="der23"
							valueChangeListener="#{MGBean.handleLayersRadioValueChange}"
							onchange="dataTableSelectOneRadio(this)"
							onclick="dataTableSelectOneRadio(this)">
							<f:selectItems value="#{myentry2.layerName}" />
						</h:selectOneRadio>
					</h:column>
					<h:column   id="der3">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Author1</b>" />
						</f:facet>
						<h:outputText   id="der4" value="#{myentry2.layerAuthor}" />
					</h:column>
					<h:column   id="der5">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Action</b>" />
						</f:facet>
						<h:commandLink id="cl5" actionListener="#{MGBean.handleLayerEntryEdit}">
							<h:outputText value="Get" />
						</h:commandLink>
					</h:column>
				</h:dataTable>

				<h:commandButton id="SelectLayerDBEntry" value="SelectLayerDBEntry"
					actionListener="#{MGBean.toggleSelectLayerDBEntry}" />
			</h:form>
