package org.sigaim.siie.clients.ws.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sigaim.siie.clients.ws.WSIntSIIE001EQLClient;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.iso13606.rm.II;

public class TestEHRIdFromSubjectId {

	@Test
	public void test() throws Exception {
			OpenEHRDADLManager manager=new OpenEHRDADLManager();
			WSIntSIIE001EQLClient eqlClient=new WSIntSIIE001EQLClient("http://sigaim.siie.cesga.es:8080/SIIEWS3/services/INTSIIE001EQLImplService");
			II res=eqlClient.getEHRIdFromSubject(4);
			System.out.println(res);
	}

}
