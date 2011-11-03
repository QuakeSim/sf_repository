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

@Path("/catalog")
public class AnssCatalogService {
	 //This is the URL of the ANSS catalog perl script.
	 String ANSSURL="http://www.ncedc.org/cgi-bin/catalog-search2.pl";
	 //This regular expression parses the date in the description field of the ANSS catalog.
	 String dateRegex="[1-2][0-9][0-9][0-9]/[0-1][0-9]/[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9][0-9]";
	 
	 //These are used only for testing.
	 String[] paramArray = {"output","format","mintime","maxtime","minmag","maxmag","etype","outputloc"};
	 String[] paramValArray = {"kml","cnss","2002/01/01,00:00:00","2010/12/01,00:00:00","8.0","10.0","E","web"};
	 
	 int uid;
	 String kmzDownloadDirectory="/tmp/";
	 String kmzDownloadLocation=null;  //This will be junk+uid+kmz

	 

	 //Get the servlet context
	 @Context ServletContext context;

	 public AnssCatalogService() {
		  //The UID will be reused within the invocation of the service and then discarded.
		  uid=(UUID.randomUUID()).hashCode();
		  kmzDownloadLocation=kmzDownloadDirectory+"junk-"+uid+".kmz";
	 }
	 
	 @GET 
	 /**
	  * The GET version returns a URL pointing to the newly created KML file.
	  * TODO: There is a better way to handle input parameters than this. See other QuakeSim REST examples.
	  */ 
	 public String getCatalog(@Context UriInfo ui) {
									  //Use the servlet context to find out where we are.
									  String outputFileName=null,outputFileShortName=null;
									  outputFileName=context.getRealPath("");
									  String outputDirName=(new File(outputFileName)).getName();
									  outputFileName=outputFileName.substring(0,outputFileName.indexOf(outputDirName));
									  outputFileName+="/ROOT/anss-catalog-movie-"+uid+".kml";
									  outputFileShortName=(new File(outputFileName)).getName();
									  System.out.println(outputFileName+" "+outputFileShortName);

									  MultivaluedMap<String,String> queryParams=ui.getQueryParameters();
									  
									  String memKml=null;
									  String kmlUrl=null;
									  try {
											String ftpUrl=fetchAnssDataSetFtpUrl(ANSSURL,queryParams);
											downloadCatalog(ftpUrl,getKmzDownloadLocation());
											memKml=extractKmlFile(getKmzDownloadLocation());
											ArrayList dateMatches=extractMatchingDates(memKml);
											memKml=revisedKmlWithTimeStamps(memKml,dateMatches);
											createRevisedKmlFile(memKml,outputFileName);
											kmlUrl="http://156.56.179.234:8080/"+outputFileShortName;
											System.out.println("kml url:"+kmlUrl);
											return kmlUrl;
									  }
									  catch (Exception ex) {
											ex.printStackTrace();
									  }
									  return kmlUrl;
									  }
		  @POST
		  @Consumes("application/x-www-form-urlencoded")
		  @Produces("application/vnd.google-earth.kml+xml") 
		  /**
			* The POST version returns the actual KML 
			*/
		  public String postCatalog(MultivaluedMap<String,String> queryParams) {
		  
		  String memKml=null;
		  try {
				String ftpUrl=fetchAnssDataSetFtpUrl(ANSSURL,queryParams);
				downloadCatalog(ftpUrl,getKmzDownloadLocation());
				memKml=extractKmlFile(getKmzDownloadLocation());
				ArrayList dateMatches=extractMatchingDates(memKml);
				memKml=revisedKmlWithTimeStamps(memKml,dateMatches);
				return memKml;
		  }
		  catch (Exception ex) {
				memKml="Unable to retreive KML file.";
				ex.printStackTrace();
		  }
		  System.out.println("returning kml");
		  return memKml;
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
		  				  ftpUrl=line.substring(line.indexOf("ftp://"),line.indexOf(".kmz")+".kmz".length());
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

	 protected void downloadCatalog(String ftpUrl, 
											  String downloadLocation) throws Exception {
		  BufferedInputStream bis=null;
		  BufferedOutputStream bos=null;
		  try {
				URLConnection ftpConn=(new URL(ftpUrl)).openConnection();
				ftpConn.setDoOutput(true); 
				//	 OutputStreamWriter ftpWriter=new OutputStreamWriter(ftpConn.getOutputStream());
				bis=new BufferedInputStream(ftpConn.getInputStream());
				bos=new BufferedOutputStream(new FileOutputStream(downloadLocation));
				int i;
				while((i=bis.read()) != -1) {
					 bos.write(i);
				}
		  }
		  catch (Exception ex) { throw ex; }
		  finally {
				bis.close();
				bos.close();
		  }
	 }

	 protected String extractKmlFile(String downloadLocation) throws Exception {
		  String kmlFile=null, memKml=null;

		  JarFile kmzJar=new JarFile(downloadLocation);
		  
		  //First, get the name off the KML file
		  Enumeration entries=kmzJar.entries();
		  if(entries!=null && entries.hasMoreElements()){
				kmlFile=kmzJar.entries().nextElement().toString();
		  }
		  
		  //--------------------------------------------------
		  //Next, extract the KML and put it into a String
		  //--------------------------------------------------
		  InputStream jarIn=null;
		  try {
				jarIn=kmzJar.getInputStream(kmzJar.getEntry(kmlFile));
				StringWriter memKmlWriter=new StringWriter();
				IOUtils.copy(jarIn,memKmlWriter);
				memKml=memKmlWriter.toString();
		  }
		  catch (Exception ex) { throw ex; }
		  
		  finally {
				//Clean up.
				jarIn.close();
		  }
		  return memKml;
	 }
	 
	 protected ArrayList extractMatchingDates(String memKml) throws Exception {
		  ArrayList dateMatchArray=new ArrayList();

		  String dateRegex="[1-2][0-9][0-9][0-9]/[0-1][0-9]/[0-3][0-9] [0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9][0-9]";
		  Pattern datePattern=Pattern.compile(dateRegex);	 
		  Matcher dateMatcher=datePattern.matcher(memKml);
		  
		  while(dateMatcher.find()){
				//Reformat the strings so that they will work with KML
				String dateMatch=dateMatcher.group();
				dateMatch=dateMatch.replaceAll("/","-");		 
				//Replace the space between the date and time with a "T"
				dateMatch=dateMatch.replace(" ","T");						 
				//Append a "Z" at the end				
				dateMatch+="Z";						 				
				
				//Now put it in an array
				dateMatchArray.add(dateMatch);
		  }
		  
		  return dateMatchArray;
	 }
	 
	 protected String revisedKmlWithTimeStamps(String memKml, ArrayList dateMatchArray) throws Exception {
		  String placemarkRegex="<Placemark>";
		  Pattern placemarkPattern=Pattern.compile(placemarkRegex);
		  Matcher pmMatcher=placemarkPattern.matcher(memKml);
		  
		  StringBuffer memKmlBuffer=new StringBuffer(memKml);
		  String startTimeStamp="<TimeSpan><begin>";
		  String middleStamp="</begin><end>";
		  String endTimeStamp="</end></TimeSpan>";
		  
		  String datePlusMonth=null;
		  int index=0;
		  while(pmMatcher.find()){
				String toInsert=startTimeStamp
					 +dateMatchArray.get(index)
					 +middleStamp
					 +datePlusMonth((String)dateMatchArray.get(index))
					 +endTimeStamp;
				//				System.out.println(toInsert+" "+toInsert.length());
				memKmlBuffer=memKmlBuffer.insert(pmMatcher.end()+index*(toInsert.length()),toInsert);
				index++;
		  }
		  
		  memKml=memKmlBuffer.toString();
		  
		  return memKml;
	 }
	 
	 protected String datePlusMonth(String oldDate){
		  String beginning=oldDate.substring(0,oldDate.indexOf("T"));
		  String ending=oldDate.substring(oldDate.indexOf("T"),oldDate.length());
		  //		  System.out.println(beginning+ending);
		  StringTokenizer st=new StringTokenizer(beginning,"-");
		  //Year,month,day
		  Calendar calendar=Calendar.getInstance();
		  calendar.set(Integer.parseInt(st.nextToken()),
							Integer.parseInt(st.nextToken()),
							Integer.parseInt(st.nextToken()));
		  //Increment 30 days
		  calendar.add(Calendar.DATE,30);
		  String year=calendar.get(Calendar.YEAR)+"";
		  String month="";
		  if(calendar.get(Calendar.MONTH)<10){
				month="0"+calendar.get(Calendar.MONTH);
		  }
		  else {
				month=calendar.get(Calendar.MONTH)+"";
		  }
		  String day="";
		  if(calendar.get(Calendar.DATE)<10) {
				day="0"+calendar.get(Calendar.DATE);
		  }
		  else {
				day=calendar.get(Calendar.DATE)+"";
		  }
		  beginning=year+"-"+month+"-"+day;
		  //		  System.out.println(beginning+ending);
		  return beginning+ending;
	 }

	 protected void createRevisedKmlFile(String memKml,String outputFileName) throws Exception {
		  System.out.println("Output file name: "+outputFileName);
		  PrintWriter pw=new PrintWriter(new FileWriter(outputFileName));
		  try {
				pw.println(memKml);
				pw.flush();
				pw.close();
		  }
		  catch(Exception ex) {
				ex.printStackTrace();
				throw ex;
		  }
		  finally{
				pw.flush();
				pw.close();
		  }
		  
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