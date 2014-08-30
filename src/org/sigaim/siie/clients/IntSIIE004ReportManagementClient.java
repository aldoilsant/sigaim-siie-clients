package org.sigaim.siie.clients;

import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.iso13606.rm.Cluster;
import org.sigaim.siie.iso13606.rm.Composition;
import org.sigaim.siie.iso13606.rm.EHRExtract;
import org.sigaim.siie.iso13606.rm.FunctionalRole;
import org.sigaim.siie.iso13606.rm.HealthcareFacility;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.Performer;
import org.sigaim.siie.iso13606.rm.SubjectOfCare;
import org.sigaim.siie.iso13606.rm.VersionStatus;
import org.sigaim.siie.rm.exceptions.RejectException;


public interface IntSIIE004ReportManagementClient {
	public HealthcareFacility createHealthcareFacility(String requestId) throws RejectException;
	public EHRExtract createSubjectOfCare(String requestId) throws RejectException;
	public Performer createPerformer(String requestId) throws RejectException;
	public Composition createReport(String requestId, II subjectOfCareId,
			FunctionalRole composerId, 
			String textTranscription, boolean dictated, II rootArchetypeId)
			throws RejectException;
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
			) throws RejectException;
}
