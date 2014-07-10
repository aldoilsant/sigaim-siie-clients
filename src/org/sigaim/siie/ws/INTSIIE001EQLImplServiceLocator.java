/**
 * INTSIIE001EQLImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sigaim.siie.ws;

public class INTSIIE001EQLImplServiceLocator extends org.apache.axis.client.Service implements org.sigaim.siie.ws.INTSIIE001EQLImplService {

    public INTSIIE001EQLImplServiceLocator() {
    }


    public INTSIIE001EQLImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public INTSIIE001EQLImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for INTSIIE001EQLImpl
    private java.lang.String INTSIIE001EQLImpl_address = "http://localhost:8080/SIIEWS/services/INTSIIE001EQLImpl";

    public java.lang.String getINTSIIE001EQLImplAddress() {
        return INTSIIE001EQLImpl_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String INTSIIE001EQLImplWSDDServiceName = "INTSIIE001EQLImpl";

    public java.lang.String getINTSIIE001EQLImplWSDDServiceName() {
        return INTSIIE001EQLImplWSDDServiceName;
    }

    public void setINTSIIE001EQLImplWSDDServiceName(java.lang.String name) {
        INTSIIE001EQLImplWSDDServiceName = name;
    }

    public org.sigaim.siie.ws.INTSIIE001EQLImpl getINTSIIE001EQLImpl() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(INTSIIE001EQLImpl_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getINTSIIE001EQLImpl(endpoint);
    }

    public org.sigaim.siie.ws.INTSIIE001EQLImpl getINTSIIE001EQLImpl(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.sigaim.siie.ws.INTSIIE001EQLImplSoapBindingStub _stub = new org.sigaim.siie.ws.INTSIIE001EQLImplSoapBindingStub(portAddress, this);
            _stub.setPortName(getINTSIIE001EQLImplWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setINTSIIE001EQLImplEndpointAddress(java.lang.String address) {
        INTSIIE001EQLImpl_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.sigaim.siie.ws.INTSIIE001EQLImpl.class.isAssignableFrom(serviceEndpointInterface)) {
                org.sigaim.siie.ws.INTSIIE001EQLImplSoapBindingStub _stub = new org.sigaim.siie.ws.INTSIIE001EQLImplSoapBindingStub(new java.net.URL(INTSIIE001EQLImpl_address), this);
                _stub.setPortName(getINTSIIE001EQLImplWSDDServiceName());
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
        if ("INTSIIE001EQLImpl".equals(inputPortName)) {
            return getINTSIIE001EQLImpl();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.siie.sigaim.org", "INTSIIE001EQLImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.siie.sigaim.org", "INTSIIE001EQLImpl"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("INTSIIE001EQLImpl".equals(portName)) {
            setINTSIIE001EQLImplEndpointAddress(address);
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
