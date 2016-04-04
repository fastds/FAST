package org.fastds.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
        String cmd = ExplorerQueries.getNeighbors1(objID);
        ds_neighbor1 = explorerService.runCmd(cmd);
        
        cmd = ExplorerQueries.getNeighbors2(objID);
        ds_neighbor2 = explorerService.runCmd(cmd);

    }
    public Map<Integer, List<Object>> getMapNeighbor1()
    {
    	Map<Integer, List<Object>> res = new HashMap<Integer,List<Object>>();
		List<Object> values = null;
		int num = 0;
		
		try {
			if (ds_neighbor1!=null && !ds_neighbor1.isAfterLast())
			{
				/*把属性名存储如Map<0,List<>>中*/
		    	values = new ArrayList<Object>();
		    	values.add("IAU name");
		    	values.add("objid");
		    	values.add("thingid");
			    res.put(num++,values);
			    values.clear();
			    
	            // 添加值到Map当中
			    values.add(Functions.fIAUFromEq(ds_neighbor1.getFloat("ra"), ds_neighbor1.getFloat("dec")));
			    values.add(ds_neighbor1.getLong("objID"));
			    values.add(ds_neighbor1.getLong("thingId"));
		        res.put(num++,values);
			    values.clear();
			}
			else {
			    return res;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return res;
    }
    public Map<Integer, List<Object>> getMapNeighbor2()
    {
    	Map<Integer, List<Object>> res = new HashMap<Integer,List<Object>>();
		List<Object> values = null;
		int num = 0;
		
		try {
			if (ds_neighbor2!=null && !ds_neighbor2.isAfterLast())
			{
				/*把属性名存储如Map<0,List<>>中*/
		    	values = new ArrayList<Object>();
		    	values.add("objID");
		    	values.add("ra");
		    	values.add("dec");
		    	values.add("distance(arcmin)");
		    	values.add("type");
		    	values.add("mode");
		    	values.add("(mode description)");
			    res.put(num++,values);
			    values.clear();
			    
			    while(!ds_neighbor2.isAfterLast())
			    {
			    	// 添加值到Map当中
			    	long objID = ds_neighbor2.getLong("NeighborObjID");
			    	float ra = ds_neighbor2.getFloat("ra");
			    	float dec = ds_neighbor2.getFloat("dec");
			    	float distance = ds_neighbor2.getFloat("distance");
			    	byte mode = ds_neighbor2.getByte("neighborMode");
			    	
			    	values.add(objID);
			    	values.add(new DecimalFormat("####.00000").format(ra));
			    	values.add(new DecimalFormat("####.00000").format(dec));
			    	values.add(new DecimalFormat("#.000").format(distance));
			    	values.add(PhotoObjAll.getTypeName(ds_neighbor2.getShort("neighborType")));
			    	values.add(mode);
			    	values.add(Functions.fPhotoModeN(mode));
			    	
			    	res.put(num++,values);
			    	values.clear();
			    	
			    	ds_neighbor2.next();
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
