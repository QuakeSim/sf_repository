package cgl.quakesim.hazus;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

// //Logging
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

//These are OpenSha dependencies
import org.opensha.commons.data.region.SitesInGriddedRegion;
import org.opensha.commons.data.xyz.GeoDataSet;
import org.opensha.commons.exceptions.GMT_MapException;
import org.opensha.commons.geo.GriddedRegion;
import org.opensha.commons.geo.Location;
import org.opensha.commons.mapping.gmt.GMT_MapGenerator;
import org.opensha.commons.param.Parameter;
import org.opensha.commons.param.ParameterList;
import org.opensha.commons.param.event.ParameterChangeWarningEvent;
import org.opensha.commons.param.event.ParameterChangeWarningListener;
import org.opensha.sha.calc.ScenarioShakeMapCalculator;
import org.opensha.sha.earthquake.EqkRupture;
import org.opensha.sha.faultSurface.EvenlyGriddedSurface;
import org.opensha.sha.faultSurface.PointSurface;
import org.opensha.sha.imr.AttenuationRelationship;
import org.opensha.sha.imr.ScalarIMR;
import org.opensha.sha.imr.PropagationEffect;
import org.opensha.sha.imr.attenRelImpl.Abrahamson_2000_AttenRel;
import org.opensha.sha.imr.attenRelImpl.AS_1997_AttenRel;
import org.opensha.sha.imr.param.IntensityMeasureParams.PGA_Param;
import org.opensha.sha.imr.param.IntensityMeasureParams.PGV_Param;
import org.opensha.sha.imr.param.IntensityMeasureParams.PeriodParam;
import org.opensha.sha.imr.param.IntensityMeasureParams.SA_Param;
import org.opensha.sha.imr.ScalarIMR;
import org.opensha.sha.mapping.GMT_MapGeneratorForShakeMaps;
import org.opensha.sha.util.SiteTranslator;

public class OpenShaBean implements ParameterChangeWarningListener {

	 private static final String OPENSHA_SERVLET_URL="http://opensha.ucs.edu:8080/OpenSHA";

	 //These are class variables that get modified by different methods as side effects.  Not good....
	 private String metadata;
	 private GeoDataSet sa_03xyzData;
	 private GeoDataSet sa_10xyzData;
	 private GeoDataSet pgv_xyzData;
	 private GeoDataSet pga_xyzData;

	 private double minLat, maxLat, minLon, maxLon, gridSpacing;

	 private ScenarioShakeMapCalculator shakeMapCalc;
	 private GMT_MapGeneratorForShakeMaps gmtMapGen;
	 private double imlProbValue=0.5;
	 private boolean imlAtProb=false;
	 private boolean pointSourceCorrection=false;
	 private String defaultSiteType="DE";
	 private String imt="PGA";
	 private String dirName=null;

	 /**
	  * This is a main method for testing.
	  */ 
	 public static void main(String[] args) throws Exception {
		  System.out.println("Main called for testing.");
		  (new OpenShaBean()).getOpenShaHazusOutput(33.5,34.5,-119.0,-117.0,0.1,7.2,0.0,33.94,-117.87,5.0,90.0);
		  //(new OpenShaBean()).getOpenShaHazusOutput(0.27358882500898296, 2.1600422924901186, 95.8797958013251, 97.76665461854239, 0.1, 5.3, 0.0, 1.1719, 96.7783, 4.777213961021833, 90.0);
	 }

	 /**
	  * This is the constructor
	  */
	 public OpenShaBean() {
		  System.out.println("Calling Constructor");
		  metadata="";

		  //Create the GMT map generator and turn off log plotting.
		  gmtMapGen=new GMT_MapGeneratorForShakeMaps();
		  gmtMapGen.setParameter(gmtMapGen.LOG_PLOT_NAME,Boolean.FALSE);
		  gmtMapGen.setParameter(gmtMapGen.HAZUS_SHAPE_PARAM_NAME,Boolean.TRUE);
		  

		  //Taken from PagerShakeMapCalc, but I'm not sure if this is correct.
		  PropagationEffect propagationEffect = new PropagationEffect();		  
		  // ParameterList paramList = propagationEffect.getAdjustableParameterList();
		  // paramList.getParameter(propagationEffect.APPROX_DIST_PARAM_NAME).setValue(new Boolean(true));
		  
		  // if (pointSourceCorrection){
		  // 		paramList.getParameter(propagationEffect.POINT_SRC_CORR_PARAM_NAME).setValue(new Boolean(true));
		  // }
		  // else {
		  // 		paramList.getParameter(propagationEffect.POINT_SRC_CORR_PARAM_NAME).setValue(new Boolean(false));
		  // }
		  
		  shakeMapCalc=new ScenarioShakeMapCalculator(propagationEffect);
	 }
	 
