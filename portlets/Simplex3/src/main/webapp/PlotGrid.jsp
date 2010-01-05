<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<%@ page import="cgl.quakesim.simplex.SimplexBean" %>
<jsp:useBean id="SimplexBean" scope="session" class="cgl.quakesim.simplex.SimplexBean"/>

<html xmlns="http://www.w3.org/1999/xhtml">  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Google Maps Example</title>
 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here"
      type="text/javascript"></script>
  <script src="@host.base.url@@artifactId@/egeoxml.js" type="text/javascript"></script>
  </head>
  <body>
  <%
	  SimplexBean simplexBean=(SimplexBean)session.getAttribute("SimplexBean");
	  String fileName=simplexBean.getKmlProjectFile();
	  // simplexBean.setCodeName("Simplex2");
	  // simplexBean.setContextBasePath("/WebServices/WEB-INF/Descriptors/users/");
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
   
  //  var exml = new EGeoXml("exml", map, ["<%= fileName %>"], {dropboxid:"dropboxdiv",addmarker:addMark});
  var exml = new EGeoXml("exml", map, ["<%= fileName %>"], {dropboxid:"dropboxdiv"});
    exml.parse();

//    GEvent.addListener(exml,"parse",function(){
//        mgr.refresh();
//     });

    //]]>
    </script>
    </td>
    <td valign="top">

	<f:view>    
    <h:form>
	 <h:dataTable id="projectlist" 
					  border="1"
					  binding="#{SimplexBean.myArchiveDataTable}"
					  value="#{SimplexBean.myarchivedFileEntryList}"
					  var="summaryBean">
					<h:column>
					    <f:facet name="header">
					    <h:outputText  id="blaher0er" value="Project Name"/>
						 </f:facet>
				       <h:outputText  id="blah1ere34" value="#{summaryBean.projectName}"/>
					</h:column>

				<h:column>
					<f:facet name="header">
						<h:outputText  id="kjb132623d" escape="false" value="<b>Creation Date</b>" />
					</f:facet>
					<h:outputText id="kj45rb15123cr4d" value="#{summaryBean.creationDate}" />
				</h:column>

				<h:column>
					<f:facet name="header">
						<h:outputText id="blah1er8er" escape="false" value="<b>Select</b>" />
					</f:facet>
						<h:panelGroup id="pgselect34cd">
							<h:commandButton id="SelectProject" value="Plot"
												  actionListener="#{SimplexBean.togglePlotProject}"/>
						</h:panelGroup>
				</h:column>					

	 </h:dataTable>
    </h:form>
   </f:view>
    </td>
	 </table>
  </body>
</html>
