package org.apache.myfaces.blank;

//Imports from the mother ship
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.servogrid.genericproject.Utility;

//Faces classes
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;

//Servlet and portlet API stuff.
import javax.xml.namespace.QName;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

//SOPAC Client Stuff
import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

//Usual java stuff.
import java.net.*;
import java.io.*;
import java.util.*;

// db4objects
import com.db4o.*;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

// Test
import org.servogrid.genericproject.*;


/**
 * Everything you need to set up and run STFILTER.
 */

public class STFILTERBean extends GenericSopacBean {
	String[] siteCodeArr;
	String[] sopacDataFileUrlArr;
	
	String contextUrl;

	boolean projectCreated = false;

	STFILTERProjectBean projectBean = new STFILTERProjectBean();

	AnalyzeTseriResultsBean resultsBean;

	List projectArchive=new ArrayList();
	
	String endpoint;

	String workDir;

	ObjectContainer db;

	// STFILTER properties
	String codeName = "STFILTER";

	String userName = Utility.getUserName("defaultUser");

	// private int resOption = 387;
	// private int termOption = 556;
	// private double cutoffCriterion = 1.0;
	// private double estJumpSpan = 1.0;
	// private WeakObsCriteria weakObsCriteria = new WeakObsCriteria(30.0, 30.0,
	// 50.0);
	// private OutlierCriteria outlierCriteria = new OutlierCriteria(800.0,
	// 800.0, 800.0);
	// private BadObsCriteria badObsCriteria = new BadObsCriteria(10000.0,
	// 10000.0, 10000.0);
	// private TimeInterval timeInterval = new TimeInterval(1998.0, 2006.800);

	// This is the file that will hold the
	// results of the GPS station query.
	// private String sopacDataFileName = "";

	//private String sopacDataFileContent = "";

	// private String sopacDataFileExt = ".data";

	// This is the working diretory for running the
	// code on the execution host. The global data
	// directory is the location of things like the
	// apriori file.
	// private String globalDataDir = "";

	// This is the driver file and its constituent lines.
	// private String driverFileName = "";

	// private String driverFileContent = "";
	//
	// private String driverFileExtension = ".drv";
	//
	// // These are fixed files, at least for now.
	// private String aprioriValueFile = "itrf2000_final.net";
	//
	// private String mosesParamFile = "moses_test.para";
	//
	// // These are file extensions. The files will be named after the
	// // project.
	// private String mosesDataListExt = ".list";
	//
	// private String mosesSiteListExt = ".site";
	//
	// private String mosesParamFileExt = ".para";
	//
	// private String residualFileExt = ".resi";
	//
	// private String termOutFileExt = ".mdl";
	//
	// private String outputFileExt = ".out";

	// This is the site list file
	// private String siteListFile;
	//
	// private String dataListFile;
	//
	// private String estParameterFile;

	// Project properties
	// private String[] contextList;
	//
	// private Hashtable contextListHash;
	//
	// private Vector contextListVector;

	// These contain the site estimate params. Note
	// this needs to be generalized, as I'm assuming only
	// one site is used at a time.
	StationContainer myStation;

	StationContainer allsites;

	StationParamList myStationList, allsitesList;

	MasterParamList masterList;

	// These are not needed?
	// Vector allsitesVec;
	// Vector mysiteVec;

	// Some useful rendering constants
	boolean renderAllSites = false;

	boolean renderMySite = false;

	boolean renderMasterParamList1 = false;

	boolean renderMasterParamList2 = false;

	// String stamp=(new Date()).getTime()+"";
	String stamp = "TEST";

	// .resi file
	// String resiURL = null;
	
	String DB_FILE_NANME;

	/**
	 * default empty constructor
	 */
	public STFILTERBean() {
		super();

		// Init. By Jong
		this.resource = "procCoords";
		this.contextGroup = "reasonComb";

		loadPrefs();
		updateWithPrefs();

		// cm = getContextManagerImp();

		// Set up here the station list vectors.
		masterList = new MasterParamList();
		myStationList = new StationParamList();
		allsitesList = new AllSitesParamList();

		// Set up here the station conntainer
		myStation = new MyStationContainer("LBC1");
		myStation.setEstParamVector(myStationList.getStationParamList());
		myStation.setMasterParamList(masterList.getStationParamList());

		// Set up the default station list.
		allsites = new AllStationsContainer();
		allsites.setEstParamVector(allsitesList.getStationParamList());
		allsites.setMasterParamList(masterList.getStationParamList());

		FacesContext fc = FacesContext.getCurrentInstance();
		endpoint = fc.getExternalContext().getInitParameter(
				"analyze_tseri.service.url");
		workDir = fc.getExternalContext().getInitParameter("work.dir");
		
		DB_FILE_NANME = workDir + File.separator + userName + File.separator + codeName + ".db";
	}

