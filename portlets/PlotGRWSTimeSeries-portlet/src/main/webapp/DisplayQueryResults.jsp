<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
 <head>
  <title>SOPAC Query Results</title>
 </head>
   <body>
    <h2>Query Results</h2>
    <f:view>
       <h:outputText value="#{stfilterBean.sopacQueryResults}" escape="false"/>
        <h:form>
        <h:commandLink id="link1" action="back">
         <h:outputText id="linkText" value="#{stfilterBean.codeName} Main Menu"/>
        </h:commandLink>
    </h:form>
   </f:view>

    </body>
</html>
