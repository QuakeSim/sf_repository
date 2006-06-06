<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>RDAHMM Main Page</title>
    </head>
    <body>
        <f:view>
            <h2>RDAHMM Code Submission Forms</h2>
            <p>
            Click the button below to choose either to create a new 
            project or else load and edit an existing project.
            </p>
            <h:form id="form">
              <h:panelGrid id="grid" columns="2">
          
                <h:commandButton id="button1" value="New Project" 
	                      action="#{rdahmmBean.newProject}"/>
 		<h:outputText id="output1" value="Create a new project"/>

                <h:commandButton id="button2" value="Load Project" 
	                      action="#{rdahmmBean.loadProject}"/>
                <h:outputText id="output2" value="Load existing project"/>
              </h:panelGrid>
            </h:form>
        </f:view>
    </body>
</html>
