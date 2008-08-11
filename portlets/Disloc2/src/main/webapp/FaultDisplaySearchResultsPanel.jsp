			<h:form id="SelectFaultDBEntryForm" 
				rendered="#{DislocBean2.renderAddFaultFromDBForm}"> 

				<h:outputText id="dbisdownklj" escape="false"
								  value="<b>Error:</b> Data base returned no response.  Contact portal administrator."
								  rendered="#{DislocBean2.myFaultDBEntryList}"/>

				<h:dataTable rendered="#{!empty DislocBean2.myFaultDBEntryList}"
								 value="#{DislocBean2.myFaultDBEntryList}" 
								 var="myentry1" 
				    			 id="stuff74" 
								 binding="#{DislocBean2.myFaultDataTable}"> 
 
					<h:column  id="pg6"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>FaultName</b>" /> 
						</f:facet> 
						<h:selectOneRadio layout="pageDirection" 
						    id="stuff75" 
							valueChangeListener="#{DislocBean2.handleFaultsRadioValueChange}" 
							onchange="dataTableSelectOneRadio(this)" 
							onclick="dataTableSelectOneRadio(this)"> 
							<f:selectItems value="#{myentry1.faultName}" /> 
						</h:selectOneRadio> 
					</h:column> 
 
					<h:column  id="pg7"> 
						<f:facet name="header"> 
							<h:outputText  id="pg8" escape="false" value="<b>SegmentName</b>" /> 
						</f:facet> 
						<h:outputText value="#{myentry1.faultSegmentName}" /> 
					</h:column> 
 
					<h:column  id="pg9"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>Author1</b>" /> 
						</f:facet> 
						<h:outputText  id="pg10" value="#{myentry1.faultAuthor}" /> 
					</h:column> 
 
					<h:column  id="lereid1"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>Segment Coordinates</b>" /> 
						</f:facet> 
						<h:outputText  id="liered1" value="#{myentry1.faultSegmentCoordinates}" /> 
					</h:column> 
					<h:column  id="ldkjrle93d"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>InterpId</b>" /> 
						</f:facet> 
						<h:outputText  id="lie323291" value="#{myentry1.interpId}" /> 
					</h:column> 
					<h:column  id="lieraed1"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>Action</b>" /> 
						</f:facet> 
						<h:commandLink id="stuff76" 
							actionListener="#{DislocBean2.handleFaultEntryEdit}"> 
							<h:outputText value="Get" /> 
						</h:commandLink> 
					</h:column> 
				</h:dataTable> 
				<h:commandButton rendered="#{!empty DislocBean2.myFaultDBEntryList}"
									  id="SelectFaultDBEntry" 
									  value="SelectFaultDBEntry" 
									  actionListener="#{DislocBean2.toggleSelectFaultDBEntry}" /> 
			</h:form> 
 
