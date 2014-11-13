package org.sigaim.siie.clients.ws.tests;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.openehr.am.parser.ContentObject;
import org.sigaim.siie.clients.ws.WSIntSIIE001EQLClient;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.IVLTS;
import org.sigaim.siie.iso13606.rm.TS;

public class TestISO136065 {

	@Test
	public void test()  throws Exception{
		WSIntSIIE001EQLClient eqlClient=new WSIntSIIE001EQLClient("http://sigaim.siie.cesga.es:8080/SIIEWS3/services/INTSIIE001EQLImplService");
		OpenEHRDADLManager dadlManager=new OpenEHRDADLManager();

		II subjectOfCareId= new II();
		subjectOfCareId.setRoot("org.sigaim");
		subjectOfCareId.setExtension("3");
		II testRcId=new II();
		testRcId.setRoot("org.sigaim");
		testRcId.setExtension("9");
		Set<II> rc_ids=new HashSet<II>();
		rc_ids.add(testRcId);
		testRcId=new II();
		testRcId.setRoot("org.sigaim");
		testRcId.setExtension("40");
		rc_ids.add(testRcId);
		
		Set<II> archetypeIds= new HashSet<II> ();
		testRcId=new II();
		testRcId.setRoot("CEN-EN13606-SECTION.ImpresionMedica.v1");
		archetypeIds.add(testRcId);
		
		Set<CDCV> meanings= new HashSet<CDCV>();
		CDCV testCode=new CDCV();
		testCode.setCode("at0009");
		testCode.setCodeSystemName("CEN-EN13606-ENTRY.Informacion.v1");
		meanings.add(testCode);
		
		IVLTS time_period= new IVLTS();
		TS low=new TS();
		low.setValue("2014-08-21T04:50:29.923+02:00");
		time_period.setLow(low);
		time_period.setLowClosed(true);
		
		
		ContentObject ret=eqlClient.requestEhrExtract("", subjectOfCareId, null, null, null, 0, true, false, null, null);
		System.out.println(dadlManager.serialize(ret,false));
	}

}
