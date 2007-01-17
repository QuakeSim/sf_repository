package org.apache.myfaces.blank;

//Imports from the mother ship
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Hashtable;
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
import WebFlowClient.fsws.FSClientStub;
import cgl.webclients.AntVisco;
import cgl.webclients.AntViscoServiceLocator;
import WebFlowClient.cm.*;
import WebFlowClient.sjws.*;

/**
 * Everything you need to set up and run MeshGenerator.
 */

public class MeshGeneratorBean extends GenericSopacBean {

	// Variables that we need to get from the parent.
	// ContextManagerImp cm=null;
	// boolean isInitialized=false;

	// RDAHMM file extensions
	String[] fileExtension = { ".input", ".stdout", ".A", ".B", ".L", ".Q",
			".pi" };

	// RDAHMM properties

	protected int numModelStates = 2;

	protected int randomSeed = 1;

	protected String outputType = "";

	// protected String sopacDataFileName="";
	// protected String sopacDataFileContent="";
	protected double annealStep = 0.01;

	// RDAHMM Gnuplot stuff properties
	protected String gnuplotAntUrl;

	protected String gnuplotBinPath;

	protected String gnuplotBaseWorkDir;

	protected String gnuplotFileServiceUrl;

	protected String localImageFileX;

	protected String localImageFileY;

	protected String localImageFileZ;

	// MeshGenerator Bean staff
	protected String codeName = "MeshGenerator";

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

	// Select Bean staff

	// --------------------------------------------------
	// Set up the DB
	// --------------------------------------------------
	String selectdbURL = new String(
			"http://danube.ucs.indiana.edu:9090/axis/services/" + "/Select");

	String meshServerUrl = new String("http://gf2.ucs.indiana.edu:18084");

	String submitjobUrl = new String(
			"http://danube.ucs.indiana.edu:8045/GCWS/services/Submitjob");

	Layer currentLayer = new Layer();

	Fault currentFault = new Fault();

	List myFaultDBEntryList = new ArrayList();

	List myLayerDBEntryList = new ArrayList();

	List myFaultEntryForProjectList = new ArrayList();

	List myLayerEntryForProjectList = new ArrayList();

	List myFaultsForProject = new ArrayList();
	
	List myProjectNameList = new ArrayList();
	
	List myLoadMeshTableEntryList = new ArrayList();
	
	List myarchivedMeshTableEntryList =new ArrayList();
	
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

	String meshSize = new String("50");

	String magic15 = new String("1.5");

	String workDir = new String();

	String projectDir = new String();

	MeshViewer myMeshViewer = new MeshViewer(meshServerUrl);
	String mesh_gen_viz_fileServiceUrl= new String("http://gf2.ucs.indiana.edu:6060/jetspeed/services/FileService");
	String mesh_gen_viz_base_dir = new String("/home/gateway/yan_offscreen/offscreen/");

	String myLayersParamForJnlp = new String("");
	String myFaultsParamForJnlp = new String("");
	String workDirForJnlp = new String("");
	String projectNameForJnlp = new String("");
	String fsURLForJnlp = new String("");
	
	String refineOutMessage = new String(
			"Finite element mesh is being initialized.  Depending on your problem, this may take several minutes to complete.  Select Refine Mesh to see if initialization is complete and to iteratively refine your mesh.  Click Save Mesh when you are done.");

	// --------------------------------------------------
	// Set some variables. Need to put in properties.
	// --------------------------------------------------
	String FAULTS = "Faults";

	String LAYERS = "Layers";

	String SEPARATOR = "/";

	// --------------------------------------------------
	public void setMyarchivedMeshTableEntryList(List tmp_str) {
		this.myarchivedMeshTableEntryList = tmp_str;
	}

