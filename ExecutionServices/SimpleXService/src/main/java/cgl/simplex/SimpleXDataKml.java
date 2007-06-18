/**
 * SimpleXDataKml.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package cgl.webservices.simplex;

public interface SimpleXDataKml extends java.rmi.Remote {
    public void setDatalist(PointEntry[] inputDataList) throws java.rmi.RemoteException;
    public java.lang.String runMakeKml(java.lang.String serverTag, java.lang.String userName, java.lang.String projectName, java.lang.String jobUID) throws java.rmi.RemoteException;
    public void setGridLine(java.lang.String folderName, double start_x, double start_y, double end_x, double end_y, double interval) throws java.rmi.RemoteException;
    public void setPointPlacemark(java.lang.String folderName) throws java.rmi.RemoteException;
    public void setArrowPlacemark(java.lang.String folderName, java.lang.String lineColor, double lineWidth) throws java.rmi.RemoteException;
    public void setCoordinateUnit(java.lang.String unit) throws java.rmi.RemoteException;
    public void setFaultPlot(java.lang.String folderName, java.lang.String faultName, java.lang.String lonstart, java.lang.String latstart, java.lang.String lonend, java.lang.String latend, java.lang.String lineColor, double lineWidth) throws java.rmi.RemoteException;
    public void setOriginalCoordinate(java.lang.String lon, java.lang.String lat) throws java.rmi.RemoteException;
    public void setRefCoordinate(java.lang.String lon, java.lang.String lat) throws java.rmi.RemoteException;
}
