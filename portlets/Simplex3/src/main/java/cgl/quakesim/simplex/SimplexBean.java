package cgl.quakesim.simplex;

//Imports from the mother ship
import java.io.*;
import java.net.*;
import java.rmi.server.UID;
import java.util.*;
import java.text.NumberFormat;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.UIData;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.servogrid.genericproject.GenericSopacBean;

import sun.misc.BASE64Encoder;
import WebFlowClient.cm.ContextManagerImp;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;


import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

/**
 * Everything you need to set up and run SimpleBean.
 */

public class SimplexBean extends GenericSopacBean {
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

    //These are properties needed if you want to include a query
    //to the GRWS web service.

	// Variables that we need to get from the parent.
	// ContextManagerImp cm=null;
	// boolean isInitialized=false;

	// Simplex Bean staff
	// Protected String codeName = "Simplex3";
	// modified to be from faces-config.xml 2010/1/8 Jun Ji

	String codeName = "";
	private int SIMPLEX_OBSV_COUNT=5;

	// This is the db4o database
	ObjectContainer db = null;

	// --------------------------------------------------
	// Set some variables. Need to put in properties.
	// --------------------------------------------------
	String FAULTS = "Faults";
	 
	 String SEPARATOR = "/";
	 
	 String OBSERVATIONS = "Observations";
	 
	 // member for Simplex
	 projectEntry currentProjectEntry = new projectEntry();
	 
	 String selectdbURL="";
	 
	 editProjectForm currentEditProjectForm;
	 
	 List myFaultEntryForProjectList = new ArrayList();
	 
	 List myObservationEntryForProjectList = new ArrayList();
	 
	 String[] selectProjectsList;
	 
	 List myProjectNameList = new ArrayList();
	 
	 String[] deleteProjectsList;
	 
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
	 String kmlGeneratorBaseurl= "";
	 String kmlGeneratorUrl = "";
	 
	 SimpleXOutputBean projectSimpleXOutput;
	 
	 HtmlDataTable myArchiveDataTable;
	 UIData myArchiveDataTable2;
	 
	 String kmlProjectFile="network0.kml";
	 
	 String gpsStationName="";
	 String gpsStationLat="";
	 String gpsStationLon="";
	 boolean gpsRefStation=false;
	 boolean searcharea=false;

	 String selectedGpsStationName="";
 	 
 	 String selectedminlon=null;
 	 String selectedmaxlon=null;	
 	 String selectedminlat=null;
	 String selectedmaxlat=null; 
	 
 	 String[] latArray;
	 String[] lonArray;
	 String[] nameArray;
 
	 
	 //These are needed for the SOPAC GRWS query.
	 //These dates are arbitrary but apparently needed.
	 String beginDate="2006-01-10";
	 String endDate="2008-04-10";
	 String resource="procVels";
	 String contextGroup="sopacGlobk";
	 String minMaxLatLon="";
	 String contextId="38";
	 String kmlfiles = "";

     NumberFormat format=null;

     protected String faultKmlUrl;
	 protected String faultKmlFilename;
	 protected String portalBaseUrl;

    
    public boolean getGpsRefStation(){
	return this.gpsRefStation;
    }
    public void setGpsRefStation(boolean gpsRefStation){
	this.gpsRefStation=gpsRefStation;
    }

	 public String getGpsStationName() {
		  return gpsStationName;
	 }

	 public void setGpsStationName(String gpsStationName) {
		  this.gpsStationName=gpsStationName;
	 }


	 public String getSelectedGpsStationName() {
		  return selectedGpsStationName;
	 }

	 public void setSelectedGpsStationName(String selectedGpsStationName) {
		  this.selectedGpsStationName=selectedGpsStationName;
	 }
	 
 	 public boolean getSearcharea() {
 		return searcharea;
 	 }
 	 public void setSearcharea(boolean searcharea) {
 		this.searcharea = searcharea;
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
		  this.gpsStationLat=gpsStationLat;
	 }
	 
	 public String getGpsStationLon() {
		  return gpsStationLon;
	 }

