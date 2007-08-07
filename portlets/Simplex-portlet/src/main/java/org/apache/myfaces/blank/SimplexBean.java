package org.apache.myfaces.blank;

//Imports from the mother ship
import java.io.*;
import java.net.*;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;
import java .util.StringTokenizer;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.faces.component.html.HtmlDataTable;

import org.servogrid.genericproject.GenericSopacBean;

import sun.misc.BASE64Encoder;
import WebFlowClient.cm.ContextManagerImp;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * Everything you need to set up and run SimpleBean.
 */

public class SimplexBean extends GenericSopacBean {

	// Variables that we need to get from the parent.
	// ContextManagerImp cm=null;
	// boolean isInitialized=false;

	// Simplex Bean staff
	 protected String codeName = "Simplex2";
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

	String selectdbURL = new String(
			"http://gf2.ucs.indiana.edu:9090/axis/services/" + "/Select");

	editProjectForm currentEditProjectForm = new editProjectForm(selectdbURL);

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

	String simpleXBaseUrl = "http://gf19.ucs.indiana.edu:8080/simplexexec/";

	String simpleXServiceUrl = "http://gf1.ucs.indiana.edu:13080/simplexexec/services/SimpleXExec";
	
	String kmlGeneratorBaseurl= "http://gf1.ucs.indiana.edu:13080/KmlGenerator/";
	
	String kmlGeneratorUrl = "http://gf1.ucs.indiana.edu:13080/KmlGenerator/services/KmlGenerator";

	SimpleXOutputBean projectSimpleXOutput;
	 
	 HtmlDataTable myArchiveDataTable;

	 String kmlProjectFile="network0.kml";

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
		System.out.println("Project list size:"+myprojectlist.size());

