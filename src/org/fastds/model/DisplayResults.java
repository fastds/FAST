package org.fastds.model;

import java.sql.ResultSet;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.service.ExplorerService;

public class DisplayResults {
	 protected String cmd = null;
     protected String name = null;
     protected String url = null;
     protected String objID = null;
     protected String specID = null;
     protected String apid = null;
     protected String fieldID = null;


     protected ObjectExplorer master = null;
     protected ResultSet ds = null;
     private ExplorerService explorerService = new ExplorerService();
    
     public DisplayResults(ObjectExplorer master)
     {
    	 this.master = master;
     }
     
//  old   protected void Page_Load(ObjectExplorer master)
//     {
//         this.master = master;
//         
//
//         for (String key .equals(name)) Request.QueryString.Keys)
//         {
//             if(key == "apid")
//                 apid= HttpUtility.UrlEncode(Request.QueryString["apid"]);
//             objID = Request.QueryString["id"];
//             specID = Request.QueryString["spec"];
//             
//             fieldID = Request.QueryString["field"];
//
//             cmd = Request.QueryString["cmd"];
//             name = Request.QueryString["name"];
//             url = Request.QueryString["url"];
//         }
//        
//         if(cmd == null || cmd.equals(""))
//             getQuery();
//
//         executeQuery();
//     }
     
     public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public ResultSet getDs() {
		return ds;
	}

	public void executeQuery()  {

         try {
             ds = explorerService.runCmd(cmd);
         }
         catch (Exception e) {
             e.printStackTrace();
         }

     }

     public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setObjID(String objID) {
		this.objID = objID;
	}

	public void setSpecID(String specID) {
		this.specID = specID;
	}

	public void setApid(String apid) {
		this.apid = apid;
	}

	public void setFieldID(String fieldID) {
		this.fieldID = fieldID;
	}

	public void setMaster(ObjectExplorer master) {
		this.master = master;
	}

	public void setDs(ResultSet ds) {
		this.ds = ds;
	}

	public void getQuery() {
		
		String objIDLong = objID.startsWith("0x")? Long.parseLong(objID.substring(2), 16)+"" : objID;
		String fieldIDLong = fieldID.startsWith("0x")? Long.parseLong(fieldID.substring(2), 16)+"" : fieldID;
		String specIDLong = specID.startsWith("0x")? Long.parseLong(specID.substring(2), 16)+"" : specID;
		
		if("PhotoObj".equals(name))
			cmd = ExplorerQueries.PhotoObjQuery.replace("@objID", objIDLong);
		else if("PhotoTag".equals(name))
			cmd = ExplorerQueries.PhotoTagQuery.replace("@objID", objIDLong);
		else if("photoZ".equals(name))
			cmd = ExplorerQueries.PhotoZ.replace("@objID", objIDLong);
		else if("Field".equals(name))
			cmd = ExplorerQueries.FieldQuery.replace("@fieldID", fieldIDLong);
		else if("Frame".equals(name))
			 cmd = ExplorerQueries.FrameQuery.replace("@fieldID", fieldIDLong);
		else if("SpecObj".equals(name))
			cmd = ExplorerQueries.SpecObjQuery.replace("@specID", specIDLong); 
		else if("sppLines".equals(name))
			cmd = ExplorerQueries.sppLinesQuery.replace("@specID", specIDLong);
         else if( "sppParams".equals(name))
                 cmd = ExplorerQueries.sppParamsQuery.replace("@specID", specIDLong); 
         else if( "galSpecLine".equals(name))
                 cmd = ExplorerQueries.galSpecLineQuery.replace("@specID", specIDLong); 
         else if( "galSpecIndx".equals(name))
                 cmd = ExplorerQueries.galSpecIndexQuery.replace("@specID", specIDLong); 
         else if( "galSpecInfo".equals(name))
                 cmd = ExplorerQueries.galSpecInfoQuery.replace("@specID", specIDLong); 
         else if( "stellarMassStarFormingPort".equals(name))
                 cmd = ExplorerQueries.stellarMassStarformingPortQuery.replace("@specID", specIDLong);   
         else if( "stellarMassPassivePort".equals(name))
                 cmd = ExplorerQueries.stellarMassPassivePortQuery.replace("@specID", specIDLong); 
         else if( "emissionlinesPort".equals(name))
                 cmd = ExplorerQueries.emissionLinesPortQuery.replace("@specID", specIDLong); 
         else if( "stellarMassPCAWiscBC03".equals(name))
                 cmd = ExplorerQueries.stellarMassPCAWiscBC03Query.replace("@specID",specIDLong); 
         else if( "stellarMassPCAWiscM11".equals(name))
                 cmd = ExplorerQueries.stellarMassPCAWiscM11Query.replace("@specID", specIDLong); 
         else if( "stellarMassFSPSGranEarlyDust".equals(name))
                 cmd = ExplorerQueries.stellarMassFSPSGranEarlyDust.replace("@specID", specIDLong); 
         else if( "stellarMassFSPSGranEarlyNoDust".equals(name))
                 cmd = ExplorerQueries.stellarMassFSPSGranEarlyNoDust.replace("@specID", specIDLong); 
         else if( "stellarMassFSPSGranWideDust".equals(name))
                 cmd = ExplorerQueries.stellarMassFSPSGranWideDust.replace("@specID", specIDLong); 
         else if( "stellarMassFSPSGranWideNoDust".equals(name))
                 cmd = ExplorerQueries.stellarMassFSPSGranWideNoDust.replace("@specID", specIDLong); 
         
         else if( "apogeeStar".equals(name))
                 cmd= ExplorerQueries.apogeeStar.replace("@apid", apid); 
         else if( "aspcapStar".equals(name))
                 cmd= ExplorerQueries.aspcapStar.replace("@apid", apid); 

         else
        	 cmd = ""; 


     }

}
