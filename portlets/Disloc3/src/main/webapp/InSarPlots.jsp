<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>

<title>InSAR Plots</title>
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
	<h:form id="insarf1">
		<h:commandLink id="insarblaheoru203" action="disloc-this">
			<h:outputText id="insarblaheoru129" value="Refresh Page" />
		</h:commandLink>
	</h:form>

	<h:form id="insarf2">
	
					    <h:outputText id="insarblaheoru3" value="Elevation"/>
				       <h:inputText id="insarblaheoru2" value="#{DislocBean2.elevation}"/>

					    <h:outputText id="insarblaheoru3" value="Azimuth"/>
				       <h:inputText id="insarblaheoru2" value="#{DislocBean.azimuth}"/>

					    <h:outputText id="insarblaheoru3" value="Freq (GHz)"/>
				       <h:inputText id="insarblaheoru2" value="#{DislocBean.frequency}"/>

		<h:panelGrid id="insarpgridmain" columns="1" border="0">
			<h:outputText id="insarheader" escape="false" value="<h2>Archived Results</h2>" />
			<h:outputText id="insardosomework" escape="false"
				value="You have the following archived disloc runs. Click the link to download the desired file to your desk top. To save directly to your desktop, click your mouse's right button over the link and select<br>" />

  				<h:dataTable id="insarDislocOutputPanel3" var="summaryBean"
								 value="#{DislocBean2.myArchivedDislocResultsList}" 
								 binding="#{DislocBean2.myProjectSummaryDataTable}"
								 border="1">
					<h:column>
					    <f:facet name="header">
					    <h:outputText  id="insarblaheoru0" value="Project Name"/>
						 </f:facet>
				       <h:outputText  id="insarblaheoru1" value="#{summaryBean.projectName}"/>
					</h:column>

					<h:column>
					    <f:facet name="header">
					    <h:outputText id="insarblaheoru3" value="Creation Date"/>
						 </f:facet>
				       <h:outputText id="insarblaheoru2" value="#{summaryBean.creationDate}"/>
					</h:column>

					
					<h:column>
					 <f:facet name="header">
								 <h:outputText  id="insarblaheoru15" 
								 					 escape="false" value="<b>InSAR Kml file</b>" />
					 </f:facet>
					 <h:panelGroup id="insarpgkml">
					    <h:commandLink id="insarlinkdre5" action="{DislocBean2.toggleInsarKmlGen}"
						 					 target="_blank">
							<h:outputText id="insarkdjfjk" value="[<b>Download</b>]" escape="false" />
						 </h:outputLink>
						<h:outputText id="insarblaheoru16" escape="false" value="<b> </b>" />
						<h:outputLink id="insarlinkdre6" value="http://maps.google.com/maps?q=#{summaryBean.kmlurl}" target="_blank">
							<h:outputText id="insarblaheoru17" value="[<font size=1px>View In Google map</font>]" escape="false" />
						</h:outputLink>
						</h:panelGroup>
				</h:column>					
					
			</h:dataTable>
      </h:panelGrid>
	</h:form>

		<hr />
	<h:form id="insarf3">

		<h:commandLink id="insarblaheoru20" action="disloc-this">
			<h:outputText id="insarblaheoru19" value="Refresh Page" />
		</h:commandLink>
	</h:form>

</f:view>
</body>
</html>
