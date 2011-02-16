<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>

<title>Archived Data</title>
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
			<h:outputText escape="false" value="<h3>Archived Data</h3><br>" />
			<h:outputText escape="false"
				value="You have the following archived data files. Click the link to download the file.<br>" />

			<h:dataTable border="1" value="#{MGBean.myarchivedMeshTableEntryList}"
				var="myentry3">
				<h:column>
					<f:facet name="header">
						<h:outputText escape="false" value="<b>Project Name</b>" />
					</f:facet>
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
						<h:outputText escape="false" value="<b>	Storage Host</b>" />
					</f:facet>
					<h:outputText value="#{myentry3.meshHost}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText escape="false" value="<b>	Mesh File</b>" />
					</f:facet>
					<h:panelGrid columns="4" border="1">

						<h:outputLink id="link1" value="#{facesContext.externalContext.requestContextPath}/meshdownloads/#{myentry3.projectName}.inp" target="_blank">
							<h:outputText value="#{myentry3.projectName}.inp" />
						</h:outputLink>
						<h:outputLink id="link2" value="#{facesContext.externalContext.requestContextPath}/meshdownloads/#{myentry3.projectName}.out" target="_blank">
							<h:outputText value="#{myentry3.projectName}.out" />
						</h:outputLink>
						<h:outputLink id="link3" value="#{facesContext.externalContext.requestContextPath}/meshdownloads/#{myentry3.projectName}.log" target="_blank">
							<h:outputText value="#{myentry3.projectName}.log" />
						</h:outputLink>
						<h:outputLink id="link4" value="#{facesContext.externalContext.requestContextPath}/meshdownloads/#{myentry3.projectName}.tar.gz" target="_blank">
							<h:outputText value="#{myentry3.projectName}.tar.gz" />
						</h:outputLink>

					</h:panelGrid>
				</h:column>


			</h:dataTable>
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
