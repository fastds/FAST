package org.fastds.model;

import java.sql.ResultSet;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.service.ExplorerService;

public class AllSpec {
	protected Globals globals;
    protected ObjectExplorer master;
    protected String objID;        
    protected ResultSet ds_spec1, ds_spec2;
    ExplorerService explorerService = new ExplorerService();
    public AllSpec(ObjectExplorer master)
    {
    	this.master = master;            
    }
//    protected void load(ObjectExplorer master)
//    {
//        globals = (Globals)Application[Globals.PROPERTY_NAME]; 
//        objID = Request.QueryString["id"];
//        this.master = master;            
//        executeQuery();
//    }    old  这一部分的代码移动到了请求处理也就是ExplorerResource里面
    
    public String getObjID() {
		return objID;
	}
	public ResultSet getDs_spec1() {
		return ds_spec1;
	}


	public ResultSet getDs_spec2() {
		return ds_spec2;
	}


	public void setObjID(String objID) {
		this.objID = objID;
	}
	public void executeQuery() {
        String cmd = ExplorerQueries.AllSpec1(objID);
        ds_spec1 = explorerService.findAllSpec1RS(objID);

        cmd = ExplorerQueries.AllSpec2(objID);
        ds_spec2 = explorerService.findAllSpec2RS(objID);;
    }

}
