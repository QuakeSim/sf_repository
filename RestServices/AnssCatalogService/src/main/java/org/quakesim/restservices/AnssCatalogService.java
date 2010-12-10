package org.quakesim.restservices;

//These are needed to process the ANSS catalog site.
import javax.servlet.*;
import java.io.*;
import java.net.*;
import java.util.jar.*;
import java.util.*;
import java.util.regex.*;
import org.apache.commons.io.*;

//These are Jersey jars
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;

@Path("/catalog")
public class AnssCatalogService {
	 //This is the URL of the ANSS catalog perl script.
	 String ANSSURL="http://www.ncedc.org/cgi-bin/catalog-search2.pl";
	 //This regular expression parses the date in the description field of the ANSS catalog.
	 String dateRegex="[1-2][0-9][0-9][0-9]/[0-1][0-9]/[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9][0-9]";

	 //These are used only for testing.
	 String[] paramArray = {"output","format","mintime","maxtime","minmag","maxmag","etype","outputloc"};
	 String[] paramValArray = {"kml","cnss","2002/01/01,00:00:00","2010/12/01,00:00:00","8.0","10.0","E","web"};

	 String kmzDownloadLocation="/tmp/junk.kmz";
	 
	 @GET
		  @Produces("text/plain")
		  public String getCatalog() {
		  
		  String memKml=null;
		  try {
				String ftpUrl=fetchAnssDataSetFtpUrl(ANSSURL,getParamArray(),getParamValArray());
				downloadCatalog(ftpUrl,getKmzDownloadLocation());
				memKml=extractKmlFile(getKmzDownloadLocation());
				ArrayList dateMatches=extractMatchingDates(memKml);
				memKml=revisedKmlWithTimeStamps(memKml,dateMatches);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
		  return memKml;
	 }
	 
	 protected String fetchAnssDataSetFtpUrl(String catalogServiceUrl, 
														  String[] paramArray, 
														  String[] paramValArray) throws Exception {
		  String ftpUrl=null;
		  //Do the thing.

		  return ftpUrl;
	 }

	 protected void downloadCatalog(String ftpUrl, 
											  String downloadLocation) throws Exception {
	 }

	 protected String extractKmlFile(String downloadLocation) throws Exception {
		  String kmlFile=null;
		  return kmlFile;
	 }

	 protected ArrayList extractMatchingDates(String memKml) throws Exception {
		  ArrayList dateMatchArray=new ArrayList();

		  return dateMatchArray;
	 }
	 
	 protected String revisedKmlWithTimeStamps(String memKml, ArrayList dateMatchArray) throws Exception {
		  return memKml;
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
	
	 protected String getKmzDownloadLocation() {
		  return kmzDownloadLocation;
	 }
}