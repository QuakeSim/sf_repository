/**
 * This is a straightforward extension of the GenericProjectBean
 * class.  It includes accessor methods for interacting with 
 * Sopac services.
 */
package org.servogrid.genericproject;

//SOPAC Client Stuff
import edu.ucsd.sopac.reason.grws.client.GRWS_SubmitQuery;

//Usual java stuff.
import java.net.URL;
import java.io.File;
import java.io.LineNumberReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;

import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Date;

/**
 * Everything you need to set up and run RDAHMM.
 */

public class GenericSopacBean extends GenericProjectBean{

    //These are SOPAC properties
    protected String siteCode="sio5";
    protected String beginDate="2006-01-01";
    protected String endDate="2006-01-10";
    protected boolean bboxChecked=false;
    protected double minLatitude=32.0;
    protected double maxLatitude=33.4;
    protected double minLongitude=-120.0;
    protected double maxLongitude=-117.0;
    protected String resource;
    protected String contextGroup;
    protected String contextId="4";
    protected String sopacQueryResults="";

    //--------------------------------------------------
    // These are universal accessor methods.
    //--------------------------------------------------

    public String getSopacQueryResults() {
	return sopacQueryResults;
    }

    public void setSopacQueryResults(String sopacQueryResults) {
	this.sopacQueryResults=sopacQueryResults;
    }

    public String getContextGroup() {
	return contextGroup;
    }
    public void setContextGroup(String contextGroup) {
	this.contextGroup=contextGroup;
    }

    public String getContextId() {
	return contextId;
    }

    public void setContextId(String contextId) {
	this.contextId=contextId;
    }

    public String getResource() {
	return resource;
    }

    public void setResource(String resource) {
	this.resource=resource;
    }

    public String getSiteCode() {
	return siteCode;
    }
    public void setSiteCode(String siteCode) {
	this.siteCode=siteCode;
	this.siteCode=this.siteCode.toLowerCase();
    }
    
    public String getBeginDate() {
	return beginDate;
    }

    public void setBeginDate(String beginDate) {
	this.beginDate=beginDate;
    }

    public String getEndDate() {
	return endDate;
    }

    public void setEndDate(String endDate) {
	this.endDate=endDate;
    }
    
    public boolean getBboxChecked() {
	return bboxChecked;
    }

    public void setBboxChecked(boolean bboxChecked) {
	this.bboxChecked=bboxChecked;
    }
    
    public double getMinLatitude() {
	return minLatitude;
    }
    
    public void setMinLatitude(double minLatitude) {
	this.minLatitude=minLatitude;
    }

    public double getMaxLatitude() {
	return maxLatitude;
    }
    
    public void setMaxLatitude(double maxLatitude) {
	this.maxLatitude=maxLatitude;
    }
    
    public double getMinLongitude() {
	return minLongitude;
    }
    
    public void setMinLongitude(double minLongitude) {
	this.minLatitude=minLatitude;
    }
    
    public void setMaxLongitude(double maxLongitude) {
	this.maxLongitude=maxLongitude;
    }

    public double getMaxLongitude() {
	return maxLongitude;
    }
    
    
    /**
     * default empty constructor
     */
    public GenericSopacBean(){   
	super();
	System.out.println("Generic Sopac Bean Created");
    }


    public String querySOPAC() throws Exception {

	String minMaxLatLon=null;
	
	System.out.println("Do the query");
	System.out.println("Use bounding box:"+bboxChecked);
	System.out.println(siteCode);
	System.out.println(beginDate);
	System.out.println(endDate);
	System.out.println(resource);	
	System.out.println(contextGroup);	
	System.out.println(contextId);	
	System.out.println(minMaxLatLon);	


	if(bboxChecked) {
	    minMaxLatLon=minLatitude+" "+minLongitude+
		" "+maxLatitude+" "+maxLongitude;
	}
	
	GRWS_SubmitQuery gsq = new GRWS_SubmitQuery();
	gsq.setFromServlet(siteCode, beginDate, endDate, resource,
			   contextGroup, contextId, minMaxLatLon);
	sopacQueryResults=gsq.getResource();
	System.out.println(sopacQueryResults);
	sopacQueryResults=filterResults(sopacQueryResults,2,3);
	
	inputFileContent=sopacQueryResults;
		
	return "display-query-results";
    }

    public String setTheStation() {
	System.out.println("Station set: "+siteCode);
	return "parameters-to-database";
    }
    /**
     * This helper method assumes input is a multlined
     * String of tabbed columns.  It cuts out the number of
     * columns on the left specified by cutLeftColumns and 
     * number on the right by cutRightColumns.
     */
    protected String filterResults(String tabbedString,
				 int cutLeftColumns,
				 int cutRightColumns) throws Exception {
	String returnString="";
	String space=" ";
	StringTokenizer st;
	BufferedReader br=new BufferedReader(new StringReader(tabbedString));
	String line=br.readLine();
	while(line!=null) {
	    st=new StringTokenizer(line);
	    String newLine="";
	    int tokenCount=st.countTokens();
	    for (int i=0;i<tokenCount;i++) {
		String temp=st.nextToken();
		if(i>=cutLeftColumns && i<(tokenCount-cutRightColumns)) {
		    newLine+=temp+space;
		}
	    }
	    returnString+=newLine+"\n";
	    line=br.readLine();
	}
	System.out.println(returnString);
	return returnString;
    }

}
