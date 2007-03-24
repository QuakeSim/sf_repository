package org.apache.myfaces.blank;

public class GMTViewForm {
	String area_prop= "";
	String scale_prop="";
	String mapticks_prop="";
	String vectormag_prop="";
	String plot_background="";
	boolean bplot_background=false;
	String plot_causative="";
	boolean bplot_causative=false;
	String plot_obs="";
	boolean bplot_obs=false;
	String plot_calc="";
	boolean bplot_calc=false;
	String plot_resid="";
	boolean bplot_resid=false;

	public boolean getBplot_resid() {
		if (this.plot_resid.equals("")) {
			this.bplot_resid=false;
		}else {
			this.bplot_resid=true;
		}
		return this.bplot_resid;
	}
	public void setBplot_resid(boolean tmp) {
		if (tmp) {
			plot_resid="1";
		}else {
			plot_resid="";
		}
		this.bplot_resid=tmp;
	}
	
	public boolean getBplot_calc() {
		if (this.plot_calc.equals("")) {
			this.bplot_calc=false;
		}else {
			this.bplot_calc=true;
		}
		return this.bplot_calc;
	}
	public void setBplot_calc(boolean tmp) {
		if (tmp) {
			plot_calc="1";
		}else {
			plot_calc="";
		}
		this.bplot_calc=tmp;
	}
	
	public boolean getBplot_obs() {
		if (this.plot_obs.equals("")) {
			this.bplot_obs=false;
		}else {
			this.bplot_obs=true;
		}
		return this.bplot_obs;
	}
	public void setBplot_obs(boolean tmp) {
		if (tmp) {
			plot_obs="1";
		}else {
			plot_obs="";
		}
		this.bplot_obs=tmp;
	}
	
	public boolean getBplot_causative() {
		if (this.plot_causative.equals("")) {
			this.bplot_causative=false;
		}else {
			this.bplot_causative=true;
		}
		return this.bplot_causative;
	}
	public void setBplot_causative(boolean tmp) {
		if (tmp) {
			plot_causative="1";
		}else {
			plot_causative="";
		}
		this.bplot_causative=tmp;
	}
	
	public boolean getBplot_background() {
		if (this.plot_background.equals("")) {
			this.bplot_background=false;
		}else {
			this.bplot_background=true;
		}
		return this.bplot_background;
	}
	public void setBplot_background(boolean tmp) {
		if (tmp) {
			plot_background="1";
		}else {
			plot_background="";
		}
		this.bplot_background=tmp;
	}
	
	public String getPlot_resid() {
		return this.plot_resid;
	}
	public void setPlot_resid(String tmp) {
		this.plot_resid=tmp;
	}
	
	public String getPlot_calc() {
		return this.plot_calc;
	}
	public void setPlot_calc(String tmp) {
		this.plot_calc=tmp;
	}
	
	public String getPlot_obs() {
		return this.plot_obs;
	}
	public void setPlot_obs(String tmp) {
		this.plot_obs=tmp;
	}
	
	public String getPlot_causative() {
		return this.plot_causative;
	}
	public void setPlot_causative(String tmp) {
		this.plot_causative=tmp;
	}
	
	public String getPlot_background() {
		return this.plot_background;
	}
	public void setPlot_background(String tmp) {
		this.plot_background=tmp;
	}
	
	public String getVectormag_prop() {
		return this.vectormag_prop;
	}
	public void setVectormag_prop(String tmp) {
		this.vectormag_prop=tmp;
	}
	
	public String getMapticks_prop() {
		return this.mapticks_prop;
	}
	public void setMapticks_prop(String tmp) {
		this.mapticks_prop=tmp;
	}
	
	public String getScale_prop() {
		return this.scale_prop;
	}
	public void setScale_prop(String tmp) {
		this.scale_prop=tmp;
	}
	
	public String getArea_prop() {
		return this.area_prop;
	}
	public void setArea_prop(String tmp) {
		this.area_prop=tmp;
	}
	
	
	

}
