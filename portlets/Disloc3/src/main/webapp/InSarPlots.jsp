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
		<h:panelGrid id="insarpgridmain" columns="1" border="0">
			<h:outputText id="insarheader" escape="false" value="<h2>Archived Results</h2>" />
			<h:outputText id="insardosomework" escape="false"
				value="You have the following disloc InSAR plots. Click the link to download the desired file to your desk top. To save directly to your desktop, click your mouse's right button over the link and select<br>" />

  				<h:dataTable id="insarDislocOutputPanel3" var="insarParamsBean"
								 value="#{DislocBean2.myInsarParamsList}" 
								 binding="#{DislocBean2.myInsarDataTable}"
								 border="1">
					<h:column>
					    <f:facet name="header">
					    <h:outputText  id="insarblaheoru0" value="Project Name"/>
						 </f:facet>
				       <h:outputText  id="insarblaheoru1" value="#{insarParamsBean.projectName}"/>
					</h:column>

					<h:column>
					    <f:facet name="header">
					    <h:outputText id="insarblaheoru3" value="Creation Date"/>
						 </f:facet>
				       <h:outputText id="insarblaheoru2" value="#{insarParamsBean.creationDate}"/>
					</h:column>

					<h:column>
					    <f:facet name="header">
					    <h:outputText  id="insarblahddfeoru0" value="Elevation (Deg)"/>
						 </f:facet>
				       <h:inputText  id="insarblaheoereru1" value="#{insarParamsBean.elevation}"/>
					</h:column>
					<h:column>
					    <f:facet name="header">
					    <h:outputText  id="insarblahe1oruere0" value="Azimuth (Deg)"/>
						 </f:facet>
				       <h:inputText  id="ereinsarblaheoru1" value="#{insarParamsBean.azimuth}"/>
					</h:column>
					<h:column>
					    <f:facet name="header">
					    <h:outputText  id="insdferearblaheoru0" value="Frequency (GHz)"/>
						 </f:facet>
				       <h:inputText  id="insarblaerereheoru1" value="#{insarParamsBean.frequency}"/>
					</h:column>
					
					<h:column>
					 <f:facet name="header">
								 <h:outputText  id="insarblaheoru15" 
								 					 escape="false" value="<b>InSAR Kml file</b>" />
					 </f:facet>
						<h:panelGroup id="insarpgkml">
						<h:outputLink id="insarlinkdre5" value="#{insarParamsBean.insarKmlUrl}" target="_blank">
							<h:outputText id="insarkdjfjk" value="[<b>Download</b>]" escape="false" />
						</h:outputLink>
						<h:outputText id="insarblaheoru16" escape="false" value="<b> </b>" />
						<h:outputLink id="insarlinkdre6" value="http://maps.google.com/maps?q=#{insarParamsBean.insarKmlUrl }" target="_blank">
							<h:outputText id="insarblaheoru17" value="[<font size=1px>View In Google map</font>]" escape="false" />
						</h:outputLink>
						</h:panelGroup>
				</h:column>					
				<h:column>
					<f:facet name="header">
						<h:outputText id="insarblaheoru18" escape="false" value="<b>Replot</b>" />
					</f:facet>
						<h:panelGroup id="insarpgreplot">
							<h:commandButton id="ReplotInsarldk" value="Replot"
												  action="#{DislocBean2.toggleReplotInsar}"/>
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
