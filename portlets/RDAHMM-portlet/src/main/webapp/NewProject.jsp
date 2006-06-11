<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>RDAHMM Project Management</title>
  </head>
 <body>
  <f:view>        
    <h:outputText value="We'll now set up a new project"/>
    <h:form id="form2">
    <h:outputText value="Submission Host: #{rdahmmBean.hostName}"/>
    <h:outputText value="Input Parameters"/>
    <h:panelGrid columns="3" border="1">
       <h:outputText value="Project Name:"/>
       <h:inputText id="projectName" value="#{rdahmmBean.projectName}" 
                    required="true"/>
       <h:message for="projectName" showDetail="true" showSummary="true" errorStyle="color: red"/>

       <h:outputText value="Number of Model States:"/>
       <h:inputText id="nmodel" value="#{rdahmmBean.numModelStates}"
                     required="true"/>
       <h:message for="nmodel" showDetail="true" showSummary="true" errorStyle="color: red"/>

       <h:outputText value="Random Number Seed:"/>
       <h:inputText id="randomSeed" value="#{rdahmmBean.randomSeed}"
                     required="true"/>
       <h:message for="randomSeed" showDetail="true" showSummary="true" errorStyle="color: red"/>


       <h:outputText value="OutputType:"/>
       <h:inputText id="outputType" value="#{rdahmmBean.outputType}"
                     required="true"/>
       <h:message for="outputType" showDetail="true" showSummary="true" errorStyle="color: red"/>
    </h:panelGrid>
    <h:commandButton id="createProject" value="Set Input Parameters"
                     action="#{rdahmmBean.setParameterValues}"/>
    </h:form>

    <h:form>
    <h:commandLink id="link1" action="back">
        <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
    </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>