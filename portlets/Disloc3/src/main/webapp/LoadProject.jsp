<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%> 
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%> 
<html>
  <head> 
	 <link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/quakesim_style.css"/>
	 <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
  </head>
  <body>
	 <script type="text/javascript">
		function showLoading() {
		$("#loading").show();
		}
	 </script>  
	 <div id="loading">
		<center>
		  <p>
			 <img src="@host.base.url@@artifactId@/images/animation_processing.gif"/>
		  </p>
		</center>
	 </div>

	 <script type="text/javascript"> 
		//<![CDATA[
		
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
		
	   <h:outputText id="dislocLoadProjectTitle" styleClass="h2"
						  value="Project Archive Management"/>
		<h:outputText id="lptv11" value="You don't have any archived projects."
						  rendered="#{empty DislocBean2.myProjectNameList}"/>

		<h:panelGrid id="lptv12" 
						 columnClasses="alignTop,alignTop,alignTop" 
						 columns="3"  
						 rendered="#{!(empty DislocBean2.myProjectNameList)}"		  
						 border="1">
		  <h:form id="dislocLoadProject">
			 <h:panelGrid id="dislocpgridselect" columns="1" border="0"> 
				<h:outputText id="lptv21" escape="false" 
								  style="font-weight:bold" 
								  value="Select Project" /> 
				<h:outputText id="lptv22" escape="false" 
								  value="Please select from one of the previous projects." /> 
				<h:selectManyCheckbox id="projectlistforload"  
											 required="true"
											 value="#{DislocBean2.selectProjectsArray}" 		
											 onchange="dataTableSelectOneRadio(this)"
											 onclick="dataTableSelectOneRadio(this)"
											 layout="pageDirection"> 
				  <f:selectItems value="#{DislocBean2.myProjectNameList}" /> 
				</h:selectManyCheckbox> 
				<h:commandButton id="dislocSelectProject" 
									  onclick="showLoading()"
									  value="Select" 
									  action="#{DislocBean2.toggleSelectProject}" /> 
			 </h:panelGrid> 
		  </h:form> 

		  <h:form id="dislocCopyProject">
			 <h:panelGrid id="dislocpgridCopy" columns="1" border="0">
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
			  <h:commandButton id="dislocCpyProjectCmd" 
									 onclick="showLoading()"
									 value="Copy"
									 action="#{DislocBean2.toggleCopyProject}" />
			</h:panelGrid>
		 </h:form>

		 <h:form id="dislocDeleteProject"> 
			<h:panelGrid id="deleteProjectPGrid" columns="1"  
							 border="0"> 
			  <h:outputText escape="false" 
								 id="dislocDeleteProjectTitle"
								 style="font-weight:bold" 
								 value="Delete Projects"/> 
				<h:outputText escape="false" 
								  id="dislocDeleteProjectText"
								  value="Please select from one of the previous projects." /> 
				<h:selectManyCheckbox id="projectfordelete" 
											 required="true"
											 value="#{DislocBean2.deleteProjectsArray}" 
											 onchange="dataTableSelectOneRadio(this)"
											 onclick="dataTableSelectOneRadio(this)"
											 layout="pageDirection"> 
				  <f:selectItems value="#{DislocBean2.myProjectNameList}" /> 
				</h:selectManyCheckbox> 
				<h:commandButton id="dislocDeleteCmdButton" 
									  onclick="showLoading()"
									  value="Delete" 
									  action="#{DislocBean2.toggleDeleteProject}" /> 
			 </h:panelGrid> 
		  </h:form> 
		</h:panelGrid> 
     
		<f:verbatim><p/></f:verbatim>
		
		<h:form id="dislocMakeNewProject"> 
		  <f:verbatim>
			 <fieldset><legend class="portlet-form-label">Create New Project</legend>
		  </f:verbatim>	 
		  <h:outputText id="dislocCNPInstructions"
							 value="To create a new, blank project, type in a name and hit enter."/>
		  
		  <h:panelGrid columns="3" border="0"> 
			 <h:outputText id="lpj_projectname" value="Project Name:" /> 
			 <h:inputText id="projectName" 
							  value="#{DislocBean2.projectName}" 
							  required="true" /> 
			 
			 <h:commandButton id="lpj_makeselection" 
									value="Create Project" 
									action="#{DislocBean2.NewProjectThenEditProject}" /> 
		  </h:panelGrid> 
		  <f:verbatim></fieldset></f:verbatim>
		</h:form> 
		
		<%@ include file="footer.jsp" %> 
		  
		</f:view> 
	 </body> 
</html> 
