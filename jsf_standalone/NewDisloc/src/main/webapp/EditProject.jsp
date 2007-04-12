<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<style>
	.alignTop {
		vertical-align:top;
	}
	.header2 {
		font-family: Arial, sans-serif;
		font-size: 18pt;
		font: bold;
	}
</style>


<html>
<head>
<link rel="stylesheet" type="text/css"
	href='<%= request.getContextPath() + "/stylesheet.css" %>'>

<title>Edit Project</title>
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
	<h:outputText styleClass="header2" value="Project Input"/>

	<p>Create your geometry out of observation points and faults.</p>

	<h:panelGrid id="EditProject" 
		columnClasses="alignTop,alignTop"
		columns="2" border="1">
		<h:form id="selectproj">
			<h:panelGroup>

				<h:outputFormat escape="false"
					value="<b>Project Name: #{DislocBean.projectName} </b>" />

				<h:selectOneRadio layout="pageDirection" id="subscriptions"
					value="#{DislocBean.projectSelectionCode}">
					<f:selectItem id="item1"
						itemLabel="Observations: Click to specify grid of observation points."
						itemValue="CreateObservationGrid" />
					<f:selectItem id="item2"
						itemLabel="Create New Fault: Click to specify geometry for a fault segment."
						itemValue="CreateNewFault" />
					<f:selectItem id="item4"
						itemLabel="Add Fault from DB: Click to select a fault segment from the database."
						itemValue="AddFaultSelection" />
				</h:selectOneRadio>
				<h:commandButton id="button1" value="Make Selection"
					actionListener="#{DislocBean.toggleProjectSelection}">
				</h:commandButton>


			</h:panelGroup>
		</h:form>

		<h:panelGroup>
			<h:form id="obsvform" rendered="#{DislocBean.renderDislocGridParamsForm}">

				<h:panelGrid id="ObsvTable" columns="2" footerClass="subtitle"
					headerClass="subtitlebig" styleClass="medium"
					columnClasses="subtitle,medium">

					<f:facet name="header">
						<h:outputFormat id="output2" escape="false"
							value="<b>Define Grid of Observation Points </b>" />
					</f:facet>

					<h:outputText value="Grid Minimum X Value:" />
					<h:panelGroup>
						<h:inputText id="minx"
							value="#{DislocBean.dislocParams.gridMinXValue}" required="true" />
					</h:panelGroup>

					<h:outputText value="X Spacing:" />
					<h:panelGroup>
						<h:inputText id="xspacing"
							value="#{DislocBean.dislocParams.gridXSpacing}" required="true" />
					</h:panelGroup>

					<h:outputText value="X Iterations" />
					<h:panelGroup>
						<h:inputText id="xiterations"
							value="#{DislocBean.dislocParams.gridXIterations}" required="true" />
					</h:panelGroup>

					<h:outputText value="Grid Minimum Y Value:" />
					<h:panelGroup>
						<h:inputText id="miny"
							value="#{DislocBean.dislocParams.gridMinYValue}" required="true" />
					</h:panelGroup>

					<h:outputText value="Y Spacing:" />
					<h:panelGroup>
						<h:inputText id="yspacing"
							value="#{DislocBean.dislocParams.gridYSpacing}" required="true" />
					</h:panelGroup>

					<h:outputText value="Y Iterations" />
					<h:panelGroup>
						<h:inputText id="yiterations"
							value="#{DislocBean.dislocParams.gridYIterations}" required="true" />
					</h:panelGroup>

					<h:commandButton id="addobservation" value="select"
						actionListener="#{DislocBean.toggleAddObservationsForProject}" />


				</h:panelGrid>
			</h:form>

			<h:form id="Faultform" rendered="#{DislocBean.renderCreateNewFaultForm}">
				<h:panelGrid id="FaultTable" columns="2" footerClass="subtitle"
					headerClass="subtitlebig" styleClass="medium"
					columnClasses="subtitle,medium">

					<f:facet name="header">
						<h:outputFormat id="output3" escape="false"
							value="<b>Input Fault Geometry </b>" />
					</f:facet>

					<h:outputText value="Fault Name:" />
					<h:panelGroup>
						<h:inputText id="FaultName"
							value="#{DislocBean.currentFault.faultName}" required="true" />
					</h:panelGroup>

					<h:outputText value="Location X:" />
					<h:panelGroup>
						<h:inputText id="FaultLocationX"
							value="#{DislocBean.currentFault.faultLocationX}" required="true" />
					</h:panelGroup>

					<h:outputText value="Location Y:" />
					<h:panelGroup>
						<h:inputText id="FaultLocationY"
							value="#{DislocBean.currentFault.faultLocationY}" required="true" />
					</h:panelGroup>

					<h:outputText value="Length:" />
					<h:panelGroup>
						<h:inputText id="FaultLength"
							value="#{DislocBean.currentFault.faultLength}" required="true" />
					</h:panelGroup>

					<h:outputText value="Width:" />
					<h:panelGroup>
						<h:inputText id="FaultWidth"
							value="#{DislocBean.currentFault.faultWidth}" required="true" />
					</h:panelGroup>

					<h:outputText value="Depth:" />
					<h:panelGroup>
						<h:inputText id="FaultDepth"
							value="#{DislocBean.currentFault.faultDepth}" required="true" />
					</h:panelGroup>

					<h:outputText value="Dip Angle:" />
					<h:panelGroup>
						<h:inputText id="FaultDipAngle"
							value="#{DislocBean.currentFault.faultDipAngle}" required="true" />
					</h:panelGroup>

					<h:outputText value="Dip Slip:" />
					<h:panelGroup>
						<h:inputText id="FaultSlip"
							value="#{DislocBean.currentFault.faultDipSlip}" required="true" />
					</h:panelGroup>

					<h:outputText value="Strike Angle:" />
					<h:panelGroup>
						<h:inputText id="FaultStrikeAngle"
							value="#{DislocBean.currentFault.faultStrikeAngle}" required="true" />
					</h:panelGroup>

					<h:outputText value="Strike Slip:" />
					<h:panelGroup>
						<h:inputText id="FaultStrikeSlip"
							value="#{DislocBean.currentFault.faultStrikeSlip}" required="true" />
					</h:panelGroup>

					<h:outputText value="Tensile Slip:" />
					<h:panelGroup>
						<h:inputText id="FaultTensileSlip"
							value="#{DislocBean.currentFault.faultTensileSlip}" required="true" />
					</h:panelGroup>

					<h:outputText value="Lame Lambda:" />
					<h:panelGroup>
						<h:inputText id="LameLambda"
							value="#{DislocBean.currentFault.faultLameLambda}" required="true" />
					</h:panelGroup>

					<h:outputText value="Lame Mu:" />
					<h:panelGroup>
						<h:inputText id="LameMu"
							value="#{DislocBean.currentFault.faultLameMu}" required="true" />
					</h:panelGroup>

					<h:commandButton id="addfault" value="select"
						actionListener="#{DislocBean.toggleAddFaultForProject}" />


				</h:panelGrid>
			</h:form>

			<h:form id="faultselection"
				rendered="#{DislocBean.renderAddFaultSelectionForm}">
				<h:panelGrid id="AddFaultSelection" columns="1"
					footerClass="subtitle" headerClass="subtitlebig"
					styleClass="medium" columnClasses="subtitle,medium">
					<h:panelGroup>
						<h:outputFormat escape="false"
							value="<b>Fault Database Selection</b><br><br>" />
						<h:outputFormat escape="false"
							value="You may select faults from the Fault Database using author search, latitude/longitude bounding box, or by viewing the master list (long).<br><br>" />
						<h:outputFormat escape="false"
							value="Please choose a radio button and click <b>Select</b>.<br><br>" />
					</h:panelGroup>

					<h:panelGroup>

						<h:selectOneRadio layout="pageDirection" id="subscriptionssss"
							value="#{DislocBean.faultSelectionCode}">
							<f:selectItem id="item01" itemLabel="Search by fault name."
								itemValue="SearchByFaultName" />
							<f:selectItem id="item02"
								itemLabel="Search by Lat/Lon bounding box."
								itemValue="SearchByLatLon" />
							<f:selectItem id="item03" itemLabel="Search by author."
								itemValue="SearchByAuthor" />
							<f:selectItem id="item04" itemLabel="View all faults."
								itemValue="ViewAllFaults" />
						</h:selectOneRadio>
						<h:commandButton id="button122" value="Make Selection"
							actionListener="#{DislocBean.toggleFaultSelection}" />

					</h:panelGroup>
				</h:panelGrid>
			</h:form>

			<h:form id="faultsearchByNameform"
				rendered="#{DislocBean.renderSearchByFaultNameForm}">
				<h:panelGrid id="FaultSearchName" columns="1" footerClass="subtitle"
					headerClass="subtitlebig" styleClass="medium"
					columnClasses="subtitle,medium">
					<h:outputFormat escape="false"
						value="<b>Search Fault DB by Fault Name</b><br><br>" />
					<h:panelGroup>
						<h:panelGroup>
							<h:outputText escape="false"
								value="Enter the name of the fault. The search will return partial matches." />
							<h:outputText escape="false" value="<br>" />
						</h:panelGroup>

						<h:panelGroup>
							<h:inputText id="Fault_Name" value="#{DislocBean.forSearchStr}"
								required="true" />
							<h:message for="Fault_Name" showDetail="true" showSummary="true"
								errorStyle="color: red" />
							<h:commandButton value="Query"
								actionListener="#{DislocBean.toggleFaultSearchByName}" />
						</h:panelGroup>
					</h:panelGroup>
				</h:panelGrid>
			</h:form>

			<h:form id="faultlatlonsearchform"
				rendered="#{DislocBean.renderSearchByLatLonForm}">
				<h:panelGrid id="FaultLatLonSearch" columns="1"
					footerClass="subtitle" headerClass="subtitlebig"
					styleClass="medium" columnClasses="subtitle,medium">
					<h:outputFormat escape="false"
						value="<b>Search Fault DB by Bounding Latitude and Longitude</b><br><br>" />
					<h:outputFormat escape="false"
						value="Enter the starting and ending latitude and longitude values (in decimal degrees) of the search bounding box. All faults completely within the bounding box will be returned.<br><br>" />


					<h:panelGrid columns="2" border="0">


						<h:outputText value="Starting Latitude: " />
						<h:panelGroup>
							<h:inputText id="StartingLatitude"
								value="#{DislocBean.faultLatStart}" required="true" />
							<h:message for="StartingLatitude" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="Ending Latitude: " />
						<h:panelGroup>
							<h:inputText id="EndingLatitude" value="#{DislocBean.faultLatEnd}"
								required="true" />
							<h:message for="EndingLatitude" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:outputText value="Starting Longitude: " />
						<h:panelGroup>
							<h:inputText id="StartingLongitude"
								value="#{DislocBean.faultLonStart}" required="true" />
							<h:message for="StartingLongitude" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:outputText value="Ending Longitude: " />
						<h:panelGroup>
							<h:inputText id="EndingLongitude" value="#{DislocBean.faultLonEnd}"
								required="true" />
							<h:message for="EndingLongitude" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:panelGroup>

							<h:commandButton value="Query"
								actionListener="#{DislocBean.toggleFaultSearchByLonLat}" />
						</h:panelGroup>
					</h:panelGrid>

				</h:panelGrid>
			</h:form>

			<h:form id="FaultAuthorSearchform"
				rendered="#{DislocBean.renderSearchByAuthorForm}">
				<h:panelGrid id="FaultAuthorSearch" columns="1"
					footerClass="subtitle" headerClass="subtitlebig"
					styleClass="medium" columnClasses="subtitle,medium">
					<h:outputFormat escape="false"
						value="<b>Search Fault DB by Author</b><br><br>" />
					<h:panelGroup>
						<h:panelGroup>
							<h:outputText escape="false"
								value="Enter the last name of the principal author of the desired fault descriptions. The search will do partial matches." />
							<h:outputText escape="false" value="<br>" />
						</h:panelGroup>

						<h:panelGroup>
							<h:inputText id="FaultAuthorForSearch"
								value="#{DislocBean.forSearchStr}" required="true" />
							<h:message for="FaultAuthorForSearch" showDetail="true"
								showSummary="true" errorStyle="color: red" />
							<h:commandButton value="Query"
								actionListener="#{DislocBean.toggleFaultSearchByAuthor}" />
						</h:panelGroup>
					</h:panelGroup>
				</h:panelGrid>
			</h:form>

			<h:form id="SelectFaultDBEntryForm"
				rendered="#{DislocBean.renderAddFaultFromDBForm}">
				<h:dataTable value="#{DislocBean.myFaultDBEntryList}" var="myentry1"
					binding="#{DislocBean.myFaultDataTable}">

					<h:column>
						<f:facet name="header">
							<h:outputText escape="false" value="<b>FaultName</b>" />
						</f:facet>
						<h:selectOneRadio layout="pageDirection"
							valueChangeListener="#{DislocBean.handleFaultsRadioValueChange}"
							onchange="dataTableSelectOneRadio(this)"
							onclick="dataTableSelectOneRadio(this)">
							<f:selectItems value="#{myentry1.faultName}" />
						</h:selectOneRadio>
					</h:column>

					<h:column>
						<f:facet name="header">
							<h:outputText escape="false" value="<b>SegmentName</b>" />
						</f:facet>
						<h:outputText value="#{myentry1.faultSegmentName}" />
					</h:column>

					<h:column>
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Author1</b>" />
						</f:facet>
						<h:outputText value="#{myentry1.faultAuthor}" />
					</h:column>

					<h:column>
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Segment Coordinates</b>" />
						</f:facet>
						<h:outputText value="#{myentry1.faultSegmentCoordinates}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Action</b>" />
						</f:facet>
						<h:commandLink actionListener="#{DislocBean.handleFaultEntryEdit}">
							<h:outputText value="Get" />
						</h:commandLink>
					</h:column>
				</h:dataTable>
				<h:commandButton id="SelectFaultDBEntry" value="SelectFaultDBEntry"
					actionListener="#{DislocBean.toggleSelectFaultDBEntry}" />
			</h:form>

		</h:panelGroup>
	</h:panelGrid>

	<h:panelGroup 
		rendered="#{!empty DislocBean.myFaultEntryForProjectList
					   || !empty DislocBean.myObsvEntryForProjectList}">

	<h:outputText styleClass="header2" value="Current Project Components"/>

	<h:panelGrid id="ProjectComponentList" columns="2" border="1"
			columnClasses="alignTop, alignTop">

		<h:panelGroup>
			<h:form id="UpdateSelectFaultsForm"
				rendered="#{!empty DislocBean.myFaultEntryForProjectList}">
				<h:panelGrid columns="1" border="1">
					<h:panelGroup>
						<h:panelGrid columns="1">
							<h:outputText escape="false" value="<b>Faults</b>"/>
						</h:panelGrid>

						<h:dataTable border="1"
							value="#{DislocBean.myFaultEntryForProjectList}" var="myentry3">
							<h:column>
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Name</b>">
                           </h:outputText>
								</f:facet>
								<h:outputText value="#{myentry3.faultName}" />
							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText escape="false" value="<b>View</b>" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry3.view}"
									onchange="selectOne(this.form,this)"
									onclick="selectOne(this.form,this)" />

							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Remove</b>" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry3.delete}"
									onchange="selectOne(this.form,this)"
									onclick="selectOne(this.form,this)" />
							</h:column>

						</h:dataTable>
					</h:panelGroup>

				</h:panelGrid>
				<h:commandButton id="SelectFault4proj" value="UpdateFault"
					actionListener="#{DislocBean.toggleUpdateFaultProjectEntry}" />

			</h:form>

			<h:form id="UpdateSelectedParamsForm"
				rendered="#{!empty DislocBean.myObsvEntryForProjectList}">
				<h:panelGrid columns="1" border="1">
					<h:panelGroup>
						<h:panelGrid columns="1">
							<h:outputText escape="false" value="<b>Observations</b>"/>
						</h:panelGrid>

						<h:dataTable border="1"
							value="#{DislocBean.myObsvEntryForProjectList}" var="myentry4">
							<h:column>
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Name</b>">
                           </h:outputText>
								</f:facet>
								<h:outputText value="Observations" />
							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText escape="false" value="<b>View</b>" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry4.view}"
									onchange="selectOne(this.form,this)"
									onclick="selectOne(this.form,this)" />

							</h:column>
							<h:column>
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Remove</b>" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry4.delete}"
									onchange="selectOne(this.form,this)"
									onclick="selectOne(this.form,this)" />
							</h:column>

						</h:dataTable>
					</h:panelGroup>

				</h:panelGrid>
				<h:commandButton id="updateObsv" value="Update Observations"
					actionListener="#{DislocBean.toggleUpdateProjectObservations}" />

			</h:form>
		</h:panelGroup>

		<h:form id="RunDisloc">
			<h:panelGrid columns="1" 
				rendered="#{!(empty DislocBean.myFaultEntryForProjectList)
							   && !(empty DislocBean.myObsvEntryForProjectList)}"
				footerClass="subtitle"
				headerClass="subtitlebig" styleClass="medium"
				columnClasses="subtitle,medium">

				<h:outputFormat escape="false"
					value="<b>Run Disloc</b><br><br>" />
				<h:outputFormat escape="false"
					value="Click the button below to run Disloc.<br><br>" />

					<h:commandButton value="Run Disloc"
						action="#{DislocBean.runNonBlockingDislocJSF}" />

			</h:panelGrid>
		</h:form>
	</h:panelGrid>
   </h:panelGroup>

	<h:form>
		<hr />
		<h:commandLink action="Disloc-back">
			<h:outputText value="#{DislocBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>

</f:view>

</body>
</html>
