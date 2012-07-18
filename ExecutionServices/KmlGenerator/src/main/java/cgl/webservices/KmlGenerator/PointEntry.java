package cgl.webservices.KmlGenerator;

public class PointEntry {

	private String x;
	private String y;
	private String deltaXValue;
	private String deltaXName;
	private String deltaYValue;
	private String deltaYName;
	private String deltaZValue;
	private String deltaZName;
	private String folderTag;
	

	public PointEntry() {
	}
	
	public void setFolderTag(String tmp_str) {
		this.folderTag=tmp_str;
	}
	public String getFolderTag() {
		return this.folderTag;
	}
	public void setDeltaZName(String tmp_str) {
		 this.deltaZName=tmp_str;
	}
	public String getDeltaZName() {
		return this.deltaZName;
	}
	public void setDeltaYName(String tmp_str) {
		this.deltaYName=tmp_str;
	}
	public String getDeltaYName() {
		return this.deltaYName;
	}
	
	public void setDeltaXName(String tmp_str) {
		this.deltaXName=tmp_str;
	}
	public String getDeltaXName() {
		return this.deltaXName;
	}
	public void setDeltaZValue(String tmp_str) {
		this.deltaZValue=tmp_str;
	}
	public String getDeltaZValue() {
		return this.deltaZValue;
	}
	public void setDeltaYValue(String tmp_str) {
		this.deltaYValue=tmp_str;
	}
	public String getDeltaYValue() {
		return this.deltaYValue;
	}
	public void setDeltaXValue(String tmp_str) {
		this.deltaXValue=tmp_str;
	}
	public String getDeltaXValue() {
		return this.deltaXValue;
	}
	public void setY(String tmp_str) {
		this.y=tmp_str;
	}
	public String getY() {
		return this.y;
	}
	public void setX(String tmp_str) {
		this.x=tmp_str;
	}
	public String getX() {
		return this.x;
	}
	
	
}
