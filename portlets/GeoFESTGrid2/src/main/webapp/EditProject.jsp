<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<html>
  
  <head>
	 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here"
				type="text/javascript"></script> 
	 <link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/styles/quakesim_style.css"/>
	 <link rel="stylesheet" type="text/css"
			 href='<%= request.getContextPath() + "/stylesheet.css" %>'>
	 
<title>Edit Project</title>
</head>
<body onload="myInit() onunload="GUnload()">
<script type="text/javascript">
function myInit() {
  	$("#browser").treeview({
		animated:"normal",
		persist: "cookie"
		});
 }


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
  <f:verbatim>
	 <fieldset><legend class="portlet-form-label"> Project Dashboard </legend>
  </f:verbatim>

		<h:messages id="geoFESTMessagesDynamicFault" 
				  showDetail="true"
				  showSummary="true"
				  errorStyle="color: red"/>
		
		<f:verbatim><p>Create your geometry out of layers and faults.</p></f:verbatim>
		
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
			<%@include file="GeoFESTLayerPanelFrame.jsp" %>
			<%@include file="GeoFESTFaultPanelFrame.jsp" %>
			<%@include file="GeoFESTFaultDBPanelFrame.jsp" %>
			<%@include file="GeoFESTFaultSelectionFrame.jsp" %>
			<%@include file="GeoFESTFaultSearchByNameFrame.jsp" %>
			<%@include file="GeoFESTFaultSearchByLatLonFrame.jsp" %>
			<%@include file="GeoFESTFaultSearchByAuthorFrame.jsp" %>
			<%@include file="GeoFESTFaultSelectDBEntry.jsp" %>
		</h:panelGroup>
	</h:panelGrid>
	<f:verbatim></fieldset></f:verbatim>

	<f:verbatim>
	  <fieldset><legend class="portlet-form-label"> Project Components </legend>
	</f:verbatim>
	
	<h:panelGroup 
	   id="ldop8"
		rendered="#{!empty MGBean.myFaultEntryForProjectList 
							|| !empty MGBean.myLayerEntryForProjectList}">

	<h:panelGrid id="ProjectComponentList" columns="3" border="1"
			columnClasses="alignTop, alignTop, alignTop">

	<%@include file="GeoFESTProjectComponents.jsp" %>
	<%@include file="GeoFESTPlottingTools.jsp" %>
	<%@include file="GeoFESTMeshGenExec.jsp" %>

	</h:panelGrid>
   </h:panelGroup>

	<f:verbatim></fieldset></f:verbatim>

	<h:form>
		<hr />
		<h:commandLink id="goback" action="MG-back">
			<h:outputText value="#{MGBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>

</f:view>

</body>
</html>
