<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>

<title>Select Project Files</title>
</head>
<body>

<script language="JavaScript">
//<![CDATA[ 
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
//]]>
</script>
<f:view>
	<h:form>
		<h:panelGrid columns="1" border="0">
			<h3><h:outputText escape="false" value="Archived Data" /></h3><br>
			<h:outputText escape="false"
							  value="You don't have any archived results."
							  rendered="#{empty SimplexBean.myarchivedFileEntryList}"/>

			<h:outputText escape="false"
  						 	 rendered="#{!(empty SimplexBean.myarchivedFileEntryList)}"
							 value="You have the following archived data files. Select the radio button to create GMT plots. Download the input and output files for more information on the data." /><br>


			<h:dataTable border="1"
  						 	 rendered="#{!(empty SimplexBean.myarchivedFileEntryList)}"
						 	 value="#{SimplexBean.myarchivedFileEntryList}" var="myentry3">
				<h:column>
					<f:facet name="header">
						<b><h:outputText escape="false" value="Project Name" /></b>
					</f:facet>
					<h:outputText value="#{myentry3.projectName}" />
				</h:column>
				<h:column>
					<f:facet name="header">
						<b><h:outputText escape="false" value="Archived DATA File" /></b>
					</f:facet>
					<h:panelGrid columns="4" border="1">
						<h:outputLink id="link1" value="#{myentry3.inputUrl}" target="_blank">
							<h:outputText value="input" />
						</h:outputLink>
						<h:outputLink id="link2" value="#{myentry3.logUrl}" target="_blank">
							<h:outputText value="stdout" />
						</h:outputLink>
						<h:outputLink id="link3" value="#{myentry3.outputUrl}" target="_blank">
							<h:outputText value="output" />
						</h:outputLink>
						<h:outputLink id="link4" value="#{myentry3.faultUrl}" target="_blank">
							<h:outputText value="fault" />
						</h:outputLink>
					</h:panelGrid>
				</h:column>
				<h:column>
					<f:facet name="header">
						<b><h:outputText escape="false" value="Kml file" /></b>
					</f:facet>
						<h:panelGroup>
						<h:outputLink id="link5" value="#{myentry3.kmlUrls[0]}" target="_blank">
							<b><h:outputText value="[Download]" escape="false" /></b>
						</h:outputLink>
						<b><h:outputText escape="false" value=" " /></b>
						<h:outputLink id="link6" value="http://maps.google.com/maps?q=#{myentry3.kmlUrls[0]}" target="_blank">
							<font size=1px><h:outputText value="[View In Google map]" escape="false" /></font>
						</h:outputLink>
						</h:panelGroup>
				</h:column>
				<h:column>
					<f:facet name="header">
						<b><h:outputText escape="false" value="Select to Plot" /></b>
					</f:facet>
					<h:selectBooleanCheckbox value="#{myentry3.view}"
						onchange="selectOne(this.form,this)"
						onclick="selectOne(this.form,this)" />

				</h:column>


			</h:dataTable>
		</h:panelGrid>
		<h:commandButton id="gmt4plot" value="[GMT Plot]"
  						 	  rendered="#{!(empty SimplexBean.myarchivedFileEntryList)}"
							  action="#{SimplexBean.toggleGMTPlot}" />
		<h:commandButton id="gmt4map" value="[Map plot]"
  						 	  rendered="#{!(empty SimplexBean.myarchivedFileEntryList)}"
							  action="#{SimplexBean.toggleMakeMap}" />
		<h:commandButton id="loadkml" value="[Multi Kml Loader]"
  						 	  rendered="#{!(empty SimplexBean.myarchivedFileEntryList)}"
							  action="#{SimplexBean.toggleViewKml}" />
	</h:form>

	<h:form>
		<hr />
		<h:commandLink action="Simplex2-back">
			<h:outputText value="#{SimplexBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>
</f:view>
</body>
</html>
