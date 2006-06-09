<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>RDAHMM Input File Selection</title>
  </head>
 <body>
  <f:view>        
    <h:outputText value="Now select the input file."/>
    <h:form id="form2">
    <h:inputTextarea id="projectInput" value="#{rdahmmBean.inputFile}"
                    rows="20" 
                    required="true"/>
    <h:commandButton id="createProject" value="Set Input File"
                     action="#{rdahmmBean.createInputFile}"/>
    </h:form>

    <h:form>
    <h:commandLink id="link1" action="back">
        <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
    </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>