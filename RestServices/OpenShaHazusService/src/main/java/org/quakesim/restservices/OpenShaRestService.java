package org.quakesim.restservices;

import java.net.*;
import java.io.*;
import java.util.*;
import java.lang.reflect.*;

//Logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//These are Jersey jars
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.UriInfo;

//These are OpenSha dependencies
import org.opensha.commons.data.region.SitesInGriddedRegion;
import org.opensha.commons.data.xyz.GeoDataSet;
import org.opensha.commons.exceptions.GMT_MapException;
import org.opensha.commons.geo.GriddedRegion;
import org.opensha.commons.geo.Location;
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

@Path("/openhazus/{minLat}/{maxLat}/{minLon}/{maxLon}/{gridSpacing}/{mag}/{rake}/{lat}/{lon}/{depth}")
public class OpenShaRestService implements ParameterChangeWarningListener {

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
	 private String dirName="/tmp/";

	 public OpenShaRestService() {
		  System.out.println("Calling Constructor");
		  metadata="";

		  //Create the GMT map generator and turn off log plotting.
		  gmtMapGen=new GMT_MapGeneratorForShakeMaps();
		  gmtMapGen.setParameter(gmtMapGen.LOG_PLOT_NAME,Boolean.FALSE);

		  PropagationEffect propagationEffect = new PropagationEffect();
		  
		  ParameterList paramList = propagationEffect.getAdjustableParameterList();
		  paramList.getParameter(propagationEffect.APPROX_DIST_PARAM_NAME).setValue(new Boolean(true));
		  
		  if (pointSourceCorrection){
				paramList.getParameter(propagationEffect.POINT_SRC_CORR_PARAM_NAME).setValue(new Boolean(true));
		  }
		  else {
				paramList.getParameter(propagationEffect.POINT_SRC_CORR_PARAM_NAME).setValue(new Boolean(false));
		  }
		  
		  shakeMapCalc=new ScenarioShakeMapCalculator(propagationEffect);
	 }
	 
	 public void parameterChangeWarning(ParameterChangeWarningEvent event) {
		  System.out.println("Really?");
	 }

	 public static void main(String[] args) {
		  System.out.println("Main called for testing.");
		  (new OpenShaRestService()).getOpenShaHazusOutput(33.5,34.5,-119.0,-117.0,0.1,7.2,90,33.94,-117.87,5.0);
	 }

