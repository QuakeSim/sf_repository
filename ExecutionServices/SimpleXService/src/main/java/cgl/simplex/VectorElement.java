package cgl.webservices.simplex;

public class VectorElement {
	public double observ;
	public double calc;
	public double o_c;
	public double error;
	public int type;
	//type 1 = north 2 = east 3 = up 0 = null point
	public VectorElement() {
		observ=0.0;
		calc=0.0;
		o_c=0.0;
		error=0.0;
		type=0;
	}

	public String toString() {
		//return "[observ=" + observ + ", calc=" + calc +", o_c=" + o_c +", error=" + error + "]";
		String tmp="<br>[observ   "+ "   calc   " + "   o_c   " + "   error" + "]<br>";
		tmp+=""+observ+" "+ calc+ " "+ o_c+" "+error;
		return tmp;
	}
	
}

