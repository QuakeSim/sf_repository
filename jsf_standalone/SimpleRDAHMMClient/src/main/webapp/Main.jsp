<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>RDAHMM Minimalist Input</title>
  </head>
 <body>
  The input data URL is obtained directly from the GRWS web service
  as a return type.

  <f:view>        
    <h:form>
    <b>Input Parameters</b>
    <h:panelGrid columns="3" border="1">

       <h:outputText value="Input Data Url"/>
       <h:inputText id="inputDataUrl" value="#{simpleRdahmmClientBean.dataUrl}"
                     required="true" size="50"/>
       <h:message for="inputDataUrl" showDetail="true" showSummary="true" errorStyle="color: red"/>

       <h:outputText value="Number of Model States"/>	
       <h:inputText id="nmodel" value="#{simpleRdahmmClientBean.numModelStates}"
                     required="true"/>
       <h:message for="nmodel" showDetail="true" showSummary="true" errorStyle="color: red"/>

    </h:panelGrid>
    <h:commandButton value="Submit"
                     action="#{simpleRdahmmClientBean.runNonblockingRDAHMM}"/>
    </h:form>

  </f:view>



 </body>
</html>
