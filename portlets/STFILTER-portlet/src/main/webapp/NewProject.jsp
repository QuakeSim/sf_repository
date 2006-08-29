<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>STFILTER Project Management</title>
  </head>
 <body>
  <h2>Project Setup Page</h2>
  <p>
  Please provide the following STFILTER submission parameters and then
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
    <b>Input Parameter</b>
    <h:panelGrid columns="3" border="1">
       <h:outputText value="Project Name:"/>
       <h:inputText id="projectName" value="#{stfilterBean.projectName}" 
                    required="true"/>
       <h:message for="projectName" showDetail="true" showSummary="true" errorStyle="color: red"/>

       <h:outputText value="Number of Model States:"/>
       <h:inputText id="nmodel" value="#{stfilterBean.numModelStates}"
                     required="true"/>
       <h:message for="nmodel" showDetail="true" showSummary="true" errorStyle="color: red"/>

       <h:outputText value="Annealing Step Size:"/>
       <h:inputText id="annealStep" value="#{stfilterBean.annealStep}"
                     required="true"/>
       <h:message for="annealStep" showDetail="true" showSummary="true" errorStyle="color: red"/>

       <h:outputText value="Random Number Seed:"/>
       <h:inputText id="randomSeed" value="#{stfilterBean.randomSeed}"
                     required="true"/>
       <h:message for="randomSeed" showDetail="true" showSummary="true" errorStyle="color: red"/>


       <h:outputText value="OutputType:"/>
       <h:selectOneListbox 
		value="#{stfilterBean.outputType}"size="1">
    		<f:selectItem itemValue="gauss"
 			      itemLabel="Gaussian"/>
       </h:selectOneListbox>

    </h:panelGrid>
    <h:commandButton value="Pick Station from Map"
                     action="#{stfilterBean.paramsThenMap}"/>
    <h:commandButton value="Upload Input from SOPAC"
                     action="#{stfilterBean.paramsThenDB}"/>
    <h:commandButton value="Manual Input"
                     action="#{stfilterBean.paramsThenTextArea}"/>
    </h:form>

    <h:form>
    <hr/>
    <h:commandLink action="back">
        <h:outputText value="#{stfilterBean.codeName} Main Menu"/>
    </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>