package cgl.quakesim.simplex;

import java.net.URL;
import java.text.DecimalFormat;
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

//Need this to get the factor(double,double) method
import org.servogrid.genericproject.GenericProjectBean;
import org.servogrid.genericproject.FaultDBEntry;

public class editProjectForm extends GenericProjectBean {
	 
	 String obsvTextArea="";
	 Fault currentFault = new Fault();
	 Observation currentObservation = new Observation();
	 String projectSelectionCode = "";

	 boolean renderCreateObsvCutPaste=false;
	 boolean renderCreateObservationForm = false;
	 boolean renderCreateNewFaultForm = false;
	 boolean renderDislocListForm = false;
	 boolean renderAddFaultSelectionForm = false;
	 boolean renderSearchByFaultNameForm = false;
	 boolean renderSearchByAuthorForm = false;
	 boolean renderSearchByLatLonForm = false;	 
	 boolean renderViewAllFaultsForm = false;
	 boolean renderGPSStationMap = false;
	 boolean renderObsvEntries = false;
	 boolean renderFaultMap = false;

	 String faultSelectionCode = "";
	 boolean renderAddFaultFromDBForm = false;
	 String forSearchStr = new String("");
	 List myFaultDBEntryList = new ArrayList();
	 String selectdbURL="";

	 String mapFaultName;	 

	 String faultName = "newfault";
	 
	 public String getFaultName() {
		return faultName;
	}
	public void setFaultName(String faultName) {
		this.faultName = faultName;
	}

	String faultLatStart;
	 String faultLatEnd;
	 String faultLonStart;
	 String faultLonEnd;

		
	 private HtmlDataTable myFaultDataTable;
	 double projectOriginLon, projectOriginLat;
	 
	 projectEntry currentProject;


	 String kmlfiles = ""; // This should be passed from SimplexBean (faces-config.xml)
	 String codeName = ""; // This should be passed from SimplexBean (faces-config.xml)

	 DecimalFormat df;
	 

	 /**
	  * Create the bean
	  */
	 public editProjectForm() {
		  //The db url will need to be set separately.
		  this("");
	 }
	 public editProjectForm(String selectdbURL) {
		  super();
		  this.selectdbURL=selectdbURL;
		  System.out.println("editProjectForm Created");
		  df = new DecimalFormat(".###");
	 }
	 
	public void initEditFormsSelection() {
		 renderCreateObsvCutPaste=false;
		 renderSearchByFaultNameForm = false;
		 renderSearchByAuthorForm = false;
		 renderSearchByLatLonForm = false;
		 renderViewAllFaultsForm = false;
		 renderCreateObservationForm = false;
		 renderCreateNewFaultForm = false;
		 renderDislocListForm = false;
		 renderAddFaultSelectionForm = false;
		 renderAddFaultFromDBForm = false;
		 renderGPSStationMap = false;
		 renderFaultMap =  false;
	}	
	 
	 public void toggleShowObsvEntries(ActionEvent ev) {
		  renderObsvEntries=!renderObsvEntries;
	 }

