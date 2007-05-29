package org.apache.myfaces.blank;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import TestClient.Select.Select;
import TestClient.Select.SelectService;
import TestClient.Select.SelectServiceLocator;
import WebFlowClient.ViscoViz.MyVTKServiceLocator;
import WebFlowClient.ViscoViz.MyVTKServicePortType;

public class editProjectForm {

	Fault currentFault = new Fault();

	Observation currentObservation = new Observation();

	String projectSelectionCode = "";
	
	boolean renderCreateObservationForm = false;

	boolean renderCreateNewFaultForm = false;

	boolean renderDislocListForm = false;

	boolean renderAddFaultSelectionForm = false;

	boolean renderSearchByFaultNameForm = false;

	boolean renderSearchByAuthorForm = false;

	boolean renderSearchByLatLonForm = false;

	boolean renderViewAllFaultsForm = false;

	String faultSelectionCode = "";

	boolean renderAddFaultFromDBForm = false;
	
	String forSearchStr = new String("");
	
	List myFaultDBEntryList = new ArrayList();

	String selectdbURL="http://gf2.ucs.indiana.edu:9090/axis/services/Select";

	String faultLatStart = new String();
	
	String faultLatEnd = new String();

	String faultLonStart = new String();

	String faultLonEnd = new String();
	
	private HtmlDataTable myFaultDataTable;

	/**
	 * default empty constructor
	 */
	public editProjectForm( String tmp) {
		super();
		selectdbURL=tmp;
		System.out.println("editProjectForm Created");
	}
	
	public void initEditFormsSelection() {
		renderSearchByFaultNameForm = false;
		renderSearchByAuthorForm = false;
		renderSearchByLatLonForm = false;
		renderViewAllFaultsForm = false;
		renderCreateObservationForm = false;
		renderCreateNewFaultForm = false;
		renderDislocListForm = false;
		renderAddFaultSelectionForm = false;
		renderAddFaultFromDBForm = false;

	}	
	public void toggleProjectSelection(ActionEvent ev) {
		initEditFormsSelection();
		if (projectSelectionCode.equals("ShowObservation")) {
			currentObservation=new Observation();
			renderCreateObservationForm = !renderCreateObservationForm;
		}
		if (projectSelectionCode.equals("CreateNewFault")) {
			currentFault= new Fault();
			renderCreateNewFaultForm = !renderCreateNewFaultForm;
		}
		if (projectSelectionCode.equals("ShowDislocList")) {
//			QueryLayersList();
			renderDislocListForm = !renderDislocListForm;
		}
		if (projectSelectionCode.equals("AddFaultSelection")) {
			renderAddFaultSelectionForm = !renderAddFaultSelectionForm;
		}
		if (projectSelectionCode.equals("")) {
			;
		}
	}