	 /**
	  * This is required by the ParameterChangeWarningListener interface, which we have to use as a workaround
	  * for creating the attenuation relationships.
	  */ 
	 public void parameterChangeWarning(ParameterChangeWarningEvent event) {
		  System.out.println("Really?");
	 }

	 /**
	  * This is the primary exposed method.  Works on either the command line or via REST call.
	  */ 
	 public String getOpenShaHazusOutput(double minLat, double maxLat, double minLon, double maxLon, double gridSpacing, double mag, double rake, double lat, double lon, double depth, double aveDip) throws Exception { 
												  //public void getOpenShaHazusOutput() {  //For formatting
		  String retString;
		  try {		  
				
				gmtMapGen.setParameter(GMT_MapGenerator.MIN_LAT_PARAM_NAME,minLat);
				gmtMapGen.setParameter(GMT_MapGenerator.MAX_LAT_PARAM_NAME,maxLat);
				gmtMapGen.setParameter(GMT_MapGenerator.MIN_LON_PARAM_NAME,minLon);
				gmtMapGen.setParameter(GMT_MapGenerator.MAX_LON_PARAM_NAME,maxLon);
				gmtMapGen.setParameter(GMT_MapGenerator.GRID_SPACING_PARAM_NAME,gridSpacing);
				
				//Set up the data.
				//These methods are from GenerateHazus...'s generateHazusFiles() method.
				metadata="<br>Hazus Metadata: \n<br>"+
					 "-------------------\n<br>";
				
				SitesInGriddedRegion sites=createGriddedRegion(minLat, maxLat, minLon, maxLon, gridSpacing);
				EqkRupture eqkRupture=createEarthquakeRupture(mag,rake,lat,lon,depth,aveDip);
				ArrayList attrRelList=createAttenuationRelationships(eqkRupture,imt);
				ArrayList attrRelListWt=createAttenRelWeights();

				sites=getSiteParamsForRegion(sites,(ScalarIMR)attrRelList.get(0));
				sites.setSameSiteParams();

				hazusCalcForSA(attrRelList,attrRelListWt,imlProbValue,sites,eqkRupture,imlAtProb);
				hazusCalcForPGV(attrRelList,attrRelListWt,imlProbValue,sites,eqkRupture,imlAtProb);
				hazusCalcForPGA(attrRelList,attrRelListWt,imlProbValue,sites,eqkRupture,imlAtProb);
												  
				//Make the maps.
				//These methods are from ScenarioShakeMapApp's makeMapForHazus() method
				//setRegionForGMT(minLat, maxLat, minLon, maxLon, gridSpacing);
				//setGMT_ParamsForHazus();
				retString=makeHazusShapeFilesAndMap(sa_03xyzData,sa_10xyzData,pga_xyzData,pgv_xyzData,eqkRupture,metadata,dirName);
				return retString;
		  }
		  catch (Exception ex) {
				System.out.println("OpenShaRestService threw an exception: "+ex.getMessage());
				ex.printStackTrace();
				throw ex;
		  }
		}

	 /**
	  * Create an array list of attenuation relationships. For now, we only support one
	  * AR type, AS_1997_AttenRel.  This code is stolen from PagerShakeMapCalc.
	  */ 
		  protected ArrayList createAttenuationRelationships(EqkRupture eqkRupture, String imt) throws Exception {
		  System.out.println("Creating attenuation relationships");
		  String attenRelClassPackage = "org.opensha.sha.imr.attenRelImpl.";
		  String attenRelImplClass="AS_1997_AttenRel";
		  
		  //
		  Class listenerClass = Class.forName( "org.opensha.commons.param.event.ParameterChangeWarningListener" );
		  Object[] paramObjects = new Object[]{ this };  
		  Class[] params = new Class[]{ listenerClass };

		  Class imrClass = Class.forName(attenRelClassPackage+attenRelImplClass);
		  Constructor con = imrClass.getConstructor( params );
		  //The attenuationrelationships object requires a ParameterChangeWarningListener in its constructor,
		  //so we point the newly created object to back to the OpenShaRestService class.
		  ScalarIMR attenRel = (ScalarIMR)con.newInstance( paramObjects );  
		  attenRel.setParamDefaults();
		  attenRel.setEqkRupture(eqkRupture);
		  attenRel.setIntensityMeasure(imt);
		  if(imt.equalsIgnoreCase("SA")){
				double period = 1.0;  //Hard coded
				attenRel.getParameter(PeriodParam.NAME).setValue(new Double(period));
				imt += "-"+period;
		  }

		  ArrayList attRelList=new ArrayList();
		  attRelList.add((AttenuationRelationship)attenRel);
	  
		  return attRelList;
	 }

