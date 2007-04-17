package org.apache.myfaces.blank;

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
import WebFlowClient.fsws.*;
import cgl.webclients.AntVisco;
import cgl.webclients.AntViscoServiceLocator;
import WebFlowClient.cm.*;
import WebFlowClient.sjws.*;
import WebFlowClient.aws2.*;

/**
 * Everything you need to set up and run MeshGenerator.
 */

public class SimplexBean extends GenericSopacBean {

	// Variables that we need to get from the parent.
	// ContextManagerImp cm=null;
	// boolean isInitialized=false;

	// Simplex Bean staff
	protected String codeName = "Simplex2";

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

	GeoFESTUtils gfutils = new GeoFESTUtils();

	List myFaultEntryForProjectList = new ArrayList();

	List myObservationEntryForProjectList = new ArrayList();

	String buildPath = "/home/gateway/GEMCodes/Simplex/";

	String[] selectProjectsList;

	List myProjectNameList = new ArrayList();

	String[] deleteProjectsList;

	List myarchivedFileEntryList = new ArrayList();

	String submitjobUrl = "http://gf2.ucs.indiana.edu:6060/GCWS/services/Submitjob";

	String submitjob_binPath = "/home/gateway/GEMCodes/Simplex/";

	String submitjob_buildPath = "/home/gateway/GEMCodes/Simplex/";

	String submitjob_baseWorkDir = "/tmp/";

	String submitjob_fileServiceUrl = "http://gf2.ucs.indiana.edu:6060/GCWS/services/FileService";

	GMTViewForm currentGMTViewForm = new GMTViewForm();

	String baseImageName = "";
	
	String gmtPlotPdfUrl = "";

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

	public void setSubmitjob_fileServiceUrl(String tmp_str) {
		this.submitjob_fileServiceUrl = tmp_str;
	}

	public String getSubmitjob_fileServiceUrl() {
		return this.submitjob_fileServiceUrl;
	}

	public void setSubmitjob_baseWorkDir(String tmp_str) {
		this.submitjob_baseWorkDir = tmp_str;
	}

	public String getSubmitjob_baseWorkDir() {
		return this.submitjob_baseWorkDir;
	}

	public void setSubmitjob_buildPath(String tmp_str) {
		this.submitjob_buildPath = tmp_str;
	}

	public String getSubmitjob_buildPath() {
		return this.submitjob_buildPath;
	}

	public void setSubmitjob_binPath(String tmp_str) {
		this.submitjob_binPath = tmp_str;
	}

	public String getSubmitjob_binPath() {
		return this.submitjob_binPath;
	}

	public void setSubmitjobUrl(String tmp_str) {
		this.submitjobUrl = tmp_str;
	}

	public String getSubmitjobUrl() {
		return this.submitjobUrl;
	}

	public void setMyarchivedFileEntryList(List tmp_str) {
		this.myarchivedFileEntryList = tmp_str;
	}

