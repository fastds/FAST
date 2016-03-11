package org.fastds.model;

import java.sql.ResultSet;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.service.ExplorerService;

public class Matches {
	protected String objID;

    protected Globals globals;
    protected ObjectExplorer master;

    protected ResultSet ds_match1, ds_match2;
    private ExplorerService explorerService = new ExplorerService();
    public Matches(ObjectExplorer master)
    {
    	this.master = master;
    }
//    protected void load(ObjectExplorer master)
//    {
//        globals = (Globals)Application[Globals.PROPERTY_NAME];
//        objID = Request.QueryString["id"];
//        master = (ObjectExplorer)Page.Master;
//        runQuery = new RunQuery();
//        executeQueries();
//    } old
    
    public void executeQueries() {
        String cmd = ExplorerQueries.getMatches1(objID);
        this.ds_match1 = explorerService.runCmd(cmd);
        

        cmd = ExplorerQueries.getMatches2(objID);
        this.ds_match2 = explorerService.runCmd(cmd);
    }

	public ResultSet getDs_match1() {
		return ds_match1;
	}

	public ResultSet getDs_match2() {
		return ds_match2;
	}

	public void setObjID(String objID) {
		this.objID = objID;
	}

}
