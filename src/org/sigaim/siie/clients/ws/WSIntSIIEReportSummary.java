package org.sigaim.siie.clients.ws;

import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

import org.sigaim.siie.clients.IntSIIEReportSummary;
import org.sigaim.siie.iso13606.rm.AuditInfo;
import org.sigaim.siie.iso13606.rm.Composition;
import org.sigaim.siie.iso13606.rm.EHRExtract;
import org.sigaim.siie.iso13606.rm.FunctionalRole;
import org.sigaim.siie.iso13606.rm.II;

public class WSIntSIIEReportSummary implements IntSIIEReportSummary {

	EHRExtract ehr;
	Composition comp;
	AuditInfo audit;
	FunctionalRole funcrole;
	
	public WSIntSIIEReportSummary(EHRExtract ehrExtract, Composition composition, AuditInfo auditInfo,
			FunctionalRole functionalRole) {
		ehr = ehrExtract;
		comp = composition;
		audit = auditInfo;
		funcrole = functionalRole;
	}

	@Override
	public II getEhr() {
		return ehr.getEhrId();
	}

	@Override
	public II getSubject() {
		return ehr.getSubjectOfCare();
	}

	@Override
	public II getId() {
		return comp.getRcId();
	}

	@Override
	public XMLGregorianCalendar getCreationDate() {
		return ehr.getTimeCreated();
	}

	@Override
	public XMLGregorianCalendar getCommisionDate() {
		return comp.getCommittal().getTimeCommitted();
	}

	@Override
	public II getPerformer() {
		return funcrole.getPerformer();
	}

	@Override
	public II getHCFacility() {
		return comp.getComposer().getHealthcareFacility();
	}
	
	@Override
	public II getVersionSet(){
		return comp.getCommittal().getVersionSetId();
	}
	
}
