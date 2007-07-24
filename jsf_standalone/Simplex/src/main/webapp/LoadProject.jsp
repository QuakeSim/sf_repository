<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
		font: bold;
	}
</style>

<head>

<title>Simplex2 Load and Delete Project</title>
</head>
<body>
<script language="JavaScript">

function selectOne(form , button)
{
  turnOffRadioForForm(form);
  button.checked = true;
}

function turnOffRadioForForm(form)
{
  for(var i=0; i<form.elements.length; i++)
  {
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

</script>

<f:view>
	<h:form>
	<h:outputText styleClass="header2" value="Project Manager"/>
        <p/>
		  <h:outputText value="You don't have any archived projects. Fill out the forms below to create one."
							 rendered="#{empty SimplexBean.myProjectNameList}"/>

		<h:panelGrid columns="2" 
						 rendered="#{!(empty SimplexBean.myProjectNameList)}"		 
						 border="1">
			<h:panelGrid columns="1" border="0">
				<h:outputText escape="false" value="<b>Select Projects</b><br><br>" />
				<h:outputText escape="false"
					value="Please select from one of the previous projects." />
				<h:selectManyCheckbox id="projectlistforload" value="#{SimplexBean.selectProjectsList}"
					onchange="dataTableSelectOneRadio(this)"
					onclick="dataTableSelectOneRadio(this)"
					layout="pageDirection">
					
					<f:selectItems value="#{SimplexBean.myProjectNameList}" />
				</h:selectManyCheckbox>
				<h:commandButton value="Select"
					action="#{SimplexBean.toggleSelectProject}" />
			</h:panelGrid>
			<h:panelGrid columns="1" border="0">
				<h:outputText escape="false" value="<b>Select Projects</b><br><br>" />
				<h:outputText escape="false"
					value="Please select from one of the previous projects." />
				<h:selectManyCheckbox id="projectfordelete" value="#{SimplexBean.deleteProjectsList}"
					layout="pageDirection">
					<f:selectItems value="#{SimplexBean.myProjectNameList}" />
				</h:selectManyCheckbox>
				<h:commandButton value="Delete"
					action="#{SimplexBean.toggleDeleteProject}" />
			</h:panelGrid>
		</h:panelGrid>

	</h:form>

	<h:form>
		<b>New Project Name</b>
		<h:panelGrid columns="2" border="0">
			<h:outputText value="Provide a Project Name:" />
			<h:panelGroup>
			<h:inputText id="projectName" value="#{SimplexBean.projectName}"
				required="true" />
			<h:message for="projectName" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>
			
			<h:outputText value="Problem starting temperature:" />
			<h:panelGroup>
			<h:inputText id="startTemp" value="#{SimplexBean.currentProjectEntry.startTemp}"
				required="true" />
			<h:message for="startTemp" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>

			<h:outputText value="Specify the maximum number of iterations:" />
			<h:panelGroup>
			<h:inputText id="maxIters" value="#{SimplexBean.currentProjectEntry.maxIters}"
				required="true" />
			<h:message for="maxIters" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>

			<h:outputText value="Specify the latitude of the problem origin:" />
			<h:panelGroup>
			<h:inputText id="origin_lat" value="#{SimplexBean.currentProjectEntry.origin_lat}"
				required="true" />
			<h:message for="origin_lat" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>

			<h:outputText value="Specify the longitude of the problem origin:" />
			<h:panelGroup>
			<h:inputText id="origin_lon" value="#{SimplexBean.currentProjectEntry.origin_lon}"
				required="true" />
			<h:message for="origin_lon" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>
			
		</h:panelGrid>
		<h:commandButton value="Create Project"
			action="#{SimplexBean.NewProjectThenEditProject}" />

	</h:form>

	<h:form>
		<hr />
		<h:commandLink action="Simplex2-back">
			<h:outputText value="#{SimplexBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>
</f:view>
</body>
</html>
