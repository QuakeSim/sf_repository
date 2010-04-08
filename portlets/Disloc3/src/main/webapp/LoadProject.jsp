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
		font-weight: bold;
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
	<h:outputText id="lptv1" styleClass="header2" value="Project Archive"/>
        <p/>
		  <h:outputText id="lptv11" value="You don't have any archived projects."
							 rendered="#{empty DislocBean2.myProjectNameList}"/>
		<h:panelGrid id="lptv12" columnClasses="alignTop,alignTop,alignTop" columns="3" 
						 rendered="#{!(empty DislocBean2.myProjectNameList)}"		 
						 border="1">
	     <h:form>
		<h:panelGrid columns="1" border="0">
				<h:outputText id="lptv21" escape="false" value="<b>Select Project</b><br><br>" />
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

			<h:form>
			<h:panelGrid columns="1" border="0">
				<h:outputText escape="false" value="<b>Copy Project</b><br><br>" />
				<h:outputText escape="false"
					value="Please select from one of the previous projects." />
	
				<h:selectManyCheckbox id="projectlistforcopy" 
											 value="#{DislocBean2.copyProjectsArray}"
					onchange="dataTableSelectOneRadio(this)"
					onclick="dataTableSelectOneRadio(this)"

					layout="pageDirection">					
					<f:selectItems value="#{DislocBean2.myProjectNameList}" />
				</h:selectManyCheckbox>

				   <h:outputText value="New ProjectName:"/>
				   <h:inputText id="newProjectName" 
									 value="#{DislocBean2.projectName}"
									 required="true"/>

				<h:commandButton value="Copy"
					action="#{DislocBean2.toggleCopyProject}" />
			</h:panelGrid>
			</h:form>

			<h:form>
			<h:panelGrid columns="1" 
							 border="0">
				<h:outputText escape="false" value="<b>Delete Projects</b><br><br>" />
				<h:outputText escape="false"
					value="Please select from one of the previous projects." />
				<h:selectManyCheckbox id="projectfordelete" value="#{DislocBean2.deleteProjectsArray}"
					layout="pageDirection">
					<f:selectItems value="#{DislocBean2.myProjectNameList}" />
				</h:selectManyCheckbox>
				<h:commandButton value="Delete"
					action="#{DislocBean2.toggleDeleteProject}" />
			</h:panelGrid>
			</h:form>

		</h:panelGrid>

<p/>
	<h:form>
		<b>New Project Name</b>
		<h:panelGrid columns="2" border="1">
			<h:outputText id="lpj_projectname" value="Project Name:" />
			<h:panelGroup>
			<h:inputText id="projectName" value="#{DislocBean2.projectName}"
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
