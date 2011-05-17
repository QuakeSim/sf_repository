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

 <script language="JavaScript">

  function selectOne(form , button) {
   turnOffRadioForForm(form);
   button.checked = true;
  }

  function turnOffRadioForForm(form) {
   for(var i=0; i<form.elements.length; i++) {
    form.elements[i].checked = false;
   }
  }

  function dataTableSelectOneRadio(radio) {
   var id = radio.name.substring(radio.name.lastIndexOf(':'));
   var el = radio.form.elements;
   // alert (el.length);
   for (var i = 0; i < el.length; i++) {
    if (el[i].name.substring(el[i].name.lastIndexOf(':')) == id) {
     // alert (el[i].checked)
     el[i].checked = false;   
    }
   }
   radio.checked = true;
  }

  Array.prototype.remove = function(e) {
   for(var nA = 0; nA < this.length; nA++ ) {
    if(this[nA]==e)
     this.splice(nA,1);
   }
  }

  togglerssbox = function(n) {
   var source =document.getElementById("dpdp1:projectsource");
   
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

 <f:view>
  <h:form id="ioppll1">
   <h:outputText id="lptv1" styleClass="header2" value="Project Archive"/>   
  </h:form>
  <p/>
  <h:outputText id="lptv11" value="You don't have any archived projects." rendered="#{empty DislocBean2.myProjectNameList && empty DislocBean2.dbProjectNameList}"/>
  
  <h:panelGrid id="lptv12" columnClasses="alignTop,alignTop,alignTop, alignTop" columns="4" rendered="#{!(empty DislocBean2.myProjectNameList) || !empty DislocBean2.dbProjectNameList}" border="1">


   <h:form id="dpdp1">
    <h:inputHidden id="projectsource" value="#{DislocBean2.projectsource}"/>   
    <h:panelGrid id="asdvp1"  columns="1" border="0">
     <h:outputText id="dptv21" escape="false" value="<b>Rss Projects</b><br><br>"/>
     <h:outputText id="lptvm2" escape="false" value="Please select from the USGS earthquakes RSS"/>
     <f:verbatim>
      <link rel="stylesheet" href="@host.base.url@@artifactId@/jquery.treeview.css" />
      <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.2.6/jquery.min.js"></script>
      <script type="text/javascript" src="@host.base.url@@artifactId@/lib/jquery.cookie.js"></script>
      <script type="text/javascript" src="@host.base.url@@artifactId@/jquery.treeview.js"></script>
      <script type="text/javascript" src="@host.base.url@@artifactId@/demo.js"></script>

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
	

	// System.out.println(dsb.getCodeName());

      %>

      <ul id="browser">

      <%
	HashMap hm = dsb.getDbProjectNameList();
	Set set = hm.entrySet();
	Iterator itr = set.iterator();
	List l3 = null;

	while(itr.hasNext()) {
	  Map.Entry me = (Map.Entry)itr.next();
	  l3 = (ArrayList)me.getValue();  
      %>

	<li><%=me.getKey()%></span>
	<ul>
	<%
	  for (int nA = 0 ; nA < l3.size() ; nA++) {
	%>
	  <li><input type="checkbox" id="rssplist_<%=nA%>" onchange="togglerssbox('<%=me.getKey() + "_n_" + ((l3.get(nA))).toString()%>')"><%=((l3.get(nA))).toString()%></span></li>
	  <%
	  }
	  %>
	</ul>
	</li>
	<%
	}
	%>
      </ul>	
     </f:verbatim>
     <h:commandButton value="Copy to my projects" action="#{DislocBean2.toggleCopyRssProject}"/>
    </h:panelGrid>
   </h:form>

   <h:form id="baasz1">
    <h:panelGrid id="asdvww1" columns="1" border="0">
     <h:outputText id="lptv21" escape="false" value="<b>My Projects</b><br><br>"/>
     <h:outputText id="lptv22" escape="false" value="Please select from one of the previous projects."/>
     <h:selectManyCheckbox id="projectlistforload" value="#{DislocBean2.selectProjectsArray}" onchange="dataTableSelectOneRadio(this)" onclick="dataTableSelectOneRadio(this)" layout="pageDirection">
      <f:selectItems value="#{DislocBean2.myProjectNameList}"/>
     </h:selectManyCheckbox>
     <h:commandButton value="Select" action="#{DislocBean2.toggleSelectProject}"/>
    </h:panelGrid>
   </h:form>

   <h:form id="bbasd123">        
    <h:panelGrid id="asdvww2" columns="1" border="0">
     <h:message id="NoCopyProject" for="newProjectName" showDetail="#{true}" showSummary="#{true}" errorStyle="color:red" styleClass="errors"/>
     <h:message id="projectlistforcopyMsg" for="projectlistforcopy" showDetail="#{true}" showSummary="#{true}" errorStyle="color:red" styleClass="errors"/>
     <h:outputText escape="false" value="<b>Copy Project</b><br><br>"/>
     <h:outputText escape="false" value="Please select from one of the previous projects."/>
     <h:selectManyCheckbox id="projectlistforcopy" value="#{DislocBean2.copyProjectsArray}" onchange="dataTableSelectOneRadio(this)" onclick="dataTableSelectOneRadio(this)" layout="pageDirection">
      <f:selectItems value="#{DislocBean2.myProjectNameList}"/>
     </h:selectManyCheckbox>
     <h:outputText value="New ProjectName:"/>
     <h:inputText id="newProjectName" value="#{DislocBean2.projectName}" required="true"/>
     <h:commandButton value="Copy" action="#{DislocBean2.toggleCopyProject}"/>
    </h:panelGrid>
   </h:form>

   <h:form id="bazzz1b">
    <h:panelGrid id="asdvww3"  columns="1" border="0">
     <h:outputText id="aksdadd2" escape="false" value="<b>Delete Projects</b><br><br>"/>
     <h:outputText id="aksdadd3" escape="false" value="Please select from one of the previous projects."/>
     <h:selectManyCheckbox id="projectfordelete" value="#{DislocBean2.deleteProjectsArray}" layout="pageDirection">
      <f:selectItems value="#{DislocBean2.myProjectNameList}"/>
     </h:selectManyCheckbox>
     <h:commandButton value="Delete" action="#{DislocBean2.toggleDeleteProject}"/>
    </h:panelGrid>
   </h:form>
  </h:panelGrid>
  <p/>
	
  <h:form id="nasdfwe1">
   <b>New Project Name</b>
   <h:panelGrid id="asdvww4" columns="2" border="1">
    <h:outputText id="lpj_projectname" value="Project Name:" />
    <h:panelGroup>
     <h:inputText id="projectName" value="#{DislocBean2.projectName}" required="true" />
     <h:message for="projectName" showDetail="true" showSummary="true" errorStyle="color: red" />
    </h:panelGroup>
   </h:panelGrid>
   <h:commandButton id="lpj_makeselection" value="Make Selection" action="#{DislocBean2.NewProjectThenEditProject}"/>
  </h:form>
  <%@ include file="footer.jsp" %>
  </f:view>
 </body>
</html>
