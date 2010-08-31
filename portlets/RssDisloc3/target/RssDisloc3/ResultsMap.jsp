<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ page import="java.util.*, java.io.*, java.util.*, java.net.URL, java.lang.*, org.dom4j.*, org.dom4j.io.*, cgl.quakesim.disloc.*, javax.faces.context.ExternalContext, javax.servlet.http.HttpServletRequest, javax.portlet.PortletRequest, javax.faces.context.FacesContext, javax.faces.model.SelectItem, com.db4o.*"%>

<html>
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

<head>
 <title>Disloc Load and Delete Project</title>
</head>

<body>

<f:view>
  <h:form id="bidne">
   <h:outputText id="abdv1" styleClass="header2" value="Results Map"/>   
  </h:form>
<p/>
 <h:form id="RssFaultsMap">
  <h:inputHidden id="faultName" value=""/> 
  <h:inputHidden id="projectsource" value="#{DislocBean2.projectsource}"/>   
  <h:inputHidden id="faultlistsize" value="#{DislocBean2.myFaultsForProjectListsize}"/>
  <h:panelGrid id="gridforbutton" columns="1" border="0" style="vertical-align:top;">
    <f:verbatim>1. Click the button below to copy chosen projects to my projects. </f:verbatim>
    <f:verbatim>2. Check the first check box to draw the fault and the second box to copy the project before clicking the 'Copy to my projects'</f:verbatim>
    <h:commandButton id="copytomyprojects" value="Copy to my projects" action="#{DislocBean2.toggleCopyRssProject}"/> 
  </h:panelGrid>
  <f:verbatim>
    <link rel="stylesheet" type="text/css" href="http://129.79.49.68:8080/RssDisloc3/quakesim_style.css">
    <link rel="stylesheet" type="text/css" href="http://129.79.49.68:8080/RssDisloc3/jquery.treeview.css">
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAgYAii_xZWT_zf_1Dj7VvgBTf0RZ3CvQOmi-GOjEFoiamz50c8BRdcsDMSPvaTAMTVPL7sMxMzuZWCQ" type="text/javascript"></script>      
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
    <script type="text/javascript" src="http://129.79.49.68:8080/RssDisloc3/lib/jquery.cookie.js"></script>
    <script type="text/javascript" src="http://129.79.49.68:8080/RssDisloc3/jquery.treeview.js"></script>
    <script type="text/javascript" src="http://129.79.49.68:8080/RssDisloc3/egeoxml_for_rssdisloc.js"></script>
    <script language="JavaScript">
     
      Array.prototype.remove = function(e) {
       for(var nA = 0; nA < this.length; nA++ ) {
        if(this[nA]==e)
         this.splice(nA,1);
       }
      }

      togglerssbox = function(n) {
      var source =document.getElementById("RssFaultsMap:projectsource");
   
      var a = new Array();
      if (source.value != "")
       a =source.value.split("/");   
       var b=0;

       for (var nA = 0 ; nA < a.length ; nA++) {
        if (a[nA] == n) 
         b = 1;    
       }

     if (b== 0)
      a.push(n);   
 
     if (b== 1)
      a.remove(n);
   
     var c = "";   

     for (nA = 0 ; nA < a.length ; nA++) {    
      if (nA==0)
       c = a[nA];    
      else
       c = c + "/" + a[nA];
      }
      source.setAttribute("value",c);    
     }
 
   </script>

      <%

	ExternalContext context = null;
	FacesContext facesContext=FacesContext.getCurrentInstance();
	/*
	if(facesContext==null)
	  System.out.println("[LoadProject.jsp] a null facesContext error");
	else
	  System.out.println("[LoadProject.jsp] the facesContext isn't null");
	*/

	try {
	  context=facesContext.getExternalContext();
	}
	catch(Exception ex) {
	  ex.printStackTrace();
	}

	Object requestObj=null;
	requestObj=context.getRequest();
	
	DislocBean dsb = null;
	
	List l = null;
	List l2 = null;
	      
	/*
	Enumeration e = application.getAttributeNames();
	while(e.hasMoreElements())
	  System.out.println("[Disloc3/LoadProject] " + e.nextElement());
	*/

	

	if(requestObj instanceof PortletRequest) {
	  // System.out.println("[LoadProject.jsp] requestObj is an instance of PortletRequest");
	  dsb = (DislocBean)((PortletRequest)requestObj).getPortletSession().getAttribute("DislocBean2");
	}
	
	else if(requestObj instanceof HttpServletRequest) {
	  // System.out.println("[LoadProject.jsp] requestObj is an instance of HttpServletRequest");
	  dsb = (DislocBean)request.getSession().getAttribute("DislocBean2");
	}

      
	// System.out.println("[ResultsMap.jsp] " + dsb.getCodeName());
	// System.out.println("[ResultsMap.jsp] " + dsb.getMyFaultsForProjectListsize());

      %>



    <h:panelGrid id="faultMapsideGrid" columns="2" border="1">
      <f:verbatim>
	  <div id="faultMapside" style="width: 300px; height: 800px; overflow:auto;"></div>
      </f:verbatim>
      <f:verbatim>
	<div id="faultMap" style="width: 700px; height: 800px;"></div>
	</f:verbatim> 
    </h:panelGrid>
  </f:verbatim>


<f:verbatim>   

<script language="JavaScript">


	// These are used by the fault map 	
	var faultMap=null;
	faultMap=new GMap2(document.getElementById("faultMap"));
	var flistsize = document.getElementById("RssFaultsMap:faultlistsize");
	var flistpolyline = new Array();
	for (var nA = 0 ; nA < flistsize.value ; nA++){
	  flistpolyline[nA] = null;
	}


	// The gridsphere container doesn't work with urls. That should be solved
	// var kmllist = ["http://129.79.49.68:8080/RssDisloc3/geo_000520-001216-sim_HDR_4rlks.unw.kml","http://129.79.49.68:8080/RssDisloc3/QuakeTables_CGS_1996.kml","http://129.79.49.68:8080/RssDisloc3/QuakeTables_CGS_2002.kml"];
	var kmllist = ["overm5.kml"];	
	
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
					return '<input type="checkbox"  onchange="togglerssbox(\'' + name + '\')" value="1">' + '<a id="'+name+'" href="javascript:GEvent.trigger(document.getElementById(\'RssFaultsMap:faultName\'),\'click\',\''+name+'\','+myvar+'.gpolyobjs['+i+'], \'script\', '+myvar+'.gpolyobjs_desc['+i+'])">' + s + '</a>';
				}

				return "";
		}

	
	 // This overrides the default clickpolyobjfn of egeoxml.js
	 function clickpolyobj(p, name, desc) {
	     GEvent.trigger(document.getElementById('RssFaultsMap:faultName'),'click', name, p, 'frommap', desc);	    
	  }


</script>
</f:verbatim>

</h:form>
</f:view>

 </body>
</html>
