<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>
<title>Get computational portal</title>
</head>
<body>

<f:view>
	<h:form>
		<h:panelGrid columns="1" border="0">
			<h:panelGrid columns="1" border="0">
				<h:outputText escape="false"
					value="<b>Input and Output File Names</b>" />
				<h:panelGrid columns="2" border="0">
					<h:outputText value="Input File Name:" />
					<h:panelGroup>
						<h:inputText id="inputFileName"
							value="#{MGBean.currentGeotransParamsBean.inputFileName}"
							required="true" />
						<h:message for="inputFileName" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>
					<h:outputText value="Output File Name:" />
					<h:panelGroup>
						<h:inputText id="outputFileName"
							value="#{MGBean.currentGeotransParamsBean.outputFileName}"
							required="true" />
						<h:message for="outputFileName" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>
					<h:outputText value="Log File Name:" />
					<h:panelGroup>
						<h:inputText id="logFileName"
							value="#{MGBean.currentGeotransParamsBean.logFileName}"
							required="true" />
						<h:message for="logFileName" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>
				</h:panelGrid>
			</h:panelGrid>
			<h:panelGrid columns="2" border="1">
				<h:panelGrid columns="1" border="0">
					<h:outputText escape="false" value="<b>Input Parameters</b>" />
					<h:panelGrid columns="2" border="0">
						<h:outputText value="number_space_dimensions" />
						<h:panelGroup>
							<h:inputText id="number_space_dimensions"
								value="#{MGBean.currentGeotransParamsBean.number_space_dimensions}"
								required="true" />
							<h:message for="number_space_dimensions" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="number_degrees_freedom" />
						<h:panelGroup>
							<h:inputText id="number_degrees_freedom"
								value="#{MGBean.currentGeotransParamsBean.number_degrees_freedom}"
								required="true" />
							<h:message for="number_degrees_freedom" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="nrates" />
						<h:panelGroup>
							<h:inputText id="nrates"
								value="#{MGBean.currentGeotransParamsBean.nrates}"
								required="true" />
							<h:message for="nrates" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="shape_flag" />
						<h:panelGroup>
							<h:inputText id="shape_flag"
								value="#{MGBean.currentGeotransParamsBean.shape_flag}"
								required="true" />
							<h:message for="shape_flag" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="solver_flag" />
						<h:panelGroup>
							<h:inputText id="solver_flag"
								value="#{MGBean.currentGeotransParamsBean.solver_flag}"
								required="true" />
							<h:message for="solver_flag" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="number_time_groups" />
						<h:panelGroup>
							<h:inputText id="number_time_groups"
								value="#{MGBean.currentGeotransParamsBean.number_time_groups}"
								required="true" />
							<h:message for="number_time_groups" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="reform_steps" />
						<h:panelGroup>
							<h:inputText id="reform_steps"
								value="#{MGBean.currentGeotransParamsBean.reform_steps}"
								required="true" />
							<h:message for="reform_steps" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="backup_steps" />
						<h:panelGroup>
							<h:inputText id="backup_steps"
								value="#{MGBean.currentGeotransParamsBean.backup_steps}"
								required="true" />
							<h:message for="backup_steps" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="fault_interval" />
						<h:panelGroup>
							<h:inputText id="fault_interval"
								value="#{MGBean.currentGeotransParamsBean.fault_interval}"
								required="true" />
							<h:message for="fault_interval" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="end_time" />
						<h:panelGroup>
							<h:inputText id="end_time"
								value="#{MGBean.currentGeotransParamsBean.end_time}"
								required="true" />
							<h:message for="end_time" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="alpha" />
						<h:panelGroup>
							<h:inputText id="alpha"
								value="#{MGBean.currentGeotransParamsBean.alpha}"
								required="true" />
							<h:message for="alpha" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="time_step" />
						<h:panelGroup>
							<h:inputText id="time_step"
								value="#{MGBean.currentGeotransParamsBean.time_step}"
								required="true" />
							<h:message for="time_step" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

					</h:panelGrid>
				</h:panelGrid>
				<h:panelGrid columns="1" border="0">
					<h:outputText escape="false" value="<b>Boundary Conditions</b>" />
					<h:panelGrid columns="4" border="0">

						<h:outputText value="top_bc" />
						<h:selectOneMenu id="top_bc"
							value="#{MGBean.currentGeotransParamsBean.top_bc}">
							<f:selectItem id="top_bcitem2" itemLabel="Free Node"
								itemValue="free node" />
							<f:selectItem id="top_bcitem1" itemLabel="Locked Node"
								itemValue="locked node" />
						</h:selectOneMenu>
						<h:outputText value="BC Values:" />
						<h:panelGroup>
							<h:inputText id="top_bc_value"
								value="#{MGBean.currentGeotransParamsBean.top_bc_value}"
								required="true" />
							<h:message for="top_bc_value" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="east_bc" />
						<h:selectOneMenu id="east_bc"
							value="#{MGBean.currentGeotransParamsBean.east_bc}">
							<f:selectItem id="east_bcitem1" itemLabel="Locked Node"
								itemValue="locked node" />
							<f:selectItem id="east_bcitem2" itemLabel="Free Node"
								itemValue="free node" />
						</h:selectOneMenu>
						<h:outputText value="BC Values:" />
						<h:panelGroup>
							<h:inputText id="east_bc_value"
								value="#{MGBean.currentGeotransParamsBean.east_bc_value}"
								required="true" />
							<h:message for="east_bc_value" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="west_bc" />
						<h:selectOneMenu id="west_bc"
							value="#{MGBean.currentGeotransParamsBean.west_bc}">
							<f:selectItem id="west_bcitem1" itemLabel="Locked Node"
								itemValue="locked node" />
							<f:selectItem id="west_bcitem2" itemLabel="Free Node"
								itemValue="free node" />
						</h:selectOneMenu>
						<h:outputText value="BC Values:" />
						<h:panelGroup>
							<h:inputText id="west_bc_value"
								value="#{MGBean.currentGeotransParamsBean.west_bc_value}"
								required="true" />
							<h:message for="west_bc_value" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="north_bc" />
						<h:selectOneMenu id="north_bc"
							value="#{MGBean.currentGeotransParamsBean.north_bc}">
							<f:selectItem id="north_bcitem1" itemLabel="Locked Node"
								itemValue="locked node" />
							<f:selectItem id="north_bcitem2" itemLabel="Free Node"
								itemValue="free node" />
						</h:selectOneMenu>
						<h:outputText value="BC Values:" />
						<h:panelGroup>
							<h:inputText id="north_bc_value"
								value="#{MGBean.currentGeotransParamsBean.north_bc_value}"
								required="true" />
							<h:message for="north_bc_value" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="south_bc" />
						<h:selectOneMenu id="south_bc"
							value="#{MGBean.currentGeotransParamsBean.south_bc}">
							<f:selectItem id="south_bcitem1" itemLabel="Locked Node"
								itemValue="locked node" />
							<f:selectItem id="south_bcitem2" itemLabel="Free Node"
								itemValue="free node" />
						</h:selectOneMenu>
						<h:outputText value="BC Values:" />
						<h:panelGroup>
							<h:inputText id="south_bc_value"
								value="#{MGBean.currentGeotransParamsBean.south_bc_value}"
								required="true" />
							<h:message for="south_bc_value" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="bottom_bc" />
						<h:selectOneMenu id="bottom_bc"
							value="#{MGBean.currentGeotransParamsBean.bottom_bc}">
							<f:selectItem id="bottom_bcitem1" itemLabel="Locked Node"
								itemValue="locked node" />
							<f:selectItem id="bottom_bcitem2" itemLabel="Free Node"
								itemValue="free node" />
						</h:selectOneMenu>
						<h:outputText value="BC Values:" />
						<h:panelGroup>
							<h:inputText id="bottom_bc_value"
								value="#{MGBean.currentGeotransParamsBean.bottom_bc_value}"
								required="true" />
							<h:message for="bottom_bc_value" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

					</h:panelGrid>
					<h:outputText escape="false"
						value="<b>Output Parameters and Formatting</b>" />
					<h:panelGrid columns="2" border="0">
						<h:outputText value="Reporting Nodes:" />
						<h:selectOneMenu id="ReportingNodes"
							value="#{MGBean.currentGeotransParamsBean.reportingNodes}">
							<f:selectItem id="reportingNodesitem1" itemLabel="All"
								itemValue="all" />
							<f:selectItem id="reportingNodesitem2" itemLabel="None"
								itemValue="None" />
						</h:selectOneMenu>

						<h:outputText value="Reporting Elements:" />
						<h:selectOneMenu id="ReportingElements"
							value="#{MGBean.currentGeotransParamsBean.reportingElements}">
							<f:selectItem id="reportingElementsitem1" itemLabel="All"
								itemValue="all" />
							<f:selectItem id="reportingElementsitem2" itemLabel="None"
								itemValue="None" />
						</h:selectOneMenu>

						<h:outputText value="Print Times Type:" />
						<h:selectOneMenu id="PrintTimesType"
							value="#{MGBean.currentGeotransParamsBean.printTimesType}">
							<f:selectItem id="printTimesTypeitem1" itemLabel="Steps"
								itemValue="steps" />
						</h:selectOneMenu>

						<h:outputText value="Number of Print Times:" />
						<h:panelGroup>
							<h:inputText id="numberofPrintTimes"
								value="#{MGBean.currentGeotransParamsBean.numberofPrintTimes}"
								required="true" />
							<h:message for="numberofPrintTimes" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="Print Times Interval:" />
						<h:panelGroup>
							<h:inputText id="printTimesInterval"
								value="#{MGBean.currentGeotransParamsBean.printTimesInterval}"
								required="true" />
							<h:message for="printTimesInterval" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="Restart File:" />
						<h:selectOneMenu id="RestartFile"
							value="#{MGBean.currentGeotransParamsBean.restartFile}">
							<f:selectItem id="restartFileitem1" itemLabel="No Restart"
								itemValue="NO_RESTART" />
						</h:selectOneMenu>

						<h:outputText value="Checkpoint File:" />
						<h:selectOneMenu id="CheckpointFile"
							value="#{MGBean.currentGeotransParamsBean.checkpointFile}">
							<f:selectItem id="checkpointFileitem1" itemLabel="No Save"
								itemValue="NO_SAVE" />
						</h:selectOneMenu>


					</h:panelGrid>

				</h:panelGrid>


			</h:panelGrid>
		</h:panelGrid>
		<h:panelGrid columns="1">
		<h:outputText escape="false"
			value="Select a host and click the <b>Launch GeoFEST</b> button if you want to run GeoFEST with the above parameter settings. ">
		</h:outputText>
		<h:panelGroup>
           <h:outputText  id="peedfg33ere" value="GeoFEST Execution Host"/>
			  <h:selectOneListbox title="Mesh Refinement Host"
			  		      size="1"
			  		      id="refhostde3094" 
					      required="true" value="#{MGBean.gridGeoFESTHost}">
			     <f:selectItems id="ler3eihrc93" value="#{MGBean.gridHostList}"/>
		 	  </h:selectOneListbox>
		
			<h:commandButton value="Launch GeoFEST"
				action="#{MGBean.runGeoFESTJSF}" />
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
