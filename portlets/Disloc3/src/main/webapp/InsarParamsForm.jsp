<h:form id="InsarParamsForm" rendered="#{DislocBean2.renderInsarParamsForm}">
  <f:verbatim>
	 <fieldset><legend class="portlet-form-label">Set Interferogram Parameters</legend>
  </f:verbatim>	 
  <h:panelGrid id="InsarParamsGrid" 
					columns="2">
	 <%-- Column 1 --%>
	 <f:verbatim>
		<b>Elevation (Deg): </b>
	 </f:verbatim>
	 <h:inputText id="insarElevationParam" 
					  value="#{DislocBean2.elevation}"
					  required="true"/>

	 <%-- Column 2--%>
	 <f:verbatim>
		<b>Azimuth (Deg): </b>
	 </f:verbatim>
	 <h:inputText id="insarAzimuthParam" 
					  value="#{DislocBean2.azimuth}"
					  required="true"/>

	 <%-- Column 3 --%>
	 <f:verbatim>
		<b>Frequency (GHz): </b>
	 </f:verbatim>
	 <h:inputText id="insarFrequencyParam" 
					  value="#{DislocBean2.frequency}"
					  required="true"/>

  </h:panelGrid>
  
  <h:commandButton id="updateInsarParams" 
						 value="Update Values"
						 actionListener="#{DislocBean2.toggleInsarParamsUpdate}"/>
  <f:verbatim></fieldset></f:verbatim>			   
</h:form>
