/**
 * INTSIIE004ReportManagementImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sigaim.siie.ws;

public class INTSIIE004ReportManagementImplServiceLocator extends org.apache.axis.client.Service implements org.sigaim.siie.ws.INTSIIE004ReportManagementImplService {

    public INTSIIE004ReportManagementImplServiceLocator() {
    }


    public INTSIIE004ReportManagementImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public INTSIIE004ReportManagementImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for INTSIIE004ReportManagementImpl
    private java.lang.String INTSIIE004ReportManagementImpl_address = "http://localhost:8080/SIIEWS/services/INTSIIE004ReportManagementImpl";

    public java.lang.String getINTSIIE004ReportManagementImplAddress() {
        return INTSIIE004ReportManagementImpl_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String INTSIIE004ReportManagementImplWSDDServiceName = "INTSIIE004ReportManagementImpl";

    public java.lang.String getINTSIIE004ReportManagementImplWSDDServiceName() {
        return INTSIIE004ReportManagementImplWSDDServiceName;
    }

    public void setINTSIIE004ReportManagementImplWSDDServiceName(java.lang.String name) {
        INTSIIE004ReportManagementImplWSDDServiceName = name;
    }

    public org.sigaim.siie.ws.INTSIIE004ReportManagementImpl getINTSIIE004ReportManagementImpl() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(INTSIIE004ReportManagementImpl_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getINTSIIE004ReportManagementImpl(endpoint);
    }

    public org.sigaim.siie.ws.INTSIIE004ReportManagementImpl getINTSIIE004ReportManagementImpl(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            org.sigaim.siie.ws.INTSIIE004ReportManagementImplSoapBindingStub _stub = new org.sigaim.siie.ws.INTSIIE004ReportManagementImplSoapBindingStub(portAddress, this);
            _stub.setPortName(getINTSIIE004ReportManagementImplWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setINTSIIE004ReportManagementImplEndpointAddress(java.lang.String address) {
        INTSIIE004ReportManagementImpl_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.sigaim.siie.ws.INTSIIE004ReportManagementImpl.class.isAssignableFrom(serviceEndpointInterface)) {
                org.sigaim.siie.ws.INTSIIE004ReportManagementImplSoapBindingStub _stub = new org.sigaim.siie.ws.INTSIIE004ReportManagementImplSoapBindingStub(new java.net.URL(INTSIIE004ReportManagementImpl_address), this);
                _stub.setPortName(getINTSIIE004ReportManagementImplWSDDServiceName());
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
        if ("INTSIIE004ReportManagementImpl".equals(inputPortName)) {
            return getINTSIIE004ReportManagementImpl();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.siie.sigaim.org", "INTSIIE004ReportManagementImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.siie.sigaim.org", "INTSIIE004ReportManagementImpl"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("INTSIIE004ReportManagementImpl".equals(portName)) {
            setINTSIIE004ReportManagementImplEndpointAddress(address);
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
