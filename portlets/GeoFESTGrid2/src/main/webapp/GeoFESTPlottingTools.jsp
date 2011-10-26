		<h:form id="PlottingTools">
			<h:panelGroup 				
			   id="pg26"
				rendered="#{!empty MGBean.myFaultEntryForProjectList 
							|| !empty MGBean.myLayerEntryForProjectList}">
				<h:outputFormat id="pg27" escape="false" value="<b>Plotting Tools</b><br><br>" />
				<h:panelGrid id="pg28" columns="1" border="0">
					<h:outputFormat id="pg29" escape="false"
						value="<br>Use Java Web Start mesh viewer by clicking the link below." />

					<h:outputLink id="link1" value="#{facesContext.externalContext.requestContextPath}/painter.jsp">
						<f:param name="layers" value="#{MGBean.myLayersParamForJnlp}" />
						<f:param name="faults" value="#{MGBean.myFaultsParamForJnlp}" />
						<h:outputText value="Web Start Plotter" />
					</h:outputLink>

				</h:panelGrid>
			</h:panelGroup>
		</h:form>
