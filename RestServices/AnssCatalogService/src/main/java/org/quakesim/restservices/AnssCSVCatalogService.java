package org.quakesim.restservices;

//These are needed to process the ANSS catalog site.
import javax.servlet.*;
import java.io.*;
import java.net.*;
import java.util.UUID;
import java.util.jar.*;
import java.util.*;
import java.util.regex.*;
import org.apache.commons.io.*;

//These are Jersey jars
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

@Path("/csvcatalog")
public class AnssCSVCatalogService {
	 //This is the URL of the ANSS catalog perl script.
	 String ANSSURL="http://www.ncedc.org/cgi-bin/catalog-search2.pl";
	 //This regular expression parses the date in the description field of the ANSS catalog.
	 String dateRegex="[1-2][0-9][0-9][0-9]/[0-1][0-9]/[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9][0-9]";
	 
	 //These are used only for testing.
	 String[] paramArray = {"output","format","mintime","maxtime","minmag","maxmag","etype","outputloc"};
	 String[] paramValArray = {"csv","cnss","2002/01/01,00:00:00","2010/12/01,00:00:00","8.0","10.0","E","web"};
	 
	 int uid;

	 //Get the servlet context
	 @Context ServletContext context;

	 public AnssCSVCatalogService() {
		  //The UID will be reused within the invocation of the service and then discarded.
		  uid=(UUID.randomUUID()).hashCode();
	 }
	 
	 @GET 
	 /**
	  * The GET version returns a URL pointing to the newly created KML file.
	  * TODO: There is a better way to handle input parameters than this. See other QuakeSim REST examples.
	  */ 
	 public String getCatalog(@Context UriInfo ui) {
									  //Use the servlet context to find out where we are.

									  MultivaluedMap<String,String> queryParams=ui.getQueryParameters();
									  String ftpUrl=null;
									  try {
											ftpUrl=fetchAnssDataSetFtpUrl(ANSSURL,queryParams);
											System.out.println(ftpUrl);
											return ftpUrl;
									  }
									  catch (Exception ex) {
											ex.printStackTrace();
									  }
									  return ftpUrl;
									  }
		  @POST
		  @Consumes("application/x-www-form-urlencoded")
		  @Produces("application/vnd.google-earth.kml+xml") 
		  /**
			* The POST version returns the actual KML 
			*/
		  public String postCatalog(MultivaluedMap<String,String> queryParams) {
		  
		  String ftpUrl=null;
		  try {
				ftpUrl=fetchAnssDataSetFtpUrl(ANSSURL,queryParams);
				return ftpUrl;
		  }
		  catch (Exception ex) {
				ftpUrl="Unable to retreive CSV file.";
				ex.printStackTrace();
		  }
		  System.out.println("returning CSV");
		  return ftpUrl;
	 }
	 
	 protected String fetchAnssDataSetFtpUrl(String anssUrl, 
														  MultivaluedMap queryParams) throws Exception {
		  String ftpUrl=null, data=null;
		  //Construct data
		  Iterator it=queryParams.keySet().iterator();
		  //Set the first value
		  String theKey=(String)it.next();
		  data=URLEncoder.encode(theKey,"UTF-8")+"="+URLEncoder.encode((String)queryParams.getFirst(theKey),"UTF-8");
		  //Now set the rest.
		  while(it.hasNext()) {
				theKey=(String)it.next();
				data += "&" + URLEncoder.encode(theKey,"UTF-8")+"="+URLEncoder.encode((String)queryParams.getFirst(theKey),"UTF-8");
		  }
		  System.out.println("Query params: "+data);
		  
		  OutputStreamWriter wr=null;
		  BufferedReader rd=null;
		  try {
		  		URL url = new URL(anssUrl);
		  		URLConnection conn = url.openConnection();
		  		conn.setDoOutput(true);
		  		wr = new OutputStreamWriter(conn.getOutputStream());
		  		wr.write(data);
		  		wr.flush();
				
		  		// Get the response
		  		rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  		String line="";
		  		while ((line = rd.readLine()) != null) {
		  			 //Look for the line that begins with URL
		  			 if(line.indexOf("URL")>0) {
		  				  ftpUrl=line.substring(line.indexOf("ftp://"));
						  ftpUrl=ftpUrl.substring(0,ftpUrl.indexOf("\""));
		  				  //We are done, so break out of the while loop.
		  				  break;
		  			 }			 
		  		}
		  }
		  catch (Exception ex) {
		  		throw ex;
		  }
		  finally {
		  		wr.close();
		  		rd.close();
		  }
		  System.out.println("Ftp URL is "+ftpUrl);
		  return ftpUrl;
	 }

	 //--------------------------------------------------
	 // These are some accessor methods.
	 //--------------------------------------------------

	 protected String[] getParamArray() {
		  return paramArray;
	 }

	 protected String[] getParamValArray(){
		  return paramValArray;
	 }
}