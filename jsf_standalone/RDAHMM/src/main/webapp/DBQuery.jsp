<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
    <head>
        <title>RDAHMM Main Page</title>
    </head>
    <body>
     <f:view>
      <h:form>
       <h:panelGrid id="form1" border="1" columns="3">
         <h:outputText value="Instructions: provide four-character GPS site code(s), bounding box parameters (optional), and dates of interest to obtain processed coordinates.  REASoN combination and SOPAC globk coordinates are currently available."/>
         <h:outputText value="filler"/>
         <h:outputText value="filler"/>
	  
	 <h:outputText value="Site Code(s):"/>
         <h:inputText id="site_codes" value="#{rdahmmBean.siteCode}"/>
         <h:outputText value="(space-delimited)"/>

	 <h:outputText value="Begin Date:"/>
         <h:inputText id="begin_date" value="#{rdahmmBean.beginDate}"/>
         <h:outputText value="(YYYY-MM-DD)"/>

	 <h:outputText value="End Date"/>
         <h:inputText id="begin_date" value="#{rdahmmBean.endDate}"/>
         <h:outputText value="(YYYY-MM-DD)"/>

	
	 <h:selectBooleanCheckbox id="user_bbox" 
              value="rdahmmBean.bboxChecked"
	      title="Use Bounding Box Settings Below (optional; check box):"/>
         <h:outputText value="filler"/>
         <h:outputText value="filler"/>


	 <h:outputText value="Minimum Latitude:"/>
         <h:inputText id="min_lat" value="#{rdahmm.minLatitude}"/>

	 <h:outputText value="Maximum Latitude:"/>
         <h:inputText id="max_lat" value="#{rdahmm.maxLatitude}"/>

	 <h:outputText value="Minimum Longitude:"/>
         <h:inputText id="min_long" value="#{rdahmm.minLongitude}"/>

	 <h:outputText value="Maximum Latitude:"/>
         <h:inputText id="max_long" value="#{rdahmm.maxLongitude}"/>


         <h:selectOneListbox title="Resource:">
            <f:selectItem itemValue="#{rdahmm.procCoords}"
                          itemLabel="Processed Coordinates"/>
         </h:selectOneListbox>
         <h:outputText value="filler"/>
         <h:outputText value="filler"/>

         <h:selectOneListbox title="Context Group"
                             value="#{rdahmmBean.contextGroup}">
	     <f:selectItem itemValue="reasonComb" itemLabel="REASoN combination"/>
	     <f:selectItem itemValue="sopacGlobk" itemLabel="SOPAC GLOBK"/>
	     <f:selectItem itemValue="jplGipsy" itemLabel="JPL GIPSY"/>
	     <f:selectItem itemValue="usgsGlobk" itemLabel="USGS GLOBK"/>
         </h:selectOneListbox>

         <h:outputText value="filler"/>
         <h:outputText value="filler"/>

	 <h:outputText value="Context Id:"/>
         <h:inputText id="context_id" value="#{rdahmmBean.contextId}"/>
         <h:outputText value="filler"/>

       </h:panelGrid>
       <h:commandButton id="button3" value="Query Database"/>
   </h:form>	
  </f:view>
  </body>
</html>