	public List getMyarchivedMeshTableEntryList() {
		myarchivedMeshTableEntryList.clear();
		try {
			String[] tmp_contextlist = cm.listContext(codeName);
			if (tmp_contextlist.length > 0) {
				for (int i = 0; i < tmp_contextlist.length; i++) {
					loadMeshTableEntry tmp_loadMeshTableEntry=new loadMeshTableEntry();
					tmp_loadMeshTableEntry.projectName=tmp_contextlist[i];
					tmp_loadMeshTableEntry.meshHost=cm.getCurrentProperty(codeName+"/"+tmp_contextlist[i],"hostName");
					if(tmp_loadMeshTableEntry.meshHost==null) {
						tmp_loadMeshTableEntry.meshHost="null";
					}
					tmp_loadMeshTableEntry.creationDate=(new Date(Long.parseLong(cm.getCurrentProperty(codeName+"/"+tmp_contextlist[i],"LastTime")))).toString();
					tmp_loadMeshTableEntry.view=false;
	                String archived=cm.getCurrentProperty(codeName+"/"+tmp_contextlist[i],"MeshArchived");
	                if(archived!=null && archived.equalsIgnoreCase("true")) {
	                	myarchivedMeshTableEntryList.add(tmp_loadMeshTableEntry);
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
					loadMeshTableEntry tmp_loadMeshTableEntry=new loadMeshTableEntry();
					tmp_loadMeshTableEntry.projectName=tmp_contextlist[i];
					tmp_loadMeshTableEntry.meshHost=cm.getCurrentProperty(codeName+"/"+tmp_contextlist[i],"hostName");
					if(tmp_loadMeshTableEntry.meshHost==null) {
						tmp_loadMeshTableEntry.meshHost="null";
					}
					tmp_loadMeshTableEntry.creationDate=(new Date(Long.parseLong(cm.getCurrentProperty(codeName+"/"+tmp_contextlist[i],"LastTime")))).toString();
					tmp_loadMeshTableEntry.view=false;
					myLoadMeshTableEntryList.add(tmp_loadMeshTableEntry);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.myLoadMeshTableEntryList;
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
					myProjectMeshHostArray[i]=cm.getCurrentProperty(codeName+"/"+tmp_contextlist[i],"hostName");
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
					myProjectCreationDateArray[i]=(new Date(Long.parseLong(cm.getCurrentProperty(codeName+"/"+tmp_contextlist[i],"LastTime")))).toString();
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
					myProjectNameList.add(new SelectItem(tmp_contextlist[i],tmp_contextlist[i]));
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
		this.fsURLForJnlp=getBASE64(fileServiceUrl);
		return this.fsURLForJnlp;
	}
	
	public String getProjectNameForJnlp() {
		this.projectNameForJnlp=getBASE64(projectName);
		return this.projectNameForJnlp;
	}
	
	public String getWorkDirForJnlp() {
		this.workDirForJnlp=getBASE64(workDir);
		return this.workDirForJnlp;
	}
	
	public void setFaultarrayForMesh(String[] tmp_str) {
		this.faultarrayForMesh = tmp_str;
	}

	public String[] getFaultarrayForMesh() {

		return this.faultarrayForMesh;
	}
	
	public void setRefineOutMessage(String tmp_str) {
		this.refineOutMessage = tmp_str;
	}

	public String getRefineOutMessage() {

		return this.refineOutMessage;
	}

	public void setMyFaultsForProject(List tmp_str) {
		this.myFaultsForProject = tmp_str;
	}

	public List getMyFaultsForProject() {
		this.myFaultsForProject.clear();
		myFaultEntryForProjectList=getMyFaultEntryForProjectList();
		for (int i = 0; i < myFaultEntryForProjectList.size(); i++) {
			faultEntryForProject tmp_FaultEntryForProject = (faultEntryForProject) myFaultEntryForProjectList
					.get(i);
			String tmp_str = tmp_FaultEntryForProject.faultName;
			myFaultsForProject.add(new SelectItem(tmp_str, tmp_str));
		}

		return this.myFaultsForProject;
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

	public void setSubmitjobUrl(String tmp_str) {
		this.submitjobUrl = tmp_str;
	}

	public String getSubmitjobUrl() {
		return this.submitjobUrl;
	}

	public void setMeshSize(String tmp_str) {
		this.meshSize = tmp_str;
	}

	public String getMeshSize() {
		return this.meshSize;
	}

	public void setMagic15(String tmp_str) {
		this.magic15 = tmp_str;
	}

	public String getMagic15() {
		return this.magic15;
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
						tmp_myFaultEntryForProject.faultName = faults[i];
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

	// Actions ----------------------------------------------------------
	public void handleFaultsRadioValueChange(ValueChangeEvent event) {

		try {
			// Catch the MyData item during the third phase of the JSF lifecycle.
			FaultDBEntry tmp_FaultDBEntry = (FaultDBEntry) getMyFaultDataTable()
					.getRowData();
			SelectItem tmp_SelectItem = tmp_FaultDBEntry.getFaultName();
			currentFault.faultName = tmp_SelectItem.getValue().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String handleFaultEntryEdit(ActionEvent ev) {

		try {
			// Catch the MyData item during the third phase of the JSF lifecycle.
			FaultDBEntry tmp_FaultDBEntry = (FaultDBEntry) getMyFaultDataTable()
					.getRowData();
			SelectItem tmp_SelectItem = tmp_FaultDBEntry.getFaultName();
			currentFault.faultName = tmp_SelectItem.getValue().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initEditFormsSelection();
		currentFault.faultName = currentFault.faultName.trim();
		if (!currentFault.faultName.equals("")) {
			currentFault = QueryFaultFromDB(currentFault.faultName.trim());
		}
		renderCreateNewFaultForm = !renderCreateNewFaultForm;

		return "edit"; // Navigation case.
	}

	public String handleLayerEntryEdit(ActionEvent ev) {

		// Get selected MyData item to be edited.
		try {
			// Catch the MyData item during the third phase of the JSF lifecycle.
			LayerDBEntry tmp_LayerDBEntry = (LayerDBEntry) getMyLayerDataTable()
					.getRowData();
			SelectItem tmp_SelectItem = tmp_LayerDBEntry.getLayerName();
			currentLayer.layerName = tmp_SelectItem.getValue().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initEditFormsSelection();
		currentLayer.layerName = currentLayer.layerName.trim();
		if (!currentLayer.layerName.equals("")) {
			currentLayer = QueryLayerFromDB(currentLayer.layerName.trim());
		}
		renderCreateNewLayerForm = !renderCreateNewLayerForm;

		return "edit"; // Navigation case.
	}

	public String setValue(ContextManagerImp cm, String Status, String name,
			String prop) throws Exception {
		String retval = "";
		if (Status.equals("Update")) {
			retval = cm.getCurrentProperty(name, prop);
		}
		return retval;
	}

	//	public void handleUpdateFaultsRadioChange(ValueChangeEvent event) {
	//
	//
	//	}

	public void handleLayersRadioValueChange(ValueChangeEvent event) {

		try {
			// Catch the MyData item during the third phase of the JSF lifecycle.
			LayerDBEntry tmp_LayerDBEntry = (LayerDBEntry) getMyLayerDataTable()
					.getRowData();
			SelectItem tmp_SelectItem = tmp_LayerDBEntry.getLayerName();
			currentLayer.layerName = tmp_SelectItem.getValue().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public void QueryLayersList() {

		String getLayerListSQL = "select LayerName from LAYER LEFT JOIN LREFERENCE on LAYER.InterpId=LREFERENCE.InterpId;";
		String getAuthorListSQL = "select Author1 from LAYER LEFT JOIN LREFERENCE on LAYER.InterpId=LREFERENCE.InterpId;";

		myLayerDBEntryList.clear();
		currentLayer.reset();

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
			//			System.out.println(latEnd);
			//			System.out.println(latStart);
			//			System.out.println(lonStart);
			//			System.out.println(lonEnd);

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
			tmp_fault.faultName = theFault;
			tmp_fault.faultLocationX = "0.0";
			tmp_fault.faultLocationY = "0.0";
			tmp_fault.faultLength = length;
			tmp_fault.faultWidth = width;
			tmp_fault.faultDepth = depth;
			tmp_fault.faultDipAngle = dip;
			tmp_fault.faultStrikeAngle = strike;
			tmp_fault.faultSlip = "";
			tmp_fault.faultRakeAngle = "";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return tmp_fault;
	}

	public Layer QueryLayerFromDB(String tmp_layername) {
		Layer tmp_layer = new Layer();
		try {
			SelectService ss = new SelectServiceLocator();
			Select select = ss.getSelect(new URL(selectdbURL));

			// --------------------------------------------------
			// Make queries.
			// --------------------------------------------------
			tmp_layer.layerName = tmp_layername;
			tmp_layer.layerOriginX = getDBValue(select, "OriginX",
					tmp_layername);
			tmp_layer.layerOriginY = getDBValue(select, "OriginY",
					tmp_layername);
			tmp_layer.layerOriginZ = getDBValue(select, "OriginZ",
					tmp_layername);
			tmp_layer.layerLatOrigin = getDBValue(select, "LatOrigin",
					tmp_layername);
			tmp_layer.layerLonOrigin = getDBValue(select, "LonOrigin",
					tmp_layername);
			tmp_layer.layerLength = getDBValue(select, "Length", tmp_layername);
			tmp_layer.layerWidth = getDBValue(select, "Width", tmp_layername);
			tmp_layer.layerDepth = getDBValue(select, "Depth", tmp_layername);
			tmp_layer.lameLambda = getDBValue(select, "LameLambda",
					tmp_layername);
			tmp_layer.lameMu = getDBValue(select, "LameMu", tmp_layername);
			tmp_layer.viscosity = getDBValue(select, "Viscosity", tmp_layername);
			tmp_layer.exponent = getDBValue(select, "ViscosityExponent",
					tmp_layername);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return tmp_layer;
	}

	public static void main(String[] args) {

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

	public void setMeshServerUrl(String tmp_url) {
		this.meshServerUrl = tmp_url;
	}

	public String getMeshServerUrl() {
		return this.meshServerUrl;
	}

	public void setSelectdbURL(String tmp_url) {
		this.selectdbURL = tmp_url;
	}

	public String getSelectdbURL() {
		return this.selectdbURL;
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

	public void toggleProjectSelection(ActionEvent ev) {
		initEditFormsSelection();
		if (projectSelectionCode.equals("CreateNewLayer")) {
			currentLayer.reset();
			renderCreateNewLayerForm = !renderCreateNewLayerForm;
		}
		if (projectSelectionCode.equals("CreateNewFault")) {
			currentFault.reset();
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
		currentFault.faultName = currentFault.faultName.trim();
		if (!currentFault.faultName.equals("")) {
			currentFault = QueryFaultFromDB(currentFault.faultName.trim());
		}
		renderCreateNewFaultForm = !renderCreateNewFaultForm;
	}

	public void toggleSelectLayerDBEntry(ActionEvent ev) {
		initEditFormsSelection();
		currentLayer.layerName = currentLayer.layerName.trim();
		if (!currentLayer.layerName.equals("")) {
			currentLayer = QueryLayerFromDB(currentLayer.layerName.trim());
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
			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
			layerEntryForProject tmp_LayerEntryForProject = new layerEntryForProject();
			for (int i = 0; i < myLayerEntryForProjectList.size(); i++) {
				tmp_LayerEntryForProject = (layerEntryForProject) myLayerEntryForProjectList
						.get(i);
				if ((tmp_LayerEntryForProject.getView() == true)
						|| (tmp_LayerEntryForProject.getDelete() == true)) {
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
				currentLayer.layerName = tmp_layerName;
				String thename = projectFullName + SEPARATOR + LAYERS;

				String fullname = thename + SEPARATOR + currentLayer.layerName;
				currentLayer.layerOriginX = setValue(cm, layerStatus, fullname,
						"layerOriginX");
				currentLayer.layerOriginY = setValue(cm, layerStatus, fullname,
						"layerOriginY");
				currentLayer.layerOriginZ = setValue(cm, layerStatus, fullname,
						"layerOriginZ");
				currentLayer.layerLatOrigin = setValue(cm, layerStatus,
						fullname, "layerLatOrigin");
				currentLayer.layerLonOrigin = setValue(cm, layerStatus,
						fullname, "layerLonOrigin");
				currentLayer.layerLength = setValue(cm, layerStatus, fullname,
						"layerLength");
				currentLayer.layerWidth = setValue(cm, layerStatus, fullname,
						"layerWidth");
				currentLayer.layerDepth = setValue(cm, layerStatus, fullname,
						"layerDepth");
				currentLayer.lameLambda = setValue(cm, layerStatus, fullname,
						"lameLambda");
				currentLayer.lameMu = setValue(cm, layerStatus, fullname,
						"lameMu");
				currentLayer.viscosity = setValue(cm, layerStatus, fullname,
						"viscosity");
				currentLayer.exponent = setValue(cm, layerStatus, fullname,
						"exponent");
				renderCreateNewLayerForm = !renderCreateNewLayerForm;

			}
			if ((tmp_update == true) && (tmp_view == false)) {
				String tmp = projectFullName + SEPARATOR + LAYERS + SEPARATOR
						+ tmp_layerName;
				cm.removeContext(tmp);
				gfutils.updateGroupFile(cm, projectFullName, projectName);
				try {
					MyVTKServiceLocator service = new MyVTKServiceLocator();
					MyVTKServicePortType meshserv = service
							.getMyVTKService(new URL(meshServerUrl));
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
			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
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
			String projectFullName = codeName + SEPARATOR + projectName;

			initEditFormsSelection();
			if ((tmp_view == true) && (tmp_update == true)) {
				System.out.println("error");
			}
			if ((tmp_view == true) && (tmp_update == false)) {
				currentFault.faultName = tmp_faultName;
				String thename = projectFullName + SEPARATOR + FAULTS;

				String fullname = thename + SEPARATOR + currentFault.faultName;
				currentFault.faultLocationX = setValue(cm, faultStatus,
						fullname, "faultOriginX");
				currentFault.faultLocationY = setValue(cm, faultStatus,
						fullname, "faultOriginY");
				currentFault.faultLength = setValue(cm, faultStatus, fullname,
						"faultLength");
				currentFault.faultWidth = setValue(cm, faultStatus, fullname,
						"faultWidth");
				currentFault.faultDepth = setValue(cm, faultStatus, fullname,
						"faultDepth");
				currentFault.faultDipAngle = setValue(cm, faultStatus,
						fullname, "faultDipAngle");
				currentFault.faultStrikeAngle = setValue(cm, faultStatus,
						fullname, "faultStrikeAngle");
				currentFault.faultSlip = setValue(cm, faultStatus, fullname,
						"faultSlip");
				currentFault.faultRakeAngle = setValue(cm, faultStatus,
						fullname, "faultRakeAngle");
				renderCreateNewFaultForm = !renderCreateNewFaultForm;

			}
			if ((tmp_update == true) && (tmp_view == false)) {
				String tmp = projectFullName + SEPARATOR + FAULTS + SEPARATOR
						+ tmp_faultName;
				cm.removeContext(tmp);
				gfutils.updateGroupFile(cm, projectFullName, projectName);
				try {
					MyVTKServiceLocator service = new MyVTKServiceLocator();
					MyVTKServicePortType meshserv = service
							.getMyVTKService(new URL(meshServerUrl));
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

	public void toggleRefineMesh(ActionEvent ev) {

		try {

			// --------------------------------------------------
			// Set up the file service
			// --------------------------------------------------
			FSClientStub fsclient = new FSClientStub();
			fsclient.setBindingUrl(fileServiceUrl);
			// --------------------------------------------------
			// Get the client stub
			// --------------------------------------------------
			AntVisco ant = new AntViscoServiceLocator().getAntVisco(new URL(
					antUrl));

			// --------------------------------------------------
			// Get the parameters we need.
			// The "RefineMesh" param is set if we hit the
			// refine button on the bottom of the page. It
			// will NOT be set if we are coming in from the
			// initial mesh refine.
			// --------------------------------------------------
			String bf_loc = binPath + "/" + "build.xml";
			String projectFullName = codeName + SEPARATOR + projectName;

			String[] faultArray = cm.listContext(projectFullName + SEPARATOR
					+ FAULTS);
			// Refine the mesh (only called if CROM has been completed).
			// Note using ant.run() here blocks until the task is
			// completed.
			String[] refiningFaults = faultarrayForMesh;
			if (refiningFaults == null)
				refiningFaults = faultArray;
			for (int i = 0; i < refiningFaults.length; i++) {
			}

			// First, clean up the old file. This should block until
			// complete.
			String[] args0 = new String[4];
			args0[0] = "-DprojectDir.prop=" + projectDir;
			args0[1] = "-buildfile";
			args0[2] = bf_loc;
			args0[3] = "REFINE_CLEAN";

			ant.setArgs(args0);
			ant.run();

			// Now call refine for each fault. This does not block,
			// instead returns immediately.
			for (int i = 0; i < refiningFaults.length; i++) {
				String faultName = refiningFaults[i] + ".flt";
				String[] args = new String[10];
				args[0] = "-Dworkdir.prop=" + workDir;
				args[1] = "-DprojectDir.prop=" + projectDir;
				args[2] = "-DprojectName.prop=" + projectName;
				args[3] = "-Dbindir.prop=" + binPath;
				args[4] = "-Dmeshsize.prop=" + meshSize;
				args[5] = "-DfaultName.prop=" + faultName;
				args[6] = "-Dmagic15.prop=" + magic15;
				args[7] = "-buildfile";
				args[8] = bf_loc;
				args[9] = "REFINE";

				ant.setArgs(args);
				ant.execute();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void toggleViewMeshMessages(ActionEvent ev) {

		String refineout = "refine_messages.out";
		long tstamp = (new Date()).getTime();
		try {
			// --------------------------------------------------
			// Set up the file service
			// --------------------------------------------------
			FSClientStub fsclient = new FSClientStub();
			fsclient.setBindingUrl(fileServiceUrl);
			fsclient.downloadFile(workDir + File.separator + refineout, "/tmp/"
					+ refineout + tstamp);
			BufferedReader buf = new BufferedReader(new FileReader("/tmp/"
					+ refineout + tstamp));
			this.refineOutMessage = "";
			String line = buf.readLine();
			StringBuffer appendBuf = new StringBuffer(line);

			while (line != null) {
				appendBuf.insert(0, line + "\n");
				line = buf.readLine();
			}
			refineOutMessage = appendBuf.toString();
		} catch (Exception ex) {
			refineOutMessage = "Answer unclear.  Ask again later.";
			ex.printStackTrace();
		}

	}
	
	
	public void toggleAddLayerForProject(ActionEvent ev) {
		initEditFormsSelection();
		String newline = "<br>";
		String projectFullName = codeName + SEPARATOR + projectName;
		String gcname = currentLayer.layerName;

		// Get rid of spaces.
		while (gcname.indexOf(" ") > -1) {
			gcname = gcname.substring(0, gcname.indexOf(" "))
					+ "_"
					+ gcname
							.substring(gcname.indexOf(" ") + 1, gcname.length());
		}
		currentLayer.layerName = gcname;
		GeoFESTElement layerelement = new GeoFESTElement();
		layerelement.addElement("layerName", currentLayer.layerName);
		layerelement.addElement("layerOriginX", currentLayer.layerOriginX);
		layerelement.addElement("layerOriginY", currentLayer.layerOriginY);
		layerelement.addElement("layerOriginZ", currentLayer.layerOriginZ);
		layerelement.addElement("layerLatOrigin", currentLayer.layerLatOrigin);
		layerelement.addElement("layerLonOrigin", currentLayer.layerLonOrigin);
		layerelement.addElement("layerLength", currentLayer.layerLength);
		layerelement.addElement("layerWidth", currentLayer.layerWidth);
		layerelement.addElement("layerDepth", currentLayer.layerDepth);
		layerelement.addElement("lameLambda", currentLayer.lameLambda);
		layerelement.addElement("lameMu", currentLayer.lameMu);
		layerelement.addElement("viscosity", currentLayer.viscosity);
		layerelement.addElement("exponent", currentLayer.exponent);

		try {
			gfutils.setContextProperties(cm, projectFullName, LAYERS, gcname,
					layerelement);

			gfutils.writeLayerOutputFile(cm, projectFullName, LAYERS, gcname);
			gfutils.updateGroupFile(cm, projectFullName, projectName);

			// This is obsolete, should be removed
			gfutils.updateMaterialsFile(cm, projectFullName, LAYERS, gcname);

			// This is needed by new geotrans code
			gfutils.updateMaterialsFile2(cm, projectFullName, LAYERS, gcname);
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
			float X = Float.parseFloat(currentLayer.layerOriginX);
			float Y = Float.parseFloat(currentLayer.layerOriginY);
			float Z = Float.parseFloat(currentLayer.layerOriginZ);
			float Length = Float.parseFloat(currentLayer.layerLength);
			float Width = Float.parseFloat(currentLayer.layerWidth);
			float Depth = Float.parseFloat(currentLayer.layerDepth);
			MyVTKServiceLocator tmp_service = new MyVTKServiceLocator();
			MyVTKServicePortType meshserv = tmp_service
					.getMyVTKService(new URL(meshServerUrl));
			meshserv.addLayer(gcname, X, Y, Z, Length, Width, Depth);
		} catch (Exception ex) {
		}
	}

	public void toggleAddFaultForProject(ActionEvent ev) {
		initEditFormsSelection();
		String newline = "<br>";
		String projectFullName = codeName + SEPARATOR + projectName;
		String gcname = currentFault.faultName;

		// Get rid of spaces.
		while (gcname.indexOf(" ") > -1) {
			gcname = gcname.substring(0, gcname.indexOf(" "))
					+ "_"
					+ gcname
							.substring(gcname.indexOf(" ") + 1, gcname.length());
		}
		GeoFESTElement faultelement = new GeoFESTElement();
		faultelement.addElement("faultName", currentFault.faultName);
		faultelement.addElement("faultOriginX", currentFault.faultLocationX);
		faultelement.addElement("faultOriginY", currentFault.faultLocationY);
		faultelement.addElement("faultLength", currentFault.faultLength);
		faultelement.addElement("faultWidth", currentFault.faultWidth);
		faultelement.addElement("faultDepth", currentFault.faultDepth);
		faultelement.addElement("faultDipAngle", currentFault.faultDipAngle);
		faultelement.addElement("faultStrikeAngle",
				currentFault.faultStrikeAngle);
		faultelement.addElement("faultSlip", currentFault.faultSlip);
		faultelement.addElement("faultRakeAngle", currentFault.faultRakeAngle);

		faultelement.addElement("faultOriginZ", "0.0");
		faultelement.addElement("faultNumber", "1.0");

		try {
			gfutils.setContextProperties(cm, projectFullName, FAULTS, gcname,
					faultelement);
			gfutils.updateGroupFile(cm, projectFullName, projectName);

			// This is obsolete, needs to be removed.
			gfutils.writeFaultParamFile(cm, projectFullName, FAULTS, gcname);

			// This is needed by the new geotrans code.
			gfutils.writeFaultParamFile2(cm, projectFullName, FAULTS, gcname);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		faultelement.reset();

		// -----------------------------------------------------------
		// add Fault to mesh image.
		// -----------------------------------------------------------
		try {
			float X = Float.parseFloat(currentFault.faultLocationX);
			float Y = Float.parseFloat(currentFault.faultLocationY);
			float Width = Float.parseFloat(currentFault.faultWidth);
			float Depth = Float.parseFloat(currentFault.faultDepth);
			float Length = Float.parseFloat(currentFault.faultLength);
			float Dip = (float) Double.parseDouble(currentFault.faultDipAngle);
			float Strike = (float) Double
					.parseDouble(currentFault.faultStrikeAngle);
			MyVTKServiceLocator service = new MyVTKServiceLocator();
			MyVTKServicePortType meshserv = service.getMyVTKService(new URL(
					meshServerUrl));
			meshserv.addFault(gcname, X, Y, Length, Width, Depth, Dip, Strike);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String toggleFireMeshGen() {
		this.workDir = baseWorkDir;
		this.projectDir = this.workDir;

		int MINIMUM_NUMBER_FAULTS = 1;
		int MINIMUM_NUMBER_LAYERS = 1;
		try {
			// --------------------------------------------------
			// Get some request parameters
			// --------------------------------------------------
			String projectFullName = codeName + SEPARATOR + projectName;
			// --------------------------------------------------
			// Write the Lee-formated fault data file to the context.
			// --------------------------------------------------
			String[] layerlist = cm.listContext(projectFullName + "/" + LAYERS);
			String[] faultlist = cm.listContext(projectFullName + "/" + FAULTS);
			// Loop over the faults in the context and add them.
			// Note we use the same layer every time.
			for (int i = 0; i < faultlist.length; i++) {
				gfutils.writeFaultOutputFile(cm, projectFullName, FAULTS,
						LAYERS, faultlist[i], layerlist[0]);
			}
			// --------------------------------------------------
			// Set up the file service
			// --------------------------------------------------
			FSClientStub fsclient = new FSClientStub();
			fsclient.setBindingUrl(fileServiceUrl);
			// --------------------------------------------------
			// Upload all the files to the appropriate place.
			// --------------------------------------------------
			String[] layers = cm.listContext(projectFullName + SEPARATOR
					+ LAYERS);
			String[] faults = cm.listContext(projectFullName + SEPARATOR
					+ FAULTS);

			// Bail out if any conditions are met.
			if (layers == null || layers.length < MINIMUM_NUMBER_LAYERS
					|| faults == null || faults.length < MINIMUM_NUMBER_FAULTS) {
				return "MG-this";
			} else {
				// First, make the remote directory.
				SJwsImp sjws = new SJwsImpServiceLocator()
						.getSubmitjob(new URL(submitjobUrl));

				if (sjws == null) {
					return "MG-this";
				}

				String hostDir = workDir + "/" + userName + "/" + projectName
						+ "/";
				String command = "mkdir -p " + hostDir;
				sjws.execLocalCommand(command);

				// Next, move the group file.
				String cwd = cm
						.getCurrentProperty(projectFullName, "Directory");
				String grpfile = cm.getCurrentProperty(projectFullName, "Name")
						+ ".grp";
				String filename = cwd + SEPARATOR + grpfile;
				fsclient.uploadFile(filename, hostDir + grpfile);
				// Next, move each of the layers
				if (layers != null) {
					for (int i = 0; i < layers.length; i++) {
						String lcwd = cm.getCurrentProperty(projectFullName
								+ SEPARATOR + LAYERS + SEPARATOR + layers[i],
								"Directory");
						String sldfile = layers[i] + ".sld";
						filename = lcwd + SEPARATOR + sldfile;
						fsclient.uploadFile(filename, hostDir + sldfile);
					}
				}
				// --------------------------------------------------
				// Next, move each of the faults
				// --------------------------------------------------
				if (faults != null) {
					for (int i = 0; i < faults.length; i++) {
						String fcwd = cm.getCurrentProperty(projectFullName
								+ SEPARATOR + FAULTS + SEPARATOR + faults[i],
								"Directory");
						String fltfile = faults[i] + ".flt";
						filename = fcwd + SEPARATOR + fltfile;
						fsclient.uploadFile(filename, hostDir + fltfile);
					}
				}
				// --------------------------------------------------
				// --------------------------------------------------
				// Next move the fault params file(s). There is a
				// legacy file called "params" that we eventually
				// remove, and then one param file named after
				// each fault.
				// --------------------------------------------------
				if (faults != null) {
					// This is the legacy file.
					for (int i = 0; i < faults.length; i++) {
						String fcwd = cm.getCurrentProperty(projectFullName
								+ SEPARATOR + FAULTS + SEPARATOR + faults[i],
								"Directory");
						String paramfile = "params";
						filename = fcwd + SEPARATOR + paramfile;
						fsclient.uploadFile(filename, hostDir + paramfile);
					}

					// This is the file needed by Geotrans.
					for (int i = 0; i < faults.length; i++) {
						String fcwd = cm.getCurrentProperty(projectFullName
								+ SEPARATOR + FAULTS + SEPARATOR + faults[i],
								"Directory");
						String paramfile = faults[i] + ".params";
						filename = fcwd + SEPARATOR + paramfile;
						fsclient.uploadFile(filename, hostDir + paramfile);
					}
				}
				// --------------------------------------------------
				// --------------------------------------------------
				// Finally, move material file. There is a legacy
				// file called "materials" and a materials file
				// for each layer.
				// --------------------------------------------------
				String fcwd = cm.getCurrentProperty(projectFullName + SEPARATOR
						+ LAYERS, "Directory");
				// Move the legacy materials file.
				if (fcwd != null) {
					String matfile = "materials";
					filename = fcwd + SEPARATOR + matfile;
					fsclient.uploadFile(filename, hostDir + matfile);
				}

				// Move the layer materials
				if (layers != null) {
					for (int i = 0; i < layers.length; i++) {
						String lcwd = cm.getCurrentProperty(projectFullName
								+ SEPARATOR + LAYERS + SEPARATOR + layers[i],
								"Directory");
						String sldfile = layers[i] + ".materials";
						filename = lcwd + SEPARATOR + sldfile;
						fsclient.uploadFile(filename, hostDir + sldfile);
					}
				}
				// --------------------------------------------------
				// --------------------------------------------------
				// Get the client stub
				// --------------------------------------------------
				AntVisco ant = new AntViscoServiceLocator()
						.getAntVisco(new URL(antUrl));

				// Finally, initialize the mesh

				this.workDir = hostDir;
				this.projectDir = hostDir;

				// These need to come from AWS
				String bindir = this.binPath;
				String bf_loc = bindir + "build.xml";

				// Get faults from the context. For now, we
				// only have one fault, so take the first element.
				String[] faultArray = cm.listContext(projectFullName
						+ SEPARATOR + FAULTS);

				// for(int i=0;i<faultArray.length;i++) {

				String faultName = faultArray[0] + ".flt";
				// session.setAttribute("faultName",faultName);
				// System.out.println("Mesh size:"+meshsize);

				String[] args = new String[10];
				args[0] = "-Dworkdir.prop=" + hostDir;
				args[1] = "-DprojectDir.prop=" + hostDir;
				args[2] = "-DprojectName.prop=" + projectName;
				args[3] = "-Dbindir.prop=" + bindir;
				args[4] = "-Dmeshsize.prop=" + meshSize;
				args[5] = "-DfaultName.prop=" + faultName;
				args[6] = "-Dmagic15.prop=" + magic15;
				args[7] = "-buildfile";
				args[8] = bf_loc;
				args[9] = "CROM";

				ant.setArgs(args);
				ant.execute();
				// }
				for (int i = 0; i < faultArray.length; i++) {
					String faultName2 = faultArray[i] + ".flt";
					// session.setAttribute("faultName",faultName);
					System.out.println("Tagging fault:" + faultName2);

					String[] args2 = new String[10];
					args2[0] = "-Dworkdir.prop=" + hostDir;
					args2[1] = "-DprojectDir.prop=" + hostDir;
					args2[2] = "-DprojectName.prop=" + projectName;
					args2[3] = "-Dbindir.prop=" + bindir;
					args2[4] = "-Dmeshsize.prop=" + meshSize;
					args2[5] = "-DfaultName.prop=" + faultName2;
					args2[6] = "-Dmagic15.prop=" + magic15;
					args2[7] = "-buildfile";
					args2[8] = bf_loc;
					args2[9] = "TAG_ONCE";

					ant.setArgs(args2);
					ant.execute();
				}
				return "MG-ant-run";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return "MG-this";
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
	 * default empty constructor
	 */
	public MeshGeneratorBean() {
		super();
		gfutils.initLayerInteger();
		cm = getContextManagerImp();
		System.out.println("MeshGenerator Bean Created");
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
		cm.setCurrentProperty(projectFullName,"MeshArchived","true");
		cm.setCurrentProperty(projectFullName,"hostName",hostName);
		cm.setCurrentProperty(projectFullName,"workDir",workDir);

		String name=cm.getCurrentProperty(projectFullName,"Name");

		String UserMsg="Index, tetra, and node files were saved";
		
		return ("MG-back");
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
		myMeshViewer.setServiceUrl(this.meshServerUrl);
		myMeshViewer.fileServiceUrl=fileServiceUrl;
		myMeshViewer.projectName=projectName;
		myMeshViewer.workDir=workDir;
		myMeshViewer.mesh_gen_viz_base_dir=mesh_gen_viz_base_dir;
		myMeshViewer.mesh_gen_viz_fileServiceUrl=mesh_gen_viz_fileServiceUrl;
		return "MG-view-mesh";
	}
	
	public String SetAndPlot() throws Exception {
		myMeshViewer.reset();
		myMeshViewer.setServiceUrl(this.meshServerUrl);
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
	
	public String loadThenAntRun() throws Exception {
		System.out.println("Loading Mesh");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		for(int i=0; i< myLoadMeshTableEntryList.size();i++)
		{
			loadMeshTableEntry tmp_myLoadMeshTableEntry=(loadMeshTableEntry)myLoadMeshTableEntryList.get(i);
			if( tmp_myLoadMeshTableEntry.view == true) {
				this.projectName=tmp_myLoadMeshTableEntry.projectName;
				this.meshSize="50";
				this.magic15="1.5";
				this.workDir=baseWorkDir + "/" + userName + "/" + projectName
				+ "/";
				break;
			}
		}
		
		return toggleFireMeshGen();
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
		
		return ("MG-fetch-mesh");
	}	
	public String loadProjectPlots() throws Exception {
		System.out.println("Loading project");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		return ("rdahmm-list-project-plots");
	}

	public String launchRDAHMM() throws Exception {
		String sopacDataFileName = projectName + ".input";
		String cfullName = codeName + "/" + projectName;
		String contextDir = cm.getCurrentProperty(cfullName, "Directory");
		String sopacDataFileContent = getSopacDataFileContent();

		createSopacDataFile(contextDir, sopacDataFileName, sopacDataFileContent);
		String value = executeRDAHMM(contextDir, sopacDataFileName, cfullName);
		return "rdahmm-rdahmm-launched";

	}

	public String populateAndPlot() throws Exception {
		populateProject();
		launchPlot();
		return "rdahmm-plot-created";
	}

	public String launchPlot() throws Exception {
		String sopacDataFileName = projectName + ".input";
		String cfullName = codeName + "/" + projectName;
		String contextDir = cm.getCurrentProperty(cfullName, "Directory");

		createSopacDataFile(contextDir, sopacDataFileName, sopacDataFileContent);
		String value = createDataPlot(contextDir, sopacDataFileName, cfullName);
		return "rdahmm-gnuplot-launched";

	}

	// Possibly obsolete--need to check.
	public String launchProject() {
		return "rdahmm-project-launched";
	}

	public String populateProject() throws Exception {
		System.out.println("Chosen project: " + chosenProject);
		String contextName = codeName + "/" + chosenProject;
		projectName = cm.getCurrentProperty(contextName, "projectName");
		hostName = cm.getCurrentProperty(contextName, "hostName");
		numModelStates = Integer.parseInt(cm.getCurrentProperty(contextName,
				"numModelStates"));
		randomSeed = Integer.parseInt(cm.getCurrentProperty(contextName,
				"randomSeed"));
		outputType = cm.getCurrentProperty(contextName, "outputType");
		annealStep = Double.parseDouble(cm.getCurrentProperty(contextName,
				"annealStep"));

		sopacDataFileName = cm.getCurrentProperty(contextName,
				"sopacDataFileName");
		sopacDataFileContent = setRDAHMMSopacDataFile(projectName);
		// System.out.println("Input File:"+sopacDataFileContent);
		return "rdahmm-project-populated";
	}

	public String executeRDAHMM(String contextDir, String sopacDataFileName,
			String cfullName) throws Exception {

		System.out.println("FileService URL:" + fileServiceUrl);
		System.out.println("AntService URL:" + antUrl);

		String workDir = baseWorkDir + File.separator + userName
				+ File.separator + projectName;

		int ndim = getFileDimension(contextDir, sopacDataFileName);
		int nobsv = getLineCount(contextDir, sopacDataFileName);
		// --------------------------------------------------
		// Set up the Ant Service and make the directory
		// --------------------------------------------------
		AntVisco ant = new AntViscoServiceLocator()
				.getAntVisco(new URL(antUrl));
		String bf_loc = binPath + "/" + "build.xml";
		String[] args0 = new String[4];
		args0[0] = "-DworkDir.prop=" + workDir;
		args0[1] = "-buildfile";
		args0[2] = bf_loc;
		args0[3] = "MakeWorkDir";

		ant.setArgs(args0);
		ant.run();

		// --------------------------------------------------
		// Set up the file service and move the file.
		// --------------------------------------------------
		FSClientStub fsclient = new FSClientStub();
		String destfile = workDir + "/" + sopacDataFileName;
		try {
			fsclient.setBindingUrl(fileServiceUrl);
			fsclient.uploadFile(contextDir + "/" + sopacDataFileName, destfile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// --------------------------------------------------
		// Record the names of the input, output, and log
		// files on the remote server.
		// --------------------------------------------------
		String remoteOutputFile = workDir + "/" + projectName + ".output";
		String remoteLogFile = workDir + "/" + projectName + ".stdout";

		cm.setCurrentProperty(cfullName, "RemoteInputFile", destfile);
		cm.setCurrentProperty(cfullName, "RemoteOutputFile", remoteOutputFile);
		cm.setCurrentProperty(cfullName, "RemoteLogFile", remoteLogFile);

		// --------------------------------------------------
		// Set up the Ant Service.
		// --------------------------------------------------
		// AntVisco ant=new AntViscoServiceLocator().getAntVisco(new
		// URL(antUrl));

		String[] args = new String[13];
		args[0] = "-DworkDir.prop=" + workDir;
		args[1] = "-DprojectName.prop=" + projectName;
		args[2] = "-Dbindir.prop=" + binPath;
		args[3] = "-DRDAHMMBaseName.prop=" + projectName;
		args[4] = "-Dnobsv.prop=" + nobsv;
		args[5] = "-Dndim.prop=" + ndim;
		args[6] = "-Dnstates.prop=" + numModelStates;
		args[7] = "-Dranseed.prop=" + randomSeed;
		args[8] = "-Doutput_type.prop=" + outputType;
		args[9] = "-DannealStep.prop=" + annealStep;
		args[10] = "-buildfile";
		args[11] = bf_loc;
		args[12] = "RunRDAHMM";

		ant.setArgs(args);
		ant.execute();

		return "rdahmm-rdahmm-executing";
	}

	/**
	 * This is similar to executeRDAHMM but it must take place on a host with
	 * gnuplot installed on it. Note this assumes for historical reasons that
	 * rdahmm and the plotting tool (gnuplot) are on separate machines.
	 */
	public String createDataPlot(String contextDir, String sopacDataFileName,
			String cfullName) throws Exception {

		String workDir = baseWorkDir + File.separator + userName
				+ File.separator + projectName;

		String gnuplotWorkDir = gnuplotBaseWorkDir + File.separator + userName
				+ File.separator + projectName;

		// --------------------------------------------------
		// Set up the Ant Service and make the directory on
		// the gnuplot host.
		// --------------------------------------------------
		AntVisco ant = new AntViscoServiceLocator().getAntVisco(new URL(
				gnuplotAntUrl));
		String bf_loc = gnuplotBinPath + "/" + "build.xml";

		String[] args0 = new String[4];
		args0[0] = "-DworkDir.prop=" + workDir;
		args0[1] = "-buildfile";
		args0[2] = bf_loc;
		args0[3] = "MakeWorkDir";

		ant.setArgs(args0);
		ant.run();

		// --------------------------------------------------
		// Set up the file service and move the file.
		// --------------------------------------------------
		FSClientStub fsclient = new FSClientStub();
		String sourceFile = workDir + "/" + sopacDataFileName;
		String inputdestfile = gnuplotBinPath + "/" + sopacDataFileName;

		String qSourceFile = workDir + "/" + projectName + ".Q";
		String qDestFile = gnuplotBinPath + "/" + projectName + ".Q";

		String plotFileNameX = gnuplotBinPath + "/" + sopacDataFileName
				+ ".X.png";
		String plotFileNameY = gnuplotBinPath + "/" + sopacDataFileName
				+ ".Y.png";
		String plotFileNameZ = gnuplotBinPath + "/" + sopacDataFileName
				+ ".Z.png";

		try {
			fsclient.setBindingUrl(fileServiceUrl);
			fsclient
					.crossload(sourceFile, gnuplotFileServiceUrl, inputdestfile);
			fsclient.crossload(qSourceFile, gnuplotFileServiceUrl, qDestFile);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception();
		}

		String[] args = new String[7];
		args[0] = "-DworkDir.prop=" + gnuplotWorkDir;
		args[1] = "-DbinDir.prop=" + gnuplotBinPath;
		args[2] = "-DinputFile.prop=" + sopacDataFileName;
		args[3] = "-DqFile.prop=" + projectName + ".Q";
		args[4] = "-buildfile";
		args[5] = bf_loc;
		args[6] = "ExecGnuplot";

		ant.setArgs(args);
		ant.run();

		cm.setCurrentProperty(cfullName, "PlotFileNameX", plotFileNameZ);
		cm.setCurrentProperty(cfullName, "PlotFileNameY", plotFileNameY);
		cm.setCurrentProperty(cfullName, "PlotFileNameZ", plotFileNameZ);

		// Download the image file

		ExternalContext ec = FacesContext.getCurrentInstance()
				.getExternalContext();
		Object context = ec.getContext();
		String basePath = "";
		if (context instanceof ServletContext) {
			basePath = ((ServletContext) context).getRealPath("/");
		} else if (context instanceof PortletContext) {
			basePath = ((PortletContext) context).getRealPath("/");
		}

		long timestamp = (new Date()).getTime();
		String realImageFileX = basePath + "/" + "junkX_" + timestamp + ".png";
		String realImageFileY = basePath + "/" + "junkY_" + timestamp + ".png";
		String realImageFileZ = basePath + "/" + "junkZ_" + timestamp + ".png";
		localImageFileX = "junkX_" + (new Date()).getTime() + ".png";
		localImageFileY = "junkY_" + (new Date()).getTime() + ".png";
		localImageFileZ = "junkZ_" + (new Date()).getTime() + ".png";
		fsclient.setBindingUrl(gnuplotFileServiceUrl);

		try {
			fsclient.downloadFile(plotFileNameX, realImageFileX);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			fsclient.downloadFile(plotFileNameY, realImageFileY);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			fsclient.downloadFile(plotFileNameZ, realImageFileZ);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "rdahmm-gnuplot-plot-created";
	}

	// --------------------------------------------------
	// Find the first non-blank line and count columns.
	// Note this can screw up if input file is not
	// formated correctly, but then RDAHMM itself
	// would probably not work either.
	// --------------------------------------------------

	protected int getFileDimension(String contextDir, String sopacDataFileName) {

		boolean success = false;
		int ndim = 0;
		StringTokenizer st;
		try {

			BufferedReader buf = new BufferedReader(new FileReader(contextDir
					+ "/" + sopacDataFileName));

			String line = buf.readLine();
			if (line != null) {
				while (!success) {
					if (line.trim().equals("")) {
						line = buf.readLine();
					} else {
						success = true;
						st = new StringTokenizer(line);
						ndim = st.countTokens();
					}
				}
			}
			buf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ndim;
	}

	// --------------------------------------------------
	// This counts the line number.
	// --------------------------`------------------------
	protected int getLineCount(String contextDir, String sopacDataFileName) {
		int nobsv = 0;
		try {
			LineNumberReader lnr = new LineNumberReader(new FileReader(
					contextDir + "/" + sopacDataFileName));

			String line2 = lnr.readLine();
			while (line2 != null) {
				line2 = lnr.readLine();
			}
			lnr.close();
			nobsv = lnr.getLineNumber();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return nobsv;

	}

	protected String setRDAHMMSopacDataFile(String projectName) {
		String sopacDataFileContent = "Null Content; please re-enter";
		String sopacDataFileName = projectName + ".input";
		try {
			String thedir = cm.getCurrentProperty(codeName + "/" + projectName,
					"Directory");
			// System.out.println(thedir+"/"+sopacDataFileName);

			BufferedReader buf = new BufferedReader(new FileReader(thedir + "/"
					+ sopacDataFileName));
			String line = buf.readLine();
			sopacDataFileContent = line + "\n";
			while (line != null && !line.equals("")) {
				// System.out.println(line);
				line = trimLine(line);
				sopacDataFileContent += line + "\n";
				line = buf.readLine();
			}
			buf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return sopacDataFileContent;
	}

	public String querySOPAC() throws Exception {
		String retString = super.querySOPAC();
		sopacDataFileContent = filterResults(sopacDataFileContent, 2, 3);
		return retString;
	}

	/**
	 * This helper method assumes input is a multlined String of tabbed columns.
	 * It cuts out the number of columns on the left specified by cutLeftColumns
	 * and number on the right by cutRightColumns.
	 */
	protected String filterResults(String tabbedString, int cutLeftColumns,
			int cutRightColumns) throws Exception {
		String returnString = "";
		String space = " ";
		StringTokenizer st;
		BufferedReader br = new BufferedReader(new StringReader(tabbedString));
		String line = br.readLine();
		while (line != null && !line.equals("")) {
			// System.out.println("Line: "+line);
			st = new StringTokenizer(line);
			String newLine = "";
			int tokenCount = st.countTokens();
			for (int i = 0; i < tokenCount; i++) {
				String temp = st.nextToken();
				if (i >= cutLeftColumns && i < (tokenCount - cutRightColumns)) {
					newLine += temp + space;
				}
			}
			// System.out.println("New Line: "+newLine);
			returnString += newLine + "\n";
			line = br.readLine();
		}
		returnString = returnString.trim();
		// System.out.println("Here is the file");
		// System.out.println(returnString);
		// System.out.println("That was the file");
		return returnString;
	}

	// --------------------------------------------------
	// These are accessor methods.
	// --------------------------------------------------

	public String getLocalImageFileX() {
		return localImageFileX;
	}

	public void setLocalImageFileX(String localImageFileX) {
		this.localImageFileX = localImageFileX;
	}

	public String getLocalImageFileY() {
		return localImageFileY;
	}

	public void setLocalImageFileY(String localImageFileY) {
		this.localImageFileY = localImageFileY;
	}

	public String getLocalImageFileZ() {
		return localImageFileZ;
	}

	public void setLocalImageFileZ(String localImageFileZ) {
		this.localImageFileZ = localImageFileZ;
	}

	public String getGnuplotFileServiceUrl() {
		return gnuplotFileServiceUrl;
	}

	public void setGnuplotFileServiceUrl(String gnuplotFileServiceUrl) {
		this.gnuplotFileServiceUrl = gnuplotFileServiceUrl;
	}

	public String getGnuplotBaseWorkDir() {
		return gnuplotBaseWorkDir;
	}

	public void setGnuplotBaseWorkDir(String gnuplotBaseWorkDir) {
		this.gnuplotBaseWorkDir = gnuplotBaseWorkDir;
	}

	public String getGnuplotBinPath() {
		return gnuplotBinPath;
	}

	public void setGnuplotBinPath(String gnuplotBinPath) {
		this.gnuplotBinPath = gnuplotBinPath;
	}

	public String getGnuplotAntUrl() {
		return gnuplotAntUrl;
	}

	public void setGnuplotAntUrl(String gnuplotAntUrl) {
		this.gnuplotAntUrl = gnuplotAntUrl;
	}

	public String getGnuplotHostName() {
		return gnuplotHostName;
	}

	public void setGnuplotHostName(String gnuplotHostName) {
		this.gnuplotHostName = gnuplotHostName;
	}

	public double getAnnealStep() {
		return annealStep;
	}

	public void setAnnealStep(double annealStep) {
		this.annealStep = annealStep;
	}

	public int getNumModelStates() {
		return numModelStates;
	}

	public void setNumModelStates(int numModelStates) {
		this.numModelStates = numModelStates;
	}

	public int getRandomSeed() {
		return randomSeed;
	}

	public void setRandomSeed(int randomSeed) {
		this.randomSeed = randomSeed;
	}

	public String getOutputType() {
		return outputType;
	}

	public void setOutputType(String outputType) {
		this.outputType = outputType;
	}

}
