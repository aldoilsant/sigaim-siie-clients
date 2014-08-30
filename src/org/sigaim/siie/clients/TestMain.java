package org.sigaim.siie.clients;

import org.sigaim.siie.clients.ws.WSIntSIIE001EQLClient;
import org.sigaim.siie.clients.ws.WSIntSIIE004ReportManagementClient;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.iso13606.rm.Composition;
import org.sigaim.siie.iso13606.rm.EHRExtract;
import org.sigaim.siie.iso13606.rm.FunctionalRole;
import org.sigaim.siie.iso13606.rm.HealthcareFacility;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.Performer;
import org.sigaim.siie.seql.model.SEQLResultSet;

public class TestMain {
	
	public static void main(String[] args) {
		WSIntSIIE004ReportManagementClient client;
		WSIntSIIE001EQLClient eqlClient;

		OpenEHRDADLManager manager=new OpenEHRDADLManager();

		if(args.length==2) {
			//http://localhost:8080/SIIEWS/services/INTSIIE001EQLImpl
			client=new WSIntSIIE004ReportManagementClient(args[0]);
			eqlClient=new WSIntSIIE001EQLClient(args[1]);
		} else if(args.length==0) {
			client=new WSIntSIIE004ReportManagementClient();
			eqlClient=new WSIntSIIE001EQLClient();
		} else {
			System.err.println("Expected 0 parameters (connection to localhost:8080) or 2 parameters with the endpoint urls (by default,  http://localhost:8080/SIIEWS/services/INTSIIE004ReportManagementImpl  http://localhost:8080/SIIEWS/services/INTSIIE001EQLImpl)");
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
			Composition newReport=client.createReport("4", newEHR.getEhrId(), composer, "some text", true, rootArchetypeId);
			System.out.println("New report id: "+newReport.getRcId().getRoot()+" "+newReport.getRcId().getExtension());
			SEQLResultSet rs=eqlClient.query("1", "SELECT c FROM EHR CONTAINS COMPOSITION c WHERE c/rc_id/extension="+newReport.getRcId().getExtension()+";");
			int nrow=0;
			while(rs.nextRow()) {
				int i;
				for(i=0;i<rs.getNumberOfColumns();i++) {
					System.out.println("Row: "+nrow+" Column: "+i+": "+manager.serialize(rs.getColumn(i),false));
				}
				nrow++;
			} 
			if(nrow==0) {
				System.out.println("No results found...");
			}
			System.out.println("ALL DONE!");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
