<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://big.faceless.org/products/graph" prefix="bfg" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.el.ValueBinding" %>
<%@ page import="javax.faces.application.Application" %>
<%@ page import="java.util.*" %>

<%
	FacesContext context = FacesContext.getCurrentInstance();
	Application app = context.getApplication();
%>

<html>
  <head>
        <title>STFILTER Estimate Parameters</title>
  </head>
 <body>
 <h2>Station Estimate Parameters</h2>
  <f:view>        
	<script type="text/ecmascript" src="<h:outputText value="#{facesContext.externalContext.requestContextPath}"/>/scripts/bfograph.js"> </script>
	<script type="text/ecmascript" src="<h:outputText value="#{facesContext.externalContext.requestContextPath}"/>/scripts/bfograph2.js"> </script>
	
    <table>
    <tr>
    <td>
	<h:form id="myStationParam">
		<h:selectOneMenu 
			valueChangeListener="#{stfilterBean.myStationParamListChanged}" 
			onchange="this.form.submit()">
			<f:selectItem itemValue="0" itemLabel="Episodic East"/> 
			<f:selectItem itemValue="1" itemLabel="Episodic North"/> 
			<f:selectItem itemValue="2" itemLabel="Episodic Up"/> 
		</h:selectOneMenu> 
		<%--
		&nbsp;
		<h:outputText value="Maximum number of history: "/>
		<h:selectOneMenu 
			value="#{stfilterBean.maxHistory}"
			onchange="this.form.submit()">
			<f:selectItem itemValue="1" itemLabel="1"/> 
			<f:selectItem itemValue="2" itemLabel="2"/> 
			<f:selectItem itemValue="3" itemLabel="3"/> 
		</h:selectOneMenu> 
		--%>
		<br>
	<%--
	<c:forEach var="idx" begin="0" end="<%= stfilterBean.maxHistory %>">
		<c:out value="1"/> <br>
	</c:forEach>
	--%>

<% System.out.println("[!!] Begin ... "); %>

<%
	ValueBinding binding = app.createValueBinding("#{stfilterBean.resiURL != null}");
	boolean isTrue = ((Boolean) binding.getValue(context)).booleanValue();
	System.out.println("[!!] isTrue = "+isTrue);
%>
	<c:if test="<%= isTrue %>">
<% System.out.println("[!!] True ... "); %>

	<bfg:axesgraph width="800" height="400" 
		backgroundcolor="#ededed"
		backwallpaint="stripe(#ffffff,axis=bottom,line=#cccccc,altaxis=left)">
		<bfg:axis pos="left" type="float"/>
		<bfg:axis pos="bottom" type="date(yyyy-MM-dd)" rotate="-20" density="sparse"/>
	
<%
	ValueBinding vbName = app.createValueBinding("#{stfilterBean.graphName}");
	String[] strName = (String[]) vbName.getValue(context);
	System.out.println("[!!] strName[0] = "+strName[0]);

	ValueBinding vbHistory = app.createValueBinding("#{stfilterBean.paramHistory}");
	Vector vecHistory = (Vector) vbHistory.getValue(context);
	System.out.println("[!!] vecHistory.size() = "+vecHistory.size());

	ValueBinding vbIndex = app.createValueBinding("#{stfilterBean.myStationParamListIndex}");
	int intIndex = ((Integer) vbIndex.getValue(context)).intValue();
	System.out.println("[!!] intIndex = "+ intIndex);


	ValueBinding vbSize = app.createValueBinding("#{stfilterBean.paramHistorySize}");
	int intSize = ((Integer) vbSize.getValue(context)).intValue();
	System.out.println("[!!] intVal = "+intSize);
	// Begin of outer loop
	for (int idx = 0; idx < intSize; idx++) {
%>
<% System.out.println("[!!] Outer loop ... "); %>
		<bfg:lineseries name="<%= strName[idx] %>"
			linethickness="2" 
			onmousemove="bfgShowPopup('('+bfgToDate(seriesx, 'yyyy-MM-dd') +','+bfgRound(seriesy,2)+')', event)" 
			onmouseout="bfgHidePopup()" 
			onclick="Update(seriesx)">
<%
		// Begin of inner loop
		ArrayList list = (ArrayList) vecHistory.get(idx);
		ArrayList row = null;
		for (int i = 0; i < list.size(); i++) {
			row = (ArrayList) list.get(i);
%>
				<bfg:data x="<%= ((Date) row.get(1)).toString() %>" 
						y="<%= (String) row.get(intIndex +2) %>"/>
<%
		// End of inner loop
		}
%>
		</bfg:lineseries>
<%
	// End of outer loop
	}
%>
		<bfg:key align="bottom" color="#ffffff">
		<c:forEach var="idx" begin="0" end="<%= intSize %>">
			<bfg:keyitem series="${stfilterBean.graphName[idx]}"/>
		</c:forEach>
		</bfg:key>

    </bfg:axesgraph>
    </c:if>
	<br>
	<h:commandButton value="Clear History" action="#{stfilterBean.clearHistory}"/>
    

	</h:form>
    </td>
    <td valign="top">
    <br>
	<h:form id="myStationCurrentParam">
		<h:panelGrid columns="2">
				<h:outputText value="Apriori Value"/>
				<h:inputText id="aprioriValue" 
					valueChangeListener="#{stfilterBean.aprioriValueChanged}"
					value="#{stfilterBean.currentParam.aprioriValue}"/>
				<h:outputText value="Apriori Constraint"/>
				<h:inputText id="aprioriConstraint" 
					valueChangeListener="#{stfilterBean.aprioriConstraintChanged}"
					value="#{stfilterBean.currentParam.aprioriConstraint}"/>
				<h:outputText value="Start Date"/>
				<h:inputText id="startDate" 
					valueChangeListener="#{stfilterBean.startDateChanged}"
					value="#{stfilterBean.currentParam.startDate}"/>
				<h:outputText value="End Date"/>
				<h:inputText id="endDate" 
					valueChangeListener="#{stfilterBean.endDateChanged}"
					value="#{stfilterBean.currentParam.endDate}"/>
		</h:panelGrid>
	    <h:commandButton value="Run ST_FILTER_WS" action="#{stfilterBean.launchSTFILTERWS}"/>
	</h:form>
    </td>
    </tr>
    </table>

    <h:form>
    <hr/>
    <h:commandLink action="back">
        <h:outputText value="#{stfilterBean.codeName} Main Menu"/>
    </h:commandLink>
    </h:form>
  </f:view>
 <body>
</html>
