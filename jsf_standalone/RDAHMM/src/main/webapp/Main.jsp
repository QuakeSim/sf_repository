<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>RDAHMM Main Page</title>
    </head>
    <body>
        <f:view>
            <h:form id="form">
              <h:panelGrid id="grid" columns="2">
                <h:outputText id="output1" value="Create a new project"/>
                <h:commandButton id="button1" value="press me" 
	                      action="#{rdahmmBean.newProject}"/>

                <h:outputText id="output2" value="Load existing project"/>
                <h:commandButton id="button2" value="press me" 
	                      action="#{rdahmmBean.loadProject}"/>
              </h:panelGrid>
            </h:form>
        </f:view>
    </body>
</html>
