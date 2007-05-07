package cgl.webservices;

//Not explicitly naming these because they are famous.
import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.tools.ant.Main;
import org.apache.log4j.*;
//import org.apache.myfaces.blank.EpisodicEast;

//Needed to get the ServletContext to read the properties.
import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

//SOPAC Client Stuff
import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

/**
 * Despite the name, this is not a general purpose AnalyzeTseri service.  It
 * is used to make plots of the GRWS time series data.
 */

public class AnalyzeTseriService extends AntVisco implements Runnable {
	static Logger logger = Logger.getLogger(AnalyzeTseriService.class);

	final String FILE_PROTOCOL = "file";

	final String HTTP_PROTOCOL = "http";

	// These are properties read from the property file.
	Properties properties;

	String serverUrl;

	String baseWorkDir;

	String baseDestDir;

	String outputDestDir;

	String projectName;

	String binPath;

	String buildFilePath;

	String antTarget;

	// The working directory, derived from baseWorkDir and projectName;
	String workDir;

	// The site code value, passed in when the service is invoked.
	// String siteCode;

	// Useful formatting strings
	String twospace = "  ";

	String fivespace = "     ";

	String slash = "/";

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

	// STFILTER properties
	private String codeName = "STFILTER";

	private int resOption = 1;

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
	private String filteredDataFileExt = ".input";

	// These contain the site estimate params. Note
	// this needs to be generalized, as I'm assuming only
	// one site is used at a time.
	StationContainer myStation;

	StationContainer allsites;

	StationParamList myStationList, allsitesList;

	MasterParamList masterList;

	/**
	 * Fun begins here. This is the workhorse constructor.
	 */
	public AnalyzeTseriService(boolean useClassLoader) throws Exception {
		super();

		// Set up here the station list vectors.
		masterList = new MasterParamList();
		myStationList = new StationParamList();
		allsitesList = new AllSitesParamList();

		// Set up here the station conntainer
		myStation = new MyStationContainer("LBC1");
		myStation.setEstParamVector(myStationList.getStationParamList());
		myStation.setMasterParamList(masterList.getStationParamList());
		System.out.println("[!!] myStationList size = "
				+ myStationList.getStationParamList().size());
		System.out.println("[!!] masterList size = "
				+ masterList.getStationParamList().size());

		// Set up the default station list.
		allsites = new AllStationsContainer();
		allsites.setEstParamVector(allsitesList.getStationParamList());
		allsites.setMasterParamList(masterList.getStationParamList());
		System.out.println("[!!] allsitesList size = "
				+ allsitesList.getStationParamList().size());

		if (useClassLoader) {
			System.out.println("Using classloader");
			// This is useful for command line clients but does not work
			// inside Tomcat.
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			properties = new Properties();
			Enumeration e = loader
					.getResources("analyze_tseri_config.properties");

			// This works if you are using the classloader but not inside
			// Tomcat.
			properties.load(loader
					.getResourceAsStream("analyze_tseri_config.properties"));
		} else {
			// Extract the Servlet Context
			System.out.println("Using Servlet Context");
			MessageContext msgC = MessageContext.getCurrentContext();
			ServletContext context = ((HttpServlet) msgC
					.getProperty(HTTPConstants.MC_HTTP_SERVLET))
					.getServletContext();

			String propertyFile = context.getRealPath("/")
					+ "/WEB-INF/classes/analyze_tseri_config.properties";
			System.out.println("Prop file location " + propertyFile);

			properties = new Properties();
			properties.load(new FileInputStream(propertyFile));
		}

		serverUrl = properties.getProperty("analyze_tseri.service.url");
		baseWorkDir = properties.getProperty("base.workdir");
		baseDestDir = properties.getProperty("base.dest.dir");
		projectName = properties.getProperty("project.name");
		binPath = properties.getProperty("bin.path");
		buildFilePath = properties.getProperty("build.file.path");
		antTarget = properties.getProperty("ant.target");
		aprioriValueFile = properties.getProperty("apriori.value.file");

		// Put a time stamp on the project name:
		projectName += "-" + (new Date()).getTime();

		outputDestDir = baseDestDir + "/" + projectName;
		workDir = baseWorkDir + File.separator + projectName;

		debug("Here are some property values");
		debug("serverUrl", serverUrl);
		debug("baseWorkDir", baseWorkDir);
		debug("baseDestDir", baseDestDir);
		debug("projectName", projectName);
		debug("binPath", binPath);
		debug("buildFilePath", buildFilePath);
		debug("antTarget", antTarget);
		debug("aprioriValueFile", aprioriValueFile);
	}

	/**
	 * This is an empty argument constructor required by the bean pattern.
	 */
	public AnalyzeTseriService() throws Exception {
		this(false);
	}

	/**
	 * Create the site list file. Currently we only support one site and the XYZ
	 * format (ie "1 8").
	 */
	private void createSiteListFile(String siteCode) throws Exception {

		siteListFile = projectName + mosesSiteListExt;
		System.out.println("Writing input file: " + workDir + "/"
				+ siteListFile);
		PrintWriter pw = new PrintWriter(new FileWriter(workDir + "/"
				+ siteListFile), true);

		pw.println("  1"); // Need to make this more general.
		pw.println(siteCode.toUpperCase() + "_GPS");
		pw.close();
	}

