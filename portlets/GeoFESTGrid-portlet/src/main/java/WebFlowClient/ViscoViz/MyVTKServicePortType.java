/**
 * MyVTKServicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package WebFlowClient.ViscoViz;

public interface MyVTKServicePortType extends java.rmi.Remote {

    // Service definition of function impl__addFault
    public void addFault(java.lang.String in0, float in1, float in2, float in3, float in4, float in5, float in6, float in7) throws java.rmi.RemoteException;

    // Service definition of function impl__zoomIn
    public void zoomIn(float in0, float in1, float in2) throws java.rmi.RemoteException;

    // Service definition of function impl__removeFault
    public void removeFault(java.lang.String in0) throws java.rmi.RemoteException;

    // Service definition of function impl__plot
    public void plot(javax.xml.rpc.holders.ByteArrayHolder plotReturn, javax.xml.rpc.holders.IntHolder width, javax.xml.rpc.holders.IntHolder height) throws java.rmi.RemoteException;

    // Service definition of function impl__mesh
    public void mesh(int m, java.lang.String n, javax.xml.rpc.holders.ByteArrayHolder meshReturn, javax.xml.rpc.holders.IntHolder width, javax.xml.rpc.holders.IntHolder height) throws java.rmi.RemoteException;

    // Service definition of function impl__rotate
    public void rotate(float in0, float in1, float in2) throws java.rmi.RemoteException;

    // Service definition of function impl__addLayer
    public void addLayer(java.lang.String in0, float in1, float in2, float in3, float in4, float in5, float in6) throws java.rmi.RemoteException;

    // Service definition of function impl__exit
    public void exit() throws java.rmi.RemoteException;

    // Service definition of function impl__removeLayer
    public void removeLayer(java.lang.String in0) throws java.rmi.RemoteException;

    // Service definition of function impl__setWindow
    public void setWindow(int w, int h) throws java.rmi.RemoteException;
}
