/**
 * GeotransParamsBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 *
 * Additional sweetener manually/manfully added.
 */

package cgl.quakesim.geofest;

public class GeotransParallelParamsBean  implements java.io.Serializable {
    private java.lang.String alpha="1.0";

    private java.lang.String backup_steps="5000";

    private java.lang.String bottom_bc="Locked Node";

    private java.lang.String bottom_bc_value="0 0. 0. 0. 1.";

    private java.lang.String checkpointFile="NO_SAVE";	;

    private java.lang.String east_bc="Locked Node";

    private java.lang.String east_bc_value="0 0. 0. 0. 1.";

    private java.lang.String end_time="50.0";

    private java.lang.String fault_interval="5000.0";

    private java.lang.String inputFileName;

    private java.lang.String logFileName;

    private java.lang.String north_bc="Locked Node";

    private java.lang.String north_bc_value="0 0. 0. 0. 1.";

    private java.lang.String nrates="0";

    private java.lang.String number_degrees_freedom="3";

    private java.lang.String number_space_dimensions="3";

    private java.lang.String number_time_groups="1";

    private java.lang.String numberofPrintTimes="20";

    private java.lang.String outputFileName;

    private java.lang.String printTimesInterval="1.0";

    private java.lang.String printTimesType="steps";

    private java.lang.String reform_steps="1";

    private java.lang.String reportingElements="All";

    private java.lang.String reportingNodes="All";

    private java.lang.String restartFile="NO_RESTART";

    private java.lang.String run_choice;

    private java.lang.String shape_flag="1";

    private java.lang.String solver_flag="2";

    private java.lang.String south_bc="Locked Node";

    private java.lang.String south_bc_value="0 0. 0. 0. 1.";

    private java.lang.String time_step="0.5";

    private java.lang.String top_bc="Free Node";

    private java.lang.String top_bc_value="0 0. 0. 0. 1.";

    private java.lang.String west_bc="Locked Node";

    private java.lang.String west_bc_value="0 0. 0. 0. 1.";


	 /**
	  * Provide with default values.
	  */ 
    public GeotransParallelParamsBean() {
		this.number_space_dimensions="3";
		this.number_degrees_freedom="3";
		this.nrates ="0";
		this.shape_flag = "1";
		this.solver_flag = "2";
		this.number_time_groups = "1";
		this.reform_steps ="1";
		this.backup_steps="5000";
		this.fault_interval="5000.0";
		this.end_time="50.0";
		this.alpha ="1.0";
		this.time_step="0.5";
		//cordinate boundary
		
		this.top_bc="Free Node";
		this.top_bc_value="0 0. 0. 0. 1.";
		this.east_bc="Locked Node";
		this.east_bc_value="0 0. 0. 0. 1.";
		this.west_bc="Locked Node";
		this.west_bc_value="0 0. 0. 0. 1.";
		this.north_bc="Locked Node";
		this.north_bc_value="0 0. 0. 0. 1.";
		this.south_bc="Locked Node";
		this.south_bc_value="0 0. 0. 0. 1.";
		this.bottom_bc="Locked Node";
		this.bottom_bc_value="0 0. 0. 0. 1.";
		
		//Output Parameters and Formatting
		
		this.reportingNodes="All";
		this.reportingElements="All";
		this.printTimesType="steps";
		this.numberofPrintTimes="20";
		this.printTimesInterval="1.0";
		this.restartFile="NO_RESTART";
		this.checkpointFile="NO_SAVE";	

    }

