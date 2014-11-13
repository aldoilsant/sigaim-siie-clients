package org.sigaim.siie.clients.ws.tests;

import static org.junit.Assert.*;

import org.junit.Test;
import org.sigaim.siie.clients.ws.WSIntSIIE001EQLClient;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.seql.model.SEQLResultSet;

public class SimpleEQLTest {

	@Test
	public void test() throws Exception{
		OpenEHRDADLManager manager=new OpenEHRDADLManager();
		WSIntSIIE001EQLClient eqlClient=new WSIntSIIE001EQLClient("http://sigaim.siie.cesga.es:8080/SIIEWS3/services/INTSIIE001EQLImplService");
		SEQLResultSet result=eqlClient.query("", "SELECT e/subject_of_care FROM EHR e;");
		System.out.println("Number of rows: "+result.getNumberOfRows());
		int row=0;
		while(result.nextRow()) {
			int i;
			for(i=0;i<result.getNumberOfColumns();i++) {
				System.out.println("Row "+row+",  Column: "+i);
				System.out.println(manager.serialize(result.getColumn(i),false));
			}
			row++;
		}
	}

}
