package org.apache.myfaces.blank;

//Faces classes
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

//Servlet and portlet stuff
import javax.servlet.http.HttpServletRequest;
import javax.portlet.PortletRequest;


//Standard Java stuff
import java.util.Map;

public class Utility {

    static FacesContext facesContext=null;
    static ExternalContext extrenalContext=null;
    static Object requestObj=null;

    public static String getUserName(String defaultName) {
	
	//This may change
	String userName=defaultName;
	
	ExternalContext context=getContext();
	if(context==null) return defaultName;
	
	requestObj=context.getRequest();
	
	//We may be in a portlet
	if(requestObj instanceof PortletRequest) {	    
	    Map userInfo=
		(Map) ((PortletRequest)requestObj).getAttribute(PortletRequest.USER_INFO);
	    userName = 
		(userInfo!=null) ? (String) userInfo.get("user.name") : null;
	    if (userName == null) {
		userName = ((PortletRequest)requestObj).getRemoteUser();
	    }

	    //Still null?
	    if(userName==null) {
		userName=defaultName;
	    }
	    return userName;
	}

	//We have have this somehow through other means
	else if(requestObj instanceof HttpServletRequest) {	    
	    userName = ((HttpServletRequest)requestObj).getRemoteUser();
	    
	    //Still null?
	    if(userName==null) {
		userName=defaultName;
	    }
	    return userName;
	}
	
	//Use default value
	else {
	    return defaultName;
	}
	
    }
	
    /**
     * Return the context. Note this is allowed to be null.
     */
    private static ExternalContext getContext() {
	ExternalContext externalContext=null;
	FacesContext facesContext=FacesContext.getCurrentInstance();
	if(facesContext==null) {
	    return null;
	}
	
	try {
	    externalContext=facesContext.getExternalContext();
	}
	catch(Exception ex) {
	    ex.printStackTrace();
	}

	return externalContext;
    }
}
