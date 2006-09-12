/**
 * This is the abstract parent class for the station containers.
 * Note the estParamVector is actually filled in by the child
 * classes.
 */
package org.apache.myfaces.blank;

import java.util.Vector;

public abstract class StationContainer {
    Vector estParamVector=new Vector();
    String newLine="\n";
    String space=" ";


    String siteName;

    public Vector getEstParamVector() {
	return estParamVector;
    }
    
    public void setEstParamVector(Vector estParamVector){
	this.estParamVector=estParamVector;
    }

    /**
     * This adds the provided EstimateParameter to the 
     * containing vector.
     */
    public void addEstParameter(EstimateParameter estParam){
	estParamVector.add(estParam);
    }

    /**
     * This removes the provided EstimateParameter to the 
     * containing vector.
     */    
    public void removeEstParameter(EstimateParameter estParam) {
	estParamVector.remove(estParam);
    }

    public String getSiteName() {
	return siteName;
    }
    
    public void setSiteName(String siteName) {
	this.siteName=siteName;
    }

    /**
     * This returns as a big string a single station's information.
     * If the station does not have any values (i.e. it is going to 
     * use the all_sites default values), then don't print anything.
     * 
     */
    public String printContents() {
	String retString=newLine; //Default value
	if(estParamVector!=null && estParamVector.size()>0) {
	    retString=getSiteName()+space+estParamVector.size()+newLine;
	    for(int i=0;i<estParamVector.size();i++) {
		retString+=((EstimateParameter)estParamVector.get(i)).printStringLine()+newLine;
	    }
	}
	return retString;
    }
}
