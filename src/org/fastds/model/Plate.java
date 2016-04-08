package org.fastds.model;

import java.sql.ResultSet;
import java.text.DecimalFormat;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.service.ExplorerService;

import edu.gzu.utils.Utilities;

public class Plate {
	 protected Long plateID = null;
     protected Globals globals;
     protected ObjectExplorer master;
     protected ResultSet ds;
     ExplorerService explorerService = new ExplorerService();
     public Plate(ObjectExplorer master)
     {
    	 this.master = master;
     }
//     protected void Page_Load()
//     {
//         globals = (Globals)Application[Globals.PROPERTY_NAME];
//         master = (ObjectExplorer)Page.Master;            
//         String s = Request.QueryString["plateID"];            
//         plateID = Utilities.ParseID(s);                      
//         runQuery = new RunQuery();
//         executeQuery();
//     }  old 
     
     public void executeQuery() {
    	 String id = plateID.toString().startsWith("ox")?Utilities.longToHex(plateID):plateID.toString();
         String cmd = ExplorerQueries.Plate.replace("@plateID",id);
         ds = explorerService.runCmd(cmd);
     }

     public void setPlateID(Long plateID) {
		this.plateID = plateID;
	}

	public ResultSet getDs() {
		return ds;
	}

	public String showFTable()
     {
         String cmd = ExplorerQueries.PlateShow(plateID.toString());
         ResultSet ds = explorerService.runCmd(cmd);
         
         String u = "<a class='content' target='_top' href='summary.aspx?sid=";
         String sid;
         int col = 0;
         int row = 0;
         String c = "st";
         String specObjID;
         StringBuilder res = new StringBuilder();
         res.append("<table>\n");
         res.append("<tr>");
         try{
             while (!ds.isAfterLast())
             {
                 specObjID = ds.getLong("specObjID") == 0 ? null : Utilities.longToHex(ds.getLong("specObjID"));
                 sid = u + specObjID + "'>";
                 String v = "[" + ds.getShort("fiberID") + "]&nbsp;";
                 v += ds.getString("name") + " z=" + new DecimalFormat("###.##").format(ds.getFloat("z"));
                 res.append("<td nowrap class='" + c + "'>" + sid + v + "</a></td>\n");
                 if (++col > 3)
                 {
                     col = 0;
                     row++;
                     res.append("</tr>\n<tr>\n");
                     c = ("st".equals(c) ? "sb" : "st");
                 }
             }
         }
         catch(Exception e)
         {e.printStackTrace();}
         res.append("<td></td></tr>\n</table>\n");
         return res.toString();
     }
}
