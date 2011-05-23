<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ page import="java.util.*, java.io.*, java.util.*, java.net.URL, java.lang.*, org.dom4j.*, org.dom4j.io.*, cgl.quakesim.disloc.*, javax.faces.context.ExternalContext, javax.servlet.http.HttpServletRequest, javax.portlet.PortletRequest, javax.faces.context.FacesContext, javax.faces.model.SelectItem, com.db4o.*"%>

<html>
 <head>
 <style>
   .alignTop {
    vertical-align:top;
   }

   .header2 {
    font-family: Arial, sans-serif;
    font-size: 18pt;
    font-weight: bold;
   }
 </style>

	<title>Disloc Load and Delete Project</title>
	
	<link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/quakesim_style.css"/>
	<link rel="stylesheet" type="text/css" href="@host.base.url@@artifactId@/jquery.treeview.css"/>
	<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=put.google.map.key.here" type="text/javascript"></script>      
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
	<script type="text/javascript" src="@host.base.url@@artifactId@/lib/jquery.cookie.js"></script>
	<script type="text/javascript" src="@host.base.url@@artifactId@/jquery.treeview.js"></script>
	<script type="text/javascript" src="@host.base.url@@artifactId@/egeoxml_for_rssdisloc.js"></script>
 </head>
 
 <body onload="myInit()" onunload="GUnload()"> 
	 
	 <script type="text/javascript">
	 //<![CDATA[
	 //Any "onload" operations for subcomponent includes need to go here.
	 function myInit() {
		  //Script below initializes the tree view for the FaultMapPanelFrame sidebar.
		  $("#browser").treeview({
				animated:"normal",
				persist: "cookie"
		  });
	 }
