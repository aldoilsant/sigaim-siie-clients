package org.sigaim.siie.clients.ws;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.openehr.am.parser.ComplexObjectBlock;
import org.openehr.am.parser.ContentObject;
import org.openehr.am.parser.KeyedObject;
import org.openehr.am.parser.MultipleAttributeObjectBlock;
import org.openehr.am.parser.SingleAttributeObjectBlock;
import org.sigaim.siie.clients.IntSIIE001EQLClient;
import org.sigaim.siie.dadl.DADLManager;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.interfaces.eql.ReturnValueEQL;
import org.sigaim.siie.iso13606.rm.Cluster;
import org.sigaim.siie.iso13606.rm.HealthcareFacility;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.Performer;
import org.sigaim.siie.iso13606.rm.SubjectOfCare;
import org.sigaim.siie.rm.ReferenceModelManager;
import org.sigaim.siie.rm.ReflectorReferenceModelManager;
import org.sigaim.siie.rm.exceptions.CSReason;
import org.sigaim.siie.rm.exceptions.RejectException;
import org.sigaim.siie.seql.model.SEQLException;
import org.sigaim.siie.seql.model.SEQLResultSet;
import org.sigaim.siie.seql.model.SEQLSelectCondition;
import org.sigaim.siie.ws.INTSIIE001EQLImplProxy;

public class WSIntSIIE001EQLClient implements  IntSIIE001EQLClient {
	private INTSIIE001EQLImplProxy proxy;
	private DADLManager dadlManager;
	private ReferenceModelManager referenceModelManager;
	
	public WSIntSIIE001EQLClient() {
		this.proxy=new INTSIIE001EQLImplProxy();
		this.dadlManager=new OpenEHRDADLManager();
		this.referenceModelManager=new ReflectorReferenceModelManager(dadlManager);
	}
	protected SEQLResultSet createResultSet(String input) throws SEQLException{
		SEQLResultSet rs=new SEQLResultSet();
		ContentObject serializedResultSet=this.dadlManager.parseDADL(new ByteArrayInputStream(input.getBytes()));
		//The first multipleAttributeObjectBlock are the rows. 
		if(serializedResultSet.getComplexObjectBlock() instanceof SingleAttributeObjectBlock) {
			//Empty result set
			rs.setNumberOfColumns(0);
			rs.compile();
			return rs;
		}
		MultipleAttributeObjectBlock rowsBlock=(MultipleAttributeObjectBlock)serializedResultSet.getComplexObjectBlock();
		for(KeyedObject row : rowsBlock.getKeyObjects()) {
			rs.addRow();
			//Each column is, itself, a multipleAttributeobjectblock
			MultipleAttributeObjectBlock columnBlock=(MultipleAttributeObjectBlock)row.getObject();
			int columns=0;
			for(KeyedObject column : columnBlock.getKeyObjects()) {
				rs.appendToRow(new ContentObject(null,(ComplexObjectBlock)column.getObject()));
				columns++;
			}
			rs.setNumberOfColumns(columns);
		}
		rs.compile();
		return rs;
	}
	public org.sigaim.siie.seql.model.SEQLResultSet query(String requestId, String eqlQuery) throws RejectException {
		ReturnValueEQL ret;
		try {
			ret=proxy.query(requestId, eqlQuery);
			if(ret.getReasonCode()!=null) {
				throw new RejectException(requestId,CSReason.valueOf(ret.getReasonCode()));
			}
			return createResultSet(ret.getSerialized());
		} catch(RemoteException e) {
			e.getCause().printStackTrace();
			throw new RejectException(requestId,CSReason.REAS02);
		} catch(Exception e) {
			e.printStackTrace();
			throw new RejectException(requestId,CSReason.REAS02);
		}
	}
	@Override
	public List<HealthcareFacility> getAllHealthcareFacilities()
			throws RejectException {
		try {
			SEQLResultSet rs=this.query("", "SELECT e/all_healthcare_facilities FROM EHR SYSTEM e;");
			ArrayList<HealthcareFacility> hfs=new ArrayList<HealthcareFacility>();
			while(rs.nextRow()){
				hfs.add((HealthcareFacility)this.referenceModelManager.bind(rs.getColumn(0)));
			}
			return hfs;
		} catch(RejectException e) {
			throw e;
		} catch(Exception e) {
			e.printStackTrace();
			throw new RejectException("",CSReason.REAS02);
		}
	}
	@Override
	public List<SubjectOfCare> getAllSubjectsOfCare() throws RejectException {
		try {
			SEQLResultSet rs=this.query("", "SELECT e/all_subjects_of_care FROM EHR SYSTEM e;");
			ArrayList<SubjectOfCare> hfs=new ArrayList<SubjectOfCare>();
			while(rs.nextRow()){
				hfs.add((SubjectOfCare)this.referenceModelManager.bind(rs.getColumn(0)));
			}
			return hfs;
		} catch(RejectException e) {
			throw e;
		} catch(Exception e) {
			e.printStackTrace();
			throw new RejectException("",CSReason.REAS02);
		}
	}
	@Override
	public List<Performer> getAllPerformers() throws RejectException {
		try {
			SEQLResultSet rs=this.query("", "SELECT e/all_performers FROM EHR SYSTEM e;");
			ArrayList<Performer> hfs=new ArrayList<Performer>();
			while(rs.nextRow()){
				hfs.add((Performer)this.referenceModelManager.bind(rs.getColumn(0)));
			}
			return hfs;
		} catch(RejectException e) {
			throw e;
		} catch(Exception e) {
			e.printStackTrace();
			throw new RejectException("",CSReason.REAS02);
		}
	}
	@Override
	public Cluster getConceptInformationForReportId(II reportId) throws RejectException  {
		try {
			Cluster ret=null;
			SEQLResultSet rs=this.query("", "SELECT e/items[at0008] WITH DESCENDANTS FROM EHR CONTAINS COMPOSITION c CONTAINS ENTRY e[CEN-EN13606-ENTRY.Informacion.v1] WHERE c/rc_id/extension="+reportId.getExtension()+";");
			while(rs.nextRow()){
				ContentObject serializedCluster=rs.getColumn(0);
				ret=(Cluster)this.referenceModelManager.bind(serializedCluster);
				break;
			}		
			return ret;
		} catch(RejectException e) {
			throw e;
		} catch(Exception e) {
			e.printStackTrace();
			throw new RejectException("",CSReason.REAS02);
		}
	}

}
