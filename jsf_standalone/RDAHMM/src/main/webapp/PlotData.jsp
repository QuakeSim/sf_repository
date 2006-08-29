<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>RDAHMM Data Plot Page</title>
    </head>
     <f:view>
        <h2>Project Archive</h2>
	Click the desired project to view a plot of the output.
        <p/>
        <h:outputText value="You don't have any archived projects yet. You
                    must first run RDAHMM." 
                    rendered="#{empty rdahmmBean.contextListVector}"/>

        <h:form id="download_table">
        <h:dataTable value="#{rdahmmBean.contextListHash}"
                  var="contexts"
	          rendered="#{!(empty rdahmmBean.contextListVector)}">
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
        <h:commandButton action="#{rdahmmBean.populateAndPlot}"
                      rendered="#{!(empty rdahmmBean.contextList)}"/>
     <p/>

     <hr/>
     <h:commandLink id="link1" action="rdahmm-back">
       <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
     </h:commandLink>

    </h:form>
    </body>
   </f:view>
</html>
