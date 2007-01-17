<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>
<title>MeshGenerator Ant Run Page</title>
</head>
<body>
<h2>Refine Mesh</h2>
<p>Your initial mesh has been generated. You may now iteratively
refine it by pressing the "Refine" button.<br>
By default, the mesh generator will refine the mesh for all of the
faults in your mesh. You may optionally choose to refine around only a
subset of your system faults but selecting one or more checkboxes below.
<br>
<br>
Please choose one or more faults for refinement, then click "Refine
Mesh" below. To refine all faults, just click "Refine Mesh".</p>

<f:view>
	<h:form>
		<h:panelGrid columns="1" border="0">
			<h:panelGroup>
				<h:selectManyCheckbox id="FaultslistCheckbox" value="#{MGBean.faultarrayForMesh}" layout="pageDirection">
				<f:selectItems value="#{MGBean.myFaultsForProject}" />
				</h:selectManyCheckbox>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText escape="false" value="<br><b>Mesh Refine Limit: </b>" />
				<h:inputText id="MeshRefineLimit" value="#{MGBean.magic15}"
					required="true" />
				<h:message for="MeshRefineLimit" showDetail="true"
					showSummary="true" errorStyle="color: red" />
			</h:panelGroup>
			<h:panelGroup>
				<h:inputTextarea id="textArea" rows="20" cols="80"
					value="#{MGBean.refineOutMessage}" />
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText escape="false"
					value="Click <b>Refine Mesh</b> to launch the Mesh Refiner. The Mesh Refiner may take several minutes to complete.<br>" />
				<h:outputText escape="false"
					value="Click <b>View Messages</b> to view the Mesh Refiner's output messages.<br>" />

			</h:panelGroup>
			<h:panelGroup>
				<h:panelGrid columns="2" border="0">
					<h:commandButton value="Refine Mesh" actionListener="#{MGBean.toggleRefineMesh}" />
					<h:commandButton value="View Messages" actionListener="#{MGBean.toggleViewMeshMessages}" />
					<h:commandButton value="View Mesh" action="#{MGBean.SetAndViewMeshImage}" />
					<h:outputLink id="link1" value="painter.jsp">
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
