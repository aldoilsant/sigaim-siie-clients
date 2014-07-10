package org.sigaim.siie.clients.ws.tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sigaim.siie.clients.ws.WSIntSIIE001EQLClient;
import org.sigaim.siie.clients.ws.WSIntSIIE004ReportManagementClient;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.iso13606.rm.Cluster;
import org.sigaim.siie.iso13606.rm.Composition;
import org.sigaim.siie.iso13606.rm.EHRExtract;
import org.sigaim.siie.iso13606.rm.Element;
import org.sigaim.siie.iso13606.rm.FunctionalRole;
import org.sigaim.siie.iso13606.rm.HealthcareFacility;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.INT;
import org.sigaim.siie.iso13606.rm.Item;
import org.sigaim.siie.iso13606.rm.Performer;
import org.sigaim.siie.iso13606.rm.ST;
import org.sigaim.siie.iso13606.rm.SubjectOfCare;
import org.sigaim.siie.seql.model.SEQLResultSet;

public class TestSEQLWebService {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	String[] queries={
		"SELECT e/all_subjects_of_care FROM EHR SYSTEM e",
		"SELECT e/all_performers FROM EHR SYSTEM e",
		"SELECT "
	};
	@Test
	public void test() throws Exception {
		OpenEHRDADLManager manager=new OpenEHRDADLManager();
		WSIntSIIE001EQLClient eqlClient=new WSIntSIIE001EQLClient();
		/*List<HealthcareFacility> hfs=eqlClient.getAllHealthcareFacilities();
		System.out.println("Total healtchare facilities: "+hfs.size());*/
		II reportId=new II();
		reportId.setExtension("6");
		Cluster concepts=eqlClient.getConceptInformationForReportId(reportId);
		System.out.println("Cluster: "+concepts);
		for(Item i : concepts.getParts()) {
			assert(i instanceof Cluster);
			System.out.println("=======");
			Cluster conceptCluster=(Cluster)i;
			for(Item ii : conceptCluster.getParts()) { //Colletion is ordered, so you can just index when building
				assert(ii instanceof Element);
				Element conceptPropertyElement=(Element)ii;
				System.out.print(conceptPropertyElement.getName().getDisplayName().getValue()+": ");
				if(conceptPropertyElement.getValue() instanceof INT) {
					INT intValue=(INT) conceptPropertyElement.getValue();
					System.out.println(intValue.getValue());
				} else {
					ST stringValue=(ST) conceptPropertyElement.getValue();
					System.out.println(stringValue.getValue());
				}
			}
		}
		/*SEQLResultSet rs=eqlClient.query("1", "SELECT e/items[at0008]/parts[at0009] WITH DESCENDANTS FROM EHR CONTAINS COMPOSITION c CONTAINS ENTRY e[CEN-EN13606-ENTRY.Informacion.v1] WHERE c/rc_id/extension=6;");
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
		}*/
	}

}
