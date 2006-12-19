/**
 * GnuplotServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.webservices;

public class GnuplotServiceServiceLocator extends org.apache.axis.client.Service implements cgl.webservices.GnuplotServiceService {

    public GnuplotServiceServiceLocator() {
    }


    public GnuplotServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GnuplotServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GnuplotExec
    private java.lang.String GnuplotExec_address = "http://gf2.ucs.indiana.edu:8080/gnuplotexec/services/GnuplotExec";

    public java.lang.String getGnuplotExecAddress() {
        return GnuplotExec_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GnuplotExecWSDDServiceName = "GnuplotExec";

    public java.lang.String getGnuplotExecWSDDServiceName() {
        return GnuplotExecWSDDServiceName;
    }

    public void setGnuplotExecWSDDServiceName(java.lang.String name) {
        GnuplotExecWSDDServiceName = name;
    }

    public cgl.webservices.GnuplotService getGnuplotExec() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GnuplotExec_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGnuplotExec(endpoint);
    }

    public cgl.webservices.GnuplotService getGnuplotExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cgl.webservices.GnuplotExecSoapBindingStub _stub = new cgl.webservices.GnuplotExecSoapBindingStub(portAddress, this);
            _stub.setPortName(getGnuplotExecWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGnuplotExecEndpointAddress(java.lang.String address) {
        GnuplotExec_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cgl.webservices.GnuplotService.class.isAssignableFrom(serviceEndpointInterface)) {
                cgl.webservices.GnuplotExecSoapBindingStub _stub = new cgl.webservices.GnuplotExecSoapBindingStub(new java.net.URL(GnuplotExec_address), this);
                _stub.setPortName(getGnuplotExecWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("GnuplotExec".equals(inputPortName)) {
            return getGnuplotExec();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://gf2.ucs.indiana.edu:8080/gnuplotexec/services/GnuplotExec", "GnuplotServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://gf2.ucs.indiana.edu:8080/gnuplotexec/services/GnuplotExec", "GnuplotExec"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GnuplotExec".equals(portName)) {
            setGnuplotExecEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
