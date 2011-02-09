<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
  <style>
  .alignTop {
    vertical-align:top;
  }
  .header2 {
    font-family: Arial, sans-serif;
    font-size: 18pt;
    font-weight: bold;
  }
  </style>

 <head>
  <title>Simplex2 Load and Delete Project</title>
 </head>
  
  <body>

  

   <script language="JavaScript">
//<![CDATA[ 
    function selectOne(form, button)
    {
      turnOffRadioForForm(form);
      button.checked = true;
    }

    function turnOffRadioForForm(form)
    {
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
	<h:form>
	<h:outputText  id="lkdjf2" styleClass="header2" value="Project Archive"/>
	<p/>
		<h:outputText  id="lkdjf3" value="You don't have any archived projects. Use the form below to create a new one."
							rendered="#{empty SimplexBean.myProjectNameList}"/>

	        <h:panelGrid id="lkdjf4"
			  			columnClasses="alignTop,alignTop,alignTop"
						columns="3" 
						rendered="#{!(empty SimplexBean.myProjectNameList)}"		 
						border="1">
			<h:panelGrid  id="lkdjf5" columns="1" border="0">
				<b><h:outputText  id="lkdjf6" escape="false" value="Select Projects" /></b><br>
				<h:outputText id="lkdjf7" 
								  escape="false"
								value="Please select from one of the previous projects." />
				<h:selectManyCheckbox id="projectlistforload" 
											value="#{SimplexBean.selectProjectsList}"
											onchange="dataTableSelectOneRadio(this)"
											onclick="dataTableSelectOneRadio(this)"
											layout="pageDirection">
					
					<f:selectItems value="#{SimplexBean.myProjectNameList}" />
				</h:selectManyCheckbox>
				<h:commandButton id="lkdjf8" value="Select"
									  action="#{SimplexBean.toggleSelectProject}" />
			</h:panelGrid>
			
			<h:panelGrid id="dlrfih1" columns="1" border="0">
				<b><h:outputText escape="false" value="Copy Project" /></b><br>
				<h:outputText escape="false"
					value="Please select from one of the previous projects." />
	
				<h:selectManyCheckbox id="projectlistforcopy" 
											 value="#{SimplexBean.copyProjectsList}"
											 onchange="dataTableSelectOneRadio(this)"
											 onclick="dataTableSelectOneRadio(this)"
											 layout="pageDirection">					
					<f:selectItems value="#{SimplexBean.myProjectNameList}" />
				</h:selectManyCheckbox>

				   <h:outputText value="New Project Name:"/>
				   <h:inputText id="newProjectName" 
									 value="#{SimplexBean.projectName}"/>

				<h:commandButton value="Copy"
					action="#{SimplexBean.toggleCopyProject}" />
			</h:panelGrid>
			
			<h:panelGrid id="lkdjf9" columns="1" border="0">
				<b><h:outputText id="lkdjf10" escape="false" value="Select Projects" /></b><br>
				<h:outputText  id="lkdjf11" escape="false"
					value="Please select from one of the previous projects." />
				<h:selectManyCheckbox id="projectfordelete" value="#{SimplexBean.deleteProjectsList}"
					layout="pageDirection">
					<f:selectItems value="#{SimplexBean.myProjectNameList}" />
				</h:selectManyCheckbox>
				<h:commandButton id="lkdjf12" value="Delete"
					action="#{SimplexBean.toggleDeleteProject}" />
			</h:panelGrid>

		</h:panelGrid>

	</h:form>

	<h:form id="lkdjf13">
		<b>New Project Name</b>
		<h:panelGrid id="lkdjf14" columns="2" border="0">
			<h:outputText id="lkdjf15" value="Provide a Project Name:" />
			<h:panelGroup id="lkdjf16">
			<h:inputText id="projectName" value="#{SimplexBean.projectName}"
				required="false" />
			<h:message for="projectName" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>
			
			<h:outputText id="lkdjf17" value="Problem starting temperature:" />
			<h:panelGroup id="lkdjf18">
			<h:inputText id="startTemp" value="#{SimplexBean.currentProjectEntry.startTemp}"
				required="false" />
			<h:message for="startTemp" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>

			<h:outputText id="lkdjf19" value="Specify the maximum number of iterations:" />
			<h:panelGroup id="lkdjf111">
			<h:inputText id="maxIters" value="#{SimplexBean.currentProjectEntry.maxIters}"
				required="false" />
			<h:message  id="lkdjf112" for="maxIters" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>			
		</h:panelGrid>
		
		<h:commandButton id="lkdjf113" value="Create Project"
							action="#{SimplexBean.NewProjectThenEditProject}" />
	</h:form>	

	<h:form id="lkdjf114">
		<hr />
		<h:commandLink  id="lkdjf115" action="Simplex2-back">
			<h:outputText id="lkdjf116" value="#{SimplexBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>
</f:view>
</body>
</html>
