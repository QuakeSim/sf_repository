/**
 * GridInfoService_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.geofest;

public interface GridInfoService_PortType extends java.rmi.Remote {
    public java.lang.String[] getHosts() throws java.rmi.RemoteException;
    public java.lang.String getUserName(java.lang.String host) throws java.rmi.RemoteException;
    public java.lang.String getHomeDirectory(java.lang.String host) throws java.rmi.RemoteException;
    public java.lang.String getJobManager(java.lang.String host) throws java.rmi.RemoteException;
    public java.lang.String getForkManager(java.lang.String host) throws java.rmi.RemoteException;
}
