<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>STFILTER Estimate Parameters</title>
  </head>
 <body>
  <h2>Station Estimate Parameters</h2>

  <f:view>        
  <p>
   This page allows you to specify time series parameters.  These
are more fully described in documentation 
<a href="http://gipsy.jpl.nasa.gov/qoca/advclass/tutor_adv.html">
here</a>.  For a complete list of parameters, see 
<a href="http://gipsy.jpl.nasa.gov/qoca/advclass/tsa_plist.html">
here</a>.
  </p>
  <p>
   We divide parameters into two sets: the "global" values for all sites
and the specific extensions for your selected site.  Click "All Sites" to
add or remove a "global" parameter.  Click "My Sites" to add or remove
additional parameters for your chosen site.
  </p>
   
  The following are global, or "all site" parameters.  Edit values that you
want to change and submit.
  <h:form>
    <h:dataTable var="allsites" 
 		 border="2"
                 binding="#{stfilterBean.allsites.dataTable}"
                 rendered="#{!empty stfilterBean.allsites.estParamVector}"
                 value="#{stfilterBean.allsites.estParamVector}">
	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Parameter Full Name"/>
            </f:facet>
  	    <h:outputText value="#{allsites.parameterFullName}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Parameter Type"/>
            </f:facet>
  	    <h:outputText value="#{allsites.parameterType}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Apriori Value"/>
            </f:facet>
  	    <h:inputText id="allsite_aprioriValue"
                  required="true" value="#{allsites.aprioriValue}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Apriori Constraint"/>
            </f:facet>
  	    <h:inputText value="#{allsites.aprioriConstraint}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Start Date"/>
            </f:facet>
  	    <h:inputText value="#{allsites.startDate}"
                          rendered="#{!empty allsites.startDate}"/>
  	    <h:outputText value="N/A"
                          rendered="#{empty allsites.startDate}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="End Date"/>
            </f:facet>
  	    <h:inputText value="#{allsites.endDate}"
                          rendered="#{!empty allsites.endDate}"/>
  	    <h:outputText value="N/A"
                          rendered="#{empty allsites.endDate}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Period Length"/>
            </f:facet>
  	    <h:inputText value="#{allsites.periodLength}"
                          rendered="#{!empty allsites.periodLength}"/>
  	    <h:outputText value="N/A"
                          rendered="#{empty allsites.periodLength}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Remove Parameter"/>
            </f:facet>
            <h:commandLink value="Remove"
	               actionListener="#{stfilterBean.allsites.removeEstParameterListener}"/>
           </h:column>

    </h:dataTable>


<hr/>

    <h:outputText 
       value="The following estimate parameters can be added." />

    <h:dataTable var="masterParamList" 
 		 border="2"
                 binding="#{stfilterBean.allsites.dataTable2}"
                 rendered="#{!empty stfilterBean.allsites.masterParamList}"
                 value="#{stfilterBean.allsites.masterParamList}">
	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Parameter Full Name"/>
            </f:facet>
  	    <h:outputText value="#{masterParamList.parameterFullName}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Parameter Type"/>
            </f:facet>
  	    <h:outputText value="#{masterParamList.parameterType}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Apriori Value"/>
            </f:facet>
  	    <h:inputText
                  required="true" value="#{masterParamList.aprioriValue}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Apriori Constraint"/>
            </f:facet>
  	    <h:inputText value="#{masterParamList.aprioriConstraint}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Start Date"/>
            </f:facet>
  	    <h:inputText value="#{masterParamList.startDate}"
                          rendered="#{!empty masterParamList.startDate}"/>
  	    <h:outputText value="N/A"
                          rendered="#{empty masterParamList.startDate}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="End Date"/>
            </f:facet>
  	    <h:inputText value="#{masterParamList.endDate}"
                          rendered="#{!empty masterParamList.endDate}"/>
  	    <h:outputText value="N/A"
                          rendered="#{empty masterParamList.endDate}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Period Length"/>
            </f:facet>
  	    <h:inputText value="#{masterParamList.periodLength}"
                          rendered="#{!empty masterParamList.periodLength}"/>
  	    <h:outputText value="N/A"
                          rendered="#{empty masterParamList.periodLength}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Add Parameter"/>
            </f:facet>
            <h:commandLink value="Add"
	               actionListener="#{stfilterBean.allsites.addEstParameterListener}"/>
           </h:column>

    </h:dataTable>


    <h:outputText 
       value="You have not yet added any parameters for your chosen station" rendered="#{empty stfilterBean.mysiteVec}"/>


    <h:dataTable var="mysite" 
 		 border="2"
                 rendered="#{!empty stfilterBean.mysiteVec}"
                 value="#{stfilterBean.mysiteVec}">
	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Parameter Full Name"/>
            </f:facet>
  	    <h:outputText value="#{mysite.parameterFullName}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Parameter Type"/>
            </f:facet>
  	    <h:outputText value="#{mysite.parameterType}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Apriori Value"/>
            </f:facet>
  	    <h:inputText value="#{mysite.aprioriValue}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Apriori Constraint"/>
            </f:facet>
  	    <h:inputText id="mysite_apriori" required="true"
                  value="#{mysite.aprioriConstraint}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Start Date"/>
            </f:facet>
  	    <h:inputText value="#{mysite.startDate}"
                          rendered="#{!empty mysite.startDate}"/>
  	    <h:outputText value="N/A"
                          rendered="#{empty mysite.startDate}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="End Date"/>
            </f:facet>
  	    <h:inputText value="#{mysite.endDate}"
                          rendered="#{!empty mysite.endDate}"/>
  	    <h:outputText value="N/A"
                          rendered="#{empty mysite.endDate}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Period Length"/>
            </f:facet>
  	    <h:inputText value="#{mysite.periodLength}"
                          rendered="#{!empty mysite.periodLength}"/>
  	    <h:outputText value="N/A"
                          rendered="#{empty mysite.periodLength}"/>
           </h:column>

    </h:dataTable>

<p/>
    <h:commandButton value="Run ST_FILTER"
                     action="#{stfilterBean.launchSTFILTER}"/>

  </h:form>
    <h:form>
    <hr/>
    <h:commandLink action="back">
        <h:outputText value="#{stfilterBean.codeName} Main Menu"/>
    </h:commandLink>
    </h:form>



  </f:view>
</html>