	 public void setGpsStationLon(String gpsStationLon) {
		  this.gpsStationLon=gpsStationLon;
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
		  File projectDir = new File(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
											  + codeName + "/");
		  projectDir.mkdirs();
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

	 protected Fault[] getFaultsFromDB() {
		  Fault[] returnFaults = null;
		  db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
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
	 
	 protected Observation[] getObservationsFromDB() {
		  Observation[] returnObservations = null;
		  db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
									+ codeName + "/" + projectName + ".db");
		  Observation ObservationToGet = new Observation();
		  ObjectSet results = db.get(ObservationToGet);
		  if (results.hasNext()) {
				returnObservations = new Observation[results.size()];
				for (int i = 0; i < results.size(); i++) {
					 returnObservations[i] = (Observation) results.next();
				}
		  }
		  db.close();
		  
		  return returnObservations;
	 }

	public void setKmlGeneratorUrl(String tmp_str) {
		this.kmlGeneratorUrl=tmp_str;
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
		List tmpList=new ArrayList();

		myarchivedFileEntryList.clear();
		//		System.out.println("Project list size:"+myprojectlist.size());

		for (int i = 0; i < myprojectlist.size(); i++) {
			 String projectName = ((SelectItem) myprojectlist.get(i)).getLabel();
			 
			 SimpleXOutputBean mega = new SimpleXOutputBean();
			 mega.setProjectName(projectName);
			 // System.out.println("ProjectName: "+projectName);
			 db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
									  + codeName + "/" + projectName + ".db");
			 // ObjectSet results=db.get(mega);
			 //			 ObjectSet results = db.get(SimpleXOutputBean.class);
			 ObjectSet results = db.get(new SimpleXOutputBean());
			 //System.out.println("Matches for "+projectName+":"+results.size());
			 while (results.hasNext()) {
				  //mega = (SimpleXOutputBean) results.next();
				  //					myarchivedFileEntryList.add(mega);
				  SimpleXOutputBean sob=(SimpleXOutputBean)results.next();
				  tmpList.add(sob);
			 }
			 db.close();
			 myarchivedFileEntryList=sortByDate(tmpList);
		}
		
		return myarchivedFileEntryList;
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
		  //		  System.out.println("Reconstructing the project name list");
		  myProjectNameList.clear();
		  try {
				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
										 + codeName + ".db");
				projectEntry project = new projectEntry();
				ObjectSet results = db.get(projectEntry.class);
				// System.out.println("Got results:"+results.size());
				while (results.hasNext()) {
					 project = (projectEntry) results.next();
					 //	 System.out.println(project.getProjectName());
					 if(project==null || project.getProjectName()==null) {
						  db.delete(project);
					 }
					 else {
						  myProjectNameList.add(new SelectItem(project.getProjectName(),
																			project.getProjectName()));
					 }
				}
				db.close();
		  } 
		  catch (Exception ex) {
				// ex.printStackTrace();
				if(db!=null) db.close();
				System.err.println("Could not open " + getBasePath()+"/"+getContextBasePath() + "/"
										 + userName + "/" + codeName + ".db");
				System.err.println("Returning empty list.");
		  }
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
		  try {
				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
										 + codeName + "/" + projectName + ".db");
				
