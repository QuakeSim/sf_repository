/**
 * DislocExtendedService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.disloc;

public interface DislocExtendedService extends java.rmi.Remote {
    public cgl.quakesim.disloc.DislocResultsBean runNonBlockingDisloc(java.lang.String userName, java.lang.String projectName, cgl.quakesim.disloc.Fault[] faults, cgl.quakesim.disloc.DislocParamsBean dislocParams, cgl.quakesim.disloc.XYPoint[] XYPoints, java.lang.String targetName) throws java.rmi.RemoteException;
    public cgl.quakesim.disloc.DislocResultsBean runNonBlockingDisloc(java.lang.String userName, java.lang.String projectName, cgl.quakesim.disloc.Fault[] faults, cgl.quakesim.disloc.DislocParamsBean dislocParams, java.lang.String targetName) throws java.rmi.RemoteException;
    public cgl.quakesim.disloc.DislocResultsBean runBlockingDisloc(java.lang.String userName, java.lang.String projectName, cgl.quakesim.disloc.Fault[] faults, cgl.quakesim.disloc.DislocParamsBean dislocParams, cgl.quakesim.disloc.XYPoint[] XYPoints, java.lang.String targetName) throws java.rmi.RemoteException;
    public cgl.quakesim.disloc.DislocResultsBean runBlockingDisloc(java.lang.String userName, java.lang.String projectName, cgl.quakesim.disloc.Fault[] faults, cgl.quakesim.disloc.DislocParamsBean dislocParams, java.lang.String targetName) throws java.rmi.RemoteException;
}
