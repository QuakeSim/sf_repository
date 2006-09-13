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
   
  The following are global, or "all site" parameters.
  <h:form>
    <h:dataTable var="allsites" 
                 rendered="#{empty stfilterBean.mysiteVec}"
                 value="#{stfilterBean.allsitesVec}">
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
  	    <h:outputText value="#{allsites.aprioriValue}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Apriori Contstraint"/>
            </f:facet>
  	    <h:outputText value="#{allsites.aprioriConstraint}"/>
           </h:column>
    </h:dataTable>
<hr/>
<p/>

    <h:outputText 
       value="You have not yet added any parameters for your chosen station" rendered="#{empty stfilterBean.mysiteVec}"/>

    <h:outputText 
       value="For your chosen station, you have chosen the following parameters." rendered="#{!empty stfilterBean.mysiteVec}"/>

    <h:dataTable var="mysite" 
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
  	    <h:outputText value="#{mysite.aprioriValue}"/>
           </h:column>

	   <h:column>
  	    <f:facet name="header">
	        <h:outputText value="Apriori Contstraint"/>
            </f:facet>
  	    <h:outputText value="#{mysite.aprioriConstraint}"/>
           </h:column>
    </h:dataTable>
  </h:form>



  </f:view>
</html>