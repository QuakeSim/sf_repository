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


@Path("/insarlos/{format}/{lon0}/{lat0}/{lon1}/{lat1}")

public class InSarLosRest {
	 private static final String IMAGE="image=InSAR:unw";
	 private static final String POINT="point=";
	 private static final String FORMAT="format=";
	 private static final String JSON="json";
	 private static final String CSV="csv";
	 private static final String AMP="&";
	 private static final String COMMA=",";
	 private static final String INSAR_TOOL_URL = "http://gf2.ucs.indiana.edu/insartool/profile?";

	 //These are the latitude and longitude values of the west (0) and east (1) points. Getter and
	 //Setter methods are below.  Provided values are defaults.
	 private double lat0, lon0, lat1, lon1;
	 private String outputFormat;
	 private String outputResponse=null;

	 protected final Logger logger=LoggerFactory.getLogger(getClass());

	 
	 public InSarLosRest() {
		  lat0=32.6907989885; 
		  lon0=-115.739447357;
		  lat1=32.8296310667; 
		  lon1=-115.335307904;
		  outputFormat=CSV;
	 }
	 
	 
	 /**
	  * The returned line should have the format "lon, lat, distance, value".  We only want
	  * the last two values, so the method below does this.
	  */
	 protected String processLine(String line) {
		  String[] splitLine=line.split(",");
		  return splitLine[splitLine.length-2]+","+splitLine[splitLine.length-1];
	 }
	 
	 public double getLon0() { return this.lon0; }
	 public double getLon1() { return this.lon1; }
	 public double getLat0() { return this.lat0; }
	 public double getLat1() { return this.lat1; }
	 public String getOutputFormat() { return this.outputFormat; }
	 public String getOutputResponse() { return this.outputResponse; }
	 
	 public void setLon0(double lon0) { this.lon0=lon0; }
	 public void setLon1(double lon1) { this.lon1=lon1; }
	 public void setLat0(double lat0) { this.lat0=lat0; }
	 public void setLat1(double lat1) { this.lat1=lat1; }
	 public void setOutputFormat(String outputFormat) { this.outputFormat=outputFormat; }
	 public void setOutputResponse(String outputResponse) { this.outputResponse=outputResponse; }
	 
	 /**
	  * This is an exposed REST method. 
	  */ 
	 @GET
    @Produces("text/plain")
	 public String getImageLOSRest(@PathParam("format") String outputFormat,
											 @PathParam("lon0") double lon0,
											 @PathParam("lat0") double lat0,
											 @PathParam("lon1") double lon1,
											 @PathParam("lat1") double lat1) 
											 throws Exception {
												  outputResponse=null;  
												  
												  String outputResponse=null;
												  String bbox=lon0+COMMA+lat0+COMMA+lon1+COMMA+lat1;
												  String urlToCall=INSAR_TOOL_URL+IMAGE+AMP+POINT+bbox+AMP+FORMAT+outputFormat;
												  logger.debug("Calling URL:"+urlToCall);
												  
												  URL url=null;
												  HttpURLConnection connect=null;
												  BufferedReader readResponse=null;
												  String responseString=null;
												  
												  try {
														outputResponse="Distance (km), Displacement\n";
														url=new URL(urlToCall);
														connect=(HttpURLConnection)url.openConnection();
														readResponse=new BufferedReader(new InputStreamReader(connect.getInputStream()));
														String line=null;
														while((line=readResponse.readLine())!=null){
															 outputResponse+=processLine(line)+"\n";
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