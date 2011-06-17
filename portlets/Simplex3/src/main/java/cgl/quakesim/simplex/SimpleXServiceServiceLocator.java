/**
 * SimpleXServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.simplex;

public class SimpleXServiceServiceLocator extends org.apache.axis.client.Service implements cgl.quakesim.simplex.SimpleXServiceService {

    public SimpleXServiceServiceLocator() {
    }


    public SimpleXServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SimpleXServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SimpleXExec
    private java.lang.String SimpleXExec_address = "http://gf19.ucs.indiana.edu:8080/simplexexec/services/SimpleXExec";

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

    public cgl.quakesim.simplex.SimpleXService getSimpleXExec() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SimpleXExec_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSimpleXExec(endpoint);
    }

    public cgl.quakesim.simplex.SimpleXService getSimpleXExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cgl.quakesim.simplex.SimpleXExecSoapBindingStub _stub = new cgl.quakesim.simplex.SimpleXExecSoapBindingStub(portAddress, this);
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
            if (cgl.quakesim.simplex.SimpleXService.class.isAssignableFrom(serviceEndpointInterface)) {
                cgl.quakesim.simplex.SimpleXExecSoapBindingStub _stub = new cgl.quakesim.simplex.SimpleXExecSoapBindingStub(new java.net.URL(SimpleXExec_address), this);
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
        return new javax.xml.namespace.QName("http://gf19.ucs.indiana.edu:8080/simplexexec/services/SimpleXExec", "SimpleXServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://gf19.ucs.indiana.edu:8080/simplexexec/services/SimpleXExec", "SimpleXExec"));
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
