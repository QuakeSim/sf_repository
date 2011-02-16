<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>

<title>Select Project Meshes</title>
</head>
<body>
<script text="text/javascript">

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
		<h:panelGrid columns="1" border="0">
			<h:outputText escape="false"
				value="<h3>Select Project Meshes</h3><br>" />
			<h:outputText escape="false"
				value="Please select from one of the previous projects. <b>Mesh Host</b> is the computer where the mesh is located." />

			<h:dataTable border="1" value="#{MGBean.myLoadMeshTableEntryList}"
				var="myentry3">
				<h:column>
					<f:facet name="header">
						<h:outputText escape="false" value="<b>Project Name</b>" />
					</f:facet>
					<h:selectBooleanCheckbox value="#{myentry3.view}"
						onchange="dataTableSelectOneRadio(this)"
						onclick="dataTableSelectOneRadio(this)" />
					<h:outputText value="#{myentry3.projectName}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText escape="false" value="<b>Creation Date</b>" />
					</f:facet>
					<h:outputText value="#{myentry3.creationDate}" />

				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText escape="false" value="<b>Mesh Host</b>" />
					</f:facet>
					<h:outputText value="#{myentry3.meshHost}" />
				</h:column>

			</h:dataTable>
			<h:commandButton id="button3" value="Select" action="#{MGBean.loadThenAntRun}" />
		</h:panelGrid>

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