				Observation tmpobser = new Observation();
				ObjectSet results = db.get(tmpobser);
				while (results.hasNext()) {
					 tmpobser = (Observation) results.next();
					 observationEntryForProject tmp_myObservationEntryForProject = new observationEntryForProject();
					 tmp_myObservationEntryForProject.observationName = tmpobser
						  .getObsvName();
					 tmp_myObservationEntryForProject.view = false;
					 tmp_myObservationEntryForProject.delete = false;
					 if(tmpobser.getObsvRefSite().equals("-1")) {
						  tmp_myObservationEntryForProject.refSite = "Yes";
					 }
					 else if(tmpobser.getObsvRefSite().equals("1")) {
						  tmp_myObservationEntryForProject.refSite = "No";
					 }
					 //Just in case.
					 else {
						  tmp_myObservationEntryForProject.refSite = "No";
					 }
					 this.myObservationEntryForProjectList
						  .add(tmp_myObservationEntryForProject);
				}
				db.close();
		  } 
		  catch (Exception ex) {
				ex.printStackTrace();
				db.close();
		  }
		  return this.myObservationEntryForProjectList;
	 }

	 public List getMyObservationEntryForProjectList() {
		  String projectName = getProjectName();
		  System.out.println("ProjectName : " + projectName);
		  return reconstructMyObservationEntryForProjectList(projectName);
	 }
	 
	 public void setMyObservationEntryForProjectList(List tmp_list) {
		  this.myObservationEntryForProjectList = tmp_list;
	 }
	 
	 /**
	  * Create the Observation collection
	  */
	 protected List populateObservationCollection(List myObservationEntryProjectList) throws Exception {
		  List myObservationCollection = new ArrayList();
		  for (int i = 0; i < myObservationEntryProjectList.size(); i++) {
				observationEntryForProject tmp_ObservationEntryForProject = 
					 (observationEntryForProject) myObservationEntryForProjectList.get(i);
				String tmp_observationName = tmp_ObservationEntryForProject
					 .getObservationName();
				myObservationCollection
					 .add(populateObservationFromContext(tmp_observationName));
		  }
		  return myObservationCollection;
	 }
	 
	 protected Observation populateObservationFromContext(String tmp_observationName) throws Exception {
// 		  System.out.println("Populating Layer " + tmp_observationName + " for  "
// 								 + projectName);
		  String observationStatus = "Update";
		  
		  db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
									+ codeName + "/" + projectName + ".db");
		  
		  Observation observationToGet = new Observation();
		  observationToGet.setObsvName(tmp_observationName);
		  ObjectSet results = db.get(observationToGet);
		  // Should only have one value.
		  Observation currentObservation = null;
		  if (results.hasNext()) {
				currentObservation = (Observation) results.next();
		  }
		  db.close();
		  return currentObservation;
	 }
	 
	/**
	 * Reconstructs the fault entry list.
	 */
	protected List reconstructMyFaultEntryForProjectList(String projectName) {
		this.myFaultEntryForProjectList.clear();
		try {
			 db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
									  + codeName + "/" + projectName + ".db");
			 
			 Fault tmpfault = new Fault();
			 ObjectSet results = db.get(tmpfault);
			 while (results.hasNext()) {
				  tmpfault = (Fault) results.next();
				  faultEntryForProject tmp_myFaultEntryForProject = new faultEntryForProject();
				  tmp_myFaultEntryForProject
						.setFaultName(tmpfault.getFaultName());
				  tmp_myFaultEntryForProject.view = false;
				  tmp_myFaultEntryForProject.delete = false;
				  this.myFaultEntryForProjectList.add(tmp_myFaultEntryForProject);
			 }
			 db.close();
			 
		} catch (Exception ex) {
			 ex.printStackTrace();
			 db.close();
		}
		
		return this.myFaultEntryForProjectList;
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

	protected Fault populateFaultFromContext(String tmp_faultName)
			throws Exception {
		String faultStatus = "Update";

		db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
				+ codeName + "/" + projectName + ".db");
		Fault faultToGet = new Fault();
		faultToGet.setFaultName(tmp_faultName);
		ObjectSet results = db.get(faultToGet);
		// Should only have one value.
		Fault currentFault = null;
		if (results.hasNext()) {
			currentFault = (Fault) results.next();
		}
		db.close();
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
		  System.out.println("[SimplexBean] this.currentEditProjectForm.setKmlfiles( " + getKmlfiles() + ")");
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
	 
	 // common function for Simplex Bean.	 
	 public String getBASE64(String s) {
		  if (s == null)
				return null;
		  String tmp_str = (new BASE64Encoder()).encode(s.getBytes());
		  return tmp_str;
	 }
	 
	 /**
	  * This is a placeholder main method that may be used for commandline testing.
	  */ 
	 public static void main(String[] args) {
		  
	 }
	 
	 /**
	  * default empty constructor
	 * @throws IOException 
	  */

	 public void readStations(){
		    System.out.println("[readStations] : " + getCodeName());
		    File localFile = new File(getBasePath() + "/" + getCodeName() + "/" + "stations-rss-new.xml");
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
				statusDoc = reader.read( new StringReader(sb.toString()) );
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}          
		    Element eleXml = (Element)statusDoc.getRootElement();
		    List stationList = eleXml.elements("station");
		    latArray=new String[stationList.size()];
		    lonArray=new String[stationList.size()];
		    nameArray=new String[stationList.size()];
		    

		    //Set upt the arrays
		    for(int i=0;i<stationList.size();i++) {
		      Element station=(Element)stationList.get(i);
		      latArray[i]=station.element("latitude").getText();
			lonArray[i]=station.element("longitude").getText();
			nameArray[i]=station.element("id").getText();
		    }   




	 }
	 public SimplexBean() throws Exception {
		  super();
		  cm = getContextManagerImp();
		  format=NumberFormat.getInstance();

		  System.out.println("Simplex Bean Created");


	 }
	 
	 protected void initSimplexService() throws Exception {
		  simplexService = new SimpleXServiceServiceLocator()
				.getSimpleXExec(new URL(simpleXServiceUrl));
	 }
	  	 
	 public String toggleRunSimplex2() {
		  
		  Observation[] obsv = getObservationsFromDB();
		  Fault[] faults = getFaultsFromDB();
		  //		  String timeStamp = "";
		  String timeStamp=generateTimeStamp();
		  System.out.println("ProjectName:" + projectName);
		  try {
				initSimplexService();
				
				projectSimpleXOutput=simplexService.runSimplex(userName, projectName,
																			  faults, obsv, currentProjectEntry.startTemp,
																			  currentProjectEntry.maxIters,
																			  currentProjectEntry.getOrigin_lon()+"", 
																			  currentProjectEntry.getOrigin_lat()+"",
																			  this.kmlGeneratorUrl, timeStamp);
				
				System.out.println(projectSimpleXOutput.getProjectName());
				System.out.println(projectSimpleXOutput.getInputUrl());
				saveSimpleXOutputBeanToDB(projectSimpleXOutput);
				saveSimplexProjectEntry(currentProjectEntry);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  return ("Simplex2-back");
	 }

	 /**
	  * Save and if necessary reassign the project entry.
	  */
	 protected void saveSimplexProjectEntry(projectEntry currentProjectEntry) {
		  db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
									+ codeName +  ".db");
		  
		  ObjectSet results=db.get(projectEntry.class);
		  
		  System.out.println("Project entry: "+currentProjectEntry.getProjectName());
		  while(results.hasNext()) {
				projectEntry tmp=(projectEntry)results.next();
				if(tmp==null 
					|| tmp.getProjectName()==null
					|| tmp.getProjectName().equals(currentProjectEntry.getProjectName())) {
					 System.out.println("Updating/deleting project");
					 db.delete(tmp);
				}
		  }
		  db.set(currentProjectEntry);
		  db.close();
	 }
	 
	 /**
	  * Saves the simplex output to the db.
	  */
	 //	 protected void saveSimpleXOutputBeanToDB(SimpleXOutputBean projectSimpleXOutput) {
	 protected void saveSimpleXOutputBeanToDB(SimpleXOutputBean mega) {
		  // Set up the bean template for searching.
		  //		  SimpleXOutputBean mega = new SimpleXOutputBean();
		  // 		  mega.setProjectName(projectSimpleXOutput.getProjectName());
		  // 		  mega.setJobUIDStamp(projectSimpleXOutput.getJobUIDStamp());
		  // Find the matching bean
		  db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
									+ codeName + "/" + projectName  +".db");
		  // 				ObjectSet results = db.get(mega);
		  
		  // 				if (results.hasNext()) {
		  // 					 // Reassign the bean. Should only be one match.
		  // 					 mega = (SimpleXOutputBean) results.next();
		  // 				}
		  // 		  mega.setInputUrl(projectSimpleXOutput.getInputUrl());
		  // 		  mega.setLogUrl(projectSimpleXOutput.getLogUrl());
		  // 		  mega.setOutputUrl(projectSimpleXOutput.getOutputUrl());
		  // 		  mega.setFaultUrl(projectSimpleXOutput.getFaultUrl());
		  // 		  String[] kmlurls=projectSimpleXOutput.getKmlUrls();
		  // 		  mega.setKmlUrls(kmlurls);
		  db.set(mega);
		  db.commit();
		  db.close();
		  
	 }
	 
	 /**
	  * This creates a new project and related session bean objects. It is called by
	  * a JSF action method and so must return a navigation string.
	  */
	 public String NewProjectThenEditProject() {
		  String returnMessage="";
		  try {
				currentEditProjectForm=new editProjectForm(selectdbURL);
				currentEditProjectForm.setKmlfiles(getKmlfiles());
				currentEditProjectForm.setCodeName(getCodeName());
				createNewProject();
				currentEditProjectForm.init_edit_project();
				returnMessage="Simplex2-edit-project";
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  //The default value is blank, will load the original page.
		  return returnMessage;
	 }

	 /**
	  * Delete the selected project.  This is called by a JSF page.  Projects are
	  * deleted by name.
	  */
	 public String toggleDeleteProject() {
		  try {
				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName
										 + "/" + codeName + ".db");
				if (deleteProjectsList != null) {
					 for (int i = 0; i < deleteProjectsList.length; i++) {
						  System.out.println("Deleting:"+deleteProjectsList[i]);
						  //Delete the input bean
						  projectEntry delproj = new projectEntry();
						  ObjectSet results = db.get(projectEntry.class);
						  System.out.println("results size:"+results.size());
						  while (results.hasNext()) {
								delproj = (projectEntry) results.next();
								if(delproj.getProjectName().equals((String)deleteProjectsList[i])) {
									 System.out.println("Deleting:"+delproj.getProjectName());
									 db.delete(delproj);
								}
						  }
						  
						  //Delete the output bean
						  SimpleXOutputBean sxob = new SimpleXOutputBean();
						  sxob.setProjectName((String) deleteProjectsList[i]);
						  results = db.get(sxob);
						  if (results.hasNext()) {
								sxob = (SimpleXOutputBean) results.next();
								db.delete(sxob);
						  }
					 }
					 db.close();
				}
				else {
					 System.out.println("No projects selected for deletion.");
				}
		  } 
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
		  return "Simplex2-this";
	 }
	 
	 /**
	  * Loads the selected project from the database, sets the current project session variable.
	  * 
	  */ 
	 public String toggleSelectProject() {
		  currentEditProjectForm=new editProjectForm(selectdbURL);
		  currentEditProjectForm.setKmlfiles(getKmlfiles());
		  currentEditProjectForm.setCodeName(getCodeName());
		  currentEditProjectForm.initEditFormsSelection();
		  if (selectProjectsList != null) {
				for (int i = 0; i < 1; i++) {
					 this.projectName = selectProjectsList[0];
				}
		  }
		  
		  try {
				// Reconstruct the project lists
				myFaultEntryForProjectList=reconstructMyFaultEntryForProjectList(projectName);
				myObservationEntryForProjectList=reconstructMyObservationEntryForProjectList(projectName);
				
				// Reconstruct the fault and layer object collections from the
				// context
				myFaultCollection = populateFaultCollection(myFaultEntryForProjectList);
				myObservationCollection = populateObservationCollection(myObservationEntryForProjectList);
				
				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
										 + codeName + ".db");
				projectEntry tmp_proj = new projectEntry();
				tmp_proj.setProjectName(projectName);
				ObjectSet results = db.get(projectEntry.class);
				while (results.hasNext()) {
					 tmp_proj=(projectEntry)results.next();
					 if(tmp_proj.getProjectName()!=null 
						 && tmp_proj.getProjectName().equals(projectName)) {
						  System.out.println("Found project, reassigning");
						  this.currentProjectEntry=tmp_proj;
						  currentEditProjectForm.setProjectEntry(currentProjectEntry);
						  break;
					 } 
				}
				db.close();
				
				//Just for laughs, print out the origin to make sure it is OK.
				System.out.println("Project origin: "
										 +currentProjectEntry.getOrigin_lat()+" "
										 + currentProjectEntry.getOrigin_lon());
				
		  } 
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  currentEditProjectForm.projectSelectionCode = "";
		  currentEditProjectForm.faultSelectionCode = "";
		  return "Simplex2-edit-project";
	 }

	 /**
	  * This creates a new projectEntry object, stores it in the DB, and sets the currentProject
	  */
	 protected String createNewProject() throws Exception {
		  System.out.println("Creating new project");
		  makeProjectDirectory();
		  db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
									+ codeName + ".db");
