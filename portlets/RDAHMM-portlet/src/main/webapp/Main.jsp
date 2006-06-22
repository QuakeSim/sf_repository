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
              <h:panelGrid id="grid" columns="2" border="1">
          
                <h:commandButton id="button1" value="New Project" 
	                      action="#{rdahmmBean.newProject}"/>
 		<h:outputFormat id="output1" escape="false"
                              value="<b>New Project:</b>Create a new project"/>

                <h:commandButton id="button2" value="Load Project" 
	                      action="#{rdahmmBean.loadProject}"/>
                <h:outputFormat id="output2" escape="false"
  			       value="<b>Load Project:</b>Load old project"/>

                <h:commandButton id="button3" value="Query DB" 
	                      action="load-database-page"/>
                <h:outputFormat id="output3" escape="false"
                              value="<b>Query Database:</b> Look at database query page"/>

                <h:commandButton id="button4" value="Data Archive" 
	                      action="#{rdahmmBean.loadDataArchive}"/>
                <h:outputFormat id="output4" escape="false"   
                              value="<b>Data Archive:</b> Download output files"/>

                <h:commandButton id="button5" value="Plot Data" 
	                      action="plot-rdahmm-data"/>
                <h:outputFormat id="output5" escape="false"
                              value="<b>Plot Output:</b> Plot RDAHMM ouput files"/>
              </h:panelGrid>
            </h:form>
        </f:view>
    </body>
</html>