		if (myprojectlist.size() > 0) {
			for (int i = 0; i < myprojectlist.size(); i++) {
				String projectName = ((SelectItem) myprojectlist.get(i))
						.getLabel();

				SimpleXOutputBean mega = new SimpleXOutputBean();
				mega.setProjectName(projectName);
				// System.out.println("ProjectName: "+projectName);
				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
						+ codeName + "/" + projectName + ".db");
				System.out.println(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
										 + codeName + "/" + projectName + ".db");
				// ObjectSet results=db.get(mega);
				ObjectSet results = db.get(SimpleXOutputBean.class);
				// System.out.println("Matches for
				// "+projectName+":"+results.size());
				while (results.hasNext()) {
					mega = (SimpleXOutputBean) results.next();
					//					myarchivedFileEntryList.add(mega);
					tmpList.add(mega);
				}
				db.close();
				myarchivedFileEntryList=sortByDate(tmpList);
			}
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
		this.myProjectNameList.clear();
		try {
			 System.out.println(getBasePath()+"/"+getContextBasePath()+"/"+userName+"/"+codeName+".db");
			 db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
					+ codeName + ".db");
			projectEntry project = new projectEntry();
			ObjectSet results = db.get(projectEntry.class);
			// System.out.println("Got results:"+results.size());
			while (results.hasNext()) {
				project = (projectEntry) results.next();
				System.out.println(project.getProjectName());
				myProjectNameList.add(new SelectItem(project.getProjectName(),
						project.getProjectName()));
			}
			db.close();

		} catch (Exception ex) {
			// ex.printStackTrace();
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
	protected List reconstructMyObservationEntryForProjectList(
			String projectName) {
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
				this.myObservationEntryForProjectList
						.add(tmp_myObservationEntryForProject);
			}
			db.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			db.close();
		}
		return this.myObservationEntryForProjectList;
	}

	public List getMyObservationEntryForProjectList() {
		String projectName = getProjectName();
		return reconstructMyObservationEntryForProjectList(projectName);

	}

	public void setMyObservationEntryForProjectList(List tmp_list) {
		this.myObservationEntryForProjectList = tmp_list;
	}

	/**
	 * Create the Observation collection
	 */
	protected List populateObservationCollection(
			List myObservationEntryProjectList) throws Exception {
		List myObservationCollection = new ArrayList();
		for (int i = 0; i < myObservationEntryProjectList.size(); i++) {
			observationEntryForProject tmp_ObservationEntryForProject = (observationEntryForProject) myObservationEntryForProjectList
					.get(i);
			String tmp_observationName = tmp_ObservationEntryForProject
					.getObservationName();
			myObservationCollection
					.add(populateObservationFromContext(tmp_observationName));
		}
		return myObservationCollection;
	}

	protected Observation populateObservationFromContext(
			String tmp_observationName) throws Exception {
		System.out.print("Populating Layer " + tmp_observationName + " for  "
				+ projectName);
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

	public static void main(String[] args) {

	}

	/**
	 * default empty constructor
	 */
	public SimplexBean() throws Exception {
		super();
		cm = getContextManagerImp();
		System.out.println("Simplex Bean Created");
	}

	 protected void initSimplexService() throws Exception {
		simplexService = new SimpleXServiceServiceLocator()
				.getSimpleXExec(new URL(simpleXServiceUrl));
	 }

	/**
	 * These are methods associated with Faces navigations.
	 */
	public String newProject() throws Exception {
		isInitialized = getIsInitialized();
		if (!isInitialized) {
			initWebServices();
		}
		makeProjectDirectory();
		setContextList();
		return ("Simplex2-new-project");
	}

	public String loadProject() throws Exception {
		System.out.println("Loading project");
		if (!isInitialized) {
			initWebServices();
		}
		makeProjectDirectory();
		setContextList();
		return ("Simplex2-load-project");
	}

	public String archivedData() throws Exception {
		System.out.println("load archived Data");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		return ("Simplex2-archived-data");
	}

	public String GMTDataPlots() throws Exception {
		System.out.println("Plot Data");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		return ("Simplex2-listgmt-archives");
	}

	public String toggleRunSimplex2() throws Exception {

		Observation[] obsv = getObservationsFromDB();
		Fault[] faults = getFaultsFromDB();
		String timeStamp = "";
		System.out.println("ProjectName:" + projectName);
		initSimplexService();
		projectSimpleXOutput = simplexService.runSimplex(userName, projectName,
																		 faults, obsv, currentProjectEntry.startTemp,
																		 currentProjectEntry.maxIters,
																		 currentProjectEntry.origin_lon, 
																		 currentProjectEntry.origin_lat,
																		 this.kmlGeneratorUrl, timeStamp);
		
		System.out.println(projectSimpleXOutput.getProjectName());
		System.out.println(projectSimpleXOutput.getInputUrl());
		saveSimpleXOutputBeanToDB(projectSimpleXOutput);
		
		return ("Simplex2-back");
	}
	 
	 protected void saveSimpleXOutputBeanToDB(SimpleXOutputBean projectSimpleXOutput) {
		  
		  // Set up the bean template for searching.
		  SimpleXOutputBean mega = new SimpleXOutputBean();
		  mega.setProjectName(projectSimpleXOutput.getProjectName());
		  mega.setJobUIDStamp(projectSimpleXOutput.getJobUIDStamp());
		  // Find the matching bean
		  db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
									+ codeName + "/" + projectName + ".db");
		  ObjectSet results = db.get(mega);
		  System.out.println("SimpleXOutputBean to update found? "
									+ results.size());
		  System.out.println("Saving SimpleXOutputBean getProjectName:"
									+ projectSimpleXOutput.getProjectName());
		  System.out.println("Saving getInputUrl url:"
									+ projectSimpleXOutput.getInputUrl());
		  if (results.hasNext()) {
				// Reassign the bean. Should only be one match.
				mega = (SimpleXOutputBean) results.next();
				
		  }
		  mega.setInputUrl(projectSimpleXOutput.getInputUrl());
		  mega.setLogUrl(projectSimpleXOutput.getLogUrl());
		  mega.setOutputUrl(projectSimpleXOutput.getOutputUrl());
		  mega.setFaultUrl(projectSimpleXOutput.getFaultUrl());
		  String[] kmlurls=projectSimpleXOutput.getKmlUrls();
		  mega.setKmlUrls(kmlurls);
		  db.set(mega);
		  db.commit();
		  db.close();
	}

	public String NewProjectThenEditProject() throws Exception {
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		setProjectname();
		currentEditProjectForm.init_edit_project();
		return "Simplex2-edit-project";
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
			 db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName
									  + "/" + codeName + ".db");
			 if (deleteProjectsList != null) {
				  for (int i = 0; i < deleteProjectsList.length; i++) {
						//Delete the input bean
						projectEntry delproj = new projectEntry();
						delproj.setProjectName((String) deleteProjectsList[i]);
						ObjectSet results = db.get(delproj);
						if (results.hasNext()) {
							 delproj = (projectEntry) results.next();
							 db.delete(delproj);
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
		} catch (Exception ex) {
			 ex.printStackTrace();
		}
		
		return "Simplex2-this";
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

		currentEditProjectForm.initEditFormsSelection();
		if (selectProjectsList != null) {
			for (int i = 0; i < 1; i++) {
				this.projectName = selectProjectsList[0];
			}
		}

		try {

			// Reconstruct the project lists
			myFaultEntryForProjectList = reconstructMyFaultEntryForProjectList(projectName);
			myObservationEntryForProjectList = reconstructMyObservationEntryForProjectList(projectName);

			// Reconstruct the fault and layer object collections from the
			// context
			myFaultCollection = populateFaultCollection(myFaultEntryForProjectList);
			myObservationCollection = populateObservationCollection(myObservationEntryForProjectList);

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

			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		currentEditProjectForm.projectSelectionCode = "";
		currentEditProjectForm.faultSelectionCode = "";
		return "Simplex2-edit-project";
	}

	public String setProjectname() throws Exception {
		// Do real logic
		System.out.println("Creating new project");
		makeProjectDirectory();
		db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
				+ codeName + ".db");
		projectEntry project = new projectEntry();
		project.projectName = this.projectName;
		projectEntry tmp_project = new projectEntry();
		tmp_project.projectName = this.projectName;
		ObjectSet result = db.get(tmp_project);

		if (result.hasNext()) {
			tmp_project = (projectEntry) result.next();
			db.delete(tmp_project);
		}
		project.setMaxIters(currentProjectEntry.maxIters);
		project.setStartTemp(currentProjectEntry.startTemp);
		project.setOrigin_lat(currentProjectEntry.origin_lat);
		project.setOrigin_lon(currentProjectEntry.origin_lon);
		this.currentProjectEntry=project;
		db.set(project);
		db.commit();
		db.close();

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

	public String toggleGMTPlot() {

		System.out.println("GMT Plot");
		if (!isInitialized) {
			initWebServices();
		}
		try {
			setContextList();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {

			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
			SimpleXOutputBean tmp_loadMeshTableEntry = new SimpleXOutputBean();
			for (int i = 0; i < myarchivedFileEntryList.size(); i++) {
				tmp_loadMeshTableEntry = (SimpleXOutputBean) myarchivedFileEntryList
						.get(i);
				if ((tmp_loadMeshTableEntry.getView() == true)) {
					break;
				}
			}
			String tmp_projectName = tmp_loadMeshTableEntry.getProjectName();
			projectName = tmp_projectName;
			boolean tmp_view = tmp_loadMeshTableEntry.getView();
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
						projectName, currentProjectEntry.origin_lat,
						currentProjectEntry.origin_lon, timeStamp);
				this.gmtPlotPdfUrl = this.currentGMTViewForm.getGmtPlotPdfUrl();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ("Simplex2-gmt-view");

	}

	public String toggleMakeMap() {

		System.out.println("GMT Plot");
		if (!isInitialized) {
			initWebServices();
		}
		try {
			setContextList();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {

			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
			SimpleXOutputBean tmp_loadMeshTableEntry = new SimpleXOutputBean();
			for (int i = 0; i < myarchivedFileEntryList.size(); i++) {
				tmp_loadMeshTableEntry = (SimpleXOutputBean) myarchivedFileEntryList
						.get(i);
				if ((tmp_loadMeshTableEntry.getView() == true)) {
					break;
				}
			}

			String tmp_projectName = tmp_loadMeshTableEntry.getProjectName();
			projectName = tmp_projectName;
			boolean tmp_view = tmp_loadMeshTableEntry.getView();
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
						projectName, currentProjectEntry.origin_lat,
						currentProjectEntry.origin_lon, timeStamp);
				System.out.println(mapXmlUrl);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ("Simplex2-google-map");
	}
	
	public String toggleViewKml() {

		System.out.println("Kml viewer");
		if (!isInitialized) {
			initWebServices();
		}
		try {
			setContextList();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {

			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
			SimpleXOutputBean tmp_loadMeshTableEntry = new SimpleXOutputBean();
			for (int i = 0; i < myarchivedFileEntryList.size(); i++) {
				tmp_loadMeshTableEntry = (SimpleXOutputBean) myarchivedFileEntryList
						.get(i);
				if ((tmp_loadMeshTableEntry.getView() == true)) {
					break;
				}
			}

			String tmp_projectName = tmp_loadMeshTableEntry.getProjectName();
			projectName = tmp_projectName;
			boolean tmp_view = tmp_loadMeshTableEntry.getView();
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

	}

	 public void toggleAddObsvTextAreaForProject(ActionEvent ev) {
		  currentEditProjectForm.initEditFormsSelection();
		  db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
									+ codeName + "/" + projectName + ".db");
		  System.out.println("Parsing Output");
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
					 System.out.println("\n");
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
	}

	public void toggleAddFaultForProject(ActionEvent ev) {
		currentEditProjectForm.initEditFormsSelection();
		db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
				+ codeName + "/" + projectName + ".db");

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
		db.close();

	}

	public String toggleReplotGMT() {

		try {

			 initSimplexService();
			this.gmtPlotPdfUrl = simplexService.runRePlotGMT(userName,
					projectName, currentGMTViewForm, this.gmtPlotPdf_timeStamp);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.gmtPlotPdfUrl;
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
				SimpleXOutputBean dpsb=
					 (SimpleXOutputBean)getMyArchiveDataTable().getRowData();

				db = Db4o.openFile(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
										 + codeName + "/" + dpsb.getProjectName() + ".db");
				System.out.println(getBasePath()+"/"+getContextBasePath() + "/" + userName + "/"
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
	 
	 public HtmlDataTable getMyArchiveDataTable() {
		  return myArchiveDataTable;
	 }

	 public void setMyArchiveDataTable(HtmlDataTable myArchiveDataTable){
		  this.myArchiveDataTable=myArchiveDataTable;
	 }

	 /**
	  * Used for selecting the data to plot
	  */
    public void togglePlotProject(ActionEvent ev) {
		  try {
				SimpleXOutputBean dpsb=
					 (SimpleXOutputBean)getMyArchiveDataTable().getRowData();
				
				System.out.println("Found project:"+dpsb.getProjectName()+" "
										 +dpsb.getKmlUrls()[0]);
				String kmlUrlString=dpsb.getKmlUrls()[0];
				String kmlName=
					 kmlUrlString.substring(kmlUrlString.lastIndexOf("/")+1,kmlUrlString.length());

				downloadKmlFile(kmlUrlString,
									 this.getBasePath()+"/"+"gridsphere"+"/"+kmlName);

				System.out.println(kmlName);
				setKmlProjectFile(kmlName);
				
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
	  * Some of the stuff below should be abstracted to the generic project classes.
	  */
 
	 /**
	  * Do nothing for now.
	  */
	 protected List sortByDate(List fullList) {
		  return fullList;
	 }



	 /**
	  * Sort the list by date
	  */

// 	 protected List sortByDate(List fullList) {
// 		  if(fullList==null) return null;
// 		  int size=fullList.size();
// 		  if(size<2) {
// 				return fullList;
// 		  }
// 		  //Ordered list is originally empty and reducedlist is full.
// 		  List orderedList=new ArrayList();
// 		  List reducedList=new ArrayList();
// 		  myListToVectorCopy(reducedList,fullList);
		  
// 		  orderedList=setListOrder(orderedList, reducedList, fullList);

// 		  return orderedList;
// 	 }

// 	 protected void myListToVectorCopy(List dest, List src) {
// 		  for(int i=0;i<src.size();i++) {
// 				dest.add(new Integer(i));
// 		  }
// 	 }
	 
// 	 protected List setListOrder(List orderedList, List reducedList, List fullList) {
		  
// 		  if(reducedList==null) return null;
// 		  int size=reducedList.size();
// 		  if(size<2) {
// 				return fullList;
// 		  }
// 		  while(reducedList!=null && reducedList.size()>0) {
// 				int first=getFirst(reducedList, fullList);
// 				orderedList.add((SimpleXOutputBean)fullList.get(((Integer)reducedList.get(first)).intValue()));
// 				reducedList.remove(first);
// 		  }
// 		  return orderedList;
// 	 }
	 
// 	 protected int getFirst(List reducedList, List fullList) {
// 		  int first=0;
// 		  for(int i=1;i<reducedList.size();i++) {
// 				SimpleXOutputBean mb1=(SimpleXOutputBean)fullList.get(((Integer)reducedList.get(first)).intValue());
// 				SimpleXOutputBean mb2=(SimpleXOutputBean)fullList.get(((Integer)reducedList.get(i)).intValue());
// 				if(mb1.getCreationDate()==null||mb1.getCreationDate().equals("")) { 
// 					 mb1.setCreationDate((new Date()).toString());
// 				}
// 				if(mb2.getCreationDate()==null||mb2.getCreationDate().equals("")) { 
// 					 mb2.setCreationDate((new Date()).toString());
// 				}
// 				Date date1=new Date(mb1.getCreationDate());
// 				Date date2=new Date(mb2.getCreationDate());			  
// 				if(date2.before(date1)) first=i;
// 		  }

// 		  return first;
// 	 }

}
