<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>STFILTER Data Plot Page</title>
    </head>
   <f:view>
    <body>
	<h:graphicImage url="#{stfilterBean.localImageFileX}"/>
        <br/>
	<h:graphicImage url="#{stfilterBean.localImageFileY}"/>
        <br/>
	<h:graphicImage url="#{stfilterBean.localImageFileZ}"/>
     <p/>
     <h:commandLink id="link1" action="back-to-main">
       <h:outputText id="linkText" value="#{stfilterBean.codeName} Main Menu"/>
     </h:commandLink>
    </body>
   </f:view>
</html>
