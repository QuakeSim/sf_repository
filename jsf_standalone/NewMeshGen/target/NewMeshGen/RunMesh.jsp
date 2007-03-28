<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>
<title>MeshGenerator Results</title>
</head>
<body>
<h2>Mesh Refinement</h2>
Your mesh has been created.  To view output, click the links below.  To see a 
visualization of the mesh, click the button.  To set up and run GeoFEST for this
mesh, click the button.
<br>

<f:view>
	<h:form>
		<h:panelGrid columns="1" border="0">
			<h:panelGroup>
  				<h:panelGrid id="MeshOutputPanel" columns="2" border="0">
					    <h:outputText value="Project Name"/>
				       <h:outputText value="#{MGBean.projectMeshRunBean.projectName}"/>

					    <h:outputText value="Job UID Stamp"/>
				       <h:outputText value="#{MGBean.projectMeshRunBean.jobUIDStamp}"/>
	
				       <h:outputLink value="#{MGBean.projectMeshRunBean.autoref}" target="_blank">
						    <h:outputText value="Autoref"/>
						 </h:outputLink>
	
				       <h:outputLink value="#{MGBean.projectMeshRunBean.autorefError}" target="_blank">
						    <h:outputText value="Autoref Error"/>
						 </h:outputLink>

				       <h:outputLink value="#{MGBean.projectMeshRunBean.bcUrl}" target="_blank">
						    <h:outputText value="BC File"/>
						 </h:outputLink>

				       <h:outputLink value="#{MGBean.projectMeshRunBean.indexUrl}" target="_blank">
						    <h:outputText value="Index File"/>
						 </h:outputLink>

				       <h:outputLink value="#{MGBean.projectMeshRunBean.junkBox}" target="_blank">
						    <h:outputText value="junkBox"/>
						 </h:outputLink>

				       <h:outputLink value="#{MGBean.projectMeshRunBean.leeRefinerLog}" target="_blank">
						    <h:outputText value="Lee Refiner Log"/>
						 </h:outputLink>

				       <h:outputLink value="#{MGBean.projectMeshRunBean.nodeUrl}" target="_blank">
						    <h:outputText value="Node File"/>
						 </h:outputLink>

				       <h:outputLink value="#{MGBean.projectMeshRunBean.tagbigfltLog}" target="_blank">
						    <h:outputText value="Tagging Log"/>
						 </h:outputLink>

				       <h:outputLink value="#{MGBean.projectMeshRunBean.tetraUrl}" target="_blank">
						    <h:outputText value="Tetra Url"/>
						 </h:outputLink>

				       <h:outputLink value="#{MGBean.projectMeshRunBean.tstout}" target="_blank">
						    <h:outputText value="Tstout URL"/>
						 </h:outputLink>

				       <h:outputLink value="#{MGBean.projectMeshRunBean.refinerLog}" target="_blank">
						    <h:outputText value="refinerLog"/>
						 </h:outputLink>

				       <h:outputLink value="#{MGBean.projectMeshRunBean.refinerLog}" target="_blank">
						    <h:outputText value="refinerLog"/>
						 </h:outputLink>
						 
				</h:panelGrid>

 				<h:panelGrid columns="2" border="0">
					<h:commandButton value="View Mesh" action="#{MGBean.SetAndViewMeshImage}" />
					<h:outputLink id="link1" value="#{facesContext.externalContext.requestContextPath}/painter.jsp">
						<f:param name="layers" value="#{MGBean.myLayersParamForJnlp}" />
						<f:param name="faults" value="#{MGBean.myFaultsParamForJnlp}" />
						<f:param name="plotMesh" value="true" />
						<f:param name="workDir" value="#{MGBean.workDirForJnlp}" />
						<f:param name="projectName" value="#{MGBean.projectNameForJnlp}" />
						<f:param name="fsURL" value="#{MGBean.fsURLForJnlp}" />
						
						<h:outputText value="Web Start Plotter" />
					</h:outputLink>
					<h:commandButton value="Save Mesh" action="#{MGBean.SaveMeshMetaData}" />
					<h:commandButton value="Return" action="MG-edit-project" />

				</h:panelGrid>
			</h:panelGroup>
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
