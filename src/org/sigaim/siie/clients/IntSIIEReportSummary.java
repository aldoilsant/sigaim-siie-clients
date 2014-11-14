package org.sigaim.siie.clients;

import java.util.Date;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.sigaim.siie.iso13606.rm.AuditInfo;
import org.sigaim.siie.iso13606.rm.Composition;
import org.sigaim.siie.iso13606.rm.EHRExtract;
import org.sigaim.siie.iso13606.rm.FunctionalRole;
import org.sigaim.siie.iso13606.rm.II;

public interface IntSIIEReportSummary {
	II getEhr();
	II getSubject();
	II getId();
	XMLGregorianCalendar getCreationDate();
	XMLGregorianCalendar getCommisionDate();
	II getPerformer();
	II getHCFacility();
	public II getVersionSet();
}
