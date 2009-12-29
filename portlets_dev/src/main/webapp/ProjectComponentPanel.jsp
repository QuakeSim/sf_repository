	<h:panelGroup  id="stuff77" 
		rendered="#{!empty DislocBean2.myFaultEntryForProjectList 
					   || !empty DislocBean2.myObsvEntryForProjectList}"> 
 
	<h:outputText  id="stuff78" styleClass="header2" value="Current Project Components"
						rendered="#{!empty DislocBean2.myFaultEntryForProjectList 
					   || !empty DislocBean2.myObsvEntryForProjectList}"/> 
 

	<h:panelGrid id="ProjectComponentList" columns="2" border="1" 
			columnClasses="alignTop, alignTop"> 
 
		<h:panelGroup id="stuff79" rendered="#{!empty DislocBean2.myFaultEntryForProjectList}"> 
          <%@ include file="ProjectFaultPanel.jsp" %> 
          <%@ include file="ProjectObsvPanel.jsp" %> 
		</h:panelGroup> 

   <%@ include file="DislocSubmitPanel.jsp" %> 

	</h:panelGrid> 
  </h:panelGroup> 
