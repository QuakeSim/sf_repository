/**
 * DislocServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.disloc;

public interface DislocServiceService extends javax.xml.rpc.Service {
    public java.lang.String getDislocExecAddress();

    public cgl.quakesim.disloc.DislocService getDislocExec() throws javax.xml.rpc.ServiceException;

    public cgl.quakesim.disloc.DislocService getDislocExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
