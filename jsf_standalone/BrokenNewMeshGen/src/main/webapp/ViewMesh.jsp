<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>
<title>MeshGenerator Mesh Plot Tool</title>
</head>
<body>

<f:view>
	<h:form>
		<b>Input Parameter</b>
		<h:panelGrid columns="1" border="1">
			<h:panelGroup>
			<h:graphicImage id="image1" value="#{MGBean.myMeshViewer.meshImageJspUrl}"/> 
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText escape="false" value="<b>Rotate</b>" />
				<h:panelGrid columns="3" border="1">
					<h:outputText escape="false" value="X" />
					<h:commandButton value="+10 Degrees" actionListener="#{MGBean.myMeshViewer.toggleUpRotateXFactor}" />
					<h:commandButton value="-10 Degrees" actionListener="#{MGBean.myMeshViewer.toggleDownRotateXFactor}" />
					<h:outputText escape="false" value="Y" />
					<h:commandButton value="+10 Degrees" actionListener="#{MGBean.myMeshViewer.toggleUpRotateYFactor}" />
					<h:commandButton value="-10 Degrees" actionListener="#{MGBean.myMeshViewer.toggleDownRotateYFactor}" />
					<h:outputText escape="false" value="Z" />
					<h:commandButton value="+10 Degrees" actionListener="#{MGBean.myMeshViewer.toggleUpRotateZFactor}" />
					<h:commandButton value="-10 Degrees" actionListener="#{MGBean.myMeshViewer.toggleDownRotateZFactor}" />
				</h:panelGrid>

			</h:panelGroup>
		</h:panelGrid>
		<h:commandButton value="Return" action="MG-ant-run" />

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
