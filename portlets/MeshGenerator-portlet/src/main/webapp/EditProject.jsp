<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
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
	<h2>Project Input</h2>
	<p>Create your geometry out of layers and faults.</p>


	<h:panelGrid id="EditProject" columns="2" border="1">
		<h:form id="selectproj">
			<h:panelGroup>

				<h:outputFormat escape="false"
					value="<b>Project Name: #{MGBean.projectName} </b>" />

				<h:selectOneRadio layout="pageDirection" id="subscriptions"
					value="#{MGBean.projectSelectionCode}">
					<f:selectItem id="item1"
						itemLabel="Create New Layer: Click to specify geometry for a layer."
						itemValue="CreateNewLayer" />
					<f:selectItem id="item2"
						itemLabel="Create New Fault: Click to specify geometry for a fault segment."
						itemValue="CreateNewFault" />
					<f:selectItem id="item3"
						itemLabel="Add Layer from DB: Click to select a layer from the database."
						itemValue="AddLayerFromDB" />
					<f:selectItem id="item4"
						itemLabel="Add Fault from DB: Click to select a fault segment from the database."
						itemValue="AddFaultSelection" />
				</h:selectOneRadio>
				<h:commandButton id="button1" value="Make Selection"
					actionListener="#{MGBean.toggleProjectSelection}">
				</h:commandButton>


			</h:panelGroup>
		</h:form>



		<h:panelGroup>
			<h:form id="layerform" rendered="#{MGBean.renderCreateNewLayerForm}">

				<h:panelGrid id="LayerTable" columns="2" footerClass="subtitle"
					headerClass="subtitlebig" styleClass="medium"
					columnClasses="subtitle,medium">

					<f:facet name="header">
						<h:outputFormat id="output2" escape="false"
							value="<b>Input Solid Layer Geometry </b>" />
					</f:facet>

					<h:outputText value="Layer Name:" />
					<h:panelGroup>
						<h:inputText id="LayerName"
							value="#{MGBean.currentLayer.layerName}" required="true" />
						<h:message for="LayerName" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Origin X:" />
					<h:panelGroup>
						<h:inputText id="LayerOriginX"
							value="#{MGBean.currentLayer.layerOriginX}" required="true" />
						<h:message for="LayerOriginX" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Origin Y:" />
					<h:panelGroup>
						<h:inputText id="LayerOriginY"
							value="#{MGBean.currentLayer.layerOriginY}" required="true" />
						<h:message for="LayerOriginY" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Origin Z:" />
					<h:panelGroup>
						<h:inputText id="LayerOriginZ"
							value="#{MGBean.currentLayer.layerOriginZ}" required="true" />
						<h:message for="LayerOriginZ" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Length:" />
					<h:panelGroup>
						<h:inputText id="LayerLength"
							value="#{MGBean.currentLayer.layerLength}" required="true" />
						<h:message for="LayerLength" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Width:" />
					<h:panelGroup>
						<h:inputText id="LayerWidth"
							value="#{MGBean.currentLayer.layerWidth}" required="true" />
						<h:message for="LayerWidth" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Depth:" />
					<h:panelGroup>
						<h:inputText id="LayerDepth"
							value="#{MGBean.currentLayer.layerDepth}" required="true" />
						<h:message for="LayerDepth" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Lame Lambda:" />
					<h:panelGroup>
						<h:inputText id="LayerLameLambda"
							value="#{MGBean.currentLayer.lameLambda}" required="true" />
						<h:message for="LayerLameLambda" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Lame Mu:" />
					<h:panelGroup>
						<h:inputText id="LayerLameMu"
							value="#{MGBean.currentLayer.lameMu}" required="true" />
						<h:message for="LayerLameMu" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Viscosity:" />
					<h:panelGroup>
						<h:inputText id="LayerViscosity"
							value="#{MGBean.currentLayer.viscosity}" required="true" />
						<h:message for="LayerViscosity" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Exponent:" />
					<h:panelGroup>
						<h:inputText id="LayerExponent"
							value="#{MGBean.currentLayer.exponent}" required="true" />
						<h:message for="LayerExponent" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:commandButton id="addlayer" value="select"
						actionListener="#{MGBean.toggleAddLayerForProject}" />


				</h:panelGrid>
			</h:form>

			<h:form id="Faultform" rendered="#{MGBean.renderCreateNewFaultForm}">
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
							value="#{MGBean.currentFault.faultName}" required="true" />
						<h:message for="FaultName" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Location X:" />
					<h:panelGroup>
						<h:inputText id="FaultLocationX"
							value="#{MGBean.currentFault.faultLocationX}" required="true" />
						<h:message for="FaultLocationX" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Location Y:" />
					<h:panelGroup>
						<h:inputText id="FaultLocationY"
							value="#{MGBean.currentFault.faultLocationY}" required="true" />
						<h:message for="FaultLocationY" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Length:" />
					<h:panelGroup>
						<h:inputText id="FaultLength"
							value="#{MGBean.currentFault.faultLength}" required="true" />
						<h:message for="FaultLength" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Width:" />
					<h:panelGroup>
						<h:inputText id="FaultWidth"
							value="#{MGBean.currentFault.faultWidth}" required="true" />
						<h:message for="FaultWidth" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Depth:" />
					<h:panelGroup>
						<h:inputText id="FaultDepth"
							value="#{MGBean.currentFault.faultDepth}" required="true" />
						<h:message for="FaultDepth" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Dip Angle:" />
					<h:panelGroup>
						<h:inputText id="FaultDipAngle"
							value="#{MGBean.currentFault.faultDipAngle}" required="true" />
						<h:message for="FaultDipAngle" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Strike Angle:" />
					<h:panelGroup>
						<h:inputText id="FaultStrikeAngle"
							value="#{MGBean.currentFault.faultStrikeAngle}" required="true" />
						<h:message for="FaultStrikeAngle" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Slip:" />
					<h:panelGroup>
						<h:inputText id="FaultSlip"
							value="#{MGBean.currentFault.faultSlip}" required="true" />
						<h:message for="FaultSlip" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Rake Angle:" />
					<h:panelGroup>
						<h:inputText id="FaultRakeAngle"
							value="#{MGBean.currentFault.faultRakeAngle}" required="true" />
						<h:message for="FaultRakeAngle" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>
					<h:commandButton id="addfault" value="select"
						actionListener="#{MGBean.toggleAddFaultForProject}" />


				</h:panelGrid>
			</h:form>

			<h:form id="SelectLayerDBEntryForm"
				rendered="#{MGBean.renderAddLayerFromDBForm}">
				<h:dataTable value="#{MGBean.myLayerDBEntryList}" var="myentry2"
					binding="#{MGBean.myLayerDataTable}">
					<h:column>
						<f:facet name="header">
							<h:outputText escape="false" value="<b>LayerName</b>" />
						</f:facet>
						<h:selectOneRadio layout="pageDirection"
							valueChangeListener="#{MGBean.handleLayersRadioValueChange}"
							onchange="dataTableSelectOneRadio(this)"
							onclick="dataTableSelectOneRadio(this)">
							<f:selectItems value="#{myentry2.layerName}" />
						</h:selectOneRadio>
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Author1</b>" />
						</f:facet>
						<h:outputText value="#{myentry2.layerAuthor}" />
					</h:column>
					<h:column>
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Action</b>" />
						</f:facet>
						<h:commandLink actionListener="#{MGBean.handleLayerEntryEdit}">
							<h:outputText value="Get" />
						</h:commandLink>
					</h:column>
				</h:dataTable>

				<h:commandButton id="SelectLayerDBEntry" value="SelectLayerDBEntry"
					actionListener="#{MGBean.toggleSelectLayerDBEntry}" />
			</h:form>

			<h:form id="faultselection"
				rendered="#{MGBean.renderAddFaultSelectionForm}">
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
							value="#{MGBean.faultSelectionCode}">
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
							actionListener="#{MGBean.toggleFaultSelection}" />

					</h:panelGroup>
				</h:panelGrid>
			</h:form>

			<h:form id="faultsearchByNameform"
				rendered="#{MGBean.renderSearchByFaultNameForm}">
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
							<h:inputText id="Fault_Name" value="#{MGBean.forSearchStr}"
								required="true" />
							<h:message for="Fault_Name" showDetail="true" showSummary="true"
								errorStyle="color: red" />
							<h:commandButton value="Query"
								actionListener="#{MGBean.toggleFaultSearchByName}" />
						</h:panelGroup>
					</h:panelGroup>
				</h:panelGrid>
			</h:form>

			<h:form id="faultlatlonsearchform"
				rendered="#{MGBean.renderSearchByLatLonForm}">
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
								value="#{MGBean.faultLatStart}" required="true" />
							<h:message for="StartingLatitude" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="Ending Latitude: " />
						<h:panelGroup>
							<h:inputText id="EndingLatitude" value="#{MGBean.faultLatEnd}"
								required="true" />
							<h:message for="EndingLatitude" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:outputText value="Starting Longitude: " />
						<h:panelGroup>
							<h:inputText id="StartingLongitude"
								value="#{MGBean.faultLonStart}" required="true" />
							<h:message for="StartingLongitude" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:outputText value="Ending Longitude: " />
						<h:panelGroup>
							<h:inputText id="EndingLongitude" value="#{MGBean.faultLonEnd}"
								required="true" />
							<h:message for="EndingLongitude" showDetail="true"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:panelGroup>

							<h:commandButton value="Query"
								actionListener="#{MGBean.toggleFaultSearchByLonLat}" />
						</h:panelGroup>
					</h:panelGrid>

				</h:panelGrid>
			</h:form>

			<h:form id="FaultAuthorSearchform"
				rendered="#{MGBean.renderSearchByAuthorForm}">
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
								value="#{MGBean.forSearchStr}" required="true" />
							<h:message for="FaultAuthorForSearch" showDetail="true"
								showSummary="true" errorStyle="color: red" />
							<h:commandButton value="Query"
								actionListener="#{MGBean.toggleFaultSearchByAuthor}" />
						</h:panelGroup>
					</h:panelGroup>
				</h:panelGrid>
			</h:form>

			<h:form id="SelectFaultDBEntryForm"
				rendered="#{MGBean.renderAddFaultFromDBForm}">
				<h:dataTable value="#{MGBean.myFaultDBEntryList}" var="myentry1"
					binding="#{MGBean.myFaultDataTable}">

					<h:column>
						<f:facet name="header">
							<h:outputText escape="false" value="<b>FaultName</b>" />
						</f:facet>
						<h:selectOneRadio layout="pageDirection"
							valueChangeListener="#{MGBean.handleFaultsRadioValueChange}"
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
						<h:commandLink actionListener="#{MGBean.handleFaultEntryEdit}">
							<h:outputText value="Get" />
						</h:commandLink>
					</h:column>
				</h:dataTable>
				<h:commandButton id="SelectFaultDBEntry" value="SelectFaultDBEntry"
					actionListener="#{MGBean.toggleSelectFaultDBEntry}" />
			</h:form>

		</h:panelGroup>
	</h:panelGrid>
	<hr />
	<h2>Current Project Components</h2>

	<h:panelGrid id="ProjectComponentList" columns="3" border="1"
		columnClasses="list-column-top,
  list-column-left, list-column-top,
