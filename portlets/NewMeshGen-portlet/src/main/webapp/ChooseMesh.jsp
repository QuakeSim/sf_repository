<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

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
	<h:panelGrid rendered="#{empty MGBean.myArchivedMeshRunList}">
	<h:outputText styleClass="header2" value="Mesh Refinement"/>
	<h:outputText value="You don't have any archived projects."/>
   </h:panelGrid>

	<h:panelGrid rendered="#{!empty MGBean.myArchivedMeshRunList}">
	<h:outputText styleClass="header2" value="Mesh Refinement"/>
	<h:outputText value="Here are your archived projects."/>
	<h:form>

  				<h:dataTable value="#{MGBean.myArchivedMeshRunList}"					  
								 binding="#{MGBean.myMeshDataTable}"
								 var="mrb" id="MeshOutputPanel" border="1">
					<h:column>
					    <f:facet name="header">
					    <h:outputText value="Project Name"/>
						 </f:facet>
				       <h:outputText value="#{mrb.meshRunBean.projectName}"/>
					</h:column>

					<h:column>
					    <f:facet name="header">
					    <h:outputText value="Creation Date"/>
						 </f:facet>
				       <h:outputText value="#{mrb.creationDate}"/>
					</h:column>

					<h:column>
					    <f:facet name="header">
					    <h:outputText value="Job UID Stamp"/>
						 </f:facet>
				       <h:outputText value="#{mrb.meshRunBean.jobUIDStamp}"/>
					</h:column>

					<h:column>
					<f:facet name="header">
						    <h:outputText value="Index File"/>
				   </f:facet>
				       <h:outputLink value="#{mrb.meshRunBean.indexUrl}" target="_blank">
						    <h:outputText value="Index File"/>
						 </h:outputLink>
					</h:column>

					<h:column>
					<f:facet name="header">
						    <h:outputText value="Node File"/>
				   </f:facet>
				       <h:outputLink value="#{mrb.meshRunBean.nodeUrl}" target="_blank">
						    <h:outputText value="Node File"/>
						 </h:outputLink>
					</h:column>
					
					<h:column>
					<f:facet name="header">
						    <h:outputText value="Plot Mesh"/>
				   </f:facet>
					<h:outputLink value="#{facesContext.externalContext.requestContextPath}/painter.jsp">
						<f:param name="layers" value="#{mrb.jnlpLayers}" />
						<f:param name="faults" value="#{mrb.jnlpFaults}" />
						<f:param name="plotMesh" value="true" />
						<f:param name="gfHostName" value="#{mrb.geoFESTBaseUrlForJnlp}" />
						<f:param name="projectName" value="#{mrb.projectName}" />
						<f:param name="userName" value="#{mrb.userName}" />
						<f:param name="jobUIDStamp" value="#{mrb.meshRunBean.jobUIDStamp}" />
						<h:outputText value="Plot"/>
					</h:outputLink>
					</h:column>
					
					<h:column>
					<f:facet name="header">
						    <h:outputText value="Run GeoFEST"/>
				   </f:facet>
					<h:commandButton actionListener="#{MGBean.selectMeshForGeoFEST}" 
										  action="MG-geotransparams"
										  value="Select"/>
						
 					</h:column>

			</h:dataTable>
 	</h:form>
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
