package cgl.webservices.geofest;

/**
 * This is a bean for modeling geofest layers.
 */

public class Layer {
	
	 String layerName = "";
	 String layerOriginX = "";
	 String layerOriginY = "";
	 String layerOriginZ = "";
	 String layerLatOrigin = "";
	 String layerLonOrigin = "";
	 String layerLength = "";
	 String layerWidth = "";
	 String layerDepth = "";
	 String lameLambda = "";
	 String lameMu = "";
	 String viscosity = "";
	 String exponent = "";
	 
	 
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
