<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
 <head>
  <title>Load a Project from the Archive</title>
 </head>
 <body>
  <h2>Load a Project from the Archive</h2>
  <f:view>        
   <h:outputText value="You don't have any archived projects yet. You
                    must first run RDAHMM." 
                    rendered="#{empty rdahmmBean.contextListHash}"/>

    <h:form id="form2">
     <h:dataTable value="#{rdahmmBean.contextListHash}"
                  var="contexts"
                  rendered="#{!(empty rdahmmBean.contextListHash)}">
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
     <h:commandButton action="#{rdahmmBean.populateProject}"
                      rendered="#{!(empty rdahmmBean.contextListHash)}"/>
     <p/>	

     <hr/>
     <h:commandLink id="link1" action="back">
       <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
     </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>