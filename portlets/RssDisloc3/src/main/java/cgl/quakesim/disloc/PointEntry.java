/**
 * PointEntry.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.disloc;

public class PointEntry  implements java.io.Serializable {
    private java.lang.String deltaXName;

    private java.lang.String deltaXValue;

    private java.lang.String deltaYName;

    private java.lang.String deltaYValue;

    private java.lang.String deltaZName;

    private java.lang.String deltaZValue;

    private java.lang.String folderTag;

    private java.lang.String x;

    private java.lang.String y;

    public PointEntry() {
    }

    public PointEntry(
           java.lang.String deltaXName,
           java.lang.String deltaXValue,
           java.lang.String deltaYName,
           java.lang.String deltaYValue,
           java.lang.String deltaZName,
           java.lang.String deltaZValue,
           java.lang.String folderTag,
           java.lang.String x,
           java.lang.String y) {
           this.deltaXName = deltaXName;
           this.deltaXValue = deltaXValue;
           this.deltaYName = deltaYName;
           this.deltaYValue = deltaYValue;
           this.deltaZName = deltaZName;
           this.deltaZValue = deltaZValue;
           this.folderTag = folderTag;
           this.x = x;
           this.y = y;
    }


    /**
     * Gets the deltaXName value for this PointEntry.
     * 
     * @return deltaXName
     */
    public java.lang.String getDeltaXName() {
        return deltaXName;
    }


    /**
     * Sets the deltaXName value for this PointEntry.
     * 
     * @param deltaXName
     */
    public void setDeltaXName(java.lang.String deltaXName) {
        this.deltaXName = deltaXName;
    }


    /**
     * Gets the deltaXValue value for this PointEntry.
     * 
     * @return deltaXValue
     */
    public java.lang.String getDeltaXValue() {
        return deltaXValue;
    }


    /**
     * Sets the deltaXValue value for this PointEntry.
     * 
     * @param deltaXValue
     */
    public void setDeltaXValue(java.lang.String deltaXValue) {
        this.deltaXValue = deltaXValue;
    }


    /**
     * Gets the deltaYName value for this PointEntry.
     * 
     * @return deltaYName
     */
    public java.lang.String getDeltaYName() {
        return deltaYName;
    }


    /**
     * Sets the deltaYName value for this PointEntry.
     * 
     * @param deltaYName
     */
    public void setDeltaYName(java.lang.String deltaYName) {
        this.deltaYName = deltaYName;
    }


    /**
     * Gets the deltaYValue value for this PointEntry.
     * 
     * @return deltaYValue
     */
    public java.lang.String getDeltaYValue() {
        return deltaYValue;
    }


    /**
     * Sets the deltaYValue value for this PointEntry.
     * 
     * @param deltaYValue
     */
    public void setDeltaYValue(java.lang.String deltaYValue) {
        this.deltaYValue = deltaYValue;
    }


    /**
     * Gets the deltaZName value for this PointEntry.
     * 
     * @return deltaZName
     */
    public java.lang.String getDeltaZName() {
        return deltaZName;
    }


    /**
     * Sets the deltaZName value for this PointEntry.
     * 
     * @param deltaZName
     */
    public void setDeltaZName(java.lang.String deltaZName) {
        this.deltaZName = deltaZName;
    }


    /**
     * Gets the deltaZValue value for this PointEntry.
     * 
     * @return deltaZValue
     */
    public java.lang.String getDeltaZValue() {
        return deltaZValue;
    }


    /**
     * Sets the deltaZValue value for this PointEntry.
     * 
     * @param deltaZValue
     */
    public void setDeltaZValue(java.lang.String deltaZValue) {
        this.deltaZValue = deltaZValue;
    }


    /**
     * Gets the folderTag value for this PointEntry.
     * 
     * @return folderTag
     */
    public java.lang.String getFolderTag() {
        return folderTag;
    }


    /**
     * Sets the folderTag value for this PointEntry.
     * 
     * @param folderTag
     */
    public void setFolderTag(java.lang.String folderTag) {
        this.folderTag = folderTag;
    }


    /**
     * Gets the x value for this PointEntry.
     * 
     * @return x
     */
    public java.lang.String getX() {
        return x;
    }


    /**
     * Sets the x value for this PointEntry.
     * 
     * @param x
     */
    public void setX(java.lang.String x) {
        this.x = x;
    }


    /**
     * Gets the y value for this PointEntry.
     * 
     * @return y
     */
    public java.lang.String getY() {
        return y;
    }


    /**
     * Sets the y value for this PointEntry.
     * 
     * @param y
     */
    public void setY(java.lang.String y) {
        this.y = y;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PointEntry)) return false;
        PointEntry other = (PointEntry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.deltaXName==null && other.getDeltaXName()==null) || 
             (this.deltaXName!=null &&
              this.deltaXName.equals(other.getDeltaXName()))) &&
            ((this.deltaXValue==null && other.getDeltaXValue()==null) || 
             (this.deltaXValue!=null &&
              this.deltaXValue.equals(other.getDeltaXValue()))) &&
            ((this.deltaYName==null && other.getDeltaYName()==null) || 
             (this.deltaYName!=null &&
              this.deltaYName.equals(other.getDeltaYName()))) &&
            ((this.deltaYValue==null && other.getDeltaYValue()==null) || 
             (this.deltaYValue!=null &&
              this.deltaYValue.equals(other.getDeltaYValue()))) &&
            ((this.deltaZName==null && other.getDeltaZName()==null) || 
             (this.deltaZName!=null &&
              this.deltaZName.equals(other.getDeltaZName()))) &&
            ((this.deltaZValue==null && other.getDeltaZValue()==null) || 
             (this.deltaZValue!=null &&
              this.deltaZValue.equals(other.getDeltaZValue()))) &&
            ((this.folderTag==null && other.getFolderTag()==null) || 
             (this.folderTag!=null &&
              this.folderTag.equals(other.getFolderTag()))) &&
            ((this.x==null && other.getX()==null) || 
             (this.x!=null &&
              this.x.equals(other.getX()))) &&
            ((this.y==null && other.getY()==null) || 
             (this.y!=null &&
              this.y.equals(other.getY())));
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
        if (getDeltaXName() != null) {
            _hashCode += getDeltaXName().hashCode();
        }
        if (getDeltaXValue() != null) {
            _hashCode += getDeltaXValue().hashCode();
        }
        if (getDeltaYName() != null) {
            _hashCode += getDeltaYName().hashCode();
        }
        if (getDeltaYValue() != null) {
            _hashCode += getDeltaYValue().hashCode();
        }
        if (getDeltaZName() != null) {
            _hashCode += getDeltaZName().hashCode();
        }
        if (getDeltaZValue() != null) {
            _hashCode += getDeltaZValue().hashCode();
        }
        if (getFolderTag() != null) {
            _hashCode += getFolderTag().hashCode();
        }
        if (getX() != null) {
            _hashCode += getX().hashCode();
        }
        if (getY() != null) {
            _hashCode += getY().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PointEntry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:SimpleXDataKml", "PointEntry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deltaXName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "deltaXName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deltaXValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "deltaXValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deltaYName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "deltaYName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deltaYValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "deltaYValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deltaZName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "deltaZName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deltaZValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "deltaZValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("folderTag");
        elemField.setXmlName(new javax.xml.namespace.QName("", "folderTag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("x");
        elemField.setXmlName(new javax.xml.namespace.QName("", "x"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("y");
        elemField.setXmlName(new javax.xml.namespace.QName("", "y"));
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
