package cgl.quakesim.disloc;

//Imports from the mother ship
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;
import java.text.*;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.event.*;
import javax.faces.component.UIData;
import javax.faces.event.ActionEvent;
import javax.faces.component.UIComponent;

import javax.portlet.PortletContext;
import javax.servlet.ServletContext;

import org.servogrid.genericproject.GenericSopacBean;
import org.servogrid.genericproject.FaultDBEntry;

//import sun.misc.BASE64Encoder;

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
    
    //KML stuff, need to move this to another place.
    String xmlHead="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    String kmlHead="<kml xmlns=\"http://earth.google.com/kml/2.2\">";
    String kmlEnd="</kml>";
    String pmBegin="<Placemark>";
    String pmEnd="</Placemark>";
    String lsBegin="<LineString>";
    String lsEnd="</LineString>";
    String pointBegin="<Point>";
    String pointEnd="</Point>";
    String coordBegin="<coordinates>";
    String coordEnd="</coordinates>";
    String docBegin="<Document>";
    String docEnd="</Document>";
    String comma=", ";
    String descBegin="<description>";
    String descEnd="</description>";
    
	 
    //Some navigation strings.
    static final String DEFAULT_USER_NAME="disloc_default_user";
    static final String DISLOC_NAV_STRING="disloc-submitted";
    static final String SEPARATOR="/";
    
    /**
     * The following are property fields.  Associated get/set methods
     * are at the end of the code listing.
     */ 
    boolean renderCreateNewFaultForm = false;    
    boolean renderAddFaultSelectionForm = false; 
    boolean renderDislocGridParamsForm = false;       
    boolean renderSearchByFaultNameForm = false;    
    boolean renderSearchByAuthorForm = false;    
    boolean renderSearchByLatLonForm = false;
    boolean renderViewAllFaultsForm = false;    
    boolean renderAddFaultFromDBForm = false;    
    boolean renderMap=false;
	 boolean usesGridPoints;
	 boolean renderChooseObsvStyleForm=false;

    Fault currentFault = new Fault();    
    //    DislocParamsBean dislocParams=new DislocParamsBean();
    DislocParamsBean currentParams=new DislocParamsBean();
    DislocProjectBean currentProject=new DislocProjectBean();
    DislocProjectSummaryBean currentSummary=new DislocProjectSummaryBean();
    String faultSelectionCode = "";    
    String projectSelectionCode = "";    
    String obsvStyleSelectionCode="";

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
	 String[] copyProjectsArray;

    List myProjectNameList=new ArrayList();
    List myFaultCollection=new ArrayList();
    List myDislocParamsCollection=new ArrayList();
    List myFaultDBEntryList = new ArrayList();    
    List myFaultEntryForProjectList = new ArrayList();    
    List myObservationsForProjectList=new ArrayList();
    List myObsvEntryForProjectList=new ArrayList();
    List myArchivedDislocResultsList=new ArrayList();
    List myPointObservationList=new ArrayList();
    List myInterpIdList=new ArrayList();

    HtmlDataTable myFaultDataTable,myProjectSummaryDataTable,myScatterPointsTable;
    
    //Create the database
    ObjectContainer db=null;
    
    //Service information
    DislocService dislocService;
    DislocExtendedService dislocExtendedService;
    
    String dislocServiceUrl;
    String dislocExtendedServiceUrl;
    String faultDBServiceUrl;
    String kmlGeneratorBaseurl;
    String kmlGeneratorUrl;
    
    String realPath;
    String codeName;
    String kmlProjectFile="network0.kml";

    double originLon, originLat;
    
    String faultKmlUrl;
    String portalBaseUrl;
    String faultKmlFilename;
	 String obsvKmlUrl;
	 String obsvKmlFilename;
	 
	 String gpsStationName="";
	 String gpsStationLat="";
	 String gpsStationLon="";
    NumberFormat format = null;
    
    /**
     * The client constructor.
     */
    public DislocBean() throws Exception {
	super();
	format=NumberFormat.getInstance();
	//		  currentParams.setObservationPointStyle(1);
	
	//We are done.
		  System.out.println("Primary Disloc Bean Created");
    }
    
    //--------------------------------------------------
    // This section contains the main execution calls.
    //--------------------------------------------------

    public void runTestCall() throws Exception{
	ObsvPoint[] testBean=new ObsvPoint[3];
	for(int i=0;i<testBean.length;i++) {
	    testBean[i]=new ObsvPoint();
	    testBean[i].setLatPoint(10.0+i+"");
	    testBean[i].setLonPoint(20.0+i+"");
	    testBean[i].setXcartPoint(30.0+i+"");
	    testBean[i].setYcartPoint(40.0+i+"");
	}

	initDislocExtendedService();
	dislocExtendedService.testCall(testBean);
    }
    
    /**
     * Protected convenience method. 
     */ 
    
    protected void initDislocService() throws Exception {
	dislocService=new DislocServiceServiceLocator().getDislocExec(new URL(dislocServiceUrl));
	System.out.println("Binding to: "+dislocServiceUrl);
    }
    
    protected void initDislocExtendedService() throws Exception {
	dislocExtendedService=new DislocExtendedServiceServiceLocator().
	    getDislocExtendedExec(new URL(dislocExtendedServiceUrl));
	System.out.println("Binding to: "+dislocExtendedServiceUrl);
    }
    
    protected void makeProjectDirectory() {
	File projectDir=new File(getBasePath()+"/"+
				 getContextBasePath()+"/"
				 +userName+"/"+codeName+"/");
	projectDir.mkdirs();
    }
    
    /**
     * This grabs the faults from the DB.
     */
    protected Fault[] getFaultsFromDB(){
	Fault[] returnFaults=null;
	db=Db4o.openFile(getBasePath()
			 +"/"+getContextBasePath()
			 +"/"+userName
			 +"/"+codeName
			 +"/"+projectName
			 +".db");		  
	//Fault faultToGet=new Fault();
	//	ObjectSet results=db.get(faultToGet);
	Fault faultToGet;
	ObjectSet results=db.get(Fault.class);
	if(results.hasNext()) {
	    returnFaults=new Fault[results.size()];
	    for(int i=0;i<results.size();i++){
		returnFaults[i]=(Fault)results.next();
	    }
	}
	db.close();
	return returnFaults;
    }

    /**
     * Grabs the scatter points from the DB.  This may be null.
     */ 
    protected ObsvPoint[] getObsvPointsFromDB(){
		  ObsvPoint[] returnPoints=null;
		  db=Db4o.openFile(getBasePath()
								 +"/"+getContextBasePath()
								 +"/"+userName
								 +"/"+codeName
								 +"/"+projectName
								 +".db");		  
		  
		  ObjectSet results=db.get(ObsvPoint.class);
		  if(results.hasNext()) {
				returnPoints=new ObsvPoint[results.size()];
				for(int i=0;i<results.size();i++){
					 returnPoints[i]=(ObsvPoint)results.next();
				}
		  }
		  db.close();
		  return returnPoints;
    }

    /**
     * Note this method assumes projectName has been set externally.  It will return a
     * new object if it can't locate one in the db.
     */
    protected DislocParamsBean getDislocParamsFromDB(String projectName){
		  //Create an empty params.  We will use this if we can't find one
		  //in the DB.  Set the origin just in case.
		  DislocParamsBean paramsBean=new DislocParamsBean();
		  paramsBean.setOriginLat(DislocParamsBean.DEFAULT_LAT);
		  paramsBean.setOriginLon(DislocParamsBean.DEFAULT_LON);
		  
		  db=Db4o.openFile(getBasePath()+"/"
								 +getContextBasePath()
								 +"/"+userName+"/"+codeName+"/"+projectName+".db");		  
		  ObjectSet results=db.get(DislocParamsBean.class);
		  System.out.println("Getting params from db:"+results.size());
		  if(results.hasNext()) {
				paramsBean=(DislocParamsBean)results.next();
		  }
		  System.out.println("Project Origin:"
									+paramsBean.getOriginLat()+" "
									+paramsBean.getOriginLon());
		  db.close();
		  return paramsBean;
    }
    
    protected void storeProjectInContext(String userName,
					 String projectName,
					 String jobUIDStamp,
					 DislocParamsBean paramsBean,
					 DislocResultsBean dislocResultsBean,
					 String kml_url) 
	throws Exception {

	DislocProjectSummaryBean summaryBean=new DislocProjectSummaryBean();
	summaryBean.setUserName(userName);
	summaryBean.setProjectName(projectName);
	summaryBean.setJobUIDStamp(jobUIDStamp);
	summaryBean.setParamsBean(paramsBean);
	summaryBean.setResultsBean(dislocResultsBean);
	summaryBean.setCreationDate(new Date().toString());
	summaryBean.setKmlurl(kml_url);
	
	//Store the summary bean.
	db=Db4o.openFile(getBasePath()+"/"
			 +getContextBasePath()+"/"+userName+"/"+codeName+".db");	
	db.set(summaryBean);
	
	//Say goodbye.
	db.commit();
	db.close();
	
	//Store the params bean for the current project, 
	//deleting any old one as necessary.
	db=Db4o.openFile(getBasePath()+"/"
			 +getContextBasePath()+"/"
			 +userName+"/"
			 +codeName+"/"+projectName+".db");	

	ObjectSet result=db.get(DislocParamsBean.class);
	if(result.hasNext()) {
	    DislocParamsBean tmp=(DislocParamsBean)result.next();
	    db.delete(tmp);
	}
	db.set(paramsBean);
	
	//Say goodbye.
	db.commit();
	db.close();
    }
    
    /**
     * This is a JSF compatible method for running Disloc
     * in blocking mode.  That is, it takes no argument and assumes
     * the values have been set by accessors.
     */ 
    public String runBlockingDislocJSF() 
	throws Exception {
	
	try { 
	    
	    Fault[] faults=getFaultsFromDB();
	    ObsvPoint[] points=getObsvPointsFromDB();
	    initDislocExtendedService();
	    DislocResultsBean dislocResultsBean=
		dislocExtendedService.runBlockingDislocExt(userName,
							   projectName,
							   points,
							   faults,
							   currentParams,
							   null);
	    setJobToken(dislocResultsBean.getJobUIDStamp());
	    
	    String myKmlUrl="";
	    myKmlUrl=createKml(currentParams, dislocResultsBean, faults);
	    setJobToken(dislocResultsBean.getJobUIDStamp());
	    storeProjectInContext(userName,
				  projectName,
				  dislocResultsBean.getJobUIDStamp(),
				  currentParams,
				  dislocResultsBean,
				  myKmlUrl);
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	}
	return DISLOC_NAV_STRING;		  
    }
    
	 /**
	  * This method is used to generate the output plots with the remote KML service.
	  */
    protected String createKml(DislocParamsBean dislocParams,
			       DislocResultsBean dislocResultsBean,
			       Fault[] faults) throws Exception {
	
		  System.out.println("Creating the KML file");
		  
		  //Get the project lat/lon origin.  It is the lat/lon origin of the first fault.
		  String origin_lat=dislocParams.getOriginLat()+"";
		  String origin_lon=dislocParams.getOriginLon()+"";
		  
		  System.out.println("Origin: "+origin_lon+" "+origin_lat);
		  
		  // get my  kml
		  SimpleXDataKml kmlService;
		  SimpleXDataKmlServiceLocator locator = new SimpleXDataKmlServiceLocator();
		  locator.setMaintainSession(true);
		  kmlService = locator.getKmlGenerator(new URL(kmlGeneratorUrl));
		  
		  PointEntry[] tmp_pointentrylist = LoadDataFromUrl(dislocResultsBean.getOutputFileUrl());
		  
		  kmlService.setDatalist(tmp_pointentrylist);
		  kmlService.setOriginalCoordinate(origin_lon, origin_lat);
		  kmlService.setCoordinateUnit("1000");

		  //These plot grid lines.
		  double start_x,start_y,end_x,end_y,xiterationsNumber,yiterationsNumber;
		  start_x=Double.valueOf(dislocParams.getGridMinXValue() ).doubleValue();
		  start_y=Double.valueOf(dislocParams.getGridMinYValue() ).doubleValue();
		  xiterationsNumber=Double.valueOf(dislocParams.getGridXIterations() ).doubleValue();
		  yiterationsNumber=Double.valueOf(dislocParams.getGridYIterations() ).doubleValue();
		  int xinterval= (int)(Double.valueOf(dislocParams.getGridXSpacing()).doubleValue() );
		  int yinterval=(int)(Double.valueOf(dislocParams.getGridYSpacing()).doubleValue() );
		  end_x=start_x+xinterval*(xiterationsNumber-1);
		  end_y=start_y+yinterval*(yiterationsNumber-1);
		  
		  //	kmlService.setGridLine("Grid Line", start_x, start_y, end_x, end_y, xinterval,yinterval);
		  //kmlService.setPointPlacemark("Icon Layer");
		  //kmlService.setArrowPlacemark("Arrow Layer", "ff66a1cc", 2);
		  kmlService.setArrowPlacemark("Arrow Layer","ff0000f",1);
		  
		  //Plot the faults
		  for (int i = 0; i < faults.length; i++) {
		      kmlService.setFaultPlot("", 
												faults[i].getFaultName()+"", 
												faults[i].getFaultLonStart()+"",
												faults[i].getFaultLatStart()+"", 
												faults[i].getFaultLonEnd()+"", 
												faults[i].getFaultLatEnd()+"", 
												"ff66a1cc", 5);
		  }
		  
		  String myKmlUrl = kmlService.runMakeKml("", userName,
																projectName, 
																(dislocResultsBean.getJobUIDStamp()).hashCode()+"");
		  return myKmlUrl;
	 }
    
    /**
     * This runs disloc in non-blocking mode, so it returns
     * immediately.  The client must determine separately if the mesh
     * generation has finished.
     */ 
    public String runNonBlockingDislocJSF() 
	throws Exception {
	
	try {
	    Fault[] faults=getFaultsFromDB();
	    ObsvPoint[] points=getObsvPointsFromDB();
	    
	    initDislocExtendedService();
	    DislocResultsBean dislocResultsBean=
		dislocExtendedService.runNonBlockingDislocExt(userName,
							   projectName,
							   points,
							   faults,
							   currentParams,
							   null);

	    String myKmlUrl="";
	    myKmlUrl=createKml(currentParams,dislocResultsBean,faults);
	    setJobToken(dislocResultsBean.getJobUIDStamp());
	    storeProjectInContext(userName,
				  projectName,
				  dislocResultsBean.getJobUIDStamp(),
				  currentParams,
				  dislocResultsBean,
				  myKmlUrl);
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	}
	return DISLOC_NAV_STRING;
    }
    
    /**
     * Another famous method that I googled. This downloads contents from the
     * given URL to a local file.
     */
    public PointEntry[] LoadDataFromUrl(String InputUrl) {
	System.out.println("Creating Point Entry");
	    ArrayList dataset = new ArrayList();
		 try {
			  String line = new String();
			  int skipthreelines = 1;
			  
			  URL inUrl= new URL(InputUrl);
			  URLConnection uconn = inUrl.openConnection();
			  InputStream instream = inUrl.openStream();
			  
			  BufferedReader in =
					new BufferedReader(new InputStreamReader(instream)); 
			  
			  //Need to make sure this will work with multiple faults.
			  Pattern p = Pattern.compile(" {1,20}");
			  while ((line = in.readLine()) != null) {
					String tmp[] = p.split(line);

					if(tmp[1].trim().equals("x") 
						&& tmp[2].trim().equals("y")) { 
						 System.out.println("Past the faults");
						 break;
					}
			  }
			  
			  while ((line = in.readLine()) != null) {
					if (!line.trim().equalsIgnoreCase("")) {
						 PointEntry tempPoint = new PointEntry();

						 String tmp[] = p.split(line);
						 
						 //Look for NaN or other problems.
						 for(int i=0;i<tmp.length;i++) {
							  String oldtmp=tmp[i];
							  if(tmp[i].trim().equalsIgnoreCase("nan")) {
									tmp[i]="0.0";
							  }
						 }
						 
						 tempPoint.setX(tmp[1].trim());
						 tempPoint.setY(tmp[2].trim());
						 tempPoint.setDeltaXName("dx");
						 tempPoint.setDeltaXValue(tmp[3].trim());
						 tempPoint.setDeltaYName("dy");
						 tempPoint.setDeltaYValue(tmp[4].trim());
						 tempPoint.setDeltaZName("dz");
						 tempPoint.setDeltaZValue(tmp[5].trim());
						 tempPoint.setFolderTag("point");
						 dataset.add(tempPoint);
					} 
					else {
						 break;
					}
			  }
			  in.close();
			  instream.close();
		 } catch (IOException ex1) {
			  ex1.printStackTrace();
		 }
		 return (PointEntry[]) (dataset.toArray(new PointEntry[dataset.size()]));
	}	
    
	 // End main execution method section.
	 //--------------------------------------------------


    public String getDBValue(Select select, 
			     String param, 
			     String theLayer)
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
		  
