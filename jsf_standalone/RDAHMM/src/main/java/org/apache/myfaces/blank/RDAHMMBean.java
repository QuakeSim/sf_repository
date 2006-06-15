package org.apache.myfaces.blank;

//Web Service clients
import WebFlowClient.cm.*;

//Usual java stuff.
import java.net.URL;
import java.io.File;

/**
 * 
 */
public class RDAHMMBean {

    //Internal properties
    boolean isInitialized=false;
    ContextManagerImp cm=null;
    String contextName;

    //These are SOPAC properties
    private String siteCode="sio5";
    private String beginDate="2006-01-01";
    private String endDate="2006-01-10";
    private boolean bboxChecked=false;
    private double minLatitude;
    private double maxLatitude;
    private double minLongitude;
    private double maxLongitude;
    private double procCoords;
    private double contextGroup;
    private double contextId;


    //properties
    private String userName;
    private String defaultName="defaultUser";
    private String contextUrl;
    private String contextBasePath;
    private String FS="FS";
    private String codeName="RDAHMM";
    private String projectName="";
    private int numModelStates;
    private int randomSeed;
    private String outputType="";
    private String inputFile="";

    private String[] contextList;
    
    //This will just be hard coded for now.
    private String hostName="danube.ucs.indiana.edu";

    //--------------------------------------------------
    // These are accessor methods.
    //--------------------------------------------------
    
    public String getSiteCode() {
	return siteCode;
    }
    public void setSiteCode(String siteCode) {
	this.siteCode=siteCode;
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
    public void setendDate(String endDate) {
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
    
    
    public void setContextList(String[] cl) {
	System.arraycopy(cl,0,this.contextList,0,cl.length);
    }
    public String[] getContextList() {
	return this.contextList;
    }
    
    public void setHostName(String hostName){
	this.hostName=hostName;
    }
    public String getHostName() {
	return this.hostName;
    }
    public void  setInputFile(String inputFile) {
	this.inputFile=inputFile;
    }
    public String getInputFile() {
	return this.inputFile;
    }

    public boolean getIsInitialized() {
	return isInitialized;
    }
    
    public void setIsInitialized(boolean isInitialized) {
	this.isInitialized=isInitialized;
    }

    public String getProjectName() {
	return projectName;
    }
    public void setProjectName(String projectName){
	this.projectName=projectName;
    }

    public int getNumModelStates() {
	return numModelStates;
    }
    public void setNumModelStates(int numModelStates){
	this.numModelStates=numModelStates;
    }

    public int getRandomSeed() {
	return randomSeed;
    }
    public void setRandomSeed(int randomSeed){
	this.randomSeed=randomSeed;
    }
    
    public String getOutputType() {
	return outputType;
    }
    public void setOutputType(String outputType){
	this.outputType=outputType;
    }

    public String getCodeName() {
	return codeName;
    }
    
    public void setCodeName(String codeName) {
	this.codeName=codeName;
    }

    public String getContextUrl() {
	System.out.println(this.toString()+":getContextUrl:"+contextUrl);
	return contextUrl;
    }

    public void setContextUrl(String cUrl) {
	this.contextUrl=cUrl;
	System.out.println(this.toString()+":setContextUrl:"+contextUrl);
    }

    public String getContextBasePath() {
	System.out.println(this.toString()+":getContextBasePath:"+contextBasePath);
	return contextBasePath;
    }

    public void setContextBasePath(String basepath) {
	this.contextBasePath=basepath;
	System.out.println(this.toString()+":setContextBasePath:"+contextBasePath);
    }
    
    /**
     * default empty constructor
     */
    public RDAHMMBean(){   
	System.out.println("RDAHMM Bean Created");
	userName=getPortalUserName();
    }

    public void initWebServices() {
	System.out.println("Initializing web services");

	try {
	    //--------------------------------------------------
	    //Set up the context manager service.
	    //--------------------------------------------------	
	    String base_userpath=getContextBasePath()+
		File.separator+userName+File.separator+codeName;
	    System.out.println("baseuserpath:"+base_userpath);
	    

	    cm=(new ContextManagerImpServiceLocator()).
		getContextManager(new URL(contextUrl));
	    ((ContextManagerSoapBindingStub) cm).setMaintainSession(true);
	    System.out.println("Stub initialized");

	    cm.setContextStorage(FS);
	    cm.init(userName,base_userpath);
	    cm.addContext(codeName);

	    System.out.println("We're done, take it home.");
	    
	    isInitialized=true;
	}
	catch(Exception ex) {
	    System.out.println("We got an exception");
	    ex.printStackTrace();
	}
    }
    
    /**
     * Method that is backed to a submit button of a form.
     */
    public String newProject() throws Exception{
	if(!isInitialized) {
	    initWebServices();
	}
	return ("new-project-created");
    }
    
    public String setParameterValues() throws Exception {
        //Do real logic
	System.out.println("Creating new project");
	
	//Store the request values persistently
	contextName=codeName+"/"+projectName;
	cm.addContext(contextName);
	cm.setCurrentProperty(contextName,"projectName",projectName);
	cm.setCurrentProperty(contextName,"hostName",hostName);
	cm.setCurrentProperty(contextName,"numModelStates",
			      numModelStates+"");
	cm.setCurrentProperty(contextName,"randomSeed",randomSeed+"");
	return "parameters-set";
    }

    public String loadProject() throws Exception {
	System.out.println("Loading project");
	if(!isInitialized) {
	    initWebServices();
	}
	contextList=cm.listContext(codeName);
	if(contextList==null || contextList.length<=0) {
	    System.out.println(contextList.toString());
	    System.out.println("No archived projects");
	}
	else {
	    System.out.println("Context has "+contextList.length+" elements");
	    for(int i=0;i<contextList.length;i++) {
		System.out.println(contextList[i]);
	    }
	}
	
        return ("list-old-projects");

    }
    
    public String createInputFile() throws Exception {
	//The value should be set by JSF from the associated JSP page.
	//We just need to clean it up and add it to the context
	
	cm.setCurrentProperty(contextName,"scriptInput",inputFile);
	return "input-file-set";
    }

    public String getPortalUserName() {
	userName=Utility.getUserName(defaultName);
	System.out.println("Username is "+userName);
	return userName;
    }

    //This is the command that runs the thing.
    public String launchProject() {
	return "project-launched";
    }

    private String trimLine(String line) {
	String endLine="\015";
	while(line.lastIndexOf(endLine)>0) {
	    line=line.substring(line.lastIndexOf(endLine));
	}
	
	return line;
    }
}
