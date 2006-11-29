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
                    must first run STFILTER." 
                    rendered="#{empty stfilterBean.contextListHash}"/>



    <h:form>
     <h:dataTable value="#{stfilterBean.contextListHash}"
                  border="1"
                  var="context"
                  rendered="#{!(empty stfilterBean.contextListHash)}">


        <h:column>
         <f:facet name="header">
           <h:outputText value="Select and load a project"/>
         </f:facet>

             <h:selectOneRadio 
                       id="projectRadioSelect"
                       required="true"
                       layout="pageDirection"
                       value="#{stfilterBean.chosenProject}">
                <f:selectItems value="#{context}"/>
             </h:selectOneRadio>
             <h:message for="projectRadioAdd" showDetail="true" showSummary="true" errorStyle="color: red"/>

        </h:column>
     </h:dataTable>
     <h:commandButton action="#{stfilterBean.populateProject}"
                      rendered="#{!(empty stfilterBean.contextListHash)}"/>


    </h:form>
     <p/>	

     <hr/>
   <h:form>
     <h:commandLink id="link1" action="back">
       <h:outputText id="linkText" value="#{stfilterBean.codeName} Main Menu"/>
     </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>