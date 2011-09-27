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

@Path("/insarnav/{lon0}/{lat0}/{lon1}/{lat1}/{width}/{height}/{x}/{y}")

public class InSarNavRest {
	 
	 static final String amp="&";
	 static final String comma=",";
	 static final String wmsUrl="http://gf2.ucs.indiana.edu:8080/geoserver/InSAR/wms?service=wms";
	 static final String version="version=1.1.0";
	 static final String request="request=GetfeatureInfo";
	 static final String layers="layers=InSAR:insar";
	 static final String query_layers="query_layers=InSAR:insar";
	 static final String SRS="SRS=EPSG:4326";
	 static final String info_format="info_format=text/html";
	 static final String feature_count="feature_count=50";
	 static final String BBOX="BBOX=";
	 static final String WIDTH="WIDTH=";
	 static final String HEIGHT="HEIGHT=";
	 static final String pixel_x="x=";
	 static final String pixel_y="y=";


	 private String outputResponse=null;
	 private String urlToCall=null;

	 protected final Logger logger=LoggerFactory.getLogger(getClass());

	 public InSarNavRest() {
		  urlToCall=wmsUrl+amp+version+amp+request+amp+layers+amp+query_layers+amp+SRS+amp+info_format+amp+feature_count;
	 }
	 
	 protected String processLine(String line){
		  return line;
	 }

	 /**
	  * This is an exposed REST method. 
	  */ 
	 @GET
    @Produces("application/json")
	 public String getImageLOSRest(@PathParam("lon0") double lon0,
											 @PathParam("lat0") double lat0,
											 @PathParam("lon1") double lon1,
											 @PathParam("lat1") double lat1,
											 @PathParam("width") int width,
											 @PathParam("height") int height,
											 @PathParam("x") int x,
											 @PathParam("y") int y) 
											 throws Exception {
												  outputResponse="";

												  String bbox=BBOX+lon0+comma+lat0+comma+lon1+comma+lat1;
												  urlToCall+=amp+bbox+amp+WIDTH+width;
												  urlToCall+=amp+HEIGHT+height;
												  urlToCall+=amp+pixel_x+x;
												  urlToCall+=amp+pixel_y+y;

												  System.out.println("Calling:"+urlToCall);

												  URL url=null;
												  HttpURLConnection connect=null;
												  BufferedReader readResponse=null;
												  String responseString=null;
												  
												  try {
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
