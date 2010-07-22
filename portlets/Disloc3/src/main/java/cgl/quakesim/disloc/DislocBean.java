package cgl.quakesim.disloc;

//Imports from the mother ship
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;
import java.text.*;

import javax.faces.model.SelectItem;
import javax.faces.component.html.HtmlDataTable;

import javax.faces.event.*;

import javax.faces.event.ActionEvent;

import org.servogrid.genericproject.GenericSopacBean;
import org.servogrid.genericproject.FaultDBEntry;

//import sun.misc.BASE64Encoder;

import TestClient.Select.Select;
import TestClient.Select.SelectService;
import TestClient.Select.SelectServiceLocator;



import com.db4o.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * Everything you need to set up and run MeshGenerator.
 */

public class DislocBean extends GenericSopacBean {

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

	// Some navigation strings.
	static final String DEFAULT_USER_NAME = "disloc_default_user";
	static final String DISLOC_NAV_STRING = "disloc-submitted";
	static final String SEPARATOR = "/";

	/**
	 * The following are property fields. Associated get/set methods are at the
	 * end of the code listing.
	 */
	boolean renderCreateNewFaultForm = false;
	boolean renderAddFaultSelectionForm = false;
	boolean renderDislocGridParamsForm = false;
	boolean renderSearchByFaultNameForm = false;
	boolean renderSearchByAuthorForm = false;
	boolean renderSearchByLatLonForm = false;
	boolean renderViewAllFaultsForm = false;
	boolean renderAddFaultFromDBForm = false;
	boolean renderMap = false;
	boolean renderFaultMap = false;
	boolean usesGridPoints;
	boolean renderChooseObsvStyleForm = false;

	Fault currentFault = new Fault();
	// DislocParamsBean dislocParams=new DislocParamsBean();
	DislocParamsBean currentParams = new DislocParamsBean();
	DislocProjectBean currentProject = new DislocProjectBean();
	DislocProjectSummaryBean currentSummary = new DislocProjectSummaryBean();
	String faultSelectionCode = "";
	String projectSelectionCode = "";
	String obsvStyleSelectionCode = "";

	// These are search parameter strings.
	String forSearchStr = "";
	
	boolean faultdrawing = false;
	
	public boolean getFaultdrawing() {
		return faultdrawing;
	}

	public void setFaultdrawing(boolean faultdrawing) {
		this.faultdrawing = faultdrawing;
	}
	
	 String faultName = "newfault";
	 
	 public String getFaultName() {
		return faultName;
	}
	public void setFaultName(String faultName) {
		this.faultName = faultName;
	}
	
	
	int myFaultsForProjectListsize;
	public int getMyFaultsForProjectListsize() {
		return myFaultsForProjectList.size();
	}

	public void setMyFaultsForProjectListsize(int myFaultsForProjectListsize) {
		this.myFaultsForProjectListsize = myFaultsForProjectListsize;
	}
	
	String faultLatStart = new String();
	String faultLatEnd = new String();
	String faultLonStart = new String();
	String faultLonEnd = new String();
	String jobToken = "";

	// These are useful object lists.
	String[] selectProjectsArray;
	String[] selectdbProjectsArray;
	String[] deleteProjectsArray;
	String[] copyProjectsArray;

	List myProjectNameList = new ArrayList();	
	HashMap dbProjectNameList = new HashMap();
	
	List myFaultCollection = new ArrayList();
	List myDislocParamsCollection = new ArrayList();
	List myFaultDBEntryList = new ArrayList();
	List myFaultEntryForProjectList = new ArrayList();
	List myFaultsForProjectList = new ArrayList();
	
	List myObservationsForProjectList = new ArrayList();
	List myObsvEntryForProjectList = new ArrayList();
	List myArchivedDislocResultsList = new ArrayList();
	List myPointObservationList = new ArrayList();
	List myInterpIdList = new ArrayList();
	List myInsarParamsList = new ArrayList();

	HtmlDataTable myFaultDataTable, myProjectSummaryDataTable;
	HtmlDataTable myScatterPointsTable, myInsarDataTable;

	// Create the database
	

	// Service information
	DislocService dislocService;
	DislocExtendedService dislocExtendedService;

	String dislocServiceUrl;
	String dislocExtendedServiceUrl;
	String faultDBServiceUrl;
	String kmlGeneratorBaseurl;
	String kmlGeneratorUrl;
	String insarkmlServiceUrl;

	// These are used for the InSAR KML service
	String insarKmlUrl;
	String elevation = "60";
	String azimuth = "0";
	String frequency = "1.26";

	String realPath;
	String codeName;
	String kmlProjectFile = "network0.kml";
	String kmlfiles = "";
	
	String projectsource = "";
	String projectname = "";

	double originLon, originLat;

	String faultKmlUrl;
	String portalBaseUrl;
	String faultKmlFilename;
	String obsvKmlUrl;
	String obsvKmlFilename;

	String gpsStationName = "";
	String gpsStationLat = "";
	String gpsStationLon = "";
	DecimalFormat df;
	FaultDBEntry faultDBEntry;

	/**
	 * The client constructor.
	 */
	public DislocBean() throws Exception {
		super();
		faultDBEntry = new FaultDBEntry();
		df = new DecimalFormat(".###");
		// currentParams.setObservationPointStyle(1);

		// We are done.
		
		System.out.println("[" + getUserName() +"/DislocBean/DislocBean] Created");
	}

	// --------------------------------------------------
	// This section contains the main execution calls.
	// --------------------------------------------------

	public void runTestCall() throws Exception {
		ObsvPoint[] testBean = new ObsvPoint[3];
		for (int i = 0; i < testBean.length; i++) {
			testBean[i] = new ObsvPoint();
			testBean[i].setLatPoint(10.0 + i + "");
			testBean[i].setLonPoint(20.0 + i + "");
			testBean[i].setXcartPoint(30.0 + i + "");
			testBean[i].setYcartPoint(40.0 + i + "");
		}

		initDislocExtendedService();
		dislocExtendedService.testCall(testBean);
	}

	/**
	 * Protected convenience method.
	 */

	protected void initDislocService() throws Exception {
		dislocService = new DislocServiceServiceLocator()
				.getDislocExec(new URL(dislocServiceUrl));
		System.out.println("[" + getUserName() +"/DislocBean/initDislocService] Binding to: " + dislocServiceUrl);
	}

	protected void initDislocExtendedService() throws Exception {
		dislocExtendedService = new DislocExtendedServiceServiceLocator()
				.getDislocExtendedExec(new URL(dislocExtendedServiceUrl));
		System.out.println("[" + getUserName() +"/DislocBean/initDislocService] Binding to: " + dislocExtendedServiceUrl);
	}

	protected void makeProjectDirectory() {
		File projectDir = new File(getBasePath() + "/" + getContextBasePath()
				+ "/" + userName + "/" + codeName + "/");
		projectDir.mkdirs();
	}