    public GeotransParallelParamsBean(
           java.lang.String alpha,
           java.lang.String backup_steps,
           java.lang.String bottom_bc,
           java.lang.String bottom_bc_value,
           java.lang.String checkpointFile,
           java.lang.String east_bc,
           java.lang.String east_bc_value,
           java.lang.String end_time,
           java.lang.String fault_interval,
           java.lang.String inputFileName,
           java.lang.String logFileName,
           java.lang.String north_bc,
           java.lang.String north_bc_value,
           java.lang.String nrates,
           java.lang.String number_degrees_freedom,
           java.lang.String number_space_dimensions,
           java.lang.String number_time_groups,
           java.lang.String numberofPrintTimes,
           java.lang.String outputFileName,
           java.lang.String printTimesInterval,
           java.lang.String printTimesType,
           java.lang.String reform_steps,
           java.lang.String reportingElements,
           java.lang.String reportingNodes,
           java.lang.String restartFile,
           java.lang.String run_choice,
           java.lang.String shape_flag,
           java.lang.String solver_flag,
           java.lang.String south_bc,
           java.lang.String south_bc_value,
           java.lang.String time_step,
           java.lang.String top_bc,
           java.lang.String top_bc_value,
           java.lang.String west_bc,
           java.lang.String west_bc_value) {

           this.alpha = alpha;
           this.backup_steps = backup_steps;
           this.bottom_bc = bottom_bc;
           this.bottom_bc_value = bottom_bc_value;
           this.checkpointFile = checkpointFile;
           this.east_bc = east_bc;
           this.east_bc_value = east_bc_value;
           this.end_time = end_time;
           this.fault_interval = fault_interval;
           this.inputFileName = inputFileName;
           this.logFileName = logFileName;
           this.north_bc = north_bc;
           this.north_bc_value = north_bc_value;
           this.nrates = nrates;
           this.number_degrees_freedom = number_degrees_freedom;
           this.number_space_dimensions = number_space_dimensions;
           this.number_time_groups = number_time_groups;
           this.numberofPrintTimes = numberofPrintTimes;
           this.outputFileName = outputFileName;
           this.printTimesInterval = printTimesInterval;
           this.printTimesType = printTimesType;
           this.reform_steps = reform_steps;
           this.reportingElements = reportingElements;
           this.reportingNodes = reportingNodes;
           this.restartFile = restartFile;
           this.run_choice = run_choice;
           this.shape_flag = shape_flag;
           this.solver_flag = solver_flag;
           this.south_bc = south_bc;
           this.south_bc_value = south_bc_value;
           this.time_step = time_step;
           this.top_bc = top_bc;
           this.top_bc_value = top_bc_value;
           this.west_bc = west_bc;
           this.west_bc_value = west_bc_value;
    }


    /**
     * Gets the alpha value for this GeotransParamsBean.
     * 
     * @return alpha
     */
    public java.lang.String getAlpha() {
        return alpha;
    }


    /**
     * Sets the alpha value for this GeotransParamsBean.
     * 
     * @param alpha
     */
    public void setAlpha(java.lang.String alpha) {
		  System.out.println("This is alpha setter:"+alpha);
        this.alpha = alpha;
    }


    /**
     * Gets the backup_steps value for this GeotransParamsBean.
     * 
     * @return backup_steps
     */
    public java.lang.String getBackup_steps() {
        return backup_steps;
    }


    /**
     * Sets the backup_steps value for this GeotransParamsBean.
     * 
     * @param backup_steps
     */
    public void setBackup_steps(java.lang.String backup_steps) {
        this.backup_steps = backup_steps;
    }


    /**
     * Gets the bottom_bc value for this GeotransParamsBean.
     * 
     * @return bottom_bc
     */
    public java.lang.String getBottom_bc() {
        return bottom_bc;
    }


    /**
     * Sets the bottom_bc value for this GeotransParamsBean.
     * 
     * @param bottom_bc
     */
    public void setBottom_bc(java.lang.String bottom_bc) {
        this.bottom_bc = bottom_bc;
    }


    /**
     * Gets the bottom_bc_value value for this GeotransParamsBean.
     * 
     * @return bottom_bc_value
     */
    public java.lang.String getBottom_bc_value() {
        return bottom_bc_value;
    }


    /**
     * Sets the bottom_bc_value value for this GeotransParamsBean.
     * 
     * @param bottom_bc_value
     */
    public void setBottom_bc_value(java.lang.String bottom_bc_value) {
        this.bottom_bc_value = bottom_bc_value;
    }


    /**
     * Gets the checkpointFile value for this GeotransParamsBean.
     * 
     * @return checkpointFile
     */
    public java.lang.String getCheckpointFile() {
        return checkpointFile;
    }


