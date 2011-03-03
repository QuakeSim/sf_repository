<h:panelGroup id="lck0ere93ks"
				  rendered="#{DislocBean2.renderChooseObsvStyleForm}">
  <f:verbatim>
	 <fieldset><legend class="portlet-form-label">Choose Observation Style</legend>
  </f:verbatim>	 
  
  <h:form id="obsvStyleForm">
	 <h:outputText id="clrr33asz3" escape="false"
						value="Click to choose either grid or scatter point observation style."/>
	 <h:selectOneRadio layout="pageDirection" id="ere34ionssss" 
							 value="#{DislocBean2.obsvStyleSelectionCode}"> 
		<f:selectItem id="item02121" 
						  itemLabel="Rectangular grid of observation points" 
						  itemValue="GridStyle" /> 
		<f:selectItem id="item0332" 
						  itemLabel="Scattered observation points." 
								itemValue="ScatterStyle" /> 
	 </h:selectOneRadio> 
	 <h:commandButton id="chooseAStyle" value="Choose Style"
							actionListener="#{DislocBean2.toggleSetObsvStyle}"/>
  </h:form>
  <f:verbatim></fieldset></f:verbatim>			 
</h:panelGroup>

