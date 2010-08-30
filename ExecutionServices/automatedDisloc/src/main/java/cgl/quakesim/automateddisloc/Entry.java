package cgl.quakesim.automateddisloc;

public class Entry {
		
	String id = null;
	String title = null;
	String updated = null;
	String link1 = null;
	String link2 = null;
	String summary = null;
	String georss_point = null;
	String georss_elev = null;
	String category = null;
	
	Double M = null;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getLink1() {
		return link1;
	}
	public void setLink1(String link1) {
		this.link1 = link1;
	}
	public String getLink2() {
		return link2;
	}
	public void setLink2(String link2) {
		this.link2 = link2;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getGeorss_point() {
		return georss_point;
	}
	public void setGeorss_point(String georssPoint) {
		georss_point = georssPoint;
	}
	public String getGeorss_elev() {
		return georss_elev;
	}
	public void setGeorss_elev(String georssElev) {
		georss_elev = georssElev;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Double getM() {
		M = Double.parseDouble((getTitle().split(","))[0].substring(1));		
		return M;
	}
	
	
	public boolean isMover7() {
		
		if (getM() >= 7.0)
			return true;			
		
		return false;
	}
}
