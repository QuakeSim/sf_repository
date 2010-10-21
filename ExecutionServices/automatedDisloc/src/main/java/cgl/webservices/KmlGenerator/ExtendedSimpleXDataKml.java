/**
 * ExtendedSimpleXDataKml.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package cgl.webservices.KmlGenerator;

public interface ExtendedSimpleXDataKml extends java.rmi.Remote {
    public void setFaultPlot(java.lang.String folderName, java.lang.String faultName, java.lang.String lonstart, java.lang.String latstart, java.lang.String lonend, java.lang.String latend, java.lang.String lineColor, double lineWidth) throws java.rmi.RemoteException;
    public void setOriginalCoordinate(java.lang.String lon, java.lang.String lat) throws java.rmi.RemoteException;
    public void setCoordinateUnit(java.lang.String unit) throws java.rmi.RemoteException;
    public java.lang.String runMakeSubKmls(cgl.webservices.KmlGenerator.PointEntry[] inputDataList, java.lang.String serverTag, java.lang.String userName, java.lang.String projectName, java.lang.String jobUID) throws java.rmi.RemoteException;
}
