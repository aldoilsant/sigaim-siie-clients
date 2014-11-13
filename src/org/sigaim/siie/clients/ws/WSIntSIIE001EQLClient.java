package org.sigaim.siie.clients.ws;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openehr.am.parser.AttributeValue;
import org.openehr.am.parser.ComplexObjectBlock;
import org.openehr.am.parser.ContentObject;
import org.openehr.am.parser.KeyedObject;
import org.openehr.am.parser.MultipleAttributeObjectBlock;
import org.openehr.am.parser.PrimitiveObjectBlock;
import org.openehr.am.parser.SingleAttributeObjectBlock;
import org.openehr.am.parser.StringValue;
import org.sigaim.siie.clients.IntSIIE001EQLClient;
import org.sigaim.siie.clients.IntSIIEReportSummary;
import org.sigaim.siie.dadl.DADLManager;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.iso13606.rm.AuditInfo;
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.iso13606.rm.Cluster;
import org.sigaim.siie.iso13606.rm.Composition;
import org.sigaim.siie.iso13606.rm.EHRExtract;
import org.sigaim.siie.iso13606.rm.Element;
import org.sigaim.siie.iso13606.rm.ExtractCriteria;
import org.sigaim.siie.iso13606.rm.FunctionalRole;
import org.sigaim.siie.iso13606.rm.HealthcareFacility;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.IVLTS;
import org.sigaim.siie.iso13606.rm.Performer;
import org.sigaim.siie.iso13606.rm.SubjectOfCare;
import org.sigaim.siie.iso13606.rm.TS;
import org.sigaim.siie.rm.ReferenceModelManager;
import org.sigaim.siie.rm.ReflectorReferenceModelManager;
import org.sigaim.siie.rm.exceptions.CSReason;
import org.sigaim.siie.rm.exceptions.RejectException;
import org.sigaim.siie.seql.model.SEQLException;
import org.sigaim.siie.seql.model.SEQLResultSet;
import org.sigaim.siie.seql.model.SEQLSelectCondition;
import org.sigaim.siie.ws2.INTSIIE001EQLImplServiceStub;
import org.sigaim.siie.ws2.INTSIIE001EQLImplServiceStub.QueryResponseE;
import org.sigaim.siie.ws2.INTSIIE001EQLImplServiceStub.WsReturnValueEQL;

public class WSIntSIIE001EQLClient implements  IntSIIE001EQLClient {
	private INTSIIE001EQLImplServiceStub proxy;
	private DADLManager dadlManager;
	private ReferenceModelManager referenceModelManager;
	
