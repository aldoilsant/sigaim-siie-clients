package org.sigaim.siie.clients.ws.tests;

import static org.junit.Assert.*;

import java.util.Timer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sigaim.siie.clients.ws.WSIntSIIE004ReportManagementClient;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.iso13606.rm.Composition;
import org.sigaim.siie.iso13606.rm.EHRExtract;
import org.sigaim.siie.iso13606.rm.FunctionalRole;
import org.sigaim.siie.iso13606.rm.HealthcareFacility;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.Performer;
import org.sigaim.siie.iso13606.rm.SubjectOfCare;

public class TestReportManagementService {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		WSIntSIIE004ReportManagementClient client=new WSIntSIIE004ReportManagementClient();
		HealthcareFacility newFacility=client.createHealthcareFacility("1");
		System.out.println("New facility: "+newFacility.getIdentifier().getRoot()+" "+newFacility.getIdentifier().getExtension());
		EHRExtract newEHR=client.createSubjectOfCare("2");
		System.out.println("New subject: "+newEHR.getEhrId().getRoot()+" "+newEHR.getEhrId().getExtension());
		Performer newPerformer=client.createPerformer("3");
		System.out.println("New performer: "+newPerformer.getIdentifier().getRoot()+" "+newPerformer.getIdentifier().getExtension());
		FunctionalRole composer=new FunctionalRole();
		composer.setHealthcareFacility(newFacility.getIdentifier());
		composer.setPerformer(newPerformer.getIdentifier());
		II rootArchetypeId= new II();
		rootArchetypeId.setRoot("CEN-EN13606-COMPOSITION.InformeClinicoNotaSOIP.v1");
		CDCV reportStatus=new CDCV();
		reportStatus.setCode("RSTA02");
		Composition newReport=client.createReport("4", newEHR.getEhrId(), composer, "", "some text", reportStatus, rootArchetypeId);
		System.out.println("New report id: "+newReport.getRcId().getRoot()+" "+newReport.getRcId().getExtension());
	}
}
