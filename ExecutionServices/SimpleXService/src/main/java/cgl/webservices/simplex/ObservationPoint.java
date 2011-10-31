package cgl.webservices.simplex;
/**
 * This is a bean for modeling Observation Point in google map.
 */


public class ObservationPoint {
	public double xloc;

	public double yloc;

	public VectorElement NorthVec = new VectorElement();

	public VectorElement EastVec = new VectorElement();

	public VectorElement UpVec = new VectorElement();

	 public VectorElement SarVec=new VectorElement();

	public String toString() {
		return "[xloc=" + xloc + ", yloc=" + yloc + "]";
	}

}
