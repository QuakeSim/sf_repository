<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
 <head>
  <title>Loading Projects from Archive</title>
 </head>
 <body>
  <f:view>        
   <h:outputText value="Here are your old projects"/>
    <h:form id="form2">
     <h:dataTable value="#{rdahmmBean.contextListHash}"
                  styleClass="table-background"
                  rowClasses="table-row-odd, table-row-even"
                  var="contexts">
        <f:facet name="header">
           <h:outputText value="Select and load a project"/>
        </f:facet>
        <h:column>
             <h:selectOneRadio>
                <f:selectItems value="#{contexts}"/>
             </h:selectOneRadio>
        </h:column>
      </h:dataTable>	
     <h:commandLink id="link1" action="back">
       <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
     </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>