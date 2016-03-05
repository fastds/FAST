package org.fastds.model;

import java.util.Map;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.service.ExplorerService;

import edu.gzu.utils.Utilities;

public class SpectralControl {
	protected Globals globals;
    protected ObjectExplorer master;

    protected String objID;
    protected String specID;     

    protected long plate;
    protected int mjd;
    protected int fiberid;
    protected String instrument;
    protected String objclass;
    protected double redshift_z;
    protected double redshift_err;
    protected String redshift_flags;
    protected String survey;
    protected String programname;
    protected int primary;
    protected int otherspec;
    protected String sourcetype;
    protected double veldisp;
    protected double veldisp_err;
    protected String targeting_flags;
    protected Long specObjID;

    public SpectralControl(ObjectExplorer master)
    {
    	load(master);
    }
    protected void load(ObjectExplorer master)
    {
//        globals = (Globals)Application[Globals.PROPERTY_NAME]; old
        try
        {
            //objID = Request.QueryString["id"];
            //specID = Request.QueryString["spec"];
            objID = master.objID;
            specID = master.specObjID;
            specObjID = Utilities.ParseId(specID);
        }
        catch(Exception exp){
            specObjID = null;
        }
        if(specID != null && !specID.equals(""))
        executeQuery();
    }
    ExplorerService explorerService = new ExplorerService();
    private void executeQuery()
    {

        String aql = ExplorerQueries.getSpectroQuery(objID,master.specID.toString());
        Map<String,Object> attrs = null;
        attrs = explorerService.findAttrsFromSpecObjAllAndPlateX(objID,master.specID.toString());
        /*if (reader.HasRows)
        {
            plate = reader["plate"] is DBNull ? -99999 : (short)reader["plate"]; 
            mjd = reader["mjd"] is DBNull ? -99999 : (int)reader["mjd"];
            fiberid = reader["fiberid"] is DBNull ? -99999 : (short)reader["fiberid"];
            instrument = reader["instrument"] is DBNull ? "" : (String)reader["instrument"];
            objclass = reader["objclass"] is DBNull ? "" : (String)reader["objclass"];
            redshift_z = reader["redshift_z"] is DBNull ? -999.99 : (float)reader["redshift_z"];
            redshift_err = reader["redshift_err"] is DBNull ? -999.99 : (float)reader["redshift_err"];
            redshift_flags = reader["redshift_flags"] is DBNull ? "" : (String)reader["redshift_flags"];
            survey = reader["survey"] is DBNull ? "" : (String)reader["survey"];
            programname = reader["programname"] is DBNull ? "" : (String)reader["programname"];
            primary = reader["primary"] is DBNull ? -99999 : (short)reader["primary"];
            otherspec = reader["otherspec"] is DBNull ? -99999 : (int)reader["otherspec"];
            sourcetype = reader["sourcetype"] is DBNull ? "" : (String)reader["sourcetype"];
            veldisp = reader["veldisp"] is DBNull ? -999.99 : (float)reader["veldisp"];
            veldisp_err = reader["veldisp_err"] is DBNull ? -999.99 : (float)reader["veldisp_err"];
            targeting_flags = reader["targeting_flags"] is DBNull ? "" : (String)reader["targeting_flags"];
        }*/
        if(attrs != null && attrs.size()!= 0)
        {
        	plate = (Short)attrs.get("plate") == 0? -9999:(Short)attrs.get("plate");
        	mjd = (Integer)attrs.get("mjd") == 0 ? -99999 : (Integer)attrs.get("mjd");
            fiberid = (Short)attrs.get("fiberid") == 0 ? -99999 : (Short)attrs.get("fiberid");
            instrument = attrs.get("instrument") == null ? "" : (String)attrs.get("instrument");
            objclass = (Float)attrs.get("objclass") == null ? "" : (String)attrs.get("objclass");
            redshift_z = (Float)attrs.get("redshift_z") == 0 ? -999.99 : (Float)attrs.get("redshift_z");
            redshift_err = (Float)attrs.get("redshift_err") == 0 ? -999.99 : (Float)attrs.get("redshift_err");
            redshift_flags = attrs.get("redshift_flags") == null ? "" : (String)attrs.get("redshift_flags");
            survey = attrs.get("survey") == null ? "" : (String)attrs.get("survey");
            programname = attrs.get("programname") == null ? "" : (String)attrs.get("programname");
            primary = (Short)attrs.get("primary") == 0 ? -99999 : (Short)attrs.get("primary");
            otherspec = (Integer)attrs.get("otherspec") == 0 ? -99999 : (Integer)attrs.get("otherspec");
            sourcetype = attrs.get("sourcetype") == null ? "" : (String)attrs.get("sourcetype");
            veldisp = (Float)attrs.get("veldisp") == 0 ? -999.99 : (Float)attrs.get("veldisp");
            veldisp_err = (Float)attrs.get("veldisp_err") == 0 ? -999.99 : (Float)attrs.get("veldisp_err");
            targeting_flags = attrs.get("targeting_flags") == null ? "" : (String)attrs.get("targeting_flags");
        }
    }
	public Globals getGlobals() {
		return globals;
	}
	public ObjectExplorer getMaster() {
		return master;
	}
	public String getObjID() {
		return objID;
	}
	public String getSpecID() {
		return specID;
	}
	public long getPlate() {
		return plate;
	}
	public int getMjd() {
		return mjd;
	}
	public int getFiberid() {
		return fiberid;
	}
	public String getInstrument() {
		return instrument;
	}
	public String getObjclass() {
		return objclass;
	}
	public double getRedshift_z() {
		return redshift_z;
	}
	public double getRedshift_err() {
		return redshift_err;
	}
	public String getRedshift_flags() {
		return redshift_flags;
	}
	public String getSurvey() {
		return survey;
	}
	public String getProgramname() {
		return programname;
	}
	public int getPrimary() {
		return primary;
	}
	public int getOtherspec() {
		return otherspec;
	}
	public String getSourcetype() {
		return sourcetype;
	}
	public double getVeldisp() {
		return veldisp;
	}
	public double getVeldisp_err() {
		return veldisp_err;
	}
	public String getTargeting_flags() {
		return targeting_flags;
	}
	public Long getSpecObjID() {
		return specObjID;
	}
	public ExplorerService getExplorerService() {
		return explorerService;
	}
    

}
