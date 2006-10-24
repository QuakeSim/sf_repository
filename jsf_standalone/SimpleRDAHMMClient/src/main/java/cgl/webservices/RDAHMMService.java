/**
 * RDAHMMService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.webservices;

public interface RDAHMMService extends java.rmi.Remote {
    public java.lang.String[] runBlockingRDAHMM(java.lang.String inputFileUrlString, int numModelStates) throws java.rmi.RemoteException;
    public java.lang.String[] runBlockingRDAHMM(java.lang.String inputFileUrlString, java.lang.String baseWorkDir, java.lang.String projectName, java.lang.String binPath, int numModelStates, int randomSeed, java.lang.String outputType, double annealStep, java.lang.String buildFilePath, java.lang.String antTarget) throws java.rmi.RemoteException;
    public java.lang.String[] runNonblockingRDAHMM(java.lang.String inputFileUrlString, int numModelStates) throws java.rmi.RemoteException;
    public java.lang.String[] runNonblockingRDAHMM(java.lang.String inputFileUrlString, java.lang.String baseWorkDir, java.lang.String projectName, java.lang.String binPath, int numModelStates, int randomSeed, java.lang.String outputType, double annealStep, java.lang.String buildFilePath, java.lang.String antTarget) throws java.rmi.RemoteException;
}
