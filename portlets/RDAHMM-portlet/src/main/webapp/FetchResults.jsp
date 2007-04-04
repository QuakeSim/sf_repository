<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
 <head>
  <title>Fetch Data from the Archive</title>
 </head>
 <body>
  <style>
		  .centered-column {		  
		     text-align: center;
		  }

		  	.header2 {
			font-family: Arial, sans-serif;
			font-size: 18pt;
			font: bold;
		}
  </style>

  <f:view>        
  <h:outputText styleClass="header2" value="RDAHMM Data Archive"/>
   <p/>
   <h:outputText value="You don't have any archived projects yet. You
                    must first run RDAHMM." 
                    rendered="#{empty rdahmmBean.projectArchive}"/>

    <h:form id="form2">
     <h:dataTable value="#{rdahmmBean.projectArchive}"
	  					columnClasses="centered-column"
						border="1"
                  var="rdahmmProjectBean"
                  rendered="#{!(empty rdahmmBean.projectArchive)}">

			 <h:column>
      	 <f:facet name="header">
			 <h:outputText value="Project Name"/>	
			 </f:facet>
			 <h:outputText id="projectName" 
							value="#{rdahmmProjectBean.projectName}" />
			 </h:column>							


			 <h:column>
      	 <f:facet name="header">
       	 <h:outputText value="Begin Date"/>
			 </f:facet>
       	 <h:outputText id="beginDate" 
		 				  value="#{rdahmmProjectBean.beginDate}"/>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
       	 <h:outputText value="End Date"/>
			 </f:facet>
       	 <h:outputText id="endDate" value="#{rdahmmProjectBean.endDate}"/>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
			 <h:outputText value="Site Code"/>
			 </f:facet>
          <h:outputText id="station_name" value="#{rdahmmProjectBean.siteCode}"/>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
	 		 <h:outputText value="Number of Model States"/>
			 </f:facet>
       	 <h:outputText id="nmodel" 
							 value="#{rdahmmProjectBean.numModelStates}"/>
			 </h:column>				

			 <h:column>
      	 <f:facet name="header">
			 <h:outputText value="Input File"/>
			 </f:facet>
       	 <h:outputLink value="#{rdahmmProjectBean.resultsBean.inputUrl}" target="_blank">
       	 <h:outputText value="Input File"/>
       	 </h:outputLink>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
       	 <h:outputText value="Range"/>
			 </f:facet>
       	 <h:outputLink value="#{rdahmmProjectBean.resultsBean.rangeUrl}" target="_blank">
       	 <h:outputText value="Range"/>
       	 </h:outputLink>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
       	 <h:outputText value="Optimal State Sequence File (Q)"/>
			 </f:facet>
       	 <h:outputLink target="_blank" value="#{rdahmmProjectBean.resultsBean.QUrl}">
       	 <h:outputText value="Q File"/>
       	 </h:outputLink>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
       	 <h:outputText value="Model Transition Probability (A)"/>
			 </f:facet>
       	 <h:outputLink target="_blank" value="#{rdahmmProjectBean.resultsBean.AUrl}">
       	 <h:outputText value="A File"/>
       	 </h:outputLink>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
      	 <h:outputText value="Model Output Distribution (B)"/>
			 </f:facet>
       	 <h:outputLink target="_blank" value="#{rdahmmProjectBean.resultsBean.BUrl}">
       	 <h:outputText value="B File"/>
       	 </h:outputLink>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
       	 <h:outputText value="Model Log Likelihood (L)"/>
			 </f:facet>
       	 <h:outputLink target="_blank" value="#{rdahmmProjectBean.resultsBean.LUrl}">
       	 <h:outputText value="L File"/>
       	 </h:outputLink>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
       	 <h:outputText value="Model Initial State Probability (PI)"/>
			 </f:facet>
       	 <h:outputLink target="_blank" value="#{rdahmmProjectBean.resultsBean.piUrl}">
       	 <h:outputText value="Pi File"/>
       	 </h:outputLink>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
       	 <h:outputText value="Minimum Value"/>
			 </f:facet>
       	 <h:outputLink target="_blank" value="#{rdahmmProjectBean.resultsBean.minvalUrl}">
       	 <h:outputText value="Minval File"/>
       	 </h:outputLink>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
       	 <h:outputText value="Maximum Value"/>
			 </f:facet>
       	 <h:outputLink target="_blank" value="#{rdahmmProjectBean.resultsBean.maxvalUrl}">
       	 <h:outputText value="Maxval File"/>
       	 </h:outputLink>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
       	 <h:outputText value="Plot of X Values"/>
			 </f:facet>
      	 <h:outputLink target="_blank" value="#{rdahmmProjectBean.resultsBean.inputXPngUrl}">
       	 <h:outputText value="X Plot"/>
       	 </h:outputLink>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
       	 <h:outputText value="Plot of Y Values"/>
			 </f:facet>
       	 <h:outputLink target="_blank" value="#{rdahmmProjectBean.resultsBean.inputYPngUrl}">
       	 <h:outputText value="Y Plot"/>
       	 </h:outputLink>
			 </h:column>							

			 <h:column>
      	 <f:facet name="header">
      	 <h:outputText value="Plot of Z Values"/>
			 </f:facet>
       	 <h:outputLink target="_blank" value="#{rdahmmProjectBean.resultsBean.inputZPngUrl}">
       	 <h:outputText value="Z Plot"/>
       	 </h:outputLink>
			 </h:column>							
			
     </h:dataTable>
     <hr/>
     <h:commandLink id="link1" action="rdahmm-back">
       <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
     </h:commandLink>
    </h:form>
  </f:view>
 </body>
</html>
