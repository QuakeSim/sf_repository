<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
 <head>
  <title>Load/Delete a Project</title>
 </head>
 <body>
  <h2>Load a Project from the Archive</h2>
  <f:view>        
   <h:outputText value="You don't have any archived projects yet. You must first run STFILTER." 
                    rendered="#{empty stfilterBean.projectArchive}"/>
   <h:outputText value="Select and load a project" 
                    rendered="#{!(empty stfilterBean.projectArchive)}"/>
                    
     <h:dataTable value="#{stfilterBean.projectArchive}"
                  border="1"
                  var="context"
                  rendered="#{!(empty stfilterBean.projectArchive)}">

		<h:column>
			<f:facet name="header">
				<h:outputText value="Select"/>
			</f:facet>
			<h:form style="margin-bottom:0;">
			<h:inputHidden id="projectName" value="#{context.projectName}" />
			<h:inputHidden id="creationDate" value="#{context.creationDate.time.time}"/>
			<h:commandButton value="Load" action="#{stfilterBean.populateProject}"/>
			<h:commandButton value="Delete" action="#{stfilterBean.deleteProject}"/>
			</h:form>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="Project Name"/>
			</f:facet>
			<h:outputText value="#{context.projectName}"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="Creation Time"/>
			</f:facet>
			<h:outputText value="#{context.creationDate.time}"> 
				<f:convertDateTime type="both"/>
			</h:outputText>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="Site Code"/>
			</f:facet>
			<h:outputText value="#{context.siteCode}"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="SOPAC Begin Date"/>
			</f:facet>
			<h:outputText value="#{context.beginDate}"/>
		</h:column>
		<h:column>
			<f:facet name="header">
				<h:outputText value="SOPAC End Date"/>
			</f:facet>
			<h:outputText value="#{context.endDate}"/>
		</h:column>
	</h:dataTable>
	<%--
	<h:form rendered="#{!(empty stfilterBean.projectArchive)}">
	<h:commandButton action="#{stfilterBean.populateProject}"/>
	</h:form>
	--%>
	<h:messages style="color: red"/>
	<p/>	

    <hr/>
   <h:form>
     <h:commandLink id="link1" action="back">
       <h:outputText id="linkText" value="#{stfilterBean.codeName} Main Menu"/>
     </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>

<%--
<html>
 <head>
  <title>Load a Project from the Archive</title>
 </head>
 <body>
  <h2>Load a Project from the Archive</h2>
  <f:view>        
   <h:outputText value="You don't have any archived projects yet. You
                    must first run STFILTER." 
                    rendered="#{empty stfilterBean.contextListHash}"/>



    <h:form>
     <h:dataTable value="#{stfilterBean.contextListHash}"
                  border="1"
                  var="context"
                  rendered="#{!(empty stfilterBean.contextListHash)}">


        <h:column>
         <f:facet name="header">
           <h:outputText value="Select and load a project"/>
         </f:facet>

             <h:selectOneRadio 
                       id="projectRadioSelect"
                       required="true"
                       layout="pageDirection"
                       value="#{stfilterBean.chosenProject}">
                <f:selectItems value="#{context}"/>
             </h:selectOneRadio>
             <h:message for="projectRadioAdd" showDetail="true" showSummary="true" errorStyle="color: red"/>

        </h:column>
     </h:dataTable>
     <h:commandButton action="#{stfilterBean.populateProject}"
                      rendered="#{!(empty stfilterBean.contextListHash)}"/>


    </h:form>
     <p/>	

     <hr/>
   <h:form>
     <h:commandLink id="link1" action="back">
       <h:outputText id="linkText" value="#{stfilterBean.codeName} Main Menu"/>
     </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>
--%>