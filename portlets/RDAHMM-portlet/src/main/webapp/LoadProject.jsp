<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
 <head>
  <title>Loading Projects from Archive</title>
 </head>
 <body>
  <f:view>        
    <h:form id="form2">
     <h:dataTable value="#{rdahmmBean.contextListHash}"
                  var="contexts">
        <f:facet name="header">
           <h:outputText value="Select and load a project"/>
        </f:facet>
        <h:column>
             <h:selectOneRadio 
                       layout="pageDirection"
                       value="#{rdahmmBean.chosenProject}">
                <f:selectItems value="#{contexts}"/>
             </h:selectOneRadio>
        </h:column>
     </h:dataTable>
     <h:commandButton action="#{rdahmmBean.populateProject}"/>
     <p/>	
     <h:commandLink id="link1" action="back">
       <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
     </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>