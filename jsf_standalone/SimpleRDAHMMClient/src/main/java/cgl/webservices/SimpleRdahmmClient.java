/**
* A simple Web Service client for interacting with a remote 
* RDAHMM web service.  This illustrates how to use the 
* simplified service interface.  This can be used as a standalone
* command line tool or it can be embedded in a Java Web App.
*
*/

/** 
 * Note the package is the same as the client stubs.  If you
 * use a different client, you will need to import cgl.webservices.*.
 */
package cgl.webservices;

import java.net.URL;

public class SimpleRdahmmClient {
    String rdahmmServiceUrl="http://gf2.ucs.indiana.edu:8080/rdahmmexec/services/";
    int numModelStates=0;
    String dataUrl;
    RDAHMMService rdservice;
    

    public SimpleRdahmmClient() throws Exception {
	rdservice=(new RDAHMMServiceServiceLocator()).
	    getRDAHMMExec(new URL(rdahmmServiceUrl));
    }
    
    //This is the no-argument version. It requires these values to 
    //be set outside the method.  This is typical usage in a JSF
    //page.
    public String[] runNonblockingRDAHMM() throws Exception {
	if(dataUrl!=null && numModelStates>0) {
	    return rdservice.runNonblockingRDAHMM(dataUrl,numModelStates);	
	}
	else throw new Exception();
    }

    public String[] runBlockingRDAHMM()  throws Exception {
	if(dataUrl!=null && numModelStates>0) {
	    return rdservice.runBlockingRDAHMM(dataUrl,numModelStates);	
	}
	else throw new Exception();
    }

    public String[] runBlockingRDAHMM(String dataUrl,
				      int numModelStates) 
	throws Exception {

	setDataUrl(dataUrl);
	setNumModelStates(numModelStates);
	return rdservice.runBlockingRDAHMM(dataUrl,numModelStates);
    }

    public String[] runNonblockingRDAHMM(String dataUrl,
					 int numModelStates) throws Exception {
	setDataUrl(dataUrl);
	setNumModelStates(numModelStates);
	return rdservice.runNonblockingRDAHMM(dataUrl,numModelStates);
    }

    public void setNumModelStates(int numModelStates){
	this.numModelStates=numModelStates;
    }

    public int getNumModelStates() {
	return numModelStates;
    }
    
    public void setDataUrl(String dataUrl) {
	this.dataUrl=dataUrl;
    }

    public String getDataUrl() {
	return dataUrl;
    }

    public void setRdahmmServiceUrl(String rdahmmServiceUrl) {
	this.rdahmmServiceUrl=rdahmmServiceUrl;
    }

    public String getRdahmmServiceUrl(){
	return rdahmmServiceUrl;
    }

    public static void main(String[] args) {
	String dataUrl="http://geoapp.ucsd.edu/xml/geodesy/reason/grws/resources/output/procCoords/4-47353-20061008100245.txt";
	int numModelStates=2;
	try {
	    SimpleRdahmmClient rds=new SimpleRdahmmClient();
	    System.out.println("----------------------------------");
	    System.out.println("Testing blocking version");
	    rds.runBlockingRDAHMM(dataUrl,
				  numModelStates);

	    System.out.println("----------------------------------");
	    System.out.println("Testing non-blocking version");
	    rds.runNonblockingRDAHMM(dataUrl,
				     numModelStates);
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}

