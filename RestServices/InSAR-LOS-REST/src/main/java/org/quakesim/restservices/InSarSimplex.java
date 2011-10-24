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


@Path("/insarsimplex/{uid}/{m}/{n}/{lon0}/{lat0}/{lon1}/{lat1}/{lon2}/{lat2}/{lon3}/{lat3}")
public class InSarSimplex {

	 private static final String AMP="&";
	 private static final String INSAR_TOOL_URL="http://gf1.ucs.indiana.edu/insartool/simplex?";
	 private static final String UID="uid=";
	 private static final String POLYGON="polygon=";
	 private static final String COMMA=",";
	 private static final String PIPE="|";
	 private static final String M="m=";
	 private static final String N="n=";

	 public InSarSimplex() {
	 }

	 @GET
	 @Produces("text/plain")
	 
	 public String getInSarSimplex(@PathParam("uid") int uid, @PathParam("m") int m, @PathParam("n") int n,@PathParam("lon0") double lon0, @PathParam("lat0") double lat0, @PathParam("lon1") double lon1, @PathParam("lat1") double lat1, @PathParam("lon2") double lon2,@PathParam("lat2") double lat2, @PathParam("lon3") double lon3,@PathParam("lat3") double lat3) throws Exception {
											 //public String getInSarSimplex() throws Exception { //For formatting
		  
		  //Construct the REST call to the INSAR tool. The lat,lon points have to be in clockwise ordering. 
		  //The order NW->NE->SE->SW is known to work.
		  String sarSimplexResponse="";
		  String polygon=lon0+COMMA+lat0+PIPE+lon1+COMMA+lat1+PIPE+lon2+COMMA+lat2+PIPE+lon3+COMMA+lat3+PIPE+lon0+COMMA+lat0;
		  String urlToCall=INSAR_TOOL_URL+UID+uid+AMP+M+m+AMP+N+n+AMP+POLYGON+polygon;
		  
		  System.out.println("urlToCall:"+urlToCall);
		  URL url=null;
		  HttpURLConnection connect=null;
		  BufferedReader readResponse=null;
		  String responseString="";
		  
		  try {
				url=new URL(urlToCall);
				connect=(HttpURLConnection)url.openConnection();
				readResponse=new BufferedReader(new InputStreamReader(connect.getInputStream()));
				String line=null;
				while((line=readResponse.readLine())!=null){
					 sarSimplexResponse+=line+"\n";
				} 
		  }
		  catch(Exception ex) {
				//logger.error(ex.getMessage());
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
		  //logger.debug("Here is the response:"+losOutputResponse);

		  System.out.println(sarSimplexResponse);
		  return sarSimplexResponse;
	 }
}