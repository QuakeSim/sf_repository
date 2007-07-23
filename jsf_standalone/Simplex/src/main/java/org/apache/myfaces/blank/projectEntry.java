package org.apache.myfaces.blank;

public class projectEntry {
	 String projectName;
	 String projectDirectory;
		String startTemp ;
		
		String maxIters;

		String origin_lon;

		String origin_lat;
		
		String hostName;
		
		String creationDate;
		
	    public projectEntry() {
	    }

		public void reset()
		{
			projectName="";
			projectDirectory="";
			startTemp = "";
			
			maxIters = "";

			origin_lon = "";

			origin_lat = "";
			hostName = "";
			creationDate = "";

		}

		public void setCreationDate(String tmp_str) {
			this.creationDate= tmp_str;
		}
		public String getCreationDate() {
			return this.creationDate;
		}
		
		public void setHostName ( String tmp_str ) {
			this.hostName= tmp_str;
			
		}
		
		public String getHostName ( ) {
			return this.hostName;
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
	 
	 public String getProjectName(){
		  return this.projectName;
	 }
	 public void setProjectName(String tmp_str){
		  this.projectName=tmp_str;
	 }

	 public String getProjectDirectory(){
		  return this.projectDirectory;
	 }

	 public void setProjectDirectory(String tmp_str){
		 this.projectDirectory=tmp_str;
	 }




}