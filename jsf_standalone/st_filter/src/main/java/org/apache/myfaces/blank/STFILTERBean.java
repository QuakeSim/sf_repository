package org.apache.myfaces.blank;

//Imports from the mother ship
import org.apache.axis.MessageContext;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.transport.http.HTTPConstants;
import org.servogrid.genericproject.GenericSopacBean;
import org.servogrid.genericproject.GenericProjectBean;
import org.servogrid.genericproject.Utility;
import org.servogrid.genericproject.ProjectBean;

//Faces classes
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

//Servlet and portlet API stuff.
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.xml.namespace.QName;
import javax.portlet.PortletContext;

//QuakeSim Web Service clients
import WebFlowClient.cm.*;
import WebFlowClient.fsws.*;
import cgl.webclients.*;

//SOPAC Client Stuff
import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

//Usual java stuff.
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;

import java.text.DateFormat;
import java.util.*;


/**
 * Everything you need to set up and run STFILTER.
 */

public class STFILTERBean extends GenericSopacBean {

	final String FILE_PROTOCOL = "file";

	final String HTTP_PROTOCOL = "http";


	// Some internal fields.
	String twospace = "  "; // Used to format the driver file.

	boolean projectCreated = false;

	// STFILTER properties
	private String codeName = "STFILTER";

	private int resOption = 387;

	private int termOption = 556;

	private double cutoffCriterion = 1.0;

	private double estJumpSpan = 1.0;

	private WeakObsCriteria weakObsCriteria = new WeakObsCriteria(30.0, 30.0,
			50.0);

	private OutlierCriteria outlierCriteria = new OutlierCriteria(800.0, 800.0,
			800.0);

	private BadObsCriteria badObsCriteria = new BadObsCriteria(10000.0,
			10000.0, 10000.0);

	private TimeInterval timeInterval = new TimeInterval(1998.0, 2006.800);

	// This is the file that will hold the
	// results of the GPS station query.
	private String sopacDataFileName = "";

	private String sopacDataFileContent = "";

	private String sopacDataFileExt = ".data";

	// This is the working diretory for running the
	// code on the execution host. The global data
	// directory is the location of things like the
	// apriori file.
	private String workDir = "";

	private String globalDataDir = "";

	// This is the driver file and its constituent lines.
	private String driverFileName = "";

	private String driverFileContent = "";

	private String driverFileExtension = ".drv";

	// These are fixed files, at least for now.
	private String aprioriValueFile = "itrf2000_final.net";

	private String mosesParamFile = "moses_test.para";

	// These are file extensions. The files will be named after the
	// project.
	private String mosesDataListExt = ".list";

	private String mosesSiteListExt = ".site";

	private String mosesParamFileExt = ".para";

	private String residualFileExt = ".resi";

	private String termOutFileExt = ".mdl";

	private String outputFileExt = ".out";

	// This is the site list file
	private String siteListFile;

	private String dataListFile;

	private String estParameterFile;

	// Project properties
	private String[] contextList;

	private Hashtable contextListHash;

	private Vector contextListVector;

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
	String resiURL = null;

	/**
	 * default empty constructor
	 */
	public STFILTERBean() {
		super();
		cm = getContextManagerImp();
		setSiteCode("LBC1"); // Use this for testing.

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
		
		
		// Init. By Jong
		setResource("procCoords");
		setContextGroup("reasonComb");
	}

	/**
	 * Method that is backed to a submit button of a form.
	 */
	public String newProject() throws Exception {
		if (!isInitialized) {
			initWebServices();
		}
		return ("new-project-created");
	}

	public String paramsThenTextArea() throws Exception {
		setParameterValues();
		return "parameters-to-textfield";
	}

	public String paramsThenDB() throws Exception {
		// setParameterValues();
		createNewProject();
		return "parameters-to-database";
	}

	public String paramsThenMap() throws Exception {
		// setParameterValues();
		createNewProject();
		return "parameters-to-googlemap";
	}

	public String createNewProject() throws Exception {
		System.out.println("Creating new project");
		System.out.println("Project name is " + projectName);

		// Store the request values persistently
		contextName = codeName + "/" + projectName;
		String hostName = getHostName();
		cm.addContext(contextName);
		cm.setCurrentProperty(contextName, "projectName", projectName);
		cm.setCurrentProperty(contextName, "hostName", hostName);
		projectCreated = true;

		// This is the working directory of the execution host.
		workDir = getBaseWorkDir() + File.separator + userName + File.separator
				+ projectName;

		globalDataDir = getBinPath();

		return "new-project-created";
	}

