/**
 * QueueServiceService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.webservices.geofest;

public interface QueueServiceService extends javax.xml.rpc.Service {
    public java.lang.String getQueueExecAddress();

    public cgl.webservices.geofest.QueueService getQueueExec() throws javax.xml.rpc.ServiceException;

    public cgl.webservices.geofest.QueueService getQueueExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
