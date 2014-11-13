package org.sigaim.siie.clients.ws.tests;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.Timer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openehr.am.parser.ContentObject;
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
		WSIntSIIE004ReportManagementClient client=new WSIntSIIE004ReportManagementClient("http://sigaim.siie.cesga.es:8080/SIIEWS3/services/INTSIIE004ReportManagementImplService");
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
		OpenEHRDADLManager dadlManager=new OpenEHRDADLManager();
		InputStream dadl=TestReportManagementService.class.getResourceAsStream("/org/sigaim/siie/clients/ws/tests/input/saprm_input.dadl");
		ContentObject input=dadlManager.parseDADL(dadl);
		String serialized=dadlManager.serialize(input,false);
		
		Composition newReport=client.createReport("4", newEHR.getEhrId(), composer, serialized, true, rootArchetypeId);
		System.out.println("New report id: "+newReport.getRcId().getRoot()+" "+newReport.getRcId().getExtension());
		//Update the report
		//newReport=client.updateReport("5", newEHR.getEhrId(), newReport.getRcId(), composer, serialized, true, true, true, rootArchetypeId, null);
		//System.out.println("Updated report id: "+newReport.getRcId().getRoot()+" "+newReport.getRcId().getExtension());

	}
}