	public String setParameterValues() throws Exception {
		// This should always be true at this point, but check for
		// safety.
		if (projectCreated != true) {
			createNewProject();
		}

		// Now set the rest of the parameters.
		// The cm object is inherited.
		cm.setCurrentProperty(contextName, "resOption", resOption + "");
		cm.setCurrentProperty(contextName, "termOption", termOption + "");
		cm.setCurrentProperty(contextName, "cutoffCriterion", cutoffCriterion
				+ "");
		cm.setCurrentProperty(contextName, "estJumpSpan", estJumpSpan + "");
		// cm.setCurrentProperty(contextName,"weakObsCriteria",
		// weakObsCriteria);
		// cm.setCurrentProperty(contextName,"outlierCriteria",outlierCriteria);
		// cm.setCurrentProperty(contextName,"badObsCriteria",badObsCriteria);
		// cm.setCurrentProperty(contextName,"timeInterval",timeInterval);
		return "parameters-set";
	}

	public String loadDataArchive() throws Exception {
		System.out.println("Loading project");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		return ("load-data-archive");
	}

	public String loadProject() throws Exception {
		System.out.println("Loading project");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		return ("list-old-projects");
	}

	public String loadProjectKillList() throws Exception {
		System.out.println("Loading project");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		return ("list-death-row");
	}

	public String loadProjectPlots() throws Exception {
		System.out.println("Loading project");
		if (!isInitialized) {
			initWebServices();
		}
		setContextList();
		return ("list-project-plots");
	}

	public String launchSTFILTER() throws Exception {
		// Do this here.
		setParameterValues();

		String sopacDataFileName = getSiteCode() + sopacDataFileExt;
		String cfullName = codeName + "/" + projectName;
		String contextDir = cm.getCurrentProperty(cfullName, "Directory");

		createDriverFile(contextDir);
		createSopacDataFile(contextDir, sopacDataFileName, sopacDataFileContent);
		createSiteListFile(contextDir);
		createDataListFile(contextDir);
		createEstimatedParamFile(contextDir);
		String value = executeSTFILTER(contextDir, sopacDataFileName, cfullName);
		return "stfilter-launched";
	}
	
