/**
 * SimpleXOutputBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 * Some of this was cleaned up to make it simpler.  
 */

package cgl.quakesim.simplex;

public class SimpleXOutputBean  {
    private java.lang.String creationDate;

    private java.lang.String faultUrl;

    private java.lang.String inputUrl;

    private java.lang.String jobUIDStamp;

    private java.lang.String[] kmlUrls;

    private java.lang.String logUrl;

    private java.lang.String outputUrl;

    private java.lang.String projectName;

    private boolean view;

    public SimpleXOutputBean() {
    }

    public SimpleXOutputBean(
           java.lang.String creationDate,
           java.lang.String faultUrl,
           java.lang.String inputUrl,
           java.lang.String jobUIDStamp,
           java.lang.String[] kmlUrls,
           java.lang.String logUrl,
           java.lang.String outputUrl,
           java.lang.String projectName,
           boolean view) {
           this.creationDate = creationDate;
           this.faultUrl = faultUrl;
           this.inputUrl = inputUrl;
           this.jobUIDStamp = jobUIDStamp;
           this.kmlUrls = kmlUrls;
           this.logUrl = logUrl;
           this.outputUrl = outputUrl;
           this.projectName = projectName;
           this.view = view;
    }


    /**
     * Gets the creationDate value for this SimpleXOutputBean.
     * 
     * @return creationDate
     */
    public java.lang.String getCreationDate() {
        return creationDate;
    }


    /**
     * Sets the creationDate value for this SimpleXOutputBean.
     * 
     * @param creationDate
     */
    public void setCreationDate(java.lang.String creationDate) {
        this.creationDate = creationDate;
    }


    /**
     * Gets the faultUrl value for this SimpleXOutputBean.
     * 
     * @return faultUrl
     */
    public java.lang.String getFaultUrl() {
        return faultUrl;
    }


    /**
     * Sets the faultUrl value for this SimpleXOutputBean.
     * 
     * @param faultUrl
     */
    public void setFaultUrl(java.lang.String faultUrl) {
        this.faultUrl = faultUrl;
    }


    /**
     * Gets the inputUrl value for this SimpleXOutputBean.
     * 
     * @return inputUrl
     */
    public java.lang.String getInputUrl() {
        return inputUrl;
    }


    /**
     * Sets the inputUrl value for this SimpleXOutputBean.
     * 
     * @param inputUrl
     */
    public void setInputUrl(java.lang.String inputUrl) {
        this.inputUrl = inputUrl;
    }


    /**
     * Gets the jobUIDStamp value for this SimpleXOutputBean.
     * 
     * @return jobUIDStamp
     */
    public java.lang.String getJobUIDStamp() {
        return jobUIDStamp;
    }


    /**
     * Sets the jobUIDStamp value for this SimpleXOutputBean.
     * 
     * @param jobUIDStamp
     */
    public void setJobUIDStamp(java.lang.String jobUIDStamp) {
        this.jobUIDStamp = jobUIDStamp;
    }


    /**
     * Gets the kmlUrls value for this SimpleXOutputBean.
     * 
     * @return kmlUrls
     */
    public java.lang.String[] getKmlUrls() {
        return kmlUrls;
    }


    /**
     * Sets the kmlUrls value for this SimpleXOutputBean.
     * 
     * @param kmlUrls
     */
    public void setKmlUrls(java.lang.String[] kmlUrls) {
        this.kmlUrls = kmlUrls;
    }


    /**
     * Gets the logUrl value for this SimpleXOutputBean.
     * 
     * @return logUrl
     */
    public java.lang.String getLogUrl() {
        return logUrl;
    }


    /**
     * Sets the logUrl value for this SimpleXOutputBean.
     * 
     * @param logUrl
     */
    public void setLogUrl(java.lang.String logUrl) {
        this.logUrl = logUrl;
    }


    /**
     * Gets the outputUrl value for this SimpleXOutputBean.
     * 
     * @return outputUrl
     */
    public java.lang.String getOutputUrl() {
        return outputUrl;
    }


    /**
     * Sets the outputUrl value for this SimpleXOutputBean.
     * 
     * @param outputUrl
     */
    public void setOutputUrl(java.lang.String outputUrl) {
        this.outputUrl = outputUrl;
    }


    /**
     * Gets the projectName value for this SimpleXOutputBean.
     * 
     * @return projectName
     */
    public java.lang.String getProjectName() {
        return projectName;
    }


    /**
     * Sets the projectName value for this SimpleXOutputBean.
     * 
     * @param projectName
     */
    public void setProjectName(java.lang.String projectName) {
        this.projectName = projectName;
    }


    /**
     * Gets the view value for this SimpleXOutputBean.
     * 
     * @return view
     */
    public boolean isView() {
        return view;
    }


    /**
     * Sets the view value for this SimpleXOutputBean.
     * 
     * @param view
     */
    public void setView(boolean view) {
        this.view = view;
    }


}
