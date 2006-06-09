package org.apache.myfaces.blank;
public class main {
    public static void main(String[] args) {	    
	try {
	    RDAHMMBean rb=new RDAHMMBean();
	    rb.setCodeName("RDAHMM");
	    rb.setContextUrl("");
	    rb.setIsInitialized(true);
	    rb.newProject();
	}
	catch(Exception ex) {
	    ex.printStackTrace();
	}
    }
}
