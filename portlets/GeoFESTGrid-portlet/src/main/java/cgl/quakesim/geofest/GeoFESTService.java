/**
 * GeoFESTService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.geofest;

public interface GeoFESTService extends java.rmi.Remote {
    public cgl.quakesim.geofest.MeshRunBean runBlockingMeshGenerator(java.lang.String userName, java.lang.String projectName, cgl.quakesim.geofest.Fault[] faults, cgl.quakesim.geofest.Layer[] layers, java.lang.String autoref_mode) throws java.rmi.RemoteException;
    public cgl.quakesim.geofest.MeshRunBean runNonBlockingMeshGenerator(java.lang.String userName, java.lang.String projectName, cgl.quakesim.geofest.Fault[] faults, cgl.quakesim.geofest.Layer[] layers, java.lang.String autoref_mode) throws java.rmi.RemoteException;
    public void queryMeshGeneratorStatus() throws java.rmi.RemoteException;
    public java.lang.String[] runPackageGeoFESTFiles(java.lang.String userName, java.lang.String projectName, cgl.quakesim.geofest.GeotransParamsBean gpb, java.lang.String timeStamp) throws java.rmi.RemoteException;
    public cgl.quakesim.geofest.GFOutputBean runGeoFEST(java.lang.String userName, java.lang.String projectName, cgl.quakesim.geofest.GeotransParamsBean gpb, java.lang.String timeStamp) throws java.rmi.RemoteException;
    public void queryGeoFESTStatus() throws java.rmi.RemoteException;
}
