package cgl.quakesim.disloc;

//Imports from the mother ship
import java.io.*;
import java.net.URL;
import java.util.*;
import java.text.*;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.event.*;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;

import javax.portlet.PortletContext;
import javax.servlet.ServletContext;

import org.servogrid.genericproject.GenericSopacBean;

import sun.misc.BASE64Encoder;

import TestClient.Select.Select;
import TestClient.Select.SelectService;
import TestClient.Select.SelectServiceLocator;

//Import the project utility class
import org.servogrid.genericproject.Utility;

//Import stuff from db4o
import com.db4o.*;


/**
 * Everything you need to set up and run MeshGenerator.
 */

public class DislocBean extends GenericSopacBean {
    
	 //Some navigation strings.
    static final String DEFAULT_USER_NAME="disloc_default_user";
	 static final String DISLOC_NAV_STRING="Disloc-this";

    /**
     * The following are property fields.  Associated get/set methods
     * are at the end of the code listing.
     */ 
    boolean renderCreateNewFaultForm = false;    
    boolean renderAddFaultSelectionForm = false;    
    String projectSelectionCode = "";    
    boolean renderSearchByFaultNameForm = false;    
    boolean renderSearchByAuthorForm = false;    
    boolean renderSearchByLatLonForm = false;
    boolean renderViewAllFaultsForm = false;    
    String faultSelectionCode = "";    
    boolean renderAddFaultFromDBForm = false;    
    Fault currentFault = new Fault();    

	 //These are search parameter strings.
	 String forSearchStr="";
    String faultLatStart = new String();    
    String faultLatEnd = new String();    
    String faultLonStart = new String();    
    String faultLonEnd = new String();    
    String jobToken="";

	 //These are useful object lists.
	 String[] selectProjectsArray;
	 String[] deleteProjectsArray;
	 List myProjectNameList=new ArrayList();
	 List myFaultCollection=new ArrayList();
    List myFaultDBEntryList = new ArrayList();    

	 HtmlDataTable myFaultDataTable;
	 
	 //Create the database
	 ObjectContainer db=null;

	 //Service information
	 DislocService dislocService;
	 String dislocServiceUrl="http://localhost:8080/dislocexec/services/DislocExec";
    String faultDBServiceUrl="http://gf2.ucs.indiana.edu:9090/axis/services/Select";

    /**
     * The client constructor.
     */
    public DislocBean() throws Exception {
		  super();

		  dislocService=new DislocServiceServiceLocator().getDislocExec(new URL(dislocServiceUrl));

		  //We are done.
		  System.out.println("Primary Disloc Bean Created");
    }
	 
    //--------------------------------------------------
    // This section contains the main execution calls.
    //--------------------------------------------------

    /**
     * Protected convenience method. 
     */ 
	 
	 protected void makeProjectDirectory() {
		  File projectDir=new File(getContextBasePath()+"/"+userName+"/"+codeName+"/");
		  projectDir.mkdirs();
	 }
	 
