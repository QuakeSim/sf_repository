<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>

<title>Select Project Files</title>
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
		<h:panelGrid columns="1" border="0">
			<h:outputText escape="false" value="<h3>Archived Data</h3><br>" />
			<h:outputText escape="false"
				value="You have the following archived data files. Select the radio button to create GMT plots. Download the input and output files for more information on the data.<br>" />

			<h:dataTable border="1"
				value="#{SimplexBean.myarchivedFileEntryList}" var="myentry3">
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
					<h:panelGrid columns="3" border="1">

						<h:outputLink id="link1"
							value="#{facesContext.externalContext.requestContextPath}/DownloadDataFile.jsp"
							target="_blank">
							<f:param name="ProjectSelect" value="#{myentry3.projectName}" />
							<f:param name="MeshChoice" value=".input" />
							<f:param name="codeName" value="#{SimplexBean.codeName}" />
							<f:param name="baseWorkDir" value="#{SimplexBean.baseWorkDir}" />
							<f:param name="userName" value="#{SimplexBean.userName}" />
							<f:param name="fileServiceUrl"
								value="#{SimplexBean.fileServiceUrl}" />

							<h:outputText value="input" />
						</h:outputLink>
						<h:outputLink id="link2"
							value="#{facesContext.externalContext.requestContextPath}/DownloadDataFile.jsp"
							target="_blank">
							<f:param name="ProjectSelect" value="#{myentry3.projectName}" />
							<f:param name="MeshChoice" value=".stdout" />
							<f:param name="codeName" value="#{SimplexBean.codeName}" />
							<f:param name="baseWorkDir" value="#{SimplexBean.baseWorkDir}" />
							<f:param name="userName" value="#{SimplexBean.userName}" />
							<f:param name="fileServiceUrl"
								value="#{SimplexBean.fileServiceUrl}" />

							<h:outputText value="stdout" />
						</h:outputLink>
						<h:outputLink id="link3"
							value="#{facesContext.externalContext.requestContextPath}/DownloadDataFile.jsp"
							target="_blank">
							<f:param name="ProjectSelect" value="#{myentry3.projectName}" />
							<f:param name="MeshChoice" value=".output" />
							<f:param name="codeName" value="#{SimplexBean.codeName}" />
							<f:param name="baseWorkDir" value="#{SimplexBean.baseWorkDir}" />
							<f:param name="userName" value="#{SimplexBean.userName}" />
							<f:param name="fileServiceUrl"
								value="#{SimplexBean.fileServiceUrl}" />

							<h:outputText value="output" />
						</h:outputLink>
					</h:panelGrid>
				</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText escape="false" value="<b>Select to Plot</b>" />
					</f:facet>
					<h:selectBooleanCheckbox value="#{myentry3.view}"
						onchange="selectOne(this.form,this)"
						onclick="selectOne(this.form,this)" />

				</h:column>


			</h:dataTable>
		</h:panelGrid>
		<h:commandButton id="gmt4plot" value="GMT Plot"
			action="#{SimplexBean.toggleGMTPlot}" />
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
