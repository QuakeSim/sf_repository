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

@Path("/insarid/{id}")

public class InSarIdRest {
	 
	 static final String amp="&";
	 static final String comma=",";
	 static final String wmsUrl="http://gf1.ucs.indiana.edu/insartool/checkkml?";
	 static final String format="format=json";
	 static final String uid="uid=";

	 private String outputResponse=null;
	 private String urlToCall=null;

	 protected final Logger logger=LoggerFactory.getLogger(getClass());

	 public InSarIdRest() {
	 }

	 /**
	  * This is an exposed REST method. 
	  */ 
	 @GET
    @Produces("application/json")
	 public String getImageById(@PathParam("id") int id)
										 throws Exception {
											  outputResponse="";
											  String urlToCall=wmsUrl+uid+id+amp+format;
											  
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
														 outputResponse+=line+"\n";
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
											  System.out.println("Response:"+outputResponse);
											  return outputResponse;
										 }

										 }
