<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://myfaces.apache.org/extensions" prefix="x"%>
<html>
<head>

<title>Plot Data Menu</title>
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

	<h:panelGrid columns="1" border="0">
		<h:outputText escape="false" value="<h3>Plot Data Menu</h3><br>" />
		<h:outputText escape="false"
			value="You have the following archived sessions. Click the link to download the desired contour plot.<br>" />

		<h:dataTable border="1" value="#{MGBean.myarchivedMeshTableEntryList}"
			var="myentry3">
			<h:column>
				<f:facet name="header">
					<h:outputText escape="false" value="<b>Project Name</b>" />
				</f:facet>
				<h:outputText value="#{myentry3.projectName}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText escape="false" value="<b>Creation Date</b>" />
				</f:facet>
				<h:outputText value="#{myentry3.creationDate}" />

			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText escape="false" value="<b>	Storage Host</b>" />
				</f:facet>
				<h:outputText value="#{myentry3.meshHost}" />
			</h:column>
			<h:column>
				<f:facet name="header">
					<h:outputText escape="false" value="<b>	Mesh File</b>" />
				</f:facet>
				<h:panelGrid columns="3" border="1">

					<h:form>
						<h:commandLink id="link1" value="postseis"
							action="#{MGBean.ContourPlot}">
							<f:param name="ProjectSelect" value="#{myentry3.projectName}" />
							<f:param name="DataChoice" value="postseis" />
						</h:commandLink>
					</h:form>
					<h:form>
						<h:commandLink id="link2" value="coseis"
							action="#{MGBean.ContourPlot}">
							<f:param name="ProjectSelect" value="#{myentry3.projectName}" />
							<f:param name="DataChoice" value="coseis" />

						</h:commandLink>
					</h:form>
					<h:form>
						<h:commandLink id="link3" value="totaldispl"
							action="#{MGBean.ContourPlot}">
							<f:param name="ProjectSelect" value="#{myentry3.projectName}" />
							<f:param name="DataChoice" value="totaldispl" />

						</h:commandLink>
					</h:form>


				</h:panelGrid>
			</h:column>


		</h:dataTable>
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
