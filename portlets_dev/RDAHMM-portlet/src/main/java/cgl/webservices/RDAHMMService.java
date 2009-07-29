/**
 * RDAHMMService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.webservices;

public interface RDAHMMService extends java.rmi.Remote {
    public java.lang.String[] runNonblockingRDAHMM(java.lang.String siteCode, java.lang.String beginDate, java.lang.String endDate, int numModelStates) throws java.rmi.RemoteException;
    public java.lang.String[] runNonblockingRDAHMM(java.lang.String siteCode, java.lang.String resource, java.lang.String contextGroup, java.lang.String contextId, java.lang.String minMaxLatLon, java.lang.String beginDate, java.lang.String endDate, int numModelStates) throws java.rmi.RemoteException;
    public java.lang.String[] runNonblockingRDAHMM(java.lang.String inputFileUrlString, int numModelStates) throws java.rmi.RemoteException;
    public java.lang.String[] runNonblockingRDAHMM(java.lang.String inputFileUrlString, java.lang.String baseWorkDir, java.lang.String outputDestDir, java.lang.String projectName, java.lang.String binPath, int numModelStates, int randomSeed, java.lang.String outputType, double annealStep, java.lang.String buildFilePath, java.lang.String antTarget) throws java.rmi.RemoteException;
    public cgl.webservices.RDAHMMResultsBean runNonblockingRDAHMM2(java.lang.String siteCode, java.lang.String beginDate, java.lang.String endDate, int numModelStates) throws java.rmi.RemoteException;
    public cgl.webservices.RDAHMMResultsBean runNonblockingRDAHMM2(java.lang.String siteCode, java.lang.String resource, java.lang.String contextGroup, java.lang.String contextId, java.lang.String minMaxLatLon, java.lang.String beginDate, java.lang.String endDate, int numModelStates) throws java.rmi.RemoteException;
    public cgl.webservices.RDAHMMResultsBean runNonblockingRDAHMM2(java.lang.String inputFileUrlString, int numModelStates) throws java.rmi.RemoteException;
    public cgl.webservices.RDAHMMResultsBean runNonblockingRDAHMM2(java.lang.String inputFileUrlString, java.lang.String baseWorkDir, java.lang.String outputDestDir, java.lang.String projectName, java.lang.String binPath, int numModelStates, int randomSeed, java.lang.String outputType, double annealStep, java.lang.String buildFilePath, java.lang.String antTarget) throws java.rmi.RemoteException;
    public java.lang.String[] runBlockingRDAHMM(java.lang.String siteCode, java.lang.String beginDate, java.lang.String endDate, int numModelStates) throws java.rmi.RemoteException;
    public java.lang.String[] runBlockingRDAHMM(java.lang.String siteCode, java.lang.String resource, java.lang.String contextGroup, java.lang.String contextId, java.lang.String minMaxLatLon, java.lang.String beginDate, java.lang.String endDate, int numModelStates) throws java.rmi.RemoteException;
    public java.lang.String[] runBlockingRDAHMM(java.lang.String inputFileUrlString, int numModelStates) throws java.rmi.RemoteException;
    public java.lang.String[] runBlockingRDAHMM(java.lang.String inputFileUrlString, java.lang.String baseWorkDir, java.lang.String outputDestDir, java.lang.String projectName, java.lang.String binPath, int numModelStates, int randomSeed, java.lang.String outputType, double annealStep, java.lang.String buildFilePath, java.lang.String antTarget) throws java.rmi.RemoteException;
    public cgl.webservices.RDAHMMResultsBean runBlockingRDAHMM2(java.lang.String siteCode, java.lang.String beginDate, java.lang.String endDate, int numModelStates) throws java.rmi.RemoteException;
    public cgl.webservices.RDAHMMResultsBean runBlockingRDAHMM2(java.lang.String siteCode, java.lang.String resource, java.lang.String contextGroup, java.lang.String contextId, java.lang.String minMaxLatLon, java.lang.String beginDate, java.lang.String endDate, int numModelStates) throws java.rmi.RemoteException;
    public cgl.webservices.RDAHMMResultsBean runBlockingRDAHMM2(java.lang.String inputFileUrlString, int numModelStates) throws java.rmi.RemoteException;
    public cgl.webservices.RDAHMMResultsBean runBlockingRDAHMM2(java.lang.String inputFileUrlString, java.lang.String baseWorkDir, java.lang.String outputDestDir, java.lang.String projectName, java.lang.String binPath, int numModelStates, int randomSeed, java.lang.String outputType, double annealStep, java.lang.String buildFilePath, java.lang.String antTarget) throws java.rmi.RemoteException;
}