// 		  projectEntry project = new projectEntry();
// 		  project.projectName = this.projectName;

		  projectEntry tmp_project = new projectEntry();
		  tmp_project.projectName = this.projectName;
		  ObjectSet result = db.get(projectEntry.class);

		  //Create the new project.  These may be overwritten below.
		  //		  currentProjectEntry=new projectEntry();
		  currentProjectEntry.setProjectName(projectName);
		  currentProjectEntry.setOrigin_lon(currentProjectEntry.DEFAULT_LON);
		  currentProjectEntry.setOrigin_lat(currentProjectEntry.DEFAULT_LAT);
// 		  currentProjectEntry.setMaxIters(currentProjectEntry.maxIters);
// 		  currentProjectEntry.setStartTemp(currentProjectEntry.startTemp);
		  
		  while (result.hasNext()) {
				tmp_project = (projectEntry) result.next();
				//Clean up any null projects
				if(tmp_project==null 
					|| tmp_project.getProjectName()==null) {
					 db.delete(tmp_project);
				}
				//This is an existing project, so load it and replace
				else if (tmp_project.getProjectName().equals(projectName)) {
					 db.delete(tmp_project);
					 currentProjectEntry=tmp_project;
// 					 currentProjectEntry.setMaxIters(tmp_project.maxIters);
// 					 currentProjectEntry.setStartTemp(tmp_project.startTemp);
					 break;
				}
		  }
		  db.set(currentProjectEntry);
		  db.commit();
		  db.close();
		  
		  currentEditProjectForm.setProjectEntry(currentProjectEntry);
		  
		  return "MG-set-project";
	 }
	 
	public void toggleUpdateObservationProjectEntry(ActionEvent ev) {

		String observationStatus = "Update";
		try {
			int iSelectObservation = -1;

			// Find out which Observation was selected.
			observationEntryForProject tmp_ObservationEntryForProject = new observationEntryForProject();
			for (int i = 0; i < myObservationEntryForProjectList.size(); i++) {
				tmp_ObservationEntryForProject = (observationEntryForProject) myObservationEntryForProjectList
						.get(i);
				if ((tmp_ObservationEntryForProject.getView() == true)
						|| (tmp_ObservationEntryForProject.getDelete() == true)) {
					iSelectObservation = i;
					break;
				}
			}

			// This is the info about the Observation.
			String tmp_ObservationName = tmp_ObservationEntryForProject
					.getObservationName();
			boolean tmp_view = tmp_ObservationEntryForProject.getView();
			boolean tmp_update = tmp_ObservationEntryForProject.getDelete();

			currentEditProjectForm.initEditFormsSelection();
			if ((tmp_view == true) && (tmp_update == true)) {
				System.out.println("error");
			}

			// Update the Observation.
			if ((tmp_view == true) && (tmp_update == false)) {

				currentEditProjectForm.currentObservation = (Observation) (populateObservationFromContext(tmp_ObservationName));
				currentEditProjectForm.renderCreateObservationForm = !currentEditProjectForm.renderCreateObservationForm;

			}

			// This is the deletion case.
			if ((tmp_update == true) && (tmp_view == false)) {

				// Delete from the database.
				// This requires we first search for the desired object
				// and then delete the specific value that we get back.
				System.out.println("Deleteing " + tmp_ObservationName
						+ "from db");
				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
						+ codeName + "/" + projectName + ".db");

				Observation todelete = new Observation();
				todelete.setObsvName(tmp_ObservationName);
				ObjectSet result = db.get(todelete);
				if (result.hasNext()) {
					todelete = (Observation) result.next();
					db.delete(todelete);
				}
				db.close();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	 /**
	  * This is obsolete.
	  */ 
	public String toggleGMTPlot() {
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
			this.gmtPlotPdf_timeStamp = timeStamp;
			if ((tmp_view == true)) {
				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
						+ codeName + ".db");
				projectEntry tmp_proj = new projectEntry();
				tmp_proj.projectName = this.projectName;
				ObjectSet results = db.get(tmp_proj);
				if (results.hasNext()) {
					this.currentProjectEntry = (projectEntry) results.next();
				} else {
					System.out.println("error: can not find this project");
				}
				db.close();
				System.out.println(currentProjectEntry.origin_lat);
				System.out.println(currentProjectEntry.origin_lon);

				initSimplexService();
				this.currentGMTViewForm = simplexService.runPlotGMT(userName,
																					 projectName, 
																					 currentProjectEntry.getOrigin_lat()+"",
																					 currentProjectEntry.getOrigin_lon()+"",
																					 timeStamp);
				this.gmtPlotPdfUrl = this.currentGMTViewForm.getGmtPlotPdfUrl();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ("Simplex2-gmt-view");

	}

	 /**
	  * This is an obsolete method.
	  */ 
	public String toggleMakeMap() {

		System.out.println("GMT Plot");

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
				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
						+ codeName + ".db");
				projectEntry tmp_proj = new projectEntry();
				tmp_proj.projectName = this.projectName;
				ObjectSet results = db.get(tmp_proj);
				if (results.hasNext()) {
					this.currentProjectEntry = (projectEntry) results.next();
				} else {
					System.out.println("error: can not find this project");
				}
				db.close();
				System.out.println(currentProjectEntry.origin_lat);
				System.out.println(currentProjectEntry.origin_lon);

				initSimplexService();
				this.mapXmlUrl = simplexService.runMakeMapXml(userName,
																			 projectName, 
																			 currentProjectEntry.getOrigin_lat()+"",
																			 currentProjectEntry.getOrigin_lon()+"",
																			 timeStamp);
				System.out.println(mapXmlUrl);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ("Simplex2-google-map");
	}
	
	public String toggleViewKml() {

		System.out.println("Kml viewer");

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
				// System.out.println("ProjectName: "+projectName);
				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
						+ codeName + "/" + projectName + ".db");
				ObjectSet results=db.get(mega);
				// System.out.println("Matches for
				// "+projectName+":"+results.size());
				if (results.hasNext()) {
					projectSimpleXOutput = (SimpleXOutputBean) results.next();
				}else {
					System.out.println("error: can not find this project for SimpleXOutputBean");
				}
				db.close();
				
				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
						+ codeName + ".db");
				projectEntry tmp_proj = new projectEntry();
				tmp_proj.projectName = this.projectName;
				ObjectSet results2 = db.get(tmp_proj);
				if (results2.hasNext()) {
					this.currentProjectEntry = (projectEntry) results2.next();
				} else {
					System.out.println("error: can not find this project");
				}
				db.close();
				System.out.println(currentProjectEntry.origin_lat);
				System.out.println(currentProjectEntry.origin_lon);
				
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ("Simplex2-kml-viewer");
	}
	 
	public void toggleUpdateFaultProjectEntry(ActionEvent ev) {

		String faultStatus = "Update";
		try {
			int iSelectFault = -1;

			// Find out which fault was selected.
			faultEntryForProject tmp_FaultEntryForProject = new faultEntryForProject();
			for (int i = 0; i < myFaultEntryForProjectList.size(); i++) {
				tmp_FaultEntryForProject = (faultEntryForProject) myFaultEntryForProjectList
						.get(i);
				if ((tmp_FaultEntryForProject.getView() == true)
						|| (tmp_FaultEntryForProject.getDelete() == true)) {
					iSelectFault = i;
					break;
				}
			}

			// This is the info about the fault.
			String tmp_faultName = tmp_FaultEntryForProject.getFaultName();
			boolean tmp_view = tmp_FaultEntryForProject.getView();
			boolean tmp_update = tmp_FaultEntryForProject.getDelete();

			currentEditProjectForm.initEditFormsSelection();
			if ((tmp_view == true) && (tmp_update == true)) {
				System.out.println("error");
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
				System.out.println("Deleteing " + tmp_faultName + "from db");
				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
						+ codeName + "/" + projectName + ".db");

				Fault todelete = new Fault();
				todelete.setFaultName(tmp_faultName);
				ObjectSet result = db.get(todelete);
				if (result.hasNext()) {
					todelete = (Fault) result.next();
					db.delete(todelete);
				}
				db.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		//Print this out as KML
		faultKmlUrl=createFaultKmlFile();
	}

	 public void toggleAddObsvTextAreaForProject(ActionEvent ev) {
		  currentEditProjectForm.initEditFormsSelection();
		  db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
									+ codeName + "/" + projectName + ".db");
		  //		  System.out.println("Parsing Output");
		  Observation tmpObsv = new Observation();
		  ObjectSet result = db.get(tmpObsv);
		  int obsvCount=result.size();
		 
		  StringTokenizer st1, st2;
		  st1=new StringTokenizer(currentEditProjectForm.getObsvTextArea().trim(),"\n");
		  String line;

		  while(st1.hasMoreTokens()) {
				line=st1.nextToken();
				st2=new StringTokenizer(line.trim(),"\t , ");  //Should accept spaces, tabs, commas
				if(st2.countTokens()==SIMPLEX_OBSV_COUNT) {
					 tmpObsv = new Observation();
					 while(st2.hasMoreTokens()){
						  tmpObsv.setObsvName(projectName+obsvCount);
						  tmpObsv.setObsvType(st2.nextToken());
						  tmpObsv.setObsvLocationEast(st2.nextToken());
						  tmpObsv.setObsvLocationNorth(st2.nextToken());
						  tmpObsv.setObsvValue(st2.nextToken());
						  tmpObsv.setObsvError(st2.nextToken());
						  tmpObsv.setObsvRefSite("1");						  
					 }
					 //					 System.out.println("\n");
				}
				else {
					 System.out.println("Line malformed: "+line);
				}
				obsvCount++;
				db.set(tmpObsv);
		  }
		  db.commit();
		  db.close();
	 }

	public void toggleAddObservationForProject(ActionEvent ev) {

		currentEditProjectForm.initEditFormsSelection();
		db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
				+ codeName + "/" + projectName + ".db");

		Observation tmpObservation = new Observation();
		tmpObservation.setObsvName(currentEditProjectForm.currentObservation
				.getObsvName());
		System.out.println(currentEditProjectForm.currentObservation
				.getObsvName());
		ObjectSet result = db.get(tmpObservation);
		if (result.hasNext()) {
			tmpObservation = (Observation) result.next();
			db.delete(tmpObservation);
		}
		db.set(currentEditProjectForm.currentObservation);
		db.commit();
		db.close();
		reconstructMyObservationEntryForProjectList(projectName);
	}

	 /**
	  * This adds or updates the fault and saves to the db.  
	  * The fault form values are actually
	  * sent to the current editProjectForm object.
	  * The faults are also printed out as KML.
	  */
	public void toggleAddFaultForProject(ActionEvent ev) {
		currentEditProjectForm.initEditFormsSelection();
		db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
				+ codeName + "/" + projectName + ".db");

		Fault tmpfault = new Fault();
		tmpfault.setFaultName(currentEditProjectForm.currentFault.getFaultName());
		ObjectSet result = db.get(tmpfault);
		if (result.hasNext()) {
			tmpfault = (Fault) result.next();
			db.delete(tmpfault);
		}
		db.set(currentEditProjectForm.currentFault);
		db.commit();
		db.close();
		
		saveSimplexProjectEntry(currentProjectEntry);
		//Print this out as KML
		faultKmlUrl=createFaultKmlFile();
	}


	 public void toggleAddGPSObsvForProject(ActionEvent ev) {
		 
		 readStations();
		 String dataUrl="";
		 db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");
		 
		 System.out.println ("searcharea : " + getSearcharea());
		 System.out.println ("gpsRefStation : " + getGpsRefStation());
		 
		 // if (searcharea == false) {
			 String[] stations = selectedGpsStationName.split(",");
			 for (int nA = 0 ; nA < stations.length ; nA++) {
				 System.out.println("Here are the choices:"+ stations[nA] + " " + gpsStationLat + " "+ gpsStationLon);
				 gpsStationName=stations[nA].toLowerCase();
				 
				 // I'm not sure if these are even used, but they can't be blank.
				 
				 try {
					 
					 GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
					 
					 gsq.setFromServlet(gpsStationName, beginDate, endDate, resource, contextGroup, contextId, minMaxLatLon, false);					 						
					 
					 dataUrl=gsq.getResource()+" ";
					 System.out.println(dataUrl);
					 
					 Observation[] obsv=makeGPSObservationPoints(gpsStationName,dataUrl,getGpsRefStation());
					 obsv=setXYLocations(obsv,gpsStationLat,gpsStationLon);
					 
					 for(int i=0;i<obsv.length;i++) {						 
						 db.set(obsv[i]);
						 
					 }					 			 
				 }

				 catch (Exception ex) {			 
					 ex.printStackTrace();			 
				 }				 
			 }
			 
			 db.commit();
			 db.close();
			 saveSimplexProjectEntry(currentProjectEntry);
			 setGpsRefStation(false);			 
		 // }
		 
		 // GRWS_SubmitQuery isn't working correctly with a box option
		 /*
		 else {			 
			 
			 try {
				 
				 GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
			 
				 System.out.println ("minMaxLatLon of this query : " + selectedminlon + " " + selectedminlat + " " + selectedmaxlon + " " + selectedmaxlat);
				 gsq.setFromServlet("ALL", beginDate, endDate, resource, contextGroup, contextId, selectedminlat + " " + selectedminlon + " " + selectedmaxlat + " " + selectedmaxlat, false);							
				 
				 dataUrl=gsq.getResource()+" ";
				 System.out.println(dataUrl);
				 String[] splitdataUrl = dataUrl.split("\n");
				 for (int nA = 0 ; nA < splitdataUrl.length ; nA++)
					 System.out.println("splitdataUrl[" + nA + "] : " + splitdataUrl[nA]);	
					 
				 
				 for (int nA = 0 ; nA < splitdataUrl.length ; nA++) {				 
					 Observation[] obsv=makeGPSObservationPoints(gpsStationName,splitdataUrl[nA],getGpsRefStation());
					 obsv=setXYLocations(obsv,gpsStationLat,gpsStationLon);
					 
					 for(int i=0;i<obsv.length;i++) {
						 db.set(obsv[i]);				 
					 }
				 }			 
			 }

			 catch (Exception ex) {			 
				 ex.printStackTrace();			 
			 }

			 db.commit();
			 db.close();
			 saveSimplexProjectEntry(currentProjectEntry);
			 setGpsRefStation(false);			 
		 }
		 */
	 }
	 
	 private Observation[] setXYLocations(Observation[] obsv,
													 String gpsStationLat,
													 String gpsStationLon) {
		  //Get project origin.
		  double origin_lat=currentProjectEntry.getOrigin_lat();
		  double origin_lon=currentProjectEntry.getOrigin_lon();

		  //Make these conversions
		  double gpsLat=Double.parseDouble(gpsStationLat);
		  double gpsLon=Double.parseDouble(gpsStationLon);

		  //Project origin is not set, so set it.
		  if(origin_lat==projectEntry.DEFAULT_LAT 
			  && origin_lon==projectEntry.DEFAULT_LON) {
				currentProjectEntry.setOrigin_lat(gpsLat);
				currentProjectEntry.setOrigin_lon(gpsLon);
				for(int i=0;i<obsv.length;i++) {
					 //We are at the origin.
					 obsv[i].setObsvLocationEast("0.0");
					 obsv[i].setObsvLocationNorth("0.0");
				}
		  }
		  //Find where we are.
		  else {
				double x=(gpsLon-origin_lon)*factor(origin_lon,origin_lat);
				double y=(gpsLat-origin_lat)*111.32;
				for(int i=0;i<obsv.length;i++) {
					 //We are at the origin.
				    obsv[i].setObsvLocationEast(format.format(x));
				    obsv[i].setObsvLocationNorth(format.format(y));
				}
		  }
		  //We have probably updated the origin, so save to db.
		  return obsv;
	 }

    private Observation[] makeGPSObservationPoints(String stationName,
						   String rawGRWSResponse,
						   boolean gpsRefStation) {
	     
		  Observation[] observations=new Observation[3];
		  //Check the response and only take the latest value.
		  StringTokenizer st=new StringTokenizer(rawGRWSResponse,"\n");
		  
		  int lastToken=st.countTokens();
		  int icount=0;
		  String space=" ";
		  while(st.hasMoreTokens()) {
				icount++;
				String line=st.nextToken();
				if(icount==lastToken) {
					 //parse the line
					 String[] neu=new String[3];
					 String[] sig_neu=new String[3];
					 //Simplex uses enu instead of neu order.
					 String[] simplexObType={"2","1","3"};
					 String[] simplexObsvName={"north","east","up"};

					 StringTokenizer st2=new StringTokenizer(line," ");
					 String station=st2.nextToken();
					 String date=st2.nextToken();
					 neu[0]=st2.nextToken();
					 neu[1]=st2.nextToken();
					 neu[2]=st2.nextToken();
					 sig_neu[0]=st2.nextToken();
					 sig_neu[1]=st2.nextToken();
					 sig_neu[2]=st2.nextToken();
					 
					 for(int i=0;i<3;i++) {
						  observations[i]=new Observation();
						  observations[i].setObsvName(station+"_"+simplexObsvName[i]);
						  observations[i].setObsvType(simplexObType[i]);
						  observations[i].setObsvValue(neu[i]);
						  observations[i].setObsvError(sig_neu[i]);
						  //Note all 3 obsv values for this one station
						  //will be set to this value.
						  if(gpsRefStation) {
						      observations[i].setObsvRefSite("-1");
						  }
						  else {
						      observations[i].setObsvRefSite("1");
						  }
					 }
				}

		  }
		  return observations;
	 }

	 public void toggleCloseMap(ActionEvent ev) {
		  System.out.println("Turn off the map display");
		  getCurrentEditProjectForm().setRenderGPSStationMap(false);		  
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
		  try {
				System.out.println("Getting selected archived project row");
 				// HtmlDataTable hdt=getMyArchiveDataTable();
				// System.out.println(hdt.getRowCount()+hdt.getId());
				// Object obj=hdt.getRowData();

				// SimpleXOutputBean dpsb=
				// 	 (SimpleXOutputBean)getMyArchiveDataTable().getRowData();
				
				
				SimpleXOutputBean dpsb=(SimpleXOutputBean)(getMyArchiveDataTable().getRowData());

				System.out.println(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
										 + codeName + "/" + dpsb.getProjectName() + ".db");

				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
										 + codeName + "/" + dpsb.getProjectName() + ".db");
				
				System.out.println("Found project:"+dpsb.getProjectName()+" "+dpsb.getJobUIDStamp());
				ObjectSet results=db.get(dpsb);
				System.out.println("Result size: "+results.size());
				//Should only have one value.
				if(results.hasNext()){
					 dpsb=(SimpleXOutputBean)results.next();
					 db.delete(dpsb);
				}
				db.close();
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
    }
	 
	 public UIData getMyArchiveDataTable2(){
		  return this.myArchiveDataTable2;
	 }

	 public void setMyArchiveDataTable2(UIData myArchiveDataTable2){
		  this.myArchiveDataTable2=myArchiveDataTable2;
	 }

	 public HtmlDataTable getMyArchiveDataTable() {
		  return this.myArchiveDataTable;
	 }

	 public void setMyArchiveDataTable(HtmlDataTable myArchiveDataTable){
		  this.myArchiveDataTable=myArchiveDataTable;
	 }

	 /**
	  * Used for selecting the data to plot
	  */
    public void togglePlotProject(ActionEvent ev) {
		  try {				
				if(getMyArchiveDataTable()!=null){
				  System.out.println("[togglePlotProject] getMyArchiveDataTable() isn't null");	
				  SimpleXOutputBean dpsb= (SimpleXOutputBean)(getMyArchiveDataTable().getRowData());
				  				  
				  String kmlUrlString=dpsb.getKmlUrls()[0];
				  String kmlName=
					  kmlUrlString.substring(kmlUrlString.lastIndexOf("/")+1,kmlUrlString.length());

				  downloadKmlFile(kmlUrlString,
									  this.getBasePath()+"/"+"gridsphere"+"/"+kmlName);

				  System.out.println("[togglePlotProject] kmlpath :  " + this.getBasePath()+"/"+"gridsphere"+"/"+kmlName);
				  setKmlProjectFile(kmlName);				  
				}
				 else 
				      System.out.println("[togglePlotProject] getMyArchiveDataTable() is null");
				
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  
    }

	 public void setKmlProjectFile(String kmlProjectFile){
		  this.kmlProjectFile=kmlProjectFile;
	 }
	 
	 public String getKmlProjectFile(){
		  return this.kmlProjectFile;
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
				orderedList.add((SimpleXOutputBean)fullList.get(((Integer)reducedList.get(first)).intValue()));
				reducedList.remove(first);
		  }
		  return orderedList;
	 }
	 
	 protected int getFirst(List reducedList, List fullList) {
		  int first=0;
		  for(int i=1;i<reducedList.size();i++) {
				SimpleXOutputBean mb1=(SimpleXOutputBean)fullList.get(((Integer)reducedList.get(first)).intValue());
				SimpleXOutputBean mb2=(SimpleXOutputBean)fullList.get(((Integer)reducedList.get(i)).intValue());
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
     * Create a KML file of the faults.  The method assumes
     * access to global variables.
     */

    public String createFaultKmlFile() { 
		  String newFaultFilename="";
		  String oldLocalDestination=this.getBasePath()+"/"+"gridsphere"+"/"
				+getFaultKmlFilename();
		  String localDestination="";

		  try {
				//Remove the previous file.
				System.out.println("Old fault kml file:"+localDestination);
				File oldFile=new File(oldLocalDestination);
				if (oldFile.exists()) {
					 System.out.println("Deleting old fault kml file");
					 oldFile.delete();
				}

				//Create the new file.
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
						  out.println(faults[i].getFaultLonStarts()+comma+faults[i].getFaultLatStarts()+comma+"0");
						  out.println(faults[i].getFaultLonEnds()+comma+faults[i].getFaultLatEnds()+comma+"0");
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
	 
	 public String getFaultKmlFilename() {
		  return faultKmlFilename;
	 }
	 
	 public void setFaultKmlFilename(String faultKmlFilename) {
		  this.faultKmlFilename=faultKmlFilename;
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
}
