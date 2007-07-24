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
			<h:outputText escape="false" value="<h3>Archived Results</h3><br>" />
			<h:outputText escape="false"
				value="You have the following archived disloc runs. Click the link to download the desired file to your desk top. To save directly to your desktop, click your mouse's right button over the link and select<br>" />

  				<h:dataTable value="#{DislocBean.myArchivedDislocResultsList}" 
								 var="summaryBean" id="DislocOutputPanel" border="1">
					<h:column>
					    <f:facet name="header">
					    <h:outputText value="Project Name"/>
						 </f:facet>
				       <h:outputText value="#{summaryBean.projectName}"/>
					</h:column>

					<h:column>
					    <f:facet name="header">
					    <h:outputText value="Creation Date"/>
						 </f:facet>
				       <h:outputText value="#{summaryBean.creationDate}"/>
					</h:column>

					<h:column>
					    <f:facet name="header">
					    <h:outputText value="Job UID Stamp"/>
						 </f:facet>
				       <h:outputText value="#{summaryBean.jobUIDStamp}"/>
					</h:column>

	
					<h:column>
					<f:facet name="header">	 
						    <h:outputText value="Input File"/>
				   </f:facet>
				       <h:outputLink value="#{summaryBean.resultsBean.inputFileUrl}" target="_blank">
						    <h:outputText value="Input"/>
						 </h:outputLink>
					</h:column>

	
					<h:column>
					<f:facet name="header">
						    <h:outputText value="Output File"/>
				   </f:facet>
				       <h:outputLink value="#{summaryBean.resultsBean.outputFileUrl}" target="_blank">
						    <h:outputText value="Output"/>
						 </h:outputLink>
					</h:column>


					<h:column>
					<f:facet name="header">
						    <h:outputText value="Standard Out and Error"/>
				   </f:facet>
				       <h:outputLink value="#{summaryBean.resultsBean.stdoutUrl}" target="_blank">
						    <h:outputText value="Stdout"/>
						 </h:outputLink>
					</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText escape="false" value="<b>Kml file</b>" />
					</f:facet>
						<h:panelGroup>
						<h:outputLink id="link5" value="#{summaryBean.kmlurl}" target="_blank">
							<h:outputText value="[<b>Download</b>]" escape="false" />
						</h:outputLink>
						<h:outputText escape="false" value="<b> </b>" />
						<h:outputLink id="link6" value="http://maps.google.com/maps?q=#{summaryBean.kmlurl}" target="_blank">
							<h:outputText value="[<font size=1px>View In Google map</font>]" escape="false" />
						</h:outputLink>
						</h:panelGroup>
				</h:column>					
					
			</h:dataTable>
      </h:panelGrid>
	</h:form>

	<h:form>
		<hr />
		<h:commandLink action="disloc-this">
			<h:outputText value="Refresh Page" />
		</h:commandLink>
	</h:form>

</f:view>
</body>
</html>