	 protected Fault[] getFaultsFromDB(){
		  Fault[] returnFaults=null;
 		  db=Db4o.openFile(getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
		  Fault faultToGet=new Fault();
		  ObjectSet results=db.get(faultToGet);
		  if(results.hasNext()) {
				returnFaults=new Fault[results.size()];
				for(int i=0;i<results.size();i++){
					 returnFaults[i]=(Fault)results.next();
				}
		  }
		  db.close();
		  return returnFaults;
	 }

	 protected DislocParamsBean getDislocParamsFromDB(){
		  DislocParamsBean paramsBean=new DislocParamsBean();
 		  db=Db4o.openFile(getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
		  ObjectSet results=db.get(paramsBean);
		  if(results.hasNext()) {
				paramsBean=(DislocParamsBean)results.next();
		  }
		  db.close();
		  
		  return paramsBean;
	 }
	 
	 protected void storeResultsInContext(String userName,
													  String projectName,
													  String jobUIDStamp,
													  DislocResultsBean dislocResultsBean) {
		  //Fill in later
	 }


    /**
     * This is a JSF compatible method for running Disloc
     * in blocking mode.  That is, it takes no argument and assumes
     * the values have been set by accessors.
     */ 
    public String runBlockingDislocJSF() 
		  throws Exception {
		  
		  Fault[] faults=getFaultsFromDB();
		  DislocParamsBean paramsBean=getDislocParamsFromDB();
		  
		  DislocResultsBean dislocResultsBean=dislocService.runBlockingDisloc(userName,
																									 projectName,
																									 paramsBean,
																									 faults,
																									 null);
		  setJobToken(dislocResultsBean.getJobUIDStamp());
		  storeResultsInContext(userName,
										projectName,
										dislocResultsBean.getJobUIDStamp(),
										dislocResultsBean);
		  return DISLOC_NAV_STRING;
    }
    
    /**
     * This runs disloc in non-blocking mode, so it returns
     * immediately.  The client must determine separately if the mesh
     * generation has finished.
     */ 
    public String runNonBlockingDislocJSF() 
		  throws Exception {

		  Fault[] faults=getFaultsFromDB();
		  DislocParamsBean paramsBean=getDislocParamsFromDB();
		  
		  DislocResultsBean dislocResultsBean=dislocService.runNonBlockingDisloc(userName,
																										 projectName,
																										 paramsBean,
																										 faults,
																										 null);
		  setJobToken(dislocResultsBean.getJobUIDStamp());
		  storeResultsInContext(userName,
										projectName,
										dislocResultsBean.getJobUIDStamp(),
										dislocResultsBean);

		  return DISLOC_NAV_STRING;
    }

	 // End main execution method section.
	 //--------------------------------------------------

    //--------------------------------------------------
    // The methods below are used to interact with the
    // Fault DB service.
    //--------------------------------------------------

    /**
     * This method is used to query the fault database using a lat/lon
     * bounding box.
     */
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
				+ " and LonStart<`="
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
				tmp_FaultDBEntry.faultName=new SelectItem(tmp1 + "@"
																		+ faultSegmentNameList.get(i).toString(), tmp1);
				tmp_FaultDBEntry.faultAuthor=faultAuthorList.get(i).toString();
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
    
    /**
     * This method queries the DB by author name.
     */
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
	 
    /**
     * Query the fault db by the fault name.
     */ 
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
    
    /** 
     * Queries the fault db for everything.
     */
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
				Select select = ss.getSelect(new URL(faultDBServiceUrl));
	    
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
    
    public String getDBValue(Select select, String param, String theLayer)
		  throws Exception {
		  
		  String DB_RESPONSE_HEADER = "results of the query:";
		  
		  String sqlQuery = "select " + param
				+ " from LAYER, LREFERENCE where LayerName=\'" + theLayer
				+ "\' and LAYER.InterpId=LREFERENCE.InterpId;";
	
		  String tmp = select.select(sqlQuery);
		  tmp = tmp.substring(DB_RESPONSE_HEADER.length() + 1);
		  tmp = tmp.substring(param.length() + 1);
		  tmp = tmp.trim();
		  if (tmp != null && !tmp.equals("null")) {
				return tmp.trim();
		  } else {
				return "0.0";
		  }
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
				Select select = ss.getSelect(new URL(faultDBServiceUrl));
				
				// --------------------------------------------------
				// Make queries.
				// --------------------------------------------------
				double dip = Double.parseDouble(getDBValue(select, "Dip", theFault, theSegment));
				double strike = Double.parseDouble(getDBValue(select, "Strike", theFault, theSegment));
				double depth = Double.parseDouble(getDBValue(select, "Depth", theFault, theSegment));
				double width = Double.parseDouble(getDBValue(select, "Width", theFault, theSegment));
				
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
				//				String length = format.format(Math.sqrt(x * x + y * y));
				double length = Math.sqrt(x * x + y * y);
				tmp_fault.setFaultName (theFault);
				tmp_fault.setFaultLocationX(0.0);
				tmp_fault.setFaultLocationY(0.0);
				tmp_fault.setFaultLength(length);
				tmp_fault.setFaultWidth(width);
				tmp_fault.setFaultDepth(depth);
				tmp_fault.setFaultDipAngle(dip);
				tmp_fault.setFaultStrikeAngle(strike);
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  return tmp_fault;
    }
	 
    //--------------------------------------------------
    // End of the fault db section
    //--------------------------------------------------
	 
    //--------------------------------------------------
    // Begin the event handling section for the JSF pages.
    //--------------------------------------------------

    public void handleFaultsRadioValueChange(ValueChangeEvent event) {
		  
		  try {
				// Catch the MyData item during the third phase of the JSF
				// lifecycle.
				currentFault = (Fault) getMyFaultDataTable().getRowData();
		  } catch (Exception e) {
				e.printStackTrace();
		  }
    }
    
    /**
     * This is used to handle the forms for editing a fault's params.
     */ 
    public String handleFaultEntryEdit(ActionEvent ev) {
		  try {
				// Catch the MyData item during the third phase of the JSF
				// lifecycle.
				currentFault = (Fault) getMyFaultDataTable().getRowData();
		  } catch (Exception e) {
				e.printStackTrace();
		  }
		  initEditFormsSelection();
		  currentFault.setFaultName(currentFault.getFaultName().trim());
		  if (!currentFault.getFaultName().equals("")) {
				currentFault = QueryFaultFromDB(currentFault.getFaultName().trim());
		  }
		  renderCreateNewFaultForm = !renderCreateNewFaultForm;
		  
		  return "edit"; // Navigation case.
    }
    
    
    //--------------------------------------------------
    // End change event listener handlers.
    //--------------------------------------------------

    //--------------------------------------------------
    // Begin action event processing methods.
    //--------------------------------------------------

    /**
     * This method clears all the render choices, setting them to false.
	  * Not sure if this should be public.
     */
    public void initEditFormsSelection() {
		  renderSearchByFaultNameForm = false;
		  renderSearchByAuthorForm = false;
		  renderSearchByLatLonForm = false;
		  renderViewAllFaultsForm = false;
		  renderCreateNewFaultForm = false;
		  renderAddFaultSelectionForm = false;
		  renderAddFaultFromDBForm = false;
    }

	 /**
	  * This is called when a project is seleted for loading.
	  */
    public String toggleSelectProject() throws Exception  {
		  initEditFormsSelection();
		  //This is implemented as a selectmanycheckbox on the client side (LoadProject.jsp),
		  //hence the unusual for loop.
		  if (selectProjectsArray != null) {
				for (int i = 0; i < 1; i++) {
					 this.projectName = selectProjectsArray[0];
				}
		  }

		  //Reconstruct the fault and layer object collections from the context
		  myFaultCollection=populateFaultCollection(projectName);

		  projectSelectionCode = "";
		  faultSelectionCode = "";
		  return "disloc-edit-project";
    }

    /**
     * Handle action events in the project selection area.
     */
    public void toggleProjectSelection(ActionEvent ev) {
		  initEditFormsSelection();
		  if (projectSelectionCode.equals("CreateNewFault")) {
				currentFault=new Fault();
				renderCreateNewFaultForm = !renderCreateNewFaultForm;
		  }
		  if (projectSelectionCode.equals("AddFaultSelection")) {
				renderAddFaultSelectionForm = !renderAddFaultSelectionForm;
		  }
		  if (projectSelectionCode.equals("")) {
				;
		  }
    }
    
    /**
     * Handle fault selection events.
     */ 
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
	 
    /**
     * Handle fault db entry events.
     */
    public void toggleSelectFaultDBEntry(ActionEvent ev) {
		  initEditFormsSelection();
		  currentFault.setFaultName(currentFault.getFaultName().trim());
		  if (!currentFault.getFaultName().equals("")) {
				currentFault = QueryFaultFromDB(currentFault.getFaultName().trim());
		  }
		  renderCreateNewFaultForm = !renderCreateNewFaultForm;
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

    /**
	  * This will return all of the faults in the db for a given project.
	  */
	 protected List populateFaultCollection(String projectName) throws Exception {
		  List myFaultCollection=new ArrayList();
		  db=Db4o.openFile(getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
		  ObjectSet results=db.get(Fault.class);
		  //Should only have one value.
		  Fault currentFault=null;
		  while(results.hasNext()){
				currentFault=(Fault)results.next();
				myFaultCollection.add(currentFault);
		  }
		  db.close();
		  return myFaultCollection;
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
        
    public void toggleAddFaultForProject(ActionEvent ev) throws Exception {
		  initEditFormsSelection();
		  db=Db4o.openFile(getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");
				 
		  Fault tmpfault=new Fault();
		  tmpfault.setFaultName(currentFault.getFaultName());
		  ObjectSet result=db.get(tmpfault);
		  if(result.hasNext()) {
				tmpfault=(Fault)result.next();
				db.delete(tmpfault);
		  }
		  db.set(currentFault);
		  db.commit();
		  db.close();
    }

    /**
     * These are methods associated with Faces navigations.
     */
    public String newProject() throws Exception {
		  isInitialized = getIsInitialized();
		  if (!isInitialized) {
				initWebServices();
		  }
		  setContextList();
		  makeProjectDirectory();
		  return ("disloc-new-project");
    }
    
    public void init_edit_project() {
		  initEditFormsSelection();
		  projectSelectionCode = "";
		  faultSelectionCode = "";
    }
    

    public String NewProjectThenEditProject() throws Exception {
		  setProjectName();
		  init_edit_project();
		  return "disloc-edit-project";
    }
    
    public String toggleDeleteProject() {
		  try {
				db=Db4o.openFile(getContextBasePath()+"/"+userName+"/"+codeName+".db");
				if (deleteProjectsArray != null) {
					 for (int i = 0; i < deleteProjectsArray.length; i++) {
						  DislocProjectBean delproj=new DislocProjectBean();
						  delproj.setProjectName(deleteProjectsArray[i]);
						  ObjectSet results=db.get(delproj);
						  if(results.hasNext()){
								delproj=(DislocProjectBean)results.next();
								db.delete(delproj);
						  }
					 }
				}
				db.close();
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
		  return "disloc-this";
    }
    
    public String setProjectName() throws Exception {
		  System.out.println("Creating new project");
		  makeProjectDirectory();
 		  db=Db4o.openFile(getContextBasePath()+"/"+userName+"/"+codeName+".db");		  		  
		  DislocProjectBean project=new DislocProjectBean();
		  project.setProjectName(projectName);
		  db.set(project);
		  db.commit();
		  db.close();

		  return "disloc-set-project";
    }
        
    public String loadProjectList() throws Exception {
		  if (!isInitialized) {
				initWebServices();
		  }
		  setContextList();
		  makeProjectDirectory();

		  return ("disloc-list-project");
    }


	 /**
	  * Contains a list of project beans.
	  */ 
    public List getMyProjectNameList() {
		  this.myProjectNameList.clear();
		  try {
				db=Db4o.openFile(getContextBasePath()+"/"+userName+"/"+codeName+".db");		  
				DislocProjectBean project=new DislocProjectBean();
				ObjectSet results=db.get(DislocProjectBean.class);
				//System.out.println("Got results:"+results.size());
				while(results.hasNext()) {
					 project=(DislocProjectBean)results.next();
					 //System.out.println(project.getProjectName());
					 myProjectNameList.add(new SelectItem(project.getProjectName(),
																	  project.getProjectName()));
				}
				db.close();
				
		  } catch (Exception ex) {
				//ex.printStackTrace();
				System.err.println("Could not open "+getContextBasePath()
										 +"/"+userName+"/"+codeName+".db");		
				System.err.println("Returning empty list.");  
		  }
		  return this.myProjectNameList;
    }

	 public String[] getDeleteProjectsArray() { return this.deleteProjectsArray; }
	 public String[] getSelectProjectsArray() { return this.selectProjectsArray; }

    public String getForSearchStr() {
		  return this.forSearchStr;
    }


	 public void setSelectProjectsArray(String[] selectProjectsArray) { 
		  this.selectProjectsArray=selectProjectsArray; 
	 }
	 public void setDeleteProjectsArray(String[] deleteProjectsArray) { 
		  this.deleteProjectsArray=deleteProjectsArray; 
	 }
	 public void setMyProjectNameList(List myProjectNameList) { this.myProjectNameList=myProjectNameList; }

	 public void setForSearchStr(String tmp_str) {
		  this.forSearchStr = tmp_str;
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

    public List getMyFaultDBEntryList() {
		  return myFaultDBEntryList;
    }

    public void setJobToken(String jobToken){
		  this.jobToken=jobToken;
    }
    
    public String getJobToken(){
		  return jobToken;
    }
    
}
