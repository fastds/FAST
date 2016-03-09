package org.fastds.model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.service.ExplorerService;

public class FitsImg {
	 protected Globals globals;
     protected String[] hrefsCf;

	 protected String fieldID;
     ExplorerService explorerService = new ExplorerService();
     public FitsImg()
     {
    	 globals = new Globals();
     }
//     protected void Page_Load()
//     {
//         globals = (Globals)Application[Globals.PROPERTY_NAME];
//         runQuery = null;
//         long? fieldID = Utilities.ParseID(Request.QueryString["field"]);
//         if (fieldID.HasValue)
//         {
//            
//             hrefsCf = getCFrame(fieldID.Value);
//             
//         }
//     }  old
     
     public void setHrefsCf(String[] hrefsCf) {
    	 this.hrefsCf = hrefsCf;
     }
     public Globals getGlobals() {
		return globals;
	}

	public String[] getHrefsCf() {
		return hrefsCf;
	}

	public String[] getCFrame(long fieldID)
     {
         String[] result = null;
         String cmd = ExplorerQueries.getFitsimg(fieldID+"");
         ResultSet rs = explorerService.runCmd(cmd);
         
         ResultSetMetaData meta;
		try {
			meta = rs.getMetaData();
		
         if (!rs.isAfterLast())
         {
             result = new String[meta.getColumnCount()];
             for (int i = 1; i <= meta.getColumnCount(); i++)
             {
            	 if ("bool".endsWith(meta.getColumnTypeName(i))) {
						result[i] = rs.getBoolean(meta.getColumnLabel(i))+"";
					} else if ("int".startsWith(meta.getColumnTypeName(i))) {
						result[i] = rs.getBigDecimal(meta.getColumnLabel(i))+"";
					} else if ("string".endsWith(meta.getColumnTypeName(i))) {
						String str = rs.getString(meta.getColumnTypeName(i));
						if(str.contains("<"))
							str = str.replace("<", "&lt;");
						if(str.contains(">"))
							str = str.replace(">","&gt;");
						//System.out.println(str);
						result[i] = str;
						
					} else if ("datetime".endsWith(meta.getColumnTypeName(i))) {
						
						result[i] = rs.getTime(meta.getColumnLabel(i))+"";
					} else {
						result[i] = rs.getDouble(meta.getColumnLabel(i))+"";
					}
             }
            rs.next();
         }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         return result;
     }

	public void setFieldID(String fieldID) {
		this.fieldID = fieldID;
	}
 
}
