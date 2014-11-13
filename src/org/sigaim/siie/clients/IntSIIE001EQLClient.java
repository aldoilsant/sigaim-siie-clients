package org.sigaim.siie.clients;

import java.util.List;
import java.util.Set;

import org.openehr.am.parser.ContentObject;
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.iso13606.rm.Cluster;
import org.sigaim.siie.iso13606.rm.Element;
import org.sigaim.siie.iso13606.rm.HealthcareFacility;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.IVLTS;
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
	public List<IntSIIEReportSummary> getAllReportSummariesForVersionSet(II versionSet)
			throws RejectException;
	Cluster getConceptInformationForReportId(II reportId) throws RejectException;
	boolean getUserExists(long userId) throws RejectException;
	List<Element> getReportSoip(long reportId) throws RejectException;
	II getEHRIdFromSubject(long subjectId) throws RejectException;
	//ISO 13606-5 proxy method
	public ContentObject requestEhrExtract(
			String requestId,  
			II subjectOfCareId, //Mandatory
			CDCV purpose, //Optional, purpopse of the EHR extract, IGNORED
			Set<II> rc_ids, //Explicits rc_ids for components to be included.
			IVLTS time_period, //Date or time interval for data
			int max_sensitivity, //Max_sensitivity, IGNORED
			boolean all_versions, //Latest version if false
			boolean multimedia_included, //Include multimedia, IGNORED
			Set<II> archetype_ids, //record components matching archetype ids
			Set<CDCV> meanings //meaning attribute match
			) throws RejectException;
}