    /**
     * Sets the checkpointFile value for this GeotransParamsBean.
     * 
     * @param checkpointFile
     */
    public void setCheckpointFile(java.lang.String checkpointFile) {
        this.checkpointFile = checkpointFile;
    }


    /**
     * Gets the east_bc value for this GeotransParamsBean.
     * 
     * @return east_bc
     */
    public java.lang.String getEast_bc() {
        return east_bc;
    }


    /**
     * Sets the east_bc value for this GeotransParamsBean.
     * 
     * @param east_bc
     */
    public void setEast_bc(java.lang.String east_bc) {
        this.east_bc = east_bc;
    }


    /**
     * Gets the east_bc_value value for this GeotransParamsBean.
     * 
     * @return east_bc_value
     */
    public java.lang.String getEast_bc_value() {
        return east_bc_value;
    }


    /**
     * Sets the east_bc_value value for this GeotransParamsBean.
     * 
     * @param east_bc_value
     */
    public void setEast_bc_value(java.lang.String east_bc_value) {
        this.east_bc_value = east_bc_value;
    }


    /**
     * Gets the end_time value for this GeotransParamsBean.
     * 
     * @return end_time
     */
    public java.lang.String getEnd_time() {
        return end_time;
    }


    /**
     * Sets the end_time value for this GeotransParamsBean.
     * 
     * @param end_time
     */
    public void setEnd_time(java.lang.String end_time) {
        this.end_time = end_time;
    }


    /**
     * Gets the fault_interval value for this GeotransParamsBean.
     * 
     * @return fault_interval
     */
    public java.lang.String getFault_interval() {
        return fault_interval;
    }


    /**
     * Sets the fault_interval value for this GeotransParamsBean.
     * 
     * @param fault_interval
     */
    public void setFault_interval(java.lang.String fault_interval) {
        this.fault_interval = fault_interval;
    }


    /**
     * Gets the inputFileName value for this GeotransParamsBean.
     * 
     * @return inputFileName
     */
    public java.lang.String getInputFileName() {
        return inputFileName;
    }


    /**
     * Sets the inputFileName value for this GeotransParamsBean.
     * 
     * @param inputFileName
     */
    public void setInputFileName(java.lang.String inputFileName) {
        this.inputFileName = inputFileName;
    }


    /**
     * Gets the logFileName value for this GeotransParamsBean.
     * 
     * @return logFileName
     */
    public java.lang.String getLogFileName() {
        return logFileName;
    }


    /**
     * Sets the logFileName value for this GeotransParamsBean.
     * 
     * @param logFileName
     */
    public void setLogFileName(java.lang.String logFileName) {
        this.logFileName = logFileName;
    }


    /**
     * Gets the north_bc value for this GeotransParamsBean.
     * 
     * @return north_bc
     */
    public java.lang.String getNorth_bc() {
        return north_bc;
    }


    /**
     * Sets the north_bc value for this GeotransParamsBean.
     * 
     * @param north_bc
     */
    public void setNorth_bc(java.lang.String north_bc) {
        this.north_bc = north_bc;
    }


    /**
     * Gets the north_bc_value value for this GeotransParamsBean.
     * 
     * @return north_bc_value
     */
    public java.lang.String getNorth_bc_value() {
        return north_bc_value;
    }


    /**
     * Sets the north_bc_value value for this GeotransParamsBean.
     * 
     * @param north_bc_value
     */
    public void setNorth_bc_value(java.lang.String north_bc_value) {
        this.north_bc_value = north_bc_value;
    }


    /**
     * Gets the nrates value for this GeotransParamsBean.
     * 
     * @return nrates
     */
    public java.lang.String getNrates() {
        return nrates;
    }


    /**
     * Sets the nrates value for this GeotransParamsBean.
     * 
     * @param nrates
     */
    public void setNrates(java.lang.String nrates) {
        this.nrates = nrates;
    }


    /**
     * Gets the number_degrees_freedom value for this GeotransParamsBean.
     * 
     * @return number_degrees_freedom
     */
    public java.lang.String getNumber_degrees_freedom() {
        return number_degrees_freedom;
    }


    /**
     * Sets the number_degrees_freedom value for this GeotransParamsBean.
     * 
     * @param number_degrees_freedom
     */
    public void setNumber_degrees_freedom(java.lang.String number_degrees_freedom) {
        this.number_degrees_freedom = number_degrees_freedom;
    }


