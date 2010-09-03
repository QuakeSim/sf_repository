package cgl.quakesim.disloc;

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


import TestClient.Select.Select;
import TestClient.Select.SelectService;
import TestClient.Select.SelectServiceLocator;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;



import com.db4o.*;

/**
 * Everything you need to set up and run MeshGenerator.
 */

public class DislocBean extends GenericSopacBean {

	// Some navigation strings.
	static final String DEFAULT_USER_NAME = "disloc_default_user";
	static final String DISLOC_NAV_STRING = "disloc-submitted";
	static final String SEPARATOR = "/";
	String azimuth = "0";
	String codeName;
	String comma = ", ";
	String coordBegin = "<coordinates>";
	String coordEnd = "</coordinates>";
	String[] copyProjectsArray;
	Fault currentFault = new Fault();
	// DislocParamsBean dislocParams=new DislocParamsBean();
	DislocParamsBean currentParams = new DislocParamsBean();
	DislocProjectBean currentProject = new DislocProjectBean();
	DislocProjectSummaryBean currentSummary = new DislocProjectSummaryBean();
	HashMap dbProjectNameList = new HashMap();
	ObjectServer dbs = null;
	String[] deleteProjectsArray;

	String descBegin = "<description>";
	String descEnd = "</description>";
	DecimalFormat df;

	DislocExtendedService dislocExtendedService;
	String dislocExtendedServiceUrl;
	// Service information
	DislocService dislocService;
	String dislocServiceUrl;
	String docBegin = "<Document>";
	String docEnd = "</Document>";
	String elevation = "60";
	FaultDBEntry faultDBEntry;
	String faultDBServiceUrl;
	boolean faultdrawing = false;
	String faultKmlFilename;
	String faultKmlUrl;

	String faultLatEnd = new String();
	String faultLatStart = new String();
	String faultLonEnd = new String();
	String faultLonStart = new String();
	String faultName = "newfault";
	String faultSelectionCode = "";

	String forSearchStr = "";
	String frequency = "1.26";
	String gpsStationLat = "";
	String gpsStationLon = "";
	String gpsStationName = "";
	
	String insarkmlServiceUrl;
	String insarKmlUrl;

	String jobToken = "";

	String kmlEnd = "</kml>";
	String kmlfiles = "";
	
	String kmlGeneratorBaseurl;
	String kmlGeneratorUrl;

	String kmlHead = "<kml xmlns=\"http://earth.google.com/kml/2.2\">";

	String kmlProjectFile = "network0.kml";
	String lsBegin = "<LineString>";
	String lsEnd = "</LineString>";
	String mapFaultName;
	List myArchivedDislocResultsList = new ArrayList();

	List myDislocParamsCollection = new ArrayList();
	List myFaultCollection = new ArrayList();
	HtmlDataTable myFaultDataTable, myProjectSummaryDataTable;

	List myFaultDBEntryList = new ArrayList();
	List myFaultEntryForProjectList = new ArrayList();
	List myFaultsForProjectList = new ArrayList();
	int myFaultsForProjectListsize;
	List myInsarParamsList = new ArrayList();
	List myInterpIdList = new ArrayList();

	List myObservationsForProjectList = new ArrayList();
	List myObsvEntryForProjectList = new ArrayList();
	List myPointObservationList = new ArrayList();
	List myProjectNameList = new ArrayList();
	HtmlDataTable myScatterPointsTable, myInsarDataTable;
	String obsvKmlFilename;

	String obsvKmlUrl;
	String obsvStyleSelectionCode = "";

	double originLon, originLat;
	String pmBegin = "<Placemark>";
	String pmEnd = "</Placemark>";
	String pointBegin = "<Point>";
	String pointEnd = "</Point>";
	String portalBaseUrl;
	String projectSelectionCode = "";

	String projectsource = "";

	String realPath;
	boolean renderAddFaultFromDBForm = false;
	boolean renderAddFaultSelectionForm = false;
	boolean renderChooseObsvStyleForm = false;

	/**
	 * The following are property fields. Associated get/set methods are at the
	 * end of the code listing.
	 */
	boolean renderCreateNewFaultForm = false;
	boolean renderDislocGridParamsForm = false;
	boolean renderFaultMap = false;
	boolean renderMap = false;

	boolean renderSearchByAuthorForm = false;

	boolean renderSearchByFaultNameForm = false;
	boolean renderSearchByLatLonForm = false;
	boolean renderViewAllFaultsForm = false;
	// These are useful object lists.
	String[] selectProjectsArray;
	boolean usesGridPoints;
	
	
	String automatedDislocServiceUrl;
	

	public String getAutomatedDislocServiceUrl() {
		return automatedDislocServiceUrl;
	}
	public void setAutomatedDislocServiceUrl(String automatedDislocServiceUrl) {
		this.automatedDislocServiceUrl = automatedDislocServiceUrl;
	}

