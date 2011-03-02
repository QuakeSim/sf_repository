<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%> 
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%> 
<html>
  <head> 
	 <link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/quakesim_style.css"/>
  </head>
  <body>
	 <script type="text/javascript"> 
		<![CDATA[
		
		function selectOne(form , button) { 
		turnOffRadioForForm(form); 
		button.checked = true; 
		} 
		
		function turnOffRadioForForm(form){
		for(var i=0; i<form.elements.length; i++) {
		form.elements[i].checked = false;
		}
		}
		
		function dataTableSelectOneRadio(radio) { 
		var id = radio.name.substring(radio.name.lastIndexOf(':')); 
		var el = radio.form.elements; 
		//alert (el.length); 
		for (var i = 0; i < el.length; i++) { 
		if (el[i].name.substring(el[i].name.lastIndexOf(':')) == id) { 
		//alert (el[i].checked) 
		el[i].checked = false; 
		el[i].checked = false; 
		} 
		} 
		radio.checked = true;
		}
		//]]>
	 </script>

	 <f:view>
		<h:messages id="dislocMessagesLoadProject" 
						showDetail="true"
						showSummary="true"
						errorStyle="color: red"/>
		
		<h:outputText id="lptv1" styleClass="h2" value="Project Archive"/>
		
		<h:outputText id="lptv11" value="You don't have any archived projects."
						  rendered="#{empty DislocBean2.myProjectNameList}"/>

		<h:panelGrid id="lptv12" 
						 columnClasses="alignTop,alignTop,alignTop" 
						 columns="3"  
						 rendered="#{!(empty DislocBean2.myProjectNameList)}"		  
						 border="1"> 
		  <h:form id="dislocLoadProject">
			 <h:panelGrid columns="1" border="0"> 
				<h:outputText id="lptv21" escape="false" 
								  style="font-weight:bold" 
								  value="Select Project" /> 
				<h:outputText id="lptv22" escape="false" 
								  value="Please select from one of the previous projects." /> 
				<h:selectManyCheckbox id="projectlistforload"  
											 value="#{DislocBean2.selectProjectsArray}" 		
											 onchange="dataTableSelectOneRadio(this)"
											 onclick="dataTableSelectOneRadio(this)"
											 layout="pageDirection"> 
				  <f:selectItems value="#{DislocBean2.myProjectNameList}" /> 
				</h:selectManyCheckbox> 
				<h:commandButton value="Select" 
									  action="#{DislocBean2.toggleSelectProject}" /> 
			 </h:panelGrid> 
		  </h:form> 
		  
		  <h:form id="dislocCopyProject">
			 <h:panelGrid columns="1" border="0">
			  <h:outputText id="dislocCopyProjectTitle"
								 escape="false" 
								 style="font-weight:bold" 								 
								 value="Copy Project" />
			  <h:outputText id="dislocCopyProjectText"
								 escape="false"
								 value="Please select from one of the previous projects." />
			  
			  <h:selectManyCheckbox id="projectlistforcopy"  
									  required="true"
									  value="#{DislocBean2.copyProjectsArray}"
									  onchange="dataTableSelectOneRadio(this)"
									  onclick="dataTableSelectOneRadio(this)"
									  layout="pageDirection">					

				 <f:selectItems value="#{DislocBean2.myProjectNameList}" />
			  </h:selectManyCheckbox>
			  
			  <h:panelGroup id="dislocCopyProjectGroup">
				 <h:outputText id="dislocCopyProjectNewTitle"
									value="New ProjectName:"/>
				 <h:inputText id="newSimplexCopyProjectName" 
								  value="#{DislocBean2.projectName}"
								  required="true"/>
			  </h:panelGroup>
			  <h:commandButton value="Copy"
									 action="#{DislocBean2.toggleCopyProject}" />
			</h:panelGrid>
		 </h:form>
		 
		 <h:form id="dislocDeleteProject"> 
			<h:panelGrid columns="1"  
							 border="0"> 
			  <h:outputText escape="false" 
								 id="dislocDeleteProjectTitle"
								 style="font-weight:bold" 
								 value="Delete Projects"/> 
				<h:outputText escape="false" 
								  id="dislocDeleteProjectText"
								  value="Please select from one of the previous projects." /> 
				<h:selectManyCheckbox id="projectfordelete" 
											 value="#{DislocBean2.deleteProjectsArray}" 
											 onchange="dataTableSelectOneRadio(this)"
											 onclick="dataTableSelectOneRadio(this)"
											 layout="pageDirection"> 
				  <f:selectItems value="#{DislocBean2.myProjectNameList}" /> 
				</h:selectManyCheckbox> 
				<h:commandButton value="Delete" 
									  action="#{DislocBean2.toggleDeleteProject}" /> 
			 </h:panelGrid> 
		  </h:form> 
		  
		</h:panelGrid> 
		
		<h:form id="dislocMakeNewProject"> 
		  <h:outputText id="dislocNewProjectName"
							 styleClass="h3"
							 value="New Project Name"/>
			 <h:panelGrid columns="2" border="1"> 
				<h:outputText id="lpj_projectname" value="Project Name:" /> 
				<h:panelGroup> 
			  <h:inputText id="projectName" 
								value="#{DislocBean2.projectName}" 
								required="true" /> 
			  <h:message for="projectName" showDetail="true" showSummary="true" 
							 errorStyle="color: red" /> 
				</h:panelGroup> 
				
			 </h:panelGrid> 
			 <h:commandButton id="lpj_makeselection" value="Make Selection" 
									action="#{DislocBean2.NewProjectThenEditProject}" /> 
			 
		  </h:form> 

   <%@ include file="footer.jsp" %> 
 
</f:view> 
</body> 
</html> 
