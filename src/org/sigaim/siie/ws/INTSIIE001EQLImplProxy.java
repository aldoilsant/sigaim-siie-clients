package org.sigaim.siie.ws;

public class INTSIIE001EQLImplProxy implements org.sigaim.siie.ws.INTSIIE001EQLImpl {
  private String _endpoint = null;
  private org.sigaim.siie.ws.INTSIIE001EQLImpl iNTSIIE001EQLImpl = null;
  
  public INTSIIE001EQLImplProxy() {
    _initINTSIIE001EQLImplProxy();
  }
  
  public INTSIIE001EQLImplProxy(String endpoint) {
    _endpoint = endpoint;
    _initINTSIIE001EQLImplProxy();
  }
  
  private void _initINTSIIE001EQLImplProxy() {
    try {
      iNTSIIE001EQLImpl = (new org.sigaim.siie.ws.INTSIIE001EQLImplServiceLocator()).getINTSIIE001EQLImpl();
      if (iNTSIIE001EQLImpl != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iNTSIIE001EQLImpl)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iNTSIIE001EQLImpl)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iNTSIIE001EQLImpl != null)
      ((javax.xml.rpc.Stub)iNTSIIE001EQLImpl)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.sigaim.siie.ws.INTSIIE001EQLImpl getINTSIIE001EQLImpl() {
    if (iNTSIIE001EQLImpl == null)
      _initINTSIIE001EQLImplProxy();
    return iNTSIIE001EQLImpl;
  }
  
  public org.sigaim.siie.interfaces.eql.ReturnValueEQL query(java.lang.String requestId, java.lang.String eqlQuery) throws java.rmi.RemoteException{
    if (iNTSIIE001EQLImpl == null)
      _initINTSIIE001EQLImplProxy();
    return iNTSIIE001EQLImpl.query(requestId, eqlQuery);
  }
  
  
}