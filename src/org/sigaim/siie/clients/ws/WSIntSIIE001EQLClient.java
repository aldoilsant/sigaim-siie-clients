package org.sigaim.siie.clients.ws;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openehr.am.parser.ComplexObjectBlock;
import org.openehr.am.parser.ContentObject;
import org.openehr.am.parser.KeyedObject;
import org.openehr.am.parser.MultipleAttributeObjectBlock;
import org.openehr.am.parser.SingleAttributeObjectBlock;
import org.sigaim.siie.clients.IntSIIE001EQLClient;
import org.sigaim.siie.clients.IntSIIEReportSummary;
import org.sigaim.siie.dadl.DADLManager;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.interfaces.eql.ReturnValueEQL;
import org.sigaim.siie.iso13606.rm.AuditInfo;
import org.sigaim.siie.iso13606.rm.Cluster;
import org.sigaim.siie.iso13606.rm.Composition;
import org.sigaim.siie.iso13606.rm.EHRExtract;
import org.sigaim.siie.iso13606.rm.Element;
import org.sigaim.siie.iso13606.rm.FunctionalRole;
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
				ContentObject co=rs.getColumn(0);
				List<SingleAttributeObjectBlock> facilityBlocks=new ArrayList<SingleAttributeObjectBlock>();
				if(co.getComplexObjectBlock() instanceof SingleAttributeObjectBlock) {
					facilityBlocks.add((SingleAttributeObjectBlock)co.getComplexObjectBlock());
				} else {
					MultipleAttributeObjectBlock mb=(MultipleAttributeObjectBlock)co.getComplexObjectBlock();
					for(KeyedObject ob : mb.getKeyObjects()) {
						facilityBlocks.add((SingleAttributeObjectBlock)ob.getObject());
					}
				}
				for(SingleAttributeObjectBlock sb : facilityBlocks) {
					hfs.add((HealthcareFacility)this.referenceModelManager.bindSingleAttributeObjectBlock(sb));
				}
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
			ArrayList<SubjectOfCare> subjects=new ArrayList<SubjectOfCare>();
			while(rs.nextRow()){
				ContentObject co=rs.getColumn(0);
				List<SingleAttributeObjectBlock> facilityBlocks=new ArrayList<SingleAttributeObjectBlock>();
				if(co.getComplexObjectBlock() instanceof SingleAttributeObjectBlock) {
					facilityBlocks.add((SingleAttributeObjectBlock)co.getComplexObjectBlock());
				} else {
					MultipleAttributeObjectBlock mb=(MultipleAttributeObjectBlock)co.getComplexObjectBlock();
					for(KeyedObject ob : mb.getKeyObjects()) {
						facilityBlocks.add((SingleAttributeObjectBlock)ob.getObject());
					}
				}
				for(SingleAttributeObjectBlock sb : facilityBlocks) {
					subjects.add((SubjectOfCare)this.referenceModelManager.bindSingleAttributeObjectBlock(sb));
				}
			}
			return subjects;
		} catch(RejectException e) {
			throw e;
		} catch(Exception e) {
			e.printStackTrace();
			throw new RejectException("",CSReason.REAS02);
		}
		/*try {
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
		}*/
	}
	@Override
	public List<Performer> getAllPerformers() throws RejectException {
		try {
			SEQLResultSet rs=this.query("", "SELECT e/all_performers FROM EHR SYSTEM e;");
			ArrayList<Performer> performers=new ArrayList<Performer>();
			while(rs.nextRow()){
				ContentObject co=rs.getColumn(0);
				List<SingleAttributeObjectBlock> facilityBlocks=new ArrayList<SingleAttributeObjectBlock>();
				if(co.getComplexObjectBlock() instanceof SingleAttributeObjectBlock) {
					facilityBlocks.add((SingleAttributeObjectBlock)co.getComplexObjectBlock());
				} else {
					MultipleAttributeObjectBlock mb=(MultipleAttributeObjectBlock)co.getComplexObjectBlock();
					for(KeyedObject ob : mb.getKeyObjects()) {
						facilityBlocks.add((SingleAttributeObjectBlock)ob.getObject());
					}
				}
				for(SingleAttributeObjectBlock sb : facilityBlocks) {
					performers.add((Performer)this.referenceModelManager.bindSingleAttributeObjectBlock(sb));
				}
			}
			return performers;
		} catch(RejectException e) {
			throw e;
		} catch(Exception e) {
			e.printStackTrace();
			throw new RejectException("",CSReason.REAS02);
		}
		/*try {
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
		}*/
		
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
	public boolean getUserExists(long userId) throws RejectException {
		try {
			SEQLResultSet rs = this.query("getUserExists", "SELECT p/identifier FROM EHR SYSTEM e CONTAINS PERFORMER p WHERE p/identifier/extension="+userId+";");
			try {
				if(rs.nextRow())
					//try to bind and if error no user returned
					this.referenceModelManager.bind(rs.getColumn(0));
				else
					return false;
			} catch (Exception e) {
				return false;
			}
			return true;
		} catch(Exception e) {
			throw new RejectException(e.getMessage(),CSReason.REAS02);
		}
	}
	@Override
	public List<IntSIIEReportSummary> getAllReportSummaries()
			throws RejectException {
		ArrayList<IntSIIEReportSummary> rtn = new ArrayList<IntSIIEReportSummary>();
		try {
			//SEQLResultSet rs = this.query("csig", "SELECT e/ehr_id, e/subject_of_care,  e/time_created,  c/rc_id, c/audit_info/time_committed, c/composer/performer, c/composer/healthcare_facility FROM EHR e CONTAINS COMPOSITION c[CEN-EN13606-COMPOSITION.InformeClinicoNotaSOIP.v1];");
			SEQLResultSet rs = this.query("", "SELECT e,  c, c/committal, c/composer FROM EHR e CONTAINS COMPOSITION c[CEN-EN13606-COMPOSITION.InformeClinicoNotaSOIP.v1];");
			while(rs.nextRow()) {
				//Columna 0 bind a EHRExtract
				//Columna 1 bind a Composition
				//Columna 2 bind a AuditInfo
				//Columna 3 bind a FunctionalRole 
				IntSIIEReportSummary r = new WSIntSIIEReportSummary(
						(EHRExtract)this.referenceModelManager.bind(rs.getColumn(0)),
						(Composition)this.referenceModelManager.bind(rs.getColumn(1)),
						(AuditInfo)this.referenceModelManager.bind(rs.getColumn(2)),
						(FunctionalRole)this.referenceModelManager.bind(rs.getColumn(3))
						);
				rtn.add(r);
			}
		} catch(Exception e) {
			throw new RejectException(e.getMessage(),CSReason.REAS02);
		}
		return rtn;
	}
	@Override
	public List<Element> getReportSoip(long reportId) throws RejectException {
		ArrayList<Element> rtn = new ArrayList<Element>();
		try {
			//SELECT r/items[at0002]/parts[at0003] FROM EHR e CONTAINS COMPOSITION c[CEN-EN13606-COMPOSITION.InformeClinicoNotaSOIP.v1] CONTAINS ENTRY r[CEN-EN13606-ENTRY.Informacion.v1];
			SEQLResultSet rs = this.query("","SELECT  r/items[at0002]/parts[at0003], r/items[at0002]/parts[at0004], r/items[at0002]/parts[at0005], r/items[at0002]/parts[at0006] "
					+ "FROM EHR e CONTAINS COMPOSITION c[CEN-EN13606-COMPOSITION.InformeClinicoNotaSOIP.v1] CONTAINS ENTRY r[CEN-EN13606-ENTRY.Informacion.v1] "
					+ "WHERE c/rc_id/extension="+reportId+";");
			if(rs.nextRow()) {
				rtn.add((Element)this.referenceModelManager.bind(rs.getColumn(0)));//Element bias 
				rtn.add((Element)this.referenceModelManager.bind(rs.getColumn(1)));//Element unbias
				rtn.add((Element)this.referenceModelManager.bind(rs.getColumn(2)));//Element impr
				rtn.add((Element)this.referenceModelManager.bind(rs.getColumn(0)));//Element plan
			}			
		} catch(Exception e) {
			throw new RejectException(e.getMessage(), CSReason.REAS02);
		}
		return rtn;
	}

}
