package org.apache.myfaces.blank;
public class main {
    public static void main(String[] args) {	    
	try {
	    SimplexBean rb=new SimplexBean();
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