	/**
	 * This grabs the faults from the DB.
	 */
	protected Fault[] getFaultsFromDB() {
		Fault[] returnFaults = null;
		ObjectContainer db = null;

		try {			
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");
			// Fault faultToGet=new Fault();
			// ObjectSet results=db.get(faultToGet);
			Fault faultToGet;
			ObjectSet results = db.get(Fault.class);
			if (results.hasNext()) {
				returnFaults = new Fault[results.size()];
				for (int i = 0; i < results.size(); i++) {
					returnFaults[i] = (Fault) results.next();
				}
			}
			
		} catch (Exception e) {			
			System.out.println("[" + getUserName() +"/DislocBean/getFaultsFromDB] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return returnFaults;
	}

	/**
	 * Grabs the scatter points from the DB. This may be null.
	 */
	protected ObsvPoint[] getObsvPointsFromDB() {
		ObsvPoint[] returnPoints = null;
		ObjectContainer db = null;
		
		try {			
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			ObjectSet results = db.get(ObsvPoint.class);
			if (results.hasNext()) {
				returnPoints = new ObsvPoint[results.size()];
				for (int i = 0; i < results.size(); i++) {
					returnPoints[i] = (ObsvPoint) results.next();
				}
			}			
		} catch (Exception e) {			
			System.out.println("[" + getUserName() +"/DislocBean/getObsvPointsFromDB] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
		return returnPoints;
	}

	/**
	 * Note this method assumes projectName has been set externally. It will
	 * return a new object if it can't locate one in the db.
	 */
	protected DislocParamsBean getDislocParamsFromDB(String projectName) {
		// Create an empty params. We will use this if we can't find one
		// in the DB. Set the origin just in case.
		DislocParamsBean paramsBean = new DislocParamsBean();
		paramsBean.setOriginLat(DislocParamsBean.DEFAULT_LAT);
		paramsBean.setOriginLon(DislocParamsBean.DEFAULT_LON);
		ObjectContainer db = null;

		try {			
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");
			ObjectSet results = db.get(DislocParamsBean.class);
			System.out.println("[" + getUserName() +"/DislocBean/getDislocParamsFromDB] Getting params from db:" + results.size());
			if (results.hasNext()) {
				paramsBean = (DislocParamsBean) results.next();
			}
			System.out.println("[" + getUserName() +"/DislocBean/getDislocParamsFromDB] Project Origin:" + paramsBean.getOriginLat()
					+ " " + paramsBean.getOriginLon());
			
		} catch (Exception e) {			
			System.out.println("[" + getUserName() +"/DislocBean/getDislocParamsFromDB] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
		return paramsBean;
	}

	protected void storeProjectInContext(String userName, String projectName,
			String jobUIDStamp, DislocParamsBean paramsBean,
			DislocResultsBean dislocResultsBean, String kml_url,
			String insarKmlUrl, String elevation, String azimuth,
			String frequency) throws Exception {

		DislocProjectSummaryBean summaryBean = new DislocProjectSummaryBean();
		summaryBean.setUserName(userName);
		summaryBean.setProjectName(projectName);
		summaryBean.setJobUIDStamp(jobUIDStamp);
		summaryBean.setParamsBean(paramsBean);
		summaryBean.setResultsBean(dislocResultsBean);
		summaryBean.setCreationDate(new Date().toString());
		summaryBean.setKmlurl(kml_url);
		// summaryBean.setInsarKmlUrl(insarKmlUrl);
		// summaryBean.setElevation(elevation);
		// summaryBean.setAzimuth(azimuth);
		// summaryBean.setFrequency(frequency);

		InsarParamsBean ipb = new InsarParamsBean();
		ipb.setUserName(userName);
		ipb.setProjectName(projectName);
		ipb.setJobUIDStamp(jobUIDStamp);
		ipb.setCreationDate(new Date().toString());
		ipb.setInsarKmlUrl(insarKmlUrl);
		ipb.setElevation(elevation);
		ipb.setAzimuth(azimuth);
		ipb.setFrequency(frequency);
		ipb.setDislocOutputUrl(dislocResultsBean.getOutputFileUrl());

		// Store the summary and insar params beans.
		ObjectContainer db = null;
		try {		
			
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + ".db");
			db.set(summaryBean);
			db.set(ipb);

			// Say goodbye.
			db.commit();
			

			// Store the params bean for the current project,
			// deleting any old one as necessary.

			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			ObjectSet result = db.get(DislocParamsBean.class);
			if (result.hasNext()) {
				DislocParamsBean tmp = (DislocParamsBean) result.next();
				db.delete(tmp);
			}
			db.set(paramsBean);

			// Say goodbye.
			db.commit();
			
		} catch (Exception e) {
			
			System.out.println("[" + getUserName() +"/DislocBean/storeProjectInContext] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

	}

	/**
	 * This is a JSF compatible method for running Disloc in blocking mode. That
	 * is, it takes no argument and assumes the values have been set by
	 * accessors.
	 */
	public String runBlockingDislocJSF() throws Exception {
		
		System.out.println("[" + getUserName() +"/DislocBean/runBlockingDislocJSF] Started");

		try {

			Fault[] faults = getFaultsFromDB();
			ObsvPoint[] points = getObsvPointsFromDB();
			initDislocExtendedService();

			System.out.println("[" + getUserName() +"/DislocBean/runBlockingDislocJSF] The user name : " + userName);
			System.out.println("[" + getUserName() +"/DislocBean/runBlockingDislocJSF] The project name: " + projectName);
			System.out.println("[" + getUserName() +"/DislocBean/runBlockingDislocJSF] Points : " + points);
			System.out.println("[" + getUserName() +"/DislocBean/runBlockingDislocJSF] Faults : " + faults);
			System.out.println("[" + getUserName() +"/DislocBean/runBlockingDislocJSF] Current Params : " + currentParams);

			// This step runs disloc
			DislocResultsBean dislocResultsBean = dislocExtendedService
					.runBlockingDislocExt(userName, projectName, points,
							faults, currentParams, null);
			setJobToken(dislocResultsBean.getJobUIDStamp());

			// This step makes the kml plots.  We allow this to fail.
			String myKmlUrl = "";			
			try {
				 myKmlUrl = createKml(currentParams, dislocResultsBean, faults);
				 setJobToken(dislocResultsBean.getJobUIDStamp());
			}
			catch (Exception ex) {
				 ex.printStackTrace();
			}

			// This step runs the insar plotting stuff.  We also allow this
			// to fail.
			InsarKmlService iks = new InsarKmlServiceServiceLocator()
					.getInsarKmlExec(new URL(insarkmlServiceUrl));

			insarKmlUrl="";
			try {
				 insarKmlUrl = iks.runBlockingInsarKml(userName, projectName,
																	dislocResultsBean.getOutputFileUrl(), this.getElevation(),
																	this.getAzimuth(), this.getFrequency(), "ExecInsarKml");
			}
			catch (Exception ex) {
				 ex.printStackTrace();
			}
			// This sets the InSAR KML URL, which will be accessed by other
			// pages.
			setInsarKmlUrl(insarKmlUrl);

			storeProjectInContext(userName, projectName, dislocResultsBean
										 .getJobUIDStamp(), currentParams, dislocResultsBean,
										 myKmlUrl, insarKmlUrl, elevation, azimuth, frequency);
			
		} catch (Exception ex) {
			 ex.printStackTrace();
		}
		System.out.println("[" + getUserName() +"/DislocBean/runBlockingDislocJSF] Finished");
		return DISLOC_NAV_STRING;
	}

	/**
	 * This method is used to generate the output plots with the remote KML
	 * service.
	 */
	protected String createKml(DislocParamsBean dislocParams,
			DislocResultsBean dislocResultsBean, Fault[] faults)
			throws Exception {
		System.out.println("[" + getUserName() +"/DislocBean/createKml] Started");
		System.out.println("[" + getUserName() +"/DislocBean/createKml] Creating the KML file at " + kmlGeneratorUrl);
		

		// Get the project lat/lon origin. It is the lat/lon origin of the first fault.
		String origin_lat = dislocParams.getOriginLat() + "";
		String origin_lon = dislocParams.getOriginLon() + "";

		System.out.println("[" + getUserName() +"/DislocBean/createKml] The origin: " + origin_lon + " " + origin_lat);

		// get my kml
		SimpleXDataKml kmlService;
		SimpleXDataKmlServiceLocator locator = new SimpleXDataKmlServiceLocator();
		locator.setMaintainSession(true);		
		kmlService = locator.getKmlGenerator(new URL(kmlGeneratorUrl));

		PointEntry[] tmp_pointentrylist = LoadDataFromUrl(dislocResultsBean.getOutputFileUrl());
		
		System.out.println("[" + getUserName() +"/DislocBean/createKml] The size of tmp_pointentrylist : " + tmp_pointentrylist.length);
		System.out.println("[" + getUserName() +"/DislocBean/createKml] dislocResultsBean.getOutputFileUrl() : " + dislocResultsBean.getOutputFileUrl());

		kmlService.setDatalist(tmp_pointentrylist);
		kmlService.setOriginalCoordinate(origin_lon, origin_lat);
		kmlService.setCoordinateUnit("1000");

		// These plot grid lines.
		double start_x, start_y, end_x, end_y, xiterationsNumber, yiterationsNumber;
		start_x = Double.valueOf(dislocParams.getGridMinXValue()).doubleValue();
		start_y = Double.valueOf(dislocParams.getGridMinYValue()).doubleValue();
		xiterationsNumber = Double.valueOf(dislocParams.getGridXIterations())
				.doubleValue();
		yiterationsNumber = Double.valueOf(dislocParams.getGridYIterations())
				.doubleValue();
		int xinterval = (int) (Double.valueOf(dislocParams.getGridXSpacing())
				.doubleValue());
		int yinterval = (int) (Double.valueOf(dislocParams.getGridYSpacing())
				.doubleValue());
		end_x = start_x + xinterval * (xiterationsNumber - 1);
		end_y = start_y + yinterval * (yiterationsNumber - 1);

		// kmlService.setGridLine("Grid Line", start_x, start_y, end_x, end_y,
		// xinterval,yinterval);
		// kmlService.setPointPlacemark("Icon Layer");
		// kmlService.setArrowPlacemark("Arrow Layer", "ff66a1cc", 2);
		kmlService.setArrowPlacemark("Arrow Layer", "#000000", 0.95);

		// Plot the faults
		for (int i = 0; i < faults.length; i++) {
			kmlService.setFaultPlot("", faults[i].getFaultName() + "",
					faults[i].getFaultLonStart() + "", faults[i]
							.getFaultLatStart()
							+ "", faults[i].getFaultLonEnd() + "", faults[i]
							.getFaultLatEnd()
							+ "", "ff6af0ff", 5);
		}

		String myKmlUrl = kmlService.runMakeKml("", userName, projectName,
				(dislocResultsBean.getJobUIDStamp()).hashCode() + "");
		
		System.out.println("[" + getUserName() +"/DislocBean/createKml] Finished");
		return myKmlUrl;
	}

	/**
	 * This runs disloc in non-blocking mode, so it returns immediately. The
	 * client must determine separately if the mesh generation has finished.
	 */
	public String runNonBlockingDislocJSF() throws Exception {

		try {
			Fault[] faults = getFaultsFromDB();
			ObsvPoint[] points = getObsvPointsFromDB();

			initDislocExtendedService();
			DislocResultsBean dislocResultsBean = dislocExtendedService
					.runNonBlockingDislocExt(userName, projectName, points,
							faults, currentParams, null);

			String myKmlUrl = "";
			myKmlUrl = createKml(currentParams, dislocResultsBean, faults);
			setJobToken(dislocResultsBean.getJobUIDStamp());

			// This step runs the insar plotting stuff.
			InsarKmlService iks = new InsarKmlServiceServiceLocator()
					.getInsarKmlExec(new URL(insarkmlServiceUrl));
			System.out.println("[" + getUserName() +"/DislocBean/runNonBlockingDislocJSF] The service URL:" + insarkmlServiceUrl);

			insarKmlUrl = iks.runBlockingInsarKml(userName, projectName,
					dislocResultsBean.getOutputFileUrl(), this.getElevation(),
					this.getAzimuth(), this.getFrequency(), "ExecInsarKml");
			// This sets the InSAR KML URL, which will be accessed by other
			// pages.
			setInsarKmlUrl(insarKmlUrl);			

			storeProjectInContext(userName, projectName, dislocResultsBean
					.getJobUIDStamp(), currentParams, dislocResultsBean,
					myKmlUrl, insarKmlUrl, elevation, azimuth, frequency);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return DISLOC_NAV_STRING;
	}

	/**
	 * Another famous method that I googled. This downloads contents from the
	 * given URL to a local file.
	 */
	public PointEntry[] LoadDataFromUrl(String InputUrl) {
		System.out.println("[" + getUserName() +"/DislocBean/LoadDataFromUrl] Creating Point Entry");
		ArrayList dataset = new ArrayList();
		try {
			String line = new String();
			int skipthreelines = 1;

			URL inUrl = new URL(InputUrl);
			URLConnection uconn = inUrl.openConnection();
			InputStream instream = inUrl.openStream();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					instream));

			// Need to make sure this will work with multiple faults.
			Pattern p = Pattern.compile(" {1,20}");
			while ((line = in.readLine()) != null) {
				String tmp[] = p.split(line);

				if (tmp[1].trim().equals("x") && tmp[2].trim().equals("y")) {
					System.out.println("[" + getUserName() +"/DislocBean/LoadDataFromUrl] Past the faults");
					break;
				}
			}

			while ((line = in.readLine()) != null) {
				if (!line.trim().equalsIgnoreCase("")) {
					PointEntry tempPoint = new PointEntry();

					String tmp[] = p.split(line);

					// Look for NaN or other problems.
					for (int i = 0; i < tmp.length; i++) {
						String oldtmp = tmp[i];
						if (tmp[i].trim().equalsIgnoreCase("nan")) {
							tmp[i] = "0.0";
						}
					}

					tempPoint.setX(tmp[1].trim());
					tempPoint.setY(tmp[2].trim());
					tempPoint.setDeltaXName("dx");
					tempPoint.setDeltaXValue(tmp[3].trim());
					tempPoint.setDeltaYName("dy");
					tempPoint.setDeltaYValue(tmp[4].trim());
					tempPoint.setDeltaZName("dz");
					tempPoint.setDeltaZValue(tmp[5].trim());
					tempPoint.setFolderTag("point");
					dataset.add(tempPoint);
				} else {
					break;
				}
			}
			in.close();
			instream.close();
		} catch (IOException ex1) {
			ex1.printStackTrace();
		}
		System.out.println("[" + getUserName() +"/DislocBean/LoadDataFromUrl] Finished");
		return (PointEntry[]) (dataset.toArray(new PointEntry[dataset.size()]));
	}

	// End main execution method section.
	// --------------------------------------------------

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
		System.out.println("[" + getUserName() +"/DislocBean/getDBValue] SQL Query on:" + param);

		String sqlQuery = "select " + param
				+ " from SEGMENT, REFERENCE where FaultName=\'" + theFault
				+ "\' and SegmentName=\'" + theSegment
				+ "\' and SEGMENT.InterpId=REFERENCE.InterpId;";

		// String sqlQuery = "select F." + param
		// + " from FAULT AS F, REFERENCE AS R where F.FaultName=\'" + theFault
		// + "\' and F.InterpId=R.InterpId;";

		System.out.println("[" + getUserName() +"/DislocBean/getDBValue] SQL Query is " + sqlQuery);

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

	/**
	 * Get the specific fault from the DB. Fill in some parameters as necessary.
	 * Determine if this is an origin fault or not.
	 * 
	 * The input value supports a backward-compatible @ token that can be
	 * ignored these days.
	 */
	public Fault QueryFaultFromDB(String faultname) {
		// Check request with fallback

		/*
		 * Modified to the new module importing from kml desc 09/12/18 Jun Ji at
		 * CGL, jid@cs.indiana.edu
		 */

		Fault tmp_fault = new Fault();

		System.out.println("[" + getUserName() +"/DislocBean/QueryFaultFromDB] faultname : " + faultname);

		String theFault = faultname;

		KMLdescriptionparser kdp = new KMLdescriptionparser();
		kdp.parseXml(getBasePath() + "/" + codeName + "/", kmlfiles);

		kdp.getDesc(theFault);
		kdp.parsevalues();

		try {
			double dip = kdp.getdip();
			double strike = kdp.getstrike();
			double depth = kdp.getdepth();
			double width = kdp.getwidth();
			double length = kdp.getlength();

			double latEnd = kdp.getlatEnd();
			double latStart = kdp.getlatStart();
			double lonStart = kdp.getlonStart();			
			double lonEnd = kdp.getlonEnd();

			double d2r = Math.acos(-1.0) / 180.0;
			double flatten = 1.0 / 298.247;

			double x = (lonEnd - lonStart) * factor(lonStart, latStart);
			double y = (latEnd - latStart) * 111.32;

			// we decided to use the value in the kml 04/29/2010 Jun Ji
			// double length = Double.parseDouble(df.format(Math.sqrt(x * x + y * y)));
			tmp_fault.setFaultName(theFault);
			tmp_fault.setFaultLatStart(latStart);
			tmp_fault.setFaultLonStart(lonStart);
			tmp_fault.setFaultLonEnd(lonEnd);
			tmp_fault.setFaultLatEnd(latEnd);
			tmp_fault.setFaultLength(length);
			tmp_fault.setFaultWidth(width);
			tmp_fault.setFaultDepth(depth);
			tmp_fault.setFaultDipAngle(dip);
			tmp_fault.setFaultDipSlip(kdp.getdipslip());			
			tmp_fault.setFaultStrikeSlip(kdp.getstrikeslip());

			// This is the fault's strike angle
			// strike=Math.atan2(x,y)/d2r;
			tmp_fault.setFaultStrikeAngle(Double.parseDouble(df.format(strike)));

			// This is the (x,y) of the fault relative to the project's origin
			// The project origin is the lower left lat/lon of the first fault.
			// If any of these conditions hold, we need to reset.
			System.out.println("[" + getUserName() +"/DislocBean/QueryFaultFromDB] The origin:" + currentParams.getOriginLat() + " "
					+ currentParams.getOriginLon());
			
			if (currentParams.getOriginLat() == DislocParamsBean.DEFAULT_LAT
					|| currentParams.getOriginLon() == DislocParamsBean.DEFAULT_LON) {
				currentParams.setOriginLat(latStart);
				currentParams.setOriginLon(lonStart);
				// Update the parameters
				
				ObjectContainer db = null;

				try {
					db = Db4o.openFile(getBasePath() + "/"
							+ getContextBasePath() + "/" + userName + "/"
							+ codeName + "/" + projectName + ".db");

					ObjectSet result = db.get(DislocParamsBean.class);
					if (result.hasNext()) {
						DislocParamsBean tmp = (DislocParamsBean) result.next();
						db.delete(tmp);
					}

					db.set(currentParams);

					// Say goodbye.
					db.commit();

				} catch (Exception e) {					
					System.out.println("[" + getUserName() +"/DislocBean/QueryFaultFromDB] " + e);
				}
				finally {
					if (db != null)
						db.close();			
				}

			}
			System.out.println("[" + getUserName() +"/DislocBean/QueryFaultFromDB] The updated origin:" + currentParams.getOriginLat()
					+ " " + currentParams.getOriginLon());

			// The following should be done in any case.
			// If the origin was just (re)set above,
			// we will get a harmless (0,0);
			double x1 = (lonStart - currentParams.getOriginLon())
					* factor(currentParams.getOriginLon(), currentParams
							.getOriginLat());

			double y1 = (latStart - currentParams.getOriginLat()) * 111.32;
			System.out.println("[" + getUserName() +"/DislocBean/QueryFaultFromDB] The fault origin: " + x1 + " " + y1);

			tmp_fault.setFaultLocationX(Double.parseDouble(df.format(x1)));
			tmp_fault.setFaultLocationY(Double.parseDouble(df.format(y1)));

			// tmp_fault.setFaultLocationX(x1);
			// tmp_fault.setFaultLocationY(y1);

		} catch (Exception ex) {
			ex.printStackTrace();

		}

		/*
		 * 
		 * 
		 * String theFault = faultAndSegment.substring(0,
		 * faultAndSegment.indexOf("@")); String
		 * theSegment=faultAndSegment.substring(faultAndSegment.indexOf("@") +
		 * 1, faultAndSegment.indexOf("%"));
		 * 
		 * String
		 * interpId=faultAndSegment.substring(faultAndSegment.indexOf("%") + 1,
		 * faultAndSegment.length());
		 * 
		 * 
		 * try { SelectService ss = new SelectServiceLocator(); Select select =
		 * ss.getSelect(new URL(faultDBServiceUrl));
		 * 
		 * // -------------------------------------------------- // Make
		 * queries. // -------------------------------------------------- double
		 * dip = Double.parseDouble(getDBValue(select, "Dip", theFault,
		 * theSegment,interpId)); double strike =
		 * Double.parseDouble(getDBValue(select, "Strike", theFault,
		 * theSegment,interpId)); double depth =
		 * Double.parseDouble(getDBValue(select, "Depth", theFault,
		 * theSegment,interpId)); double width =
		 * Double.parseDouble(getDBValue(select, "Width", theFault,
		 * theSegment,interpId));
		 * 
		 * // Get the length and width double latEnd =
		 * Double.parseDouble(getDBValue(select, "LatEnd", theFault,
		 * theSegment,interpId)); double latStart =
		 * Double.parseDouble(getDBValue(select, "LatStart", theFault,
		 * theSegment,interpId)); double lonStart =
		 * Double.parseDouble(getDBValue(select, "LonStart", theFault,
		 * theSegment,interpId)); double lonEnd =
		 * Double.parseDouble(getDBValue(select, "LonEnd", theFault,
		 * theSegment,interpId)); // Calculate the length double d2r =
		 * Math.acos(-1.0) / 180.0; double flatten=1.0/298.247;
		 * 
		 * double x = (lonEnd - lonStart) * factor(lonStart,latStart); double y
		 * = (latEnd - latStart) * 111.32; // String length =
		 * df.format(Math.sqrt(x * x + y * y)); // double length = Math.sqrt(x *
		 * x + y * y);
		 * 
		 * double length=Double.parseDouble(df.format(Math.sqrt(x * x + y *
		 * y))); tmp_fault.setFaultName(theFault);
		 * tmp_fault.setFaultLatStart(latStart);
		 * tmp_fault.setFaultLonStart(lonStart);
		 * tmp_fault.setFaultLonEnd(lonEnd); tmp_fault.setFaultLatEnd(latEnd);
		 * tmp_fault.setFaultLength(length); tmp_fault.setFaultWidth(width);
		 * tmp_fault.setFaultDepth(depth); tmp_fault.setFaultDipAngle(dip);
		 * 
		 * //This is the fault's strike angle strike=Math.atan2(x,y)/d2r;
		 * tmp_fault.setFaultStrikeAngle(Double.parseDouble(df.format(strike)));
		 * 
		 * //This is the (x,y) of the fault relative to the project's origin
		 * //The project origin is the lower left lat/lon of the first fault.
		 * //If any of these conditions hold, we need to reset.
		 * System.out.println("Origin:"+currentParams.getOriginLat()+" "
		 * +currentParams.getOriginLon());
		 * if(currentParams.getOriginLat()==DislocParamsBean.DEFAULT_LAT ||
		 * currentParams.getOriginLon()==DislocParamsBean.DEFAULT_LON ) {
		 * currentParams.setOriginLat(latStart);
		 * currentParams.setOriginLon(lonStart); //Update the parameters
		 * 
		 * if(db!=null) db.close(); db=Db4o.openFile(getBasePath()+"/"
		 * +getContextBasePath() +"/"+userName
		 * +"/"+codeName+"/"+projectName+".db"); ObjectSet
		 * result=db.get(DislocParamsBean.class); if(result.hasNext()) {
		 * DislocParamsBean tmp=(DislocParamsBean)result.next(); db.delete(tmp);
		 * } db.set(currentParams);
		 * 
		 * //Say goodbye. db.commit(); if(db!=null) db.close();
		 * 
		 * }
		 * System.out.println("Updated Origin:"+currentParams.getOriginLat()+" "
		 * +currentParams.getOriginLon());
		 * 
		 * //The following should be done in any case. //If the origin was just
		 * (re)set above, //we will get a harmless (0,0); double
		 * x1=(lonStart-currentParams.getOriginLon())
		 * factor(currentParams.getOriginLon(), currentParams.getOriginLat());
		 * double y1=(latStart-currentParams.getOriginLat())*111.32;
		 * System.out.println("Fault origin: "+x1+" "+y1);
		 * tmp_fault.setFaultLocationX(Double.parseDouble(df.format(x1)));
		 * tmp_fault.setFaultLocationY(Double.parseDouble(df.format(y1))); //
		 * tmp_fault.setFaultLocationX(x1); // tmp_fault.setFaultLocationY(y1);
		 * 
		 * } catch (Exception ex) { ex.printStackTrace(); }
		 */
		return tmp_fault;
	}

	// --------------------------------------------------
	// End of the fault db section
	// --------------------------------------------------

	// --------------------------------------------------
	// Begin the event handling section for the JSF pages.
	// --------------------------------------------------

	public void handleFaultsRadioValueChange(ValueChangeEvent event) {
		try {
			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
			FaultDBEntry tmp_FaultDBEntry = (FaultDBEntry) getMyFaultDataTable()
					.getRowData();
			SelectItem tmp_SelectItem = tmp_FaultDBEntry.getFaultName();

			System.out.println("[" + getUserName() +"/DislocBean/handleFaultsRadioValueChange] changed fault value : "
					+ tmp_SelectItem.getValue().toString());
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

	// --------------------------------------------------
	// End change event listener handlers.
	// --------------------------------------------------

	// --------------------------------------------------
	// Begin action event processing methods.
	// --------------------------------------------------

	/**
	 * This method clears all the render choices, setting them to false. Not
	 * sure if this should be public.
	 */
	public void initEditFormsSelection() {
		renderSearchByFaultNameForm = false;
		renderSearchByAuthorForm = false;
		renderSearchByLatLonForm = false;
		renderViewAllFaultsForm = false;
		renderCreateNewFaultForm = false;
		renderAddFaultSelectionForm = false;
		renderAddFaultFromDBForm = false;
		renderDislocGridParamsForm = false;
		renderMap = false;
		renderFaultMap = false;
		renderChooseObsvStyleForm = false;
	}

	/**
	 * This is called when a project is seleted for loading.
	 */
	public String toggleSelectProject() throws Exception {
		System.out.println("[" + getUserName() +"/DislocBean/toggleSelectProject] Loading Project from " + projectsource);
		initEditFormsSelection();
		// This is implemented as a selectmanycheckbox on the client side
		// (LoadProject.jsp),
		// hence the unusual for loop.
		if (selectProjectsArray != null) {
			for (int i = 0; i < 1; i++) {
				this.projectName = selectProjectsArray[0];
			}
		}

		ObjectContainer db = null;
		try {
			
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"+ userName + "/" + codeName + ".db");
			
			// First, get the project bean
			DislocProjectBean project = new DislocProjectBean();
			project.setProjectName(projectName);
			ObjectSet results = db.get(project);
			System.out.println("[" + getUserName() +"/DislocBean/toggleSelectProject] Got results:" + results.size());
			if (results.hasNext()) {
				currentProject = (DislocProjectBean) results.next();
			}
			// Say goodbye.			
		} catch (Exception e) {			
			System.out.println("[" + getUserName() +"/DislocBean/toggleSelectProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		// Reconstruct the fault and layer object collections from the context
		// myFaultCollection=populateFaultCollection(projectName);
		myFaultEntryForProjectList = reconstructMyFaultEntryForProjectList(projectName);

		// Now look up the project params bean and set the project origin.
		currentParams = getDislocParamsFromDB(projectName);

		// Some final stuff.
		projectSelectionCode = "";
		faultSelectionCode = "";

		return "disloc-edit-project";
	}

	/**
	 * This is called when a project is seleted for copying and loading.
	 */
	public String toggleCopyProject() throws Exception {
		System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] Copying project from " + projectsource);
		initEditFormsSelection();
		// Get the old project name from the checkboxes
		String oldProjectName = "";
		
		if (projectsource.compareToIgnoreCase("my") == 0) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] project source is my list");
			if (copyProjectsArray != null) {
				for (int i = 0; i < 1; i++) {
					oldProjectName = copyProjectsArray[0];
				}
			}
		}
		else if (projectsource.compareToIgnoreCase("db") == 0) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] project source is db");
			oldProjectName = projectname;
		}
			
		System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] The old project name: " + oldProjectName);

		// Create an empty project
		String newProjectName = this.getProjectName();
		System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] The new project name : " + newProjectName);
		createNewProject(newProjectName);

		// Now replace empty new project pieces with old stuff.
		ObjectContainer db = null;
		try {			
			if (projectsource.compareToIgnoreCase("my") == 0) {
				System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] projectsource is my list");
				db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"+ userName + "/" + codeName + ".db");
			}
			else if (projectsource.compareToIgnoreCase("db") == 0) {
				System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] projectsource is db");
				db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/automatedDislocDB/overm5.db");
			}

			// Get the old project bean.
			DislocProjectBean project = new DislocProjectBean();
			project.setProjectName(oldProjectName);
			ObjectSet results = db.get(project);
			System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] Got results:" + results.size());
			if (results.hasNext()) {
				currentProject = (DislocProjectBean) results.next();
			}
			// Say goodbye.
			
		} catch (Exception e) {			
			System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		// //Reconstruct the fault and layer object collections from the context
		// myFaultEntryForProjectList=reconstructMyFaultEntryForProjectList(oldProjectName);
		// System.out.println("Old project size:"+myFaultEntryForProjectList.size());
		// System.out.println("Some faultentrystuff:"+((faultEntryForProject)myFaultEntryForProjectList.get(0)).getFaultName());

		// Copy the DB file for the old project to the new project.
		File oldFileDB = null;
		if (projectsource.compareToIgnoreCase("my") == 0) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] projectsource is my list");
			oldFileDB = new File(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + oldProjectName + ".db");
		}
		else if (projectsource.compareToIgnoreCase("db") == 0) {
			oldProjectName = projectname;
			System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] projectsource is db");
			oldFileDB = new File(getBasePath() + "/" + getContextBasePath() + "/automatedDislocDB/overm5/" + oldProjectName + ".db");			
		}
		
		File newFileDB = new File(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + newProjectName + ".db");
		copyFile(oldFileDB, newFileDB);

		// Now look up the project params bean and set the project origin.
		currentParams = getDislocParamsFromDB(newProjectName);
		System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] The origin:" + currentParams.getOriginLat() + ", " + currentParams.getOriginLon());		
		System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] Min Y:" + currentParams.getGridMinYValue());
		System.out.println("[" + getUserName() +"/DislocBean/toggleCopyProject] Min X:" + currentParams.getGridMinXValue());

		// Some final stuff.
		projectSelectionCode = "";
		faultSelectionCode = "";
		return "disloc-edit-project";
	}

	/**
	 * Handle action events in the project selection area.
	 */
	public void toggleProjectSelection(ActionEvent ev) {

		initEditFormsSelection();
		if (projectSelectionCode.equals("CreateObservationGrid")) {
			currentParams = getDislocParamsFromDB(projectName);
			renderDislocGridParamsForm = !renderDislocGridParamsForm;
		}

		else if (projectSelectionCode.equals("CreateNewFault")) {
			currentFault = new Fault();
			renderCreateNewFaultForm = !renderCreateNewFaultForm;
		} else if (projectSelectionCode.equals("AddFaultSelection")) {
			renderAddFaultSelectionForm = !renderAddFaultSelectionForm;
		} else if (projectSelectionCode.equals("ShowMap")) {
			renderMap = !renderMap;
		} else if (projectSelectionCode.equals("ShowFaultMap")) {
			renderFaultMap = !renderFaultMap;
		} else if (projectSelectionCode.equals("ChooseObsvStyleForm")) {
			renderChooseObsvStyleForm = !renderChooseObsvStyleForm;
		} else if (projectSelectionCode.equals("")) {
			;
		}
	}

	/**
	 * This is where we set the project's observation style.
	 */
	public void toggleSetObsvStyle(ActionEvent ev) {
		initEditFormsSelection();
		if (obsvStyleSelectionCode.equals("GridStyle")) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleSetObsvStyle] GridStyle");
			usesGridPoints = true;
			currentParams.setObservationPointStyle(1);
		} else if (obsvStyleSelectionCode.equals("ScatterStyle")) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleSetObsvStyle] ScatterStyle");
			usesGridPoints = false;
			currentParams.setObservationPointStyle(0);
		} else {
			System.out.println("[" + getUserName() +"/DislocBean/toggleSetObsvStyle] Unexpected obsv style");
		}
		obsvStyleSelectionCode = "";
		try {
			storeParamsInDB();
		} catch (Exception ex) {
			ex.printStackTrace();
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
			// myFaultDBEntryList=ViewAllFaults(faultDBServiceUrl);
			KMLdescriptionparser kdp = new KMLdescriptionparser();
			kdp.parseXml(getBasePath() + "/" + codeName + "/", kmlfiles);
			myFaultDBEntryList = kdp.getFaultList("All", "");

			renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
		}
		if (projectSelectionCode.equals("")) {
			;
		}
		faultSelectionCode = "";
	}

	public void toggleUpdateProjectObservations(ActionEvent ev) {
		System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateProjectObservations] Updating observation entry for project");
		try {
			obsvEntryForProject tmp_ObsvEntryForProject = new obsvEntryForProject();

			// Find out which one was selected
			for (int i = 0; i < myObsvEntryForProjectList.size(); i++) {
				tmp_ObsvEntryForProject = (obsvEntryForProject) myObsvEntryForProjectList
						.get(i);
				if ((tmp_ObsvEntryForProject.getView() == true)
						|| (tmp_ObsvEntryForProject.getDelete() == true)) {
					break;
				}
			}

			boolean tmp_view = tmp_ObsvEntryForProject.getView();
			boolean tmp_update = tmp_ObsvEntryForProject.getDelete();

			initEditFormsSelection();
			if ((tmp_view == true) && (tmp_update == true)) {
				System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateProjectObservations] error");
			}
			// This is the edit case.
			if ((tmp_view == true) && (tmp_update == false)) {
				System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateProjectObservations] We are adding/editing the observations");
				currentParams = populateParamsFromContext(projectName);
				renderDislocGridParamsForm = !renderDislocGridParamsForm;
				System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateProjectObservations] Rendering:" + renderDislocGridParamsForm);
			}

			// This is the deletion case.
			if ((tmp_update == true) && (tmp_view == false)) {
				System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateProjectObservations] We are deleteing the observations");

				ObjectContainer db = null;
				try {				
					
					db = Db4o.openFile(getBasePath() + "/"
							+ getContextBasePath() + "/" + userName + "/"
							+ codeName + "/" + projectName + ".db");

					// There is only one of these
					ObjectSet result1 = db.get(DislocParamsBean.class);
					if (result1.hasNext()) {
						DislocParamsBean todelete = (DislocParamsBean) result1
								.next();
						// Now that we have the specific object, we can delete
						// it.
						db.delete(todelete);
					}
					
				} catch (Exception e) {					
					System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateProjectObservations]  " + e);
				}
				finally {
					if (db != null)
						db.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void toggleUpdateFaultProjectEntry(ActionEvent ev) {
		String faultStatus = "Update";
		System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateFaultProjectEntry] Updating fault entry for project");
		try {

			// This is the info about the fault.
			faultEntryForProject tmp_FaultEntryForProject = new faultEntryForProject();

			for (int i = 0; i < myFaultEntryForProjectList.size(); i++) {
				tmp_FaultEntryForProject = (faultEntryForProject) myFaultEntryForProjectList
						.get(i);
				if ((tmp_FaultEntryForProject.getUpdate() == true)
						|| (tmp_FaultEntryForProject.getDelete() == true)) {
					break;
				}
			}

			String tmp_faultName = tmp_FaultEntryForProject.getFaultName();
			boolean tmp_view = tmp_FaultEntryForProject.getUpdate();
			boolean tmp_update = tmp_FaultEntryForProject.getDelete();

			System.out.println(tmp_view + " " + tmp_update);

			initEditFormsSelection();
			if ((tmp_view == true) && (tmp_update == true)) {
				System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateFaultProjectEntry] error");
			}

			// Update the fault.
			if ((tmp_view == true) && (tmp_update == false)) {
				currentFault = populateFaultFromContext(tmp_faultName);
				renderCreateNewFaultForm = !renderCreateNewFaultForm;
				System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateFaultProjectEntry] Rendering:" + renderCreateNewFaultForm);
			}

			// This is the deletion case.
			if ((tmp_update == true) && (tmp_view == false)) {

				// Delete from the database.
				// This requires we first search for the desired object
				// and then delete the specific value that we get back.
				System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateFaultProjectEntry] Deleting " + tmp_faultName + "from db");

				ObjectContainer db = null;
							
				try {
					db = Db4o.openFile(getBasePath() + "/"
							+ getContextBasePath() + "/" + userName + "/"
							+ codeName + "/" + projectName + ".db");
					// Fault todelete=new Fault();
					// todelete.setFaultName(tmp_faultName);
					ObjectSet result = db.get(Fault.class);
					Fault todelete;
					while (result.hasNext()) {
						todelete = (Fault) result.next();
						if (todelete.getFaultName().equals(tmp_faultName)) {
							db.delete(todelete);
						}
					}
				} catch (Exception e) {					
					System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateFaultProjectEntry] " + e);
				}
				finally {
					if (db != null)
						db.close();			
				}
			}

			// Possibly update the project origin. Need to do this if the
			// original fault
			// use to set the problem origin is deleted.
			// setProjectOrigin(projectName);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void toggleUpdateFaults(ActionEvent ev) {
		System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateFaults] Started");
		String faultStatus = "Update";
		ObjectContainer db = null;
		try {

			int iSelectFault = -1;
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

				initEditFormsSelection();
				if ((tmp_update == true) && (tmp_delete == true)) {
					System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateFaults] error");
				}

				// Update the fault.
				if ((tmp_update == true) && (tmp_delete == false)) {

					System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateFaults] Updating "
							+ tmp_Fault.getFaultName() + "(old name)"
							+ tmp_faultName);
					
					db = Db4o.openFile(getBasePath() + "/"
							+ getContextBasePath() + "/" + userName + "/"
							+ codeName + "/" + projectName + ".db");

					Fault toUpdate = new Fault();
					toUpdate.setFaultName(tmp_faultName);
					ObjectSet result = db.get(toUpdate);

					if (result.hasNext()) {
						toUpdate = (Fault) result.next();

						toUpdate.setFaultDepth(tmp_Fault.getFaultDepth());
						toUpdate.setFaultDipAngle(tmp_Fault.getFaultDipAngle());
						toUpdate.setFaultDipSlip(tmp_Fault.getFaultDipSlip());
						toUpdate.setFaultLameLambda(tmp_Fault.getFaultLameLambda());
						toUpdate.setFaultLameMu(tmp_Fault.getFaultLameMu());
						toUpdate.setFaultLatEnd(tmp_Fault.getFaultLatEnd());
						toUpdate.setFaultLatStart(tmp_Fault.getFaultLatStart());
						toUpdate.setFaultLength(tmp_Fault.getFaultLength());
						toUpdate.setFaultLocationX(tmp_Fault.getFaultLocationX());
						toUpdate.setFaultLocationY(tmp_Fault.getFaultLocationY());
						toUpdate.setFaultLocationZ(tmp_Fault.getFaultLocationZ());
						toUpdate.setFaultLonEnd(tmp_Fault.getFaultLonEnd());
						toUpdate.setFaultLonStart(tmp_Fault.getFaultLonStart());
						toUpdate.setFaultName(tmp_Fault.getFaultName());
						toUpdate.setFaultRakeAngle(tmp_Fault.getFaultRakeAngle());
						toUpdate.setFaultStrikeAngle(tmp_Fault.getFaultStrikeAngle());
						toUpdate.setFaultStrikeSlip(tmp_Fault.getFaultStrikeSlip());
						toUpdate.setFaultTensileSlip(tmp_Fault.getFaultTensileSlip());
						toUpdate.setFaultWidth(tmp_Fault.getFaultWidth());
						
					}
					db.set(toUpdate);
					db.commit();					
				}

				// This is the deletion case.
				else if ((tmp_update == false) && (tmp_delete == true)) {

					// Delete from the database.
					// This requires we first search for the desired object
					// and then delete the specific value that we get back.
					System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateFaults] Deleteing "
							+ tmp_faultName + "from db");

					db = Db4o.openFile(getBasePath() + "/"
							+ getContextBasePath() + "/" + userName + "/"
							+ codeName + "/" + projectName + ".db");

					Fault todelete = new Fault();
					todelete.setFaultName(tmp_faultName);
					ObjectSet result = db.get(todelete);
					if (result.hasNext()) {
						todelete = (Fault) result.next();
						db.delete(todelete);
					}
				}
			}

		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleUpdateFaults] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		// Print this out as KML
		faultKmlUrl = createFaultKmlFile();
	}
	
	
	
	
	
	/**
	 * Handle fault db entry events.
	 */
	public void toggleSelectFaultDBEntry(ActionEvent ev) {
		initEditFormsSelection();
		// currentFault.setFaultName(currentFault.getFaultName().trim());
		if (!currentFault.getFaultName().equals("")) {
			currentFault = QueryFaultFromDB(currentFault.getFaultName().trim());
		}
		renderCreateNewFaultForm = !renderCreateNewFaultForm;
	}

	public void toggleFaultSearchByName(ActionEvent ev) {
		initEditFormsSelection();
		this.forSearchStr = this.forSearchStr.trim();
		if (!this.forSearchStr.equals("")) {
			// myFaultDBEntryList=QueryFaultsByName(this.forSearchStr,faultDBServiceUrl);
			KMLdescriptionparser kdp = new KMLdescriptionparser();
			kdp.parseXml(getBasePath() + "/" + codeName + "/", kmlfiles);
			myFaultDBEntryList = kdp.getFaultList("Name", this.forSearchStr);
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
			// myFaultDBEntryList=QueryFaultsByLonLat(this.faultLatStart,
			// this.faultLatEnd, this.faultLonStart, this.faultLonEnd,
			// faultDBServiceUrl);
			KMLdescriptionparser kdp = new KMLdescriptionparser();
			kdp.parseXml(getBasePath() + "/" + codeName + "/", kmlfiles);
			myFaultDBEntryList = kdp.getFaultList("LonLat", this.faultLatStart
					+ " " + this.faultLatEnd + " " + this.faultLonStart + " "
					+ this.faultLonEnd);

		}
		renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
	}

	/**
	 * This will delete projects
	 */
	public void toggleDeleteProjectSummary() throws Exception {
		System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteProjectSummary] Deleting Project");
		ObjectContainer db = null;
		try {
			DislocProjectSummaryBean dpsb = (DislocProjectSummaryBean) getMyProjectSummaryDataTable().getRowData();

			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + ".db");
			System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteProjectSummary] Found project:" + dpsb.getProjectName() + " "
					+ dpsb.getJobUIDStamp());
			ObjectSet results = db.get(dpsb);
			System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteProjectSummary] Result size: " + results.size());
			// Should only have one value.
			if (results.hasNext()) {
				DislocProjectSummaryBean deleteme = (DislocProjectSummaryBean) results
						.next();
				db.delete(deleteme);
			}

			// Delete also the associated insar plots
			ObjectSet results2 = db.get(InsarParamsBean.class);
			System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteProjectSummary] Number of matches:" + results.size());
			while (results2.hasNext()) {
				InsarParamsBean delinsar = (InsarParamsBean) results2.next();
				System.out.println(delinsar.getProjectName() + " "
						+ dpsb.getProjectName() + " "
						+ delinsar.getJobUIDStamp() + " "
						+ dpsb.getJobUIDStamp());

				if (delinsar.getProjectName().equals(dpsb.getProjectName())
						&& delinsar.getJobUIDStamp().equals(
								dpsb.getJobUIDStamp())) {
					System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteProjectSummary] Deleting insar params");
					db.delete(delinsar);
				}
			}

		} catch (Exception e) {			
			System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteProjectSummary] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

	}

	/**
	 * Stop showing the map.
	 */
	public void toggleCloseMap(ActionEvent ev) {
		setRenderMap(false);
	}

	public void toggleCloseFaultMap(ActionEvent ev) {
		setRenderFaultMap(false);
	}

	/**
	 * Used for selecting the data to plot
	 */
	public void togglePlotProject(ActionEvent ev) {
		System.out.println("[" + getUserName() +"/DislocBean/togglePlotProject] Plotting project");
		ObjectContainer db = null;
		try {
			// 
			
			
			// db=Db4o.openFile(getBasePath()+"/"+getContextBafsePath()+"/"+userName+"/"+codeName+".db");
			DislocProjectSummaryBean dpsb = (DislocProjectSummaryBean) getMyProjectSummaryDataTable()
					.getRowData();

			System.out.println("[" + getUserName() +"/DislocBean/togglePlotProject] Found project:" + dpsb.getProjectName() + " "
					+ dpsb.getJobUIDStamp() + dpsb.getKmlurl());
			String kmlName = dpsb.getKmlurl().substring(
					dpsb.getKmlurl().lastIndexOf("/") + 1,
					dpsb.getKmlurl().length());

			downloadKmlFile(dpsb.getKmlurl(), this.getBasePath() + "/"
					+ "gridsphere" + "/" + kmlName);

			System.out.println("[" + getUserName() +"/DislocBean/togglePlotProject] KML Name: " + kmlName);
			setKmlProjectFile(kmlName);

			// ObjectSet results=db.get(dpsb);
			// if(results.hasNext()) {
			// //Reconstitute the bean from the db
			// //Find the URL of the kml and download it.
			// //Set the kml project file.
			// }
			
			
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/togglePlotProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

	}

	protected List populateParamsCollection(String projectName)
			throws Exception {
		List myDislocParamsCollection = new ArrayList();
		ObjectContainer db = null;
		
		try {			
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");
			ObjectSet results = db.get(DislocParamsBean.class);
			// Should only have one value.
			DislocParamsBean dislocParams = null;
			// There should only be one of these
			if (results.hasNext()) {
				dislocParams = (DislocParamsBean) results.next();
				myDislocParamsCollection.add(dislocParams);
			}
		} catch (Exception e) {			
			System.out.println("[" + getUserName() +"/DislocBean/populateParamsCollection] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return myDislocParamsCollection;
	}

	public void toggleFaultSearchByAuthor(ActionEvent ev) {
		initEditFormsSelection();
		this.forSearchStr = this.forSearchStr.trim();
		if (!this.forSearchStr.equals("")) {
			myFaultDBEntryList = QueryFaultsByAuthor(this.forSearchStr,
					faultDBServiceUrl);
		}
		this.forSearchStr = "";
		renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
	}

	public void toggleAddFaultForProject(ActionEvent ev) throws Exception {
		ObjectContainer db = null;
		try {
			initEditFormsSelection();
			System.out.println("[" + getUserName() +"/DislocBean/toggleAddFaultForProject] Setting current fault");

			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			// Add the fault to the DB.
			// Fault tmpfault=new Fault();
			// tmpfault.setFaultName(currentFault.getFaultName());
			// ObjectSet result=db.get(tmpfault);
			ObjectSet result = db.get(Fault.class);
			Fault tmpfault;
			// Remove any previous versions
			while (result.hasNext()) {
				tmpfault = (Fault) result.next();
				if (tmpfault.getFaultName().equals(currentFault.getFaultName())) {
					System.out.println("[" + getUserName() +"/DislocBean/toggleAddFaultForProject] Deleting old fault : " + currentFault.getFaultName());
					db.delete(tmpfault);
				}
			}
			db.set(currentFault);
			db.commit();
			
			if (currentParams.getOriginLat() == DislocParamsBean.DEFAULT_LAT|| currentParams.getOriginLon() == DislocParamsBean.DEFAULT_LON) {
				currentParams.setOriginLat(currentFault.getFaultLatStart());
				currentParams.setOriginLon(currentFault.getFaultLonStart());
			}
			
			db.set(currentParams);
			db.commit();
			

			// setProjectOrigin(projectName);
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleAddFaultForProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

	}
	public void createFaultFromMap() {
		
		Fault tmp_fault = new Fault();
		System.out.println ("[DislocBean/createFaultFromMap] started");
		
		double dip = 0;
		double depth = 0;
		double width = 0;    

		double latEnd = Double.parseDouble(faultLatEnd);
		double latStart = Double.parseDouble(faultLatStart);
		double lonStart = Double.parseDouble(faultLonStart);
		double lonEnd = Double.parseDouble(faultLonEnd);


		// Calculate the length			
		double d2r = Math.acos(-1.0) / 180.0;
		double flatten=1.0/298.247;

		double x = (lonEnd - lonStart) * factor(lonStart,latStart);
		double y = (latEnd - latStart) * 111.32;

		// String length = df.format(Math.sqrt(x * x + y * y));
		double length=Double.parseDouble(df.format(Math.sqrt(x * x + y * y)));
		tmp_fault.setFaultName(faultName);
		
		tmp_fault.setFaultLength(length);
		tmp_fault.setFaultWidth(width);
		tmp_fault.setFaultDepth (depth);
		tmp_fault.setFaultDipAngle(dip);
		
		//Probably hokey default values		
		tmp_fault.setFaultDipSlip(1.0);		
		tmp_fault.setFaultRakeAngle(1.0);
		
		tmp_fault.setFaultLonStart(lonStart);
		tmp_fault.setFaultLatStart(latStart);
		tmp_fault.setFaultLonEnd(lonEnd);
		tmp_fault.setFaultLatEnd(latEnd);
		
		//Set the strike
		double strike=Math.atan2(x,y)/d2r;		
		
		// This is the fault's strike angle
		// strike=Math.atan2(x,y)/d2r;
		tmp_fault.setFaultStrikeAngle(Double.parseDouble(df.format(strike)));

		// This is the (x,y) of the fault relative to the project's origin
		// The project origin is the lower left lat/lon of the first fault.
		// If any of these conditions hold, we need to reset.
		System.out.println("[" + getUserName() +"/DislocBean/createFaultFromMap] The origin:" + currentParams.getOriginLat() + " "
				+ currentParams.getOriginLon());
		if (currentParams.getOriginLat() == DislocParamsBean.DEFAULT_LAT|| currentParams.getOriginLon() == DislocParamsBean.DEFAULT_LON) {
			currentParams.setOriginLat(latStart);
			currentParams.setOriginLon(lonStart);
		}
		
			// Update the parameters

			ObjectContainer db = null;
			try {
				db = Db4o.openFile(getBasePath() + "/"
						+ getContextBasePath() + "/" + userName + "/"
						+ codeName + "/" + projectName + ".db");

				ObjectSet result = db.get(DislocParamsBean.class);
				if (result.hasNext()) {
					DislocParamsBean tmp = (DislocParamsBean) result.next();
					db.delete(tmp);
				}

				db.set(currentParams);

				// Say goodbye.
				db.commit();
			} catch (Exception e) {
				System.out.println("[" + getUserName() +"/DislocBean/createFaultFromMap] " + e);
			}
			finally {
				if (db != null)
					db.close();			
			}
		
		
		System.out.println("[" + getUserName() +"/DislocBean/createFaultFromMap] Updated Origin:" + currentParams.getOriginLat()
				+ " " + currentParams.getOriginLon());

		// The following should be done in any case.
		// If the origin was just (re)set above,
		// we will get a harmless (0,0);
		double x1 = (lonStart - currentParams.getOriginLon())
				* factor(currentParams.getOriginLon(), currentParams
						.getOriginLat());

		double y1 = (latStart - currentParams.getOriginLat()) * 111.32;
		System.out.println("[" + getUserName() +"/DislocBean/createFaultFromMap] Fault origin: " + x1 + " " + y1);

		tmp_fault.setFaultLocationX(Double.parseDouble(df.format(x1)));
		tmp_fault.setFaultLocationY(Double.parseDouble(df.format(y1)));

		// tmp_fault.setFaultLocationX(x1);
		// tmp_fault.setFaultLocationY(y1);


		currentFault = tmp_fault;
	}
	
	public void toggleDrawFaultFromMap(ActionEvent ev) {

		System.out.println("[" + getUserName() +"/DislocBean/toggleDrawFaultFromMap] Started");

		createFaultFromMap();

		initEditFormsSelection();
		System.out
				.println("[DislocBean/toggleDrawFaultFromMap] currentFault.getFaultName() "
						+ currentFault.getFaultName());

		ObjectContainer db = null;
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			Fault tmpfault = new Fault();
			tmpfault.setFaultName(currentFault
					.getFaultName());
			ObjectSet result = db.get(tmpfault);
			if (result.hasNext()) {
				tmpfault = (Fault) result.next();
				db.delete(tmpfault);
			}
			db.set(currentFault);
			db.commit();
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleDrawFaultFromMap] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
	}
	
	public void deleteObsv(ActionEvent ev) throws Exception {
		
		ObjectContainer db = null;
		try {
			initEditFormsSelection();
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");
			
			System.out.println("[" + getUserName() +"/DislocBean/deleteObsv] Deleting an observation");
			// There should only be one of these at most.
			// Delete the stored one and replace it with the new one.
			
			ObjectSet result = db.get(DislocParamsBean.class);
			if (result.hasNext()) {
				DislocParamsBean tmp = (DislocParamsBean) result.next();
				db.delete(tmp);
				}
			db.commit();
			} catch (Exception e) {				
				System.out.println("[" + getUserName() +"/DislocBean/deleteObsv] " + e);				
			}
			finally {
				if (db != null)
					db.close();			
			}
	}
	
	public void toggleAddObservationsForProject(ActionEvent ev) throws Exception {
		ObjectContainer db = null;
		
		try {
			initEditFormsSelection();

			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			System.out.println("[" + getUserName() +"/DislocBean/toggleAddObservationsForProject] Adding an observation");

			// There should only be one of these at most.
			// Delete the stored one and replace it with the new one.
			ObjectSet result = db.get(DislocParamsBean.class);
			if (result.hasNext()) {
				DislocParamsBean tmp = (DislocParamsBean) result.next();
				db.delete(tmp);
			}
			System.out.println("[" + getUserName() +"/DislocBean/toggleAddObservationsForProject] Disloc params are "
					+ currentParams.getGridXIterations());
			System.out.println("[" + getUserName() +"/DislocBean/toggleAddObservationsForProject] Disloc params are also "
					+ currentParams.getGridYIterations());
			db.set(currentParams);

			ObjectSet faultResults = db.get(Fault.class);
			while (faultResults.hasNext()) {
				Fault tmp_fault = (Fault) faultResults.next();
				System.out.println("[" + getUserName() +"/DislocBean/toggleAddObservationsForProject] Updating fault origins for "
						+ tmp_fault.getFaultName());

				double x1 = (tmp_fault.getFaultLonStart() - currentParams
						.getOriginLon())
						* factor(currentParams.getOriginLon(), currentParams
								.getOriginLat());
				double y1 = (tmp_fault.getFaultLatStart() - currentParams
						.getOriginLat()) * 111.32;
				System.out.println("[" + getUserName() +"/DislocBean/toggleAddObservationsForProject] New fault origin: " + x1 + " " + y1);

				tmp_fault.setFaultLocationX(Double.parseDouble(df.format(x1)));
				tmp_fault.setFaultLocationY(Double.parseDouble(df.format(y1)));
				db.set(tmp_fault);
			}
			db.commit();

		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleAddObservationsForProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
	}

	/**
	 * These are methods associated with Faces navigations.
	 */
	public String newProject() throws Exception {
		initDislocService();
		isInitialized = getIsInitialized();
		// if (!isInitialized) {
		// initWebServices();
		// }
		// setContextList();

		makeProjectDirectory();
		return ("disloc-new-project");
	}

	public void init_edit_project() {
		initEditFormsSelection();
		projectSelectionCode = "";
		faultSelectionCode = "";
	}

	/**
	 * Create the new project bean, store it in the db, and initialize.
	 */
	public String NewProjectThenEditProject() throws Exception {
		System.out.println("[" + getUserName() +"/DislocBean/NewProjectThenEditProject] From NewProjectThenEditProject...");
		System.out.println("[" + getUserName() +"/DislocBean/NewProjectThenEditProject] portalBaseUrl" + getPortalBaseUrl());
		System.out.println("[" + getUserName() +"/DislocBean/NewProjectThenEditProject] faultDBServiceUrl : " + getFaultDBServiceUrl());
		System.out.println("[" + getUserName() +"/DislocBean/NewProjectThenEditProject] kmlGeneratorUrl : " + getKmlGeneratorUrl());
		System.out.println("[" + getUserName() +"/DislocBean/NewProjectThenEditProject] ContextBasePath : " + getContextBasePath());

		try {
			// dislocParams=new DislocParamsBean();
			createNewProject(projectName);
			init_edit_project();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "disloc-edit-project";
	}

	public String toggleDeleteProject() {
		System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteProject] Deleting a project");
		ObjectContainer db = null;
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + ".db");
			if (deleteProjectsArray != null) {
				for (int i = 0; i < deleteProjectsArray.length; i++) {
					// Delete the project input data
					System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteProject] Deleting project input junk");
					DislocProjectBean delproj = new DislocProjectBean();
					delproj.setProjectName(deleteProjectsArray[i]);
					ObjectSet results = db.get(delproj);
					if (results.hasNext()) {
						delproj = (DislocProjectBean) results.next();
						db.delete(delproj);
					}
					// Delete the results summary bean also.
					System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteProject] Deleting project summaries");
					DislocProjectSummaryBean delprojsum = new DislocProjectSummaryBean();
					delprojsum.setProjectName(deleteProjectsArray[i]);
					ObjectSet results2 = db.get(delprojsum);
					while (results2.hasNext()) {
						delprojsum = (DislocProjectSummaryBean) results2.next();
						db.delete(delprojsum);
					}
					// Delete the insar plotting bean, too
					System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteProject] Deleting insar plots");
					InsarParamsBean delinsar = new InsarParamsBean();
					delinsar.setProjectName(deleteProjectsArray[i]);
					ObjectSet results3 = db.get(delinsar);
					while (results3.hasNext()) {
						delinsar = (InsarParamsBean) results3.next();
						db.delete(delinsar);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return "disloc-this";
	}

	/**
	 * This method stores the new project in the DB and updates the
	 * currentProject
	 */
	protected void createNewProject(String projectName) {
		System.out.println("[" + getUserName() +"/DislocBean/createNewProject] Creating new project");
		makeProjectDirectory();

		ObjectContainer db = null;
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + ".db");
			DislocProjectBean tmp = new DislocProjectBean();
			ObjectSet results = db.get(DislocProjectBean.class);

			// Create a new project. This may be overwritten later
			currentProject = new DislocProjectBean();
			currentProject.setProjectName(projectName);

			// See if project already exists
			while (results.hasNext()) {
				tmp = (DislocProjectBean) results.next();
				// This is a screwed up project so delete it.
				if (tmp == null || tmp.getProjectName() == null) {
					db.delete(tmp);
				}
				// The project already exists, so update it.
				else if (tmp.getProjectName().equals(projectName)) {
					db.delete(tmp);
					currentProject = tmp;
					break;
				}
			}
			db.set(currentProject);
			db.commit();
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/createNewProject] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		currentParams = getDislocParamsFromDB(projectName);
		System.out.println("[" + getUserName() +"/DislocBean/createNewProject] Finsished");
	}

	public String loadProjectList() throws Exception {
		// if (!isInitialized) {
		// initWebServices();
		// }
		// setContextList();

		makeProjectDirectory();

		return ("disloc-list-project");
	}

	/**
	 * Contains a list of project beans.
	 */
	public List getMyProjectNameList() {

		this.myProjectNameList.clear();
		ObjectContainer db = null;
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath()
						+ "/" + userName + "/" + codeName + ".db");
				DislocProjectBean project = new DislocProjectBean();
				ObjectSet results = db.get(DislocProjectBean.class);
				// System.out.println("Got results:"+results.size());
				while (results.hasNext()) {
					project = (DislocProjectBean) results.next();
					myProjectNameList.add(new SelectItem(project
							.getProjectName(), project.getProjectName()));			
			}
				
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/getMyProjectNameList] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return this.myProjectNameList;
	}
	
	public HashMap getDbProjectNameList() {

		Client c = Client.create();
		WebResource webResource = c.resource("http://129.79.49.68:8080/axis2/services/AutomatedDislocBean/run?url=http://earthquake.usgs.gov/earthquakes/catalogs/7day-M5.xml");
		webResource.get(String.class);
		// System.out.println("[" + getUserName() +"/main] " + webResource.get(String.class));
		
		this.dbProjectNameList.clear();
		ObjectContainer db = null;
		
		List sl = null;		
		String pn = null;
		String spn = null;
		
		try {

				db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/automatedDislocDB/overm5.db");
				DislocProjectBean project = new DislocProjectBean();
				ObjectSet results = db.get(DislocProjectBean.class);
				// System.out.println("Got results:"+results.size());				
				while (results.hasNext()) {
					project = (DislocProjectBean) results.next();
					pn = project.getProjectName();
					spn = pn.substring(0, pn.length()-1);
					// System.out.println("[" + getUserName() +"/getDbProjectNameList]" + pn + "/" + spn);
					
					if (this.dbProjectNameList.containsKey(spn)) {
						sl = (ArrayList)this.dbProjectNameList.get(spn);
						sl.add(pn);						
					}
					
					else if (!this.dbProjectNameList.containsKey(spn)) {
						sl = new ArrayList();
						sl.add(pn);					
						this.dbProjectNameList.put(spn, sl);
						
					}
				}				
			
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/getDbProjectNameList] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
		
		return this.dbProjectNameList;
	}
	

	public String[] getDeleteProjectsArray() {
		return this.deleteProjectsArray;
	}

	public String[] getSelectProjectsArray() {
		return this.selectProjectsArray;
	}
	
	public String[] getSelectdbProjectsArray() {
		return this.selectdbProjectsArray;
	}

	public String[] getCopyProjectsArray() {
		return this.copyProjectsArray;
	}

	public String getForSearchStr() {
		return this.forSearchStr;
	}

	public void setSelectProjectsArray(String[] selectProjectsArray) {
		this.selectProjectsArray = selectProjectsArray;
	}
	
	public void setSelectdbProjectsArray(String[] selectdbProjectsArray) {
		this.selectdbProjectsArray = selectdbProjectsArray;
	}

	public void setDeleteProjectsArray(String[] deleteProjectsArray) {
		this.deleteProjectsArray = deleteProjectsArray;
	}

	public void setCopyProjectsArray(String[] copyProjectsArray) {
		this.copyProjectsArray = copyProjectsArray;
	}

	public void setMyProjectNameList(List myProjectNameList) {
		this.myProjectNameList = myProjectNameList;
	}
	
	public void setDbProjectNameList(HashMap dbProjectNameList) {
		this.dbProjectNameList = dbProjectNameList;
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

	public HtmlDataTable getMyFaultDataTable() {
		return myFaultDataTable;
	}

	public void setMyFaultDataTable(HtmlDataTable tmp_DataTable) {
		this.myFaultDataTable = tmp_DataTable;
	}

	public HtmlDataTable getMyProjectSummaryDataTable() {
		return myProjectSummaryDataTable;
	}

	public void setMyProjectSummaryDataTable(
			HtmlDataTable myProjectSummaryDataTable) {
		this.myProjectSummaryDataTable = myProjectSummaryDataTable;
	}

	public HtmlDataTable getMyScatterPointsTable() {
		return this.myScatterPointsTable;
	}

	public void setMyScatterPointsTable(HtmlDataTable myScatterPointsTable) {
		this.myScatterPointsTable = myScatterPointsTable;
	}

	public List getMyFaultDBEntryList() {
		return myFaultDBEntryList;
	}

	public void setFaultDBEntry(FaultDBEntry faultDBEntry) {
		this.faultDBEntry = faultDBEntry;
	}

	public FaultDBEntry getFaultDBEntry() {
		return this.faultDBEntry;
	}

	public void setJobToken(String jobToken) {
		this.jobToken = jobToken;
	}

	public String getJobToken() {
		return jobToken;
	}

	public void setFaultDBServiceUrl(String faultDBServiceUrl) {
		this.faultDBServiceUrl = faultDBServiceUrl;
	}

	public String getFaultDBServiceUrl() {
		return faultDBServiceUrl;
	}

	public void setProjectName(String projectName) {
		// Get rid of dubious characters
		projectName = filterTheBadGuys(projectName);

		// Remove spaces and less dubious stuff.
		projectName = URLDecoder.decode(projectName);
		projectName = URLEncoder.encode(projectName);
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getProjectSelectionCode() {
		return this.projectSelectionCode;
	}

	public void setProjectSelectionCode(String projectSelectionCode) {
		this.projectSelectionCode = projectSelectionCode;
	}

	public boolean getRenderChooseObsvStyleForm() {
		return this.renderChooseObsvStyleForm;
	}

	public void setRenderChooseObsvStyleForm(boolean renderChooseObsvStyleForm) {
		this.renderChooseObsvStyleForm = renderChooseObsvStyleForm;
	}

	public boolean getRenderSearchByFaultNameForm() {
		return renderSearchByFaultNameForm;
	}

	public void setRenderSearchByFaultNameForm(boolean tmp_boolean) {
		this.renderSearchByFaultNameForm = tmp_boolean;
	}
	
	public String getProjectsource() {
		return projectsource;
	}

	public void setProjectsource(String projectsource) {
		this.projectsource = projectsource;
	}
	
	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
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

	public boolean getRenderCreateNewFaultForm() {
		return renderCreateNewFaultForm;
	}

	public void setRenderCreateNewFaultForm(boolean tmp_boolean) {
		this.renderCreateNewFaultForm = tmp_boolean;
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

	public boolean getRenderDislocGridParamsForm() {
		return renderDislocGridParamsForm;
	}

	public void setRenderDislocGridParamsForm(boolean tmp_boolean) {
		this.renderDislocGridParamsForm = tmp_boolean;
	}

	public boolean getRenderMap() {
		return this.renderMap;
	}

	public void setRenderMap(boolean renderMap) {
		this.renderMap = renderMap;
	}

	public boolean getRenderFaultMap() {
		return this.renderFaultMap;
	}

	public void setRenderFaultMap(boolean renderFaultMap) {
		this.renderFaultMap = renderFaultMap;
	}

	public boolean getUsesGridPoints() {
		if (currentParams.getObservationPointStyle() == 1) {
			usesGridPoints = true;
		} else {
			usesGridPoints = false;
		}
		return usesGridPoints;
	}

	public void setUsesGridPoints(boolean usesGridPoints) {
		this.usesGridPoints = usesGridPoints;
	}

	/**
	 * Makes an array list out of the ObsvPoints of the current pararms. Useful
	 * for the JSF datatable but not much else.
	 */
	protected List reconstructMyPointObservationList(String projectName) {
		this.myPointObservationList.clear();
		try {
			ObsvPoint[] xypoints = getObsvPointsFromDB();
			if (xypoints != null && xypoints.length > 0) {
				// System.out.println(xypoints.length);
				for (int i = 0; i < xypoints.length; i++) {
					// System.out.println(i + " " + xypoints[i].getXcartPoint() + " " + xypoints[i].getcartPoint());
					myPointObservationList.add(xypoints[i]);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return this.myPointObservationList;
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
			System.out.println("[" + getUserName() +"/DislocBean/reconstructMyFaultEntryForProjectList] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return this.myFaultEntryForProjectList;
	}
	
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
			System.out.println("[" + getUserName() +"/DislocBean/reconstructMyFaultsForProjectList] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		reconstructMyFaultEntryForProjectList(projectName);
		return this.myFaultsForProjectList;
	}
	

	protected List reconstructMyObservationsForProjectList(String projectName) {
		// List myObsvEntryForProjectList=new ArrayList();
		this.myObsvEntryForProjectList.clear();
		ObjectContainer db = null;
		try {
			
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			// Unlike faults, there will only be one project bean.
			ObjectSet results = db.get(DislocProjectBean.class);
			while (results.hasNext()) {
				Object tmp = results.next();
				obsvEntryForProject tmp_myObsvEntryForProject = new obsvEntryForProject();
				tmp_myObsvEntryForProject.view = false;
				tmp_myObsvEntryForProject.delete = false;
				this.myObsvEntryForProjectList.add(tmp_myObsvEntryForProject);
			}

		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/reconstructMyObservationsForProjectList] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return this.myObsvEntryForProjectList;
	}

	protected List reconstructMyObsvForProjectList(String projectName) {
		// List myObsvEntryForProjectList=new ArrayList();
		this.myObsvEntryForProjectList.clear();
		ObjectContainer db = null;
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			// Unlike faults, there will only be one project bean.
			ObjectSet results = db.get(DislocParamsBean.class);
			while (results.hasNext()) {
				Object tmp = results.next();
				obsvEntryForProject tmp_myObsvEntryForProject = new obsvEntryForProject();
				tmp_myObsvEntryForProject.view = false;
				tmp_myObsvEntryForProject.delete = false;
				this.myObsvEntryForProjectList.add(tmp_myObsvEntryForProject);
			}
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/reconstructMyObsvForProjectList] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return this.myObsvEntryForProjectList;
	}

	public List getMyObservationsForProjectList() throws Exception {
		String projectName = getProjectName();
		return reconstructMyObservationsForProjectList(projectName);
	}

	public void setMyObservationsForProjectList(List tmp_list) {
		this.myObservationsForProjectList = tmp_list;
	}

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
	
	public void setMyObsvEntryForProjectList(List tmp_list) {
		this.myObsvEntryForProjectList = tmp_list;
	}

	public List getMyObsvEntryForProjectList() throws Exception {
		String projectName = getProjectName();
		return reconstructMyObsvForProjectList(projectName);
	}

	public String getFaultSelectionCode() {
		return faultSelectionCode;
	}

	public void setFaultSelectionCode(String tmp_str) {
		this.faultSelectionCode = tmp_str;
	}

	public String getObsvStyleSelectionCode() {
		return obsvStyleSelectionCode;
	}

	public void setObsvStyleSelectionCode(String obsvStyleSelectionCode) {
		this.obsvStyleSelectionCode = obsvStyleSelectionCode;
	}

	public Fault getCurrentFault() {
		return currentFault;
	}

	public void setCurrentFault(Fault currentFault) {
		this.currentFault = currentFault;
	}

	// public void setOrigin_lat(String tmp_str) {
	// this.origin_lat=tmp_str;
	// }
	// public String getOrigin_lat() {
	// return this.origin_lat;
	// }
	// public void setOrigin_lon(String tmp_str) {
	// this.origin_lon=tmp_str;
	// }
	// public String getOrigin_lon() {
	// return this.origin_lon;
	// }

	protected Fault populateFaultFromContext(String tmp_faultName)
			throws Exception {
		String faultStatus = "Update";
		ObjectContainer db = null;

		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");

			// Fault faultToGet=new Fault();
			// faultToGet.setFaultName(tmp_faultName);
			// ObjectSet results=db.get(faultToGet);

			ObjectSet results = db.get(Fault.class);
			Fault currentFault = null;
			while (results.hasNext()) {
				currentFault = (Fault) results.next();
				if (currentFault.getFaultName().equals(tmp_faultName)) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/populateFaultFromContext] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return currentFault;
	}

	protected DislocParamsBean populateParamsFromContext(String projectName)
			throws Exception {

		DislocParamsBean params = null;
		ObjectContainer db = null;
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");
			System.out.println("[" + getUserName() +"/DislocBean/populateParamsFromContext]Populating params from context.");
			ObjectSet results = db.get(DislocParamsBean.class);
			// Should only have one value.

			if (results.hasNext()) {
				params = (DislocParamsBean) results.next();
			}

		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/populateParamsFromContext] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return params;
	}

	public void setMyPointObservationList(List myPointObservationList) {
		this.myPointObservationList = myPointObservationList;
	}

	/**
	 * This should read from the db.
	 */
	public List getMyPointObservationList() {
		return reconstructMyPointObservationList(projectName);
	}

	public void setMyArchivedDislocResultsList(List myArchivedDislocResultsList) {
		this.myArchivedDislocResultsList = myArchivedDislocResultsList;
	}

	public List getMyArchivedDislocResultsList() {
		myArchivedDislocResultsList.clear();
		List tmpList = new ArrayList();

		ObjectContainer db = null;
		try {
				db = Db4o.openFile(getBasePath() + "/" + getContextBasePath()
						+ "/" + userName + "/" + codeName + ".db");
				ObjectSet results = db.get(new DislocProjectSummaryBean());

				while (results.hasNext()) {
					DislocProjectSummaryBean dpsb = (DislocProjectSummaryBean) results
							.next();
					// myArchivedDislocResultsList.add(dpsb);
					tmpList.add(dpsb);
				}
				myArchivedDislocResultsList = sortByDate(tmpList);
			
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/getMyArchivedDislocResultsList] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		return myArchivedDislocResultsList;
	}

	public void setKmlGeneratorUrl(String tmp_str) {
		this.kmlGeneratorUrl = tmp_str;
	}

	public String getKmlGeneratorUrl() {
		return this.kmlGeneratorUrl;
	}

	public void setDislocExtendedServiceUrl(String dislocExtendedServiceUrl) {
		this.dislocExtendedServiceUrl = dislocExtendedServiceUrl;
	}

	public String getDislocExtendedServiceUrl() {
		return dislocExtendedServiceUrl;
	}

	public void setDislocServiceUrl(String dislocServiceUrl) {
		this.dislocServiceUrl = dislocServiceUrl;
	}

	public String getDislocServiceUrl() {
		return dislocServiceUrl;
	}

	public void setKmlProjectFile(String kmlProjectFile) {
		this.kmlProjectFile = kmlProjectFile;
	}

	public String getKmlProjectFile() {
		return this.kmlProjectFile;
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
			System.out.println("[" + getUserName() +"/DislocBean/downloadKmlFile] Unable to download kml file");
			ex.printStackTrace();
		}
	}

	/**
	 * Some of the stuff below should be abstracted to the generic project
	 * classes.
	 */

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
			// orderedList.add((DislocProjectSummaryBean)fullList.get(((Integer)reducedList.get(first)).intValue()));
			orderedList.add((TimeOrderedInterface) fullList
					.get(((Integer) reducedList.get(first)).intValue()));
			reducedList.remove(first);
		}
		return orderedList;
	}

	protected int getFirst(List reducedList, List fullList) {
		int first = 0;
		for (int i = 1; i < reducedList.size(); i++) {
			// DislocProjectSummaryBean
			// mb1=(DislocProjectSummaryBean)fullList.get(((Integer)reducedList.get(first)).intValue());
			// DislocProjectSummaryBean
			// mb2=(DislocProjectSummaryBean)fullList.get(((Integer)reducedList.get(i)).intValue());

			TimeOrderedInterface mb1 = (TimeOrderedInterface) fullList
					.get(((Integer) reducedList.get(first)).intValue());
			TimeOrderedInterface mb2 = (TimeOrderedInterface) fullList
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

	public void setCurrentSummary(DislocProjectSummaryBean currentSummary) {
		this.currentSummary = currentSummary;
	}

	public DislocProjectSummaryBean getCurrentSummary() {
		return this.currentSummary;
	}

	public void setCurrentProject(DislocProjectBean currentProject) {
		this.currentProject = currentProject;
	}

	public DislocProjectBean getCurrentProject() {
		return currentProject;
	}

	public void setCurrentParams(DislocParamsBean currentParams) {
		this.currentParams = currentParams;
	}

	public DislocParamsBean getCurrentParams() {
		return currentParams;
	}

	public String getObsvKmlFilename() {
		return obsvKmlFilename;
	}

	public void setObsvKmlFilename(String obsvKmlFilename) {
		this.obsvKmlFilename = obsvKmlFilename;
	}

	public String getObsvKmlUrl() {
		obsvKmlUrl = createObsvKmlFile();
		return obsvKmlUrl;
	}

	public void setObsvKmlUrl(String obsvKmlUrl) {
		this.obsvKmlUrl = obsvKmlUrl;
	}

	/**
	 * Create a KML file of the point observations. The method assumes access to
	 * global variables. This method is used to plot INPUT values for disloc.
	 */
	public String createObsvKmlFile() {
		String newObsvFilename = "";
		String oldLocalDestination = this.getBasePath() + "/" + "gridsphere"
				+ "/" + getObsvKmlFilename();
		String localDestination = "";

		try {
			// Delete the old file.
			System.out.println("[" + getUserName() +"/DislocBean/createObsvKmlFile] Old fault kml file:" + localDestination);
			File oldFile = new File(oldLocalDestination);
			if (oldFile.exists()) {
				System.out.println("[" + getUserName() +"/DislocBean/createObsvKmlFile] Deleting old fault kml file");
				oldFile.delete();
			}

			long timeStamp = (new Date()).getTime();
			newObsvFilename = userName + "-" + codeName + "-" + projectName
					+ "-" + timeStamp + ".kml";
			setObsvKmlFilename(newObsvFilename);
			// This should be the new file name.
			localDestination = this.getBasePath() + "/" + "gridsphere" + "/"
					+ getObsvKmlFilename();

			PrintWriter out = new PrintWriter(new FileWriter(localDestination));

			if (currentParams.getObservationPointStyle() == 1) {
				DislocParamsBean dpb = getDislocParamsFromDB(projectName);
				// Get data points
				int xint = dpb.getGridXIterations();
				int yint = dpb.getGridYIterations();
				double xspacing = dpb.getGridXSpacing();
				double yspacing = dpb.getGridYSpacing();
				double xmin = dpb.getGridMinXValue();
				double ymin = dpb.getGridMinYValue();
				double originLat = dpb.getOriginLat();
				double originLon = dpb.getOriginLon();

				// Make the KML
				out.println(xmlHead);
				out.println(kmlHead);
				out.println(docBegin);

				double cartX, cartY;
				double lat, lon;

				for (int i = 0; i < xint; i++) {
					cartX = xmin + i * xspacing;
					for (int j = 0; j < yint; j++) {
						cartY = ymin + j * yspacing;
						lon = cartX / factor(originLon, originLat) + originLon;
						lat = cartY / 111.32 + originLat;
						writeKmlPoint(lat, lon, out);
					}
				}
				out.println(docEnd);
				out.println(kmlEnd);
				out.flush();
				out.close();
			}

			// Default to scatter style
			else {
				ObsvPoint[] points = getObsvPointsFromDB();
				out.println(xmlHead);
				out.println(kmlHead);

				if (points != null && points.length > 0) {
					out.println(docBegin);
					for (int i = 0; i < points.length; i++) {
						writeKmlPoint(Double.parseDouble(points[i]
								.getLatPoint()), Double.parseDouble(points[i]
								.getLonPoint()), out);
					}
					out.println(docEnd);
					out.println(kmlEnd);
					out.flush();
					out.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		String returnString = portalBaseUrl + "/gridsphere/"
				+ getObsvKmlFilename();
		System.out.println("[" + getUserName() +"/DislocBean/createObsvKmlFile] KML:" + returnString);
		return returnString;
	}

	protected void writeKmlPoint(double lat, double lon, PrintWriter out) {
		out.println(pmBegin);
		out.println(descBegin);
		out.println("<b>Observation Point:</b> " + lon + comma + lat);
		out.println(descEnd);
		out.println(pointBegin);
		out.println(coordBegin);
		out.println(lon + comma + lat + comma + "0");
		out.println(coordEnd);
		out.println(pointEnd);
		out.println(pmEnd);
	}

	public String getFaultKmlFilename() {
		return faultKmlFilename;
	}

	public void setFaultKmlFilename(String faultKmlFilename) {
		this.faultKmlFilename = faultKmlFilename;
	}

	/**
	 * Create a KML file of the faults. The method assumes access to global
	 * variables.
	 */
	public String createFaultKmlFile() {
		String newFaultFilename = "";
		String oldLocalDestination = this.getBasePath() + "/" + "gridsphere"
				+ "/" + getFaultKmlFilename();
		String localDestination = "";

		try {
			// Delete the old file.
			System.out.println("[" + getUserName() +"/DislocBean/createFaultKmlFile] Old fault kml file:" + localDestination);
			File oldFile = new File(oldLocalDestination);
			if (oldFile.exists()) {
				System.out.println("[" + getUserName() +"/DislocBean/createFaultKmlFile] Deleting old fault kml file");
				oldFile.delete();
			}

			long timeStamp = (new Date()).getTime();
			newFaultFilename = userName + "-" + codeName + "-" + projectName
					+ "-" + timeStamp + ".kml";
			setFaultKmlFilename(newFaultFilename);
			// This should be the new file name.
			localDestination = this.getBasePath() + "/" + "gridsphere" + "/"
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
					out.println(faults[i].getFaultLonStart() + comma
							+ faults[i].getFaultLatStart() + comma + "0");
					out.println(faults[i].getFaultLonEnd() + comma
							+ faults[i].getFaultLatEnd() + comma + "0");
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

		String returnString = portalBaseUrl + "/gridsphere/"
				+ getFaultKmlFilename();
		System.out.println("[" + getUserName() +"/DislocBean/createFaultKmlFile] KML:" + returnString);
		return returnString;
	}

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

	public Fault[] getProjectFaultsFromDB(String userName, String projectName,
			String codeName, String basePath, String relPath) {
		Fault[] returnFaults = null;
		System.out.println("[" + getUserName() +"/DislocBean/getProjectFaultsFromDB] Opening Fault DB:" + basePath + "/" + relPath + "/"
				+ userName + "/" + codeName + "/" + projectName + ".db");

		ObjectContainer db = null;
		try {
			db = Db4o.openFile(basePath + "/" + relPath + "/" + userName + "/"
					+ codeName + "/" + projectName + ".db");
			Fault faultToGet = new Fault();
			ObjectSet results = db.get(faultToGet);
			if (results.hasNext()) {
				returnFaults = new Fault[results.size()];
				for (int i = 0; i < results.size(); i++) {
					returnFaults[i] = (Fault) results.next();
				}
			}
			
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/getProjectFaultsFromDB] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
		return returnFaults;
	}

	public String getGpsStationName() {
		return gpsStationName;
	}

	public void setGpsStationName(String gpsStationName) {
		this.gpsStationName = gpsStationName;
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

	public void toggleAddPointObsvForProject(ActionEvent ev) {
		String space = " ";
		System.out.println("[" + getUserName() +"/DislocBean/toggleAddPointObsvForProject] Here are the choices:" + space + gpsStationLat
				+ space + gpsStationLon);

		// Project origin is not set, so set it.
		if (currentParams.getOriginLat() == DislocParamsBean.DEFAULT_LAT
				&& currentParams.getOriginLon() == DislocParamsBean.DEFAULT_LON) {
			currentParams.setOriginLat(Double.parseDouble(gpsStationLat));
			currentParams.setOriginLon(Double.parseDouble(gpsStationLon));
		}
		double dLat = Double.parseDouble(gpsStationLat);
		double dLon = Double.parseDouble(gpsStationLon);
		ObsvPoint point = convertLatLon(Double.parseDouble(df.format(dLat)),
				Double.parseDouble(df.format(dLon)), currentParams
						.getOriginLat(), currentParams.getOriginLon());

		// Update the current project
		ObsvPoint[] allpoints = getObsvPointsFromDB();
		ObsvPoint[] newpoints;

		if (allpoints == null || allpoints.length < 0) {
			newpoints = new ObsvPoint[1];
			newpoints[0] = point;
		} else {
			newpoints = new ObsvPoint[allpoints.length + 1];
			System.arraycopy(allpoints, 0, newpoints, 0, allpoints.length);
			newpoints[allpoints.length] = point;
		}

		// Update the DB.
		try {
			storeObsvPointsInDB(newpoints);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected void storeParamsInDB() throws Exception {
		ObjectContainer db = null;
		
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");
			ObjectSet result = db.get(DislocParamsBean.class);
			if (result.hasNext()) {
				DislocParamsBean tmp = (DislocParamsBean) result.next();
				db.delete(tmp);
			}
			db.set(currentParams);

			// Say goodbye.
			db.commit();

		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/storeParamsInDB] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

	}

	protected void storeObsvPointsInDB(ObsvPoint[] points) throws Exception {
		ObjectContainer db = null;
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");
			ObjectSet result = db.get(ObsvPoint.class);
			while (result.hasNext()) {
				ObsvPoint tmp = (ObsvPoint) result.next();
				db.delete(tmp);
			}
			for (int i = 0; i < points.length; i++) {
				db.set(points[i]);

			}
			// Say goodbye.
			db.commit();
			
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/storeObsvPointsInDB] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

	}

	/**
	 * Converts the provided lat and lon into cartesian coordinates.
	 */
	protected ObsvPoint convertLatLon(double lat, double lon,
			double origin_lat, double origin_lon) {
		double x = (lon - origin_lon) * factor(origin_lon, origin_lat);
		double y = (lat - origin_lat) * 111.32;

		x = Double.parseDouble(df.format(x));
		y = Double.parseDouble(df.format(y));

		System.out.println("[" + getUserName() +"/DislocBean/convertLatLon] ObsvPoints:" + lat + " " + lon + " " + x + " " + y);

		ObsvPoint point = new ObsvPoint();
		point.setXcartPoint(x + "");
		point.setYcartPoint(y + "");
		point.setLatPoint(lat + "");
		point.setLonPoint(lon + "");

		return point;
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

	public void deleteScatterPoint(ActionEvent ev) throws Exception {
		System.out.println("[" + getUserName() +"/DislocBean/deleteScatterPoint] Deleting point");
		List scatterPointList = reconstructMyPointObservationList(projectName);

		// Delete the entry from the list.
		ObsvPoint deadPoint = (ObsvPoint) getMyScatterPointsTable()
				.getRowData();
		int rowValue = getMyScatterPointsTable().getRowIndex();
		System.out.println("[" + getUserName() +"/DislocBean/deleteScatterPoint] Selected row value:" + rowValue);
		scatterPointList.remove(rowValue);

		// Remove the old db

		ObjectContainer db = null;
		try {

			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + "/" + projectName + ".db");
			// System.out.println(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");
			ObjectSet results = db.get(ObsvPoint.class);
			while (results.hasNext()) {
				db.delete(results.next());

			}
			db.commit();
			
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/deleteScatterPoint] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}

		// Convert array list to standard array.
		ObsvPoint[] newPoints = new ObsvPoint[scatterPointList.size()];
		for (int i = 0; i < scatterPointList.size(); i++) {
			newPoints[i] = (ObsvPoint) scatterPointList.get(i);
		}
		// Store the new db.
		storeObsvPointsInDB(newPoints);
	}

	// --------------------------------------------------
	/**
	 * Needed to make the map interface work
	 **/
	public void toggleSetFaultFromMap(ActionEvent ev) throws Exception {
		renderFaultMap = false;
		try {
			System.out.println("[" + getUserName() +"/DislocBean/toggleSetFaultFromMap] Adding fault from map");

			initEditFormsSelection();

			String dbQuery = getMapFaultName();
			if (dbQuery.compareTo("polygon") != 0) {
				System.out.println("[" + getUserName() +"/DislocBean/toggleSetFaultFromMap] get desc :" + dbQuery);
				currentFault = QueryFaultFromDB(dbQuery);
				renderCreateNewFaultForm = true;
			}
		} catch (Exception ex) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleSetFaultFromMap] Map fault selection error.");
			ex.printStackTrace();
		}
	}
	
	
	String mapFaultName;

	public void setMapFaultName(String mapFaultName) {
		this.mapFaultName = mapFaultName;
	}

	public String getMapFaultName() {
		return this.mapFaultName;
	}

	// --------------------------------------------------

	/**
	 * These methods are used by the InSAR KML Generation service
	 */
	public void setAzimuth(String azimuth) {
		this.azimuth = azimuth;
	}

	public void setElevation(String elevation) {
		this.elevation = elevation;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public void setInsarkmlServiceUrl(String insarkmlServiceUrl) {
		this.insarkmlServiceUrl = insarkmlServiceUrl;
	}

	public void setInsarKmlUrl(String insarKmlUrl) {
		this.insarKmlUrl = insarKmlUrl;
	}

	public String getAzimuth() {
		return azimuth;
	}

	public String getElevation() {
		return elevation;
	}

	public String getFrequency() {
		return frequency;
	}

	public String getInsarkmlServiceUrl() {
		return insarkmlServiceUrl;
	}

	public String getInsarKmlUrl() {
		return insarKmlUrl;
	}

	public HtmlDataTable getMyInsarDataTable() {
		return myInsarDataTable;
	}

	public void setMyInsarDataTable(HtmlDataTable myInsarDataTable) {
		this.myInsarDataTable = myInsarDataTable;
	}

	public void setMyInsarParamsList(List myInsarParamsList) {
		this.myInsarParamsList = myInsarParamsList;
	}

	public List getMyInsarParamsList() {
		myInsarParamsList.clear();
		List tmpList = new ArrayList();
		ObjectContainer db = null;
		try {
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath()
						+ "/" + userName + "/" + codeName + ".db");
				ObjectSet results = db.get(new InsarParamsBean());

				while (results.hasNext()) {
					InsarParamsBean ipb = (InsarParamsBean) results.next();
					tmpList.add(ipb);
				}
				myInsarParamsList = sortByDate(tmpList);
			
		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/getMyInsarParamsList] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
		return myInsarParamsList;
	}

	public void toggleDeleteInsar() throws Exception {
		ObjectContainer db = null;
		try {
			InsarParamsBean ipb = (InsarParamsBean) getMyInsarDataTable().getRowData();
			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + ".db");

			ObjectSet results2 = db.get(ipb);
			System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteInsar] Number of matches:" + results2.size());

			if (results2.hasNext()) {
				InsarParamsBean delinsar = (InsarParamsBean) results2.next();
				System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteInsar] Deleting insar params");
				db.delete(delinsar);
			}

		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleDeleteInsar] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
	}

	public void toggleReplotInsar() throws Exception {
		ObjectContainer db = null;
		try {
			// Get the project
			InsarParamsBean ipb = (InsarParamsBean) getMyInsarDataTable()
					.getRowData();
			ipb.setCreationDate(new Date().toString());

			// Invoke the service
			InsarKmlService iks = new InsarKmlServiceServiceLocator()
					.getInsarKmlExec(new URL(insarkmlServiceUrl));
			insarKmlUrl = iks.runBlockingInsarKml(userName, ipb
					.getProjectName(), ipb.getDislocOutputUrl(), ipb
					.getElevation(), ipb.getAzimuth(), ipb.getFrequency(),
					"ExecInsarKml");

			// Now update the database
			ipb.setInsarKmlUrl(insarKmlUrl);
			ipb.setCreationDate((new Date()).toString());			

			db = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/"
					+ userName + "/" + codeName + ".db");
			ObjectSet results = db.get(InsarParamsBean.class);
			System.out.println("[" + getUserName() +"/DislocBean/toggleReplotInsar] Result set size: " + results.size());

			while (results.hasNext()) {
				InsarParamsBean tmpbean = (InsarParamsBean) results.next();
				db.set(ipb);
			}
			db.commit();

		} catch (Exception e) {
			System.out.println("[" + getUserName() +"/DislocBean/toggleReplotInsar] " + e);
		}
		finally {
			if (db != null)
				db.close();			
		}
	}

	// --------------------------------------------------
}