	 /**
	  * Creates a gridded region from the bounding box and spacing.
	  */
	 protected SitesInGriddedRegion createGriddedRegion(double minLat,double maxLat, double minLon, double maxLon, double gridSpacing) {
		  GriddedRegion eggr = new GriddedRegion(new Location(minLat, minLon),
															  new Location(maxLat, maxLon),
															  gridSpacing, new Location(0,0));
		  return new SitesInGriddedRegion(eggr);
		  
	 }

	 /**
	  * These are the weights associated with the attribute relationship list.  The two arrays must be
	  * the same size.  For now, hard code this since we only have one attrRel.
	  */
	 protected ArrayList createAttenRelWeights(){
		  System.out.println("Creating attrel weights");
		  ArrayList attRelWtList=new ArrayList();
		  attRelWtList.add(new Double(1.0));
		  return attRelWtList;
	 }

	 /**
	  * Creates the earthquake rupture object. We currently consider only point source earthquakes.
	  * This is taken from PagerShakeMapCalc.
	  */ 
	 protected EqkRupture createEarthquakeRupture(double mag, double rake, double lat, double lon, double depth,double aveDip) {
		  // double aveDip=27.0; //Should pass in
		  // double aveUpperSiesDepth=5.0; //Should pass in
		  System.out.println("Creating earthquake rupture");
		  Location location=new Location(lat,lon,depth);
		  EqkRupture rupture=new EqkRupture();
		  rupture.setPointSurface(location,aveDip);
		  rupture.setMag(mag);
		  rupture.setAveRake(rake);
		  rupture.setHypocenterLocation(location);
		  return rupture;
	 }
	 
	 /**
	  * Create the SA data sets.  This is taken from GenerateHazusControlPanelForSingleMultipleIMRs.
	  */ 
	 protected void hazusCalcForSA(ArrayList selectedAttenRels,ArrayList selectedAttenRelsWt,double imlProbValue,SitesInGriddedRegion sites, EqkRupture eqkRupture,boolean probAtIml) throws Exception {
		  System.out.println("Doing calc for SA");
		metadata += "IMT = SA [ SA Damping = 5.0 ; SA Period = 0.3 ]"+"<br>\n";
		//Doing for SA
		int size = selectedAttenRels.size();
		for(int i=0;i<size;++i)
			((ScalarIMR)selectedAttenRels.get(i)).setIntensityMeasure(SA_Param.NAME);

		//Doing for SA-0.3sec
		setSA_PeriodForSelectedIMRs(selectedAttenRels,0.3);
		sa_03xyzData=shakeMapCalc.getScenarioShakeMapData(selectedAttenRels,selectedAttenRelsWt,sites,eqkRupture,probAtIml,imlProbValue);

		//Doing for SA-1.0sec
		setSA_PeriodForSelectedIMRs(selectedAttenRels,1.0);
		sa_10xyzData=shakeMapCalc.getScenarioShakeMapData(selectedAttenRels,selectedAttenRelsWt,sites,eqkRupture,probAtIml,imlProbValue);
	}

