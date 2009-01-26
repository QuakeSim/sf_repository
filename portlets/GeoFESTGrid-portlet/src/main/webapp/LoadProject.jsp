<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>

<title>MeshGenerator Load and Delete Project</title>
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
      <h:outputText rendered="#{(empty MGBean.myProjectNameList)}"
						  value="You don't have any archived projects.  Please create a new one."/>
		<h:panelGrid columns="2" 
 						 rendered="#{!(empty MGBean.myProjectNameList)}"
						 border="1">
			<h:panelGrid columns="1" 
							 border="0">
				<h:outputText escape="false" value="<b>Select Project</b><br><br>" />
				<h:outputText escape="false"
					value="Please select from one of the previous projects." />
				<h:selectManyCheckbox id="projectlistforload" value="#{MGBean.selectProjectsList}"
					onchange="dataTableSelectOneRadio(this)"
					onclick="dataTableSelectOneRadio(this)"
					layout="pageDirection">
					
					<f:selectItems value="#{MGBean.myProjectNameList}" />
				</h:selectManyCheckbox>
				<h:commandButton value="Select"
					action="#{MGBean.toggleSelectProject}" />
			</h:panelGrid>

			<h:panelGrid columns="1" 
							 border="0">
				<h:outputText escape="false" value="<b>Delete Projects</b><br><br>" />
				<h:outputText escape="false"
					value="Please select from one of the previous projects." />
				<h:selectManyCheckbox id="projectfordelete" value="#{MGBean.deleteProjectsList}"
					layout="pageDirection">
					<f:selectItems value="#{MGBean.myProjectNameList}" />
				</h:selectManyCheckbox>
				<h:commandButton value="Delete"
					action="#{MGBean.toggleDeleteProject}" />
			</h:panelGrid>
		</h:panelGrid>
	</h:form>

<p/>
	<h:form>
		<b>New Project Name</b>
		<h:panelGrid columns="2" border="1">
			<h:outputText value="Project Name:" />
			<h:panelGroup>
			<h:inputText id="projectName" value="#{MGBean.projectName}"
				required="true" />
			<h:message for="projectName" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>
		</h:panelGrid>
		<h:commandButton value="Make Selection"
			action="#{MGBean.NewProjectThenEditProject}" />

	</h:form>


	<h:form>
		<hr />
		<h:commandLink action="MG-back">
			<h:outputText value="#{MGBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>
</f:view>
</body>
</html>
