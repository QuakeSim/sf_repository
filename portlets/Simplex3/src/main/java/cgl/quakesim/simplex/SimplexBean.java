package cgl.quakesim.simplex;

//Imports from the mother ship
import java.io.*;
import java.net.*;
import java.rmi.server.UID;
import java.util.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.UIData;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

//Import some of our packages.
import org.servogrid.genericproject.GenericSopacBean;
//import WebFlowClient.cm.ContextManagerImp;

//DB4O imports
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

//GRWS imports
import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

//Jacskon JSON handling imports
import org.codehaus.jackson.map.ObjectMapper;

//Commons logging
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/**
 * Everything you need to set up and run SimpleBean.
 */

public class SimplexBean extends GenericSopacBean {
	// KML stuff, need to move this to another place.
	String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	String kmlHead = "<kml xmlns=\"http://earth.google.com/kml/2.2\">";
	String kmlEnd = "</kml>";
	String pmBegin = "<Placemark>";
	String pmEnd = "</Placemark>";
	String lsBegin = "<LineString>";
	String lsEnd = "</LineString>";
	String pointBegin = "<Point>";
	String pointEnd = "</Point>";
	String coordBegin = "<coordinates>";
	String coordEnd = "</coordinates>";
	String docBegin = "<Document>";
	String docEnd = "</Document>";
	String comma = ", ";
	String descBegin = "<description>";
	String descEnd = "</description>";
	
	// These are properties needed if you want to include a query
	// to the GRWS web service.

	// Variables that we need to get from the parent.
	// ContextManagerImp cm=null;
	// boolean isInitialized=false;

	// Simplex Bean staff
	// Protected String codeName = "Simplex3";
	// modified to be from faces-config.xml 2010/1/8 Jun Ji

	String codeName = "";
	private int SIMPLEX_OBSV_COUNT = 5;
	 //Original format had no verticial displacement and two dummy columns.
	private int ARIA_TOKEN_COUNT_V0 = 8;
	 //Updated version adds vertical displacements and deletes the two dummy columns
	private int ARIA_TOKEN_COUNT_V1= 7;

	// This is the db4o database	

	// --------------------------------------------------
	// Set some variables. Need to put in properties.
	// --------------------------------------------------
	String FAULTS = "Faults";

	String SEPARATOR = "/";

	String OBSERVATIONS = "Observations";

	// member for Simplex
	projectEntry currentProjectEntry = new projectEntry();

	String selectdbURL = "";

	editProjectForm currentEditProjectForm;

	List myFaultEntryForProjectList = new ArrayList();
	List myFaultsForProjectList = new ArrayList();
	int myFaultsForProjectListsize;

	List myObservationEntryForProjectList = new ArrayList();
	
	List mycandidateObservationsForProjectList = new ArrayList();

	List myObservationsForProjectList = new ArrayList();

	List myProjectNameList = new ArrayList();

	String[] selectProjectsList;
	String[] deleteProjectsList;
	String[] copyProjectsList;

	List myarchivedFileEntryList = new ArrayList();

	GMTViewForm currentGMTViewForm = new GMTViewForm();

	String gmtPlotPdfUrl = "";

	String gmtPlotPdf_timeStamp = "";

	String mapXmlUrl = "";

	// These are used to store the actual layers and faults
	List myFaultCollection = new ArrayList();

	List myObservationCollection = new ArrayList();

	// This is our geofest service stub.
	SimpleXService simplexService;

	String simpleXBaseUrl = "";

	String simpleXServiceUrl = "";
	String kmlGeneratorBaseurl = "";
	String kmlGeneratorUrl = "";

	SimpleXOutputBean projectSimpleXOutput;

	HtmlDataTable myArchiveDataTable;
	UIData myArchiveDataTable2;

	String kmlProjectFile = "network0.kml";

	String gpsStationName = "";
	String gpsStationLat = "";
	String gpsStationLon = "";
	boolean gpsRefStation = false;
	boolean searcharea = false;
	boolean gpssource1 = true;
	boolean gpssource2 = true;
	
	boolean gpssource1v = false;
	boolean gpssource2v = false;

	boolean faultdrawing = false;
	String selectedGpsStationName = "";

	String selectedminlon = null;
	String selectedmaxlon = null;
	String selectedminlat = null;
	String selectedmaxlat = null;
	
	private List stationsources =  new ArrayList();
	private List stationlist =  new ArrayList();
	
	String selectedstation = null;
	
	String selectedss = null;

	String[] latArray;
	String[] lonArray;
	String[] nameArray;

	 //Parameters needed for UNAVCO GPS parameters
	 String gpsJSONValues="";
	 String selectedGPSJSONValues="";

	// These are needed for the SOPAC GRWS query.
	// These dates are arbitrary but apparently needed.
	String beginDate = "2006-01-10";
	String endDate = "2008-04-10";
	String resource = "procVels";
	String contextGroup = "sopacGlobk";
	String minMaxLatLon = "";
	String contextId = "38";
	String kmlfiles = "";

	NumberFormat format = null;

	protected String faultKmlUrl;
	protected String faultKmlFilename;
	 protected String obsvKmlFilename;
	 protected String obsvKmlUrl;
	protected String portalBaseUrl;

	 //This is the logger
	 private static Logger logger;
	 
	 public String getGpsJSONValues(){
		  return this.gpsJSONValues;
	 }
	 
	 public void setGpsJSONValues(String gpsJSONValues) {
		  this.gpsJSONValues=gpsJSONValues;
	 }

	public boolean getGpsRefStation() {		
		return this.gpsRefStation;
	}

	public void setGpsRefStation(boolean gpsRefStation) {
		this.gpsRefStation = gpsRefStation;
	}

	public String getGpsStationName() {
		return gpsStationName;
	}

	public void setGpsStationName(String gpsStationName) {
		this.gpsStationName = gpsStationName;
	}

	public String getSelectedGpsStationName() {
		return selectedGpsStationName;
	}

	public void setSelectedGpsStationName(String selectedGpsStationName) {
		this.selectedGpsStationName = selectedGpsStationName;
	}

	public boolean getSearcharea() {
		return searcharea;
	}

	public void setSearcharea(boolean searcharea) {
		this.searcharea = searcharea;
	}

	public boolean getGpssource1() {
		return gpssource1;
	}

	public void setGpssource1(boolean gpssource1) {
		this.gpssource1 = gpssource1;
	}	

	public boolean getGpssource2() {
		return gpssource2;
	}

	public void setGpssource2(boolean gpssource2) {
		this.gpssource2 = gpssource2;
	}
	
	public boolean getGpssource1v() {
		return gpssource1v;
	}

	public void setGpssource1v(boolean gpssource1v) {
		this.gpssource1v = gpssource1v;
	}	

	public boolean getGpssource2v() {
		return gpssource2v;
	}

	public void setGpssource2v(boolean gpssource2v) {
		this.gpssource2v = gpssource2v;
	}
	
	
	public boolean getFaultdrawing() {
		return faultdrawing;
	}

	public void setFaultdrawing(boolean faultdrawing) {
		this.faultdrawing = faultdrawing;
	}

	public String getSelectedminlon() {
		return selectedminlon;
	}

	public void setSelectedminlon(String selectedminlon) {
		this.selectedminlon = selectedminlon;
	}

	public String getSelectedmaxlon() {
		return selectedmaxlon;
	}

	public void setSelectedmaxlon(String selectedmaxlon) {
		this.selectedmaxlon = selectedmaxlon;
	}

	public String getSelectedminlat() {
		return selectedminlat;
	}

	public void setSelectedminlat(String selectedminlat) {
		this.selectedminlat = selectedminlat;
	}

	public String getSelectedmaxlat() {
		return selectedmaxlat;
	}

	public void setSelectedmaxlat(String selectedmaxlat) {
		this.selectedmaxlat = selectedmaxlat;
	}

	public String getGpsStationLat() {
		return gpsStationLat;
	}

	public void setGpsStationLat(String gpsStationLat) {
		this.gpsStationLat = gpsStationLat;
	}

	public String getGpsStationLon() {
		return gpsStationLon;
	}

	public void setGpsStationLon(String gpsStationLon) {
		this.gpsStationLon = gpsStationLon;
	}

	public String getMapXmlUrl() {
		return this.mapXmlUrl;
	}

	public void setMapXmlUrl(String tmp_str) {
		this.mapXmlUrl = tmp_str;
	}

	public SimpleXOutputBean getProjectSimpleXOutput() {
		return this.projectSimpleXOutput;
	}

	public void setProjectSimpleXOutput(SimpleXOutputBean tmp_str) {
		this.projectSimpleXOutput = tmp_str;
	}

	public String getSimpleXBaseUrl() {
		return this.simpleXBaseUrl;
	}

	public void setSimpleXBaseUrl(String tmp_str) {
		this.simpleXBaseUrl = tmp_str;
	}

	public String getSimpleXServiceUrl() {
		return this.simpleXServiceUrl;
	}

	public void setSimpleXServiceUrl(String tmp_str) {
		this.simpleXServiceUrl = tmp_str;
	}

	 protected void makeProjectDirectory() {
		  File projectDir = new File(getBasePath() + "/" + getContextBasePath()
											  + "/" + userName + "/" + codeName + "/");
		  projectDir.mkdirs();
	 }
	 
	 public Fault[] getProjectFaultsFromDB(String userName, 
														String projectName, 
														String codeName, 
														String basePath, 
														String relPath) {
		  Fault[] returnFaults = null;
		  logger.info("[" + getUserName() 
							+ "/SimplexBean/getProjectFaultsFromDB] Opening Fault DB:" 
							+ basePath + "/" + relPath + "/" + userName + "/" + codeName 
							+ "/" + projectName + ".db");
		  
		  ObjectContainer db = null;
		  
		try {

			db = Db4o.openFile(basePath + "/" + relPath + "/" + userName + "/"
					+ codeName + "/" + projectName + ".db");
			Fault faultToGet = new Fault();
			ObjectSet results = db.get(faultToGet);
			if (results.hasNext()) {
				returnFaults = new Fault[results.size()];
				for (int i = 0; i < results.size(); i++)
					returnFaults[i] = (Fault) results.next();
			}
			
		} catch (Exception e) {			
			logger.error("[" + getUserName() + "/SimplexBean/getProjectFaultsFromDB] " + e);
		}
		finally {
			if (db != null) db.close();			
		}

		return returnFaults;
	}