	public void handleFaultsRadioValueChange(ValueChangeEvent event) {

		try {
			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
			FaultDBEntry tmp_FaultDBEntry = (FaultDBEntry) getMyFaultDataTable()
					.getRowData();
			SelectItem tmp_SelectItem = tmp_FaultDBEntry.getFaultName();
			currentFault.setFaultName(tmp_SelectItem.getValue().toString()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String handleFaultEntryEdit(ActionEvent ev) {

		try {
			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
			FaultDBEntry tmp_FaultDBEntry = (FaultDBEntry) getMyFaultDataTable()
					.getRowData();
			SelectItem tmp_SelectItem = tmp_FaultDBEntry.getFaultName();
			currentFault.setFaultName(tmp_SelectItem.getValue().toString()) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		initEditFormsSelection();
		currentFault.setFaultName( currentFault.getFaultName().trim() );
		if (!currentFault.getFaultName().equals("")) {
			currentFault = QueryFaultFromDB(currentFault.getFaultName().trim());
		}
		renderCreateNewFaultForm = !renderCreateNewFaultForm;

		return "edit"; // Navigation case.
	}
	
	public void toggleFaultSelection(ActionEvent ev) {
		initEditFormsSelection();
		if (faultSelectionCode.equals("SearchByFaultName")) {
			renderSearchByFaultNameForm = !renderSearchByFaultNameForm;
		}
		if (faultSelectionCode.equals("SearchByAuthor")) {
			renderSearchByAuthorForm = !renderSearchByAuthorForm;
		}
		if (faultSelectionCode.equals("SearchByLatLon")) {
			renderSearchByLatLonForm = !renderSearchByLatLonForm;
		}
		if (faultSelectionCode.equals("ViewAllFaults")) {
			initEditFormsSelection();
			ViewAllFaults();
			renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
		}
		if (projectSelectionCode.equals("")) {
			;
		}
		faultSelectionCode = "";
	}

	public void toggleSelectFaultDBEntry(ActionEvent ev) {
		initEditFormsSelection();
		currentFault.setFaultName( currentFault.getFaultName().trim() );
		if (!currentFault.getFaultName().equals("")) {
			currentFault = QueryFaultFromDB(currentFault.getFaultName().trim());
		}
		renderCreateNewFaultForm = !renderCreateNewFaultForm;
	}

	public void ViewAllFaults() {

		String getFaultList = "SELECT F.FaultName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";
		String getAuthorList = "SELECT R.Author1 FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";
		String getSegmentList = "SELECT F.SegmentName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";
		String getLatStartList = "SELECT F.LatStart FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";
		String getLatEndList = "SELECT F.LatEnd FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";
		String getLonStartList = "SELECT F.LonStart FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";
		String getLonEndList = "SELECT F.LonEnd FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId";

		myFaultDBEntryList.clear();

		List faultSegmentNameList = QueryFaultsBySQL(getSegmentList);
		List faultAuthorList = QueryFaultsBySQL(getAuthorList);
		List faultLatStarts = QueryFaultsBySQL(getLatStartList);
		List faultLatEnds = QueryFaultsBySQL(getLatEndList);
		List faultLonStarts = QueryFaultsBySQL(getLonStartList);
		List faultLonEnds = QueryFaultsBySQL(getLonEndList);
		List tmp_faultNameList = QueryFaultsBySQL(getFaultList);
		for (int i = 0; i < tmp_faultNameList.size(); i++) {
			String tmp1 = tmp_faultNameList.get(i).toString();
			FaultDBEntry tmp_FaultDBEntry = new FaultDBEntry();
			tmp_FaultDBEntry.faultName = new SelectItem(tmp1 + "@"
					+ faultSegmentNameList.get(i).toString(), tmp1);
			tmp_FaultDBEntry.faultAuthor = faultAuthorList.get(i).toString();
			tmp_FaultDBEntry.faultSegmentName = faultSegmentNameList.get(i)
					.toString();
			tmp_FaultDBEntry.faultSegmentCoordinates = "("
					+ faultLatStarts.get(i).toString() + ","
					+ faultLatEnds.get(i).toString() + ")-("
					+ faultLonStarts.get(i).toString() + ","
					+ faultLonEnds.get(i).toString() + ")";
			myFaultDBEntryList.add(tmp_FaultDBEntry);

		}

	}	
	public List QueryFaultsBySQL(String tmp_query_sql) {
		List tmp_list = new ArrayList();
		try {

			String DB_RESPONSE_HEADER = "results of the query:";
			SelectService ss = new SelectServiceLocator();
			Select select = ss.getSelect(new URL(selectdbURL));

			// --------------------------------------------------
			// Make queries.
			// --------------------------------------------------
			String tmp_str = select.select(tmp_query_sql);
			tmp_str = tmp_str.substring(DB_RESPONSE_HEADER.length());
			StringTokenizer st1 = new StringTokenizer(tmp_str, "\n");
			// They begin with blank lines ?!
			st1.nextToken();
			st1.nextToken();
			tmp_list.clear();
			while (st1.hasMoreTokens()) {
				String tmp1 = st1.nextToken().trim();
				if (tmp1 == null || tmp1.equals("null"))
					tmp1 = "N/A";
				tmp_list.add(tmp1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return tmp_list;
	}
	
	public String getDBValue(Select select, String param, String theFault,
			String theSegment) throws Exception {

		String DB_RESPONSE_HEADER = "results of the query:";
		System.out.println("SQL Query on:" + param);

		String sqlQuery = "select " + param
				+ " from SEGMENT, REFERENCE where FaultName=\'" + theFault
				+ "\' and SegmentName=\'" + theSegment
				+ "\' and SEGMENT.InterpId=REFERENCE.InterpId;";

		String tmp = select.select(sqlQuery);
		if (tmp == null || tmp.equals("null") || tmp.equals("")) {
			System.out.println();
			return "0.0";
		}

		if (tmp.indexOf("no data") > -1)
			return "0.0";
		if (tmp.length() > DB_RESPONSE_HEADER.length() + 1) {
			tmp = tmp.substring(DB_RESPONSE_HEADER.length() + 1);
			tmp = tmp.substring(param.length() + 1);
			if (tmp.trim().equals("null"))
				return "0.0";
			else
				return tmp.trim();
		} else {
			return "0.0";
		}

	}
	
	public Fault QueryFaultFromDB(String tmp_str) {
		// Check request with fallback

		String theFault = tmp_str.substring(0, tmp_str.indexOf("@"));
		String theSegment = tmp_str.substring(tmp_str.indexOf("@") + 1, tmp_str
				.length());
		tmp_str = "";
		Fault tmp_fault = new Fault();

		try {

			SelectService ss = new SelectServiceLocator();
			Select select = ss.getSelect(new URL(selectdbURL));

			// --------------------------------------------------
			// Make queries.
			// --------------------------------------------------
			String dip = getDBValue(select, "Dip", theFault, theSegment);
			String strike = getDBValue(select, "Strike", theFault, theSegment);
			String depth = getDBValue(select, "Depth", theFault, theSegment);
			String width = getDBValue(select, "Width", theFault, theSegment);

			// Get the length and width
			double latEnd = Double.parseDouble(getDBValue(select, "LatEnd",
					theFault, theSegment));
			double latStart = Double.parseDouble(getDBValue(select, "LatStart",
					theFault, theSegment));
			double lonStart = Double.parseDouble(getDBValue(select, "LonStart",
					theFault, theSegment));
			double lonEnd = Double.parseDouble(getDBValue(select, "LonEnd",
					theFault, theSegment));

			// Calculate the length
			NumberFormat format = NumberFormat.getInstance();
			double d2r = Math.acos(-1.0) / 180.0;
			double factor = d2r
					* Math.cos(d2r * latStart)
					* (6378.139 * (1.0 - Math.sin(d2r * latStart)
							* Math.sin(d2r * latStart) / 298.247));

			double x = (lonEnd - lonStart) * factor;
			double y = (latEnd - latStart) * 111.32;
			String length = format.format(Math.sqrt(x * x + y * y));
			tmp_fault.setFaultName( theFault );
			tmp_fault.setFaultLocationX("0.0" );
			tmp_fault.setFaultLocationY ("0.0");
			tmp_fault.setFaultLength(length);
			tmp_fault.setFaultWidth( width);
			tmp_fault.setFaultDepth (depth);
			tmp_fault.setFaultDipAngle( dip);
			tmp_fault.setFaultStrikeAngle( strike);
			tmp_fault.setFaultSlip ("" ); 
			tmp_fault.setFaultRakeAngle("");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return tmp_fault;
	}
	
	public void QueryFaultsByName(String input_str) {

		String getFaultList = "SELECT F.FaultName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and F.FaultName like \'%"
				+ input_str + "%\';";
		String getSegmentList = "SELECT F.SegmentName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and F.FaultName like \'%"
				+ input_str + "%\';";
		String getAuthorList = "SELECT R.Author1 FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and F.FaultName like \'%"
				+ input_str + "%\';";
		String getLatStartList = "SELECT F.LatStart FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and F.FaultName like \'%"
				+ input_str + "%\';";
		String getLatEndList = "SELECT F.LatEnd FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and F.FaultName like \'%"
				+ input_str + "%\';";
		String getLonStartList = "SELECT F.LonStart FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and F.FaultName like \'%"
				+ input_str + "%\';";
		String getLonEndList = "SELECT F.LonEnd FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and F.FaultName like \'%"
				+ input_str + "%\';";

		myFaultDBEntryList.clear();

		List faultSegmentNameList = QueryFaultsBySQL(getSegmentList);
		List faultAuthorList = QueryFaultsBySQL(getAuthorList);
		List faultLatStarts = QueryFaultsBySQL(getLatStartList);
		List faultLatEnds = QueryFaultsBySQL(getLatEndList);
		List faultLonStarts = QueryFaultsBySQL(getLonStartList);
		List faultLonEnds = QueryFaultsBySQL(getLonEndList);
		List tmp_faultNameList = QueryFaultsBySQL(getFaultList);
		for (int i = 0; i < tmp_faultNameList.size(); i++) {
			String tmp1 = tmp_faultNameList.get(i).toString();
			FaultDBEntry tmp_FaultDBEntry = new FaultDBEntry();
			tmp_FaultDBEntry.faultName = new SelectItem(tmp1 + "@"
					+ faultSegmentNameList.get(i).toString(), tmp1);
			tmp_FaultDBEntry.faultAuthor = faultAuthorList.get(i).toString();
			tmp_FaultDBEntry.faultSegmentName = faultSegmentNameList.get(i)
					.toString();
			tmp_FaultDBEntry.faultSegmentCoordinates = "("
					+ faultLatStarts.get(i).toString() + ","
					+ faultLatEnds.get(i).toString() + ")-("
					+ faultLonStarts.get(i).toString() + ","
					+ faultLonEnds.get(i).toString() + ")";
			myFaultDBEntryList.add(tmp_FaultDBEntry);

		}

	}
	
	public void QueryFaultsByAuthor(String input_str) {

		String getAuthorList = "SELECT R.Author1 FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and R.Author1 like \'%"
				+ input_str + "%\';";
		String getFaultList = "SELECT F.FaultName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and R.Author1 like \'%"
				+ input_str + "%\';";
		String getSegmentList = "SELECT F.SegmentName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and R.Author1 like \'%"
				+ input_str + "%\';";
		String getLatStartList = "SELECT F.LatStart FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and R.Author1 like \'%"
				+ input_str + "%\';";
		String getLatEndList = "SELECT F.LatEnd FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and R.Author1 like \'%"
				+ input_str + "%\';";
		String getLonStartList = "SELECT F.LonStart FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and R.Author1 like \'%"
				+ input_str + "%\';";
		String getLonEndList = "SELECT F.LonEnd FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and R.Author1 like \'%"
				+ input_str + "%\';";

		myFaultDBEntryList.clear();

		List faultSegmentNameList = QueryFaultsBySQL(getSegmentList);
		List faultAuthorList = QueryFaultsBySQL(getAuthorList);
		List faultLatStarts = QueryFaultsBySQL(getLatStartList);
		List faultLatEnds = QueryFaultsBySQL(getLatEndList);
		List faultLonStarts = QueryFaultsBySQL(getLonStartList);
		List faultLonEnds = QueryFaultsBySQL(getLonEndList);
		List tmp_faultNameList = QueryFaultsBySQL(getFaultList);
		for (int i = 0; i < tmp_faultNameList.size(); i++) {
			String tmp1 = tmp_faultNameList.get(i).toString();
			FaultDBEntry tmp_FaultDBEntry = new FaultDBEntry();
			tmp_FaultDBEntry.faultName = new SelectItem(tmp1 + "@"
					+ faultSegmentNameList.get(i).toString(), tmp1);
			tmp_FaultDBEntry.faultAuthor = faultAuthorList.get(i).toString();
			tmp_FaultDBEntry.faultSegmentName = faultSegmentNameList.get(i)
					.toString();
			tmp_FaultDBEntry.faultSegmentCoordinates = "("
					+ faultLatStarts.get(i).toString() + ","
					+ faultLatEnds.get(i).toString() + ")-("
					+ faultLonStarts.get(i).toString() + ","
					+ faultLonEnds.get(i).toString() + ")";
			myFaultDBEntryList.add(tmp_FaultDBEntry);

		}

	}
	
	public void QueryFaultsByLonLat(String input_str1, String input_str2,
			String input_str3, String input_str4) {

		String getAuthorList = "SELECT R.Author1 FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and LatStart>="
				+ input_str1
				+ " and LatEnd<="
				+ input_str2
				+ " and LonStart<="
				+ input_str3 + " and LonEnd>=" + input_str4 + ";";
		String getFaultList = "SELECT F.FaultName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and LatStart>="
				+ input_str1
				+ " and LatEnd<="
				+ input_str2
				+ " and LonStart<="
				+ input_str3 + " and LonEnd>=" + input_str4 + ";";
		String getSegmentList = "SELECT F.SegmentName FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and LatStart>="
				+ input_str1
				+ " and LatEnd<="
				+ input_str2
				+ " and LonStart<="
				+ input_str3 + " and LonEnd>=" + input_str4 + ";";
		String getLatStartList = "SELECT F.LatStart FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and LatStart>="
				+ input_str1
				+ " and LatEnd<="
				+ input_str2
				+ " and LonStart<="
				+ input_str3 + " and LonEnd>=" + input_str4 + ";";
		String getLatEndList = "SELECT F.LatEnd FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and LatStart>="
				+ input_str1
				+ " and LatEnd<="
				+ input_str2
				+ " and LonStart<="
				+ input_str3 + " and LonEnd>=" + input_str4 + ";";
		String getLonStartList = "SELECT F.LonStart FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and LatStart>="
				+ input_str1
				+ " and LatEnd<="
				+ input_str2
				+ " and LonStart<="
				+ input_str3 + " and LonEnd>=" + input_str4 + ";";
		String getLonEndList = "SELECT F.LonEnd FROM FAULT AS F, REFERENCE AS R WHERE R.InterpId=F.InterpId and LatStart>="
				+ input_str1
				+ " and LatEnd<="
				+ input_str2
				+ " and LonStart<="
				+ input_str3 + " and LonEnd>=" + input_str4 + ";";

		myFaultDBEntryList.clear();

		List faultSegmentNameList = QueryFaultsBySQL(getSegmentList);
		List faultAuthorList = QueryFaultsBySQL(getAuthorList);
		List faultLatStarts = QueryFaultsBySQL(getLatStartList);
		List faultLatEnds = QueryFaultsBySQL(getLatEndList);
		List faultLonStarts = QueryFaultsBySQL(getLonStartList);
		List faultLonEnds = QueryFaultsBySQL(getLonEndList);
		List tmp_faultNameList = QueryFaultsBySQL(getFaultList);
		for (int i = 0; i < tmp_faultNameList.size(); i++) {
			String tmp1 = tmp_faultNameList.get(i).toString();
			FaultDBEntry tmp_FaultDBEntry = new FaultDBEntry();
			tmp_FaultDBEntry.faultName = new SelectItem(tmp1 + "@"
					+ faultSegmentNameList.get(i).toString(), tmp1);
			tmp_FaultDBEntry.faultAuthor = faultAuthorList.get(i).toString();
			tmp_FaultDBEntry.faultSegmentName = faultSegmentNameList.get(i)
					.toString();
			tmp_FaultDBEntry.faultSegmentCoordinates = "("
					+ faultLatStarts.get(i).toString() + ","
					+ faultLatEnds.get(i).toString() + ")-("
					+ faultLonStarts.get(i).toString() + ","
					+ faultLonEnds.get(i).toString() + ")";
			myFaultDBEntryList.add(tmp_FaultDBEntry);

		}

	}
	
	public void toggleFaultSearchByName(ActionEvent ev) {
		initEditFormsSelection();
		this.forSearchStr = this.forSearchStr.trim();
		if (!this.forSearchStr.equals("")) {
			QueryFaultsByName(this.forSearchStr);
		}
		this.forSearchStr = "";
		renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
	}

	public void toggleFaultSearchByLonLat(ActionEvent ev) {
		initEditFormsSelection();
		this.faultLatStart = this.faultLatStart.trim();
		this.faultLatEnd = this.faultLatEnd.trim();
		this.faultLonStart = this.faultLonStart.trim();
		this.faultLonEnd = this.faultLonEnd.trim();
		if ((!this.faultLatStart.equals("")) && (!this.faultLatEnd.equals(""))
				&& (!this.faultLonStart.equals(""))
				&& (!this.faultLonEnd.equals(""))) {
			QueryFaultsByLonLat(this.faultLatStart, this.faultLatEnd,
					this.faultLonStart, this.faultLonEnd);
		}
		renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
	}

	public void toggleFaultSearchByAuthor(ActionEvent ev) {
		initEditFormsSelection();
		this.forSearchStr = this.forSearchStr.trim();
		if (!this.forSearchStr.equals("")) {
			QueryFaultsByAuthor(this.forSearchStr);
		}
		this.forSearchStr = "";
		renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
	}
	
	public void init_edit_project() {
		initEditFormsSelection();
		projectSelectionCode = "";
		faultSelectionCode = "";

	}

	public String getForSearchStr() {
		return this.forSearchStr;
	}

	public void setForSearchStr(String tmp_str) {
		this.forSearchStr = tmp_str;
	}
	
	public List getMyFaultDBEntryList() {
		return myFaultDBEntryList;
	}

	public void setCurrentObservation(Observation tmp) {
		this.currentObservation = tmp;
	}
	
	public Observation getCurrentObservation ( ) {
		return this.currentObservation;
	}
		
	public void setCurrentFault(Fault tmp_fault) {
		this.currentFault = tmp_fault;
	}

	public Fault getCurrentFault() {
		return this.currentFault;
	}

	public String getProjectSelectionCode() {
		return projectSelectionCode;
	}

	public void setProjectSelectionCode(String tmp_str) {
		this.projectSelectionCode = tmp_str;
	}

	public String getFaultSelectionCode() {
		return faultSelectionCode;
	}

	public void setFaultSelectionCode(String tmp_str) {
		this.faultSelectionCode = tmp_str;
	}

	public boolean getRenderSearchByFaultNameForm() {
		return renderSearchByFaultNameForm;
	}

	public void setRenderSearchByFaultNameForm(boolean tmp_boolean) {
		this.renderSearchByFaultNameForm = tmp_boolean;
	}

	public boolean getRenderSearchByAuthorForm() {
		return renderSearchByAuthorForm;
	}

	public void setRnderSearchByAuthorForm(boolean tmp_boolean) {
		this.renderSearchByAuthorForm = tmp_boolean;
	}

	public boolean getRenderSearchByLatLonForm() {
		return renderSearchByLatLonForm;
	}

	public void setRenderSearchByLatLonForm(boolean tmp_boolean) {
		this.renderSearchByLatLonForm = tmp_boolean;
	}

	public boolean getRenderViewAllFaultsForm() {
		return renderViewAllFaultsForm;
	}

	public void setRenderViewAllFaultsForm(boolean tmp_boolean) {
		this.renderViewAllFaultsForm = tmp_boolean;
	}

	public boolean getRenderCreateNewFaultForm() {
		return renderCreateNewFaultForm;
	}

	public void setRenderCreateNewFaultForm(boolean tmp_boolean) {
		this.renderCreateNewFaultForm = tmp_boolean;
	}

	public boolean getRenderDislocListForm() {
		return renderDislocListForm;
	}

	public void setRenderDislocListForm(boolean tmp_boolean) {
		this.renderDislocListForm = tmp_boolean;
	}

	public boolean getRenderCreateObservationForm() {
		return renderCreateObservationForm;
	}

	public void setRenderCreateObservationForm(boolean tmp_boolean) {
		this.renderCreateObservationForm = tmp_boolean;
	}

	public boolean getRenderAddFaultFromDBForm() {
		return renderAddFaultFromDBForm;
	}

	public void setRenderAddFaultFromDBForm(boolean tmp_boolean) {
		this.renderAddFaultFromDBForm = tmp_boolean;
	}

	public boolean getRenderAddFaultSelectionForm() {
		return renderAddFaultSelectionForm;
	}

	public void setRenderAddFaultSelectionForm(boolean tmp_boolean) {
		this.renderAddFaultSelectionForm = tmp_boolean;
	}

	public String getFaultLatStart() {
		return this.faultLatStart;
	}

	public void setFaultLatStart(String tmp_str) {
		this.faultLatStart = tmp_str;
	}

	public String getFaultLatEnd() {
		return this.faultLatEnd;
	}

	public void setFaultLatEnd(String tmp_str) {
		this.faultLatEnd = tmp_str;
	}

	public String getFaultLonStart() {
		return this.faultLonStart;
	}

	public void setFaultLonStart(String tmp_str) {
		this.faultLonStart = tmp_str;
	}

	public String getFaultLonEnd() {
		return this.faultLonEnd;
	}

	public void setFaultLonEnd(String tmp_str) {
		this.faultLonEnd = tmp_str;
	}

	public HtmlDataTable getMyFaultDataTable() {
		return myFaultDataTable;
	}

	public void setMyFaultDataTable(HtmlDataTable tmp_DataTable) {
		this.myFaultDataTable = tmp_DataTable;
	}
}
