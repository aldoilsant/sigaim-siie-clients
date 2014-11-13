package org.sigaim.siie.clients.ws.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.openehr.am.parser.ContentObject;
import org.sigaim.siie.clients.ws.WSIntSIIE001EQLClient;
import org.sigaim.siie.dadl.DADLManager;
import org.sigaim.siie.dadl.OpenEHRDADLManager;
import org.sigaim.siie.dadl.exceptions.SemanticDADLException;
import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.iso13606.rm.II;
import org.sigaim.siie.iso13606.rm.IVLTS;
import org.sigaim.siie.iso13606.rm.SubjectOfCare;
import org.sigaim.siie.iso13606.rm.TS;
import org.sigaim.siie.rm.ReferenceModelManager;
import org.sigaim.siie.rm.ReflectorReferenceModelManager;
import org.sigaim.siie.rm.exceptions.ReferenceModelException;
import org.sigaim.siie.rm.exceptions.RejectException;

public class TestISO136065 {
	WSIntSIIE001EQLClient eqlClient;
	DADLManager dadlManager;
	ReferenceModelManager referenceModelManager;
	
	public ContentObject doRequestEhrExtract(
			String requestId,  
			II subjectOfCareId, //Mandatory
			CDCV purpose, //Optional, purpopse of the EHR extract, IGNORED
			Set<II> rc_ids, //Explicits rc_ids for components to be included.
			IVLTS time_period, //Date or time interval for data
			int max_sensitivity, //Max_sensitivity, IGNORED
			boolean all_versions, //Latest version if false
			boolean multimedia_included, //Include multimedia, IGNORED
			Set<II> archetype_ids, //record components matching archetype ids
			Set<CDCV> meanings //meaning attribute match
			) throws RejectException, SemanticDADLException, ReferenceModelException {
		//Hacer la llamada y recuperar el resulado en dadl
		ContentObject ret=this.eqlClient.requestEhrExtract("", subjectOfCareId, purpose, rc_ids, time_period, max_sensitivity, all_versions, multimedia_included, archetype_ids, meanings);
		if(ret==null) {
			System.out.println("Empty result");
			return null;
		} else {
			//Imprimir el dadl para ver el resultado
			System.out.println(dadlManager.serialize(ret,false));
			//pasarlo a un mapa de path
			/*
			 * 	public Map<String, String> createPathMap(ContentObject obj,
			boolean useArchetypeNodes, boolean useImplicitIndexes,List<String> exclusions) throws SemanticDADLException, ReferenceModelException {
			 * 
			 */
			//obj es lo que devuelve la consulta al SIIE
			//useArchetypeNotes: se incluyen los nodos de arquetipo en el mapa de path: 
			
			///all_compositions[at0000]/content[at0012]/members[at0014]/items[at0008]/parts[at0009][11]/parts[at0014]/rc_id/root 
			//Si es falso, solo se incluye el path del modelo de referencia:
			///all_compositions/content/members/items/parts[11]/parts/rc_id/root 

			//UseImplicitIndexes: tal y como habiamos hablado, si solo hay un objeto para un path, se elimina el índice
			//es decir, no se pone /xxxx[1]/, se pone /xxxx/ solo 
			
			//Exclusions: borra automaticamente los paths que terminan con las cadenas dadas.
			//(por lo que también habiamos hablado)
			//Una vez tengas el mapa realmente ya puedes quitar tu los que quieras.
			List<String> exclusions=new ArrayList<String>();
			exclusions.add("archetype_id");
			exclusions.add("meaning");
			exclusions.add("/reference_model_class_name");
			exclusions.add("/name");
			//La llamada que querrías tu sería:
			Map<String,String> retMap=referenceModelManager.createPathMap(ret, true,true,exclusions);
			printMap(retMap);
			System.out.println("Path Count: "+retMap.size());
			return ret;
		}
	}
	
