<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
<head>
<title>Edit defalut parameters</title>
</head>
<body>
<f:view>
<%-- Javascript Toolbox --%>
<script type="text/ecmascript" src="<h:outputText value="#{facesContext.externalContext.requestContextPath}"/>/scripts/date.js"> </script>

<%-- Yahoo UI --%>
<script type="text/javascript" src="/yui_0.12.2/build/yahoo/yahoo.js"></script>
<script type="text/javascript" src="/yui_0.12.2/build/event/event.js"></script>
<script type="text/javascript" src="/yui_0.12.2/build/dom/dom.js"></script>

<script type="text/javascript" src="/yui_0.12.2/build/calendar/calendar.js"></script>
<link type="text/css" rel="stylesheet" href="/yui_0.12.2/build/calendar/assets/calendar.css">

<style>
#beginDateContainer { display:none; position:absolute; }
#endDateContainer { display:none; position:absolute;  }
</style>

<script>
YAHOO.namespace("example.calendar");

function beginDateHandler(type,args,obj) {
	var dates=args[0];
	var date=dates[0];
	var year=date[0],month=date[1],day=date[2];
	var strDate=year+"-"+month+"-"+day;
	var newDateVal=document.getElementById("form1:beginDate");
	newDateVal.setAttribute("value",strDate);
}

function endDateHandler(type,args,obj) {
	var dates=args[0];
	var date=dates[0];
	var year=date[0],month=date[1],day=date[2];
	var strDate=year+"-"+month+"-"+day;
	var newDateVal=document.getElementById("form1:endDate");
	newDateVal.setAttribute("value",strDate);
}

function checkDate() {
	var beginDateObj = Date.parseString(document.forms.form1["form1:beginDate"].value, 'yyyy-MM-dd');
	var endDateObj = Date.parseString(document.forms.form1["form1:endDate"].value, 'yyyy-MM-dd');
	if (beginDateObj.isBefore(endDateObj)) {
		return true;
	} else {
		alert("Begin date should be before end date");
		return false;
	} 
}

function showAndFocus() {
	YAHOO.example.calendar.beginDate.show;
	YAHOO.example.calendar.beginDate.focus;
}

function init() 
{
	var beginDateObj = Date.parseString(document.forms.form1["form1:beginDate"].value, 'yyyy-MM-dd');
	YAHOO.example.calendar.beginDate = new YAHOO.widget.Calendar("beginDate","beginDateContainer", { title:"Choose a start date:", close:true, pagedate:beginDateObj.format('MM/yyyy'), selected:beginDateObj.format('MM/dd/yyyy') } );
	YAHOO.example.calendar.beginDate.selectEvent.subscribe(beginDateHandler, YAHOO.example.calendar.beginDate, true);
	YAHOO.example.calendar.beginDate.render();
	YAHOO.util.Event.addListener("form1:beginDateImg", "mouseover", YAHOO.example.calendar.beginDate.show, YAHOO.example.calendar.beginDate, true); 

	var endDateObj = Date.parseString(document.forms.form1["form1:endDate"].value, 'yyyy-MM-dd');
	YAHOO.example.calendar.endDate = new YAHOO.widget.Calendar("endDate","endDateContainer", { title:"Choose an end date:", close:true, pagedate:endDateObj.format('MM/yyyy'), selected:endDateObj.format('MM/dd/yyyy') } );
	YAHOO.example.calendar.endDate.selectEvent.subscribe(endDateHandler, YAHOO.example.calendar.endDate, true);
	YAHOO.example.calendar.endDate.render();
	YAHOO.util.Event.addListener("form1:endDateImg", "mouseover", YAHOO.example.calendar.endDate.show, YAHOO.example.calendar.endDate, true); 
}