	 public void toggleProjectSelection(ActionEvent ev) {
		initEditFormsSelection();
		if (projectSelectionCode.equals("ShowObservation")) {
			currentObservation=new Observation();
			renderCreateObservationForm = !renderCreateObservationForm;
		}

		else if (projectSelectionCode.equals("ShowObsvCutPaste")) {
			renderCreateObsvCutPaste = !renderCreateObsvCutPaste;
		}

		else if (projectSelectionCode.equals("CreateNewFault")) {
			currentFault= new Fault();
			renderCreateNewFaultForm = !renderCreateNewFaultForm;
		}
		else if (projectSelectionCode.equals("ShowDislocList")) {
//			QueryLayersList();
			renderDislocListForm = !renderDislocListForm;
		}

		else if (projectSelectionCode.equals("AddFaultSelection")) {
			renderAddFaultSelectionForm = !renderAddFaultSelectionForm;
		}

		else if (projectSelectionCode.equals("ShowGPSObsv")) {
			 System.out.println("Showing GPS Map");
			 renderGPSStationMap=!renderGPSStationMap;
		}
		
		else if (projectSelectionCode.equals("ShowFaultMap")) {
			 System.out.println("Showing Fault Map");
			 renderFaultMap=!renderFaultMap;
		}
		
		else if (projectSelectionCode.equals("")) {
			 ;
		}

		else {
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
			// myFaultDBEntryList=ViewAllFaults(selectdbURL);
			KMLdescriptionparser kdp = new KMLdescriptionparser();
			kdp.parseXml(getBasePath() + "/" + codeName + "/", kmlfiles);
			myFaultDBEntryList = kdp.getFaultList("All", "");
			
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
	
	// public String getDBValue(Select select, String param, String theFault,
	// 		String theSegment) throws Exception {

	// 	String DB_RESPONSE_HEADER = "results of the query:";
	// 	System.out.println("SQL Query on:" + param);

	// 	String sqlQuery = "select " + param
	// 			+ " from SEGMENT, REFERENCE where FaultName=\'" + theFault
	// 			+ "\' and SegmentName=\'" + theSegment
	// 			+ "\' and SEGMENT.InterpId=REFERENCE.InterpId;";

	// 	String tmp = select.select(sqlQuery);
	// 	if (tmp == null || tmp.equals("null") || tmp.equals("")) {
	// 		System.out.println();
	// 		return "0.0";
	// 	}

	// 	if (tmp.indexOf("no data") > -1)
	// 		return "0.0";
	// 	if (tmp.length() > DB_RESPONSE_HEADER.length() + 1) {
	// 		tmp = tmp.substring(DB_RESPONSE_HEADER.length() + 1);
	// 		tmp = tmp.substring(param.length() + 1);
	// 		if (tmp.trim().equals("null"))
	// 			return "0.0";
	// 		else
	// 			return tmp.trim();
	// 	} else {
	// 		return "0.0";
	// 	}

	// }
	
	public void createFaultFromMap() {
		
		Fault tmp_fault = new Fault();
		System.out.println ("[createFaultFromMap] started");
		
		double dip = 0;
		double depth = 0;
		double width = 0;    

		double latEnd = Double.parseDouble(faultLatEnd);
		double latStart = Double.parseDouble(faultLatStart);
		double lonStart = Double.parseDouble(faultLonStart);
		double lonEnd = Double.parseDouble(faultLonEnd);


		// Calculate the length			
		double d2r = Math.acos(-1.0) / 180.0;
		double flatten=1.0/298.247;

		double x = (lonEnd - lonStart) * factor(lonStart,latStart);
		double y = (latEnd - latStart) * 111.32;

		// String length = df.format(Math.sqrt(x * x + y * y));
		double length=Double.parseDouble(df.format(Math.sqrt(x * x + y * y)));
		tmp_fault.setFaultName(faultName);
		
		tmp_fault.setFaultLength(length+"");
		tmp_fault.setFaultWidth(width+"");
		tmp_fault.setFaultDepth (depth+"");
		tmp_fault.setFaultDipAngle(dip+"");
		
		//Probably hokey default values
		tmp_fault.setFaultSlip ("1.0"); 
		tmp_fault.setFaultRakeAngle("1.0");
		tmp_fault.setFaultLonStarts(lonStart+"");
		tmp_fault.setFaultLatStarts(latStart+"");
		tmp_fault.setFaultLonEnds(lonEnd+"");
		tmp_fault.setFaultLatEnds(latEnd+"");
		
		//Set the strike
		double strike=Math.atan2(x,y)/d2r;
		tmp_fault.setFaultStrikeAngle(strike+"");
		
		//Set the origin
		//This is the (x,y) of the fault relative to the project's origin
		//The project origin is the lower left lat/lon of the first fault.
		//If any of these conditions hold, we need to reset.
		
		System.out.println("Check Project Origin: "
								+currentProject.getOrigin_lat()
								+currentProject.getOrigin_lon());
		
		if(currentProject.getOrigin_lat()==projectEntry.DEFAULT_LAT
			|| currentProject.getOrigin_lon()==projectEntry.DEFAULT_LON ) {
			currentProject.setOrigin_lat(latStart);
			currentProject.setOrigin_lon(lonStart);
		}
		double projectOriginLat=currentProject.getOrigin_lat();
		double projectOriginLon=currentProject.getOrigin_lon();

		System.out.println("Confirm Project Origin: "
								+currentProject.getOrigin_lat()
								+currentProject.getOrigin_lon());
					
		//The following should be done in any case.  If the origin was just (re)set above,
		//we will get a harmless (0,0);
		System.out.println("lonStart : " + lonStart);
		System.out.println("latStart : " + latStart);
		double x1=(lonStart-projectOriginLon)*factor(projectOriginLon,projectOriginLat);
		double y1=(latStart-projectOriginLat)*111.32;
		System.out.println("Fault origin: "+x1+" "+y1);
		tmp_fault.setFaultLocationX(df.format(x1));
		tmp_fault.setFaultLocationY(df.format(y1));
		
		currentFault = tmp_fault;
		
		
	}
	
	
	public Fault QueryFaultFromDB(String faultname) {
		// Check request with fallback

		/* modified to the new module importing from kml desc 09/12/18 Jun Ji at CGL, jid@cs.indiana.edu
		*/

		Fault tmp_fault = new Fault();

		System.out.println ("[QueryFaultFromDB] faultname : " + faultname);

		String theFault = faultname;	    

		KMLdescriptionparser kdp = new KMLdescriptionparser();	 
		kdp.parseXml(getBasePath() + "/" + codeName + "/", kmlfiles);
		
		kdp.getDesc(theFault);
		kdp.parsevalues();
		
		try {
			double dip = kdp.getdip();
			double strike = kdp.getstrike();
			double depth = kdp.getdepth();
			double width = kdp.getwidth();
			double length = kdp.getlength();

			double latEnd = kdp.getlatEnd();
			double latStart = kdp.getlatStart();
			double lonStart = kdp.getlonStart();
			double lonEnd = kdp.getlonEnd();


			// Calculate the length			
			double d2r = Math.acos(-1.0) / 180.0;
			double flatten=1.0/298.247;

			double x = (lonEnd - lonStart) * factor(lonStart,latStart);
			double y = (latEnd - latStart) * 111.32;

			// String length = df.format(Math.sqrt(x * x + y * y));
			// double length=Double.parseDouble(df.format(Math.sqrt(x * x + y * y)));
			tmp_fault.setFaultName(theFault+"");			
			tmp_fault.setFaultLength(length+"");
			tmp_fault.setFaultWidth(width+"");
			tmp_fault.setFaultDepth (depth+"");
			tmp_fault.setFaultDipAngle(dip+"");
			
			//Probably hokey default values
			tmp_fault.setFaultSlip ("1.0"); 
			tmp_fault.setFaultRakeAngle("1.0");
			tmp_fault.setFaultLonStarts(lonStart+"");
			tmp_fault.setFaultLatStarts(latStart+"");
			tmp_fault.setFaultLonEnds(lonEnd+"");
			tmp_fault.setFaultLatEnds(latEnd+"");		
			
			
			//Set the strike
			//			double strike=Math.atan2(x,y)/d2r;
			tmp_fault.setFaultStrikeAngle(strike+"");
			
			//Set the origin
			//This is the (x,y) of the fault relative to the project's origin
			//The project origin is the lower left lat/lon of the first fault.
			//If any of these conditions hold, we need to reset.
			
			System.out.println("Check Project Origin: "
									+currentProject.getOrigin_lat()
									+currentProject.getOrigin_lon());
			
			if(currentProject.getOrigin_lat()==projectEntry.DEFAULT_LAT
				|| currentProject.getOrigin_lon()==projectEntry.DEFAULT_LON ) {
				currentProject.setOrigin_lat(latStart);
				currentProject.setOrigin_lon(lonStart);
			}
			double projectOriginLat=currentProject.getOrigin_lat();
			double projectOriginLon=currentProject.getOrigin_lon();

			System.out.println("Confirm Project Origin: "
									+currentProject.getOrigin_lat()
									+currentProject.getOrigin_lon());
						
			//The following should be done in any case.  If the origin was just (re)set above,
			//we will get a harmless (0,0);
			System.out.println("lonStart : " + lonStart);
			System.out.println("latStart : " + latStart);
			double x1=(lonStart-projectOriginLon)*factor(projectOriginLon,projectOriginLat);
			double y1=(latStart-projectOriginLat)*111.32;
			System.out.println("Fault origin: "+x1+" "+y1);
			tmp_fault.setFaultLocationX(df.format(x1));
			tmp_fault.setFaultLocationY(df.format(y1));
			
		      } catch (Exception ex) {
			      ex.printStackTrace();
		      }		

	      /*
	      String theFault = faultAndSegment.substring(0, faultAndSegment.indexOf("@"));
	      String theSegment=faultAndSegment.substring(faultAndSegment.indexOf("@") + 1, faultAndSegment.indexOf("%"));

	      String interpId=faultAndSegment.substring(faultAndSegment.indexOf("%") + 1, faultAndSegment.length());

	      faultAndSegment = "";

	      try {

		      SelectService ss = new SelectServiceLocator();
		      Select select = ss.getSelect(new URL(selectdbURL));

		      // --------------------------------------------------
		      // Make queries.
		      // --------------------------------------------------
		      String dip = getDBValue(select, "Dip", theFault, theSegment,interpId);
		      String strike = getDBValue(select, "Strike", theFault, theSegment,interpId);
		      String depth = getDBValue(select, "Depth", theFault, theSegment,interpId);
		      String width = getDBValue(select, "Width", theFault, theSegment,interpId);

		      // Get the length and width
		      double latEnd = Double.parseDouble(getDBValue(select, "LatEnd",
																	      theFault, theSegment,interpId));
		      double latStart = Double.parseDouble(getDBValue(select, "LatStart",
																		      theFault, theSegment,interpId));
		      double lonStart = Double.parseDouble(getDBValue(select, "LonStart",
																		      theFault, theSegment,interpId));
		      double lonEnd = Double.parseDouble(getDBValue(select, "LonEnd",
																	      theFault, theSegment,interpId));
		      
		      // Calculate the length
		      NumberFormat format = NumberFormat.getInstance();
		      double d2r = Math.acos(-1.0) / 180.0;
		      double flatten=1.0/298.247;

		      double x = (lonEnd - lonStart) * factor(lonStart,latStart);
		      double y = (latEnd - latStart) * 111.32;

		      String length = df.format(Math.sqrt(x * x + y * y));
		      tmp_fault.setFaultName(theFault );
		      tmp_fault.setFaultLength(length);
		      tmp_fault.setFaultWidth(width);
		      tmp_fault.setFaultDepth (depth);
		      tmp_fault.setFaultDipAngle(dip);
		      
		      //Probably hokey default values
		      tmp_fault.setFaultSlip ("1.0"); 
		      tmp_fault.setFaultRakeAngle("1.0");
		      tmp_fault.setFaultLonStarts(lonStart+"");
		      tmp_fault.setFaultLatStarts(latStart+"");
		      tmp_fault.setFaultLonEnds(lonEnd+"");
		      tmp_fault.setFaultLatEnds(latEnd+"");
		      
		      //Set the strike
		      strike=df.format(Math.atan2(x,y)/d2r);
		      tmp_fault.setFaultStrikeAngle(strike);
		      
		      //Set the origin
		      //This is the (x,y) of the fault relative to the project's origin
		      //The project origin is the lower left lat/lon of the first fault.
		      //If any of these conditions hold, we need to reset.
		      
		      System.out.println("Check Project Origin: "
								      +currentProject.getOrigin_lat()
								      +currentProject.getOrigin_lon());
		      
		      if(currentProject.getOrigin_lat()==projectEntry.DEFAULT_LAT
			      || currentProject.getOrigin_lon()==projectEntry.DEFAULT_LON ) {
			      currentProject.setOrigin_lat(latStart);
			      currentProject.setOrigin_lon(lonStart);
		      }
		      double projectOriginLat=currentProject.getOrigin_lat();
		      double projectOriginLon=currentProject.getOrigin_lon();

		      System.out.println("Confirm Project Origin: "
								      +currentProject.getOrigin_lat()
								      +currentProject.getOrigin_lon());
					      
		      //The following should be done in any case.  If the origin was just (re)set above,
		      //we will get a harmless (0,0);
		      double x1=(lonStart-projectOriginLon)*factor(projectOriginLon,projectOriginLat);
		      double y1=(latStart-projectOriginLat)*111.32;
		      System.out.println("Fault origin: "+x1+" "+y1);
		      tmp_fault.setFaultLocationX(df.format(x1));
		      tmp_fault.setFaultLocationY(df.format(y1));
		      
	      } catch (Exception ex) {
		      ex.printStackTrace();
	      }
		  
	    */	 
		return tmp_fault;
	}
		
	public void toggleFaultSearchByName(ActionEvent ev) {
		initEditFormsSelection();
		this.forSearchStr = this.forSearchStr.trim();
		if (!this.forSearchStr.equals("")) {
		    // myFaultDBEntryList=QueryFaultsByName(this.forSearchStr,selectdbURL);
		    KMLdescriptionparser kdp = new KMLdescriptionparser();
		    kdp.parseXml(getBasePath() + "/" + codeName + "/", kmlfiles);
		    myFaultDBEntryList = kdp.getFaultList("Name", this.forSearchStr);
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
		    // myFaultDBEntryList=QueryFaultsByLonLat(this.faultLatStart, this.faultLatEnd,this.faultLonStart, this.faultLonEnd, selectdbURL);
		    KMLdescriptionparser kdp = new KMLdescriptionparser();
		    kdp.parseXml(getBasePath() + "/" + codeName + "/", kmlfiles);
		    myFaultDBEntryList = kdp.getFaultList("LonLat", this.faultLatStart + " " + this.faultLatEnd + " " + this.faultLonStart + " " + this.faultLonEnd);
		}
		renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
	}

	public void toggleFaultSearchByAuthor(ActionEvent ev) {
		initEditFormsSelection();
		this.forSearchStr = this.forSearchStr.trim();
		if (!this.forSearchStr.equals("")) {
		    myFaultDBEntryList=QueryFaultsByAuthor(this.forSearchStr,selectdbURL);
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

    public boolean getRenderFaultMap() {
		  return this.renderFaultMap;
    }
    
    public void setRenderFaultMap(boolean renderFaultMap) {
		  this.renderFaultMap=renderFaultMap;
    }
    



	public HtmlDataTable getMyFaultDataTable() {
		return myFaultDataTable;
	}

	public void setMyFaultDataTable(HtmlDataTable tmp_DataTable) {
		this.myFaultDataTable = tmp_DataTable;
	}

	 public void setRenderCreateObsvCutPaste(boolean renderCreateObsvCutPaste){
		  this.renderCreateObsvCutPaste=renderCreateObsvCutPaste;
	 }
	 
	 public boolean getRenderCreateObsvCutPaste() {
		  return renderCreateObsvCutPaste;
	 }

	 public void setRenderGPSStationMap(boolean renderGPSStationMap){
		  this.renderGPSStationMap=renderGPSStationMap;
	 }
	 
	 public boolean getRenderGPSStationMap() {
		  return renderGPSStationMap;
	 }
	 
	 public void setObsvTextArea(String obsvTextArea) {
		  this.obsvTextArea=obsvTextArea;
	 }
	 
	 public String getObsvTextArea() {
		  return obsvTextArea;
	 }
	 
	 public void setProjectEntry(projectEntry currentProject) {
		  this.currentProject=currentProject;
	 }
	 
	 public projectEntry getProjectEntry() {
		  return currentProject;
	 }

	 public boolean getRenderObsvEntries() {
		  return renderObsvEntries;
	 }

	 public void setRenderObsvEntries(boolean renderObsvEntries) {
		  this.renderObsvEntries=renderObsvEntries;
	 }

	 public String getFaultLatStart() {
			return faultLatStart;
		}
		public void setFaultLatStart(String faultLatStart) {
			this.faultLatStart = faultLatStart;
		}
		public String getFaultLatEnd() {
			return faultLatEnd;
		}
		public void setFaultLatEnd(String faultLatEnd) {
			this.faultLatEnd = faultLatEnd;
		}
		public String getFaultLonStart() {
			return faultLonStart;
		}
		public void setFaultLonStart(String faultLonStart) {
			this.faultLonStart = faultLonStart;
		}
		public String getFaultLonEnd() {
			return faultLonEnd;
		}
		public void setFaultLonEnd(String faultLonEnd) {
			this.faultLonEnd = faultLonEnd;
		}
		
	 public String getKmlfiles() {
		return kmlfiles;
	 }

	 public void setKmlfiles(String kmlfiles) {
		this.kmlfiles = kmlfiles;
	 }

	 public String getCodeName() {
		return codeName;
	 }

	 public void setCodeName(String codeName) {
		this.codeName = codeName;
	 }

	 //--------------------------------------------------
	 /**
	  * Needed to make the map interface work
	  **/
	 public void toggleSetFaultFromMap(ActionEvent ev) throws Exception {
		  renderFaultMap=false;
		  try {
				System.out.println("Adding fault from map");
				
				initEditFormsSelection();
				
				String dbQuery=getMapFaultName();
				System.out.println("DB qeury:"+dbQuery);
				currentFault=QueryFaultFromDB(dbQuery);
				
				renderCreateNewFaultForm = true;
		  }
		  catch (Exception ex){
				System.out.println("Map fault selection error.");
				ex.printStackTrace();
		  }
	 } 
	 
	 public void setMapFaultName(String mapFaultName) {
		  this.mapFaultName=mapFaultName;
	 }

	 public String getMapFaultName() {
		  return this.mapFaultName;
	 }
	 //--------------------------------------------------


}
