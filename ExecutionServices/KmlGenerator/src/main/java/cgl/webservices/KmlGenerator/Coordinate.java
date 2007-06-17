/*
 *  a Coordinate class to contain x,y
 */

package cgl.webservices.KmlGenerator;

public class Coordinate implements Comparable {

	public Coordinate() {
		x = new Double(0.0);
		y = new Double(0.0);
	}
	
	public Coordinate(double ax, double ay) {
		x = new Double(ax);
		y = new Double(ay);
	}

	public double getX() {
		return x.doubleValue();
	}

	public double getY() {
		return y.doubleValue();
	}

	public void setX(double t) {
		this.x = new Double(t);
	}

	public void setY(double t) {
		this.y = new Double(t);
	}

	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}

	public boolean equals(Object other) {
		if (getClass() == other.getClass()) {
			Coordinate otherItem = (Coordinate) other;
			return x.equals(otherItem.x) && y.equals(otherItem.y);
		} else
			return false;
	}

	public int hashCode() {
		return 13 * x.hashCode() + 17 * y.hashCode();
	}

	public int compareTo(Object other) {
		Coordinate otherItem = (Coordinate) other;
		if (y != otherItem.y) {
			return y.compareTo(otherItem.y);
		} else {
			return x.compareTo(otherItem.x);
		}
	}

	private Double x;

	private Double y;
}