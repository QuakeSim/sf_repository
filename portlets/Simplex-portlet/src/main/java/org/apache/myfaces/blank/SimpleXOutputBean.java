/**
 * SimpleXOutputBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.apache.myfaces.blank;

import java.util.Date;

public class SimpleXOutputBean  implements java.io.Serializable {
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
           java.lang.String faultUrl,
           java.lang.String inputUrl,
           java.lang.String jobUIDStamp,
           java.lang.String[] kmlUrls,
           java.lang.String logUrl,
           java.lang.String outputUrl,
           java.lang.String projectName,
           boolean view) {
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
    public boolean getView() {
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

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SimpleXOutputBean)) return false;
        SimpleXOutputBean other = (SimpleXOutputBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.faultUrl==null && other.getFaultUrl()==null) || 
             (this.faultUrl!=null &&
              this.faultUrl.equals(other.getFaultUrl()))) &&
            ((this.inputUrl==null && other.getInputUrl()==null) || 
             (this.inputUrl!=null &&
              this.inputUrl.equals(other.getInputUrl()))) &&
            ((this.jobUIDStamp==null && other.getJobUIDStamp()==null) || 
             (this.jobUIDStamp!=null &&
              this.jobUIDStamp.equals(other.getJobUIDStamp()))) &&
            ((this.kmlUrls==null && other.getKmlUrls()==null) || 
             (this.kmlUrls!=null &&
              java.util.Arrays.equals(this.kmlUrls, other.getKmlUrls()))) &&
            ((this.logUrl==null && other.getLogUrl()==null) || 
             (this.logUrl!=null &&
              this.logUrl.equals(other.getLogUrl()))) &&
            ((this.outputUrl==null && other.getOutputUrl()==null) || 
             (this.outputUrl!=null &&
              this.outputUrl.equals(other.getOutputUrl()))) &&
            ((this.projectName==null && other.getProjectName()==null) || 
             (this.projectName!=null &&
              this.projectName.equals(other.getProjectName()))) &&
            this.view == other.getView();
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
        if (getFaultUrl() != null) {
            _hashCode += getFaultUrl().hashCode();
        }
        if (getInputUrl() != null) {
            _hashCode += getInputUrl().hashCode();
        }
        if (getJobUIDStamp() != null) {
            _hashCode += getJobUIDStamp().hashCode();
        }
        if (getKmlUrls() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getKmlUrls());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getKmlUrls(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLogUrl() != null) {
            _hashCode += getLogUrl().hashCode();
        }
        if (getOutputUrl() != null) {
            _hashCode += getOutputUrl().hashCode();
        }
        if (getProjectName() != null) {
            _hashCode += getProjectName().hashCode();
        }
        _hashCode += (getView() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SimpleXOutputBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:SimpleXService", "SimpleXOutputBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("faultUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "faultUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inputUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("jobUIDStamp");
        elemField.setXmlName(new javax.xml.namespace.QName("", "jobUIDStamp"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("kmlUrls");
        elemField.setXmlName(new javax.xml.namespace.QName("", "kmlUrls"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("logUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "logUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outputUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "outputUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("projectName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "projectName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("view");
        elemField.setXmlName(new javax.xml.namespace.QName("", "view"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
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
