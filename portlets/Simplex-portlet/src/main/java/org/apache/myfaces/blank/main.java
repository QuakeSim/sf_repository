package org.apache.myfaces.blank;
public class main {
    public static void main(String[] args) {	    
	try {
	    Simplex2Bean rb=new Simplex2Bean();
	    rb.setCodeName("Simplex");
	    rb.setContextUrl("");
	    rb.setIsInitialized(true);
	    rb.newProject();
	}
	catch(Exception ex) {
	    ex.printStackTrace();
	}
    }
}
