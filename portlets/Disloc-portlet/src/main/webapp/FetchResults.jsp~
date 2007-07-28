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
	<h:form>
		<h:panelGrid columns="1" border="0">
			<h:outputText id="header" escape="false" value="<h3>Archived Results</h3><br>" />
			<h:outputText id="dosomework" escape="false"
				value="You have the following archived disloc runs. Click the link to download the desired file to your desk top. To save directly to your desktop, click your mouse's right button over the link and select<br>" />

  				<h:dataTable value="#{DislocBean.myArchivedDislocResultsList}" 
								 binding="#{DislocBean.myProjectSummaryDataTable}"
								 var="summaryBean" id="DislocOutputPanel" border="1">
					<h:column>
					    <f:facet name="header">
					    <h:outputText  id="blah0" value="Project Name"/>
						 </f:facet>
				       <h:outputText  id="blah1" value="#{summaryBean.projectName}"/>
					</h:column>

					<h:column>
					    <f:facet name="header">
					    <h:outputText id="blah3" value="Creation Date"/>
						 </f:facet>
				       <h:outputText id="blah2" value="#{summaryBean.creationDate}"/>
					</h:column>

					<h:column>
					    <f:facet name="header">
					    <h:outputText id="blah4" value="Job UID Stamp"/>
						 </f:facet>
				       <h:outputText  id="blah5" value="#{summaryBean.jobUIDStamp}"/>
					</h:column>
	
					<h:column>
					<f:facet name="header">	 
						    <h:outputText  id="blah6" value="Input File"/>
				   </f:facet>
				       <h:outputLink id="blah7" value="#{summaryBean.resultsBean.inputFileUrl}" target="_blank">
						    <h:outputText id="blah8" value="Input"/>
						 </h:outputLink>
					</h:column>

	
					<h:column>
					<f:facet name="header">
						    <h:outputText id="blah9" value="Output File"/>
				   </f:facet>
				       <h:outputLink id="blah10" value="#{summaryBean.resultsBean.outputFileUrl}" target="_blank">
						    <h:outputText id="blah11" value="Output"/>
						 </h:outputLink>
					</h:column>

					<h:column>
					<f:facet name="header">
						    <h:outputText id="blah12" value="Standard Out and Error"/>
				   </f:facet>
				       <h:outputLink  id="blah13" value="#{summaryBean.resultsBean.stdoutUrl}" target="_blank">
						    <h:outputText id="blah14" value="Stdout"/>
						 </h:outputLink>
					</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText  id="blah15" escape="false" value="<b>Kml file</b>" />
					</f:facet>
						<h:panelGroup>
						<h:outputLink id="link5" value="#{summaryBean.kmlurl}" target="_blank">
							<h:outputText value="[<b>Download</b>]" escape="false" />
						</h:outputLink>
						<h:outputText id="blah16" escape="false" value="<b> </b>" />
						<h:outputLink id="link6" value="http://maps.google.com/maps?q=#{summaryBean.kmlurl}" target="_blank">
							<h:outputText id="blah17" value="[<font size=1px>View In Google map</font>]" escape="false" />
						</h:outputLink>
						</h:panelGroup>
				</h:column>					

				<h:column>
					<f:facet name="header">
						<h:outputText id="blah18" escape="false" value="<b>Delete</b>" />
					</f:facet>
						<h:panelGroup>
							<h:commandButton id="DeleteSummary" value="Delete"
												  actionListener="#{DislocBean.toggleDeleteProjectSummary}"/>
						</h:panelGroup>
				</h:column>					
					
			</h:dataTable>
      </h:panelGrid>
	</h:form>

	<h:form>
		<hr />
		<h:commandLink id="blah20" action="disloc-this">
			<h:outputText id="blah19" value="Refresh Page" />
		</h:commandLink>
	</h:form>

</f:view>
</body>
</html>
