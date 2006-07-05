<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>RDAHMM Project Management</title>
  </head>
 <body>
  <f:view>        
    <h:outputText value="We'll now set up a new project"/>
    <h:form>
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

       <h:outputText value="Annealing Step Size:"/>
       <h:inputText id="annealStep" value="#{rdahmmBean.annealStep}"
                     required="true"/>
       <h:message for="annealStep" showDetail="true" showSummary="true" errorStyle="color: red"/>

       <h:outputText value="Random Number Seed:"/>
       <h:inputText id="randomSeed" value="#{rdahmmBean.randomSeed}"
                     required="true"/>
       <h:message for="randomSeed" showDetail="true" showSummary="true" errorStyle="color: red"/>


       <h:outputText value="OutputType:"/>
       <h:selectOneListbox 
		value="#{rdahmmBean.outputType}"size="1">
    		<f:selectItem itemValue="gauss"
 			      itemLabel="Gaussian"/>
       </h:selectOneListbox>
       <h:message for="OutputType" showDetail="true" showSummary="true" errorStyle="color: red"/>
    </h:panelGrid>
    <h:commandButton value="Cut and Past Input"
                     action="#{rdahmmBean.paramsThenTextArea}"/>
    <h:commandButton value="Upload Input from SOPAC"
                     action="#{rdahmmBean.paramsThenDB}"/>
    <h:commandButton value="Pick Station from Map"
                     action="#{rdahmmBean.paramsThenMap}"/>
    </h:form>

    <h:form>
    <h:commandLink action="back">
        <h:outputText value="#{rdahmmBean.codeName} Main Menu"/>
    </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>