package org.servogrid.genericproject;

//Faces classes
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

//Servlet and portlet stuff
import javax.servlet.http.HttpServletRequest;
import javax.portlet.PortletRequest;
import javax.servlet.http.*;

//Standard Java stuff
import java.util.Map;

public class Utility {

    static FacesContext facesContext=null;
    static ExternalContext extrenalContext=null;
    static Object requestObj=null;
	 
    public static String getUserName(String defaultName) {
	
        String userName = defaultName;
        ExternalContext context = getContext();
        if(context == null)
            return defaultName;
        requestObj = context.getRequest();
        if(requestObj instanceof PortletRequest) {
				
            // System.out.println("[Utility/getUserName] This request is an instanceof PortletRequest");
            Map userInfo = (Map)((PortletRequest)requestObj).getAttribute("javax.portlet.userinfo");
            userName = userInfo == null ? null : (String)userInfo.get("user.name");
            if(userName == null) {
                userName = ((PortletRequest)requestObj).getRemoteUser();
				}
				//Still null?  Use default name
            if(userName == null) {
                userName = defaultName;
				}
            // System.out.println("[Utility/getUserName] Username : " + userName + "\n\n");
            return userName;
        }

        if(requestObj instanceof HttpServletRequest){
            // System.out.println("[Utility/getUserName] This request is an instanceof HttpServletRequest");
            HttpSession session = (HttpSession)context.getSession(false);
            HttpServletResponse res = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            java.util.Enumeration e = session.getAttributeNames();
            // System.out.println("[Utility/getUserName] Current page : " + req.getRequestURI());
            userName = (String)((HttpSession)(HttpSession)context.getSession(false)).getAttribute("email");
            if(userName == null) {
                userName = defaultName;
				}
            // System.out.println("[Utility/getUserName] Username : " + userName + "\n\n");
            return userName;
        } else
        {
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