	 /**
	  * Create the PGV data sets. This is taken from GenerateHazusControlPanelForSingleMultipleIMRs.
	  */ 
	 protected void hazusCalcForPGV(ArrayList selectedAttenRels, ArrayList selectedAttenRelsWts, double imlProbValue,SitesInGriddedRegion sites,EqkRupture eqkRupture, boolean probAtIml) throws Exception {
		  System.out.println("Doing pgv calc");
		  metadata += "IMT = PGV"+"<br>\n";
		  
		  //creating the 2 seperate list for the attenRels selected, for one suuporting
		  //the PGV and results calculated using PGV and other not supporting PGV and result
		  //calculated using the SA at 1sec and multiplying by 37.24*2.54.
		  ArrayList attenRelListSupportingPGV = new ArrayList();
		  ArrayList attenRelListNotSupportingPGV = new ArrayList();
		  
		  //List of the Attenuations Wts supporting PGV
		  ArrayList attenRelListPGV_Wts = new ArrayList();
		  //List of the Attenuations Wts not supporting PGV
		  ArrayList attenRelListNot_PGV_Wts = new ArrayList();
		  
		  int size = selectedAttenRels.size();
		  for(int i=0;i<size;++i){
				AttenuationRelationship attenRel = (AttenuationRelationship)selectedAttenRels.get(i);
				if(attenRel.isIntensityMeasureSupported(PGV_Param.NAME)){
					 attenRelListSupportingPGV.add(attenRel);
					 attenRelListPGV_Wts.add(selectedAttenRelsWts.get(i));
				}
				else{
					 attenRelListNotSupportingPGV.add(attenRel);
					 attenRelListNot_PGV_Wts.add(selectedAttenRelsWts.get(i));
				}
		  }
		  pgv_xyzData=doCalcForPGV_OnServer(attenRelListSupportingPGV,attenRelListNotSupportingPGV,attenRelListPGV_Wts,attenRelListNot_PGV_Wts,imlProbValue,sites,eqkRupture,probAtIml);
	 }
	 
	 /**
	  * Create the PGA data sets. This is taken from GenerateHazusControlPanelForSingleMultipleIMRs.
	  */ 
	 protected void hazusCalcForPGA(ArrayList selectedAttenRels,ArrayList selectedAttenRelsWt,double imlProbValue,SitesInGriddedRegion sites, EqkRupture eqkRupture,boolean probAtIml) throws Exception {
		  System.out.println("Calc for PGA");
		  int size = selectedAttenRels.size();
		  for(int i=0;i<size;++i) {
				((ScalarIMR)selectedAttenRels.get(i)).setIntensityMeasure(PGA_Param.NAME);
		  }
		  pga_xyzData=shakeMapCalc.getScenarioShakeMapData(selectedAttenRels,selectedAttenRelsWt,sites,eqkRupture,probAtIml,imlProbValue);
		metadata += "IMT = PGA"+"\n";
	 }
	 
	 /**
	  * For some reason, we break the PGV calculation into two methods
	  */
	 private GeoDataSet doCalcForPGV_OnServer(ArrayList attenRelsSupportingPGV, ArrayList attenRelsNotSupportingPGV, ArrayList attenRelListPGV_Wts, ArrayList attenRelListNot_PGV_Wts, double imlProbValue, SitesInGriddedRegion sites,EqkRupture eqkRupture, boolean probAtIml) throws Exception {
		  
		  //contains the list of all the selected AttenuationRelationship models
		  ArrayList attenRelList = new ArrayList();
		  
		  //contains the list of all the selected AttenuationRelationship with their wts.
		  ArrayList attenRelWtList = new ArrayList();
		  
		  //setting the IMT to PGV for the AttenRels supporting PGV
		  int size = attenRelsSupportingPGV.size();
		  
		  for(int i=0;i<size;++i){
				((ScalarIMR)attenRelsSupportingPGV.get(i)).setIntensityMeasure(PGV_Param.NAME);
				attenRelList.add((ScalarIMR)attenRelsSupportingPGV.get(i));
				attenRelWtList.add(attenRelListPGV_Wts.get(i));
		  }
		  
		  //setting the IMT to SA-1sec for the AttenRels not supporting PGV
		  size = attenRelsNotSupportingPGV.size();
		  for(int i=0;i<size;++i){
				((ScalarIMR)attenRelsNotSupportingPGV.get(i)).setIntensityMeasure(SA_Param.NAME);
				attenRelList.add((ScalarIMR)attenRelsNotSupportingPGV.get(i));
				attenRelWtList.add(attenRelListNot_PGV_Wts.get(i));
		  }
		  //setting the SA period to 1.0 for the atten rels not supporting PGV
		  //		this.setSA_PeriodForSelectedIMRs(attenRelsNotSupportingPGV,1.0);
		  
		  metadata += "IMT = PGV"+"<br>\n";
		  return shakeMapCalc.getScenarioShakeMapData(attenRelList,attenRelWtList,sites,eqkRupture,probAtIml,imlProbValue);
	}