	private void createEstimatedParamFile() throws Exception {

		estParameterFile = projectName + mosesParamFileExt;
		PrintWriter pw = new PrintWriter(new FileWriter(workDir + "/"
				+ estParameterFile), true);
		if (myStation.printContents() != null) {
			pw.println("  2");
			// Newline is not necessary
			pw.print(allsites.printContents());
			pw.println(myStation.printContents());
		} else {
			pw.println("  1");
			pw.println(allsites.printContents());
		}
		pw.close();
	}

	private void createDataListFile(String siteCode, String dataFileName)
			throws Exception {

		dataListFile = projectName + mosesDataListExt;
		System.out.println("Writing input file: " + workDir + "/"
				+ dataListFile);
		PrintWriter pw = new PrintWriter(new FileWriter(workDir + "/"
				+ dataListFile), true);

		pw.println(" 1   8"); // Need to make this more general.
		// pw.println(siteCode+sopacDataFileExt);
		File tmp = new File(dataFileName);
		//pw.println(dataFileName);
		pw.println(tmp.getName());
		pw.close();
	}

	/**
	 * Create the stfilter driver file.
	 */
	private String createDriverFile() throws Exception {
//		driverFileName = projectName + driverFileExtension;
//		System.out.println("Writing input file: " + workDir + slash + driverFileName);
//		PrintWriter pw = new PrintWriter(new FileWriter(workDir + slash + driverFileName), true);
//		pw.println(twospace + "apriori value file:" + twospace + aprioriValueFile);
//		pw.println(twospace + "input file:" + twospace + workDir + slash + projectName + mosesDataListExt);
//		pw.println(twospace + "sit_list file:" + twospace + workDir + slash + projectName + mosesSiteListExt);
//		pw.println(twospace + "est_parameter file:" + twospace + workDir + slash + projectName + mosesParamFileExt);
//		// pw.println(twospace+"est_parameter
//		// file:"+twospace+globalDataDir+mosesParamFile);
//		pw.println(twospace + "output file:" + twospace + workDir + slash + projectName + outputFileExt);
//		pw.println(twospace + "residual file:" + twospace + workDir + slash + projectName + residualFileExt);
//		pw.println(twospace + "res_option:" + twospace + resOption);
//		pw.println(twospace + "specific term_out file:" + twospace + workDir + slash + projectName + termOutFileExt);
//		pw.println(twospace + "specific term_option:" + twospace + termOption);
//		pw.println(twospace + "enu_correlation usage:" + twospace + "no");
//		pw.println(twospace + "cutoff criterion (year):" + twospace + cutoffCriterion);
//		pw.println(twospace + "span to est jump aper (est_jump_span):" + twospace + estJumpSpan);
//		pw.println(twospace + "weak_obs (big sigma) criteria:" + twospace + weakObsCriteria.getEast() + twospace + weakObsCriteria.getNorth() + twospace + weakObsCriteria.getUp());
//		pw.println(twospace + "outlier (big o-c) criteria mm:" + twospace + outlierCriteria.getEast() + twospace + outlierCriteria.getNorth() + twospace + outlierCriteria.getUp());
//		pw.println(twospace + "very bad_obs criteria mm:" + twospace + badObsCriteria.getEast() + twospace + badObsCriteria.getNorth() + twospace + badObsCriteria.getUp());
//		pw.println(twospace + "t_interval:" + twospace + timeInterval.getBeginTime() + twospace + timeInterval.getEndTime()); 
//		pw.println(twospace + "end:");
//		pw.println("---------- part 2 -- apriori information");
//		pw.println(twospace + "exit:");
//		pw.close();

		driverFileName = projectName + driverFileExtension;
		System.out.println("Writing input file: " + workDir + slash + driverFileName);
		PrintWriter pw = new PrintWriter(new FileWriter(workDir + slash + driverFileName), true);
		pw.println(twospace + "apriori value file:" + twospace + aprioriValueFile);
		pw.println(twospace + "input file:" + twospace + projectName + mosesDataListExt);
		pw.println(twospace + "sit_list file:" + twospace + projectName + mosesSiteListExt);
		pw.println(twospace + "est_parameter file:" + twospace + projectName + mosesParamFileExt);
		// pw.println(twospace+"est_parameter
		// file:"+twospace+globalDataDir+mosesParamFile);
		pw.println(twospace + "output file:" + twospace + projectName + outputFileExt);
		pw.println(twospace + "residual file:" + twospace + projectName + residualFileExt);
		pw.println(twospace + "res_option:" + twospace + resOption);
		pw.println(twospace + "specific term_out file:" + twospace + projectName + termOutFileExt);
		pw.println(twospace + "specific term_option:" + twospace + termOption);
		pw.println(twospace + "enu_correlation usage:" + twospace + "no");
		pw.println(twospace + "cutoff criterion (year):" + twospace + cutoffCriterion);
		pw.println(twospace + "span to est jump aper (est_jump_span):" + twospace + estJumpSpan);
		pw.println(twospace + "weak_obs (big sigma) criteria:" + twospace + weakObsCriteria.getEast() + twospace + weakObsCriteria.getNorth() + twospace + weakObsCriteria.getUp());
		pw.println(twospace + "outlier (big o-c) criteria mm:" + twospace + outlierCriteria.getEast() + twospace + outlierCriteria.getNorth() + twospace + outlierCriteria.getUp());
		pw.println(twospace + "very bad_obs criteria mm:" + twospace + badObsCriteria.getEast() + twospace + badObsCriteria.getNorth() + twospace + badObsCriteria.getUp());
		pw.println(twospace + "t_interval:" + twospace + timeInterval.getBeginTime() + twospace + timeInterval.getEndTime()); 
		pw.println(twospace + "end:");
		pw.println("---------- part 2 -- apriori information");
		pw.println(twospace + "exit:");
		pw.close();
		// Clean this up since it could be a memory drain.
		// sopacDataFileContent=null;
		return "input-file-created";
	}

