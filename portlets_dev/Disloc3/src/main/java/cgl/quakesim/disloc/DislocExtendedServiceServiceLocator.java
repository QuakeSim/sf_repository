/**
 * DislocExtendedServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.disloc;

public class DislocExtendedServiceServiceLocator extends org.apache.axis.client.Service implements cgl.quakesim.disloc.DislocExtendedServiceService {

    public DislocExtendedServiceServiceLocator() {
    }


    public DislocExtendedServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DislocExtendedServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DislocExtendedExec
    private java.lang.String DislocExtendedExec_address = "http://localhost:8080/dislocexec/services/DislocExtendedExec";

    public java.lang.String getDislocExtendedExecAddress() {
        return DislocExtendedExec_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DislocExtendedExecWSDDServiceName = "DislocExtendedExec";

    public java.lang.String getDislocExtendedExecWSDDServiceName() {
        return DislocExtendedExecWSDDServiceName;
    }

    public void setDislocExtendedExecWSDDServiceName(java.lang.String name) {
        DislocExtendedExecWSDDServiceName = name;
    }

    public cgl.quakesim.disloc.DislocExtendedService getDislocExtendedExec() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DislocExtendedExec_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDislocExtendedExec(endpoint);
    }

    public cgl.quakesim.disloc.DislocExtendedService getDislocExtendedExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cgl.quakesim.disloc.DislocExtendedExecSoapBindingStub _stub = new cgl.quakesim.disloc.DislocExtendedExecSoapBindingStub(portAddress, this);
            _stub.setPortName(getDislocExtendedExecWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDislocExtendedExecEndpointAddress(java.lang.String address) {
        DislocExtendedExec_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cgl.quakesim.disloc.DislocExtendedService.class.isAssignableFrom(serviceEndpointInterface)) {
                cgl.quakesim.disloc.DislocExtendedExecSoapBindingStub _stub = new cgl.quakesim.disloc.DislocExtendedExecSoapBindingStub(new java.net.URL(DislocExtendedExec_address), this);
                _stub.setPortName(getDislocExtendedExecWSDDServiceName());
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
        if ("DislocExtendedExec".equals(inputPortName)) {
            return getDislocExtendedExec();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/dislocexec/services/DislocExtendedExec", "DislocExtendedServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/dislocexec/services/DislocExtendedExec", "DislocExtendedExec"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DislocExtendedExec".equals(portName)) {
            setDislocExtendedExecEndpointAddress(address);
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
