<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>

<%@page import="java.util.*, cgl.sensorgrid.sopac.gps.GetStationsRSS,
cgl.sensorgrid.gui.google.MapBean, java.io.*"%>
<jsp:useBean id="RSSBeanID" scope="session" class="cgl.sensorgrid.sopac.gps.GetStationsRSS"/>
<jsp:useBean id="MapperID" scope="session" class="cgl.sensorgrid.gui.google.Mapper"/>

<%
Vector networkNames = RSSBeanID.networkNames();

//Vector stationsVec = RSSBeanID.getAllStationsVec();
String mapcenter_x = "33.036";
String mapcenter_y = "-117.24";

String [] center_xy = RSSBeanID.getMapCenter();
mapcenter_x = center_xy[0];
mapcenter_y = center_xy[1];
%>
<html>
  <head>
 <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxOZ1VuCkrWUtft6jtubycBSP8m3Tdo1MGc8NudJLOupgVl5cGRRkVwnXuOTLyf1PITz2N2IjgsfSkw"
      type="text/javascript"></script>
  </head>
  <body>

  <script type="text/javascript" src="/yui_0.12.2/build/yahoo/yahoo.js"></script>
  <script type="text/javascript" src="/yui_0.12.2/build/event/event.js"></script>
  <script type="text/javascript" src="/yui_0.12.2/build/dom/dom.js"></script>

  <script type="text/javascript" src="/yui_0.12.2/build/calendar/calendar.js"></script>
  <link type="text/css" rel="stylesheet" href="/yui_0.12.2/build/calendar/assets/calendar.css">

  <style>
   #cal1Container { display:none; position:relative; width:200px;}
   #cal2Container { display:none; position:relative; width:200px}
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
	     	YAHOO.example.calendar.cal1=new YAHOO.widget.Calendar("cal1","cal1Container",{title:"Beginning Date:",close:true});
 	     	YAHOO.example.calendar.cal1.selectEvent.subscribe(myBeginDateHandler1,YAHOO.example.calendar.cal1, true);
	     	YAHOO.example.calendar.cal1.render();
		YAHOO.util.Event.addListener("form1:cal1Button","mouseover",YAHOO.example.calendar.cal1.show,YAHOO.example.calendar.cal1,true);

	
		//Set up the second calendar
	     	YAHOO.example.calendar.cal2=new YAHOO.widget.Calendar("cal2","cal2Container",{title:"Ending Date:",close:true});
		YAHOO.example.calendar.cal2.selectEvent.subscribe(myEndDateHandler2,YAHOO.example.calendar.cal2, true);
	     	YAHOO.example.calendar.cal2.render();
		YAHOO.util.Event.addListener("form1:cal2Button","mouseover",YAHOO.example.calendar.cal2.show,YAHOO.example.calendar.cal2,true);

	}

        YAHOO.util.Event.addListener(window,"load",init);

  </script>
    <table>
      <tr>
        <td width="650" colspan="3">
          <b><font size="4" face="Verdana">RDAHMM GPS Data Analysis</font></b><p>
            <font face="Verdana" size="2">Click on a station symbol for more
              information on a particular station.  Then click the "Run RDAHMM"
				 link below the map.
             </font><p></p></td>
            </tr>
      <tr>
        <td valign="top" width="50">
          <div id="networksDiv"> Network Names and Colors     </div>
        </td>

        <td valign="top" width="600">
          <div id="map" style="width: 600px; height: 600px">      </div>
        </td>

     	<td valign="top">
      <table>
      <tr>
      <td>
      <f:view>
       <h:form id="form1">

	    <b>Input Parameters</b>

   	 <h:panelGrid id="InputGridPanel" columns="4" border="1">

      	 <h:outputText value="Project Name:"/>
       	<h:inputText id="projectName" 
							value="#{rdahmmBean.projectName}" 
       	 	         required="true"/>
       	<h:message for="projectName" 
							showDetail="true" 
							showSummary="true" 
							errorStyle="color: red"/>
         <h:outputText value=""/>

	 		<h:outputText value="Number of Model States:"/>
       	<h:inputText id="nmodel" value="#{rdahmmBean.numModelStates}"
                     required="true"/>
       	<h:message for="nmodel" 
							showDetail="true" 
							showSummary="true" 
							errorStyle="color: red"/>
         <h:outputText value=""/>


       <h:outputText value="Begin Date"/>
       <h:inputText id="beginDate" value="#{rdahmmBean.beginDate}"
                     required="true"/>
       <h:graphicImage id="cal1Button" url="calendar.gif"/>

       <h:message for="beginDate" showDetail="true" showSummary="true" errorStyle="color: red"/>


       <h:outputText value="End Date"/>
       <h:inputText id="endDate" value="#{rdahmmBean.endDate}"
                     required="true"/>

       <h:graphicImage id="cal2Button" url="calendar.gif"/>
       <h:message for="endDate" showDetail="true" showSummary="true" errorStyle="color: red"/>


       <h:outputText value="Site Code"/>
       <h:inputText id="station_name" value="#{rdahmmBean.siteCode}"/>
       <h:message for="station_name" showDetail="true" showSummary="true" errorStyle="color: red"/>
         <h:outputText value=""/>

       </h:panelGrid>

       <h:commandLink action="#{rdahmmBean.runBlockingRDAHMM_Full}">
            <h:outputText value="Run RDAHMM"/>
       </h:commandLink>

       </h:form>
	    </f:view>

      </td>
      </tr>
      <tr>      
      <td valign="top" align="left">
       <div id="cal1Container"></div>
       <div id="cal2Container"></div>
      </td>
      </tr>
     <tr>
       <td>

     <f:view>
      <h:panelGrid id="OutputGridPanel"
				rendered="#{!(empty rdahmmBean.rdahmmRunValues)}">
       <h:outputText  escape="false" value="<b>Output Values</b>"/>
       <h:outputLink value="#{rdahmmBean.projectInput}" target="_blank">
       <h:outputText value="Input File"/>
       </h:outputLink>

       <h:outputLink value="#{rdahmmBean.projectRange}" target="_blank">
       <h:outputText value="Range"/>
       </h:outputLink>

       <h:outputLink target="_blank" value="#{rdahmmBean.projectQ}">
       <h:outputText value="Optimal State Sequence File (Q)"/>
       </h:outputLink>


       <h:outputLink target="_blank" value="#{rdahmmBean.projectA}">
       <h:outputText value="Model Transition Probability (A)"/>
       </h:outputLink>


       <h:outputLink target="_blank" value="#{rdahmmBean.projectB}">
       <h:outputText value="Model Output Distribution (B)"/>
       </h:outputLink>


       <h:outputLink target="_blank" value="#{rdahmmBean.projectL}">
       <h:outputText value="Model Log Likelihood (L)"/>
       </h:outputLink>>


       <h:outputLink target="_blank" value="#{rdahmmBean.projectPi}">
       <h:outputText value="Model Initial State Probability (PI)"/>
       </h:outputLink>

       <h:outputLink target="_blank" value="#{rdahmmBean.projectMinval}">
       <h:outputText value="Minimum Value"/>
       </h:outputLink>

       <h:outputLink target="_blank" value="#{rdahmmBean.projectMaxval}">
       <h:outputText value="Maximum Value"/>
       </h:outputLink>

       <h:outputLink target="_blank" value="#{rdahmmBean.projectGraphX}">
       <h:outputText value="Plot of X Values"/>
       </h:outputLink>

       <h:outputLink target="_blank" value="#{rdahmmBean.projectGraphY}">
       <h:outputText value="Plot of Y Values"/>
       </h:outputLink>

       <h:outputLink target="_blank" value="#{rdahmmBean.projectGraphZ}">
       <h:outputText value="Plot of Z Values"/>
       </h:outputLink>
	

      </h:panelGrid>

    </f:view>
	  </td>
    </tr>
     </table>
    </tr>
    </table>


    <script type="text/javascript">
        var req;
        var baseIcon = new GIcon();
        baseIcon.shadow = "http://www.google.com/mapfiles/shadow50.png";
        baseIcon.iconSize = new GSize(15, 20);
        baseIcon.shadowSize = new GSize(10, 10);
        baseIcon.iconAnchor = new GPoint(1, 10);
        baseIcon.infoWindowAnchor = new GPoint(5, 1);
        baseIcon.infoShadowAnchor = new GPoint(5, 5);

        // Create the map
        var map = new GMap(document.getElementById("map"));
        map.addControl(new GLargeMapControl());
        map.addControl(new GMapTypeControl());
        map.addControl(new GScaleControl());
        map.centerAndZoom(new GPoint(<%=mapcenter_y%>, <%=mapcenter_x%>), 10);

        var colors = new Array (6);
        colors[0]="red";
        colors[1]="green";
        colors[2]="blue";
        colors[3]="black";
        colors[4]="white";
        colors[5]="yellow";
        colors[6]="purple";
        colors[7]="brown";

        var networkInfo = new Array (<%=networkNames.size()%>);
        for (i = 0; i < networkInfo.length; ++ i){
          networkInfo [i] = new Array (2);
        }

        function overlayNetworks(){
          var icon = new GIcon(baseIcon);
          icon.image = "http://labs.google.com/ridefinder/images/mm_20_green.png";

          <%
          for (int j = 0; j < networkNames.size(); j++) {
            String networkName = (String)networkNames.get(j);
            Vector stationsVec = RSSBeanID.getStationsVec(networkName);
            %>
            networkInfo [<%=j%>] [0] = "<%=networkName%>";

            var k;
            if(<%=j%>>=colors.length)
            k = 0;
            else
            k=<%=j%>;
            icon.image = "http://labs.google.com/ridefinder/images/mm_20_" + colors[k] + ".png";
            networkInfo [<%=j%>] [1] = "http://labs.google.com/ridefinder/images/mm_20_" + colors[k] + ".png";

            var stationCount = <%=stationsVec.size()%>;
            var stations = new Array (stationCount);
            var Markers = new Array (stationCount);
            for (i = 0; i < stations.length; ++ i){
              stations [i] = new Array (3);
            }

            <%
            for (int i = 0; i < stationsVec.size(); i++) {
              String name = (String)stationsVec.get(i);
              String lat = RSSBeanID.getStationInfo(name)[0];
              String lon = RSSBeanID.getStationInfo(name)[1];
              %>
              stations [<%=i%>] [0] = "<%=name%>";
              stations [<%=i%>] [1] = "<%=lat%>";
              stations [<%=i%>] [2] = "<%=lon%>";

              Markers[<%=i%>] = createMarker("<%=networkName%>", "<%=name%>", "<%=lon%>", "<%=lat%>", icon);
              map.addOverlay(Markers[<%=i%>]);
	
              <%
            }
          }
          %>
        }

        function createMarker(networkName, name, lon, lat, icon) {
          var marker = new GMarker(new GPoint(lon, lat),icon);
          // Show this marker's name in the info window when it is clicked
          var html = "<b>Station Name= </b>" + name + "<br><b>Lat=</b>" + lat + "<br><b>Lon= </b>" + lon + "<br><b>Network= </b>" + networkName;


          GEvent.addListener(marker, "click", function() {
            marker.openInfoWindowHtml(html);});

          GEvent.addListener(marker, "click", function() {
		var newElement=document.getElementById("form1:station_name");
		newElement.setAttribute("value",name);
	  });

          return marker;
          }

        overlayNetworks();



        function printNetworkColors (array)
        {
          var html = "<table border='0'><tr><td><b>Network</b></td><td nowrap><b>Icon Color<b></td></tr>";

          var row;
          for (row = 0; row < array.length; ++ row)
          {
            html = html + " <tr>";
            var col;
            for (col = 0; col < array [row] . length; ++ col){
              if(col==0)
              html = html + "  <td>" + array [row] [col] + "</td>";
              if(col==1)
              html = html + "  <td align='center'><img border=0 src=" + array [row] [col] + "></td>";

            }
            html = html + " </tr>";
          }
           html = html + "</table>";
           var idiv = window.document.getElementById("networksDiv");
           idiv.innerHTML = html;
         }
         printNetworkColors(networkInfo);

	//Needed for Firefox 2.0 compatibility
	function getScrolling() {
	    var x = 0; var y = 0;
    		if (document.body && document.body.scrollLeft && !isNaN(document.body.scrollLeft)) {
	        x = document.body.scrollLeft;
    		} else if (window.pageXOffset && !isNaN(window.pageXOffset)) {
        	x = window.pageXOffset;
    		}
    		if (document.body && document.body.scrollTop && !isNaN(document.body.scrollTop)) {
        	y = document.body.scrollTop;
    		} else if (window.pageYOffset && !isNaN(window.pageYOffset)) {
        	y = window.pageYOffset;
    		}
    		return x + "," + y;
	}

      </script>


     <hr/>
     <f:view>
       <h:form>
         <h:commandLink action="rdahmm-back">
            <h:outputText value="#{rdahmmBean.codeName} Main Menu"/>
         </h:commandLink>
        </h:form>
      </f:view>

     </body>
</html>