//]]>
</script>
	
	<f:view>
	  
	  <h:outputText id="abdv1" styleClass="header2" value="Results Map"/>   
	  <f:verbatim>
		 <ul>
			<li>Click the earthquake name link to go its location. </li>
			<li>Click the checkbox next to "InSAR Plot" or "Surface Displacement" to toggle results display on/off.</li>
			<li>Click the "InSAR Plot" or "Surface Diplacement" links to download the source KML.
		 </ul>
	  </f:verbatim>
	  <h:form id="refreshPage1">
		 <h:commandLink id="lrilehdk239" action="disloc-this">
			<h:outputText id="feo0re0" value="Refresh Page" />
		 </h:commandLink>
	  </h:form>
	  
	  <h:form id="RssFaultsMap">
		 <h:inputHidden id="faultName" value=""/> 
		 
		 <h:inputHidden id="projectsource" value="#{DislocBean2.projectsource}"/>   
		 <h:inputHidden id="faultlistsize" value="#{DislocBean2.myFaultsForProjectListsize}"/>
		 <h:panelGrid id="gridforbutton" columns="1" border="0" style="vertical-align:top;">
		 </h:panelGrid>
		 <%
	 //<![CDATA[
	 //This is some crappy code for getting the disloc bean object.
	 //Needs to be improved and is probably conceptually flawed.
		 ExternalContext context = null;
		 FacesContext facesContext=FacesContext.getCurrentInstance();
		 try {
		 context=facesContext.getExternalContext();
		 }
		 catch(Exception ex) {
		 out.println(ex.getMessage());
		 }
		 Object requestObj=null;
		 requestObj=context.getRequest();
		 
		 DislocBean dsb = null;
		 
		 if(requestObj instanceof PortletRequest) {
		 dsb = (DislocBean)((PortletRequest)requestObj).getPortletSession().getAttribute("DislocBean2");
		 }
		 else if(requestObj instanceof HttpServletRequest) {
		 dsb = (DislocBean)request.getSession().getAttribute("DislocBean2");
		 }
		 //And this code calls the function that conditionally runs Disloc.
		 HashMap hm = dsb.getDbProjectNameList();
		 //]]>
		 %>
		 
		 <f:verbatim>
			<script type="text/javascript">
			</script>
			
			<h:panelGrid id="faultMapsideGrid" columns="2" border="1">
			  <f:verbatim>
				 <div id="faultMapside" style="width: 300px; height: 800px; overflow:auto;"></div>
			  </f:verbatim>
			  <f:verbatim>
				 <div id="faultMap" style="width: 850px; height: 800px;"></div>
			  </f:verbatim> 
			</h:panelGrid>
		 </f:verbatim>
		 
		 <f:verbatim>   
			<script type="text/javascript">
			  //<![CDATA[ //Use for formatting.
			  // These are used by the fault map 	
			  var faultMap=null;
			  faultMap=new GMap2(document.getElementById("faultMap"));
			  var flistsize = document.getElementById("RssFaultsMap:faultlistsize");
			  var flistpolyline = new Array();
			  for (var nA = 0 ; nA < flistsize.value ; nA++){
			  flistpolyline[nA] = null;
			  }
			  
			  var kmllist = ["@host.base.url@/overm5.kml"];	
			  
			  exmlFMap = new EGeoXml("exmlFMap", faultMap, kmllist, {sidebarfn:myside,nozoom:true,sidebarid:"faultMapside",parentformofsidebarid:"RssFaultsMap",clickpolyobjfn:clickpolyobj,iwwidth:300,off:true,width:1});       
			  exmlFMap.parse();
			  
			  function jsleep(s){
			  s=s*1000;
			  var a=true;
			  var n=new Date();
			  var w;
			  var sMS=n.getTime();
			  while(a){
			  w=new Date();
			  wMS=w.getTime();
			  if(wMS-sMS>s) a=false;
			  }
			  }
			  
			  jsleep(1);
			  
			  faultMap.addMapType(G_PHYSICAL_MAP);
			  faultMap.setMapType(G_PHYSICAL_MAP);
			  faultMap.setCenter(new GLatLng(35.0,-118.5),6);
			  faultMap.addControl(new GLargeMapControl());
			  faultMap.addControl(new GMapTypeControl());
			  
			  // Handle sidebar events.  Param1 is the fault+segment name, param2 is the polyline.
			  var faultField=document.getElementById("RssFaultsMap:faultName");
			  GEvent.addDomListener(faultField,"click",function(param1,param2,param3,param4){
			  
			  var interpHead=" (InterpId:";
			  var faultName,segmentName;
			  var segmentNamePlusId, interpId;
	
			  var newElement1=document.getElementById("RssFaultsMap:faultName");
			  
			  // Parse out the segment name
			  if((param1 != null) && (param2 != null) && (param3 != null)){
			  if(param1.indexOf("-") > -1) {
			  faultName=param1.substring(0,param1.indexOf(" - "));
			  segmentName=param1.substring(param1.indexOf(" - ")+4,param1.indexOf(interpHead));
			  interpId=param1.substring(param1.indexOf(interpHead)+interpHead.length+1,param1.length-1);
			  }
			  else {
			  // No segment name
			  faultName=param1.substring(0,param1.indexOf(interpHead));
			  segmentName="N/A";
			  interpId=param1.substring(param1.indexOf(interpHead)+interpHead.length+1,param1.length-1);
			  }
	
			  faultName=faultName+"@"+segmentName+"%"+interpId;
	
			  // We're going to use just the short name with the new search feature.
			  newElement1.value = param1;
			  
			  // Trigger the polyline click event to show the popup window.
			  if(param3 != 'frommap')
			  GEvent.trigger(param2,'click');						
			  }
			  
			  });
			  
			  // This function overrides the default side panel.
			  function myside(myvar,name,type,i,graphic) {
			  if((type == "polyline" || type == "polygon") || type == "GroundOverlay") {
			  var s = name.split("_n_")[1];
			  return '<a id="'+name+'" href="javascript:GEvent.trigger(document.getElementById(\'RssFaultsMap:faultName\'),\'click\',\''+name+'\','+myvar+'.gpolyobjs['+i+'], \'script\', '+myvar+'.gpolyobjs_desc['+i+'])">' + s + '</a>';
			  }
			  
			  return "";
			  }
				  
			  // This overrides the default clickpolyobjfn of egeoxml.js
			  function clickpolyobj(p, name, desc) {
			  GEvent.trigger(document.getElementById('RssFaultsMap:faultName'),'click', name, p, 'frommap', desc);	    
			  }
			  //]]> //Keep for formatting
			</script>
		 </f:verbatim>
		 
	  </h:form>
	  <h:form id="refreshMe2">
		 <h:commandLink id="jdflkdjkjfd" action="disloc-this">
			<h:outputText id="kjdkjfdlkjdfldjk" value="Refresh Page" />
		 </h:commandLink>
	  </h:form>
	  
	</f:view>
 </body>
</html>
