/**
 * This is the abstract parent class for the station containers.
 * Note the estParamVector is actually filled in by the child
 * classes.
 */
package org.apache.myfaces.blank;

import java.util.Vector;
import java.util.List;

//Faces-specific classes
import javax.faces.event.ActionEvent;
import javax.faces.component.UIData;
import javax.faces.model.SelectItem;

public class StationContainer {
    //These must be shared between all StationContainer
    //instances.
    static Vector masterParamList;
    static boolean mplInitialized=false;

    SelectItem[] mplHelper;
    Vector estParamVector=new Vector();
    EstimateParameter newEstParameter;
    String newLine="\n";
    String space=" ";
    String siteName;

    UIData dataTable,dataTable2;

    static EstimateParameter constantBiasEast, constantBiasNorth, constantBiasUp;
    EstimateParameter velocityEast, velocityNorth, velocityUp;
    EstimateParameter episodicEast,episodicNorth, episodicUp;
    EstimateParameter annualAmpEast, annualAmpNorth, annualAmpUp;
    EstimateParameter annualPhaseEast, annualPhaseNorth, annualPhaseUp;
    EstimateParameter semiannualAmpEast, semiannualAmpNorth, semiannualAmpUp;

    public StationContainer(String stationName){
	this();
	setSiteName(stationName);
    }

    public StationContainer() {
	masterParamList=MasterParamList.getMasterParamList();

	constantBiasEast=new ConstantBiasEast();
	constantBiasNorth=new ConstantBiasNorth();
	constantBiasUp=new ConstantBiasUp();
	velocityEast=new VelocityEast();
	velocityNorth=new VelocityNorth();
	velocityUp=new VelocityUp();
	episodicEast=new EpisodicEast();
	episodicNorth=new EpisodicNorth();
	episodicUp=new EpisodicUp();
	annualAmpEast=new AnnualAmpEast();
	annualAmpNorth=new AnnualAmpNorth();
	annualAmpUp=new AnnualAmpUp();
	annualPhaseEast=new AnnualPhaseEast();
	annualPhaseNorth=new AnnualPhaseNorth();
	annualPhaseUp=new AnnualPhaseUp();
	semiannualAmpEast=new SemiannualAmpEast();
	semiannualAmpNorth=new SemiannualAmpNorth();
	semiannualAmpUp=new SemiannualAmpUp();

	initMasterParamList();
	//	addDefaultEstParams();
	//	updateMplHelper();
    }

    /**
     * The masterParamList vector contains one of each
     * parameter type. 
     */
    public void initMasterParamList() {
	if(!mplInitialized) {
	    System.out.println("Initializing master list");
	    masterParamList.add(constantBiasEast);
	    masterParamList.add(constantBiasNorth);
	    masterParamList.add(constantBiasUp);
	    masterParamList.add(velocityEast);
	    masterParamList.add(velocityNorth);
	    masterParamList.add(velocityUp);
	    masterParamList.add(episodicEast);
	    masterParamList.add(episodicNorth);
	    masterParamList.add(episodicUp);
	    masterParamList.add(annualAmpEast);
	    masterParamList.add(annualAmpNorth);
	    masterParamList.add(annualAmpUp);
	    masterParamList.add(annualPhaseEast);
	    masterParamList.add(annualPhaseNorth);
	    masterParamList.add(annualPhaseUp);
	    masterParamList.add(semiannualAmpEast);
	    masterParamList.add(semiannualAmpNorth);
	    masterParamList.add(semiannualAmpUp);

	    mplInitialized=true;
	}
    }

    public void addDefaultEstParams() {
// 	addEstParameter(constantBiasEast);
// 	addEstParameter(constantBiasNorth);
// 	addEstParameter(constantBiasUp);
// 	addEstParameter(velocityEast);
// 	addEstParameter(velocityNorth);
// 	addEstParameter(velocityUp);
    }
    

    /**
     * This updates the SelectItem array that is 
     * used in the display.
     */
//     public void updateMplHelper(){
// 	if(masterParamList!=null && masterParamList.size()>1) {
// 	    mplHelper=new SelectItem[masterParamList.size()];
// 	    for(int i=0;i<masterParamList.size();i++) {
// 		String thename=((EstimateParameter)masterParamList.get(i)).getParameterFullName();
// 		System.out.println(thename);
// 		mplHelper[i]=new SelectItem(thename,thename);
// 	    }
// 	}
//     }

    public void setNewEstParameter(EstimateParameter newEstParameter){
	this.newEstParameter=newEstParameter;
    }
    
    public EstimateParameter getNewEstParameter(){
	return newEstParameter;
    }
    
    public SelectItem[] getMplHelper() {
	return mplHelper;
    }

    public void setMplHelper(SelectItem[] mplHelper) {
	this.mplHelper=mplHelper;
    }
	
    public Vector getMasterParamList() {
	return masterParamList;
    }

    public void setMasterParamList(Vector masterParamList) {
	this.masterParamList=masterParamList;
    }

    public UIData getDataTable() {
	return dataTable;
    }
    
    public void setDataTable(UIData dataTable) {
	this.dataTable=dataTable;
    }

    public UIData getDataTable2() {
	return dataTable2;
    }
    
    public void setDataTable2(UIData dataTable2) {
	this.dataTable2=dataTable2;
    }

    public Vector getEstParamVector() {
	return estParamVector;
    }
    
    public void setEstParamVector(Vector estParamVector){
	this.estParamVector=estParamVector;
    }
    
    public void updateEstParameterListener(ActionEvent actionEvent) {
	System.out.println("I'm listening...");
    }

    public void removeEstParameterListener(ActionEvent actionEvent) {
	if(dataTable.getRowData() instanceof EstimateParameter){
	    removeEstParameter((EstimateParameter)dataTable.getRowData());
	}
    }

    public void addEstParameterListener(ActionEvent actionEvent) {
	if(dataTable2.getRowData() instanceof EstimateParameter){
	    addEstParameter((EstimateParameter)dataTable2.getRowData());
	}
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
     * This is a no-argument form suitable for interaction with 
     * JSP pages.
     */
//     public void addEstParameterAction(){
// 	if(newEstParameter!=null) {
// 	    masterParamList.remove(newEstParameter);
// 	    estParamVector.add(newEstParameter);
// 	    //    updateMplHelper();
// 	}
// 	else {
// 	    System.out.println("No new parameter was added.");
// 	}
//     }

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
}
