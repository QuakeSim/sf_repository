
/**
 * RDAHMMResultsBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.webservices;

public class RDAHMMResultsBean  implements java.io.Serializable {
    private java.lang.String AUrl;

    private java.lang.String BUrl;

    private java.lang.String LUrl;

    private java.lang.String QUrl;

    private java.lang.String inputUrl;

    private java.lang.String inputXPngUrl;

    private java.lang.String inputYPngUrl;

    private java.lang.String inputZPngUrl;

    private java.lang.String maxvalUrl;

    private java.lang.String minvalUrl;

    private java.lang.String piUrl;

    private java.lang.String rangeUrl;

    private java.lang.String stdoutUrl;

    public RDAHMMResultsBean() {
    }

    public RDAHMMResultsBean(
           java.lang.String AUrl,
           java.lang.String BUrl,
           java.lang.String LUrl,
           java.lang.String QUrl,
           java.lang.String inputUrl,
           java.lang.String inputXPngUrl,
           java.lang.String inputYPngUrl,
           java.lang.String inputZPngUrl,
           java.lang.String maxvalUrl,
           java.lang.String minvalUrl,
           java.lang.String piUrl,
           java.lang.String rangeUrl,
           java.lang.String stdoutUrl) {
           this.AUrl = AUrl;
           this.BUrl = BUrl;
           this.LUrl = LUrl;
           this.QUrl = QUrl;
           this.inputUrl = inputUrl;			  
//            this.inputXPngUrl = inputXPngUrl;
//            this.inputYPngUrl = inputYPngUrl;
//            this.inputZPngUrl = inputZPngUrl;
			  setInputXPngUrl(inputXPngUrl);
			  setInputYPngUrl(inputYPngUrl);
			  setInputZPngUrl(inputZPngUrl);
           this.maxvalUrl = maxvalUrl;
           this.minvalUrl = minvalUrl;
           this.piUrl = piUrl;
           this.rangeUrl = rangeUrl;
           this.stdoutUrl = stdoutUrl;
    }


    /**
     * Gets the AUrl value for this RDAHMMResultsBean.
     * 
     * @return AUrl
     */
    public java.lang.String getAUrl() {
        return AUrl;
    }


    /**
     * Sets the AUrl value for this RDAHMMResultsBean.
     * 
     * @param AUrl
     */
    public void setAUrl(java.lang.String AUrl) {
        this.AUrl = AUrl;
    }


    /**
     * Gets the BUrl value for this RDAHMMResultsBean.
     * 
     * @return BUrl
     */
    public java.lang.String getBUrl() {
        return BUrl;
    }


    /**
     * Sets the BUrl value for this RDAHMMResultsBean.
     * 
     * @param BUrl
     */
    public void setBUrl(java.lang.String BUrl) {
        this.BUrl = BUrl;
    }


    /**
     * Gets the LUrl value for this RDAHMMResultsBean.
     * 
     * @return LUrl
     */
    public java.lang.String getLUrl() {
        return LUrl;
    }


    /**
     * Sets the LUrl value for this RDAHMMResultsBean.
     * 
     * @param LUrl
     */
    public void setLUrl(java.lang.String LUrl) {
        this.LUrl = LUrl;
    }


    /**
     * Gets the QUrl value for this RDAHMMResultsBean.
     * 
     * @return QUrl
     */
    public java.lang.String getQUrl() {
        return QUrl;
    }


    /**
     * Sets the QUrl value for this RDAHMMResultsBean.
     * 
     * @param QUrl
     */
    public void setQUrl(java.lang.String QUrl) {
        this.QUrl = QUrl;
    }


    /**
     * Gets the inputUrl value for this RDAHMMResultsBean.
     * 
     * @return inputUrl
     */
    public java.lang.String getInputUrl() {
        return inputUrl;
    }


    /**
     * Sets the inputUrl value for this RDAHMMResultsBean.
     * 
     * @param inputUrl
     */
    public void setInputUrl(java.lang.String inputUrl) {
        this.inputUrl = inputUrl;
    }


    /**
     * Gets the inputXPngUrl value for this RDAHMMResultsBean.
     * 
     * @return inputXPngUrl
     */
    public java.lang.String getInputXPngUrl() {
        return inputXPngUrl;
    }


    /**
     * Sets the inputXPngUrl value for this RDAHMMResultsBean.
     * 
     * @param inputXPngUrl
     */
    public void setInputXPngUrl(java.lang.String inputXPngUrl) {
		  this.inputXPngUrl = inputXPngUrl;
		  
    }


    /**
     * Gets the inputYPngUrl value for this RDAHMMResultsBean.
     * 
     * @return inputYPngUrl
     */
    public java.lang.String getInputYPngUrl() {
        return inputYPngUrl;
    }


    /**
     * Sets the inputYPngUrl value for this RDAHMMResultsBean.
     * 
     * @param inputYPngUrl
     */
    public void setInputYPngUrl(java.lang.String inputYPngUrl) {
		  this.inputYPngUrl = inputYPngUrl;
    }


    /**
     * Gets the inputZPngUrl value for this RDAHMMResultsBean.
     * 
     * @return inputZPngUrl
     */
    public java.lang.String getInputZPngUrl() {
        return inputZPngUrl;
    }


    /**
     * Sets the inputZPngUrl value for this RDAHMMResultsBean.
     * 
     * @param inputZPngUrl
     */
    public void setInputZPngUrl(java.lang.String inputZPngUrl) {
		  this.inputZPngUrl = inputZPngUrl;
    }

    /**
     * Gets the maxvalUrl value for this RDAHMMResultsBean.
     * 
     * @return maxvalUrl
     */
    public java.lang.String getMaxvalUrl() {
        return maxvalUrl;
    }


    /**
     * Sets the maxvalUrl value for this RDAHMMResultsBean.
     * 
     * @param maxvalUrl
     */
    public void setMaxvalUrl(java.lang.String maxvalUrl) {
        this.maxvalUrl = maxvalUrl;
    }


    /**
     * Gets the minvalUrl value for this RDAHMMResultsBean.
     * 
     * @return minvalUrl
     */
    public java.lang.String getMinvalUrl() {
        return minvalUrl;
    }


    /**
     * Sets the minvalUrl value for this RDAHMMResultsBean.
     * 
     * @param minvalUrl
     */
    public void setMinvalUrl(java.lang.String minvalUrl) {
        this.minvalUrl = minvalUrl;
    }


    /**
     * Gets the piUrl value for this RDAHMMResultsBean.
     * 
     * @return piUrl
     */
    public java.lang.String getPiUrl() {
        return piUrl;
    }


    /**
     * Sets the piUrl value for this RDAHMMResultsBean.
     * 
     * @param piUrl
     */
    public void setPiUrl(java.lang.String piUrl) {
        this.piUrl = piUrl;
    }


    /**
     * Gets the rangeUrl value for this RDAHMMResultsBean.
     * 
     * @return rangeUrl
     */
    public java.lang.String getRangeUrl() {
        return rangeUrl;
    }


    /**
     * Sets the rangeUrl value for this RDAHMMResultsBean.
     * 
     * @param rangeUrl
     */
    public void setRangeUrl(java.lang.String rangeUrl) {
        this.rangeUrl = rangeUrl;
    }


    /**
     * Gets the stdoutUrl value for this RDAHMMResultsBean.
     * 
     * @return stdoutUrl
     */
    public java.lang.String getStdoutUrl() {
        return stdoutUrl;
    }


    /**
     * Sets the stdoutUrl value for this RDAHMMResultsBean.
     * 
     * @param stdoutUrl
     */
    public void setStdoutUrl(java.lang.String stdoutUrl) {
        this.stdoutUrl = stdoutUrl;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RDAHMMResultsBean)) return false;
        RDAHMMResultsBean other = (RDAHMMResultsBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.AUrl==null && other.getAUrl()==null) || 
             (this.AUrl!=null &&
              this.AUrl.equals(other.getAUrl()))) &&
            ((this.BUrl==null && other.getBUrl()==null) || 
             (this.BUrl!=null &&
              this.BUrl.equals(other.getBUrl()))) &&
            ((this.LUrl==null && other.getLUrl()==null) || 
             (this.LUrl!=null &&
              this.LUrl.equals(other.getLUrl()))) &&
            ((this.QUrl==null && other.getQUrl()==null) || 
             (this.QUrl!=null &&
              this.QUrl.equals(other.getQUrl()))) &&
            ((this.inputUrl==null && other.getInputUrl()==null) || 
             (this.inputUrl!=null &&
              this.inputUrl.equals(other.getInputUrl()))) &&
            ((this.inputXPngUrl==null && other.getInputXPngUrl()==null) || 
             (this.inputXPngUrl!=null &&
              this.inputXPngUrl.equals(other.getInputXPngUrl()))) &&
            ((this.inputYPngUrl==null && other.getInputYPngUrl()==null) || 
             (this.inputYPngUrl!=null &&
              this.inputYPngUrl.equals(other.getInputYPngUrl()))) &&
            ((this.inputZPngUrl==null && other.getInputZPngUrl()==null) || 
             (this.inputZPngUrl!=null &&
              this.inputZPngUrl.equals(other.getInputZPngUrl()))) &&
            ((this.maxvalUrl==null && other.getMaxvalUrl()==null) || 
             (this.maxvalUrl!=null &&
              this.maxvalUrl.equals(other.getMaxvalUrl()))) &&
            ((this.minvalUrl==null && other.getMinvalUrl()==null) || 
             (this.minvalUrl!=null &&
              this.minvalUrl.equals(other.getMinvalUrl()))) &&
            ((this.piUrl==null && other.getPiUrl()==null) || 
             (this.piUrl!=null &&
              this.piUrl.equals(other.getPiUrl()))) &&
            ((this.rangeUrl==null && other.getRangeUrl()==null) || 
             (this.rangeUrl!=null &&
              this.rangeUrl.equals(other.getRangeUrl()))) &&
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
        if (getAUrl() != null) {
            _hashCode += getAUrl().hashCode();
        }
        if (getBUrl() != null) {
            _hashCode += getBUrl().hashCode();
        }
        if (getLUrl() != null) {
            _hashCode += getLUrl().hashCode();
        }
        if (getQUrl() != null) {
            _hashCode += getQUrl().hashCode();
        }
        if (getInputUrl() != null) {
            _hashCode += getInputUrl().hashCode();
        }
        if (getInputXPngUrl() != null) {
            _hashCode += getInputXPngUrl().hashCode();
        }
        if (getInputYPngUrl() != null) {
            _hashCode += getInputYPngUrl().hashCode();
        }
        if (getInputZPngUrl() != null) {
            _hashCode += getInputZPngUrl().hashCode();
        }
        if (getMaxvalUrl() != null) {
            _hashCode += getMaxvalUrl().hashCode();
        }
        if (getMinvalUrl() != null) {
            _hashCode += getMinvalUrl().hashCode();
        }
        if (getPiUrl() != null) {
            _hashCode += getPiUrl().hashCode();
        }
        if (getRangeUrl() != null) {
            _hashCode += getRangeUrl().hashCode();
        }
        if (getStdoutUrl() != null) {
            _hashCode += getStdoutUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RDAHMMResultsBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:RDAHMMResultsBean", "RDAHMMResultsBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("AUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "AUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("LUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "LUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("QUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "QUrl"));
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
        elemField.setFieldName("inputXPngUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inputXPngUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputYPngUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inputYPngUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inputZPngUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inputZPngUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxvalUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "maxvalUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minvalUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "minvalUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("piUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "piUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rangeUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rangeUrl"));
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
