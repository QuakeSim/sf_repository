package org.apache.myfaces.blank;

import WebFlowClient.cm.*;

/**
 * 
 */
public class RDAHMMBean {

    //properties
    private String userName;
    private String defaultName="defaultUser";
    
    /**
     * default empty constructor
     */
    public RDAHMMBean(){   
	//--------------------------------------------------
	//Set up the context manager service.
	//--------------------------------------------------
	String propfile=
	    application.getRealPath("/WEB-INF/conf/GEMDSTEST.properties");
	props.setProperties(propfile);
	
	
	String complexUrl=props.getProperty("@UI_SERVER_NAME@");
	String complexCM=complexUrl+props.getProperty("CM");
	String base_userpath=props.getProperty("ContextBasePath")
	    +File.separator+userName+File.separator+CODENAME;
	
	ContextManagerImpService cmws = new ContextManagerImpServiceLocator();
	ContextManagerImp cm = cmws.getContextManager(new URL(complexCM));
	((ContextManagerSoapBindingStub) cm).setMaintainSession(true);
	
	cm.setContextStorage(props.getProperty("Storage"));
	cm.init(userName,base_userpath);
	cm.addContext(CODENAME);
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