    /**
     * Gets the number_space_dimensions value for this GeotransParamsBean.
     * 
     * @return number_space_dimensions
     */
    public java.lang.String getNumber_space_dimensions() {
        return number_space_dimensions;
    }


    /**
     * Sets the number_space_dimensions value for this GeotransParamsBean.
     * 
     * @param number_space_dimensions
     */
    public void setNumber_space_dimensions(java.lang.String number_space_dimensions) {
        this.number_space_dimensions = number_space_dimensions;
    }


    /**
     * Gets the number_time_groups value for this GeotransParamsBean.
     * 
     * @return number_time_groups
     */
    public java.lang.String getNumber_time_groups() {
        return number_time_groups;
    }


    /**
     * Sets the number_time_groups value for this GeotransParamsBean.
     * 
     * @param number_time_groups
     */
    public void setNumber_time_groups(java.lang.String number_time_groups) {
        this.number_time_groups = number_time_groups;
    }


    /**
     * Gets the numberofPrintTimes value for this GeotransParamsBean.
     * 
     * @return numberofPrintTimes
     */
    public java.lang.String getNumberofPrintTimes() {
        return numberofPrintTimes;
    }


    /**
     * Sets the numberofPrintTimes value for this GeotransParamsBean.
     * 
     * @param numberofPrintTimes
     */
    public void setNumberofPrintTimes(java.lang.String numberofPrintTimes) {
        this.numberofPrintTimes = numberofPrintTimes;
    }


    /**
     * Gets the outputFileName value for this GeotransParamsBean.
     * 
     * @return outputFileName
     */
    public java.lang.String getOutputFileName() {
        return outputFileName;
    }


    /**
     * Sets the outputFileName value for this GeotransParamsBean.
     * 
     * @param outputFileName
     */
    public void setOutputFileName(java.lang.String outputFileName) {
        this.outputFileName = outputFileName;
    }


    /**
     * Gets the printTimesInterval value for this GeotransParamsBean.
     * 
     * @return printTimesInterval
     */
    public java.lang.String getPrintTimesInterval() {
        return printTimesInterval;
    }


    /**
     * Sets the printTimesInterval value for this GeotransParamsBean.
     * 
     * @param printTimesInterval
     */
    public void setPrintTimesInterval(java.lang.String printTimesInterval) {
        this.printTimesInterval = printTimesInterval;
    }


    /**
     * Gets the printTimesType value for this GeotransParamsBean.
     * 
     * @return printTimesType
     */
    public java.lang.String getPrintTimesType() {
        return printTimesType;
    }


    /**
     * Sets the printTimesType value for this GeotransParamsBean.
     * 
     * @param printTimesType
     */
    public void setPrintTimesType(java.lang.String printTimesType) {
        this.printTimesType = printTimesType;
    }


    /**
     * Gets the reform_steps value for this GeotransParamsBean.
     * 
     * @return reform_steps
     */
    public java.lang.String getReform_steps() {
        return reform_steps;
    }


    /**
     * Sets the reform_steps value for this GeotransParamsBean.
     * 
     * @param reform_steps
     */
    public void setReform_steps(java.lang.String reform_steps) {
        this.reform_steps = reform_steps;
    }


    /**
     * Gets the reportingElements value for this GeotransParamsBean.
     * 
     * @return reportingElements
     */
    public java.lang.String getReportingElements() {
        return reportingElements;
    }


    /**
     * Sets the reportingElements value for this GeotransParamsBean.
     * 
     * @param reportingElements
     */
    public void setReportingElements(java.lang.String reportingElements) {
        this.reportingElements = reportingElements;
    }


    /**
     * Gets the reportingNodes value for this GeotransParamsBean.
     * 
     * @return reportingNodes
     */
    public java.lang.String getReportingNodes() {
        return reportingNodes;
    }


    /**
     * Sets the reportingNodes value for this GeotransParamsBean.
     * 
     * @param reportingNodes
     */
    public void setReportingNodes(java.lang.String reportingNodes) {
        this.reportingNodes = reportingNodes;
    }


