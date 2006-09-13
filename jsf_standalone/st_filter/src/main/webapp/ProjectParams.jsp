<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>STFILTER Input Parameters</title>
  </head>
 <body>
  <h2>Project Setup Page</h2>
  <p>
  Please provide the following STFILTER submission parameters and then
  click  the submit button to run.
  </p>

  <f:view>        
    <h:form>
    <b>Input Parameters</b>
    <h:panelGrid columns="1">
    <h:panelGrid columns="3" border="0">

       <h:outputText value="Residual Option:"/>
       <h:inputText id="resOption" value="#{stfilterBean.resOption}"
                     required="true"/>
       <h:message for="resOption" showDetail="true" showSummary="true" errorStyle="color: red"/>


       <h:outputText value="Term Option:"/>
       <h:inputText id="termOption" value="#{stfilterBean.termOption}"
                     required="true"/>
       <h:message for="termOption" showDetail="true" showSummary="true" errorStyle="color: red"/>


       <h:outputText value="Cutoff Criterion (Year):"/>
       <h:inputText id="cutoffCriterion" 
		    value="#{stfilterBean.cutoffCriterion}"
                    required="true"/>
       <h:message for="cutoffCriterion" showDetail="true" showSummary="true" errorStyle="color: red"/>


       <h:outputText value="Span to Estimated Jump Apr:"/>
       <h:inputText id="estJumpSpan" value="#{stfilterBean.estJumpSpan}"
                     required="true"/>
       <h:message for="estJumpSpan" showDetail="true" showSummary="true" errorStyle="color: red"/>
    </h:panelGrid>

       <h:dataTable value="#{stfilterBean.weakObsCriteria}" 
		    var="weakObsCriteria" border="0">
         <h:column>
           <h:outputText value="Weak Obs Criteria (Year):"/>       
         </h:column>
	  <h:column>
            <h:inputText id="weakObsCriteria1" 
                     value="#{weakObsCriteria.east}"
                     required="true"/>
   	  </h:column>
	  <h:column>
            <h:inputText id="weakObsCriteria2" 
                     value="#{weakObsCriteria.north}"
                     required="true"/>
   	  </h:column>
	  <h:column>
            <h:inputText id="weakObsCriteria3" 
                     value="#{weakObsCriteria.up}"
                     required="true"/>
   	  </h:column>
       </h:dataTable>

       <h:dataTable value="#{stfilterBean.outlierCriteria}" 
		    var="outlierCriteria" border="0">
	  <h:column>
	       <h:outputText value="Outlier Criteria (mm):"/>
          </h:column>
	  <h:column>
            <h:inputText id="outlierCriteria1" 
                     value="#{outlierCriteria.east}"
                     required="true"/>
   	  </h:column>
	  <h:column>
            <h:inputText id="outlierCriteria2" 
                     value="#{outlierCriteria.north}"
                     required="true"/>
   	  </h:column>
	  <h:column>
            <h:inputText id="outlierCriteria3" 
                     value="#{outlierCriteria.up}"
                     required="true"/>
   	  </h:column>
       </h:dataTable>

       <h:dataTable value="#{stfilterBean.badObsCriteria}" 
		    var="badObsCriteria" border="0">
	  <h:column>
	       <h:outputText value="Bad Obs Criteria (mm):"/>
          </h:column>
	  <h:column>
            <h:inputText id="badObsCriteria1" 
                     value="#{badObsCriteria.east}"
                     required="true"/>
   	  </h:column>
	  <h:column>
            <h:inputText id="badObsCriteria2" 
                     value="#{badObsCriteria.north}"
                     required="true"/>
   	  </h:column>
	  <h:column>
            <h:inputText id="badObsCriteria3" 
                     value="#{badObsCriteria.up}"
                     required="true"/>
   	  </h:column>
       </h:dataTable>

       <h:dataTable value="#{stfilterBean.timeInterval}" 
		    var="timeInterval" border="0">
	  <h:column>
	       <h:outputText value="Time Interval:"/>
          </h:column>
	  <h:column>
            <h:inputText id="timeInterval1" 
                     value="#{timeInterval.beginTime}"
                     required="true"/>
   	  </h:column>
	  <h:column>
            <h:inputText id="timeInterval2" 
                     value="#{timeInterval.endTime}"
                     required="true"/>
   	  </h:column>
       </h:dataTable>



    </h:panelGrid>

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
 </body>
</html>
