<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>
<title>MeshGenerator</title>
</head>
<body>
<f:view>
	<h2>Geometry Creation and Mesh Generation</h2>
	<p>These pages will guide you through the steps needed to create a
	fault geometry and a finite element mesh.</p>
	
	<h:form id="form">
		<h:panelGrid id="grid" columns="2" border="1">

			<h:commandButton id="button1" value="New Project"
				action="#{MGBean.newProject}" />
			<h:outputFormat id="output1" escape="false"
				value="<b>New Project:</b> Create a new geometry out of new and existing faults and layers." />

			<h:commandButton id="button2" value="Load Project"
				action="#{MGBean.loadProjectList}" />
			<h:outputFormat id="output2" escape="false"
				value="<b>Load Project:</b> Load or delete a previously created project. Use this to add or remove faults and layers, and to change properties." />

			<h:commandButton id="button5" value="Fetch Mesh"
				action="#{MGBean.fetchMesh}" />
			<h:outputFormat id="output5" escape="false"
				value="<b>Retrieve Meshes:</b> Download previously generated mesh files and other output to your desktop. You may download individual files or get everything (including geometry specification files) in a single tar bundle." />
		</h:panelGrid>
	</h:form>
</f:view>


</body>
</html>
