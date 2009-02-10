/**
 * GeoFESTParallelService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.geofest;

public interface GeoFESTParallelService extends java.rmi.Remote {
    public cgl.quakesim.geofest.GFOutputBean runGridGeoFEST(java.lang.String userName, java.lang.String projectName, cgl.quakesim.geofest.GeotransParallelParamsBean gpb, java.lang.String exec, java.lang.String args, java.lang.String gridResourceVal, java.lang.String proxyLocation, java.lang.String envSettings, java.lang.String timeStamp) throws java.rmi.RemoteException;
    public cgl.quakesim.geofest.GFOutputBean runGridGeoFEST(java.lang.String userName, java.lang.String projectName, cgl.quakesim.geofest.GeotransParamsBean gpb, java.lang.String exec, java.lang.String args, java.lang.String gridResourceVal, java.lang.String proxyLocation, java.lang.String envSettings, java.lang.String timeStamp) throws java.rmi.RemoteException;
    public cgl.quakesim.geofest.MeshRunBean runGridMeshGenerator(java.lang.String userName, java.lang.String projectName, cgl.quakesim.geofest.Fault[] faults, cgl.quakesim.geofest.Layer[] layers, java.lang.String autoref_mode, java.lang.String proxyLocation, java.lang.String gridResourceVal, java.lang.String envSettings, java.lang.String meshExec) throws java.rmi.RemoteException;
    public java.lang.String queryGeoFESTStatus(java.lang.String userName, java.lang.String projectId, java.lang.String jobStamp) throws java.rmi.RemoteException;
    public java.lang.String queryGeoFESTStatus() throws java.rmi.RemoteException;
    public java.lang.String queryMeshGeneratorStatus(java.lang.String userName, java.lang.String projectId, java.lang.String jobStamp) throws java.rmi.RemoteException;
    public void queryMeshGeneratorStatus() throws java.rmi.RemoteException;
    public void deleteMeshGeneratorJob(java.lang.String userName, java.lang.String projectId, java.lang.String jobStamp) throws java.rmi.RemoteException;
    public void deleteGeoFESTJob(java.lang.String userName, java.lang.String projectId, java.lang.String jobStamp) throws java.rmi.RemoteException;
}