	 //This is needed to support the SA cases.
	private void setSA_PeriodForSelectedIMRs(ArrayList selectedAttenRels, double period) {
		int size = selectedAttenRels.size();
		for(int i=0;i<size;++i)
			((ScalarIMR)selectedAttenRels.get(i)).getParameter(PeriodParam.NAME).setValue(new Double(period));
	}

	 
	 //This is probably a useless method.
	 protected void setRegionForGMT(double minLat, double maxLat, double minLon, double maxLon, double gridSpacing){
		  this.minLat=minLat;
		  this.maxLat=maxLat;
		  this.minLon=minLon;
		  this.maxLon=maxLon;
		  this.gridSpacing=gridSpacing;
	 }
	 
	 /**
	  * Make the files and map on the server.  
	  */ 
	 protected String makeHazusShapeFilesAndMap(GeoDataSet sa03_xyzVals,GeoDataSet sa10_xyzVals,GeoDataSet pga_xyzVals,GeoDataSet pgv_xyzVals,EqkRupture eqkRupture,String metadataAsHTML, String dirName) throws Exception {
		  String[] imgNames = null;
		  System.out.println("Making the Shape Files.");
		  try {
				System.out.println("Doing the shape file thing");
				imgNames=gmtMapGen.makeHazusFileSetUsingServlet(sa03_xyzVals, sa10_xyzVals, pga_xyzVals, pgv_xyzVals, eqkRupture, metadataAsHTML,dirName);
				System.out.println("*********************Imgnames array:"+imgNames[0]);
				String webaddr = imgNames[0].substring(0,imgNames[0].lastIndexOf("/")+1);
				metadataAsHTML += "";
				System.out.println("*********************Here is the hazus webaddr output: "+webaddr);
				return webaddr;
		  }
		  catch (Exception ex) {
				System.err.println("Error calling the Hazus service.");
				ex.printStackTrace();
				throw ex;
		  }
	 }

	 /**
	  * Sets a bunch of values.  This probably isn't correct.
	  */ 
	protected void setGMT_ParamsForHazus(){
		//checking if hazus file generator param is selected, if not then make it selected and the deselect it again
		boolean hazusFileGeneratorCheck = true;
		
		//checking if log map generator param is selected, if yes then make it unselected and the select it again
		boolean generateMapInLogSpace = false;

		//always making the map color scale from the data if the person has choosen the Hazus control panel
		String mapColorScaleValue = "";

 		 // String mapColorScaleValue=(String)paramList.getParameter(GMT_MapGeneratorForShakeMaps.COLOR_SCALE_MODE_NAME).getValue();
		 // if(!mapColorScaleValue.equals(GMT_MapGeneratorForShakeMaps.COLOR_SCALE_MODE_FROMDATA)) {
		 // 	  paramList.getParameter(GMT_MapGeneratorForShakeMaps.COLOR_SCALE_MODE_NAME).setValue(GMT_MapGeneratorForShakeMaps.COLOR_SCALE_MODE_FROMDATA);
		//}

	}
	 
	 /**
	  * Update the SitesInGriddedRegion parameters; taken from PagerShakeMapCalc.
	  */
	 private SitesInGriddedRegion getSiteParamsForRegion(SitesInGriddedRegion sites, ScalarIMR attenRel) {
		  sites.addSiteParams(attenRel.getSiteParamsIterator());
		  //getting Wills Site Class
		  sites.setSiteParamsForRegionFromServlet(false);
		  //getting the Attenuation Site Parameters Liat
		  ListIterator it = attenRel.getSiteParamsIterator();
		  //creating the list of default Site Parameters, so that site parameter values can be filled in
		  //if Site params file does not provide any value to us for it.
		  ArrayList defaultSiteParams = new ArrayList();
		  SiteTranslator siteTrans = new SiteTranslator();
		  while (it.hasNext()) {
				//adding the clone of the site parameters to the list
				Parameter tempParam = (Parameter) ( (Parameter) it.next()).clone();
				//getting the Site Param Value corresponding to the default Wills site class selected by the user
				// for the seleted IMR  from the SiteTranslator
				siteTrans.setParameterValue(tempParam, defaultSiteType, Double.NaN);
				defaultSiteParams.add(tempParam);
		  }
		  sites.setDefaultSiteParams(defaultSiteParams);
		  return sites;
	 }
	 
}

