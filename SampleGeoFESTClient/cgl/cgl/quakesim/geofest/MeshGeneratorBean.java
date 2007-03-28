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

//Import the project utility class.
import org.servogrid.genericproject.Utility;

/**
 * Everything you need to set up and run MeshGenerator.
 */

public class MeshGeneratorBean extends GenericSopacBean {
    
    // MeshGenerator Bean stuff
    protected String codeName = "MeshGenerator";
    static final String MESH_GENERATION_NAV_STRING="mesh_generation_running";
    static final String GEOFEST_EXECUTION_LAUNCHED="geofest_execution_launched";
    static final String DEFAULT_USER_NAME="geofest_default_user";
    
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
    GeotransParamsData currentGeotransParamsData = new GeotransParamsData();    
    List myFaultDBEntryList = new ArrayList();    
    List myLayerDBEntryList = new ArrayList();    
    List myFaultEntryForProjectList = new ArrayList();    
    List myLayerEntryForProjectList = new ArrayList();    
    List myFaultsForProject = new ArrayList();    
    List myProjectNameList = new ArrayList();    
    List myLoadMeshTableEntryList = new ArrayList();    
    List myarchivedMeshTableEntryList = new ArrayList();    

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
    String userName="";
    String projectName="";
    String meshResolution="rare";

    //These should be populated from faces-config.xml
    String meshViewerServerUrl="http://gf2.ucs.indiana.edu:18084";
    String geoFESTServiceUrl="http://gf19.ucs.indiana.edu:8080/geofestexec/services/GeoFESTExec";
    String faultDBServiceUrl="http://gf2.ucs.indiana.edu:9090/axis/services/Select";
    
    //This is our geofest service stub.
    GeoFESTService geofestService;

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
		  
		  geofestService=
				new GeoFESTServiceServiceLocator().getGeoFESTExec(new URL(geoFESTServiceUrl));
		  
