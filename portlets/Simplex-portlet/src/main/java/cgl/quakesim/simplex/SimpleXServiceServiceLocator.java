/**
 * SimpleXServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package cgl.quakesim.simplex;

public class SimpleXServiceServiceLocator extends org.apache.axis.client.Service implements SimpleXServiceService {

    public SimpleXServiceServiceLocator() {
    }


    public SimpleXServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SimpleXServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SimpleXExec
    private java.lang.String SimpleXExec_address = "http://gf1.ucs.indiana.edu:13080/simplexexec/services/SimpleXExec";

    public java.lang.String getSimpleXExecAddress() {
        return SimpleXExec_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SimpleXExecWSDDServiceName = "SimpleXExec";

    public java.lang.String getSimpleXExecWSDDServiceName() {
        return SimpleXExecWSDDServiceName;
    }

    public void setSimpleXExecWSDDServiceName(java.lang.String name) {
        SimpleXExecWSDDServiceName = name;
    }

    public SimpleXService getSimpleXExec() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SimpleXExec_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSimpleXExec(endpoint);
    }

    public SimpleXService getSimpleXExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            SimpleXExecSoapBindingStub _stub = new SimpleXExecSoapBindingStub(portAddress, this);
            _stub.setPortName(getSimpleXExecWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSimpleXExecEndpointAddress(java.lang.String address) {
        SimpleXExec_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SimpleXService.class.isAssignableFrom(serviceEndpointInterface)) {
                SimpleXExecSoapBindingStub _stub = new SimpleXExecSoapBindingStub(new java.net.URL(SimpleXExec_address), this);
                _stub.setPortName(getSimpleXExecWSDDServiceName());
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
        if ("SimpleXExec".equals(inputPortName)) {
            return getSimpleXExec();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://gf1.ucs.indiana.edu:13080/simplexexec/services/SimpleXExec", "SimpleXServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://gf1.ucs.indiana.edu:13080/simplexexec/services/SimpleXExec", "SimpleXExec"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("SimpleXExec".equals(portName)) {
            setSimpleXExecEndpointAddress(address);
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
