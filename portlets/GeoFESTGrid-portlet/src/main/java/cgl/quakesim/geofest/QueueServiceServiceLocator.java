/**
 * QueueServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.geofest;

public class QueueServiceServiceLocator extends org.apache.axis.client.Service implements cgl.quakesim.geofest.QueueServiceService {

    public QueueServiceServiceLocator() {
    }


    public QueueServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public QueueServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for QueueExec
    private java.lang.String QueueExec_address = "http://gf19.ucs.indiana.edu:8080/queueservice/services/QueueExec";

    public java.lang.String getQueueExecAddress() {
        return QueueExec_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String QueueExecWSDDServiceName = "QueueExec";

    public java.lang.String getQueueExecWSDDServiceName() {
        return QueueExecWSDDServiceName;
    }

    public void setQueueExecWSDDServiceName(java.lang.String name) {
        QueueExecWSDDServiceName = name;
    }

    public cgl.quakesim.geofest.QueueService getQueueExec() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(QueueExec_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getQueueExec(endpoint);
    }

    public cgl.quakesim.geofest.QueueService getQueueExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cgl.quakesim.geofest.QueueExecSoapBindingStub _stub = new cgl.quakesim.geofest.QueueExecSoapBindingStub(portAddress, this);
            _stub.setPortName(getQueueExecWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setQueueExecEndpointAddress(java.lang.String address) {
        QueueExec_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cgl.quakesim.geofest.QueueService.class.isAssignableFrom(serviceEndpointInterface)) {
                cgl.quakesim.geofest.QueueExecSoapBindingStub _stub = new cgl.quakesim.geofest.QueueExecSoapBindingStub(new java.net.URL(QueueExec_address), this);
                _stub.setPortName(getQueueExecWSDDServiceName());
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
        if ("QueueExec".equals(inputPortName)) {
            return getQueueExec();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://gf19.ucs.indiana.edu:8080/queueservice/services/QueueExec", "QueueServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://gf19.ucs.indiana.edu:8080/queueservice/services/QueueExec", "QueueExec"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("QueueExec".equals(portName)) {
            setQueueExecEndpointAddress(address);
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