list-column-left">

		<h:panelGroup>
			<h:form id="UpdateSelectFaultsForm"
				rendered="#{!empty MGBean.myFaultEntryForProjectList}">
				<h:panelGrid columns="1" border="1">
					<h:panelGroup>
						<h:panelGrid columns="1" border="1">
							<h:outputFormat escape="false" value="<b>Faults</b>">
							</h:outputFormat>
						</h:panelGrid>

						<h:dataTable border="1"
							value="#{MGBean.myFaultEntryForProjectList}" var="myentry3">
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
					actionListener="#{MGBean.toggleUpdateFaultProjectEntry}" />

			</h:form>
			<h:form id="UpdateSelectLayersForm"
				rendered="#{!empty MGBean.myLayerEntryForProjectList}">
				<h:panelGrid columns="1" border="1">
					<h:panelGroup>
						<h:panelGrid columns="1" border="1">
							<h:outputFormat escape="false" value="<b>Layers</b>">
							</h:outputFormat>
						</h:panelGrid>

						<h:dataTable border="1"
							value="#{MGBean.myLayerEntryForProjectList}" var="myentry4">
							<h:column>
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Name</b>" />
								</f:facet>
								<h:outputText value="#{myentry4.layerName}" />
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
				<h:commandButton id="SelectFault4proj" value="UpdateLayer"
					actionListener="#{MGBean.toggleUpdateLayerProjectEntry}" />

			</h:form>
		</h:panelGroup>

		<h:form id="MeshGen">
			<h:panelGrid columns="1" footerClass="subtitle"
				headerClass="subtitlebig" styleClass="medium"
				columnClasses="subtitle,medium">
				<h:outputFormat escape="false"
					value="<b>Create Initial Mesh</b><br><br>" />
				<h:outputFormat escape="false"
					value="Click the button below to generate a mesh for the geometry.<br><br>" />

				<h:panelGrid columns="2" border="0">
					<h:outputText escape="false" value="<b>Mesh Size: </b>" />
					<h:panelGroup>
						<h:inputText id="MeshSize" value="#{MGBean.meshSize}"
							required="true" />
						<h:message for="MeshSize" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>
					<h:outputText escape="false" value="<b>Mesh Refine Limit: </b>" />
					<h:panelGroup>
						<h:inputText id="MeshRefineLimit" value="#{MGBean.magic15}"
							required="true" />
						<h:message for="MeshRefineLimit" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>
					<h:commandButton value="Generate Mesh"
						action="#{MGBean.toggleFireMeshGen}" />
				</h:panelGrid>

			</h:panelGrid>
		</h:form>

		<h:form id="PlottingTools">
			<h:panelGroup>
				<h:outputFormat escape="false" value="<b>Plotting Tools</b><br><br>" />
				<h:panelGrid columns="1" border="0">
					<h:outputFormat escape="false"
						value="Click the button below to plot Layers and Faults.<br><br>" />
					<h:commandButton value="  Plot  " action="#{MGBean.SetAndPlot}" />
					<h:outputFormat escape="false"
						value="<br>You can also try the Java Web Start version by clicking the link below." />

					<h:outputLink id="link1" value="painter.jsp">
						<f:param name="layers" value="#{MGBean.myLayersParamForJnlp}" />
						<f:param name="faults" value="#{MGBean.myFaultsParamForJnlp}" />
						<h:outputText value="Web Start Plotter" />
					</h:outputLink>

				</h:panelGrid>
			</h:panelGroup>
		</h:form>

	</h:panelGrid>

	<h:form>
		<hr />
		<h:commandLink action="MG-back">
			<h:outputText value="#{MGBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>

</f:view>

</body>
</html>
