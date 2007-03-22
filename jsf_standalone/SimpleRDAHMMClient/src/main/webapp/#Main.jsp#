<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<html>
  <head>
        <title>RDAHMM Minimalist Input</title>
  </head>


 <body>
  <script type="text/javascript" src="/yui_0.12.2/build/yahoo/yahoo.js"></script>
  <script type="text/javascript" src="/yui_0.12.2/build/event/event.js"></script>
  <script type="text/javascript" src="/yui_0.12.2/build/dom/dom.js"></script>

  <script type="text/javascript" src="/yui_0.12.2/build/calendar/calendar.js"></script>
  <link type="text/css" rel="stylesheet" href="/yui_0.12.2/build/calendar/assets/calendar.css">

  <style>
     #cal1Button { position:absolute; left:10px; top:10px; width:200px; }
     #cal1Container { display:none; position:absolute; left:10px; top:10px;  }

     #cal2Button { position:absolute; left:10px; top:10px; width:200px; }
     #cal2Container { display:none; position:absolute; left:10px; top:10px;  }
  </style>

  <script>
	//Set up the object and add a listener.
	YAHOO.namespace("example.calendar");

	//Add an alert window.
	function myBeginDateHandler1(type,args,obj) {
		var dates=args[0];
		var date=dates[0];
		var year=date[0],month=date[1],day=date[2];
		var startDate=year+"-"+month+"-"+day;
		var newStartDateVal=document.getElementById("form1:beginDate");
	        newStartDateVal.setAttribute("value",startDate);
       	}

	//Add an alert window.
	function myEndDateHandler2(type,args,obj) {
		var dates=args[0];
		var date=dates[0];
		var year=date[0],month=date[1],day=date[2];
		var endDate=year+"-"+month+"-"+day;
		var newEndDateVal=document.getElementById("form1:endDate");
	        newEndDateVal.setAttribute("value",endDate);
       	}

	function init() {
	     	//Set up the first calendar
	     	YAHOO.example.calendar.cal1=new YAHOO.widget.Calendar("cal1","cal1Container",{title:"Choose a date1:",close:true});
 	     	YAHOO.example.calendar.cal1.selectEvent.subscribe(myBeginDateHandler1,YAHOO.example.calendar.cal1, true);
	     	YAHOO.example.calendar.cal1.render();
		YAHOO.util.Event.addListener("form1:cal1Button","mouseover",YAHOO.example.calendar.cal1.show,YAHOO.example.calendar.cal1,true);

	
		//Set up the second calendar
	     	YAHOO.example.calendar.cal2=new YAHOO.widget.Calendar("cal2","cal2Container",{title:"Choose a date2:",close:true});
		YAHOO.example.calendar.cal2.selectEvent.subscribe(myEndDateHandler2,YAHOO.example.calendar.cal2, true);
	     	YAHOO.example.calendar.cal2.render();
		YAHOO.util.Event.addListener("form1:cal2Button","mouseover",YAHOO.example.calendar.cal2.show,YAHOO.example.calendar.cal2,true);

	}

        YAHOO.util.Event.addListener(window,"load",init);

  </script>



  Here is the calendar <br>
  <div id="cal1Container"></div>
  <p>
  <div id="cal2Container"></div>

  The input data URL is obtained directly from the GRWS web service
  as a return type.
	

  <f:view>        

    <h:form id="form1">
    <b>Input Parameters</b>
    <h:panelGrid columns="4" border="1">

       <h:outputText value="Site Code"/>
       <h:inputText id="siteCode" value="#{simpleRdahmmClientBean.siteCode}"
                     required="true"/>
       <h:message for="siteCode" showDetail="true" showSummary="true" errorStyle="color: red"/>
       <h:outputText value=""/>


       <h:outputText value="Begin Date"/>
       <h:inputText id="beginDate" value="#{simpleRdahmmClientBean.beginDate}"
                     required="true"/>
       <h:message for="beginDate" showDetail="true" showSummary="true" errorStyle="color: red"/>
       <h:commandButton 
              id="cal1Button"
	      image="#{facesContext.externalContext.requestContextPath}/calendar.gif"/>


       <h:outputText value="End Date"/>
       <h:inputText id="endDate" value="#{simpleRdahmmClientBean.endDate}"
                     required="true"/>
       <h:message for="endDate" showDetail="true" showSummary="true" errorStyle="color: red"/>
       <h:commandButton 
              id="cal2Button"
	      image="#{facesContext.externalContext.requestContextPath}/calendar.gif" />


       <h:outputText value="Number of Model States"/>	
       <h:inputText id="nmodel" value="#{simpleRdahmmClientBean.numModelStates}"
                     required="true"/>
       <h:message for="nmodel" showDetail="true" showSummary="true" errorStyle="color: red"/>
       <h:outputText value=""/>

    </h:panelGrid>

    <h:commandButton value="Submit"
                     action="#{simpleRdahmmClientBean.runBlockingRDAHMM2}"/>
    </h:form>

  </f:view>



 </body>
</html>
