			<h:form id="layerform" rendered="#{MGBean.renderCreateNewLayerForm}">

				<h:panelGrid id="LayerTable" columns="2" footerClass="subtitle"
					headerClass="subtitlebig" styleClass="medium"
					columnClasses="subtitle,medium">

					<f:facet name="header">
						<h:outputFormat id="output2" escape="false"
							value="<b>Input Solid Layer Geometry </b>" />
					</f:facet>

					<h:outputText  id="stuff6" value="Layer Name:" />
					<h:panelGroup  id="stuff7">
						<h:inputText id="LayerName"
							value="#{MGBean.currentLayer.layerName}" required="true" />
						<h:message  id="stuff77" 
							for="LayerName" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText  id="stuff8" value="Origin X:" />
					<h:panelGroup  id="stuff9">
						<h:inputText id="LayerOriginX"
							value="#{MGBean.currentLayer.layerOriginX}" required="true" />
						<h:message  id="stuff10"
						   for="LayerOriginX" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Origin Y:"  id="stuff11"/>
					<h:panelGroup  id="stuff12">
						<h:inputText id="LayerOriginY"
							value="#{MGBean.currentLayer.layerOriginY}" required="true" />
						<h:message for="LayerOriginY" showDetail="true" showSummary="true"
									   id="stuff123"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Origin Z:"  id="stuff13"/>
					<h:panelGroup  id="stuff14">
						<h:inputText id="LayerOriginZ"
							value="#{MGBean.currentLayer.layerOriginZ}" required="true" />
						<h:message for="LayerOriginZ" showDetail="true" showSummary="true"
									   id="stuff15"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText  id="stuff16" value="Length:" />
					<h:panelGroup  id="stuff17">
						<h:inputText id="LayerLength"
							value="#{MGBean.currentLayer.layerLength}" required="true" />
						<h:message for="LayerLength" showDetail="true" showSummary="true"
									   id="stuff18"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Width:"  id="stuff19"/>
					<h:panelGroup  id="stuff20">
						<h:inputText id="LayerWidth"
							value="#{MGBean.currentLayer.layerWidth}" required="true" />
						<h:message for="LayerWidth" showDetail="true" showSummary="true"
									   id="stuff21"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Depth:"  id="stuff22" />
					<h:panelGroup  id="stuff23">
						<h:inputText id="LayerDepth"
							value="#{MGBean.currentLayer.layerDepth}" required="true" />
						<h:message for="LayerDepth" showDetail="true" showSummary="true"
									   id="stuff24"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Lame Lambda:"  id="stuff25"/>
					<h:panelGroup  id="stuff26">
						<h:inputText id="LayerLameLambda"
							value="#{MGBean.currentLayer.lameLambda}" required="true" />
						<h:message for="LayerLameLambda" showDetail="true"
						 id="stuff27"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Lame Mu:"  id="stuff28" />
					<h:panelGroup  id="stuff29">
						<h:inputText id="LayerLameMu"
							value="#{MGBean.currentLayer.lameMu}" required="true" />
						<h:message for="LayerLameMu" showDetail="true" showSummary="true"
									   id="stuff30"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Viscosity:" id="stuff31"/>
					<h:panelGroup  id="stuff32">
						<h:inputText id="LayerViscosity"
							value="#{MGBean.currentLayer.viscosity}" required="true" />
						<h:message for="LayerViscosity" showDetail="true"			
									   id="stuff33"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Exponent:"  id="stuff34"/>
					<h:panelGroup  id="stuff35">
						<h:inputText id="LayerExponent"
							value="#{MGBean.currentLayer.exponent}" required="true" />
						<h:message for="LayerExponent" showDetail="true"
									   id="stuff36"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:commandButton id="addlayer" value="select"
						actionListener="#{MGBean.toggleAddLayerForProject}" />
				</h:panelGrid>
			</h:form>