	// KML stuff, need to move this to another place.
	String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	/**
	 * The client constructor.
	 */
	public DislocBean() throws Exception {
		super();
		faultDBEntry = new FaultDBEntry();
		df = new DecimalFormat(".###");
		// currentParams.setObservationPointStyle(1);

		// We are done.
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/DislocBean] Primary Disloc Bean Created");
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

		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/convertLatLon] ObsvPoints:" + lat + " " + lon + " " + x + " " + y);

		ObsvPoint point = new ObsvPoint();
		point.setXcartPoint(x + "");
		point.setYcartPoint(y + "");
		point.setLatPoint(lat + "");
		point.setLonPoint(lon + "");

		return point;
	}
	public void createFaultFromMap() {

		Fault tmp_fault = new Fault();
		System.out.println ("[" + getUserName() + "/RssDisloc3/DislocBean/createFaultFromMap] started");

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
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createFaultFromMap] Origin:" + currentParams.getOriginLat() + " " + currentParams.getOriginLon());
		if (currentParams.getOriginLat() == DislocParamsBean.DEFAULT_LAT || currentParams.getOriginLon() == DislocParamsBean.DEFAULT_LON) {
			currentParams.setOriginLat(latStart);
			currentParams.setOriginLon(lonStart);
			// Update the parameters

			ObjectContainer projectdb = null;

			try {

				projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

				ObjectSet result = projectdb.get(DislocParamsBean.class);
				if (result.hasNext()) {
					DislocParamsBean tmp = (DislocParamsBean) result.next();
					projectdb.delete(tmp);
				}

				projectdb.set(currentParams);

				// Say goodbye.
				projectdb.commit();

			} catch (Exception e) {

				System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createFaultFromMap] " + e);
			}
			finally {
				if (projectdb!=null)
					projectdb.close();
			}

		}
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createFaultFromMap] Updated Origin:" + currentParams.getOriginLat() + " " + currentParams.getOriginLon());

		// The following should be done in any case.
		// If the origin was just (re)set above,
		// we will get a harmless (0,0);
		double x1 = (lonStart - currentParams.getOriginLon()) * factor(currentParams.getOriginLon(), currentParams.getOriginLat());

		double y1 = (latStart - currentParams.getOriginLat()) * 111.32;
		System.out.println("Fault origin: " + x1 + " " + y1);

		tmp_fault.setFaultLocationX(Double.parseDouble(df.format(x1)));
		tmp_fault.setFaultLocationY(Double.parseDouble(df.format(y1)));

		// tmp_fault.setFaultLocationX(x1);
		// tmp_fault.setFaultLocationY(y1);

		currentFault = tmp_fault;
	}
	/**
	 * Create a KML file of the faults. The method assumes access to global
	 * variables.
	 */
	public String createFaultKmlFile() {
		String newFaultFilename = "";
		String oldLocalDestination = this.getBasePath() + "/" + "gridsphere" + "/" + getFaultKmlFilename();
		String localDestination = "";

		try {
			// Delete the old file.
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createFaultKmlFile] Old fault kml file:" + localDestination);
			File oldFile = new File(oldLocalDestination);
			if (oldFile.exists()) {
				System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createFaultKmlFile] Deleting old fault kml file");
				oldFile.delete();
			}

			long timeStamp = (new Date()).getTime();
			newFaultFilename = userName + "-" + codeName + "-" + projectName + "-" + timeStamp + ".kml";
			setFaultKmlFilename(newFaultFilename);
			// This should be the new file name.
			localDestination = this.getBasePath() + "/" + "gridsphere" + "/" + getFaultKmlFilename();

			Fault[] faults = getProjectFaultsFromDB(userName, projectName, codeName, getBasePath(), getContextBasePath());
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
					out.println(faults[i].getFaultLonStart() + comma + faults[i].getFaultLatStart() + comma + "0");
					out.println(faults[i].getFaultLonEnd() + comma + faults[i].getFaultLatEnd() + comma + "0");
					out.println(coordEnd);
					out.println(lsEnd);
					out.println(pmEnd);
				}
				out.println(docEnd);
				out.println(kmlEnd);
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String returnString = portalBaseUrl + "/gridsphere/" + getFaultKmlFilename();
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createFaultKmlFile] KML : " + returnString);
		return returnString;
	}


	/**
	 * This method is used to generate the output plots with the remote KML
	 * service.
	 */
	protected String createKml(DislocParamsBean dislocParams, DislocResultsBean dislocResultsBean, Fault[] faults)
	throws Exception {
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createKml] Started");
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createKml] Creating the KML file at " + kmlGeneratorUrl);

		// Get the project lat/lon origin. It is the lat/lon origin of the first fault.
		String origin_lat = dislocParams.getOriginLat() + "";
		String origin_lon = dislocParams.getOriginLon() + "";

		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createKml] Origin: " + origin_lon + " " + origin_lat);

		// get my kml
		SimpleXDataKml kmlService;
		SimpleXDataKmlServiceLocator locator = new SimpleXDataKmlServiceLocator();
		locator.setMaintainSession(true);
		kmlService = locator.getKmlGenerator(new URL(kmlGeneratorUrl));

		PointEntry[] tmp_pointentrylist = LoadDataFromUrl(dislocResultsBean.getOutputFileUrl());

		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createKml] the size of tmp_pointentrylist : " + tmp_pointentrylist.length);
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createKml] dislocResultsBean.getOutputFileUrl() " + dislocResultsBean.getOutputFileUrl());

		kmlService.setDatalist(tmp_pointentrylist);
		kmlService.setOriginalCoordinate(origin_lon, origin_lat);
		kmlService.setCoordinateUnit("1000");

		// These plot grid lines.
		double start_x, start_y, end_x, end_y, xiterationsNumber, yiterationsNumber;
		start_x = Double.valueOf(dislocParams.getGridMinXValue()).doubleValue();
		start_y = Double.valueOf(dislocParams.getGridMinYValue()).doubleValue();
		xiterationsNumber = Double.valueOf(dislocParams.getGridXIterations()).doubleValue();
		yiterationsNumber = Double.valueOf(dislocParams.getGridYIterations())
		.doubleValue();
		int xinterval = (int) (Double.valueOf(dislocParams.getGridXSpacing()).doubleValue());
		int yinterval = (int) (Double.valueOf(dislocParams.getGridYSpacing()).doubleValue());
		end_x = start_x + xinterval * (xiterationsNumber - 1);
		end_y = start_y + yinterval * (yiterationsNumber - 1);

		// kmlService.setGridLine("Grid Line", start_x, start_y, end_x, end_y,
		// xinterval,yinterval);
		// kmlService.setPointPlacemark("Icon Layer");
		// kmlService.setArrowPlacemark("Arrow Layer", "ff66a1cc", 2);
		kmlService.setArrowPlacemark("Arrow Layer", "#000000", 0.95);

		// Plot the faults
		for (int i = 0; i < faults.length; i++) {
			kmlService.setFaultPlot("", faults[i].getFaultName() + "",faults[i].getFaultLonStart() + "", faults[i].getFaultLatStart()+ "", faults[i].getFaultLonEnd() + "", faults[i].getFaultLatEnd()+ "", "ff6af0ff", 5);
		}

		String myKmlUrl = kmlService.runMakeKml("", userName, projectName, (dislocResultsBean.getJobUIDStamp()).hashCode() + "");
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createKml] Finished");
		return myKmlUrl;
	}
	/**
	 * This method stores the new project in the DB and updates the
	 * currentProject
	 */
	protected void createNewProject(String projectName) {
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createNewProject] Creating new project");
		makeProjectDirectory();

		ObjectContainer codedb = null;

		try {

			codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");
			DislocProjectBean tmp = new DislocProjectBean();
			ObjectSet results = codedb.get(DislocProjectBean.class);

			// Create a new project. This may be overwritten later
			currentProject = new DislocProjectBean();
			currentProject.setProjectName(projectName);

			// See if project already exists
			while (results.hasNext()) {
				tmp = (DislocProjectBean) results.next();
				// This is a screwed up project so delete it.
				if (tmp == null || tmp.getProjectName() == null) {
					codedb.delete(tmp);
				}
				// The project already exists, so update it.
				else if (tmp.getProjectName().equals(projectName)) {
					codedb.delete(tmp);
					currentProject = tmp;
					break;
				}
			}
			codedb.set(currentProject);
			codedb.commit();

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createNewProject] " + e);
		}

		finally {
			if (codedb!=null)
				codedb.close();
		}
		currentParams = getDislocParamsFromDB(projectName);
	}



	/**
	 * Create a KML file of the point observations. The method assumes access to
	 * global variables. This method is used to plot INPUT values for disloc.
	 */
	public String createObsvKmlFile() {
		String newObsvFilename = "";
		String oldLocalDestination = this.getBasePath() + "/" + "gridsphere" + "/" + getObsvKmlFilename();
		String localDestination = "";

		try {
			// Delete the old file.
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createObsvKmlFile] Old fault kml file:" + localDestination);
			File oldFile = new File(oldLocalDestination);
			if (oldFile.exists()) {
				System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createObsvKmlFile] Deleting old fault kml file");
				oldFile.delete();
			}

			long timeStamp = (new Date()).getTime();
			newObsvFilename = userName + "-" + codeName + "-" + projectName + "-" + timeStamp + ".kml";
			setObsvKmlFilename(newObsvFilename);
			// This should be the new file name.
			localDestination = this.getBasePath() + "/" + "gridsphere" + "/" + getObsvKmlFilename();

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
						writeKmlPoint(Double.parseDouble(points[i].getLatPoint()), Double.parseDouble(points[i].getLonPoint()), out);
					}
					out.println(docEnd);
					out.println(kmlEnd);
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String returnString = portalBaseUrl + "/gridsphere/"
		+ getObsvKmlFilename();
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/createObsvKmlFile] KML : " + returnString);
		return returnString;
	}

	// --------------------------------------------------
	// This section contains the main execution calls.
	// --------------------------------------------------

	public void deleteObsv(ActionEvent ev) throws Exception {

		ObjectContainer projectdb = null;

		try {

			initEditFormsSelection();

			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/deleteObsv] Deleting an observation");
			// There should only be one of these at most.
			// Delete the stored one and replace it with the new one.

			ObjectSet result = projectdb.get(DislocParamsBean.class);
			if (result.hasNext()) {
				DislocParamsBean tmp = (DislocParamsBean) result.next();
				projectdb.delete(tmp);
			}
			projectdb.commit();

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/deleteObsv] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}
	}

	public void deleteScatterPoint(ActionEvent ev) throws Exception {
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/deleteScatterPoint] Deleting point");
		List scatterPointList = reconstructMyPointObservationList(projectName);

		// Delete the entry from the list.
		ObsvPoint deadPoint = (ObsvPoint) getMyScatterPointsTable()
		.getRowData();
		int rowValue = getMyScatterPointsTable().getRowIndex();
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/deleteScatterPoint] Selected row value:" + rowValue);
		scatterPointList.remove(rowValue);

		// Remove the old db

		ObjectContainer projectdb = null;

		try {
			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");
			System.out.println(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");
			ObjectSet results = projectdb.get(ObsvPoint.class);
			while (results.hasNext()) {
				projectdb.delete(results.next());
			}
			projectdb.commit();
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/deleteScatterPoint] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}

		// Convert array list to standard array.
		ObsvPoint[] newPoints = new ObsvPoint[scatterPointList.size()];
		for (int i = 0; i < scatterPointList.size(); i++) {
			newPoints[i] = (ObsvPoint) scatterPointList.get(i);
		}
		// Store the new db.
		storeObsvPointsInDB(newPoints);
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
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/downloadKmlFile] Unable to download kml file");
			e.printStackTrace();
		}
	}

	public String getAzimuth() {
		return azimuth;
	}

	public String getCodeName() {
		return codeName;
	}

	public String[] getCopyProjectsArray() {
		return this.copyProjectsArray;
	}


	public Fault getCurrentFault() {
		return currentFault;
	}





	public DislocParamsBean getCurrentParams() {
		return currentParams;
	}

	public DislocProjectBean getCurrentProject() {
		return currentProject;
	}

	public DislocProjectSummaryBean getCurrentSummary() {
		return this.currentSummary;
	}

	public HashMap getDbProjectNameList()
	{

		System.out.println("[" + getUserName() + "/DislocBean/getDbProjectNameList] called");

		Client c = Client.create();
		WebResource webResource = c.resource(getAutomatedDislocServiceUrl() + "/run?url=http://earthquake.usgs.gov/earthquakes/catalogs/7day-M5.xml");
		System.out.println("[" + getUserName() + "/DislocBean/getDbProjectNameList] " + getAutomatedDislocServiceUrl());
		
		webResource.get(String.class);
		dbProjectNameList.clear();
		List sl = null;
		String pn = null;
		String spn = null;
		ObjectContainer shareddb = null;
		try {

			File f = new File(getBasePath() + "/" + getContextBasePath() + "/automatedDislocDB/overm5.db");
			if (!f.exists()) {
				if (shareddb != null)
					shareddb.close();
				return dbProjectNameList;
			}

			shareddb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/automatedDislocDB/overm5.db");
			DislocProjectBean project = new DislocProjectBean();
			ObjectSet results = shareddb.get(DislocProjectBean.class);
			while(results.hasNext())
			{
				project = (DislocProjectBean)results.next();
				pn = project.getProjectName().split("_n_")[1];
				spn = project.getProjectName().split("_n_")[0];
				if(dbProjectNameList.containsKey(spn))
				{
					sl = (ArrayList)dbProjectNameList.get(spn);
					sl.add(pn);
				}
				else {
					sl = new ArrayList();
					sl.add(pn);
					dbProjectNameList.put(spn, sl);
				}
			}

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/DislocBean/getDbProjectNameList] " + e);
		} finally {
			if (shareddb != null)
				shareddb.close();
		}


		return dbProjectNameList;
	}

	public ObjectServer getDbs() {
		return dbs;
	}

	public String getDBValue(Select select, String param, String theLayer)
	throws Exception {

		String DB_RESPONSE_HEADER = "results of the query:";

		String sqlQuery = "select " + param + " from LAYER, LREFERENCE where LayerName=\'" + theLayer + "\' and LAYER.InterpId=LREFERENCE.InterpId;";

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

	// End main execution method section.
	// --------------------------------------------------

	public String getDBValue(Select select, String param, String theFault,
			String theSegment) throws Exception {

		String DB_RESPONSE_HEADER = "results of the query:";
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/getDBValue] SQL Query on:" + param);

		String sqlQuery = "select " + param + " from SEGMENT, REFERENCE where FaultName=\'" + theFault + "\' and SegmentName=\'" + theSegment + "\' and SEGMENT.InterpId=REFERENCE.InterpId;";

		// String sqlQuery = "select F." + param
		// + " from FAULT AS F, REFERENCE AS R where F.FaultName=\'" + theFault
		// + "\' and F.InterpId=R.InterpId;";

		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/getDBValue] SQL Query is " + sqlQuery);

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

	public String[] getDeleteProjectsArray() {
		return this.deleteProjectsArray;
	}

	public String getDislocExtendedServiceUrl() {
		return dislocExtendedServiceUrl;
	}

	// --------------------------------------------------
	// End of the fault db section
	// --------------------------------------------------

	// --------------------------------------------------
	// Begin the event handling section for the JSF pages.
	// --------------------------------------------------

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

		ObjectContainer projectdb = null;

		try {
			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");
			ObjectSet results = projectdb.get(DislocParamsBean.class);
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/getDislocParamsFromDB] Getting params from db:" + results.size());
			if (results.hasNext()) {
				paramsBean = (DislocParamsBean) results.next();
			}
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/getDislocParamsFromDB] Project Origin:" + paramsBean.getOriginLat() + " " + paramsBean.getOriginLon());

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/getDislocParamsFromDB] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}
		return paramsBean;
	}

	public String getDislocServiceUrl() {
		return dislocServiceUrl;
	}

	public String getElevation() {
		return elevation;
	}

	public FaultDBEntry getFaultDBEntry() {
		return this.faultDBEntry;
	}

	public String getFaultDBServiceUrl() {
		return faultDBServiceUrl;
	}

	public boolean getFaultdrawing() {
		return faultdrawing;
	}

	public String getFaultKmlFilename() {
		return faultKmlFilename;
	}

	public String getFaultKmlUrl() {
		faultKmlUrl = createFaultKmlFile();
		return faultKmlUrl;
	}

	public String getFaultLatEnd() {
		return this.faultLatEnd;
	}

	public String getFaultLatStart() {
		return this.faultLatStart;
	}

	public String getFaultLonEnd() {
		return this.faultLonEnd;
	}

	public String getFaultLonStart() {
		return this.faultLonStart;
	}


	public String getFaultName() {
		return faultName;
	}

	public String getFaultSelectionCode() {
		return faultSelectionCode;
	}

	/**
	 * This grabs the faults from the DB.
	 */
	protected Fault[] getFaultsFromDB() {
		Fault[] returnFaults = null;

		ObjectContainer projectdb = null;
		try {
			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");
			// Fault faultToGet=new Fault();
			// ObjectSet results=db.get(faultToGet);
			Fault faultToGet;
			ObjectSet results = projectdb.get(Fault.class);
			if (results.hasNext()) {
				returnFaults = new Fault[results.size()];
				for (int i = 0; i < results.size(); i++) {
					returnFaults[i] = (Fault) results.next();
				}
			}

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/getFaultsFromDB] " + e);
		}

		finally {
			if (projectdb != null)
				projectdb.close();
		}

		return returnFaults;
	}

	protected int getFirst(List reducedList, List fullList) {
		int first = 0;
		for (int i = 1; i < reducedList.size(); i++) {
			// DislocProjectSummaryBean
			// mb1=(DislocProjectSummaryBean)fullList.get(((Integer)reducedList.get(first)).intValue());
			// DislocProjectSummaryBean
			// mb2=(DislocProjectSummaryBean)fullList.get(((Integer)reducedList.get(i)).intValue());

			TimeOrderedInterface mb1 = (TimeOrderedInterface) fullList.get(((Integer) reducedList.get(first)).intValue());
			TimeOrderedInterface mb2 = (TimeOrderedInterface) fullList.get(((Integer) reducedList.get(i)).intValue());
			if (mb1.getCreationDate() == null || mb1.getCreationDate().equals("")) {
				mb1.setCreationDate((new Date()).toString());
			}
			if (mb2.getCreationDate() == null || mb2.getCreationDate().equals("")) {
				mb2.setCreationDate((new Date()).toString());
			}
			Date date1 = new Date(mb1.getCreationDate());
			Date date2 = new Date(mb2.getCreationDate());
			if (date2.after(date1))
				first = i;
		}

		return first;
	}

	public String getForSearchStr() {
		return this.forSearchStr;
	}

	public String getFrequency() {
		return frequency;
	}

	public String getGpsStationLat() {
		return gpsStationLat;
	}

	public String getGpsStationLon() {
		return gpsStationLon;
	}

	public String getGpsStationName() {
		return gpsStationName;
	}

	public String getInsarkmlServiceUrl() {
		return insarkmlServiceUrl;
	}
	public String getInsarKmlUrl() {
		return insarKmlUrl;
	}

	public String getJobToken() {
		return jobToken;
	}

	public String getKmlfiles() {
		return kmlfiles;
	}

	public String getKmlGeneratorUrl() {
		return this.kmlGeneratorUrl;
	}

	public String getKmlProjectFile() {
		return this.kmlProjectFile;
	}

	public String getMapFaultName() {
		return this.mapFaultName;
	}

	public List getMyArchivedDislocResultsList() {
		myArchivedDislocResultsList.clear();
		List tmpList = new ArrayList();

		ObjectContainer codedb = null;

		try {
			File f = new File(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");

			if (f.exists()) {

				codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");
				ObjectSet results = codedb.get(new DislocProjectSummaryBean());

				while (results.hasNext()) {
					DislocProjectSummaryBean dpsb = (DislocProjectSummaryBean) results.next();
					// myArchivedDislocResultsList.add(dpsb);
					tmpList.add(dpsb);
				}
				myArchivedDislocResultsList = sortByDate(tmpList);
			}
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/getMyArchivedDislocResultsList] " + e);
		}

		finally {
			if (codedb!=null)
				codedb.close();
		}
		return myArchivedDislocResultsList;
	}

	public HtmlDataTable getMyFaultDataTable() {
		return myFaultDataTable;
	}

	public List getMyFaultDBEntryList() {
		return myFaultDBEntryList;
	}

	public List getMyFaultEntryForProjectList() throws Exception {
		String projectName = getProjectName();
		return reconstructMyFaultEntryForProjectList(projectName);
	}

	public List getMyFaultsForProjectList() throws Exception {
		String projectName = getProjectName();
		return reconstructMyFaultsForProjectList(projectName);
	}

	public int getMyFaultsForProjectListsize() {
		return myFaultsForProjectList.size();
	}

	public HtmlDataTable getMyInsarDataTable() {
		return myInsarDataTable;
	}

	public List getMyInsarParamsList() {
		myInsarParamsList.clear();
		List tmpList = new ArrayList();

		ObjectContainer codedb = null;

		try {
			File f = new File(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");
			if (f.exists()) {
				codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");
				ObjectSet results = codedb.get(new InsarParamsBean());

				while (results.hasNext()) {
					InsarParamsBean ipb = (InsarParamsBean) results.next();
					tmpList.add(ipb);
				}
				myInsarParamsList = sortByDate(tmpList);
			}
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/getMyInsarParamsList] " + e);
		}
		finally {
			if (codedb!=null)
				codedb.close();
		}
		return myInsarParamsList;
	}

	public List getMyObservationsForProjectList() throws Exception {
		String projectName = getProjectName();
		return reconstructMyObservationsForProjectList(projectName);
	}

	public List getMyObsvEntryForProjectList() throws Exception {
		String projectName = getProjectName();
		return reconstructMyObsvForProjectList(projectName);
	}

	/**
	 * This should read from the db.
	 */
	public List getMyPointObservationList() {
		return reconstructMyPointObservationList(projectName);
	}

	/**
	 * Contains a list of project beans.
	 */
	public List getMyProjectNameList() {

		this.myProjectNameList.clear();

		ObjectContainer codedb = null;

		try {

			File f = new File(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");

			if (f.exists()) {
				codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");
				DislocProjectBean project = new DislocProjectBean();
				ObjectSet results = codedb.get(DislocProjectBean.class);
				// System.out.println("Got results:"+results.size());
				while (results.hasNext()) {
					project = (DislocProjectBean) results.next();
					myProjectNameList.add(new SelectItem(project.getProjectName(), project.getProjectName()));
				}
			}
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/getMyProjectNameList] " + e);
		}
		finally {
			if (codedb!=null)
				codedb.close();
		}

		return this.myProjectNameList;
	}

	public HtmlDataTable getMyProjectSummaryDataTable() {
		return myProjectSummaryDataTable;
	}

	public HtmlDataTable getMyScatterPointsTable() {
		return this.myScatterPointsTable;
	}

	public String getObsvKmlFilename() {
		return obsvKmlFilename;
	}

	public String getObsvKmlUrl() {
		obsvKmlUrl = createObsvKmlFile();
		return obsvKmlUrl;
	}

	/**
	 * Grabs the scatter points from the DB. This may be null.
	 */
	protected ObsvPoint[] getObsvPointsFromDB() {
		ObsvPoint[] returnPoints = null;

		ObjectContainer projectdb = null;
		try {

			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

			ObjectSet results = projectdb.get(ObsvPoint.class);
			if (results.hasNext()) {
				returnPoints = new ObsvPoint[results.size()];
				for (int i = 0; i < results.size(); i++) {
					returnPoints[i] = (ObsvPoint) results.next();
				}
			}

		} catch (Exception e) {
			if (projectdb != null)
				projectdb.close();
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/getObsvPointsFromDB] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}
		return returnPoints;
	}

	public String getObsvStyleSelectionCode() {
		return obsvStyleSelectionCode;
	}

	public String getPortalBaseUrl() {
		return portalBaseUrl;
	}

	public Fault[] getProjectFaultsFromDB(String userName, String projectName, String codeName, String basePath, String relPath) {
		Fault[] returnFaults = null;
		System.out.println("Opening Fault DB:" + basePath + "/" + relPath + "/" + userName + "/" + codeName + "/" + projectName + ".db");

		ObjectContainer projectdb = null;

		try {
			projectdb = Db4o.openFile(basePath + "/" + relPath + "/" + userName + "/" + codeName + "/" + projectName + ".db");
			Fault faultToGet = new Fault();
			ObjectSet results = projectdb.get(faultToGet);
			if (results.hasNext()) {
				returnFaults = new Fault[results.size()];
				for (int i = 0; i < results.size(); i++) {
					returnFaults[i] = (Fault) results.next();
				}
			}
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/getProjectFaultsFromDB] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}
		return returnFaults;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getProjectSelectionCode() {
		return this.projectSelectionCode;
	}

	public String getProjectsource() {
		return projectsource;
	}

	public boolean getRenderAddFaultFromDBForm() {
		return renderAddFaultFromDBForm;
	}

	public boolean getRenderAddFaultSelectionForm() {
		return renderAddFaultSelectionForm;
	}

	public boolean getRenderChooseObsvStyleForm() {
		return this.renderChooseObsvStyleForm;
	}

	public boolean getRenderCreateNewFaultForm() {
		return renderCreateNewFaultForm;
	}

	public boolean getRenderDislocGridParamsForm() {
		return renderDislocGridParamsForm;
	}

	public boolean getRenderFaultMap() {
		return this.renderFaultMap;
	}

	public boolean getRenderMap() {
		return this.renderMap;
	}

	public boolean getRenderSearchByAuthorForm() {
		return renderSearchByAuthorForm;
	}

	public boolean getRenderSearchByFaultNameForm() {
		return renderSearchByFaultNameForm;
	}

	public boolean getRenderSearchByLatLonForm() {
		return renderSearchByLatLonForm;
	}

	public boolean getRenderViewAllFaultsForm() {
		return renderViewAllFaultsForm;
	}

	public String[] getSelectProjectsArray() {
		return this.selectProjectsArray;
	}

	public boolean getUsesGridPoints() {
		if (currentParams.getObservationPointStyle() == 1) {
			usesGridPoints = true;
		} else {
			usesGridPoints = false;
		}
		return usesGridPoints;
	}

	/**
	 * This is used to handle the forms for editing a fault's params.
	 */
	public String handleFaultEntryEdit(ActionEvent ev) {
		try {
			// Catch the MyData item during the third phase of the JSF
			// lifecycle.

			FaultDBEntry tmp_FaultDBEntry = (FaultDBEntry) getMyFaultDataTable().getRowData();
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

		return "edit";
	}

	public void handleFaultsRadioValueChange(ValueChangeEvent event) {
		try {
			// Catch the MyData item during the third phase of the JSF
			// lifecycle.
			FaultDBEntry tmp_FaultDBEntry = (FaultDBEntry) getMyFaultDataTable().getRowData();
			SelectItem tmp_SelectItem = tmp_FaultDBEntry.getFaultName();

			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/handleFaultsRadioValueChange] changed fault value : " + tmp_SelectItem.getValue().toString());
			currentFault.setFaultName(tmp_SelectItem.getValue().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init_edit_project() {
		initEditFormsSelection();
		projectSelectionCode = "";
		faultSelectionCode = "";
	}

	protected void initDislocExtendedService() throws Exception {
		dislocExtendedService = new DislocExtendedServiceServiceLocator().getDislocExtendedExec(new URL(dislocExtendedServiceUrl));
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/initDislocExtendedService] Binding to: " + dislocExtendedServiceUrl);
	}

	/**
	 * Protected convenience method.
	 */

	protected void initDislocService() throws Exception {
		dislocService = new DislocServiceServiceLocator().getDislocExec(new URL(dislocServiceUrl));
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/initDislocService] Binding to: " + dislocServiceUrl);
	}

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
	 * Another famous method that I googled. This downloads contents from the
	 * given URL to a local file.
	 */
	public PointEntry[] LoadDataFromUrl(String InputUrl) {
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/LoadDataFromUrl] Creating Point Entry");
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
					System.out.println("Past the faults");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/LoadDataFromUrl] Finished");
		return (PointEntry[]) (dataset.toArray(new PointEntry[dataset.size()]));
	}

	public String loadProjectList() throws Exception {
		// if (!isInitialized) {
		// initWebServices();
		// }
		// setContextList();

		makeProjectDirectory();

		return ("disloc-list-project");
	}

	protected void makeProjectDirectory() {
		File projectDir = new File(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/");
		projectDir.mkdirs();
	}

	protected void myListToVectorCopy(List dest, List src) {
		for (int i = 0; i < src.size(); i++) {
			dest.add(new Integer(i));
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

	/**
	 * Create the new project bean, store it in the db, and initialize.
	 */
	public String NewProjectThenEditProject() throws Exception {
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/NewProjectThenEditProject] From NewProjectThenEditProject...");
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/NewProjectThenEditProject] portalBaseUrl" + getPortalBaseUrl());
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/NewProjectThenEditProject] faultDBServiceUrl : " + getFaultDBServiceUrl());
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/NewProjectThenEditProject] kmlGeneratorUrl : " + getKmlGeneratorUrl());
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/NewProjectThenEditProject] ContextBasePath : " + getContextBasePath());

		try {
			// dislocParams=new DislocParamsBean();
			createNewProject(projectName);
			init_edit_project();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "disloc-edit-project";
	}

	protected Fault populateFaultFromContext(String tmp_faultName) throws Exception {
		String faultStatus = "Update";

		ObjectContainer projectdb = null;

		try {
			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

			// Fault faultToGet=new Fault();
			// faultToGet.setFaultName(tmp_faultName);
			// ObjectSet results=db.get(faultToGet);

			ObjectSet results = projectdb.get(Fault.class);
			Fault currentFault = null;
			while (results.hasNext()) {
				currentFault = (Fault) results.next();
				if (currentFault.getFaultName().equals(tmp_faultName)) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/populateFaultFromContext] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}

		return currentFault;
	}

	protected List populateParamsCollection(String projectName) throws Exception {
		List myDislocParamsCollection = new ArrayList();

		ObjectContainer projectdb = null;
		try {

			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");
			ObjectSet results = projectdb.get(DislocParamsBean.class);
			// Should only have one value.
			DislocParamsBean dislocParams = null;
			// There should only be one of these
			if (results.hasNext()) {
				dislocParams = (DislocParamsBean) results.next();
				myDislocParamsCollection.add(dislocParams);
			}

		} catch (Exception e) {

			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/populateParamsCollection] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}

		return myDislocParamsCollection;
	}

	protected DislocParamsBean populateParamsFromContext(String projectName) throws Exception {

		DislocParamsBean params = null;
		ObjectContainer projectdb = null;

		try {

			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/populateParamsFromContext] Populating params from context.");
			ObjectSet results = projectdb.get(DislocParamsBean.class);
			// Should only have one value.

			if (results.hasNext()) {
				params = (DislocParamsBean) results.next();
			}
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/populateParamsFromContext] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}

		return params;
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

		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/QueryFaultFromDB] faultname : " + faultname);

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
			System.out.println("latStart : " + latStart);
			System.out.println("lonStart : " + lonStart);
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

			tmp_fault.setFaultLameLambda(1.0);
			tmp_fault.setFaultLameMu(1.0);

			// This is the fault's strike angle
			// strike=Math.atan2(x,y)/d2r;
			tmp_fault
			.setFaultStrikeAngle(Double.parseDouble(df.format(strike)));

			// This is the (x,y) of the fault relative to the project's origin
			// The project origin is the lower left lat/lon of the first fault.
			// If any of these conditions hold, we need to reset.
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/QueryFaultFromDB] Origin:" + currentParams.getOriginLat() + " " + currentParams.getOriginLon());
			if (currentParams.getOriginLat() == DislocParamsBean.DEFAULT_LAT || currentParams.getOriginLon() == DislocParamsBean.DEFAULT_LON) {
				currentParams.setOriginLat(latStart);
				currentParams.setOriginLon(lonStart);
				// Update the parameters

				ObjectContainer projectdb = null;

				try {

					projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

					ObjectSet result = projectdb.get(DislocParamsBean.class);
					if (result.hasNext()) {
						DislocParamsBean tmp = (DislocParamsBean) result.next();
						projectdb.delete(tmp);
					}

					projectdb.set(currentParams);

					// Say goodbye.
					projectdb.commit();

				} catch (Exception e) {
					System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/QueryFaultFromDB] " + e);
				}
				finally {
					if (projectdb!=null)
						projectdb.close();
				}

			}
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/QueryFaultFromDB] Updated Origin:" + currentParams.getOriginLat() + " " + currentParams.getOriginLon());

			// The following should be done in any case.
			// If the origin was just (re)set above,
			// we will get a harmless (0,0);
			double x1 = (lonStart - currentParams.getOriginLon()) * factor(currentParams.getOriginLon(), currentParams.getOriginLat());
			double y1 = (latStart - currentParams.getOriginLat()) * 111.32;
			System.out.println("Fault origin: " + x1 + " " + y1);

			tmp_fault.setFaultLocationX(Double.parseDouble(df.format(x1)));
			tmp_fault.setFaultLocationY(Double.parseDouble(df.format(y1)));

			// tmp_fault.setFaultLocationX(x1);
			// tmp_fault.setFaultLocationY(y1);

		} catch (Exception e) {
			e.printStackTrace();
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
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
		return tmp_fault;
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tmp_list;
	}

	/**
	 * Reconstructs the fault entry list.
	 */
	protected List reconstructMyFaultEntryForProjectList(String projectName) {
		this.myFaultEntryForProjectList.clear();

		ObjectContainer projectdb = null;

		try {

			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

			Fault tmpfault = new Fault();
			ObjectSet results = projectdb.get(tmpfault);
			while (results.hasNext()) {
				tmpfault = (Fault) results.next();
				faultEntryForProject tmp_myFaultEntryForProject = new faultEntryForProject();
				tmp_myFaultEntryForProject.setFaultName(tmpfault.getFaultName());
				tmp_myFaultEntryForProject.setoldFaultName(tmpfault.getFaultName());
				tmp_myFaultEntryForProject.update = false;
				tmp_myFaultEntryForProject.delete = false;
				this.myFaultEntryForProjectList.add(tmp_myFaultEntryForProject);
			}

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/reconstructMyFaultEntryForProjectList] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}

		return this.myFaultEntryForProjectList;
	}

	protected List reconstructMyFaultsForProjectList(String projectName) {
		this.myFaultsForProjectList.clear();

		ObjectContainer projectdb = null;

		try {

			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

			Fault tmpfault = new Fault();
			ObjectSet results = projectdb.get(tmpfault);
			while (results.hasNext()) {
				tmpfault = (Fault) results.next();

				this.myFaultsForProjectList.add(tmpfault);
			}

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/reconstructMyFaultsForProjectList] " + e);
		}
		
		finally {
			if (projectdb!=null)
				projectdb.close();
		}
		reconstructMyFaultEntryForProjectList(projectName);
		return this.myFaultsForProjectList;
	}

	protected List reconstructMyObservationsForProjectList(String projectName) {
		// List myObsvEntryForProjectList=new ArrayList();
		this.myObsvEntryForProjectList.clear();
		ObjectContainer projectdb = null;
		try {

			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

			// Unlike faults, there will only be one project bean.
			ObjectSet results = projectdb.get(DislocProjectBean.class);
			while (results.hasNext()) {
				Object tmp = results.next();
				obsvEntryForProject tmp_myObsvEntryForProject = new obsvEntryForProject();
				tmp_myObsvEntryForProject.view = false;
				tmp_myObsvEntryForProject.delete = false;
				this.myObsvEntryForProjectList.add(tmp_myObsvEntryForProject);
			}
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/reconstructMyObservationsForProjectList] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}

		return this.myObsvEntryForProjectList;
	}

	protected List reconstructMyObsvForProjectList(String projectName) {
		// List myObsvEntryForProjectList=new ArrayList();
		this.myObsvEntryForProjectList.clear();

		ObjectContainer projectdb = null;
		try {

			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

			// Unlike faults, there will only be one project bean.
			ObjectSet results = projectdb.get(DislocParamsBean.class);
			while (results.hasNext()) {
				Object tmp = results.next();
				obsvEntryForProject tmp_myObsvEntryForProject = new obsvEntryForProject();
				tmp_myObsvEntryForProject.view = false;
				tmp_myObsvEntryForProject.delete = false;
				this.myObsvEntryForProjectList.add(tmp_myObsvEntryForProject);
			}

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/reconstructMyObsvForProjectList] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}

		return this.myObsvEntryForProjectList;
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
				System.out.println(xypoints.length);
				for (int i = 0; i < xypoints.length; i++) {
					System.out.println(i + " " + xypoints[i].getXcartPoint() + " " + xypoints[i].getYcartPoint());
					myPointObservationList.add(xypoints[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.myPointObservationList;
	}

	/**
	 * This is a JSF compatible method for running Disloc in blocking mode. That
	 * is, it takes no argument and assumes the values have been set by
	 * accessors.
	 */
	public String runBlockingDislocJSF() throws Exception {

		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/runBlockingDislocJSF] Started");

		try {

			Fault[] faults = getFaultsFromDB();
			ObsvPoint[] points = getObsvPointsFromDB();
			initDislocExtendedService();

			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/runBlockingDislocJSF] userName : " + userName);
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/runBlockingDislocJSF] projectName : " + projectName);
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/runBlockingDislocJSF] points : " + points);
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/runBlockingDislocJSF] faults : " + faults);
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/runBlockingDislocJSF] currentParams : " + currentParams);

			// This step runs disloc
			DislocResultsBean dislocResultsBean = dislocExtendedService.runBlockingDislocExt(userName, projectName, points, faults, currentParams, null);
			setJobToken(dislocResultsBean.getJobUIDStamp());

			// This step makes the kml plots.  We allow this to fail.
			String myKmlUrl = "";
			try {
				myKmlUrl = createKml(currentParams, dislocResultsBean, faults);
				setJobToken(dislocResultsBean.getJobUIDStamp());
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			// This step runs the insar plotting stuff.  We also allow this
			// to fail.
			InsarKmlService iks = new InsarKmlServiceServiceLocator().getInsarKmlExec(new URL(insarkmlServiceUrl));

			insarKmlUrl="";
			try {
				insarKmlUrl = iks.runBlockingInsarKml(userName, projectName, dislocResultsBean.getOutputFileUrl(), this.getElevation(), this.getAzimuth(), this.getFrequency(), "ExecInsarKml");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			// This sets the InSAR KML URL, which will be accessed by other
			// pages.
			setInsarKmlUrl(insarKmlUrl);

			storeProjectInContext(userName, projectName, dislocResultsBean.getJobUIDStamp(), currentParams, dislocResultsBean, myKmlUrl, insarKmlUrl, elevation, azimuth, frequency);

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/runBlockingDislocJSF] Finished");
		return DISLOC_NAV_STRING;
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
			DislocResultsBean dislocResultsBean = dislocExtendedService.runNonBlockingDislocExt(userName, projectName, points, faults, currentParams, null);

			String myKmlUrl = "";
			myKmlUrl = createKml(currentParams, dislocResultsBean, faults);
			setJobToken(dislocResultsBean.getJobUIDStamp());

			// This step runs the insar plotting stuff.
			InsarKmlService iks = new InsarKmlServiceServiceLocator().getInsarKmlExec(new URL(insarkmlServiceUrl));
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/runNonBlockingDislocJSF] Service URL:" + insarkmlServiceUrl);

			insarKmlUrl = iks.runBlockingInsarKml(userName, projectName, dislocResultsBean.getOutputFileUrl(), this.getElevation(), this.getAzimuth(), this.getFrequency(), "ExecInsarKml");
			// This sets the InSAR KML URL, which will be accessed by other
			// pages.
			setInsarKmlUrl(insarKmlUrl);

			storeProjectInContext(userName, projectName, dislocResultsBean.getJobUIDStamp(), currentParams, dislocResultsBean, myKmlUrl, insarKmlUrl, elevation, azimuth, frequency);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return DISLOC_NAV_STRING;
	}

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
	 * These methods are used by the InSAR KML Generation service
	 */
	public void setAzimuth(String azimuth) {
		this.azimuth = azimuth;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public void setCopyProjectsArray(String[] copyProjectsArray) {
		this.copyProjectsArray = copyProjectsArray;
	}

	public void setCurrentFault(Fault currentFault) {
		this.currentFault = currentFault;
	}

	public void setCurrentParams(DislocParamsBean currentParams) {
		this.currentParams = currentParams;
	}

	public void setCurrentProject(DislocProjectBean currentProject) {
		this.currentProject = currentProject;
	}

	public void setCurrentSummary(DislocProjectSummaryBean currentSummary) {
		this.currentSummary = currentSummary;
	}


	public void setDeleteProjectsArray(String[] deleteProjectsArray) {
		this.deleteProjectsArray = deleteProjectsArray;
	}

	public void setDislocExtendedServiceUrl(String dislocExtendedServiceUrl) {
		this.dislocExtendedServiceUrl = dislocExtendedServiceUrl;
	}

	public void setDislocServiceUrl(String dislocServiceUrl) {
		this.dislocServiceUrl = dislocServiceUrl;
	}

	public void setElevation(String elevation) {
		this.elevation = elevation;
	}

	public void setFaultDBEntry(FaultDBEntry faultDBEntry) {
		this.faultDBEntry = faultDBEntry;
	}

	public void setFaultDBServiceUrl(String faultDBServiceUrl) {
		this.faultDBServiceUrl = faultDBServiceUrl;
	}

	public void setFaultdrawing(boolean faultdrawing) {
		this.faultdrawing = faultdrawing;
	}

	public void setFaultKmlFilename(String faultKmlFilename) {
		this.faultKmlFilename = faultKmlFilename;
	}

	public void setFaultKmlUrl(String faultKmlUrl) {
		this.faultKmlUrl = faultKmlUrl;
	}

	public void setFaultLatEnd(String tmp_str) {
		this.faultLatEnd = tmp_str;
	}

	public void setFaultLatStart(String tmp_str) {
		this.faultLatStart = tmp_str;
	}

	public void setFaultLonEnd(String tmp_str) {
		this.faultLonEnd = tmp_str;
	}

	public void setFaultLonStart(String tmp_str) {
		this.faultLonStart = tmp_str;
	}

	public void setFaultName(String faultName) {
		this.faultName = faultName;
	}

	public void setFaultSelectionCode(String tmp_str) {
		this.faultSelectionCode = tmp_str;
	}

	public void setForSearchStr(String tmp_str) {
		this.forSearchStr = tmp_str;
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

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public void setGpsStationLat(String gpsStationLat) {
		this.gpsStationLat = gpsStationLat;
	}

	public void setGpsStationLon(String gpsStationLon) {
		this.gpsStationLon = gpsStationLon;
	}

	public void setGpsStationName(String gpsStationName) {
		this.gpsStationName = gpsStationName;
	}

	public void setInsarkmlServiceUrl(String insarkmlServiceUrl) {
		this.insarkmlServiceUrl = insarkmlServiceUrl;
	}

	public void setInsarKmlUrl(String insarKmlUrl) {
		this.insarKmlUrl = insarKmlUrl;
	}

	public void setJobToken(String jobToken) {
		this.jobToken = jobToken;
	}

	public void setKmlfiles(String kmlfiles) {
		this.kmlfiles = kmlfiles;
	}

	public void setKmlGeneratorUrl(String tmp_str) {
		this.kmlGeneratorUrl = tmp_str;
	}

	public void setKmlProjectFile(String kmlProjectFile) {
		this.kmlProjectFile = kmlProjectFile;
	}

	protected List setListOrder(List orderedList, List reducedList, List fullList) {

		if (reducedList == null)
			return null;
		int size = reducedList.size();
		if (size < 2) {
			return fullList;
		}
		while (reducedList != null && reducedList.size() > 0) {
			int first = getFirst(reducedList, fullList);
			// orderedList.add((DislocProjectSummaryBean)fullList.get(((Integer)reducedList.get(first)).intValue()));
			orderedList.add((TimeOrderedInterface) fullList.get(((Integer) reducedList.get(first)).intValue()));
			reducedList.remove(first);
		}
		return orderedList;
	}

	public void setMapFaultName(String mapFaultName) {
		this.mapFaultName = mapFaultName;
	}

	public void setMyArchivedDislocResultsList(List myArchivedDislocResultsList) {
		this.myArchivedDislocResultsList = myArchivedDislocResultsList;
	}

	public void setMyFaultDataTable(HtmlDataTable tmp_DataTable) {
		this.myFaultDataTable = tmp_DataTable;
	}

	public void setMyFaultEntryForProjectList(List tmp_list) {
		this.myFaultEntryForProjectList = tmp_list;
	}

	public void setMyFaultsForProjectList(List tmp_list) {
		this.myFaultsForProjectList = tmp_list;
	}

	public void setMyFaultsForProjectListsize(int myFaultsForProjectListsize) {
		this.myFaultsForProjectListsize = myFaultsForProjectListsize;
	}

	public void setMyInsarDataTable(HtmlDataTable myInsarDataTable) {
		this.myInsarDataTable = myInsarDataTable;
	}

	public void setMyInsarParamsList(List myInsarParamsList) {
		this.myInsarParamsList = myInsarParamsList;
	}

	/**
	 * Some of the stuff below should be abstracted to the generic project
	 * classes.
	 */

	public void setMyObservationsForProjectList(List tmp_list) {
		this.myObservationsForProjectList = tmp_list;
	}

	public void setMyObsvEntryForProjectList(List tmp_list) {
		this.myObsvEntryForProjectList = tmp_list;
	}

	public void setMyPointObservationList(List myPointObservationList) {
		this.myPointObservationList = myPointObservationList;
	}

	public void setMyProjectNameList(List myProjectNameList) {
		this.myProjectNameList = myProjectNameList;
	}

	public void setMyProjectSummaryDataTable(
			HtmlDataTable myProjectSummaryDataTable) {
		this.myProjectSummaryDataTable = myProjectSummaryDataTable;
	}

	public void setMyScatterPointsTable(HtmlDataTable myScatterPointsTable) {
		this.myScatterPointsTable = myScatterPointsTable;
	}

	public void setObsvKmlFilename(String obsvKmlFilename) {
		this.obsvKmlFilename = obsvKmlFilename;
	}

	public void setObsvKmlUrl(String obsvKmlUrl) {
		this.obsvKmlUrl = obsvKmlUrl;
	}

	public void setObsvStyleSelectionCode(String obsvStyleSelectionCode) {
		this.obsvStyleSelectionCode = obsvStyleSelectionCode;
	}

	public void setPortalBaseUrl(String portalBaseUrl) {
		this.portalBaseUrl = portalBaseUrl;
	}

	public void setProjectName(String projectName) {
		// Get rid of dubious characters
		projectName = filterTheBadGuys(projectName);

		// Remove spaces and less dubious stuff.
		projectName = URLDecoder.decode(projectName);
		projectName = URLEncoder.encode(projectName);
		this.projectName = projectName;
	}

	public void setProjectSelectionCode(String projectSelectionCode) {
		this.projectSelectionCode = projectSelectionCode;
	}

	public void setProjectsource(String projectsource) {
		this.projectsource = projectsource;
	}

	public void setRenderAddFaultFromDBForm(boolean tmp_boolean) {
		this.renderAddFaultFromDBForm = tmp_boolean;
	}

	public void setRenderAddFaultSelectionForm(boolean tmp_boolean) {
		this.renderAddFaultSelectionForm = tmp_boolean;
	}

	public void setRenderChooseObsvStyleForm(boolean renderChooseObsvStyleForm) {
		this.renderChooseObsvStyleForm = renderChooseObsvStyleForm;
	}

	public void setRenderCreateNewFaultForm(boolean tmp_boolean) {
		this.renderCreateNewFaultForm = tmp_boolean;
	}

	public void setRenderDislocGridParamsForm(boolean tmp_boolean) {
		this.renderDislocGridParamsForm = tmp_boolean;
	}

	public void setRenderFaultMap(boolean renderFaultMap) {
		this.renderFaultMap = renderFaultMap;
	}

	public void setRenderMap(boolean renderMap) {
		this.renderMap = renderMap;
	}

	public void setRenderSearchByFaultNameForm(boolean tmp_boolean) {
		this.renderSearchByFaultNameForm = tmp_boolean;
	}

	public void setRenderSearchByLatLonForm(boolean tmp_boolean) {
		this.renderSearchByLatLonForm = tmp_boolean;
	}

	public void setRenderViewAllFaultsForm(boolean tmp_boolean) {
		this.renderViewAllFaultsForm = tmp_boolean;
	}

	public void setRnderSearchByAuthorForm(boolean tmp_boolean) {
		this.renderSearchByAuthorForm = tmp_boolean;
	}

	public void setSelectProjectsArray(String[] selectProjectsArray) {
		this.selectProjectsArray = selectProjectsArray;
	}

	public void setUsesGridPoints(boolean usesGridPoints) {
		this.usesGridPoints = usesGridPoints;
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

	protected void storeObsvPointsInDB(ObsvPoint[] points) throws Exception {

		ObjectContainer projectdb = null;
		try {

			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");
			ObjectSet result = projectdb.get(ObsvPoint.class);
			while (result.hasNext()) {
				ObsvPoint tmp = (ObsvPoint) result.next();
				projectdb.delete(tmp);
			}
			for (int i = 0; i < points.length; i++) {
				projectdb.set(points[i]);

			}
			// Say goodbye.
			projectdb.commit();
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/storeObsvPointsInDB] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}

	}

	protected void storeParamsInDB() throws Exception {

		ObjectContainer projectdb = null;

		try {

			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");
			ObjectSet result = projectdb.get(DislocParamsBean.class);
			if (result.hasNext()) {
				DislocParamsBean tmp = (DislocParamsBean) result.next();
				projectdb.delete(tmp);
			}
			projectdb.set(currentParams);

			// Say goodbye.
			projectdb.commit();
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/storeParamsInDB] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}

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

		ObjectContainer codedb = null;
		ObjectContainer projectdb = null;

		// Store the summary and insar params beans.
		try {

			codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");
			codedb.set(summaryBean);
			codedb.set(ipb);

			// Say goodbye.
			codedb.commit();


			// Store the params bean for the current project,
			// deleting any old one as necessary.

			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

			ObjectSet result = projectdb.get(DislocParamsBean.class);
			if (result.hasNext()) {
				DislocParamsBean tmp = (DislocParamsBean) result.next();
				projectdb.delete(tmp);
			}
			projectdb.set(paramsBean);

			// Say goodbye.
			projectdb.commit();

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/storeProjectInContext] " + e);
		}

		finally {
			if (codedb!=null)
				codedb.close();
			if (projectdb!=null)
				projectdb.close();
		}

	}

	public void toggleAddFaultForProject(ActionEvent ev) throws Exception {

		ObjectContainer projectdb = null;

		try {
			initEditFormsSelection();
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleAddFaultForProject] Setting current fault");

			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

			// Add the fault to the DB.
			// Fault tmpfault=new Fault();
			// tmpfault.setFaultName(currentFault.getFaultName());
			// ObjectSet result=db.get(tmpfault);
			ObjectSet result = projectdb.get(Fault.class);
			Fault tmpfault;
			// Remove any previous versions
			while (result.hasNext()) {
				tmpfault = (Fault) result.next();
				if (tmpfault.getFaultName().equals(currentFault.getFaultName())) {
					System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleAddFaultForProject] Deleting old fault: " + currentFault.getFaultName());
					projectdb.delete(tmpfault);
				}
			}
			projectdb.set(currentFault);
			projectdb.commit();

			// setProjectOrigin(projectName);
		} catch (Exception e) {

			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleAddFaultForProject] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}

	}

	public void toggleAddObservationsForProject(ActionEvent ev) throws Exception {

		ObjectContainer projectdb = null;
		try {
			initEditFormsSelection();
			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleAddObservationsForProject] Adding an observation");

			// There should only be one of these at most.
			// Delete the stored one and replace it with the new one.
			ObjectSet result = projectdb.get(DislocParamsBean.class);
			if (result.hasNext()) {
				DislocParamsBean tmp = (DislocParamsBean) result.next();
				projectdb.delete(tmp);
			}
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleAddObservationsForProject] Disloc params are " + currentParams.getGridXIterations());
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleAddObservationsForProject] Disloc params are also " + currentParams.getGridYIterations());
			projectdb.set(currentParams);

			ObjectSet faultResults = projectdb.get(Fault.class);
			while (faultResults.hasNext()) {
				Fault tmp_fault = (Fault) faultResults.next();
				System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleAddObservationsForProject] Updating fault origins for " + tmp_fault.getFaultName());

				double x1 = (tmp_fault.getFaultLonStart() - currentParams.getOriginLon()) * factor(currentParams.getOriginLon(), currentParams.getOriginLat());
				double y1 = (tmp_fault.getFaultLatStart() - currentParams.getOriginLat()) * 111.32;
				System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleAddObservationsForProject] New fault origin: " + x1 + " " + y1);

				tmp_fault.setFaultLocationX(Double.parseDouble(df.format(x1)));
				tmp_fault.setFaultLocationY(Double.parseDouble(df.format(y1)));
				projectdb.set(tmp_fault);
			}
			projectdb.commit();

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleAddObservationsForProject] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}

	}

	public void toggleAddPointObsvForProject(ActionEvent ev) {
		String space = " ";
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleAddPointObsvForProject] Here are the choices:" + space + gpsStationLat + space + gpsStationLon);

		// Project origin is not set, so set it.
		if (currentParams.getOriginLat() == DislocParamsBean.DEFAULT_LAT && currentParams.getOriginLon() == DislocParamsBean.DEFAULT_LON) {
			currentParams.setOriginLat(Double.parseDouble(gpsStationLat));
			currentParams.setOriginLon(Double.parseDouble(gpsStationLon));
		}
		double dLat = Double.parseDouble(gpsStationLat);
		double dLon = Double.parseDouble(gpsStationLon);
		ObsvPoint point = convertLatLon(Double.parseDouble(df.format(dLat)), Double.parseDouble(df.format(dLon)), currentParams .getOriginLat(), currentParams.getOriginLon());

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void toggleCloseFaultMap(ActionEvent ev) {
		setRenderFaultMap(false);
	}

	/**
	 * Stop showing the map.
	 */
	public void toggleCloseMap(ActionEvent ev) {
		setRenderMap(false);
	}

	/**
	 * This is called when a project is seleted for copying and loading.
	 */
	public String toggleCopyProject() throws Exception {
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyProject] Copying project");
		initEditFormsSelection();
		// Get the old project name from the checkboxes
		String oldProjectName = "";
		if (copyProjectsArray != null) {
			for (int i = 0; i < 1; i++) {
				oldProjectName = copyProjectsArray[0];
			}
		}
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyProject] Old project name: " + oldProjectName);

		// Create an empty project
		String newProjectName = this.getProjectName();
		createNewProject(newProjectName);

		// Now replace empty new project pieces with old stuff.

		ObjectContainer codedb = null;

		try {

			codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");

			// Get the old project bean.
			DislocProjectBean project = new DislocProjectBean();
			project.setProjectName(oldProjectName);
			ObjectSet results = codedb.get(project);
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyProject] Got results:" + results.size());
			if (results.hasNext()) {
				currentProject = (DislocProjectBean) results.next();
			}

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyProject] " + e);
		}
		finally {
			if (codedb!=null)
				codedb.close();
		}

		// //Reconstruct the fault and layer object collections from the context
		// myFaultEntryForProjectList=reconstructMyFaultEntryForProjectList(oldProjectName);
		// System.out.println("Old project size:"+myFaultEntryForProjectList.size());
		// System.out.println("Some faultentrystuff:"+((faultEntryForProject)myFaultEntryForProjectList.get(0)).getFaultName());

		// Copy the DB file for the old project to the new project.
		File oldFileDB = new File(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + oldProjectName + ".db");
		File newFileDB = new File(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + newProjectName + ".db");
		copyFile(oldFileDB, newFileDB);

		// Now look up the project params bean and set the project origin.
		currentParams = getDislocParamsFromDB(oldProjectName);
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyProject] Min Y:" + currentParams.getGridMinYValue());
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyProject] Min X:" + currentParams.getGridMinXValue());

		// Some final stuff.
		projectSelectionCode = "";
		faultSelectionCode = "";
		return "disloc-edit-project";
	}

	public void toggleCopyRssProject() throws Exception {
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyRssProject] Copying project");
		initEditFormsSelection();
		// Get the old project name from the checkboxes

		String sourcenames[];
		sourcenames = projectsource.split("/");
		String oldProjectName = "";

		for (int nA = 0 ; nA < sourcenames.length ; nA++) {

			oldProjectName = sourcenames[nA];
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyRssProject] Old project name: " + oldProjectName);

			// Create an empty project
			String newProjectName = oldProjectName + "_copied";
			createNewProject(newProjectName);

			// Now replace empty new project pieces with old stuff.

			ObjectContainer shareddb = null;
			ObjectContainer codedb = null;

			try {

				shareddb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/automatedDislocDB/overm5.db");

				// Get the old project bean.
				DislocProjectBean project = new DislocProjectBean();
				project.setProjectName(oldProjectName);
				ObjectSet results = shareddb.get(project);
				System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyRssProject] Got results:" + results.size());
				if (results.hasNext()) {
					currentProject = (DislocProjectBean) results.next();
				}

				DislocProjectSummaryBean dpsb = null;
				results = shareddb.get(new DislocProjectSummaryBean());
				while (results.hasNext()) {
					DislocProjectSummaryBean temp = (DislocProjectSummaryBean)results.next();
					if (temp.getProjectName().equals(oldProjectName))
						dpsb = temp;
				}

				if (dpsb != null) {
					codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");
					codedb.set(dpsb);					
				}
				else
					System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyRssProject] This project doesn't have a DislocProjectSummaryBean.");
				
				InsarParamsBean ipb = null;
				results = shareddb.get(new InsarParamsBean());
				while (results.hasNext()) {
					InsarParamsBean temp = (InsarParamsBean)results.next();
					if (temp.getProjectName().equals(oldProjectName))
						ipb = temp;
				}
				
				if (ipb != null) {					
					codedb.set(ipb);
				}
				else
					System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyRssProject] This project doesn't have a InsarParamsBean.");
				
				
				
				codedb.commit();			
				

			} catch (Exception e) {
				System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyRssProject] " + e);
			}

			finally {
				if (shareddb!=null)
					shareddb.close();
				if (codedb!=null)
					codedb.close();
			}

			// //Reconstruct the fault and layer object collections from the context
			// myFaultEntryForProjectList=reconstructMyFaultEntryForProjectList(oldProjectName);
			// System.out.println("Old project size:"+myFaultEntryForProjectList.size());
			// System.out.println("Some faultentrystuff:"+((faultEntryForProject)myFaultEntryForProjectList.get(0)).getFaultName());

			// Copy the DB file for the old project to the new project.
			File oldFileDB = new File(getBasePath() + "/" + getContextBasePath() + "/automatedDislocDB/overm5/" + oldProjectName + ".db");
			File newFileDB = new File(getBasePath() + "/" + getContextBasePath()+ "/" + userName + "/" + codeName + "/" + newProjectName + ".db");
			copyFile(oldFileDB, newFileDB);

			// Now look up the project params bean and set the project origin.
			currentParams = getDislocParamsFromDB(newProjectName);
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyRssProject] Min Y:" + currentParams.getGridMinYValue());
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleCopyRssProject] Min X:" + currentParams.getGridMinXValue());
		}

		// Some final stuff.
		projectSelectionCode = "";
		faultSelectionCode = "";
		projectsource = "";
	}
	
	public void toggleDeleteInsar() throws Exception {

		ObjectContainer codedb = null;

		try {
			InsarParamsBean ipb = (InsarParamsBean) getMyInsarDataTable().getRowData();
			codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");

			ObjectSet results2 = codedb.get(ipb);
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteInsar] Number of matches:" + results2.size());

			if (results2.hasNext()) {
				InsarParamsBean delinsar = (InsarParamsBean) results2.next();
				System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteInsar] Deleting insar params");
				codedb.delete(delinsar);
			}
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteInsar] " + e);
		}
		finally {
			if (codedb!=null)
				codedb.close();
		}
	}

	public String toggleDeleteProject() {
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteProject] Deleting a project");
		ObjectContainer codedb = null;
		try {

			codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");
			if (deleteProjectsArray != null) {
				for (int i = 0; i < deleteProjectsArray.length; i++) {
					// Delete the project input data
					System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteProject] Deleting project input junk");
					DislocProjectBean delproj = new DislocProjectBean();
					delproj.setProjectName(deleteProjectsArray[i]);
					ObjectSet results = codedb.get(delproj);
					if (results.hasNext()) {
						delproj = (DislocProjectBean) results.next();
						codedb.delete(delproj);
					}
					// Delete the results summary bean also.
					System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteProject] Deleting project summaries");
					DislocProjectSummaryBean delprojsum = new DislocProjectSummaryBean();
					delprojsum.setProjectName(deleteProjectsArray[i]);
					ObjectSet results2 = codedb.get(delprojsum);
					while (results2.hasNext()) {
						delprojsum = (DislocProjectSummaryBean) results2.next();
						codedb.delete(delprojsum);
					}
					// Delete the insar plotting bean, too
					System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteProject] Deleting insar plots");
					InsarParamsBean delinsar = new InsarParamsBean();
					delinsar.setProjectName(deleteProjectsArray[i]);
					ObjectSet results3 = codedb.get(delinsar);
					while (results3.hasNext()) {
						delinsar = (InsarParamsBean) results3.next();
						codedb.delete(delinsar);
					}
				}
			}
		} catch (Exception e) {

			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteProject] " + e);
		}
		finally {
			if (codedb!=null)
				codedb.close();
		}

		return "disloc-this";
	}

	/**
	 * This will delete projects
	 */
	public void toggleDeleteProjectSummary() throws Exception {
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteProjectSummary] Deleting Project");

		ObjectContainer codedb = null;

		try {
			DislocProjectSummaryBean dpsb = (DislocProjectSummaryBean) getMyProjectSummaryDataTable().getRowData();

			codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteProjectSummary] Found project:" + dpsb.getProjectName() + " "+ dpsb.getJobUIDStamp());
			ObjectSet results = codedb.get(dpsb);
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteProjectSummary] Result size: " + results.size());
			// Should only have one value.
			if (results.hasNext()) {
				DislocProjectSummaryBean deleteme = (DislocProjectSummaryBean) results.next();
				codedb.delete(deleteme);
			}

			// Delete also the associated insar plots
			ObjectSet results2 = codedb.get(InsarParamsBean.class);
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteProjectSummary] Number of matches:" + results.size());
			while (results2.hasNext()) {
				InsarParamsBean delinsar = (InsarParamsBean) results2.next();
				System.out.println(delinsar.getProjectName() + " " + dpsb.getProjectName() + " " + delinsar.getJobUIDStamp() + " " + dpsb.getJobUIDStamp());

				if (delinsar.getProjectName().equals(dpsb.getProjectName()) && delinsar.getJobUIDStamp().equals(dpsb.getJobUIDStamp())) {
					System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteProjectSummary] Deleting insar params");
					codedb.delete(delinsar);
				}
			}


		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDeleteProjectSummary] " + e);
		}
		finally {
			if (codedb!=null)
				codedb.close();
		}

	}

	// --------------------------------------------------

	public void toggleDrawFaultFromMap(ActionEvent ev) {

		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDrawFaultFromMap] started");

		createFaultFromMap();

		initEditFormsSelection();
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDrawFaultFromMap] currentFault.getFaultName() " + currentFault.getFaultName());

		ObjectContainer projectdb = null;

		try {
			projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

			Fault tmpfault = new Fault();
			tmpfault.setFaultName(currentFault.getFaultName());
			ObjectSet result = projectdb.get(tmpfault);
			if (result.hasNext()) {
				tmpfault = (Fault) result.next();
				projectdb.delete(tmpfault);
			}
			projectdb.set(currentFault);
			projectdb.commit();

		} catch (Exception e) {

			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleDrawFaultFromMap] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}
	}

	public void toggleFaultSearchByAuthor(ActionEvent ev) {
		initEditFormsSelection();
		this.forSearchStr = this.forSearchStr.trim();
		if (!this.forSearchStr.equals("")) {
			myFaultDBEntryList = QueryFaultsByAuthor(this.forSearchStr,faultDBServiceUrl);
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
		if ((!this.faultLatStart.equals("")) && (!this.faultLatEnd.equals("")) && (!this.faultLonStart.equals("")) && (!this.faultLonEnd.equals(""))) {
			// myFaultDBEntryList=QueryFaultsByLonLat(this.faultLatStart,
			// this.faultLatEnd, this.faultLonStart, this.faultLonEnd,
			// faultDBServiceUrl);
			KMLdescriptionparser kdp = new KMLdescriptionparser();
			kdp.parseXml(getBasePath() + "/" + codeName + "/", kmlfiles);
			myFaultDBEntryList = kdp.getFaultList("LonLat", this.faultLatStart + " " + this.faultLatEnd + " " + this.faultLonStart + " " + this.faultLonEnd);

		}
		renderAddFaultFromDBForm = !renderAddFaultFromDBForm;
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

	/**
	 * Used for selecting the data to plot
	 */
	public void togglePlotProject(ActionEvent ev) {
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/togglePlotProject] Plotting project");

		try {

			// db=Db4o.openFile(getBasePath()+"/"+getContextBafsePath()+"/"+userName+"/"+codeName+".db");
			DislocProjectSummaryBean dpsb = (DislocProjectSummaryBean) getMyProjectSummaryDataTable().getRowData();

			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/togglePlotProject] Found project:" + dpsb.getProjectName() + " " + dpsb.getJobUIDStamp() + dpsb.getKmlurl());
			String kmlName = dpsb.getKmlurl().substring(dpsb.getKmlurl().lastIndexOf("/") + 1, dpsb.getKmlurl().length());

			downloadKmlFile(dpsb.getKmlurl(), this.getBasePath() + "/" + "gridsphere" + "/" + kmlName);

			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/togglePlotProject] KML Name : " + kmlName);
			setKmlProjectFile(kmlName);

			// ObjectSet results=db.get(dpsb);
			// if(results.hasNext()) {
			// //Reconstitute the bean from the db
			// //Find the URL of the kml and download it.
			// //Set the kml project file.
			// }


		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/togglePlotProject] " + e);
		}

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

	public void toggleReplotInsar() throws Exception {
		ObjectContainer codedb = null;

		try {
			// Get the project
			InsarParamsBean ipb = (InsarParamsBean) getMyInsarDataTable()
			.getRowData();
			ipb.setCreationDate(new Date().toString());

			// Invoke the service
			InsarKmlService iks = new InsarKmlServiceServiceLocator().getInsarKmlExec(new URL(insarkmlServiceUrl));
			insarKmlUrl = iks.runBlockingInsarKml(userName, ipb.getProjectName(), ipb.getDislocOutputUrl(), ipb.getElevation(), ipb.getAzimuth(), ipb.getFrequency(), "ExecInsarKml");

			// Now update the database
			ipb.setInsarKmlUrl(insarKmlUrl);
			ipb.setCreationDate((new Date()).toString());

			codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");
			ObjectSet results = codedb.get(InsarParamsBean.class);
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleReplotInsar] Result set size: " + results.size());

			while (results.hasNext()) {
				InsarParamsBean tmpbean = (InsarParamsBean) results.next();
				codedb.set(ipb);
			}
			codedb.commit();

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleReplotInsar] " + e);
		}
		finally {
			if (codedb!=null)
				codedb.close();
		}

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

	/**
	 * This is called when a project is seleted for loading.
	 */
	public String toggleSelectProject() throws Exception {
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleSelectProject] Loading Project");
		initEditFormsSelection();
		// This is implemented as a selectmanycheckbox on the client side
		// (LoadProject.jsp),
		// hence the unusual for loop.
		if (selectProjectsArray != null) {
			for (int i = 0; i < 1; i++) {
				this.projectName = selectProjectsArray[0];
			}
		}

		ObjectContainer codedb = null;

		try {
			codedb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + ".db");
			// First, get the project bean
			DislocProjectBean project = new DislocProjectBean();
			project.setProjectName(projectName);
			ObjectSet results = codedb.get(project);
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleSelectProject] Got results:" + results.size());
			if (results.hasNext()) {
				currentProject = (DislocProjectBean) results.next();
			}

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleSelectProject] " + e);
		}
		finally {
			if (codedb!=null)
				codedb.close();
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

	// --------------------------------------------------
	/**
	 * Needed to make the map interface work
	 **/
	public void toggleSetFaultFromMap(ActionEvent ev) throws Exception {
		renderFaultMap = false;
		try {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleSetFaultFromMap] Adding fault from map");

			initEditFormsSelection();

			String dbQuery = getMapFaultName();
			if (dbQuery.compareTo("polygon") != 0) {
				System.out.println("get desc :" + dbQuery);
				currentFault = QueryFaultFromDB(dbQuery);
				renderCreateNewFaultForm = true;
			}
		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleSetFaultFromMap] Map fault selection error.");
			e.printStackTrace();
		}
	}

	/**
	 * This is where we set the project's observation style.
	 */
	public void toggleSetObsvStyle(ActionEvent ev) {
		initEditFormsSelection();
		if (obsvStyleSelectionCode.equals("GridStyle")) {
			System.out.println("GridStyle");
			usesGridPoints = true;
			currentParams.setObservationPointStyle(1);
		} else if (obsvStyleSelectionCode.equals("ScatterStyle")) {
			System.out.println("ScatterStyle");
			usesGridPoints = false;
			currentParams.setObservationPointStyle(0);
		} else {
			System.out.println("Unexpected obsv style");
		}
		obsvStyleSelectionCode = "";
		try {
			storeParamsInDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void toggleUpdateFaultProjectEntry(ActionEvent ev) {
		String faultStatus = "Update";
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleUpdateFaultProjectEntry] Updating fault entry for project");
		try {

			// This is the info about the fault.
			faultEntryForProject tmp_FaultEntryForProject = new faultEntryForProject();

			for (int i = 0; i < myFaultEntryForProjectList.size(); i++) {
				tmp_FaultEntryForProject = (faultEntryForProject) myFaultEntryForProjectList.get(i);
				if ((tmp_FaultEntryForProject.getUpdate() == true) || (tmp_FaultEntryForProject.getDelete() == true)) {
					break;
				}
			}

			String tmp_faultName = tmp_FaultEntryForProject.getFaultName();
			boolean tmp_view = tmp_FaultEntryForProject.getUpdate();
			boolean tmp_update = tmp_FaultEntryForProject.getDelete();

			System.out.println(tmp_view + " " + tmp_update);

			initEditFormsSelection();
			if ((tmp_view == true) && (tmp_update == true)) {
				System.out.println("error");
			}

			// Update the fault.
			if ((tmp_view == true) && (tmp_update == false)) {
				currentFault = populateFaultFromContext(tmp_faultName);
				renderCreateNewFaultForm = !renderCreateNewFaultForm;
				System.out.println("Rendering:" + renderCreateNewFaultForm);
			}

			// This is the deletion case.
			if ((tmp_update == true) && (tmp_view == false)) {

				// Delete from the database.
				// This requires we first search for the desired object
				// and then delete the specific value that we get back.
				System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleUpdateFaultProjectEntry] Deleting " + tmp_faultName + "from db");

				ObjectContainer projectdb = null;

				try {

					projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");
					// Fault todelete=new Fault();
					// todelete.setFaultName(tmp_faultName);
					ObjectSet result = projectdb.get(Fault.class);
					Fault todelete;
					while (result.hasNext()) {
						todelete = (Fault) result.next();
						if (todelete.getFaultName().equals(tmp_faultName)) {
							projectdb.delete(todelete);
						}
					}

				} catch (Exception e) {
					System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleUpdateFaultProjectEntry] " + e);
				}
				finally {
					if (projectdb!=null)
						projectdb.close();
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
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleUpdateFaults] started...");
		
		String faultStatus = "Update";
		ObjectContainer projectdb = null;
		try {

			int iSelectFault = -1;
			// Find out which fault was selected.

			for (int i = 0; i < myFaultsForProjectList.size(); i++) {
				Fault tmp_Fault = new Fault();
				tmp_Fault = (Fault) myFaultsForProjectList.get(i);

				// This is the info about the fault.
				String tmp_faultName = ((faultEntryForProject) myFaultEntryForProjectList.get(i)).getoldFaultName();

				boolean tmp_update = ((faultEntryForProject) myFaultEntryForProjectList.get(i)).getUpdate();
				boolean tmp_delete = ((faultEntryForProject) myFaultEntryForProjectList.get(i)).getDelete();

				initEditFormsSelection();
				if ((tmp_update == true) && (tmp_delete == true)) {
					System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleUpdateFaults] error");
				}

				// Update the fault.
				if ((tmp_update == true) && (tmp_delete == false)) {

					System.out.println("[toggleUpdateFaults] Updating " + tmp_Fault.getFaultName() + "(old name)" + tmp_faultName);


					projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

					Fault toUpdate = new Fault();
					toUpdate.setFaultName(tmp_faultName);
					ObjectSet result = projectdb.get(toUpdate);

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
					projectdb.set(toUpdate);
					projectdb.commit();

				}

				// This is the deletion case.
				if ((tmp_update == false) && (tmp_delete == true)) {

					// Delete from the database.
					// This requires we first search for the desired object
					// and then delete the specific value that we get back.
					System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleUpdateFaults] Deleteing " + tmp_faultName + "from db");

					projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

					Fault todelete = new Fault();
					todelete.setFaultName(tmp_faultName);
					ObjectSet result = projectdb.get(todelete);
					if (result.hasNext()) {
						todelete = (Fault) result.next();
						projectdb.delete(todelete);
					}
				}
			}

		} catch (Exception e) {
			System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleUpdateFaults] " + e);
		}
		finally {
			if (projectdb!=null)
				projectdb.close();
		}

		// Print this out as KML
		faultKmlUrl = createFaultKmlFile();
	}

	public void toggleUpdateProjectObservations(ActionEvent ev) {
		
		System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleUpdateProjectObservations] Updating observation entry for project");
		try {
			obsvEntryForProject tmp_ObsvEntryForProject = new obsvEntryForProject();

			// Find out which one was selected
			for (int i = 0; i < myObsvEntryForProjectList.size(); i++) {
				tmp_ObsvEntryForProject = (obsvEntryForProject) myObsvEntryForProjectList.get(i);
				if ((tmp_ObsvEntryForProject.getView() == true) || (tmp_ObsvEntryForProject.getDelete() == true)) {
					break;
				}
			}

			boolean tmp_view = tmp_ObsvEntryForProject.getView();
			boolean tmp_update = tmp_ObsvEntryForProject.getDelete();

			initEditFormsSelection();
			if ((tmp_view == true) && (tmp_update == true)) {
				System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleUpdateProjectObservations] error");
			}
			// This is the edit case.
			if ((tmp_view == true) && (tmp_update == false)) {
				System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleUpdateProjectObservations] We are adding/editing the observations");
				currentParams = populateParamsFromContext(projectName);
				renderDislocGridParamsForm = !renderDislocGridParamsForm;
				System.out.println("Rendering:" + renderDislocGridParamsForm);
			}

			// This is the deletion case.
			if ((tmp_update == true) && (tmp_view == false)) {
				System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleUpdateProjectObservations] we are deleteing the observations");

				ObjectContainer projectdb = null;

				try {

					projectdb = Db4o.openFile(getBasePath() + "/" + getContextBasePath() + "/" + userName + "/" + codeName + "/" + projectName + ".db");

					// There is only one of these
					ObjectSet result1 = projectdb.get(DislocParamsBean.class);
					if (result1.hasNext()) {
						DislocParamsBean todelete = (DislocParamsBean) result1.next();
						// Now that we have the specific object, we can delete
						// it.
						projectdb.delete(todelete);
					}
				} catch (Exception e) {
					System.out.println("[" + getUserName() + "/RssDisloc3/DislocBean/toggleUpdateProjectObservations] " + e);
				}
				finally {
					if (projectdb!=null)
						projectdb.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
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

	// --------------------------------------------------
}
