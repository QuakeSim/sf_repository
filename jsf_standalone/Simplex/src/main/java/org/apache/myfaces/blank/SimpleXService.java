/**
 * SimpleXService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package org.apache.myfaces.blank;

public interface SimpleXService extends java.rmi.Remote {
    public SimpleXOutputBean runSimplex(java.lang.String userName, java.lang.String projectName, Fault[] faults, Observation[] obsv, java.lang.String startTemp, java.lang.String maxIters, java.lang.String origin_lon, java.lang.String origin_lat, java.lang.String kmlGeneratorUrl, java.lang.String timeStamp) throws java.rmi.RemoteException;
    public GMTViewForm runPlotGMT(java.lang.String userName, java.lang.String projectName, java.lang.String origin_lat, java.lang.String origin_lon, java.lang.String timeStamp) throws java.rmi.RemoteException;
    public java.lang.String runMakeMapXml(java.lang.String userName, java.lang.String projectName, java.lang.String origin_lat, java.lang.String origin_lon, java.lang.String jobUIDStamp) throws java.rmi.RemoteException;
    public java.lang.String runRePlotGMT(java.lang.String userName, java.lang.String projectName, GMTViewForm currentGMTViewForm, java.lang.String jobUIDStamp) throws java.rmi.RemoteException;
}
