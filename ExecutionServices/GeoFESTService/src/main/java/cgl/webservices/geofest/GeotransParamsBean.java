package cgl.webservices.geofest;

/**
 * This bean contains all the necessary data for running
 * GeoFEST.
 */
public class GeotransParamsBean {
	 
	 //input params
	 String number_space_dimensions="3";
	 String number_degrees_freedom="3";
	 String nrates ="0";
	 String shape_flag = "1";
	 String solver_flag = "2";
	 String number_time_groups = "1";
	 String reform_steps ="1";
	 String backup_steps="5000";
	 String fault_interval="5000.0";
	 String end_time="50.0";
	 String alpha ="1.0";
	 String time_step="0.5";
	
	 //cordinate boundary
	 
	 String top_bc="locked node";
	 String top_bc_value="0 0. 0. 0. 1. ";
	 String east_bc="locked node";
	 String east_bc_value="0 0. 0. 0. 1. ";
	 String west_bc="locked node";
	 String west_bc_value="0 0. 0. 0. 1. ";
	 String north_bc="locked node";
	 String north_bc_value="0 0. 0. 0. 1. ";
	 String south_bc="locked node";
	 String south_bc_value="0 0. 0. 0. 1. ";
	 String bottom_bc="locked node";
	 String bottom_bc_value="0 0. 0. 0. 1. ";
	
	//Output Parameters and Formatting
	 
	 String reportingNodes="All";
	 String reportingElements="All";
	 String printTimesType="steps";
	 String numberofPrintTimes="5";
	 String printTimesInterval="1.0";
	 String restartFile="NO_RESTART";
	 String checkpointFile="NO_SAVE";
	
	 String inputFileName="";
	 String outputFileName="";
	 String logFileName="";
	 String run_choice="";
	 
	 //These are new parameters
	 String elastic1="1";
	 String elasOut1="1";
	 String refine="0";
	 String refineOut="0";
	 String elastic2="0";
	 String elasOut2="0";
	 String visco="1";
	 String velblock=" ";
	 //Ramp BC
	 String[] ramp_bc;

	
	public void reset(String projectName) {
		number_space_dimensions="3";
		number_degrees_freedom="3";
		nrates ="0";
		shape_flag = "1";
		solver_flag = "2";
		number_time_groups = "1";
		reform_steps ="1";
		backup_steps="5000";
		fault_interval="5000.0";
		end_time="500.0";
		alpha ="1.0";
		time_step="0.5";
		//cordinate boundary
		
		top_bc="Free Node";
		top_bc_value="0 0. 0. 0. 1.";
		east_bc="Locked Node";
		east_bc_value="0 0. 0. 0. 1.";
		west_bc="Locked Node";
		west_bc_value="0 0. 0. 0. 1.";
		north_bc="Locked Node";
		north_bc_value="0 0. 0. 0. 1.";
		south_bc="Locked Node";
		south_bc_value="0 0. 0. 0. 1.";
		bottom_bc="Locked Node";
		bottom_bc_value="0 0. 0. 0. 1.";
		
		//Output Parameters and Formatting
		reportingNodes="All";
		reportingElements="All";
		printTimesType="steps";
		numberofPrintTimes="20";
		printTimesInterval="1.0";
		restartFile="NO_RESTART";
		checkpointFile="NO_SAVE";	
		
		inputFileName=projectName+".inp";
		outputFileName=projectName+".out";
		logFileName=projectName+".log";
		
	}

	public void setRun_choice(String tmp_str) {
		this.run_choice = tmp_str;
	}

	public String getRun_choice() {
		return run_choice;
	}
	
	public void setLogFileName(String tmp_str) {
		this.logFileName = tmp_str;
	}

	public String getLogFileName() {
		return logFileName;
	}
	
	public void setOutputFileName(String tmp_str) {
		this.outputFileName = tmp_str;
	}

	public String getOutputFileName() {
		return outputFileName;
	}
	
	public void setInputFileName(String tmp_str) {
		this.inputFileName = tmp_str;
	}

	public String getInputFileName() {
		return inputFileName;
	}
	
	public void setCheckpointFile(String tmp_str) {
		this.checkpointFile = tmp_str;
	}

	public String getCheckpointFile() {
		return checkpointFile;
	}	

	public void setRestartFile(String tmp_str) {
		this.restartFile = tmp_str;
	}

	public String getRestartFile() {
		return restartFile;
	}	

	public void setPrintTimesInterval(String tmp_str) {
		this.printTimesInterval = tmp_str;
	}

	public String getPrintTimesInterval() {
		return printTimesInterval;
	}	

	public void setNumberofPrintTimes(String tmp_str) {
		this.numberofPrintTimes = tmp_str;
	}

	public String getNumberofPrintTimes() {
		return numberofPrintTimes;
	}	

	public void setPrintTimesType(String tmp_str) {
		this.printTimesType = tmp_str;
	}

	public String getPrintTimesType() {
		return printTimesType;
	}	

	public void setReportingElements(String tmp_str) {
		this.reportingElements = tmp_str;
	}

	public String getReportingElements() {
		return reportingElements;
	}	

	public void setReportingNodes(String tmp_str) {
		this.reportingNodes = tmp_str;
	}

	public String getReportingNodes() {
		return reportingNodes;
	}	

	public void setBottom_bc_value(String tmp_str) {
		this.bottom_bc_value = tmp_str;
	}

	public String getBottom_bc_value() {
		return bottom_bc_value;
	}	

	public void setBottom_bc(String tmp_str) {
		this.bottom_bc = tmp_str;
	}

