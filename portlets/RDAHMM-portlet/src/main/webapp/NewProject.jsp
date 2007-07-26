<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>RDAHMM Project Management</title>
  </head>
 <body>
  <h2>Project Setup Page</h2>
  <p>
  Please provide the following RDAHMM submission parameters and then
  click one of the buttons at the bottom of the page:
  </p>
  <ul>
  <li> 
  Use <b>Pick Station from Map</b> if you want to pick the station to
  analyze. 
  </li>
  <li> 
  Use <b>Query SOPAC Data Services</b> if you want to query the SOPAC 
  data services.  This assumes you know the station you want to analyze.
  </li>
  <li> 
  Use <b>Manual Input</b> if you want to cut and paste in a data file.
  </li>
  </ul>
  <f:view>        
    <h:form>
    <h:outputText id="head" escape="false" value="<b>Input Parameter</b>"/>
	 <h:outputText id="uhrur" value="Some blah #{rdahmmBean.warningMessage}"/>
    <h:panelGrid columns="3" border="1">
       <h:outputText value="Project Name:"/>
       <h:inputText id="projectName" value="#{rdahmmBean.projectName}" 
                    required="true"/>
       <h:message for="projectName" showDetail="true" showSummary="true" errorStyle="color: red"/>

       <h:outputText value="Number of Model States:"/>
       <h:inputText id="nmodel" value="#{rdahmmBean.numModelStates}"
                     required="true"/>
       <h:message for="nmodel" showDetail="true" showSummary="true" errorStyle="color: red"/>

    </h:panelGrid>
    <h:commandButton value="Pick Station from Map"
                     action="#{rdahmmBean.paramsThenMap}"/>
    <h:commandButton value="Upload Input from SOPAC"
                     action="#{rdahmmBean.paramsThenDB}"/>
    <h:commandButton value="Manual Input"
                     action="#{rdahmmBean.paramsThenTextArea}"/>
    </h:form>

    <h:form>
    <hr/>
    <h:commandLink action="rdahmm-back">
        <h:outputText value="#{rdahmmBean.codeName} Main Menu"/>
    </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>
