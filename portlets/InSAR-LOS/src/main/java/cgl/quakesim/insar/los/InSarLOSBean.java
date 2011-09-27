package cgl.quakesim.insar.los;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.*;
import javax.faces.event.ActionEvent;

//Logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This bean is used to call the InSAR_TOOL_URL REST service and parse the results. It assumes
 * the use has selected a specific InSAR image.
 */
public class InSarLOSBean {

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

	 public InSarLOSBean() {
		  lat0=32.6907989885; 
		  lon0=-115.739447357;
		  lat1=32.8296310667; 
		  lon1=-115.335307904;
		  outputFormat=CSV;
	 }

	 /**
	  * This method queries the service.  Note that it expects the lat and lon values to 
	  * be set separately (ie by injection using the setters.). This is the method to call
	  * is using JSF. Its return value is also accessible via the getOutputResponse() method.
	  * @return the URL (as a string) of the file containing the values.
	  */
	 public void getImageLOSValues(ActionEvent ev) throws Exception {
		  logger.debug("action method called----------");
		  outputResponse=getImageLOSValues(outputFormat, lat0, lon0, lat1, lon1);
	 }
	 
	 /**
	  * This is the companion method for the no-argument getImageLOSValues() that has an
	  * explicit interface. 
	  */ 
	 public String getImageLOSValues(String outputFormat, double lat0, double lon0, double lat1, double lon1) throws Exception {
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
				throw ex;
		  }
		  finally {
				connect.disconnect();
		  }
		  logger.debug("Here is the response:"+outputResponse);
		  return outputResponse;
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
	 
}
