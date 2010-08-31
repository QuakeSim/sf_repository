package cgl.quakesim.disloc;

import java.util.Map;

import com.db4o.Db4o;
import com.db4o.ObjectServer;


import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

import org.servogrid.genericproject.Utility;

public class Db4oServletContextListener implements ServletContextListener {
	
		
	private ObjectServer userdb = null;
	private ObjectServer shareddb = null;
	
    static FacesContext facesContext=null;
    static ExternalContext extrenalContext=null;
    static Object requestObj=null;

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
		ServletContext context = arg0.getServletContext();
		context.removeAttribute("userdb");
		context.removeAttribute("shareddb");
		close();
		context.log("db4o shutdown");		
	}

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
    	
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
		close();
		
        /*
        ExternalContext context = getContext();
        ServletContext scontext = arg0.getServletContext();        
        requestObj = context.getRequest();
        */        
        
		ServletContext scontext = arg0.getServletContext();
		
        String filePath = scontext.getRealPath("../Disloc3.db");        
		userdb = Db4o.openServer(filePath, 0);   		
		
		String filePath2 = scontext.getRealPath("../WebServices/WEB-INF/Descriptors/users/automatedDislocDB/overm5.db");
		shareddb = Db4o.openServer(filePath2, 0);
        
        
		scontext.setAttribute("userdb", userdb);
		scontext.setAttribute("shareddb", shareddb);

		System.out.println("[Db4oServletContextListener/contextDestroyted] " + filePath + Utility.getUserName("deafultuser"));
		
		
		/*  
		ServletContext context = arg0.getServletContext();
		System.out.println("[Db4oServletContextListener/contextDestroyted] " + context.getRealPath("../WebServices/WEB-INF/Descriptors/users/automatedDislocDB"));
		String filePath = context.getRealPath("../Disloc3.db");
		userdb = Db4o.openServer(filePath, 0);		
		
		filePath = context.getRealPath("../WebServices/WEB-INF/Descriptors/users/automatedDislocDB/overm5.db");
		shareddb = Db4o.openServer(filePath, 0);
		context.setAttribute("userdb", userdb);
		context.setAttribute("shareddb", shareddb);
		*/
		
		// context.log("db4o startup on");		
	}
	

	private void close() {
		
		if (userdb != null)
			userdb.close();			

		userdb = null;
		
		if (shareddb != null)
			shareddb.close();			

		shareddb = null;
	}
}
