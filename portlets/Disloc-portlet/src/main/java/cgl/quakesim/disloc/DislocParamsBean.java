/**
 * DislocParamsBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.disloc;

public class DislocParamsBean  implements java.io.Serializable {

    private double gridMinXValue=-20.0;

    private double gridMinYValue=-20.0;

    private int gridXIterations=5;

    private double gridXSpacing=10.0;

    private int gridYIterations=5;

    private double gridYSpacing=-10.;

    private int observationPointStyle;

	 //These are some impossible values that are used to indicate the proper values have not yet
	 //been set.
	 static double DEFAULT_LON=-9999.99;
	 static double DEFAULT_LAT=-9999.99;
	 
	 double originLon=DEFAULT_LON;
	 double originLat=DEFAULT_LAT;

//     private double originLat;

//     private double originLon;

    public DislocParamsBean() {
    }

    public DislocParamsBean(
           double gridMinXValue,
           double gridMinYValue,
           int gridXIterations,
           double gridXSpacing,
           int gridYIterations,
           double gridYSpacing,
           int observationPointStyle,
           double originLat,
           double originLon) {
           this.gridMinXValue = gridMinXValue;
           this.gridMinYValue = gridMinYValue;
           this.gridXIterations = gridXIterations;
           this.gridXSpacing = gridXSpacing;
           this.gridYIterations = gridYIterations;
           this.gridYSpacing = gridYSpacing;
           this.observationPointStyle = observationPointStyle;
           this.originLat = originLat;
           this.originLon = originLon;
    }


    /**
     * Gets the gridMinXValue value for this DislocParamsBean.
     * 
     * @return gridMinXValue
     */
    public double getGridMinXValue() {
        return gridMinXValue;
    }


    /**
     * Sets the gridMinXValue value for this DislocParamsBean.
     * 
     * @param gridMinXValue
     */
    public void setGridMinXValue(double gridMinXValue) {
        this.gridMinXValue = gridMinXValue;
    }


    /**
     * Gets the gridMinYValue value for this DislocParamsBean.
     * 
     * @return gridMinYValue
     */
    public double getGridMinYValue() {
        return gridMinYValue;
    }


    /**
     * Sets the gridMinYValue value for this DislocParamsBean.
     * 
     * @param gridMinYValue
     */
    public void setGridMinYValue(double gridMinYValue) {
        this.gridMinYValue = gridMinYValue;
    }


    /**
     * Gets the gridXIterations value for this DislocParamsBean.
     * 
     * @return gridXIterations
     */
    public int getGridXIterations() {
        return gridXIterations;
    }


    /**
     * Sets the gridXIterations value for this DislocParamsBean.
     * 
     * @param gridXIterations
     */
    public void setGridXIterations(int gridXIterations) {
        this.gridXIterations = gridXIterations;
    }


    /**
     * Gets the gridXSpacing value for this DislocParamsBean.
     * 
     * @return gridXSpacing
     */
    public double getGridXSpacing() {
        return gridXSpacing;
    }


    /**
     * Sets the gridXSpacing value for this DislocParamsBean.
     * 
     * @param gridXSpacing
     */
    public void setGridXSpacing(double gridXSpacing) {
        this.gridXSpacing = gridXSpacing;
    }


    /**
     * Gets the gridYIterations value for this DislocParamsBean.
     * 
     * @return gridYIterations
     */
    public int getGridYIterations() {
        return gridYIterations;
    }


    /**
     * Sets the gridYIterations value for this DislocParamsBean.
     * 
     * @param gridYIterations
     */
    public void setGridYIterations(int gridYIterations) {
        this.gridYIterations = gridYIterations;
    }


    /**
     * Gets the gridYSpacing value for this DislocParamsBean.
     * 
     * @return gridYSpacing
     */
    public double getGridYSpacing() {
        return gridYSpacing;
    }


    /**
     * Sets the gridYSpacing value for this DislocParamsBean.
     * 
     * @param gridYSpacing
     */
    public void setGridYSpacing(double gridYSpacing) {
        this.gridYSpacing = gridYSpacing;
    }


    /**
     * Gets the observationPointStyle value for this DislocParamsBean.
     * 
     * @return observationPointStyle
     */
    public int getObservationPointStyle() {
        return observationPointStyle;
    }


    /**
     * Sets the observationPointStyle value for this DislocParamsBean.
     * 
     * @param observationPointStyle
     */
    public void setObservationPointStyle(int observationPointStyle) {
        this.observationPointStyle = observationPointStyle;
    }


    /**
     * Gets the originLat value for this DislocParamsBean.
     * 
     * @return originLat
     */
    public double getOriginLat() {
        return originLat;
    }


    /**
     * Sets the originLat value for this DislocParamsBean.
     * 
     * @param originLat
     */
    public void setOriginLat(double originLat) {
        this.originLat = originLat;
    }


    /**
     * Gets the originLon value for this DislocParamsBean.
     * 
     * @return originLon
     */
    public double getOriginLon() {
        return originLon;
    }


    /**
     * Sets the originLon value for this DislocParamsBean.
     * 
     * @param originLon
     */
    public void setOriginLon(double originLon) {
        this.originLon = originLon;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DislocParamsBean)) return false;
        DislocParamsBean other = (DislocParamsBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.gridMinXValue == other.getGridMinXValue() &&
            this.gridMinYValue == other.getGridMinYValue() &&
            this.gridXIterations == other.getGridXIterations() &&
            this.gridXSpacing == other.getGridXSpacing() &&
            this.gridYIterations == other.getGridYIterations() &&
            this.gridYSpacing == other.getGridYSpacing() &&
            this.observationPointStyle == other.getObservationPointStyle() &&
            this.originLat == other.getOriginLat() &&
            this.originLon == other.getOriginLon();
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
        _hashCode += new Double(getGridMinXValue()).hashCode();
        _hashCode += new Double(getGridMinYValue()).hashCode();
        _hashCode += getGridXIterations();
        _hashCode += new Double(getGridXSpacing()).hashCode();
        _hashCode += getGridYIterations();
        _hashCode += new Double(getGridYSpacing()).hashCode();
        _hashCode += getObservationPointStyle();
        _hashCode += new Double(getOriginLat()).hashCode();
        _hashCode += new Double(getOriginLon()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DislocParamsBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:DislocService", "DislocParamsBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gridMinXValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gridMinXValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gridMinYValue");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gridMinYValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gridXIterations");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gridXIterations"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gridXSpacing");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gridXSpacing"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gridYIterations");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gridYIterations"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gridYSpacing");
        elemField.setXmlName(new javax.xml.namespace.QName("", "gridYSpacing"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("observationPointStyle");
        elemField.setXmlName(new javax.xml.namespace.QName("", "observationPointStyle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originLat");
        elemField.setXmlName(new javax.xml.namespace.QName("", "originLat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originLon");
        elemField.setXmlName(new javax.xml.namespace.QName("", "originLon"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
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
