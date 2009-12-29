/**
 * QueueService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.geofest;

public interface QueueService extends java.rmi.Remote {
    public void createQueue(java.lang.String queueBaseName) throws java.rmi.RemoteException;
    public void writeQueueMessage(java.lang.String queueBaseName, java.lang.String message) throws java.rmi.RemoteException;
    public void deleteQueue(java.lang.String queueBaseName) throws java.rmi.RemoteException;
    public java.lang.String readQueueMessage(java.lang.String queueBaseName) throws java.rmi.RemoteException;
    public java.lang.String getQueueUpdateTime(java.lang.String queueBaseName) throws java.rmi.RemoteException;
}
