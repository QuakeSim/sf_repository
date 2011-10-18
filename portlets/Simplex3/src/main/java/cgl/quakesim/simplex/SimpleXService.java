/**
 * SimpleXService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.simplex;

public interface SimpleXService extends java.rmi.Remote {
    public java.lang.String runMakeMapXml(java.lang.String userName, java.lang.String projectName, java.lang.String origin_lat, java.lang.String origin_lon, java.lang.String jobUIDStamp) throws java.rmi.RemoteException;
    public cgl.quakesim.simplex.SimpleXOutputBean runSimplex(java.lang.String userName, java.lang.String projectName, cgl.quakesim.simplex.Fault[] faults, cgl.quakesim.simplex.Observation[] obsv, java.lang.String startTemp, java.lang.String maxIters, java.lang.String origin_lon, java.lang.String origin_lat, java.lang.String kmlGeneratorUrl, java.lang.String timeStamp) throws java.rmi.RemoteException;
    public cgl.quakesim.simplex.SimpleXOutputBean runSimplex(java.lang.String userName, java.lang.String projectName, cgl.quakesim.simplex.Fault[] faults, cgl.quakesim.simplex.Observation[] obsv, java.lang.String startTemp, java.lang.String maxIters, java.lang.String origin_lon, java.lang.String origin_lat, java.lang.String kmlGeneratorUrl, java.lang.String timeStamp, java.lang.String emailAddress) throws java.rmi.RemoteException;
    public cgl.quakesim.simplex.GMTViewForm runPlotGMT(java.lang.String userName, java.lang.String projectName, java.lang.String origin_lat, java.lang.String origin_lon, java.lang.String timeStamp) throws java.rmi.RemoteException;
    public java.lang.String runRePlotGMT(java.lang.String userName, java.lang.String projectName, cgl.quakesim.simplex.GMTViewForm currentGMTViewForm, java.lang.String jobUIDStamp) throws java.rmi.RemoteException;
    public cgl.quakesim.simplex.SimpleXOutputBean runBlockingSimplex(java.lang.String userName, java.lang.String projectName, cgl.quakesim.simplex.Fault[] faults, cgl.quakesim.simplex.Observation[] obsv, java.lang.String startTemp, java.lang.String maxIters, java.lang.String origin_lon, java.lang.String origin_lat, java.lang.String kmlGeneratorUrl, java.lang.String timeStamp, java.lang.String emailAddress) throws java.rmi.RemoteException;
}
