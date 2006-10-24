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
    String rdahmmServiceUrl="http://gf2.ucs.indiana.edu:8080/rdahmmexec/services/RDAHMMExec";
    int numModelStates=0;
    String dataUrl;
    RDAHMMService rdservice;

    //These are the output files
    String projectStdout;
    String projectRange;
    String projectQ;
    String projectPi;
    String projectMinval;
    String projectMaxval;
    String projectL;
    String projectInput;
    String projectB;
    String projectA;

    public SimpleRdahmmClient() throws Exception {
	rdservice=(new RDAHMMServiceServiceLocator()).
	    getRDAHMMExec(new URL(rdahmmServiceUrl));
    }
    
    //This is the no-argument version. It requires these values to 
    //be set outside the method.  This is typical usage in a JSF
    //page.
    public void runNonblockingRDAHMM() throws Exception {
	if(dataUrl!=null && numModelStates>0) {
	    String[] vals=rdservice.runNonblockingRDAHMM(dataUrl,numModelStates);
	}
	else throw new Exception();
    }

    //This method is also suitable for use in a JSF page.
    public void runBlockingRDAHMM()  throws Exception {
	if(dataUrl!=null && numModelStates>0) {
	    String [] vals=rdservice.runBlockingRDAHMM(dataUrl,numModelStates);	
	    setPropertyVals(vals);
	}
	else throw new Exception();
    }

    //This is a more full-fledged method.  The returned strings
    // are URLs for the output values.
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

    //This method is pretty dumb. The values's order is hard-coded
    //on the server.
    protected void setPropertyVals(String[] vals) {
	projectInput=vals[0];
	projectRange=vals[1];
	projectQ=vals[2];
	projectPi=vals[3];
	projectMinval=vals[4];
	projectMaxval=vals[5];
	projectL=vals[6];
	projectB=vals[7];
	projectQ=vals[8];
	projectStdout=vals[9];
    }

    /**
     * Below is psycho-bean property freakout madness.
     */
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

    public String getProjectStdout() {
	return projectStdout;
    }

    public void setProjectStdout(String projectStdout) {
	this.projectStdout=projectStdout;
    }

    public String getProjectRange(){
	return projectRange;
    }
    
    public void setProjectRange(String projectRange){
	this.projectRange=projectRange;
    }

    public String getProjectQ(){
	return projectQ;
    }
    
    public void setProjectQ(String projectQ){
	this.projectQ=projectQ;
    }

    public String getProjectPi(){
	return projectPi;
    }

    public void setProjectPi(String projectPi){
	this.projectPi=projectPi;
    }

    public String getProjectMinval(){
	return projectMinval;
    }

    public void setProjetcMinval(String projectMinval){
	this.projectMinval=projectMinval;
    }

    public String getProjectMaxval(){
	return projectMaxval;
    }

    public void setProjectMaxval(String projectMaxval){
	this.projectMaxval=projectMaxval;
    }

    public String getProjectL(){
	return projectL;
    }

    public void setProjectL(String projectL){
	this.projectL=projectL;
    }

    public String getProjectInput(){
	return projectInput;
    }
    public void setProjectInput(String projectInput){
	this.projectInput=projectInput;
    }

    public String getProjectB(){
	return projectB;
    }

    public void setProjectB(String projectB){
	this.projectB=projectB;
    }

    public String  getProjectA(){
	return projectA;
    }

    public void setProjectA(String projectA){
	this.projectA=projectA;
    }


    public static void main(String[] args) {
	String dataUrl="http://geoapp.ucsd.edu/xml/geodesy/reason/grws/resources/output/procCoords/4-47353-20061008100245.txt";
	int numModelStates=2;
	try {
	    SimpleRdahmmClient rds=new SimpleRdahmmClient();
	    System.out.println("----------------------------------");
	    System.out.println("Testing blocking version");
	    String[] returnVals=rds.runBlockingRDAHMM(dataUrl,
						      numModelStates);
	    for(int i=0;i<returnVals.length;i++) {
		System.out.println(returnVals[i]);
	    }
	}
	catch (Exception ex) {
	    ex.printStackTrace();
	}
    }
}

