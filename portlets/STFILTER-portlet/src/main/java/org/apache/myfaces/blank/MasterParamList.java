/**
* This static class is used to create and manage a singleton Vector.
*/
package org.apache.myfaces.blank;

import java.util.Vector;

public class MasterParamList {
    
    static Vector masterParamList=null;

    public static Vector getMasterParamList() {
	if(masterParamList==null) {
	    System.out.println("Initializing singleton");
	    masterParamList=new Vector();
	}
	return masterParamList;
    }

}
