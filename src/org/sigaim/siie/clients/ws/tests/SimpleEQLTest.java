package org.sigaim.siie.clients.ws.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sigaim.siie.clients.ws.WSIntSIIE001EQLClient;
import org.sigaim.siie.dadl.OpenEHRDADLManager;

public class SimpleEQLTest {

	@Test
	public void test() throws Exception{
		OpenEHRDADLManager manager=new OpenEHRDADLManager();
		WSIntSIIE001EQLClient eqlClient=new WSIntSIIE001EQLClient("http://sigaim.siie.cesga.es:8080/SIIEWS3/services/INTSIIE001EQLImplService");
		eqlClient.query("", "SELECT MERGED AS m e,r FROM EHR e CONTAINS ALL VERSIONS OF RECORD_COMPONENT r WHERE e/subject_of_care/extension=\"3\" AND e/subject_of_care/root=\"org.sigaim\";");
	}

}
