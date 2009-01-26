/**
 * GridInfoServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cgl.quakesim.geofest;

public class GridInfoServiceServiceLocator extends org.apache.axis.client.Service implements cgl.quakesim.geofest.GridInfoServiceService {

    public GridInfoServiceServiceLocator() {
    }


    public GridInfoServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GridInfoServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GridInfoService
    private java.lang.String GridInfoService_address = "http://localhost:8080/gridinfo/services/GridInfoService";

    public java.lang.String getGridInfoServiceAddress() {
        return GridInfoService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GridInfoServiceWSDDServiceName = "GridInfoService";

    public java.lang.String getGridInfoServiceWSDDServiceName() {
        return GridInfoServiceWSDDServiceName;
    }

    public void setGridInfoServiceWSDDServiceName(java.lang.String name) {
        GridInfoServiceWSDDServiceName = name;
    }

    public cgl.quakesim.geofest.GridInfoService_PortType getGridInfoService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GridInfoService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGridInfoService(endpoint);
    }

    public cgl.quakesim.geofest.GridInfoService_PortType getGridInfoService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cgl.quakesim.geofest.GridInfoServiceSoapBindingStub _stub = new cgl.quakesim.geofest.GridInfoServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getGridInfoServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGridInfoServiceEndpointAddress(java.lang.String address) {
        GridInfoService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cgl.quakesim.geofest.GridInfoService_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                cgl.quakesim.geofest.GridInfoServiceSoapBindingStub _stub = new cgl.quakesim.geofest.GridInfoServiceSoapBindingStub(new java.net.URL(GridInfoService_address), this);
                _stub.setPortName(getGridInfoServiceWSDDServiceName());
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
        if ("GridInfoService".equals(inputPortName)) {
            return getGridInfoService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:8080/gridinfo/services/GridInfoService", "GridInfoServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:8080/gridinfo/services/GridInfoService", "GridInfoService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GridInfoService".equals(portName)) {
            setGridInfoServiceEndpointAddress(address);
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
