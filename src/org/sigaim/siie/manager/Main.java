package org.sigaim.siie.manager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import org.openehr.am.parser.ContentObject;
import org.sigaim.siie.clients.ws.WSIntSIIE001EQLClient;
import org.sigaim.siie.clients.ws.WSIntSIIE003TerminologiesClient;
import org.sigaim.siie.clients.ws.WSIntSIIE004ReportManagementClient;
import org.sigaim.siie.dadl.DADLManager;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.iso13606.rm.Cluster;
import org.sigaim.siie.iso13606.rm.Composition;
import org.sigaim.siie.iso13606.rm.EHRExtract;
import org.sigaim.siie.iso13606.rm.FunctionalRole;
import org.sigaim.siie.iso13606.rm.HealthcareFacility;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.Performer;
import org.sigaim.siie.rm.ReferenceModelManager;
import org.sigaim.siie.rm.ReflectorReferenceModelManager;
import org.sigaim.siie.seql.model.SEQLResultSet;

public class Main {

	private static II stringToII(String input) {
		String[] parts=input.split("/");
		if(parts.length!=2) {
			throw new IllegalArgumentException("Invalid II string: "+input);
		} else {
			II ret=new II();
			ret.setRoot(parts[0]);
			ret.setExtension(parts[1]);
			return ret;
		}
	}
	private static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  String content = new Scanner(new File(path),encoding.name()).useDelimiter("\\A").next();
			  return content;
			}
	public static void main(String[] args) throws Exception {
		DADLManager  dadlManager=new OpenEHRDADLManager();
		ReferenceModelManager referenceModelManager=new ReflectorReferenceModelManager(dadlManager);
		if(args.length==0 || args.length==1) {
			System.err.println("Usage: siiemng <endpoint> <operation> [parameter] [parameter...]");
			System.err.println("Where operation is one of: ");
			System.err.println("create_report ehr_id healthcare_facility_id perfomer_id dictated soip_file");
			System.err.println("create_subject_of_care");
			System.err.println("create_healthcare_facility");
			System.err.println("create_perfomer");
			System.err.println("update_report ehr_id previous_version_id healthcare_facility_id perfomer_id dictated signed confirmed soip_file updated_concepts_file");
			System.err.println("query <query_string>");
			System.err.println("get_synonyms_for <concept_code> <terminology_code>");
			return;
		}
		String endpoint=args[0];
		String operation= args[1];
		if(operation.equals("query")) {
			if(args.length!=3) {
				System.err.println("Query operations requires exactly one query parameter");
				return;
			}
			WSIntSIIE001EQLClient eqlClient=new WSIntSIIE001EQLClient(endpoint);
			SEQLResultSet rs=eqlClient.query("", args[2]);
    		if(rs!=null) {
    			int nrow=0;
				while(rs.nextRow()) {
					for(int i=0;i<rs.getNumberOfColumns();i++) {
						ContentObject cellObject=rs.getColumn(i);
						System.out.println("Row "+(nrow)+", Column "+i+": "+dadlManager.serialize(cellObject, false));
					}
					nrow++;
				}
    		}		
    	} else if (operation.equals("create_subject_of_care")) {
    		WSIntSIIE004ReportManagementClient client=new WSIntSIIE004ReportManagementClient(endpoint);
    		EHRExtract res=client.createSubjectOfCare("");
    		System.out.println(res.getEhrId().getRoot()+"/"+res.getEhrId().getExtension());
    	} else if (operation.equals("create_performer")) {
    		WSIntSIIE004ReportManagementClient client=new WSIntSIIE004ReportManagementClient(endpoint);
    		Performer res=client.createPerformer("");
    		System.out.println(res.getIdentifier().getRoot()+"/"+res.getIdentifier().getExtension());
    	} else if (operation.equals("create_healthcare_facility")) {
    		WSIntSIIE004ReportManagementClient client=new WSIntSIIE004ReportManagementClient(endpoint);
    		HealthcareFacility res=client.createHealthcareFacility("");
    		System.out.println(res.getIdentifier().getRoot()+"/"+res.getIdentifier().getExtension());
    	} else if(operation.equals("create_report")) {
    		if(args.length!=7) {
    			System.err.println("Invalid number of arguments for create_report, expected 7, found "+args.length);
    			return; 
    		}
    		WSIntSIIE004ReportManagementClient client=new WSIntSIIE004ReportManagementClient(endpoint);
    		II ehrId=stringToII(args[2]);
    		II healthcareId=stringToII(args[3]);
    		II performerId=stringToII(args[4]);
    		boolean dictated=Boolean.parseBoolean(args[5]);
    		String inputFileName=args[6];
    		String inputText=readFile(inputFileName,Charset.forName("ISO-8859-1"));
    		FunctionalRole composerRole=new FunctionalRole();
    		composerRole.setHealthcareFacility(healthcareId);
    		composerRole.setPerformer(performerId);
    		II rootArchetypeId= new II();
    		rootArchetypeId.setRoot("CEN-EN13606-COMPOSITION.InformeClinicoNotaSOIP.v1");
    		
    		Composition newReport=client.createReport("", ehrId, composerRole, inputText, dictated, rootArchetypeId);
    		System.out.println(dadlManager.serialize(referenceModelManager.unbind(newReport), false));
    	}  else if(operation.equals("update_report")) {
    		WSIntSIIE004ReportManagementClient client=new WSIntSIIE004ReportManagementClient(endpoint);
    		if(args.length!=11) {
    			System.err.println("Invalid number of arguments for update_report, expected 11, found "+args.length);
    			return;
    		}
    		II ehrId=stringToII(args[2]);
    		II previousVersionId=stringToII(args[3]);
    		II healthcareId=stringToII(args[4]);
    		II performerId=stringToII(args[5]);
    		boolean dictated=Boolean.parseBoolean(args[6]);
    		boolean signed=Boolean.parseBoolean(args[7]);
    		boolean confirmed=Boolean.parseBoolean(args[8]);
    		String inputFileName=args[9];
    		String inputText=readFile(inputFileName,Charset.forName("ISO-8859-1"));
    		String conceptFileName=args[10];
    		String encodedConcepts=readFile(conceptFileName,Charset.forName("ISO-8859-1"));
    		FunctionalRole composerRole=new FunctionalRole();
    		composerRole.setHealthcareFacility(healthcareId);
    		composerRole.setPerformer(performerId);
    		II rootArchetypeId= new II();
    		rootArchetypeId.setRoot("CEN-EN13606-COMPOSITION.InformeClinicoNotaSOIP.v1");
    		Composition newReport=client.updateReport("", ehrId, previousVersionId, composerRole, inputText, dictated, signed, confirmed, rootArchetypeId, (Cluster) referenceModelManager.bind(dadlManager.parseDADL(new ByteArrayInputStream(encodedConcepts.getBytes()))));
    		System.out.println(dadlManager.serialize(referenceModelManager.unbind(newReport), false));
    	} else if(operation.equals("get_synonyms_for"))  {
    		WSIntSIIE003TerminologiesClient client=new WSIntSIIE003TerminologiesClient(endpoint);
    		List<String> concepts=new ArrayList<String>();
    		CDCV concept= new CDCV();
    		concept.setCode(args[2]);
    		concept.setCodeSystemName(args[3]);
    		concepts.add(dadlManager.serialize(referenceModelManager.unbind(concept),false));
    		concepts.add(dadlManager.serialize(referenceModelManager.unbind(concept),false));	
    		Map<CDCV,Set<CDCV>> synonyms=client.getSynonymsForConcepts("", concepts);
    		for(Entry<CDCV,Set<CDCV>> entry : synonyms.entrySet()) {
    			System.out.println("Synonyms for: "+entry.getKey().getCode());
    			for(CDCV syn : entry.getValue()) {
    				System.out.println(">>>"+syn.getCode()+ " ("+syn.getDisplayName().getValue()+")");

    			}
    		}
    	} else {
    		System.err.println("Uknown operation: "+operation);
    	}
	}

}