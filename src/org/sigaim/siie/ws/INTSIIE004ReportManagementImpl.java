/**
 * INTSIIE004ReportManagementImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sigaim.siie.ws;

public interface INTSIIE004ReportManagementImpl extends java.rmi.Remote {
    public org.sigaim.siie.interfaces.reportmanagement.ReturnValueCreateHealthcareFacility createHealthcareFacility(java.lang.String requestId) throws java.rmi.RemoteException;
    public org.sigaim.siie.interfaces.reportmanagement.ReturnValueCreateReport createReport(java.lang.String requestId, java.lang.String subjectOfCareId, java.lang.String composerId, java.lang.String audioData, java.lang.String textTranscription, java.lang.String reportStatus, java.lang.String rootArchetypeId) throws java.rmi.RemoteException;
    public org.sigaim.siie.interfaces.reportmanagement.ReturnValueCreateSubjectOfCare createSubjectOfCare(java.lang.String requestId) throws java.rmi.RemoteException;
    public org.sigaim.siie.interfaces.reportmanagement.ReturnValueCreatePerformer createPerformer(java.lang.String requestId) throws java.rmi.RemoteException;
}
