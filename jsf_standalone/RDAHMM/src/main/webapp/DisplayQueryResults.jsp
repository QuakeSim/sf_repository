<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
 <head>
  <title>SOPAC Query Results</title>
 </head>
   <body>
    <h2>Query Results</h2>
    You can download your data by clicking the following link.
    <f:view>
       <h:outputLink value="#{rdahmmBean.sopacDataFileUrl}" target="_blank">
          <h:outputText value="#{rdahmmBean.sopacDataFileUrl}"/>
       </h:outputLink>
        <hr/>
        <h:form>
        <h:commandLink id="link1" action="rdahmm-back">
         <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
        </h:commandLink>
    </h:form>
   </f:view>

    </body>
</html>

