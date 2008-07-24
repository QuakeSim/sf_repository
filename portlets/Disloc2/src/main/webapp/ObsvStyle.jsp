		<h:panelGroup id="lck0ere93ks"
					rendered="#{DislocBean2.renderChooseObsvStyleForm}">
		      <h:form id="obsvStyleForm">
                	<h:outputText id="clrr33asz3" escape="false"
					    value="<b>Select Sites:</b>Click to choose scatter point."/>
					  <h:selectOneRadio layout="pageDirection" id="ere34ionssss" 
							value="#{DislocBean2.obsvStyleSelectionCode}"> 
							<f:selectItem id="item021" 
											  itemLabel="Rectangular grid of observation points" 
											  itemValue="GridStyle" /> 
							<f:selectItem id="item0332" 
								itemLabel="Scattered observation points." 
								itemValue="ScatterStyle" /> 
					  </h:selectOneRadio> 
					  <h:commandButton id="chooseAStyle" value="Choose Style"
						 		actionListener="#{DislocBean2.toggleSetObsvStyle}"/>
					 </h:form>
		        </h:panelGroup>
