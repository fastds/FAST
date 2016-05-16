package org.fastds.model;

import java.util.Map;

import org.fastds.service.ExplorerService;

public class MetaDataControl {

    protected double ra ;
    protected double dec ;
    protected Globals globals;  
    protected Long specObjId = null;
    protected Integer clean = null;
    protected Integer mode = null;
    protected String otype = null;
    protected String survey;
    protected Integer imageMJD = null;

    protected ObjectExplorer master = null;
    private ExplorerService explorerService = new ExplorerService();
  
    public MetaDataControl(ObjectExplorer master)
    {
    	globals = master.globals;
    	this.master = master;
        if (master.objID != null && !master.objID.equals(""))
        	executeQuery();      
    }
    private void executeQuery()
    {
        Map<String,Object> params = null;
        params = explorerService.findAttrsFromPhotoAndSpec(master.objID);
        if(params !=null && params.size()!=0)
        {
        	ra = (Double)params.get("ra");
            dec = (Double)params.get("dec");
            specObjId = (Long)params.get("specObjID") == 0 ? -999999 : (Long)(params.get("specObjID"));                       
            clean = (Integer)params.get("clean");
            survey = params.get("survey") == null ? null:(String)params.get("survey");
            mode = (Integer)params.get("mode");
            otype = params.get("otype") ==null ? null : (String)params.get("otype");
            imageMJD = (Integer)params.get("mjd");
        }
    }
	public double getRa() {
		return ra;
	}
	public double getDec() {
		return dec;
	}
	public Long getSpecObjId() {
		return specObjId;
	}
	public Integer getClean() {
		return clean;
	}
	public Integer getMode() {
		return mode;
	}
	public String getOtype() {
		return otype;
	}
	public String getSurvey() {
		return survey;
	}
	public Integer getImageMJD() {
		return imageMJD;
	}
	public ObjectExplorer getMaster() {
		return master;
	}
	public ExplorerService getExplorerService() {
		return explorerService;
	}
    
}
