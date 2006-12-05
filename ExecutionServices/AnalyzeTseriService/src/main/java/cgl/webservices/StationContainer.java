/**
 * This is the abstract parent class for the station containers.
 * Note the estParamVector is actually filled in by the child
 * classes.
 */
package cgl.webservices;

import java.util.Vector;
import java.util.List;


public class StationContainer {
    //These must be shared between all StationContainer
    //instances.
    Vector masterParamList;
    Vector estParamVector;

    boolean mplInitialized=false;

    EstimateParameter newEstParameter;
    String newLine="\n";
    String space=" ";
    String siteName;

    public StationContainer(String stationName){
	this();
	setSiteName(stationName);
    }

    public StationContainer() {
    }

    
    /**
     * This adds the provided EstimateParameter to the 
     * containing vector.  It is also removed from the 
     * available master list, since we can't duplicate.
     */
    public void addEstParameter(EstimateParameter estParam){
	boolean testit=masterParamList.removeElement(estParam);
	estParamVector.add(estParam);
	//	    updateMplHelper();
	System.out.println("testit is "+testit);
    }
    

    /**
     * This removes the provided EstimateParameter to the 
     * containing vector.  The parameter is added back to the
     * master list.
     */    
    public void removeEstParameter(EstimateParameter estParam) {
	estParamVector.removeElement(estParam);
	masterParamList.add(estParam);
	//	updateMplHelper();
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
	String retString=null; //Default value
	if(estParamVector!=null && estParamVector.size()>0) {
	    retString=getSiteName()+space+estParamVector.size()+newLine;
	    for(int i=0;i<estParamVector.size();i++) {
		retString+=((EstimateParameter)estParamVector.get(i)).printStringLine()+newLine;
	    }
	}
	return retString;
    }

    public void setNewEstParameter(EstimateParameter newEstParameter){
	this.newEstParameter=newEstParameter;
    }
    
    public EstimateParameter getNewEstParameter(){
	return newEstParameter;
    }
    	
    public Vector getMasterParamList() {
	return masterParamList;
    }

    public void setMasterParamList(Vector masterParamList) {
	this.masterParamList=masterParamList;
    }

    public Vector getEstParamVector() {
	return estParamVector;
    }
    
    public void setEstParamVector(Vector estParamVector){
	this.estParamVector=estParamVector;
    }

}
