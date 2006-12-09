/**
 * This bean includes metadata needed to set up plot of a
 * network.
 */
package cgl.webservices;

public class GPSNetworkInfoBean {
    String networkName;
    String networkIconUrl;
    GPSStationBean[] stationBeanArray;
    int numberOfStations;
    

    public GPSNetworkInfoBean() {
    }
    
    public int getNumberOfStations(){
	return stationBeanArray.length;
    }

    /**
     * Note this differs from the usual bean setter.
     */
    public void assignNetworkIconUrl(String color) {
	networkIconUrl="http://labs.google.com/ridefinder/images/mm_20_" 
	    + color+".png";
    }

    /**
     * These are standard accessor methods.
     */
    public void setStationsBeanArray(GPSStationBean[] stationBeanArray){
	this.stationBeanArray=new GPSStationBean[stationBeanArray.length];
	for(int i=0;i<stationBeanArray.length;i++){
	    this.stationBeanArray[i]=stationBeanArray[i];
	}
    }

    public GPSStationBean[] getStationsBeanArray() {
	return stationBeanArray;
    }
    
    public void setNetworkName(String networkName) {
	this.networkName=networkName;
    }

    public String getNetworkName() {
	return networkName;
    }

    public void setNetworkIconUrl(String networkIconUrl){
	this.networkIconUrl=networkIconUrl;
    }
    
    public String getNetworkIconUrl(){
	return networkIconUrl;
    }

    public int getStationBeanArraySize(){
	return stationBeanArray.length;
    }

}
