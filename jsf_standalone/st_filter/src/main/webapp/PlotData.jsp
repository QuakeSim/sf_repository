<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>STFILTER Data Plot Page</title>
    </head>
     <f:view>
        <h2>Project Archive</h2>
	Click the desired project to view a plot of the output.
        <p/>
        <h:outputText value="You don't have any archived projects yet. You
                    must first run STFILTER." 
                    rendered="#{empty stfilterBean.contextListVector}"/>

        <h:form id="download_table">
        <h:dataTable value="#{stfilterBean.contextListHash}"
                  var="contexts"
	          rendered="#{!(empty stfilterBean.contextListVector)}">
        <f:facet name="header">
           <h:outputText value="Select and load a project"/>
        </f:facet>
        <h:column>
             <h:selectOneRadio 
                       layout="pageDirection"
                       value="#{stfilterBean.chosenProject}">
                <f:selectItems value="#{contexts}"/>
             </h:selectOneRadio>
        </h:column>
        </h:dataTable>
        <h:commandButton action="#{stfilterBean.populateAndPlot}"
                      rendered="#{!(empty stfilterBean.contextList)}"/>
     <p/>

     <hr/>
     <h:commandLink id="link1" action="back">
       <h:outputText id="linkText" value="#{stfilterBean.codeName} Main Menu"/>
     </h:commandLink>

    </h:form>
    </body>
   </f:view>
</html>
