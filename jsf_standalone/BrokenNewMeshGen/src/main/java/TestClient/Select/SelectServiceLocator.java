/**
 * SelectServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package TestClient.Select;

public class SelectServiceLocator extends org.apache.axis.client.Service implements TestClient.Select.SelectService {

    // Use to get a proxy class for Select
    private final java.lang.String Select_address = "http://infogroup.usc.edu:8080/axis/services/Select";

    public java.lang.String getSelectAddress() {
        return Select_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String SelectWSDDServiceName = "Select";

    public java.lang.String getSelectWSDDServiceName() {
        return SelectWSDDServiceName;
    }

    public void setSelectWSDDServiceName(java.lang.String name) {
        SelectWSDDServiceName = name;
    }

    public TestClient.Select.Select getSelect() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Select_address);
        }
        catch (java.net.MalformedURLException e) {
            return null; // unlikely as URL was validated in WSDL2Java
        }
        return getSelect(endpoint);
    }

    public TestClient.Select.Select getSelect(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            TestClient.Select.SelectSoapBindingStub _stub = new TestClient.Select.SelectSoapBindingStub(portAddress, this);
            _stub.setPortName(getSelectWSDDServiceName());
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
            if (TestClient.Select.Select.class.isAssignableFrom(serviceEndpointInterface)) {
                TestClient.Select.SelectSoapBindingStub _stub = new TestClient.Select.SelectSoapBindingStub(new java.net.URL(Select_address), this);
                _stub.setPortName(getSelectWSDDServiceName());
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
        java.rmi.Remote _stub = getPort(serviceEndpointInterface);
        ((org.apache.axis.client.Stub) _stub).setPortName(portName);
        return _stub;
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://infogroup.usc.edu:8080/axis/services/Select", "SelectService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("Select"));
        }
        return ports.iterator();
    }

}