	protected void loadPrefs() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.getExternalContext().getRequest() instanceof PortletRequest) {
			debug("loadPrefs", "request is PortletRequest. Pref. will be loaded.");
			PortletRequest pRequest = (PortletRequest) facesContext
					.getExternalContext().getRequest();
			PortletPreferences prefs = pRequest.getPreferences();

			_siteCode = prefs.getValue("_siteCode", this.siteCode);
			_beginDate = prefs.getValue("_beginDate", this.beginDate);
			_endDate = prefs.getValue("_endDate", this.endDate);
			_bboxChecked = Boolean.parseBoolean(prefs.getValue("_bboxChecked",
					Boolean.toString(this.bboxChecked)));
			_minLatitude = Double.parseDouble(prefs.getValue("_minLatitude",
					Double.toString(this.minLatitude)));
			_maxLatitude = Double.parseDouble(prefs.getValue("_maxLatitude",
					Double.toString(this.maxLatitude)));
			_minLongitude = Double.parseDouble(prefs.getValue("_minLongitude",
					Double.toString(this.minLongitude)));
			_maxLongitude = Double.parseDouble(prefs.getValue("_maxLongitude",
					Double.toString(this.maxLongitude)));
			_resource = prefs.getValue("_resource", this.resource);
			_contextGroup = prefs.getValue("_contextGroup", this.contextGroup);
			_contextId = prefs.getValue("_contextId", this.contextId);

			// AnalyzeTseri Params
			_resOption = Integer.parseInt(prefs.getValue("_resOption", Integer
					.toString(projectBean.getResOption())));
			_termOption = Integer.parseInt(prefs.getValue("_termOption",
					Integer.toString(projectBean.getTermOption())));
			_cutoffCriterion = Double.parseDouble(prefs.getValue(
					"_cutoffCriterion", Double.toString(projectBean
							.getCutoffCriterion())));
			_estJumpSpan = Double.parseDouble(prefs.getValue("_estJumpSpan",
					Double.toString(projectBean.getEstJumpSpan())));

			_weakObsCriteria.north = Double.parseDouble(prefs.getValue(
					"_weakObsCriteria.north", Double.toString(projectBean
							.getWeakObsCriteria().getNorth())));
			_weakObsCriteria.east = Double.parseDouble(prefs.getValue(
					"_weakObsCriteria.east", Double.toString(projectBean
							.getWeakObsCriteria().getEast())));
			_weakObsCriteria.up = Double.parseDouble(prefs.getValue(
					"_weakObsCriteria.up", Double.toString(projectBean
							.getWeakObsCriteria().getUp())));

			_outlierCriteria.north = Double.parseDouble(prefs.getValue(
					"_outlierCriteria.north", Double.toString(projectBean
							.getOutlierCriteria().getNorth())));
			_outlierCriteria.east = Double.parseDouble(prefs.getValue(
					"_outlierCriteria.east", Double.toString(projectBean
							.getOutlierCriteria().getEast())));
			_outlierCriteria.up = Double.parseDouble(prefs.getValue(
					"_outlierCriteria.up", Double.toString(projectBean
							.getOutlierCriteria().getUp())));

			_badObsCriteria.north = Double.parseDouble(prefs.getValue(
					"_badObsCriteria.north", Double.toString(projectBean
							.getBadObsCriteria().getNorth())));
			_badObsCriteria.east = Double.parseDouble(prefs.getValue(
					"_badObsCriteria.east", Double.toString(projectBean
							.getBadObsCriteria().getEast())));
			_badObsCriteria.up = Double.parseDouble(prefs.getValue(
					"_badObsCriteria.up", Double.toString(projectBean
							.getBadObsCriteria().getUp())));

			_timeInterval.beginTime = Double.parseDouble(prefs.getValue(
					"_timeInterval.beginTime", Double.toString(projectBean
							.getTimeInterval().getBeginTime())));
			_timeInterval.endTime = Double.parseDouble(prefs.getValue(
					"_timeInterval.endTime", Double.toString(projectBean
							.getTimeInterval().getEndTime())));
		} else {
			debug("loadPrefs", "request is NOT PortletRequest. Pref. will NOT be loaded.");
		}
	}

	protected void makeProjectDirectory() {
		try {
			File projectDir = new File(workDir + File.separator + userName
					+ File.separator);
			debug("makeProjectDirectory", "make dir : " + projectDir.getCanonicalPath());
			projectDir.mkdirs();
		} catch (IOException e) {
			debug("makeProjectDirectory", "Failure on makeProjectDirectory()");
			e.printStackTrace();
		}
	}

	public String saveProject() throws Exception {
		STFILTERProjectBean localBean = getProjectBean();
		
		localBean.projectName = this.projectName;
		localBean.creationDate = new GregorianCalendar();
		//localBean.hostName = ?
		
		localBean.siteCode = this.siteCode;
		localBean.beginDate = this.beginDate;
		localBean.endDate = this.endDate;
		localBean.bboxChecked = this.bboxChecked;
		
		localBean.minLatitude=this.minLatitude;
		localBean.maxLatitude=this.maxLatitude;
		localBean.minLongitude=this.minLongitude;
		localBean.maxLongitude=this.maxLongitude;
		localBean.resource=this.resource;
		localBean.contextGroup=this.contextGroup;
		localBean.contextId=this.contextId;
		localBean.sopacDataFileContent = this.sopacDataFileContent;
		
		localBean.setResultsBean(getResultsBean());
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Saved with project name : "+this.projectName)); 
		makeProjectDirectory();
		Db4o.configure().lockDatabaseFile(false);
		db = Db4o.openFile(DB_FILE_NANME);
		db.set(localBean);
		db.commit();
		db.close();
		
		// Clear the in-memory value.
		// setResultsBean(null);

		return ("stfilter-project-saved");
	}

	/**
	 * Method that is backed to a submit button of a form.
	 */
	public String createNewProject() throws Exception {
		// Clear history
		paramHistory.clear();
		
//		if (!isInitialized) {
//			initWebServices();
//		}
		//updateWithPrefs();
		return ("new-project-created");
	}

	// public String paramsThenTextArea() throws Exception {
	// setParameterValues();
	// return "parameters-to-textfield";
	// }
	//
	// public String paramsThenDB() throws Exception {
	// // setParameterValues();
	// createNewProject();
	// return "parameters-to-database";
	// }
	//
	// public String paramsThenMap() throws Exception {
	// // setParameterValues();
	// createNewProject();
	// return "parameters-to-googlemap";
	// }

	// public String createNewProject() throws Exception {
	// System.out.println("Creating new project");
	// System.out.println("Project name is " + projectName);
	//
	// // Store the request values persistently
	// contextName = codeName + "/" + projectName;
	// String hostName = getHostName();
	// cm.addContext(contextName);
	// cm.setCurrentProperty(contextName, "projectName", projectName);
	// cm.setCurrentProperty(contextName, "hostName", hostName);
	// projectCreated = true;
	//
	// // This is the working directory of the execution host.
	// workDir = getBaseWorkDir() + File.separator + userName + File.separator
	// + projectName;
	//
	// globalDataDir = getBinPath();
	//
	// return "new-project-created";
	// }

	// public String setParameterValues() throws Exception {
	// // This should always be true at this point, but check for
	// // safety.
	// if (projectCreated != true) {
	// createNewProject();
	// }
	//
	// // Now set the rest of the parameters.
	// // The cm object is inherited.
	// cm.setCurrentProperty(contextName, "resOption", resOption + "");
	// cm.setCurrentProperty(contextName, "termOption", termOption + "");
	// cm.setCurrentProperty(contextName, "cutoffCriterion", cutoffCriterion
	// + "");
	// cm.setCurrentProperty(contextName, "estJumpSpan", estJumpSpan + "");
	// // cm.setCurrentProperty(contextName,"weakObsCriteria",
	// // weakObsCriteria);
	// // cm.setCurrentProperty(contextName,"outlierCriteria",outlierCriteria);
	// // cm.setCurrentProperty(contextName,"badObsCriteria",badObsCriteria);
	// // cm.setCurrentProperty(contextName,"timeInterval",timeInterval);
	// return "parameters-set";
	// }

	// public String loadDataArchive() throws Exception {
	// System.out.println("Loading project");
	// if (!isInitialized) {
	// initWebServices();
	// }
	// setContextList();
	// return ("load-data-archive");
	// }

	 public String loadProject() throws Exception {
		projectArchive.clear();
		try {
			Db4o.configure().lockDatabaseFile(false);
			db = Db4o.openFile(DB_FILE_NANME);

			Query query = db.query();
			query.constrain(STFILTERProjectBean.class);
			// query.descend("creationDate").orderAscending();
			ObjectSet results = query.execute();
			while (results.hasNext()) {
				projectArchive.add((STFILTERProjectBean) results.next());
			}
			db.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return ("list-old-projects");
	}

	// public String loadProjectKillList() throws Exception {
	// System.out.println("Loading project");
	// if (!isInitialized) {
	// initWebServices();
	// }
	// setContextList();
	// return ("list-death-row");
	// }

	// public String loadProjectPlots() throws Exception {
	// System.out.println("Loading project");
	// if (!isInitialized) {
	// initWebServices();
	// }
	// setContextList();
	// return ("list-project-plots");
	// }

	// public String launchSTFILTER() throws Exception {
	// // Do this here.
	// setParameterValues();
	//
	// String sopacDataFileName = getSiteCode() + sopacDataFileExt;
	// String cfullName = codeName + "/" + projectName;
	// String contextDir = cm.getCurrentProperty(cfullName, "Directory");
	//
	// createDriverFile(contextDir);
	// createSopacDataFile(contextDir, sopacDataFileName, sopacDataFileContent);
	// createSiteListFile(contextDir);
	// createDataListFile(contextDir);
	// createEstimatedParamFile(contextDir);
	// String value = executeSTFILTER(contextDir, sopacDataFileName, cfullName);
	// return "stfilter-launched";
	// }

	public String callAnalyzeTseriService() throws Exception {
		// Do this here.
		try {

			debug("callAnalyzeTseriService", "endpoint = " + endpoint);

			if ((endpoint == null) || (endpoint.equals(""))) {
				debug("callAnalyzeTseriService", "Set init-param in web.xml");
				return "";
			}
			
			int globalParamSize = allsites.estParamVector.size()
					+ episodicParams.size() + annualAmpParams.size()
					+ annualPhaseParams.size() + semiannualAmpParams.size();
			int siteParamSize = myStation.estParamVector.size();
			projectBean.globalParam = new double[globalParamSize][5];
			projectBean.siteParam = new double[siteParamSize][5];

			setParam(allsites, projectBean.globalParam);
			setParam(myStation, projectBean.siteParam);

			// For "Applied Filter List"
			allParams.clear();
			for (Iterator i = episodicParams.iterator(); i.hasNext();) {
				allParams.add((EstimateParameter) i.next());
			}
			for (Iterator i = annualAmpParams.iterator(); i.hasNext();) {
				allParams.add((EstimateParameter) i.next());
			}
			for (Iterator i = annualPhaseParams.iterator(); i.hasNext();) {
				allParams.add((EstimateParameter) i.next());
			}
			for (Iterator i = semiannualAmpParams.iterator(); i.hasNext();) {
				allParams.add((EstimateParameter) i.next());
			}

			// siteParam doesn't need anymore
			int init = allsites.estParamVector.size();
			debug("callAnalyzeTseriService", "episodicParams.size = " + episodicParams.size());
			for (int i = init; i < init + this.episodicParams.size(); i++) {
				debug("callAnalyzeTseriService", episodicParams.get(i - init).toString());
				EpisodicBias eb = (EpisodicBias) episodicParams.get(i - init);
				projectBean.globalParam[i][0] = eb.getParameterType();
				projectBean.globalParam[i][1] = eb.getAprioriValue().doubleValue();
				projectBean.globalParam[i][2] = eb.getAprioriConstraint().doubleValue();
				projectBean.globalParam[i][3] = eb.getStartDate().doubleValue();
				projectBean.globalParam[i][4] = eb.getEndDate().doubleValue();
			}

			init = init + episodicParams.size();
			debug("callAnalyzeTseriService", "annualAmpParams.size = " + annualAmpParams.size());
			for (int i = init; i < init + this.annualAmpParams.size(); i++) {
				debug("callAnalyzeTseriService", annualAmpParams.get(i - init).toString());
				AnnualAmpBias eb = (AnnualAmpBias) annualAmpParams
						.get(i - init);
				projectBean.globalParam[i][0] = eb.getParameterType();
				projectBean.globalParam[i][1] = eb.getAprioriValue().doubleValue();
				projectBean.globalParam[i][2] = eb.getAprioriConstraint().doubleValue();
				projectBean.globalParam[i][3] = eb.getStartDate().doubleValue();
				projectBean.globalParam[i][4] = eb.getPeriodLength().doubleValue();
			}

			init = init + this.annualAmpParams.size();
			debug("callAnalyzeTseriService", "annualPhaseParams.size = " + annualPhaseParams.size());
			for (int i = init; i < init + this.annualPhaseParams.size(); i++) {
				debug("callAnalyzeTseriService", annualPhaseParams.get(i - init).toString());
				AnnualPhaseBias eb = (AnnualPhaseBias) annualPhaseParams.get(i
						- init);
				projectBean.globalParam[i][0] = eb.getParameterType();
				projectBean.globalParam[i][1] = eb.getAprioriValue().doubleValue();
				projectBean.globalParam[i][2] = eb.getAprioriConstraint().doubleValue();
				projectBean.globalParam[i][3] = eb.getStartDate().doubleValue();
				projectBean.globalParam[i][4] = eb.getPeriodLength().doubleValue();
			}

			init = init + this.annualPhaseParams.size();
			debug("callAnalyzeTseriService", "semiannualAmpParams.size = " + semiannualAmpParams.size());
			for (int i = init; i < init + this.semiannualAmpParams.size(); i++) {
				debug("callAnalyzeTseriService", semiannualAmpParams.get(i - init).toString());
				SemiannualAmpBias eb = (SemiannualAmpBias) semiannualAmpParams
						.get(i - init);
				projectBean.globalParam[i][0] = eb.getParameterType();
				projectBean.globalParam[i][1] = eb.getAprioriValue().doubleValue();
				projectBean.globalParam[i][2] = eb.getAprioriConstraint().doubleValue();
				projectBean.globalParam[i][3] = eb.getStartDate().doubleValue();
				projectBean.globalParam[i][4] = eb.getPeriodLength().doubleValue();
			}

			Service service = new Service();
			Call call = (Call) service.createCall();

			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://soapinterop.org/",
					"execAnalyzeTseri"));

			debug("callAnalyzeTseriService", "projectBean.globalParam.length = "+projectBean.globalParam.length);
			debug("callAnalyzeTseriService", "projectBean.siteParam.length = "+projectBean.siteParam.length);

			String[] ret = (String[]) call.invoke(new Object[] { this.siteCodeArr,
					new Integer(this.projectBean.resOption),
					new Integer(this.projectBean.termOption),
					new Double(this.projectBean.cutoffCriterion),
					new Double(this.projectBean.estJumpSpan),
					new Double(this.projectBean.weakObsCriteria.east),
					new Double(this.projectBean.weakObsCriteria.north),
					new Double(this.projectBean.weakObsCriteria.up),
					new Double(this.projectBean.outlierCriteria.east),
					new Double(this.projectBean.outlierCriteria.north),
					new Double(this.projectBean.outlierCriteria.up),
					new Double(this.projectBean.badObsCriteria.east),
					new Double(this.projectBean.badObsCriteria.north),
					new Double(this.projectBean.badObsCriteria.up),
					new Double(this.projectBean.timeInterval.beginTime),
					new Double(this.projectBean.timeInterval.endTime),
					this.sopacDataFileUrlArr, projectBean.globalParam,
					projectBean.siteParam });

			System.out.println("Output: ");
			for (int i = 0; i < ret.length; i++) {
				System.out.println(ret[i]);
			}

			// Temporary. will be updated soon
			// String[] extensions = { ".input.xyz.X.png", ".input.xyz.Y.png",
			// ".input.xyz.Z.png", ".resi", ".data", ".drv", ".input",
			// ".list", ".mdl", ".out", ".para", ".site" };
			resultsBean = new AnalyzeTseriResultsBean();
			resultsBean.setXPngUrl(ret[0]);
			resultsBean.setYPngUrl(ret[1]);
			resultsBean.setZPngUrl(ret[2]);
			resultsBean.setResiUrl(ret[3]);
			resultsBean.setDataUrl(ret[4]);
			resultsBean.setDrvUrl(ret[5]);
			resultsBean.setInputUrl(ret[6]);
			resultsBean.setListUrl(ret[6+siteCodeArr.length]);
			resultsBean.setMdlUrl(ret[7+siteCodeArr.length]);
			resultsBean.setOutUrl(ret[8+siteCodeArr.length]);
			resultsBean.setParaUrl(ret[9+siteCodeArr.length]);
			resultsBean.setSiteUrl(ret[10+siteCodeArr.length]);

			String[] inputUrlArr = new String[siteCodeArr.length];
			for (int i = 0; i < siteCodeArr.length; i++) {
				inputUrlArr[i] = ret[6 + i];
			}
			resultsBean.setInputUrlArr(inputUrlArr);

			// Draw graphs
			// resiURL = ret[3];

			//List filteredList = createFilteredList();
			List[] filteredListArr = createFilteredListArr();
			
			if (paramHistory.size() < maxHistory) {
				paramHistory.add(0, filteredListArr);
			} else {
				paramHistory.add(0, filteredListArr);
				for (int i = paramHistory.size(); i > maxHistory; i--) {
					paramHistory.remove(i - 1);
				}
			}
			//debug("callAnalyzeTseriService", "paramHistory.size() = " + paramHistory.size());

			// System.out.println("Output: " + ret);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return "stfilterws-launched";
	}

	// public String launchSTFILTERWS() throws Exception {
	// // Do this here.
	// try {
	//		
	// // String endpoint =
	// //
	// "http://gf3.ucs.indiana.edu:8888/analyze-tseri-exec/services/AnalyzeTseriExec";
	// // String contextDir =
	// //
	// "/home/jychoi/apps/QuakeSim2/portal_deploy/apache-tomcat-5.5.20/webapps/STFILTER/WDIR/";
	// // String dataUrl =
	// // "http://gf3.ucs.indiana.edu:8888/STFILTER/WDIR/"+sopacDataFileName;
	// FacesContext fc = FacesContext.getCurrentInstance();
	// String endpoint =
	// fc.getExternalContext().getInitParameter("analyze_tseri.service.url");
	// debug("endpoint = "+endpoint);
	// String workDir = fc.getExternalContext().getInitParameter("work.dir");
	// debug("workDir = "+workDir);
	// String workUrl = fc.getExternalContext().getInitParameter("work.url");
	// debug("workUrl = "+workUrl);
	//			
	// if ((endpoint == null) || (endpoint.equals(""))) {
	// debug("Set init-param in web.xml");
	// return "";
	// }
	//			
	// String siteCode = getSiteCode();
	// String sopacDataFileName = projectName+"-"+getSiteCode()+"-"+stamp +
	// sopacDataFileExt;
	// debug("sopacDataFileName = "+sopacDataFileName);
	// String cfullName = codeName + "/" + projectName;
	//			
	// String dataUrl = workUrl+sopacDataFileName;
	// debug("dataUrl = "+workUrl);
	//			
	// createSopacDataFile(workDir, sopacDataFileName, sopacDataFileContent);
	// debug("dataUrl = "+dataUrl);
	//			
	// double[][] globalParam = new double[allsites.estParamVector.size()][5];
	// double[][] siteParam = new
	// double[myStation.estParamVector.size()+episodicParams.size()][5];
	//			
	// setParam(allsites, globalParam);
	// setParam(myStation, siteParam);
	//		    
	// allParams.clear();
	// allParams.add(new AnnualAmpNorth());
	// allParams.add(new AnnualAmpUp());
	// for (Iterator i = episodicParams.iterator();i.hasNext();) {
	// allParams.add((EstimateParameter) i.next());
	// }
	// for (Iterator i = annualAmpParams.iterator();i.hasNext();) {
	// allParams.add((EstimateParameter) i.next());
	// }
	// for (Iterator i = annualPhaseParams.iterator();i.hasNext();) {
	// allParams.add((EstimateParameter) i.next());
	// }
	// for (Iterator i = semiannualAmpParams.iterator();i.hasNext();) {
	// allParams.add((EstimateParameter) i.next());
	// }
	//		    
	// // siteParam doesn't need anymore
	// int init = myStation.estParamVector.size();
	// for (int i = init; i < init + this.episodicParams.size(); i++) {
	// System.out.println("[!!]"+episodicParams.get(i-init).toString());
	// System.out.println("[!!]"+episodicParams.size());
	// EpisodicBias eb = (EpisodicBias) episodicParams.get(i-init);
	// globalParam[i][0] = eb.getParameterType();
	// globalParam[i][1] = eb.getAprioriConstraint().doubleValue();
	// globalParam[i][2] = eb.getAprioriValue().doubleValue();
	// globalParam[i][3] = eb.getStartDate().doubleValue();
	// globalParam[i][4] = eb.getEndDate().doubleValue();
	// }
	//		    
	// init = init + episodicParams.size();
	// for (int i = init; i < init + this.annualAmpParams.size(); i++) {
	// System.out.println("[!!]"+annualAmpParams.get(i-init).toString());
	// System.out.println("[!!]"+annualAmpParams.size());
	// AnnualAmpBias eb = (AnnualAmpBias) annualAmpParams.get(i-init);
	// globalParam[i][0] = eb.getParameterType();
	// globalParam[i][1] = eb.getAprioriConstraint().doubleValue();
	// globalParam[i][2] = eb.getAprioriValue().doubleValue();
	// globalParam[i][3] = eb.getStartDate().doubleValue();
	// globalParam[i][4] = eb.getPeriodLength().doubleValue();
	// }
	//
	// init = init + this.annualAmpParams.size();
	// for (int i = init; i < init + this.annualPhaseParams.size(); i++) {
	// System.out.println("[!!]"+annualPhaseParams.get(i-init).toString());
	// System.out.println("[!!]"+annualPhaseParams.size());
	// AnnualPhaseBias eb = (AnnualPhaseBias) annualPhaseParams.get(i-init);
	// globalParam[i][0] = eb.getParameterType();
	// globalParam[i][1] = eb.getAprioriConstraint().doubleValue();
	// globalParam[i][2] = eb.getAprioriValue().doubleValue();
	// globalParam[i][3] = eb.getStartDate().doubleValue();
	// globalParam[i][4] = eb.getPeriodLength().doubleValue();
	// }
	//
	// init = init + this.annualPhaseParams.size();
	// for (int i = init; i < init + this.semiannualAmpParams.size(); i++) {
	// System.out.println("[!!]"+semiannualAmpParams.get(i-init).toString());
	// System.out.println("[!!]"+semiannualAmpParams.size());
	// SemiannualAmpBias eb = (SemiannualAmpBias)
	// semiannualAmpParams.get(i-init);
	// globalParam[i][0] = eb.getParameterType();
	// globalParam[i][1] = eb.getAprioriConstraint().doubleValue();
	// globalParam[i][2] = eb.getAprioriValue().doubleValue();
	// globalParam[i][3] = eb.getStartDate().doubleValue();
	// globalParam[i][4] = eb.getPeriodLength().doubleValue();
	// }
	//
	// Service service = new Service();
	// Call call = (Call) service.createCall();
	//
	// call.setTargetEndpointAddress(new java.net.URL(endpoint));
	// call.setOperationName(new QName("http://soapinterop.org/",
	// "execATS"));
	//
	// // public static String[] execATS(String siteCode,
	// // int resOption, int termOption, double cutoffCriterion, double
	// estJumpSpan,
	// // double weakObsCriteriaEast, double weakObsCriteriaNorth, double
	// weakObsCriteriaUp,
	// // double outlierCriteriaEast, double outlierCriteriaNorth, double
	// outlierCriteriaUp,
	// // double badObsCriteriaEast, double badObsCriteriaNorth, double
	// badObsCriteriaUp,
	// // double timeIntervalBeginTime, double timeIntervalEndTime,
	// // String dataUrl, double[][] globalParam, double[][] siteParam) {
	//			
	// String[] ret = (String[]) call.invoke(new Object[] { siteCode,
	// new Integer(this.resOption), new Integer(this.termOption),
	// new Double(this.cutoffCriterion), new Double(this.estJumpSpan),
	// new Double(this.weakObsCriteria.east), new
	// Double(this.weakObsCriteria.north), new Double(this.weakObsCriteria.up),
	// new Double(this.outlierCriteria.east), new
	// Double(this.outlierCriteria.north), new Double(this.outlierCriteria.up),
	// new Double(this.badObsCriteria.east), new
	// Double(this.badObsCriteria.north), new Double(this.badObsCriteria.up),
	// new Double(this.timeInterval.beginTime), new
	// Double(this.timeInterval.endTime),
	// dataUrl, globalParam, siteParam });
	//			
	// System.out.println("Output: ");
	// for (int i = 0; i < ret.length; i++) {
	// System.out.println(ret[i]);
	// }
	//
	// // Draw graphs
	// //resiURL = ret[3];
	//			
	// List filteredList = createFilteredList();
	// if (paramHistory.size() < maxHistory) {
	// paramHistory.add(0, filteredList);
	// } else {
	// paramHistory.add(0, filteredList);
	// for (int i = paramHistory.size(); i > maxHistory ; i--) {
	// paramHistory.remove(i-1);
	// }
	// }
	//			
	// System.out.println("paramHistory.size() = "+paramHistory.size());
	//
	// // System.out.println("Output: " + ret);
	// } catch (Exception e) {
	// System.err.println(e.toString());
	// }
	// return "stfilterws-launched";
	// }

	// private String extractSimpleName(String extendedName) {
	// return (new File(extendedName)).getName();
	// }

	// /**
	// * Famous method that I googled. This copies a file to a new place on the
	// * file system.
	// */
	// private void copyFileToFile(File sourceFile, File destFile)
	// throws Exception {
	// InputStream in = new FileInputStream(sourceFile);
	// OutputStream out = new FileOutputStream(destFile);
	// byte[] buf = new byte[1024];
	// int length;
	// while ((length = in.read(buf)) > 0) {
	// out.write(buf, 0, length);
	// }
	// in.close();
	// out.close();
	// }

	// /**
	// * Another famous method that I googled. This downloads contents from the
	// * given URL to a local file.
	// */
	// private void copyUrlToFile(URL inputFileUrl, String destFile)
	// throws Exception {
	//
	// URLConnection uconn = inputFileUrl.openConnection();
	// InputStream in = inputFileUrl.openStream();
	// OutputStream out = new FileOutputStream(destFile);
	//
	// // Extract the name of the file from the url.
	//
	// byte[] buf = new byte[1024];
	// int length;
	// while ((length = in.read(buf)) > 0) {
	// out.write(buf, 0, length);
	// }
	// in.close();
	// out.close();
	//
	// }

	// private String downloadInputFile(String inputFileUrlString,
	// String inputFileDestDir) throws Exception {
	//
	// // Convert to a URL. This will throw an exception if
	// // malformed.
	// URL inputFileUrl = new URL(inputFileUrlString);
	//
	// String protocol = inputFileUrl.getProtocol();
	// System.out.println("Protocol: " + protocol);
	// String fileSimpleName = extractSimpleName(inputFileUrl.getFile());
	// System.out.println(fileSimpleName);
	//
	// String fileLocalFullName = inputFileDestDir + File.separator
	// + fileSimpleName;
	//
	// if (protocol.equals(FILE_PROTOCOL)) {
	// String filePath = inputFileUrl.getFile();
	// fileSimpleName = inputFileUrl.getFile();
	//
	// System.out.println("File path is " + filePath);
	// File filePathObject = new File(filePath);
	// File destFileObject = new File(fileLocalFullName);
	//
	// // See if the inputFileUrl and the dest file are the same.
	// if (filePathObject.getCanonicalPath().equals(
	// destFileObject.getCanonicalPath())) {
	// System.out.println("Files are the same. We're done.");
	// return fileLocalFullName;
	// }
	//
	// // Otherwise, we will have to copy it.
	// copyFileToFile(filePathObject, destFileObject);
	// return fileLocalFullName;
	// }
	//
	// else if (protocol.equals(HTTP_PROTOCOL)) {
	// copyUrlToFile(inputFileUrl, fileLocalFullName);
	// }
	//
	// else {
	// System.out.println("Unknown protocol for accessing inputfile");
	// throw new Exception("Unknown protocol");
	// }
	// return fileLocalFullName;
	// }

	public List createFilteredList() {
		URL url;
		ArrayList list = new ArrayList();
		ArrayList row = null;

		try {

			url = new URL(resultsBean.getResiUrl());
			debug("createFilteredList", "resultsBean.getResiUrl() = " +resultsBean.getResiUrl());
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			String line;

			Calendar thisYear = Calendar.getInstance();
			Calendar nextYear = Calendar.getInstance();
			Calendar cur = Calendar.getInstance();

			double time;
			int idx = 1;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				if (line.startsWith("*")) {
					continue;
				}
				st = new StringTokenizer(line);
				row = new ArrayList();
				row.add(String.valueOf(idx)); // idx

				time = Double.parseDouble(st.nextToken());
				thisYear.set((int) Math.floor(time), 0, 1, 0, 0, 0);
				nextYear.set((int) Math.ceil(time), 0, 1, 0, 0, 0);
				cur = Calendar.getInstance();
				cur
						.setTimeInMillis(thisYear.getTimeInMillis()
								+ (long) ((time - Math.floor(time)) * (nextYear
										.getTimeInMillis() - thisYear
										.getTimeInMillis())));
				// debug("["+idx+"] time = "+time+", cur
				// ="+DateFormat.getDateInstance().format(cur.getTime()));
				row.add(cur.getTime()); // time
				row.add(st.nextToken()); // E
				row.add(st.nextToken()); // N
				st.nextToken(); // Se
				st.nextToken(); // Sn
				st.nextToken(); // Ren
				row.add(st.nextToken()); // U
				list.add(row);
				idx++;
			}
			in.close();

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return list;
		// BufferedReader rd = new BufferedReader( new StringReader
		// (sopacDataFileContent));
	}

	public List[] createFilteredListArr() {
		URL url;
		ArrayList[] listArr = new ArrayList[siteCodeArr.length];
		ArrayList row = null;

		try {

			url = new URL(resultsBean.getResiUrl());
			debug("createFilteredList", "resultsBean.getResiUrl() = " +resultsBean.getResiUrl());
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			String line;

			Calendar thisYear = Calendar.getInstance();
			Calendar nextYear = Calendar.getInstance();
			Calendar cur = Calendar.getInstance();

			double time;
			int listIdx = -1;
			int idx = 1;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				if (line.startsWith("*")) {
					if (line.startsWith("*   residuals for site")) {
						listIdx++;
						listArr[listIdx] = new ArrayList();
					}
					continue;
				}
				st = new StringTokenizer(line);
				row = new ArrayList();
				row.add(String.valueOf(idx)); // idx

				time = Double.parseDouble(st.nextToken());
				thisYear.set((int) Math.floor(time), 0, 1, 0, 0, 0);
				nextYear.set((int) Math.ceil(time), 0, 1, 0, 0, 0);
				cur = Calendar.getInstance();
				cur
						.setTimeInMillis(thisYear.getTimeInMillis()
								+ (long) ((time - Math.floor(time)) * (nextYear
										.getTimeInMillis() - thisYear
										.getTimeInMillis())));
				// debug("["+idx+"] time = "+time+", cur
				// ="+DateFormat.getDateInstance().format(cur.getTime()));
				row.add(cur.getTime()); // time
				row.add(st.nextToken()); // E
				row.add(st.nextToken()); // N
				st.nextToken(); // Se
				st.nextToken(); // Sn
				st.nextToken(); // Ren
				row.add(st.nextToken()); // U
//				debug("createFilteredListArr", "OK1");
//				debug("createFilteredListArr", "listArr.length = "+listArr.length);
//				debug("createFilteredListArr", "listArr[0].size = "+listArr[0].size());
				
				listArr[listIdx].add(row);
//				debug("createFilteredListArr", "OK2. idx = " + idx);
				idx++;
			}
			in.close();

		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		debug("createFilteredListArr", "listArr.length = "+ listArr.length);
		return listArr;
		// BufferedReader rd = new BufferedReader( new StringReader
		// (sopacDataFileContent));
	}

	public List getSopacDataList() {
		ArrayList list = new ArrayList();
		ArrayList row = null;
		StringTokenizer st = new StringTokenizer(sopacDataFileContent);
		int idx = 1;
		while (st.hasMoreTokens()) {
			row = new ArrayList();
			row.add(String.valueOf(idx));
			row.add(st.nextToken());
			row.add(st.nextToken());
			row.add(st.nextToken());
			row.add(st.nextToken());
			row.add(st.nextToken());
			row.add(st.nextToken());
			row.add(st.nextToken());
			row.add(st.nextToken());
			list.add(row);
			idx++;
		}
		return list;
		// BufferedReader rd = new BufferedReader( new StringReader
		// (sopacDataFileContent));
	}

	private void setParam(StationContainer station, double[][] param) {
		for (int i = 0; i < station.estParamVector.size(); i++) {
			EstimateParameter ep = (EstimateParameter) station.estParamVector
					.get(i);
			param[i][0] = ep.parameterType;
			switch (ep.parameterType) {
			case 1:
			case 2:
			case 3:
				param[i][1] = ((ConstantBias) ep).aprioriValue.doubleValue();
				param[i][2] = ((ConstantBias) ep).aprioriConstraint.doubleValue();
				param[i][3] = ((ConstantBias) ep).startDate.doubleValue();
				break;
			case 4:
			case 5:
			case 6:
				param[i][1] = ((VelocityBias) ep).aprioriValue.doubleValue();
				param[i][2] = ((VelocityBias) ep).aprioriConstraint.doubleValue();
				param[i][3] = ((VelocityBias) ep).startDate.doubleValue();
				param[i][4] = ((VelocityBias) ep).endDate.doubleValue();
				break;
			case 7:
			case 8:
			case 9:
				param[i][1] = ((EpisodicBias) ep).aprioriValue.doubleValue();
				param[i][2] = ((EpisodicBias) ep).aprioriConstraint.doubleValue();
				param[i][3] = ((EpisodicBias) ep).startDate.doubleValue();
				param[i][4] = ((EpisodicBias) ep).endDate.doubleValue();
				break;
			}
		}
	}

	// public String populateAndPlot() throws Exception {
	// populateProject();
	// launchPlot();
	// return "plot-created";
	// }
	//
	// /**
	// * Currently empty.
	// */
	// public String launchPlot() throws Exception {
	//
	// return "does nothing";
	// }
	//
	// /**
	// * Create the site list file. Currently we only support one site and the
	// XYZ
	// * format (ie "1 8").
	// */
	// public void createSiteListFile(String contextDir) throws Exception {
	//
	// String slash = "/"; // This is not File.separator of the webserver
	// siteListFile = projectName + mosesSiteListExt;
	// System.out.println("Writing input file: " + contextDir + "/"
	// + siteListFile);
	// PrintWriter pw = new PrintWriter(new FileWriter(contextDir + "/"
	// + siteListFile), true);
	//
	// pw.println(" 1"); // Need to make this more general.
	// pw.println(getSiteCode().toUpperCase() + "_GPS");
	// pw.close();
	// }
	//
	// public void createEstimatedParamFile(String contextDir) throws Exception
	// {
	// estParameterFile = projectName + mosesParamFileExt;
	// PrintWriter pw = new PrintWriter(new FileWriter(contextDir + "/"
	// + estParameterFile), true);
	// if (myStation.printContents() != null) {
	// pw.println(" 2");
	// pw.println(allsites.printContents());
	// pw.println(myStation.printContents());
	// } else {
	// pw.println(" 1");
	// pw.println(allsites.printContents());
	// }
	// pw.close();
	// }
	//
	// public void createDataListFile(String contextDir) throws Exception {
	//
	// String slash = "/"; // This is not File.separator of the webserver
	// dataListFile = projectName + mosesDataListExt;
	// System.out.println("Writing input file: " + contextDir + "/"
	// + dataListFile);
	// PrintWriter pw = new PrintWriter(new FileWriter(contextDir + "/"
	// + dataListFile), true);
	//
	// pw.println(" 1 8"); // Need to make this more general.
	// pw.println(getSiteCode() + sopacDataFileExt);
	// pw.close();
	// }

	// /**
	// * Create the stfilter driver file.
	// */
	// public String createDriverFile(String contextDir) throws Exception {
	//
	// String fivespace = " ";
	// String slash = "/"; // This is not File.separator of the webserver
	// driverFileName = projectName + driverFileExtension;
	// System.out.println("Writing input file: " + contextDir + "/"
	// + driverFileName);
	// PrintWriter pw = new PrintWriter(new FileWriter(contextDir + "/"
	// + driverFileName), true);
	// pw.println(twospace + "apriori value file:" + twospace + globalDataDir
	// + slash + aprioriValueFile);
	// pw.println(twospace + "input file:" + twospace + workDir + slash
	// + projectName + mosesDataListExt);
	// pw.println(twospace + "sit_list file:" + twospace + workDir + slash
	// + projectName + mosesSiteListExt);
	// pw.println(twospace + "est_parameter file:" + twospace + workDir
	// + slash + projectName + mosesParamFileExt);
	// // pw.println(twospace+"est_parameter
	// // file:"+twospace+globalDataDir+mosesParamFile);
	// pw.println(twospace + "output file:" + twospace + workDir + slash
	// + projectName + outputFileExt);
	// pw.println(twospace + "residual file:" + twospace + workDir + slash
	// + projectName + residualFileExt);
	// pw.println(twospace + "res_option:" + twospace + resOption);
	// pw.println(twospace + "specific term_out file:" + twospace + workDir
	// + slash + projectName + termOutFileExt);
	// pw.println(twospace + "specific term_option:" + twospace + termOption);
	// pw.println(twospace + "enu_correlation usage:" + twospace + "no");
	// pw.println(twospace + "cutoff criterion (year):" + twospace
	// + cutoffCriterion);
	// pw.println(twospace + "span to est jump aper (est_jump_span):"
	// + twospace + estJumpSpan);
	// pw.println(twospace + "weak_obs (big sigma) criteria:" + twospace
	// + weakObsCriteria.getEast() + twospace
	// + weakObsCriteria.getNorth() + twospace
	// + weakObsCriteria.getUp());
	// pw.println(twospace + "outlier (big o-c) criteria mm:" + twospace
	// + outlierCriteria.getEast() + twospace
	// + outlierCriteria.getNorth() + twospace
	// + outlierCriteria.getUp());
	// pw
	// .println(twospace + "very bad_obs criteria mm:" + twospace
	// + badObsCriteria.getEast() + twospace
	// + badObsCriteria.getNorth() + twospace
	// + badObsCriteria.getUp());
	// pw.println(twospace + "t_interval:" + twospace
	// + timeInterval.getBeginTime() + twospace
	// + timeInterval.getEndTime());
	// pw.println(twospace + "end:");
	// pw.println("---------- part 2 -- apriori information");
	// pw.println(twospace + "exit:");
	// pw.close();
	//
	// // Clean this up since it could be a memory drain.
	// // sopacDataFileContent=null;
	// return "input-file-created";
	// }

	 public String deleteProject() throws Exception {

		Db4o.configure().lockDatabaseFile(false);
		db = Db4o.openFile(DB_FILE_NANME);
		List<STFILTERProjectBean> result = queryDB(db);

		if (result.size() == 0) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage("No such project exists."));
			db.close();
			return "project-populated-error";
		} else if (result.size() > 1) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"More than one projects exist."));
			db.close();
			return "project-populated-error";
		}

		projectBean = (STFILTERProjectBean) result.get(0);
		// debug("projectBean.projectName = " + projectBean.projectName);
		// debug("projectBean.beginDate = " + projectBean.beginDate);

		db.delete(projectBean);
		db.close();

		loadProject();
		return "project-removed";
	}

	/**
	 * As currently written, this method sets properties that are specific to
	 * the backend application.
	 */
	public String populateProject() throws Exception {
		Db4o.configure().lockDatabaseFile(false);
		db = Db4o.openFile(DB_FILE_NANME);
		List<STFILTERProjectBean> result = queryDB(db);

		if (result.size() == 0) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null,
					new FacesMessage("No such project exists."));
			db.close();
			return "project-populated-error";
		} else if (result.size() > 1) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(
					"More than one projects exist."));
			db.close();
			return "project-populated-error";
		}

		projectBean = (STFILTERProjectBean) result.get(0);
		// debug("projectBean.projectName = " + projectBean.projectName);
		// debug("projectBean.beginDate = " + projectBean.beginDate);

		db.close();

		this.projectName = projectBean.projectName;
		this.siteCode = projectBean.siteCode;
		this.beginDate = projectBean.beginDate;
		this.endDate = projectBean.endDate;
		this.bboxChecked = projectBean.bboxChecked;
		this.minLatitude = projectBean.minLatitude;
		this.maxLatitude = projectBean.maxLatitude;
		this.minLongitude = projectBean.minLongitude;
		this.maxLongitude = projectBean.maxLongitude;
		this.resource = projectBean.resource;
		this.contextGroup = projectBean.contextGroup;
		this.contextId = projectBean.contextId;
		this.sopacDataFileContent = projectBean.sopacDataFileContent;

		return "project-populated";
	}

	private List<STFILTERProjectBean> queryDB(ObjectContainer db4o) {
		final String queryDate = getChoosenProject();
		debug("queryDB", "queryDate = "+queryDate);
		
		List <STFILTERProjectBean> result = db4o.query( new Predicate<STFILTERProjectBean> () {
			public boolean match (STFILTERProjectBean bean) {
				return bean.creationDate.getTime().getTime() == Long.parseLong(queryDate);
			}
		});
		
		
		// Query query = db.query();
		// query.constrain(STFILTERProjectBean.class);
		// query.descend("projectName").constrain(chosenProject);
		// ObjectSet result = query.execute();
		debug("queryDB", "result.size() = " + result.size());
		//db.close();
		
		return result;
	}

	private String getChoosenProject() {
		// debug("getChoosenProject");
		String chosenProject;
		Map requestParams = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap();
		Iterator itr = requestParams.keySet().iterator();
		String key = "";
		for (; itr.hasNext();) {
			key = (String) itr.next();
			// debug("key = "+key);
			// debug("value = "+requestParams.get(key).getClass().getName());
			// debug("value = "+(String) (requestParams.get(key)));
			if (key.endsWith("creationDate")) {
				// debug("found : " + key);
				break;
			}
		}
		chosenProject = (String) (requestParams.get(key));
		debug("getChoosenProject", "chosenProject = " + chosenProject);
		// debug("requestParams.size() = "+requestParams.size());
		return chosenProject;
	}
	// public String executeSTFILTER(String contextDir, String
	// sopacDataFileName,
	// String cfullName) throws Exception {
	//
	// System.out.println("FileService URL:" + fileServiceUrl);
	// System.out.println("AntService URL:" + antUrl);
	//
	// // --------------------------------------------------
	// // Set up the Ant Service and make the directory
	// // --------------------------------------------------
	// AntVisco ant = new AntViscoServiceLocator()
	// .getAntVisco(new URL(antUrl));
	// String bf_loc = binPath + "/" + "build.xml";
	// String[] args0 = new String[4];
	// args0[0] = "-DworkDir.prop=" + workDir;
	// args0[1] = "-buildfile";
	// args0[2] = bf_loc;
	// args0[3] = "MakeWorkDir";
	//
	// ant.setArgs(args0);
	// ant.run();
	//
	// // --------------------------------------------------
	// // Set up the file service and upload the driver,
	// // site list, and gps data files.
	// // --------------------------------------------------
	// FSClientStub fsclient = new FSClientStub();
	// String sopacDestfile = workDir + "/" + sopacDataFileName;
	// String driverDestfile = workDir + "/" + driverFileName;
	// String siteListDestfile = workDir + "/" + siteListFile;
	// String dataListDestfile = workDir + "/" + dataListFile;
	// String estParamDestfile = workDir + "/" + estParameterFile;
	//
	// try {
	// fsclient.setBindingUrl(fileServiceUrl);
	// fsclient.uploadFile(contextDir + "/" + sopacDataFileName,
	// sopacDestfile);
	// fsclient.uploadFile(contextDir + "/" + siteListFile,
	// siteListDestfile);
	// fsclient.uploadFile(contextDir + "/" + dataListFile,
	// dataListDestfile);
	// fsclient.uploadFile(contextDir + "/" + driverFileName,
	// driverDestfile);
	// fsclient.uploadFile(contextDir + "/" + estParameterFile,
	// estParamDestfile);
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	//
	// // //--------------------------------------------------
	// // // Record the names of the input, output, and log
	// // // files on the remote server.
	// // //--------------------------------------------------
	// // String remoteOutputFile=workDir+"/"+projectName+".output";
	// // String remoteLogFile=workDir+"/"+projectName+".stdout";
	//
	// // cm.setCurrentProperty(cfullName,"RemoteInputFile",destfile);
	// // cm.setCurrentProperty(cfullName,"RemoteOutputFile",remoteOutputFile);
	// // cm.setCurrentProperty(cfullName,"RemoteLogFile",remoteLogFile);
	//
	// // --------------------------------------------------
	// // Run the code.
	// // --------------------------------------------------
	//
	// String[] args = new String[7];
	// args[0] = "-DworkDir.prop=" + workDir;
	// args[1] = "-DprojectName.prop=" + projectName;
	// args[2] = "-Dbindir.prop=" + binPath;
	// args[3] = "-DSTFILTERBaseName.prop=" + projectName;
	// args[4] = "-buildfile";
	// args[5] = bf_loc;
	// args[6] = "RunSTFILTER";
	//
	// ant.setArgs(args);
	// ant.execute();
	//
	// return "stfilter-executing";
	// }
	// /**
	// * This is similar to executeSTFILTER but it must take place on a host
	// with
	// * gnuplot installed on it. Note this assumes for historical reasons that
	// * stfilter and the plotting tool (gnuplot) are on separate machines.
	// *
	// * This method is currently empty.
	// */
	// public String createDataPlot(String contextDir, String sopacDataFileName,
	// String cfullName) throws Exception {
	//
	// return "gnuplot-plot-created";
	// }
	// /**
	// * Override this method.
	// */
	// public String querySOPAC() throws Exception {
	//
	// String minMaxLatLon = null;
	//
	// if (this.bboxChecked) {
	// minMaxLatLon = this.minLatitude + " " + this.minLongitude + " "
	// + this.maxLatitude + " " + this.maxLongitude;
	// }
	//
	// debug("Do the query");
	// debug("siteCode = " + this.siteCode);
	// debug("beginDate = " + this.beginDate);
	// debug("endDate = " + this.endDate);
	// debug("resource = " + this.resource);
	// debug("contextGroup = " + this.contextGroup);
	// debug("contextId = " + this.contextId);
	// debug("minMaxLatLon = " + minMaxLatLon);
	// GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
	// gsq.setFromServlet(this.siteCode, this.beginDate, this.endDate,
	// this.resource, this.contextGroup, this.contextId, minMaxLatLon);
	// sopacDataFileContent = gsq.getResource();
	//
	// System.out.println("Sopac query action string:" + codeName.toLowerCase()
	// + "-display-query-results");
	// return codeName.toLowerCase() + "-display-query-results";
	//	}
	

	// private String setSTFILTERInputFile(String projectName) {
	// String sopacDataFileContent = "Null Content; please re-enter";
	// String sopacDataFileName = projectName + driverFileExtension;
	// try {
	// String thedir = cm.getCurrentProperty(codeName + "/" + projectName,
	// "Directory");
	// System.out.println(thedir + "/" + sopacDataFileName);
	//
	// BufferedReader buf = new BufferedReader(new FileReader(thedir + "/"
	// + sopacDataFileName));
	// String line = buf.readLine();
	// sopacDataFileContent = line + "\n";
	// while (line != null) {
	// System.out.println(line);
	// line = trimLine(line);
	// sopacDataFileContent += line + "\n";
	// line = buf.readLine();
	// }
	// buf.close();
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// return sopacDataFileContent;
	// }

	// --------------------------------------------------
	// These are accessor methods.
	// --------------------------------------------------
	// public void toggleRenderMPL1(ActionEvent ev) {
	// renderMasterParamList1 = !renderMasterParamList1;
	// // return renderMasterParamList;
	// }
	//
	// public boolean getRenderMasterParamList1() {
	// return renderMasterParamList1;
	// }
	//
	// public void setRenderMasterParamList1(boolean renderMasterParamList1) {
	// this.renderMasterParamList1 = renderMasterParamList1;
	// }
	//
	// public void toggleRenderMPL2(ActionEvent ev) {
	// renderMasterParamList2 = !renderMasterParamList2;
	// // return renderMasterParamList;
	// }
	//
	// public boolean getRenderMasterParamList2() {
	// return renderMasterParamList2;
	// }
	//
	// public void setRenderMasterParamList2(boolean renderMasterParamList2) {
	// this.renderMasterParamList2 = renderMasterParamList2;
	// }

	// public AllStationsContainer getAllsites() {
	// return (AllStationsContainer) allsites;
	// }
	//
	// public MyStationContainer getMyStation() {
	// return (MyStationContainer) myStation;
	// }

	// public Vector getAllsitesVec(){
	// return allsitesVec;
	// }

	// public void setAllsitesVec(Vector asvec) {
	// this.allsitesVec=asvec;
	// }

	// public Vector getMysiteVec(){
	// return mysiteVec;
	// }

	// public void setMysiteVec(Vector mysiteVec){
	// this.mysiteVec=mysiteVec;
	// }

	// public String getDriverFileName() {
	// return driverFileName;
	// }
	//
	// public void setDriverFileName(String driverFileName) {
	// this.driverFileName = driverFileName;
	// }
	//
	// public OutlierCriteria getOutlierCriteria() {
	// return outlierCriteria;
	// }
	//
	// public void setOutlierCriteria(OutlierCriteria outlierCriteria) {
	// this.outlierCriteria = outlierCriteria;
	// }
	//
	// public int getResOption() {
	// return resOption;
	// }
	//
	// public void setResOption(int resOption) {
	// this.resOption = resOption;
	// }
	//
	// public int getTermOption() {
	// return termOption;
	// }
	//
	// public void setTermOption(int termOption) {
	// this.termOption = termOption;
	// }
	//
	// public double getCutoffCriterion() {
	// return cutoffCriterion;
	// }
	//
	// public void setCutoffCriterion(double cutoffCriterion) {
	// this.cutoffCriterion = cutoffCriterion;
	// }
	//
	// public double getEstJumpSpan() {
	// return estJumpSpan;
	// }
	//
	// public void setEstJumpSpan(double estJumpSpan) {
	// this.estJumpSpan = estJumpSpan;
	// }
	//
	// public WeakObsCriteria getWeakObsCriteria() {
	// return weakObsCriteria;
	// }
	//
	// public void setWeakObsCriteria(WeakObsCriteria wobc) {
	// weakObsCriteria = wobc;
	// }
	//
	// public BadObsCriteria getBadObsCriteria() {
	// return badObsCriteria;
	// }
	//
	// public void setBadObsCriteria(BadObsCriteria badObsCriteria) {
	// this.badObsCriteria = badObsCriteria;
	// }
	//
	// public TimeInterval getTimeInterval() {
	// return timeInterval;
	// }
	//
	// public void setTimeInterval(TimeInterval timeInterval) {
	// this.timeInterval = timeInterval;
	// }

	/*
	 * Added by Jong
	 * 
	 */
	private SelectItem[] myStationParamList;

	public SelectItem[] getMyStationParamList() {
		debug("getMyStationParamList", "Size = " + myStation.getMasterParamList().size());
		myStationParamList = new SelectItem[myStation.getMasterParamList()
				.size()];
		for (int i = 0; i < myStation.getMasterParamList().size(); i++) {
			myStationParamList[i] = new SelectItem(new Integer(i),
					((EstimateParameter) myStation.getMasterParamList().get(i))
							.getParameterFullName());
		}
		return myStationParamList;
	}

	private int myStationParamListIndex = 7;

	public int getMyStationParamListIndex() {
		return myStationParamListIndex;
	}

	public void myStationParamListChanged(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			debug("myStationParamListChanged", "ValueChangeEvent = " + event.getNewValue());
			// myStationParamListIndex =
			// ((Integer)event.getNewValue()).intValue();
			myStationParamListIndex = Integer.parseInt((String) event
					.getNewValue());
			curEpisodicParamRendered = false;
			curAnnualAmpParamRendered = false;
			curAnnualPhaseParamRendered = false;
			curSemiannualAmpParamRendered = false;

			switch (myStationParamListIndex) {
			case 7:
				curEpisodicParam = episodicEast;
				break;
			case 8:
				curEpisodicParam = episodicNorth;
				break;
			case 9:
				curEpisodicParam = episodicUp;
				break;
				
			case 16:
				curAnnualAmpParam = annualAmpEast;
				break;
			case 17:
				curAnnualAmpParam = annualAmpNorth;
				break;
			case 18:
				curAnnualAmpParam = annualAmpUp;
				break;
				
			case 19:
				curAnnualPhaseParam = annualPhaseEast;
				break;
			case 20:
				curAnnualPhaseParam = annualPhaseNorth;
				break;
			case 21:
				curAnnualPhaseParam = annualPhaseUp;
				break;
				
			case 22:
				curSemiannualAmpParam = semiannualAmpEast;
				break;
			case 23:
				curSemiannualAmpParam = semiannualAmpNorth;
				break;
			case 24:
				curSemiannualAmpParam = semiannualAmpUp;
				break;
			}

			switch (myStationParamListIndex) {
			case 7:
			case 8:
			case 9:
				curEpisodicParamRendered = true;
				break;
				
			case 16:
			case 17:
			case 18:
				curAnnualAmpParamRendered = true;
				break;
				
			case 19:
			case 20:
			case 21:
				curAnnualPhaseParamRendered = true;
				break;
				
			case 22:
			case 23:
			case 24:
				curSemiannualAmpParamRendered = true;
				break;
			}
		}
		// debug("curEpisodicParam =
		// "+curEpisodicParam.getParameterFullName());
		// debug("curAnnualAmpParam =
		// "+curAnnualAmpParam.getParameterFullName());
		// debug("curAnnualPhaseParam =
		// "+curAnnualPhaseParam.getParameterFullName());
		// debug("curSemiannualAmpParam =
		// "+curSemiannualAmpParam.getParameterFullName());
	}

	public void curEpisodicAprioriValueChanged(ValueChangeEvent event) {
		debug("curEpisodicAprioriValueChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curEpisodicParam.setAprioriValue((Double) event.getNewValue());
			addCurEpisodicParam();
		}
	}

	public void curEpisodicAprioriConstraintChanged(ValueChangeEvent event) {
		debug("curEpisodicAprioriConstraintChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curEpisodicParam.setAprioriConstraint((Double) event.getNewValue());
			addCurEpisodicParam();
		}
	}

	public void curEpisodicStartDateChanged(ValueChangeEvent event) {
		debug("curEpisodicStartDateChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curEpisodicParam.setStartDate((Double) event.getNewValue());
			addCurEpisodicParam();
		}
	}

	public void curEpisodicEndDateChanged(ValueChangeEvent event) {
		debug("curEpisodicEndDateChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curEpisodicParam.setEndDate((Double) event.getNewValue());
			addCurEpisodicParam();
		}
	}

	private void addCurEpisodicParam() {
		if (!episodicParams.contains(curEpisodicParam)) {
			episodicParams.add(curEpisodicParam);
		}
	}

	Vector episodicParams = new Vector();

	EpisodicBias episodicEast = new EpisodicEast();

	EpisodicBias episodicNorth = new EpisodicNorth();

	EpisodicBias episodicUp = new EpisodicUp();

	EpisodicBias curEpisodicParam = episodicEast;

	boolean curEpisodicParamRendered = true;

	public void curAnnualAmpAprioriValueChanged(ValueChangeEvent event) {
		debug("curAnnualAmpAprioriValueChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curAnnualAmpParam.setAprioriValue((Double) event.getNewValue());
			addCurAnnualAmpParam();
		}
	}

	public void curAnnualAmpAprioriConstraintChanged(ValueChangeEvent event) {
		debug("curAnnualAmpAprioriConstraintChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curAnnualAmpParam
					.setAprioriConstraint((Double) event.getNewValue());
			addCurAnnualAmpParam();
		}
	}

	public void curAnnualAmpStartDateChanged(ValueChangeEvent event) {
		debug("curAnnualAmpStartDateChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curAnnualAmpParam.setStartDate((Double) event.getNewValue());
			addCurAnnualAmpParam();
		}
	}

	public void curAnnualAmpPeriodLengthChanged(ValueChangeEvent event) {
		debug("curAnnualAmpPeriodLengthChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curAnnualAmpParam.setPeriodLength((Double) event.getNewValue());
			addCurAnnualAmpParam();
		}
	}

	private void addCurAnnualAmpParam() {
		if (!annualAmpParams.contains(curAnnualAmpParam)) {
			annualAmpParams.add(curAnnualAmpParam);
		}
	}

	Vector annualAmpParams = new Vector();

	AnnualAmpBias annualAmpEast = new AnnualAmpEast();

	AnnualAmpBias annualAmpNorth = new AnnualAmpNorth();

	AnnualAmpBias annualAmpUp = new AnnualAmpUp();

	AnnualAmpBias curAnnualAmpParam = annualAmpEast;

	boolean curAnnualAmpParamRendered = false;

	public void curAnnualPhaseAprioriValueChanged(ValueChangeEvent event) {
		debug("curAnnualPhaseAprioriValueChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curAnnualPhaseParam.setAprioriValue((Double) event.getNewValue());
			addCurAnnualPhaseParam();
		}
	}

	public void curAnnualPhaseAprioriConstraintChanged(ValueChangeEvent event) {
		debug("curAnnualPhaseAprioriConstraintChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curAnnualPhaseParam.setAprioriConstraint((Double) event
					.getNewValue());
			addCurAnnualPhaseParam();
		}
	}

	public void curAnnualPhaseStartDateChanged(ValueChangeEvent event) {
		debug("curAnnualPhaseStartDateChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curAnnualPhaseParam.setStartDate((Double) event.getNewValue());
			addCurAnnualPhaseParam();
		}
	}

	public void curAnnualPhasePeriodLengthChanged(ValueChangeEvent event) {
		debug("curAnnualPhasePeriodLengthChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curAnnualPhaseParam.setPeriodLength((Double) event.getNewValue());
			addCurAnnualPhaseParam();
		}
	}

	private void addCurAnnualPhaseParam() {
		if (!annualPhaseParams.contains(curAnnualPhaseParam)) {
			annualPhaseParams.add(curAnnualPhaseParam);
		}
	}

	Vector annualPhaseParams = new Vector();

	AnnualPhaseBias annualPhaseEast = new AnnualPhaseEast();

	AnnualPhaseBias annualPhaseNorth = new AnnualPhaseNorth();

	AnnualPhaseBias annualPhaseUp = new AnnualPhaseUp();

	AnnualPhaseBias curAnnualPhaseParam = annualPhaseEast;

	boolean curAnnualPhaseParamRendered = false;

	public void curSemiannualAmpAprioriValueChanged(ValueChangeEvent event) {
		debug("curSemiannualAmpAprioriValueChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curSemiannualAmpParam.setAprioriValue((Double) event.getNewValue());
			addCurSemiannualAmpParam();
		}
	}

	public void curSemiannualAmpAprioriConstraintChanged(ValueChangeEvent event) {
		debug("curSemiannualAmpAprioriConstraintChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curSemiannualAmpParam.setAprioriConstraint((Double) event
					.getNewValue());
			addCurSemiannualAmpParam();
		}
	}

	public void curSemiannualAmpStartDateChanged(ValueChangeEvent event) {
		debug("curSemiannualAmpStartDateChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curSemiannualAmpParam.setStartDate((Double) event.getNewValue());
			addCurSemiannualAmpParam();
		}
	}

	public void curSemiannualAmpPeriodLengthChanged(ValueChangeEvent event) {
		debug("curSemiannualAmpPeriodLengthChanged", "ValueChanged = " + event.getNewValue());
		if (event.getNewValue() != null) {
			curSemiannualAmpParam.setPeriodLength((Double) event.getNewValue());
			addCurSemiannualAmpParam();
		}
	}

	private void addCurSemiannualAmpParam() {
		if (!semiannualAmpParams.contains(curSemiannualAmpParam)) {
			semiannualAmpParams.add(curSemiannualAmpParam);
		}
	}

	Vector semiannualAmpParams = new Vector();

	SemiannualAmpBias semiannualAmpEast = new SemiannualAmpEast();

	SemiannualAmpBias semiannualAmpNorth = new SemiannualAmpNorth();

	SemiannualAmpBias semiannualAmpUp = new SemiannualAmpUp();

	SemiannualAmpBias curSemiannualAmpParam = semiannualAmpEast;

	boolean curSemiannualAmpParamRendered = false;

	Vector<EstimateParameter> allParams = new Vector<EstimateParameter>();

	UIData allParamsTable;

	public void removeParamListener(ActionEvent actionEvent) {
		if (allParamsTable.getRowData() instanceof EstimateParameter) {
			EstimateParameter ep = (EstimateParameter) allParamsTable
					.getRowData();
			allParams.remove(ep);
			switch (ep.getParameterType()) {
			case 7:
			case 8:
			case 9:
				episodicParams.remove(ep);
				break;
			case 16:
			case 17:
			case 18:
				annualAmpParams.remove(ep);
				break;
			case 19:
			case 20:
			case 21:
				annualPhaseParams.remove(ep);
				break;
			case 22:
			case 23:
			case 24:
				semiannualAmpParams.remove(ep);
				break;
			}
		}
	}

	public String setEstimatedParams() throws Exception {
		// launchSTFILTERWS();
		callAnalyzeTseriService();
		return "set-estimated-params";
	}

	int maxHistory = 3;

	Vector paramHistory = new Vector();

	String[] graphName = { "Current", "Old 1", "Old 2" };

	public int getParamHistorySize() {
		return paramHistory.size();
	}

	public String clearHistory() throws Exception {
		for (int i = paramHistory.size(); i > 1; i--) {
			paramHistory.remove(i - 1);
		}

		return "";
	}

	/**
	 * Navigation Rule
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createNewAndQuery() throws Exception {
		// setParameterValues();
		createNewProject();
		//querySOPAC();

		StringTokenizer parser = new StringTokenizer(siteCode);
		siteCodeArr = new String[parser.countTokens()];
		sopacDataFileUrlArr = new String[parser.countTokens()];
		
		String tmpSiteCode = siteCode;
		int i = 0;
	    while (parser.hasMoreTokens()) {
	        siteCode = parser.nextToken();
	        siteCodeArr[i] = siteCode;
	        querySOPACGetURL();
	        sopacDataFileUrlArr[i] = sopacDataFileUrl; 
	        debug("createNewAndQuery", "sopacDataFileUrlArr["+i+"] =" + sopacDataFileUrlArr[i]);
	        i++;
	    }
	    siteCode = tmpSiteCode;
	    
		//debug("createNewAndQuery", "getSopacDataFileUrl = "+super.getSopacDataFileUrl());
		return setEstimatedParams();
	}

	public int getMaxHistory() {
		return maxHistory;
	}

	public void setMaxHistory(int maxHistory) {
		this.maxHistory = maxHistory;
	}

	public Vector getParamHistory() {
		return paramHistory;
	}

	public void setParamHistory(Vector paramHistory) {
		this.paramHistory = paramHistory;
	}

	public String[] getGraphName() {
		return graphName;
	}

	public void setGraphName(String[] graphName) {
		this.graphName = graphName;
	}

	protected String _siteCode = this.siteCode;

	protected String _beginDate = this.beginDate;

	protected String _endDate = this.endDate;

	protected boolean _bboxChecked = this.bboxChecked;

	protected double _minLatitude = this.minLatitude;

	protected double _maxLatitude = this.maxLatitude;

	protected double _minLongitude = this.minLongitude;

	protected double _maxLongitude = this.maxLongitude;

	protected String _resource = "procCoords";

	protected String _contextGroup = "reasonComb";

	protected String _contextId = this.contextId;

	protected int _resOption = this.projectBean.resOption;

	protected int _termOption = this.projectBean.termOption;

	protected double _cutoffCriterion = this.projectBean.cutoffCriterion;

	protected double _estJumpSpan = this.projectBean.estJumpSpan;

	protected WeakObsCriteria _weakObsCriteria = this.projectBean.weakObsCriteria;

	protected OutlierCriteria _outlierCriteria = this.projectBean.outlierCriteria;

	protected BadObsCriteria _badObsCriteria = this.projectBean.badObsCriteria;

	protected TimeInterval _timeInterval = this.projectBean.timeInterval;

	public String savePref() {

		savePrefs();
		updateWithPrefs();

		// WindowState windowState = pRequest.getWindowState();

		return "S_OK";
	}

	private void savePrefs() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext.getExternalContext().getRequest() instanceof PortletRequest) {
			debug("savePrefs", "Request is PortletRequest. Pref. will save.");
			PortletRequest pRequest = (PortletRequest) facesContext
					.getExternalContext().getRequest();
			PortletPreferences prefs = pRequest.getPreferences();

			try {
				prefs.setValue("_siteCode", _siteCode);
				prefs.setValue("_beginDate", _beginDate);
				prefs.setValue("_endDate", _endDate);
				prefs.setValue("_bboxChecked", Boolean.toString(_bboxChecked));
				prefs.setValue("_minLatitude", Double.toString(_minLatitude));
				prefs.setValue("_maxLatitude", Double.toString(_maxLatitude));
				prefs.setValue("_minLongitude", Double.toString(_minLongitude));
				prefs.setValue("_maxLongitude", Double.toString(_maxLongitude));
				prefs.setValue("_resource", _resource);
				prefs.setValue("_contextGroup", _contextGroup);
				prefs.setValue("_contextId", _contextId);
				prefs.setValue("_resOption", Integer.toString(_resOption));
				prefs.setValue("_termOption", Integer.toString(_termOption));
				prefs.setValue("_cutoffCriterion", Double
						.toString(_cutoffCriterion));
				prefs.setValue("_estJumpSpan", Double.toString(_estJumpSpan));

				prefs.setValue("_weakObsCriteria.north", Double
						.toString(_weakObsCriteria.north));
				prefs.setValue("_weakObsCriteria.east", Double
						.toString(_weakObsCriteria.east));
				prefs.setValue("_weakObsCriteria.up", Double
						.toString(_weakObsCriteria.up));

				prefs.setValue("_outlierCriteria.north", Double
						.toString(_outlierCriteria.north));
				prefs.setValue("_outlierCriteria.east", Double
						.toString(_outlierCriteria.east));
				prefs.setValue("_outlierCriteria.up", Double
						.toString(_outlierCriteria.up));

				prefs.setValue("_badObsCriteria.north", Double
						.toString(_badObsCriteria.north));
				prefs.setValue("_badObsCriteria.east", Double
						.toString(_badObsCriteria.east));
				prefs.setValue("_badObsCriteria.up", Double
						.toString(_badObsCriteria.up));

				prefs.setValue("_timeInterval.beginTime", Double
						.toString(_timeInterval.beginTime));
				prefs.setValue("_timeInterval.endTime", Double
						.toString(_timeInterval.endTime));

				prefs.store();
				debug("savePrefs", "Pref. saved.");
			} catch (ReadOnlyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ValidatorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			debug("savePrefs", "request is NOT PortletRequest. Pref. will NOT save.");
		}
	}

	protected void updateWithPrefs() {

		this.siteCode = _siteCode;
		this.beginDate = _beginDate;
		this.endDate = _endDate;
		this.bboxChecked = _bboxChecked;
		this.minLatitude = _minLatitude;
		this.maxLatitude = _maxLatitude;
		this.minLongitude = _minLongitude;
		this.maxLongitude = _maxLongitude;
		this.resource = _resource;
		this.contextGroup = _contextGroup;
		this.contextId = _contextId;

		// AnalyzeTseri Params
		projectBean.resOption = _resOption;
		projectBean.termOption = _termOption;
		projectBean.cutoffCriterion = _cutoffCriterion;
		projectBean.estJumpSpan = _estJumpSpan;

		projectBean.weakObsCriteria.north = _weakObsCriteria.north;
		projectBean.weakObsCriteria.east = _weakObsCriteria.east;
		projectBean.weakObsCriteria.up = _weakObsCriteria.up;

		projectBean.outlierCriteria.north = _outlierCriteria.north;
		projectBean.outlierCriteria.east = _outlierCriteria.east;
		projectBean.outlierCriteria.up = _outlierCriteria.up;

		projectBean.badObsCriteria.north = _badObsCriteria.north;
		projectBean.badObsCriteria.east = _badObsCriteria.east;
		projectBean.badObsCriteria.up = _badObsCriteria.up;

		projectBean.timeInterval.beginTime = _timeInterval.beginTime;
		projectBean.timeInterval.endTime = _timeInterval.endTime;
	}

	public BadObsCriteria get_badObsCriteria() {
		return _badObsCriteria;
	}

	public void set_badObsCriteria(BadObsCriteria obsCriteria) {
		_badObsCriteria = obsCriteria;
	}

	public boolean is_bboxChecked() {
		return _bboxChecked;
	}

	public void set_bboxChecked(boolean checked) {
		_bboxChecked = checked;
	}

	public String get_beginDate() {
		return _beginDate;
	}

	public void set_beginDate(String date) {
		_beginDate = date;
	}

	public String get_contextGroup() {
		return _contextGroup;
	}

	public void set_contextGroup(String group) {
		_contextGroup = group;
	}

	public String get_contextId() {
		return _contextId;
	}

	public void set_contextId(String id) {
		_contextId = id;
	}

	public double get_cutoffCriterion() {
		return _cutoffCriterion;
	}

	public void set_cutoffCriterion(double criterion) {
		_cutoffCriterion = criterion;
	}

	public String get_endDate() {
		return _endDate;
	}

	public void set_endDate(String date) {
		_endDate = date;
	}

	public double get_estJumpSpan() {
		return _estJumpSpan;
	}

	public void set_estJumpSpan(double jumpSpan) {
		_estJumpSpan = jumpSpan;
	}

	public double get_maxLatitude() {
		return _maxLatitude;
	}

	public void set_maxLatitude(double latitude) {
		_maxLatitude = latitude;
	}

	public double get_maxLongitude() {
		return _maxLongitude;
	}

	public void set_maxLongitude(double longitude) {
		_maxLongitude = longitude;
	}

	public double get_minLatitude() {
		return _minLatitude;
	}

	public void set_minLatitude(double latitude) {
		_minLatitude = latitude;
	}

	public double get_minLongitude() {
		return _minLongitude;
	}

	public void set_minLongitude(double longitude) {
		_minLongitude = longitude;
	}

	public OutlierCriteria get_outlierCriteria() {
		return _outlierCriteria;
	}

	public void set_outlierCriteria(OutlierCriteria criteria) {
		_outlierCriteria = criteria;
	}

	public int get_resOption() {
		return _resOption;
	}

	public void set_resOption(int option) {
		_resOption = option;
	}

	public String get_resource() {
		return _resource;
	}

	public void set_resource(String _resource) {
		this._resource = _resource;
	}

	public String get_siteCode() {
		return _siteCode;
	}

	public void set_siteCode(String code) {
		_siteCode = code;
	}

	public int get_termOption() {
		return _termOption;
	}

	public void set_termOption(int option) {
		_termOption = option;
	}

	public TimeInterval get_timeInterval() {
		return _timeInterval;
	}

	public void set_timeInterval(TimeInterval interval) {
		_timeInterval = interval;
	}

	public WeakObsCriteria get_weakObsCriteria() {
		return _weakObsCriteria;
	}

	public void set_weakObsCriteria(WeakObsCriteria obsCriteria) {
		_weakObsCriteria = obsCriteria;
	}

	public AnnualAmpBias getCurAnnualAmpParam() {
		return curAnnualAmpParam;
	}

	public void setCurAnnualAmpParam(AnnualAmpBias curAnnualAmpParam) {
		this.curAnnualAmpParam = curAnnualAmpParam;
	}

	public boolean isCurAnnualAmpParamRendered() {
		return curAnnualAmpParamRendered;
	}

	public void setCurAnnualAmpParamRendered(boolean curAnnualAmpParamRendered) {
		this.curAnnualAmpParamRendered = curAnnualAmpParamRendered;
	}

	public AnnualPhaseBias getCurAnnualPhaseParam() {
		return curAnnualPhaseParam;
	}

	public void setCurAnnualPhaseParam(AnnualPhaseBias curAnnualPhaseParam) {
		this.curAnnualPhaseParam = curAnnualPhaseParam;
	}

	public boolean isCurAnnualPhaseParamRendered() {
		return curAnnualPhaseParamRendered;
	}

	public void setCurAnnualPhaseParamRendered(
			boolean curAnnualPhaseParamRendered) {
		this.curAnnualPhaseParamRendered = curAnnualPhaseParamRendered;
	}

	public EpisodicBias getCurEpisodicParam() {
		return curEpisodicParam;
	}

	public void setCurEpisodicParam(EpisodicBias curEpisodicParam) {
		this.curEpisodicParam = curEpisodicParam;
	}

	public boolean isCurEpisodicParamRendered() {
		return curEpisodicParamRendered;
	}

	public void setCurEpisodicParamRendered(boolean curEpisodicParamRendered) {
		this.curEpisodicParamRendered = curEpisodicParamRendered;
	}

	public SemiannualAmpBias getCurSemiannualAmpParam() {
		return curSemiannualAmpParam;
	}

	public void setCurSemiannualAmpParam(SemiannualAmpBias curSemiannualAmpParam) {
		this.curSemiannualAmpParam = curSemiannualAmpParam;
	}

	public boolean isCurSemiannualAmpParamRendered() {
		return curSemiannualAmpParamRendered;
	}

	public void setCurSemiannualAmpParamRendered(boolean curSemiannualAmpParamRendered) {
		this.curSemiannualAmpParamRendered = curSemiannualAmpParamRendered;
	}

	public Vector getAnnualAmpParams() {
		return annualAmpParams;
	}

	public void setAnnualAmpParams(Vector annualAmpParams) {
		this.annualAmpParams = annualAmpParams;
	}

	public Vector getAnnualPhaseParams() {
		return annualPhaseParams;
	}

	public void setAnnualPhaseParams(Vector annualPhaseParams) {
		this.annualPhaseParams = annualPhaseParams;
	}

	public Vector getEpisodicParams() {
		return episodicParams;
	}

	public void setEpisodicParams(Vector episodicParams) {
		this.episodicParams = episodicParams;
	}

	public Vector getSemiannualAmpParams() {
		return semiannualAmpParams;
	}

	public void setSemiannualAmpParams(Vector semiannualAmpParams) {
		this.semiannualAmpParams = semiannualAmpParams;
	}

	public Vector getAllParams() {
		return allParams;
	}

	public void setAllParams(Vector<EstimateParameter> allParams) {
		this.allParams = allParams;
	}

	public UIData getAllParamsTable() {
		return allParamsTable;
	}

	public void setAllParamsTable(UIData allParamsTable) {
		this.allParamsTable = allParamsTable;
	}

	public AnalyzeTseriResultsBean getResultsBean() {
		return resultsBean;
	}

	public void setResultsBean(AnalyzeTseriResultsBean resultsBean) {
		this.resultsBean = resultsBean;
	}

	public STFILTERProjectBean getProjectBean() {
		return projectBean;
	}

	public void setProjectBean(STFILTERProjectBean projectBean) {
		this.projectBean = projectBean;
	}

	public StationContainer getAllsites() {
		return allsites;
	}

	public void setAllsites(StationContainer allsites) {
		this.allsites = allsites;
	}

	public StationParamList getAllsitesList() {
		return allsitesList;
	}

	public void setAllsitesList(StationParamList allsitesList) {
		this.allsitesList = allsitesList;
	}

	public AnnualAmpBias getAnnualAmpEast() {
		return annualAmpEast;
	}

	public void setAnnualAmpEast(AnnualAmpBias annualAmpEast) {
		this.annualAmpEast = annualAmpEast;
	}

	public AnnualAmpBias getAnnualAmpNorth() {
		return annualAmpNorth;
	}

	public void setAnnualAmpNorth(AnnualAmpBias annualAmpNorth) {
		this.annualAmpNorth = annualAmpNorth;
	}

	public AnnualAmpBias getAnnualAmpUp() {
		return annualAmpUp;
	}

	public void setAnnualAmpUp(AnnualAmpBias annualAmpUp) {
		this.annualAmpUp = annualAmpUp;
	}

	public AnnualPhaseBias getAnnualPhaseEast() {
		return annualPhaseEast;
	}

	public void setAnnualPhaseEast(AnnualPhaseBias annualPhaseEast) {
		this.annualPhaseEast = annualPhaseEast;
	}

	public AnnualPhaseBias getAnnualPhaseNorth() {
		return annualPhaseNorth;
	}

	public void setAnnualPhaseNorth(AnnualPhaseBias annualPhaseNorth) {
		this.annualPhaseNorth = annualPhaseNorth;
	}

	public AnnualPhaseBias getAnnualPhaseUp() {
		return annualPhaseUp;
	}

	public void setAnnualPhaseUp(AnnualPhaseBias annualPhaseUp) {
		this.annualPhaseUp = annualPhaseUp;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public ObjectContainer getDb() {
		return db;
	}

	public void setDb(ObjectContainer db) {
		this.db = db;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public EpisodicBias getEpisodicEast() {
		return episodicEast;
	}

	public void setEpisodicEast(EpisodicBias episodicEast) {
		this.episodicEast = episodicEast;
	}

	public EpisodicBias getEpisodicNorth() {
		return episodicNorth;
	}

	public void setEpisodicNorth(EpisodicBias episodicNorth) {
		this.episodicNorth = episodicNorth;
	}

	public EpisodicBias getEpisodicUp() {
		return episodicUp;
	}

	public void setEpisodicUp(EpisodicBias episodicUp) {
		this.episodicUp = episodicUp;
	}

	public MasterParamList getMasterList() {
		return masterList;
	}

	public void setMasterList(MasterParamList masterList) {
		this.masterList = masterList;
	}

	public StationContainer getMyStation() {
		return myStation;
	}

	public void setMyStation(StationContainer myStation) {
		this.myStation = myStation;
	}

	public StationParamList getMyStationList() {
		return myStationList;
	}

	public void setMyStationList(StationParamList myStationList) {
		this.myStationList = myStationList;
	}

	public boolean isProjectCreated() {
		return projectCreated;
	}

	public void setProjectCreated(boolean projectCreated) {
		this.projectCreated = projectCreated;
	}

	public boolean isRenderAllSites() {
		return renderAllSites;
	}

	public void setRenderAllSites(boolean renderAllSites) {
		this.renderAllSites = renderAllSites;
	}

	public boolean isRenderMasterParamList1() {
		return renderMasterParamList1;
	}

	public void setRenderMasterParamList1(boolean renderMasterParamList1) {
		this.renderMasterParamList1 = renderMasterParamList1;
	}

	public boolean isRenderMasterParamList2() {
		return renderMasterParamList2;
	}

	public void setRenderMasterParamList2(boolean renderMasterParamList2) {
		this.renderMasterParamList2 = renderMasterParamList2;
	}

	public boolean isRenderMySite() {
		return renderMySite;
	}

	public void setRenderMySite(boolean renderMySite) {
		this.renderMySite = renderMySite;
	}

	public SemiannualAmpBias getSemiannualAmpEast() {
		return semiannualAmpEast;
	}

	public void setSemiannualAmpEast(SemiannualAmpBias semiannualAmpEast) {
		this.semiannualAmpEast = semiannualAmpEast;
	}

	public SemiannualAmpBias getSemiannualAmpNorth() {
		return semiannualAmpNorth;
	}

	public void setSemiannualAmpNorth(SemiannualAmpBias semiannualAmpNorth) {
		this.semiannualAmpNorth = semiannualAmpNorth;
	}

	public SemiannualAmpBias getSemiannualAmpUp() {
		return semiannualAmpUp;
	}

	public void setSemiannualAmpUp(SemiannualAmpBias semiannualAmpUp) {
		this.semiannualAmpUp = semiannualAmpUp;
	}

	public String getSopacDataFileContent() {
		return sopacDataFileContent;
	}

	public void setSopacDataFileContent(String sopacDataFileContent) {
		this.sopacDataFileContent = sopacDataFileContent;
	}

	public String getStamp() {
		return stamp;
	}

	public void setStamp(String stamp) {
		this.stamp = stamp;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWorkDir() {
		return workDir;
	}

	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}

	public void setMyStationParamList(SelectItem[] myStationParamList) {
		this.myStationParamList = myStationParamList;
	}

	public void setMyStationParamListIndex(int myStationParamListIndex) {
		this.myStationParamListIndex = myStationParamListIndex;
	}

	public String getContextUrl() {
		return contextUrl;
	}

	public void setContextUrl(String contextUrl) {
		this.contextUrl = contextUrl;
	}

    public void setProjectArchive(List projectArchive) {
        this.projectArchive=projectArchive;
   }

    public List getProjectArchive() {
		return projectArchive;
	}
    
	public static void debug(String func, String msg) {
		System.out.println("[DEBUG][STFILTERBean:"+func+"] " + msg);
	}

	public static void debug(String func, String name, String value) {
		System.out.println("[DEBUG][STFILTERBean:"+func+"] " + name + " = " + value);
	}
	
	public String[] getSiteCodeArr() {
		return siteCodeArr;
	}

	public void setSiteCodeArr(String[] siteCodeArr) {
		this.siteCodeArr = siteCodeArr;
	}

	public String[] getSopacDataFileUrlArr() {
		return sopacDataFileUrlArr;
	}

	public void setSopacDataFileUrlArr(String[] sopacDataFileUrlArr) {
		this.sopacDataFileUrlArr = sopacDataFileUrlArr;
	}
    
}
