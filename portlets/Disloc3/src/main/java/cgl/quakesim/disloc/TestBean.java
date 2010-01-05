/**
 * TestBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.disloc;

public class TestBean  implements java.io.Serializable {
    private java.lang.String latPoint;

    private java.lang.String lonPoint;

    private java.lang.String xcartPoint;

    private java.lang.String ycartPoint;

    public TestBean() {
    }

    public TestBean(
           java.lang.String latPoint,
           java.lang.String lonPoint,
           java.lang.String xcartPoint,
           java.lang.String ycartPoint) {
           this.latPoint = latPoint;
           this.lonPoint = lonPoint;
           this.xcartPoint = xcartPoint;
           this.ycartPoint = ycartPoint;
    }


    /**
     * Gets the latPoint value for this TestBean.
     * 
     * @return latPoint
     */
    public java.lang.String getLatPoint() {
        return latPoint;
    }


    /**
     * Sets the latPoint value for this TestBean.
     * 
     * @param latPoint
     */
    public void setLatPoint(java.lang.String latPoint) {
        this.latPoint = latPoint;
    }


    /**
     * Gets the lonPoint value for this TestBean.
     * 
     * @return lonPoint
     */
    public java.lang.String getLonPoint() {
        return lonPoint;
    }


    /**
     * Sets the lonPoint value for this TestBean.
     * 
     * @param lonPoint
     */
    public void setLonPoint(java.lang.String lonPoint) {
        this.lonPoint = lonPoint;
    }


    /**
     * Gets the xcartPoint value for this TestBean.
     * 
     * @return xcartPoint
     */
    public java.lang.String getXcartPoint() {
        return xcartPoint;
    }


    /**
     * Sets the xcartPoint value for this TestBean.
     * 
     * @param xcartPoint
     */
    public void setXcartPoint(java.lang.String xcartPoint) {
        this.xcartPoint = xcartPoint;
    }


    /**
     * Gets the ycartPoint value for this TestBean.
     * 
     * @return ycartPoint
     */
    public java.lang.String getYcartPoint() {
        return ycartPoint;
    }


    /**
     * Sets the ycartPoint value for this TestBean.
     * 
     * @param ycartPoint
     */
    public void setYcartPoint(java.lang.String ycartPoint) {
        this.ycartPoint = ycartPoint;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TestBean)) return false;
        TestBean other = (TestBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.latPoint==null && other.getLatPoint()==null) || 
             (this.latPoint!=null &&
              this.latPoint.equals(other.getLatPoint()))) &&
            ((this.lonPoint==null && other.getLonPoint()==null) || 
             (this.lonPoint!=null &&
              this.lonPoint.equals(other.getLonPoint()))) &&
            ((this.xcartPoint==null && other.getXcartPoint()==null) || 
             (this.xcartPoint!=null &&
              this.xcartPoint.equals(other.getXcartPoint()))) &&
            ((this.ycartPoint==null && other.getYcartPoint()==null) || 
             (this.ycartPoint!=null &&
              this.ycartPoint.equals(other.getYcartPoint())));
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
        if (getLatPoint() != null) {
            _hashCode += getLatPoint().hashCode();
        }
        if (getLonPoint() != null) {
            _hashCode += getLonPoint().hashCode();
        }
        if (getXcartPoint() != null) {
            _hashCode += getXcartPoint().hashCode();
        }
        if (getYcartPoint() != null) {
            _hashCode += getYcartPoint().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TestBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:DislocExtendedService", "TestBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("latPoint");
        elemField.setXmlName(new javax.xml.namespace.QName("", "latPoint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lonPoint");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lonPoint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xcartPoint");
        elemField.setXmlName(new javax.xml.namespace.QName("", "xcartPoint"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ycartPoint");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ycartPoint"));
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
