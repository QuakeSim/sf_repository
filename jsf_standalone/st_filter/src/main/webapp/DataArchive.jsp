<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>STFILTER Data Archive Page</title>
    </head>
    <body>
     <f:view>
        <h2>Project Archive</h2>
	Click the link to download the desired output file.

        <h:outputText value="You don't have any archived projects yet. You
                    must first run STFILTER." 
                    rendered="#{empty stfilterBean.contextListVector}"/>

	<h:form id="download_table">
          <h:dataTable value="#{stfilterBean.contextListVector}"
                       border="1"
                       var="project"
                       rendered="#{!(empty stfilterBean.contextListVector)}">
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
           	<h:outputText value="Driver File"/>
            </f:facet>
  	    <h:outputLink value="#{stfilterBean.codeName}/DownloadData.jsp" target="_blank">
           	<h:outputText value="#{project.projectName}.drv"/>
                <f:param name="userName" value="#{stfilterBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".drv"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="GPS Input Data"/>
            </f:facet>
  	    <h:outputLink value="#{stfilterBean.codeName}/DownloadData.jsp" target="_blank">
           	<h:outputText value="#{project.projectName}.data"/>
                <f:param name="userName" value="#{stfilterBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".data"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="Input Station List"/>
            </f:facet>
  	    <h:outputLink value="#{stfilterBean.codeName}/DownloadData.jsp" target="_blank">
           	<h:outputText value="#{project.projectName}.list"/>
                <f:param name="userName" value="#{stfilterBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".list"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="Site List"/>
            </f:facet>
  	    <h:outputLink value="#{stfilterBean.codeName}/DownloadData.jsp" target="_blank">
           	<h:outputText value="#{project.projectName}.site"/>
                <f:param name="userName" value="#{stfilterBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".site"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="Estimated Parameter File"/>
            </f:facet>
  	    <h:outputLink value="#{stfilterBean.codeName}/DownloadData.jsp" target="_blank">
           	<h:outputText value="#{project.projectName}.para"/>
                <f:param name="userName" value="#{stfilterBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".para"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="Output File"/>
            </f:facet>
  	    <h:outputLink value="#{stfilterBean.codeName}/DownloadData.jsp" target="_blank">
           	<h:outputText value="#{project.projectName}.output"/>
                <f:param name="userName" value="#{stfilterBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".output"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="Residual File"/>
            </f:facet>
  	    <h:outputLink value="#{stfilterBean.codeName}/DownloadData.jsp" target="_blank">
           	<h:outputText value="#{project.projectName}.resi"/>
                <f:param name="userName" value="#{stfilterBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".resi"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="Model File"/>
            </f:facet>
  	    <h:outputLink value="#{stfilterBean.codeName}/DownloadData.jsp" target="_blank">
           	<h:outputText value="#{project.projectName}.mdl"/>
                <f:param name="userName" value="#{stfilterBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".mdl"/>
            </h:outputLink>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
           	<h:outputText value="Standard Output"/>
            </f:facet>
  	    <h:outputLink value="#{stfilterBean.codeName}/DownloadData.jsp" target="_blank">
           <h:outputText value="#{project.projectName}.stdout"/>
                <f:param name="userName" value="#{stfilterBean.userName}"/>
                <f:param name="hostName" value="#{project.hostName}"/>
                <f:param name="projectName" value="#{project.projectName}"/>
                <f:param name="baseWorkDir" value="#{project.baseWorkDir}"/>
                <f:param name="fileServiceUrl" value="#{project.fileServiceUrl}"/>
                <f:param name="fileExtension" value=".stdout"/>
            </h:outputLink>
           </h:column>
          </h:dataTable>
        </h:form>       

     <p/>	
     <hr/>
     <h:commandLink id="link1" action="back">
       <h:outputText id="linkText" value="#{stfilterBean.codeName} Main Menu"/>
     </h:commandLink>

     </f:view>
    </body>
</html>
