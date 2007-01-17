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
			<h:outputText escape="false" value="<h3>Archived Meshes</h3><br>" />
			<h:outputText escape="false"
				value="You have the following archived meshes. Click the link to download the desired file to your desk top. To save directly to your desktop, click your mouse's right button over the link and select<br>" />
			<h:outputText escape="false"
				value="Click the <b>Tar</b> link if you want to download all the files in the project in a single bundle. This includes the mesh files, geometry and fault specification files, among other things. the appropriate option.<br>" />

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

						<h:outputLink id="link1" value="DownloadMeshes.jsp" target="_blank">
							<f:param name="ProjectSelect" value="#{myentry3.projectName}" />
							<f:param name="MeshChoice" value=".node" />
							<f:param name="baseWorkDir" value="#{MGBean.baseWorkDir}" />
							<f:param name="userName" value="#{MGBean.userName}" />
							<f:param name="antUrl" value="#{MGBean.antUrl}" />
							<f:param name="binPath" value="#{MGBean.binPath}" />
							<f:param name="fileServiceUrl" value="#{MGBean.fileServiceUrl}" />

							<h:outputText value="Node" />
						</h:outputLink>
						<h:outputLink id="link2" value="DownloadMeshes.jsp" target="_blank">
							<f:param name="ProjectSelect" value="#{myentry3.projectName}" />
							<f:param name="MeshChoice" value=".tetra" />
							<f:param name="baseWorkDir" value="#{MGBean.baseWorkDir}" />
							<f:param name="userName" value="#{MGBean.userName}" />
							<f:param name="antUrl" value="#{MGBean.antUrl}" />
							<f:param name="binPath" value="#{MGBean.binPath}" />
							<f:param name="fileServiceUrl" value="#{MGBean.fileServiceUrl}" />

							<h:outputText value="Tetra" />
						</h:outputLink>
						<h:outputLink id="link3" value="DownloadMeshes.jsp" target="_blank">
							<f:param name="ProjectSelect" value="#{myentry3.projectName}" />
							<f:param name="MeshChoice" value=".index" />
							<f:param name="baseWorkDir" value="#{MGBean.baseWorkDir}" />
							<f:param name="userName" value="#{MGBean.userName}" />
							<f:param name="antUrl" value="#{MGBean.antUrl}" />
							<f:param name="binPath" value="#{MGBean.binPath}" />
							<f:param name="fileServiceUrl" value="#{MGBean.fileServiceUrl}" />

							<h:outputText value="Index" />
						</h:outputLink>
						<h:outputLink id="link4" value="DownloadMeshes.jsp" target="_blank">
							<f:param name="ProjectSelect" value="#{myentry3.projectName}" />
							<f:param name="MeshChoice" value=".tar.gz" />
							<f:param name="baseWorkDir" value="#{MGBean.baseWorkDir}" />
							<f:param name="userName" value="#{MGBean.userName}" />
							<f:param name="antUrl" value="#{MGBean.antUrl}" />
							<f:param name="binPath" value="#{MGBean.binPath}" />
							<f:param name="fileServiceUrl" value="#{MGBean.fileServiceUrl}" />

							<h:outputText value="Tar.gz" />
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
