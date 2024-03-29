/**
 * DislocExtendedService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.disloc;

public interface DislocExtendedService extends java.rmi.Remote {
    public void testCall(cgl.quakesim.disloc.ObsvPoint[] testBean) throws java.rmi.RemoteException;
    public cgl.quakesim.disloc.DislocResultsBean runNonBlockingDislocExt(java.lang.String userName, java.lang.String projectName, cgl.quakesim.disloc.ObsvPoint[] obsvPoints, cgl.quakesim.disloc.Fault[] faults, cgl.quakesim.disloc.DislocParamsBean dislocParams, java.lang.String targetName) throws java.rmi.RemoteException;
    public cgl.quakesim.disloc.DislocResultsBean runBlockingDislocExt(java.lang.String userName, java.lang.String projectName, cgl.quakesim.disloc.ObsvPoint[] obsvPoints, cgl.quakesim.disloc.Fault[] faults, cgl.quakesim.disloc.DislocParamsBean dislocParams, java.lang.String targetName) throws java.rmi.RemoteException;
}
