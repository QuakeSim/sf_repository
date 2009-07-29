<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>
<title>Simplex2 Project Management</title>
</head>
<body>

<f:view>
	<h:form>
		<h:panelGrid columns="2" border="0">
			<h:outputText value="Provide a Project Name:" />
			<h:panelGroup>
			<h:inputText id="projectName" value="#{SimplexBean.projectName}"
				required="true" />
			<h:message for="projectName" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>
			
			<h:outputText value="Problem starting temperature:" />
			<h:panelGroup>
			<h:inputText id="startTemp" value="#{SimplexBean.currentProjectEntry.startTemp}"
				required="true" />
			<h:message for="startTemp" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>

			<h:outputText value="Specify the maximum number of iterations:" />
			<h:panelGroup>
			<h:inputText id="maxIters" value="#{SimplexBean.currentProjectEntry.maxIters}"
				required="true" />
			<h:message for="maxIters" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>

			<h:outputText value="Specify the latitude of the problem origin:" />
			<h:panelGroup>
			<h:inputText id="origin_lat" value="#{SimplexBean.currentProjectEntry.origin_lat}"
				required="true" />
			<h:message for="origin_lat" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>

			<h:outputText value="Specify the longitude of the problem origin:" />
			<h:panelGroup>
			<h:inputText id="origin_lon" value="#{SimplexBean.currentProjectEntry.origin_lon}"
				required="true" />
			<h:message for="origin_lon" showDetail="true" showSummary="true"
				errorStyle="color: red" />
			</h:panelGroup>
			
		</h:panelGrid>
		<h:commandButton value="Create Project"
			action="#{SimplexBean.NewProjectThenEditProject}" />

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
