/**
 * InsarKmlService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.disloc;

public interface InsarKmlService extends java.rmi.Remote {
    public java.lang.String runNonBlockingInsarKml(java.lang.String userName, java.lang.String projectName, java.lang.String dislocOutputUrl, java.lang.String elevation, java.lang.String azimuth, java.lang.String radarFrequency, java.lang.String targetName) throws java.rmi.RemoteException;
    public java.lang.String runBlockingInsarKml(java.lang.String userName, java.lang.String projectName, java.lang.String dislocOutputUrl, java.lang.String elevation, java.lang.String azimuth, java.lang.String radarFrequency, java.lang.String targetName) throws java.rmi.RemoteException;
}
