<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://big.faceless.org/products/graph" prefix="bfg" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="javax.faces.context.FacesContext" %>
<%@ page import="javax.faces.el.ValueBinding" %>
<%@ page import="javax.faces.application.Application" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.myfaces.blank.*" %>


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
 <script>
 </script>
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
			<f:selectItem itemValue="7" itemLabel="Episodic East"/> 
			<f:selectItem itemValue="8" itemLabel="Episodic North"/> 
			<f:selectItem itemValue="9" itemLabel="Episodic Up"/> 
			<f:selectItem itemValue="16" itemLabel="Annual Amp East"/> 
			<f:selectItem itemValue="17" itemLabel="Annual Amp North"/> 
			<f:selectItem itemValue="18" itemLabel="Annual Amp Up"/> 
			<f:selectItem itemValue="19" itemLabel="Annual Phase East"/> 
			<f:selectItem itemValue="20" itemLabel="Annual Phase North"/> 
			<f:selectItem itemValue="21" itemLabel="Annual Phase Up"/> 
			<f:selectItem itemValue="22" itemLabel="Semiannual Amp East"/> 
			<f:selectItem itemValue="23" itemLabel="Semiannual Amp North"/> 
			<f:selectItem itemValue="24" itemLabel="Semiannual Amp Up"/> 
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
	ValueBinding binding = app.createValueBinding("#{stfilterBean.resultsBean.resiUrl != null}");
	boolean isResiUrlNotNull = ((Boolean) binding.getValue(context)).booleanValue();
	System.out.println("[!!] isResiUrlNotNull = "+isResiUrlNotNull);
%>
	<c:if test="<%= isResiUrlNotNull %>">

	<bfg:axesgraph width="800" height="400" 
		backgroundcolor="#ededed"
		backwallpaint="stripe(#ffffff,axis=bottom,line=#cccccc,altaxis=left)">
		<bfg:axis pos="left" type="float"/>
		<bfg:axis pos="bottom" type="date(yyyy-MM-dd)" rotate="-20" density="sparse"/>
	