	public WSIntSIIE001EQLClient() {
		this(null);
	}
	public WSIntSIIE001EQLClient(String endpoint) {
		try {
			if(endpoint==null) {
				this.proxy=new INTSIIE001EQLImplServiceStub();
			} else {
				this.proxy=new INTSIIE001EQLImplServiceStub(endpoint);
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
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
		WsReturnValueEQL ret;
		try {
			INTSIIE001EQLImplServiceStub.Query query= new INTSIIE001EQLImplServiceStub.Query();
			query.setArg0(requestId);
			query.setArg1(eqlQuery);
			INTSIIE001EQLImplServiceStub.QueryE querye= new INTSIIE001EQLImplServiceStub.QueryE();
			querye.setQuery(query);
			QueryResponseE response=proxy.query(querye);
			ret=response.getQueryResponse().get_return();
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
			SEQLResultSet rs=this.query("", "SELECT e/items[at0008] WITH DESCENDANTS FROM EHR CONTAINS COMPOSITION c CONTAINS ENTRY e[CEN-EN13606-ENTRY.Informacion.v1] WHERE c/rc_id/extension=\""+reportId.getExtension()+"\";");
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
	//FIXME: should return II of user
	public boolean getUserExists(long userId) throws RejectException {
		try {
			SEQLResultSet rs = this.query("getUserExists", "SELECT p/identifier FROM EHR SYSTEM e CONTAINS PERFORMER p WHERE p/identifier/extension=\""+userId+"\";");
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
	public List<IntSIIEReportSummary> getAllReportSummariesForVersionSet(II versionSet)
			throws RejectException {
		ArrayList<IntSIIEReportSummary> rtn = new ArrayList<IntSIIEReportSummary>();
		try {
			//SEQLResultSet rs = this.query("csig", "SELECT e/ehr_id, e/subject_of_care,  e/time_created,  c/rc_id, c/audit_info/time_committed, c/composer/performer, c/composer/healthcare_facility FROM EHR e CONTAINS COMPOSITION c[CEN-EN13606-COMPOSITION.InformeClinicoNotaSOIP.v1];");
			SEQLResultSet rs = this.query("", "SELECT e,  c, c/committal, c/composer FROM EHR e CONTAINS ALL VERSIONS OF COMPOSITION c[CEN-EN13606-COMPOSITION.InformeClinicoNotaSOIP.v1]; WHERE c/committal/version_set_id/extension=\""+versionSet.getExtension()+"\";");
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
					+ "WHERE c/rc_id/extension=\""+reportId+"\";");
			if(rs.nextRow()) {
				rtn.add((Element)this.referenceModelManager.bind(rs.getColumn(0)));//Element bias 
				rtn.add((Element)this.referenceModelManager.bind(rs.getColumn(1)));//Element unbias
				rtn.add((Element)this.referenceModelManager.bind(rs.getColumn(2)));//Element impr
				rtn.add((Element)this.referenceModelManager.bind(rs.getColumn(3)));//Element plan
			}			
		} catch(Exception e) {
			throw new RejectException(e.getMessage(), CSReason.REAS02);
		}
		return rtn;
	}
	@Override
	public II getEHRIdFromSubject(long subjectId) throws RejectException {
		try {
			SEQLResultSet rs=this.query("", "SELECT e/ehr_id FROM EHR e WHERE e/subject_of_care/extension=\""+subjectId+"\";");
			if(rs.nextRow()){
				return (II)this.referenceModelManager.bind(rs.getColumn(0));
			}		
			return null;
		} catch(RejectException e) {
			throw e;
		} catch(Exception e) {
			e.printStackTrace();
			throw new RejectException("",CSReason.REAS02);
		}
	}
	protected String getDateComparisonFromTS(TS time, boolean closed, boolean high) {
		String operator=null;
		if(closed && high) {
			operator="<=";
		} else if(closed && !high) {
			operator=">=";
		} else if(!closed && high) {
			operator="<";
		} else {
			operator=">";
		}
		return operator + "'"+time.getValue()+"'";
	}
	
	public ContentObject requestEhrExtract(
			String requestId,  
			II subjectOfCareId, //Mandatory
			CDCV purpose, //Optional, purpose of the EHR extract, IGNORED
			Set<II> rc_ids, //Explicits rc_ids for components to be included.
			IVLTS time_period, //Date or time interval for data
			int max_sensitivity, //Max_sensitivity, IGNORED
			boolean all_versions, //Latest version oliny if false
			boolean multimedia_included, //Include multimedia, IGNORED
			Set<II> archetype_ids, //record components matching archetype ids
			Set<CDCV> meanings //meaning attribute match
			) throws RejectException{
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT MERGED AS m e,r FROM EHR e CONTAINS ");
		if(all_versions) {
			queryBuilder.append("ALL VERSIONS OF ");
		}
		queryBuilder.append("RECORD_COMPONENT r WHERE e/subject_of_care/extension=\"");
		queryBuilder.append(subjectOfCareId.getExtension());
		queryBuilder.append("\" AND e/subject_of_care/root=\"");
		queryBuilder.append(subjectOfCareId.getRoot());
		queryBuilder.append("\"");
		boolean areRcIds=rc_ids != null && rc_ids.size()!=0;
		boolean areArchetypeIds=archetype_ids != null && archetype_ids.size()!=0;
		boolean areMeanings=meanings != null && meanings.size()!=0;

		if(areRcIds) {
			boolean nfirst=false;
			queryBuilder.append(" AND (");
			for(II id : rc_ids) {
				if(nfirst) {
					queryBuilder.append(" OR ");
				}
				nfirst=true;
				queryBuilder.append(" (r/rc_id/root=\"");
				queryBuilder.append(id.getRoot());
				queryBuilder.append("\" ");
				queryBuilder.append(" AND r/rc_id/extension=\"");
				queryBuilder.append(id.getExtension());
				queryBuilder.append("\")");
			}
			queryBuilder.append(" )  ");
		}
		if(areArchetypeIds) {
			boolean nfirst=false;
			queryBuilder.append(" AND (");
			for(II id : archetype_ids) {
				if(nfirst) {
					queryBuilder.append(" OR ");
				}
				nfirst=true;
				queryBuilder.append(" (r/archetype_id/root=\"");
				queryBuilder.append(id.getRoot());
				queryBuilder.append("\" ");
				if(id.getExtension()!=null) {
					queryBuilder.append("AND r/archetype_id/extension=\"");
					queryBuilder.append(id.getExtension());
					queryBuilder.append("\"");
				}
				queryBuilder.append(")");
			}
			queryBuilder.append(" )  ");
		}
		if(areMeanings) {
			boolean nfirst=false;
			queryBuilder.append(" AND (");
			for(CDCV id : meanings) {
				if(nfirst) {
					queryBuilder.append(" OR ");
				}
				nfirst=true;
				queryBuilder.append(" (r/meaning/code_system_name=\"");
				queryBuilder.append(id.getCodeSystemName());
				queryBuilder.append("\" ");
				if(id.getCode()!=null) {
					queryBuilder.append(" AND r/meaning/code=\"");
					queryBuilder.append(id.getCode());
					queryBuilder.append("\"");
				}

				queryBuilder.append(")");
			}
			queryBuilder.append(" )  ");
		}
		if(time_period!=null && (time_period.getHigh()!=null || time_period.getLow() != null) ) {
			queryBuilder.append("HAVING ");
			String time=null;
			if(time_period.getHigh()!=null) {
				time=this.getDateComparisonFromTS(time_period.getHigh(), time_period.isHighClosed(), true);
				queryBuilder.append(" (m/reference_model_class_name!= \"Composition\" OR m/committal/time_committed");
				queryBuilder.append(time);
				queryBuilder.append(" ) ");
			}
			if(time_period.getLow()!=null) {
				if(time!=null) {
					queryBuilder.append(" AND ");
				}
				time=this.getDateComparisonFromTS(time_period.getLow(), time_period.isLowClosed(), false);
				queryBuilder.append(" (m/reference_model_class_name!= \"Composition\" OR m/committal/time_committed");
				queryBuilder.append(time);
				queryBuilder.append(" ) ");
			}
		}
		queryBuilder.append(";");
		System.out.println("Query: "+queryBuilder);
		SEQLResultSet rs=this.query(requestId, queryBuilder.toString());
		System.out.println("Query complete: "+queryBuilder);
		if(rs.getNumberOfRows()==0) {
			return null;
		} else {
			try {
				rs.nextRow();
				ContentObject obj= rs.getColumn(0);
				//Modify time_created and add criteria
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
				XMLGregorianCalendar now = datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
				SingleAttributeObjectBlock block=this.referenceModelManager.getSingleAttributeObjectBlockFromContentObject(obj);
				AttributeValue timeCreatedAtt=null;
				for(AttributeValue att : block.getAttributeValues()) {
					if(att.getId().equals("time_created")) {
						timeCreatedAtt=att;
						break;
					}
				}
				block.getAttributeValues().remove(timeCreatedAtt);
				block.getAttributeValues().add(new AttributeValue("time_created",new PrimitiveObjectBlock(null,new StringValue(now.toString()),null,null,null,null)));
				ExtractCriteria crit=new ExtractCriteria();
				crit.setAllVersions(all_versions);
				crit.setMaxSensitivity(BigInteger.valueOf(max_sensitivity));
				crit.setMultimediaIncluded(multimedia_included);
				crit.setTimePeriod(time_period);
				if(archetype_ids!=null) {
					crit.getArchetypeIds().addAll(archetype_ids);
				}
				SingleAttributeObjectBlock critBlock=this.referenceModelManager.getSingleAttributeObjectBlockFromContentObject(this.referenceModelManager.unbind(crit));
				KeyedObject kCrit=new KeyedObject(new StringValue("1"),critBlock);
				List<KeyedObject> kCritList=new ArrayList<KeyedObject>();
				kCritList.add(kCrit);
				MultipleAttributeObjectBlock mblock=new MultipleAttributeObjectBlock(null, kCritList);
				block.getAttributeValues().add(new AttributeValue("criteria", mblock));
				return obj;
			} catch(Exception e) {
				throw new RejectException(requestId, CSReason.REAS02);
			}
		}
	} 
}
