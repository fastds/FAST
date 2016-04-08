package org.fastds.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.fastds.service.ExplorerService;

public class FitsSpec {
	 protected String[] hrefsSpec;
     Long specObjID = null;

     protected Globals globals;
     ExplorerService service = new ExplorerService();
     public FitsSpec(ObjectExplorer master)
     {
    	 load(master);
     }
     protected void load(ObjectExplorer master)
     {
         globals = master.getGlobals();

//    old     String sid = Request.QueryString["sid"];
//         specObjId = Utilities.ParseId(sid);
//
//         using (SqlConnection oConn = new SqlConnection(globals.ConnectionString))
//         {
//             oConn.Open();
//             hrefsSpec = getFits(oConn, specObjId);
//         }
     }
     
     public void setSpecObjID(Long specObjID) {
		this.specObjID = specObjID;
	}
     
	public Long getSpecObjID() {
		return specObjID;
	}
	public void setHrefsSpec(String[] hrefsSpec) {
		this.hrefsSpec = hrefsSpec;
	}
	
	public String[] getHrefsSpec() {
		return hrefsSpec;
	}
	public String[] getFits() {
	        String[] result = null;
	        String aql = "select dbo.fGetUrlFitsSpectrum(@specObjId)";
	        aql.replace("@specObjId", this.specObjID.toString());
	        ResultSet rs = null;
	        rs = service.runCmd(aql);
	        System.out.println("FitsSpec.getFits--->aql:"+aql);
	        ResultSetMetaData meta;
			try {
				meta = rs.getMetaData();
				int colNum = meta.getColumnCount();
	            if (rs!=null && !rs.isAfterLast())
	            {
	                result = new String[colNum];
	            }
	            if (!rs.isAfterLast())
	            {
	                for (int k = 1; k <= colNum; k++)
	                {
	                	if ("bool".endsWith(meta.getColumnTypeName(k))) {
							result[k-1] = rs.getBoolean(k)+"";
						} else if (meta.getColumnTypeName(k).startsWith("int")) {
							result[k-1] = rs.getLong(k)+"";
						} else if ("string".endsWith(meta.getColumnTypeName(k))) {
							String str = rs.getString(k);
							if(str!=null)
							{
								if(str.contains("<"))
									str = str.replace("<", "&lt;");
								if(str.contains(">"))
									str = str.replace(">","&gt;");
								result[k-1] = str;
							}
							else
								result[k-1] = "";
						} else if ("datetime".endsWith(meta.getColumnTypeName(k))) {
							
							result[k-1] = rs.getTime(k).toString();
						} else {
							result[k-1] = rs.getDouble(k)+"";
						}
	                	
	                }
	            }
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        
	        return result;
     }

}
