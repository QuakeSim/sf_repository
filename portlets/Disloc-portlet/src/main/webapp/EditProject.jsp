<%@ page language="java" contentType="text/html; charset=ISO-8859-1" 
	pageEncoding="ISO-8859-1"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%> 
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%> 
 
<style> 
	.alignTop { 
		vertical-align:top; 
	} 
	.header2 { 
		font-family: Arial, sans-serif; 
		font-size: 18pt; 
		font: bold; 
	} 
</style> 
 
 
<html> 
<head> 
<link rel="stylesheet" type="text/css" 
	href='<%= request.getContextPath() + "/stylesheet.css" %>'> 
 
<title>Edit Project</title> 
</head> 
<body> 
<script language="JavaScript"> 
 
function selectOne(form , button) 
{ 
  turnOffRadioForForm(form); 
  button.checked = true; 
} 
 
function turnOffRadioForForm(form) 
{ 
  for(var i=0; i<form.elements.length; i++) 
  { 
   form.elements[i].checked = false; 
       
  } 
} 
 
function dataTableSelectOneRadio(radio) { 
    var id = radio.name.substring(radio.name.lastIndexOf(':')); 
    var el = radio.form.elements; 
     //alert (el.length); 
    for (var i = 0; i < el.length; i++) { 
        if (el[i].name.substring(el[i].name.lastIndexOf(':')) == id) { 
        //alert (el[i].checked) 
            el[i].checked = false; 
            el[i].checked = false; 
        } 
    } 
    radio.checked = true; 
} 
 