	/**
	 * This helper method assumes input is a multlined String of tabbed columns.
	 * It cuts out the number of columns on the left specified by cutLeftColumns
	 * and number on the right by cutRightColumns.
	 * 
	 * Use -1 for both cutLeft and cutRight to do null filtering (ie just
	 * renaming the file).
	 */
	protected void filterResults(String tabbedFile, String gnuplotInputFile,
			int cutLeftColumns, int cutRightColumns) throws Exception {
		String returnString = "";
		String space = " ";
		StringTokenizer st;
		BufferedReader br = new BufferedReader(new FileReader(tabbedFile));
		PrintWriter printer = new PrintWriter(new FileWriter(gnuplotInputFile),
				true);
		String line = br.readLine();
		while (line != null) {
			st = new StringTokenizer(line);
			String newLine = line;
			int tokenCount = st.countTokens();
			if (cutRightColumns < 0)
				cutRightColumns = tokenCount + 1;
			for (int i = 0; i < tokenCount; i++) {
				String temp = st.nextToken();
				if (i >= cutLeftColumns && i < (tokenCount - cutRightColumns)) {
					newLine += temp + space;
				}
			}
			// System.out.println(newLine);
			printer.println(newLine);
			line = br.readLine();
		}
		return;
	}

	private void makeWorkDir(String workDir, String bf_loc) throws Exception {
		System.out.println("Working Directory is " + workDir);

		String[] args0 = new String[4];
		args0[0] = "-DworkDir.prop=" + workDir;
		args0[1] = "-buildfile";
		args0[2] = bf_loc;
		args0[3] = "MakeWorkDir";

		setArgs(args0);
		run();
	}