	protected Fault[] getFaultsFromDB() {
		Fault[] returnFaults = null;

		ObjectContainer db = null;
		
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"  
									 + userName + "/" + codeName + "/" + projectName + ".db");
			Fault faultToGet = new Fault();
			ObjectSet results = db.get(faultToGet);
			if (results.hasNext()) {
				returnFaults = new Fault[results.size()];
				for (int i = 0; i < results.size(); i++) {
					returnFaults[i] = (Fault) results.next();
				}
			}

		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/getFaultsFromDB] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return returnFaults;
	}

	protected Observation[] getObservationsFromDB() {
		Observation[] returnObservations = null;

		ObjectContainer db = null;
		
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");
			Observation ObservationToGet = new Observation();
			ObjectSet results = db.get(ObservationToGet);
			if (results.hasNext()) {
				returnObservations = new Observation[results.size()];
				for (int i = 0; i < results.size(); i++) {
					returnObservations[i] = (Observation) results.next();
				}
			}
			
		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/getObservationsFromDB] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return returnObservations;
	}

	public void setKmlGeneratorUrl(String tmp_str) {
		this.kmlGeneratorUrl = tmp_str;
	}

	public String getKmlGeneratorUrl() {
		return this.kmlGeneratorUrl;
	}

	// set and get data staff
	public void setGmtPlotPdfUrl(String tmp_str) {
		this.gmtPlotPdfUrl = tmp_str;
	}

	public String getGmtPlotPdfUrl() {
		return this.gmtPlotPdfUrl;
	}

	public void setCurrentGMTViewForm(GMTViewForm tmp_str) {
		this.currentGMTViewForm = tmp_str;
	}

	public GMTViewForm getCurrentGMTViewForm() {
		return this.currentGMTViewForm;
	}

	public void setMyarchivedFileEntryList(List tmp_str) {
		this.myarchivedFileEntryList = tmp_str;
	}

	public List getMyarchivedFileEntryList() {
		List myprojectlist = getMyProjectNameList();
		List tmpList = new ArrayList();

		myarchivedFileEntryList.clear();
		logger.info("Project list size:"+myprojectlist.size());

		for (int i = 0; i < myprojectlist.size(); i++) {
			String projectName = ((SelectItem) myprojectlist.get(i)).getLabel();

			SimpleXOutputBean mega = new SimpleXOutputBean();
			mega.setProjectName(projectName);
			logger.info("ProjectName: "+projectName);

			ObjectContainer db = null;
			
			try {
				db = Db4o.openFile(getBasePath() + "/" + getContextBasePath()
						+ "/" + userName + "/" + codeName + "/" + projectName
						+ ".db");

				ObjectSet results = db.get(new SimpleXOutputBean());
				logger.info("Matches for "+projectName+":"+results.size());
				while (results.hasNext()) {
					SimpleXOutputBean sob = (SimpleXOutputBean) results.next();
					tmpList.add(sob);
				}				
			} catch (Exception e) {				
				logger.error("[" + getUserName() + "/SimplexBean/getMyarchivedFileEntryList] " + e);
			}
			finally {
				if (db != null) db.close();			
			}		

			myarchivedFileEntryList = sortByDate(tmpList);
		}

		return myarchivedFileEntryList;
	}

	public void setDeleteProjectsList(String[] tmp_str) {
		this.deleteProjectsList = tmp_str;
	}

	public String[] getDeleteProjectsList() {
		return this.deleteProjectsList;
	}

	public int getMyFaultsForProjectListsize() {

		return myFaultsForProjectList.size();
	}

	public void setMyFaultsForProjectListsize(int myFaultsForProjectListsize) {
		this.myFaultsForProjectListsize = myFaultsForProjectListsize;
	}

	public void setMyProjectNameList(List tmp_str) {
		this.myProjectNameList = tmp_str;
	}

	public List getMyProjectNameList() {
		logger.debug("Reconstructing the project name list");
		myProjectNameList.clear();
		
		ObjectContainer db = null;
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + ".db");
			projectEntry project = new projectEntry();
			ObjectSet results = db.get(projectEntry.class);
			logger.debug("Got results:"+results.size());
			while (results.hasNext()) {
				project = (projectEntry) results.next();
				logger.debug(project.getProjectName());
				if (project == null || project.getProjectName() == null) {
					db.delete(project);
				} else {
					myProjectNameList.add(new SelectItem(project
							.getProjectName(), project.getProjectName()));
				}
			}
			
		} catch (Exception ex) {
			logger.error("[" + getUserName() + "/SimplexBean/getMyProjectNameList] " + ex);
			logger.error("[" + getUserName() 
							 + "/SimplexBean/getMyProjectNameList] Returning an empty list.");
		}
		finally {
			if (db != null) db.close();			
		}
		Collections.sort(this.myProjectNameList, new Comparator() {
				  public int compare (Object o1, Object o2) {
						String p1=(String) ((SelectItem)o1).getValue();
						String p2=(String) ((SelectItem)o2).getValue();
						//							System.out.println(p2.getProjectName() +" "+p1.getProjectName());
						return p1.compareToIgnoreCase(p2);
				  }
			 });
		
		return this.myProjectNameList;
	}

	public void setSelectProjectsList(String[] tmp_str) {
		this.selectProjectsList = tmp_str;
	}

	public String[] getSelectProjectsList() {
		return this.selectProjectsList;
	}

	/**
	 * Reconstruct the Observation list from the context.
	 */
	protected List reconstructMyObservationEntryForProjectList(String projectName) {
		this.myObservationEntryForProjectList.clear();
		ObjectContainer db = null;
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			Observation tmpobser = new Observation();
			ObjectSet results = db.get(tmpobser);
			while (results.hasNext()) {
				tmpobser = (Observation) results.next();
				observationEntryForProject tmp_myObservationEntryForProject = new observationEntryForProject();
				tmp_myObservationEntryForProject.observationName = tmpobser
						.getObsvName();
				tmp_myObservationEntryForProject.update = false;
				tmp_myObservationEntryForProject.delete = false;
				if (tmpobser.getObsvRefSite().equals("-1")) {
					tmp_myObservationEntryForProject.refSite = "Yes";
				} else if (tmpobser.getObsvRefSite().equals("1")) {
					tmp_myObservationEntryForProject.refSite = "No";
				}
				// Just in case.
				else {
					tmp_myObservationEntryForProject.refSite = "No";
				}
				this.myObservationEntryForProjectList
						.add(tmp_myObservationEntryForProject);
			}

		} catch (Exception e) {
			 logger.info(e);
		}
		finally {
			if (db != null) db.close();			
		}
		return this.myObservationEntryForProjectList;
	}

	protected List reconstructMyObservationsForProjectList(String projectName) {
		this.myObservationsForProjectList.clear();
		
		ObjectContainer db = null;
		
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			Observation tmpobser = new Observation();
			ObjectSet results = db.get(tmpobser);
			while (results.hasNext()) {
				tmpobser = (Observation) results.next();
				myObservationsForProjectList.add(tmpobser);
			}
		} catch (Exception e) {
			 logger.error(e);
		}
		finally {
			if (db != null) db.close();			
		}
		
		reconstructMyObservationEntryForProjectList(projectName);
		Collections.sort(this.myObservationsForProjectList,new Comparator(){
				  public int compare(Object o1, Object o2) {
						Observation ob1=(Observation) o1;
						Observation ob2=(Observation) o2;
						return ob1.getObsvName().compareTo(ob2.getObsvName());
				  }
			 });
		return this.myObservationsForProjectList;
	}

	public List getMyObservationEntryForProjectList() {
		String projectName = getProjectName();
		logger.debug ("[" + getUserName() 
						  + "/SimplexBean/getMyObservationEntryForProjectList] ProjectName : "	
						  + projectName);
		return reconstructMyObservationEntryForProjectList(projectName);
	}

	public void setMyObservationEntryForProjectList(List tmp_list) {
		this.myObservationEntryForProjectList = tmp_list;
	}
	
	
	public List getMycandidateObservationsForProjectList() {
		String projectName = getProjectName();
		logger.info("[" + getUserName() 
						 + "/SimplexBean/getMycandidateObservationsForProjectList] ProjectName : "
						 + projectName);
		return mycandidateObservationsForProjectList;
	}

	public void setMycandidateObservationsForProjectList(List tmp_list) {
		this.mycandidateObservationsForProjectList = tmp_list;
	}
	
	public List getMyObservationsForProjectList() {
		String projectName = getProjectName();
		logger.debug("[" + getUserName() 
						 + "/SimplexBean/getMyObservationsForProjectList] ProjectName : "
						 + projectName);
		return reconstructMyObservationsForProjectList(projectName);
	}

	public void setMyObservationsForProjectList(List tmp_list) {
		this.myObservationsForProjectList = tmp_list;
	}
	 
	 /**
	  * Create the Observation collection
	  */
	 protected List populateObservationCollection(List myObservationEntryProjectList) 
		  throws Exception {
		  List myObservationCollection = new ArrayList();
		  for (int i = 0; i < myObservationEntryProjectList.size(); i++) {
				observationEntryForProject tmp_ObservationEntryForProject 
					 = (observationEntryForProject) myObservationEntryForProjectList.get(i);
			String tmp_observationName = tmp_ObservationEntryForProject
					.getObservationName();
			myObservationCollection
					.add(populateObservationFromContext(tmp_observationName));
		}
		return myObservationCollection;
	}

	protected Observation populateObservationFromContext(String tmp_observationName) 
		 throws Exception {
		 logger.debug("Populating observation " + tmp_observationName 
						  + " for  "+ projectName);
		 String observationStatus = "Update";
		 
		 Observation currentObservation = null;
		 
		 ObjectContainer db = null;
		 
		 try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			Observation observationToGet = new Observation();
			observationToGet.setObsvName(tmp_observationName);
			ObjectSet results = db.get(observationToGet);
			// Should only have one value.

			if (results.hasNext()) {
				currentObservation = (Observation) results.next();
			}
		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/populateObservationFromContext] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return currentObservation;
	}

	/**
	 * Reconstructs the fault entry list.
	 */
	protected List reconstructMyFaultEntryForProjectList(String projectName) {
		this.myFaultEntryForProjectList.clear();
		
		ObjectContainer db = null;
		
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			Fault tmpfault = new Fault();
			ObjectSet results = db.get(tmpfault);
			while (results.hasNext()) {
				tmpfault = (Fault) results.next();
				faultEntryForProject tmp_myFaultEntryForProject = new faultEntryForProject();
				tmp_myFaultEntryForProject
						.setFaultName(tmpfault.getFaultName());
				tmp_myFaultEntryForProject.setoldFaultName(tmpfault
						.getFaultName());
				tmp_myFaultEntryForProject.update = false;
				tmp_myFaultEntryForProject.delete = false;
				this.myFaultEntryForProjectList.add(tmp_myFaultEntryForProject);
			}

		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/reconstructMyFaultEntryForProjectList] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return this.myFaultEntryForProjectList;
	}

	/**
	 * Reconstructs the fault entry list.
	 */
	protected List reconstructMyFaultsForProjectList(String projectName) {
		this.myFaultsForProjectList.clear();
		ObjectContainer db = null;
		
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			Fault tmpfault = new Fault();
			ObjectSet results = db.get(tmpfault);
			while (results.hasNext()) {
				tmpfault = (Fault) results.next();

				this.myFaultsForProjectList.add(tmpfault);
			}
		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/reconstructMyFaultsForProjectList] " + e);
		}
		finally {
			if (db != null) db.close();			
		}

		reconstructMyFaultEntryForProjectList(projectName);
		Collections.sort(this.myFaultsForProjectList,new Comparator(){
				  public int compare(Object o1, Object o2) {
						Fault f1=(Fault) o1;
						Fault f2=(Fault) o2;
						return f1.getFaultName().compareToIgnoreCase(f2.getFaultName());
				  }
			 });

		return this.myFaultsForProjectList;
	}

	/**
	 * This is a fairly muscular getter. It reconstructs the entire list from
	 * the context.
	 */
	public List getMyFaultEntryForProjectList() throws Exception {
		String projectName = getProjectName();
		return reconstructMyFaultEntryForProjectList(projectName);
	}

	public void setMyFaultEntryForProjectList(List tmp_list) {
		this.myFaultEntryForProjectList = tmp_list;
	}

	public List getMyFaultsForProjectList() throws Exception {
		String projectName = getProjectName();
		return reconstructMyFaultsForProjectList(projectName);
	}

	public void setMyFaultsForProjectList(List tmp_list) {
		this.myFaultsForProjectList = tmp_list;
	}

	protected List populateFaultCollection(List myFaultEntryProjectList)
			throws Exception {
		List myFaulCollection = new ArrayList();
		for (int i = 0; i < myFaultEntryProjectList.size(); i++) {
			faultEntryForProject tmp_faultEntryForProject = (faultEntryForProject) myFaultEntryForProjectList
					.get(i);
			String tmp_faultName = tmp_faultEntryForProject.getFaultName();
			myFaultCollection.add(populateFaultFromContext(tmp_faultName));
		}
		return myFaultCollection;
	}

	protected Fault populateFaultFromContext(String tmp_faultName) {

		String faultStatus = "Update";
		Fault currentFault = null;

		ObjectContainer db = null;
		
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");
			Fault faultToGet = new Fault();
			faultToGet.setFaultName(tmp_faultName);
			ObjectSet results = db.get(faultToGet);
			// Should only have one value.
			if (results.hasNext()) {
				currentFault = (Fault) results.next();
			}			
		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/populateFaultFromContext] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return currentFault;
	}

	public void setCurrentProjectEntry(projectEntry tmp) {
		this.currentProjectEntry = tmp;
	}

	public projectEntry getCurrentProjectEntry() {
		return this.currentProjectEntry;
	}

	public void setCurrentEditProjectForm(editProjectForm tmp) {
		this.currentEditProjectForm = tmp;
		this.currentEditProjectForm.setKmlfiles(getKmlfiles());
		this.currentEditProjectForm.setCodeName(getCodeName());
		logger.info("[" + getUserName() 
								 + "/SimplexBean/setCurrentEditProjectForm] this.currentEditProjectForm.setKmlfiles( "
								 + getKmlfiles() + ")");
	}

	public editProjectForm getCurrentEditProjectForm() {
		return this.currentEditProjectForm;
	}

	public void setSelectdbURL(String tmp_url) {
		this.selectdbURL = tmp_url;
	}

	public String getSelectdbURL() {
		return this.selectdbURL;
	}

	/**
	 * This is a placeholder main method that may be used for commandline
	 * testing.
	 */
	public static void main(String[] args) {
		 
	}

	public SimplexBean() throws Exception {
		super();
		//Set up logging
		logger=Logger.getLogger(SimplexBean.class);
		//		logger.setLevel(Level.INFO);

		//		cm = getContextManagerImp();
		format = NumberFormat.getInstance();
		logger.info("[" + getUserName() + "/SimplexBean/SimplexBean] Created");
	}


	 /**
	  * Read the KML file of GPS stations, parse it, and put the name, lat, and lon in 
	  * arrays.
	  */
	 public void readStations() {
		  logger.info("[" + getUserName() + "/SimplexBean/readStations] : " + getCodeName());
		  File localFile = new File(getBasePath() + "/" + getCodeName() + "/"
											 + "stations-rss-new.xml");
		  BufferedReader br = null;
		  try {
				br = new BufferedReader(new FileReader(localFile));
		  } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		  }
		StringBuffer sb = new StringBuffer();
		try {
			 while (br.ready()) {
				  sb.append(br.readLine());
			 }
		} catch (IOException e1) {
			 // TODO Auto-generated catch block
			 e1.printStackTrace();
		}
		SAXReader reader = new SAXReader();
		Document statusDoc = null;
		try {
			 statusDoc = reader.read(new StringReader(sb.toString()));
		} catch (DocumentException e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		}
		Element eleXml = (Element) statusDoc.getRootElement();
		List stationList = eleXml.elements("station");
		latArray = new String[stationList.size()];
		lonArray = new String[stationList.size()];
		nameArray = new String[stationList.size()];
		
		// Set upt the arrays
		for (int i = 0; i < stationList.size(); i++) {
			 Element station = (Element) stationList.get(i);
			 latArray[i] = station.element("latitude").getText();
			 lonArray[i] = station.element("longitude").getText();
			 nameArray[i] = station.element("id").getText();
		}
	 }
	 

	protected void initSimplexService() throws Exception {
		simplexService = new SimpleXServiceServiceLocator()
				.getSimpleXExec(new URL(simpleXServiceUrl));
	}
	 
	 public String toggleRunSimplex2() {
		  
		  Observation[] obsv = getObservationsFromDB();
		  Fault[] faults = getFaultsFromDB();
		  // String timeStamp = "";
		  String timeStamp = generateTimeStamp();		
		  try {
				initSimplexService();
				
				projectSimpleXOutput = simplexService.runSimplex(userName,
																				 projectName, 
																				 faults, 
																				 obsv, 
																				 currentProjectEntry.startTemp,
																				 currentProjectEntry.maxIters, 
																				 currentProjectEntry.getOrigin_lon()+ "", 
																				 currentProjectEntry.getOrigin_lat() + "",
																				 this.kmlGeneratorUrl, timeStamp);

			logger.info("[" + getUserName() 
									 + "/SimplexBean/toggleRunSimplex2] " 
									 + projectSimpleXOutput.getProjectName());
			logger.info("[" + getUserName() 
									 + "/SimplexBean/toggleRunSimplex2] " 
									 + projectSimpleXOutput.getInputUrl());
			saveSimpleXOutputBeanToDB(projectSimpleXOutput);
			saveSimplexProjectEntry(currentProjectEntry);
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  return ("Simplex2-back");
	 }
	 
	/**
	 * Save and if necessary reassign the project entry.
	 */
	protected void saveSimplexProjectEntry(projectEntry currentProjectEntry) {
		
		ObjectContainer db = null;
		
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + ".db");

			ObjectSet results = db.get(projectEntry.class);

			logger.info("[" + getUserName() + "/SimplexBean/saveSimplexProjectEntry] Project entry: "
					+ currentProjectEntry.getProjectName());
			while (results.hasNext()) {
				projectEntry tmp = (projectEntry) results.next();
				if (tmp == null
						|| tmp.getProjectName() == null
						|| tmp.getProjectName().equals(
								currentProjectEntry.getProjectName())) {
					logger.info("[" + getUserName() + "/SimplexBean/saveSimplexProjectEntry] Updating/deleting project");
					db.delete(tmp);
				}
			}
			db.set(currentProjectEntry);

		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/saveSimplexProjectEntry] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
	}

	/**
	 * Saves the simplex output to the db.  
	 * REVIEW: This may be obsolete.
	 */
	protected void saveSimpleXOutputBeanToDB(SimpleXOutputBean mega) {
		 ObjectContainer db = null;
		 
		 try {
			  db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
										+ userName + "/" + codeName + "/" + projectName + ".db");
			  db.set(mega);
			  db.commit();
		 } catch (Exception e) {
			  logger.error("[" + getUserName() + "/SimplexBean/saveSimpleXOutputBeanToDB] " + e);
		 }
		 finally {
			  if (db != null)
					db.close();			
		 }
	}

	/**
	 * This creates a new project and related session bean objects. It is called
	 * by a JSF action method and so must return a navigation string.
	 */
	public String NewProjectThenEditProject() {
		String returnMessage = "";
		
		mycandidateObservationsForProjectList.clear();
		selectedGpsStationName = "";
		gpsStationName = "";
		
		
		try {
			currentEditProjectForm = new editProjectForm(selectdbURL);
			currentEditProjectForm.setKmlfiles(getKmlfiles());
			currentEditProjectForm.setCodeName(getCodeName());
			createNewProject();
			currentEditProjectForm.init_edit_project();
			returnMessage = "Simplex2-edit-project";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// The default value is blank, will load the original page.
		return returnMessage;
	}

	/**
	 * This is called when a project is seleted for copying and loading.
	 */
	public String toggleCopyProject() throws Exception {
		String returnString = "";
		logger.info("[" + getUserName() + "/SimplexBean/toggleCopyProject] Copying project");
		// Get the old project name from the checkboxes
		String oldProjectName = "";
		if (copyProjectsList != null) {
			oldProjectName = copyProjectsList[0];
		}
		logger.info("[" + getUserName() + "/SimplexBean/toggleCopyProject] Old project name: " + oldProjectName);

		// --------------------------------------------------
		// Create an empty project and add to the parent code database
		// --------------------------------------------------
		String newProjectName = this.getProjectName();
		currentEditProjectForm = new editProjectForm(selectdbURL);
		currentEditProjectForm.setKmlfiles(getKmlfiles());
		currentEditProjectForm.setCodeName(getCodeName());
		currentEditProjectForm.initEditFormsSelection();

		// Add the project to the code database, cleaning up if necessary.
		logger.info("[" + getUserName() + "/SimplexBean/toggleCopyProject] Creating new project");
		makeProjectDirectory();

		ObjectContainer db = null;
		
		try {
			
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + ".db");

			// --------------------------------------------------
			// Now recover some of the project properties from
			// the old project and write this to the current project,
			// then commit to the database.
			// --------------------------------------------------
			projectEntry oldProjectEntry = new projectEntry();
			oldProjectEntry.setProjectName(oldProjectName);
			ObjectSet results2 = db.get(projectEntry.class);
			logger.info("[" + getUserName() 
									 + "/SimplexBean/toggleCopyProject] Got results:" 
									 + results2.size());
			// There should only be 0 or 1 matching entry.
			while (results2.hasNext()) {
				projectEntry tmpProj = (projectEntry) results2.next();
				// Clean up any empty or null projects
				if (tmpProj == null || tmpProj.getProjectName() == null) {
					db.delete(tmpProj);
				}

				if (tmpProj.getProjectName().equals(oldProjectName)) {
					logger.info("[" + getUserName() 
											 + "/SimplexBean/toggleCopyProject] Old Origin:" 
											 + tmpProj.getOrigin_lat()
											 + " " + tmpProj.getOrigin_lon());
					currentProjectEntry.setProjectName(this.projectName);
					currentProjectEntry.setOrigin_lat(tmpProj.getOrigin_lat());
					currentProjectEntry.setOrigin_lon(tmpProj.getOrigin_lon());
					currentProjectEntry.setMaxIters(tmpProj.getMaxIters());
					currentProjectEntry.setStartTemp(tmpProj.getStartTemp());
				}
			}
			// Say goodbye.
			db.set(currentProjectEntry);
			db.commit();
			
		} catch (Exception e) {			
			logger.error("[" + getUserName() + "/SimplexBean/toggleCopyProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
		

		// --------------------------------------------------
		// Copy over the project entry database.
		// --------------------------------------------------
		File oldFileDB = new File(getBasePath() + "/" + getContextBasePath()
				+ "/" + userName + "/" + codeName + "/" + oldProjectName
				+ ".db");
		File newFileDB = new File(getBasePath() + "/" + getContextBasePath()
				+ "/" + userName + "/" + codeName + "/" + newProjectName
				+ ".db");
		if (!newFileDB.exists()) {
			newFileDB.createNewFile();
		}
		copyFile(oldFileDB, newFileDB);

		currentEditProjectForm.setProjectEntry(currentProjectEntry);

		// --------------------------------------------------
		// Finally, select the new project and load everything up
		// --------------------------------------------------
		returnString = toggleSelectProject(newProjectName);

		return returnString;

		// Some final stuff.
		// projectSelectionCode = "";
		// faultSelectionCode = "";
		// return "Simplex2-edit-project";
	}

	/**
	 * Delete the selected project. This is called by a JSF page. Projects are
	 * deleted by name.
	 */
	 public String toggleDeleteProject() {
		  ObjectContainer db = null;
		  
		  try {
				db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
										 + userName + "/" + codeName + ".db");
				if (deleteProjectsList != null) {
					 for (int i = 0; i < deleteProjectsList.length; i++) {
						  logger.info("[" + getUserName() 
										  + "/SimplexBean/toggleDeleteProject] Deleting:" 
										  + deleteProjectsList[i]);
						  // Delete the input bean
						  projectEntry delproj = new projectEntry();
						  ObjectSet results = db.get(projectEntry.class);
						  logger.info("[" + getUserName() 
										  + "/SimplexBean/toggleDeleteProject] results size:" 
										  + results.size());
						  while (results.hasNext()) {
								delproj = (projectEntry) results.next();
								if (delproj.getProjectName().equals(
																				(String) deleteProjectsList[i])) {
									 logger.info("[" + getUserName() 
													 + "/SimplexBean/toggleDeleteProject] Deleting:"
													 + delproj.getProjectName());
									 db.delete(delproj);
								}
						  }
						  
						  // Delete the output bean
						  SimpleXOutputBean sxob = new SimpleXOutputBean();
						  sxob.setProjectName((String) deleteProjectsList[i]);
						  results = db.get(sxob);
						  if (results.hasNext()) {
								sxob = (SimpleXOutputBean) results.next();
								db.delete(sxob);
						  }
					 }
					 
				} else {
					 logger.info("[" + getUserName() 
									 + "/SimplexBean/toggleDeleteProject] No projects selected for deletion.");
				}
				
				//Delete also the project db itself.
				File projectDbFile=new File(getBasePath() + "/" + getContextBasePath()
													 + "/" + userName + "/" + codeName + "/" + projectName
													 + ".db");
				boolean delBool=projectDbFile.delete();
				logger.info("Successfully deleted the project db file: "+delBool);

		  } catch (Exception e) {
				logger.info("[" + getUserName() + "/SimplexBean/toggleDeleteProject] " + e);
		  }
		  finally {
				if (db != null)
				db.close();			
		  }
		  return "Simplex2-this";
	 }

	/**
	 * Loads the selected project from the database, sets the current project
	 * session variable. This method is suitable for calling as a JSF action (ie
	 * no argument). The project name is set separately.
	 */
	public String toggleSelectProject() throws Exception {
		String returnString = "";
		if (selectProjectsList != null) {
			this.projectName = selectProjectsList[0];
			logger.info("[" + getUserName() + "/SimplexBean/toggleSelectProject] Project name selected" + projectName);
			mycandidateObservationsForProjectList.clear();
			selectedGpsStationName = "";
			gpsStationName = "";
			returnString = toggleSelectProject(projectName);
			logger.info("[" + getUserName() + "/SimplexBean/toggleSelectProject] Return value: " + returnString);
		} else {
			logger.info("[" + getUserName() + "/SimplexBean/toggleSelectProject] Project name selection list is null or empty");
			throw new Exception("[SimplexBean/toggleSelectProject] Project not found in toggleSelectProject.");
		}
		return returnString;
	}

	/**
	 * Loads the selected project from the database, sets the current project
	 * session variable.
	 */
	public String toggleSelectProject(String projectName) {
		currentEditProjectForm = new editProjectForm(selectdbURL);
		currentEditProjectForm.setKmlfiles(getKmlfiles());
		currentEditProjectForm.setCodeName(getCodeName());
		currentEditProjectForm.initEditFormsSelection();

		
		ObjectContainer db = null;
		
		try {
			// Reconstruct the project lists
			
			myFaultEntryForProjectList = reconstructMyFaultEntryForProjectList(projectName);
			myObservationEntryForProjectList = reconstructMyObservationEntryForProjectList(projectName);

			// Reconstruct the fault and layer object collections from the
			// context
			myFaultCollection = populateFaultCollection(myFaultEntryForProjectList);
			myObservationCollection = populateObservationCollection(myObservationEntryForProjectList);
			
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + ".db");
			projectEntry tmp_proj = new projectEntry();
			tmp_proj.setProjectName(projectName);
			ObjectSet results = db.get(projectEntry.class);
			while (results.hasNext()) {
				tmp_proj = (projectEntry) results.next();
				if (tmp_proj.getProjectName() != null
						&& tmp_proj.getProjectName().equals(projectName)) {
					logger.info("[" + getUserName() 
									+ "/SimplexBean/toggleSelectProject] Found project, reassigning");
					this.currentProjectEntry = tmp_proj;
					currentEditProjectForm.setProjectEntry(currentProjectEntry);
					break;
				}
			}
			
			// Just for laughs, print out the origin to make sure it is OK.
			logger.info("[" + getUserName() + "/SimplexBean/toggleSelectProject] Project origin: "
					+ currentProjectEntry.getOrigin_lat() + " "
					+ currentProjectEntry.getOrigin_lon());

		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/toggleSelectProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		currentEditProjectForm.projectSelectionCode = "";
		currentEditProjectForm.faultSelectionCode = "";
		return "Simplex2-edit-project";
	}

	/**
	 * This creates a new projectEntry object, stores it in the DB, and sets the
	 * currentProject Use this version to call from JSF.
	 */
	public String createNewProject() throws Exception {
		// projectName is set separately through bean methods.
		currentEditProjectForm = new editProjectForm(selectdbURL);
		currentEditProjectForm.setKmlfiles(getKmlfiles());
		currentEditProjectForm.setCodeName(getCodeName());
		currentEditProjectForm.initEditFormsSelection();

		return createNewProject(this.getProjectName());
	}

	/**
	 * This creates a new projectEntry object, stores it in the DB, and sets the
	 * currentProject
	 */
	protected String createNewProject(String projectName) {
		logger.info("[" + getUserName() + "/SimplexBean/createNewProject] Creating new project");
		makeProjectDirectory();
		
		ObjectContainer db = null;
		
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + ".db");
			// projectEntry project = new projectEntry();
			// project.projectName = this.projectName;

			projectEntry tmp_project = new projectEntry();
			tmp_project.projectName = this.projectName;
			ObjectSet result = db.get(projectEntry.class);

			// Create the new project. These may be overwritten below.
			// currentProjectEntry=new projectEntry();
			currentProjectEntry.setProjectName(projectName);
			currentProjectEntry.setOrigin_lon(currentProjectEntry.DEFAULT_LON);
			currentProjectEntry.setOrigin_lat(currentProjectEntry.DEFAULT_LAT);
			// currentProjectEntry.setMaxIters(currentProjectEntry.maxIters);
			// currentProjectEntry.setStartTemp(currentProjectEntry.startTemp);

			while (result.hasNext()) {
				tmp_project = (projectEntry) result.next();
				// Clean up any null projects
				if (tmp_project == null || tmp_project.getProjectName() == null) {
					db.delete(tmp_project);
				}
				// This is an existing project, so load it and replace
				else if (tmp_project.getProjectName().equals(projectName)) {
					db.delete(tmp_project);
					currentProjectEntry = tmp_project;
					// currentProjectEntry.setMaxIters(tmp_project.maxIters);
					// currentProjectEntry.setStartTemp(tmp_project.startTemp);
					break;
				}
			}
			db.set(currentProjectEntry);
			db.commit();
		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/createNewProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		currentEditProjectForm.setProjectEntry(currentProjectEntry);

		return "MG-set-project";
	}
	 
	 /**
	  * This may be an obsolete method.
	  */ 
	 public void toggleUpdateObservationProjectEntry(ActionEvent ev) {
		  String observationStatus = "Update";
		  ObjectContainer db = null;
		  try {
				int iSelectObservation = -1;
				// Find out which Observation was selected.
				observationEntryForProject tmp_ObservationEntryForProject =
					 new observationEntryForProject();
				for (int i = 0; i < myObservationEntryForProjectList.size(); i++) {
					 tmp_ObservationEntryForProject = 
						  (observationEntryForProject) myObservationEntryForProjectList.get(i);
					 if ((tmp_ObservationEntryForProject.getUpdate() == true)
						  || (tmp_ObservationEntryForProject.getDelete() == true)) {
						  iSelectObservation = i;
						  break;
					 }
				}
				
				// This is the info about the Observation.
				String tmp_ObservationName = 
					 tmp_ObservationEntryForProject.getObservationName();
				boolean tmp_view = tmp_ObservationEntryForProject.getUpdate();
				boolean tmp_update = tmp_ObservationEntryForProject.getDelete();
				
				currentEditProjectForm.initEditFormsSelection();
				if ((tmp_view == true) && (tmp_update == true)) {
					 logger.info("[" + getUserName() 
									 + "/SimplexBean/toggleUpdateObservationProjectEntry] info");
				}
				
				// Update the Observation.
				if ((tmp_view == true) && (tmp_update == false)) {
					 currentEditProjectForm.currentObservation = 
						  (Observation) (populateObservationFromContext(tmp_ObservationName));
					 currentEditProjectForm.renderCreateObservationForm = 
						  !currentEditProjectForm.renderCreateObservationForm;
				}
				
				// This is the deletion case.
				if ((tmp_update == true) && (tmp_view == false)) {
					 // Delete from the database.
					 // This requires we first search for the desired object
					 // and then delete the specific value that we get back.
					 logger.info("[" + getUserName() 
									 + "/SimplexBean/toggleUpdateObservationProjectEntry] Deleteing " 
									 + tmp_ObservationName + "from db");
					 
					 db = Db4o.openFile(getBasePath() + "/" + getContextBasePath()
											  + "/" + userName + "/" + codeName + "/" + projectName
											  + ".db");
					 
					 Observation todelete = new Observation();
					 todelete.setObsvName(tmp_ObservationName);
					 ObjectSet result = db.get(todelete);
					 if (result.hasNext()) {
						  todelete = (Observation) result.next();
						  db.delete(todelete);
					 }
				}
		  } catch (Exception e) {
				logger.error("[" + getUserName() 
								 + "/SimplexBean/toggleUpdateObservationProjectEntry] " + e);
		  }
		  finally {
				if (db != null)
					 db.close();			
		  }
	 }
	 
	 /**
	  * This method is associated with actions of the observations data table in 
	  * ProjectComponentsPanel.jsp
	  */
	 public void toggleUpdateObservations(ActionEvent ev) {
		  String observationStatus = "Update";
		  ObjectContainer db = null;
		  try {
				int iSelectObservation = -1;
				// Find out which Observations were selected.  Loop over all of them
				// and take appropriate actions in each case.
				
				db = Db4o.openFile(getBasePath() + "/"
										 + getContextBasePath() + "/" + userName + "/"
										 + codeName + "/" + projectName + ".db");
				
				for (int i = 0; i < myObservationsForProjectList.size(); i++) {
					 Observation tmp_Observation = 
						  (Observation) myObservationsForProjectList.get(i);
					 
					 // This is the info about the Observation.
					 String tmp_ObservationName = tmp_Observation.getObsvName();
					 boolean tmp_update = ((observationEntryForProject) myObservationEntryForProjectList
												  .get(i)).getUpdate();
					 boolean tmp_delete = ((observationEntryForProject) myObservationEntryForProjectList
												  .get(i)).getDelete();
					 
					 currentEditProjectForm.initEditFormsSelection();
					 
					 //Update the Observation. This will take precedence over deletion if 
					 //both are selected for the same station.
					 if ((tmp_update == true)) {
						  logger.info(getUserName() + "Updating "
										  + tmp_ObservationName + " "
										  + tmp_Observation.getObsvError());
						  
						  Observation toUpdate = new Observation();
						  toUpdate.setObsvName(tmp_Observation.getObsvName());
						  toUpdate.setObsvType(tmp_Observation.getObsvType());
						  ObjectSet result = db.get(toUpdate);
						  if (result.hasNext()) {
								logger.info("["+getUserName() 
												+ "/SimplexBean/toggleUpdateObservations] "
												+ toUpdate.getObsvName()+" "
												+ toUpdate.getObsvRefSite());
								toUpdate = (Observation) result.next();
								
								toUpdate.setObsvError(tmp_Observation.getObsvError());
								toUpdate.setObsvLocationEast(tmp_Observation
																	  .getObsvLocationEast());
								toUpdate.setObsvLocationNorth(tmp_Observation
																		.getObsvLocationNorth());
								toUpdate.setObsvName(tmp_Observation.getObsvName());
								toUpdate.setObsvRefSite(tmp_Observation.getObsvRefSite());
								toUpdate.setObsvType(tmp_Observation.getObsvType());
								toUpdate.setObsvValue(tmp_Observation.getObsvValue());
						  }
						  
						  db.set(toUpdate);
					 }
					 
					 // This is the deletion case. Update must be false as well as delete
					 //set to true.
					 if ((tmp_update == false) && (tmp_delete == true)) {
						  
						  // Delete from the database.
						  // This requires we first search for the desired object
						  // and then delete the specific value that we get back.
						  logger.info("[" + getUserName() + "/SimplexBean/toggleUpdateObservations] Deleting "
										  + tmp_ObservationName);
						  Observation todelete = new Observation();
						  todelete.setObsvName(tmp_Observation.getObsvName());
						  todelete.setObsvRefSite(tmp_Observation
														  .getObsvRefSite());
						  todelete.setObsvType(tmp_Observation.getObsvType());
						  
						  ObjectSet result = db.get(todelete);
						  if (result.hasNext()) {
								todelete = (Observation) result.next();
								db.delete(todelete);
						  }
					 }
				}
				db.commit();
				if (db != null) db.close();			
				reconstructMyObservationsForProjectList(projectName);				
		  } catch (Exception e) {			
				logger.error("[" + getUserName() + "/SimplexBean/toggleUpdateObservations] " + e);
		  }
		  finally {
				if (db != null) db.close();			
		  }
	 }
	 
	/**
	 * This is an obsolete method.
	 */
	public String toggleMakeMap() {

		logger.info("[" + getUserName() + "/SimplexBean/toggleMakeMap] GMT Plot");

		ObjectContainer db = null;
		
		try {

			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
			SimpleXOutputBean tmp_loadMeshTableEntry = new SimpleXOutputBean();
			for (int i = 0; i < myarchivedFileEntryList.size(); i++) {
				tmp_loadMeshTableEntry = (SimpleXOutputBean) myarchivedFileEntryList
						.get(i);
				if ((tmp_loadMeshTableEntry.isView() == true)) {
					break;
				}
			}

			String tmp_projectName = tmp_loadMeshTableEntry.getProjectName();
			projectName = tmp_projectName;
			boolean tmp_view = tmp_loadMeshTableEntry.isView();
			String timeStamp = "";
			if ((tmp_view == true)) {

				db = Db4o.openFile(getBasePath() + "/" + getContextBasePath()
						+ "/" + userName + "/" + codeName + ".db");
				projectEntry tmp_proj = new projectEntry();
				tmp_proj.projectName = this.projectName;
				ObjectSet results = db.get(tmp_proj);
				if (results.hasNext()) {
					this.currentProjectEntry = (projectEntry) results.next();
				} else {
					logger.info("[" + getUserName() + "/SimplexBean/toggleMakeMap] info: can not find this project");
				}
				logger.info(currentProjectEntry.origin_lat);
				logger.info(currentProjectEntry.origin_lon);

				initSimplexService();
				this.mapXmlUrl = simplexService.runMakeMapXml(userName,
						projectName, currentProjectEntry.getOrigin_lat() + "",
						currentProjectEntry.getOrigin_lon() + "", timeStamp);
				logger.info("[" + getUserName() + "/SimplexBean/toggleMakeMap] " + mapXmlUrl);
			}

		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/toggleMakeMap] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return ("Simplex2-google-map");
	}
	 
	
	public void gpslistupdate(ValueChangeEvent event) {
		
		stationsources = new ArrayList();
		stationsources.add(new SelectItem("v1", event.getNewValue().toString()));
			
		logger.info("[" + getUserName() + "/SimplexBean/gpslistupdate] called");
		
	}  
	
	 /**
	  * This method returns the UNAVCO velocity values from the FTP site.
	  * REVIEW: Need to check if this method should be public or protected. 
	  */
	public void getvalues(ActionEvent ev) throws IOException {
		
		logger.info("[" + getUserName() + "/SimplexBean/getvalues] called");
		mycandidateObservationsForProjectList.clear();
		
		if (selectedGpsStationName.trim() != "") {
			
			String[] stations = selectedGpsStationName.split(",");
			String s = null;
			
			// modified to use unavco .vel files. 05/28/2010 Jun Ji
			String snf01 = "ftp://data-out.unavco.org/pub/products/velocity/pbo.final_snf01.vel";
			String igs05 = "ftp://data-out.unavco.org/pub/products/velocity/pbo.final_igs05.vel";
			UnavcoVelParser uvp1 = new UnavcoVelParser();
			uvp1.getFile(snf01);
			UnavcoVelParser uvp2 = new UnavcoVelParser();
			uvp2.getFile(igs05);
			String dataUrl = "";
			
			logger.info("[" + getUserName() 
							+ "/SimplexBean/getvalues] stationlist : " + selectedGpsStationName);
			logger.info("[" 
							+ getUserName() + "/SimplexBean/getvalues] stationlist size : " 
							+ stations.length);
			
			for (int nA = 0; nA < stations.length; nA++) {
				String[] station = stations[nA].split("/");
				s = station[0];
				s = s.toLowerCase().trim();
				CandidateObservation co = new CandidateObservation();
				co.setStationName(s);
				co.setGpsStationLat(station[1]);
				co.setGpsStationLon(station[2]);
				List stationlist = new ArrayList();
							
				String v = uvp1.getStationVelocity(co.getStationName());
				int i = 0;
				
				if (v != null && s.trim() != "") {
					
					logger.info("[" + getUserName() + "/SimplexBean/getvalues] " + s +" is from " + snf01);
					dataUrl = v;			
					
					String[] s1 = dataUrl.split("\n");
					
					for (int nB = 0 ; nB < s1.length ; nB++) {
						stationlist.add(new SelectItem(Integer.toString(i), "snf01" + "/" + s1[nB]));
						i++;
					}
									
				}
				
				String v1 = uvp2.getStationVelocity(co.getStationName());
				
				if (v1 != null && s.trim() != "") {
					
					logger.info("[" + getUserName() + "/SimplexBean/getvalues] " + s +" is from " + igs05);
					dataUrl = v1;
					
					String[] s1 = dataUrl.split("\n");
					for (int nB = 0 ; nB < s1.length ; nB++) {
						stationlist.add(new SelectItem(Integer.toString(i), "igs05" + "/" + s1[nB]));
						i++;
					}				
				}
				
				
				if (s.trim() != "") {
					
					logger.info("[" + getUserName() + "/SimplexBean/getvalues] " + s +" is from the GRWS db");
				
					GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
	
					gsq.setFromServlet(s, beginDate, endDate, resource, contextGroup, contextId, minMaxLatLon, false);
	
					dataUrl = gsq.getResource() + " ";
					String[] s1 = dataUrl.split("\n");				
					for (int nB = 0 ; nB < s1.length ; nB++)
						if (!s1[nB].contains("ERROR")){
							stationlist.add(new SelectItem(Integer.toString(i), "GRWS" + "/" + s1[nB]));
							i++;
						}
				}

				co.setStationSources(stationlist);
				mycandidateObservationsForProjectList.add(co);
				if (stationlist.size() > 0)
					co.setSelectedSource(((SelectItem) stationlist.get(0)).getValue().toString());
				
			}
		}

	}
	
	public List getStationsources() {
		 if (!stationsources.isEmpty()) {
			  /*
				 stationsources = new ArrayList();
				 stationsources.add(new SelectItem("v1", "index1"));
				 stationsources.add(new SelectItem("v2", "index2"));
			  */
			  
			  selectedss = (String) ((SelectItem) stationsources.get(0)).getValue();
		 }
		 
		for (int nA = 0 ; nA < stationsources.size() ; nA++){			
			logger.info("[" + getUserName() + "/SimplexBean/getStationsources] " 
							 + (String) ((SelectItem) stationsources.get(nA)).getValue());
		}

		return stationsources;
	}

	public void setStationsources(List stationsources) {
		this.stationsources = stationsources;
	}
	
	public List getStationlist() {

		
		if (!stationlist.isEmpty()) {
			/*
			stationsources = new ArrayList();
			stationsources.add(new SelectItem("v1", "index1"));
			stationsources.add(new SelectItem("v2", "index2"));
			*/
			
			selectedstation = (String) ((SelectItem) stationlist.get(0)).getValue();
		}
		
		
		for (int nA = 0 ; nA < stationlist.size() ; nA++){			
			logger.info("[" + getUserName() + "/SimplexBean/getStationlist] " + (String) ((SelectItem) stationlist.get(nA)).getValue());
		}

		return stationlist;
	}

	public void setStationlist(List stationsources) {
		this.stationlist = stationsources;
	}
	

	public String getSelectedss() {
		
		
		logger.info("[" + getUserName() + "/SimplexBean/getSelectedss] " + selectedss);
		  
		
		return selectedss;
	}

	public void setSelectedss(String stationsourceSelectedItem) {
		this.selectedss = stationsourceSelectedItem;
	}

	
	public String getSelectedstation() {
		
		
		logger.info("[" + getUserName() + "/SimplexBean/getSelectedstation] " + selectedstation);
		  
		
		return selectedstation;
	}

	public void setSelectedstation(String stationsourceSelectedItem) {
		this.selectedstation = stationsourceSelectedItem;
	}
	
	
	public String toggleViewKml() {

		logger.info("[" + getUserName() + "/SimplexBean/toggleViewKml] Kml viewer");

		ObjectContainer db = null;
		
		try {
			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
			SimpleXOutputBean tmp_loadMeshTableEntry = new SimpleXOutputBean();
			for (int i = 0; i < myarchivedFileEntryList.size(); i++) {
				tmp_loadMeshTableEntry = (SimpleXOutputBean) myarchivedFileEntryList
						.get(i);
				if ((tmp_loadMeshTableEntry.isView() == true)) {
					break;
				}
			}

			String tmp_projectName = tmp_loadMeshTableEntry.getProjectName();
			projectName = tmp_projectName;
			boolean tmp_view = tmp_loadMeshTableEntry.isView();
			if ((tmp_view == true)) {
				SimpleXOutputBean mega = new SimpleXOutputBean();
				mega.setProjectName(projectName);
				logger.info("ProjectName: "+projectName);

				db = Db4o.openFile(getBasePath() + "/" + getContextBasePath()
						+ "/" + userName + "/" + codeName + "/" + projectName
						+ ".db");
				ObjectSet results = db.get(mega);
				logger.info("Matches for"+projectName+":"+results.size());
				if (results.hasNext()) {
					projectSimpleXOutput = (SimpleXOutputBean) results.next();
				} else {
					logger.error("[" + getUserName() 
									 + "/SimplexBean/toggleViewKml] error: can not find project: "+projectName);
				}
				db = Db4o.openFile(getBasePath() + "/" + getContextBasePath()
						+ "/" + userName + "/" + codeName + ".db");
				projectEntry tmp_proj = new projectEntry();
				tmp_proj.projectName = this.projectName;
				ObjectSet results2 = db.get(tmp_proj);
				if (results2.hasNext()) {
					this.currentProjectEntry = (projectEntry) results2.next();
				} else {
					logger.error("[" + getUserName() 
									 + "/SimplexBean/toggleViewKml] error: can not find this project");
				}
			}

		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/toggleViewKml] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return ("Simplex2-kml-viewer");
	}

	public void toggleUpdateFaultProjectEntry(ActionEvent ev) {
		String faultStatus = "Update";
		ObjectContainer db = null;
		try {
			int iSelectFault = -1;

			// Find out which fault was selected.
			faultEntryForProject tmp_FaultEntryForProject = new faultEntryForProject();
			for (int i = 0; i < myFaultEntryForProjectList.size(); i++) {
				tmp_FaultEntryForProject = (faultEntryForProject) myFaultEntryForProjectList
						.get(i);
				if ((tmp_FaultEntryForProject.getUpdate() == true)
						|| (tmp_FaultEntryForProject.getDelete() == true)) {
					iSelectFault = i;
					break;
				}
			}

			// This is the info about the fault.
			String tmp_faultName = tmp_FaultEntryForProject.getFaultName();
			boolean tmp_view = tmp_FaultEntryForProject.getUpdate();
			boolean tmp_update = tmp_FaultEntryForProject.getDelete();

			currentEditProjectForm.initEditFormsSelection();
			if ((tmp_view == true) && (tmp_update == true)) {
				logger.info("[" + getUserName() + "/SimplexBean/toggleUpdateFaultProjectEntry] info");
			}

			// Update the fault.
			if ((tmp_view == true) && (tmp_update == false)) {

				currentEditProjectForm.currentFault = populateFaultFromContext(tmp_faultName);
				currentEditProjectForm.renderCreateNewFaultForm = !currentEditProjectForm.renderCreateNewFaultForm;

			}

			// This is the deletion case.
			if ((tmp_update == true) && (tmp_view == false)) {

				// Delete from the database.
				// This requires we first search for the desired object
				// and then delete the specific value that we get back.
				logger.info("[" + getUserName() + "/SimplexBean/toggleUpdateFaultProjectEntry] Deleteing " + tmp_faultName + "from db");

				db = Db4o.openFile(getBasePath() + "/" + getContextBasePath()
						+ "/" + userName + "/" + codeName + "/" + projectName
						+ ".db");

				Fault todelete = new Fault();
				todelete.setFaultName(tmp_faultName);
				ObjectSet result = db.get(todelete);
				if (result.hasNext()) {
					todelete = (Fault) result.next();
					db.delete(todelete);
				}
			}

		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/toggleUpdateFaultProjectEntry] " + e);
		}
		finally {
			if (db != null) db.close();			
		}

		// Print this out as KML
		faultKmlUrl = createFaultKmlFile();
	}

	public void toggleUpdateFaults(ActionEvent ev) {
		logger.info("[" + getUserName() + "/SimplexBean/toggleUpdateFaults] started...");
		String faultStatus = "Update";
		
		ObjectContainer db = null;
		
		try {
			int iSelectFault = -1;
			//Open the database
			db = Db4o.openFile(getBasePath() + "/"
									 + getContextBasePath() + "/" + userName + "/"
									 + codeName + "/" + projectName + ".db");

			// Find out which fault was selected.
			for (int i = 0; i < myFaultsForProjectList.size(); i++) {
				Fault tmp_Fault = new Fault();
				tmp_Fault = (Fault) myFaultsForProjectList.get(i);

				// This is the info about the fault.
				String tmp_faultName = ((faultEntryForProject) myFaultEntryForProjectList
						.get(i)).getoldFaultName();

				boolean tmp_update = ((faultEntryForProject) myFaultEntryForProjectList
						.get(i)).getUpdate();
				boolean tmp_delete = ((faultEntryForProject) myFaultEntryForProjectList
						.get(i)).getDelete();

				currentEditProjectForm.initEditFormsSelection();
				if ((tmp_update == true) && (tmp_delete == true)) {
					logger.info("[" + getUserName() + "/SimplexBean/toggleUpdateFaults] error");
				}

				// Update the fault.
				if ((tmp_update == true) && (tmp_delete == false)) {

					logger.info("[" + getUserName() + "/SimplexBean/toggleUpdateFaults] Updating "
							+ tmp_Fault.getFaultName() + "(old name)"
							+ tmp_faultName + " " + tmp_Fault.getFaultSlip());


					Fault toUpdate = new Fault();
					toUpdate.setFaultName(tmp_faultName);
					ObjectSet result = db.get(toUpdate);

					if (result.hasNext()) {
						toUpdate = (Fault) result.next();

						toUpdate.setFaultDepth(tmp_Fault.getFaultDepth());
						toUpdate.setFaultDepthVary(tmp_Fault.isFaultDepthVary());
						toUpdate.setFaultDipAngle(tmp_Fault.getFaultDipAngle());
						toUpdate.setFaultDipAngleVary(tmp_Fault.isFaultDipAngleVary());
						toUpdate.setFaultDipSlipVary(tmp_Fault.isFaultDipSlipVary());
						toUpdate.setFaultLatEnds(tmp_Fault.getFaultLatEnds());
						toUpdate.setFaultLatStarts(tmp_Fault.getFaultLatStarts());
						toUpdate.setFaultLength(tmp_Fault.getFaultLength());
						toUpdate.setFaultLengthVary(tmp_Fault.isFaultLengthVary());
						toUpdate.setFaultLocationX(tmp_Fault.getFaultLocationX());
						toUpdate.setFaultLocationY(tmp_Fault.getFaultLocationY());
						toUpdate.setFaultLonEnds(tmp_Fault.getFaultLonEnds());
						toUpdate.setFaultLonStarts(tmp_Fault.getFaultLonStarts());
						toUpdate.setFaultName(tmp_Fault.getFaultName());

						((faultEntryForProject) myFaultEntryForProjectList
								.get(i)).setoldFaultName(tmp_Fault
								.getFaultName());

						toUpdate.setFaultOriginXVary(tmp_Fault.isFaultOriginXVary());
						toUpdate.setFaultOriginYVary(tmp_Fault.isFaultOriginYVary());
						toUpdate.setFaultRakeAngle(tmp_Fault.getFaultRakeAngle());
						toUpdate.setFaultSlip(tmp_Fault.getFaultSlip());
						toUpdate.setFaultStrikeAngle(tmp_Fault.getFaultStrikeAngle());
						toUpdate.setFaultStrikeAngleVary(tmp_Fault.isFaultStrikeAngleVary());
						toUpdate.setFaultStrikeSlipVary(tmp_Fault.isFaultStrikeSlipVary());
						toUpdate.setFaultWidth(tmp_Fault.getFaultWidth());
						toUpdate.setFaultWidthVary(tmp_Fault.isFaultWidthVary());
					}
					db.set(toUpdate);
					db.commit();
				}

				// This is the deletion case.
				if ((tmp_update == false) && (tmp_delete == true)) {

					// Delete from the database.
					// This requires we first search for the desired object
					// and then delete the specific value that we get back.
					logger.info("[" + getUserName() + "/SimplexBean/toggleUpdateFaults] Deleteing "
							+ tmp_faultName + "from db");

					Fault todelete = new Fault();
					todelete.setFaultName(tmp_faultName);
					ObjectSet result = db.get(todelete);
					if (result.hasNext()) {
						todelete = (Fault) result.next();
						db.delete(todelete);
					}
				}
			}

			//Close the database.
			if (db != null)  db.close();			

		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/toggleUpdateFaults] " + e);
		}
		finally {
			if (db != null) db.close();			
		}
		// Print this out as KML
		faultKmlUrl = createFaultKmlFile();
	}
	 
	 public void toggleAddAriaObsvForProject(ActionEvent ev){
		currentEditProjectForm.initEditFormsSelection();
		
		ObjectContainer db = null;
		
		try {
			 db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
									  + userName + "/" + codeName + "/" + projectName + ".db");
			 Observation[] tmpObsv=new Observation[3];
			 
			 StringTokenizer st1, st2;
			 st1 = new StringTokenizer(currentEditProjectForm.getObsvAriaTextArea().trim(), "\n");
			 String line;
			 
			 while (st1.hasMoreTokens()) {
				  line = st1.nextToken();
				  if(line.indexOf("#")>-1) {
						logger.info("Comment line: "+line);
				  }
				  else {
						// Should accept spaces, tabs, commas
						st2 = new StringTokenizer(line.trim(), "\t , "); 
						logger.debug("Aria line token count: "+st2.countTokens());
						tmpObsv[0] = new Observation();
						tmpObsv[1] = new Observation();
						tmpObsv[2] = new Observation();  //Not used in V0
						
						if(st2.countTokens()==ARIA_TOKEN_COUNT_V0) {
							 logger.debug("Detected V0 style ARIA input file");							 

							 String firstToken=st2.nextToken();
							 //Not a comment but make sure the line is properly formatted.
							 
							 //NOTE: We don't have anything to check for malformed lines.
							 //Each row corresponds to two simplex observations: one
							 //"east" type and one "north" type.
							 
							 String lon=firstToken;
							 String lat=st2.nextToken();
							 String eastDisp=st2.nextToken();
							 String northDisp=st2.nextToken();
							 String errorDisp=st2.nextToken();
							 //Next two are not used.
							 String zero1=st2.nextToken();
							 String zero2=st2.nextToken();
							 String stationName=st2.nextToken();
							 
							 //This is the East displacement
							 tmpObsv[0].setObsvType("1");							 
							 tmpObsv[0].setObsvName(projectName + stationName);
							 tmpObsv[0].setObsvValue(eastDisp);
							 tmpObsv[0].setObsvError(errorDisp);
							 tmpObsv[0].setObsvRefSite("1");
							 
							 //This is the North displacement
							 tmpObsv[1].setObsvType("2");							 
							 tmpObsv[1].setObsvName(projectName + stationName);
							 tmpObsv[1].setObsvValue(northDisp);
							 tmpObsv[1].setObsvError(errorDisp);
							 tmpObsv[1].setObsvRefSite("1");
							 
							 tmpObsv=setXYLocations(tmpObsv, lat, lon);
							 
							 db.set(tmpObsv[0]);
							 db.set(tmpObsv[1]);
						}
						else if(st2.countTokens()==ARIA_TOKEN_COUNT_V1) {
							 logger.debug("Detected V1 style ARIA input file");
							 String firstToken=st2.nextToken();
							 //Not a comment but make sure the line is properly formatted.
							 
							 //NOTE: We don't have anything to check for malformed lines.
							 //Each row corresponds to two simplex observations: one
							 //"east" type and one "north" type.
							 String lon=firstToken;
							 String lat=st2.nextToken();
							 String eastDisp=st2.nextToken();
							 String northDisp=st2.nextToken();
							 String vertDisp=st2.nextToken();
							 String errorDisp=st2.nextToken();
							 String stationName=st2.nextToken();
							 
							 //This is the East displacement
							 tmpObsv[0].setObsvType("1");							 
							 tmpObsv[0].setObsvName(projectName + stationName);
							 tmpObsv[0].setObsvValue(eastDisp);
							 tmpObsv[0].setObsvError(errorDisp);
							 tmpObsv[0].setObsvRefSite("1");
							 
							 //This is the North displacement
							 tmpObsv[1].setObsvType("2");							 
							 tmpObsv[1].setObsvName(projectName + stationName);
							 tmpObsv[1].setObsvValue(northDisp);
							 tmpObsv[1].setObsvError(errorDisp);
							 tmpObsv[1].setObsvRefSite("1");
							 
							 //This is the Vertical (Up) displacement
							 tmpObsv[2].setObsvType("3");							 
							 tmpObsv[2].setObsvName(projectName + stationName);
							 tmpObsv[2].setObsvValue(vertDisp);
							 tmpObsv[2].setObsvError(errorDisp);
							 tmpObsv[2].setObsvRefSite("1");
							 
							 tmpObsv=setXYLocations(tmpObsv, lat, lon);
							 
							 db.set(tmpObsv[0]);
							 db.set(tmpObsv[1]);
							 db.set(tmpObsv[2]);
						}
				  }
			 }
			 db.commit();
		} catch (Exception e) {
			 logger.error("[" + getUserName() + "/SimplexBean/toggleAddObsvTextAreaForProject] " + e);
			 e.printStackTrace();
		}
		finally {
			 if (db != null) db.close();			
		}
	 }

	 /**
	  * This is used to add a list of already formatted Simplex observation points.
	  * This is typically useful for importing projects developed outside the portal.
	  */
	public void toggleAddObsvTextAreaForProject(ActionEvent ev) {
		currentEditProjectForm.initEditFormsSelection();

		ObjectContainer db = null;
		
		try {

			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");
			logger.info("Parsing Output");
			Observation tmpObsv = new Observation();
			ObjectSet result = db.get(tmpObsv);
			int obsvCount = result.size();

			StringTokenizer st1, st2;
			st1 = new StringTokenizer(currentEditProjectForm.getObsvTextArea()
					.trim(), "\n");
			String line;

			while (st1.hasMoreTokens()) {
				line = st1.nextToken();
				st2 = new StringTokenizer(line.trim(), "\t , "); // Should
																	// accept
				// spaces, tabs,
				// commas
				if (st2.countTokens() == SIMPLEX_OBSV_COUNT) {
					tmpObsv = new Observation();
					while (st2.hasMoreTokens()) {
						 String obsvType=st2.nextToken();
						 String obsvLocationEast=st2.nextToken();
						 String obsvLocationNorth=st2.nextToken();
						 String obsvValue=st2.nextToken();
						 String obsvError=st2.nextToken();
						 
						 int obsvTypeInt=Integer.parseInt(obsvType);
						 if(obsvTypeInt<0){
							  tmpObsv.setObsvRefSite("-1");
							  tmpObsv.setObsvType(-obsvTypeInt+"");
						 }
						 else {
							  tmpObsv.setObsvRefSite("1");
							  tmpObsv.setObsvType(obsvTypeInt+"");
						 }

						tmpObsv.setObsvName(projectName + obsvCount);
						tmpObsv.setObsvLocationEast(obsvLocationEast);
						tmpObsv.setObsvLocationNorth(obsvLocationNorth);
						tmpObsv.setObsvValue(obsvValue);
						tmpObsv.setObsvError(obsvError);
					}
				} else {
					logger.warn("[" + getUserName() 
									+ "/SimplexBean/toggleAddObsvTextAreaForProject] Line malformed: " 
									+ line);
				}
				obsvCount++;
				db.set(tmpObsv);
			}
			db.commit();
		} catch (Exception e) {
			logger.error("[" + getUserName() 
							 + "/SimplexBean/toggleAddObsvTextAreaForProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
	}

	public void toggleAddObservationForProject(ActionEvent ev) {

		currentEditProjectForm.initEditFormsSelection();
		ObjectContainer db = null;
		
		try {

			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			Observation tmpObservation = new Observation();
			tmpObservation.setObsvName(currentEditProjectForm.currentObservation.getObsvName());
			logger.info("[" + getUserName() + "/SimplexBean/toggleAddObservationForProject] " + currentEditProjectForm.currentObservation.getObsvName());
			ObjectSet result = db.get(tmpObservation);
			if (result.hasNext()) {
				tmpObservation = (Observation) result.next();
				db.delete(tmpObservation);
			}
			db.set(currentEditProjectForm.currentObservation);
			db.commit();
		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/toggleAddObservationForProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
		reconstructMyObservationEntryForProjectList(projectName);
	}

	/**
	 * This adds or updates the fault and saves to the db. The fault form values
	 * are actually sent to the current editProjectForm object. The faults are
	 * also printed out as KML.
	 */
	public void toggleAddFaultForProject(ActionEvent ev) {
		currentEditProjectForm.initEditFormsSelection();

		
		logger.info("Check Project Origin: "
						+currentProjectEntry.getOrigin_lat()
						+currentProjectEntry.getOrigin_lon());
		
		double latStart=Double.parseDouble(currentEditProjectForm.currentFault.getFaultLatStarts());
		double lonStart=Double.parseDouble(currentEditProjectForm.currentFault.getFaultLonStarts());
		if(currentProjectEntry.getOrigin_lat()==projectEntry.DEFAULT_LAT
			|| currentProjectEntry.getOrigin_lon()==projectEntry.DEFAULT_LON ) {
			 currentProjectEntry.setOrigin_lat(latStart);
			 currentProjectEntry.setOrigin_lon(lonStart);
		}
		double projectOriginLat=currentProjectEntry.getOrigin_lat();
		double projectOriginLon=currentProjectEntry.getOrigin_lon();
		
		ObjectContainer db = null;
		
		try {
			 db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
									  + userName + "/" + codeName + "/" + projectName + ".db");
			 
			 Fault tmpfault = new Fault();
			 tmpfault.setFaultName(currentEditProjectForm.currentFault
										  .getFaultName());
			 ObjectSet result = db.get(tmpfault);
			 if (result.hasNext()) {
				  tmpfault = (Fault) result.next();
				  db.delete(tmpfault);
			 }
			 faultdrawing = false;
			 db.set(currentEditProjectForm.currentFault);
			 db.commit();
		} catch (Exception e) {
			 logger.error("[" + getUserName() + "/SimplexBean/toggleAddFaultForProject] " + e);
		}
		finally {
			 if (db != null) db.close();			
		}
		
		saveSimplexProjectEntry(currentProjectEntry);
		// Print this out as KML
		faultKmlUrl = createFaultKmlFile();
	}

	public void toggleDrawFaultFromMap(ActionEvent ev) {

		logger.info("[" + getUserName() + "/SimplexBean/toggleDrawFaultFromMap] started");

		currentEditProjectForm.createFaultFromMap();

		currentEditProjectForm.initEditFormsSelection();
		logger.info("[" + getUserName() + "/SimplexBean/toggleDrawFaultFromMap] currentFault.getFaultName() " + currentEditProjectForm.currentFault.getFaultName());

		ObjectContainer db = null;
		
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			Fault tmpfault = new Fault();
			tmpfault.setFaultName(currentEditProjectForm.currentFault
					.getFaultName());
			ObjectSet result = db.get(tmpfault);
			if (result.hasNext()) {
				tmpfault = (Fault) result.next();
				db.delete(tmpfault);
			}
			db.set(currentEditProjectForm.currentFault);
			db.commit();
		} catch (Exception e) {
			logger.error("[" + getUserName() + "/SimplexBean/toggleDrawFaultFromMap] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		saveSimplexProjectEntry(currentProjectEntry);
		// Print this out as KML
		faultKmlUrl = createFaultKmlFile();
	}

	 /**
	  * Add selected UNAVCO stations to the project.  
	  * Each station will actually correspond to 3 (N, E, and U) observations.
	  * TODO: may look more elegant if we used object casting. This will also remove
	  * the dangers of hardcoding the parameter names below, whic must correspond to the 
	  * values used in the JSF page.
	  */ 
	 public void toggleAddJSONGPSObsvForProject(ActionEvent ev) {
		  //		  logger.error("Unavaco JSON:"+this.getGpsJSONValues());
		  Observation[] obsvs=new Observation[3];
		  ObjectContainer db = null;
		  try{
				
				ObjectMapper mapper=new ObjectMapper();
				Map<String,Object> stationData=mapper.readValue(this.getGpsJSONValues(),Map.class);
				Iterator it=stationData.values().iterator();
				
				while(it.hasNext()) {
					 Map stationValues=(Map)it.next();

					 //This is an obsolete errorging method
					 // Iterator it2=stationValues.keySet().iterator();
					 // while(it2.hasNext()){
					 // 	  String stationKey=(String)it2.next();
					 // 	  logger.error(stationKey+"="+stationValues.get(stationKey));
					 // }

					 //The following assumes I know the key values.   These
					 //must be matched to UnavacoGPSMapPanel.
					 //East Stations
					 obsvs[0]=new Observation();
					 obsvs[0].setObsvName((String)stationValues.get("id"));
					 obsvs[0].setObsvType("1");
					 obsvs[0].setObsvRefSite("1");
					 obsvs[0].setObsvValue(stationValues.get("ve").toString());
					 obsvs[0].setObsvError(stationValues.get("stddevE").toString());
					 
					 //North stations
					 obsvs[1]=new Observation();
					 obsvs[1].setObsvName((String)stationValues.get("id"));
					 obsvs[1].setObsvType("2");
					 obsvs[1].setObsvRefSite("1");
					 obsvs[1].setObsvValue(stationValues.get("vn").toString());
					 obsvs[1].setObsvError(stationValues.get("stddevN").toString());
		
					 //Up stations
					 obsvs[2]=new Observation();
					 obsvs[2].setObsvName((String)stationValues.get("id"));
					 obsvs[2].setObsvType("3");
					 obsvs[2].setObsvRefSite("1");
					 obsvs[2].setObsvValue(stationValues.get("vu").toString());
					 obsvs[2].setObsvError(stationValues.get("stddevU").toString());

					 obsvs=setXYLocations(obsvs,
												 stationValues.get("y0").toString(),
												 stationValues.get("x0").toString());
					 
					 //Now open the DB and insert.
					 db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
											  + userName + "/" + codeName + "/" + projectName + ".db");
					 //Add the observations
					 for(int i=0;i<obsvs.length;i++){
						  db.set(obsvs[i]);
					 }
					 
					 db.commit();
					 db.close();

				}
		  }
		  catch(Exception ex){
				ex.printStackTrace();
		  }
		  finally {
				if(db != null) db.close();			
		  }
		  setSearcharea(false);
		  setGpsRefStation(false);
		  //Is this needed?
		  saveSimplexProjectEntry(currentProjectEntry);
	 }

	 /**
	  * Add selected GRWS GPS stations to a project.
	  */
	 public void toggleAddGPSObsvForProject(ActionEvent ev) {
		  readStations();
		  String dataUrl = "";
		  ObjectContainer db = null;
		  
		  try {
				db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
										 + userName + "/" + codeName + "/" + projectName + ".db");
				
				logger.info("[" + getUserName() 
										 + "/SimplexBean/toggleAddGPSObsvForProject]searcharea : " 
										 + getSearcharea());
				logger.info("[" + getUserName() 
										 + "/SimplexBean/toggleAddGPSObsvForProject] gpsRefStation : " 
										 + getGpsRefStation());
			
				for (int nA = 0; nA < mycandidateObservationsForProjectList.size() ; nA++) {
					 CandidateObservation co = 
						  (CandidateObservation) mycandidateObservationsForProjectList.get(nA);
					 logger.info("[" + getUserName() 
											  + "/SimplexBean/toggleAddGPSObsvForProject] Here are the choices:" 
											  + co.getStationName() + " "
											  + co.getGpsStationLat() + " " 
											  + co.getGpsStationLon() + " index " 
											  + co.getSelectedSource());
					 gpsStationName = co.getStationName().toLowerCase();
					 
					 logger.info("[" + getUserName() 
											  + "/SimplexBean/toggleAddGPSObsvForProject] size " 
											  + co.getStationSources().size());
					 String[] s = {"", ""};
					 
					 for (int nB = 0 ; nB < co.getStationSources().size() ; nB++) {					
						  logger.info("[" + getUserName() 
													+ "/SimplexBean/toggleAddGPSObsvForProject] No." 
													+ nB + " " 
													+ ((SelectItem) co.getStationSources().get(nB)).getLabel());
						  s = ((SelectItem) co.getStationSources().get(nB)).getLabel().split("/");
						  for (int nC = 0 ; nC < s.length ; nC++)
								logger.info("[" + getUserName() 
														 + "/SimplexBean/toggleAddGPSObsvForProject] " 
														 + s[nC]);
					 }
					 
					 dataUrl = s[1];
					 
					 logger.info("[" + getUserName() 
											  + "/SimplexBean/toggleAddGPSObsvForProject] dataUrl : " 
											  + dataUrl);
					 
					 Observation[] obsv = makeGPSObservationPoints(co.getStationName(),
																				  dataUrl, 
																				  getGpsRefStation());				
					 obsv = setXYLocations(obsv, co.getGpsStationLat(), co.getGpsStationLon()); 
					 
					 for (int i = 0; i < obsv.length; i++)
						  db.set(obsv[i]);
				}
				
				db.commit();
				mycandidateObservationsForProjectList.clear();
				selectedGpsStationName = "";
				gpsStationName = "";
				
		  } catch (Exception e) {
				logger.error("[" + getUserName() + "/SimplexBean/toggleAddGPSObsvForProject] " + e);
		  }
		  finally {
				if (db != null) db.close();			
		  }
		  setSearcharea(false);
		  saveSimplexProjectEntry(currentProjectEntry);
		  setGpsRefStation(false);
	 }

	 /**
	  * Reconstructs the lat/lon positions for a given project from the 
	  * project origin and the (x,y) coordinate.  We use the convention that
	  * latLonVals[0]=latitude and latLonVals[1]=longitude.
	  */
	 private double[] getObsvLatLonFromXY(Observation obsv){
		  double[] latLonVals=new double[2];
		  
		  //Note we are assuming here the project origin has 
		  //been set. This is a bad assumption.
		  double origin_lat = currentProjectEntry.getOrigin_lat();
		  double origin_lon = currentProjectEntry.getOrigin_lon();
		 
		  double xloc=Double.parseDouble(obsv.getObsvLocationEast());
		  double yloc=Double.parseDouble(obsv.getObsvLocationNorth());
		  
		  latLonVals[0]=yloc/111.32+origin_lat;
		  latLonVals[1]=xloc/factor(origin_lon,origin_lat)+origin_lon;

		  return latLonVals;
	 }

	 /**
	  * Returns the relative cartesian coordinates for a given observation.
	  * Note we throw away the original lat/lon values.
	  * 
	  * REVIEW: Really should keep the lat/lon, but modifying Observation.java
	  * and all the related server-side code is tricky.  Also unsure how well
	  * the database query stuff would work (forward and backward compatibility).
	  */
	private Observation[] setXYLocations(Observation[] obsv,
			String gpsStationLat, String gpsStationLon) {
		// Get project origin.
		double origin_lat = currentProjectEntry.getOrigin_lat();
		double origin_lon = currentProjectEntry.getOrigin_lon();

		// Make these conversions
		double gpsLat = Double.parseDouble(gpsStationLat);
		double gpsLon = Double.parseDouble(gpsStationLon);

		// Project origin is not set, so set it.
		if (origin_lat == projectEntry.DEFAULT_LAT
				&& origin_lon == projectEntry.DEFAULT_LON) {
			currentProjectEntry.setOrigin_lat(gpsLat);
			currentProjectEntry.setOrigin_lon(gpsLon);
			for (int i = 0; i < obsv.length; i++) {
				// We are at the origin.
				obsv[i].setObsvLocationEast("0.0");
				obsv[i].setObsvLocationNorth("0.0");
			}
		}
		// Find where we are.
		else {
			double x = (gpsLon - origin_lon) * factor(origin_lon, origin_lat);
			double y = (gpsLat - origin_lat) * 111.32;
			for (int i = 0; i < obsv.length; i++) {
				// We are at the origin.
				obsv[i].setObsvLocationEast(format.format(x));
				obsv[i].setObsvLocationNorth(format.format(y));
			}
		}
		// We have probably updated the origin, so save to db.
		return obsv;
	}
	 

	 /**
	  * This converts a single selected station in to 3 observation
	  * points (for East, North, and Up).
	  */
	private Observation[] makeGPSObservationPoints(String stationName,
			String rawGRWSResponse, boolean gpsRefStation) {

		Observation[] observations = new Observation[3];
		// Check the response and only take the latest value.
		StringTokenizer st = new StringTokenizer(rawGRWSResponse, "\n");

		int lastToken = st.countTokens();
		int icount = 0;
		String space = " ";
		while (st.hasMoreTokens()) {
			icount++;
			String line = st.nextToken();
			if (icount == lastToken) {
				// parse the line
				String[] neu = new String[3];
				String[] sig_neu = new String[3];
				// Simplex uses enu instead of neu order.
				String[] simplexObType = { "2", "1", "3" };
				String[] simplexObsvName = { "north", "east", "up" };

				StringTokenizer st2 = new StringTokenizer(line, " ");
				String station = st2.nextToken();
				String date = st2.nextToken();
				neu[0] = st2.nextToken();
				neu[1] = st2.nextToken();
				neu[2] = st2.nextToken();
				sig_neu[0] = st2.nextToken();
				sig_neu[1] = st2.nextToken();
				sig_neu[2] = st2.nextToken();

				for (int i = 0; i < 3; i++) {
					observations[i] = new Observation();
					observations[i].setObsvName(station + "_"
							+ simplexObsvName[i]);
					observations[i].setObsvType(simplexObType[i]);
					observations[i].setObsvValue(neu[i]);
					observations[i].setObsvError(sig_neu[i]);
					// Note all 3 obsv values for this one station
					// will be set to this value.
					if (gpsRefStation) {
						observations[i].setObsvRefSite("-1");
					} else {
						observations[i].setObsvRefSite("1");
					}
				}
			}

		}
		return observations;
	}
	

	public void toggleCloseMap(ActionEvent ev) {
		logger.info("[" + getUserName() + "/SimplexBean/toggleCloseMap] Turn off the map display");
		//		getCurrentEditProjectForm().setRenderGPSStationMap(false);	
		getCurrentEditProjectForm().initEditFormsSelection();
	}

	protected static String getRealPath() {
		String path = ".";
		try {
			path = FacesContext.getCurrentInstance().getApplication()
					.getClass().getResource("/")
					+ "../../downloads/";
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
					+ "/downloads/";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * Generate a ticket. This can be used to make "gentle" status queries
	 * later.
	 */
	protected String generateTimeStamp() {
		// String stringDate=(new Date().getTime())+"";
		// String stringDate="NOW";
		// short s=1;
		String stringDate = (new UID().toString());
		return stringDate;
	}

	/**
	 * This will delete projects
	 */
	public void toggleDeleteProjectSummary(ActionEvent ev) {
		
		ObjectContainer db = null;
		
		try {
			logger.info("[" + getUserName() 
							 + "/SimplexBean/toggleDeleteProjectSummary] Getting selected archived project row");
			HtmlDataTable hdt=getMyArchiveDataTable();
			logger.info(hdt.getRowCount()+hdt.getId());
			SimpleXOutputBean dpsb = (SimpleXOutputBean) (getMyArchiveDataTable()
					.getRowData());

			logger.info("[" + getUserName() + "/SimplexBean/toggleDeleteProjectSummary] " 
							 + getBasePath() + "/" + getContextBasePath() + "/"+ userName 
							 + "/" + codeName + "/" + dpsb.getProjectName()+ ".db");

			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" 
									 + userName + "/" + codeName + "/" + dpsb.getProjectName() + ".db");

			logger.info("[" + getUserName() 
							 + "/SimplexBean/toggleDeleteProjectSummary] Found project:" 
							 + dpsb.getProjectName() + " " + dpsb.getJobUIDStamp());
			ObjectSet results = db.get(dpsb);
			logger.info("[" + getUserName() 
							 + "/SimplexBean/toggleDeleteProjectSummary] Result size: " + results.size());
			// Should only have one value.
			if (results.hasNext()) {
				dpsb = (SimpleXOutputBean) results.next();
				db.delete(dpsb);
			}
		} catch (Exception e) {
			logger.info("[" + getUserName() + "/SimplexBean/toggleDeleteProjectSummary] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

	}

	public UIData getMyArchiveDataTable2() {
		return this.myArchiveDataTable2;
	}

	public void setMyArchiveDataTable2(UIData myArchiveDataTable2) {
		this.myArchiveDataTable2 = myArchiveDataTable2;
	}

	public HtmlDataTable getMyArchiveDataTable() {
		return this.myArchiveDataTable;
	}

	public void setMyArchiveDataTable(HtmlDataTable myArchiveDataTable) {
		this.myArchiveDataTable = myArchiveDataTable;
	}

	/**
	 * Used for selecting the data to plot
	 */
	public void togglePlotProject(ActionEvent ev) {
		try {
			if (getMyArchiveDataTable() != null) {
				logger.info("[" + getUserName() 
										 + "/SimplexBean/togglePlotProject] getMyArchiveDataTable() isn't null");
				SimpleXOutputBean dpsb = (SimpleXOutputBean) (getMyArchiveDataTable()
						.getRowData());

				String kmlUrlString = dpsb.getKmlUrls()[0];
				String kmlName = kmlUrlString.substring(kmlUrlString.lastIndexOf("/") + 1, kmlUrlString.length());

				downloadKmlFile(kmlUrlString, this.getBasePath() + "/"+ codeName + "/" + kmlName);

				logger.info("[" + getUserName() + "/SimplexBean/togglePlotProject] kmlpath :  "
						+ this.getBasePath() + "/" + codeName + "/"
						+ kmlName);
				setKmlProjectFile(kmlName);
			} else
				logger.info("[" + getUserName() + "/SimplexBean/togglePlotProject] getMyArchiveDataTable() is null");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void setKmlProjectFile(String kmlProjectFile) {
		this.kmlProjectFile = kmlProjectFile;
	}

	public String getKmlProjectFile() {
		return this.kmlProjectFile;
	}

	protected void downloadKmlFile(String kmlUrlString, String localDestination) {
		try {
			URL kmlUrl = new URL(kmlUrlString);
			URLConnection uconn = kmlUrl.openConnection();
			InputStream in = kmlUrl.openStream();
			OutputStream out = new FileOutputStream(localDestination);

			// Extract the name of the file from the url.

			byte[] buf = new byte[1024];
			int length;
			while ((length = in.read(buf)) > 0) {
				out.write(buf, 0, length);
			}
			in.close();
			out.close();
		} catch (Exception ex) {
			logger.info("[" + getUserName() + "/SimplexBean/downloadKmlFile] Unable to download kml file");
			ex.printStackTrace();
		}
	}

	/**
	 * Sort the list by date
	 */

	protected List sortByDate(List fullList) {
		if (fullList == null)
			return null;
		int size = fullList.size();
		if (size < 2) {
			return fullList;
		}
		// Ordered list is originally empty and reducedlist is full.
		List orderedList = new ArrayList();
		List reducedList = new ArrayList();
		myListToVectorCopy(reducedList, fullList);

		orderedList = setListOrder(orderedList, reducedList, fullList);

		return orderedList;
	}

	protected void myListToVectorCopy(List dest, List src) {
		for (int i = 0; i < src.size(); i++) {
			dest.add(new Integer(i));
		}
	}

	protected List setListOrder(List orderedList, List reducedList,
			List fullList) {

		if (reducedList == null)
			return null;
		int size = reducedList.size();
		if (size < 2) {
			return fullList;
		}
		while (reducedList != null && reducedList.size() > 0) {
			int first = getFirst(reducedList, fullList);
			orderedList.add((SimpleXOutputBean) fullList
					.get(((Integer) reducedList.get(first)).intValue()));
			reducedList.remove(first);
		}
		return orderedList;
	}

	protected int getFirst(List reducedList, List fullList) {
		int first = 0;
		for (int i = 1; i < reducedList.size(); i++) {
			SimpleXOutputBean mb1 = (SimpleXOutputBean) fullList
					.get(((Integer) reducedList.get(first)).intValue());
			SimpleXOutputBean mb2 = (SimpleXOutputBean) fullList
					.get(((Integer) reducedList.get(i)).intValue());
			if (mb1.getCreationDate() == null
					|| mb1.getCreationDate().equals("")) {
				mb1.setCreationDate((new Date()).toString());
			}
			if (mb2.getCreationDate() == null
					|| mb2.getCreationDate().equals("")) {
				mb2.setCreationDate((new Date()).toString());
			}
			Date date1 = new Date(mb1.getCreationDate());
			Date date2 = new Date(mb2.getCreationDate());
			if (date2.after(date1))
				first = i;
		}

		return first;
	}

	 /**
	  * Create an kml file of observations. This method is currently 
	  * broken since we don't keep track of the observation points'
	  * original lat/lons.
	  */
	 public String createObsvKmlFile(){
		  String newObsvFilename = "";
		  String oldLocalDestination = this.getBasePath() + "/" + codeName
				+ "/" + getObsvKmlFilename();
		  String localDestination = "";
		  
		  try {
				// Remove the previous file.
				logger.info("[" + getUserName() 
								+ "/SimplexBean/createObsvKmlFile] Old obsv kml file:" 
								+ localDestination);
				File oldFile = new File(oldLocalDestination);
				if (oldFile.exists()) {
					 logger.info("[" + getUserName() 
									 + "/SimplexBean/createObsvKmlFile] Deleting old obsv kml file");
					 oldFile.delete();
				}
				
				// Create the new file.
				long timeStamp = (new Date()).getTime();
				newObsvFilename = userName + "-" + codeName + "-" + projectName
					 + "-" + "observations"+"-"+timeStamp + ".kml";
				setObsvKmlFilename(newObsvFilename);
				// This should be the new file name.
				localDestination = this.getBasePath() + "/" 
					 + codeName + "/"
					 + getObsvKmlFilename();
				
				Observation[] obsvs = getObservationsFromDB();
				PrintWriter out = new PrintWriter(new FileWriter(localDestination));
				logger.info("Number of observations:"+obsvs.length);
				
				if (obsvs != null && obsvs.length > 0) {
					 out.println(xmlHead);
					 out.println(kmlHead);
					 out.println(docBegin);
					 for (int i = 0; i < obsvs.length; i++) {
						  double[] obsvLatLon=getObsvLatLonFromXY(obsvs[i]);
						  out.println(pmBegin);
						  out.println(descBegin);
						  out.println("<b>Obsv: </b>" + obsvs[i].getObsvName());
						  out.println(descEnd);
						  out.println(pointBegin);
						  out.println(coordBegin);
						  out.println(obsvLatLon[1] + comma
										  + obsvLatLon[0] + comma + "0");
						  out.println(coordEnd);
						  out.println(pointEnd);
						  out.println(pmEnd);
					 }
					 out.println(docEnd);
					 out.println(kmlEnd);
					 out.flush();
					 out.close();
				}
		  } catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
		  String returnString = portalBaseUrl + "/Simplex3/"
				+ getObsvKmlFilename();
		  logger.info("[" + getUserName() + "/SimplexBean/createObsvKmlFile] KML:" + returnString);
		  return returnString;
	 }

	public String getObsvKmlFilename() {
		return obsvKmlFilename;
	}

	public void setObsvKmlFilename(String obsvKmlFilename) {
		this.obsvKmlFilename = obsvKmlFilename;
	}

	 //Note this overrides the previous KML file every time it is called.
	 public String getObsvKmlUrl() {
		  obsvKmlUrl=createObsvKmlFile();
		  return obsvKmlUrl;
	 }

	 public void setObsvKmlUrl(String obsvKmlUrl){
		  this.obsvKmlUrl=obsvKmlUrl;
	 }

	/**
	 * Create a KML file of the faults. The method assumes access to global
	 * variables.
	 */
	public String createFaultKmlFile() {
		String newFaultFilename = "";
		String oldLocalDestination = this.getBasePath() + "/" + codeName
				+ "/" + getFaultKmlFilename();
		String localDestination = "";

		try {
			// Remove the previous file.
			logger.debug("[" + getUserName() 
							+ "/SimplexBean/createFaultKmlFile] Old fault kml file:" + localDestination);
			File oldFile = new File(oldLocalDestination);
			if (oldFile.exists()) {
				logger.debug("[" + getUserName() 
								+ "/SimplexBean/createFaultKmlFile] Deleting old fault kml file");
				oldFile.delete();
			}

			// Create the new file.
			long timeStamp = (new Date()).getTime();
			newFaultFilename = userName + "-" + codeName + "-" + projectName
					+ "-" + "faults"+ "-" +timeStamp + ".kml";
			setFaultKmlFilename(newFaultFilename);
			// This should be the new file name.
			localDestination = this.getBasePath() + "/" + codeName + "/"
					+ getFaultKmlFilename();

			Fault[] faults = getProjectFaultsFromDB(userName, projectName,
					codeName, getBasePath(), getContextBasePath());
			PrintWriter out = new PrintWriter(new FileWriter(localDestination));

			if (faults != null && faults.length > 0) {
				out.println(xmlHead);
				out.println(kmlHead);
				out.println(docBegin);
				for (int i = 0; i < faults.length; i++) {
					out.println(pmBegin);
					out.println(descBegin);
					out.println("<b>Fault: </b>" + faults[i].getFaultName());
					out.println(descEnd);
					out.println(lsBegin);
					out.println(coordBegin);
					out.println(faults[i].getFaultLonStarts() + comma
							+ faults[i].getFaultLatStarts() + comma + "0");
					out.println(faults[i].getFaultLonEnds() + comma
							+ faults[i].getFaultLatEnds() + comma + "0");
					out.println(coordEnd);
					out.println(lsEnd);
					out.println(pmEnd);
				}
				out.println(docEnd);
				out.println(kmlEnd);
				out.flush();
				out.close();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String returnString = portalBaseUrl + "/Simplex3/"
				+ getFaultKmlFilename();
		logger.info("[" + getUserName() + "/SimplexBean/createFaultKmlFile] KML:" + returnString);
		return returnString;
	}

	public String getFaultKmlFilename() {
		return faultKmlFilename;
	}

	public void setFaultKmlFilename(String faultKmlFilename) {
		this.faultKmlFilename = faultKmlFilename;
	}

	 //REVIEW: Note this creates a NEW url every single time it is called, and
	 //it removes the previous KML file.  It is not your usual, inert get function.  
	 //Only call it once per page.
	public String getFaultKmlUrl() {
		faultKmlUrl = createFaultKmlFile();
		return faultKmlUrl;
	}

	public void setFaultKmlUrl(String faultKmlUrl) {
		this.faultKmlUrl = faultKmlUrl;
	}

	public String getPortalBaseUrl() {
		return portalBaseUrl;
	}

	public void setPortalBaseUrl(String portalBaseUrl) {
		this.portalBaseUrl = portalBaseUrl;
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

	public String[] getCopyProjectsList() {
		return this.copyProjectsList;
	}

	public void setCopyProjectsList(String[] copyProjectsList) {
		this.copyProjectsList = copyProjectsList;
	}

	 public void setSelectedGPSJSONValues(String selectedGPSJSONValues){
		  this.selectedGPSJSONValues=selectedGPSJSONValues;
	 }

	 public String getSelectedGPSJSONValues() {
		  logger.debug("Getting the selected gps stations' json values.");
		  Map<String,String> selectedStations=new HashMap();		  
		  
		  List stationList=getMyObservationEntryForProjectList();
		  logger.debug("List size:"+stationList.size());

		  ObjectMapper mapper=new ObjectMapper();
		  for(int i=0;i<stationList.size();i++){
				String stationName=((observationEntryForProject)stationList.get(i)).getObservationName();
				logger.debug(stationName);
				if(!selectedStations.containsKey(stationName)){
					 selectedStations.put(stationName,stationName);
				}
		  }
		  try {
				selectedGPSJSONValues=mapper.writeValueAsString(selectedStations);
		  }
		  catch(Exception ex){
				ex.printStackTrace();
		  }
		  return selectedGPSJSONValues;
	 }
}