		  myMeshViewer = new MeshViewer(meshViewerServerUrl);
		  System.out.println("MeshGenerator Bean Created");
		  
    }
	 
    //--------------------------------------------------
    // This section contains the main execution calls.
    //--------------------------------------------------

    /**
     * Protected convenience method. 
     */ 
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
	 
    /**
     * This is a JSF compatible method for running the mesh generator
     * in blocking mode.  That is, it takes no argument and assumes
     * the values have been set by accessors.
     */ 
    public String runBlockingMeshGeneratorJSF() 
		  throws Exception {
		  
		  Layer[] layers=convertArrayListToLayerArray(myLayerCollection);
		  Fault[] faults=convertArrayListToFaultArray(myFaultCollection);
		  
		  projectMeshRunBean=geofestService.runBlockingMeshGenerator(userName,
																						 projectName,
																						 faults,
																						 layers,
																						 meshResolution);
		  setJobToken(projectMeshRunBean.getJobUIDStamp());
		  //		  meshRunArrayList.add(projectMeshRunBean);
		  return MESH_GENERATION_NAV_STRING;
    }
    
    /**
     * This runs the mesh generator in non-blocking mode, so it returns
     * immediately.  The client must determine separately if the mesh
     * generation has finished.
     */ 
    public String runNonBlockingMeshGenerartorJSF() 
		  throws Exception {
		  Layer[] layers=convertArrayListToLayerArray(myLayerCollection);
		  Fault[] faults=convertArrayListToFaultArray(myFaultCollection);
		  
		  projectMeshRunBean=geofestService.runNonBlockingMeshGenerator(userName,
																							 projectName,
																							 faults,
																							 layers,
																							 meshResolution);
		  setJobToken(projectMeshRunBean.getJobUIDStamp());
		  return MESH_GENERATION_NAV_STRING;
    }

    /**
     * This is a no-argument method for running geofest that can be called
     * from a JSF pge.
     */
    public String runGeoFESTJSF()
		  throws Exception {
		  //Temporary, need to fix.
		  GeotransParamsBean geotransparamsbean=new GeotransParamsBean();
		  
		  String tokenName=getJobToken();
		  projectGeoFestOutput=geofestService.runGeoFEST(userName,
																		 projectName,
																		 geotransparamsbean,
																		 tokenName);
		  return GEOFEST_EXECUTION_LAUNCHED;
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
    
    public void toggleUpdateLayerProjectEntry(ActionEvent ev) {
		  String layerStatus = "Update";
		  try {
				int iSelectLayer=-1;
				// Catch the MyData item during the third phase of the JSF
				// lifecycle.
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
				if ((tmp_view == true) && (tmp_update == false)) {
					 currentLayer=(Layer)myLayerCollection.get(iSelectLayer);
					 currentLayer.setLayerName(tmp_layerName);
					 String thename = projectFullName + SEPARATOR + LAYERS;
					 
					 String fullname = thename + SEPARATOR + currentLayer.getLayerName();
					 
					 currentLayer.setLayerOriginX(setValue(cm, layerStatus, fullname,
																		"layerOriginX"));
					 currentLayer.setLayerOriginY(setValue(cm, layerStatus, fullname,
																		"layerOriginY"));
					 currentLayer.setLayerOriginZ(setValue(cm, layerStatus, fullname,
																		"layerOriginZ"));
					 currentLayer.setLayerLatOrigin(setValue(cm, layerStatus,
																		  fullname, "layerLatOrigin"));
					 currentLayer.setLayerLonOrigin(setValue(cm, layerStatus,
																		  fullname, "layerLonOrigin"));
					 currentLayer.setLayerLength(setValue(cm, layerStatus, fullname,
																	  "layerLength"));
					 currentLayer.setLayerWidth(setValue(cm, layerStatus, fullname,
																	 "layerWidth"));
					 currentLayer.setLayerDepth(setValue(cm, layerStatus, fullname,
																	 "layerDepth"));
					 currentLayer.setLameLambda(setValue(cm, layerStatus, fullname,
																	 "lameLambda"));
					 currentLayer.setLameMu(setValue(cm, layerStatus, fullname,
																"lameMu"));
					 currentLayer.setViscosity(setValue(cm, layerStatus, fullname,
																	"viscosity"));
					 currentLayer.setExponent(setValue(cm, layerStatus, fullname,
																  "exponent"));
					 renderCreateNewLayerForm = !renderCreateNewLayerForm;
					 
				}
				if ((tmp_update == true) && (tmp_view == false)) {
					 
					 myLayerCollection.remove(iSelectLayer);
					 
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
    
    public void toggleUpdateFaultProjectEntry(ActionEvent ev) {
		  String faultStatus = "Update";
		  try {
				int iSelectFault=-1;

				// Catch the MyData item during the third phase of the JSF
				// lifecycle.
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
				
				String tmp_faultName = tmp_FaultEntryForProject.getFaultName();
				boolean tmp_view = tmp_FaultEntryForProject.getView();
				boolean tmp_update = tmp_FaultEntryForProject.getDelete();
				String projectFullName = codeName + SEPARATOR + projectName;
				
				initEditFormsSelection();
				if ((tmp_view == true) && (tmp_update == true)) {
					 System.out.println("error");
				}
				if ((tmp_view == true) && (tmp_update == false)) {
					 currentFault=(Fault)myFaultCollection.get(iSelectFault);
					 currentFault.setFaultName(tmp_faultName);
					 String thename = projectFullName + SEPARATOR + FAULTS;
					 
					 String fullname = thename + SEPARATOR + currentFault.getFaultName();
					 currentFault.setFaultLocationX(setValue(cm, faultStatus,
																		  fullname, "faultOriginX"));
					 currentFault.setFaultLocationY(setValue(cm, faultStatus,
																		  fullname, "faultOriginY"));
					 currentFault.setFaultLength(setValue(cm, faultStatus, fullname,
																	  "faultLength"));
					 currentFault.setFaultWidth(setValue(cm, faultStatus, fullname,
																	 "faultWidth"));
					 currentFault.setFaultDepth(setValue(cm, faultStatus, fullname,
																	 "faultDepth"));
					 currentFault.setFaultDipAngle(setValue(cm, faultStatus,
																		 fullname, "faultDipAngle"));
					 currentFault.setFaultStrikeAngle(setValue(cm, faultStatus,
																			 fullname, "faultStrikeAngle"));
					 currentFault.setFaultSlip(setValue(cm, faultStatus, fullname,
																	"faultSlip"));
					 currentFault.setFaultRakeAngle(setValue(cm, faultStatus,
																		  fullname, "faultRakeAngle"));
					 renderCreateNewFaultForm = !renderCreateNewFaultForm;
					 
				}
				if ((tmp_update == true) && (tmp_view == false)) {
					 myFaultCollection.remove(iSelectFault);

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
    
    //Method needs to be either re-writteb or deleted.
    public void toggleRefineMesh(ActionEvent ev) {
    }

    //Probably need to remove this one.
    public void toggleViewMeshMessages(ActionEvent ev) {
    }
    
    public void toggleAddLayerForProject(ActionEvent ev) {
		  initEditFormsSelection();
		  String projectFullName = codeName + SEPARATOR + projectName;
		  String gcname = currentLayer.getLayerName();
		  
		  // Get rid of spaces.
		  while (gcname.indexOf(" ") > -1) {
				gcname = gcname.substring(0, gcname.indexOf(" "))
					 + "_"
					 + gcname
					 .substring(gcname.indexOf(" ") + 1, gcname.length());
		  }


		  currentLayer.setLayerName(gcname);
		  myLayerCollection.add(currentLayer);

		  //Legacy method that will wrtie to the cm.
		  GeoFESTElement layerelement = new GeoFESTElement();
		  layerelement.addElement("layerName", currentLayer.getLayerName());
		  layerelement.addElement("layerOriginX", currentLayer.getLayerOriginX());
		  layerelement.addElement("layerOriginY", currentLayer.getLayerOriginY());
		  layerelement.addElement("layerOriginZ", currentLayer.getLayerOriginZ());
		  layerelement.addElement("layerLatOrigin", currentLayer.getLayerLatOrigin());
		  layerelement.addElement("layerLonOrigin", currentLayer.getLayerLonOrigin());
		  layerelement.addElement("layerLength", currentLayer.getLayerLength());
		  layerelement.addElement("layerWidth", currentLayer.getLayerWidth());
		  layerelement.addElement("layerDepth", currentLayer.getLayerDepth());
		  layerelement.addElement("lameLambda", currentLayer.getLameLambda());
		  layerelement.addElement("lameMu", currentLayer.getLameMu());
		  layerelement.addElement("viscosity", currentLayer.getViscosity());
		  layerelement.addElement("exponent", currentLayer.getExponent());
		  
		  try {
				gfutils.setContextProperties(cm, projectFullName, LAYERS, gcname,
													  layerelement);
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  layerelement.reset();
		  
		  // --------------------------------------------------
		  // Fetch the mesh service client.
		  // --------------------------------------------------
		  try {
				// -----------------------------------------------------------
				// add Layer to VTK
				// -----------------------------------------------------------
				float X = Float.parseFloat(currentLayer.getLayerOriginX());
				float Y = Float.parseFloat(currentLayer.getLayerOriginY());
				float Z = Float.parseFloat(currentLayer.getLayerOriginZ());
				float Length = Float.parseFloat(currentLayer.getLayerLength());
				float Width = Float.parseFloat(currentLayer.getLayerWidth());
				float Depth = Float.parseFloat(currentLayer.getLayerDepth());
				MyVTKServiceLocator tmp_service = new MyVTKServiceLocator();
				MyVTKServicePortType meshserv = tmp_service
					 .getMyVTKService(new URL(meshViewerServerUrl));
				meshserv.addLayer(gcname, X, Y, Z, Length, Width, Depth);
		  } catch (Exception ex) {
		  }
    }
    
    public void toggleAddFaultForProject(ActionEvent ev) {
		  initEditFormsSelection();
		  String projectFullName = codeName + SEPARATOR + projectName;
		  String gcname = currentFault.getFaultName();
		  
		  // Get rid of spaces.
		  while (gcname.indexOf(" ") > -1) {
				gcname = gcname.substring(0, gcname.indexOf(" "))
					 + "_"
					 + gcname
					 .substring(gcname.indexOf(" ") + 1, gcname.length());
		  }
		  myFaultCollection.add(currentFault);
		  GeoFESTElement faultelement = new GeoFESTElement();
		  faultelement.addElement("faultName", currentFault.getFaultName());
		  faultelement.addElement("faultOriginX", currentFault.getFaultLocationX());
		  faultelement.addElement("faultOriginY", currentFault.getFaultLocationY());
		  faultelement.addElement("faultLength", currentFault.getFaultLength());
		  faultelement.addElement("faultWidth", currentFault.getFaultWidth());
		  faultelement.addElement("faultDepth", currentFault.getFaultDepth());
		  faultelement.addElement("faultDipAngle", currentFault.getFaultDipAngle());
		  faultelement.addElement("faultStrikeAngle",
										  currentFault.getFaultStrikeAngle());
		  faultelement.addElement("faultSlip", currentFault.getFaultSlip());
		  faultelement.addElement("faultRakeAngle", currentFault.getFaultRakeAngle());
		  
		  faultelement.addElement("faultOriginZ", "0.0");
		  faultelement.addElement("faultNumber", "1.0");
		  
		  try {
				gfutils.setContextProperties(cm, projectFullName, FAULTS, gcname,
													  faultelement);	    
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  faultelement.reset();
		  
		  // -----------------------------------------------------------
		  // add Fault to mesh image.
		  // -----------------------------------------------------------
		  try {
				float X = Float.parseFloat(currentFault.getFaultLocationX());
				float Y = Float.parseFloat(currentFault.getFaultLocationY());
				float Width = Float.parseFloat(currentFault.getFaultWidth());
				float Depth = Float.parseFloat(currentFault.getFaultDepth());
				float Length = Float.parseFloat(currentFault.getFaultLength());
				float Dip = (float) Double.parseDouble(currentFault.getFaultDipAngle());
				float Strike = (float) Double
					 .parseDouble(currentFault.getFaultStrikeAngle());
				MyVTKServiceLocator service = new MyVTKServiceLocator();
				MyVTKServicePortType meshserv = service.getMyVTKService(new URL(
																									 meshViewerServerUrl));
				meshserv.addFault(gcname, X, Y, Length, Width, Depth, Dip, Strike);
		  } catch (Exception ex) {
				ex.printStackTrace();
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
		  isInitialized = getIsInitialized();
		  if (!isInitialized) {
				initWebServices();
		  }
		  setContextList();
		  return ("MG-new-project");
    }
    
    public String SaveMeshMetaData() throws Exception {
		  String projectFullName = codeName + SEPARATOR + projectName;
		  cm.setCurrentProperty(projectFullName, "MeshArchived", "true");
		  cm.setCurrentProperty(projectFullName, "hostName", hostName);
		  cm.setCurrentProperty(projectFullName, "workDir", baseWorkDir + "/"
										+ userName + "/" + projectName + "/");
		  
		  String UserMsg = "Index, tetra, and node files were saved";
		  if (this.statusGeoFEST != true) {
				return ("MG-back");
		  } else {
				currentGeotransParamsData.reset(this.projectName);
				return ("MG-geotrans-params");
				
		  }
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
    
    public String toggleSelectProject() {
		  if (!isInitialized) {
				initWebServices();
		  }
		  try {
				setContextList();
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
		  initEditFormsSelection();
		  if (selectProjectsList != null) {
				for (int i = 0; i < 1; i++) {
					 this.projectName = selectProjectsList[0];
				}
		  }
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
						  cm.removeContext(codeName + File.separator
												 + deleteProjectsList[i]);
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
	
	// Store the request values persistently
	contextName = codeName + "/" + projectName;
	cm.addContext(contextName);
	cm.addContext(contextName + "/" + "Layers");
	cm.addContext(contextName + "/" + "Faults");
	cm.setCurrentProperty(contextName, "Name", projectName);
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
	System.out.println("Loading project");
	if (!isInitialized) {
	    initWebServices();
	}
	setContextList();
	return ("rdahmm-load-data-archive");
    }
    
    public String loadProject() throws Exception {
	System.out.println("Loading project");
	if (!isInitialized) {
	    initWebServices();
	}
	setContextList();
	return ("MG-list-project");
    }
        
    public String loadMesh() throws Exception {
	System.out.println("Loading Mesh");
	if (!isInitialized) {
	    initWebServices();
	}
	setContextList();
	
	return ("MG-load-mesh");
    }
    
    public String fetchMesh() throws Exception {
	System.out.println("Fetching Mesh");
	if (!isInitialized) {
	    initWebServices();
	}
	setContextList();
	this.ListGeoFESTData = false;
	return ("MG-fetch-mesh");
    }
    
    public String gfProject() throws Exception {
	System.out.println("GeoFest2 main page");
	if (!isInitialized) {
	    initWebServices();
	}
	
	return ("MG-gf-project");
    }
    
    public String runGeoFEST() throws Exception {
	System.out.println("GeoFest2 main page");
	if (!isInitialized) {
	    initWebServices();
	}
	this.statusGeoFEST = true;
	return ("MG-back");
    }
    
    public String GeoFEST_Full_Run() throws Exception {
	System.out.println("GeoFEST_Full_Run main page");
	if (!isInitialized) {
	    initWebServices();
	}
	this.currentGeotransParamsData.run_choice = "GeoFEST_Full_Run";
	StageGeotransFile();
	return ("MG-back");
    }
    
    public String GeoFEST_Dry_Run() throws Exception {
	System.out.println("GeoFEST_Dry_Run main page");
	if (!isInitialized) {
	    initWebServices();
	}
	this.currentGeotransParamsData.run_choice = "GeoFEST_Dry_Run";
	StageGeotransFile();
	return ("MG-back");
    }
    
    protected static String getRealPath() {
	String path = ".";
	try {
	    path = FacesContext.getCurrentInstance().getApplication()
		.getClass().getResource("/")
		+ "../../meshdownloads/";
	    path = path.substring(5);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return path;
    }
    
    protected static String getContextPath() {
	String path = ".";
	try {
	    path = FacesContext.getCurrentInstance().getExternalContext()
		.getRequestContextPath()
		+ "/meshdownloads/";
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return path;
    }
    
    public void StageGeotransFile() throws Exception {
    }
    
    public String gfarchivedData() throws Exception {
	System.out.println("gf archived data");
	if (!isInitialized) {
	    initWebServices();
	}
	setContextList();
	this.ListGeoFESTData = true;
	return ("MG-gf-archived-data");
    }
    
    public String gfGraphOutput() throws Exception {
	System.out.println("gf Graph Output");
	if (!isInitialized) {
	    initWebServices();
	}
	setContextList();
	this.ListGeoFESTData = true;
	return ("MG-gf-graph-output");
    }
    
    public String ContourPlot() throws Exception {
	System.out.println("Contour Plot");
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
    
    
    public void setCurrentGeotransParamsData(
					     GeotransParamsData tmp_GeotransParamsData) {
	this.currentGeotransParamsData = tmp_GeotransParamsData;
    }
    
    public GeotransParamsData getCurrentGeotransParamsData() {
	return this.currentGeotransParamsData;
    }
    
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
    
    public String[] getMyProjectMeshHostArray() {
	try {
	    String[] tmp_contextlist = cm.listContext(codeName);
	    if (tmp_contextlist.length > 0) {
		for (int i = 0; i < tmp_contextlist.length; i++) {
		    myProjectMeshHostArray[i] = cm.getCurrentProperty(codeName
								      + "/" + tmp_contextlist[i], "hostName");
		}
	    }
	    
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return this.myProjectMeshHostArray;
	
    }
    
    public String[] getMyProjectCreationDateArray() {
	try {
	    String[] tmp_contextlist = cm.listContext(codeName);
	    if (tmp_contextlist.length > 0) {
		for (int i = 0; i < tmp_contextlist.length; i++) {
		    myProjectCreationDateArray[i] = (new Date(Long.parseLong(cm
									     .getCurrentProperty(codeName + "/"
												 + tmp_contextlist[i], "LastTime"))))
			.toString();
		}
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return this.myProjectCreationDateArray;
    }
    
    public void setMyProjectNameList(List tmp_str) {
	this.myProjectNameList = tmp_str;
    }
    
    public List getMyProjectNameList() {
	this.myProjectNameList.clear();
	try {
	    String[] tmp_contextlist = cm.listContext(codeName);
	    if (tmp_contextlist.length > 0) {
		for (int i = 0; i < tmp_contextlist.length; i++) {
		    myProjectNameList.add(new SelectItem(tmp_contextlist[i],
							 tmp_contextlist[i]));
		}
	    }
	    
	} catch (Exception ex) {
	    ex.printStackTrace();
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
    
    public String getFsURLForJnlp() {
	this.fsURLForJnlp = getBASE64(fileServiceUrl);
	return this.fsURLForJnlp;
    }
    
    public String getProjectNameForJnlp() {
	this.projectNameForJnlp = getBASE64(projectName);
	return this.projectNameForJnlp;
    }
    
    public String getWorkDirForJnlp() {
	this.workDirForJnlp = getBASE64(baseWorkDir + "/" + userName + "/"
					+ projectName + "/");
	return this.workDirForJnlp;
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
    
    public List getMyFaultsForProject() {
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
    
    public List getMyarchivedMeshTableEntryList() {
	myarchivedMeshTableEntryList.clear();
	try {
	    String[] tmp_contextlist = cm.listContext(codeName);
	    if (tmp_contextlist.length > 0) {
		for (int i = 0; i < tmp_contextlist.length; i++) {
		    loadMeshTableEntry tmp_loadMeshTableEntry = new loadMeshTableEntry();
		    tmp_loadMeshTableEntry.projectName = tmp_contextlist[i];
		    tmp_loadMeshTableEntry.meshHost = cm.getCurrentProperty(
									    codeName + "/" + tmp_contextlist[i], "hostName");
		    if (tmp_loadMeshTableEntry.meshHost == null) {
			tmp_loadMeshTableEntry.meshHost = "null";
		    }
		    tmp_loadMeshTableEntry.creationDate = (new Date(Long
								    .parseLong(cm.getCurrentProperty(codeName + "/"
												     + tmp_contextlist[i], "LastTime"))))
			.toString();
		    tmp_loadMeshTableEntry.view = false;
		    String archived = "";
		    if (this.ListGeoFESTData == true) {
			archived = cm.getCurrentProperty(codeName + "/"
							 + tmp_contextlist[i], "ListGeoFESTData");
		    } else {
			archived = cm.getCurrentProperty(codeName + "/"
							 + tmp_contextlist[i], "MeshArchived");
		    }
		    if (archived != null && archived.equalsIgnoreCase("true")) {
			myarchivedMeshTableEntryList
			    .add(tmp_loadMeshTableEntry);
		    }
		}
	    }
	    
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return this.myarchivedMeshTableEntryList;
    }
    
    public void setMyLoadMeshTableEntryList(List tmp_str) {
	this.myLoadMeshTableEntryList = tmp_str;
    }
    
    public List getMyLoadMeshTableEntryList() {
	myLoadMeshTableEntryList.clear();
	try {
	    String[] tmp_contextlist = cm.listContext(codeName);
	    if (tmp_contextlist.length > 0) {
		for (int i = 0; i < tmp_contextlist.length; i++) {
		    loadMeshTableEntry tmp_loadMeshTableEntry = new loadMeshTableEntry();
		    tmp_loadMeshTableEntry.projectName = tmp_contextlist[i];
		    tmp_loadMeshTableEntry.meshHost = cm.getCurrentProperty(
									    codeName + "/" + tmp_contextlist[i], "hostName");
		    if (tmp_loadMeshTableEntry.meshHost == null) {
			tmp_loadMeshTableEntry.meshHost = "null";
		    }
		    tmp_loadMeshTableEntry.creationDate = (new Date(Long
								    .parseLong(cm.getCurrentProperty(codeName + "/"
												     + tmp_contextlist[i], "LastTime"))))
			.toString();
		    tmp_loadMeshTableEntry.view = false;
		    myLoadMeshTableEntryList.add(tmp_loadMeshTableEntry);
		}
	    }
	    
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return this.myLoadMeshTableEntryList;
    }
    
    public String cmGetValue(ContextManagerImp cm, String Status, String name,
			     String prop) throws Exception {
	String retval = "";
	if (Status.equals("Update")) {
	    retval = cm.getCurrentProperty(name, prop);
	}
	return retval;
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
	myFaultsParamForJnlp = "";
	// --------------------------------------------------
	// Get set up. Start with useful constants.
	// --------------------------------------------------
	String space = " ";
	String status = "Update";
	
	// --------------------------------------------------
	// Find out who we are.
	// --------------------------------------------------
	String projectFullName = codeName + SEPARATOR + projectName;
	System.out.println("Painter project name: " + projectFullName);
	String tmp_faults = new String("");
	try {
	    
	    // Here we must loop over the number of faults.
	    String thename = projectFullName + SEPARATOR + FAULTS;
	    String[] faults = cm.listContext(thename);
	    if (faults != null && faults.length > 0) {
		for (int i = 0; i < faults.length; i++) {
		    String fullname = thename + SEPARATOR + faults[i];
		    String faultDef = "addFault" + space;
		    faultDef += faults[i] + space;
		    faultDef += cmGetValue(cm, status, fullname, "faultOriginX")
			+ space;
		    faultDef += cmGetValue(cm, status, fullname, "faultOriginY")
			+ space;
		    faultDef += cmGetValue(cm, status, fullname, "faultLength")
			+ space;
		    faultDef += cmGetValue(cm, status, fullname, "faultWidth")
			+ space;
		    faultDef += cmGetValue(cm, status, fullname, "faultDepth")
			+ space;
		    faultDef += cmGetValue(cm, status, fullname,
					   "faultDipAngle")
			+ space;
		    faultDef += cmGetValue(cm, status, fullname,
					   "faultStrikeAngle")
			+ space;
		    System.out.println("Faults!" + faultDef);
		    tmp_faults = tmp_faults + "*" + faultDef;
		    
		}
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
    
    public String getMyLayersParamForJnlp() {
	myLayersParamForJnlp = "";
	// --------------------------------------------------
	// Get set up. Start with useful constants.
	// --------------------------------------------------
	String space = " ";
	String status = "Update";
	
	// --------------------------------------------------
	// Find out who we are.
	// --------------------------------------------------
	String projectFullName = codeName + SEPARATOR + projectName;
	System.out.println("Painter project name: " + projectFullName);
	String tmp_layers = new String("");
	try {
	    // Here we must loop over the number of layers
	    String thename = projectFullName + SEPARATOR + LAYERS;
	    String[] layers = cm.listContext(thename);
	    
	    if (layers != null && layers.length > 0) {
		for (int i = 0; i < layers.length; i++) {
		    String fullname = thename + SEPARATOR + layers[i];
		    String layerDef = "addLayer" + space;
		    layerDef += layers[i] + space;
		    layerDef += cmGetValue(cm, status, fullname, "layerOriginX")
			+ space;
		    layerDef += cmGetValue(cm, status, fullname, "layerOriginY")
			+ space;
		    layerDef += cmGetValue(cm, status, fullname, "layerOriginZ")
			+ space;
		    layerDef += cmGetValue(cm, status, fullname, "layerLength")
			+ space;
		    layerDef += cmGetValue(cm, status, fullname, "layerWidth")
			+ space;
		    layerDef += cmGetValue(cm, status, fullname, "layerDepth");
		    System.out.println("Layers!" + layerDef);
		    tmp_layers = tmp_layers + "*" + layerDef;
		}
	    }
	    
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
    
    public List getMyLayerEntryForProjectList() {
		  String projectFullName = codeName + SEPARATOR + projectName;
		  this.myLayerEntryForProjectList.clear();
		  try {
				if (cm.listContext(projectFullName) != null) {
					 String thename = projectFullName + SEPARATOR + LAYERS;
					 String[] layers = cm.listContext(thename);
					 if (layers.length > 0) {
						  for (int i = 0; i < layers.length; i++) {
								layerEntryForProject tmp_myLayerEntryForProject = new layerEntryForProject();
								tmp_myLayerEntryForProject.layerName = layers[i];
								tmp_myLayerEntryForProject.view = false;
								tmp_myLayerEntryForProject.delete = false;
								this.myLayerEntryForProjectList
									 .add(tmp_myLayerEntryForProject);
								
						  }
					 }
				}
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
		  return this.myLayerEntryForProjectList;
    }
    
    public void setMyLayerEntryForProjectList(List tmp_list) {
		  this.myLayerEntryForProjectList = tmp_list;
    }
    
    public List getMyFaultEntryForProjectList() {
	String projectFullName = codeName + SEPARATOR + projectName;
	this.myFaultEntryForProjectList.clear();
	try {
	    if (cm.listContext(projectFullName) != null) {
		String thename = projectFullName + SEPARATOR + FAULTS;
		String[] faults = cm.listContext(thename);
		if (faults.length > 0) {
		    for (int i = 0; i < faults.length; i++) {
			faultEntryForProject tmp_myFaultEntryForProject = new faultEntryForProject();
			tmp_myFaultEntryForProject.setFaultName(faults[i]);
			tmp_myFaultEntryForProject.view = false;
			tmp_myFaultEntryForProject.delete = false;
			this.myFaultEntryForProjectList
			    .add(tmp_myFaultEntryForProject);
			
		    }
		}
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	
	return this.myFaultEntryForProjectList;
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
    
    public HtmlDataTable getMyFaultDataTable() {
	return myFaultDataTable;
    }
    
    // Setters ----------------------------------------------------------
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


    //--------------------------------------------------
    // End the accessor method section.
    //--------------------------------------------------

}
