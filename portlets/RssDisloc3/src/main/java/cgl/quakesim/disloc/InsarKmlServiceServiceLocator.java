/**
 * InsarKmlServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.disloc;

public class InsarKmlServiceServiceLocator extends org.apache.axis.client.Service implements cgl.quakesim.disloc.InsarKmlServiceService {

    public InsarKmlServiceServiceLocator() {
    }


    public InsarKmlServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public InsarKmlServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for InsarKmlExec
    private java.lang.String InsarKmlExec_address = "http://gf19.ucs.indiana.edu:8080/insarkmlservice/services/InsarKmlExec";

    public java.lang.String getInsarKmlExecAddress() {
        return InsarKmlExec_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String InsarKmlExecWSDDServiceName = "InsarKmlExec";

    public java.lang.String getInsarKmlExecWSDDServiceName() {
        return InsarKmlExecWSDDServiceName;
    }

    public void setInsarKmlExecWSDDServiceName(java.lang.String name) {
        InsarKmlExecWSDDServiceName = name;
    }

    public cgl.quakesim.disloc.InsarKmlService getInsarKmlExec() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(InsarKmlExec_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getInsarKmlExec(endpoint);
    }

    public cgl.quakesim.disloc.InsarKmlService getInsarKmlExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cgl.quakesim.disloc.InsarKmlExecSoapBindingStub _stub = new cgl.quakesim.disloc.InsarKmlExecSoapBindingStub(portAddress, this);
            _stub.setPortName(getInsarKmlExecWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setInsarKmlExecEndpointAddress(java.lang.String address) {
        InsarKmlExec_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cgl.quakesim.disloc.InsarKmlService.class.isAssignableFrom(serviceEndpointInterface)) {
                cgl.quakesim.disloc.InsarKmlExecSoapBindingStub _stub = new cgl.quakesim.disloc.InsarKmlExecSoapBindingStub(new java.net.URL(InsarKmlExec_address), this);
                _stub.setPortName(getInsarKmlExecWSDDServiceName());
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
        if ("InsarKmlExec".equals(inputPortName)) {
            return getInsarKmlExec();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://gf19.ucs.indiana.edu:8080/insarkmlservice/services/InsarKmlExec", "InsarKmlServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://gf19.ucs.indiana.edu:8080/insarkmlservice/services/InsarKmlExec", "InsarKmlExec"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("InsarKmlExec".equals(portName)) {
            setInsarKmlExecEndpointAddress(address);
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
