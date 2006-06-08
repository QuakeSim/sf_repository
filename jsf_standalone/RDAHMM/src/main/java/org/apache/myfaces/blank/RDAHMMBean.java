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
	    
	    ContextManagerImpService cmws= 
		new ContextManagerImpServiceLocator();
	    System.out.println("CMWS initialized:"+cmws.toString());

	    ContextManagerImp cm=
		cmws.getContextManager(new URL(contextUrl));
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
    public String newProject(){
	if(!isInitialized) {
	    initWebServices();
	}
        //Do real logic
	System.out.println("Creating new project");
        return ("create-new-project");
    }

    public String loadProject() {
	System.out.println("Loading project");
	if(!isInitialized) {
	    initWebServices();
	}
        return ("list-old-projects");

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

}
