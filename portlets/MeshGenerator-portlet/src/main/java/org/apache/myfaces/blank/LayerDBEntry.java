package org.apache.myfaces.blank;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import javax.faces.model.SelectItem;

public class LayerDBEntry {
	SelectItem layerName=new SelectItem();
	String layerAuthor= new String();
	
	
	public void setLayerName(SelectItem tmp_str) {
		this.layerName = tmp_str;
	}

	public SelectItem getLayerName() {
		return layerName;
	}		
	
	public void setLayerAuthor(String tmp_str) {
		this.layerAuthor = tmp_str;
	}

	public String getLayerAuthor() {
		return layerAuthor;
	}		


}
