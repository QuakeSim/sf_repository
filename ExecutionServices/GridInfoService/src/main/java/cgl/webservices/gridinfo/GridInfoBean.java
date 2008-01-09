/** 
 * This class (it's not really a bean) encapsulates some necessary grid info operations.  
 */
package cgl.webservices.gridinfo;

public class GridInfoBean {
	 Host[] hosts;

	 public GridInfoBean() {
		  //Empty. The calling service will populate it.
	 }

	 public Host[] getAllHosts() {
		  return hosts;
	 }
	 
	 public void setAllHosts(Host[] tmphosts) {
		  this.hostNames=System.arraycopy(tmphosts,0,this.hostNames,0,tmphosts.length);
	 }

	 public String getHostGatekeeper(Host host) {
		  return host.getGatekeeper();
	 }
	 
	 public String getUserHomeDirectory(Host host, String userName) {
		  return host.getUserHomeDirectory(userName);
	 }

	 
}