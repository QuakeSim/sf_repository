/**
 * MyVTKServiceBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package WebFlowClient.ViscoViz;

public class MyVTKServiceBindingStub extends org.apache.axis.client.Stub implements WebFlowClient.ViscoViz.MyVTKServicePortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[10];
        org.apache.axis.description.OperationDesc oper;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("addFault");
        oper.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in1"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in2"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in3"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in4"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in5"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in6"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in7"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("zoomIn");
        oper.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in1"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in2"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("removeFault");
        oper.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("plot");
        oper.addParameter(new javax.xml.namespace.QName("", "plotReturn"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, org.apache.axis.description.ParameterDesc.OUT, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "width"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.OUT, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "height"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.OUT, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("mesh");
        oper.addParameter(new javax.xml.namespace.QName("", "m"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "n"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "meshReturn"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "base64Binary"), byte[].class, org.apache.axis.description.ParameterDesc.OUT, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "width"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.OUT, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "height"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.OUT, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("rotate");
        oper.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in1"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in2"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("addLayer");
        oper.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in1"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in2"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in3"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in4"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in5"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "in6"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "float"), float.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("exit");
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("removeLayer");
        oper.addParameter(new javax.xml.namespace.QName("", "in0"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("setWindow");
        oper.addParameter(new javax.xml.namespace.QName("", "w"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.addParameter(new javax.xml.namespace.QName("", "h"), new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"), int.class, org.apache.axis.description.ParameterDesc.IN, false, false);
        oper.setReturnType(org.apache.axis.encoding.XMLType.AXIS_VOID);
        oper.setStyle(org.apache.axis.enum.Style.RPC);
        oper.setUse(org.apache.axis.enum.Use.ENCODED);
        _operations[9] = oper;

    }

    public MyVTKServiceBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public MyVTKServiceBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public MyVTKServiceBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
    }

    private org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call =
                    (org.apache.axis.client.Call) super.service.createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            return _call;
        }
        catch (java.lang.Throwable t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", t);
        }
    }

    public void addFault(java.lang.String in0, float in1, float in2, float in3, float in4, float in5, float in6, float in7) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:vtk", "addFault"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, new java.lang.Float(in1), new java.lang.Float(in2), new java.lang.Float(in3), new java.lang.Float(in4), new java.lang.Float(in5), new java.lang.Float(in6), new java.lang.Float(in7)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void zoomIn(float in0, float in1, float in2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:vtk", "zoomIn"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Float(in0), new java.lang.Float(in1), new java.lang.Float(in2)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void removeFault(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:vtk", "removeFault"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void plot(javax.xml.rpc.holders.ByteArrayHolder plotReturn, javax.xml.rpc.holders.IntHolder width, javax.xml.rpc.holders.IntHolder height) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:vtk", "plot"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                plotReturn.value = (byte[]) _output.get(new javax.xml.namespace.QName("", "plotReturn"));
            } catch (java.lang.Exception _exception) {
                plotReturn.value = (byte[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "plotReturn")), byte[].class);
            }
            try {
                width.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "width"))).intValue();
            } catch (java.lang.Exception _exception) {
                width.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "width")), int.class)).intValue();
            }
            try {
                height.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "height"))).intValue();
            } catch (java.lang.Exception _exception) {
                height.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "height")), int.class)).intValue();
            }
        }
    }

    public void mesh(int m, java.lang.String n, javax.xml.rpc.holders.ByteArrayHolder meshReturn, javax.xml.rpc.holders.IntHolder width, javax.xml.rpc.holders.IntHolder height) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:vtk", "mesh"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(m), n});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            java.util.Map _output;
            _output = _call.getOutputParams();
            try {
                meshReturn.value = (byte[]) _output.get(new javax.xml.namespace.QName("", "meshReturn"));
            } catch (java.lang.Exception _exception) {
                meshReturn.value = (byte[]) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "meshReturn")), byte[].class);
            }
            try {
                width.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "width"))).intValue();
            } catch (java.lang.Exception _exception) {
                width.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "width")), int.class)).intValue();
            }
            try {
                height.value = ((java.lang.Integer) _output.get(new javax.xml.namespace.QName("", "height"))).intValue();
            } catch (java.lang.Exception _exception) {
                height.value = ((java.lang.Integer) org.apache.axis.utils.JavaUtils.convert(_output.get(new javax.xml.namespace.QName("", "height")), int.class)).intValue();
            }
        }
    }

    public void rotate(float in0, float in1, float in2) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:vtk", "rotate"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Float(in0), new java.lang.Float(in1), new java.lang.Float(in2)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void addLayer(java.lang.String in0, float in1, float in2, float in3, float in4, float in5, float in6) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:vtk", "addLayer"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0, new java.lang.Float(in1), new java.lang.Float(in2), new java.lang.Float(in3), new java.lang.Float(in4), new java.lang.Float(in5), new java.lang.Float(in6)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void exit() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:vtk", "exit"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void removeLayer(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:vtk", "removeLayer"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

    public void setWindow(int w, int h) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:vtk", "setWindow"));

        setRequestHeaders(_call);
        setAttachments(_call);
        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {new java.lang.Integer(w), new java.lang.Integer(h)});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        extractAttachments(_call);
    }

}
