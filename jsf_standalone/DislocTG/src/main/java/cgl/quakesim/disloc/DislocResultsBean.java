/**
 * DislocResultsBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.disloc;

public class DislocResultsBean  implements java.io.Serializable {
    private java.lang.String inputFileUrl;

    private java.lang.String jobUIDStamp;

    private java.lang.String outputFileUrl;

    private java.lang.String projectName;

    private java.lang.String stdoutUrl;

    public DislocResultsBean() {
    }

    public DislocResultsBean(
           java.lang.String inputFileUrl,
           java.lang.String jobUIDStamp,
           java.lang.String outputFileUrl,
           java.lang.String projectName,
           java.lang.String stdoutUrl) {
           this.inputFileUrl = inputFileUrl;
           this.jobUIDStamp = jobUIDStamp;
           this.outputFileUrl = outputFileUrl;
           this.projectName = projectName;
           this.stdoutUrl = stdoutUrl;
    }


    /**
     * Gets the inputFileUrl value for this DislocResultsBean.
     * 
     * @return inputFileUrl
     */
    public java.lang.String getInputFileUrl() {
        return inputFileUrl;
    }


    /**
     * Sets the inputFileUrl value for this DislocResultsBean.
     * 
     * @param inputFileUrl
     */
    public void setInputFileUrl(java.lang.String inputFileUrl) {
        this.inputFileUrl = inputFileUrl;
    }


    /**
     * Gets the jobUIDStamp value for this DislocResultsBean.
     * 
     * @return jobUIDStamp
     */
    public java.lang.String getJobUIDStamp() {
        return jobUIDStamp;
    }


    /**
     * Sets the jobUIDStamp value for this DislocResultsBean.
     * 
     * @param jobUIDStamp
     */
    public void setJobUIDStamp(java.lang.String jobUIDStamp) {
        this.jobUIDStamp = jobUIDStamp;
    }


    /**
     * Gets the outputFileUrl value for this DislocResultsBean.
     * 
     * @return outputFileUrl
     */
    public java.lang.String getOutputFileUrl() {
        return outputFileUrl;
    }


    /**
     * Sets the outputFileUrl value for this DislocResultsBean.
     * 
     * @param outputFileUrl
     */
    public void setOutputFileUrl(java.lang.String outputFileUrl) {
        this.outputFileUrl = outputFileUrl;
    }


    /**
     * Gets the projectName value for this DislocResultsBean.
     * 
     * @return projectName
     */
    public java.lang.String getProjectName() {
        return projectName;
    }


    /**
     * Sets the projectName value for this DislocResultsBean.
     * 
     * @param projectName
     */
    public void setProjectName(java.lang.String projectName) {
        this.projectName = projectName;
    }


    /**
     * Gets the stdoutUrl value for this DislocResultsBean.
     * 
     * @return stdoutUrl
     */
    public java.lang.String getStdoutUrl() {
        return stdoutUrl;
    }


    /**
     * Sets the stdoutUrl value for this DislocResultsBean.
     * 
     * @param stdoutUrl
     */
    public void setStdoutUrl(java.lang.String stdoutUrl) {
        this.stdoutUrl = stdoutUrl;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DislocResultsBean)) return false;
        DislocResultsBean other = (DislocResultsBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.inputFileUrl==null && other.getInputFileUrl()==null) || 
             (this.inputFileUrl!=null &&
              this.inputFileUrl.equals(other.getInputFileUrl()))) &&
            ((this.jobUIDStamp==null && other.getJobUIDStamp()==null) || 
             (this.jobUIDStamp!=null &&
              this.jobUIDStamp.equals(other.getJobUIDStamp()))) &&
            ((this.outputFileUrl==null && other.getOutputFileUrl()==null) || 
             (this.outputFileUrl!=null &&
              this.outputFileUrl.equals(other.getOutputFileUrl()))) &&
            ((this.projectName==null && other.getProjectName()==null) || 
             (this.projectName!=null &&
              this.projectName.equals(other.getProjectName()))) &&
            ((this.stdoutUrl==null && other.getStdoutUrl()==null) || 
             (this.stdoutUrl!=null &&
              this.stdoutUrl.equals(other.getStdoutUrl())));
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
        if (getInputFileUrl() != null) {
            _hashCode += getInputFileUrl().hashCode();
        }
        if (getJobUIDStamp() != null) {
            _hashCode += getJobUIDStamp().hashCode();
        }
        if (getOutputFileUrl() != null) {
            _hashCode += getOutputFileUrl().hashCode();
        }
        if (getProjectName() != null) {
            _hashCode += getProjectName().hashCode();
        }
        if (getStdoutUrl() != null) {
            _hashCode += getStdoutUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DislocResultsBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:DislocService", "DislocResultsBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputFileUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inputFileUrl"));
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
        elemField.setFieldName("outputFileUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "outputFileUrl"));
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
        elemField.setFieldName("stdoutUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stdoutUrl"));
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
