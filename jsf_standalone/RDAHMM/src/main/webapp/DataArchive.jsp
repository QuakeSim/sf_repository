<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>RDAHMM Data Archive Page</title>
    </head>
    <body>
     <f:view>
        <h2>Project Archive</h2>
	Click the link to download the desired output file.
	<h:form id="download_table">
          <h:dataTable value="#{rdahmmBean.contextList}"
                       border="1"
                       var="context">
	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Project Name"/>
            </f:facet>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
        	<h:outputText value="Creation Date"/>
            </f:facet>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="Input"/>
            </f:facet>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="A"/>
            </f:facet>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="B"/>
            </f:facet>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="L"/>
            </f:facet>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="Q"/>
            </f:facet>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="PI"/>
            </f:facet>
           </h:column>
          </h:dataTable>
        </h:form>       

     <p/>	
     <h:commandLink id="link1" action="back">
       <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
     </h:commandLink>

     </f:view>
    </body>
</htdml>
