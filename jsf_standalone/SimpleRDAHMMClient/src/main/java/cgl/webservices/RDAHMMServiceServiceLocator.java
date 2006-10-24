/**
 * RDAHMMServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package cgl.webservices;

public class RDAHMMServiceServiceLocator extends org.apache.axis.client.Service implements cgl.webservices.RDAHMMServiceService {

    // Use to get a proxy class for RDAHMMExec
    private final java.lang.String RDAHMMExec_address = "http://gf2.ucs.indiana.edu:8080/rdahmmexec/services/RDAHMMExec";

    public java.lang.String getRDAHMMExecAddress() {
        return RDAHMMExec_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String RDAHMMExecWSDDServiceName = "RDAHMMExec";

    public java.lang.String getRDAHMMExecWSDDServiceName() {
        return RDAHMMExecWSDDServiceName;
    }

    public void setRDAHMMExecWSDDServiceName(java.lang.String name) {
        RDAHMMExecWSDDServiceName = name;
    }

    public cgl.webservices.RDAHMMService getRDAHMMExec() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(RDAHMMExec_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getRDAHMMExec(endpoint);
    }

    public cgl.webservices.RDAHMMService getRDAHMMExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cgl.webservices.RDAHMMExecSoapBindingStub _stub = new cgl.webservices.RDAHMMExecSoapBindingStub(portAddress, this);
            _stub.setPortName(getRDAHMMExecWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cgl.webservices.RDAHMMService.class.isAssignableFrom(serviceEndpointInterface)) {
                cgl.webservices.RDAHMMExecSoapBindingStub _stub = new cgl.webservices.RDAHMMExecSoapBindingStub(new java.net.URL(RDAHMMExec_address), this);
                _stub.setPortName(getRDAHMMExecWSDDServiceName());
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
        String inputPortName = portName.getLocalPart();
        if ("RDAHMMExec".equals(inputPortName)) {
            return getRDAHMMExec();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://gf2.ucs.indiana.edu:8080/rdahmmexec/services/RDAHMMExec", "RDAHMMServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("RDAHMMExec"));
        }
        return ports.iterator();
    }

}
