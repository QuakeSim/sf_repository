package cgl.webservices.gridinfo;

public HomeDirectory {
	 String userName;
	 String homeDirectory;
	 String hostName;

	 public void setHomeDirectory(String userName,
											String hostName,
											String homeDirectory) {
		  this.homeDirectory=homeDirectory;
	 }

	 public String getHomeDirectory(String userName, String hostName) {
		  return this.homeDirectory;
	 }

}