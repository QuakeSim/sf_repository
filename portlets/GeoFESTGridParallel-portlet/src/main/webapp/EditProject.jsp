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
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here"
type="text/javascript"></script>
 <script src="@host.base.url@/Disloc2/egeoxml.js" type="text/javascript"></script>

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
	<h:outputText id="stuff1" styleClass="header2" value="Project Input"/>

	<p>Create your geometry out of layers and faults.</p>

	<h:panelGrid id="EditProject" 
		columnClasses="alignTop,alignTop"
		columns="2" border="1">
		<h:form id="selectproj">
			<h:panelGroup  id="stuff2">
				<h:outputText escape="false"  id="stuff3"
					value="<b>Project Name: #{MGBean.projectName}</b>" />

				<h:selectOneRadio layout="pageDirection" id="subscriptions"
				    id="stuff4"
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
					<f:selectItem id="item5"
						itemLabel="Add Fault from Map: Click to select a fault segment using the map interface."
						itemValue="FaultMapSelection" />
				</h:selectOneRadio>
				<h:commandButton id="button1" value="Make Selection"
					actionListener="#{MGBean.toggleProjectSelection}">
				</h:commandButton>
			</h:panelGroup>
		</h:form>
		<h:panelGroup  id="stuff4">

			<%/* Fault map search */%>
			<%@include file="FaultMapPanelFrame.jsp" %>
		   
			<h:form id="layerform" rendered="#{MGBean.renderCreateNewLayerForm}">

				<h:panelGrid id="LayerTable" columns="2" footerClass="subtitle"
					headerClass="subtitlebig" styleClass="medium"
					columnClasses="subtitle,medium">

					<f:facet name="header">
						<h:outputFormat id="output2" escape="false"
							value="<b>Input Solid Layer Geometry </b>" />
					</f:facet>

					<h:outputText  id="stuff6" value="Layer Name:" />
					<h:panelGroup  id="stuff7">
						<h:inputText id="LayerName"
							value="#{MGBean.currentLayer.layerName}" required="true" />
						<h:message  id="stuff77" 
							for="LayerName" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText  id="stuff8" value="Origin X:" />
					<h:panelGroup  id="stuff9">
						<h:inputText id="LayerOriginX"
							value="#{MGBean.currentLayer.layerOriginX}" required="true" />
						<h:message  id="stuff10"
						   for="LayerOriginX" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Origin Y:"  id="stuff11"/>
					<h:panelGroup  id="stuff12">
						<h:inputText id="LayerOriginY"
							value="#{MGBean.currentLayer.layerOriginY}" required="true" />
						<h:message for="LayerOriginY" showDetail="true" showSummary="true"
									   id="stuff123"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Origin Z:"  id="stuff13"/>
					<h:panelGroup  id="stuff14">
						<h:inputText id="LayerOriginZ"
							value="#{MGBean.currentLayer.layerOriginZ}" required="true" />
						<h:message for="LayerOriginZ" showDetail="true" showSummary="true"
									   id="stuff15"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText  id="stuff16" value="Length:" />
					<h:panelGroup  id="stuff17">
						<h:inputText id="LayerLength"
							value="#{MGBean.currentLayer.layerLength}" required="true" />
						<h:message for="LayerLength" showDetail="true" showSummary="true"
									   id="stuff18"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Width:"  id="stuff19"/>
					<h:panelGroup  id="stuff20">
						<h:inputText id="LayerWidth"
							value="#{MGBean.currentLayer.layerWidth}" required="true" />
						<h:message for="LayerWidth" showDetail="true" showSummary="true"
									   id="stuff21"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Depth:"  id="stuff22" />
					<h:panelGroup  id="stuff23">
						<h:inputText id="LayerDepth"
							value="#{MGBean.currentLayer.layerDepth}" required="true" />
						<h:message for="LayerDepth" showDetail="true" showSummary="true"
									   id="stuff24"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Lame Lambda:"  id="stuff25"/>
					<h:panelGroup  id="stuff26">
						<h:inputText id="LayerLameLambda"
							value="#{MGBean.currentLayer.lameLambda}" required="true" />
						<h:message for="LayerLameLambda" showDetail="true"
						 id="stuff27"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Lame Mu:"  id="stuff28" />
					<h:panelGroup  id="stuff29">
						<h:inputText id="LayerLameMu"
							value="#{MGBean.currentLayer.lameMu}" required="true" />
						<h:message for="LayerLameMu" showDetail="true" showSummary="true"
									   id="stuff30"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Viscosity:" id="stuff31"/>
					<h:panelGroup  id="stuff32">
						<h:inputText id="LayerViscosity"
							value="#{MGBean.currentLayer.viscosity}" required="true" />
						<h:message for="LayerViscosity" showDetail="true"			
									   id="stuff33"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Exponent:"  id="stuff34"/>
					<h:panelGroup  id="stuff35">
						<h:inputText id="LayerExponent"
							value="#{MGBean.currentLayer.exponent}" required="true" />
						<h:message for="LayerExponent" showDetail="true"
									   id="stuff36"
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

					<h:outputText value="Fault Name:" id="kld1"/>
					<h:panelGroup id="kld2">
						<h:inputText id="FaultName"
							value="#{MGBean.currentFault.faultName}" required="true" />
						<h:message for="FaultName" showDetail="true" showSummary="true"
									  id="stuff45"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText  id="kld3" value="Location X:" />
					<h:panelGroup  id="kld4">
						<h:inputText id="FaultLocationX"
							value="#{MGBean.currentFault.faultLocationX}" required="true" />
						<h:message for="FaultLocationX" showDetail="true"
												id="stuff55"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Location Y:"  id="kld5"/>
					<h:panelGroup  id="kld6">
						<h:inputText id="FaultLocationY"
							value="#{MGBean.currentFault.faultLocationY}" required="true" />
						<h:message for="FaultLocationY" showDetail="true"
									  id="stuff56"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Length:"  id="kld8"/>
					<h:panelGroup id="kld7">
						<h:inputText id="FaultLength"
							value="#{MGBean.currentFault.faultLength}" required="true" />
						<h:message for="FaultLength" showDetail="true" showSummary="true"
									 id="stuff57"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Width:"  id="kld9"/>
					<h:panelGroup  id="kld10">
						<h:inputText id="FaultWidth"
							value="#{MGBean.currentFault.faultWidth}" required="true" />
						<h:message for="FaultWidth" showDetail="true" showSummary="true"
												id="stuff57"
							errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Depth:"  id="kld11"/>
					<h:panelGroup  id="kld21">
						<h:inputText id="FaultDepth"
							value="#{MGBean.currentFault.faultDepth}" required="true" />
						<h:message for="FaultDepth" showDetail="true" showSummary="true"
									  id="stuff58"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Dip Angle:"  id="kld22"/>
					<h:panelGroup  id="kld23">
						<h:inputText id="FaultDipAngle"
							value="#{MGBean.currentFault.faultDipAngle}" required="true" />
						<h:message for="FaultDipAngle" showDetail="true"
									  id="stuff59"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Strike Angle:"  id="kld24"/>
					<h:panelGroup  id="kld25">
						<h:inputText id="FaultStrikeAngle"
							value="#{MGBean.currentFault.faultStrikeAngle}" required="true" />
						<h:message for="FaultStrikeAngle" showDetail="true"
									  id="stuff60"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>


					<h:outputText value="Slip:"  id="kld26"/>
					<h:panelGroup  id="kld27">
						<h:inputText id="FaultSlip"
							value="#{MGBean.currentFault.faultSlip}" required="true" />
						<h:message for="FaultSlip" showDetail="true" showSummary="true"
									   id="stuff61"
							errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Rake Angle:"  id="kld28"/>
					<h:panelGroup  id="kld29">
						<h:inputText id="FaultRakeAngle"
							value="#{MGBean.currentFault.faultRakeAngle}" required="true" />
						<h:message for="FaultRakeAngle" showDetail="true"
									   id="stuff62"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="First Event:"  id="kld30"/>
					<h:panelGroup  id="kld31">
						<h:inputText id="FaultFirstEvent"
							value="#{MGBean.currentFault.firstEvent}" required="true" />
						<h:message for="FaultFirstEvent" showDetail="true"
									   id="stuff63"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:outputText value="Repeat Time:"  id="klr3ee3"/>
					<h:panelGroup  id="ker3332d">
						<h:inputText id="FaultRepeatTime"
							value="#{MGBean.currentFault.repeatTime}" required="true" />
						<h:message for="FaultRepeatTime" showDetail="true"
									   id="stuff343"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>

					<h:commandButton id="addfault" value="select"
						actionListener="#{MGBean.toggleAddFaultForProject}" />
				</h:panelGrid>
			</h:form>

			<h:form id="SelectLayerDBEntryForm"
				rendered="#{MGBean.renderAddLayerFromDBForm}">
				<h:dataTable value="#{MGBean.myLayerDBEntryList}" var="myentry2"
				    id="der1"
					binding="#{MGBean.myLayerDataTable}">
					<h:column   id="der2">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>LayerName</b>" />
						</f:facet>
						<h:selectOneRadio layout="pageDirection"
						     id="der23"
							valueChangeListener="#{MGBean.handleLayersRadioValueChange}"
							onchange="dataTableSelectOneRadio(this)"
							onclick="dataTableSelectOneRadio(this)">
							<f:selectItems value="#{myentry2.layerName}" />
						</h:selectOneRadio>
					</h:column>
					<h:column   id="der3">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Author1</b>" />
						</f:facet>
						<h:outputText   id="der4" value="#{myentry2.layerAuthor}" />
					</h:column>
					<h:column   id="der5">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Action</b>" />
						</f:facet>
						<h:commandLink id="cl5" actionListener="#{MGBean.handleLayerEntryEdit}">
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
					<h:panelGroup   id="der6">
						<h:outputFormat escape="false"
						     id="der7"
							value="<b>Fault Database Selection</b><br><br>" />
						<h:outputFormat escape="false"
						     id="der8"
							value="You may select faults from the Fault Database using author search, latitude/longitude bounding box, or by viewing the master list (long).<br><br>" />
						<h:outputFormat escape="false" 
						     id="der9"
							value="Please choose a radio button and click <b>Select</b>.<br><br>" />
					</h:panelGroup>

					<h:panelGroup   id="der10">
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
					    id="der11"
						value="<b>Search Fault DB by Fault Name</b><br><br>" />
					<h:panelGroup   id="der12">
						<h:panelGroup   id="der13">
							<h:outputText escape="false"   id="der14"
								value="Enter the name of the fault. The search will return partial matches." />
							<h:outputText escape="false" value="<br>"  id="der15"/>
						</h:panelGroup>

						<h:panelGroup  id="der16">
							<h:inputText id="Fault_Name" value="#{MGBean.forSearchStr}"
								required="true" />
							<h:message for="Fault_Name" showDetail="true" showSummary="true"
										   id="stuff63"
								errorStyle="color: red" />
							<h:commandButton id="cb6" value="Query"
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
					  id="der17"
						value="<b>Search Fault DB by Bounding Latitude and Longitude</b><br><br>" />
					<h:outputFormat escape="false"  id="der18"
						value="Enter the starting and ending latitude and longitude values (in decimal degrees) of the search bounding box. All faults completely within the bounding box will be returned.<br><br>" />

					<h:panelGrid id="pgfaultlatlon" columns="2" border="0">

						<h:outputText value="Starting Latitude: "   id="erd19"/>
						<h:panelGroup  id="erd1">
							<h:inputText id="StartingLatitude"
								value="#{MGBean.faultLatStart}" required="true" />
							<h:message for="StartingLatitude" showDetail="true"
										   id="stuff70"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:outputText value="Ending Latitude: "   id="erd2"/>
						<h:panelGroup  id="erd3">
							<h:inputText id="EndingLatitude" value="#{MGBean.faultLatEnd}"
								required="true" />
							<h:message for="EndingLatitude" showDetail="true"
										   id="stuff71"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:outputText value="Starting Longitude: "   id="erd4"/>
						<h:panelGroup   id="erd5">
							<h:inputText id="StartingLongitude"
								value="#{MGBean.faultLonStart}" required="true" />
							<h:message for="StartingLongitude" showDetail="true"
										   id="stuff72"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>
						<h:outputText value="Ending Longitude: "   id="erd6"/>
						<h:panelGroup  id="erd7">
							<h:inputText id="EndingLongitude" value="#{MGBean.faultLonEnd}"
								required="true" />
							<h:message for="EndingLongitude" showDetail="true"
										   id="stuff73"
								showSummary="true" errorStyle="color: red" />
						</h:panelGroup>

						<h:panelGroup  id="erd8">
							<h:commandButton id="cb7" value="Query"
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
					<h:outputFormat escape="false"   id="erd9"
						value="<b>Search Fault DB by Author</b><br><br>" />
					<h:panelGroup   id="erd10">
						<h:panelGroup  id="erd11">
							<h:outputText escape="false"  id="erd12"
								value="Enter the last name of the principal author of the desired fault descriptions. The search will do partial matches." />
							<h:outputText escape="false" value="<br>"   id="erd13"/>
						</h:panelGroup>

						<h:panelGroup  id="erd14">
							<h:inputText id="FaultAuthorForSearch"
								value="#{MGBean.forSearchStr}" required="true" />
							<h:message for="FaultAuthorForSearch" showDetail="true"
										   id="stuff74"
								showSummary="true" errorStyle="color: red" />
							<h:commandButton id="cb7" value="Query"
								actionListener="#{MGBean.toggleFaultSearchByAuthor}" />
						</h:panelGroup>
					</h:panelGroup>
				</h:panelGrid>
			</h:form>

			<h:form id="SelectFaultDBEntryForm"
				rendered="#{MGBean.renderAddFaultFromDBForm}">
				<h:dataTable value="#{MGBean.myFaultDBEntryList}" var="myentry1"
  				     id="erd15"
					binding="#{MGBean.myFaultDataTable}">

					<h:column  id="erd16">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>FaultName</b>" />
						</f:facet>
						<h:selectOneRadio layout="pageDirection"
						     id="erd17"
							valueChangeListener="#{MGBean.handleFaultsRadioValueChange}"
							onchange="dataTableSelectOneRadio(this)"
							onclick="dataTableSelectOneRadio(this)">
							<f:selectItems value="#{myentry1.faultName}" />
						</h:selectOneRadio>
					</h:column>

					<h:column   id="erd18">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>SegmentName</b>" />
						</f:facet>
						<h:outputText   id="erd19" value="#{myentry1.faultSegmentName}" />
					</h:column>

					<h:column   id="ldop1">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Author1</b>" />
						</f:facet>
						<h:outputText value="#{myentry1.faultAuthor}"   id="ldop2" />
					</h:column>

					<h:column   id="ldop3">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Segment Coordinates</b>" />
						</f:facet>
						<h:outputText   id="ldop4" value="#{myentry1.faultSegmentCoordinates}" />
					</h:column>
					<h:column   id="ldop5">
						<f:facet name="header">
							<h:outputText escape="false" value="<b>Action</b>" />
						</f:facet>
						<h:commandLink   id="ldop6" actionListener="#{MGBean.handleFaultEntryEdit}">
							<h:outputText value="Get"   id="ldop7"/>
						</h:commandLink>
					</h:column>
				</h:dataTable>
				<h:commandButton id="SelectFaultDBEntry" value="SelectFaultDBEntry"
					actionListener="#{MGBean.toggleSelectFaultDBEntry}" />
			</h:form>

		</h:panelGroup>
	</h:panelGrid>

  <p/>

	<h:panelGroup 
	   id="ldop8"
		rendered="#{!empty MGBean.myFaultEntryForProjectList 
							|| !empty MGBean.myLayerEntryForProjectList}">

	<h:outputText   id="ldop9" styleClass="header2" value="Current Project Components"/>

	<h:panelGrid id="ProjectComponentList" columns="3" border="1"
			columnClasses="alignTop, alignTop, alignTop">

		<h:panelGroup   id="ldop10">
			<h:form id="UpdateSelectFaultsForm"
				rendered="#{!empty MGBean.myFaultEntryForProjectList}">
				<h:panelGrid columns="1" border="1"  id="ldop11">
					<h:panelGroup  id="ldop12">
						<h:panelGrid columns="1"  id="ldop13">
							<h:outputText   id="ldop14" escape="false" value="<b>Faults</b>"/>
						</h:panelGrid>

						<h:dataTable border="1"  id="ldop15"
							value="#{MGBean.myFaultEntryForProjectList}" var="myentry3">
							<h:column  id="ldop16">
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Name</b>">
                           </h:outputText>
								</f:facet>
								<h:outputText   id="ldop17" value="#{myentry3.faultName}" />
							</h:column>
							<h:column  id="ldop18">
								<f:facet name="header">
									<h:outputText   id="ldop19" escape="false" value="<b>View</b>" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry3.view}"
								     id="cjd1"
									onchange="selectOne(this.form,this)"
									onclick="selectOne(this.form,this)" />

							</h:column>
							<h:column  id="cjd2">
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Remove</b>" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry3.delete}"
								     id="cjd3"
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
				<h:panelGrid id="updateSelectpg" columns="1" border="1">
					<h:panelGroup id="pg21">
						<h:panelGrid id="pg22" columns="1">
							<h:outputText   id="cjd5" escape="false" value="<b>Layers</b>">
                     </h:outputText>
						</h:panelGrid>

						<h:dataTable border="1"
										 id="pg23"
							value="#{MGBean.myLayerEntryForProjectList}" var="myentry4">
							<h:column  id="cjd6">
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Name</b>" />
								</f:facet>
								<h:outputText value="#{myentry4.layerName}" />
							</h:column>
							<h:column  id="cjd7">
								<f:facet name="header">
									<h:outputText escape="false" value="<b>View</b>" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry4.view}"
										id="pg24"
									onchange="selectOne(this.form,this)"
									onclick="selectOne(this.form,this)" />

							</h:column>
							<h:column  id="cjd8">
								<f:facet name="header">
									<h:outputText escape="false" value="<b>Remove</b>" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{myentry4.delete}"
								   id="pg25"
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

		<h:form id="PlottingTools">
			<h:panelGroup 				
			   id="pg26"
				rendered="#{!empty MGBean.myFaultEntryForProjectList 
							|| !empty MGBean.myLayerEntryForProjectList}">
				<h:outputFormat id="pg27" escape="false" value="<b>Plotting Tools</b><br><br>" />
				<h:panelGrid id="pg28" columns="1" border="0">
					<h:outputFormat id="pg29" escape="false"
						value="<br>Use Java Web Start mesh viewer by clicking the link below." />

					<h:outputLink id="link1" value="#{facesContext.externalContext.requestContextPath}/painter.jsp">
						<f:param name="layers" value="#{MGBean.myLayersParamForJnlp}" />
						<f:param name="faults" value="#{MGBean.myFaultsParamForJnlp}" />
						<h:outputText value="Web Start Plotter" />
					</h:outputLink>

				</h:panelGrid>
			</h:panelGroup>
		</h:form>

		<h:form id="MeshGen">
			<h:panelGrid columns="1" 
			   id="pg30" 
				rendered="#{!empty MGBean.myFaultEntryForProjectList 
							&& !empty MGBean.myLayerEntryForProjectList}"
				footerClass="subtitle"
				headerClass="subtitlebig" styleClass="medium"
				columnClasses="subtitle,medium">

				<h:outputFormat escape="false"
				     id="pg31" 
					value="<b>Create Initial Mesh</b><br><br>" />
				<h:outputFormat escape="false"
                 id="pg32" 
					value="Click the button below to generate a mesh for the geometry.<br><br>" />
			  
           <h:outputText  id="pg33" value="Mesh Refinement Level"/>
			  <h:selectOneListbox title="Mesh Refinement Level"
			  		      size="1"
			  		      id="refinementr43c" 
					      required="true" value="#{MGBean.meshResolution}">
			     <f:selectItem id="rare" itemValue="rare"/>
			     <f:selectItem id="orig" itemValue="original"/>
			     <f:selectItem id="extra" itemValue="extra"/>
		 	  </h:selectOneListbox>
					
           <h:outputText  id="pg33ere" value="Mesh Refinement Host"/>
			  <h:selectOneListbox title="Mesh Refinement Host"
			  		      size="1"
			  		      id="refhost3094" 
					      required="true" value="#{MGBean.gridRefinerHost}">
			     <f:selectItems id="leihrc93" value="#{MGBean.gridHostList}"/>
		 	  </h:selectOneListbox>

				<h:panelGrid   id="pg34" columns="2" border="0">
					<h:commandButton id="meshgencb" value="Generate Mesh"
						action="#{MGBean.runNonBlockingMeshGenerartorJSF}" />
				</h:panelGrid>

			</h:panelGrid>
		</h:form>
	</h:panelGrid>

   </h:panelGroup>

	<h:form>
		<hr />
		<h:commandLink id="goback" action="MG-back">
			<h:outputText value="#{MGBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>

</f:view>

</body>
</html>
