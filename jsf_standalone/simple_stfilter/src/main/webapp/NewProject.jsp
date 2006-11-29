<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>STFILTER Project Management</title>
  </head>
 <body>
  <h2>Project Setup Page</h2>
  <p>
  Provide a name for your project and then pick the station from the map. 
  <ul>
  <li> Use <i>Pick Station from Map</i> if you want to find your station on 
an interactive map before querying it.
  </li>
  <li> Use <i>Query Station from Databae</i> if you know the station name 
and want to go directly to the database query form.
  </li>
  </ul>
  </p>
  <f:view>        
    <h:form>
    <b>Input Parameter</b>
    <h:panelGrid columns="3" border="1">
       <h:outputText value="Project Name:"/>
       <h:inputText id="projectName" value="#{stfilterBean.projectName}" 
                    required="true"/>
       <h:message for="projectName" showDetail="true" showSummary="true" errorStyle="color: red"/>

    </h:panelGrid>
    <p>
    <h:commandButton value="Pick Station from Map"
                     action="#{stfilterBean.paramsThenMap}"/>

    <h:commandButton value="Query Station from Database"
                     action="#{stfilterBean.paramsThenDB}"/>

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