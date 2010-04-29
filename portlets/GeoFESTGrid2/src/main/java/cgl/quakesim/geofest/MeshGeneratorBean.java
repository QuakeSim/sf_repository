package cgl.quakesim.geofest;

//Imports from the mother ship
import java.io.*;

import java.net.*;
import java.util.*;
import java.text.*;
import java.text.DecimalFormat;

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
import org.servogrid.genericproject.GenericProjectBean;
import org.servogrid.genericproject.FaultDBEntry;

import sun.misc.BASE64Encoder;

//These are web service clients for various services.
import TestClient.Select.Select;
import TestClient.Select.SelectService;
import TestClient.Select.SelectServiceLocator;
import cgl.webclients.AntVisco;
import cgl.webclients.AntViscoServiceLocator;
import WebFlowClient.cm.*;

//Import the project utility class
import org.servogrid.genericproject.Utility;

//Import stuff from db4o
import com.db4o.*;

/**
 * Everything you need to set up and run MeshGenerator.
 */

public class MeshGeneratorBean extends GenericSopacBean {
    
    // MeshGenerator Bean stuff
    
    static final String MESH_GENERATION_NAV_STRING="mesh_generation_running";
    static final String GEOFEST_EXECUTION_LAUNCHED="geofest_execution_launched";
    static final String DEFAULT_USER_NAME="geofest_default_user";
    static final String GO_TO_GEOTRANSPARAMS="MG-geotransparams";
    
    /**
     * The following are property fields.  Associated get/set methods
     * are at the end of the code listing.
     */ 
    boolean renderDBLayerList = false;
    boolean renderCreateNewLayerForm = false;    
    boolean renderCreateNewFaultForm = false;    
    boolean renderAddLayerFromDBForm = false;    
    boolean renderAddFaultSelectionForm = false;    
    String projectSelectionCode = "";    
    boolean renderSearchByFaultNameForm = false;    
    boolean renderSearchByAuthorForm = false;    
    boolean renderSearchByLatLonForm = false;
    boolean renderViewAllFaultsForm = false;    
    String faultSelectionCode = "";    
    boolean renderAddFaultFromDBForm = false;
    boolean renderFaultMap = false;

    long EditProjectTableColumns = 1;    
    Layer currentLayer = new Layer();    
    Fault currentFault = new Fault();
    // GeotransParamsData currentGeotransParamsData = new GeotransParamsData();    
    GeotransParamsBean currentGeotransParamsBean = new GeotransParamsBean();

    // Need to go through and simplify these.
    List myFaultDBEntryList = new ArrayList();    
    List myLayerDBEntryList = new ArrayList();    
    List myFaultEntryForProjectList = new ArrayList();    
    List myLayerEntryForProjectList = new ArrayList();    
    List myFaultsForProject = new ArrayList();    
    List myProjectNameList = new ArrayList();    
    List myLoadMeshTableEntryList = new ArrayList();    
    List myarchivedMeshTableEntryList = new ArrayList();
    List myArchivedMeshRunList=new ArrayList();
    List myArchivedMeshRunList2=new ArrayList();
    List meshDataMegaList=new ArrayList();

    // This is used to store the grid host names.
    // List gridHostList=new ArrayList();
    SelectItem[] gridHostList;
    // These are used to store the actual layers and faults
    List myFaultCollection=new ArrayList();
    List myLayerCollection=new ArrayList();
    List meshRunArrayList=new ArrayList();
    List gfoutputArrayList=new ArrayList();
    GFOutputBean projectGeoFestOutput;
    MeshRunBean projectMeshRunBean;

    String[] myProjectCreationDateArray;    
    String[] myProjectMeshHostArray;    
    String[] myProjectNameArray;    
    String[] faultarrayForMesh;    
    String[] deleteProjectsList;    
    String[] selectProjectsList;    
    private HtmlDataTable myLayerDataTable;
    private HtmlDataTable myFaultDataTable;
    private HtmlDataTable archivedMeshTable;

    UIData myMeshDataTable, myMeshDataTable2;
    String forSearchStr = new String();    
    String faultLatStart = new String();    
    String faultLatEnd = new String();    
    String faultLonStart = new String();    
    String faultLonEnd = new String();    
    GeoFESTUtils gfutils = new GeoFESTUtils();
    String projectDir = new String();

    MeshViewer myMeshViewer; 

    String mesh_gen_viz_fileServiceUrl =  "http://gf2.ucs.indiana.edu:6060/jetspeed/services/FileService";
    String mesh_gen_viz_base_dir = new String("/home/gateway/yan_offscreen/offscreen/");
    String myLayersParamForJnlp = new String("");
    String myFaultsParamForJnlp = new String("");
    String workDirForJnlp = new String("");
    String projectNameForJnlp = new String("");
    String fsURLForJnlp = new String("");
    String jobToken="";
    String userName="";
    String projectName="";
    String meshResolution="rare";

    // These should be populated from faces-config.xml
    String meshViewerServerUrl="http://gf2.ucs.indiana.edu:18084";
    String faultDBServiceUrl;
    // String geoFESTBaseUrl="http://gf19.ucs.indiana.edu:8080/geofestexec/";
    String geoFESTBaseUrl;//="http://156.56.104.143:8080/geofestexec/";
    String geoFESTServiceUrl="";
    String geoFESTGridServiceUrl="";
    String geoFESTBaseUrlForJnlp="";
    String queueServiceUrl="";
    String gridInfoServiceUrl="";
    String gridRefinerHost="";
    String gridGeoFESTHost="";

    // This is our geofest service stub.
    GeoFESTService geofestService;
    GeoFESTGridService geofestGridService;
    QueueService queueService;
    GridInfoService_PortType gridInfoService;

    // This is the db4o database
    ObjectContainer db=null;
    static final String DB_FILE_NAME="meshgen.db";

    // Useful for manipulating fetchmesh data tables.
    UIData jnlpTable;
    List jnlpList;

    // --------------------------------------------------
    // Set some variables. Need to put in properties.
    // --------------------------------------------------
    String FAULTS = "Faults";
    String LAYERS = "Layers";
    String SEPARATOR = "/";
    boolean statusGeoFEST = false;
    boolean ListGeoFESTData = false;
    String contourPlotPdfUrl = "";
    String selectedProject="";
    String plotTarget="";
    // --------------------------------------------------

    // Project origin placeholder, until I do a better job
    double origin_lon=0.0;
    double origin_lat=0.0;
    DecimalFormat df;
    String kmlfiles = "";

    /**
     * The client constructor.
     */
    public MeshGeneratorBean() throws Exception {
		  super();

		  gfutils.initLayerInteger();
		  cm = getContextManagerImp();
		  myMeshViewer = new MeshViewer(meshViewerServerUrl);

		  df = new DecimalFormat(".###");

		  //We are done.
		  System.out.println("MeshGenerator Bean Created");
    }
	 
    //--------------------------------------------------
    // This section contains the main execution calls.
    //--------------------------------------------------

    /**
     * Protected convenience method. 
     */ 
	 
	 protected void initGeofestService() throws Exception {
		  geofestService=
				new GeoFESTServiceServiceLocator().getGeoFESTExec(new URL(geoFESTServiceUrl));
	 }

	 protected void initGeofestGridService() throws Exception {
		  geofestGridService=
				new GeoFESTGridServiceServiceLocator().getGeoFESTGridExec(new URL(geoFESTGridServiceUrl));
	 }

