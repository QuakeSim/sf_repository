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
	<h:panelGrid id="cmm1" rendered="#{empty MGBean.myArchivedMeshRunList}">
	<h:outputText id="cmm1o1" styleClass="header2" value="Mesh Refinement"/>
	<h:outputText id="cmm1o2" value="You don't have any archived projects."/>
   </h:panelGrid>

	<h:panelGrid id="cmm2"  rendered="#{!empty MGBean.myArchivedMeshRunList}">
	<h:outputText  id="cmm2o1" styleClass="header2" value="Mesh Refinement"/>
	<h:outputText  id="cmm2o2" value="Here are your archived projects."/>
	<h:form id="cmmf1"> 

  				<h:dataTable id="chmesht" value="#{MGBean.myArchivedMeshRunList}"					  
								 binding="#{MGBean.archivedMeshTable}"
								 var="mrb" id="MeshOutputPanel" border="1">
					<h:column id="chmeshtc1">
					    <f:facet name="header">
					    <h:outputText id="chmeshtc1ot" value="Project Name"/>
						 </f:facet>
					    <h:outputText id="chmeshtc1ot2" value="#{mrb.meshRunBean.projectName}"/>
					</h:column>

					<h:column  id="chmeshtc2">
					    <f:facet name="header">
					    <h:outputText  id="chmeshtc2ot" value="Creation Date"/>
						 </f:facet>
					    <h:outputText  id="chmeshtc2ot1" value="#{mrb.creationDate}"/>
					</h:column>

					<h:column  id="chmeshtc3">
					    <f:facet name="header">
					    <h:outputText  id="chmeshtc3ot" value="Job UID Stamp"/>
						 </f:facet>
				       <h:outputText id="chmeshtc3ot1" value="#{mrb.meshRunBean.jobUIDStamp}"/>
					</h:column>

					<h:column id="chmeshtc4">
					<f:facet name="header">
						    <h:outputText id="chmeshtc3ot" value="Index File"/>
					</f:facet>
					
					      <h:outputLink id="chmeshtc4ol" value="#{mrb.meshRunBean.indexUrl}" target="_blank">
						    <h:outputText id="chmeshtc4olo" value="Index File"/>
					       </h:outputLink>
					</h:column>

					<h:column id="chmeshtc5">
					<f:facet name="header">
						    <h:outputText id="chmeshtc4o" value="Node File"/>
				   </f:facet>
				       <h:outputLink id="chmeshtc4ol" value="#{mrb.meshRunBean.nodeUrl}" target="_blank">
						    <h:outputText id="chmeshtc4olt" value="Node File"/>
						 </h:outputLink>
					</h:column>
					
					<h:column id="chmeshtc5">
					<f:facet name="header">
						    <h:outputText id="chmeshtc5t" value="Plot Mesh"/>
				   </f:facet>
					<h:outputLink id="chmeshtc5t1" value="@host.base.url@@artifactId@/GeoFESTGrid2/painter.jsp">
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
					
					<h:column id="chmeshtc6">
					<f:facet name="header">
						    <h:outputText id="chmeshtc6c1" value="Mesh Status"/>
				   </f:facet>
					<h:commandLink id="chmeshtc6c2" actionListener="#{MGBean.queryMeshGeneratorStatus}">
								<h:outputText id="chmeshtc6c3" value="#{mrb.meshStatus}"/>
					</h:commandLink>
					</h:column>

					<h:column id="chmeshtc7c">
					<f:facet name="header">
						    <h:outputText id="chmeshtc7c1" value="Run GeoFEST"/>
				   </f:facet>
					<h:commandButton id="chmeshtc7c2" actionListener="#{MGBean.selectMeshForGeoFEST}" 
										  action="MG-geotransparams"
										  value="Select"/>
						
 					</h:column>

			</h:dataTable>
 	</h:form>
   </h:panelGrid>
	<h:form id="cmmf2">
		<hr />
		<h:commandLink id="comli1" action="MG-back">
			<h:outputText id="comli2" value="#{MGBean.codeName} Main Menu" />
		</h:commandLink>
	</h:form>

</f:view>
</body>
</html>