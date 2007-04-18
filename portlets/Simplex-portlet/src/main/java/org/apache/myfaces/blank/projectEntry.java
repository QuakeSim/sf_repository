/**
 * This is the class for the layer
 * classes.
 */
package org.apache.myfaces.blank;


public class projectEntry {
	
	String startTemp = "";
	
	String maxIters = "";

	String origin_lon = "";

	String origin_lat = "";


	public void reset()
	{
		startTemp = "";
		
		maxIters = "";

		origin_lon = "";

		origin_lat = "";

	}
	
	public void setStartTemp(String tmp_str) {
		this.startTemp = tmp_str;
	}

	public String getStartTemp() {
		return startTemp;
	}	
	
	public void setMaxIters(String tmp_str) {
		this.maxIters = tmp_str;
	}

	public String getMaxIters() {
		return maxIters;
	}
	
	public void setOrigin_lon(String tmp_str) {
		this.origin_lon = tmp_str;
	}

	public String getOrigin_lon() {
		return origin_lon;
	}

	public void setOrigin_lat(String tmp_str) {
		this.origin_lat = tmp_str;
	}

	public String getOrigin_lat() {
		return origin_lat;
	}
	
}