    /**
     * Gets the restartFile value for this GeotransParamsBean.
     * 
     * @return restartFile
     */
    public java.lang.String getRestartFile() {
        return restartFile;
    }


    /**
     * Sets the restartFile value for this GeotransParamsBean.
     * 
     * @param restartFile
     */
    public void setRestartFile(java.lang.String restartFile) {
        this.restartFile = restartFile;
    }


    /**
     * Gets the run_choice value for this GeotransParamsBean.
     * 
     * @return run_choice
     */
    public java.lang.String getRun_choice() {
        return run_choice;
    }


    /**
     * Sets the run_choice value for this GeotransParamsBean.
     * 
     * @param run_choice
     */
    public void setRun_choice(java.lang.String run_choice) {
        this.run_choice = run_choice;
    }


    /**
     * Gets the shape_flag value for this GeotransParamsBean.
     * 
     * @return shape_flag
     */
    public java.lang.String getShape_flag() {
        return shape_flag;
    }


    /**
     * Sets the shape_flag value for this GeotransParamsBean.
     * 
     * @param shape_flag
     */
    public void setShape_flag(java.lang.String shape_flag) {
        this.shape_flag = shape_flag;
    }


    /**
     * Gets the solver_flag value for this GeotransParamsBean.
     * 
     * @return solver_flag
     */
    public java.lang.String getSolver_flag() {
        return solver_flag;
    }


    /**
     * Sets the solver_flag value for this GeotransParamsBean.
     * 
     * @param solver_flag
     */
    public void setSolver_flag(java.lang.String solver_flag) {
        this.solver_flag = solver_flag;
    }


    /**
     * Gets the south_bc value for this GeotransParamsBean.
     * 
     * @return south_bc
     */
    public java.lang.String getSouth_bc() {
        return south_bc;
    }


    /**
     * Sets the south_bc value for this GeotransParamsBean.
     * 
     * @param south_bc
     */
    public void setSouth_bc(java.lang.String south_bc) {
        this.south_bc = south_bc;
    }


    /**
     * Gets the south_bc_value value for this GeotransParamsBean.
     * 
     * @return south_bc_value
     */
    public java.lang.String getSouth_bc_value() {
        return south_bc_value;
    }


    /**
     * Sets the south_bc_value value for this GeotransParamsBean.
     * 
     * @param south_bc_value
     */
    public void setSouth_bc_value(java.lang.String south_bc_value) {
        this.south_bc_value = south_bc_value;
    }


    /**
     * Gets the time_step value for this GeotransParamsBean.
     * 
     * @return time_step
     */
    public java.lang.String getTime_step() {
        return time_step;
    }


    /**
     * Sets the time_step value for this GeotransParamsBean.
     * 
     * @param time_step
     */
    public void setTime_step(java.lang.String time_step) {
        this.time_step = time_step;
    }


    /**
     * Gets the top_bc value for this GeotransParamsBean.
     * 
     * @return top_bc
     */
    public java.lang.String getTop_bc() {
        return top_bc;
    }


    /**
     * Sets the top_bc value for this GeotransParamsBean.
     * 
     * @param top_bc
     */
    public void setTop_bc(java.lang.String top_bc) {
        this.top_bc = top_bc;
    }


    /**
     * Gets the top_bc_value value for this GeotransParamsBean.
     * 
     * @return top_bc_value
     */
    public java.lang.String getTop_bc_value() {
        return top_bc_value;
    }


    /**
     * Sets the top_bc_value value for this GeotransParamsBean.
     * 
     * @param top_bc_value
     */
    public void setTop_bc_value(java.lang.String top_bc_value) {
        this.top_bc_value = top_bc_value;
    }


    /**
     * Gets the west_bc value for this GeotransParamsBean.
     * 
     * @return west_bc
     */
    public java.lang.String getWest_bc() {
        return west_bc;
    }


    /**
     * Sets the west_bc value for this GeotransParamsBean.
     * 
     * @param west_bc
     */
    public void setWest_bc(java.lang.String west_bc) {
        this.west_bc = west_bc;
    }


