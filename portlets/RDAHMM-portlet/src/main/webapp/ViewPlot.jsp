<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>RDAHMM Data Plot Page</title>
    </head>
   <f:view>
    <body>
	<h:graphicImage url="#{rdahmmBean.localImageFile}" width="75%" 
			height="75%"/>
     <p/>
     <h:commandLink id="link1" action="back">
       <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
     </h:commandLink>
    </body>
   </f:view>
</html>
