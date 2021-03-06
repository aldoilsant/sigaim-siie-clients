package org.sigaim.siie.clients;

import org.sigaim.siie.clients.ws.WSIntSIIE001EQLClient;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.seql.model.SEQLResultSet;

public class TestSEQLMain {
	public static void main(String[] args) {
		WSIntSIIE001EQLClient eqlClient;
		String query;
		
		OpenEHRDADLManager manager=new OpenEHRDADLManager();

		if(args.length==2) {
			//http://localhost:8080/SIIEWS/services/INTSIIE001EQLImpl
			eqlClient=new WSIntSIIE001EQLClient(args[0]);
			query=args[1];
		} else if(args.length==1) {
			eqlClient=new WSIntSIIE001EQLClient();
			query=args[0];
		} else {
			System.err.println("Usage: java -jar ... [endpoint_url] eql_query. E.g. java -jar ... \"SELECT c FROM EHR CONTAINS COMPOSITION c;\"");
			return;
		}
		System.out.println("Running query: "+query);
		try{
			SEQLResultSet rs=eqlClient.query("1", query);
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
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
