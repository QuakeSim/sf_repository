/**
 * MyVTKServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package WebFlowClient.ViscoViz;

public class MyVTKServiceLocator extends org.apache.axis.client.Service implements WebFlowClient.ViscoViz.MyVTKService {

    // gSOAP 2.2.3b generated service definition

    // Use to get a proxy class for MyVTKService
    private final java.lang.String MyVTKService_address = "http://kamet.ucs.indiana.edu:18084";

    public java.lang.String getMyVTKServiceAddress() {
        return MyVTKService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MyVTKServiceWSDDServiceName = "MyVTKService";

    public java.lang.String getMyVTKServiceWSDDServiceName() {
        return MyVTKServiceWSDDServiceName;
    }

    public void setMyVTKServiceWSDDServiceName(java.lang.String name) {
        MyVTKServiceWSDDServiceName = name;
    }

    public WebFlowClient.ViscoViz.MyVTKServicePortType getMyVTKService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MyVTKService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMyVTKService(endpoint);
    }

    public WebFlowClient.ViscoViz.MyVTKServicePortType getMyVTKService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            WebFlowClient.ViscoViz.MyVTKServiceBindingStub _stub = new WebFlowClient.ViscoViz.MyVTKServiceBindingStub(portAddress, this);
            _stub.setPortName(getMyVTKServiceWSDDServiceName());
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
            if (WebFlowClient.ViscoViz.MyVTKServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                WebFlowClient.ViscoViz.MyVTKServiceBindingStub _stub = new WebFlowClient.ViscoViz.MyVTKServiceBindingStub(new java.net.URL(MyVTKService_address), this);
                _stub.setPortName(getMyVTKServiceWSDDServiceName());
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
        if ("MyVTKService".equals(inputPortName)) {
            return getMyVTKService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:vtk", "MyVTKService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("MyVTKService"));
        }
        return ports.iterator();
    }

}