	public String launchSTFILTERWS() throws Exception {
		// Do this here.
		try {
		
			//String endpoint = "http://gf3.ucs.indiana.edu:8888/analyze-tseri-exec/services/AnalyzeTseriExec";
			//String contextDir = "/home/jychoi/apps/QuakeSim2/portal_deploy/apache-tomcat-5.5.20/webapps/STFILTER/WDIR/"; 
			//String dataUrl = "http://gf3.ucs.indiana.edu:8888/STFILTER/WDIR/"+sopacDataFileName;
			FacesContext fc = FacesContext.getCurrentInstance();
			String endpoint = fc.getExternalContext().getInitParameter("analyze_tseri.service.url");
			System.out.println("[!!] endpoint = "+endpoint);
			String workDir = fc.getExternalContext().getInitParameter("work.dir");
			System.out.println("[!!] workDir = "+workDir);
			String workUrl = fc.getExternalContext().getInitParameter("work.url");
			System.out.println("[!!] workUrl = "+workUrl);
			
			if ((endpoint == null) || (endpoint.equals(""))) {
				System.out.println("[!!] Set init-param in web.xml");
				return "";
			}
			
			String siteCode = getSiteCode();
			String sopacDataFileName  = projectName+"-"+getSiteCode()+"-"+stamp + sopacDataFileExt;
			System.out.println("[!!] sopacDataFileName = "+sopacDataFileName);
			String cfullName = codeName + "/" + projectName;
			
			String dataUrl = workUrl+sopacDataFileName;
			System.out.println("[!!] dataUrl = "+workUrl);
			
			createSopacDataFile(workDir, sopacDataFileName, sopacDataFileContent);
			System.out.println("[!!] dataUrl = "+dataUrl);
			
			double[][] globalParam = new double[allsites.estParamVector.size()][5];
			double[][] siteParam = new double[myStation.estParamVector.size()+EpisodicBiasParam.size()][5];
			
		    setParam(allsites, globalParam);
		    setParam(myStation, siteParam);
		    
		    for (int i = myStation.estParamVector.size(); i < myStation.estParamVector.size() + EpisodicBiasParam.size(); i++) {
				System.out.println("[!!]"+EpisodicBiasParam.get(i-myStation.estParamVector.size()).toString());
				System.out.println("[!!]"+EpisodicBiasParam.size());
				EpisodicBias eb = (EpisodicBias) EpisodicBiasParam.get(i-myStation.estParamVector.size());
				globalParam[i][0] = eb.parameterType;
				globalParam[i][1] = eb.aprioriConstraint.doubleValue();
				globalParam[i][2] = eb.aprioriValue.doubleValue();
				globalParam[i][3] = eb.startDate.doubleValue();
				globalParam[i][4] = eb.endDate.doubleValue();
		    }

			Service service = new Service();
			Call call = (Call) service.createCall();

			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName(new QName("http://soapinterop.org/",
					"execATS"));
			
			String[] ret = (String[]) call.invoke(new Object[] {siteCode, dataUrl, globalParam, siteParam});
			
			System.out.println("Output: ");
			for (int i = 0; i < ret.length; i++) {
				System.out.println(ret[i]);
			}

			// Draw graphs
			resiURL = ret[3];
			
			List filteredList = createFilteredList();
			if (paramHistory.size() < maxHistory) {
				paramHistory.add(0, filteredList);
			} else {
				paramHistory.add(0, filteredList);
				for (int i = paramHistory.size(); i > maxHistory ; i--) {
					paramHistory.remove(i-1);
				}
			}
			
			System.out.println("paramHistory.size() = "+paramHistory.size());

			//System.out.println("Output: " + ret);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		return "stfilterws-launched";
	}

	private String extractSimpleName(String extendedName) {
		return (new File(extendedName)).getName();
	}

	/**
	 * Famous method that I googled. This copies a file to a new place on the
	 * file system.
	 */
	private void copyFileToFile(File sourceFile, File destFile)
			throws Exception {
		InputStream in = new FileInputStream(sourceFile);
		OutputStream out = new FileOutputStream(destFile);
		byte[] buf = new byte[1024];
		int length;
		while ((length = in.read(buf)) > 0) {
			out.write(buf, 0, length);
		}
		in.close();
		out.close();
	}

	/**
	 * Another famous method that I googled. This downloads contents from the
	 * given URL to a local file.
	 */
	private void copyUrlToFile(URL inputFileUrl, String destFile)
			throws Exception {

		URLConnection uconn = inputFileUrl.openConnection();
		InputStream in = inputFileUrl.openStream();
		OutputStream out = new FileOutputStream(destFile);

		// Extract the name of the file from the url.

		byte[] buf = new byte[1024];
		int length;
		while ((length = in.read(buf)) > 0) {
			out.write(buf, 0, length);
		}
		in.close();
		out.close();

	}

	private String downloadInputFile(String inputFileUrlString,
			String inputFileDestDir) throws Exception {

		// Convert to a URL. This will throw an exception if
		// malformed.
		URL inputFileUrl = new URL(inputFileUrlString);

		String protocol = inputFileUrl.getProtocol();
		System.out.println("Protocol: " + protocol);
		String fileSimpleName = extractSimpleName(inputFileUrl.getFile());
		System.out.println(fileSimpleName);

		String fileLocalFullName = inputFileDestDir + File.separator
				+ fileSimpleName;

		if (protocol.equals(FILE_PROTOCOL)) {
			String filePath = inputFileUrl.getFile();
			fileSimpleName = inputFileUrl.getFile();

			System.out.println("File path is " + filePath);
			File filePathObject = new File(filePath);
			File destFileObject = new File(fileLocalFullName);

			// See if the inputFileUrl and the dest file are the same.
			if (filePathObject.getCanonicalPath().equals(
					destFileObject.getCanonicalPath())) {
				System.out.println("Files are the same.  We're done.");
				return fileLocalFullName;
			}

			// Otherwise, we will have to copy it.
			copyFileToFile(filePathObject, destFileObject);
			return fileLocalFullName;
		}

		else if (protocol.equals(HTTP_PROTOCOL)) {
			copyUrlToFile(inputFileUrl, fileLocalFullName);
		}

		else {
			System.out.println("Unknown protocol for accessing inputfile");
			throw new Exception("Unknown protocol");
		}
		return fileLocalFullName;
	}

//	*   residuals for site  LBC1_GPS  with option   1  0  0  0  0  0  0  0  0  0  0  0  0  0  0
//	*   time       E        N        Se       Sn      Ren      U        Su      Reu     Rnu     site      long     lati
//	 2004.5779   -13.28   -56.52     4.41     4.63  0.0000  -100.55     5.00  0.0000  0.0000  LBC1_GPS  241.8628  33.8321
//	 2004.5806   -11.88   -60.75     4.38     4.54  0.0000   -75.21     4.91  0.0000  0.0000  LBC1_GPS  241.8628  33.8321
//	 2004.5833   -10.35   -65.99     4.83     4.99  0.0000  -105.65     5.43  0.0000  0.0000  LBC1_GPS  241.8628  33.8321

	public List createFilteredList() {
		URL url;
		ArrayList list = new ArrayList();
		ArrayList row = null;

		try {

			url = new URL(resiURL);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			String line;
			
			line = in.readLine();
			line = in.readLine();
			
			Calendar thisYear = Calendar.getInstance();
			Calendar nextYear= Calendar.getInstance();
			Calendar cur = Calendar.getInstance();
			
			double time;
			int idx = 1;
			StringTokenizer st;
			while ((line = in.readLine()) != null) {
				st = new StringTokenizer (line);
				row = new ArrayList();
				row.add(String.valueOf(idx)); // idx
				
				time = Double.parseDouble(st.nextToken());
				thisYear.set((int)Math.floor(time), 0, 1, 0, 0, 0);
				nextYear.set((int)Math.ceil(time), 0, 1, 0, 0, 0);
				cur = Calendar.getInstance();
				cur.setTimeInMillis(thisYear.getTimeInMillis() + (long)((time-Math.floor(time))*(nextYear.getTimeInMillis() - thisYear.getTimeInMillis())));
				//System.out.println("[!!] ["+idx+"] time = "+time+", cur ="+DateFormat.getDateInstance().format(cur.getTime()));
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
		//BufferedReader rd = new BufferedReader( new StringReader (sopacDataFileContent));
	}

	public List getSopacDataList() {
		ArrayList list = new ArrayList();
		ArrayList row = null;
		StringTokenizer st = new StringTokenizer (sopacDataFileContent);
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
		//BufferedReader rd = new BufferedReader( new StringReader (sopacDataFileContent));
	}

	private void setParam(StationContainer station, double[][] globalParam) {
		for (int i = 0; i < station.estParamVector.size(); i++) {
			EstimateParameter ep = (EstimateParameter) station.estParamVector.get(i);
			globalParam[i][0] = ep.parameterType;
			switch (ep.parameterType) {
			case 1:
			case 2:
			case 3:
				globalParam[i][1] = ((ConstantBias) ep).aprioriConstraint.doubleValue();
				globalParam[i][2] = ((ConstantBias) ep).aprioriValue.doubleValue();
				globalParam[i][3] = ((ConstantBias) ep).startDate.doubleValue();
				break;
			case 4:
			case 5:
			case 6:
				globalParam[i][1] = ((VelocityBias) ep).aprioriConstraint.doubleValue();
				globalParam[i][2] = ((VelocityBias) ep).aprioriValue.doubleValue();
				globalParam[i][3] = ((VelocityBias) ep).startDate.doubleValue();
				globalParam[i][4] = ((VelocityBias) ep).endDate.doubleValue();
				break;
			case 7:
			case 8:
			case 9:
				globalParam[i][1] = ((EpisodicBias) ep).aprioriConstraint.doubleValue();
				globalParam[i][2] = ((EpisodicBias) ep).aprioriValue.doubleValue();
				globalParam[i][3] = ((EpisodicBias) ep).startDate.doubleValue();
				globalParam[i][4] = ((EpisodicBias) ep).endDate.doubleValue();
				break;
			}
		}
	}

	public String populateAndPlot() throws Exception {
		populateProject();
		launchPlot();
		return "plot-created";
	}

	/**
	 * Currently empty.
	 */
	public String launchPlot() throws Exception {

		return "does nothing";
	}

	/**
	 * Create the site list file. Currently we only support one site and the XYZ
	 * format (ie "1 8").
	 */
	public void createSiteListFile(String contextDir) throws Exception {

		String slash = "/"; // This is not File.separator of the webserver
		siteListFile = projectName + mosesSiteListExt;
		System.out.println("Writing input file: " + contextDir + "/"
				+ siteListFile);
		PrintWriter pw = new PrintWriter(new FileWriter(contextDir + "/"
				+ siteListFile), true);

		pw.println("  1"); // Need to make this more general.
		pw.println(getSiteCode().toUpperCase() + "_GPS");
		pw.close();
	}

	public void createEstimatedParamFile(String contextDir) throws Exception {
		estParameterFile = projectName + mosesParamFileExt;
		PrintWriter pw = new PrintWriter(new FileWriter(contextDir + "/"
				+ estParameterFile), true);
		if (myStation.printContents() != null) {
			pw.println("  2");
			pw.println(allsites.printContents());
			pw.println(myStation.printContents());
		} else {
			pw.println("  1");
			pw.println(allsites.printContents());
		}
		pw.close();
	}

	public void createDataListFile(String contextDir) throws Exception {

		String slash = "/"; // This is not File.separator of the webserver
		dataListFile = projectName + mosesDataListExt;
		System.out.println("Writing input file: " + contextDir + "/"
				+ dataListFile);
		PrintWriter pw = new PrintWriter(new FileWriter(contextDir + "/"
				+ dataListFile), true);

		pw.println(" 1   8"); // Need to make this more general.
		pw.println(getSiteCode() + sopacDataFileExt);
		pw.close();
	}

	/**
	 * Create the stfilter driver file.
	 */
	public String createDriverFile(String contextDir) throws Exception {

		String fivespace = "     ";
		String slash = "/"; // This is not File.separator of the webserver
		driverFileName = projectName + driverFileExtension;
		System.out.println("Writing input file: " + contextDir + "/"
				+ driverFileName);
		PrintWriter pw = new PrintWriter(new FileWriter(contextDir + "/"
				+ driverFileName), true);
		pw.println(twospace + "apriori value file:" + twospace + globalDataDir
				+ slash + aprioriValueFile);
		pw.println(twospace + "input file:" + twospace + workDir + slash
				+ projectName + mosesDataListExt);
		pw.println(twospace + "sit_list file:" + twospace + workDir + slash
				+ projectName + mosesSiteListExt);
		pw.println(twospace + "est_parameter file:" + twospace + workDir
				+ slash + projectName + mosesParamFileExt);
		// pw.println(twospace+"est_parameter
		// file:"+twospace+globalDataDir+mosesParamFile);
		pw.println(twospace + "output file:" + twospace + workDir + slash
				+ projectName + outputFileExt);
		pw.println(twospace + "residual file:" + twospace + workDir + slash
				+ projectName + residualFileExt);
		pw.println(twospace + "res_option:" + twospace + resOption);
		pw.println(twospace + "specific term_out file:" + twospace + workDir
				+ slash + projectName + termOutFileExt);
		pw.println(twospace + "specific term_option:" + twospace + termOption);
		pw.println(twospace + "enu_correlation usage:" + twospace + "no");
		pw.println(twospace + "cutoff criterion (year):" + twospace
				+ cutoffCriterion);
		pw.println(twospace + "span to est jump aper (est_jump_span):"
				+ twospace + estJumpSpan);
		pw.println(twospace + "weak_obs (big sigma) criteria:" + twospace
				+ weakObsCriteria.getEast() + twospace
				+ weakObsCriteria.getNorth() + twospace
				+ weakObsCriteria.getUp());
		pw.println(twospace + "outlier (big o-c) criteria mm:" + twospace
				+ outlierCriteria.getEast() + twospace
				+ outlierCriteria.getNorth() + twospace
				+ outlierCriteria.getUp());
		pw
				.println(twospace + "very bad_obs criteria mm:" + twospace
						+ badObsCriteria.getEast() + twospace
						+ badObsCriteria.getNorth() + twospace
						+ badObsCriteria.getUp());
		pw.println(twospace + "t_interval:" + twospace
				+ timeInterval.getBeginTime() + twospace
				+ timeInterval.getEndTime());
		pw.println(twospace + "end:");
		pw.println("---------- part 2 -- apriori information");
		pw.println(twospace + "exit:");
		pw.close();

		// Clean this up since it could be a memory drain.
		// sopacDataFileContent=null;
		return "input-file-created";
	}

	public String deleteProject() throws Exception {
		// projectsToDelete is an ArrayList inherited from GenericProjectBean.
		// It is set by the calling faces page.
		if (projectsToDelete != null && projectsToDelete.size() > 0) {
			for (int i = 0; i < projectsToDelete.size(); i++) {
				String contextName = codeName + "/"
						+ (String) projectsToDelete.get(i);
				cm.removeContext(contextName);
			}
			projectsToDelete.clear();
		}
		setContextList();
		return "project-removed";
	}

	/**
	 * As currently written, this method sets properties that are specific to
	 * the backend application.
	 */
	public String populateProject() throws Exception {
		System.out.println("Chosen project: " + chosenProject);
		String contextName = codeName + "/" + chosenProject;
		projectName = cm.getCurrentProperty(contextName, "projectName");
		hostName = cm.getCurrentProperty(contextName, "hostName");

		resOption = Integer.parseInt(cm.getCurrentProperty(contextName,
				"resOption"));
		termOption = Integer.parseInt(cm.getCurrentProperty(contextName,
				"termOption"));
		cutoffCriterion = Double.parseDouble(cm.getCurrentProperty(contextName,
				"cutoffCriterion"));
		estJumpSpan = Double.parseDouble(cm.getCurrentProperty(contextName,
				"estJumpSpan"));
		// weakObsCriteria=cm.getCurrentProperty(contextName,"weakObsCriteria");
		// outlierCriteria=cm.getCurrentProperty(contextName,"outlierCriteria");
		// badObsCriteria=cm.getCurrentProperty(contextName,"badObsCriteria");
		// timeInterval=cm.getCurrentProperty(contextName,"timeInterval");

		sopacDataFileName = cm.getCurrentProperty(contextName,
				"sopacDataFileName");
		sopacDataFileContent = setSTFILTERInputFile(projectName);
		return "project-populated";
	}

	public String executeSTFILTER(String contextDir, String sopacDataFileName,
			String cfullName) throws Exception {

		System.out.println("FileService URL:" + fileServiceUrl);
		System.out.println("AntService URL:" + antUrl);

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
		// Set up the file service and upload the driver,
		// site list, and gps data files.
		// --------------------------------------------------
		FSClientStub fsclient = new FSClientStub();
		String sopacDestfile = workDir + "/" + sopacDataFileName;
		String driverDestfile = workDir + "/" + driverFileName;
		String siteListDestfile = workDir + "/" + siteListFile;
		String dataListDestfile = workDir + "/" + dataListFile;
		String estParamDestfile = workDir + "/" + estParameterFile;

		try {
			fsclient.setBindingUrl(fileServiceUrl);
			fsclient.uploadFile(contextDir + "/" + sopacDataFileName,
					sopacDestfile);
			fsclient.uploadFile(contextDir + "/" + siteListFile,
					siteListDestfile);
			fsclient.uploadFile(contextDir + "/" + dataListFile,
					dataListDestfile);
			fsclient.uploadFile(contextDir + "/" + driverFileName,
					driverDestfile);
			fsclient.uploadFile(contextDir + "/" + estParameterFile,
					estParamDestfile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// //--------------------------------------------------
		// // Record the names of the input, output, and log
		// // files on the remote server.
		// //--------------------------------------------------
		// String remoteOutputFile=workDir+"/"+projectName+".output";
		// String remoteLogFile=workDir+"/"+projectName+".stdout";

		// cm.setCurrentProperty(cfullName,"RemoteInputFile",destfile);
		// cm.setCurrentProperty(cfullName,"RemoteOutputFile",remoteOutputFile);
		// cm.setCurrentProperty(cfullName,"RemoteLogFile",remoteLogFile);

		// --------------------------------------------------
		// Run the code.
		// --------------------------------------------------

		String[] args = new String[7];
		args[0] = "-DworkDir.prop=" + workDir;
		args[1] = "-DprojectName.prop=" + projectName;
		args[2] = "-Dbindir.prop=" + binPath;
		args[3] = "-DSTFILTERBaseName.prop=" + projectName;
		args[4] = "-buildfile";
		args[5] = bf_loc;
		args[6] = "RunSTFILTER";

		ant.setArgs(args);
		ant.execute();

		return "stfilter-executing";
	}

	/**
	 * This is similar to executeSTFILTER but it must take place on a host with
	 * gnuplot installed on it. Note this assumes for historical reasons that
	 * stfilter and the plotting tool (gnuplot) are on separate machines.
	 * 
	 * This method is currently empty.
	 */
	public String createDataPlot(String contextDir, String sopacDataFileName,
			String cfullName) throws Exception {

		return "gnuplot-plot-created";
	}

	/**
	 * Override this method.
	 */
	public String querySOPAC() throws Exception {

		String minMaxLatLon = null;

		System.out.println("Do the query");
		System.out.println("Use bounding box:" + bboxChecked);
		System.out.println(siteCode);
		System.out.println(beginDate);
		System.out.println(endDate);
		System.out.println(resource);
		System.out.println(contextGroup);
		System.out.println(contextId);
		System.out.println(minMaxLatLon);

		if (bboxChecked) {
			minMaxLatLon = minLatitude + " " + minLongitude + " " + maxLatitude
					+ " " + maxLongitude;
		}

		GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
		gsq.setFromServlet(siteCode, beginDate, endDate, resource,
				contextGroup, contextId, minMaxLatLon);
		sopacQueryResults = gsq.getResource();
		System.out.println("Query Results");
		System.out.println(sopacQueryResults);
		// sopacQueryResults=filterResults(sopacQueryResults,2,3);

		sopacDataFileContent = sopacQueryResults;

		String codeName = getCodeName();
		codeName = codeName.toLowerCase();
		System.out.println("Sopac query action string:" + codeName
				+ "-display-query-results");
		return codeName + "-display-query-results";
	}

	private String setSTFILTERInputFile(String projectName) {
		String sopacDataFileContent = "Null Content; please re-enter";
		String sopacDataFileName = projectName + driverFileExtension;
		try {
			String thedir = cm.getCurrentProperty(codeName + "/" + projectName,
					"Directory");
			System.out.println(thedir + "/" + sopacDataFileName);

			BufferedReader buf = new BufferedReader(new FileReader(thedir + "/"
					+ sopacDataFileName));
			String line = buf.readLine();
			sopacDataFileContent = line + "\n";
			while (line != null) {
				System.out.println(line);
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

	// --------------------------------------------------
	// These are accessor methods.
	// --------------------------------------------------
	public void toggleRenderMPL1(ActionEvent ev) {
		renderMasterParamList1 = !renderMasterParamList1;
		// return renderMasterParamList;
	}

	public boolean getRenderMasterParamList1() {
		return renderMasterParamList1;
	}

	public void setRenderMasterParamList1(boolean renderMasterParamList1) {
		this.renderMasterParamList1 = renderMasterParamList1;
	}

	public void toggleRenderMPL2(ActionEvent ev) {
		renderMasterParamList2 = !renderMasterParamList2;
		// return renderMasterParamList;
	}

	public boolean getRenderMasterParamList2() {
		return renderMasterParamList2;
	}

	public void setRenderMasterParamList2(boolean renderMasterParamList2) {
		this.renderMasterParamList2 = renderMasterParamList2;
	}

	public AllStationsContainer getAllsites() {
		return (AllStationsContainer) allsites;
	}

	public MyStationContainer getMyStation() {
		return (MyStationContainer) myStation;
	}

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

	public String getDriverFileName() {
		return driverFileName;
	}

	public void setDriverFileName(String driverFileName) {
		this.driverFileName = driverFileName;
	}

	public OutlierCriteria getOutlierCriteria() {
		return outlierCriteria;
	}

	public void setOutlierCriteria(OutlierCriteria outlierCriteria) {
		this.outlierCriteria = outlierCriteria;
	}

	public int getResOption() {
		return resOption;
	}

	public void setResOption(int resOption) {
		this.resOption = resOption;
	}

	public int getTermOption() {
		return termOption;
	}

	public void setTermOption(int termOption) {
		this.termOption = termOption;
	}

	public double getCutoffCriterion() {
		return cutoffCriterion;
	}

	public void setCutoffCriterion(double cutoffCriterion) {
		this.cutoffCriterion = cutoffCriterion;
	}

	public double getEstJumpSpan() {
		return estJumpSpan;
	}

	public void setEstJumpSpan(double estJumpSpan) {
		this.estJumpSpan = estJumpSpan;
	}

	public WeakObsCriteria getWeakObsCriteria() {
		return weakObsCriteria;
	}

	public void setWeakObsCriteria(WeakObsCriteria wobc) {
		weakObsCriteria = wobc;
	}

	public BadObsCriteria getBadObsCriteria() {
		return badObsCriteria;
	}

	public void setBadObsCriteria(BadObsCriteria badObsCriteria) {
		this.badObsCriteria = badObsCriteria;
	}

	public TimeInterval getTimeInterval() {
		return timeInterval;
	}

	public void setTimeInterval(TimeInterval timeInterval) {
		this.timeInterval = timeInterval;
	}

	public String getResiURL() {
		return resiURL;
	}

	public void setResiURL(String resiURL) {
		this.resiURL = resiURL;
	}

	/*
	 * Added by Jong
	 * 
	 */
	private SelectItem[] myStationParamList;
	
	public SelectItem[] getMyStationParamList() {
		System.out.println("[!!] Size = "+myStation.getMasterParamList().size());
		myStationParamList = new SelectItem[myStation.getMasterParamList().size()];
		for (int i=0; i<myStation.getMasterParamList().size(); i++) {
			myStationParamList[i] = new SelectItem(new Integer(i), ((EstimateParameter)myStation.getMasterParamList().get(i)).getParameterFullName());
		}
		return myStationParamList;
	}

	private int myStationParamListIndex = 0;
	public int getMyStationParamListIndex() {
		return myStationParamListIndex;
	}
	
	public void myStationParamListChanged(ValueChangeEvent event) {
		if (event.getNewValue() != null) {
			System.out.println("[!!] ValueChangeEvent = "+event.getNewValue());
			//myStationParamListIndex = ((Integer)event.getNewValue()).intValue(); 
			myStationParamListIndex = Integer.parseInt((String) event.getNewValue());
			switch (myStationParamListIndex) {
			case 0:
				currentParam = episodicEast; 
				break;
			case 1:
				currentParam = episodicNorth; 
				break;
			case 2:
				currentParam = episodicUp; 
				break;
			}
		}
		System.out.println("[!!] currentParam = "+currentParam.getParameterFullName());
	}
	
	public void aprioriValueChanged(ValueChangeEvent event) {
		System.out.println("[!!] aprioriValueChanged = "+event.getNewValue());
		if (event.getNewValue() != null) {
			currentParam.setAprioriValue((Double) event.getNewValue());
		}
	}
	
	public void aprioriConstraintChanged(ValueChangeEvent event) {
		System.out.println("[!!] aprioriConstraintChanged = "+event.getNewValue());
		if (event.getNewValue() != null) {
			currentParam.setAprioriConstraint((Double) event.getNewValue());
			addEpisodicBiasParamVector();
		}
	}

	private void addEpisodicBiasParamVector() {
		if (!EpisodicBiasParam.contains(currentParam)) {
			EpisodicBiasParam.add(currentParam);
		}
	}
	
	public void startDateChanged(ValueChangeEvent event) {
		System.out.println("[!!] startDateChanged = "+event.getNewValue());
		if (event.getNewValue() != null) {
			currentParam.setStartDate((Double) event.getNewValue());
			addEpisodicBiasParamVector();
		}
	}
	
	public void endDateChanged(ValueChangeEvent event) {
		System.out.println("[!!] endDateChanged = "+event.getNewValue());
		if (event.getNewValue() != null) {
			currentParam.setEndDate((Double) event.getNewValue());
			addEpisodicBiasParamVector();
		}
	}
	
	Vector EpisodicBiasParam = new Vector();
	EpisodicBias episodicEast = new EpisodicEast(); 
	EpisodicBias episodicNorth = new EpisodicNorth();
	EpisodicBias episodicUp = new EpisodicUp();

	EpisodicBias currentParam = episodicEast; 
	
	public EstimateParameter getCurrentParam() {
		return currentParam;
	}
	
	public String setEstimatedParams() throws Exception {
		launchSTFILTERWS();
		return "set-estimated-params";
	}
	
	int maxHistory = 3;
	Vector paramHistory = new Vector();
	String[] graphName = {"Current", "Old 1", "Old 2"};
	
	public int getParamHistorySize() {
		return paramHistory.size();
	}
	
	public String clearHistory() throws Exception {
		for (int i = paramHistory.size(); i > 1; i--) {
			paramHistory.remove(i-1);
		}
		
		return "";
	}
	
	public String savePref() {
		return "";
	}
	
	/**
	 * Navigation Rule
	 * @return
	 * @throws Exception
	 */
	public String createNewAndQuery() throws Exception {
		// setParameterValues();
		createNewProject();
		querySOPAC();
		return setEstimatedParams();
	}
	
	public static void main (String[] args) {
		Calendar org = Calendar.getInstance();
		Calendar cal2004 = Calendar.getInstance();
		cal2004.set(2004, 0, 1, 0, 0, 0);
		Calendar cal2005 = Calendar.getInstance();
		cal2005.set(2005, 0, 1, 0, 0, 0);

		Calendar cur = Calendar.getInstance();
		cur.setTimeInMillis(cal2004.getTimeInMillis() + (long)(.5777*(cal2005.getTimeInMillis() - cal2004.getTimeInMillis())));
		System.out.println(cur.getTime());
		
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

}
