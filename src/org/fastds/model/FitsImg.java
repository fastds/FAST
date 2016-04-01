package org.fastds.model;


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
         return  ExplorerQueries.getFitsimg(fieldID+"");
     }

	public void setFieldID(String fieldID) {
		this.fieldID = fieldID;
	}
 
}
