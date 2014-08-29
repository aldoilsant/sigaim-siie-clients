package org.sigaim.siie.clients;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sigaim.siie.iso13606.rm.CDCV;
import org.sigaim.siie.rm.exceptions.RejectException;

public interface IntSIIE003Terminologies {
	public Map<CDCV,Set<CDCV>>  getSynonymsForConcepts(String requestId,List<String> concepts) throws RejectException;
}
