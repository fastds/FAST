package org.fastds.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.fastds.service.ExplorerService;

import edu.gzu.image.Functions;

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
	        return new String[]{Functions.fGetUrlFitsSpectrum(specObjID)};
     }

}
