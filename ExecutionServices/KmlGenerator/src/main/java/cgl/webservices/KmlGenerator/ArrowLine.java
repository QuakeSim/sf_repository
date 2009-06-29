package cgl.webservices.KmlGenerator;

public class ArrowLine {
	
	private Coordinate startPoint;
	private Coordinate endPoint;
	private Coordinate arrowTail1;
	private Coordinate arrowTail2;
	
	public Coordinate getStartPoint(){
		return this.startPoint;
	}
	public void setStartPoint(Coordinate tmp_str) {
		this.startPoint=tmp_str;
	}
	
	public Coordinate getEndPoint(){
		return this.endPoint;
	}
	public void setEndPoint(Coordinate tmp_str) {
		this.endPoint=tmp_str;
	}
	public Coordinate getArrowTail1(){
		return this.arrowTail1;
	}
	public void setArrowTail1(Coordinate tmp_str) {
		this.arrowTail1=tmp_str;
	}
	public Coordinate getArrowTail2(){
		return this.arrowTail2;
	}
	public void setArrowTail2(Coordinate tmp_str) {
		this.arrowTail2=tmp_str;
	}
	

}
