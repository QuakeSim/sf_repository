		<h:form id="RunDisloc"
				rendered="#{!(empty DislocBean2.myFaultEntryForProjectList) 
							   && !(empty DislocBean2.myObsvEntryForProjectList)}" > 
			<h:panelGrid columns="1"  
             id="stuff89" 
				footerClass="subtitle" 
				headerClass="subtitlebig" styleClass="medium" 
				columnClasses="subtitle,medium"> 
 
				<h:outputFormat escape="false"  id="stuff90" 
					value="<b>Run Disloc</b><br><br>" /> 
				<h:outputFormat escape="false"   id="stuff91" 
					value="Click the button below to run Disloc.<br><br>" /> 
 
					<h:commandButton id="rundisloc" value="Run Disloc" 
						action="#{DislocBean2.runBlockingDislocJSF}" /> 
			</h:panelGrid> 
		</h:form> 
