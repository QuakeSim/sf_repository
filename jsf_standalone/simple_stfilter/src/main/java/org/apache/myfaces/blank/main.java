package org.apache.myfaces.blank;
public class main {
    public static void main(String[] args) {	    
	try {
	    STFILTERBean rb=new STFILTERBean();
	    rb.setCodeName("STFILTER");
	    rb.setContextUrl("");
	    rb.setIsInitialized(true);
	    rb.newProject();
	}
	catch(Exception ex) {
	    ex.printStackTrace();
	}
    }
}
