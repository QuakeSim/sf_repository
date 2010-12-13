<%@ page import="javax.servlet.*, java.io.*, java.net.*, java.util.jar.*, java.util.*,java.util.regex.*"%>
<%@ page import="org.apache.commons.io.*"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%
//Do the post action
String anssUrl="http://www.ncedc.org/cgi-bin/catalog-search2.pl";
String[] paramArray = {"output","format","mintime","maxtime","minmag","maxmag","etype","outputloc"};
String[] paramValArray = {"kml","cnss","2002/01/01,00:00:00","2010/12/01,00:00:00","8.0","10.0","E","web"};


String data="";
String kmzDest="/tmp/junk.kmz";
String ftpUrl;

if(paramArray.length>0 && paramArray.length==paramValArray.length) {
/*
    // Construct data
	 data=URLEncoder.encode(paramArray[0],"UTF-8")+"="+URLEncoder.encode(paramValArray[0],"UTF-8");
	 for(int i=1;i<paramArray.length;i++) {
	     data += "&" + URLEncoder.encode(paramArray[i], "UTF-8") + "=" + URLEncoder.encode(paramValArray[i], "UTF-8");		  
	 }

    // Send data
    URL url = new URL(anssUrl);
    URLConnection conn = url.openConnection();
    conn.setDoOutput(true);
    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
    wr.write(data);
    wr.flush();

    // Get the response
    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String line,ftpUrl="";
    while ((line = rd.readLine()) != null) {
	 		 //Look for the line that begins with URL
			 if(line.indexOf("URL")>0) {
 			    ftpUrl=line.substring(line.indexOf("ftp://"),line.indexOf(".kmz")+".kmz".length());
			    //We are done, so break out of the while loop.
			 	 break;
			 }			 
    }
    wr.close();
    rd.close();
*/

	 //For testing only
	 ftpUrl="ftp://www.ncedc.org/outgoing/userdata/web/catsearch.7272.kmz";

	 //--------------------------------------------------
	 //Now get the contents of the FTP site.
	 //--------------------------------------------------
	 URLConnection ftpConn=(new URL(ftpUrl)).openConnection();
	 ftpConn.setDoOutput(true); 
	 //	 OutputStreamWriter ftpWriter=new OutputStreamWriter(ftpConn.getOutputStream());
	 BufferedInputStream bis=new BufferedInputStream(ftpConn.getInputStream());
	 BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(kmzDest));
	 int i;
	 while((i=bis.read()) != -1) {
	    bos.write(i);
    }
	 bis.close();

	 bos.close();
	 
	 //--------------------------------------------------
	 //Use jar utils to do stuff.
	 //--------------------------------------------------
	 JarFile kmzJar=new JarFile(kmzDest);
	 
	 //First, get the name off the KML file
	 Enumeration entries=kmzJar.entries();
	 String kmlFile=null;
	 if(entries!=null && entries.hasMoreElements()){
	    kmlFile=kmzJar.entries().nextElement().toString();
	 }

	 //--------------------------------------------------
	 //Next, extract the KML and put it into a String
	 //--------------------------------------------------
	 InputStream jarIn=kmzJar.getInputStream(kmzJar.getEntry(kmlFile));
	 StringWriter memKmlWriter=new StringWriter();
	 IOUtils.copy(jarIn,memKmlWriter);
	 String memKml=memKmlWriter.toString();
	 //Clean up.
	 jarIn.close();
	 
	 //--------------------------------------------------	 
	 // Now regexp/parse the KML to extract the dates.
	 //--------------------------------------------------	 
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
	 
   //--------------------------------------------------		  
	// Insert the time stamp tags
   //--------------------------------------------------		 
	String placemarkRegex="<Placemark>";
	Pattern placemarkPattern=Pattern.compile(placemarkRegex);
	Matcher pmMatcher=placemarkPattern.matcher(memKml);
	
	StringBuffer memKmlBuffer=new StringBuffer(memKml);
	String startTimeStamp="<TimeStamp><when>";
	String endTimeStamp="</when></TimeStamp>";

	int index=0;
	while(pmMatcher.find()){
	     String toInsert=startTimeStamp+dateMatchArray.get(index)+endTimeStamp;
		  memKmlBuffer=memKmlBuffer.insert(pmMatcher.end()+index*(toInsert.length()),toInsert);
		  index++;
   }
 	
	out.println((memKmlBuffer.toString()).trim());

}
else {
	  //out.println("Initial ANSS query parameters not properly set.");
}

%>