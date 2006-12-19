<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>STFILTER Input File Selection</title>
  </head>
 <body>
  <h2>Input File</h2>
  If you are submitting STFILTER manually, paste in your input file here.
  Otherwise, use this text area to verify that the data you plan to 
  analyze is correct.
  <f:view>        
    <h:outputText value="View and edit the input file you will submit"/>
    <h:form id="form2">
    <h:inputTextarea id="projectInput" value="#{stfilterBean.inputFileContent}"
                    rows="20" cols="75" 
                    required="true"/>
    <h:message for="projectInput" 
	       showDetail="true" showSummary="true" errorStyle="color: red"/>
    <p/>
    <h:commandButton id="createProject" value="Click to Run"
                     action="#{stfilterBean.launchSTFILTER}"/>
    </h:form>

       <hr/>
    <h:form>
    <h:commandLink id="link1" action="back">
        <h:outputText id="linkText" value="#{stfilterBean.codeName} Main Menu"/>
    </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>