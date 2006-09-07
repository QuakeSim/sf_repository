<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>STFILTER Project Management</title>
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
    <h:panelGrid columns="3" border="1">

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

       <h:outputText value="Weak Obs Criteria (Year):"/>
       <h:inputText id="weakObsCriteria" 
                     value="#{stfilterBean.weakObsCriteria}"
                     required="true"/>
       <h:message for="weakObsCriteria" showDetail="true" showSummary="true" errorStyle="color: red"/>

       <h:outputText value="Outlier Criteria (mm):"/>
       <h:inputText id="outlierCriteria" 
                     value="#{stfilterBean.outlierCriteria}"
                     required="true"/>
       <h:message for="outlierCriteria" showDetail="true" showSummary="true" errorStyle="color: red"/>

       <h:outputText value="Very Bad Obs Criteria (mm):"/>
       <h:inputText id="badObsCriteria" value="#{stfilterBean.badObsCriteria}"
                     required="true"/>
       <h:message for="badObsCriteria" showDetail="true" showSummary="true" errorStyle="color: red"/>

       <h:outputText value="Time Interval:"/>
       <h:inputText id="timeInterval" value="#{stfilterBean.timeInterval}"
                     required="true"/>
       <h:message for="timeInterval" showDetail="true" showSummary="true" errorStyle="color: red"/>


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
