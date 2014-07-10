package org.sigaim.siie.ws;

public class INTSIIE004ReportManagementImplProxy implements org.sigaim.siie.ws.INTSIIE004ReportManagementImpl {
  private String _endpoint = null;
  private org.sigaim.siie.ws.INTSIIE004ReportManagementImpl iNTSIIE004ReportManagementImpl = null;
  
  public INTSIIE004ReportManagementImplProxy() {
    _initINTSIIE004ReportManagementImplProxy();
  }
  
  public INTSIIE004ReportManagementImplProxy(String endpoint) {
    _endpoint = endpoint;
    _initINTSIIE004ReportManagementImplProxy();
  }
  
  private void _initINTSIIE004ReportManagementImplProxy() {
    try {
      iNTSIIE004ReportManagementImpl = (new org.sigaim.siie.ws.INTSIIE004ReportManagementImplServiceLocator()).getINTSIIE004ReportManagementImpl();
      if (iNTSIIE004ReportManagementImpl != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iNTSIIE004ReportManagementImpl)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iNTSIIE004ReportManagementImpl)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iNTSIIE004ReportManagementImpl != null)
      ((javax.xml.rpc.Stub)iNTSIIE004ReportManagementImpl)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.sigaim.siie.ws.INTSIIE004ReportManagementImpl getINTSIIE004ReportManagementImpl() {
    if (iNTSIIE004ReportManagementImpl == null)
      _initINTSIIE004ReportManagementImplProxy();
    return iNTSIIE004ReportManagementImpl;
  }
  
  public org.sigaim.siie.interfaces.reportmanagement.ReturnValueCreateHealthcareFacility createHealthcareFacility(java.lang.String requestId) throws java.rmi.RemoteException{
    if (iNTSIIE004ReportManagementImpl == null)
      _initINTSIIE004ReportManagementImplProxy();
    return iNTSIIE004ReportManagementImpl.createHealthcareFacility(requestId);
  }
  
  public org.sigaim.siie.interfaces.reportmanagement.ReturnValueCreateReport createReport(java.lang.String requestId, java.lang.String subjectOfCareId, java.lang.String composerId, java.lang.String audioData, java.lang.String textTranscription, java.lang.String reportStatus, java.lang.String rootArchetypeId) throws java.rmi.RemoteException{
    if (iNTSIIE004ReportManagementImpl == null)
      _initINTSIIE004ReportManagementImplProxy();
    return iNTSIIE004ReportManagementImpl.createReport(requestId, subjectOfCareId, composerId, audioData, textTranscription, reportStatus, rootArchetypeId);
  }
  
  public org.sigaim.siie.interfaces.reportmanagement.ReturnValueCreateSubjectOfCare createSubjectOfCare(java.lang.String requestId) throws java.rmi.RemoteException{
    if (iNTSIIE004ReportManagementImpl == null)
      _initINTSIIE004ReportManagementImplProxy();
    return iNTSIIE004ReportManagementImpl.createSubjectOfCare(requestId);
  }
  
  public org.sigaim.siie.interfaces.reportmanagement.ReturnValueCreatePerformer createPerformer(java.lang.String requestId) throws java.rmi.RemoteException{
    if (iNTSIIE004ReportManagementImpl == null)
      _initINTSIIE004ReportManagementImplProxy();
    return iNTSIIE004ReportManagementImpl.createPerformer(requestId);
  }
  
  
}