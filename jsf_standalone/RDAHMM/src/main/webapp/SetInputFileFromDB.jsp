<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>GPS Database Query Page</title>
    </head>
    <body>
     <f:view>
      <h:form>
       <h:panelGrid id="master_table" columns="1" border="1">         

         <h:panelGrid columns="1">
         <h:outputText value="<b>Instructions:</b> provide four-character GPS site code(s), bounding box parameters (optional), and dates of interest to obtain processed coordinates.  REASoN combination and SOPAC globk coordinates are currently available." escape="false"/>
	 </h:panelGrid>


	 <h:panelGrid columns="3"> 
	 <h:outputText value="Site Code(s):"/>
         <h:inputText id="siteCode" size="5" value="#{rdahmmBean.siteCode}"/>
         <h:outputText value="(space-delimited)"/>
       <h:message for="siteCode" showDetail="true" showSummary="true" errorStyle="color: red"/>
         </h:panelGrid>

         <h:panelGrid columns="2" border="1">
	 <h:panelGrid columns="3"> 
	 <h:outputText value="Begin Date:"/>
         <h:inputText id="beginDate" value="#{rdahmmBean.beginDate}"/>
         <h:outputText value="(YYYY-MM-DD)"/>
       <h:message for="beginDate" showDetail="true" showSummary="true" errorStyle="color: red"/>
         </h:panelGrid>

	 <h:panelGrid columns="3"> 
	 <h:outputText value="End Date"/>
         <h:inputText id="endDate" value="#{rdahmmBean.endDate}"/>
         <h:outputText value="(YYYY-MM-DD)"/>
       <h:message for="endDate" showDetail="true" showSummary="true" errorStyle="color: red"/>
         </h:panelGrid>
         </h:panelGrid>


         <h:panelGrid border="1">
 	 <h:panelGrid columns="2">
	 <h:selectBooleanCheckbox 
              value="rdahmmBean.bboxChecked"
	      title="Use Bounding Box Settings Below (optional; check box):"/>
	 <h:outputText value="Use Bounding Box Settings"/>
         </h:panelGrid>
         <h:panelGrid columns="2">
	    <h:panelGrid columns="2"> 
	      <h:outputText value="Minimum Latitude:"/>
              <h:inputText size="10" value="#{rdahmmBean.minLatitude}"/>
            </h:panelGrid>
	    <h:panelGrid columns="2"> 
	      <h:outputText value="Maximum Latitude:"/>
              <h:inputText size="10" value="#{rdahmmBean.maxLatitude}"/>
            </h:panelGrid>
         </h:panelGrid>

         <h:panelGrid columns="2">
	   <h:panelGrid columns="2"> 
	     <h:outputText value="Minimum Longitude:"/>
             <h:inputText size="10" value="#{rdahmmBean.minLongitude}"/>
           </h:panelGrid>
	   <h:panelGrid columns="2"> 
	     <h:outputText value="Maximum Longitude:"/>
             <h:inputText size="10" value="#{rdahmmBean.maxLongitude}"/>
           </h:panelGrid>
         </h:panelGrid>
         </h:panelGrid>



         <h:panelGrid columns="2" border="1">
	 <h:outputText value="Resource"/>
         <h:selectOneListbox title="Resource:"
	                     value="#{rdahmmBean.resource}"
	                     size="1">
            <f:selectItem  
                           itemValue="procCoords"
                          itemLabel="Processed Coordinates"/>
         </h:selectOneListbox>
         </h:panelGrid>




         <h:panelGrid columns="2" border="1">         
	 <h:outputText value="Context Group"/>
         <h:selectOneListbox title="Context Group"
			     value="#{rdahmmBean.contextGroup}"
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
         <h:inputText size="5" value="#{rdahmmBean.contextId}"/>
         <h:outputText value="(4=current REASoN combination coordinates)"/>
         </h:panelGrid>

       </h:panelGrid>

       <h:message for="master_table" showDetail="true" showSummary="true" errorStyle="color: red"/>


       <h:commandButton id="mybutton3" value="Query Database" action="#{rdahmmBean.querySOPAC}"/>

   </h:form>	


   </hr>
    <h:form>
    <h:commandLink id="link1" action="rdahmm-back">
        <h:outputText id="linkText" value="#{rdahmmBean.codeName} Main Menu"/>
    </h:commandLink>
    </h:form>
  </f:view>
  </body>
</html>
