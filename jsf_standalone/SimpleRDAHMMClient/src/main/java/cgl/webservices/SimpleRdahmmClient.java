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
    //The value for this property is set in faces-config.xml.
    String rdahmmServiceUrl="http://gf2.ucs.indiana.edu:8080/rdahmmexec/services/RDAHMMExec";

    //Some properties with default values
    int numModelStates=2;

    String dataUrl="http://geoapp.ucsd.edu/xml/geodesy/reason/grws/resources/output/procCoords/4-56150-20061025144159.txt";
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

    String projectGraphX;
    String projectGraphY;
    String projectGraphZ;

    //These are properties needed if you want to include a query
    //to the GRWS web service.
    String siteCode="sio5";
    String beginDate="2004-01-01";
    String endDate="2006-01-10";
    
    public SimpleRdahmmClient() throws Exception {
	System.out.println("Service to contact: "+rdahmmServiceUrl);
	rdservice=(new RDAHMMServiceServiceLocator()).
	    getRDAHMMExec(new URL(rdahmmServiceUrl));
    }
    
    /**
     * This is the no-argument version. It requires these values to 
     * be set outside the method.  This is typical usage in a JSF
     * page.
     */
    public String runNonblockingRDAHMM() throws Exception {
	if(dataUrl!=null && numModelStates>0) {
	    String[] vals=rdservice.runNonblockingRDAHMM(dataUrl,numModelStates);	    setPropertyVals(vals);
	}
	else throw new Exception();
	return "simple-rdahmm-client-nav1";
    }

    /**
     * This method is also suitable for use in a JSF page.
     */
    public String runBlockingRDAHMM()  throws Exception {
	if(dataUrl!=null && numModelStates>0) {
	    String [] vals=rdservice.runBlockingRDAHMM(dataUrl,numModelStates);	
	    setPropertyVals(vals);
	}
	else throw new Exception();
	return "simple-rdahmm-client-nav1";
    }

    /**
     * This method is also suitable for use in a JSF page.
     * This one needs a sitecode, begin and end dates.
     */
    public String runBlockingRDAHMM2()  throws Exception {
	if(dataUrl!=null && numModelStates>0) {
	    String [] vals=rdservice.runBlockingRDAHMM(siteCode,
						       beginDate,
						       endDate,
						       numModelStates);	
	    setPropertyVals(vals);
	}
	else throw new Exception();
	return "simple-rdahmm-client-nav1";
    }

    /**
     * This method is also suitable for use in a JSF page.
     * This one needs a sitecode, begin and end dates.
     */
    public String runNonblockingRDAHMM2()  throws Exception {
	if(dataUrl!=null && numModelStates>0) {
	    String [] vals=rdservice.runBlockingRDAHMM(siteCode,
						       beginDate,
						       endDate,
						       numModelStates);	
	    setPropertyVals(vals);
	}
	else throw new Exception();
	return "simple-rdahmm-client-nav1";
    }

    /** 
     * This is a more full-fledged method.  The returned strings
     * are URLs for the output values.
     */
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
	//These are the output files.
	projectInput=vals[0];
	projectRange=vals[1];
	projectQ=vals[2];
	projectPi=vals[3];
	projectA=vals[4];
	projectMinval=vals[5];
	projectMaxval=vals[6];
	projectL=vals[7];
	projectB=vals[8];
	projectQ=vals[9];
	projectStdout=vals[10];
	
	//These are the images
	projectGraphX=vals[11];
	projectGraphY=vals[12];
	projectGraphZ=vals[13];
       
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

    public String  getProjectGraphX(){
	return projectGraphX;
    }

    public void setProjectGraphX(String projectGraphX){
	this.projectGraphX=projectGraphX;
    }

    public String  getProjectGraphY(){
	return projectGraphY;
    }

    public void setProjectGraphY(String projectGraphY){
	this.projectGraphY=projectGraphY;
    }

    public String  getProjectGraphZ(){
	return projectGraphZ;
    }

    public void setProjectGraphZ(String projectGraphZ){
	this.projectGraphZ=projectGraphZ;
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

