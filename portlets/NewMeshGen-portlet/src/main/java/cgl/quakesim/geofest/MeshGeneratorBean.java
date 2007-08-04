package cgl.quakesim.geofest;

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
import WebFlowClient.ViscoViz.MyVTKServicePortType;
import WebFlowClient.ViscoViz.MyVTKServiceLocator;
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
    long EditProjectTableColumns = 1;    
    Layer currentLayer = new Layer();    
    Fault currentFault = new Fault();    
	 //    GeotransParamsData currentGeotransParamsData = new GeotransParamsData();    
	 GeotransParamsBean currentGeotransParamsBean = new GeotransParamsBean();    

	 //Need to go through and simplify these.
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

	 //These are used to store the actual layers and faults
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
	 UIData myMeshDataTable, myMeshDataTable2;
    String forSearchStr = new String();    
    String faultLatStart = new String();    
    String faultLatEnd = new String();    
    String faultLonStart = new String();    
    String faultLonEnd = new String();    
    GeoFESTUtils gfutils = new GeoFESTUtils();
    String projectDir = new String();

    MeshViewer myMeshViewer; 

    String mesh_gen_viz_fileServiceUrl = 
		  "http://gf2.ucs.indiana.edu:6060/jetspeed/services/FileService";
    String mesh_gen_viz_base_dir = new String("/home/gateway/yan_offscreen/offscreen/");
    String myLayersParamForJnlp = new String("");
    String myFaultsParamForJnlp = new String("");
    String workDirForJnlp = new String("");
    String projectNameForJnlp = new String("");
    String fsURLForJnlp = new String("");
    String jobToken="";
    String userName=getUserName();
    String projectName="";
    String meshResolution="rare";

    //These should be populated from faces-config.xml
    String meshViewerServerUrl="http://gf2.ucs.indiana.edu:18084";
    String faultDBServiceUrl="http://gf2.ucs.indiana.edu:9090/axis/services/Select";
	 String geoFESTBaseUrl="http://gf19.ucs.indiana.edu:8080/geofestexec/";
    String geoFESTServiceUrl=geoFESTBaseUrl+"/"+"services/GeoFESTExec";
	 String geoFESTBaseUrlForJnlp=getGeoFESTBaseUrlForJnlp();

    //This is our geofest service stub.
    GeoFESTService geofestService;

	 //This is the db4o database
	 ObjectContainer db=null;
	 static final String DB_FILE_NAME="meshgen.db";

	 //Useful for manipulating fetchmesh data tables.
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

    /**
     * The client constructor.
     */
    public MeshGeneratorBean() throws Exception {
		  super();

		  gfutils.initLayerInteger();
		  cm = getContextManagerImp();
		  myMeshViewer = new MeshViewer(meshViewerServerUrl);

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

	 protected void makeProjectDirectory() {
		  File projectDir=new File(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/");
		  projectDir.mkdirs();
	 }
	 
	 protected Fault[] getFaultsFromDB(){
		  Fault[] returnFaults=null;
 		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
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

	 protected Layer[] getLayersFromDB(){
		  Layer[] returnLayers=null;
 		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
		  Layer layerToGet=new Layer();
		  ObjectSet results=db.get(layerToGet);
		  if(results.hasNext()) {
				returnLayers=new Layer[results.size()];
				for(int i=0;i<results.size();i++){
					 returnLayers[i]=(Layer)results.next();
				}
		  }
		  db.close();

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
	 
	 protected void storeMeshRunInContext(String userName,
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
		  mega.setGeoFESTBaseUrlForJnlp(getGeoFESTBaseUrlForJnlp());
 		  mega.setJnlpLayers(getMyLayersParamForJnlp(null, projectName));
 		  mega.setJnlpFaults(getMyFaultsParamForJnlp(null,projectName));
		  mega.setCreationDate(new Date().toString());
						  
		  //Set up the database.  This open/close routine may need to be improved later.
		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");	 
		  db.set(mega);
		  db.commit();
		  db.close(); 
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
		  projectMeshRunBean=geofestService.runBlockingMeshGenerator(userName,
																						 projectName,
																						 faults,
																						 layers,
																						 meshResolution);
		  setJobToken(projectMeshRunBean.getJobUIDStamp());
		  storeMeshRunInContext(userName,
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

		  Layer[] layers=getLayersFromDB();
		  Fault[] faults=getFaultsFromDB();

		  initGeofestService();		  
		  projectMeshRunBean=geofestService.runNonBlockingMeshGenerator(userName,
																							 projectName,
																							 faults,
																							 layers,
																							 meshResolution);
		  setJobToken(projectMeshRunBean.getJobUIDStamp());
		  storeMeshRunInContext(userName,
										projectName, 
										projectMeshRunBean.getJobUIDStamp(),
										projectMeshRunBean);
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

		  initGeofestService();
		  projectGeoFestOutput=geofestService.runGeoFEST(userName,
																		 projectName,
																		 currentGeotransParamsBean,
																		 tokenName);

		  System.out.println(projectGeoFestOutput.getCghistUrl());
		  System.out.println(projectGeoFestOutput.getIndexUrl());
		  saveGeotransParamsToDB(userName, 
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
														GFOutputBean projectGeoFestOtput) {

		  //Set up the bean template for searching.
		  MeshDataMegaBean mega=new MeshDataMegaBean();
		  mega.setUserName(userName);
		  mega.setProjectName(projectName);
		  mega.setJobUIDStamp(tokenName);
		  //Find the matching bean
		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");
		  ObjectSet results=db.get(mega);
		  System.out.println("Megabean to update found? "+results.size());
		  System.out.println("Saving Geofest cghist url:"+projectGeoFestOutput.getCghistUrl());
		  System.out.println("Saving index url:"+projectGeoFestOutput.getIndexUrl());
		  if(results.hasNext()) {
				//Reassign the bean.  Should only be one match.
				mega=(MeshDataMegaBean)results.next();
				//Update the geotrans params
				mega.setGeotransParamsBean(currentGeotransParamsBean);
				mega.setGeofestOutputBean(projectGeoFestOutput);
				db.set(mega);
				db.commit();
				System.out.println("Mega Saving Geofest cghist url:"+mega.getGeofestOutputBean().getCghistUrl());
				System.out.println("Mega Saving index url:"+mega.getGeofestOutputBean().getIndexUrl());

		  }
		  db.close();
		  
	 }
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
				// System.out.println(latEnd);
				// System.out.println(latStart);
				// System.out.println(lonStart);
				// System.out.println(lonEnd);
				
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
				tmp_fault.setFaultName (theFault);
				tmp_fault.setFaultLocationX("0.0");
				tmp_fault.setFaultLocationY("0.0");
				tmp_fault.setFaultLength(length);
				tmp_fault.setFaultWidth(width);
				tmp_fault.setFaultDepth(depth);
				tmp_fault.setFaultDipAngle(dip);
				tmp_fault.setFaultStrikeAngle(strike);
				tmp_fault.setFaultSlip("");
				tmp_fault.setFaultRakeAngle("");
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
    }

	 /**
	  * Select Mesh for Geofest run.
	  */
	 public String selectMeshForGeoFEST(ActionEvent ev) {
		  //load the mesh into memory.
		  //Default will be an empty bean
		  
		  //Recover the mega bean.
		  MeshDataMegaBean mega=(MeshDataMegaBean)myMeshDataTable.getRowData();
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
		  if (projectSelectionCode.equals("CreateNewFault")) {
				currentFault=new Fault();
				renderCreateNewFaultForm = !renderCreateNewFaultForm;
		  }
		  if (projectSelectionCode.equals("AddLayerFromDB")) {
				QueryLayersList();
				renderAddLayerFromDBForm = !renderAddLayerFromDBForm;
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
		 
 		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  

		  Layer layerToGet=new Layer();
		  layerToGet.setLayerName(tmp_layerName);
		  ObjectSet results=db.get(layerToGet);
		  //Should only have one value.
		  Layer currentLayer=null;
		  if(results.hasNext()){
				currentLayer=(Layer)results.next();
		  }
		  db.close();
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
					 db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
					 //First, we have to query for the matching layer.
					 Layer todelete=new Layer();
					 todelete.setLayerName(tmp_layerName);
					 ObjectSet result1=db.get(todelete);
					 if(result1.hasNext()) {
						  todelete=(Layer)result1.next();
						  //Now that we have the specific object, we can delete it.
						  db.delete(todelete);
					 }
					 db.close();

					 if(myLayerCollection.size()>iSelectLayer) {					 
						  myLayerCollection.remove(iSelectLayer);
					 }
					 String tmp = projectFullName + SEPARATOR + LAYERS + SEPARATOR
						  + tmp_layerName;
					 cm.removeContext(tmp);

					 
					 gfutils.updateGroupFile(cm, projectFullName, projectName);
					 try {
						  MyVTKServiceLocator service = new MyVTKServiceLocator();
						  MyVTKServicePortType meshserv = service
								.getMyVTKService(new URL(meshViewerServerUrl));
						  meshserv.removeLayer(tmp_layerName);
					 } catch (Exception ex) {
						  ;
					 }
					 
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

		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
		  Fault faultToGet=new Fault();
		  faultToGet.setFaultName(tmp_faultName);
		  ObjectSet results=db.get(faultToGet);
		  //Should only have one value.
		  Fault currentFault=null;
		  if(results.hasNext()){
				currentFault=(Fault)results.next();
		  }
		  db.close();
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
					 db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
				 
					 Fault todelete=new Fault();
					 todelete.setFaultName(tmp_faultName);
					 ObjectSet result=db.get(todelete);
					 if(result.hasNext()) {
						  todelete=(Fault)result.next();
						  db.delete(todelete);
					 }
					 db.close(); 

					 String tmp = projectFullName + SEPARATOR + FAULTS + SEPARATOR
						  + tmp_faultName;
					 cm.removeContext(tmp);
					 gfutils.updateGroupFile(cm, projectFullName, projectName);
					 try {
						  MyVTKServiceLocator service = new MyVTKServiceLocator();
						  MyVTKServicePortType meshserv = service
								.getMyVTKService(new URL(meshViewerServerUrl));
						  meshserv.removeFault(tmp_faultName);
					 } catch (Exception ex) {
						  ;
					 }
				}
				
		  } catch (Exception e) {
				e.printStackTrace();
		  }
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
		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");	
				 
		  Layer tmpLayer=new Layer();
		  tmpLayer.setLayerName(currentLayer.getLayerName());
		  ObjectSet result=db.get(tmpLayer);
		  if(result.hasNext()){
				tmpLayer=(Layer)result.next();
				db.delete(tmpLayer);
		  }
		  db.set(currentLayer);
		  db.commit();
		  db.close();

    }
    
    public void toggleAddFaultForProject(ActionEvent ev) throws Exception {
		  initEditFormsSelection();
		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");
				 
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
		  isInitialized = getIsInitialized();
		  if (!isInitialized) {
				initWebServices();
		  }
		  setContextList();
		  makeProjectDirectory();
		  return ("MG-new-project");
    }
    
    public void init_edit_project() {
		  initEditFormsSelection();
		  projectSelectionCode = "";
		  faultSelectionCode = "";
    }
    

    public String NewProjectThenEditProject() throws Exception {
		  if (!isInitialized) {
				initWebServices();
		  }
		  setContextList();
		  setProjectname();
		  init_edit_project();
		  return "MG-edit-project";
    }
    
    public String toggleSelectProject() throws Exception  {
		  if (!isInitialized) {
				initWebServices();
		  }
		  try {
				setContextList();
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
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
		  if (!isInitialized) {
				initWebServices();
		  }
		  try {
				setContextList();
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  try {
				if (deleteProjectsList != null) {
					 for (int i = 0; i < deleteProjectsList.length; i++) {
						  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+".db");
						  ProjectBean delproj=new ProjectBean();
						  ObjectSet results=db.get(delproj);
						  if(results.hasNext()){
								delproj=(ProjectBean)results.next();
								db.delete(delproj);
						  }
						  db.close();
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
 		  db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+".db");		  		  
		  ProjectBean project=new ProjectBean();
		  project.setProjectName(projectName);
		  db.set(project);
		  db.commit();
		  db.close();

		  return "MG-set-project";
    }
    
    public String SetAndViewMeshImage() throws Exception {
		  myMeshViewer.reset();
		  myMeshViewer.setServiceUrl(this.meshViewerServerUrl);
		  myMeshViewer.fileServiceUrl = fileServiceUrl;
		  myMeshViewer.projectName = projectName;
		  myMeshViewer.workDir = baseWorkDir + "/" + userName + "/" + projectName
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
		  if (!isInitialized) {
				initWebServices();
		  }
		  setContextList();
		  return ("rdahmm-load-data-archive");
    }
    
    public String loadProjectList() throws Exception {
		  if (!isInitialized) {
				initWebServices();
		  }
		  setContextList();
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
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");
				 
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
				db.close(); 

		  } catch (Exception ex) {
				ex.printStackTrace();
				db.close();
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
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
				 
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
				db.close();
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				db.close();
		  }
		  return this.myLayerEntryForProjectList;
    }
   
     public String loadMesh() throws Exception {
		  if (!isInitialized) {
				initWebServices();
		  }
		  setContextList();
		  
		  return ("MG-load-mesh");
    }
    
    public String fetchMesh() throws Exception {
		  if (!isInitialized) {
				initWebServices();
		  }
		  setContextList();
		  this.ListGeoFESTData = false;

		  return ("MG-fetch-mesh");
    }
    
    
    public void StageGeotransFile() throws Exception {
    }
    
    public String gfarchivedData() throws Exception {
		  if (!isInitialized) {
				initWebServices();
		  }
		  setContextList();
		  this.ListGeoFESTData = true;
		  return ("MG-gf-archived-data");
    }
    
    public String gfGraphOutput() throws Exception {
		  //System.out.println("gf Graph Output");
		  if (!isInitialized) {
				initWebServices();
		  }
		  setContextList();
		  this.ListGeoFESTData = true;
		  return ("MG-gf-graph-output");
    }
    
    public String ContourPlot() throws Exception {
		  //System.out.println("Contour Plot");
		  if (!isInitialized) {
				initWebServices();
		  }
		  setContextList();
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
				db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+".db");		  
				ProjectBean project=new ProjectBean();
				ObjectSet results=db.get(ProjectBean.class);
				//System.out.println("Got results:"+results.size());
				while(results.hasNext()) {
					 project=(ProjectBean)results.next();
					 //System.out.println(project.getProjectName());
					 myProjectNameList.add(new SelectItem(project.getProjectName(),
																	  project.getProjectName()));
				}
				db.close();
				
		  } catch (Exception ex) {
				//ex.printStackTrace();
				System.err.println("Could not open "+getBasePath()+"/"+getContextBasePath()
										 +"/"+userName+"/"+codeName+".db");		
				System.err.println("Returning empty list.");  
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
	  * A muscular getter, this reconstructs the list of MeshRunBean instances from the context.
	  * Note it depends on several class-scoped variables.
	  */
	 public List getMyArchivedMeshRunList() throws Exception {
		  List myprojectlist=getMyProjectNameList();

		  myArchivedMeshRunList.clear();
		  
		  //System.out.println("Creating archived mesh list");

		  //		  String[] tmp_contextlist = cm.listContext(codeName);
		  if (myprojectlist.size() > 0) {
				for(int i=0;i<myprojectlist.size();i++){
					 String projectName=((SelectItem)myprojectlist.get(i)).getLabel();

					 MeshDataMegaBean mega=new MeshDataMegaBean();
					 mega.setProjectName(projectName);
					 //System.out.println("ProjectName: "+projectName);
					 db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");
					 //					 ObjectSet results=db.get(mega);
					 ObjectSet results=db.get(MeshDataMegaBean.class);
					 //System.out.println("Matches for "+projectName+":"+results.size());
					 while(results.hasNext()){						  
						  mega=(MeshDataMegaBean)results.next();
						  myArchivedMeshRunList.add(mega);
					 }
					 db.close();
				}
		  }
		  return this.myArchivedMeshRunList;
    }

	 public List getMyArchivedMeshRunList2() throws Exception {
		  List myprojectlist=getMyProjectNameList();

		  myArchivedMeshRunList2.clear();
		  
		  if (myprojectlist.size() > 0) {
				for(int i=0;i<myprojectlist.size();i++){
					 //					 MeshRunBean mrb=new MeshRunBean();
					 MeshDataMegaBean mega=new MeshDataMegaBean();
					 String projectName=((SelectItem)myprojectlist.get(i)).getLabel();
					 
					 db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");
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
					 db.close();
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
				boolean close=false;
				if(db==null) {
					 db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");
					 close=true;
				}
				 
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
				if(close) {
					 db.close();
				}
	
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
				boolean close=false;
				if(db==null) {
					 db=Db4o.openFile(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+"/"+projectName+".db");		  
					 close=true;
				}

				Layer layerToGet=new Layer();
				ObjectSet results=db.get(layerToGet);
				if(results.hasNext()) {
					 for(int i=0;i<results.size();i++){
						  layerToGet=(Layer)results.next();
						  String layerDef = "addLayer" + space;
						  layerDef+=layerToGet.getLayerName()+space;
						  layerDef += layerToGet.getLayerOriginX()+space;
						  layerDef +=layerToGet.getLayerOriginY() + space
;						  layerDef +=layerToGet.getLayerOriginZ() + space;
						  layerDef +=layerToGet.getLayerLength() + space;
						  layerDef +=layerToGet.getLayerWidth() + space;
						  layerDef +=layerToGet.getLayerDepth() + space;

						  //System.out.println("Layers!" + layerDef);
						  tmp_layers = tmp_layers + "*" + layerDef;
					 }
				}

				if(close) db.close();
	
		  } catch (Exception ex) {
				ex.printStackTrace();
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
		  String userName=Utility.getUserName(DEFAULT_USER_NAME);
		  return userName;
    }
	 
    public void setProjectName(String projectName){
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
		  return geoFESTBaseUrl;
	 }

	 public void setGeoFESTBaseUrl(){
		  this.geoFESTBaseUrl=geoFESTBaseUrl;
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

    //--------------------------------------------------
    // End the accessor method section.
    //--------------------------------------------------

	 public String checkMeshStatus(ActionEvent ev) {
		  return "";
	 }

}
