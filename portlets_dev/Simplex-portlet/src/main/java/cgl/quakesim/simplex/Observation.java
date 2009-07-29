/**
 * Observation.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.simplex;

public class Observation  implements java.io.Serializable {
    private java.lang.String obsvError;

    private java.lang.String obsvLocationEast;

    private java.lang.String obsvLocationNorth;

    private java.lang.String obsvName;

    private java.lang.String obsvRefSite;

    private java.lang.String obsvType;

    private java.lang.String obsvValue;

    public Observation() {
    }

    public Observation(
           java.lang.String obsvError,
           java.lang.String obsvLocationEast,
           java.lang.String obsvLocationNorth,
           java.lang.String obsvName,
           java.lang.String obsvRefSite,
           java.lang.String obsvType,
           java.lang.String obsvValue) {
           this.obsvError = obsvError;
           this.obsvLocationEast = obsvLocationEast;
           this.obsvLocationNorth = obsvLocationNorth;
           this.obsvName = obsvName;
           this.obsvRefSite = obsvRefSite;
           this.obsvType = obsvType;
           this.obsvValue = obsvValue;
    }


    /**
     * Gets the obsvError value for this Observation.
     * 
     * @return obsvError
     */
    public java.lang.String getObsvError() {
        return obsvError;
    }


    /**
     * Sets the obsvError value for this Observation.
     * 
     * @param obsvError
     */
    public void setObsvError(java.lang.String obsvError) {
        this.obsvError = obsvError;
    }


    /**
     * Gets the obsvLocationEast value for this Observation.
     * 
     * @return obsvLocationEast
     */
    public java.lang.String getObsvLocationEast() {
        return obsvLocationEast;
    }


    /**
     * Sets the obsvLocationEast value for this Observation.
     * 
     * @param obsvLocationEast
     */
    public void setObsvLocationEast(java.lang.String obsvLocationEast) {
        this.obsvLocationEast = obsvLocationEast;
    }


    /**
     * Gets the obsvLocationNorth value for this Observation.
     * 
     * @return obsvLocationNorth
     */
    public java.lang.String getObsvLocationNorth() {
        return obsvLocationNorth;
    }


    /**
     * Sets the obsvLocationNorth value for this Observation.
     * 
     * @param obsvLocationNorth
     */
    public void setObsvLocationNorth(java.lang.String obsvLocationNorth) {
        this.obsvLocationNorth = obsvLocationNorth;
    }


    /**
     * Gets the obsvName value for this Observation.
     * 
     * @return obsvName
     */
    public java.lang.String getObsvName() {
        return obsvName;
    }


    /**
     * Sets the obsvName value for this Observation.
     * 
     * @param obsvName
     */
    public void setObsvName(java.lang.String obsvName) {
        this.obsvName = obsvName;
    }


    /**
     * Gets the obsvRefSite value for this Observation.
     * 
     * @return obsvRefSite
     */
    public java.lang.String getObsvRefSite() {
        return obsvRefSite;
    }


    /**
     * Sets the obsvRefSite value for this Observation.
     * 
     * @param obsvRefSite
     */
    public void setObsvRefSite(java.lang.String obsvRefSite) {
        this.obsvRefSite = obsvRefSite;
    }


    /**
     * Gets the obsvType value for this Observation.
     * 
     * @return obsvType
     */
    public java.lang.String getObsvType() {
        return obsvType;
    }


    /**
     * Sets the obsvType value for this Observation.
     * 
     * @param obsvType
     */
    public void setObsvType(java.lang.String obsvType) {
        this.obsvType = obsvType;
    }


    /**
     * Gets the obsvValue value for this Observation.
     * 
     * @return obsvValue
     */
    public java.lang.String getObsvValue() {
        return obsvValue;
    }


    /**
     * Sets the obsvValue value for this Observation.
     * 
     * @param obsvValue
     */
    public void setObsvValue(java.lang.String obsvValue) {
        this.obsvValue = obsvValue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Observation)) return false;
        Observation other = (Observation) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.obsvError==null && other.getObsvError()==null) || 
             (this.obsvError!=null &&
              this.obsvError.equals(other.getObsvError()))) &&
            ((this.obsvLocationEast==null && other.getObsvLocationEast()==null) || 
             (this.obsvLocationEast!=null &&
              this.obsvLocationEast.equals(other.getObsvLocationEast()))) &&
            ((this.obsvLocationNorth==null && other.getObsvLocationNorth()==null) || 
             (this.obsvLocationNorth!=null &&
              this.obsvLocationNorth.equals(other.getObsvLocationNorth()))) &&
            ((this.obsvName==null && other.getObsvName()==null) || 
             (this.obsvName!=null &&
              this.obsvName.equals(other.getObsvName()))) &&
            ((this.obsvRefSite==null && other.getObsvRefSite()==null) || 
             (this.obsvRefSite!=null &&
              this.obsvRefSite.equals(other.getObsvRefSite()))) &&
            ((this.obsvType==null && other.getObsvType()==null) || 
             (this.obsvType!=null &&
              this.obsvType.equals(other.getObsvType()))) &&
            ((this.obsvValue==null && other.getObsvValue()==null) || 
             (this.obsvValue!=null &&
              this.obsvValue.equals(other.getObsvValue())));
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
        if (getObsvError() != null) {
            _hashCode += getObsvError().hashCode();
        }
        if (getObsvLocationEast() != null) {
            _hashCode += getObsvLocationEast().hashCode();
        }
        if (getObsvLocationNorth() != null) {
            _hashCode += getObsvLocationNorth().hashCode();
        }
        if (getObsvName() != null) {
            _hashCode += getObsvName().hashCode();
        }
        if (getObsvRefSite() != null) {
            _hashCode += getObsvRefSite().hashCode();
        }
        if (getObsvType() != null) {
            _hashCode += getObsvType().hashCode();
        }
        if (getObsvValue() != null) {
            _hashCode += getObsvValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Observation.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:SimpleXService", "Observation"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obsvError");
        elemField.setXmlName(new javax.xml.namespace.QName("", "obsvError"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obsvLocationEast");
        elemField.setXmlName(new javax.xml.namespace.QName("", "obsvLocationEast"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obsvLocationNorth");
        elemField.setXmlName(new javax.xml.namespace.QName("", "obsvLocationNorth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obsvName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "obsvName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obsvRefSite");
        elemField.setXmlName(new javax.xml.namespace.QName("", "obsvRefSite"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obsvType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "obsvType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obsvValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "obsvValue"));
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
