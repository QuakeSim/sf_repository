<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>

<title>Select Project Meshes</title>
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
	<h:form id="f1">
		<h:commandLink id="blaheoru203" action="disloc-this">
			<h:outputText id="blaheoru129" value="Refresh Page" />
		</h:commandLink>
	</h:form>

	<h:form id="f2">
		<h:panelGrid id="pgridmain" columns="1" border="0">
			<h:outputText id="header" escape="false" value="<h2>Archived Results</h2>" />
			<h:outputText id="dosomework" escape="false"
				value="You have the following archived disloc runs. Click the link to download the desired file to your desk top. To save directly to your desktop, click your mouse's right button over the link and select<br>" />

  				<h:dataTable id="DislocOutputPanel" 
								rendered="#{!(empty DislocBean2.myArchivedDislocResultsList)}" 
								value="#{DislocBean2.myArchivedDislocResultsList}" 
								 binding="#{DislocBean2.myProjectSummaryDataTable}"
									
								 var="summaryBean"
								 border="1">
					<h:column>
					    <f:facet name="header">
					    <h:outputText  id="blaheoru0" value="Project Name"/>
						 </f:facet>
				       <h:outputText  id="blaheoru1" value="#{summaryBean.projectName}"/>
					</h:column>

					<h:column>
					    <f:facet name="header">
					    <h:outputText id="blaheoru3" value="Creation Date"/>
						 </f:facet>
				       <h:outputText id="blaheoru2" value="#{summaryBean.creationDate}"/>
					</h:column>

					<h:column>
					    <f:facet name="header">
					    <h:outputText id="blaheoru4" value="Job UID Stamp"/>
						 </f:facet>
				       <h:outputText  id="blaheoru5" value="#{summaryBean.jobUIDStamp}"/>
					</h:column>
	
					<h:column>
					<f:facet name="header">	 
						    <h:outputText  id="blaheoru6" value="Input File"/>
				   </f:facet>
				       <h:outputLink id="blaheoru7" value="#{summaryBean.resultsBean.inputFileUrl}" target="_blank">
						    <h:outputText id="blaheoru8" value="Input"/>
						 </h:outputLink>
					</h:column>

					<h:column>
					<f:facet name="header">
						    <h:outputText id="blaheoru9" value="Output File"/>
				   </f:facet>
				       <h:outputLink id="blaheoru10" value="#{summaryBean.resultsBean.outputFileUrl}" target="_blank">
						    <h:outputText id="blaheoru11" value="Output"/>
						 </h:outputLink>
					</h:column>

				<h:column>
					<f:facet name="header">
						<h:outputText  id="blaheoru15" escape="false" value="<b>Kml file</b>" />
					</f:facet>
						<h:panelGroup id="pgkml">
						<h:outputLink id="linkdre5" value="#{summaryBean.kmlurl}" target="_blank">
							<h:outputText id="kdjfjk" value="[<b>Download</b>]" escape="false" />
						</h:outputLink>
						<h:outputText id="blaheoru16" escape="false" value="<b> </b>" />
						<h:outputLink id="linkdre6" value="http://maps.google.com/maps?q=#{summaryBean.kmlurl}" target="_blank">
							<h:outputText id="blaheoru17" value="[<font size=1px>View In Google map</font>]" escape="false" />
						</h:outputLink>
						</h:panelGroup>
				</h:column>					

				<h:column>
					<f:facet name="header">
						<h:outputText id="blaheoru18" escape="false" value="<b>Delete</b>" />
					</f:facet>
						<h:panelGroup id="pgdelete">
							<h:commandButton id="DeleteSummary" value="Delete"
												  actionListener="#{DislocBean2.toggleDeleteProjectSummary}"/>
						</h:panelGroup>

				</h:column>					
					<h:column>
					<f:facet name="header">
						    <h:outputText id="blaheoru12" value="Stdout"/>
				   </f:facet>
				       <h:outputLink id="blaheoru13" value="#{summaryBean.resultsBean.stdoutUrl}" target="_blank">
						    <h:outputText id="blaheoru14" value="Stdout"/>
						 </h:outputLink>
					</h:column>
					
			</h:dataTable>
      </h:panelGrid>
	</h:form>

		<hr />
	<h:form id="f3">

		<h:commandLink id="blaheoru20" action="disloc-this">
			<h:outputText id="blaheoru19" value="Refresh Page" />
		</h:commandLink>
	</h:form>

</f:view>
</body>
</html>