// Listener 
YAHOO.util.Event.addListener(window, "load", init);
</script>

	<h:form id="form1">
		<h:panelGrid id="master_table" columns="1" border="1"> 
		<h:panelGrid columns="3"> 
		<h:outputText value="Site Code(s):"/>
		<h:inputText id="siteCode" size="5" value="#{stfilterBean._siteCode}"/>
		<h:outputText value="(space-delimited)"/>
		<h:message for="siteCode" showDetail="true" showSummary="true" errorStyle="color: red"/>
		</h:panelGrid>
		
		<h:panelGrid columns="2" border="1">
		<h:panelGrid columns="3"> 
		<h:outputText value="Begin Date:"/>
			<h:inputText id="beginDate" value="#{stfilterBean._beginDate}"/>
			<h:graphicImage id="beginDateImg" value="/calendar.gif"/>
			<f:verbatim>
				<div id="beginDateContainer"></div> 
			</f:verbatim>
		</h:panelGrid>
		
		<h:panelGrid columns="3"> 
		<h:outputText value="End Date"/>
			<h:inputText id="endDate" value="#{stfilterBean._endDate}"/>
			<h:graphicImage id="endDateImg" value="/calendar.gif"/>
			<f:verbatim>
				<div id="endDateContainer"></div> 
			</f:verbatim>
		</h:panelGrid>
		</h:panelGrid>
		
		
		<h:panelGrid border="1">
		<h:panelGrid columns="2">
		<h:selectBooleanCheckbox value="#{stfilterBean._bboxChecked}" title="Use Bounding Box Settings Below (optional; check box):"/>
		<h:outputText value="Use Bounding Box Settings"/>
		</h:panelGrid>
		<h:panelGrid columns="2">
		<h:panelGrid columns="2"> 
		<h:outputText value="Minimum Latitude:"/>
		<h:inputText size="10" value="#{stfilterBean._minLatitude}"/>
		</h:panelGrid>
		<h:panelGrid columns="2"> 
		<h:outputText value="Maximum Latitude:"/>
		<h:inputText size="10" value="#{stfilterBean._maxLatitude}"/>
		</h:panelGrid>
		</h:panelGrid>
		
		<h:panelGrid columns="2">
		<h:panelGrid columns="2"> 
		<h:outputText value="Minimum Longitude:"/>
		<h:inputText size="10" value="#{stfilterBean._minLongitude}"/>
		</h:panelGrid>
		<h:panelGrid columns="2"> 
		<h:outputText value="Maximum Longitude:"/>
		<h:inputText size="10" value="#{stfilterBean._maxLongitude}"/>
		</h:panelGrid>
		</h:panelGrid>
		</h:panelGrid>
		
		
		
		<h:panelGrid columns="2" border="1">
		<h:outputText value="Resource"/>
		<h:selectOneListbox title="Resource:" value="#{stfilterBean._resource}" size="1">
		<f:selectItem
		itemValue="procCoords"
		itemLabel="Processed Coordinates"/>
		</h:selectOneListbox>
		</h:panelGrid>
		
		<h:panelGrid columns="2" border="1"> 
		<h:outputText value="Context Group"/>
		<h:selectOneListbox title="Context Group" value="#{stfilterBean._contextGroup}" size="1">
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
		<h:inputText size="5" value="#{stfilterBean._contextId}"/>
		<h:outputText value="(4=current REASoN combination coordinates)"/>
		</h:panelGrid>
		
		</h:panelGrid>
		
		<h:message for="master_table" showDetail="true" showSummary="true" errorStyle="color: red"/>
		
		
		<h:panelGrid columns="1" border="1">
		<h:panelGrid columns="3" border="0">
		
		<h:outputText value="Residual Option:"/>
		<h:inputText id="resOption" value="#{stfilterBean._resOption}" required="true"/>
		<h:message for="resOption" showDetail="true" showSummary="true" errorStyle="color: red"/>

		<h:outputText value="Term Option:"/>
		<h:inputText id="termOption" value="#{stfilterBean._termOption}" required="true"/>
		<h:message for="termOption" showDetail="true" showSummary="true" errorStyle="color: red"/>
		
		<h:outputText value="Cutoff Criterion (Year):"/>
		<h:inputText id="cutoffCriterion" value="#{stfilterBean._cutoffCriterion}" required="true"/>
		<h:message for="cutoffCriterion" showDetail="true" showSummary="true" errorStyle="color: red"/>
		
		<h:outputText value="Span to Estimated Jump Apr:"/>
		<h:inputText id="estJumpSpan" value="#{stfilterBean._estJumpSpan}" required="true"/>
		<h:message for="estJumpSpan" showDetail="true" showSummary="true" errorStyle="color: red"/>
		</h:panelGrid>
		
		<h:dataTable value="#{stfilterBean._weakObsCriteria}" var="weakObsCriteria" border="0">
		<h:column>
		<h:outputText value="Weak Obs Criteria (Year):"/> 
		</h:column>
		<h:column>
		<h:inputText id="weakObsCriteria1" value="#{weakObsCriteria.east}" required="true"/>
		</h:column>
		<h:column>
		<h:inputText id="weakObsCriteria2" value="#{weakObsCriteria.north}" required="true"/>
		</h:column>
		<h:column>
		<h:inputText id="weakObsCriteria3" value="#{weakObsCriteria.up}" required="true"/>
		</h:column>
		</h:dataTable>
		
		<h:dataTable value="#{stfilterBean._outlierCriteria}" var="outlierCriteria" border="0">
		<h:column>
		<h:outputText value="Outlier Criteria (mm):"/>
		</h:column>
		<h:column>
		<h:inputText id="outlierCriteria1" value="#{outlierCriteria.east}" required="true"/>
		</h:column>
		<h:column>
		<h:inputText id="outlierCriteria2" value="#{outlierCriteria.north}" required="true"/>
		</h:column>
		<h:column>
		<h:inputText id="outlierCriteria3" value="#{outlierCriteria.up}" required="true"/>
		</h:column>
		</h:dataTable>
		
		<h:dataTable value="#{stfilterBean._badObsCriteria}" var="badObsCriteria" border="0">
			<h:column>
			<h:outputText value="Bad Obs Criteria (mm):"/>
			</h:column>
			<h:column>
			<h:inputText id="badObsCriteria1" value="#{badObsCriteria.east}" required="true"/>
			</h:column>
			<h:column>
			<h:inputText id="badObsCriteria2" value="#{badObsCriteria.north}" required="true"/>
			</h:column>
			<h:column>
			<h:inputText id="badObsCriteria3" value="#{badObsCriteria.up}" required="true"/>
			</h:column>
		</h:dataTable>
		
		<h:dataTable value="#{stfilterBean._timeInterval}" var="timeInterval" border="0">
			<h:column>
			<h:outputText value="Time Interval:"/>
			</h:column>
			<h:column>
			<h:inputText id="timeInterval1" value="#{timeInterval.beginTime}" required="true"/>
			</h:column>
			<h:column>
			<h:inputText id="timeInterval2" value="#{timeInterval.endTime}" required="true"/>
			</h:column>
		</h:dataTable>
		</h:panelGrid>
		<h:commandButton id="BT_SAVE" value="Save" action="#{stfilterBean.savePref}"/>
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
