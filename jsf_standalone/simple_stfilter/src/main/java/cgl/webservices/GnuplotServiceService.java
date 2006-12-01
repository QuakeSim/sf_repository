/**
 * GnuplotServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.webservices;

public interface GnuplotServiceService extends javax.xml.rpc.Service {
    public java.lang.String getGnuplotExecAddress();

    public cgl.webservices.GnuplotService getGnuplotExec() throws javax.xml.rpc.ServiceException;

    public cgl.webservices.GnuplotService getGnuplotExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