	 protected void makeProjectDirectory() {
		  File projectDir=new File(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/");
		  projectDir.mkdirs();
	 }
	 
	 protected Fault[] getFaultsFromDB(){
		  Fault[] returnFaults=null;
		  
		  try {
			  
			  if (db!=null)
				  db.close();
	 		  
			  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");		  
			  Fault faultToGet=new Fault();
			  ObjectSet results=db.get(faultToGet);
			  if(results.hasNext()) {
					returnFaults=new Fault[results.size()];
					for(int i=0;i<results.size();i++){
						 returnFaults[i]=(Fault)results.next();
					}
			  }
			  if (db!=null)
				  db.close();		  
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[getFaultsFromDB] " + e);
		  }
		  
		  
		  return returnFaults;
	 }

	 protected Layer[] getLayersFromDB(){
		  Layer[] returnLayers=null;
		  
		  try {
			  if (db!=null)
				  db.close();
			  
	 		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");		  
			  Layer layerToGet=new Layer();
			  ObjectSet results=db.get(layerToGet);
			  if(results.hasNext()) {
					returnLayers=new Layer[results.size()];
					for(int i=0;i<results.size();i++){
						 returnLayers[i]=(Layer)results.next();
					}
			  }
			  if (db!=null)
				  db.close();
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[getLayersFromDB] " + e);
		  }

		  return returnLayers;
	 }
	 
    protected Fault[] convertArrayListToFaultArray(List faultList)
		  throws Exception {
		  if(faultList==null || faultList.size()<0){
				throw new Exception();
		  }
		  
		  Fault[] faults=new Fault[faultList.size()];
		  for(int i=0;i<faultList.size();i++) {
				faults[i]=(Fault)faultList.get(i);
		  }
		  return faults;
    }
	 
    protected Layer[] convertArrayListToLayerArray(List layerList)
		  throws Exception {
		  if(layerList==null || layerList.size()<0){
				throw new Exception();
		  }
		  
		  Layer[] layers=new Layer[layerList.size()];
		  for(int i=0;i<layerList.size();i++) {
				layers[i]=(Layer)layerList.get(i);
		  }
		  return layers;
    }
	 
	 protected MeshDataMegaBean storeMeshRunInContext(String userName,
																	  String projectName,
																	  String jobStamp,
																	  MeshRunBean mrb) throws Exception {
		  
		  System.out.println("Storing meshrun");
		  System.out.println(mrb.getProjectName());
		  System.out.println(mrb.getJobUIDStamp());
		  System.out.println(mrb.getViscoTarUrl());
		  
		  //Store the mesh run in a meshrun megabean.
		  MeshDataMegaBean mega=new MeshDataMegaBean();
		  mega.setProjectName(projectName);
		  mega.setUserName(userName);
		  mega.setJobUIDStamp(jobStamp);
		  mega.setMeshRunBean(mrb);
		  //		  mega.setGeoFESTBaseUrlForJnlp(getGeoFESTBaseUrlForJnlp());
		  mega.setGeoFESTBaseUrlForJnlp(getBASE64(geoFESTBaseUrl));
 		  mega.setJnlpLayers(getMyLayersParamForJnlp(null, projectName));
 		  mega.setJnlpFaults(getMyFaultsParamForJnlp(null,projectName));
		  mega.setCreationDate(new Date().toString());
		  mega.setGeotransParamsBean(getCurrentGeotransParamsBean());
			
		  //Set the initial status
		  initGeofestGridService();
		  String statusString="MeshGen."+geofestGridService.queryMeshGeneratorStatus(getUserName(),projectName,jobStamp);
		  System.out.println("Status at Submission:"+statusString);
		  mega.setMeshStatus(statusString);
			  
		  //Set up the database.  This open/close routine may need to be improved later.
		  
		  try {
			  if (db!=null)
				  db.close();
			  
			  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");
			  db.set(mega);
			  db.commit();
			  
			  if (db!=null)
				  db.close();
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[storeMeshRunInContext] " + e);
		  }

		  return mega;
	 }
	 
// 	 protected MeshRunBean restoreMeshRunInstance(String projectName, 
// 																 String jobUIDStamp) 
// 		  throws Exception {
// 		  //		  return (MeshRunBean)results.next();
		  
//  		  MeshRunBean mrb=new MeshRunBean();
// 		  return mrb;
// 	 }

    /**
     * This is a JSF compatible method for running the mesh generator
     * in blocking mode.  That is, it takes no argument and assumes
     * the values have been set by accessors.
     */ 
    public String runBlockingMeshGeneratorJSF() 
		  throws Exception {
		  
		  Layer[] layers=getLayersFromDB();
		  Fault[] faults=getFaultsFromDB();
		  
		  initGeofestService();
		  projectMeshRunBean=geofestService.runBlockingMeshGenerator(getUserName(),
																						 projectName,
																						 faults,
																						 layers,
																						 meshResolution);
		  setJobToken(projectMeshRunBean.getJobUIDStamp());
		  storeMeshRunInContext(getUserName(),
										projectName,
										projectMeshRunBean.getJobUIDStamp(),
										projectMeshRunBean);
		  return MESH_GENERATION_NAV_STRING;
    }
    
    /**
     * This runs the mesh generator in non-blocking mode, so it returns
     * immediately.  The client must determine separately if the mesh
     * generation has finished.
     */ 
    public String runNonBlockingMeshGenerartorJSF() 
		  throws Exception {

		  try {
		  Layer[] layers=getLayersFromDB();
		  Fault[] faults=getFaultsFromDB();

		  initGeofestGridService();		 

		  String proxyLocation=null;
		  String gridResourceVal=constructForkResource("gt2",gridRefinerHost);
		  String userHome=constructUserHome(gridRefinerHost);
		  String meshExec=userHome+"/bin/autoref.pl";
		  String envSettings="\""+"PATH="+userHome+"/bin/:/bin/:/usr/bin/"+"\"";
		  String args=meshResolution;

		  MeshRunBean projectMeshRunBean=geofestGridService.runGridMeshGenerator(getUserName(),
																										 projectName,
																										 faults,
																										 layers,
																										 args,
																										 proxyLocation,
																										 gridResourceVal,
																										 envSettings,
																										 meshExec);
		  
		  
		  setJobToken(projectMeshRunBean.getJobUIDStamp());
		  MeshDataMegaBean mega=storeMeshRunInContext(getUserName(),
																	 projectName, 
																	 projectMeshRunBean.getJobUIDStamp(),
																	 projectMeshRunBean);
		  
		  
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				throw new Exception(ex.getMessage());
		  }
		  return MESH_GENERATION_NAV_STRING;
    }

    /**
     * This is a no-argument method for running geofest that can be called
     * from a JSF pge.
     */
    public String runGeoFESTJSF()
		  throws Exception {
		  
		  String tokenName=getJobToken();
		  GeotransParamsBean currentGeotransParamsBean=getCurrentGeotransParamsBean();
		  System.out.println("ProjectName:"+projectName);
		  System.out.println("tokenName:"+tokenName);

		  initGeofestGridService();

		  String proxyLocation=null;
		  String gridResourceVal=constructGridResource("gt2",gridGeoFESTHost);
		  String userHome=constructUserHome(gridGeoFESTHost);
		  String exec=userHome+"/bin/GeoFEST";
		  String args=currentGeotransParamsBean.getInputFileName()+" "+
				currentGeotransParamsBean.getOutputFileName();
		  System.out.println("GeoFEST arguments:"+args);
		  String envSettings="\""+"PATH="+userHome+"/bin/:/bin/:/usr/bin/"+"\"";

		  projectGeoFestOutput=geofestGridService.runGridGeoFEST(getUserName(),
																					projectName,
																					currentGeotransParamsBean,
																					exec,
																					args,
																					gridResourceVal,
																					proxyLocation,
																					envSettings,
																					tokenName);

		  System.out.println(projectGeoFestOutput.getCghistUrl());
		  System.out.println(projectGeoFestOutput.getIndexUrl());
		  saveGeotransParamsToDB(getUserName(), 
										 projectName, 
										 tokenName, 
										 currentGeotransParamsBean,
										 projectGeoFestOutput);
		  return GEOFEST_EXECUTION_LAUNCHED;
    }
	 
	 protected void saveGeotransParamsToDB(String userName, 
														String projectName, 
														String tokenName, 
														GeotransParamsBean currentGeotransParamsBean,
														GFOutputBean projectGeoFestOtput) 
		  throws Exception {

		  //Set up the bean template for searching.
		  MeshDataMegaBean mega=new MeshDataMegaBean();
		  //		  mega.setUserName(userName);
		  //		  System.out.println("Bean coordinates: "+projectName+" "+tokenName);

		  mega.setProjectName(projectName);
		  mega.setJobUIDStamp(tokenName);

		  //Set the initial status
		  initGeofestGridService();
		  String statusString="GeoFEST."+geofestGridService.queryGeoFESTStatus(getUserName(),projectName,tokenName);
		  System.out.println("Status at Submission:"+statusString);

		  
		  try {
		  if (db!=null)
			  db.close();
		  
		  //Find the matching bean
		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");

		  ObjectSet results=db.get(MeshDataMegaBean.class);

		  while (results.hasNext()) {
				//Reassign the bean.  Should only be one match.
				mega=(MeshDataMegaBean)results.next();
				if(mega.getProjectName().equals(projectName) && mega.getJobUIDStamp().equals(tokenName)) {
					 System.out.println("Found the bean");
					 //Update the geotrans params
					 mega.setGeotransParamsBean(currentGeotransParamsBean);
					 mega.setGeofestOutputBean(projectGeoFestOutput);
					 mega.setCreationDate(new Date().toString());
					 mega.setGeoFestStatus(statusString);
					 db.set(mega);
					 db.commit();
					 System.out.println("Mega Saving Geofest cghist url:"+mega.getGeofestOutputBean().getCghistUrl());
					 System.out.println("Mega Saving index url:"+mega.getGeofestOutputBean().getIndexUrl());
					 break;
				}
		  }
		  if (db!=null)
			  db.close();
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[saveGeotransParamsToDB] " + e);
		  }
		  
	 }
    //--------------------------------------------------

        
    public void QueryLayersList() {
		  
		  String getLayerListSQL = "select LayerName from LAYER LEFT JOIN LREFERENCE on LAYER.InterpId=LREFERENCE.InterpId;";
		  String getAuthorListSQL = "select Author1 from LAYER LEFT JOIN LREFERENCE on LAYER.InterpId=LREFERENCE.InterpId;";
		  
		  myLayerDBEntryList.clear();
		  currentLayer=new Layer();
		  
		  List tmp_layerNameList = QueryFaultsBySQL(getLayerListSQL);
		  List tmp_layerAuthorList = QueryFaultsBySQL(getAuthorListSQL);
		  
		  for (int i = 0; i < tmp_layerNameList.size(); i++) {
				String tmp1 = tmp_layerNameList.get(i).toString();
				LayerDBEntry tmp_LayerDBEntry = new LayerDBEntry();
				tmp_LayerDBEntry.layerName = new SelectItem(tmp1, tmp1);
				tmp_LayerDBEntry.layerAuthor = tmp_layerAuthorList.get(i)
					 .toString();
				myLayerDBEntryList.add(tmp_LayerDBEntry);
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
		  
// 		  String sqlQuery = "select F." + param
// 				+ " from FAULT AS F, REFERENCE AS R where F.FaultName=\'" + theFault
// 				+ "\' and F.InterpId=R.InterpId;";

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
    
    public Fault QueryFaultFromDB(String faultname) {
	// Check request with fallback
	

	/* Modified to the new module importing from kml desc 09/12/18 Jun Ji at CGL, jid@cs.indiana.edu
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
		      String length = Double.toString(kdp.getlength());

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
		      tmp_fault.setFaultName (theFault);
		      tmp_fault.setFaultLength(length);
		      tmp_fault.setFaultWidth(width+"");
		      tmp_fault.setFaultDepth(depth+"");
		      tmp_fault.setFaultDipAngle(dip+"");
		      tmp_fault.setFaultSlip("0");
		      tmp_fault.setFaultRakeAngle("0");		      

		      //This is the fault's strike angle
				//				strike=Math.atan2(x,y)/d2r;
		      tmp_fault.setFaultStrikeAngle(df.format(strike));			

		      //Get the origin of the first fault.
		      Fault[] faults=getFaultsFromDB();
		      if(faults!=null 
			      && faults[0]!=null
			      && faults[0].getFaultLonStart()!=null
			      && faults[0].getFaultLatStart()!=null) {
			      origin_lon=Double.parseDouble(faults[0].getFaultLonStart());
			      origin_lat=Double.parseDouble(faults[0].getFaultLatStart());
		      }
		  //No fault, so set the p
		  else {
			  origin_lon=lonStart;
			  origin_lat=latStart;
		  }
		  
		  //This is the (x,y) of the fault relative to the project's origin
		  //The project origin is the lower left lat/lon of the first fault.
		  double x1=(lonStart-origin_lon)*factor(origin_lon,origin_lat);
		  double y1=(latStart-origin_lat)*111.32;
		  System.out.println("Fault origin: "+x1+" "+y1);
			    tmp_fault.setFaultLocationX(df.format(x1));
				      tmp_fault.setFaultLocationY(df.format(y1));				

			} catch (Exception ex) {
				      ex.printStackTrace();
			}
	    
		  return tmp_fault;
    }
    
    public Layer QueryLayerFromDB(String tmp_layername) {
		  Layer tmp_layer = new Layer();
		  try {
				SelectService ss = new SelectServiceLocator();
				Select select = ss.getSelect(new URL(faultDBServiceUrl));
				
				// --------------------------------------------------
				// Make queries.
				// --------------------------------------------------
				tmp_layer.setLayerName(tmp_layername);
				tmp_layer.setLayerOriginX(getDBValue(select, "OriginX",
																 tmp_layername));
				tmp_layer.setLayerOriginY(getDBValue(select, "OriginY",
																 tmp_layername));
				tmp_layer.setLayerOriginZ(getDBValue(select, "OriginZ",
																 tmp_layername));
				tmp_layer.setLayerLatOrigin(getDBValue(select, "LatOrigin",
																	tmp_layername));
				tmp_layer.setLayerLonOrigin(getDBValue(select, "LonOrigin",
																	tmp_layername));
				tmp_layer.setLayerLength(getDBValue(select, "Length", tmp_layername));
				tmp_layer.setLayerWidth(getDBValue(select, "Width", tmp_layername));
				tmp_layer.setLayerDepth(getDBValue(select, "Depth", tmp_layername));
				tmp_layer.setLameLambda(getDBValue(select, "LameLambda",
															  tmp_layername));
				tmp_layer.setLameMu(getDBValue(select, "LameMu", tmp_layername));
				tmp_layer.setViscosity(getDBValue(select, "Viscosity", tmp_layername));
				tmp_layer.setExponent(getDBValue(select, "ViscosityExponent",
															tmp_layername));
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  return tmp_layer;
    }
	 
    //--------------------------------------------------
    // End of the fault db section
    //--------------------------------------------------
	 
    //--------------------------------------------------
    // Begin the event handling section for the JSF pages.
    //--------------------------------------------------

    /**
     * 
     */
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
    
    /**
     * This is used to handle the forms for editing a layer's params.
     */ 
    public String handleLayerEntryEdit(ActionEvent ev) {
		  
		  // Get selected MyData item to be edited.
		  try {
				// Catch the MyData item during the third phase of the JSF
				// lifecycle.
				LayerDBEntry tmp_LayerDBEntry = (LayerDBEntry) getMyLayerDataTable()
					 .getRowData();
				SelectItem tmp_SelectItem = tmp_LayerDBEntry.getLayerName();
				currentLayer.setLayerName(tmp_SelectItem.getValue().toString());
		  } catch (Exception e) {
				e.printStackTrace();
		  }
		  initEditFormsSelection();
		  currentLayer.setLayerName(currentLayer.getLayerName().trim());
		  if (!currentLayer.getLayerName().equals("")) {
				currentLayer = QueryLayerFromDB(currentLayer.getLayerName().trim());
		  }
		  renderCreateNewLayerForm = !renderCreateNewLayerForm;
		  
		  return "edit"; // Navigation case.
    }
    
    /**
     * This method is used to allow for updates to the context manager.
     */ 
    public String setValue(ContextManagerImp cm, 
									String Status, 
									String name,
									String prop) throws Exception {
		  String retval = "";
		  if (Status.equals("Update")) {
				retval = cm.getCurrentProperty(name, prop);
		  }
		  return retval;
    }
	 
    public void handleLayersRadioValueChange(ValueChangeEvent event) {
	
		  try {
				// Catch the MyData item during the third phase of the JSF
				// lifecycle.
				LayerDBEntry tmp_LayerDBEntry = (LayerDBEntry) getMyLayerDataTable()
					 .getRowData();
				SelectItem tmp_SelectItem = tmp_LayerDBEntry.getLayerName();
				currentLayer.setLayerName(tmp_SelectItem.getValue().toString());
		  } catch (Exception e) {
				e.printStackTrace();
		  }
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
		  renderCreateNewLayerForm = false;
		  renderCreateNewFaultForm = false;
		  renderAddLayerFromDBForm = false;
		  renderAddFaultSelectionForm = false;
		  renderAddFaultFromDBForm = false;
		  renderFaultMap = false;
    }

	 /**
	  * Select Mesh for Geofest run.
	  */
	 public String selectMeshForGeoFEST(ActionEvent ev) {
		  //load the mesh into memory.
		  //Default will be an empty bean
		  
		  //Recover the mega bean.
		  //		  MeshDataMegaBean mega=(MeshDataMegaBean)myMeshDataTable.getRowData();
		  MeshDataMegaBean mega=(MeshDataMegaBean)getArchivedMeshTable().getRowData();
		  String selectedMeshName=mega.getMeshRunBean().getProjectName();
		  String selectedMeshStamp=mega.getMeshRunBean().getJobUIDStamp();
		  
		  //Set these class variables
		  projectName=selectedMeshName;
		  jobToken=selectedMeshStamp;

		  currentGeotransParamsBean=mega.getGeotransParamsBean();
		  currentGeotransParamsBean.setInputFileName(selectedMeshName+".inp");
		  currentGeotransParamsBean.setOutputFileName(selectedMeshName+".out");
		  currentGeotransParamsBean.setLogFileName(selectedMeshName+".log");

		  //Go to geotransparams.
		  return GO_TO_GEOTRANSPARAMS;
	 }
    
    /**
     * Possibly needed for backward compatibility.
     */
    public String toggleFireMeshGen(ActionEvent ev) {
		  try {
				return runBlockingMeshGeneratorJSF();
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				return "toggle-fire-meshgen-failure";
		  }
    }

    /**
     * Handle action events in the project selection area.
     */
    public void toggleProjectSelection(ActionEvent ev) {
		  initEditFormsSelection();
		  if (projectSelectionCode.equals("CreateNewLayer")) {
				currentLayer=new Layer();
				renderCreateNewLayerForm = !renderCreateNewLayerForm;
		  }
		  else if (projectSelectionCode.equals("CreateNewFault")) {
				currentFault=new Fault();
				renderCreateNewFaultForm = !renderCreateNewFaultForm;
		  }
		  else if (projectSelectionCode.equals("AddLayerFromDB")) {
				QueryLayersList();
				renderAddLayerFromDBForm = !renderAddLayerFromDBForm;
		  }
		  else if (projectSelectionCode.equals("AddFaultSelection")) {
				renderAddFaultSelectionForm = !renderAddFaultSelectionForm;
		  }
		  else if (projectSelectionCode.equals("FaultMapSelection")) {
				renderFaultMap=!renderFaultMap;
		  }
		  else if (projectSelectionCode.equals("")) {
				;
		  }
		  else {
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
				// myFaultDBEntryList=ViewAllFaults(faultDBServiceUrl);
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
    
    public void toggleSelectLayerDBEntry(ActionEvent ev) {
		  initEditFormsSelection();
		  currentLayer.setLayerName(currentLayer.getLayerName().trim());
		  if (!currentLayer.getLayerName().equals("")) {
		      currentLayer = QueryLayerFromDB(currentLayer.getLayerName().trim());
		  }
		  renderCreateNewLayerForm = !renderCreateNewLayerForm;
    }
    
    public void toggleFaultSearchByName(ActionEvent ev) {
		  initEditFormsSelection();
		  this.forSearchStr = this.forSearchStr.trim();
		  if (!this.forSearchStr.equals("")) {
		      // myFaultDBEntryList=QueryFaultsByName(this.forSearchStr,faultDBServiceUrl);
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
		      // myFaultDBEntryList=QueryFaultsByLonLat(this.faultLatStart, this.faultLatEnd,this.faultLonStart, this.faultLonEnd,faultDBServiceUrl);
		      KMLdescriptionparser kdp = new KMLdescriptionparser();
		      kdp.parseXml(getBasePath() + "/" + codeName + "/", kmlfiles);
		      myFaultDBEntryList = kdp.getFaultList("LonLat", this.faultLatStart + " " + this.faultLatEnd + " " + this.faultLonStart + " " + this.faultLonEnd);
		      
		  }

		  renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
    }
    
	 /**
	  * Create the layer collection
	  */ 
	 protected List populateLayerCollection(List myLayerEntryProjectList) throws Exception {
		  List myLayerCollection=new ArrayList();
		  for(int i=0;i<myLayerEntryProjectList.size();i++) {
				layerEntryForProject tmp_LayerEntryForProject 
					 = (layerEntryForProject) myLayerEntryForProjectList.get(i);
				String tmp_layerName = tmp_LayerEntryForProject.getLayerName();
				myLayerCollection.add(populateLayerFromContext(tmp_layerName));
		  }
		  return myLayerCollection;
	 }

	 protected Layer populateLayerFromContext(String tmp_layerName) throws Exception  {
		  System.out.print("Populating Layer "+tmp_layerName+" for  "+projectName);
		  String layerStatus = "Update";				
		 
		  try {
		  if (db!=null)
			  db.close();
		  
 		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");		  

		  Layer layerToGet=new Layer();
		  layerToGet.setLayerName(tmp_layerName);
		  ObjectSet results=db.get(layerToGet);
		  //Should only have one value.
		  Layer currentLayer=null;
		  if(results.hasNext()){
				currentLayer=(Layer)results.next();
		  }
		  if (db!=null)
			  db.close();
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[populateLayerFromContext] " + e);
		  }
		  
		  
		  return currentLayer;
	 }

    public void toggleUpdateLayerProjectEntry(ActionEvent ev) {
		  String layerStatus = "Update";
		  try {
				int iSelectLayer=-1;
				// Catch the MyData item during the third phase of the JSF
				// lifecycle.
				//Find out which layer the user selected.
				layerEntryForProject tmp_LayerEntryForProject = new layerEntryForProject();
				for (int i = 0; i < myLayerEntryForProjectList.size(); i++) {
					 tmp_LayerEntryForProject = (layerEntryForProject) myLayerEntryForProjectList
						  .get(i);
					 if ((tmp_LayerEntryForProject.getView() == true)
						  || (tmp_LayerEntryForProject.getDelete() == true)) {
						  iSelectLayer=i;
						  break;
					 }
				}
				
				String tmp_layerName = tmp_LayerEntryForProject.getLayerName();
				boolean tmp_view = tmp_LayerEntryForProject.getView();
				boolean tmp_update = tmp_LayerEntryForProject.getDelete();
				String projectFullName = codeName + SEPARATOR + projectName;
				
				initEditFormsSelection();
				if ((tmp_view == true) && (tmp_update == true)) {
					 System.out.println("error");
				}
				//This is the edit case.
				if ((tmp_view == true) && (tmp_update == false)) {
					 currentLayer=populateLayerFromContext(tmp_layerName);
					 renderCreateNewLayerForm = !renderCreateNewLayerForm;
				}

				//This is the deletion case.
				if ((tmp_update == true) && (tmp_view == false)) {
					 System.out.println("Deleteing "+tmp_layerName+" from db");
					 
					 try {
						 if (db!=null)
							  db.close();
					 db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");		  
					 //First, we have to query for the matching layer.
					 Layer todelete=new Layer();
					 todelete.setLayerName(tmp_layerName);
					 ObjectSet result1=db.get(todelete);
					 if(result1.hasNext()) {
						  todelete=(Layer)result1.next();
						  //Now that we have the specific object, we can delete it.
						  db.delete(todelete);
					 }
					 if (db!=null)
						  db.close();
					 } catch (Exception e) {			  
						  if (db!=null)
							  db.close();			  
						  System.out.println("[toggleUpdateLayerProjectEntry] " + e);
					  }

					 if(myLayerCollection.size()>iSelectLayer) {					 
						  myLayerCollection.remove(iSelectLayer);
					 }
					 String tmp = projectFullName + SEPARATOR + LAYERS + SEPARATOR
						  + tmp_layerName;
					 cm.removeContext(tmp);

					 
					 gfutils.updateGroupFile(cm, projectFullName, projectName);
// 					 try {
// 						  MyVTKServiceLocator service = new MyVTKServiceLocator();
// 						  MyVTKServicePortType meshserv = service
// 								.getMyVTKService(new URL(meshViewerServerUrl));
// 						  meshserv.removeLayer(tmp_layerName);
// 					 } catch (Exception ex) {
// 						  ;
// 					 }
					 
				}
				
		  } catch (Exception e) {
				e.printStackTrace();
		  }
    }
    
	 protected List populateFaultCollection(List myFaultEntryProjectList) throws Exception {
		  List myFaulCollection=new ArrayList();
		  for (int i=0;i<myFaultEntryProjectList.size();i++) {
				faultEntryForProject tmp_faultEntryForProject=
					 (faultEntryForProject)myFaultEntryForProjectList.get(i);
				String tmp_faultName=tmp_faultEntryForProject.getFaultName();
				myFaultCollection.add(populateFaultFromContext(tmp_faultName));
		  }		  
		  return myFaultCollection;
	 }

	 protected Fault populateFaultFromContext(String tmp_faultName) throws Exception {
		  String faultStatus="Update";

		  try {
		  if (db!=null)
			  db.close();
		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");		  
		  Fault faultToGet=new Fault();
		  faultToGet.setFaultName(tmp_faultName);
		  ObjectSet results=db.get(faultToGet);
		  //Should only have one value.
		  Fault currentFault=null;
		  if(results.hasNext()){
				currentFault=(Fault)results.next();
		  }
		  if (db!=null)
			  db.close();
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[populateFaultFromContext] " + e);
		  }
		  
		  return currentFault;
		  
	 }

    public void toggleUpdateFaultProjectEntry(ActionEvent ev) {
		  String faultStatus = "Update";
		  try {
				int iSelectFault=-1;

				//Find out which fault was selected.
				faultEntryForProject tmp_FaultEntryForProject = new faultEntryForProject();
				for (int i = 0; i < myFaultEntryForProjectList.size(); i++) {
					 tmp_FaultEntryForProject = (faultEntryForProject) myFaultEntryForProjectList
						  .get(i);
					 if ((tmp_FaultEntryForProject.getView() == true)
						  || (tmp_FaultEntryForProject.getDelete() == true)) {
						  iSelectFault=i;
						  break;
					 }
				}
				
				//This is the info about the fault.
				String tmp_faultName = tmp_FaultEntryForProject.getFaultName();
				boolean tmp_view = tmp_FaultEntryForProject.getView();
				boolean tmp_update = tmp_FaultEntryForProject.getDelete();
				String projectFullName = codeName + SEPARATOR + projectName;
				
				initEditFormsSelection();
				if ((tmp_view == true) && (tmp_update == true)) {
					 System.out.println("error");
				}
				
				//Update the fault.
				if ((tmp_view == true) && (tmp_update == false)) {
					 
					 currentFault=populateFaultFromContext(tmp_faultName);
					 renderCreateNewFaultForm = !renderCreateNewFaultForm;
					 
				}

				//This is the deletion case.
				if ((tmp_update == true) && (tmp_view == false)) {

					 //Delete from the database.
					 //This requires we first search for the desired object
					 //and then delete the specific value that we get back.
					 System.out.println("Deleteing "+tmp_faultName+"from db");
					 
					 try {						 
						 if (db!=null)
							  db.close();

					 db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");		  
				 
					 Fault todelete=new Fault();
					 todelete.setFaultName(tmp_faultName);
					 ObjectSet result=db.get(todelete);
					 if(result.hasNext()) {
						  todelete=(Fault)result.next();
						  db.delete(todelete);
					 }
					 if (db!=null)
						  db.close();
					 } catch (Exception e) {			  
						  if (db!=null)
							  db.close();			  
						  System.out.println("[toggleUpdateFaultProjectEntry] " + e);
					  }

					 String tmp = projectFullName + SEPARATOR + FAULTS + SEPARATOR
						  + tmp_faultName;
					 cm.removeContext(tmp);
					 gfutils.updateGroupFile(cm, projectFullName, projectName);
// 					 try {
// 						  MyVTKServiceLocator service = new MyVTKServiceLocator();
// 						  MyVTKServicePortType meshserv = service
// 								.getMyVTKService(new URL(meshViewerServerUrl));
// 						  meshserv.removeFault(tmp_faultName);
// 					 } catch (Exception ex) {
// 						  ;
// 					 }
				}
				
		  } catch (Exception e) {
				e.printStackTrace();
		  }
    }
    
    public void toggleFaultSearchByAuthor(ActionEvent ev) {
		  initEditFormsSelection();
		  this.forSearchStr = this.forSearchStr.trim();
		  if (!this.forSearchStr.equals("")) {
		      myFaultDBEntryList=QueryFaultsByAuthor(this.forSearchStr,faultDBServiceUrl);
		  }
		  this.forSearchStr = "";
		  renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
    }
    
    //Method needs to be either re-written or deleted.
    public void toggleRefineMesh(ActionEvent ev) {
    }

    //Probably need to remove this one.
    public void toggleViewMeshMessages(ActionEvent ev) {
    }
    
    public void toggleAddLayerForProject(ActionEvent ev) {
		  //This simply finds the layer to update by name (which is unique),
		  //deletes it, and replaces it with the updated layer.
		  initEditFormsSelection();
		  try {
		  if (db!=null)
			  db.close();
		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");	
				 
		  Layer tmpLayer=new Layer();
		  tmpLayer.setLayerName(currentLayer.getLayerName());
		  ObjectSet result=db.get(tmpLayer);
		  if(result.hasNext()){
				tmpLayer=(Layer)result.next();
				db.delete(tmpLayer);
		  }
		  db.set(currentLayer);
		  db.commit();
		  if (db!=null)
			  db.close();
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[toggleAddLayerForProject] " + e);
		  }

    }
    
    public void toggleAddFaultForProject(ActionEvent ev) throws Exception {
		  initEditFormsSelection();
		  try {
			  if (db!=null)
				  db.close();
		  
		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");
				 
		  Fault tmpfault=new Fault();
		  tmpfault.setFaultName(currentFault.getFaultName());
		  ObjectSet result=db.get(tmpfault);
		  if(result.hasNext()) {
				tmpfault=(Fault)result.next();
				db.delete(tmpfault);
		  }
		  db.set(currentFault);
		  db.commit();
		  if (db!=null)
			  db.close();
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[toggleAddFaultForProject] " + e);
		  }
		  
    }
    
    public void toggleRenderDBLayerList(ActionEvent ev) {
		  renderDBLayerList = !renderDBLayerList;
    }
    
    public boolean getRenderDBLayerList() {
		  return renderDBLayerList;
    }
    
    public void setRenderDBLayerList(boolean renderDBLayerList1) {
		  this.renderDBLayerList = renderDBLayerList1;
    }
    
    /**
     * These are methods associated with Faces navigations.
     */
    public String newProject() throws Exception {
// 		  isInitialized = getIsInitialized();
// 		  if (!isInitialized) {
// 				initWebServices();
// 		  }
// 		  setContextList();
		  makeProjectDirectory();
		  return ("MG-new-project");
    }
    
    public void init_edit_project() {
		  initEditFormsSelection();
		  projectSelectionCode = "";
		  faultSelectionCode = "";
    }
    

    public String NewProjectThenEditProject() throws Exception {
// 		  if (!isInitialized) {
// 				initWebServices();
// 		  }
// 		  setContextList();

		  setProjectname();
		  init_edit_project();
		  return "MG-edit-project";
    }
    
    public String toggleSelectProject() throws Exception  {
// 		  if (!isInitialized) {
// 				initWebServices();
// 		  }
// 		  try {
// 				setContextList();
// 		  } catch (Exception ex) {
// 				ex.printStackTrace();
// 		  }
		  
		  initEditFormsSelection();
		  //This is implemented as a selectmanycheckbox on the client side (LoadProject.jsp),
		  //hence the unusual for loop.
		  if (selectProjectsList != null) {
				for (int i = 0; i < 1; i++) {
					 this.projectName = selectProjectsList[0];
				}
		  }
		  
		  //Reconstruct the project lists
		  myFaultEntryForProjectList=reconstructMyFaultEntryForProjectList(projectName);
		  myLayerEntryForProjectList=reconstructMyLayerEntryForProjectList(projectName);

		  //Reconstruct the fault and layer object collections from the context
		  myFaultCollection=populateFaultCollection(myFaultEntryForProjectList);
		  myLayerCollection=populateLayerCollection(myLayerEntryForProjectList);

		  projectSelectionCode = "";
		  faultSelectionCode = "";
		  return "MG-edit-project";
    }

    public String toggleDeleteProject() {
		  System.out.println("Deleting projects");
		  try {
				if (deleteProjectsList != null) {
					 for (int i = 0; i < deleteProjectsList.length; i++) {
						  System.out.println("Deleting: "+deleteProjectsList[i]);
						  
						  try {
						  if (db!=null)
							  db.close();
						  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+".db");
						  ProjectBean delproj=new ProjectBean();
						  delproj.setProjectName(deleteProjectsList[i]);
						  ObjectSet results=db.get(delproj);
						  if(results.hasNext()){
								delproj=(ProjectBean)results.next();
								db.delete(delproj);
						  }
						  if (db!=null)
							  db.close();
						  } catch (Exception e) {			  
							  if (db!=null)
								  db.close();			  
							  System.out.println("[toggleDeleteProject] " + e);
						  }
					 }
				}
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
		  return "MG-this";
    }
    
    public String setProjectname() throws Exception {
		  // Do real logic
		  System.out.println("Creating new project");
		  makeProjectDirectory();
		  
		  try {
			  if (db!=null)
				  db.close();
 		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+".db");		  		  
		  ProjectBean project=new ProjectBean();
		  project.setProjectName(projectName);
		  db.set(project);
		  db.commit();
		  if (db!=null)
			  db.close();
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[setProjectname] " + e);
		  }

		  return "MG-set-project";
    }
    
    public String SetAndViewMeshImage() throws Exception {
		  myMeshViewer.reset();
		  myMeshViewer.setServiceUrl(this.meshViewerServerUrl);
		  myMeshViewer.fileServiceUrl = fileServiceUrl;
		  myMeshViewer.projectName = projectName;
		  myMeshViewer.workDir = baseWorkDir + "/" + getUserName() + "/" + projectName
				+ "/";
		  ;
		  myMeshViewer.mesh_gen_viz_base_dir = mesh_gen_viz_base_dir;
		  myMeshViewer.mesh_gen_viz_fileServiceUrl = mesh_gen_viz_fileServiceUrl;
		  return "MG-view-mesh";
    }
    
    public String SetAndPlot() throws Exception {
		  myMeshViewer.reset();
		  myMeshViewer.setServiceUrl(this.meshViewerServerUrl);
		  return "MG-plot";
    }
    
    public String loadDataArchive() throws Exception {
// 		  if (!isInitialized) {
// 				initWebServices();
// 		  }
// 		  setContextList();
		  return ("rdahmm-load-data-archive");
    }
    
    public String loadProjectList() throws Exception {
// 		  if (!isInitialized) {
// 				initWebServices();
// 		  }
// 		  setContextList();
		  makeProjectDirectory();


		  return ("MG-list-project");
    }

	 /**
	  * Reconstructs the fault entry list.
	  */
	 protected List reconstructMyFaultEntryForProjectList(String projectName) {
		  String projectFullName = codeName + SEPARATOR + projectName;
		  this.myFaultEntryForProjectList.clear();
		  try {
			  if (db!=null)
				  db.close();
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");
				 
				Fault tmpfault=new Fault();
				ObjectSet results=db.get(tmpfault);
				while(results.hasNext()){
					 tmpfault=(Fault)results.next();
					 faultEntryForProject tmp_myFaultEntryForProject = new faultEntryForProject();
					 tmp_myFaultEntryForProject.setFaultName(tmpfault.getFaultName());
					 tmp_myFaultEntryForProject.view = false;
					 tmp_myFaultEntryForProject.delete = false;
					 this.myFaultEntryForProjectList
						  .add(tmp_myFaultEntryForProject);
				}
				if (db!=null)
					  db.close(); 

		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[reconstructMyFaultEntryForProjectList] " + e);
		  }
		  
		  return this.myFaultEntryForProjectList;
    }

	 /**
	  * Reconstruct the layer list from the context.
	  */
    protected List reconstructMyLayerEntryForProjectList(String projectName) {
		  String projectFullName = codeName + SEPARATOR + projectName;
		  this.myLayerEntryForProjectList.clear();
		  try {
			  if (db!=null)
				  db.close();
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");		  
				 
				Layer tmplayer=new Layer();
				ObjectSet results=db.get(tmplayer);
				while(results.hasNext()){
					 tmplayer=(Layer)results.next();
					 layerEntryForProject tmp_myLayerEntryForProject = new layerEntryForProject();
					 tmp_myLayerEntryForProject.layerName = tmplayer.getLayerName();
					 tmp_myLayerEntryForProject.view = false;
					 tmp_myLayerEntryForProject.delete = false;
					 this.myLayerEntryForProjectList
						  .add(tmp_myLayerEntryForProject);
				}
				if (db!=null)
					  db.close();
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[reconstructMyLayerEntryForProjectList] " + e);
		  }

		  return this.myLayerEntryForProjectList;
    }
   
     public String loadMesh() throws Exception {
// 		  if (!isInitialized) {
// 				initWebServices();
// 		  }
// 		  setContextList();
		  
		  return ("MG-load-mesh");
    }
    
    public String fetchMesh() throws Exception {
// 		  if (!isInitialized) {
// 				initWebServices();
// 		  }
// 		  setContextList();

		  this.ListGeoFESTData = false;
		  return ("MG-fetch-mesh");
    }
    
    
    public void StageGeotransFile() throws Exception {
    }
    
    public String gfarchivedData() throws Exception {
// 		  if (!isInitialized) {
// 				initWebServices();
// 		  }
// 		  setContextList();

		  this.ListGeoFESTData = true;
		  return ("MG-gf-archived-data");
    }
    
    public String gfGraphOutput() throws Exception {
		  //System.out.println("gf Graph Output");
// 		  if (!isInitialized) {
// 				initWebServices();
// 		  }
// 		  setContextList();
		  this.ListGeoFESTData = true;
		  return ("MG-gf-graph-output");
    }
    
    public String ContourPlot() throws Exception {
		  //System.out.println("Contour Plot");
// 		  if (!isInitialized) {
// 				initWebServices();
// 		  }
// 		  setContextList();
		  FacesContext fc = FacesContext.getCurrentInstance();
		  this.selectedProject = (String) fc.getExternalContext()
				.getRequestParameterMap().get("ProjectSelect");
		  this.plotTarget = (String) fc.getExternalContext()
				.getRequestParameterMap().get("DataChoice");
		  
		  return ("MG-contour-plot");
    }	

    // --------------------------------------------------
    // These are accessor methods.
    // --------------------------------------------------
    /**
     * This method needs to be rewritten.
     */ 
	 public String getContourPlotPdfUrl() {
		  //Rewrite this and put the associated service in SVN.
		  return "Junk";
    }
    /**
     * These are some accessor methods.
     */
	 public void setSelectedProject(String tmp_str) {
		  this.selectedProject = tmp_str;
	 }
    
    public String getSelectedProject() {
		  
		  return this.selectedProject;
    }
    
    public void setPlotTarget(String tmp_str) {
		  this.plotTarget = tmp_str;
    }
    
    public String getPlotTarget() {
	
	return this.plotTarget;
    }
    
    public void setContourPlotPdfUrl(String tmp_str) {
		  this.contourPlotPdfUrl = tmp_str;
    }
	 
//     public void setCurrentGeotransParamsData(GeotransParamsData tmp_GeotransParamsData) {
// 		  this.currentGeotransParamsData = tmp_GeotransParamsData;
//     }
    
//     public GeotransParamsData getCurrentGeotransParamsData() {
// 		  return this.currentGeotransParamsData;
//     }
    
    public void setStatusGeoFEST(boolean tmp_str) {
		  this.statusGeoFEST = tmp_str;
    }
    
    public boolean getStatusGeoFEST() {
		  
		  return this.statusGeoFEST;
    }
    
    public void setMyarchivedMeshTableEntryList(List tmp_str) {
		  this.myarchivedMeshTableEntryList = tmp_str;
    }
	 
    public void setSelectProjectsList(String[] tmp_str) {
		  this.selectProjectsList = tmp_str;
    }
    
    public String[] getSelectProjectsList() {
		  return this.selectProjectsList;
    }

    public void setDeleteProjectsList(String[] tmp_str) {
		  this.deleteProjectsList = tmp_str;
    }
    
    public String[] getDeleteProjectsList() {		  
		  return this.deleteProjectsList;
    }
    
    
    public void setMyProjectNameList(List tmp_str) {
		  this.myProjectNameList = tmp_str;
    }
    
    public List getMyProjectNameList() {
		  this.myProjectNameList.clear();
		  try {
				//System.out.println(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+".db");
			  if (db!=null)
				  db.close();
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+".db");		  
				ProjectBean project=new ProjectBean();
				ObjectSet results=db.get(ProjectBean.class);
				//System.out.println("Got results:"+results.size());
				while(results.hasNext()) {
					 project=(ProjectBean)results.next();
					 //System.out.println(project.getProjectName());
					 myProjectNameList.add(new SelectItem(project.getProjectName(),
																	  project.getProjectName()));
				}
				if (db!=null)
					  db.close();				
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[getMyProjectNameList] " + e);
		  }
		  
		  return this.myProjectNameList;
    }
    
    public void setMesh_gen_viz_base_dir(String tmp_str) {
		  this.mesh_gen_viz_base_dir = tmp_str;
    }
    
    public String getMesh_gen_viz_base_dir() {
		  return this.mesh_gen_viz_base_dir;
    }
	 
	 public void setMesh_gen_viz_fileServiceUrl(String tmp_str) {
		  this.mesh_gen_viz_fileServiceUrl = tmp_str;
	 }
    
    public String getMesh_gen_viz_fileServiceUrl() {
		  return this.mesh_gen_viz_fileServiceUrl;
    }

    public String getProjectNameForJnlp() {
		  this.projectNameForJnlp = getBASE64(projectName);
		  return this.projectNameForJnlp;
    }
    
    public void setFaultarrayForMesh(String[] tmp_str) {
		  this.faultarrayForMesh = tmp_str;
    }
    
    public String[] getFaultarrayForMesh() {
		  return this.faultarrayForMesh;
    }
    
    public void setMyFaultsForProject(List tmp_str) {
		  this.myFaultsForProject = tmp_str;
    }
    
	 /**
	  * This method gets all the faults for the project.  It calls getMyFaultEntryForProjectList(),
	  * which reconstructs the contexts from file storage. 
	  */
    public List getMyFaultsForProject()  throws Exception {
		  this.myFaultsForProject.clear();
		  myFaultEntryForProjectList = getMyFaultEntryForProjectList();
		  for (int i = 0; i < myFaultEntryForProjectList.size(); i++) {
				faultEntryForProject tmp_FaultEntryForProject = (faultEntryForProject) myFaultEntryForProjectList
					 .get(i);
				String tmp_str = tmp_FaultEntryForProject.getFaultName();
				myFaultsForProject.add(new SelectItem(tmp_str, tmp_str));
		  }
		  
		  return this.myFaultsForProject;
    }
	 
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
				orderedList.add((MeshDataMegaBean)fullList.get(((Integer)reducedList.get(first)).intValue()));
				reducedList.remove(first);
		  }
		  return orderedList;
	 }
	 
	 protected int getFirst(List reducedList, List fullList) {
		  int first=0;
		  for(int i=1;i<reducedList.size();i++) {
				MeshDataMegaBean mb1=(MeshDataMegaBean)fullList.get(((Integer)reducedList.get(first)).intValue());
				MeshDataMegaBean mb2=(MeshDataMegaBean)fullList.get(((Integer)reducedList.get(i)).intValue());
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

	 /**
	  * A muscular getter, this reconstructs the list of MeshRunBean instances from the context.
	  * Note it depends on several class-scoped variables.
	  */
	 public List getMyArchivedMeshRunList() throws Exception {
		  List myprojectlist=getMyProjectNameList();
		  List tmpList=new ArrayList();
		  
		  //Initialize this just once.
		  initGeofestGridService();
		  
		  //		  myArchivedMeshRunList.clear();
		  if (myprojectlist.size() > 0) {
				for(int i=0;i<myprojectlist.size();i++){
					 String projectName=((SelectItem)myprojectlist.get(i)).getLabel();

					 MeshDataMegaBean mega=new MeshDataMegaBean();
					 mega.setProjectName(projectName);
					 //					 System.out.println("ProjectName: "+projectName);
					 
					 try {
					 if (db!=null)
						  db.close();
					 db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");
					 //					 ObjectSet results=db.get(mega);
					 ObjectSet results=db.get(MeshDataMegaBean.class);
					 //System.out.println("Matches for "+projectName+":"+results.size());
					 while(results.hasNext()){						  
						  mega=(MeshDataMegaBean)results.next();
						  //						  myArchivedMeshRunList.add(mega);
						  
						  //Update the status.
						  // String statusString="MeshGen."+geofestGridService.queryMeshGeneratorStatus(getUserName(),mega.getProjectName(),mega.getJobUIDStamp());
						  // System.out.println("Status:"+mega.getJobUIDStamp()+" "+statusString);
						  // mega.setMeshStatus(statusString);

						  tmpList.add(mega);
					 }
					 if (db!=null)
						  db.close();
					 } catch (Exception e) {			  
						  if (db!=null)
							  db.close();			  
						  System.out.println("[getMyArchivedMeshRunList] " + e);
					  }
					 
				}
				//				myArchivedMeshRunList=sortByDate(myArchivedMeshRunList);
				myArchivedMeshRunList=sortByDate(tmpList);

		  }

		  return myArchivedMeshRunList;
    }
	 
	 /**
	  * May want to get rid of this redundant method.
	  */
	 public List getMyArchivedMeshRunList2() throws Exception {
		  List myprojectlist=getMyProjectNameList();

		  myArchivedMeshRunList2.clear();
		  
		  if (myprojectlist.size() > 0) {
				for(int i=0;i<myprojectlist.size();i++){
					 //					 MeshRunBean mrb=new MeshRunBean();
					 MeshDataMegaBean mega=new MeshDataMegaBean();
					 String projectName=((SelectItem)myprojectlist.get(i)).getLabel();
					 
					 try {
					 if (db!=null)
						  db.close();
					 db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");
					 ObjectSet results=db.get(MeshDataMegaBean.class);
					 //Should only have one value.
					 //System.out.println("Mega count:"+results.size());
					 while(results.hasNext()){
						  mega=(MeshDataMegaBean)results.next();
						  if(mega.getGeofestOutputBean()!=null) {
								System.out.println("Mega has gfoutput:"+mega.getProjectName());
								System.out.println("Mega has gfoutput:"+mega.getGeofestOutputBean().getJobUIDStamp());
								System.out.println("Mega has gfoutput:"+mega.getGeofestOutputBean().getCghistUrl());
								myArchivedMeshRunList2.add(mega);
						  }
					 }
					 if (db!=null)
						  db.close();
					 } catch (Exception e) {			  
						  if (db!=null)
							  db.close();			  
						  System.out.println("[getMyArchivedMeshRunList2] " + e);
					  }
				}
		  }
		  return this.myArchivedMeshRunList2;
    }
    
    public void setMyLoadMeshTableEntryList(List tmp_str) {
		  this.myLoadMeshTableEntryList = tmp_str;
    }
    
    public String getBASE64(String s) {
		  if (s == null)
				return null;
		  String tmp_str = (new BASE64Encoder()).encode(s.getBytes());
		  return tmp_str;
    }
    
    public void setMyFaultsParamForJnlp(String tmp_str) {
		  this.myFaultsParamForJnlp = tmp_str;
    }
    
    public String getMyFaultsParamForJnlp() {
		  return getMyFaultsParamForJnlp(null, getProjectName());
	 }

    public String getMyFaultsParamForJnlp(ObjectContainer db, String projectName) {
		  myFaultsParamForJnlp = "";
		  // --------------------------------------------------
		  // Get set up. Start with useful constants.
		  // --------------------------------------------------
		  String space = " ";
		  String status = "Update";
		  
		  // --------------------------------------------------
		  // Find out who we are.
		  // --------------------------------------------------
		  String tmp_faults = new String("");
		  try {
				
				
				if (db!=null)
					  db.close();
				
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");
				 
				Fault faultToGet=new Fault();
				ObjectSet results=db.get(faultToGet);
				//System.out.println("db closed");

				if(results.hasNext()) {
					 for(int i=0;i<results.size();i++){
						  faultToGet=(Fault)results.next();
						  String faultDef = "addFault" + space;
						  faultDef+=faultToGet.getFaultName()+space;
						  faultDef += faultToGet.getFaultLocationX()+space;
						  faultDef +=faultToGet.getFaultLocationY()
								+ space;
						  faultDef +=faultToGet.getFaultLength()
								+ space;
						  faultDef +=faultToGet.getFaultWidth()
								+ space;
						  faultDef +=faultToGet.getFaultDepth()
								+ space;
						  faultDef +=faultToGet.getFaultDipAngle()
								+ space;
						  faultDef +=faultToGet.getFaultStrikeAngle()
								+ space;
						  //System.out.println("Faults!" + faultDef);
						  tmp_faults = tmp_faults + "*" + faultDef;
					 }
				}
				if (db!=null)
					  db.close();
	
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  this.myFaultsParamForJnlp = getBASE64(tmp_faults);
		  
		  return this.myFaultsParamForJnlp;
    }
    
    public void setMyLayersParamForJnlp(String tmp_str) {
		  this.myLayersParamForJnlp = tmp_str;
    }
    
	 /**
	  * Use this version if project is set externally.
	  */ 
	 public String getMyLayersParamForJnlp() {
		  return getMyLayersParamForJnlp(null, getProjectName());
	 }

    public String getMyLayersParamForJnlp(ObjectContainer db, String projectName) {

		  myLayersParamForJnlp = "";
		  // --------------------------------------------------
		  // Get set up. Start with useful constants.
		  // --------------------------------------------------
		  String space = " ";
		  String status = "Update";
		  
		  // --------------------------------------------------
		  // Find out who we are.
		  // --------------------------------------------------
		  String tmp_layers = new String("");
		  try {
				
			  if (db!=null)
				  db.close();
			  
			  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+getUserName()+"/"+codeName+"/"+projectName+".db");		  
					 
				

				Layer layerToGet=new Layer();
				ObjectSet results=db.get(layerToGet);
				if(results.hasNext()) {
					 for(int i=0;i<results.size();i++){
						  layerToGet=(Layer)results.next();
						  String layerDef = "addLayer" + space;
						  layerDef+=layerToGet.getLayerName()+space;
						  layerDef += layerToGet.getLayerOriginX()+space;
						  layerDef +=layerToGet.getLayerOriginY() + space;
						  layerDef +=layerToGet.getLayerOriginZ() + space;
						  layerDef +=layerToGet.getLayerLength() + space;
						  layerDef +=layerToGet.getLayerWidth() + space;
						  layerDef +=layerToGet.getLayerDepth() + space;

						  //System.out.println("Layers!" + layerDef);
						  tmp_layers = tmp_layers + "*" + layerDef;
					 }
				}
				
				if (db!=null)
					  db.close();
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[getMyLayersParamForJnlp] " + e);
		  }
		  
		  this.myLayersParamForJnlp = getBASE64(tmp_layers);
		  
		  return this.myLayersParamForJnlp;
    }
    
    public void setMyMeshViewer(MeshViewer tmp_str) {
		  this.myMeshViewer = tmp_str;
    }
    
    public MeshViewer getMyMeshViewer() {
		  return this.myMeshViewer;
    }
    
    public List getMyLayerEntryForProjectList() throws Exception {
		  //Assumes this has been set.
		  String projectName=getProjectName();
		  return reconstructMyLayerEntryForProjectList(projectName);
    }
    
    public void setMyLayerEntryForProjectList(List tmp_list) {
		  this.myLayerEntryForProjectList = tmp_list;
    }
    
	 /**
	  * This is a fairly muscular getter. It reconstructs the entire list from the 
	  * context.
	  */
    public List getMyFaultEntryForProjectList() throws Exception {
		  String projectName=getProjectName();
		  return reconstructMyFaultEntryForProjectList(projectName);
    }
    
    public void setMyFaultEntryForProjectList(List tmp_list) {
		  this.myFaultEntryForProjectList = tmp_list;
    }
    
    public String getForSearchStr() {
		  return this.forSearchStr;
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
    
    public List getMyFaultDBEntryList() {
	return myFaultDBEntryList;
    }
    
    public List getMyLayerDBEntryList() {
	return myLayerDBEntryList;
    }
    // Getters ----------------------------------------------------------
    
    public HtmlDataTable getMyLayerDataTable() {
		  return myLayerDataTable;
    }

    public UIData getMyMeshDataTable() {
		  return myLayerDataTable;
    }
    
    public HtmlDataTable getMyFaultDataTable() {
		  return myFaultDataTable;
    }
    
    // Setters ----------------------------------------------------------
    public void setMyMeshDataTable(UIData tmp_DataTable) {
		  this.myMeshDataTable = tmp_DataTable;
    }

    public void setMyLayerDataTable(HtmlDataTable tmp_DataTable) {
		  this.myLayerDataTable = tmp_DataTable;
    }
    
    public void setMyFaultDataTable(HtmlDataTable tmp_DataTable) {
		  this.myFaultDataTable = tmp_DataTable;
    }
    
    public void setCurrentLayer(Layer tmp_layer) {
		  this.currentLayer = tmp_layer;
    }
    
    public Layer getCurrentLayer() {
		  return this.currentLayer;
    }
    
    public void setCurrentFault(Fault tmp_fault) {
	this.currentFault = tmp_fault;
    }
    
    public Fault getCurrentFault() {
	return this.currentFault;
    }

	public void setSelectdbURL(String tmp_url) {
		this.faultDBServiceUrl = tmp_url;
	}

	public String getSelectdbURL() {
		return this.faultDBServiceUrl;
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

	public String getProjectSelectionCode() {
		return projectSelectionCode;
	}

	public void setProjectSelectionCode(String tmp_str) {
		this.projectSelectionCode = tmp_str;
	}

	public boolean getRenderCreateNewFaultForm() {
		return renderCreateNewFaultForm;
	}

	public void setRenderCreateNewFaultForm(boolean tmp_boolean) {
		this.renderCreateNewFaultForm = tmp_boolean;
	}

	public boolean getRenderAddLayerFromDBForm() {
		return renderAddLayerFromDBForm;
	}

	public void setRenderAddLayerFromDBForm(boolean tmp_boolean) {
		this.renderAddLayerFromDBForm = tmp_boolean;
	}

	public boolean getRenderCreateNewLayerForm() {
		return renderCreateNewLayerForm;
	}

	public void setRenderCreateNewLayerForm(boolean tmp_boolean) {
		this.renderCreateNewLayerForm = tmp_boolean;
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

    public void setJobToken(String jobToken){
	this.jobToken=jobToken;
    }
    
    public String getJobToken(){
	return jobToken;
    }

    public void setUserName(String userName){
		  this.userName=userName;
    }
    
    public String getUserName(){
		  if(Utility.getUserName(DEFAULT_USER_NAME) != null)
		      return Utility.getUserName(DEFAULT_USER_NAME);

		  return "";
    }

    public void setProjectName(String projectName){
		  //Get rid of dubious characters
		  projectName=filterTheBadGuys(projectName);
		  
		  //Remove spaces and less dubious stuff.
		  projectName=URLDecoder.decode(projectName);
		  projectName=URLEncoder.encode(projectName);

		  this.projectName=projectName;
    }
    
    public String getProjectName() {
		  return projectName;
    }
	 
    public String getMeshResolution() {
		  return meshResolution;
    }
    
    public void setMeshResolution(String meshResolution){
		  this.meshResolution=meshResolution;
    }
    
    public String getGeoFESTServiceUrl() {
		  return geoFESTServiceUrl;
    }
    
    public void setGeoFESTServiceUrl(String geoFESTServiceUrl){
		  this.geoFESTServiceUrl=geoFESTServiceUrl;
    }
    
    public String getFaultDBServiceUrl() {
		  return faultDBServiceUrl;
    }
	 
    public void setFaultDBServiceUrl(String faultDBServiceUrl){
		  this.faultDBServiceUrl=faultDBServiceUrl;
    }

	 public MeshRunBean getProjectMeshRunBean() {
		  return projectMeshRunBean;
	 }

	 public void setProjectMeshRunBean(MeshRunBean projectMeshRunBean) {
		  this.projectMeshRunBean=projectMeshRunBean;
	 }
	 
	 public GFOutputBean getProjectGeoFestOutput(){
		  return projectGeoFestOutput;
	 }

	 public void setProjectGeoFestOutput(GFOutputBean projectGeoFestOutput) {
		  this.projectGeoFestOutput=projectGeoFestOutput;
	 }

	 public String getGeoFESTBaseUrl() {
		  return this.geoFESTBaseUrl;
	 }

	 public void setGeoFESTBaseUrl(String geoFESTBaseUrl){
		  this.geoFESTBaseUrl=geoFESTBaseUrl;
		  System.out.println("GFBaseURL Set:"+this.geoFESTBaseUrl);
	 }

	 /**
	  * Base64 encoded for safe transmission.
	  */ 
	 public String getGeoFESTBaseUrlForJnlp() {
		  geoFESTBaseUrlForJnlp=getBASE64(geoFESTBaseUrl);
		  //System.out.println("geofestbaseurlforjnlp in mbg: "+geoFESTBaseUrlForJnlp);
		  return geoFESTBaseUrlForJnlp;
	 }
	 
	 public List getMeshDataMegaList() {
		  return meshDataMegaList;
	 }

	 public void setMeshDataMegaList(List meshDataMegaList){
		  this.meshDataMegaList=meshDataMegaList;
	 }

	 public void setCurrentGeotransParamsBean(GeotransParamsBean geotransParamsBean){
		  this.currentGeotransParamsBean=geotransParamsBean;
	 }

	 public GeotransParamsBean getCurrentGeotransParamsBean(){
		  return this.currentGeotransParamsBean;
	 }

	 public String getKmlfiles() {
		return kmlfiles;
	 }

	 public void setKmlfiles(String kmlfiles) {
		this.kmlfiles = kmlfiles;
	 }
	  

    //--------------------------------------------------
    // End the accessor method section.
    //--------------------------------------------------

	 /**
	  * These are methods for checking status of the mesh generator.  This depends on the JSF
	  * event system, so we will do the work in a separate method.
	  */
	 public String checkMeshStatus(ActionEvent ev) {
		  String statusString="Unknown";
		  try {
				//Construct the queueName
				MeshDataMegaBean mdmb=(MeshDataMegaBean)getArchivedMeshTable().getRowData();
				statusString=checkTheStatusQueue(mdmb,statusString);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  return statusString;
	 }
	 

	 /**
	  * This method does not depend explicitly on the JSF event system.  Note it depends on several
	  * global session variables: username, queueservice.
	  */
	 protected String checkTheStatusQueue(MeshDataMegaBean mdmb, String statusString) {
		  try {
				String projectName=mdmb.getProjectName();
				String jobUID=mdmb.getJobUIDStamp();
				String queueName=projectName+"."+jobUID;
				
				//DB, get going
				if (db!=null)
					  db.close();
				
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"
									  +getUserName()+"/"+getCodeName()+"/"+projectName+".db");
				
				//Get the client together
				System.out.println("Connecting to "+queueServiceUrl);
				queueService=new QueueServiceServiceLocator().getQueueExec(new URL(queueServiceUrl));
				statusString=queueService.readQueueMessage(queueName);
				
				//Update the status
				System.out.println("Stuff:"+queueName+" "+statusString);
				
				//Drop it back in the DB
				ObjectSet results=db.get(mdmb);
				//Replace the clone with the original
				System.out.println("Has object:"+results.hasNext());
				if(results.hasNext()){
					 mdmb=(MeshDataMegaBean)results.next();
					 mdmb.setMeshStatus(statusString);
					 db.set(mdmb);
					 db.commit();
				}
				
				if (db!=null)
					  db.close();
				
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[checkTheStatusQueue] " + e);
		  }		  
		  
		  return statusString;
	 }

	 public void setArchivedMeshTable(HtmlDataTable archivedMeshTable){
		  this.archivedMeshTable=archivedMeshTable;
	 }
	 
	 public HtmlDataTable getArchivedMeshTable(){
		  //		  System.out.println("Calling htmldatatable object archivedMeshTable");
		  return archivedMeshTable;
	 }

	 public String getQueueServiceUrl(){
		  return queueServiceUrl;
	 }

	 public void setQueueServiceUrl(String queueServiceUrl){
		  this.queueServiceUrl=queueServiceUrl;
	 }

	 public String getGeoFESTGridServiceUrl() {
		  return this.geoFESTGridServiceUrl;
	 }

	 public void setGeoFESTGridServiceUrl(String geoFESTGridServiceUrl){
		  this.geoFESTGridServiceUrl=geoFESTGridServiceUrl;
	 }

	 public String getGridInfoServiceUrl() {
		  return gridInfoServiceUrl;
	 }
	 
	 public void setGridInfoServiceUrl(String gridInfoServiceUrl) {
		  this.gridInfoServiceUrl=gridInfoServiceUrl;
	 }

	 public SelectItem[] getGridHostList() {
		  //Not the most efficient implementation, but we'll worry later.
		  try {
				String[] hosts=getGridInfoService().getHosts();
				gridHostList=new SelectItem[hosts.length];
				for(int i=0;i<hosts.length;i++) {
					 System.out.println("Host: "+hosts[i]);
					 gridHostList[i]=new SelectItem(hosts[i],hosts[i]);
				}
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  return gridHostList;
	 }
	 
	 public void setGridHostList(SelectItem[] gridHostList) {
		  //Need to change to arraycopy.
		  this.gridInfoService=gridInfoService;		  
	 }

	 protected GridInfoService_PortType getGridInfoService() throws Exception {
		  GridInfoService_PortType gridInfoService=
				new GridInfoServiceServiceLocator().getGridInfoService(new URL(gridInfoServiceUrl));
		  
		  return gridInfoService;
	 }
	 
	 public void setGridRefinerHost(String gridRefinerHost){
		  this.gridRefinerHost=gridRefinerHost;
		  System.out.println(this.gridRefinerHost);
	 }

	 public String getGridRefinerHost() {
		  return this.gridRefinerHost;
	 }

	 public void setGridGeoFESTHost(String gridGeoFESTHost){
		  this.gridGeoFESTHost=gridGeoFESTHost;
	 }

	 public String getGridGeoFESTHost() {
		  return this.gridGeoFESTHost;
	 }

	 public String constructForkResource(String provider, String gridHost) 
		  throws Exception {
		  GridInfoService_PortType gridInfoService=
				new GridInfoServiceServiceLocator().getGridInfoService(new URL(gridInfoServiceUrl));
		  String fork=provider+" "+gridInfoService.getForkManager(gridHost);
		  return fork;
	 }

	 public String constructGridResource(String provider, String gridHost) 
		  throws Exception {
		  GridInfoService_PortType gridInfoService=
				new GridInfoServiceServiceLocator().getGridInfoService(new URL(gridInfoServiceUrl));
		  String queue=provider+" "+gridInfoService.getJobManager(gridHost);
		  return queue;
	 }

	 public String constructUserHome(String gridHost) throws Exception {
		  GridInfoService_PortType gridInfoService=
				new GridInfoServiceServiceLocator().getGridInfoService(new URL(gridInfoServiceUrl));
		  return gridInfoService.getHomeDirectory(gridHost);
	 }

	 /**
	  * Note this method depends on externally set parameters for username, projectname, and projectId.
	  */
	 public String queryMeshGeneratorStatus(ActionEvent ev) {
		  String statusString="MeshGen.Unknown";
		  try {
				MeshDataMegaBean mdmb=(MeshDataMegaBean)getArchivedMeshTable().getRowData();
				String projectName=mdmb.getProjectName();
				String jobUID=mdmb.getJobUIDStamp();
				
				//DB, get going
				
				try {
					if (db!=null)
						  db.close();
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"
									  +getUserName()+"/"+getCodeName()+"/"+projectName+".db");		  
				
				
				//Check geofest job.
				initGeofestGridService();
				statusString="MeshGen."+geofestGridService.queryMeshGeneratorStatus(getUserName(),projectName,jobUID);

				//Drop it back in the DB
				ObjectSet results=db.get(mdmb);
				//Replace the clone with the original
				System.out.println("Has object:"+results.hasNext());
				if(results.hasNext()){
					 mdmb=(MeshDataMegaBean)results.next();
					 mdmb.setMeshStatus(statusString);
					 db.set(mdmb);
					 db.commit();
				}
				if (db!=null)
					  db.close();
				
				} catch (Exception e) {			  
					  if (db!=null)
						  db.close();			  
					  System.out.println("[queryMeshGeneratorStatus] " + e);
				  }
				
		  } catch (Exception e) {			  

			  System.out.println("[queryMeshGeneratorStatus] " + e);
		  }
		  
		  
		  System.out.println("Status:"+statusString);
		  
		  return statusString;
	 }

	 public String queryGeoFESTStatus(ActionEvent ev) {
		  String statusString="GeoFEST.Unknown";
		  try {
				MeshDataMegaBean mdmb=(MeshDataMegaBean)getArchivedMeshTable().getRowData();
				String projectName=mdmb.getProjectName();
				String jobUID=mdmb.getJobUIDStamp();

				//DB, get going
				
				if (db!=null)
					  db.close();
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"
									  +getUserName()+"/"+getCodeName()+"/"+projectName+".db");		  

				initGeofestGridService();
				statusString=geofestGridService.queryGeoFESTStatus(getUserName(),projectName,jobUID);
				System.out.println("Status:"+statusString);
				statusString="GeoFEST."+statusString;
				
				//Drop it back in the DB
				ObjectSet results=db.get(mdmb);
				//Replace the clone with the original
				System.out.println("Has object:"+results.hasNext());
				if(results.hasNext()){
					 mdmb=(MeshDataMegaBean)results.next();
					 mdmb.setGeoFestStatus(statusString);
					 db.set(mdmb);
					 db.commit();
				}
				if (db!=null)
					  db.close();
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[queryGeoFESTStatus] " + e);
		  }
		  
		  System.out.println("Status:"+statusString);
		  return statusString;
	 }

	 public String stopMeshGeneratorJob(ActionEvent ev) {
		  String stopMeshStatus="Stop Mesh Failed";
		  try {
				MeshDataMegaBean mdmb=(MeshDataMegaBean)getArchivedMeshTable().getRowData();
				String projectName=mdmb.getProjectName();
				String jobUID=mdmb.getJobUIDStamp();
				initGeofestGridService();
				geofestGridService.deleteMeshGeneratorJob(getUserName(),projectName,jobUID);
				
				queryMeshGeneratorStatus(ev);

				stopMeshStatus="Stop Mesh Succeeded";
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
		  }
		  return stopMeshStatus;
	 }

	 public String stopGeoFESTJob(ActionEvent ev) {
		  String stopGeoFestStatus="Stop GeoFEST Failed";
		  try {
				MeshDataMegaBean mdmb=(MeshDataMegaBean)getArchivedMeshTable().getRowData();
				String projectName=mdmb.getProjectName();
				String jobUID=mdmb.getJobUIDStamp();
				initGeofestGridService();
				geofestGridService.deleteGeoFESTJob(getUserName(),projectName,jobUID);
				
				queryGeoFESTStatus(ev);

				stopGeoFestStatus="Stop GeoFEST Succeeded";
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
		  }
		  return stopGeoFestStatus;
	 }

	 public void deleteSessionEntry(ActionEvent ev) {
		  MeshDataMegaBean mdmb=(MeshDataMegaBean)getArchivedMeshTable().getRowData();
		  //First, try to delete the job.
		  try {
				initGeofestGridService();

				
				//Make sure there are no running jobs.  Would be 
				//better to check status first but this is OK for now. 
				geofestGridService.deleteMeshGeneratorJob(getUserName(),
																		mdmb.getProjectName(),
																		mdmb.getJobUIDStamp());
				geofestGridService.deleteGeoFESTJob(getUserName(),
																mdmb.getProjectName(),
																mdmb.getJobUIDStamp());
		  }
		  catch (Exception ex) {
				System.out.println(ex.getMessage());
		  }

		  try {
				//DB, get going
			  if (db!=null)
				  db.close();
			  
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"
									  +getUserName()+"/"+getCodeName()+"/"+mdmb.getProjectName()+".db");

				ObjectSet results=db.get(mdmb);
				System.out.println("Found deletion candidate object:"+results.hasNext());
				System.out.println("Result set size:"+results.size());
				if(results.hasNext()) {
					 db.delete(results.next());
					 System.out.println("Deleted session object");
				}
				db.commit();
				if (db!=null)
					  db.close();
				
		  } catch (Exception e) {			  
			  if (db!=null)
				  db.close();			  
			  System.out.println("[deleteSessionEntry] " + e);
		  }
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
	 
	 String mapFaultName;
	 public void setMapFaultName(String mapFaultName) {
		  this.mapFaultName=mapFaultName;
	 }

	 public String getMapFaultName() {
		  return this.mapFaultName;
	 }
	 
	 public boolean getRenderFaultMap(){
		  return this.renderFaultMap;
	 }

	 public void setRenderFaultMap(boolean renderFaultMap){
		  this.renderFaultMap=renderFaultMap;
	 }

	 //--------------------------------------------------


	 
}
