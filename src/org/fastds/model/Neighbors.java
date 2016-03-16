package org.fastds.model;

import java.sql.ResultSet;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.service.ExplorerService;

public class Neighbors {
	protected Globals globals;
    protected ObjectExplorer master;
    protected String objID;

    protected ResultSet ds_neighbor1, ds_neighbor2;
    private ExplorerService explorerService = new ExplorerService();
    public Neighbors(ObjectExplorer master)
    {
    	this.master = master;
    }
//    protected void load(ObjectExplorer master)
//    {
//        globals = (Globals)Application[Globals.PROPERTY_NAME];
//        master = (ObjectExplorer)Page.Master;
//
//        objId = Request.QueryString["id"];
//
//        executeQuery();
//    }  old  在请求处理部分完成
    
    public void executeQuery() {
        String cmd = ExplorerQueries.neighbors1.replace("@objID", objID);
        ds_neighbor1 = explorerService.runCmd(cmd);

        cmd = ExplorerQueries.neighbors2.replace("@objID", objID);
        ds_neighbor2 = explorerService.runCmd(cmd);

    }

	public ResultSet getDs_neighbor1() {
		return ds_neighbor1;
	}

	public ResultSet getDs_neighbor2() {
		return ds_neighbor2;
	}

	public void setObjID(String objID) {
		this.objID = objID;
	}

}