</script> 
<f:view> 
	<h:outputText styleClass="header2" value="Project Input"/> 
 
	<p>Create your geometry out of observation points and faults.</p> 
 
	<h:panelGrid id="EditProject"  
		columnClasses="alignTop,alignTop" 
		columns="2" border="1"> 
		<h:form id="selectproj"> 
			<h:panelGroup id="pg1"> 
 
				<h:outputFormat id="stuff1" escape="false" 
					value="<b>Project Name: #{DislocBean.projectName} </b>" /> 
 
				<h:selectOneRadio layout="pageDirection" id="subscriptions" 
					value="#{DislocBean.projectSelectionCode}"> 
					<f:selectItem id="item1" 
						itemLabel="Observations: Click to specify grid of observation points." 
						itemValue="CreateObservationGrid" /> 
					<f:selectItem id="item2" 
						itemLabel="Create New Fault: Click to specify geometry for a fault segment." 
						itemValue="CreateNewFault" /> 
					<f:selectItem id="item4" 
						itemLabel="Add Fault from DB: Click to select a fault segment from the database." 
						itemValue="AddFaultSelection" /> 
				</h:selectOneRadio> 
				<h:commandButton id="button1" value="Make Selection" 
					actionListener="#{DislocBean.toggleProjectSelection}"> 
				</h:commandButton> 
			</h:panelGroup> 
		</h:form> 
 
		<h:panelGroup id="stuff4"> 
			<h:form id="obsvform" rendered="#{DislocBean.renderDislocGridParamsForm}"> 
				<h:panelGrid id="ObsvTable" columns="2" footerClass="subtitle" 
					headerClass="subtitlebig" styleClass="medium" 
					columnClasses="subtitle,medium"> 
 
					<f:facet name="header"> 
						<h:outputFormat id="output2" escape="false" 
							value="<b>Define Grid of Observation Points </b>" /> 
					</f:facet> 
 
					<h:outputText  id="stuff2" value="Grid Minimum X Value:" /> 
					<h:panelGroup  id="stuff5"> 
						<h:inputText id="minx" 
							value="#{DislocBean.dislocParams.gridMinXValue}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff3" value="X Spacing:" /> 
					<h:panelGroup  id="stuff6"> 
						<h:inputText id="xspacing" 
							value="#{DislocBean.dislocParams.gridXSpacing}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff7" value="X Iterations" /> 
					<h:panelGroup  id="stuff8"> 
						<h:inputText id="xiterations" 
							value="#{DislocBean.dislocParams.gridXIterations}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff9" value="Grid Minimum Y Value:" /> 
					<h:panelGroup  id="stuff10"> 
						<h:inputText id="miny" 
							value="#{DislocBean.dislocParams.gridMinYValue}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff11" value="Y Spacing:" /> 
					<h:panelGroup   id="pg2"> 
						<h:inputText id="yspacing" 
							value="#{DislocBean.dislocParams.gridYSpacing}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff12" value="Y Iterations" /> 
					<h:panelGroup  id="stuff13"> 
						<h:inputText id="yiterations" 
							value="#{DislocBean.dislocParams.gridYIterations}" required="true" /> 
					</h:panelGroup> 
 
					<h:commandButton id="addobservation" value="select" 
						actionListener="#{DislocBean.toggleAddObservationsForProject}" /> 

				</h:panelGrid> 
			</h:form> 
 
			<h:form id="Faultform" rendered="#{DislocBean.renderCreateNewFaultForm}"> 
				<h:panelGrid id="FaultTable" columns="2" footerClass="subtitle" 
					headerClass="subtitlebig" styleClass="medium" 
					columnClasses="subtitle,medium"> 
 
					<f:facet name="header"> 
						<h:outputFormat id="output3" escape="false" 
							value="<b>Input Fault Geometry </b>" /> 
					</f:facet> 
 
					<h:outputText  id="stuff15" value="Fault Name:" /> 
					<h:panelGroup  id="stuff16"> 
						<h:inputText id="FaultName" 
							value="#{DislocBean.currentFault.faultName}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff17" value="Location X:" /> 
					<h:panelGroup  id="stuff18"> 
						<h:inputText id="FaultLocationX" 
							value="#{DislocBean.currentFault.faultLocationX}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff19" value="Location Y:" /> 
					<h:panelGroup  id="stuff20"> 
						<h:inputText id="FaultLocationY" 
							value="#{DislocBean.currentFault.faultLocationY}" required="true" /> 
					</h:panelGroup> 

					<h:outputText value="Fault Origin Latitude:" />
			      <h:panelGroup>
						<h:inputText id="faultLat" value="#{DislocBean.currentFault.faultLatStart}"
										 required="true" />
					</h:panelGroup>

					<h:outputText value="Fault Origin Longitude" />
					<h:panelGroup>
					<h:inputText id="faultLon" value="#{DislocBean.currentFault.faultLonStart}"
									 required="true" />
					</h:panelGroup>
 
					<h:outputText  id="stuff21" value="Length:" /> 
					<h:panelGroup  id="stuff22"> 
						<h:inputText id="FaultLength" 
							value="#{DislocBean.currentFault.faultLength}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff23" value="Width:" /> 
					<h:panelGroup  id="stuff24"> 
						<h:inputText id="FaultWidth" 
							value="#{DislocBean.currentFault.faultWidth}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff25" value="Depth:" /> 
					<h:panelGroup  id="stuff26"> 
						<h:inputText id="FaultDepth" 
							value="#{DislocBean.currentFault.faultDepth}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff27" value="Dip Angle:" /> 
					<h:panelGroup  id="stuff28"> 
						<h:inputText id="FaultDipAngle" 
							value="#{DislocBean.currentFault.faultDipAngle}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff29" value="Dip Slip:" /> 
					<h:panelGroup  id="stuff30"> 
						<h:inputText id="FaultSlip" 
							value="#{DislocBean.currentFault.faultDipSlip}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff31" value="Strike Angle:" /> 
					<h:panelGroup  id="stuff32"> 
						<h:inputText id="FaultStrikeAngle" 
							value="#{DislocBean.currentFault.faultStrikeAngle}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff33" value="Strike Slip:" /> 
					<h:panelGroup  id="stuff35"> 
						<h:inputText id="FaultStrikeSlip" 
							value="#{DislocBean.currentFault.faultStrikeSlip}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff34" value="Tensile Slip:" /> 
					<h:panelGroup  id="stuff36"> 
						<h:inputText id="FaultTensileSlip" 
							value="#{DislocBean.currentFault.faultTensileSlip}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff37" value="Lame Lambda:" /> 
					<h:panelGroup  id="stuff38"> 
						<h:inputText id="LameLambda" 
							value="#{DislocBean.currentFault.faultLameLambda}" required="true" /> 
					</h:panelGroup> 
 
					<h:outputText  id="stuff39" value="Lame Mu:" /> 
					<h:panelGroup  id="stuff40"> 
						<h:inputText id="LameMu" 
							value="#{DislocBean.currentFault.faultLameMu}" required="true" /> 
					</h:panelGroup> 
 
					<h:commandButton id="addfault" value="select" 
						actionListener="#{DislocBean.toggleAddFaultForProject}" /> 
				</h:panelGrid> 
			</h:form> 
 
			<h:form id="faultselection" 
				rendered="#{DislocBean.renderAddFaultSelectionForm}"> 
				<h:panelGrid id="AddFaultSelection" columns="1" 
					footerClass="subtitle" headerClass="subtitlebig" 
					styleClass="medium" columnClasses="subtitle,medium"> 
					<h:panelGroup   id="stuff41"> 
						<h:outputFormat escape="false" 
						    id="stuff42" 
							value="<b>Fault Database Selection</b><br><br>" /> 
						<h:outputFormat escape="false" 
						     id="stuff43" 
							value="You may select faults from the Fault Database using author search, latitude/longitude bounding box, or by viewing the master list (long).<br><br>" /> 
						<h:outputFormat escape="false" 
						    id="pg2" 
							value="Please choose a radio button and click <b>Select</b>.<br><br>" /> 
					</h:panelGroup> 
 
					<h:panelGroup id="stuff44"> 
						<h:selectOneRadio layout="pageDirection" id="subscriptionssss" 
							value="#{DislocBean.faultSelectionCode}"> 
							<f:selectItem id="item01" itemLabel="Search by fault name." 
								itemValue="SearchByFaultName" /> 
							<f:selectItem id="item02" 
								itemLabel="Search by Lat/Lon bounding box." 
								itemValue="SearchByLatLon" /> 
							<f:selectItem id="item03" itemLabel="Search by author." 
								itemValue="SearchByAuthor" /> 
							<f:selectItem id="item04" itemLabel="View all faults." 
								itemValue="ViewAllFaults" /> 
						</h:selectOneRadio> 
						<h:commandButton id="button122" value="Make Selection" 
							actionListener="#{DislocBean.toggleFaultSelection}" /> 
					</h:panelGroup> 
				</h:panelGrid> 
			</h:form> 
 
			<h:form id="faultsearchByNameform" 
				rendered="#{DislocBean.renderSearchByFaultNameForm}"> 
				<h:panelGrid id="FaultSearchName" columns="1" footerClass="subtitle" 
					headerClass="subtitlebig" styleClass="medium" 
					columnClasses="subtitle,medium"> 
					<h:outputFormat escape="false" 
					     id="stuff44" 
						value="<b>Search Fault DB by Fault Name</b><br><br>" /> 
					<h:panelGroup   id="stuff45"> 
						<h:panelGroup   id="stuff46"> 
							<h:outputText escape="false" 
							     id="stuff47" 
								value="Enter the name of the fault. The search will return partial matches." /> 
							<h:outputText id="stuff48" escape="false" value="<br>" /> 
						</h:panelGroup> 
 
						<h:panelGroup  id="stuff49"> 
							<h:inputText id="Fault_Name" value="#{DislocBean.forSearchStr}" 
								required="true" /> 
							<h:message for="Fault_Name" showDetail="true" showSummary="true" 
							     id="stuff50" 
								errorStyle="color: red" /> 
							<h:commandButton  id="stuff51" value="Query" 
								actionListener="#{DislocBean.toggleFaultSearchByName}" /> 
						</h:panelGroup> 
					</h:panelGroup> 
				</h:panelGrid> 
			</h:form> 
 
			<h:form id="faultlatlonsearchform" 
				rendered="#{DislocBean.renderSearchByLatLonForm}"> 
				<h:panelGrid id="FaultLatLonSearch" columns="1" 
					footerClass="subtitle" headerClass="subtitlebig" 
					styleClass="medium" columnClasses="subtitle,medium"> 
					<h:outputFormat escape="false"  id="stuff52" 
						value="<b>Search Fault DB by Bounding Latitude and Longitude</b><br><br>" /> 
					<h:outputFormat escape="false"  id="stuff53" 
						value="Enter the starting and ending latitude and longitude values (in decimal degrees) of the search bounding box. All faults completely within the bounding box will be returned.<br><br>" /> 
 
					<h:panelGrid columns="2" border="0"  id="stuff54"> 
						<h:outputText value="Starting Latitude: "  id="stuff55"/> 
						<h:panelGroup  id="stuff56"> 
							<h:inputText id="StartingLatitude" 
							    id="stuff57" 
								value="#{DislocBean.faultLatStart}" required="true" /> 
							<h:message for="StartingLatitude" showDetail="true" 
							    id="stuff58" 
								showSummary="true" errorStyle="color: red" /> 
						</h:panelGroup> 
 
						<h:outputText  id="stuff59" value="Ending Latitude: " /> 
						<h:panelGroup  id="stuff60"> 
							<h:inputText id="EndingLatitude" value="#{DislocBean.faultLatEnd}" 
								required="true" /> 
							<h:message for="EndingLatitude" showDetail="true" 
							     id="stuff61" 
								showSummary="true" errorStyle="color: red" /> 
						</h:panelGroup> 
						<h:outputText value="Starting Longitude: "   id="stuff62"/> 
						<h:panelGroup  id="stuff63"> 
							<h:inputText id="StartingLongitude" 
							     id="stuff64" 
								value="#{DislocBean.faultLonStart}" required="true" /> 
							<h:message for="StartingLongitude" showDetail="true" 
							     id="stuff65" 
								showSummary="true" errorStyle="color: red" /> 
						</h:panelGroup> 
						<h:outputText value="Ending Longitude: "   id="stuff66"/> 
						<h:panelGroup   id="stuff67"> 
							<h:inputText id="EndingLongitude" value="#{DislocBean.faultLonEnd}" 
								required="true" /> 
							<h:message for="EndingLongitude" showDetail="true" 
							    id="stuff68" 
								showSummary="true" errorStyle="color: red" /> 
						</h:panelGroup> 
						<h:panelGroup> 
 
							<h:commandButton value="Query" 
							     id="stuff69" 
								actionListener="#{DislocBean.toggleFaultSearchByLonLat}" /> 
						</h:panelGroup> 
					</h:panelGrid> 
				</h:panelGrid> 
			</h:form> 
 
			<h:form id="FaultAuthorSearchform" 
				rendered="#{DislocBean.renderSearchByAuthorForm}"> 
				<h:panelGrid id="FaultAuthorSearch" columns="1" 
					footerClass="subtitle" headerClass="subtitlebig" 
					styleClass="medium" columnClasses="subtitle,medium"> 
					<h:outputFormat escape="false" 
						value="<b>Search Fault DB by Author</b><br><br>" /> 
					<h:panelGroup  id="pg3"> 
						<h:panelGroup  id="pg4"> 
							<h:outputText escape="false" 
							     id="stuff70" 
								value="Enter the last name of the principal author of the desired fault descriptions. The search will do partial matches." /> 
							<h:outputText  id="stuff71" escape="false" value="<br>" /> 
						</h:panelGroup> 
 
						<h:panelGroup  id="pg5"> 
							<h:inputText id="FaultAuthorForSearch" 
								value="#{DislocBean.forSearchStr}" required="true" /> 
							<h:message for="FaultAuthorForSearch" showDetail="true" 
							    id="stuff72" 
								showSummary="true" errorStyle="color: red" /> 
							<h:commandButton value="Query" 
							    id="stuff73" 
								actionListener="#{DislocBean.toggleFaultSearchByAuthor}" /> 
						</h:panelGroup> 
					</h:panelGroup> 
				</h:panelGrid> 
			</h:form> 
 
			<h:form id="SelectFaultDBEntryForm" 
				rendered="#{DislocBean.renderAddFaultFromDBForm}"> 
				<h:dataTable value="#{DislocBean.myFaultDBEntryList}" var="myentry1" 
				    id="stuff74" 
					binding="#{DislocBean.myFaultDataTable}"> 
 
					<h:column  id="pg6"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>FaultName</b>" /> 
						</f:facet> 
						<h:selectOneRadio layout="pageDirection" 
						    id="stuff75" 
							valueChangeListener="#{DislocBean.handleFaultsRadioValueChange}" 
							onchange="dataTableSelectOneRadio(this)" 
							onclick="dataTableSelectOneRadio(this)"> 
							<f:selectItems value="#{myentry1.faultName}" /> 
						</h:selectOneRadio> 
					</h:column> 
 
					<h:column  id="pg7"> 
						<f:facet name="header"> 
							<h:outputText  id="pg8" escape="false" value="<b>SegmentName</b>" /> 
						</f:facet> 
						<h:outputText value="#{myentry1.faultSegmentName}" /> 
					</h:column> 
 
					<h:column  id="pg9"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>Author1</b>" /> 
						</f:facet> 
						<h:outputText  id="pg10" value="#{myentry1.faultAuthor}" /> 
					</h:column> 
 
					<h:column  id="lid1"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>Segment Coordinates</b>" /> 
						</f:facet> 
						<h:outputText  id="lid1" value="#{myentry1.faultSegmentCoordinates}" /> 
					</h:column> 
					<h:column  id="lid1"> 
						<f:facet name="header"> 
							<h:outputText escape="false" value="<b>Action</b>" /> 
						</f:facet> 
						<h:commandLink id="stuff76" 
							actionListener="#{DislocBean.handleFaultEntryEdit}"> 
							<h:outputText value="Get" /> 
						</h:commandLink> 
					</h:column> 
				</h:dataTable> 
				<h:commandButton id="SelectFaultDBEntry" value="SelectFaultDBEntry" 
					actionListener="#{DislocBean.toggleSelectFaultDBEntry}" /> 
			</h:form> 
 
		</h:panelGroup> 
	</h:panelGrid> 
 
	<h:panelGroup  id="stuff77" 
		rendered="#{!empty DislocBean.myFaultEntryForProjectList 
					   || !empty DislocBean.myObsvEntryForProjectList}"> 
 
	<h:outputText  id="stuff78" styleClass="header2" value="Current Project Components"/> 
 
	<h:panelGrid id="ProjectComponentList" columns="2" border="1" 
			columnClasses="alignTop, alignTop"> 
 
		<h:panelGroup  id="stuff79"> 
			<h:form id="UpdateSelectFaultsForm" 
				rendered="#{!empty DislocBean.myFaultEntryForProjectList}"> 
				<h:panelGrid columns="1" border="1"  id="stuff80"> 
					<h:panelGroup  id="lid2"> 
						<h:panelGrid  id="lid3" columns="1"> 
							<h:outputText escape="false" value="<b>Faults</b>"/> 
						</h:panelGrid> 
 
						<h:dataTable border="1" id="stuff81" 
							value="#{DislocBean.myFaultEntryForProjectList}" var="myentry3"> 
							<h:column  id="lid4"> 
								<f:facet name="header"> 
									<h:outputText  id="lid5" escape="false" value="<b>Name</b>"> 
                           </h:outputText> 
								</f:facet> 
								<h:outputText value="#{myentry3.faultName}" 
								    id="stuff82"/> 
							</h:column> 
							<h:column  id="lid6"> 
								<f:facet name="header"> 
									<h:outputText escape="false" value="<b>View</b>" /> 
								</f:facet> 
								<h:selectBooleanCheckbox value="#{myentry3.view}" 
                            id="stuff83" 
									onchange="selectOne(this.form,this)" 
									onclick="selectOne(this.form,this)" /> 
 
							</h:column> 
							<h:column  id="lid7"> 
								<f:facet name="header"> 
									<h:outputText escape="false" value="<b>Remove</b>" /> 
								</f:facet> 
								<h:selectBooleanCheckbox value="#{myentry3.delete}" 
								    id="lid8" 
									onchange="selectOne(this.form,this)" 
									onclick="selectOne(this.form,this)" /> 
							</h:column> 
 
						</h:dataTable> 
					</h:panelGroup> 
 
				</h:panelGrid> 
				<h:commandButton id="SelectFault4proj" value="UpdateFault" 
					actionListener="#{DislocBean.toggleUpdateFaultProjectEntry}" /> 
 
			</h:form> 
 
			<h:form id="UpdateSelectedParamsForm" 
				rendered="#{!empty DislocBean.myObsvEntryForProjectList}"> 
				<h:panelGrid columns="1" border="1"  id="stuff84"> 
					<h:panelGroup  id="lid9"> 
						<h:panelGrid  id="stuff85" columns="1"> 
							<h:outputText  id="stuff86" 
                       escape="false" value="<b>Observations</b>"/> 
						</h:panelGrid> 
 
						<h:dataTable border="1"  id="stuff87" 
							value="#{DislocBean.myObsvEntryForProjectList}" var="myentry4"> 
							<h:column  id="lid110"> 
								<f:facet name="header"> 
									<h:outputText  id="lkj1" escape="false" value="<b>Name</b>"> 
                           </h:outputText> 
								</f:facet> 
								<h:outputText  id="lkj2" value="Observations" /> 
							</h:column> 
							<h:column  id="lkj11"> 
								<f:facet name="header"> 
									<h:outputText  id="lkj3" escape="false" value="<b>View</b>" /> 
								</f:facet> 
								<h:selectBooleanCheckbox value="#{myentry4.view}" 
                            id="stuff88" 
									onchange="selectOne(this.form,this)" 
									onclick="selectOne(this.form,this)" /> 
 
							</h:column> 
							<h:column  id="lkj4"> 
								<f:facet name="header"> 
									<h:outputText escape="false" value="<b>Remove</b>" /> 
								</f:facet> 
								<h:selectBooleanCheckbox value="#{myentry4.delete}" 
									onchange="selectOne(this.form,this)" 
									 id="lkj5" 
									onclick="selectOne(this.form,this)" /> 
							</h:column> 
 
						</h:dataTable> 
					</h:panelGroup> 
 
				</h:panelGrid> 
				<h:commandButton id="updateObsv" value="Update Observations" 
					actionListener="#{DislocBean.toggleUpdateProjectObservations}" /> 
 
			</h:form> 
		</h:panelGroup> 
 
		<h:form id="RunDisloc"> 
			<h:panelGrid columns="1"  
             id="stuff89" 
				rendered="#{!(empty DislocBean.myFaultEntryForProjectList) 
							   && !(empty DislocBean.myObsvEntryForProjectList)}" 
				footerClass="subtitle" 
				headerClass="subtitlebig" styleClass="medium" 
				columnClasses="subtitle,medium"> 
 
				<h:outputFormat escape="false"  id="stuff90" 
					value="<b>Run Disloc</b><br><br>" /> 
				<h:outputFormat escape="false"   id="stuff91" 
					value="Click the button below to run Disloc.<br><br>" /> 
 
					<h:commandButton value="Run Disloc" 
						action="#{DislocBean.runBlockingDislocJSF}" /> 
			</h:panelGrid> 
		</h:form> 
	</h:panelGrid> 
   </h:panelGroup> 
 
	<h:form> 
		<hr /> 
		<h:commandLink   id="stuff93" action="disloc-back"> 
			<h:outputText value="#{DislocBean.codeName} Main Menu" /> 
		</h:commandLink> 
	</h:form> 
 
</f:view> 
 
</body> 
</html> 
