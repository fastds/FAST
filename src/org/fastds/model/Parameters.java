package org.fastds.model;

import org.fastds.explorehelpers.ExplorerQueries;

public class Parameters {
	 protected Globals globals;
     protected Long id = null;
     protected Long specId = null;

     //protected String link;
     protected String idString;

     protected ObjectExplorer master;

     protected String fitsParametersSppParams, fitsParametersStellarMassStarformingPort, fitsParameterSstellarMassPassivePort, fitsParametersEmissionLinesPort,
                      fitsParametersStellarMassPCAWiscBC03, fitsParametersstellarMassPCAWiscM11, fitsParametersStellarmassFSPSGranEarlyDust,
                      fitsParametersStellarmassFSPSGranEarlyNoDust, fitsParametersStellarmassFSPSGranWideDust, fitsParametersStellarmassFSPSGranWideNoDust;
     
     public Parameters(ObjectExplorer master)
     {
    	 this.globals = master.getGlobals();
    	 this.master = master;
    	 
     }
      
     
     public Long getSpecId() {
		return specId;
	}


	public void setSpecId(Long specId) {
		this.specId = specId;
	}


	public void getQueries() {

         fitsParametersSppParams = ExplorerQueries.getFitsParametersSppParams(specId.toString());

         fitsParametersStellarMassStarformingPort = ExplorerQueries.getFitsParametersStellarMassStarformingPort(specId.toString());

         fitsParameterSstellarMassPassivePort = ExplorerQueries.getFitsParameterSstellarMassPassivePort(specId.toString());

         fitsParametersEmissionLinesPort = ExplorerQueries.getFitsParametersEmissionLinesPort(specId.toString());

         fitsParametersStellarMassPCAWiscBC03 = ExplorerQueries.getFitsParametersStellarMassPCAWiscBC03(specId.toString());

         fitsParametersstellarMassPCAWiscM11 = ExplorerQueries.fitsParametersstellarMassPCAWiscM11.replace("@specId", specId.toString());

         fitsParametersStellarmassFSPSGranEarlyDust = ExplorerQueries.getFitsParametersStellarmassFSPSGranEarlyDust(specId.toString());

         fitsParametersStellarmassFSPSGranEarlyNoDust = ExplorerQueries.getFitsParametersStellarmassFSPSGranEarlyNoDust(specId.toString());

         fitsParametersStellarmassFSPSGranWideDust = ExplorerQueries.getFitsParametersStellarmassFSPSGranWideDust(specId.toString());

         fitsParametersStellarmassFSPSGranWideNoDust = ExplorerQueries.getFitsParametersStellarmassFSPSGranWideNoDust(specId.toString());
        
     }

}
