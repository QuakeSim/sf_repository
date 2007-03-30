/**
 * GeoFESTServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.geofest;

public class GeoFESTServiceServiceLocator extends org.apache.axis.client.Service implements cgl.quakesim.geofest.GeoFESTServiceService {

    public GeoFESTServiceServiceLocator() {
    }


    public GeoFESTServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GeoFESTServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GeoFESTExec
    private java.lang.String GeoFESTExec_address = "http://gf7.ucs.indiana.edu:8080/geofestexec/services/GeoFESTExec";

    public java.lang.String getGeoFESTExecAddress() {
        return GeoFESTExec_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GeoFESTExecWSDDServiceName = "GeoFESTExec";

    public java.lang.String getGeoFESTExecWSDDServiceName() {
        return GeoFESTExecWSDDServiceName;
    }

    public void setGeoFESTExecWSDDServiceName(java.lang.String name) {
        GeoFESTExecWSDDServiceName = name;
    }

    public cgl.quakesim.geofest.GeoFESTService getGeoFESTExec() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GeoFESTExec_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGeoFESTExec(endpoint);
    }

    public cgl.quakesim.geofest.GeoFESTService getGeoFESTExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cgl.quakesim.geofest.GeoFESTExecSoapBindingStub _stub = new cgl.quakesim.geofest.GeoFESTExecSoapBindingStub(portAddress, this);
            _stub.setPortName(getGeoFESTExecWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGeoFESTExecEndpointAddress(java.lang.String address) {
        GeoFESTExec_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cgl.quakesim.geofest.GeoFESTService.class.isAssignableFrom(serviceEndpointInterface)) {
                cgl.quakesim.geofest.GeoFESTExecSoapBindingStub _stub = new cgl.quakesim.geofest.GeoFESTExecSoapBindingStub(new java.net.URL(GeoFESTExec_address), this);
                _stub.setPortName(getGeoFESTExecWSDDServiceName());
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
        if ("GeoFESTExec".equals(inputPortName)) {
            return getGeoFESTExec();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://gf7.ucs.indiana.edu:8080/geofestexec/services/GeoFESTExec", "GeoFESTServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://gf7.ucs.indiana.edu:8080/geofestexec/services/GeoFESTExec", "GeoFESTExec"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GeoFESTExec".equals(portName)) {
            setGeoFESTExecEndpointAddress(address);
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
