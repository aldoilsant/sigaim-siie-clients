package org.sigaim.siie.clients.ws.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sigaim.siie.clients.ws.WSIntSIIE001EQLClient;
import org.sigaim.siie.clients.ws.WSIntSIIE003TerminologiesClient;
import org.sigaim.siie.dadl.DADLManager;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.rm.ReferenceModelManager;
import org.sigaim.siie.rm.ReflectorReferenceModelManager;

public class TestSynonyms {
	private DADLManager dadlManager;
	private ReferenceModelManager referenceModelManager;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		WSIntSIIE003TerminologiesClient client=new WSIntSIIE003TerminologiesClient();
		this.dadlManager=new OpenEHRDADLManager();
		this.referenceModelManager=new ReflectorReferenceModelManager(this.dadlManager);
		List<String> concepts=new ArrayList<String>();
		CDCV concept= new CDCV();
		concept.setCode("394715003");
		concept.setCodeSystemName("SNOMED-CT");
		concepts.add(this.dadlManager.serialize(this.referenceModelManager.unbind(concept),false));
		concept= new CDCV();
		concept.setCode("S0000001");
		concept.setCodeSystemName("SIGAIM");
		concepts.add(this.dadlManager.serialize(this.referenceModelManager.unbind(concept),false));	
		Map<CDCV,Set<CDCV>> synonyms=client.getSynonymsForConcepts("", concepts);
		for(Entry<CDCV,Set<CDCV>> entry : synonyms.entrySet()) {
			System.out.println("Synonyms for: "+entry.getKey().getCode());
			for(CDCV syn : entry.getValue()) {
				System.out.println(">>>"+syn.getCode()+ " ("+syn.getDisplayName().getValue()+")");

			}
		}
	}

}
