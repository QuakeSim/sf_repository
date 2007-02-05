<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>RDAHMM Minimalist Input</title>

  <script type="text/javascript" src="/yui_0.12.2/build/yahoo/yahoo.js"></script>
  <script type="text/javascript" src="/yui_0.12.2/build/event/event.js"></script>
  <script type="text/javascript" src="/yui_0.12.2/build/dom/dom.js"></script>

  <script type="text/javascript" src="/yui_0.12.2/build/calendar/calendar.js"></script>
  <link type="text/css" rel="stylesheet" href="/yui_0.12.2/build/calendar/assets/calendar.css">

  </head>


 <body>
  <script>
	//Set up the object and add a listener.
	YAHOO.namespace("example.calendar");
	function init() {
	  YAHOO.example.calendar.cal1=new YAHOO.widget.Calendar("cal1","cal1Container");
	  YAHOO.example.calendar.cal1.render();

	YAHOO.util.Event.addListener(window,"load",init);

	//Add an alert window.
	var mySelectHandler=function(type,args,obj) {
		var dates=args[0];
		var date=dates[0];
		var year=date[0],month=date[1],day=date[2];
		var startDate=year+"-"+month+"-"+day;

		var newStartDateVal=document.getElementById("form1:beginDate");
	        newStartDateVal.setAttribute("value",startDate);
        }

	YAHOO.example.calendar.cal1.selectEvent.subscribe(mySelectHandler,YAHOO.example.calendar.cal1, true);
	YAHOO.example.calendar.cal1.render();
	}
	YAHOO.util.Event.addListener(window,"load",init);
  </script>

  Here is the caledar <br>
  <div id="cal1Container"></div>

  The input data URL is obtained directly from the GRWS web service
  as a return type.
	

  <f:view>        
    <h:form id="form1">
    <b>Input Parameters</b>
    <h:panelGrid columns="3" border="1">

       <h:outputText value="Site Code"/>
       <h:inputText id="siteCode" value="#{simpleRdahmmClientBean.siteCode}"
                     required="true"/>
       <h:message for="siteCode" showDetail="true" showSummary="true" errorStyle="color: red"/>


       <h:outputText value="Begin Date"/>
       <h:inputText id="beginDate" value="#{simpleRdahmmClientBean.beginDate}"
                     required="true"/>
       <h:message for="beginDate" showDetail="true" showSummary="true" errorStyle="color: red"/>


       <h:outputText value="End Date"/>
       <h:inputText id="endDate" value="#{simpleRdahmmClientBean.endDate}"
                     required="true"/>
       <h:message for="endDate" showDetail="true" showSummary="true" errorStyle="color: red"/>

       <h:outputText value="Number of Model States"/>	
       <h:inputText id="nmodel" value="#{simpleRdahmmClientBean.numModelStates}"
                     required="true"/>
       <h:message for="nmodel" showDetail="true" showSummary="true" errorStyle="color: red"/>

    </h:panelGrid>

    <h:commandButton value="Submit"
                     action="#{simpleRdahmmClientBean.runBlockingRDAHMM2}"/>
    </h:form>

  </f:view>



 </body>
</html>