	@Test
	public void test()  throws Exception{
		eqlClient=new WSIntSIIE001EQLClient("http://sigaim.siie.cesga.es:8080/SIIEWS3/services/INTSIIE001EQLImplService");
		dadlManager=new OpenEHRDADLManager();
		referenceModelManager=new ReflectorReferenceModelManager(dadlManager);

		//Puedes usar este método para listar todos los pacientes:
		
		List<SubjectOfCare> subjectsOfCare=this.eqlClient.getAllSubjectsOfCare();
		
		for(SubjectOfCare subject : subjectsOfCare) {
			//Esto es el II del subject of care, que se usa en la consulta
			System.out.println(subject.getIdentifier().getRoot()+"/"+subject.getIdentifier().getExtension());
		}
		//El subject of care se especifica con un II
		II subjectOfCareId= new II();
		subjectOfCareId.setRoot("org.sigaim");
		subjectOfCareId.setExtension("4");
		//Los rc_ids tambien
		II testRcId=new II();
		testRcId.setRoot("org.sigaim");
		testRcId.setExtension("9");
		Set<II> rc_ids=new HashSet<II>();
		rc_ids.add(testRcId);
		testRcId=new II();
		testRcId.setRoot("org.sigaim");
		testRcId.setExtension("40");
		rc_ids.add(testRcId);
		//Los archetype_id tambien
		Set<II> archetypeIds= new HashSet<II> ();
		testRcId=new II();
		testRcId.setRoot("CEN-EN13606-SECTION.ImpresionMedica.v1");
		archetypeIds.add(testRcId);
		
		//Meanings con CDV
		Set<CDCV> meanings= new HashSet<CDCV>();
		CDCV testCode=new CDCV();
		testCode.setCode("at0009");
		testCode.setCodeSystemName("CEN-EN13606-ENTRY.Informacion.v1");
		meanings.add(testCode);
		
		//Y time_period con IVLTS
		IVLTS time_period= new IVLTS();
		TS low=new TS();
		low.setValue("2014-08-21T04:50:29.923+02:00");
		time_period.setLow(low);
		time_period.setLowClosed(true);
		
		/*
		 * public ContentObject requestEhrExtract(
			String requestId,  
			II subjectOfCareId, //Mandatory
			CDCV purpose, //Optional, purpose of the EHR extract, IGNORED
			Set<II> rc_ids, //Explicits rc_ids for components to be included.
			IVLTS time_period, //Date or time interval for data
			int max_sensitivity, //Max_sensitivity, IGNORED
			boolean all_versions, //Latest version oliny if false
			boolean multimedia_included, //Include multimedia, IGNORED
			Set<II> archetype_ids, //record components matching archetype ids
			Set<CDCV> meanings //meaning attribute match
			) throws RejectException{
		 * */
		//this.doRequestEhrExtract("", subjectOfCareId, null, rc_ids, time_period, 0, true, false, archetypeIds, meanings);
		//Esta consulta solo especifica el subject of care (obligatorio), entonces devuelve todo el informe
		this.doRequestEhrExtract("", subjectOfCareId, null, null, null, 0, true, false, null, null);
		//Esta consulta especifica a mayores unos valores de rc_id. Entonces solo se devuelven los objetos
		//que encajen con ese rc_id Y TODOS los padres necesarios para cumplir con la norma.
		//(por ejemplo, si el/los rc_ids se corresponden con una entry, se devuelven las compositon, ... etc padres hasta llegar a esa profundidad
		this.doRequestEhrExtract("", subjectOfCareId, null, rc_ids, null, 0, true, false, null, null);
		
		//En vez de los rc_ids puedes especificar archetype_ids. Entonces solo se devuelven los objetos que encajen con esos archetype_ids
		//y los padres necesarios como en la anterior.
				
		this.doRequestEhrExtract("", subjectOfCareId, null, null, null, 0, true, false, archetypeIds, null);

		//Y lo mismo con los meanings
		this.doRequestEhrExtract("", subjectOfCareId, null, null, null, 0, true, false, null, meanings);

		//Tambien puedes especificar un time period para recoger solo un periodo de tiempo
		//Tal y como estamos el tiempo de commital va en las compositions (informes)
		this.doRequestEhrExtract("", subjectOfCareId, null, null, time_period, 0, true, false, null, null);
				
		//Finalmente puedes mezclar todos los parameteros. 
		//Se devuelve siempre el subjconjunto que encaje con todo, tal y como dice la norma
		//Si te pasas mucho, no hay nada que encaje y se devuelve null:
		this.doRequestEhrExtract("", subjectOfCareId, null, rc_ids, time_period, 0, true, false, archetypeIds, meanings);

	}
	
	public void printMap(Map<String,String> map) {
		for(String key : map.keySet()) {
			String value=map.get(key);
			System.out.println(key+" : "+value);
		}
	}

}
