package org.sigaim.siie.clients.ws;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;

import org.openehr.am.parser.ContentObject;
import org.sigaim.siie.clients.IntSIIE004ReportManagementClient;
import org.sigaim.siie.dadl.DADLManager;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.interfaces.reportmanagement.ReturnValueCreateHealthcareFacility;
import org.sigaim.siie.interfaces.reportmanagement.ReturnValueCreatePerformer;
import org.sigaim.siie.interfaces.reportmanagement.ReturnValueCreateReport;
import org.sigaim.siie.interfaces.reportmanagement.ReturnValueCreateSubjectOfCare;
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.iso13606.rm.Composition;
import org.sigaim.siie.iso13606.rm.EHRExtract;
import org.sigaim.siie.iso13606.rm.FunctionalRole;
import org.sigaim.siie.iso13606.rm.HealthcareFacility;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.Performer;
import org.sigaim.siie.iso13606.rm.SubjectOfCare;
import org.sigaim.siie.rm.ReferenceModelManager;
import org.sigaim.siie.rm.ReflectorReferenceModelManager;
import org.sigaim.siie.rm.exceptions.CSReason;
import org.sigaim.siie.rm.exceptions.ReferenceModelException;
import org.sigaim.siie.rm.exceptions.RejectException;
import org.sigaim.siie.ws.INTSIIE004ReportManagementImplProxy;


public class WSIntSIIE004ReportManagementClient implements IntSIIE004ReportManagementClient {
	private INTSIIE004ReportManagementImplProxy proxy;
	private DADLManager dadlManager;
	private ReferenceModelManager referenceModelManager;
	
	public WSIntSIIE004ReportManagementClient() {
		this(null);
	}
	public WSIntSIIE004ReportManagementClient(String endpoint) {
		if(endpoint==null) {
			this.proxy=new INTSIIE004ReportManagementImplProxy();
		} else {
			this.proxy=new INTSIIE004ReportManagementImplProxy(endpoint);
		}
		this.dadlManager=new OpenEHRDADLManager();
		this.referenceModelManager=new ReflectorReferenceModelManager(dadlManager);
	}
	protected <T extends Object> T bindFromDADL(String dadl, Class<T> type) throws ReferenceModelException {
		ContentObject parsedDADL=dadlManager.parseDADL(new ByteArrayInputStream(dadl.getBytes()));
		return  type.cast(referenceModelManager.bind(parsedDADL));
	}
	protected String serializeFromReferenceModel(Object rmObject) throws ReferenceModelException {
		ContentObject serialized=referenceModelManager.unbind(rmObject);
		return dadlManager.serialize(serialized, false);
		
	}
	@Override
	public HealthcareFacility createHealthcareFacility(String requestId) throws RejectException {
		// TODO Auto-generated method stub
		try {
			ReturnValueCreateHealthcareFacility res=proxy.createHealthcareFacility(requestId);
			if(res.getReasonCode()!=null) {
				throw new RejectException(requestId,CSReason.valueOf(res.getReasonCode()));
			}
			return bindFromDADL(res.getSerialized(),HealthcareFacility.class);
		} catch(RemoteException e) {
			e.getCause().printStackTrace();
			throw new RejectException(requestId,CSReason.REAS02);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RejectException(requestId,CSReason.REAS02);
		}
	}

	@Override
	public EHRExtract createSubjectOfCare(String requestId) throws RejectException {
		try {
			ReturnValueCreateSubjectOfCare res=proxy.createSubjectOfCare(requestId);
			if(res.getReasonCode()!=null) {
				throw new RejectException(requestId,CSReason.valueOf(res.getReasonCode()));
			}
			return bindFromDADL(res.getSerialized(),EHRExtract.class);
		}catch(RemoteException e) {
			e.getCause().printStackTrace();
			throw new RejectException(requestId,CSReason.REAS02);
		}  catch(Exception e) {
			e.printStackTrace();
			throw new RejectException(requestId,CSReason.REAS02);
		}
	}

	@Override
	public Performer createPerformer(String requestId) throws RejectException {
		try {
			ReturnValueCreatePerformer res=proxy.createPerformer(requestId);
			if(res.getReasonCode()!=null) {
				throw new RejectException(requestId,CSReason.valueOf(res.getReasonCode()));
			}
			return bindFromDADL(res.getSerialized(),Performer.class);
		} catch(RemoteException e) {
			e.getCause().printStackTrace();
			throw new RejectException(requestId,CSReason.REAS02);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RejectException(requestId,CSReason.REAS02);
		}
	}

	@Override
	public Composition createReport(String requestId, II subjectOfCareId,
			FunctionalRole composerId, String audioData,
			String textTranscription, CDCV reportStatus, II rootArchetypeId)
			throws RejectException {
		try {
			ReturnValueCreateReport res=proxy.createReport(requestId, serializeFromReferenceModel(subjectOfCareId), serializeFromReferenceModel(composerId), audioData, textTranscription, serializeFromReferenceModel(reportStatus), serializeFromReferenceModel(rootArchetypeId));
			if(res.getReasonCode()!=null) {
				throw new RejectException(requestId,CSReason.valueOf(res.getReasonCode()));
			}
			return bindFromDADL(res.getSerialized(),Composition.class);
		} catch(RemoteException e) {
			e.getCause().printStackTrace();
			throw new RejectException(requestId,CSReason.REAS02);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RejectException(requestId,CSReason.REAS02);
		}
	}

}
