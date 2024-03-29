/**
 * XYPoint.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.disloc;

public class XYPoint  implements java.io.Serializable {
    private double lat;

    private double lon;

    private double x;

    private double y;

    public XYPoint() {
    }

    public XYPoint(
           double lat,
           double lon,
           double x,
           double y) {
           this.lat = lat;
           this.lon = lon;
           this.x = x;
           this.y = y;
    }


    /**
     * Gets the lat value for this XYPoint.
     * 
     * @return lat
     */
    public double getLat() {
        return lat;
    }


    /**
     * Sets the lat value for this XYPoint.
     * 
     * @param lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }


    /**
     * Gets the lon value for this XYPoint.
     * 
     * @return lon
     */
    public double getLon() {
        return lon;
    }


    /**
     * Sets the lon value for this XYPoint.
     * 
     * @param lon
     */
    public void setLon(double lon) {
        this.lon = lon;
    }


    /**
     * Gets the x value for this XYPoint.
     * 
     * @return x
     */
    public double getX() {
        return x;
    }


    /**
     * Sets the x value for this XYPoint.
     * 
     * @param x
     */
    public void setX(double x) {
        this.x = x;
    }


    /**
     * Gets the y value for this XYPoint.
     * 
     * @return y
     */
    public double getY() {
        return y;
    }


    /**
     * Sets the y value for this XYPoint.
     * 
     * @param y
     */
    public void setY(double y) {
        this.y = y;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof XYPoint)) return false;
        XYPoint other = (XYPoint) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.lat == other.getLat() &&
            this.lon == other.getLon() &&
            this.x == other.getX() &&
            this.y == other.getY();
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
        _hashCode += new Double(getLat()).hashCode();
        _hashCode += new Double(getLon()).hashCode();
        _hashCode += new Double(getX()).hashCode();
        _hashCode += new Double(getY()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(XYPoint.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:DislocExtendedService", "XYPoint"));
        org.apache.axis.description.ElementDesc elemField1 = new org.apache.axis.description.ElementDesc();        
	org.apache.axis.description.ElementDesc elemField2 = new org.apache.axis.description.ElementDesc();
        org.apache.axis.description.ElementDesc elemField3 = new org.apache.axis.description.ElementDesc();
        org.apache.axis.description.ElementDesc elemField4 = new org.apache.axis.description.ElementDesc();

        elemField1.setFieldName("lon");
        elemField1.setXmlName(new javax.xml.namespace.QName("", "lon"));
        elemField1.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField1.setNillable(false);
        typeDesc.addFieldDesc(elemField1);

	//        elemField = new org.apache.axis.description.ElementDesc();
        elemField2.setFieldName("x");
        elemField2.setXmlName(new javax.xml.namespace.QName("", "x"));
        elemField2.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField2.setNillable(false);
        typeDesc.addFieldDesc(elemField2);

	//        elemField = new org.apache.axis.description.ElementDesc();
        elemField3.setFieldName("y");
        elemField3.setXmlName(new javax.xml.namespace.QName("", "y"));
        elemField3.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField3.setNillable(false);
        typeDesc.addFieldDesc(elemField3);

	//        elemField = new org.apache.axis.description.ElementDesc();
        elemField4.setFieldName("lat");
        elemField4.setXmlName(new javax.xml.namespace.QName("", "lat"));
        elemField4.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField4.setNillable(false);
        typeDesc.addFieldDesc(elemField4);
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
