<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

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

<title>Select Project Files</title>
</head>
<body>


<script type="text/javascript">

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
	<h:form id="theFormid">
		<h:panelGrid id="pgidsimplex" columns="1" border="0">
			<h:outputText escape="false" styleClass="header2" value="Archived Data" />
			<h:outputText id="message1" escape="false"
							  rendered="#{(empty SimplexBean.myarchivedFileEntryList)}"							  
							  value="You don't have any archived results yet." />

			<h:outputText id="message1" escape="false"
							  rendered="#{!(empty SimplexBean.myarchivedFileEntryList)}"							  
							  value="You have the following archived data files. Download the input and output files for more information on the data." /><br>
			
			<h:dataTable border="1"
							 id="SimplexOutputPanel"
   						 rendered="#{!(empty SimplexBean.myarchivedFileEntryList)}"										 
							 binding="#{SimplexBean.myArchiveDataTable}" 							 
							 value="#{SimplexBean.myarchivedFileEntryList}"
							 var="myentry3">
				<h:column>
					<f:facet name="header">
						<b><h:outputText  id="kjb16" escape="false" value="Project Name" /></b>
					</f:facet>
					<h:outputText id="kjb15" value="#{myentry3.projectName}" />
				</h:column>

				<h:column>
					<f:facet name="header">
						<b><h:outputText  id="kjb1623" escape="false" value="Creation Date"/></b>
					</f:facet>
					<h:outputText id="kjb15123c" value="#{myentry3.creationDate}" />
				</h:column>

				<h:column>
					<f:facet name="header">
						<b><h:outputText  id="kjb14" escape="false" value="Archived Data File" /></b>
					</f:facet>
					<h:panelGrid columns="4" border="1">
						<h:outputLink id="link1" value="#{myentry3.inputUrl}" target="_blank">
							<h:outputText id="kjb13" value="input" />
						</h:outputLink>
						<h:outputLink id="link2" value="#{myentry3.logUrl}" target="_blank">
							<h:outputText id="kjb12" value="stdout" />
						</h:outputLink>
						<h:outputLink id="link3" value="#{myentry3.outputUrl}" target="_blank">
							<h:outputText  id="kjb11" value="output" />
						</h:outputLink>
						<h:outputLink id="link4" value="#{myentry3.faultUrl}" target="_blank">
							<h:outputText  id="kjb10" value="fault" />
						</h:outputLink>
					</h:panelGrid>
				</h:column>

				<h:column>
					<f:facet name="header">
						<b><h:outputText  id="kjb3" escape="false" value="Kml file" /></b>
					</f:facet>
						<h:panelGroup id="kjb4">
						<h:outputLink id="link5" value="#{myentry3.kmlUrls[0]}" target="_blank">
							<b><h:outputText  id="kjb6" value="[Download]" escape="false" /></b>
						</h:outputLink>
						<b><h:outputText  id="kjb2" escape="false" value=" " /></b>
						<h:outputLink id="link6" value="http://maps.google.com/maps?q=#{myentry3.kmlUrls[0]}" target="_blank">
							<font size="1px"><h:outputText id="kjb1" value="[View In Google map]" escape="false" /></font>
						</h:outputLink>
						</h:panelGroup>
				</h:column>

				<h:column>
					<f:facet name="header">
						<b><h:outputText id="blah18" escape="false" value="Delete"/></b>
					</f:facet>
						<h:panelGroup id="deletepanel">
							<h:commandButton id="DeleteSummary" value="Delete"
												  actionListener="#{SimplexBean.toggleDeleteProjectSummary}"/>
						</h:panelGroup>
				</h:column>					


			</h:dataTable>
		</h:panelGrid>

	</h:form>

	<h:form>
		<hr />
		<h:commandLink action="">
			<h:outputText value="Refresh Page" />
		</h:commandLink>
	</h:form>
</f:view>
</body>
</html>
