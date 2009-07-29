<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<%@ page import="cgl.quakesim.disloc.DislocBean" %>
<jsp:useBean id="DislocBean2" scope="session" class="cgl.quakesim.disloc.DislocBean"/>

<html xmlns="http://www.w3.org/1999/xhtml">  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Google Maps Example</title>
 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here"
      type="text/javascript"></script>
  <script src="@host.base.url@/Disloc2/egeoxml.js" type="text/javascript"></script>
  </head>
  <body>
  <%
	  DislocBean dislocBean=(DislocBean)session.getAttribute("DislocBean2");
	  String fileName=dislocBean.getKmlProjectFile();
	  dislocBean.setCodeName("Disloc2");
	  dislocBean.setContextBasePath("/WebServices/WEB-INF/Descriptors/users/");
  %>

   <table>
   <tr>
   <td valign="top">
    <div id="map" style="width: 800px; height: 600px; "></div>
    <div id="dropboxdiv"></div>

    <script type="text/javascript">
    //<![CDATA[

    var map=new GMap2(document.getElementById("map"));
    map.setCenter(new GLatLng(32,-118),7);
    map.addControl(new GLargeMapControl());
    map.addControl(new GMapTypeControl());


    // == create the manager ==
    var mgr = new GMarkerManager(map);

    var mypoints=[];

    function addMark(marker,name,desc,imagefile,n) {
        mgr.addMarker(marker,0,17);
    }
   
//    var exml = new EGeoXml("exml", map, ["<%= fileName %>"], {dropboxid:"dropboxdiv",addmarker:addMark});
//    var exml = new EGeoXml("exml", map, ["<%= fileName %>"], {nozoom:true, dropboxid:"dropboxdiv"});
    var exml = new EGeoXml("exml", map, ["<%= fileName %>"]);
    exml.parse();
//    GEvent.addListener(exml,"parse",function(){
//        mgr.refresh();
//     });

    //]]>
    </script>
    </td>

    <td valign="top">
	<f:view>    
    <h:form id="elkrheh">
	 <h:dataTable id="projectlistdfeer" 
					  border="1"
					  binding="#{DislocBean2.myProjectSummaryDataTable}"
					  var="summaryBean"
					  value="#{DislocBean2.myArchivedDislocResultsList}">
					<h:column>
					    <f:facet name="header">
					    <h:outputText  id="dlkjfe0" value="Project Name"/>
						 </f:facet>
				       <h:outputText  id="dlkjfe1" value="#{summaryBean.projectName}"/>
					</h:column>
					<h:column>
					    <f:facet name="header">
					    <h:outputText id="dlkjfe3" value="Creation Date"/>
						 </f:facet>
				       <h:outputText id="dlkjfe2" value="#{summaryBean.creationDate}"/>
					</h:column>

					<h:column>
					    <f:facet name="header">
					    <h:outputText id="dlkjfe4" value="Job UID Stamp"/>
						 </f:facet>
				       <h:outputText  id="dlkjfe5" value="#{summaryBean.jobUIDStamp}"/>
					</h:column>
				<h:column>
					<f:facet name="header">
						<h:outputText id="dlkjfe18" escape="false" value="<b>Select</b>" />
					</f:facet>
						<h:panelGroup id="pgselecerert">
							<h:commandButton id="SelectProject" value="Plot"
												  actionListener="#{DislocBean2.togglePlotProject}"/>
						</h:panelGroup>
				</h:column>					

	 </h:dataTable>
    </h:form>
		<hr />
	<h:form id="df3">
		<h:commandLink id="dlkjfe2df0" action="disloc-this">
			<h:outputText id="dlkjfe1dfe9" value="Refresh Page" />
		</h:commandLink>
	</h:form>
   </f:view>
    </td>
	 </table>
  </body>
</html>