	public List getMyarchivedFileEntryList() {
		myarchivedFileEntryList.clear();
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
					if (tmp_loadMeshTableEntry.meshHost != null
							&& !tmp_loadMeshTableEntry.meshHost.equals("null")) {
						myarchivedFileEntryList.add(tmp_loadMeshTableEntry);
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.myarchivedFileEntryList;
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

	public void setSelectProjectsList(String[] tmp_str) {
		this.selectProjectsList = tmp_str;
	}

	public String[] getSelectProjectsList() {

		return this.selectProjectsList;
	}

	public String getBuildPath() {
		return this.buildPath;
	}

	public void setBuildPath(String tmp) {
		this.buildPath = tmp;
	}

	public List getMyObservationEntryForProjectList() {
		String projectFullName = codeName + SEPARATOR + projectName;
		this.myObservationEntryForProjectList.clear();
		try {
			if (cm.listContext(projectFullName) != null) {
				String thename = projectFullName + SEPARATOR + OBSERVATIONS;
				String[] observations = cm.listContext(thename);
				if (observations.length > 0) {
					for (int i = 0; i < observations.length; i++) {
						observationEntryForProject tmp_myObservationEntryForProject = new observationEntryForProject();
						tmp_myObservationEntryForProject.observationName = observations[i];
						tmp_myObservationEntryForProject.view = false;
						tmp_myObservationEntryForProject.delete = false;
						this.myObservationEntryForProjectList
								.add(tmp_myObservationEntryForProject);

					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return this.myObservationEntryForProjectList;
	}

	public void setMyObservationEntryForProjectList(List tmp_list) {
		this.myObservationEntryForProjectList = tmp_list;
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

	public String setValue(ContextManagerImp cm, String Status, String name,
			String prop) throws Exception {
		String retval = "";
		if (Status.equals("Update")) {
			retval = cm.getCurrentProperty(name, prop);
		}
		return retval;
	}

	// --------------------------------------------------
	// Used to process "varies?" checkboxes.
	// --------------------------------------------------
	public String getParamAndSet(String paramName, String paramValue,
			ContextManagerImp cm, String contextName, String defVal)
			throws Exception {

		if (paramValue == null)
			paramValue = defVal;
		cm.setCurrentProperty(contextName, paramName, paramValue);

		return paramValue;

	}

	// --------------------------------------------------
	//	 
	// --------------------------------------------------
	public void setCMParam(String paramName, String paramValue,
			ContextManagerImp cm, String contextName) throws Exception {

		cm.setCurrentProperty(contextName, paramName, paramValue);
	}

	// --------------------------------------------------
	// Substitutes underscores for blanks in the
	// provided string.
	// --------------------------------------------------
	public String removeSpaces(String spacyString) {
		spacyString = spacyString.trim();
		// Get rid of spaces
		while (spacyString.indexOf(" ") > -1) {
			spacyString = spacyString.substring(0, spacyString.indexOf(" "))
					+ ""
					+ spacyString.substring(spacyString.indexOf(" ") + 1,
							spacyString.length());

		}
		return spacyString;
	}

	// --------------------------------------------------
	// Used to copy the entry of one context into another.
	// --------------------------------------------------
	public void copyContextValue(ContextManagerImp cm, String oldName,
			String newName, String propName, String defaultVal) {

		String propVal = null;
		try {
			propVal = cm.getCurrentProperty(oldName, propName);
			System.out.println("Copy stuff: " + propName + " " + propVal);
		} catch (Exception ex) {
			System.err.println("Could not fetch old property");
			System.err.println(ex.getMessage());
		}

		if (propVal == null || propVal.equals(""))
			propVal = defaultVal;
		try {
			cm.addContext(newName);
			cm.setCurrentProperty(newName, propName, propVal);
		} catch (Exception ex) {
			System.err.println("Could not set new property");
			System.err.println(ex.getMessage());
		}
	}

	public static void main(String[] args) {

	}

	/**
	 * default empty constructor
	 */
	public SimplexBean() {
		super();
		cm = getContextManagerImp();
		System.out.println("Simplex Bean Created");
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
		return ("Simplex2-new-project");
	}

	public String loadProject() throws Exception {
		System.out.println("Loading project");
		if (!isInitialized) {
			initWebServices();
		}
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

	public void SimplexCreateInputFile() {
		try {

			// --------------------------------------------------
			// These arrays holds all the interesting values.
			// --------------------------------------------------
			String[] faultParams = { "faultOriginX", "faultOriginY",
					"faultStrikeAngle", "faultDipAngle", "faultDepth",
					"faultWidth", "faultLength", "faultStrikeSlip",
					"faultDipSlip" };

			String[] obsvParams = { "obsvRefSite", "obsvType",
					"obsvLocationEast", "obsvLocationNorth", "obsvValue",
					"obsvError" };
			String projectFullName = codeName + SEPARATOR + projectName;

			// --------------------------------------------------
			// Set up and create the input file.
			// --------------------------------------------------
			String contextDir = cm.getCurrentProperty(projectFullName,
					"Directory");
			String inputFile = projectName + ".input";

			PrintWriter pw = new PrintWriter(new FileWriter(contextDir + "/"
					+ inputFile), true);

			// --------------------------------------------------
			// First, print the faults.
			// --------------------------------------------------
			String[] faults = cm.listContext(projectFullName + "/" + FAULTS);
			pw.println(faults.length);
			pw.println("");

			String space = "\t";

			for (int i = 0; i < faults.length; i++) {
				String fcontext = projectFullName + "/" + FAULTS + "/"
						+ faults[i];
				for (int j = 0; j < faultParams.length; j++) {

					pw.print(j + 1);
					pw.print(space);
					pw.print(cm.getCurrentProperty(fcontext, faultParams[j]
							+ "Vary"));
					pw.print(space);
					pw.print(cm.getCurrentProperty(fcontext, faultParams[j]));
					pw.print("\n");
				}
				pw.println("");
			}

			// --------------------------------------------------
			// Now do the observation points.
			// --------------------------------------------------
			String[] obsv = cm
					.listContext(projectFullName + "/" + OBSERVATIONS);
			pw.println(obsv.length);

			pw.print(cm.getCurrentProperty(projectFullName, "startTemp"));
			pw.print(space);
			pw.print(cm.getCurrentProperty(projectFullName, "maxIters"));
			pw.print(space);
			pw.print("10"); // This property is obsolete.
			pw.print("\n");

			for (int i = 0; i < obsv.length; i++) {
				String ocontext = projectFullName + "/" + OBSERVATIONS + "/"
						+ obsv[i];
				int refint = Integer.parseInt(cm.getCurrentProperty(ocontext,
						obsvParams[0]));
				int obsvType = Integer.parseInt(cm.getCurrentProperty(ocontext,
						obsvParams[1]));
				pw.print(refint * obsvType);
				pw.print(space);

				for (int j = 2; j < obsvParams.length; j++) {
					pw.print(cm.getCurrentProperty(ocontext, obsvParams[j]));
					pw.print(space);
				}
				pw.print("\n");
			}

			pw.close();

		} // End of the try

		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void Simplex2RunProject() {
		try {
			// --------------------------------------------------
			// Set up the Ant Service.
			// --------------------------------------------------
			AntVisco ant = new AntViscoServiceLocator().getAntVisco(new URL(
					antUrl));

			String projectFullName = codeName + SEPARATOR + projectName;
			// --------------------------------------------------
			// Get metadata.
			// --------------------------------------------------
			String bf_loc = buildPath + "/" + "build.xml";

			cm.setCurrentProperty(projectFullName, "hostName", hostName);
			String contextDir = cm.getCurrentProperty(projectFullName,
					"Directory");

			String WORK_DIR = baseWorkDir + "/" + userName + "/" + projectName;
			// String WORK_DIR = contextDir;

			// --------------------------------------------------
			// Create the project directory and upload the file.
			// --------------------------------------------------
			String[] args2 = new String[4];
			args2[0] = "-DworkDir.prop=" + WORK_DIR;
			args2[1] = "-buildfile";
			args2[2] = bf_loc;
			args2[3] = "mkdir";

			ant.setArgs(args2);
			ant.run();

			// --------------------------------------------------
			// Set up the file service and move the file.
			// --------------------------------------------------
			FSClientStub fsclient = new FSClientStub();
			fsclient.setBindingUrl(fileServiceUrl);

			// --------------------------------------------------
			// Record the names of the input, output, and log
			// files on the remote server.
			// --------------------------------------------------
			String inputFile = projectName + ".input";
			String destfile = WORK_DIR + "/" + inputFile;

			fsclient.uploadFile(contextDir + "/" + inputFile, destfile);

			String[] args = new String[6];
			args[0] = "-DworkDir.prop=" + WORK_DIR;
			args[1] = "-DprojectName.prop=" + projectName;
			args[2] = "-Dbindir.prop=" + binPath;
			args[3] = "-buildfile";
			args[4] = bf_loc;
			args[5] = "RunSimplex";

			ant.setArgs(args);
			ant.execute();
		} // End of the try

		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public String toggleRunSimplex2() throws Exception {
		System.out.println("run Simplex2");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		SimplexCreateInputFile();
		Simplex2RunProject();

		return ("Simplex2-back");
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
			if (deleteProjectsList != null) {
				for (int i = 0; i < deleteProjectsList.length; i++) {
					System.out.println(deleteProjectsList[i]);
					cm.removeContext(codeName + SEPARATOR
							+ deleteProjectsList[i]);
				}
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
		String projectFullName = codeName + SEPARATOR + projectName;

		try {
			this.currentProjectEntry.startTemp = cm.getCurrentProperty(
					projectFullName, "startTemp");
			this.currentProjectEntry.maxIters = cm.getCurrentProperty(
					projectFullName, "maxIters");
			this.currentProjectEntry.origin_lat = cm.getCurrentProperty(
					projectFullName, "origin_lat");
			this.currentProjectEntry.origin_lon = cm.getCurrentProperty(
					projectFullName, "origin_lon");
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
		// Store the request values persistently
		String projectFullName = codeName + SEPARATOR + projectName;
		System.out.println("Adding Context:" + projectFullName);
		cm.addContext(projectFullName);
		cm.addContext(projectFullName + SEPARATOR + FAULTS);
		cm.addContext(projectFullName + SEPARATOR + OBSERVATIONS);

		cm.setCurrentProperty(projectFullName, "Name", projectName);
		cm.setCurrentProperty(projectFullName, "maxIters",
				currentProjectEntry.maxIters);
		cm.setCurrentProperty(projectFullName, "startTemp",
				currentProjectEntry.startTemp);
		cm.setCurrentProperty(projectFullName, "origin_lat",
				currentProjectEntry.origin_lat);
		cm.setCurrentProperty(projectFullName, "origin_lon",
				currentProjectEntry.origin_lon);
		return "MG-set-project";
	}

	public String setValue(ContextManagerImp cm, String Status, String name,
			String prop, String defValue) throws Exception {
		String retval = defValue;
		if (Status.equals("Update")) {
			retval = cm.getCurrentProperty(name, prop);
		}
		return retval;
	}

	// This specialized method returns "checked" for checked values.
	public boolean setVariableValue(ContextManagerImp cm, String Status,
			String name, String prop, String defValue) throws Exception {

		boolean retValue = false; // This is the default value.

		String value = setValue(cm, Status, name, prop, defValue);
		if (value.equals("0"))
			retValue = false;
		else
			retValue = true;
		return retValue;
	}

	public void toggleUpdateObservationProjectEntry(ActionEvent ev) {
		String layerStatus = "Update";
		try {
			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
			observationEntryForProject tmp_ObservationEntryForProject = new observationEntryForProject();
			for (int i = 0; i < myObservationEntryForProjectList.size(); i++) {
				tmp_ObservationEntryForProject = (observationEntryForProject) myObservationEntryForProjectList
						.get(i);
				if ((tmp_ObservationEntryForProject.getView() == true)
						|| (tmp_ObservationEntryForProject.getDelete() == true)) {
					break;
				}
			}

			String tmp_ObservationName = tmp_ObservationEntryForProject
					.getObservationName();
			boolean tmp_view = tmp_ObservationEntryForProject.getView();
			boolean tmp_update = tmp_ObservationEntryForProject.getDelete();
			String projectFullName = codeName + SEPARATOR + projectName;

			currentEditProjectForm.initEditFormsSelection();
			if ((tmp_view == true) && (tmp_update == true)) {
				System.out.println("error");
			}
			if ((tmp_view == true) && (tmp_update == false)) {
				currentEditProjectForm.currentObservation.obsvName = tmp_ObservationName;
				String thename = projectFullName + SEPARATOR + OBSERVATIONS;

				String fullname = thename + SEPARATOR
						+ currentEditProjectForm.currentObservation.obsvName;
				currentEditProjectForm.currentObservation.obsvType = setValue(
						cm, layerStatus, fullname, "obsvType");
				currentEditProjectForm.currentObservation.obsvValue = setValue(
						cm, layerStatus, fullname, "obsvValue");
				currentEditProjectForm.currentObservation.obsvError = setValue(
						cm, layerStatus, fullname, "obsvError");
				currentEditProjectForm.currentObservation.obsvLocationEast = setValue(
						cm, layerStatus, fullname, "obsvLocationEast");
				currentEditProjectForm.currentObservation.obsvLocationNorth = setValue(
						cm, layerStatus, fullname, "obsvLocationNorth");
				currentEditProjectForm.currentObservation.obsvRefSite = setValue(
						cm, layerStatus, fullname, "obsvRefSite");
				currentEditProjectForm.renderCreateObservationForm = !currentEditProjectForm.renderCreateObservationForm;

			}
			if ((tmp_update == true) && (tmp_view == false)) {
				String tmp = projectFullName + SEPARATOR + OBSERVATIONS
						+ SEPARATOR + tmp_ObservationName;
				cm.removeContext(tmp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String setValue(String param_val, String param, boolean usefallback) {
		String retval = "-D";
		if (param_val == null || param_val.equals("")) {
			param_val = "";
			retval += "junk_prop=" + param_val;
		} else {
			retval += param + "=" + param_val;
		}
		return retval;
	}

	public void processPropsFile(String propfile) {
		try {
			BufferedReader buf = new BufferedReader(new FileReader(propfile));
			String line = buf.readLine();
			StringTokenizer st;
			while (line != null) {
				st = new StringTokenizer(line, "=");
				String name = st.nextToken();
				String val = st.nextToken();
				System.out.println("Props processed:" + name + " " + val);
				val = removeSpaces(val);
				if (name.equals("area_prop")) {
					currentGMTViewForm.area_prop = val;
				} else if (name.equals("scale_prop")) {
					currentGMTViewForm.scale_prop = val;
				} else if (name.equals("mapticks_prop")) {
					currentGMTViewForm.mapticks_prop = val;
				} else if (name.equals("vectormag_prop")) {
					currentGMTViewForm.vectormag_prop = val;
				} else if (name.equals("plot_background")) {
					currentGMTViewForm.plot_background = val;
				} else if (name.equals("plot_causative")) {
					currentGMTViewForm.plot_causative = val;
				} else if (name.equals("plot_obs")) {
					currentGMTViewForm.plot_obs = val;
				} else if (name.equals("plot_calc")) {
					currentGMTViewForm.plot_calc = val;
				} else if (name.equals("plot_resid")) {
					currentGMTViewForm.plot_resid = val;
				}
				line = buf.readLine();
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void doAntPlotForGMT(String selectedProject) {

		try {

			// --------------------------------------------------
			// Set up and run ant.
			// --------------------------------------------------
			AntVisco ant = new AntViscoServiceLocator().getAntVisco(new URL(
					antUrl));

			String bf_loc = binPath + "/" + "build-gmt.xml";

			String WORK_DIR = baseWorkDir + "/" + userName + "/"
					+ selectedProject + "/";
			String[] args = new String[14];
			args[0] = "-DworkDir_prop=" + WORK_DIR;
			args[1] = "-Doutfile=" + baseImageName;
			args[2] = setValue(currentGMTViewForm.area_prop, "area_prop", true);
			args[3] = setValue(currentGMTViewForm.scale_prop, "scale_prop",
					true);
			args[4] = setValue(currentGMTViewForm.mapticks_prop,
					"mapticks_prop", true);
			args[5] = setValue(currentGMTViewForm.vectormag_prop,
					"vectormag_prop", true);
			args[6] = setValue(currentGMTViewForm.plot_background,
					"plot_background", false);
			args[7] = setValue(currentGMTViewForm.plot_causative,
					"plot_causative", false);
			args[8] = setValue(currentGMTViewForm.plot_obs, "plot_obs", false);
			args[9] = setValue(currentGMTViewForm.plot_calc, "plot_calc", false);
			args[10] = setValue(currentGMTViewForm.plot_resid, "plot_resid",
					false);
			args[11] = "-buildfile";
			args[12] = bf_loc;
			args[13] = "makepdf";

			ant.setArgs(args);
			ant.run();

			// Get the generated image.
			String relname = baseImageName + ".pdf";
			String fullname = WORK_DIR + "/" + relname;
			System.out.println("Full name is " + fullname);
			String tmpfilename = getRealPath() + "/" + relname;
			FSClientStub fsclient = new FSClientStub();
			fsclient.setBindingUrl(fileServiceUrl);
			fsclient.downloadFile(fullname, tmpfilename);
			
			this.gmtPlotPdfUrl = getContextPath() + "/"
				+ relname;
			
		} catch (Exception ex) {
			System.out.println("Image not available");
			ex.printStackTrace();
		}
	}

	public void doSjPlotForGMT(String selectedProject) {

		try {
			// --------------------------------------------------
			// Set up the FileService client
			// --------------------------------------------------
			FSClientStub fsclient = new FSClientStub();

			// --------------------------------------------------
			// Set up the job submission service
			// --------------------------------------------------

			System.out.println("SJURL is " + submitjobUrl);
			SJwsImp sjws = new SJwsImpServiceLocator().getSubmitjob(new URL(
					submitjobUrl));

			// --------------------------------------------------
			// Calculate the image properties using Ken's script.
			// This is only done the first time.
			// --------------------------------------------------
			// Set the download file name
			String dataFile = baseWorkDir + "/" + userName + "/"
					+ selectedProject + "/" + selectedProject + ".output";
			fsclient.setBindingUrl(fileServiceUrl);
			fsclient.downloadFile(dataFile, "/tmp/tmpforsj");
			String sjdataFile = submitjob_baseWorkDir + "/" + userName + "/"
					+ selectedProject + "/" + selectedProject + ".output";

			String command = "/bin/mkdir " + submitjob_baseWorkDir + "/" + userName ;
			System.out.println("Command is " + command);
			String[] stuff = sjws.execLocalCommand(command);
			System.out.println("Stuff: " + stuff[0]);
			System.out.println("Stuff: " + stuff[1]);
			command = "/bin/mkdir " + submitjob_baseWorkDir + "/" + userName + "/"	+ selectedProject + "/";
			System.out.println("Command is " + command);
			stuff = sjws.execLocalCommand(command);
			System.out.println("Stuff: " + stuff[0]);
			System.out.println("Stuff: " + stuff[1]);
	
			fsclient.setBindingUrl(submitjob_fileServiceUrl);
			fsclient.uploadFile("/tmp/tmpforsj", sjdataFile);

			long timestamp = (new Date()).getTime();
			baseImageName = selectedProject + timestamp;
			this.gmtPlotPdfUrl = getContextPath() + "/"
			+ baseImageName + ".pdf";
			String projectFullName = codeName + SEPARATOR + selectedProject;

			String origin_lon = cm.getCurrentProperty(projectFullName,
					"origin_lon");
			if (origin_lon == null || origin_lon.equals("null"))
				origin_lon = "";

			String origin_lat = cm.getCurrentProperty(projectFullName,
					"origin_lat");
			if (origin_lat == null || origin_lat.equals("null"))
				origin_lat = "";

			command = submitjob_binPath + "map_simplex.prl "
					+ "--origin_lat " + origin_lat + " " + "--origin_lon "
					+ origin_lon + " " + sjdataFile + " "
					+ submitjob_baseWorkDir + "/" + userName + "/"
					+ selectedProject + "/" + " " + baseImageName;
			System.out.println("Command is " + command);

			stuff = sjws.execLocalCommand(command);

			System.out.println("Stuff: " + stuff[0]);
			System.out.println("Stuff: " + stuff[1]);

			// Get back the properties file
			System.out.println("Initializing the project.");

			System.out.println("Downloading the properties file.");
			String dlfile = submitjob_baseWorkDir + "/" + userName + "/"
					+ selectedProject + "/" + baseImageName + ".properties";
			String locfilename = "/tmp/" + baseImageName + ".properties";
			fsclient.setBindingUrl(submitjob_fileServiceUrl);
			fsclient.downloadFile(dlfile, locfilename);

			// Read the file and set param values.
			// Don't worry too much if this fails.
			System.out.println(dlfile + " " + locfilename);

			currentGMTViewForm.reset();
			processPropsFile(locfilename);
		} catch (Exception ex) {
			System.out.println("doSjPlotForGMT failed");
			ex.printStackTrace();
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
			setContextList();

			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
			loadMeshTableEntry tmp_loadMeshTableEntry = new loadMeshTableEntry();
			for (int i = 0; i < myarchivedFileEntryList.size(); i++) {
				tmp_loadMeshTableEntry = (loadMeshTableEntry) myarchivedFileEntryList
						.get(i);
				if ((tmp_loadMeshTableEntry.getView() == true)) {
					break;
				}
			}

			String tmp_projectName = tmp_loadMeshTableEntry.getProjectName();
			projectName =tmp_projectName;
			boolean tmp_view = tmp_loadMeshTableEntry.getView();

			if ((tmp_view == true)) {
				doSjPlotForGMT(tmp_projectName);
				doAntPlotForGMT(tmp_projectName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ("Simplex2-gmt-view");

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

			currentEditProjectForm.initEditFormsSelection();

			if ((tmp_view == true) && (tmp_update == true)) {
				System.out.println("update and view can not be found error");
			}
			if ((tmp_view == true) && (tmp_update == false)) {
				currentEditProjectForm.currentFault.faultName = tmp_faultName;
				String thename = projectFullName + SEPARATOR + FAULTS;

				String fullname = thename + SEPARATOR
						+ currentEditProjectForm.currentFault.faultName;
				currentEditProjectForm.currentFault.faultLocationX = setValue(
						cm, faultStatus, fullname, "faultOriginX", "0.0");
				currentEditProjectForm.currentFault.faultLocationY = setValue(
						cm, faultStatus, fullname, "faultOriginY", "0.0");
				currentEditProjectForm.currentFault.faultLength = setValue(cm,
						faultStatus, fullname, "faultLength", "14.0");
				currentEditProjectForm.currentFault.faultWidth = setValue(cm,
						faultStatus, fullname, "faultWidth", "21.0");
				currentEditProjectForm.currentFault.faultDepth = setValue(cm,
						faultStatus, fullname, "faultDepth", "19.5");
				currentEditProjectForm.currentFault.faultDipAngle = setValue(
						cm, faultStatus, fullname, "faultDipAngle", "40.0");
				currentEditProjectForm.currentFault.faultStrikeAngle = setValue(
						cm, faultStatus, fullname, "faultStrikeAngle", "122.0");
				currentEditProjectForm.currentFault.faultSlip = setValue(cm,
						faultStatus, fullname, "faultDipSlip", "1.276");
				currentEditProjectForm.currentFault.faultRakeAngle = setValue(
						cm, faultStatus, fullname, "faultStrikeSlip", "-0.248");
				currentEditProjectForm.renderCreateNewFaultForm = !currentEditProjectForm.renderCreateNewFaultForm;

				// These are the fault parameters.

				// These are the fault params that can vary.
				// We just want to see if the value is 0 or 1.
				currentEditProjectForm.currentFault.faultOriginXVary = setVariableValue(
						cm, faultStatus, fullname, "faultOriginXVary", "0");
				currentEditProjectForm.currentFault.faultOriginYVary = setVariableValue(
						cm, faultStatus, fullname, "faultOriginYVary", "0");
				currentEditProjectForm.currentFault.faultLengthVary = setVariableValue(
						cm, faultStatus, fullname, "faultLengthVary", "0");
				currentEditProjectForm.currentFault.faultWidthVary = setVariableValue(
						cm, faultStatus, fullname, "faultWidthVary", "0");
				currentEditProjectForm.currentFault.faultDepthVary = setVariableValue(
						cm, faultStatus, fullname, "faultDepthVary", "0");
				currentEditProjectForm.currentFault.faultDipAngleVary = setVariableValue(
						cm, faultStatus, fullname, "faultDipAngleVary", "0");
				currentEditProjectForm.currentFault.faultStrikeAngleVary = setVariableValue(
						cm, faultStatus, fullname, "faultStrikeAngleVary", "0");
				currentEditProjectForm.currentFault.faultDipSlipVary = setVariableValue(
						cm, faultStatus, fullname, "faultDipSlipVary", "0");
				currentEditProjectForm.currentFault.faultStrikeSlipVary = setVariableValue(
						cm, faultStatus, fullname, "faultStrikeSlipVary", "0");

			}
			if ((tmp_update == true) && (tmp_view == false)) {
				String tmp = projectFullName + SEPARATOR + FAULTS + SEPARATOR
						+ tmp_faultName;
				cm.removeContext(tmp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void toggleAddObservationForProject(ActionEvent ev) {
		currentEditProjectForm.initEditFormsSelection();
		String projectFullName = codeName + SEPARATOR + projectName;
		String gcname = currentEditProjectForm.currentObservation.obsvName;

		// Get rid of spaces.
		while (gcname.indexOf(" ") > -1) {
			gcname = gcname.substring(0, gcname.indexOf(" "))
					+ "_"
					+ gcname
							.substring(gcname.indexOf(" ") + 1, gcname.length());
		}
		currentEditProjectForm.currentObservation.obsvName = gcname;
		GeoFESTElement layerelement = new GeoFESTElement();
		layerelement.addElement("obsvName",
				currentEditProjectForm.currentObservation.obsvName);
		layerelement.addElement("obsvType",
				currentEditProjectForm.currentObservation.obsvType);
		layerelement.addElement("obsvValue",
				currentEditProjectForm.currentObservation.obsvValue);
		layerelement.addElement("obsvError",
				currentEditProjectForm.currentObservation.obsvError);
		layerelement.addElement("obsvLocationEast",
				currentEditProjectForm.currentObservation.obsvLocationEast);
		layerelement.addElement("obsvLocationNorth",
				currentEditProjectForm.currentObservation.obsvLocationNorth);
		layerelement.addElement("obsvRefSite",
				currentEditProjectForm.currentObservation.obsvRefSite);

		try {

			String obsvName = currentEditProjectForm.currentObservation.obsvName;
			System.out.println("Observation name: " + obsvName);
			obsvName = removeSpaces(obsvName);
			String cName = projectFullName + "/" + OBSERVATIONS + "/"
					+ obsvName;

			gfutils.setContextProperties(cm, projectFullName, OBSERVATIONS,
					obsvName, layerelement);
			String obsvRefSite = getParamAndSet("obsvRefSite",
					currentEditProjectForm.currentObservation.obsvRefSite, cm,
					cName, "1");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		layerelement.reset();
	}

	public String booltoStr(boolean tmp) {
		String boolean_value = "0";
		if (tmp)
			boolean_value = "1";
		else
			boolean_value = "0";
		return boolean_value;
	}

	public void toggleAddFaultForProject(ActionEvent ev) {
		currentEditProjectForm.initEditFormsSelection();
		String projectFullName = codeName + SEPARATOR + projectName;
		String gcname = currentEditProjectForm.currentFault.faultName;

		// Get rid of spaces.
		while (gcname.indexOf(" ") > -1) {
			gcname = gcname.substring(0, gcname.indexOf(" "))
					+ "_"
					+ gcname
							.substring(gcname.indexOf(" ") + 1, gcname.length());
		}
		currentEditProjectForm.currentFault.faultName = gcname;
		GeoFESTElement faultelement = new GeoFESTElement();
		faultelement.addElement("faultName",
				currentEditProjectForm.currentFault.faultName);
		faultelement.addElement("faultOriginX",
				currentEditProjectForm.currentFault.faultLocationX);
		faultelement.addElement("faultOriginY",
				currentEditProjectForm.currentFault.faultLocationY);
		faultelement.addElement("faultLength",
				currentEditProjectForm.currentFault.faultLength);
		faultelement.addElement("faultWidth",
				currentEditProjectForm.currentFault.faultWidth);
		faultelement.addElement("faultDepth",
				currentEditProjectForm.currentFault.faultDepth);
		faultelement.addElement("faultDipAngle",
				currentEditProjectForm.currentFault.faultDipAngle);
		faultelement.addElement("faultStrikeAngle",
				currentEditProjectForm.currentFault.faultStrikeAngle);
		faultelement.addElement("faultDipSlip",
				currentEditProjectForm.currentFault.faultSlip);
		faultelement.addElement("faultStrikeSlip",
				currentEditProjectForm.currentFault.faultRakeAngle);

		faultelement.addElement("faultOriginZ", "0.0");
		faultelement.addElement("faultNumber", "1.0");

		try {
			// --------------------------------------------------
			// Add a fault to the context.
			// --------------------------------------------------
			gcname = removeSpaces(gcname);
			gfutils.setContextProperties(cm, projectFullName, FAULTS, gcname,
					faultelement);

			String cName = projectFullName + SEPARATOR + FAULTS + SEPARATOR
					+ gcname;

			String faultOriginXVary = getParamAndSet(
					"faultOriginXVary",
					booltoStr(currentEditProjectForm.currentFault.faultOriginXVary),
					cm, cName, "0");

			String faultOriginYVary = getParamAndSet(
					"faultOriginYVary",
					booltoStr(currentEditProjectForm.currentFault.faultOriginYVary),
					cm, cName, "0");

			String faultLengthVary = getParamAndSet(
					"faultLengthVary",
					booltoStr(currentEditProjectForm.currentFault.faultLengthVary),
					cm, cName, "0");

			String faultWidthVary = getParamAndSet(
					"faultWidthVary",
					booltoStr(currentEditProjectForm.currentFault.faultWidthVary),
					cm, cName, "0");

			String faultDepthVary = getParamAndSet(
					"faultDepthVary",
					booltoStr(currentEditProjectForm.currentFault.faultDepthVary),
					cm, cName, "0");

			String faultDipAngleVary = getParamAndSet(
					"faultDipAngleVary",
					booltoStr(currentEditProjectForm.currentFault.faultDipAngleVary),
					cm, cName, "0");

			String faultStrikeAngleVary = getParamAndSet(
					"faultStrikeAngleVary",
					booltoStr(currentEditProjectForm.currentFault.faultStrikeAngleVary),
					cm, cName, "0");

			String faultDipSlipVary = getParamAndSet(
					"faultDipSlipVary",
					booltoStr(currentEditProjectForm.currentFault.faultDipSlipVary),
					cm, cName, "0");

			String faultStrikeSlipVary = getParamAndSet(
					"faultStrikeSlipVary",
					booltoStr(currentEditProjectForm.currentFault.faultStrikeSlipVary),
					cm, cName, "0");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		faultelement.reset();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	public String SaveMeshMetaData() throws Exception {
		String projectFullName = codeName + SEPARATOR + projectName;
		cm.setCurrentProperty(projectFullName, "MeshArchived", "true");
		cm.setCurrentProperty(projectFullName, "hostName", hostName);
		cm.setCurrentProperty(projectFullName, "workDir", baseWorkDir + "/"
				+ userName + "/" + projectName + "/");

		String UserMsg = "Index, tetra, and node files were saved";
		return ("MG-back");

	}
	
	public String toggleReplotGMT() {
		doAntPlotForGMT(projectName);
		return this.gmtPlotPdfUrl;
	}

	public String loadDataArchive() throws Exception {
		System.out.println("Loading project");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		return ("rdahmm-load-data-archive");
	}

	public String loadMesh() throws Exception {
		System.out.println("Loading Mesh");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();

		return ("MG-load-mesh");
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

	public String gfarchivedData() throws Exception {
		System.out.println("gf archived data");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		return ("MG-gf-archived-data");
	}

	public String gfGraphOutput() throws Exception {
		System.out.println("gf Graph Output");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		return ("MG-gf-graph-output");
	}

	public String ContourPlot() throws Exception {
		System.out.println("Contour Plot");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		FacesContext fc = FacesContext.getCurrentInstance();
		String tmp = (String) fc.getExternalContext().getRequestParameterMap()
				.get("ProjectSelect");
		tmp = (String) fc.getExternalContext().getRequestParameterMap().get(
				"DataChoice");

		return ("MG-contour-plot");
	}
	// --------------------------------------------------
	// These are accessor methods.
	// --------------------------------------------------

}
