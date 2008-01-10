package cgl.quakesim.geofest;

import java.util.ArrayList;
import java.util.List;

public class GeoFESTElement {
	List name = new ArrayList();
	List value = new ArrayList();

	public int size() {
		return name.size();
	}

	public void addElement(String tmp_str1, String tmp_str2){
		
		name.add(tmp_str1);
		value.add(tmp_str2);
	}
	
	public String getValue(int i) {
		if( i< this.value.size() ) {
			return this.value.get(i).toString();
		}else {
			return "null";
		}
	}
	
	public void setValue(String tmp_str , int i) {
		if( i< this.value.size() ) {
			this.value.set(i, tmp_str);
		}
	}

	public String getName(int i) {
		if( i< this.name.size() ) {
			return this.name.get(i).toString();
		}else {
			return "null";
		}
	}
	
	public void setName(String tmp_str , int i) {
		if( i< this.name.size() ) {
			this.name.set(i, tmp_str);
		}
	}	
	
	public List getValueList() {
		return this.value;
	}
	public void setValueList(List tmp_list) {
		this.value=tmp_list;
	}
	
	public List getNameList() {
		return this.name;
	}
	public void setNameList(List tmp_list) {
		this.name=tmp_list;
	}
	public void reset(){
		name.clear();
		value.clear();
	}

}
