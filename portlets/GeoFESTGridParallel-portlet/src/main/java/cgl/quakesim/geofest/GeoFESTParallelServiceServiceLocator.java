/**
 * GeoFESTParallelServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.geofest;

public class GeoFESTParallelServiceServiceLocator extends org.apache.axis.client.Service implements cgl.quakesim.geofest.GeoFESTParallelServiceService {

    public GeoFESTParallelServiceServiceLocator() {
    }


    public GeoFESTParallelServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GeoFESTParallelServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GeoFESTParallelExec
    private java.lang.String GeoFESTParallelExec_address = "http://localhost:8080/geofest-parallel-exec/services/GeoFESTParallelExec";

    public java.lang.String getGeoFESTParallelExecAddress() {
        return GeoFESTParallelExec_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GeoFESTParallelExecWSDDServiceName = "GeoFESTParallelExec";

    public java.lang.String getGeoFESTParallelExecWSDDServiceName() {
        return GeoFESTParallelExecWSDDServiceName;
    }

    public void setGeoFESTParallelExecWSDDServiceName(java.lang.String name) {
        GeoFESTParallelExecWSDDServiceName = name;
    }

    public cgl.quakesim.geofest.GeoFESTParallelService getGeoFESTParallelExec() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GeoFESTParallelExec_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGeoFESTParallelExec(endpoint);
    }

    public cgl.quakesim.geofest.GeoFESTParallelService getGeoFESTParallelExec(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cgl.quakesim.geofest.GeoFESTParallelExecSoapBindingStub _stub = new cgl.quakesim.geofest.GeoFESTParallelExecSoapBindingStub(portAddress, this);
            _stub.setPortName(getGeoFESTParallelExecWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGeoFESTParallelExecEndpointAddress(java.lang.String address) {
        GeoFESTParallelExec_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cgl.quakesim.geofest.GeoFESTParallelService.class.isAssignableFrom(serviceEndpointInterface)) {
                cgl.quakesim.geofest.GeoFESTParallelExecSoapBindingStub _stub = new cgl.quakesim.geofest.GeoFESTParallelExecSoapBindingStub(new java.net.URL(GeoFESTParallelExec_address), this);
                _stub.setPortName(getGeoFESTParallelExecWSDDServiceName());
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
        if ("GeoFESTParallelExec".equals(inputPortName)) {
            return getGeoFESTParallelExec();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/geofest-parallel-exec/services/GeoFESTParallelExec", "GeoFESTParallelServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/geofest-parallel-exec/services/GeoFESTParallelExec", "GeoFESTParallelExec"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GeoFESTParallelExec".equals(portName)) {
            setGeoFESTParallelExecEndpointAddress(address);
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
