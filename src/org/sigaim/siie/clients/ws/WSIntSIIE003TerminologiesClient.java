package org.sigaim.siie.clients.ws;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sigaim.siie.dadl.DADLManager;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.rm.ReferenceModelManager;
import org.sigaim.siie.rm.ReflectorReferenceModelManager;
import org.sigaim.siie.rm.exceptions.CSReason;
import org.sigaim.siie.rm.exceptions.RejectException;
import org.sigaim.siie.ws2.INTSIIE001EQLImplServiceStub;
import org.sigaim.siie.ws2.INTSIIE003TerminologiesImplServiceStub;
import org.sigaim.siie.ws2.INTSIIE001EQLImplServiceStub.QueryResponseE;
import org.sigaim.siie.ws2.INTSIIE001EQLImplServiceStub.WsReturnValueEQL;
import org.sigaim.siie.ws2.INTSIIE003TerminologiesImplServiceStub.Entry_type0;
import org.sigaim.siie.ws2.INTSIIE003TerminologiesImplServiceStub.RequestSynonymsResponse;
import org.sigaim.siie.ws2.INTSIIE003TerminologiesImplServiceStub.RequestSynonymsResponseE;
import org.sigaim.siie.ws2.INTSIIE003TerminologiesImplServiceStub.WsReturnValueSynonyms;

public class WSIntSIIE003TerminologiesClient {
	private INTSIIE003TerminologiesImplServiceStub proxy;
	private DADLManager dadlManager;
	private ReferenceModelManager referenceModelManager;
	
	public WSIntSIIE003TerminologiesClient() {
		this(null);
	}
	public WSIntSIIE003TerminologiesClient(String endpoint) {
		try {
			if(endpoint==null) {
				this.proxy=new INTSIIE003TerminologiesImplServiceStub();
			} else {
				this.proxy=new INTSIIE003TerminologiesImplServiceStub(endpoint);
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		this.dadlManager=new OpenEHRDADLManager();
		this.referenceModelManager=new ReflectorReferenceModelManager(dadlManager);
	}
	public Map<CDCV,Set<CDCV>>  getSynonymsForConcepts(String requestId,List<String> concepts) throws RejectException {
		WsReturnValueSynonyms ret;
		try {
			INTSIIE003TerminologiesImplServiceStub.RequestSynonyms synonyms=new INTSIIE003TerminologiesImplServiceStub.RequestSynonyms();
			synonyms.setArg0(requestId);
			String[] arr = new String[concepts.size()];
			arr = concepts.toArray(arr);
			synonyms.setArg1(arr);
			INTSIIE003TerminologiesImplServiceStub.RequestSynonymsE synonymse=new INTSIIE003TerminologiesImplServiceStub.RequestSynonymsE();
			synonymse.setRequestSynonyms(synonyms);
			RequestSynonymsResponseE resp=proxy.requestSynonyms(synonymse);
			ret=resp.getRequestSynonymsResponse().get_return();
			if(ret.getReasonCode()!=null) {
				throw new RejectException(requestId,CSReason.valueOf(ret.getReasonCode()));
			}
			Map<CDCV,Set<CDCV>> fret=new HashMap<CDCV,Set<CDCV>>();
			//Convert result to map
			if(ret.getSynonyms().getEntry()!= null)
			for(Entry_type0 entry : ret.getSynonyms().getEntry()) {
				HashSet<CDCV> temp=new HashSet<CDCV>();
				if(entry.getValue()!=null && entry.getValue().getItem()!=null) {
					for(String sconcept: entry.getValue().getItem()) {
						temp.add((CDCV)this.referenceModelManager.bind(this.dadlManager.parseDADL(new ByteArrayInputStream(sconcept.getBytes()))));
					}
				}
				fret.put((CDCV)this.referenceModelManager.bind(this.dadlManager.parseDADL(new ByteArrayInputStream(entry.getKey().getBytes()))), temp);
			}
			return fret;
		} catch(RemoteException e) {
			e.getCause().printStackTrace();
			throw new RejectException(requestId,CSReason.REAS02);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RejectException(requestId,CSReason.REAS02);
		}	
	}
}
