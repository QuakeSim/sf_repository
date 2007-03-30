<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>
<title>GeoFEST2</title>
</head>
<body>
<f:view>
	<h2>GeoFEST2</h2>
	<p> These pages will guide you through the steps needed to run GeoFEST2 to simulate faults.</p>

	<h:form id="form">
		<h:panelGrid id="grid" columns="2" border="1">

			<h:commandButton id="button1" value="Run GeoFEST"
				action="#{MGBean.runGeoFEST}" />
			<h:outputFormat id="output1" escape="false"
				value="<b>Set Up and Run GeoFEST:</b> Set up fault and layer geometries, generate finite element meshes, and run GeoFEST." />

			<h:commandButton id="button2" value="Archived Data"
				action="#{MGBean.gfarchivedData}" />
			<h:outputFormat id="output2" escape="false"
				value="<b>Archived Data:</b> Download GeoFEST output data from previous runs." />

			<h:commandButton id="button4" value="Graph Output"
				action="#{MGBean.gfGraphOutput}" />
			<h:outputFormat id="output4" escape="false"
				value="<b>Plot GeoFEST Output:</b>View contour plots of results." />


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
