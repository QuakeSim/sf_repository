<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
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
<style>
.alignTop {
vertical-align:top;
}
</style>
<f:view>
	<h:outputText styleClass="header2" value="Project Component Manager"/>   
	<h:outputText value="You must provide at least one fault and one observation point before you can run Simplex"/>
	<h:panelGrid id="panelgrid" columns="2" border="0"
		columnClasses="alignTop, alignTop">
		<h:panelGrid id="EditProject" columns="1" border="1">
			<h:form id="selectproj">
				<h:panelGroup>
					<h:outputFormat escape="false"
						value="<b>Project Name:</b> #{SimplexBean.projectName} <br>" />
					<h:outputFormat escape="false"
						value="<b>Starting Temperature:</b> #{SimplexBean.currentProjectEntry.startTemp} <br>" />
					<h:outputFormat escape="false"
						value="<b>Maximum Iterations:</b> #{SimplexBean.currentProjectEntry.maxIters} <br>" />

					<h:selectOneRadio layout="pageDirection" id="subscriptions"
						value="#{SimplexBean.currentEditProjectForm.projectSelectionCode}">
						<f:selectItem id="item1"
							itemLabel="Add an Observation Point: Click to specify observation point parameters."
							itemValue="ShowObservation" />
						<f:selectItem id="item0"
							itemLabel="Add Observation List (Advanced): Cut and paste a list of observation points. "
							itemValue="ShowObsvCutPaste" />
						<f:selectItem id="item2"
							itemLabel="Create New Fault: Click to specify geometry for a fault segment."
							itemValue="CreateNewFault" />
						<f:selectItem id="item4"
							itemLabel="Add Fault from DB: Click to select a fault segment from the database."
							itemValue="AddFaultSelection" />
					</h:selectOneRadio>
					<h:commandButton id="button1" value="Make Selection"
						actionListener="#{SimplexBean.currentEditProjectForm.toggleProjectSelection}">
					</h:commandButton>
				</h:panelGroup>
			</h:form>

			<h:panelGroup>
					 <h:form id="obsvCutPaste"
					 			rendered="#{SimplexBean.currentEditProjectForm.renderCreateObsvCutPaste}">
                <h:outputText id="cutinstruct1" escape="false"
					    value="<b>Mass Observation Import:</b> Enter one observation point per line in following format: <br> ObservationType LocationEast LocationNorth Value Uncertainty <br> Values can be either space or comma separated."/>
					   <h:panelGrid id="ObsvTextArea" columns="1">
						  <h:inputTextarea id="obsvTextArea"
							   rows="20" cols="50"
							 	value="#{SimplexBean.currentEditProjectForm.obsvTextArea}"/>
					     <h:commandButton id="addObsvTextArea" value="select"
							   actionListener="#{SimplexBean.toggleAddObsvTextAreaForProject}" />
						</h:panelGrid>
					 </h:form>
			</h:panelGroup>


			<h:panelGroup>
				<h:form id="observationform"
					rendered="#{SimplexBean.currentEditProjectForm.renderCreateObservationForm}">

					<h:panelGrid id="LayerTable" columns="2" footerClass="subtitle"
						headerClass="subtitlebig" styleClass="medium"
						columnClasses="subtitle,medium">

						<f:facet name="header">
							<h:outputFormat id="output2" escape="false"
								value="<b>Input Observation Parameters</b>" />
						</f:facet>

						<h:outputText value="Observation Name:" />
						<h:panelGroup>
							<h:inputText id="obsvName"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvName}"
								required="true" />
							<h:message for="obsvName" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="Observation Type:" />
						<h:selectOneMenu id="obsvType"
							value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvType}">
							<f:selectItem id="obsvTypeitem1" itemLabel="Displacement East"
								itemValue="1" />
							<f:selectItem id="obsvTypeitem2" itemLabel="Displacement North"
								itemValue="2" />
							<f:selectItem id="obsvTypeitem3" itemLabel="Displacement Up"
								itemValue="3" />
						</h:selectOneMenu>

						<h:outputText value="Observation Value:" />
						<h:panelGroup>
							<h:inputText id="obsvValue"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvValue}"
								required="true" />
							<h:message for="obsvValue" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="Observation Error:" />
						<h:panelGroup>
							<h:inputText id="obsvError"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvError}"
								required="true" />
							<h:message for="obsvError" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="Location East:" />
						<h:panelGroup>
							<h:inputText id="obsvLocationEast"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvLocationEast}"
								required="true" />
							<h:message for="obsvLocationEast" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="Location North:" />
						<h:panelGroup>
							<h:inputText id="obsvLocationNorth"
								value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvLocationNorth}"
								required="true" />
							<h:message for="obsvLocationNorth" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="Reference Site?" />
						<h:selectOneMenu id="obsvRefSite"
							value="#{SimplexBean.currentEditProjectForm.currentObservation.obsvRefSite}">
							<f:selectItem id="obsvRefSiteitem1" itemLabel="unselected"
								itemValue="1" />
							<f:selectItem id="obsvRefSiteitem2" itemLabel="selected"
								itemValue="-1" />
						</h:selectOneMenu>

						<h:commandButton id="addObservation" value="select"
							actionListener="#{SimplexBean.toggleAddObservationForProject}" />

					</h:panelGrid>
				</h:form>

				<h:form id="Faultform"
					rendered="#{SimplexBean.currentEditProjectForm.renderCreateNewFaultForm}">
					<h:panelGrid id="FaultTable" columns="3" footerClass="subtitle"
						headerClass="subtitlebig" styleClass="medium"
						columnClasses="subtitle,medium">

						<f:facet name="header">
							<h:outputFormat id="output3" escape="false"
								value="<b>Input Fault Geometry</b>" />
						</f:facet>

						<h:outputText value="Fault Name:" />
						<h:panelGroup>
							<h:inputText id="FaultName"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultName}"
								required="true" />
							<h:message for="FaultName" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:panelGroup>
						</h:panelGroup>

						<h:outputText value="Location X:" />
						<h:panelGroup>
							<h:inputText id="FaultLocationX"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLocationX}"
								required="true" />
							<h:message for="FaultLocationX" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultOriginXVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultOriginXVary}" />

						<h:outputText value="Location Y:" />
						<h:panelGroup>
							<h:inputText id="FaultLocationY"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLocationY}"
								required="true" />
							<h:message for="FaultLocationY" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultOriginYVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultOriginYVary}" />


						<h:outputText value="Length:" />
						<h:panelGroup>
							<h:inputText id="FaultLength"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLength}"
								required="true" />
							<h:message for="FaultLength" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultLengthVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultLengthVary}" />


						<h:outputText value="Width:" />
						<h:panelGroup>
							<h:inputText id="FaultWidth"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultWidth}"
								required="true" />
							<h:message for="FaultWidth" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultWidthVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultWidthVary}" />


						<h:outputText value="Depth:" />
						<h:panelGroup>
							<h:inputText id="FaultDepth"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultDepth}"
								required="true" />
							<h:message for="FaultDepth" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultDepthVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultDepthVary}" />

						<h:outputText value="Dip Angle:" />
						<h:panelGroup>
							<h:inputText id="FaultDipAngle"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultDipAngle}"
								required="true" />
							<h:message for="FaultDipAngle" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultDipAngleVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultDipAngleVary}" />

						<h:outputText value="Strike Angle:" />
						<h:panelGroup>
							<h:inputText id="FaultStrikeAngle"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultStrikeAngle}"
								required="true" />
							<h:message for="FaultStrikeAngle" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultStrikeAngleVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultStrikeAngleVary}" />

						<h:outputText value="Dip Slip:" />
						<h:panelGroup>
							<h:inputText id="FaultSlip"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultSlip}"
								required="true" />
							<h:message for="FaultSlip" showDetail="true" showSummary="true"
								errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultDipSlipVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultDipSlipVary}" />

						<h:outputText value="Strike Slip:" />
						<h:panelGroup>
							<h:inputText id="FaultRakeAngle"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultRakeAngle}"
								required="true" />
							<h:message for="FaultRakeAngle" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:selectBooleanCheckbox id="faultStrikeSlipVary"
							value="#{SimplexBean.currentEditProjectForm.currentFault.faultStrikeSlipVary}" />


						<h:outputText value="Fault Lon Starts:" />
						<h:panelGroup>
							<h:inputText id="FaultLonStarts"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLonStarts}" />
						</h:panelGroup>
						<h:outputText value="opitional" />

						<h:outputText value="Fault Lat Starts:" />
						<h:panelGroup>
							<h:inputText id="FaultLatStarts"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLatStarts}" />
						</h:panelGroup>
						<h:outputText value="opitional" />

						<h:outputText value="Fault Lon Ends:" />
						<h:panelGroup>
							<h:inputText id="FaultLonEnds"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLonEnds}" />
						</h:panelGroup>
						<h:outputText value="opitional" />

						<h:outputText value="Fault Lat Ends:" />
						<h:panelGroup>
							<h:inputText id="FaultLatEnds"
								value="#{SimplexBean.currentEditProjectForm.currentFault.faultLatEnds}" />
						</h:panelGroup>
						<h:outputText value="opitional" />

						<h:commandButton id="addfault" value="select"
							actionListener="#{SimplexBean.toggleAddFaultForProject}" />

					</h:panelGrid>
				</h:form>

				<h:form id="DislocListForm"
					rendered="#{SimplexBean.currentEditProjectForm.renderDislocListForm}">
					<h:commandButton id="SelectLayerDBEntry" value="select"
						actionListener="#{SimplexBean.toggleSelectDislocListEntry}" />
				</h:form>

				<h:form id="faultselection"
					rendered="#{SimplexBean.currentEditProjectForm.renderAddFaultSelectionForm}">
					<h:panelGrid id="AddFaultSelection" columns="1"
						footerClass="subtitle" headerClass="subtitlebig"
						styleClass="medium" columnClasses="subtitle,medium">
						<h:panelGroup>
							<h:outputFormat escape="false"
								value="<b>Fault Database Selection</b><br><br>" />
							<h:outputFormat escape="false"
								value="You may select faults from the Fault Database using author search, <br>latitude/longitude bounding box, or by viewing the master list (long).<br><br>" />
							<h:outputFormat escape="false"
								value="Please choose a radio button and click <b>Select</b>.<br><br>" />
						</h:panelGroup>

						<h:panelGroup>

							<h:selectOneRadio layout="pageDirection" id="subscriptionssss"
								value="#{SimplexBean.currentEditProjectForm.faultSelectionCode}">
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
								actionListener="#{SimplexBean.currentEditProjectForm.toggleFaultSelection}" />

						</h:panelGroup>
					</h:panelGrid>
				</h:form>

				<h:form id="faultsearchByNameform"
					rendered="#{SimplexBean.currentEditProjectForm.renderSearchByFaultNameForm}">
					<h:panelGrid id="FaultSearchName" columns="1"
						footerClass="subtitle" headerClass="subtitlebig"
						styleClass="medium" columnClasses="subtitle,medium">
						<h:outputFormat escape="false"
							value="<b>Search Fault DB by Fault Name</b><br><br>" />
						<h:panelGroup>
							<h:panelGroup>
								<h:outputText escape="false"
									value="Enter the name of the fault. The search will return partial matches." />
								<h:outputText escape="false" value="<br>" />
							</h:panelGroup>

							<h:panelGroup>
								<h:inputText id="Fault_Name"
									value="#{SimplexBean.currentEditProjectForm.forSearchStr}"
									required="true" />
								<h:message for="Fault_Name" showDetail="true" showSummary="true"
									errorStyle="color: red" />
								<h:commandButton value="Query"
									actionListener="#{SimplexBean.currentEditProjectForm.toggleFaultSearchByName}" />
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGrid>
				</h:form>

				<h:form id="faultlatlonsearchform"
					rendered="#{SimplexBean.currentEditProjectForm.renderSearchByLatLonForm}">
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
									value="#{SimplexBean.currentEditProjectForm.faultLatStart}"
									required="true" />
								<h:message for="StartingLatitude" showDetail="true"
									showSummary="true" errorStyle="color: red" />
							</h:panelGroup>

							<h:outputText value="Ending Latitude: " />
							<h:panelGroup>
								<h:inputText id="EndingLatitude"
									value="#{SimplexBean.currentEditProjectForm.faultLatEnd}"
									required="true" />
								<h:message for="EndingLatitude" showDetail="true"
									showSummary="true" errorStyle="color: red" />
							</h:panelGroup>
							<h:outputText value="Starting Longitude: " />
							<h:panelGroup>
								<h:inputText id="StartingLongitude"
									value="#{SimplexBean.currentEditProjectForm.faultLonStart}"
									required="true" />
								<h:message for="StartingLongitude" showDetail="true"
									showSummary="true" errorStyle="color: red" />
							</h:panelGroup>
							<h:outputText value="Ending Longitude: " />
							<h:panelGroup>
								<h:inputText id="EndingLongitude"
									value="#{SimplexBean.currentEditProjectForm.faultLonEnd}"
									required="true" />
								<h:message for="EndingLongitude" showDetail="true"
									showSummary="true" errorStyle="color: red" />
							</h:panelGroup>
							<h:panelGroup>

								<h:commandButton value="Query"
									actionListener="#{SimplexBean.currentEditProjectForm.toggleFaultSearchByLonLat}" />
							</h:panelGroup>
						</h:panelGrid>

					</h:panelGrid>
				</h:form>

				<h:form id="FaultAuthorSearchform"
					rendered="#{SimplexBean.currentEditProjectForm.renderSearchByAuthorForm}">
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
									value="#{SimplexBean.currentEditProjectForm.forSearchStr}"
									required="true" />
								<h:message for="FaultAuthorForSearch" showDetail="true"
									showSummary="true" errorStyle="color: red" />
								<h:commandButton value="Query"
									actionListener="#{SimplexBean.currentEditProjectForm.toggleFaultSearchByAuthor}" />
							</h:panelGroup>
						</h:panelGroup>
					</h:panelGrid>
				</h:form>

				<h:form id="SelectFaultDBEntryForm"
					rendered="#{SimplexBean.currentEditProjectForm.renderAddFaultFromDBForm}">
					<h:dataTable
						value="#{SimplexBean.currentEditProjectForm.myFaultDBEntryList}"
						var="myentry1"
						binding="#{SimplexBean.currentEditProjectForm.myFaultDataTable}">

						<h:column>
							<f:facet name="header">
								<h:outputText escape="false" value="<b>FaultName</b>" />
							</f:facet>
							<h:selectOneRadio layout="pageDirection"
								valueChangeListener="#{SimplexBean.currentEditProjectForm.handleFaultsRadioValueChange}"
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
							<h:commandLink
								actionListener="#{SimplexBean.currentEditProjectForm.handleFaultEntryEdit}">
								<h:outputText value="Get" />
							</h:commandLink>
						</h:column>
					</h:dataTable>
					<h:commandButton id="SelectFaultDBEntry" value="SelectFaultDBEntry"
						actionListener="#{SimplexBean.currentEditProjectForm.toggleSelectFaultDBEntry}" />
				</h:form>

			</h:panelGroup>
		</h:panelGrid>


		<h:panelGrid id="ProjectComponentList" columns="1" border="0">

			<h:panelGroup>
				<h:form id="UpdateSelectFaultsForm"
					rendered="#{!empty SimplexBean.myFaultEntryForProjectList}">
					<h:panelGrid columns="1" border="1">
						<h:panelGroup>
							<h:panelGrid columns="1" border="1">
								<h:outputFormat escape="false"
									value="<b>Current Project Fault Components</b>">
								</h:outputFormat>
							</h:panelGrid>

							<h:dataTable border="1"
								value="#{SimplexBean.myFaultEntryForProjectList}" var="myentry3">
								<h:column>
									<f:facet name="header">
										<h:outputText escape="false" value="<b>Name</b>" />
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
						actionListener="#{SimplexBean.toggleUpdateFaultProjectEntry}" />
				</h:form>

				<h:form id="UpdateSelectObservationForm"
					rendered="#{!empty SimplexBean.myObservationEntryForProjectList}">
					<h:panelGrid columns="1" border="1">
						<h:panelGroup>
							<h:panelGrid columns="1" border="1">
								<h:outputFormat escape="false"
									value="<b>Current Project Observation Components</b>">
								</h:outputFormat>
							</h:panelGrid>

							<h:dataTable border="1"
								value="#{SimplexBean.myObservationEntryForProjectList}"
								var="myentry4">
								<h:column>
									<f:facet name="header">
										<h:outputText escape="false" value="<b>Name</b>" />
									</f:facet>
									<h:outputText value="#{myentry4.observationName}" />
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
					<h:commandButton id="SelectObservation4proj"
						value="UpdateObservation"
						actionListener="#{SimplexBean.toggleUpdateObservationProjectEntry}" />

				</h:form>
			</h:panelGroup>

		</h:panelGrid>
		
		<h:form>
		<h:commandButton id="runSimplex2" value="Run Simplex2"
			action="#{SimplexBean.toggleRunSimplex2}" />
		</h:form>2

	</h:panelGrid>


	<h:form>
		<hr />
		<h:commandLink action="Simplex2-back">
			<h:outputText value="#{SimplexBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>

</f:view>

</body>
</html>
