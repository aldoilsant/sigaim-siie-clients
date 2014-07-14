package org.sigaim.siie.clients;

import java.util.List;

import org.sigaim.siie.iso13606.rm.Cluster;
import org.sigaim.siie.iso13606.rm.Element;
import org.sigaim.siie.iso13606.rm.HealthcareFacility;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.Performer;
import org.sigaim.siie.iso13606.rm.SubjectOfCare;
import org.sigaim.siie.rm.exceptions.RejectException;
import org.sigaim.siie.seql.model.SEQLResultSet;

public interface IntSIIE001EQLClient {
	SEQLResultSet query(String requestId, String query) throws RejectException;
	//Helpers
	List<HealthcareFacility> getAllHealthcareFacilities() throws RejectException;
	List<SubjectOfCare> getAllSubjectsOfCare() throws RejectException;
	List<Performer> getAllPerformers() throws RejectException;
	List<IntSIIEReportSummary> getAllReportSummaries() throws RejectException;
	Cluster getConceptInformationForReportId(II reportId) throws RejectException;
	boolean getUserExists(long userId) throws RejectException;
	List<Element> getReportSoip(long reportId) throws RejectException;
	II getEHRIdFromSubject(long subjectId) throws RejectException;
}
