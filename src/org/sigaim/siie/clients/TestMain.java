package org.sigaim.siie.clients;

import org.sigaim.siie.clients.ws.WSIntSIIE004ReportManagementClient;
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.iso13606.rm.Composition;
import org.sigaim.siie.iso13606.rm.EHRExtract;
import org.sigaim.siie.iso13606.rm.FunctionalRole;
import org.sigaim.siie.iso13606.rm.HealthcareFacility;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.Performer;

public class TestMain {
	
	public static void main(String[] args) {
		WSIntSIIE004ReportManagementClient client;
		if(args.length==1) {
			//http://localhost:8080/SIIEWS/services/INTSIIE001EQLImpl
			client=new WSIntSIIE004ReportManagementClient(args[0]);
		} else if(args.length==0) {
			client=new WSIntSIIE004ReportManagementClient();
		} else {
			System.err.println("Expected 0 parameters (connection to localhost:8080) or 1 parameter (endpoint url, like http://localhost:8080/SIIEWS/services/INTSIIE004ReportManagementImpl)");
			return;
		}
		try{
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
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
