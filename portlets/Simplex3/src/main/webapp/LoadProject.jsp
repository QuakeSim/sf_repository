<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
		function selectOne(form, button)
		{
      turnOffRadioForForm(form);
		button.checked = true;
		}
		
		function turnOffRadioForForm(form) {
		for(var i = 0 ; i < form.elements.length ; i++)
		form.elements[i].checked = false;
		}
		
		function dataTableSelectOneRadio(radio) {
		var id = radio.name.substring(radio.name.lastIndexOf(':'));
		var el = radio.form.elements;
		
		for (var i = 0; i < el.length; i++) {
		if (el[i].name.substring(el[i].name.lastIndexOf(':')) == id) {	
		el[i].checked = false;
		el[i].checked = false;
		}
		}
		radio.checked = true;
		}
		//]]>
	 </script>
	 
	 <f:view>	
		<h:panelGrid id="simplex3loadprojectoutergrid" columns="1" columnClasses="alignTopFixWidth">
		<h:messages id="simplexMessagesLoadProject" 
						showDetail="true"
						showSummary="true"
						errorStyle="color: red"/>	 
		
	 <h:outputText  id="lkdjf2" styleClass="h2" value="Project Archive Management"/>
	 <h:outputText  id="lkdjf3"
						 escape="false"
						 value="You don't have any archived projects. Use the 
								  form below to create a new one."
						 rendered="#{empty SimplexBean.myProjectNameList}"/>

	 <h:panelGrid id="lkdjf4"
					  columnClasses="alignTop,alignTop,alignTop"
					  columns="3" 
					  rendered="#{!(empty SimplexBean.myProjectNameList)}"		 
					  border="1">
		<h:form id="SimplexNewProjCheckBox">
			 <h:panelGrid  id="lkdjf5" columns="1" border="0">
				<h:outputText  id="lkdjf6" escape="false" value="<b>Select Projects</b><br/>" />
				<h:outputText id="lkdjf7" 
								  escape="false"
								  value="Please select from one of the previous projects." />
				<h:selectManyCheckbox id="projectlistforload" 
											 required="true"
											 value="#{SimplexBean.selectProjectsList}"
											 onchange="dataTableSelectOneRadio(this)"
											 onclick="dataTableSelectOneRadio(this)"
											 layout="pageDirection">
				  
				  <f:selectItems value="#{SimplexBean.myProjectNameList}" />
				</h:selectManyCheckbox>
				<h:commandButton id="lkdjf8" 							  
									  onclick="showLoading()"
									  value="Select"
									  action="#{SimplexBean.toggleSelectProject}" />
			 </h:panelGrid>
		  </h:form>

		  <h:form id="SimplexCopyProjCheckBox">					 
			 <h:panelGrid id="dlrfih1" columns="1" border="0">
				<h:outputText escape="false" value="<b>Copy Project</b><br/>" />
				<h:outputText escape="false"
								  value="Please select from one of the previous projects." />
				
				<h:selectManyCheckbox id="projectlistforcopy" 
											 required="true"
											 value="#{SimplexBean.copyProjectsList}"
											 onchange="dataTableSelectOneRadio(this)"
											 onclick="dataTableSelectOneRadio(this)"
											 layout="pageDirection">					
				  <f:selectItems value="#{SimplexBean.myProjectNameList}" />
				</h:selectManyCheckbox>
				
				<h:outputText value="New Project Name:"/>
				<h:inputText id="newProjectName2" 
								 required="true"
								 value="#{SimplexBean.projectCopyName}"/>
				
				<h:commandButton id="simplex3CopyCmdButton" 
									  onclick="showLoading()"
									  value="Copy"
									  action="#{SimplexBean.toggleCopyProject}" />
			 </h:panelGrid>
		</h:form>

		<h:form id="SimplexDeleteProjCheckBox">		
			 <h:panelGrid id="lkdjf9" columns="1" border="0">
				<h:outputText id="lkdjf10" escape="false" value="<b>Delete Projects</b><br/>" />
				<h:outputText  id="lkdjf11" escape="false"
									value="Please select from one of the previous projects." />
				<h:selectManyCheckbox id="projectfordelete" 
											 value="#{SimplexBean.deleteProjectsList}"
										  required="true"
										  layout="pageDirection">
				  <f:selectItems value="#{SimplexBean.myProjectNameList}" />
				</h:selectManyCheckbox>
				<h:commandButton id="lkdjf12" 
									  onclick="showLoading()"
									  value="Delete"
									  action="#{SimplexBean.toggleDeleteProject}" />
			 </h:panelGrid>
		  </h:form>

		</h:panelGrid>
		<f:verbatim><p/></f:verbatim>
		<h:form id="lkdjf13">
		  <f:verbatim>
			 <fieldset>
				<legend class="portlet-form-label">Create New Project</legend>
		  </f:verbatim>	 
		  
		  <h:panelGrid id="lkdjf14" columns="2" border="0">
			 <h:outputText id="lkdjf15" value="Provide a Project Name:" />
			 <h:panelGroup id="lkdjf16">
				<h:inputText id="projectName" value="#{SimplexBean.projectName}"
								 required="true" />
				<h:message for="projectName" showDetail="true" showSummary="true"
							  errorStyle="color: red" />
			 </h:panelGroup>
			 
			 <h:outputText id="lkdjf17" value="Problem starting temperature:" />
			 <h:panelGroup id="lkdjf18">
				<h:inputText id="startTemp" value="#{SimplexBean.currentProjectEntry.startTemp}"
								 required="true" />
				<h:message for="startTemp" showDetail="true" showSummary="true"
							  errorStyle="color: red" />
			 </h:panelGroup>
			 
			 <h:outputText id="lkdjf19" value="Specify the maximum number of iterations:" />
			 <h:panelGroup id="lkdjf111">
				<h:inputText id="maxIters" value="#{SimplexBean.currentProjectEntry.maxIters}"
								 required="true" />
				<h:message id="lkdjf112" for="maxIters" showDetail="true" showSummary="true"
							  errorStyle="color: red" />
			 </h:panelGroup>			
		  </h:panelGrid>
		  
		  <h:commandButton id="lkdjf113" 
								 onclick="showLoading()"
								 value="Create Project"
								 action="#{SimplexBean.NewProjectThenEditProject}" />
		  <f:verbatim></fieldset></f:verbatim>	  
		</h:form>	
	 </h:panelGrid>
		
		<h:form id="lkdjf114">
		  <h:commandLink  id="lkdjf115" action="Simplex2-back">
			 <h:outputText id="lkdjf116" value="#{SimplexBean.codeName} Main Menu" />
		  </h:commandLink>
		</h:form>
	 </f:view>
  </body>
</html>
