/**
 * DislocServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.disloc;

public class DislocServiceServiceLocator extends org.apache.axis.client.Service implements cgl.quakesim.disloc.DislocServiceService {

    public DislocServiceServiceLocator() {
    }


    public DislocServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DislocServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DislocExec
    private java.lang.String DislocExec_address = "http://localhost:8080/dislocexec/services/DislocExec";

    public java.lang.String getDislocExecAddress() {
        return DislocExec_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DislocExecWSDDServiceName = "DislocExec";

    public java.lang.String getDislocExecWSDDServiceName() {
        return DislocExecWSDDServiceName;
    }

    public void setDislocExecWSDDServiceName(java.lang.String name) {
        DislocExecWSDDServiceName = name;
    }

    public cgl.quakesim.disloc.DislocService getDislocExec() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DislocExec_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDislocExec(endpoint);
    }

    public cgl.quakesim.disloc.DislocService getDislocExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cgl.quakesim.disloc.DislocExecSoapBindingStub _stub = new cgl.quakesim.disloc.DislocExecSoapBindingStub(portAddress, this);
            _stub.setPortName(getDislocExecWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDislocExecEndpointAddress(java.lang.String address) {
        DislocExec_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cgl.quakesim.disloc.DislocService.class.isAssignableFrom(serviceEndpointInterface)) {
                cgl.quakesim.disloc.DislocExecSoapBindingStub _stub = new cgl.quakesim.disloc.DislocExecSoapBindingStub(new java.net.URL(DislocExec_address), this);
                _stub.setPortName(getDislocExecWSDDServiceName());
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
        if ("DislocExec".equals(inputPortName)) {
            return getDislocExec();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/dislocexec/services/DislocExec", "DislocServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/dislocexec/services/DislocExec", "DislocExec"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DislocExec".equals(portName)) {
            setDislocExecEndpointAddress(address);
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
