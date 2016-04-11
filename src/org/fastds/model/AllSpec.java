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

import edu.gzu.image.Functions;

public class AllSpec {
	protected Globals globals;
    protected ObjectExplorer master;
    protected String objID;        
    protected ResultSet ds_spec1, ds_spec2;
    ExplorerService explorerService = new ExplorerService();
    public AllSpec(ObjectExplorer master)
    {
    	this.master = master;          
    	this.globals = master.globals;
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
	public Map<Integer, List<Object>> getMapSpec1()
	{
		Map<Integer, List<Object>> res = new HashMap<Integer,List<Object>>();
		List<Object> values = null;
		int num = 0;
		
		try {
			if (ds_spec1!=null && !ds_spec1.isAfterLast())
			{
				/*把属性名存储如Map<0,List<>>中*/
		    	values = new ArrayList<Object>();
		    	values.add("specObjID");
		    	values.add("plate");
		    	values.add("MJD");
		    	values.add("fiber");
		    	values.add("ra");
		    	values.add("dec");
		    	values.add("specRa");
		    	values.add("specDec");
		    	values.add("sciencePrimary");
		    	values.add("distanceArcMin");
		    	values.add("class");
			    res.put(num++,values);
			    values = new ArrayList<Object>();
			    
			   
	            // 添加值到Map当中
			    values.add(ds_spec1.getLong("specObjID"));
			    values.add(ds_spec1.getShort("plate"));
			    values.add(ds_spec1.getInt("MJD"));
			    values.add(ds_spec1.getShort("fiber"));
			    float ra = ds_spec1.getFloat("ra");
			    float dec = ds_spec1.getFloat("dec");
			    float specRa = ds_spec1.getFloat("specRa");
			    float specDec = ds_spec1.getFloat("specDec");
			    values.add(new DecimalFormat("#.#####").format(ra));
			    values.add(new DecimalFormat("#.#####").format(dec));
			    values.add(new DecimalFormat("#.#####").format(specRa));
			    values.add(new DecimalFormat("#.#####").format(specDec));
			    values.add(ds_spec1.getShort("sciencePrimary"));
			    values.add(new DecimalFormat("#.########").format(Functions.fDistanceArcMinEq(ra, dec, specRa, specDec)));
			    values.add(ds_spec1.getString("class"));
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
	public Map<Integer, List<Object>> getMapSpec2()
	{
		Map<Integer, List<Object>> res = new HashMap<Integer,List<Object>>();
		List<Object> values = null;
		int num = 0;
		
		try {
			if (ds_spec2!=null && !ds_spec2.isAfterLast())
			{
				/*把属性名存储如Map<0,List<>>中*/
		    	values = new ArrayList<Object>();
		    	values.add("specObjID");
		    	values.add("plate");
		    	values.add("MJD");
		    	values.add("fiber");
		    	values.add("ra");
		    	values.add("dec");
		    	values.add("specRa");
		    	values.add("specDec");
		    	values.add("sciencePrimary");
		    	values.add("distanceArcMin");
		    	values.add("class");
			    res.put(num++,values);
			    values = new ArrayList<Object>();
			    
			   
	            // 添加值到Map当中
			    values.add(ds_spec1.getLong("specObjID"));
			    values.add(ds_spec1.getShort("plate"));
			    values.add(ds_spec1.getInt("mjd"));
			    values.add(ds_spec1.getShort("fiber"));
			    float ra = ds_spec1.getFloat("ra");
			    float dec = ds_spec1.getFloat("dec");
			    float specRa = ds_spec1.getFloat("specRa");
			    float specDec = ds_spec1.getFloat("specDec");
			    values.add(new DecimalFormat("#.#####").format(ra));
			    values.add(new DecimalFormat("#.#####").format(dec));
			    values.add(new DecimalFormat("#.#####").format(specRa));
			    values.add(new DecimalFormat("#.#####").format(specDec));
			    values.add(ds_spec1.getShort("sciencePrimary"));
			    values.add(new DecimalFormat("#.########").format(Functions.fDistanceArcMinEq(ra, dec, specRa, specDec)));
			    values.add(ds_spec1.getString("class"));
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

}