<%
	ValueBinding vbName = app.createValueBinding("#{stfilterBean.graphName}");
	String[] graphName = (String[]) vbName.getValue(context);
	// System.out.println("[!!] graphName[0] = "+graphName[0]);

	ValueBinding vbHistory = app.createValueBinding("#{stfilterBean.paramHistory}");
	Vector vecHistory = (Vector) vbHistory.getValue(context);
	// System.out.println("[!!] vecHistory.size() = "+vecHistory.size());

	ValueBinding vbIndex = app.createValueBinding("#{stfilterBean.myStationParamListIndex}");
	int intParamIndex = ((Integer) vbIndex.getValue(context)).intValue();
	// System.out.println("[!!] intParamIndex = "+ intParamIndex);
	int intGraphIndex = 2;
	switch(intParamIndex) {
		case 7:
		case 16:
		case 19:
		case 22:
			intGraphIndex = 2;
			break;
		case 8:
		case 17:
		case 20:
		case 23:
			intGraphIndex = 3;
			break;
		case 9:
		case 18:
		case 21:
		case 24:
			intGraphIndex = 4;
			break;
	}
	//System.out.println("[!!] intGraphIndex = "+ intGraphIndex);

	ValueBinding vbSize = app.createValueBinding("#{stfilterBean.paramHistorySize}");
	int intSize = ((Integer) vbSize.getValue(context)).intValue();

	ValueBinding vbSiteCodeArr = app.createValueBinding("#{stfilterBean.siteCodeArr}");
	String[] siteCodeArr = ((String[]) vbSiteCodeArr.getValue(context));

	
	ValueBinding vbFormType = app.createValueBinding("#{stfilterBean.curEpisodicParamRendered}");
	boolean isEpisodicParam = ((Boolean) vbFormType.getValue(context)).booleanValue();
	vbFormType = app.createValueBinding("#{stfilterBean.curAnnualAmpParamRendered}");
	boolean isAnnualAmpParam = ((Boolean) vbFormType.getValue(context)).booleanValue();
	vbFormType = app.createValueBinding("#{stfilterBean.curAnnualPhaseParamRendered}");
	boolean isAnnualPhaseParam = ((Boolean) vbFormType.getValue(context)).booleanValue();
	vbFormType = app.createValueBinding("#{stfilterBean.curSemiannualAmpParamRendered}");
	boolean isSemiannualAmpParam = ((Boolean) vbFormType.getValue(context)).booleanValue();

	//System.out.println("[!!] intVal = "+intSize);
	// Begin of outer loop
	for (int idx = 0; idx < intSize; idx++) {
%>
<% System.out.println("[!!] Outer loop (History loop)... "); %>
<% 		
		String funcName = null;
		if (isEpisodicParam) { 
			funcName = "EpisodicUpdate(seriesx)";
		} else if (isAnnualAmpParam) {
			funcName = "AnnualAmpUpdate(seriesx)";
		} else if (isAnnualPhaseParam) {
			funcName = "AnnualPhaseUpdate(seriesx)";
		} else if (isSemiannualAmpParam) {
			funcName = "SemiannualAmpUpdate(seriesx)";
		} else {
			funcName = "EpisodicUpdate(seriesx)";
		}
		
		
		System.out.println("[!!] Middle loop (SiteCode loop)... "); 
		for (int siteIdx = 0; siteIdx < siteCodeArr.length; siteIdx++) {
%>
		<bfg:lineseries name="<%= siteCodeArr[siteIdx] + " (" + graphName[idx] + ")" %>"
			linethickness="2" 
			onmousemove="bfgShowPopup('('+bfgToDate(seriesx, 'yyyy-MM-dd') +','+bfgRound(seriesy,2)+')', event)" 
			onmouseout="bfgHidePopup()" 
			onclick="<%= funcName %>">
<%
		// Begin of inner loop
		System.out.println("[!!] Inner loop (Each value loop)... ");
		//ArrayList list = (ArrayList) vecHistory.get(idx);
		ArrayList[] listArr = (ArrayList[]) vecHistory.get(idx);
		ArrayList row = null;
		//for (int i = 0; i < list.size(); i++) {
		for (int i = 0; i < listArr[siteIdx].size(); i++) {
			//row = (ArrayList) list.get(i);
			row = (ArrayList) listArr[siteIdx].get(i);
			//System.out.println("[!!] row.getClass().toString() ="+row.getClass().toString());
			//System.out.println("[!!] row.get(1).getClass().toString() ="+row.get(1).getClass().toString());
			//System.out.println("[!!] row.get(intGraphIndex).getClass().toString() ="+row.get(intGraphIndex).getClass().toString());
			
			//System.out.println("[!!] ((Date) row.get(1)).toString() = "+((Date) row.get(1)).toString());
			//System.out.println("[!!] (String) row.get(intGraphIndex) = "+(String) row.get(intGraphIndex));
%>
				<bfg:data x="<%= ((Date) row.get(1)).toString() %>" 
						y="<%= (String) row.get(intGraphIndex) %>"/>
<%
		} // End of inner loop
%>
		</bfg:lineseries>
<%
		} // End of middle loop
	} // End of outer loop
%>
		
		<bfg:key align="bottom" color="#ffffff">

<%
		for (int idx = 0; idx < intSize; idx++) {
			for (int siteIdx = 0; siteIdx < siteCodeArr.length; siteIdx++) {
%>
			<bfg:keyitem series="<%= siteCodeArr[siteIdx] + " (" + graphName[idx] + ")" %>"/>
<%
			}
		}
