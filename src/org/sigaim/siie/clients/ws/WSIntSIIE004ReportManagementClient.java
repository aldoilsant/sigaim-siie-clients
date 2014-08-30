package org.sigaim.siie.clients.ws;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;

import org.openehr.am.parser.ContentObject;
import org.sigaim.siie.clients.IntSIIE004ReportManagementClient;
import org.sigaim.siie.dadl.DADLManager;
import org.sigaim.siie.dadl.OpenEHRDADLManager; 
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.iso13606.rm.Cluster;
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
import org.sigaim.siie.ws2.INTSIIE001EQLImplServiceStub;
import org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub;
import org.sigaim.siie.ws2.INTSIIE001EQLImplServiceStub.QueryResponseE;
import org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.CreateHealthcareFacilityResponseE;
import org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.CreatePerformerResponseE;
import org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.CreateReportResponseE;
import org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.CreateSubjectOfCareResponseE;
import org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.UpdateReportResponseE;
import org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.WsReturnValueCreateHealthcareFacility;
import org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.WsReturnValueCreatePerformer;
import org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.WsReturnValueCreateReport;
import org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.WsReturnValueCreateSubjectOfCare;
import org.sigaim.siie.ws2.INTSIIE004ReportManagementImplServiceStub.WsReturnValueUpdateReport;


public class WSIntSIIE004ReportManagementClient implements IntSIIE004ReportManagementClient {
	private INTSIIE004ReportManagementImplServiceStub proxy;
	private DADLManager dadlManager;
	private ReferenceModelManager referenceModelManager;
	
	public WSIntSIIE004ReportManagementClient() {
		this(null);
	}
	public WSIntSIIE004ReportManagementClient(String endpoint) {
		try {
		if(endpoint==null) {
			this.proxy=new INTSIIE004ReportManagementImplServiceStub();
		} else {
			this.proxy=new INTSIIE004ReportManagementImplServiceStub(endpoint);
		}
		} catch(Exception  e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
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
			WsReturnValueCreateHealthcareFacility res;
			INTSIIE004ReportManagementImplServiceStub.CreateHealthcareFacility par= new INTSIIE004ReportManagementImplServiceStub.CreateHealthcareFacility();
			par.setArg0(requestId);
			INTSIIE004ReportManagementImplServiceStub.CreateHealthcareFacilityE pare= new INTSIIE004ReportManagementImplServiceStub.CreateHealthcareFacilityE();
			pare.setCreateHealthcareFacility(par);
			CreateHealthcareFacilityResponseE response=proxy.createHealthcareFacility(pare);
			res=response.getCreateHealthcareFacilityResponse().get_return();
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
			WsReturnValueCreateSubjectOfCare res;
			INTSIIE004ReportManagementImplServiceStub.CreateSubjectOfCare par= new INTSIIE004ReportManagementImplServiceStub.CreateSubjectOfCare();
			par.setArg0(requestId);
			INTSIIE004ReportManagementImplServiceStub.CreateSubjectOfCareE pare= new INTSIIE004ReportManagementImplServiceStub.CreateSubjectOfCareE();
			pare.setCreateSubjectOfCare(par);
			CreateSubjectOfCareResponseE response=proxy.createSubjectOfCare(pare);
			res=response.getCreateSubjectOfCareResponse().get_return();
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
			WsReturnValueCreatePerformer res;
			INTSIIE004ReportManagementImplServiceStub.CreatePerformer par= new INTSIIE004ReportManagementImplServiceStub.CreatePerformer();
			par.setArg0(requestId);
			INTSIIE004ReportManagementImplServiceStub.CreatePerformerE pare= new INTSIIE004ReportManagementImplServiceStub.CreatePerformerE();
			pare.setCreatePerformer(par);
			CreatePerformerResponseE response=proxy.createPerformer(pare);
			res=response.getCreatePerformerResponse().get_return();			
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
	public Composition updateReport(
			String requestId,
			II ehrId, //the id of the previous version
			II previousVersionId, 
			FunctionalRole composerId,
			String textTranscription,
			boolean dictated,
			boolean signed,
			boolean confirmed,
			II rootArchetypeId,
			Cluster concepts
			) throws RejectException {
		try {
			WsReturnValueUpdateReport res;
			INTSIIE004ReportManagementImplServiceStub.UpdateReport par= new INTSIIE004ReportManagementImplServiceStub.UpdateReport();
			par.setArg0(requestId);
			par.setArg1(serializeFromReferenceModel(ehrId));
			par.setArg2(serializeFromReferenceModel(previousVersionId));
			par.setArg3(serializeFromReferenceModel(composerId));
			par.setArg4(textTranscription);
			par.setArg5(dictated);
			par.setArg6(signed);
			par.setArg7(confirmed);
			par.setArg8(serializeFromReferenceModel(rootArchetypeId));
			if(concepts!=null) {
				par.setArg9(serializeFromReferenceModel(concepts));
			} else {
				par.setArg9(null);
			}

			INTSIIE004ReportManagementImplServiceStub.UpdateReportE pare= new INTSIIE004ReportManagementImplServiceStub.UpdateReportE();
			pare.setUpdateReport(par);
			UpdateReportResponseE response=proxy.updateReport(pare);
			res=response.getUpdateReportResponse().get_return();			
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
	@Override
	public Composition createReport(String requestId, II subjectOfCareId,
			FunctionalRole composerId, 
			String textTranscription, boolean dictated, II rootArchetypeId)
			throws RejectException {
		try {
			WsReturnValueCreateReport res;
			INTSIIE004ReportManagementImplServiceStub.CreateReport par= new INTSIIE004ReportManagementImplServiceStub.CreateReport();
			par.setArg0(requestId);
			par.setArg1(serializeFromReferenceModel(subjectOfCareId));
			par.setArg2(serializeFromReferenceModel(composerId));
			par.setArg3(textTranscription);
			par.setArg4(dictated);
			par.setArg5(serializeFromReferenceModel(rootArchetypeId));
 
			INTSIIE004ReportManagementImplServiceStub.CreateReportE pare= new INTSIIE004ReportManagementImplServiceStub.CreateReportE();
			pare.setCreateReport(par);
			CreateReportResponseE response=proxy.createReport(pare);
			res=response.getCreateReportResponse().get_return();			
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
