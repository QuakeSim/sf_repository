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

<title>Disloc Load and Delete Project</title>
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
	<h:outputText styleClass="header2" value="Project Archive"/>
        <p/>
		  <h:outputText value="You don't have any archived projects."
							 rendered="#{empty DislocBean.myProjectNameList}"/>
		<h:panelGrid columns="2" 
						 rendered="#{!(empty DislocBean.myProjectNameList)}"		 
						 border="1">
			<h:panelGrid columns="1" border="0">
				<h:outputText escape="false" value="<b>Select Project</b><br><br>" />
				<h:outputText escape="false"
					value="Please select from one of the previous projects." />
	
				<h:selectManyCheckbox id="projectlistforload" 
											 value="#{DislocBean.selectProjectsArray}"
					onchange="dataTableSelectOneRadio(this)"
					onclick="dataTableSelectOneRadio(this)"
					layout="pageDirection">
					
					<f:selectItems value="#{DislocBean.myProjectNameList}" />
				</h:selectManyCheckbox>
				<h:commandButton value="Select"
					action="#{DislocBean.toggleSelectProject}" />
			</h:panelGrid>
			<h:panelGrid columns="1" 
							 border="0">
				<h:outputText escape="false" value="<b>Delete Projects</b><br><br>" />
				<h:outputText escape="false"
					value="Please select from one of the previous projects." />
				<h:selectManyCheckbox id="projectfordelete" value="#{DislocBean.deleteProjectsArray}"
					layout="pageDirection">
					<f:selectItems value="#{DislocBean.myProjectNameList}" />
				</h:selectManyCheckbox>
				<h:commandButton value="Delete"
					action="#{DislocBean.toggleDeleteProject}" />
			</h:panelGrid>
		</h:panelGrid>
	</h:form>
<p/>
	<h:form>
		<b>New Project Name</b>
		<h:panelGrid columns="2" border="1">
			<h:outputText value="Project Name:" />
			<h:panelGroup>
			<h:inputText id="projectName" value="#{DislocBean.projectName}"
				required="true" />
			<h:message for="projectName" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>
			
			
		</h:panelGrid>
		<h:commandButton value="Make Selection"
			action="#{DislocBean.NewProjectThenEditProject}" />

	</h:form>

	<h:form>
		<hr />
		<h:commandLink action="disloc-back">
			<h:outputText value="#{DislocBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>
</f:view>
</body>
</html>
