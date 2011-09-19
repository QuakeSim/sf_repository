package org.quakesim.restservices;

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

import java.net.URLEncoder;
import java.util.Iterator;
	 
	 @Path("/echo") public class TestService{
		  
		  @GET 
				@Produces("text/html") 
				public String get(@Context UriInfo ui) {
				MultivaluedMap<String,String> queryParams=ui.getQueryParameters();
				return showQueryParams(queryParams);		
		  }
		  
		  @POST
				@Consumes("application/x-www-form-urlencoded")
				@Produces("text/html") 
				public String postCatalog(MultivaluedMap<String,String> queryParams) {
				return showQueryParams(queryParams);		  
		  }  
		  
		  protected String showQueryParams(MultivaluedMap queryParams) {
				Iterator it=queryParams.keySet().iterator();
				//Set the first value
				String theKey=null;
				String returnString="";
				//Now set the rest.
				try {
					 while(it.hasNext()) {
						  theKey=(String)it.next();
						  returnString += URLEncoder.encode(theKey,"UTF8")
								+"  "+URLEncoder.encode((String)queryParams.getFirst(theKey),"UTF-8");
					 }
				}
				catch (Exception ex) { 
					 //This will probably be an UnsupportedEncodingException; handle more approrpriately. 
					 ex.printStackTrace();
					 returnString=ex.getMessage();
				}
				return returnString;
		  }
	 }