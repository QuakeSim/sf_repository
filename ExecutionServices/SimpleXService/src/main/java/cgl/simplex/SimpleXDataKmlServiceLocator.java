/**
 * SimpleXDataKmlServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package cgl.webservices.simplex;

public class SimpleXDataKmlServiceLocator extends org.apache.axis.client.Service implements SimpleXDataKmlService {

    public SimpleXDataKmlServiceLocator() {
    }


    public SimpleXDataKmlServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SimpleXDataKmlServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for KmlGenerator
    private java.lang.String KmlGenerator_address = "http://gf1.ucs.indiana.edu:13080/KmlGenerator/services/KmlGenerator";

    public java.lang.String getKmlGeneratorAddress() {
        return KmlGenerator_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String KmlGeneratorWSDDServiceName = "KmlGenerator";

    public java.lang.String getKmlGeneratorWSDDServiceName() {
        return KmlGeneratorWSDDServiceName;
    }

    public void setKmlGeneratorWSDDServiceName(java.lang.String name) {
        KmlGeneratorWSDDServiceName = name;
    }

    public SimpleXDataKml getKmlGenerator() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(KmlGenerator_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getKmlGenerator(endpoint);
    }

    public SimpleXDataKml getKmlGenerator(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            KmlGeneratorSoapBindingStub _stub = new KmlGeneratorSoapBindingStub(portAddress, this);
            _stub.setPortName(getKmlGeneratorWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setKmlGeneratorEndpointAddress(java.lang.String address) {
        KmlGenerator_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (SimpleXDataKml.class.isAssignableFrom(serviceEndpointInterface)) {
                KmlGeneratorSoapBindingStub _stub = new KmlGeneratorSoapBindingStub(new java.net.URL(KmlGenerator_address), this);
                _stub.setPortName(getKmlGeneratorWSDDServiceName());
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
        if ("KmlGenerator".equals(inputPortName)) {
            return getKmlGenerator();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://gf1.ucs.indiana.edu:13080/KmlGenerator/services/KmlGenerator", "SimpleXDataKmlService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://gf1.ucs.indiana.edu:13080/KmlGenerator/services/KmlGenerator", "KmlGenerator"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("KmlGenerator".equals(portName)) {
            setKmlGeneratorEndpointAddress(address);
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