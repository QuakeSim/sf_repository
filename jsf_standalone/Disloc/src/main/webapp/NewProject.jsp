<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>
<title>Disloc Project Management</title>
</head>
<body>
<h2>Project Input</h2>
<p>Input project observation points. </p>

<f:view>
	<h:form>
		<b>Input Parameter</b>
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