	 /**
	  * This is the primary exposed method.
	  */ 
	 @GET
	 @Produces("text/plain")
	 public void getOpenShaHazusOutput(@PathParam("minLat") double minLat, @PathParam("maxLat") double maxLat, @PathParam("minLon") double minLon, @PathParam("maxLon") double maxLon, @PathParam("gridSpacing") double gridSpacing, @PathParam("mag") double mag, @PathParam("rake") double rake, @PathParam("lat") double lat, @PathParam("lon") double lon, @PathParam("depth") double depth) { 
	 //	 public void getOpenShaHazusOutput() {  //For formatting
	

		  try {		  
				//Set up the data.
				//These methods are from GenerateHazus...'s generateHazusFiles() method.
				SitesInGriddedRegion sites=createGriddedRegion(minLat, maxLat, minLon, maxLon, gridSpacing);
				EqkRupture eqkRupture=createEarthquakeRupture(mag,rake,lat,lon,depth);
				ArrayList attrRelList=createAttenuationRelationships(eqkRupture);
				ArrayList attrRelListWt=createAttenRelWeights();

				sites=getSiteParamsForRegion(sites,(ScalarIMR)attrRelList.get(0));

				hazusCalcForSA(attrRelList,attrRelListWt,imlProbValue,sites,eqkRupture,imlAtProb);
				hazusCalcForPGV(attrRelList,attrRelListWt,imlProbValue,sites,eqkRupture,imlAtProb);
				hazusCalcForPGA(attrRelList,attrRelListWt,imlProbValue,sites,eqkRupture,imlAtProb);
												  
				//Make the maps.
				//These methods are from ScenarioShakeMapApp's makeMapForHazus() method
				setRegionForGMT(minLat, maxLat, minLon, maxLon, gridSpacing);
				setGMT_ParamsForHazus();
				makeHazusShapeFilesAndMap(sa_03xyzData,sa_10xyzData,pga_xyzData,pgv_xyzData,eqkRupture,metadata,dirName);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		}

	 /**
	  * Create an array list of attenuation relationships. For now, we only support one
	  * AR type, Abrahamson_2000_AttenRel
	  */ 
		  protected ArrayList createAttenuationRelationships(EqkRupture eqkRupture) throws Exception {
		  System.out.println("Creating attenuation relationships");
		  String attenRelClassPackage = "org.opensha.sha.imr.attenRelImpl.";
		  String attenRelImplClass="AS_1997_AttenRel";

		  Class listenerClass = Class.forName( "org.opensha.commons.param.event.ParameterChangeWarningListener" );
		  Object[] paramObjects = new Object[]{ this };
		  Class[] params = new Class[]{ listenerClass };

		  Class imrClass = Class.forName(attenRelClassPackage+attenRelImplClass);
		  Constructor con = imrClass.getConstructor( params );
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
	  */ 
	 protected EqkRupture createEarthquakeRupture(double mag, double rake, double lat, double lon, double depth) {
		  double aveDip=27.0;
		  double aveUpperSiesDepth=5.0;
		  System.out.println("Creating earthquake rupture");
		  Location location=new Location(lat,lon,aveUpperSiesDepth);
		  EqkRupture rupture=new EqkRupture();
		  rupture.setPointSurface(location,aveDip);
		  rupture.setMag(mag);
		  rupture.setAveRake(rake);
		  return rupture;
	 }
	 
	 /**
	  * Create the SA data sets
	  */ 
	 protected void hazusCalcForSA(ArrayList selectedAttenRels,ArrayList selectedAttenRelsWt,double imlProbValue,SitesInGriddedRegion sites, EqkRupture eqkRupture,boolean probAtIml) throws Exception {
		  System.out.println("Doing calc for SA");
		//Doing for SA
		int size = selectedAttenRels.size();
		for(int i=0;i<size;++i)
			((ScalarIMR)selectedAttenRels.get(i)).setIntensityMeasure(SA_Param.NAME);

		//Doing for SA-0.3sec
		setSA_PeriodForSelectedIMRs(selectedAttenRels,0.3);

		//		sa_03xyzData = generateShakeMap(selectedAttenRels,selectedAttenRelsWt,SA_Param.NAME,imlProbValue,sites,eqkRupture,probAtIml);
		sa_03xyzData=shakeMapCalc.getScenarioShakeMapData(selectedAttenRels,selectedAttenRelsWt,sites,eqkRupture,probAtIml,imlProbValue);
		metadata += "IMT = SA [ SA Damping = 5.0 ; SA Period = 0.3 ]"+"<br>\n";

		//Doing for SA-1.0sec
		setSA_PeriodForSelectedIMRs(selectedAttenRels,1.0);
		
		//		sa_10xyzData = generateShakeMap(selectedAttenRels,selectedAttenRelsWt,SA_Param.NAME,imlProbValue,sites,eqkRupture,probAtIml);
		sa_10xyzData=shakeMapCalc.getScenarioShakeMapData(selectedAttenRels,selectedAttenRelsWt,sites,eqkRupture,probAtIml,imlProbValue);
		metadata += "IMT = SA [ SA Damping = 5.0 ; SA Period = 1.0 ]"+"<br>\n";
	}

	 /**
	  * Create the PGV data sets.
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
	  * Create the PGA data sets.
	  */ 
	 protected void hazusCalcForPGA(ArrayList selectedAttenRels,ArrayList selectedAttenRelsWt,double imlProbValue,SitesInGriddedRegion sites, EqkRupture eqkRupture,boolean probAtIml) throws Exception {
		  System.out.println("Calc for PGA");
		  int size = selectedAttenRels.size();
		  for(int i=0;i<size;++i) {
				((ScalarIMR)selectedAttenRels.get(i)).setIntensityMeasure(PGA_Param.NAME);
		  }
		  //		  pga_xyzData = generateShakeMap(selectedAttenRels,selectedAttenRelsWt,PGA_Param.NAME,imlProbValue,sites,eqkRupture,probAtIml);
		  pga_xyzData=shakeMapCalc.getScenarioShakeMapData(selectedAttenRels,selectedAttenRelsWt,sites,eqkRupture,probAtIml,imlProbValue);
		  metadata += "IMT = PGA"+"\n";
	 }
	 
	 /**
	  * 
	  */ 
	 protected GeoDataSet generateShakeMap(ArrayList attenRel, ArrayList attenRelWts, String imt, double imlProbValue,SitesInGriddedRegion sites,EqkRupture eqkRupture, boolean probAtIml) throws Exception {
		try {
			double value=imlProbValue;
			//calls the scenario shakemap calculator to generate the map data file on the server
			GeoDataSet xyzDataSet = shakeMapCalc.getScenarioShakeMapData(attenRel,attenRelWts,sites,eqkRupture,probAtIml,value);
			return xyzDataSet;
		}
		catch(Exception e){
			 e.printStackTrace();
			 throw e;
		}
		//return "";  //Shouldn't get here.
	}

	 
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
		  
		  //as the calculation will be done on the server so saves the XYZ object and returns the path to object file.
		  //		  return generateShakeMap(attenRelList,attenRelWtList,PGV_Param.NAME,imlProbValue,sites,eqkRupture,probAtIml);
		  return shakeMapCalc.getScenarioShakeMapData(attenRelList,attenRelWtList,sites,eqkRupture,probAtIml,imlProbValue);
	}

	private void setSA_PeriodForSelectedIMRs(ArrayList selectedAttenRels, double period) {
		int size = selectedAttenRels.size();
		for(int i=0;i<size;++i)
			((ScalarIMR)selectedAttenRels.get(i)).getParameter(PeriodParam.NAME).setValue(new Double(period));
	}

	 protected void setRegionForGMT(double minLat, double maxLat, double minLon, double maxLon, double gridSpacing){
		  this.minLat=minLat;
		  this.maxLat=maxLat;
		  this.minLon=minLon;
		  this.maxLon=maxLon;
		  this.gridSpacing=gridSpacing;
	 }
	 
	 protected void makeHazusShapeFilesAndMap(GeoDataSet sa03_xyzVals,GeoDataSet sa10_xyzVals,GeoDataSet pga_xyzVals,GeoDataSet pgv_xyzVals,EqkRupture eqkRupture,String metadataAsHTML, String dirName){
		  String[] imgNames = null;
		  try {
				//				imgNames = openConnectionToServerToGenerateShakeMapForHazus(sa03_xyzVals, sa10_xyzVals, pga_xyzVals, pgv_xyzVals, eqkRupture, metadataAsHTML);
				imgNames=gmtMapGen.makeHazusFileSetUsingServlet(sa03_xyzVals, sa10_xyzVals, pga_xyzVals, pgv_xyzVals, eqkRupture, metadataAsHTML,dirName);
				String webaddr = imgNames[0].substring(0,imgNames[0].lastIndexOf("/")+1);
				metadataAsHTML += "";
				
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
	 }

	 /**
	  * Sets a bunch of values.
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

