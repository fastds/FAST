package org.fastds.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.service.ExplorerService;

import edu.gzu.domain.PhotoObjAll;
import edu.gzu.image.Functions;

public class Matches {
	protected String objID;

    protected Globals globals;
    protected ObjectExplorer master;

    protected ResultSet ds_match1, ds_match2;
    private ExplorerService explorerService = new ExplorerService();
    public Matches(ObjectExplorer master)
    {
    	this.master = master;
    	this.globals = master.globals;
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
    public Map<Integer, List<Object>> getMapMatches1()
    {
    	Map<Integer, List<Object>> res = new HashMap<Integer,List<Object>>();
		List<Object> values = null;
		int num = 0;
		
		try {
			if (ds_match1!=null && !ds_match1.isAfterLast())
			{
				/*把属性名存储如Map<0,List<>>中*/
		    	values = new ArrayList<Object>();
		    	values.add("IAU name");
		    	values.add("objid");
		    	values.add("thingid");
		    	values.add("mode");
			    res.put(num++,values);
			    values = new ArrayList<Object>();
			    
	            // 添加值到Map当中
			    values.add(Functions.fIAUFromEq(ds_match1.getFloat("ra"), ds_match1.getFloat("dec")));
			    values.add(ds_match1.getLong("objID"));
			    values.add(ds_match1.getLong("thingId"));
			    values.add(Functions.fPhotoModeN(ds_match1.getByte("mode")));
		        res.put(num++,values);
			}
			else {
			    return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return res;
    }
    public Map<Integer, List<Object>> getMapMatches2()
    {
    	Map<Integer, List<Object>> res = new HashMap<Integer,List<Object>>();
		List<Object> values = null;
		int num = 0;
		
		try {
			if (ds_match2!=null && !ds_match2.isAfterLast())
			{
				/*把属性名存储如Map<0,List<>>中*/
		    	values = new ArrayList<Object>();
		    	values.add("objID");
		    	values.add("thingID");
		    	values.add("mode");
		    	values.add("(mode description)");
			    res.put(num++,values);
			    values = new ArrayList<Object>();
			    
			    while(!ds_match2.isAfterLast())
			    {
			    	// 添加值到Map当中
			    	long objID = ds_match2.getLong("objID");
			    	long thingID = ds_match2.getLong("thingID");
			    	byte mode = ds_match2.getByte("mode");
			    	
			    	values.add(objID);
			    	values.add(thingID);
			    	values.add(mode);
			    	values.add(Functions.fPhotoModeN(mode));
			    	
			    	res.put(num++,values);
			    	values = new ArrayList<Object>();
			    	
			    	ds_match2.next();
			    }
			}
			else {
			    return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return res;
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
