package org.quakesim.restservices;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;

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

//TODO: Note we make the averaging parameter value required but it is only used if the method
//parameter is average.

@Path("/insarhgt/{format}/{uid}/{resolution}/{lon0}/{lat0}/{lon1}/{lat1}/{method}/{averaging}/{rpiName}")
public class InSarHgtRest {
	 private static final String IMAGE="image=InSAR:";
	 private static final String UID="uid";
	 private static final String HGT="_hgt";
	 private static final String POINT="point=";
	 private static final String FORMAT="format=";
	 private static final String JSON="json";
	 private static final String CSV="csv";
	 private static final String AMP="&";
	 private static final String COMMA=",";
	 private static final String RESOLUTION="resolution=";  
	 private static final String METHOD="method=";  
	 private static final String AVERAGE="average=";
	 private static final String INSAR_TOOL_URL = "http://gf1.ucs.indiana.edu/insartool/profile?";

	 //These are the latitude and longitude values of the west (0) and east (1) points. Getter and
	 //Setter methods are below.  Provided values are defaults.
	 private double lat0, lon0, lat1, lon1;
	 private int uid;
	 private String outputFormat;
	 private String hgtOutputResponse=null;
	 private String rpiName;

	 protected final Logger logger=LoggerFactory.getLogger(getClass());
	 
	 public InSarHgtRest() {
		  lat0=32.6907989885; 
		  lon0=-115.739447357;
		  lat1=32.8296310667; 
		  lon1=-115.335307904;
		  outputFormat=CSV;
		  rpiName="TEST";
	 }
	 
	 /**
	  * The HGT returned CSV should have the format "lon, lat, distance, height".  We only 
	  * want the distance and height.
	  */
	 protected String processHgtLine(String line) {
		  String[] splitLine=line.split(",");
		  String returnString="";
		  //This is the usual case.
		  if(splitLine.length==4) {
				returnString=splitLine[2]+","+splitLine[3];
		  }
		  //Sometimes a value at the particular point is missing, so use an empty value.
		  else if(splitLine.length==3) {
				returnString=splitLine[2]+","+" ";
		  }
		  return returnString;
	 }
	 
	 public double getLon0() { return this.lon0; }
	 public double getLon1() { return this.lon1; }
	 public double getLat0() { return this.lat0; }
	 public double getLat1() { return this.lat1; }
	 public int getUid() { return this.uid; }
	 public String getHgtOutputResponse() { return this.hgtOutputResponse; }
	 public String getOutputFormat() { return this.outputFormat; }
	 public String getRpiName() { return this.rpiName; }
	 
	 public void setLon0(double lon0) { this.lon0=lon0; }
	 public void setLon1(double lon1) { this.lon1=lon1; }
	 public void setLat0(double lat0) { this.lat0=lat0; }
	 public void setLat1(double lat1) { this.lat1=lat1; }
	 public void setUid(int uid) { this.uid=uid; }
	 public void setOutputFormat(String outputFormat) { this.outputFormat=outputFormat; }
	 public void setRpiName(String rpiName) { this.rpiName=rpiName; }
	 public void setHgtOutputResponse(String hgtOutputResponse) { this.hgtOutputResponse=hgtOutputResponse; }
	 

	 /**
	  * This is an exposed REST method. 
	  */ 
	 @GET
    @Produces("text/plain")
	 public String getImageHgtRest(@PathParam("format") String outputFormat,
											 @PathParam("uid") int uid,
											 @PathParam("resolution") int resolution, 
											 @PathParam("method") String method,
											 @PathParam("averaging") int averaging,
											 @PathParam("lon0") double lon0,
											 @PathParam("lat0") double lat0,
											 @PathParam("lon1") double lon1,
											 @PathParam("lat1") double lat1,
                                  @PathParam("rpiName") String rpiName) 
											 throws Exception {
												  
												  String outputResponse=null;
												  String bbox=lon0+COMMA+lat0+COMMA+lon1+COMMA+lat1;
												  String urlToCall=INSAR_TOOL_URL+IMAGE+UID+uid+HGT+AMP+POINT+bbox+AMP+FORMAT+outputFormat+AMP+RESOLUTION+resolution+AMP+METHOD+method;
												  if(method.equals("average")) {
														urlToCall+=AMP+AVERAGE+averaging;
												  }
												  logger.debug("Calling URL:"+urlToCall);
												  
												  URL url=null;
												  HttpURLConnection connect=null;
												  BufferedReader readResponse=null;
												  String responseString=null;
												  
												  try {
														outputResponse="Distance (km), Height\n";
														outputResponse+="#"+rpiName+"\n";
														outputResponse+="#start,"+lat0+","+lon0+"\n";
														outputResponse+="#end,"+lat1+","+lon1+"\n";
														url=new URL(urlToCall);
														connect=(HttpURLConnection)url.openConnection();
														readResponse=new BufferedReader(new InputStreamReader(connect.getInputStream()));
														String line=null;
														while((line=readResponse.readLine())!=null){
															 outputResponse+=processHgtLine(line)+"\n";
														} 
												  }
												  catch(Exception ex) {
														logger.error(ex.getMessage());
														ex.printStackTrace();
												  }
												  finally {
														try {
															 connect.disconnect();
														}
														catch(Exception ex) {
															 ex.printStackTrace();
															 throw ex;
														}
														connect=null;
												  }
												  logger.debug("Here is the response:"+outputResponse);
												  return outputResponse;
											 }


											 }