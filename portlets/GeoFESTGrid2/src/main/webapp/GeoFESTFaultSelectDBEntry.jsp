			<h:form id="SelectFaultDBEntryForm"
				rendered="#{MGBean.renderAddFaultFromDBForm}">
				<h:dataTable value="#{MGBean.myFaultDBEntryList}" var="myentry1"
  				     id="erd15"
					binding="#{MGBean.myFaultDataTable}">

					<h:column  id="erd16">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>FaultName</b>" />
						</f:facet>
						<h:selectOneRadio layout="pageDirection"
						     id="erd17"
							valueChangeListener="#{MGBean.handleFaultsRadioValueChange}"
							onchange="dataTableSelectOneRadio(this)"
							onclick="dataTableSelectOneRadio(this)">
							<f:selectItems value="#{myentry1.faultName}" />
						</h:selectOneRadio>
					</h:column>

					<h:column   id="erd18">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>SegmentName</b>" />
						</f:facet>
						<h:outputText   id="erd19" value="#{myentry1.faultSegmentName}" />
					</h:column>

					<h:column   id="ldop1">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Author1</b>" />
						</f:facet>
						<h:outputText value="#{myentry1.faultAuthor}"   id="ldop2" />
					</h:column>

					<h:column   id="ldop3">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Segment Coordinates</b>" />
						</f:facet>
						<h:outputText   id="ldop4" value="#{myentry1.faultSegmentCoordinates}" />
					</h:column>
					<h:column   id="ldop5">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Action</b>" />
						</f:facet>
						<h:commandLink   id="ldop6" actionListener="#{MGBean.handleFaultEntryEdit}">
							<h:outputText value="Get"   id="ldop7"/>
						</h:commandLink>
					</h:column>
				</h:dataTable>
				<h:commandButton id="SelectFaultDBEntry" value="SelectFaultDBEntry"
					actionListener="#{MGBean.toggleSelectFaultDBEntry}" />
			</h:form>
