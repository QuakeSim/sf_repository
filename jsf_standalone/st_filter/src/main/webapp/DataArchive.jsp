<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>RDAHMM Data Archive Page</title>
    </head>
    <body>
     <f:view>
        <h2>Project Archive</h2>
	Click the link to download the desired output file.

        <h:outputText value="You don't have any archived projects yet. You
                    must first run RDAHMM." 
                    rendered="#{empty rdahmmBean.contextListVector}"/>

	<h:form id="download_table">
          <h:dataTable value="#{rdahmmBean.contextListVector}"
                       border="1"
                       var="project"
                       rendered="#{!(empty rdahmmBean.contextListVector)}">
	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Project Name"/>
            </f:facet>
  	    <h:outputText value="#{project.projectName}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
        	<h:outputText value="Creation Date"/>
            </f:facet>
           <h:outputText value="#{project.creationDate}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="Input File"/>
            </f:facet>
  	    <h:outputLink value="#{rdahmmBean.codeName}/DownloadData.jsp" target="_blank">
           	<h:outputText value="#{project.projectName}.input"/>
                <f:param name="userName" value="#{rdahmmBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".input"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="Standard Output"/>
            </f:facet>
  	    <h:outputLink value="#{rdahmmBean.codeName}/DownloadData.jsp" target="_blank">
           <h:outputText value="#{project.projectName}.stdout"/>
                <f:param name="userName" value="#{rdahmmBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".stdout"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="A"/>
            </f:facet>
  	    <h:outputLink value="#{rdahmmBean.codeName}/DownloadData.jsp" target="_blank">
           <h:outputText value="#{project.projectName}.A"/>
                <f:param name="userName" value="#{rdahmmBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".A"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="B"/>
            </f:facet>
  	    <h:outputLink value="#{rdahmmBean.codeName}/DownloadData.jsp" target="_blank">
           <h:outputText value="#{project.projectName}.B"/>
                <f:param name="userName" value="#{rdahmmBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".B"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="L"/>
            </f:facet>
  	    <h:outputLink value="#{rdahmmBean.codeName}/DownloadData.jsp" target="_blank">
           <h:outputText value="#{project.projectName}.L"/>
                <f:param name="userName" value="#{rdahmmBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".L"/>
            </h:outputLink>

           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="Q"/>
            </f:facet>
  	    <h:outputLink value="#{rdahmmBean.codeName}/DownloadData.jsp" target="_blank">
           <h:outputText value="#{project.projectName}.Q"/>
                <f:param name="userName" value="#{rdahmmBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".Q"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="PI"/>
            </f:facet>
  	    <h:outputLink value="#{rdahmmBean.codeName}/DownloadData.jsp" target="_blank">
           <h:outputText value="#{project.projectName}.pi"/>
                <f:param name="userName" value="#{rdahmmBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".pi"/>
            </h:outputLink>
           </h:column>
          </h:dataTable>
        </h:form>       

     <p/>	
     <hr/>
     <h:commandLink id="link1" action="back">
       <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
     </h:commandLink>

     </f:view>
    </body>
</html>
