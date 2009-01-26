package cgl.webservices.geofest;

/**
 * This is a bean for modeling geofest layers.
 */

public class Layer {

	 //Some default values, taken from the Northridge 
	 //layer, of course.
	 String layerName="NorthridgeAreaUpper";
	 String layerOriginX = "-103.0";
	 String layerOriginY = "-104.0";
	 String layerOriginZ = "0.0";
	 String layerLatOrigin = "34.243";
	 String layerLonOrigin = "-118.72";
	 String layerLength = "240.0";
	 String layerWidth = "240.0";
	 String layerDepth = "5.0";
	 String lameLambda = "17.0";
	 String lameMu = "17.0";
	 String viscosity = "17.0";
	 String exponent = "1.0";
	 
	 
	 public void setLayerName(String tmp_str) {
		  this.layerName = tmp_str;
	 }
	 
	public String getLayerName() {
		 return layerName;
	}	
	 
	 public void setLayerOriginX(String tmp_str) {
		  this.layerOriginX = tmp_str;
	 }
	 
	public String getLayerOriginX() {
		 return layerOriginX;
	}
	 
	 public void setLayerOriginY(String tmp_str) {
		  this.layerOriginY = tmp_str;
	 }
	 
	 public String getLayerOriginY() {
		  return layerOriginY;
	 }
	 
	 public void setLayerOriginZ(String tmp_str) {
		this.layerOriginZ = tmp_str;
	 }
	 
	 public String getLayerOriginZ() {
		  return layerOriginZ;
	 }
	 
	 public void setLayerLatOrigin(String tmp_str) {
		this.layerLatOrigin = tmp_str;
	 }
	 
	 public String getLayerLatOrigin() {
		  return layerLatOrigin;
	}
	 
	 
	 public void setLayerLonOrigin(String tmp_str) {
		  this.layerLonOrigin = tmp_str;
	}
	 
	 public String getLayerLonOrigin() {
		  return layerLonOrigin;
	 }
	 
	 public void setLayerLength(String tmp_str) {
		  this.layerLength = tmp_str;
	 }
	 
	 public String getLayerLength() {
		  return layerLength;
	 }
	 
	 public void setLayerWidth(String tmp_str) {
		  this.layerWidth = tmp_str;
	 }
	 
	 public String getLayerWidth() {
		  return layerWidth;
	 }
	
	 public void setLayerDepth(String tmp_str) {
		  this.layerDepth = tmp_str;
	 }
	 
	 public String getLayerDepth() {
		return layerDepth;
	 }
	 
	 public void setLameLambda(String tmp_str) {
		this.lameLambda = tmp_str;
	 }

	 public String getLameLambda() {
		  return lameLambda;
	 }

	 public void setLameMu(String tmp_str) {
		  this.lameMu = tmp_str;
	 }
	 
	 public String getLameMu() {
		return lameMu;
	 }
	 
	 public void setViscosity(String tmp_str) {
		  this.viscosity = tmp_str;
	 }
	 
	 public String getViscosity() {
		  return viscosity;
	 }
	 
	 public void setExponent(String tmp_str) {
		  this.exponent = tmp_str;
	 }
	 
	 public String getExponent() {
		  return exponent;
	 }
}
