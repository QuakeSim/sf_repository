<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>RDAHMM Data Plot Page</title>
    </head>
   <f:view>
    <body>
	<h:graphicImage url="#{rdahmmBean.localImageFileX}"/>
        <br/>
	<h:graphicImage url="#{rdahmmBean.localImageFileY}"/>
        <br/>
	<h:graphicImage url="#{rdahmmBean.localImageFileZ}"/>
     <p/>
     <h:commandLink id="link1" action="rdahmm-back">
       <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
     </h:commandLink>
    </body>
   </f:view>
</html>