	private String extractSimpleName(String extendedName) {
		return (new File(extendedName)).getName();
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

	private String[] setUpArgArray(String inputFileUrlString, String workDir,
			String outputDestDir, String projectName, String binPath,
			String buildFilePath, String antTarget) throws Exception {

		String[] args = new String[8];
		args[0] = "-DworkDir.prop=" + workDir;
		args[1] = "-DprojectName.prop=" + projectName;
		args[2] = "-Dbindir.prop=" + binPath;
		args[3] = "-DAnalyzeTseriBaseName.prop=" + projectName;
		args[4] = "-DoutputDestDir.prop=" + outputDestDir;
		args[5] = "-buildfile";
		args[6] = buildFilePath;
		args[7] = antTarget;

		return args;
	}

	// --------------------------------------------------
	// Find the first non-blank line and count columns.
	// Note this can screw up if input file is not
	// formated correctly, but then AnalyzeTseri itself
	// would probably not work either.
	// --------------------------------------------------
	protected int getFileDimension(String fileFullName) {

		boolean success = false;
		int ndim = 0;
		StringTokenizer st;
		try {

			BufferedReader buf = new BufferedReader(
					new FileReader(fileFullName));

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
	protected int getLineCount(String fileFullName) {
		int nobsv = 0;
		try {
			LineNumberReader lnr = new LineNumberReader(new FileReader(
					fileFullName));

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

	/**
	 * This version runs in non-blocking mode and gets the data from the SOPAC
	 * data service.
	 */
	public String[] runNonblockingAnalyzeTseri(String siteCode,
			String beginDate, String endDate) throws Exception {
		try {
			String dataUrl = querySOPACGetURL(siteCode, beginDate, endDate);
			return runNonblockingAnalyzeTseri(siteCode, dataUrl);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception();
		}
	}

	/**
	 * This version runs in blocking mode and gets the data from the SOPAC data
	 * service.
	 */
	public String[] runBlockingAnalyzeTseri(String siteCode, String beginDate,
			String endDate) throws Exception {
		try {
			String dataUrl = querySOPACGetURL(siteCode, beginDate, endDate);
			return runBlockingAnalyzeTseri(siteCode, dataUrl);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception();
		}
	}

	/**
	 * This is the simplified API that uses default values.
	 */

	public String[] runBlockingAnalyzeTseri(String siteCode,
			String inputFileUrlString) throws Exception {
		System.out.println("Running blocking execution");
		System.out.println(inputFileUrlString);

		String[] returnVals = runBlockingAnalyzeTseri(siteCode,
				inputFileUrlString, baseWorkDir, outputDestDir, projectName,
				binPath, buildFilePath, antTarget);
		return returnVals;
	}

	/**
	 * This is the simplified API that uses default properties.
	 */
	public String[] runNonblockingAnalyzeTseri(String siteCode,
			String inputFileUrlString) throws Exception {

		System.out.println("Running non-blocking execution");
		System.out.println(inputFileUrlString);

		String[] returnVals = runNonblockingAnalyzeTseri(siteCode,
				inputFileUrlString, baseWorkDir, outputDestDir, projectName,
				binPath, buildFilePath, antTarget);
		return returnVals;
	}

	/**
	 * This version is used to to hold response until AnalyzeTseri finished
	 * executing. This is the full API.
	 * (jychoi: Deprecated)
	 */
	public String[] runBlockingAnalyzeTseri(String siteCode,
			String inputFileUrlString, String baseWorkDir,
			String outputDestDir, String projectName, String binPath,
			String buildFilePath, String antTarget) throws Exception {

		// Set up the work directory
		// String workDir=baseWorkDir+File.separator+projectName;
		System.out.println("[!!]baseWorkDir="+baseWorkDir);
		System.out.println("[!!]workDir="+workDir);
		System.out.println("[!!]outputDestDir="+outputDestDir);
		System.out.println("[!!]binPath="+binPath);
		System.out.println("[!!]buildFilePath="+buildFilePath);
		System.out.println("[!!]antTarget="+antTarget);
		makeWorkDir(workDir, buildFilePath);

		// Copy the input file to the working directory, if
		// necessary.
		String localFile = downloadInputFile(inputFileUrlString, workDir);

		// Filter the file
		String localFileFiltered = workDir + File.separator + projectName
				+ filteredDataFileExt;
		filterResults(localFile, localFileFiltered, -1, -1);

		// Make the input files.
		createSiteListFile(siteCode);
		createEstimatedParamFile();
		createDataListFile(siteCode, localFileFiltered);
		createDriverFile();

		// //Get the dimensions and number of observations.
		// int ndim=getFileDimension(localFileFiltered);
		// int nobsv=getLineCount(localFileFiltered);

		// ? localFileFiltered? localFile?
		String[] args = setUpArgArray(localFileFiltered, workDir,
				outputDestDir, projectName, binPath, buildFilePath, antTarget);

		// Methods inherited from parent
		setArgs(args);
		run();
		return getTheReturnFiles();
	}

	/**
	 * This is the simplified API that uses default values.
	 */

	public String[] execBlockingAnalyzeTseri(String siteCode, String inputFileUrlString)
			throws Exception {
		debug("Running blocking execution");
		debug(inputFileUrlString);

		String[] returnVals = execBlockingAnalyzeTseri(siteCode, inputFileUrlString,
				baseWorkDir, outputDestDir, projectName, binPath,
				buildFilePath, antTarget);
		return returnVals;
	}

	/**
	 * This version is used to to hold response until AnalyzeTseri finished
	 * executing. This is the full API.
	 */
	public String[] execBlockingAnalyzeTseri(String siteCode,
			String inputFileUrlString, String baseWorkDir,
			String outputDestDir, String projectName, String binPath,
			String buildFilePath, String antTarget) throws Exception {

		// Set up the work directory
		// String workDir=baseWorkDir+File.separator+projectName;
		System.out.println("[!!]baseWorkDir="+baseWorkDir);
		System.out.println("[!!]workDir="+workDir);
		System.out.println("[!!]outputDestDir="+outputDestDir);
		System.out.println("[!!]binPath="+binPath);
		System.out.println("[!!]buildFilePath="+buildFilePath);
		System.out.println("[!!]antTarget="+antTarget);
		makeWorkDir(workDir, buildFilePath);

		makeWorkDir(workDir, buildFilePath);

		// Copy the input file to the working directory, if necessary.
		String localFile = downloadInputFile(inputFileUrlString, workDir);

		// Filter the file
		String localFileFiltered = workDir + File.separator + projectName + filteredDataFileExt;
		filterResults(localFile, localFileFiltered, -1, -1);

		// Make the input files.
		createSiteListFile(siteCode);
		createEstimatedParamFile();
		createDataListFile(siteCode, localFileFiltered);
		createDriverFile();

		// //Get the dimensions and number of observations.
		// int ndim=getFileDimension(localFileFiltered);
		// int nobsv=getLineCount(localFileFiltered);

		// ? localFileFiltered? localFile?
		String[] args = setUpArgArray(localFileFiltered, workDir,
				outputDestDir, projectName, binPath, buildFilePath, antTarget);

		// Methods inherited from parent
		setArgs(args);
		run();
		return getTheReturnFiles();
	}

	// public String[] execBlockingAnalyzeTseri(String siteCode,
	// String data, String baseWorkDir,
	// String outputDestDir, String projectName, String binPath,
	// String buildFilePath, String antTarget) throws Exception {
	//
	// // Set up the work directory
	// // String workDir=baseWorkDir+File.separator+projectName;
	// System.out.println("[!!]baseWorkDir="+baseWorkDir);
	// System.out.println("[!!]workDir="+workDir);
	// System.out.println("[!!]outputDestDir="+outputDestDir);
	// System.out.println("[!!]binPath="+binPath);
	// System.out.println("[!!]buildFilePath="+buildFilePath);
	// System.out.println("[!!]antTarget="+antTarget);
	// makeWorkDir(workDir, buildFilePath);
	//
	// // Copy the input file to the working directory, if necessary.
	// //String localFile = downloadInputFile(inputFileUrlString, workDir);
	// //String fileSimpleName = extractSimpleName(inputFileUrl.getFile());
	// //System.out.println(fileSimpleName);
	//
	// // File names for SOPAC data and filtered data
	// String localDataFilename = workDir + File.separator + projectName +
	// sopacDataFileExt;
	// String localFileFiltered = workDir + File.separator + projectName +
	// filteredDataFileExt;
	//
	// try {
	// // Stream to write file
	// FileOutputStream fout;
	//
	// // Open an output stream
	// fout = new FileOutputStream(localDataFilename);
	//
	// // Print a line of text
	// new PrintStream(fout).print(data);
	//
	// // Close our output stream
	// fout.close();
	// }
	// // Catches any error conditions
	// catch (IOException e) {
	// System.err.println("[Error] Unable to write to file:
	// "+localDataFilename);
	// return null;
	// }
	//
	// filterResults(localDataFilename, localFileFiltered, -1, -1);
	//
	// // Make the input files.
	// createSiteListFile(siteCode);
	// createEstimatedParamFile();
	// createDataListFile(siteCode, localFileFiltered);
	// createDriverFile();
	//
	// // //Get the dimensions and number of observations.
	// // int ndim=getFileDimension(localFileFiltered);
	// // int nobsv=getLineCount(localFileFiltered);
	//
	// // ? localFileFiltered? localFile?
	// String[] args = setUpArgArray(localFileFiltered, workDir,
	// outputDestDir, projectName, binPath, buildFilePath, antTarget);
	//
	// // Methods inherited from parent
	// setArgs(args);
	//		run();
	//		return getTheReturnFiles();
	//	}
	
	/**
	 * This version immediately returns and is used for programs that take
	 * longer to run. This is the full API.
	 */
	public String[] runNonblockingAnalyzeTseri(String siteCode,
			String inputFileUrlString, String baseWorkDir,
			String outputDestDir, String projectName, String binPath,
			String buildFilePath, String antTarget) throws Exception {

		// String workDir=baseWorkDir+File.separator+projectName;

		// Make working directory
		makeWorkDir(workDir, buildFilePath);

		// Copy the input file to the working directory, if
		// necessary.
		String localFile = downloadInputFile(inputFileUrlString, workDir);

		// Filter the file
		String localFileFiltered = workDir + File.separator + projectName
				+ filteredDataFileExt;
		filterResults(localFile, localFileFiltered, -1, -1);

		// Make the input files.
		createSiteListFile(siteCode);
		createEstimatedParamFile();
		createDataListFile(siteCode, localFileFiltered);
		createDriverFile();

		String[] args = setUpArgArray(localFileFiltered, workDir,
				outputDestDir, projectName, binPath, buildFilePath, antTarget);

		// Methods inherited from parent
		setArgs(args);
		execute();

		return getTheReturnFiles();
	}

	/**
	 * A dumb little method for constructing the URL outputs. This will not get
	 * called if the execute()/run() method fails.
	 */
	protected String[] getTheReturnFiles() {

		String[] extensions = { ".input.xyz.X.png", ".input.xyz.Y.png",
				".input.xyz.Z.png", ".resi", ".data", ".drv", ".input",
				".list", ".mdl", ".out", ".para", ".site" };

		String[] returnFiles = new String[extensions.length];
		for (int i = 0; i < extensions.length; i++) {
			returnFiles[i] = serverUrl + "/" + projectName + "/" + projectName
					+ extensions[i];
		}

		return returnFiles;
	}

	/**
	 * This version of the client gets back the URL of the results, rather than
	 * the results directly. The String return type is used for JSF page
	 * navigation.
	 */
	protected String querySOPACGetURL(String siteCode, String beginDate,
			String endDate) throws Exception {

		String resource = "procCoords";
		String contextGroup = "reasonComb";
		String minMaxLatLon = "";
		String contextId = "4";

		GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
		gsq.setFromServlet(siteCode, beginDate, endDate, resource,
				contextGroup, contextId, minMaxLatLon, true);
		String dataUrl = gsq.getResource();
		System.out.println("GRWS data url: " + dataUrl);
		return dataUrl;
	}

	/**
	 * Add/Update one of default estimated parameters, VelocityEast,
	 * VelocityNorth, and VelocityUp.
	 */
	public static void addVelocityBias(StationContainer sc,
			StationParamList sp, int type, double aprioriValue,
			double aprioriConstraint, double startDate, double endDate,
			boolean isUpdateOnly) {
		VelocityBias vb = null;
		switch (type) {
		case 4:
			vb = (VelocityEast) sp.velocityEast;
			break;
		case 5:
			vb = (VelocityNorth) sp.velocityNorth;
			break;
		case 6:
			vb = (VelocityUp) sp.velocityUp;
			break;
		}

		vb.setAprioriConstraint(new Double(aprioriConstraint));
		vb.setAprioriValue(new Double(aprioriValue));
		vb.setStartDate(new Double(startDate));
		vb.setEndDate(new Double(endDate));
		if (!isUpdateOnly) {
			sc.addEstParameter(vb);
		}
	}

	/**
	 * Add one of estimated parameters, EpisodicEast, EpisodicNoth, and
	 * EpisodicUp.
	 */
	public static void addEpisodicBias(StationContainer sc,
			StationParamList sp, int type, double aprioriValue,
			double aprioriConstraint, double startDate, double endDate) {
		EpisodicBias eb = null;
		switch (type) {
		case 7:
			eb = (EpisodicBias) sp.episodicEast;
			break;
		case 8:
			eb = (EpisodicNorth) sp.episodicNorth;
			break;
		case 9:
			eb = (EpisodicUp) sp.episodicUp;
			break;
		}

		eb.setAprioriConstraint(new Double(aprioriConstraint));
		eb.setAprioriValue(new Double(aprioriValue));
		eb.setStartDate(new Double(startDate));
		eb.setEndDate(new Double(endDate));
		sc.addEstParameter(eb);
	}

	/**
	 * Add one of estimated parameters, EpisodicEast, EpisodicNoth, and
	 * EpisodicUp.
	 */
	public static void addAnnualAmpBias(StationContainer sc,
			StationParamList sp, int type, double aprioriValue,
			double aprioriConstraint, double startDate, double periodicLength) {
		AnnualAmpBias ep = null;
		switch (type) {
		case 16:
			ep = (AnnualAmpBias) sp.annualAmpEast;
			break;
		case 17:
			ep = (AnnualAmpBias) sp.annualAmpNorth;
			break;
		case 18:
			ep = (AnnualAmpBias) sp.annualAmpUp;
			break;
		}

		ep.setAprioriConstraint(new Double(aprioriConstraint));
		ep.setAprioriValue(new Double(aprioriValue));
		ep.setStartDate(new Double(startDate));
		ep.setPeriodLength(new Double(periodicLength));
		sc.addEstParameter(ep);
	}

	/**
	 * Add/Update one of default estimated parameters, ConstantBiasEast,
	 * ConstantBiasNorth, and ConstantBiasUp.
	 */
	public static void addConstantBias(StationContainer sc,
			StationParamList sp, int type, double aprioriValue,
			double aprioriConstraint, double startDate, boolean isUpdateOnly) {
		ConstantBias cb = null;
		switch (type) {
		case 1:
			cb = (ConstantBiasEast) sp.constantBiasEast;
			break;
		case 2:
			cb = (ConstantBiasNorth) sp.constantBiasNorth;
			break;
		case 3:
			cb = (ConstantBiasUp) sp.constantBiasUp;
			break;
		}
	
		cb.setAprioriConstraint(new Double(aprioriConstraint));
		cb.setAprioriValue(new Double(aprioriValue));
		cb.setStartDate(new Double(startDate));
		if (!isUpdateOnly) {
			sc.addEstParameter(cb);
		}
	}

	/**
	 * Add one of estimated parameters, EpisodicEast, EpisodicNoth, and
	 * EpisodicUp.
	 */
	public static void addAnnualPhaseBias(StationContainer sc,
			StationParamList sp, int type, double aprioriValue,
			double aprioriConstraint, double startDate, double periodicLength) {
		AnnualPhaseBias ep = null;
		switch (type) {
		case 19:
			ep = (AnnualPhaseBias) sp.annualPhaseEast;
			break;
		case 20:
			ep = (AnnualPhaseBias) sp.annualPhaseNorth;
			break;
		case 21:
			ep = (AnnualPhaseBias) sp.annualPhaseUp;
			break;
		}

		ep.setAprioriConstraint(new Double(aprioriConstraint));
		ep.setAprioriValue(new Double(aprioriValue));
		ep.setStartDate(new Double(startDate));
		ep.setPeriodLength(new Double(periodicLength));
		sc.addEstParameter(ep);
	}

	/**
	 * Add one of estimated parameters, EpisodicEast, EpisodicNoth, and
	 * EpisodicUp.
	 */
	public static void addSemiannualAmpBias(StationContainer sc,
			StationParamList sp, int type, double aprioriValue,
			double aprioriConstraint, double startDate, double periodicLength) {
		SemiannualAmpBias ep = null;
		switch (type) {
		case 22:
			ep = (SemiannualAmpBias) sp.semiannualAmpEast;
			break;
		case 23:
			ep = (SemiannualAmpBias) sp.semiannualAmpNorth;
			break;
		case 24:
			ep = (SemiannualAmpBias) sp.semiannualAmpUp;
			break;
		}

		ep.setAprioriConstraint(new Double(aprioriConstraint));
		ep.setAprioriValue(new Double(aprioriValue));
		ep.setStartDate(new Double(startDate));
		ep.setPeriodLength(new Double(periodicLength));
		sc.addEstParameter(ep);
	}

	public static String[] execTest(String siteCode, String dataUrl,
			double[][] globalParam, double[][] siteParam) {
		String[] rtn = { siteCode, dataUrl, String.valueOf(globalParam[0][0]) };
		return rtn;
	}
	
	public static String execAnalyzeTseriSimpleBean(AnalyzeTseriSimpleBean bean) {
		return Integer.toString(bean.getId());
	}
	
	public static String[] execAnalyzeTseri(AnalyzeTseriBean bean) {
		return AnalyzeTseriService.execAnalyzeTseri(bean.getSiteCode(), 
				bean.getResOption(), bean.getTermOption(), bean.getCutoffCriterion(), bean.getEstJumpSpan(), 
				bean.getWeakObsCriteriaEast(), bean.getWeakObsCriteriaNorth(), bean.getWeakObsCriteriaUp(), 
				bean.getOutlierCriteriaEast(), bean.getOutlierCriteriaNorth(), bean.getBadObsCriteriaUp(), 
				bean.getBadObsCriteriaEast(), bean.getBadObsCriteriaNorth(), bean.getBadObsCriteriaUp(), 
				bean.getTimeIntervalBeginTime(), bean.getTimeIntervalEndTime(), 
				bean.getSopacDataFileUrl(), bean.getGlobalParam(), bean.getSiteParam());
	}
	
	public static String[] execAnalyzeTseri(String siteCode,  
			int resOption, int termOption, double cutoffCriterion, double estJumpSpan,
			double weakObsCriteriaEast, double weakObsCriteriaNorth, double weakObsCriteriaUp,
			double outlierCriteriaEast, double outlierCriteriaNorth, double outlierCriteriaUp,
			double badObsCriteriaEast, double badObsCriteriaNorth, double badObsCriteriaUp,
			double timeIntervalBeginTime, double timeIntervalEndTime,
			String inputFileUrlString, double[][] globalParam, double[][] siteParam) {
		
		AnalyzeTseriService ats;
		
		try {
			ats = new AnalyzeTseriService(false);
			ats.myStation.setSiteName(siteCode);
			ats.resOption = resOption;
			ats.termOption = termOption;
			ats.cutoffCriterion = cutoffCriterion;
			ats.estJumpSpan = estJumpSpan;
			ats.weakObsCriteria.east = weakObsCriteriaEast;
			ats.weakObsCriteria.north = weakObsCriteriaNorth;
			ats.weakObsCriteria.up = weakObsCriteriaUp;
			ats.outlierCriteria.east = outlierCriteriaEast;
			ats.outlierCriteria.north = outlierCriteriaNorth;
			ats.outlierCriteria.up = outlierCriteriaUp;
			ats.badObsCriteria.east = badObsCriteriaEast;
			ats.badObsCriteria.north = badObsCriteriaNorth;
			ats.badObsCriteria.up = badObsCriteriaUp;
			ats.timeInterval.beginTime = timeIntervalBeginTime;
			ats.timeInterval.endTime = timeIntervalEndTime;
			

			System.out.println("Size : " + globalParam.length);
			System.out.println("Size : " + siteParam.length);
			for (int i = 0; i < globalParam.length; i++) {
				switch ((int) globalParam[i][0]) {
				case 1:
				case 2:
				case 3:
					addConstantBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3], true);
					break;
				case 4:
				case 5:
				case 6:
					addVelocityBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4], true);
					break;
				case 7:
				case 8:
				case 9:
					addEpisodicBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4]);
					break;
				case 16:
				case 17:
				case 18:
					addAnnualAmpBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4]);
					break;
				case 19:
				case 20:
				case 21:
					addAnnualPhaseBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4]);
					break;
				case 22:
				case 23:
				case 24:
					addSemiannualAmpBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4]);
					break;
				}
			}

			for (int i = 0; i < siteParam.length; i++) {
				switch ((int) siteParam[i][0]) {
				case 1:
				case 2:
				case 3:
					addConstantBias(ats.myStation, ats.myStationList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3], false);
					break;
				case 4:
				case 5:
				case 6:
					addVelocityBias(ats.myStation, ats.myStationList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3], siteParam[i][4],
							false);
					break;
				case 7:
				case 8:
				case 9:
					addEpisodicBias(ats.myStation, ats.myStationList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3], siteParam[i][4]);
					break;
				case 16:
				case 17:
				case 18:
					addAnnualAmpBias(ats.myStation, ats.myStationList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3],
							siteParam[i][4]);
					break;
				case 19:
				case 20:
				case 21:
					addAnnualPhaseBias(ats.allsites, ats.allsitesList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3],
							siteParam[i][4]);
					break;
				case 22:
				case 23:
				case 24:
					addSemiannualAmpBias(ats.allsites, ats.allsitesList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3],
							siteParam[i][4]);
					break;
				}
			}

			System.out.println("----------------------------------");
			System.out.println("Executing runBlockingAnalyzeTseri ... ");
			String[] returnVals = ats.execBlockingAnalyzeTseri(siteCode, inputFileUrlString);

			for (int i = 0; i < returnVals.length; i++) {
				System.out.println(returnVals[i]);
			}
			
			return returnVals;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	
	public static String[] execATS(String siteCode,  
			int resOption, int termOption, double cutoffCriterion, double estJumpSpan,
			double weakObsCriteriaEast, double weakObsCriteriaNorth, double weakObsCriteriaUp,
			double outlierCriteriaEast, double outlierCriteriaNorth, double outlierCriteriaUp,
			double badObsCriteriaEast, double badObsCriteriaNorth, double badObsCriteriaUp,
			double timeIntervalBeginTime, double timeIntervalEndTime,
			String dataUrl, double[][] globalParam, double[][] siteParam) {
		
		AnalyzeTseriService ats;
		
		try {
			ats = new AnalyzeTseriService(false);
			ats.myStation.setSiteName(siteCode);
			ats.resOption = resOption;
			ats.termOption = termOption;
			ats.cutoffCriterion = cutoffCriterion;
			ats.estJumpSpan = estJumpSpan;
			ats.weakObsCriteria.east = weakObsCriteriaEast;
			ats.weakObsCriteria.north = weakObsCriteriaNorth;
			ats.weakObsCriteria.up = weakObsCriteriaUp;
			ats.outlierCriteria.east = outlierCriteriaEast;
			ats.outlierCriteria.north = outlierCriteriaNorth;
			ats.outlierCriteria.up = outlierCriteriaUp;
			ats.badObsCriteria.east = badObsCriteriaEast;
			ats.badObsCriteria.north = badObsCriteriaNorth;
			ats.badObsCriteria.up = badObsCriteriaUp;
			ats.timeInterval.beginTime = timeIntervalBeginTime;
			ats.timeInterval.endTime = timeIntervalEndTime;
			

			System.out.println("Size : " + globalParam.length);
			System.out.println("Size : " + siteParam.length);
			for (int i = 0; i < globalParam.length; i++) {
				switch ((int) globalParam[i][0]) {
				case 1:
				case 2:
				case 3:
					addConstantBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3], true);
					break;
				case 4:
				case 5:
				case 6:
					addVelocityBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4], true);
					break;
				case 7:
				case 8:
				case 9:
					addEpisodicBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4]);
					break;
				case 16:
				case 17:
				case 18:
					addAnnualAmpBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4]);
					break;
				case 19:
				case 20:
				case 21:
					addAnnualPhaseBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4]);
					break;
				case 22:
				case 23:
				case 24:
					addSemiannualAmpBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4]);
					break;
				}
			}

			for (int i = 0; i < siteParam.length; i++) {
				switch ((int) siteParam[i][0]) {
				case 1:
				case 2:
				case 3:
					addConstantBias(ats.myStation, ats.myStationList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3], false);
					break;
				case 4:
				case 5:
				case 6:
					addVelocityBias(ats.myStation, ats.myStationList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3], siteParam[i][4],
							false);
					break;
				case 7:
				case 8:
				case 9:
					addEpisodicBias(ats.myStation, ats.myStationList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3], siteParam[i][4]);
					break;
				case 16:
				case 17:
				case 18:
					addAnnualAmpBias(ats.myStation, ats.myStationList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3],
							siteParam[i][4]);
					break;
				case 19:
				case 20:
				case 21:
					addAnnualPhaseBias(ats.allsites, ats.allsitesList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3],
							siteParam[i][4]);
					break;
				case 22:
				case 23:
				case 24:
					addSemiannualAmpBias(ats.allsites, ats.allsitesList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3],
							siteParam[i][4]);
					break;
				}
			}

			System.out.println("----------------------------------");
			System.out.println("Executing runBlockingAnalyzeTseri ... ");
			String[] returnVals = ats
					.runBlockingAnalyzeTseri(siteCode, dataUrl);

			for (int i = 0; i < returnVals.length; i++) {
				System.out.println(returnVals[i]);
			}
			
			return returnVals;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * A possible web service function
	 */
	public static String[] execATS(String siteCode, String dataUrl,
			double[][] globalParam, double[][] siteParam) {
		AnalyzeTseriService ats;
		try {
			ats = new AnalyzeTseriService(false);
			ats.myStation.setSiteName(siteCode);

			System.out.println("Size : " + globalParam.length);
			System.out.println("Size : " + siteParam.length);
			for (int i = 0; i < globalParam.length; i++) {
				switch ((int) globalParam[i][0]) {
				case 1:
				case 2:
				case 3:
					addConstantBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3], true);
					break;
				case 4:
				case 5:
				case 6:
					addVelocityBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4], true);
					break;
				case 7:
				case 8:
				case 9:
					addEpisodicBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4]);
					break;
				case 16:
				case 17:
				case 18:
					addAnnualAmpBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4]);
					break;
				case 19:
				case 20:
				case 21:
					addAnnualPhaseBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4]);
					break;
				case 22:
				case 23:
				case 24:
					addSemiannualAmpBias(ats.allsites, ats.allsitesList,
							(int) globalParam[i][0], globalParam[i][1],
							globalParam[i][2], globalParam[i][3],
							globalParam[i][4]);
					break;
				}
			}

			for (int i = 0; i < siteParam.length; i++) {
				switch ((int) siteParam[i][0]) {
				case 1:
				case 2:
				case 3:
					addConstantBias(ats.myStation, ats.myStationList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3], false);
					break;
				case 4:
				case 5:
				case 6:
					addVelocityBias(ats.myStation, ats.myStationList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3], siteParam[i][4],
							false);
					break;
				case 7:
				case 8:
				case 9:
					addEpisodicBias(ats.myStation, ats.myStationList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3], siteParam[i][4]);
					break;
				case 16:
				case 17:
				case 18:
					addAnnualAmpBias(ats.myStation, ats.myStationList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3],
							siteParam[i][4]);
					break;
				case 19:
				case 20:
				case 21:
					addAnnualPhaseBias(ats.allsites, ats.allsitesList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3],
							siteParam[i][4]);
					break;
				case 22:
				case 23:
				case 24:
					addSemiannualAmpBias(ats.allsites, ats.allsitesList,
							(int) siteParam[i][0], siteParam[i][1],
							siteParam[i][2], siteParam[i][3],
							siteParam[i][4]);
					break;
				}
			}

			System.out.println("----------------------------------");
			System.out.println("Executing runBlockingAnalyzeTseri ... ");
			String[] returnVals = ats
					.runBlockingAnalyzeTseri(siteCode, dataUrl);

			for (int i = 0; i < returnVals.length; i++) {
				System.out.println(returnVals[i]);
			}
			
			return returnVals;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
	
	public static void debug(String msg) {
		System.out.println("[DEBUG] " + msg);
	}

	public static void debug(String name, String value) {
		System.out.println("[DEBUG] " + name + " = " + value);
	}

}

