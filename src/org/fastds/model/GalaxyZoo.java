package org.fastds.model;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.service.ExplorerService;

public class GalaxyZoo {
	protected String objID;

    protected Globals globals;
    protected ObjectExplorer master;
    protected String zooSpec;
    protected String zooNoSpec;
    protected String zooConfidence;
    protected String zooMirrorBias;
    protected String zooMonochromeBias;
    
    ExplorerService explorerService = new ExplorerService();
    Map<String,String> show = new HashMap<String,String>();
    public GalaxyZoo(ObjectExplorer master)
    {
        globals = new Globals();
//        objID = Request.QueryString["id"];
        this.master = master;
    }
    public void executeQuery()
    {
    	 String explore = "DisplayResults.aspx?id=" + objID + "&cmd=";
         String cmd =ExplorerQueries.zooSpec.replace("@objID",objID);
         
         zooSpec = explore+cmd+"&name=zooSpec&id="+objID;
         ResultSet ds = explorerService.runCmd(cmd);
         show.put("showZooSpec", master.showHTable(ds, 600, "PhotoObj"));
         
         cmd = ExplorerQueries.zooSpec2.replace("@objID", objID);
         ds =explorerService.runCmd(cmd);
         show.put("showZooSpec2",master.showHTable(ds, 600, "PhotoObj"));
         
         // No spec
         cmd = ExplorerQueries.zooNoSpec.replace("@objID", objID);
         zooNoSpec = explore+cmd+"&name=zooNoSpec&id="+objID;
         
         ds = explorerService.runCmd(cmd);
         show.put("showZooNoSpec", master.showHTable(ds, 600, "PhotoObj"));
         
         // confidence
         cmd = ExplorerQueries.zooConfidence.replace("@objID", objID);
         zooConfidence = explore+cmd+"&name=zooConfidence";
         cmd = ExplorerQueries.zooConfidence2.replace("@objID", objID);
         ds = explorerService.runCmd(cmd);
         show.put("showZooConfidence2", master.showHTable(ds, 600, "PhotoObj"));  

         //zooMirrorBias
         cmd = ExplorerQueries.zooMirrorBias.replace("@objID", objID);
         zooMirrorBias = explore+cmd+"&name=zooMirrorBias";
         
         cmd = ExplorerQueries.zooMirrorBias2.replace("@objID", objID);
         ds = explorerService.runCmd(cmd);  
         show.put("showZooMirrorBias2", master.showHTable(ds, 600, "PhotoObj"));

         //zooMonochromeBias
         cmd = ExplorerQueries.zooMonochromeBias.replace("@objID", objID);
         zooMonochromeBias =  explore+cmd+"&name=zooMonochromeBias";
         
         cmd = ExplorerQueries.zooMonochromeBias2.replace("@objID", objID);
         ds=explorerService.runCmd(cmd);    //  ????源码中这句话是注释掉的
         show.put("showZooMonochromeBias2", master.showHTable(ds, 600, "PhotoObj"));
    }
    
	public Map<String, String> getShow() {
		return show;
	}
	public String getZooSpec() {
		return zooSpec;
	}
	public String getZooNoSpec() {
		return zooNoSpec;
	}
	public String getZooConfidence() {
		return zooConfidence;
	}
	public String getZooMirrorBias() {
		return zooMirrorBias;
	}
	public String getZooMonochromeBias() {
		return zooMonochromeBias;
	}
	public void setObjID(String objID)
    {
    	this.objID = objID;
    }
	public String getObjID() {
		return objID;
	}
	public Globals getGlobals() {
		return globals;
	}
	public ObjectExplorer getMaster() {
		return master;
	}
    
}