	public String getBottom_bc() {
		return bottom_bc;
	}	

	public void setSouth_bc_value(String tmp_str) {
		this.south_bc_value = tmp_str;
	}

	public String getSouth_bc_value() {
		return south_bc_value;
	}	
	
	public void setSouth_bc(String tmp_str) {
		this.south_bc = tmp_str;
	}

	public String getSouth_bc() {
		return south_bc;
	}	
	
	public void setNorth_bc_value(String tmp_str) {
		this.north_bc_value = tmp_str;
	}

	public String getNorth_bc_value() {
		return north_bc_value;
	}	

	public void setNorth_bc(String tmp_str) {
		this.north_bc = tmp_str;
	}

	public String getNorth_bc() {
		return north_bc;
	}	
	
	public void setWest_bc_value(String tmp_str) {
		this.west_bc_value = tmp_str;
	}

	public String getWest_bc_value() {
		return west_bc_value;
	}	
	
	public void setWest_bc(String tmp_str) {
		this.west_bc = tmp_str;
	}

	public String getWest_bc() {
		return west_bc;
	}	
	
	public void setEast_bc_value(String tmp_str) {
		this.east_bc_value = tmp_str;
	}

	public String getEast_bc_value() {
		return east_bc_value;
	}	
	
	public void setEast_bc(String tmp_str) {
		this.east_bc = tmp_str;
	}

	public String getEast_bc() {
		return east_bc;
	}	
	
	public void setTop_bc_value(String tmp_str) {
		this.top_bc_value = tmp_str;
	}

	public String getTop_bc_value() {
		return top_bc_value;
	}	
	
	public void setTop_bc(String tmp_str) {
		this.top_bc = tmp_str;
	}

	public String getTop_bc() {
		return top_bc;
	}	
	
	public void setTime_step(String tmp_str) {
		this.time_step = tmp_str;
	}

	public String getTime_step() {
		return time_step;
	}	
	
	public void setAlpha(String tmp_str) {
		this.alpha = tmp_str;
	}

	public String getAlpha() {
		return alpha;
	}	
	
	public void setEnd_time(String tmp_str) {
		this.end_time = tmp_str;
	}

	public String getEnd_time() {
		return end_time;
	}	
	
	public void setFault_interval(String tmp_str) {
		this.fault_interval = tmp_str;
	}

	public String getFault_interval() {
		return fault_interval;
	}	
	
	public void setBackup_steps(String tmp_str) {
		this.backup_steps = tmp_str;
	}

	public String getBackup_steps() {
		return backup_steps;
	}	

	public void setReform_steps(String tmp_str) {
		this.reform_steps = tmp_str;
	}

	public String getReform_steps() {
		return reform_steps;
	}	
	
	public void setNumber_time_groups(String tmp_str) {
		this.number_time_groups = tmp_str;
	}

	public String getNumber_time_groups() {
		return number_time_groups;
	}	
	
	public void setSolver_flag(String tmp_str) {
		this.solver_flag = tmp_str;
	}

	public String getSolver_flag() {
		return solver_flag;
	}	

	
	public void setShape_flag(String tmp_str) {
		this.shape_flag = tmp_str;
	}

	public String getShape_flag() {
		return shape_flag;
	}	
	
	public void setNrates(String tmp_str) {
		this.nrates = tmp_str;
	}

	public String getNrates() {
		return nrates;
	}	
	
	public void setNumber_degrees_freedom(String tmp_str) {
		this.number_degrees_freedom = tmp_str;
	}

	public String getNumber_degrees_freedom() {
		return number_degrees_freedom;
	}	
	
	public void setNumber_space_dimensions(String tmp_str) {
		this.number_space_dimensions = tmp_str;
	}

	public String getNumber_space_dimensions() {
		return number_space_dimensions;
	}		

	 public String getElastic1() {
		  return elastic1;
	 }

	 public void setElastic1(String elastic1) {
		  this.elastic1=elastic1;
	 }

	 public String getElastic2() {
		  return notNull(elastic2);
	 }

	 public void setElastic2(String elastic1) {
		  this.elastic2=elastic2;
	 }

	 public String getRefine() {
		  return notNull(refine);
	 }
	 
	 public void setRefine(String refine) {
		  this.refine=refine;
	 }

	 public String getRefineOut() {
		  return notNull(refineOut);
	 }
	 
	 public void setRefineOut(String refineOut) {
		  this.refineOut=refineOut;
	 }

	 public String getElasOut1() {
		  return notNull(elasOut1);
	 }

	 public void setElasOut1(String elasOut1) {
		  this.elasOut1=elasOut1;
	 }

	 public String getElasOut2() {
		  return notNull(elasOut2);
	 }

	 public void setElasOut2(String elasOut2) {
		  this.elasOut2=elasOut2;
	 }

	 public void setVisco(String visco){
		  this.visco=visco;
	 }
	 
	 public String getVisco() {
		  return notNull(visco);
	 }

	 public String[] getRampBC() {
		  return ramp_bc;  
	 }

	 public void setRampBC(String[] ramp_bc) {
		  java.lang.System.arraycopy(ramp_bc,0,this.ramp_bc,0,ramp_bc.length);
	 }
	 
	 public String getVelblock() {
		  return notNull(velblock);
	 }
	 
	 public void setVelblock(String velblock) {
		  this.velblock=velblock;
	 }

	 private String notNull(String retString) {
		  if(retString==null) {
				return "";
		  }
		  else {
				return retString;
		  }
				
	 }
}
