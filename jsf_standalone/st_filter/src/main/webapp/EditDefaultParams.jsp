<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>Edit defalut parameters</title>
    </head>
    <body>
     <f:view>
      <h:form>
       <h:panelGrid id="master_table" columns="1" border="1">         

	 <h:panelGrid columns="3"> 
	 <h:outputText value="Site Code(s):"/>
         <h:inputText id="siteCode" size="5" value="#{stfilterBean.siteCode}"/>
         <h:outputText value="(space-delimited)"/>
       <h:message for="siteCode" showDetail="true" showSummary="true" errorStyle="color: red"/>
         </h:panelGrid>

         <h:panelGrid columns="2" border="1">
	 <h:panelGrid columns="3"> 
	 <h:outputText value="Begin Date:"/>
         <h:inputText id="beginDate" value="#{stfilterBean.beginDate}"/>
         <h:outputText value="(YYYY-MM-DD)"/>
       <h:message for="beginDate" showDetail="true" showSummary="true" errorStyle="color: red"/>
         </h:panelGrid>

	 <h:panelGrid columns="3"> 
	 <h:outputText value="End Date"/>
         <h:inputText id="endDate" value="#{stfilterBean.endDate}"/>
         <h:outputText value="(YYYY-MM-DD)"/>
       <h:message for="endDate" showDetail="true" showSummary="true" errorStyle="color: red"/>
         </h:panelGrid>
         </h:panelGrid>


         <h:panelGrid border="1">
 	 <h:panelGrid columns="2">
	 <h:selectBooleanCheckbox 
              value="stfilterBean.bboxChecked"
	      title="Use Bounding Box Settings Below (optional; check box):"/>
	 <h:outputText value="Use Bounding Box Settings"/>
         </h:panelGrid>
         <h:panelGrid columns="2">
	    <h:panelGrid columns="2"> 
	      <h:outputText value="Minimum Latitude:"/>
              <h:inputText size="10" value="#{stfilterBean.minLatitude}"/>
            </h:panelGrid>
	    <h:panelGrid columns="2"> 
	      <h:outputText value="Maximum Latitude:"/>
              <h:inputText size="10" value="#{stfilterBean.maxLatitude}"/>
            </h:panelGrid>
         </h:panelGrid>

         <h:panelGrid columns="2">
	   <h:panelGrid columns="2"> 
	     <h:outputText value="Minimum Longitude:"/>
             <h:inputText size="10" value="#{stfilterBean.minLongitude}"/>
           </h:panelGrid>
	   <h:panelGrid columns="2"> 
	     <h:outputText value="Maximum Longitude:"/>
             <h:inputText size="10" value="#{stfilterBean.maxLongitude}"/>
           </h:panelGrid>
         </h:panelGrid>
         </h:panelGrid>



         <h:panelGrid columns="2" border="1">
	 <h:outputText value="Resource"/>
         <h:selectOneListbox title="Resource:"
	                     value="#{stfilterBean.resource}"
	                     size="1">
            <f:selectItem  
                           itemValue="procCoords"
                          itemLabel="Processed Coordinates"/>
         </h:selectOneListbox>
         </h:panelGrid>




         <h:panelGrid columns="2" border="1">         
	 <h:outputText value="Context Group"/>
         <h:selectOneListbox title="Context Group"
			     value="#{stfilterBean.contextGroup}"
	                     size="1">
	     <f:selectItem  
                            itemValue="reasonComb" 
                            itemLabel="REASoN combination"/>
	     <f:selectItem itemValue="sopacGlobk" itemLabel="SOPAC GLOBK"/>
	     <f:selectItem itemValue="jplGipsy" itemLabel="JPL GIPSY"/>
	     <f:selectItem itemValue="usgsGlobk" itemLabel="USGS GLOBK"/>
         </h:selectOneListbox>
         </h:panelGrid>



	 <h:panelGrid columns="3"> 
	 <h:outputText value="Context Id:"/>
         <h:inputText size="5" value="#{stfilterBean.contextId}"/>
         <h:outputText value="(4=current REASoN combination coordinates)"/>
         </h:panelGrid>

       </h:panelGrid>

       <h:message for="master_table" showDetail="true" showSummary="true" errorStyle="color: red"/>


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

       <h:commandButton id="BT_SAVE" value="Save" action="stfilterBean.savePref"/>

   </h:form>	


   </hr>
    <h:form>
    <h:commandLink id="link1" action="back">
        <h:outputText id="linkText" value="#{stfilterBean.codeName} Main Menu"/>
    </h:commandLink>
    </h:form>
  </f:view>
  </body>
</html>
