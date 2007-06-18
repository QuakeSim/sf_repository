package cgl.webservices.simplex;

//Need to import this parent.
import cgl.webservices.AntVisco;

//Not explicitly naming these because they are famous.
import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.log4j.*;

//Needed to get the ServletContext to read the properties.
import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.w3c.dom.Document;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

//Needed for a unique id
import java.rmi.server.UID;

/**
 * A simple wrapper for Ant.
 */

public class SimpleXService extends AntVisco implements Runnable {
	static Logger logger = Logger.getLogger(SimpleXService.class);

	final String FILE_PROTOCOL = "file";

	final String HTTP_PROTOCOL = "http";

	// These are the system properties that may have
	// default values.
	Properties properties;

	String serverUrl;

	String baseWorkDir;

	String baseDestDir;

	String baseOutputDestDir;

	String projectName;

	String binDir;

	String buildFilePath;

	String antTarget;

	/**
	 * This is a main() for testing.
	 */
	public static void main(String[] args) {
		// Create fault.
		Fault[] faults = new Fault[1];
		faults[0] = new Fault();

		// Create observations.
		Observation[] observations = new Observation[1];
		observations[0] = new Observation();

		String userName = "duhFaultUser";
		String projectName = "faultsatmyfeet";

		try {
			// Make the mesh.
			SimpleXService gfs = new SimpleXService(true);

			String timeStamp = gfs.generateTimeStamp();
			System.out.println("Running blocking version");
			SimpleXOutputBean sxb = gfs.runSimplex(userName, projectName,
					faults, observations, "1", "2","12","23","Url" ,timeStamp);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * The constructor. Set useClassLoader=true when running on the command
	 * line.
	 */
	public SimpleXService(boolean useClassLoader) throws Exception {

		super();

		if (useClassLoader) {
			// System.out.println("Using classloader");
			// This is useful for command line clients but does not work
			// inside Tomcat.
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			properties = new Properties();

			// This works if you are using the classloader but not inside
			// Tomcat.
			properties.load(loader
					.getResourceAsStream("simplexconfig.properties"));
		} else {
			// Extract the Servlet Context
			// System.out.println("Using Servlet Context");
			MessageContext msgC = MessageContext.getCurrentContext();
			ServletContext context = ((HttpServlet) msgC
					.getProperty(HTTPConstants.MC_HTTP_SERVLET))
					.getServletContext();

			String propertyFile = context.getRealPath("/")
					+ "/WEB-INF/classes/simplexconfig.properties";
			// System.out.println("Prop file location " + propertyFile);

			properties = new Properties();
			properties.load(new FileInputStream(propertyFile));
		}

		// Note these will be "global" for this class, so
		// I will not explicitly pass them around.
		serverUrl = properties.getProperty("simplex.service.url");
		baseWorkDir = properties.getProperty("base.workdir");
		baseDestDir = properties.getProperty("base.dest.dir");
		projectName = properties.getProperty("project.name");
		binDir = properties.getProperty("bin.path");
		buildFilePath = properties.getProperty("build.file.path");
		antTarget = properties.getProperty("ant.target");
		baseOutputDestDir = properties.getProperty("output.dest.dir");
	}

	public SimpleXService() throws Exception {
		this(false);
	}

	public String runMakeMapXml(String userName, String projectName,
			String origin_lat, String origin_lon, String jobUIDStamp)
			throws Exception {

		String baseUrl = generateBaseUrl(userName, projectName, jobUIDStamp);
		String destDir = generateOutputDestDir(userName, projectName,
				jobUIDStamp);
		String workDir = generateWorkDir(userName, projectName, jobUIDStamp);
		makeWorkDir(destDir);

		String outputfilename = workDir + "/" + projectName + ".output";
		String xmlfilename = destDir + "/" + projectName + jobUIDStamp + ".xml";
		GmapDataXml dw = new GmapDataXml();
		dw.setOriginal_lat(origin_lat);
		dw.setOriginal_lon(origin_lon);
		dw.LoadDataFromFile(outputfilename);
		dw.CalculateMarker();
		Document doc = dw.makeDoc();
		dw.printToConsole(doc);
		dw.printToFile(doc, xmlfilename);

		String mapxmlUrl = baseUrl + "/" + projectName + jobUIDStamp + ".xml";
		return mapxmlUrl;
	}

	/*
	 * bool to string :false to 0, true to 1.
	 */
	public String booltoStr(boolean tmp) {
		String boolean_value = "0";
		if (tmp)
			boolean_value = "1";
		else
			boolean_value = "0";
		return boolean_value;
	}

	/*
	 * create input file for Simplex
	 */
	protected void SimplexCreateInputFile(String workDir, String projectName,
			Fault[] faults, Observation[] obsv, String startTemp,
			String maxIters) {
		try {
			makeWorkDir(workDir);
			// --------------------------------------------------
			// Set up and create the input file.
			// --------------------------------------------------
			String inputFile = projectName + ".input";
			PrintWriter pw = new PrintWriter(new FileWriter(workDir + "/"
					+ inputFile), true);

			// --------------------------------------------------
			// First, print the faults.
			// --------------------------------------------------
			pw.println(faults.length);
			pw.println("");

			String space = "\t";

			for (int i = 0; i < faults.length; i++) {
				// 1 faultOriginX
				pw.print(1);
				pw.print(space);
				pw.print(booltoStr(faults[i].faultOriginXVary));
				pw.print(space);
				pw.print(faults[i].faultLocationX);
				pw.print("\n");
				// 2 faultOriginY
				pw.print(2);
				pw.print(space);
				pw.print(booltoStr(faults[i].faultOriginYVary));
				pw.print(space);
				pw.print(faults[i].faultLocationY);
				pw.print("\n");
				// 3 faultStrikeAngle
				pw.print(3);
				pw.print(space);
				pw.print(booltoStr(faults[i].faultStrikeAngleVary));
				pw.print(space);
				pw.print(faults[i].faultStrikeAngle);
				pw.print("\n");
				// 4 faultDipAngle
				pw.print(4);
				pw.print(space);
				pw.print(booltoStr(faults[i].faultDipAngleVary));
				pw.print(space);
				pw.print(faults[i].faultDipAngle);
				pw.print("\n");
				// 5 faultDepth
				pw.print(5);
				pw.print(space);
				pw.print(booltoStr(faults[i].faultDepthVary));
				pw.print(space);
				pw.print(faults[i].faultDepth);
				pw.print("\n");
				// 6 faultWidth
				pw.print(6);
				pw.print(space);
				pw.print(booltoStr(faults[i].faultWidthVary));
				pw.print(space);
				pw.print(faults[i].faultWidth);
				pw.print("\n");
				// 7 faultLength
				pw.print(7);
				pw.print(space);
				pw.print(booltoStr(faults[i].faultLengthVary));
				pw.print(space);
				pw.print(faults[i].faultLength);
				pw.print("\n");
				// 8 faultStrikeSlip
				pw.print(8);
				pw.print(space);
				pw.print(booltoStr(faults[i].faultStrikeSlipVary));
				pw.print(space);
				pw.print(faults[i].faultRakeAngle);
				pw.print("\n");
				// 9 faultDipSlip
				pw.print(9);
				pw.print(space);
				pw.print(booltoStr(faults[i].faultDipSlipVary));
				pw.print(space);
				pw.print(faults[i].faultSlip);
				pw.print("\n");

				pw.println("");
			}

			// --------------------------------------------------
			// Now do the observation points.
			// --------------------------------------------------
			pw.println(obsv.length);
			pw.print(startTemp);
			pw.print(space);
			pw.print(maxIters);
			pw.print(space);
			pw.print("10"); // This property is obsolete.
			pw.print("\n");

			for (int i = 0; i < obsv.length; i++) {
				// 1 obsvRefSite * obsvType
				int refint = Integer.parseInt(obsv[i].obsvRefSite);
				int obsvType = Integer.parseInt(obsv[i].obsvType);
				pw.print(refint * obsvType);
				pw.print(space);
				// 2 obsvLocationEast
				pw.print(obsv[i].obsvLocationEast);
				pw.print(space);
				// 3 obsvLocationNorth
				pw.print(obsv[i].obsvLocationNorth);
				pw.print(space);
				// 4 obsvValue
				pw.print(obsv[i].obsvValue);
				pw.print(space);
				// 5 obsvError
				pw.print(obsv[i].obsvError);
				pw.print(space);

				pw.print("\n");
			}
			pw.close();
		} // End of the try

		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Set up the arg array. Note that the binDir and buildFilePath variables
	 * are constants, so not explicitly passed in.
	 */
	protected String[] setUpSimpleXArgs(String workDir, String projectName) {
		String[] args = new String[6];
		args[0] = "-DworkDir.prop=" + workDir;
		args[1] = "-DprojectName.prop=" + projectName;
		args[2] = "-Dbindir.prop=" + binDir;
		args[3] = "-buildfile";
		args[4] = buildFilePath;
		args[5] = "RunSimplex";
		return args;
	}

	/*
	 * pre simplex function
	 */

	protected String[] prefabSimpleXCall(String userName, String projectName,
			Fault[] faults, Observation[] obsv, String startTemp,
			String maxIters, String timeStamp) throws Exception {

		String workDir = generateWorkDir(userName, projectName, timeStamp);
		SimplexCreateInputFile(workDir, projectName, faults, obsv, startTemp,
				maxIters);
		String outputDestDir = generateOutputDestDir(userName, projectName,
				timeStamp);
		String[] args = setUpSimpleXArgs(workDir, projectName);
		return args;
	}

	/**
	 * Actually runs Simplex. Always runs in non-blocking mode.
	 * 
	 * Returns the timestamp as String[0]. The rest are URLs of various output
	 * files. String[1] is the Simplex input file. String[2] is the Simplex
	 * output file. String [3] is the standard output of geofest. String [4] is
	 * the simplex log file.
	 */
	public SimpleXOutputBean runSimplex(String userName, String projectName,
			Fault[] faults, Observation[] obsv, String startTemp,
			String maxIters, String origin_lon, String origin_lat, String KmlGeneratorUrl , String timeStamp) throws Exception {

		// The target is always "tar.all".
		String[] args = prefabSimpleXCall(userName, projectName, faults, obsv,
				startTemp, maxIters, timeStamp);
		setArgs(args);
		run();
		return getAllTheSimpleXFiles(KmlGeneratorUrl, userName, projectName,origin_lon,origin_lat,faults ,timeStamp);
	}

	protected SimpleXOutputBean getAllTheSimpleXFiles(String KmlGeneratorServiceUrl,String userName,
			String projectName,String lon, String lat,Fault[] faults ,String jobUIDStamp) {

		SimpleXOutputBean sxoutput = new SimpleXOutputBean();
		String baseUrl = generateBaseUrl(userName, projectName, jobUIDStamp);

		// copy file to dest dir
		String destDir = generateOutputDestDir(userName, projectName,
				jobUIDStamp);
		String workDir = generateWorkDir(userName, projectName, jobUIDStamp);
		try {
			makeWorkDir(destDir);
			copyFileToFile(new File(workDir + "/" + projectName + ".input"),
					new File(destDir + "/" + projectName + ".input"));
			copyFileToFile(new File(workDir + "/" + projectName + ".output"),
					new File(destDir + "/" + projectName + ".output"));
			copyFileToFile(new File(workDir + "/" + projectName + ".stdout"),
					new File(destDir + "/" + projectName + ".stdout"));
			copyFileToFile(new File(workDir + "/" + projectName + ".fault"),
					new File(destDir + "/" + projectName + ".fault"));
		} // End of the try

		catch (Exception ex) {
			ex.printStackTrace();
		}
		String[] kmlurls=new String[4];

		sxoutput.setProjectName(projectName);
		sxoutput.setJobUIDStamp(jobUIDStamp);
		sxoutput.setInputUrl(baseUrl + "/" + projectName + ".input");
		sxoutput.setOutputUrl(baseUrl + "/" + projectName + ".output");
		sxoutput.setLogUrl(baseUrl + "/" + projectName + ".stdout");
		sxoutput.setFaultUrl(baseUrl + "/" + projectName + ".fault");

		// System.out.println(sxoutput.getProjectName());
		// System.out.println(sxoutput.getJobUIDStamp());
		// System.out.println(sxoutput.getInputUrl());
		// System.out.println(sxoutput.getOutputUrl());
		// System.out.println(sxoutput.getLogUrl());
		// System.out.println(sxoutput.getFaultUrl());
		try {
		String outputfilename = workDir + "/" + projectName + ".output";
		GmapDataXml dw = new GmapDataXml();
		dw.LoadDataFromFile(outputfilename);
		
		// get observ kml
		SimpleXDataKml kmlService;
		SimpleXDataKmlServiceLocator locator = new SimpleXDataKmlServiceLocator();
	    locator.setMaintainSession(true);
		kmlService = locator.getKmlGenerator(new URL(KmlGeneratorServiceUrl));
		
		PointEntry[] tmp_pointentrylist=dw.getObservList();

		kmlService.setDatalist( tmp_pointentrylist );
		kmlService.setOriginalCoordinate(lon, lat);
		kmlService.setCoordinateUnit("1000");
		kmlService=setfaultplot(kmlService,faults);
		kmlService.setPointPlacemark("Icon Layer");
		kmlService.setArrowPlacemark("Arrow Layer", "aaeeffcc", 2);
		
		String observKmlUrl=kmlService.runMakeKml("", userName, projectName, "observ");
		System.out.println(observKmlUrl);		
		//get calc kml
		locator = new SimpleXDataKmlServiceLocator();
	    locator.setMaintainSession(true);
		kmlService = locator.getKmlGenerator(new URL(KmlGeneratorServiceUrl));
		tmp_pointentrylist=dw.getCalcList();
		kmlService.setDatalist( tmp_pointentrylist );
		kmlService.setOriginalCoordinate(lon, lat);
		kmlService.setCoordinateUnit("1000");
		//kmlService=setfaultplot(kmlService,faults);
		kmlService.setPointPlacemark("Icon Layer");
		kmlService.setArrowPlacemark("Arrow Layer", "aaeeffcc", 2);
		String calcKmlUrl=kmlService.runMakeKml("", userName, projectName, "calc");

		//get o_c kml
		locator = new SimpleXDataKmlServiceLocator();
	    locator.setMaintainSession(true);
		kmlService = locator.getKmlGenerator(new URL(KmlGeneratorServiceUrl));
		tmp_pointentrylist=dw.getO_cList();
		kmlService.setDatalist( tmp_pointentrylist );
		kmlService.setOriginalCoordinate(lon, lat);
		kmlService.setCoordinateUnit("1000");
		//kmlService=setfaultplot(kmlService,faults);
		kmlService.setPointPlacemark("Icon Layer");
		kmlService.setArrowPlacemark("Arrow Layer", "aaeeffcc", 2);
		String o_cKmlUrl=kmlService.runMakeKml("", userName, projectName, "o_c");
		
		// get total kml
		locator = new SimpleXDataKmlServiceLocator();
	    locator.setMaintainSession(true);
		kmlService = locator.getKmlGenerator(new URL(KmlGeneratorServiceUrl));
		tmp_pointentrylist=dw.getO_cList();
		kmlService.setDatalist( tmp_pointentrylist );
		kmlService.setOriginalCoordinate(lon, lat);
		kmlService.setCoordinateUnit("1000");
		//kmlService=setfaultplot(kmlService,faults);
		kmlService.setPointPlacemark("o_c Icon Layer");
		kmlService.setArrowPlacemark("o_c Arrow Layer", "aaeeffcc", 2);
		tmp_pointentrylist=dw.getCalcList();
		kmlService.setDatalist( tmp_pointentrylist );
		kmlService.setPointPlacemark("calc Icon Layer");
		kmlService.setArrowPlacemark("calc Arrow Layer", "aaeeffcc", 2);
		tmp_pointentrylist=dw.getObservList();
		kmlService.setDatalist( tmp_pointentrylist );
		kmlService.setPointPlacemark("observ Icon Layer");
		kmlService.setArrowPlacemark("observ Arrow Layer", "aaeeffcc", 2);
		String totalKmlUrl=kmlService.runMakeKml("", userName, projectName, "");
		
		
		kmlurls[0]=totalKmlUrl;
		kmlurls[1]=observKmlUrl;
		kmlurls[2]=calcKmlUrl;
		kmlurls[3]=o_cKmlUrl;
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sxoutput.setKmlUrls(kmlurls);
		
		
		return sxoutput;
	}
	
	private SimpleXDataKml setfaultplot(SimpleXDataKml kmlserv, Fault[] fts) {
	
		for (int i = 0; i < fts.length; i++) {
			try {
				
				System.out.println("***********************************************");
				System.out.println(fts[i].getFaultLonStarts());
				System.out.println(fts[i].getFaultLatStarts());
				System.out.println(fts[i].getFaultLonEnds());
				System.out.println(fts[i].getFaultLatEnds());
							
				
				if ( !(fts[i].getFaultLonStarts()).equals("") && !(fts[i].getFaultLatStarts()).equals("") && !(fts[i].getFaultLonEnds()).equals("") && !(fts[i].getFaultLatEnds()).equals("") ) {
					System.out.println("###################################################");
					kmlserv.setFaultPlot("Fault list", fts[i].getFaultName(), fts[i].getFaultLonStarts(),fts[i].getFaultLatStarts(), fts[i].getFaultLonEnds(), fts[i].getFaultLatEnds(), "red", 4);
				}
			}catch ( Exception e) {
				e.printStackTrace();
			}
		}
		return kmlserv;
	}

	protected String[] setPrePlotGMTPlotArgs(String workDir,
			String projectName, String origin_lat, String origin_lon,
			String timeStamp) {
		String[] args = new String[9];
		args[0] = "-DworkDir.prop=" + workDir;
		args[1] = "-DprojectName.prop=" + projectName;
		args[2] = "-Dbindir.prop=" + binDir;
		args[3] = "-Dorigin_lat.prop=" + origin_lat;
		args[4] = "-Dorigin_lon.prop=" + origin_lon;
		args[5] = "-DbaseImageName.prop=" + projectName + timeStamp;
		args[6] = "-buildfile";
		args[7] = buildFilePath;
		args[8] = "RunPrePlotGMT";
	
		return args;
	}

	/*
	 * pre PlotGMT function
	 */

	protected String prefabPlotGMTCall(String projectName, String userName,
			String origin_lat, String origin_lon, String timeStamp) {
		String workDir = generateWorkDir(userName, projectName, timeStamp);
		String outputDestDir = generateOutputDestDir(userName, projectName,
				timeStamp);
		try {
			if (origin_lon == null || origin_lon.equals("null"))
				origin_lon = "";
			if (origin_lat == null || origin_lat.equals("null"))
				origin_lat = "";

			String[] args = setPrePlotGMTPlotArgs(workDir, projectName,
					origin_lat, origin_lon, timeStamp);
			setArgs(args);
			run();

		} catch (Exception ex) {
			System.out.println("prefabPlotGMTCall failed");
			ex.printStackTrace();
		}
		return workDir + "/" + projectName + timeStamp + ".properties";

	}

	public void processPropsFile(String propfile, GMTViewForm currentGMTViewForm) {
		try {
			BufferedReader buf = new BufferedReader(new FileReader(propfile));
			String line = buf.readLine();
			StringTokenizer st;
			while (line != null) {
				st = new StringTokenizer(line, "=");
				String name = st.nextToken();
				String val = st.nextToken();
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

	/**
	 * Actually runs PlotGMT. Always runs in non-blocking mode. return pdf Url.
	 */
	public GMTViewForm runPlotGMT(String userName, String projectName,
			String origin_lat, String origin_lon, String timeStamp) {
		String locfilename = prefabPlotGMTCall(projectName, userName,
				origin_lat, origin_lon, timeStamp);
		GMTViewForm myGMTViewForm = new GMTViewForm();
		myGMTViewForm.reset();
		processPropsFile(locfilename, myGMTViewForm);
		String pdfurl = runRePlotGMT(userName, projectName, myGMTViewForm,
				timeStamp);
		myGMTViewForm.setGmtPlotPdfUrl(pdfurl);
		return myGMTViewForm;
	}

	protected String[] setPreRePlotGMTPlotArgs(String projectName,
			String workDir, String bf_loc, GMTViewForm currentGMTViewForm,
			String timeStamp) {
		String[] args = new String[14];
		args[0] = "-DworkDir_prop=" + workDir;
		args[1] = "-Doutfile=" + projectName + timeStamp;
		args[2] = setValue(currentGMTViewForm.area_prop, "area_prop", true);
		args[3] = setValue(currentGMTViewForm.scale_prop, "scale_prop", true);
		args[4] = setValue(currentGMTViewForm.mapticks_prop, "mapticks_prop",
				true);
		args[5] = setValue(currentGMTViewForm.vectormag_prop, "vectormag_prop",
				true);
		args[6] = setValue(currentGMTViewForm.plot_background,
				"plot_background", false);
		args[7] = setValue(currentGMTViewForm.plot_causative, "plot_causative",
				false);
		args[8] = setValue(currentGMTViewForm.plot_obs, "plot_obs", false);
		args[9] = setValue(currentGMTViewForm.plot_calc, "plot_calc", false);
		args[10] = setValue(currentGMTViewForm.plot_resid, "plot_resid", false);
		args[11] = "-buildfile";
		args[12] = bf_loc;
		args[13] = "makepdf";

		return args;
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

	/**
	 * Actually runs RePlotGMT. Always runs in blocking mode. return pdf
	 * Url.
	 */

	public String runRePlotGMT(String userName, String projectName,
			GMTViewForm currentGMTViewForm, String jobUIDStamp) {
		String baseUrl = generateBaseUrl(userName, projectName, jobUIDStamp);

		// copy file to dest dir
		String destDir = generateOutputDestDir(userName, projectName,
				jobUIDStamp);
		String workDir = generateWorkDir(userName, projectName, jobUIDStamp);
		try {

			String bf_loc = binDir + "/" + "build-gmt.xml";

			String[] args = setPreRePlotGMTPlotArgs(projectName, workDir,
					bf_loc, currentGMTViewForm, jobUIDStamp);
			setArgs(args);
			run();
			// Get the generated image.
			makeWorkDir(destDir);
			copyFileToFile(new File(workDir + "/" + projectName + jobUIDStamp
					+ ".pdf"), new File(destDir + "/" + projectName
					+ jobUIDStamp + ".pdf"));
		} catch (Exception ex) {
			System.out.println("Image not available");
			ex.printStackTrace();
		}
		String gmtPlotPdfUrl = baseUrl + "/" + projectName + jobUIDStamp
				+ ".pdf";
		return gmtPlotPdfUrl;
	}

	/**
	 * 
	 */
	protected String generateOutputDestDir(String userName, String projectName,
			String timeStamp) {

		String outputDestDir = baseOutputDestDir + File.separator + userName
				+ File.separator + projectName + File.separator + timeStamp;

		return outputDestDir;

	}

	/**
	 * 
	 */
	protected String generateWorkDir(String userName, String projectName,
			String timeStamp) {

		String workDir = baseWorkDir + File.separator + userName
				+ File.separator + projectName + File.separator + timeStamp;

		return workDir;

	}

	protected String generateBaseUrl(String userName, String projectName,
			String timeStamp) {

		// Need to be careful here because this must follow
		// the workDir convention also.
		String baseUrl = serverUrl + "/" + userName + "/" + projectName + "/"
				+ "/" + timeStamp;

		return baseUrl;
	}

	/**
	 * This merges multiple files into a single file, duplicating UNIX paste.
	 */
	public void mergeInputFiles(String[] inputFileArray, String mergedFileName) {

		// Find the shortest of the input files.
		int shortCount = Integer.MAX_VALUE;
		System.out.println("Max integer Value=" + shortCount);

		for (int i = 0; i < inputFileArray.length; i++) {
			int lineCount = getLineCount(inputFileArray[i]);
			if (lineCount < shortCount)
				shortCount = lineCount;
		}
		System.out.println("Shortest file length=" + shortCount);

		// Now do the thing.
		try {
			// This is our output file.
			PrintWriter pw = new PrintWriter(new FileWriter(mergedFileName),
					true);

			// Set up bufferedreader array
			BufferedReader[] br = new BufferedReader[inputFileArray.length];
			for (int i = 0; i < br.length; i++) {
				br[i] = new BufferedReader(new FileReader(inputFileArray[i]));
			}

			// Loop over each line of the file
			for (int i = 0; i < shortCount; i++) {
				String line = "";
				for (int j = 0; j < br.length; j++) {
					line += br[j].readLine();
				}
				pw.println(line);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * This is a helper method to convert token-separated inputFileUrlStrings
	 * into arrays.
	 */
	private String[] convertInputUrlStringToArray(String inputFileUrlString) {
		inputFileUrlString.trim();

		String[] returnArray;

		StringTokenizer st = new StringTokenizer(inputFileUrlString);
		int arrayDim = st.countTokens();
		if (arrayDim < 2) {
			returnArray = new String[1];
			returnArray[0] = inputFileUrlString.trim();
		} else {
			int i = 0;
			returnArray = new String[arrayDim];
			while (st.hasMoreTokens()) {
				returnArray[i] = st.nextToken();
				i++;
			}
		}
		return returnArray;
	}

	/**
	 * This helper method assumes input is a multlined String of tabbed columns.
	 * It cuts out the number of columns on the left specified by cutLeftColumns
	 * and number on the right by cutRightColumns.
	 * 
	 * This method can accepted either single-valued or multiple valued entries.
	 */
	protected String[] filterResults(String[] tabbedFile, int cutLeftColumns,
			int cutRightColumns) throws Exception {
		String[] filteredFileArray = new String[tabbedFile.length];
		String space = " ";
		StringTokenizer st;
		for (int i = 0; i < tabbedFile.length; i++) {
			filteredFileArray[i] = tabbedFile[i] + ".filtered";
			BufferedReader br = new BufferedReader(
					new FileReader(tabbedFile[i]));
			PrintWriter printer = new PrintWriter(new FileWriter(
					filteredFileArray[i]), true);
			String line = br.readLine();
			while (line != null) {
				// System.out.println(line);
				st = new StringTokenizer(line);
				String newLine = "";
				int tokenCount = st.countTokens();
				for (int j = 0; j < tokenCount; j++) {
					String temp = st.nextToken();
					if (j >= cutLeftColumns
							&& j < (tokenCount - cutRightColumns)) {
						newLine += temp + space;
					}
				}
				// System.out.println(newLine);
				printer.println(newLine);
				line = br.readLine();
			}
		}
		return filteredFileArray;
	}

	private void makeWorkDir(String workDir) throws Exception {

		System.out.println("Working Directory is " + workDir);
		new File(workDir).mkdirs();
		/* using ant to make a dir is not good idea */
		// String[] args0 = new String[4];
		// args0[0] = "-Dworkdir.prop=" + workDir;
		// args0[1] = "-buildfile";
		// args0[2] = buildFilePath;
		// args0[3] = "MakeWorkDir";
		// setArgs(args0);
		// run();
	}

	private String extractSimpleName(String extendedName) {
		return (new File(extendedName)).getName();
	}

	/**
	 * Note that inputFileUrlString can be either single values or else have
	 * multiple, space separated values. It also returns a space-separated set
	 * of values. All files are written to the same directory.
	 */
	private String[] downloadInputFile(String[] inputFileUrlString,
			String inputFileDestDir) throws Exception {

		// Convert to a URL. This will throw an exception if
		// malformed.

		String[] fileLocalFullName = new String[inputFileUrlString.length];
		for (int i = 0; i < inputFileUrlString.length; i++) {

			URL inputFileUrl = new URL(inputFileUrlString[i]);

			String protocol = inputFileUrl.getProtocol();
			System.out.println("Protocol: " + protocol);
			String fileSimpleName = extractSimpleName(inputFileUrl.getFile());
			System.out.println(fileSimpleName);

			fileLocalFullName[i] = inputFileDestDir + File.separator
					+ fileSimpleName;

			if (protocol.equals(FILE_PROTOCOL)) {
				String filePath = inputFileUrl.getFile();
				fileSimpleName = inputFileUrl.getFile();

				System.out.println("File path is " + filePath);
				File filePathObject = new File(filePath);
				File destFileObject = new File(fileLocalFullName[i]);

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
				copyUrlToFile(inputFileUrl, fileLocalFullName[i]);
			}

			else {
				System.out.println("Unknown protocol for accessing inputfile");
				throw new Exception("Unknown protocol");
			}
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

	// --------------------------------------------------
	// Find the first non-blank line and count columns.
	// Note this can screw up if input file is not
	// formated correctly, but then GeoFEST itself
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
	// --------------------------------------------------
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
	 * A dumb little method for constructing the URL outputs. This will not get
	 * called if the execute()/run() method fails.
	 */
	protected String[] getTheReturnFiles() {

		String[] extensions = { ".input", ".range", ".Q", ".pi", ".A",
				".minval", ".maxval", ".L", ".B", ".Q", ".stdout",
				".input.X.png", ".input.Y.png", ".input.Z.png" };

		String[] returnFiles = new String[extensions.length];
		for (int i = 0; i < extensions.length; i++) {
			returnFiles[i] = serverUrl + "/" + projectName + "/" + projectName
					+ extensions[i];
		}

		return returnFiles;
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

	// --------------------------------------------------
	// Substitutes underscores for blanks in the
	// provided string.
	// --------------------------------------------------
	protected String removeSpaces(String spacyString) {
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

}
