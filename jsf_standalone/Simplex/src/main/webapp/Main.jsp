<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>
<title>Simplex2</title>
</head>
<body>
<f:view>
	<h3>Simplex2</h3>
	<p>Enter Description.</p>

	<h:form id="form">
		<h:panelGrid id="grid" columns="2" border="1">

			<h:commandButton id="button1" value="New Project"
				action="#{SimplexBean.newProject}" />
			<h:outputFormat id="output1" escape="false"
				value="<b>New Project:</b> Create a new geometry out of new and existing faults and layers." />

			<h:commandButton id="button2" value="Load Project"
				action="#{SimplexBean.loadProject}" />
			<h:outputFormat id="output2" escape="false"
				value="<b>Load Project:</b> Load or delete a previously created project." />

			<h:commandButton id="button4" value="Archived Data"
				action="#{SimplexBean.archivedData}" />
			<h:outputFormat id="output4" escape="false"
				value="<b>Archived Data:</b> Load a previously created project mesh. Use this to further refine a previously created project mesh. Also allows you to go directly to code submission for GeoFEST." />

			<h:commandButton id="button5" value="Plot Data"
				action="#{SimplexBean.GMTDataPlots}" />
			<h:outputFormat id="output5" escape="false"
				value="<b>GMT Data Plots:</b> Create GMT plots of Simplex data." />


		</h:panelGrid>
	</h:form>
</f:view>


</body>
</html>
