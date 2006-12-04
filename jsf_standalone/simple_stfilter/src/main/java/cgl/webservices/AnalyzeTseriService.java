/**
 * AnalyzeTseriService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.webservices;

public interface AnalyzeTseriService extends java.rmi.Remote {
    public java.lang.String[] runBlockingAnalyzeTseri(java.lang.String siteCode, java.lang.String beginDate, java.lang.String endDate) throws java.rmi.RemoteException;
    public java.lang.String[] runBlockingAnalyzeTseri(java.lang.String inputFileUrlString) throws java.rmi.RemoteException;
    public java.lang.String[] runBlockingAnalyzeTseri(java.lang.String inputFileUrlString, java.lang.String baseWorkDir, java.lang.String outputDestDir, java.lang.String projectName, java.lang.String binPath, java.lang.String buildFilePath, java.lang.String antTarget) throws java.rmi.RemoteException;
}