// 		  String sqlQuery = "select F." + param
// 				+ " from FAULT AS F, REFERENCE AS R where F.FaultName=\'" + theFault
// 				+ "\' and F.InterpId=R.InterpId;";

		  System.out.println("SQL Query is "+sqlQuery);
		  
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
    
    /**
     * Get the specific fault from the DB.  Fill in some parameters as necessary.
     * Determine if this is an origin fault or not.
     * 
     * The input value supports a backward-compatible @ token that can be ignored
     * these days.
     */
    public Fault QueryFaultFromDB(String faultAndSegment) {

	// Check request with fallback
	String theFault = faultAndSegment.substring(0, faultAndSegment.indexOf("@"));
	String theSegment=faultAndSegment.substring(faultAndSegment.indexOf("@") + 1, faultAndSegment.indexOf("%"));

	String interpId=faultAndSegment.substring(faultAndSegment.indexOf("%") + 1, faultAndSegment.length());

	//tmp_str = "";
	
	
	Fault tmp_fault = new Fault();
	
	try {
	    SelectService ss = new SelectServiceLocator();
	    Select select = ss.getSelect(new URL(faultDBServiceUrl));
	    
	    // --------------------------------------------------
	    // Make queries.
	    // --------------------------------------------------
	    double dip = Double.parseDouble(getDBValue(select, "Dip", theFault, theSegment,interpId));
	    double strike = Double.parseDouble(getDBValue(select, "Strike", theFault, theSegment,interpId));
	    double depth = Double.parseDouble(getDBValue(select, "Depth", theFault, theSegment,interpId));
	    double width = Double.parseDouble(getDBValue(select, "Width", theFault, theSegment,interpId));
	    
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
		      //				String length = format.format(Math.sqrt(x * x + y * y));
		      //				double length = Math.sqrt(x * x + y * y);

		      double length=Double.parseDouble(format.format(Math.sqrt(x * x + y * y)));
		      tmp_fault.setFaultName(theFault);
				tmp_fault.setFaultLatStart(latStart);
				tmp_fault.setFaultLonStart(lonStart);
				tmp_fault.setFaultLonEnd(lonEnd);
				tmp_fault.setFaultLatEnd(latEnd);
		      tmp_fault.setFaultLength(length);
		      tmp_fault.setFaultWidth(width);
		      tmp_fault.setFaultDepth(depth);
		      tmp_fault.setFaultDipAngle(dip);

				//This is the fault's strike angle
 				strike=Math.atan2(x,y)/d2r;
 		      tmp_fault.setFaultStrikeAngle(Double.parseDouble(format.format(strike)));				

				//This is the (x,y) of the fault relative to the project's origin
				//The project origin is the lower left lat/lon of the first fault.
				//If any of these conditions hold, we need to reset.
				System.out.println("Origin:"+currentParams.getOriginLat()+" "
										 +currentParams.getOriginLon());
				if(currentParams.getOriginLat()==DislocParamsBean.DEFAULT_LAT
					|| currentParams.getOriginLon()==DislocParamsBean.DEFAULT_LON ) {
					 currentParams.setOriginLat(latStart);
					 currentParams.setOriginLon(lonStart);
					 //Update the parameters
					 db=Db4o.openFile(getBasePath()+"/"
											+getContextBasePath()
											+"/"+userName
											+"/"+codeName+"/"+projectName+".db");	
					 ObjectSet result=db.get(DislocParamsBean.class);
					 if(result.hasNext()) {
						  DislocParamsBean tmp=(DislocParamsBean)result.next();
						  db.delete(tmp);
					 }
					 db.set(currentParams);
					 
					 //Say goodbye.
					 db.commit();
					 db.close();
					 
				}
				System.out.println("Updated Origin:"+currentParams.getOriginLat()+" "
										 +currentParams.getOriginLon());

				//The following should be done in any case.  
				//If the origin was just (re)set above,
				//we will get a harmless (0,0);
				double x1=(lonStart-currentParams.getOriginLon())
					 *factor(currentParams.getOriginLon(),
								currentParams.getOriginLat());
				double y1=(latStart-currentParams.getOriginLat())*111.32;
				System.out.println("Fault origin: "+x1+" "+y1);
				tmp_fault.setFaultLocationX(Double.parseDouble(format.format(x1)));
				tmp_fault.setFaultLocationY(Double.parseDouble(format.format(y1)));
// 				tmp_fault.setFaultLocationX(x1);
// 				tmp_fault.setFaultLocationY(y1);
				
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
				FaultDBEntry tmp_FaultDBEntry = (FaultDBEntry) getMyFaultDataTable()
					 .getRowData();
				SelectItem tmp_SelectItem = tmp_FaultDBEntry.getFaultName();
				currentFault.setFaultName(tmp_SelectItem.getValue().toString());
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
				FaultDBEntry tmp_FaultDBEntry = (FaultDBEntry) getMyFaultDataTable()
					 .getRowData();
				SelectItem tmp_SelectItem = tmp_FaultDBEntry.getFaultName();
				currentFault.setFaultName(tmp_SelectItem.getValue().toString());
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
		  renderDislocGridParamsForm = false;       
		  renderMap=false;
		  renderChooseObsvStyleForm=false;
    }

	 /**
	  * This is called when a project is seleted for loading.
	  */
    public String toggleSelectProject() throws Exception  {
		  System.out.println("Loading Project");
		  initEditFormsSelection();
		  //This is implemented as a selectmanycheckbox on the client side (LoadProject.jsp),
		  //hence the unusual for loop.
		  if (selectProjectsArray != null) {
				for (int i = 0; i < 1; i++) {
					 this.projectName = selectProjectsArray[0];
				}
		  }

			db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+".db");		  

			//First, get the project bean
			DislocProjectBean project=new DislocProjectBean();
			project.setProjectName(projectName);
			ObjectSet results=db.get(project);
			System.out.println("Got results:"+results.size());
			if(results.hasNext()) {
				currentProject=(DislocProjectBean)results.next();
			}
			//Say goodbye.
			db.close();

			//Reconstruct the fault and layer object collections from the context
			//			myFaultCollection=populateFaultCollection(projectName);
			myFaultEntryForProjectList=reconstructMyFaultEntryForProjectList(projectName);

			//Now look up the project params bean and set the project origin.
			currentParams=getDislocParamsFromDB(projectName);
			
			//Some final stuff.
			projectSelectionCode = "";
			faultSelectionCode = "";
			return "disloc-edit-project";
    }

	 /**
	  * This is called when a project is seleted for copying and loading.
	  */
    public String toggleCopyProject() throws Exception  {
		  System.out.println("Copying project");
		  initEditFormsSelection();
		  //Get the old project name from the checkboxes
		  String oldProjectName="";
		  if (copyProjectsArray != null) {
				for (int i = 0; i < 1; i++) {
					 oldProjectName = copyProjectsArray[0];
				}
		  }
		  System.out.println("Old project name: "+oldProjectName);

		  //Create an empty project
		  String newProjectName=this.getProjectName();
		  createNewProject(newProjectName);

		  //Now replace empty new project pieces with old stuff.
			db=Db4o.openFile(getBasePath()+"/"
								  +getContextBasePath()
								  +"/"+userName
								  +"/"+codeName+".db");		  

			//Get the old project bean.
			DislocProjectBean project=new DislocProjectBean();
			project.setProjectName(oldProjectName);
			ObjectSet results=db.get(project);
			System.out.println("Got results:"+results.size());
			if(results.hasNext()) {
				currentProject=(DislocProjectBean)results.next();
			}
			//Say goodbye.
			db.close();

			// //Reconstruct the fault and layer object collections from the context
			// myFaultEntryForProjectList=reconstructMyFaultEntryForProjectList(oldProjectName);
			// System.out.println("Old project size:"+myFaultEntryForProjectList.size());
			// System.out.println("Some faultentrystuff:"+((faultEntryForProject)myFaultEntryForProjectList.get(0)).getFaultName());

			//Copy the DB file for the old project to the new project.
			File oldFileDB=new File(getBasePath()+"/"
											+getContextBasePath()
											+"/"+userName+"/"+codeName+"/"+oldProjectName+".db");
			File newFileDB=new File(getBasePath()+"/"
											+getContextBasePath()

											+"/"+userName+"/"+codeName+"/"+newProjectName+".db");
			copyFile(oldFileDB,newFileDB);

			//Now look up the project params bean and set the project origin.
			currentParams=getDislocParamsFromDB(oldProjectName);
			System.out.println("Min Y:"+currentParams.getGridMinYValue());
			System.out.println("Min X:"+currentParams.getGridMinXValue());
			
			//Some final stuff.
			projectSelectionCode = "";
			faultSelectionCode = "";
			return "disloc-edit-project";
    }

    /**
     * Handle action events in the project selection area.
     */
    public void toggleProjectSelection(ActionEvent ev) {

		  initEditFormsSelection();
		  if (projectSelectionCode.equals("CreateObservationGrid")) {
				currentParams=getDislocParamsFromDB(projectName);
				renderDislocGridParamsForm = !renderDislocGridParamsForm;
		  }

		  else if (projectSelectionCode.equals("CreateNewFault")) {
				currentFault=new Fault();
				renderCreateNewFaultForm = !renderCreateNewFaultForm;
		  }
		  else if (projectSelectionCode.equals("AddFaultSelection")) {
				renderAddFaultSelectionForm = !renderAddFaultSelectionForm;
		  }
		  else if (projectSelectionCode.equals("ShowMap")) {
				renderMap = !renderMap;
		  }
		  else if (projectSelectionCode.equals("ChooseObsvStyleForm")) {
				renderChooseObsvStyleForm=!renderChooseObsvStyleForm;
		  }
		  else if (projectSelectionCode.equals("")) {
				;
		  }
    }

	 /**
	  * This is where we set the project's observation style.
	  */
	 public void toggleSetObsvStyle(ActionEvent ev) {
		  initEditFormsSelection();
		  if(obsvStyleSelectionCode.equals("GridStyle")) {
				System.out.println("GridStyle");
				usesGridPoints=true;
				currentParams.setObservationPointStyle(1);
		  }
		  else if(obsvStyleSelectionCode.equals("ScatterStyle")) {
				System.out.println("ScatterStyle");				
				usesGridPoints=false;
				currentParams.setObservationPointStyle(0);
		  }
		  else {
				System.out.println("Unexpected obsv style");
		  }
		  obsvStyleSelectionCode="";
		  try {
		      storeParamsInDB();
		  }
		  catch (Exception ex) {
		      ex.printStackTrace();
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
				 myFaultDBEntryList=ViewAllFaults(faultDBServiceUrl);
				renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
		  }
		  if (projectSelectionCode.equals("")) {
				;
		  }
		  faultSelectionCode = "";
    }
	 
    public void toggleUpdateProjectObservations(ActionEvent ev) {
		  System.out.println("Updating observation entry for project");
		  try {
				obsvEntryForProject tmp_ObsvEntryForProject = new obsvEntryForProject();				
				
				//Find out which one was selected
				for (int i = 0; i < myObsvEntryForProjectList.size(); i++) {
					 tmp_ObsvEntryForProject = (obsvEntryForProject) myObsvEntryForProjectList
						  .get(i);
					 if ((tmp_ObsvEntryForProject.getView() == true)
						  || (tmp_ObsvEntryForProject.getDelete() == true)) {
						  break;
					 }
				}
				
				
				boolean tmp_view = tmp_ObsvEntryForProject.getView();
				boolean tmp_update = tmp_ObsvEntryForProject.getDelete();
				
				initEditFormsSelection();
				if ((tmp_view == true) && (tmp_update == true)) {
					 System.out.println("error");
				}
				//This is the edit case.
				if ((tmp_view == true) && (tmp_update == false)) {
					 System.out.println("We are adding/editing the observations");
					 currentParams=populateParamsFromContext(projectName);
					 renderDislocGridParamsForm = !renderDislocGridParamsForm;
					 System.out.println("Rendering:"+ renderDislocGridParamsForm);
				}
				
				//This is the deletion case.
				if ((tmp_update == true) && (tmp_view == false)) {
					 System.out.println("We are deleteing the observations");
					 db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
					 
					 //There is only one of these
					 ObjectSet result1=db.get(DislocParamsBean.class);
					 if(result1.hasNext()) {
						  DislocParamsBean todelete=(DislocParamsBean)result1.next();
						  //Now that we have the specific object, we can delete it.
						  db.delete(todelete);
					 }
					 db.close();
				}
				
		  } catch (Exception e) {
				e.printStackTrace();
		  }
    }
    
    public void toggleUpdateFaultProjectEntry(ActionEvent ev) {
		  String faultStatus = "Update";
		  System.out.println("Updating fault entry for project");
		  try {
				
				//This is the info about the fault.
				faultEntryForProject tmp_FaultEntryForProject = new faultEntryForProject();			

				for (int i = 0; i < myFaultEntryForProjectList.size(); i++) {
					 tmp_FaultEntryForProject = (faultEntryForProject) myFaultEntryForProjectList
						  .get(i);
					 if ((tmp_FaultEntryForProject.getView() == true)
						  || (tmp_FaultEntryForProject.getDelete() == true)) {
						  break;
					 }
				}
				
	
				String tmp_faultName = tmp_FaultEntryForProject.getFaultName();
				boolean tmp_view = tmp_FaultEntryForProject.getView();
				boolean tmp_update = tmp_FaultEntryForProject.getDelete();

				System.out.println(tmp_view+" "+tmp_update);
				
				initEditFormsSelection();
				if ((tmp_view == true) && (tmp_update == true)) {
					 System.out.println("error");
				}
				
				//Update the fault.
				if ((tmp_view == true) && (tmp_update == false)) {
					 currentFault=populateFaultFromContext(tmp_faultName);
					 renderCreateNewFaultForm = !renderCreateNewFaultForm;
					 System.out.println("Rendering:"+ renderCreateNewFaultForm);
				}
				
				//This is the deletion case.
				if ((tmp_update == true) && (tmp_view == false)) {
					 
					 //Delete from the database.
					 //This requires we first search for the desired object
					 //and then delete the specific value that we get back.
					 System.out.println("Deleting "+tmp_faultName+"from db");
					 db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
				 
// 					 Fault todelete=new Fault();
// 					 todelete.setFaultName(tmp_faultName);
					 ObjectSet result=db.get(Fault.class);
					 Fault todelete;
					 while(result.hasNext()) {
						  todelete=(Fault)result.next();
						  if(todelete.getFaultName().equals(tmp_faultName)) {
						      db.delete(todelete);
						  }
					 }
					 db.close(); 
				}

				//Possibly update the project origin.  Need to do this if the original fault
				//use to set the problem origin is deleted.
				//				setProjectOrigin(projectName);
				
		  } catch (Exception e) {
				e.printStackTrace();
		  }
		  
    }

    /**
     * Handle fault db entry events.
     */
    public void toggleSelectFaultDBEntry(ActionEvent ev) {
		  initEditFormsSelection();
		  //		  currentFault.setFaultName(currentFault.getFaultName().trim());
		  if (!currentFault.getFaultName().equals("")) {
				currentFault = QueryFaultFromDB(currentFault.getFaultName().trim());
		  }
		  renderCreateNewFaultForm = !renderCreateNewFaultForm;
    }

    public void toggleFaultSearchByName(ActionEvent ev) {
		  initEditFormsSelection();
		  this.forSearchStr = this.forSearchStr.trim();
		  if (!this.forSearchStr.equals("")) {
		      myFaultDBEntryList=QueryFaultsByName(this.forSearchStr,faultDBServiceUrl);
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
	     myFaultDBEntryList=QueryFaultsByLonLat(this.faultLatStart, 
						    this.faultLatEnd,
						    this.faultLonStart, 
						    this.faultLonEnd,
						    faultDBServiceUrl);
	}
	renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
    }

    /**
     * This will delete projects
     */
    public void toggleDeleteProjectSummary(ActionEvent ev) {
		  System.out.println("Deleting Project");
	try {
	    DislocProjectSummaryBean dpsb=
		(DislocProjectSummaryBean)getMyProjectSummaryDataTable().getRowData();
	    db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+".db");		  
	    System.out.println("Found project:"+dpsb.getProjectName()+" "+dpsb.getJobUIDStamp());
	    ObjectSet results=db.get(dpsb);
	    System.out.println("Result size: "+results.size());
	    //Should only have one value.
	    if(results.hasNext()){
		dpsb=(DislocProjectSummaryBean)results.next();
		db.delete(dpsb);
	    }
	    db.close();
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	}
		  
    }

    /**
     * Stop showing the map.
     */
    public void toggleCloseMap(ActionEvent ev) {
	setRenderMap(false);	
    }

	 /**
	  * Used for selecting the data to plot
	  */
    public void togglePlotProject(ActionEvent ev) {
		  System.out.println("Plotting project");
		  try {
				//				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+".db");		  
				DislocProjectSummaryBean dpsb=
					 (DislocProjectSummaryBean)getMyProjectSummaryDataTable().getRowData();
				
				System.out.println("Found project:"+dpsb.getProjectName()+" "
										 +dpsb.getJobUIDStamp()
										 +dpsb.getKmlurl());
				String kmlName=
					 dpsb.getKmlurl().substring(dpsb.getKmlurl().lastIndexOf("/")+1,dpsb.getKmlurl().length());

				downloadKmlFile(dpsb.getKmlurl(),
									 this.getBasePath()+"/"+"gridsphere"+"/"+kmlName);

				System.out.println(kmlName);
				setKmlProjectFile(kmlName);
				
				//				ObjectSet results=db.get(dpsb);
// 				if(results.hasNext()) {
// 				//Reconstitute the bean from the db
// 				//Find the URL of the kml and download it.
// 				//Set the kml project file.
// 				}
// 				db.close();
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
    }

// 	 protected List populateFaultCollection(String projectName) throws Exception {
// 		  List myFaultCollection=new ArrayList();
// 		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
// 		  ObjectSet results=db.get(Fault.class);
// 		  //Should only have one value.
// 		  Fault currentFault=null;
// 		  while(results.hasNext()){
// 				currentFault=(Fault)results.next();
// 				myFaultCollection.add(currentFault);
// 		  }
// 		  db.close();
// 		  return myFaultCollection;
// 	 }

	 protected List populateParamsCollection(String projectName) throws Exception {
		  List myDislocParamsCollection=new ArrayList();
		  db=Db4o.openFile(getBasePath()+"/"
								 +getContextBasePath()+"/"
								 +userName+"/"
								 +codeName+"/"+projectName+".db");		  
		  ObjectSet results=db.get(DislocParamsBean.class);
		  //Should only have one value.
		  DislocParamsBean dislocParams=null;
		  //There should only be one of these
		  if(results.hasNext()){
				dislocParams=(DislocParamsBean)results.next();
				myDislocParamsCollection.add(dislocParams);
		  }
		  db.close();
		  return myDislocParamsCollection;
	 }
    
    public void toggleFaultSearchByAuthor(ActionEvent ev) {
		  initEditFormsSelection();
		  this.forSearchStr = this.forSearchStr.trim();
		  if (!this.forSearchStr.equals("")) {
		       myFaultDBEntryList=QueryFaultsByAuthor(this.forSearchStr,
					  faultDBServiceUrl);
		  }
		  this.forSearchStr = "";
		  renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
    }
        
    public void toggleAddFaultForProject(ActionEvent ev) throws Exception {
		  try {
				initEditFormsSelection();
				System.out.println("Setting current fault");
				db=Db4o.openFile(getBasePath()
									  +"/"+getContextBasePath()
									  +"/"+userName
									  +"/"+codeName
									  +"/"+projectName+".db");
				
				//Add the fault to the DB.
				// 		  Fault tmpfault=new Fault();
				// 		  tmpfault.setFaultName(currentFault.getFaultName());
				//		  ObjectSet result=db.get(tmpfault);
				ObjectSet result=db.get(Fault.class);
				Fault tmpfault;
				//Remove any previous versions
				while(result.hasNext()) {
					 tmpfault=(Fault)result.next();
					 if(tmpfault.getFaultName().equals(currentFault.getFaultName())) {
						  System.out.println("Deleting old fault: "
													+currentFault.getFaultName());
						  db.delete(tmpfault);
					 }
				}
				db.set(currentFault);
				db.commit();
				db.close();
				//		  setProjectOrigin(projectName);
		  }
		  catch (Exception ex){
				if(db!=null) db.close();
				ex.printStackTrace();
		  }
    }
    
    public void toggleAddObservationsForProject(ActionEvent ev) throws Exception {
		  try {
				initEditFormsSelection();
				db=Db4o.openFile(getBasePath()
									  +"/"+getContextBasePath()
									  +"/"+userName+"/"+codeName+"/"+projectName+".db");
				
				System.out.println("Adding an observation");
				
				//There should only be one of these at most.
				//Delete the stored one and replace it with the new one.
				ObjectSet result=db.get(DislocParamsBean.class);
				if(result.hasNext()) {
					 DislocParamsBean tmp=(DislocParamsBean)result.next();
					 db.delete(tmp);
				}
				System.out.println("Disloc params are " + currentParams.getGridXIterations());
				System.out.println("Disloc params are also " + currentParams.getGridYIterations());
				db.set(currentParams);
				
				NumberFormat format = NumberFormat.getInstance();
				ObjectSet faultResults=db.get(Fault.class);
				while(faultResults.hasNext()) {
					 Fault tmp_fault=(Fault)faultResults.next();
					 System.out.println("Updating fault origins for "+tmp_fault.getFaultName());
					 
					 double x1=(tmp_fault.getFaultLonStart()-currentParams.getOriginLon())
						  *factor(currentParams.getOriginLon(),currentParams.getOriginLat());
					 double y1=(tmp_fault.getFaultLatStart()-currentParams.getOriginLat())*111.32;
					 System.out.println("New fault origin: "+x1+" "+y1);
					 tmp_fault.setFaultLocationX(Double.parseDouble(format.format(x1)));
					 tmp_fault.setFaultLocationY(Double.parseDouble(format.format(y1)));
					 db.set(tmp_fault);
				}
				db.commit();
				db.close();
				
		  }
		  catch (Exception ex) {
				if(db!=null) db.close();
				ex.printStackTrace();
		  }
    }

    /**
     * These are methods associated with Faces navigations.
     */
    public String newProject() throws Exception {
		  initDislocService();
		  isInitialized = getIsInitialized();
		  // 		  if (!isInitialized) {
		  // 				initWebServices();
		  // 		  }
		  // 		  setContextList();
		  
		  makeProjectDirectory();
		  return ("disloc-new-project");
    }
    
    public void init_edit_project() {
		  initEditFormsSelection();
		  projectSelectionCode = "";
		  faultSelectionCode = "";
    }
    
    
    /**
     * Create the new project bean, store it in the db, and initialize.
     */ 
    public String NewProjectThenEditProject() throws Exception {
		  try {
				//		  dislocParams=new DislocParamsBean();
				createNewProject(projectName);
				init_edit_project();
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  return "disloc-edit-project";
    }
    
    public String toggleDeleteProject() {
		  System.out.println("Deleting a project");
		  try {
				db=Db4o.openFile(getBasePath()+"/"
									  +getContextBasePath()+"/"+userName+"/"+codeName+".db");
				if (deleteProjectsArray != null) {
					 for (int i = 0; i < deleteProjectsArray.length; i++) {
		    //Delete the project input data
						  System.out.println("Deleting project input junk");
						  DislocProjectBean delproj=new DislocProjectBean();
						  delproj.setProjectName(deleteProjectsArray[i]);
						  ObjectSet results=db.get(delproj);
						  if(results.hasNext()){
								delproj=(DislocProjectBean)results.next();
								db.delete(delproj);
						  }
						  //Delete the results summary bean also.
						  System.out.println("Deleting project summaries");
						  DislocProjectSummaryBean delprojsum=new DislocProjectSummaryBean();
						  delprojsum.setProjectName(deleteProjectsArray[i]);
						  ObjectSet results2=db.get(delprojsum);
						  while(results2.hasNext()){
								delprojsum=(DislocProjectSummaryBean)results2.next();
								db.delete(delprojsum);
						  }						  
					 }
				}
				db.close();
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
		  return "disloc-this";
    }

	 /**
	  * This method stores the new project in the DB and updates the currentProject
	  */
    protected void createNewProject(String projectName) {
		  System.out.println("Creating new project");
		  makeProjectDirectory();
		  db=Db4o.openFile(getBasePath()
								 +"/"+getContextBasePath()
								 +"/"+userName+"/"+codeName+".db");
		  DislocProjectBean tmp=new DislocProjectBean();
		  ObjectSet results=db.get(DislocProjectBean.class);
		  
		  //Create a new project. This may be overwritten later
		  currentProject=new DislocProjectBean();
		  currentProject.setProjectName(projectName);

		  //See if project already exists
		  while(results.hasNext()){
				tmp=(DislocProjectBean)results.next();
				//This is a screwed up project so delete it.
				if(tmp==null || tmp.getProjectName()==null) {
					 db.delete(tmp);
				}
				//The project already exists, so update it.
				else if (tmp.getProjectName().equals(projectName)) {
					 db.delete(tmp);
					 currentProject=tmp;
					 break;
				}
		  }
		  db.set(currentProject);
		  db.commit();
		  db.close();
		  
		  currentParams=getDislocParamsFromDB(projectName);
    }
    
    public String loadProjectList() throws Exception {
// 		  if (!isInitialized) {
// 				initWebServices();
// 		  }
// 		  setContextList();

		  makeProjectDirectory();

		  return ("disloc-list-project");
    }


    /**
     * Contains a list of project beans.
     */ 
    public List getMyProjectNameList() {
	this.myProjectNameList.clear();
	try {
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+".db");		  
				DislocProjectBean project=new DislocProjectBean();
				ObjectSet results=db.get(DislocProjectBean.class);
				//System.out.println("Got results:"+results.size());
				while(results.hasNext()) {
					 project=(DislocProjectBean)results.next();
					 myProjectNameList.add(new SelectItem(project.getProjectName(),
																	  project.getProjectName()));
				}
				db.close();
				
		  } catch (Exception ex) {
				//ex.printStackTrace();
				System.err.println("Could not open "+getBasePath()+"/"+getContextBasePath()
										 +"/"+userName+"/"+codeName+".db");		
				//				System.err.println("Returning empty list.");  
		  }
		  return this.myProjectNameList;
    }
    
    public String[] getDeleteProjectsArray() { return this.deleteProjectsArray; }
    public String[] getSelectProjectsArray() { return this.selectProjectsArray; }
    public String[] getCopyProjectsArray() { return this.copyProjectsArray; }
    
    public String getForSearchStr() {
		  return this.forSearchStr;
    }
    

	 public void setSelectProjectsArray(String[] selectProjectsArray) { 
		  this.selectProjectsArray=selectProjectsArray; 
	 }
	 public void setDeleteProjectsArray(String[] deleteProjectsArray) { 
		  this.deleteProjectsArray=deleteProjectsArray; 
	 }
	 public void setCopyProjectsArray(String[] copyProjectsArray) { 
		  this.copyProjectsArray=copyProjectsArray; 
	 }

	 public void setMyProjectNameList(List myProjectNameList) { 
		  this.myProjectNameList=myProjectNameList; 
	 }

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

    public HtmlDataTable getMyProjectSummaryDataTable() {
		  return myProjectSummaryDataTable;
    }
    public void setMyProjectSummaryDataTable(HtmlDataTable myProjectSummaryDataTable) {
		  this.myProjectSummaryDataTable=myProjectSummaryDataTable;
    }

	 public HtmlDataTable getMyScatterPointsTable() {
		  return this.myScatterPointsTable;
	 }

	 public void setMyScatterPointsTable(HtmlDataTable myScatterPointsTable) {
		  this.myScatterPointsTable=myScatterPointsTable;
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
 
	 public void setFaultDBServiceUrl(String faultDBServiceUrl){
		  this.faultDBServiceUrl=faultDBServiceUrl;
	 }
	 
	 public String getFaultDBServiceUrl() {
		  return faultDBServiceUrl;
	 }
	 
	 public void setProjectName(String projectName){
		  //Get rid of dubious characters
		  projectName=filterTheBadGuys(projectName);
		  
		  //Remove spaces and less dubious stuff.
		  projectName=URLDecoder.decode(projectName);
		  projectName=URLEncoder.encode(projectName);
		  this.projectName=projectName;
	 }
	 
	 public String getProjectName(){
		  return projectName;
	 }
	 
	 public String getProjectSelectionCode() {
		  return this.projectSelectionCode;
	 }

	 public void setProjectSelectionCode(String projectSelectionCode){
		  this.projectSelectionCode=projectSelectionCode;
	 }

	 public boolean getRenderChooseObsvStyleForm() {
		  return this.renderChooseObsvStyleForm;
	 }

	 public void setRenderChooseObsvStyleForm(boolean renderChooseObsvStyleForm){
		  this.renderChooseObsvStyleForm=renderChooseObsvStyleForm;
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

    public boolean getRenderDislocGridParamsForm() {
		  return renderDislocGridParamsForm;
    }
    
    public void setRenderDislocGridParamsForm(boolean tmp_boolean) {
		  this.renderDislocGridParamsForm = tmp_boolean;
    }

    public boolean getRenderMap() {
	return this.renderMap;
    }
    
    public void setRenderMap(boolean renderMap) {
		  this.renderMap=renderMap;
    }
    
	 public boolean getUsesGridPoints() {
		  if(currentParams.getObservationPointStyle()==1) {
				usesGridPoints=true;
		  }
		  else {
				usesGridPoints=false;
		  }
		  return usesGridPoints;
	 }

	 public void setUsesGridPoints(boolean usesGridPoints) {
		  this.usesGridPoints=usesGridPoints;
	 }

	 /**
	  * Makes an array list out of the ObsvPoints of the current pararms.
	  * Useful for the JSF datatable but not much else.
	  */ 
    protected List reconstructMyPointObservationList(String projectName) {
	this.myPointObservationList.clear();
	try {
	    ObsvPoint[] xypoints=getObsvPointsFromDB();
	    if(xypoints!=null && xypoints.length>0) {
		System.out.println(xypoints.length);
		for(int i=0;i<xypoints.length;i++) {
		    System.out.println(i+" "+xypoints[i].getXcartPoint()+" "+xypoints[i].getYcartPoint());
		    myPointObservationList.add(xypoints[i]);
		}
	    }
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	}
	return this.myPointObservationList;
    }
    
    /**
     * Reconstructs the fault entry list.
     */
    protected List reconstructMyFaultEntryForProjectList(String projectName) {
		  String projectFullName = codeName + SEPARATOR + projectName;
		  this.myFaultEntryForProjectList.clear();
		  try {
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");
				
				// 	    Fault tmpfault=new Fault();
				// 	    ObjectSet results=db.get(tmpfault);
				ObjectSet results=db.get(Fault.class);
				Fault tmpfault;
				while(results.hasNext()){
					 tmpfault=(Fault)results.next();
					 faultEntryForProject tmp_myFaultEntryForProject = new faultEntryForProject();
					 tmp_myFaultEntryForProject.setFaultName(tmpfault.getFaultName());
					 tmp_myFaultEntryForProject.view = false;
					 tmp_myFaultEntryForProject.delete = false;
					 this.myFaultEntryForProjectList
						  .add(tmp_myFaultEntryForProject);
				}
				db.close(); 
				
		  } catch (Exception ex) {
				ex.printStackTrace();
				if(db!=null) db.close();
		  }
		  
		  return this.myFaultEntryForProjectList;
    }
    
	 protected List reconstructMyObservationsForProjectList(String projectName) {
		  //		  List myObsvEntryForProjectList=new ArrayList();
		  this.myObsvEntryForProjectList.clear();
		  try {
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");
				

				//Unlike faults, there will only be one project bean.
				ObjectSet results=db.get(DislocProjectBean.class);
				while(results.hasNext()) {
					 Object tmp=results.next();
					 obsvEntryForProject tmp_myObsvEntryForProject = new obsvEntryForProject();
					 tmp_myObsvEntryForProject.view = false;
					 tmp_myObsvEntryForProject.delete = false;
					 this.myObsvEntryForProjectList.add(tmp_myObsvEntryForProject);
				}
				db.close(); 

		  } catch (Exception ex) {
				ex.printStackTrace();
				db.close();
		  }
		  
		  return this.myObsvEntryForProjectList;
    }

	 protected List reconstructMyObsvForProjectList(String projectName) {
		  //		  List myObsvEntryForProjectList=new ArrayList();
		  this.myObsvEntryForProjectList.clear();
		  try {
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");
				
				//Unlike faults, there will only be one project bean.
				ObjectSet results=db.get(DislocParamsBean.class);
				while(results.hasNext()) {
					 Object tmp=results.next();
					 obsvEntryForProject tmp_myObsvEntryForProject = new obsvEntryForProject();
					 tmp_myObsvEntryForProject.view = false;
					 tmp_myObsvEntryForProject.delete = false;
					 this.myObsvEntryForProjectList.add(tmp_myObsvEntryForProject);
				}
				db.close(); 

		  } catch (Exception ex) {
				ex.printStackTrace();
				db.close();
		  }
		  
		  return this.myObsvEntryForProjectList;
    }

    public List getMyObservationsForProjectList() throws Exception {
	String projectName=getProjectName();
	return reconstructMyObservationsForProjectList(projectName);
    }
    
    public void setMyObservationsForProjectList(List tmp_list) {
	this.myObservationsForProjectList = tmp_list;
    }

    public List getMyFaultEntryForProjectList() throws Exception {
		  String projectName=getProjectName();
		  return reconstructMyFaultEntryForProjectList(projectName);
    }
    
    public void setMyFaultEntryForProjectList(List tmp_list) {
		  this.myFaultEntryForProjectList = tmp_list;
    }

    public void setMyObsvEntryForProjectList(List tmp_list) {
		  this.myObsvEntryForProjectList = tmp_list;
    }

    public List getMyObsvEntryForProjectList() throws Exception {
		  String projectName=getProjectName();
		  return reconstructMyObsvForProjectList(projectName);
    }
    	 
	 public String getFaultSelectionCode() {
		  return faultSelectionCode;
	 }
	 
	 public void setFaultSelectionCode(String tmp_str) {
		  this.faultSelectionCode = tmp_str;
	 }

	 public String getObsvStyleSelectionCode() {
		  return obsvStyleSelectionCode;
	 }

	 public void setObsvStyleSelectionCode(String obsvStyleSelectionCode) {
		  this.obsvStyleSelectionCode=obsvStyleSelectionCode;
	 }

	 public Fault getCurrentFault() {
		  return currentFault;
	 }

	 public void setCurrentFault(Fault currentFault){
		  this.currentFault=currentFault;
	 }

// 	 public void setOrigin_lat(String tmp_str) {
// 		 this.origin_lat=tmp_str;
// 	 }
// 	 public String getOrigin_lat() {
// 		 return this.origin_lat;
// 	 }
// 	 public void setOrigin_lon(String tmp_str) {
// 		 this.origin_lon=tmp_str;
// 	 }
// 	 public String getOrigin_lon() {
// 		 return this.origin_lon;
// 	 }
	 
    protected Fault populateFaultFromContext(String tmp_faultName) throws Exception {
	String faultStatus="Update";
	
	db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
	// 		  Fault faultToGet=new Fault();
	// 		  faultToGet.setFaultName(tmp_faultName);
	// 		  ObjectSet results=db.get(faultToGet);
	
	ObjectSet results=db.get(Fault.class);
	Fault currentFault=null;
	while(results.hasNext()){
	    currentFault=(Fault)results.next();
	    if(currentFault.getFaultName().equals(tmp_faultName)) {
		break;
	    }
	}
	db.close();
	return currentFault;
    }
    
	 protected DislocParamsBean populateParamsFromContext(String projectName) throws Exception {
		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
		  System.out.println("Populating params from context.");
		  ObjectSet results=db.get(DislocParamsBean.class);
		  //Should only have one value.
		  DislocParamsBean params=null;
		  if(results.hasNext()){
				params=(DislocParamsBean)results.next();
		  }
		  db.close();
		  return params;		  
	 }

	 public void setMyPointObservationList(List myPointObservationList) {
		  this.myPointObservationList=myPointObservationList;
	 }

	 /**
	  * This should read from the db.
	  */
	 public List getMyPointObservationList() {
		  return reconstructMyPointObservationList(projectName);
	 }

	 public void setMyArchivedDislocResultsList(List myArchivedDislocResultsList)  {
		 this.myArchivedDislocResultsList=myArchivedDislocResultsList;
	 }	 

	 public List getMyArchivedDislocResultsList() {
		  myArchivedDislocResultsList.clear();
		  List tmpList=new ArrayList();
		  try {
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+".db");		  
				ObjectSet results=db.get(new DislocProjectSummaryBean());
				while(results.hasNext()) {
					 DislocProjectSummaryBean dpsb=(DislocProjectSummaryBean)results.next();
					 //					 myArchivedDislocResultsList.add(dpsb);
					 tmpList.add(dpsb);
				}
				db.close();
				myArchivedDislocResultsList=sortByDate(tmpList);
		  }
		  catch (Exception ex){
				ex.printStackTrace();
		  }
		  return myArchivedDislocResultsList;
	 }
	 
		public void setKmlGeneratorUrl(String tmp_str) {
			this.kmlGeneratorUrl=tmp_str;
		}
		
		public String getKmlGeneratorUrl() {
			return this.kmlGeneratorUrl;
		}
		

	 public void setDislocExtendedServiceUrl(String dislocExtendedServiceUrl){
		  this.dislocExtendedServiceUrl=dislocExtendedServiceUrl;
	 }

	 public String getDislocExtendedServiceUrl() {
		  return dislocExtendedServiceUrl;
	 }

	 public void setDislocServiceUrl(String dislocServiceUrl){
		  this.dislocServiceUrl=dislocServiceUrl;
	 }

	 public String getDislocServiceUrl() {
		  return dislocServiceUrl;
	 }

	 public void setKmlProjectFile(String kmlProjectFile){
		  this.kmlProjectFile=kmlProjectFile;
	 }
	 
	 public String getKmlProjectFile(){
		  return this.kmlProjectFile;
	 }
	 
	 public String getCodeName(){
		  return codeName;
	 }
	 public void setCodeName(String codeName){
		  this.codeName=codeName;
	 }
	 
	 protected void downloadKmlFile(String kmlUrlString, String localDestination) {
		  try {
				URL kmlUrl=new URL(kmlUrlString);
				URLConnection uconn=kmlUrl.openConnection();
				InputStream in=kmlUrl.openStream();
				OutputStream out=new FileOutputStream(localDestination);
				
				//Extract the name of the file from the url.
				
				byte[] buf=new byte[1024];
				int length;
				while((length=in.read(buf))>0) {
					 out.write(buf,0,length);
				}
				in.close();
				out.close();
		  }
		  catch(Exception ex) {
				System.out.println("Unable to download kml file");
				ex.printStackTrace();
		  }
	 }

	 /**
	  * Some of the stuff below should be abstracted to the generic project classes.
	  */

	 /**
	  * Sort the list by date
	  */
	 protected List sortByDate(List fullList) {
		  if(fullList==null) return null;
		  int size=fullList.size();
		  if(size<2) {
				return fullList;
		  }
		  //Ordered list is originally empty and reducedlist is full.
		  List orderedList=new ArrayList();
		  List reducedList=new ArrayList();
		  myListToVectorCopy(reducedList,fullList);
		  
		  orderedList=setListOrder(orderedList, reducedList, fullList);

		  return orderedList;
	 }

	 protected void myListToVectorCopy(List dest, List src) {
		  for(int i=0;i<src.size();i++) {
				dest.add(new Integer(i));
		  }
	 }
	 
	 protected List setListOrder(List orderedList, List reducedList, List fullList) {
		  
		  if(reducedList==null) return null;
		  int size=reducedList.size();
		  if(size<2) {
				return fullList;
		  }
		  while(reducedList!=null && reducedList.size()>0) {
				int first=getFirst(reducedList, fullList);
				orderedList.add((DislocProjectSummaryBean)fullList.get(((Integer)reducedList.get(first)).intValue()));
				reducedList.remove(first);
		  }
		  return orderedList;
	 }
	 
	 protected int getFirst(List reducedList, List fullList) {
		  int first=0;
		  for(int i=1;i<reducedList.size();i++) {
				DislocProjectSummaryBean mb1=(DislocProjectSummaryBean)fullList.get(((Integer)reducedList.get(first)).intValue());
				DislocProjectSummaryBean mb2=(DislocProjectSummaryBean)fullList.get(((Integer)reducedList.get(i)).intValue());
				if(mb1.getCreationDate()==null||mb1.getCreationDate().equals("")) { 
					 mb1.setCreationDate((new Date()).toString());
				}
				if(mb2.getCreationDate()==null||mb2.getCreationDate().equals("")) { 
					 mb2.setCreationDate((new Date()).toString());
				}
				Date date1=new Date(mb1.getCreationDate());
				Date date2=new Date(mb2.getCreationDate());			  
				if(date2.after(date1)) first=i;
		  }

		  return first;
	 }

	 public void setCurrentSummary(DislocProjectSummaryBean currentSummary) {
		  this.currentSummary=currentSummary;
	 }

	 public DislocProjectSummaryBean getCurrentSummary() {
		  return this.currentSummary;
	 }
	 
	 public void setCurrentProject(DislocProjectBean currentProject) {
		  this.currentProject=currentProject;
	 }
	 
	 public DislocProjectBean getCurrentProject() {
		  return currentProject;
	 }

	 public void setCurrentParams(DislocParamsBean currentParams) {
		  this.currentParams=currentParams;
	 }
	 
	 public DislocParamsBean getCurrentParams() {
		  return currentParams;
	 }
	 
	 public String getObsvKmlFilename() {
		  return obsvKmlFilename;
	 }
	 
	 public void setObsvKmlFilename(String obsvKmlFilename) {
		  this.obsvKmlFilename=obsvKmlFilename;
	 }
	 
	 public String getObsvKmlUrl() {
		  obsvKmlUrl=createObsvKmlFile();
		  return obsvKmlUrl;
	 }
	 
	 public void setObsvKmlUrl(String obsvKmlUrl) {
		  this.obsvKmlUrl=obsvKmlUrl;
	 }

    /**
     * Create a KML file of the point observations.  The method assumes
     * access to global variables.  This method is used to plot INPUT values for disloc.
     */
    public String createObsvKmlFile() { 
		  String newObsvFilename="";
		  String oldLocalDestination=this.getBasePath()+"/"+"gridsphere"+"/"
				+getObsvKmlFilename();
		  String localDestination="";

		  try {
				//Delete the old file.
				System.out.println("Old fault kml file:"+localDestination);
				File oldFile=new File(oldLocalDestination);
				if (oldFile.exists()) {
					 System.out.println("Deleting old fault kml file");
					 oldFile.delete();
				}
				
				long timeStamp=(new Date()).getTime();
				newObsvFilename=userName+"-"+codeName+"-"+projectName+"-"+timeStamp+".kml";
				setObsvKmlFilename(newObsvFilename);
				//This should be the new file name.
				localDestination=this.getBasePath()+"/"+"gridsphere"+"/"
					 +getObsvKmlFilename();

				PrintWriter out=new PrintWriter(new FileWriter(localDestination));
				
				if(currentParams.getObservationPointStyle()==1) {
					 DislocParamsBean dpb=getDislocParamsFromDB(projectName);
					 //Get data points
					 int xint=dpb.getGridXIterations();
					 int yint=dpb.getGridYIterations();
					 double xspacing=dpb.getGridXSpacing();
					 double yspacing=dpb.getGridYSpacing();
					 double xmin=dpb.getGridMinXValue();
					 double ymin=dpb.getGridMinYValue();
					 double originLat=dpb.getOriginLat();
					 double originLon=dpb.getOriginLon();

					 //Make the KML
					 out.println(xmlHead); 														 
					 out.println(kmlHead);
					 out.println(docBegin);
				
					 double cartX, cartY;
					 double lat, lon;

					 for(int i=0;i<xint;i++) {
						  cartX=xmin+i*xspacing;
						  for(int j=0;j<yint;j++) {
								cartY=ymin+j*yspacing;
								lon=cartX/factor(originLon, originLat)+originLon;
								lat=cartY/111.32+originLat;
								writeKmlPoint(lat, lon, out);
						  }
					 }
					 out.println(docEnd);
					 out.println(kmlEnd);
					 out.flush();
					 out.close();
				}

				//Default to scatter style
				else {
				    ObsvPoint[] points=getObsvPointsFromDB();
					 out.println(xmlHead); 														 
					 out.println(kmlHead);
				    
				    if(points!=null && points.length>0) {
						  out.println(docBegin);
						  for(int i=0;i<points.length;i++) {
								writeKmlPoint(Double.parseDouble(points[i].getLatPoint()), 
												  Double.parseDouble(points[i].getLonPoint()), 
												  out);
						  }
						  out.println(docEnd);
						  out.println(kmlEnd);
						  out.flush();
						  out.close();
					 }
				}
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
		  String returnString=portalBaseUrl+"/gridsphere/"
				+getObsvKmlFilename();
		  System.out.println("KML:"+returnString);
		  return returnString;
	 }
		  
	 protected void writeKmlPoint(double lat, double lon, PrintWriter out) {
		  out.println(pmBegin);
		  out.println(descBegin);
		  out.println("<b>Observation Point:</b> "
						  +lon+comma+lat);
		  out.println(descEnd);
		  out.println(pointBegin);
		  out.println(coordBegin);
		  out.println(lon+comma+lat+comma+"0");
		  out.println(coordEnd);
		  out.println(pointEnd);
		  out.println(pmEnd);
	 }
		 

	 public String getFaultKmlFilename() {
		  return faultKmlFilename;
	 }
	 
	 public void setFaultKmlFilename(String faultKmlFilename) {
		  this.faultKmlFilename=faultKmlFilename;
	 }


    /**
     * Create a KML file of the faults.  The method assumes
     * access to global variables.
     */
    public String createFaultKmlFile() { 
		  String newFaultFilename="";
		  String oldLocalDestination=this.getBasePath()+"/"+"gridsphere"+"/"
				+getFaultKmlFilename();
		  String localDestination="";


		  try {
				//Delete the old file.
				System.out.println("Old fault kml file:"+localDestination);
				File oldFile=new File(oldLocalDestination);
				if (oldFile.exists()) {
					 System.out.println("Deleting old fault kml file");
					 oldFile.delete();
				}
				
				long timeStamp=(new Date()).getTime();
				newFaultFilename=userName+"-"+codeName+"-"+projectName+"-"+timeStamp+".kml";
				setFaultKmlFilename(newFaultFilename);
				//This should be the new file name.
				localDestination=this.getBasePath()+"/"+"gridsphere"+"/"
					 +getFaultKmlFilename();


				Fault[] faults=getProjectFaultsFromDB(userName,
																  projectName,
																  codeName,
																  getBasePath(),
																  getContextBasePath());
				PrintWriter out=new PrintWriter(new FileWriter(localDestination));
								
				if(faults!=null && faults.length>0) {
					 out.println(xmlHead); 														 
					 out.println(kmlHead);
					 out.println(docBegin);
					 for(int i=0;i<faults.length;i++) {
						  out.println(pmBegin);
						  out.println(descBegin);
						  out.println("<b>Fault: </b>"+faults[i].getFaultName());
						  out.println(descEnd);
						  out.println(lsBegin);
						  out.println(coordBegin);
						  out.println(faults[i].getFaultLonStart()+comma+faults[i].getFaultLatStart()+comma+"0");
						  out.println(faults[i].getFaultLonEnd()+comma+faults[i].getFaultLatEnd()+comma+"0");
						  out.println(coordEnd);
						  out.println(lsEnd);
						  out.println(pmEnd);
					 }
					 out.println(docEnd);
					 out.println(kmlEnd);
					 out.flush();
					 out.close();
				}
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
		  String returnString=portalBaseUrl+"/gridsphere/"
				+getFaultKmlFilename();
		  System.out.println("KML:"+returnString);
		  return returnString;
	 }

    public String getFaultKmlUrl(){
		  faultKmlUrl=createFaultKmlFile();
		  return faultKmlUrl;
    }

    public void setFaultKmlUrl(String faultKmlUrl) {
		  this.faultKmlUrl=faultKmlUrl;
    }
	 public String getPortalBaseUrl() {
		  return portalBaseUrl;
	 }
	 
	 public void setPortalBaseUrl(String portalBaseUrl) {
		  this.portalBaseUrl=portalBaseUrl;
	 }

	 public Fault[] getProjectFaultsFromDB(String userName,
														String projectName,
														String codeName,
														String basePath,
														String relPath) {
		  Fault[] returnFaults = null;
		  System.out.println("Opening Fault DB:"+basePath+"/"+relPath+ "/" + userName + "/"	+ codeName + "/" + projectName + ".db");

		  db = Db4o.openFile(basePath+"/"+relPath+ "/" + userName + "/"
									+ codeName + "/" + projectName + ".db");
		  Fault faultToGet = new Fault();
		  ObjectSet results = db.get(faultToGet);
		  if (results.hasNext()) {
				returnFaults = new Fault[results.size()];
				for (int i = 0; i < results.size(); i++) {
					 returnFaults[i] = (Fault) results.next();
				}
		  }
		  db.close();
		  return returnFaults;
	 }


	 public String getGpsStationName() {
		  return gpsStationName;
	 }

	 public void setGpsStationName(String gpsStationName) {
		  this.gpsStationName=gpsStationName;
	 }
	 
	 public String getGpsStationLat() {
	     return gpsStationLat;
	 }

	 public void setGpsStationLat(String gpsStationLat) {
	     this.gpsStationLat=gpsStationLat;
	 }
	 
	 public String getGpsStationLon() {
	     return gpsStationLon;
	 }

	 public void setGpsStationLon(String gpsStationLon) {
	     this.gpsStationLon=gpsStationLon;
	 }

	 public void toggleAddPointObsvForProject(ActionEvent ev) {
		  String space=" ";
		  System.out.println("Here are the choices:"
									+space+gpsStationLat
									+space+gpsStationLon);
		  
		  //Project origin is not set, so set it.
		  if(currentParams.getOriginLat()==DislocParamsBean.DEFAULT_LAT 
			  && currentParams.getOriginLon()==DislocParamsBean.DEFAULT_LON) {
				currentParams.setOriginLat(Double.parseDouble(gpsStationLat));
				currentParams.setOriginLon(Double.parseDouble(gpsStationLon));
		  }
		  double dLat=Double.parseDouble(gpsStationLat);
		  double dLon=Double.parseDouble(gpsStationLon);
		  ObsvPoint point=convertLatLon(Double.parseDouble(format.format(dLat)),
						Double.parseDouble(format.format(dLon)),
						currentParams.getOriginLat(),
						currentParams.getOriginLon());
		  
		  //Update the current project
		  ObsvPoint[] allpoints=getObsvPointsFromDB();
		  ObsvPoint[] newpoints;

		  if(allpoints==null || allpoints.length<0) {
				newpoints=new ObsvPoint[1];
				newpoints[0]=point;
		  }
		  else {
				newpoints=new ObsvPoint[allpoints.length+1];
				System.arraycopy(allpoints,0,newpoints,0,allpoints.length);
				newpoints[allpoints.length]=point;
		  }

		  //Update the DB.
		  try {
		      storeObsvPointsInDB(newpoints);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
	 }

    protected void storeParamsInDB() throws Exception {
	try {
	    db=Db4o.openFile(getBasePath()+"/"
			     +getContextBasePath()
			     +"/"+userName
			     +"/"+codeName+"/"+projectName+".db");	
	    ObjectSet result=db.get(DislocParamsBean.class);
	    if(result.hasNext()) {
		DislocParamsBean tmp=(DislocParamsBean)result.next();
		db.delete(tmp);
	    }
	    db.set(currentParams);
	    
	    //Say goodbye.
	    db.commit();
	    db.close();
	}
	catch (Exception ex) {
	    db.close();
	    throw new Exception();
	}
	db.close();
    }

    protected void storeObsvPointsInDB(ObsvPoint[] points) throws Exception {
	try {
	    db=Db4o.openFile(getBasePath()+"/"
			     +getContextBasePath()
			     +"/"+userName
			     +"/"+codeName+"/"+projectName+".db");	
	    ObjectSet result=db.get(ObsvPoint.class);
	    while(result.hasNext()) {
		ObsvPoint tmp=(ObsvPoint)result.next();
		db.delete(tmp);
	    }
	    for(int i=0;i<points.length;i++) {
		db.set(points[i]);
	    }
	    
	    //Say goodbye.
	    db.commit();
	    db.close();
	}
	catch (Exception ex) {
	    db.close();
	    throw new Exception();
	}
	db.close();
    }
    
	 /**
	  * Converts the provided lat and lon into cartesian coordinates.
	  */
    protected ObsvPoint convertLatLon(double lat, 
				      double lon,
				      double origin_lat,
				      double origin_lon) {
	double x=(lon-origin_lon)*factor(origin_lon,origin_lat);
	double y=(lat-origin_lat)*111.32;
	
	x=Double.parseDouble(format.format(x));
	y=Double.parseDouble(format.format(y));
	
	System.out.println("ObsvPoints:"+lat+" "+lon+" "+x+" "+y);
	
	ObsvPoint point=new ObsvPoint();
	point.setXcartPoint(x+"");
	point.setYcartPoint(y+"");
	point.setLatPoint(lat+"");
	point.setLonPoint(lon+"");
	
	return point;
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

	 public void deleteScatterPoint(ActionEvent ev) throws Exception {
		  System.out.println("Deleting point");
		  List scatterPointList=reconstructMyPointObservationList(projectName);
		  
		  //Delete the entry from the list.
		  ObsvPoint deadPoint=(ObsvPoint)getMyScatterPointsTable().getRowData();
		  int rowValue=getMyScatterPointsTable().getRowIndex();
		  System.out.println("Selected row value:"+rowValue);
		  scatterPointList.remove(rowValue);

		  //Remove the old db
		  db=Db4o.openFile(getBasePath()
								 +"/"+getContextBasePath()
								 +"/"+userName
								 +"/"+codeName
								 +"/"+projectName
								 +".db");		  
		  System.out.println(getBasePath()
								 +"/"+getContextBasePath()
								 +"/"+userName
								 +"/"+codeName
								 +"/"+projectName
								 +".db");		  
		  ObjectSet results=db.get(ObsvPoint.class);
		  while(results.hasNext()) {
				db.delete(results.next());
		  }
		  db.commit();
		  db.close();
		  
		  //Convert array list to standard array.
		  ObsvPoint[] newPoints=new ObsvPoint[scatterPointList.size()];
		  for(int i=0;i<scatterPointList.size();i++) {
				newPoints[i]=(ObsvPoint)scatterPointList.get(i);
		  }
		  //Store the new db.
		  storeObsvPointsInDB(newPoints);
	 }
}