%>
		</bfg:key>

    </bfg:axesgraph>
    </c:if>
	<br>
	<h:commandButton value="Clear History" action="#{stfilterBean.clearHistory}"/>
    

	</h:form>
    </td>
    <td valign="top">
    <br>
	<h:form id="myStationCurrentParam" >
		<h:panelGrid columns="2" rendered="#{stfilterBean.curEpisodicParamRendered}">
				<h:outputText value="Apriori Value"/>
				<h:inputText id="EpisodicAprioriValue" 
					valueChangeListener="#{stfilterBean.curEpisodicAprioriValueChanged}"
					value="#{stfilterBean.curEpisodicParam.aprioriValue}"/>
				<h:outputText value="Apriori Constraint"/>
				<h:inputText id="EpisodicAprioriConstraint" 
					valueChangeListener="#{stfilterBean.curEpisodicAprioriConstraintChanged}"
					value="#{stfilterBean.curEpisodicParam.aprioriConstraint}"/>
				<h:outputText value="Start Date"/>
				<h:inputText id="episodicStartDate" 
					valueChangeListener="#{stfilterBean.curEpisodicStartDateChanged}"
					value="#{stfilterBean.curEpisodicParam.startDate}"/>
				<h:outputText value="End Date"/>
				<h:inputText id="episodicEndDate" 
					valueChangeListener="#{stfilterBean.curEpisodicEndDateChanged}"
					value="#{stfilterBean.curEpisodicParam.endDate}"/>
		</h:panelGrid>
		<h:panelGrid columns="2" rendered="#{stfilterBean.curAnnualAmpParamRendered}">
				<h:outputText value="Apriori Value"/>
				<h:inputText id="AnnualAmpAprioriValue" 
					valueChangeListener="#{stfilterBean.curAnnualAmpAprioriValueChanged}"
					value="#{stfilterBean.curAnnualAmpParam.aprioriValue}"/>
				<h:outputText value="Apriori Constraint"/>
				<h:inputText id="AnnualAmpAprioriConstraint" 
					valueChangeListener="#{stfilterBean.curAnnualAmpAprioriConstraintChanged}"
					value="#{stfilterBean.curAnnualAmpParam.aprioriConstraint}"/>
				<h:outputText value="Start Date"/>
				<h:inputText id="annualAmpStartDate" 
					valueChangeListener="#{stfilterBean.curAnnualAmpStartDateChanged}"
					value="#{stfilterBean.curAnnualAmpParam.startDate}"/>
				<h:outputText value="Period Length"/>
				<h:inputText id="annualAmpPeriodLength" 
					valueChangeListener="#{stfilterBean.curAnnualAmpPeriodLengthChanged}"
					value="#{stfilterBean.curAnnualAmpParam.periodLength}"/>
		</h:panelGrid>
		<h:panelGrid columns="2" rendered="#{stfilterBean.curAnnualPhaseParamRendered}">
				<h:outputText value="Apriori Value"/>
				<h:inputText id="AnnualPhaseAprioriValue" 
					valueChangeListener="#{stfilterBean.curAnnualPhaseAprioriValueChanged}"
					value="#{stfilterBean.curAnnualPhaseParam.aprioriValue}"/>
				<h:outputText value="Apriori Constraint"/>
				<h:inputText id="AnnualPhaseAprioriConstraint" 
					valueChangeListener="#{stfilterBean.curAnnualPhaseAprioriConstraintChanged}"
					value="#{stfilterBean.curAnnualPhaseParam.aprioriConstraint}"/>
				<h:outputText value="Start Date"/>
				<h:inputText id="annualPhaseStartDate" 
					valueChangeListener="#{stfilterBean.curAnnualPhaseStartDateChanged}"
					value="#{stfilterBean.curAnnualPhaseParam.startDate}"/>
				<h:outputText value="Period Length"/>
				<h:inputText id="annualPhasePeriodLength" 
					valueChangeListener="#{stfilterBean.curAnnualPhasePeriodLengthChanged}"
					value="#{stfilterBean.curAnnualPhaseParam.periodLength}"/>
		</h:panelGrid>
		<h:panelGrid columns="2" rendered="#{stfilterBean.curSemiannualAmpParamRendered}">
				<h:outputText value="Apriori Value"/>
				<h:inputText id="SemiannualAmpAprioriValue" 
					valueChangeListener="#{stfilterBean.curSemiannualAmpAprioriValueChanged}"
					value="#{stfilterBean.curSemiannualAmpParam.aprioriValue}"/>
				<h:outputText value="Apriori Constraint"/>
				<h:inputText id="SemiannualAmpAprioriConstraint" 
					valueChangeListener="#{stfilterBean.curSemiannualAmpAprioriConstraintChanged}"
					value="#{stfilterBean.curSemiannualAmpParam.aprioriConstraint}"/>
				<h:outputText value="Start Date"/>
				<h:inputText id="semiannualAmpStartDate" 
					valueChangeListener="#{stfilterBean.curSemiannualAmpStartDateChanged}"
					value="#{stfilterBean.curSemiannualAmpParam.startDate}"/>
				<h:outputText value="Period Length"/>
				<h:inputText id="semiannualAmpPeriodLength" 
					valueChangeListener="#{stfilterBean.curSemiannualAmpPeriodLengthChanged}"
					value="#{stfilterBean.curSemiannualAmpParam.periodLength}"/>
		</h:panelGrid>
	    <h:commandButton value="Run STFILTER" action="#{stfilterBean.callAnalyzeTseriService}"/>
        <h:commandButton id="save" value="Save to Archive"
        	rendered="#{(stfilterBean.resultsBean!=null)}"
        	action="#{stfilterBean.saveProject}"/>
        <h:messages style="color: red"/>
	</h:form>
	<%--
	
	ValueBinding binding1 = app.createValueBinding("#{stfilterBean.allParams}");
	ValueBinding binding2 = app.createValueBinding("#{stfilterBean.episodicParams}");
	ValueBinding binding3 = app.createValueBinding("#{stfilterBean.annualAmpParams}");
	ValueBinding binding4 = app.createValueBinding("#{stfilterBean.annualPhaseParams}");
	ValueBinding binding5 = app.createValueBinding("#{stfilterBean.semiannualAmpParams}");
	Vector allParams = ((Vector) binding1.getValue(context));
	Vector episodicParams = ((Vector) binding2.getValue(context));
	Vector annualAmpParams = ((Vector) binding3.getValue(context));
	Vector annualPhaseParams = ((Vector) binding4.getValue(context));
	Vector semiannualAmpParams = ((Vector) binding5.getValue(context));
	System.out.println("[!!] allParams.size() = "+allParams.size());
	System.out.println("[!!] ((EstimateParameter) allParams.get(0)).getParameterFullName() = "+((EstimateParameter) allParams.get(0)).getParameterFullName());
	System.out.println("[!!] episodicParams.size() = "+episodicParams.size());
	System.out.println("[!!] annualAmpParams.size() = "+annualAmpParams.size());
	System.out.println("[!!] annualPhaseParams.size() = "+annualPhaseParams.size());
	System.out.println("[!!] semiannualAmpParams.size() = "+semiannualAmpParams.size());
	
	--%>
	<h:form id="myStationCurrentAppliedFilter" rendered="#{!empty stfilterBean.allParams}">
		<h:outputText  escape="false" value="<b>Applied Filters</b>"/>
		<h:dataTable var="p"
		    border="0"
		    value="#{stfilterBean.allParams}"
		    binding="#{stfilterBean.allParamsTable}">
			<h:column>
				<h:outputText value="#{p.parameterFullName}"/>
           </h:column>
           <h:column>
           		<h:commandButton value="Remove" actionListener="#{stfilterBean.removeParamListener}"/>
           </h:column>
        </h:dataTable>
	</h:form>


	<h:panelGrid id="OutputGridPanel" rendered="#{(stfilterBean.resultsBean!=null)}">
		<h:outputText  escape="false" value="<b>Input/Output Files</b>"/>

		<h:outputLink target="_blank" value="#{stfilterBean.resultsBean.drvUrl}">
		<h:outputText value="Driver File"/>
		</h:outputLink>

		<h:outputLink target="_blank" value="#{stfilterBean.resultsBean.listUrl}">
		<h:outputText value="List File"/>
		</h:outputLink>

		<h:outputLink target="_blank" value="#{stfilterBean.resultsBean.inputUrl}">
		<h:outputText value="Input List File"/>
		</h:outputLink>

		<h:outputLink target="_blank" value="#{stfilterBean.resultsBean.siteUrl}">
		<h:outputText value="Site List File"/>
		</h:outputLink>

		<h:outputLink target="_blank" value="#{stfilterBean.resultsBean.paraUrl}">
		<h:outputText value="Parameter File"/>
		</h:outputLink>

		<h:outputLink target="_blank" value="#{stfilterBean.resultsBean.outUrl}">
		<h:outputText value="Output File"/>
		</h:outputLink>

		<h:outputLink target="_blank" value="#{stfilterBean.resultsBean.resiUrl}">
		<h:outputText value="Residual File"/>
		</h:outputLink>

		<h:outputLink target="_blank" value="#{stfilterBean.resultsBean.mdlUrl}">
		<h:outputText value="Model File"/>
		</h:outputLink>

	</h:panelGrid>
   
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
