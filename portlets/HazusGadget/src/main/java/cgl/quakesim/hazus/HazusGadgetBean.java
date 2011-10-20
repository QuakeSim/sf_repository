package cgl.quakesim.hazus;

import org.servogrid.genericproject.GenericProjectBean;
public class HazusGadgetBean extends GenericProjectBean{

	 private OpenShaBean osrs;

	 private static final String RUN_HAZUS_ACTION="run-hazus";

	 public HazusGadgetBean() {
		  System.out.println("Constructing the HazusGadget Bean");
		  osrs=new OpenShaBean();
	 }
	
	 public String invokeHazusService() throws Exception {
		  try {
				osrs.getOpenShaHazusOutput(33.5,34.5,-119.0,-117.0,0.1,7.2,0.0,33.94,-117.87,5.0,90.0);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
				throw ex;
		  }		  
		  return RUN_HAZUS_ACTION;
	 }
}