<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>STFILTER Main Page</title>
    </head>
    <body>
        <f:view>
            <h2>STFILTER Code Submission Forms</h2>
	    <p>
            The STFILTER submission pages are designed to allow you to 
            create and persistently store individual projects. Each
            project consists of a single STFILTER run and all of its
	    parameters (input, number of model states, etc).
            </p>

            <h:form id="form">
              <h:panelGrid id="grid" columns="2" border="1">
          
                <h:commandButton id="button1" value="New Project" 
	                      action="#{stfilterBean.newProject}"/>
 		<h:outputFormat id="output1" escape="false"
                              value="<b>New Project:</b> Create a new project"/>
				<%--
                <h:commandButton id="button2" value="Load Project" 
	                      action="#{stfilterBean.loadProject}"/>
                <h:outputFormat id="output2" escape="false"
  			       value="<b>Load Project:</b> Load old projects"/>

                <h:commandButton id="button3" value="Delete Project" 
	                      action="#{stfilterBean.loadProjectKillList}"/>
                <h:outputFormat id="output3" escape="false"
  			       value="<b>Delete Project:</b> Delete old projects"/>

                <h:commandButton id="button4" value="Data Archive" 
	                      action="#{stfilterBean.loadDataArchive}"/>
                <h:outputFormat id="output4" escape="false"   
                              value="<b>Data Archive:</b> Download output files from previous STFILTER runs."/>
				--%>
              </h:panelGrid>
            </h:form>
        </f:view>
            <p>
            Click the buttons below to either start a new project,
	    edit an old project, view output files from previous
	    STFILTER runs, or create plots of your data.
            </p>

    </body>
</html>
