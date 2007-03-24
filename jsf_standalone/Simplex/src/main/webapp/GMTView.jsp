<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<html>
<head>

<title>GMT View page</title>
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
			<h:panelGroup>
				<h:outputText escape="false" value="Click " />
				<h:outputLink id="link1" value="#{SimplexBean.gmtPlotPdfUrl}">
					<h:outputText value="here" />
				</h:outputLink>
				<h:outputText escape="false" value=" to open the image externally." />
			</h:panelGroup>

			<h:form id="SetParamforPlotform">
				<h:panelGrid id="Setparam" columns="3" footerClass="subtitle"
					headerClass="subtitlebig" styleClass="medium"
					columnClasses="subtitle,medium">

					<f:facet name="header">
						<h:outputFormat id="output3" escape="false"
							value="<br>Use this form to modify your plot. Click <b>Replot</b> with no modified fields to restore default values." />
					</f:facet>

					<h:outputText value="Plot Area:" />
					<h:panelGroup>
						<h:inputText id="area_prop"
							value="#{SimplexBean.currentGMTViewForm.area_prop}"
							required="true" />
						<h:message for="area_prop" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>
					<h:outputText value="Use decimal degrees in the format W/E/S/N" />

					<h:outputText value="Plot Scale:" />
					<h:panelGroup>
						<h:inputText id="scale_prop"
							value="#{SimplexBean.currentGMTViewForm.scale_prop}"
							required="true" />
						<h:message for="scale_prop" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>
					<h:outputText value="Use positive integer or float." />

					<h:outputText value="Map Ticks:" />
					<h:panelGroup>
						<h:inputText id="mapticks_prop"
							value="#{SimplexBean.currentGMTViewForm.mapticks_prop}"
							required="true" />
						<h:message for="mapticks_prop" showDetail="true"
							showSummary="true" errorStyle="color: red" />
					</h:panelGroup>
					<h:outputText value="Use positive integer or float for map spacing in degrees." />


					<h:outputText value="Plot Causative Faults:" />
					<h:panelGroup>
					<h:selectBooleanCheckbox  
                      value="#{SimplexBean.currentGMTViewForm.bplot_causative}" >
 					</h:selectBooleanCheckbox>
					</h:panelGroup>
					<h:outputText value="check box for plot option" />


					<h:outputText value="Plot Background Faults:" />
					<h:panelGroup>
					<h:selectBooleanCheckbox 
                      value="#{SimplexBean.currentGMTViewForm.bplot_background}" >
  					</h:selectBooleanCheckbox>
					</h:panelGroup>
					<h:outputText value="check box for plot option" />


					<h:outputText value="Plot Observed  Displacements:" />
					<h:panelGroup>
					<h:selectBooleanCheckbox 
                      value="#{SimplexBean.currentGMTViewForm.bplot_obs}" >
  					</h:selectBooleanCheckbox>
					</h:panelGroup>
					<h:outputText value="check box for plot option" />

					<h:outputText value="Plot Calculated  Displacements:" />
					<h:panelGroup>
					<h:selectBooleanCheckbox 
                      value="#{SimplexBean.currentGMTViewForm.bplot_calc}" >
 					</h:selectBooleanCheckbox>
					</h:panelGroup>
					<h:outputText value="check box for plot option" />

					<h:outputText value="Plot Residual Displacements:" />
					<h:panelGroup>
					<h:selectBooleanCheckbox 
                      value="#{SimplexBean.currentGMTViewForm.bplot_resid}" >
 					</h:selectBooleanCheckbox>
					</h:panelGroup>
					<h:outputText value="check box for plot option" />

					<h:outputText value="Vector Magnitude:" />
					<h:panelGroup>
						<h:inputText id="vectormag_prop"
							value="#{SimplexBean.currentGMTViewForm.vectormag_prop}"
							required="true" />
						<h:message for="vectormag_prop" showDetail="true" showSummary="true"
							errorStyle="color: red" />
					</h:panelGroup>
					<h:outputText value="Use positive integer or float." />


					<h:commandButton id="ReplotGMT" value="Click here to replot"
						actionListener="#{SimplexBean.toggleReplotGMT}" />

				</h:panelGrid>
			</h:form>

			<h:panelGrid columns="1" border="0">
				<f:verbatim>
					<iframe src="#{SimplexBean.gmtPlotPdfUrl}" width=500 height=500></iframe>
				</f:verbatim>
			</h:panelGrid>
		</h:panelGrid>

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