    /**
     * Gets the west_bc_value value for this GeotransParamsBean.
     * 
     * @return west_bc_value
     */
    public java.lang.String getWest_bc_value() {
        return west_bc_value;
    }


    /**
     * Sets the west_bc_value value for this GeotransParamsBean.
     * 
     * @param west_bc_value
     */
    public void setWest_bc_value(java.lang.String west_bc_value) {
        this.west_bc_value = west_bc_value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GeotransParamsBean)) return false;
        GeotransParamsBean other = (GeotransParamsBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.alpha==null && other.getAlpha()==null) || 
             (this.alpha!=null &&
              this.alpha.equals(other.getAlpha()))) &&
            ((this.backup_steps==null && other.getBackup_steps()==null) || 
             (this.backup_steps!=null &&
              this.backup_steps.equals(other.getBackup_steps()))) &&
            ((this.bottom_bc==null && other.getBottom_bc()==null) || 
             (this.bottom_bc!=null &&
              this.bottom_bc.equals(other.getBottom_bc()))) &&
            ((this.bottom_bc_value==null && other.getBottom_bc_value()==null) || 
             (this.bottom_bc_value!=null &&
              this.bottom_bc_value.equals(other.getBottom_bc_value()))) &&
            ((this.checkpointFile==null && other.getCheckpointFile()==null) || 
             (this.checkpointFile!=null &&
              this.checkpointFile.equals(other.getCheckpointFile()))) &&
            ((this.east_bc==null && other.getEast_bc()==null) || 
             (this.east_bc!=null &&
              this.east_bc.equals(other.getEast_bc()))) &&
            ((this.east_bc_value==null && other.getEast_bc_value()==null) || 
             (this.east_bc_value!=null &&
              this.east_bc_value.equals(other.getEast_bc_value()))) &&
            ((this.end_time==null && other.getEnd_time()==null) || 
             (this.end_time!=null &&
              this.end_time.equals(other.getEnd_time()))) &&
            ((this.fault_interval==null && other.getFault_interval()==null) || 
             (this.fault_interval!=null &&
              this.fault_interval.equals(other.getFault_interval()))) &&
            ((this.inputFileName==null && other.getInputFileName()==null) || 
             (this.inputFileName!=null &&
              this.inputFileName.equals(other.getInputFileName()))) &&
            ((this.logFileName==null && other.getLogFileName()==null) || 
             (this.logFileName!=null &&
              this.logFileName.equals(other.getLogFileName()))) &&
            ((this.north_bc==null && other.getNorth_bc()==null) || 
             (this.north_bc!=null &&
              this.north_bc.equals(other.getNorth_bc()))) &&
            ((this.north_bc_value==null && other.getNorth_bc_value()==null) || 
             (this.north_bc_value!=null &&
              this.north_bc_value.equals(other.getNorth_bc_value()))) &&
            ((this.nrates==null && other.getNrates()==null) || 
             (this.nrates!=null &&
              this.nrates.equals(other.getNrates()))) &&
            ((this.number_degrees_freedom==null && other.getNumber_degrees_freedom()==null) || 
             (this.number_degrees_freedom!=null &&
              this.number_degrees_freedom.equals(other.getNumber_degrees_freedom()))) &&
            ((this.number_space_dimensions==null && other.getNumber_space_dimensions()==null) || 
             (this.number_space_dimensions!=null &&
              this.number_space_dimensions.equals(other.getNumber_space_dimensions()))) &&
            ((this.number_time_groups==null && other.getNumber_time_groups()==null) || 
             (this.number_time_groups!=null &&
              this.number_time_groups.equals(other.getNumber_time_groups()))) &&
            ((this.numberofPrintTimes==null && other.getNumberofPrintTimes()==null) || 
             (this.numberofPrintTimes!=null &&
              this.numberofPrintTimes.equals(other.getNumberofPrintTimes()))) &&
            ((this.outputFileName==null && other.getOutputFileName()==null) || 
             (this.outputFileName!=null &&
              this.outputFileName.equals(other.getOutputFileName()))) &&
            ((this.printTimesInterval==null && other.getPrintTimesInterval()==null) || 
             (this.printTimesInterval!=null &&
              this.printTimesInterval.equals(other.getPrintTimesInterval()))) &&
            ((this.printTimesType==null && other.getPrintTimesType()==null) || 
             (this.printTimesType!=null &&
              this.printTimesType.equals(other.getPrintTimesType()))) &&
            ((this.reform_steps==null && other.getReform_steps()==null) || 
             (this.reform_steps!=null &&
              this.reform_steps.equals(other.getReform_steps()))) &&
            ((this.reportingElements==null && other.getReportingElements()==null) || 
             (this.reportingElements!=null &&
              this.reportingElements.equals(other.getReportingElements()))) &&
            ((this.reportingNodes==null && other.getReportingNodes()==null) || 
             (this.reportingNodes!=null &&
              this.reportingNodes.equals(other.getReportingNodes()))) &&
            ((this.restartFile==null && other.getRestartFile()==null) || 
             (this.restartFile!=null &&
              this.restartFile.equals(other.getRestartFile()))) &&
            ((this.run_choice==null && other.getRun_choice()==null) || 
             (this.run_choice!=null &&
              this.run_choice.equals(other.getRun_choice()))) &&
            ((this.shape_flag==null && other.getShape_flag()==null) || 
             (this.shape_flag!=null &&
              this.shape_flag.equals(other.getShape_flag()))) &&
            ((this.solver_flag==null && other.getSolver_flag()==null) || 
             (this.solver_flag!=null &&
              this.solver_flag.equals(other.getSolver_flag()))) &&
            ((this.south_bc==null && other.getSouth_bc()==null) || 
             (this.south_bc!=null &&
              this.south_bc.equals(other.getSouth_bc()))) &&
            ((this.south_bc_value==null && other.getSouth_bc_value()==null) || 
             (this.south_bc_value!=null &&
              this.south_bc_value.equals(other.getSouth_bc_value()))) &&
            ((this.time_step==null && other.getTime_step()==null) || 
             (this.time_step!=null &&
              this.time_step.equals(other.getTime_step()))) &&
            ((this.top_bc==null && other.getTop_bc()==null) || 
             (this.top_bc!=null &&
              this.top_bc.equals(other.getTop_bc()))) &&
            ((this.top_bc_value==null && other.getTop_bc_value()==null) || 
             (this.top_bc_value!=null &&
              this.top_bc_value.equals(other.getTop_bc_value()))) &&
            ((this.west_bc==null && other.getWest_bc()==null) || 
             (this.west_bc!=null &&
              this.west_bc.equals(other.getWest_bc()))) &&
            ((this.west_bc_value==null && other.getWest_bc_value()==null) || 
             (this.west_bc_value!=null &&
              this.west_bc_value.equals(other.getWest_bc_value())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAlpha() != null) {
            _hashCode += getAlpha().hashCode();
        }
        if (getBackup_steps() != null) {
            _hashCode += getBackup_steps().hashCode();
        }
        if (getBottom_bc() != null) {
            _hashCode += getBottom_bc().hashCode();
        }
        if (getBottom_bc_value() != null) {
            _hashCode += getBottom_bc_value().hashCode();
        }
        if (getCheckpointFile() != null) {
            _hashCode += getCheckpointFile().hashCode();
        }
        if (getEast_bc() != null) {
            _hashCode += getEast_bc().hashCode();
        }
        if (getEast_bc_value() != null) {
            _hashCode += getEast_bc_value().hashCode();
        }
        if (getEnd_time() != null) {
            _hashCode += getEnd_time().hashCode();
        }
        if (getFault_interval() != null) {
            _hashCode += getFault_interval().hashCode();
        }
        if (getInputFileName() != null) {
            _hashCode += getInputFileName().hashCode();
        }
        if (getLogFileName() != null) {
            _hashCode += getLogFileName().hashCode();
        }
        if (getNorth_bc() != null) {
            _hashCode += getNorth_bc().hashCode();
        }
        if (getNorth_bc_value() != null) {
            _hashCode += getNorth_bc_value().hashCode();
        }
        if (getNrates() != null) {
            _hashCode += getNrates().hashCode();
        }
        if (getNumber_degrees_freedom() != null) {
            _hashCode += getNumber_degrees_freedom().hashCode();
        }
        if (getNumber_space_dimensions() != null) {
            _hashCode += getNumber_space_dimensions().hashCode();
        }
        if (getNumber_time_groups() != null) {
            _hashCode += getNumber_time_groups().hashCode();
        }
        if (getNumberofPrintTimes() != null) {
            _hashCode += getNumberofPrintTimes().hashCode();
        }
        if (getOutputFileName() != null) {
            _hashCode += getOutputFileName().hashCode();
        }
        if (getPrintTimesInterval() != null) {
            _hashCode += getPrintTimesInterval().hashCode();
        }
        if (getPrintTimesType() != null) {
            _hashCode += getPrintTimesType().hashCode();
        }
        if (getReform_steps() != null) {
            _hashCode += getReform_steps().hashCode();
        }
        if (getReportingElements() != null) {
            _hashCode += getReportingElements().hashCode();
        }
        if (getReportingNodes() != null) {
            _hashCode += getReportingNodes().hashCode();
        }
        if (getRestartFile() != null) {
            _hashCode += getRestartFile().hashCode();
        }
        if (getRun_choice() != null) {
            _hashCode += getRun_choice().hashCode();
        }
        if (getShape_flag() != null) {
            _hashCode += getShape_flag().hashCode();
        }
        if (getSolver_flag() != null) {
            _hashCode += getSolver_flag().hashCode();
        }
        if (getSouth_bc() != null) {
            _hashCode += getSouth_bc().hashCode();
        }
        if (getSouth_bc_value() != null) {
            _hashCode += getSouth_bc_value().hashCode();
        }
        if (getTime_step() != null) {
            _hashCode += getTime_step().hashCode();
        }
        if (getTop_bc() != null) {
            _hashCode += getTop_bc().hashCode();
        }
        if (getTop_bc_value() != null) {
            _hashCode += getTop_bc_value().hashCode();
        }
        if (getWest_bc() != null) {
            _hashCode += getWest_bc().hashCode();
        }
        if (getWest_bc_value() != null) {
            _hashCode += getWest_bc_value().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GeotransParamsBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:GeoFESTService", "GeotransParamsBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("alpha");
        elemField.setXmlName(new javax.xml.namespace.QName("", "alpha"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("backup_steps");
        elemField.setXmlName(new javax.xml.namespace.QName("", "backup_steps"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bottom_bc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bottom_bc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bottom_bc_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "bottom_bc_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("checkpointFile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "checkpointFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("east_bc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "east_bc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("east_bc_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "east_bc_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("end_time");
        elemField.setXmlName(new javax.xml.namespace.QName("", "end_time"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fault_interval");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fault_interval"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputFileName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inputFileName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logFileName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "logFileName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("north_bc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "north_bc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("north_bc_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "north_bc_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nrates");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nrates"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("number_degrees_freedom");
        elemField.setXmlName(new javax.xml.namespace.QName("", "number_degrees_freedom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("number_space_dimensions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "number_space_dimensions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("number_time_groups");
        elemField.setXmlName(new javax.xml.namespace.QName("", "number_time_groups"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberofPrintTimes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numberofPrintTimes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outputFileName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "outputFileName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("printTimesInterval");
        elemField.setXmlName(new javax.xml.namespace.QName("", "printTimesInterval"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("printTimesType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "printTimesType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reform_steps");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reform_steps"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportingElements");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reportingElements"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportingNodes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "reportingNodes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("restartFile");
        elemField.setXmlName(new javax.xml.namespace.QName("", "restartFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("run_choice");
        elemField.setXmlName(new javax.xml.namespace.QName("", "run_choice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shape_flag");
        elemField.setXmlName(new javax.xml.namespace.QName("", "shape_flag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("solver_flag");
        elemField.setXmlName(new javax.xml.namespace.QName("", "solver_flag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("south_bc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "south_bc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("south_bc_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "south_bc_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("time_step");
        elemField.setXmlName(new javax.xml.namespace.QName("", "time_step"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("top_bc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "top_bc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("top_bc_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "top_bc_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("west_bc");
        elemField.setXmlName(new javax.xml.namespace.QName("", "west_bc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("west_bc_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "west_bc_value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
