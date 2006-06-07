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

    //properties
    private String userName;
    private String defaultName="defaultUser";
    private String contextUrl;
    private String contextBasePath;
    private String FS="FS";
    private String CODENAME="RDAHMM";

    public String getContextUrl() {
	System.out.println(this.toString()+" "+contextUrl);
	return contextUrl;
    }

    public void setContextUrl(String cUrl) {
	this.contextUrl=cUrl;
	System.out.println(this.toString()+" "+contextUrl);
    }

    public String getContextBasePath() {
	System.out.println(this.toString()+" "+contextBasePath);
	return contextBasePath;
    }

    public void setContextBasePath(String basepath) {
	this.contextBasePath=basepath;
	System.out.println(this.toString()+" "+contextBasePath);
    }
    
    /**
     * default empty constructor
     */
    public RDAHMMBean(){   
	try {
	    //--------------------------------------------------
	    //Set up the context manager service.
	    //--------------------------------------------------	
	    String base_userpath=getContextBasePath()+
		File.separator+userName+File.separator+CODENAME;
	    
	    ContextManagerImpService cmws= 
		new ContextManagerImpServiceLocator();
	    ContextManagerImp cm
		=cmws.getContextManager(new URL(contextUrl));
	    ((ContextManagerSoapBindingStub) cm).setMaintainSession(true);
	    
	    cm.setContextStorage(FS);
	    cm.init(userName,base_userpath);
	    cm.addContext(CODENAME);
	}
	catch(Exception ex) {
	    ex.printStackTrace();
	}
    }
    
    /**
     * Method that is backed to a submit button of a form.
     */
    public String newProject(){
        //Do real logic
	System.out.println("Creating new project");
        return ("create-new-project");
    }

    public String loadProject() {
	System.out.println("Creating new project");
        return ("list-old-projects");

    }

    public String getPortalUserName() {
	userName=Utility.getUserName(defaultName);
	System.out.println("Username is "+userName);
	return userName;
    }

}
