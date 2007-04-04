package cgl.webservices;

public class RdahmmProjectBean {
	 //These are query parameters
    String siteCode="sio5";
    String beginDate="2005-01-01";
    String endDate="2006-01-10";
	 String resource="procCoords";
	 String contextGroup="sopacGlobk";
	 String minMaxLatLon="";
	 String contextId="5";
    protected int numModelStates=4;
    protected int randomSeed=1;
    protected String outputType="";
    protected double annealStep=0.01;    

	 //Project Parameters
    String projectName;
    String creationDate;
	 String hostName;

    public String getProjectName() {
		  return projectName;
    }
    public void setProjectName(String projectName){
		  this.projectName=projectName;
    }
    public String getCreationDate() {
		  return creationDate;
    }
    public void setCreationDate(String creationDate){
		  this.creationDate=creationDate;
    }

	 public void setHostName(String hostName){
		  this.hostName=hostName;
    }
    public String getHostName() {
		  return this.hostName;
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

	 public void setContextId(String contextId){
		  this.contextId=contextId;
	 }

	 public String getContextId() {
		  return contextId;
	 }

	 public void setResource(String resource) {
		  this.resource=resource;
	 }
	 
	 public String getResource() {
		  return this.resource;
	 }

	 public void setContextGroup(String contextGroup){
		  this.contextGroup=contextGroup;
	 }

	 public String getContextGroup() {
		  return this.contextGroup;
	 }

	 public void setMinMaxLatLon(String minMaxLatLon) {
		  this.minMaxLatLon=minMaxLatLon;
	 }
	 
	 public String getMinMaxLatLon() {
		  return this.minMaxLatLon;
	 }

    public void setNumModelStates(int numModelStates){
		  this.numModelStates=numModelStates;
    }
	 
    public int getNumModelStates() {
		  return numModelStates;
    }
}
