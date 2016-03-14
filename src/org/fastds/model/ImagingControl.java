package org.fastds.model;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.service.ExplorerService;

public class ImagingControl {
//	protected Globals globals;   old
    protected ObjectExplorer master;        

    //--- phototag
    protected String flag; //0
    protected double ra = 0;//1
    protected double dec = 0;//2
    protected int run;//3
    protected int rerun;//4
    protected int camcol;//5
    protected long field;//6
    protected String fieldID;//7
    protected String objID;//8

    //--- PhotoObjAll
    public int clean;//14
    protected String otype;//15
    protected double u;//16
    protected double g;//17
    protected double r;//18
    protected double i;//19
    protected double z;//20
    protected double err_u;//21
    protected double err_g;//22
    protected double err_r;//23
    protected double err_i;//24
    protected double err_z;//25

    //--- PhotoObj
    protected String mode;//26
    protected int mjdNum;//27
    protected int otherObs;//28
    protected long parentID;//29
    protected int nchild;//30
    protected String extinction_r;//31
    protected String petrorad_r;//32

    //--- PhotoZ, photoZRF
    protected String photoZ_KD;//33
    //protected String photoZ_RF;//34
    protected String galaxyZoo_Morph;//35

    protected String mjdDate = "";

    protected String sdssUrl;

    protected String flagsLink = "";

    ExplorerService explorerService = new ExplorerService();
    
    public ImagingControl(ObjectExplorer master)
    {
    	load(master);
    }
    
    public void load(ObjectExplorer master)
    {
//        globals = (Globals)Application[Globals.PROPERTY_NAME];         old     
//        master  = (ObjectExplorer)Page.Master;   old 

        try
        {
            //objID = Request.QueryString["id"];
            objID = master.objID;
        }
        catch (Exception exp) {
            //If the querystring is empty and no objid key
            objID = null;
        }
//        sdssUrl = globals.SdssUrl;     old
        flagsLink = sdssUrl + "algorithms/photo_flags_recommend.php";

        if (objID != null && !objID.equals(""))
        {
            execQuery();
            getUnit();
        }
    }

    private void execQuery()
    {
        
        Map<String,Object> attrs = new HashMap<String,Object>();
        attrs = explorerService.findImaging(objID);
        if(attrs != null && attrs.size() > 0)
        {
        	
        	if((Integer)attrs.get("mjdNum") != -99999)
        	{
        		mjdDate = DateFormat.getDateInstance().format(HelperFunctions.ConvertFromJulian(mjdNum).getTime().getTime()).format("MM/dd/yyyy");
//      old  	mjdDate = HelperFunctions.ConvertFromJulian(mjdNum).toString("MM/dd/yyyy");  
        	}
        	/*
        	 * int otherObs = rs.getInt("Other observations") == 0 ? -99999 : rs.getInt("Other observations");
                 long parentID = rs.getLong("parentID") == 0 ? -99999 : rs.getLong("parentID");
                 short nchild = (short) (rs.getShort("nChild") == 0 ? -99999 : rs.getShort("nChild"));
                 String extinction_r = rs.getString("extinction_r") == null ? " - " : rs.getString("extinction_r");
                 String petrorad_r = rs.getString("petrorad_r") == null ? " - " : rs.getString("petrorad_r");

                 ////--- PhotoZ, photoZRF
                 String photoZ_KD = rs.getString("photoZ_KD") == null ? " - " : rs.getString("photoZ_KD");

                 //photoZ_RF = reader["photoZ_KD") == 0 ? " - " : (String)reader["photoZ_RF"];
                 String galaxyZoo_Morph = rs.getString("photoZ_KD") == null ? " - " : rs.getString("galaxyZoo_Morph");
        	 */
        	otherObs = (Integer)attrs.get("otherObjs");
        	nchild = (Integer)attrs.get("parentID");
        	nchild = (Short)attrs.get("nchild");
        	extinction_r = (String)attrs.get("extinction_r");
        	petrorad_r = (String)attrs.get("petrorad_r");
        	photoZ_KD = (String)attrs.get("photoZ_KD");
        	galaxyZoo_Morph = (String)attrs.get("galaxyZoo_Morph");
        }
    }

    //protected String u_unit, g_unit, r_unit, i_unit, z_unit, 
    //                 err_u_unit, err_g_unit, err_r_unit, err_i_unit, err_z_unit,
    //                 mjd_unit,mode_unit;

    protected Map<String,String> columnUnit = new HashMap<String,String>();

    protected void getUnit(){
        columnUnit = explorerService.findUnit();
    }

    protected String getUnit(String tablename, String columname) {
        String unit = "";
        unit = explorerService.findUnit(tablename,columname);
         
         return unit;
    }

	public ObjectExplorer getMaster() {
		return master;
	}

	public String getFlag() {
		return flag;
	}

	public double getRa() {
		return ra;
	}

	public double getDec() {
		return dec;
	}

	public int getRun() {
		return run;
	}

	public int getRerun() {
		return rerun;
	}

	public int getCamcol() {
		return camcol;
	}

	public long getField() {
		return field;
	}

	public String getFieldID() {
		return fieldID;
	}

	public String getObjID() {
		return objID;
	}

	public int getClean() {
		return clean;
	}

	public String getOtype() {
		return otype;
	}

	public double getU() {
		return u;
	}

	public double getG() {
		return g;
	}

	public double getR() {
		return r;
	}

	public double getI() {
		return i;
	}

	public double getZ() {
		return z;
	}

	public double getErr_u() {
		return err_u;
	}

	public double getErr_g() {
		return err_g;
	}

	public double getErr_r() {
		return err_r;
	}

	public double getErr_i() {
		return err_i;
	}

	public double getErr_z() {
		return err_z;
	}

	public String getMode() {
		return mode;
	}

	public int getMjdNum() {
		return mjdNum;
	}

	public int getOtherObs() {
		return otherObs;
	}

	public long getParentID() {
		return parentID;
	}

	public int getNchild() {
		return nchild;
	}

	public String getExtinction_r() {
		return extinction_r;
	}

	public String getPetrorad_r() {
		return petrorad_r;
	}

	public String getPhotoZ_KD() {
		return photoZ_KD;
	}

	public String getGalaxyZoo_Morph() {
		return galaxyZoo_Morph;
	}

	public String getMjdDate() {
		return mjdDate;
	}

	public String getSdssUrl() {
		return sdssUrl;
	}

	public String getFlagsLink() {
		return flagsLink;
	}

	public ExplorerService getExplorerService() {
		return explorerService;
	}

	public Map<String, String> getColumnUnit() {
		return columnUnit;
	}
    
}
