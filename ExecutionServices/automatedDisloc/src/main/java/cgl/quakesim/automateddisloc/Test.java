package cgl.quakesim.automateddisloc;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		

		AutomatedDislocBean adb = new AutomatedDislocBean();
		adb.run("http://earthquake.usgs.gov/earthquakes/catalogs/7day-M5.xml");
		

	}

}
