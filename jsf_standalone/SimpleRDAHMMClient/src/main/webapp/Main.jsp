<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>RDAHMM Minimalist Input</title>
  </head>
 <body>
  <f:view>        
    <h:form>
    <b>Input Parameters</b>
    <h:panelGrid columns="3" border="1">
       <h:outputText value="Number of Model States:"/>
       <h:inputText id="nmodel" value="#{rdahmmBean.numModelStates}"
                     required="true"/>
       <h:message for="nmodel" showDetail="true" showSummary="true" errorStyle="color: red"/>


    </h:panelGrid>
    <h:commandButton value="Submit"
                     action="#{rdahmmBean.}"/>
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
