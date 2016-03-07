package org.fastds.model;

import java.util.Map;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.service.ExplorerService;

public class CrossIDControls {
//	protected Globals globals;  	old
    protected ObjectExplorer master;

    protected String objID;

    protected String usno;
    protected String properMotion;
    protected float angle;

	protected String first;
    protected String peakflux;
    protected float major;
    protected float minor;

    protected String rosat;
    protected float cps;
    protected float hr1;
    protected float hr2;
    protected float ext;

    protected String rc3;
    protected String hubletype;
    protected String magnitude;
    protected float hydrogenIndex;

    protected String wise;
    protected float wmag1;
    protected float wmag2;
    protected float wmag3;
    protected float wmag4;
    protected String wiselink;
    protected String linkQuery;

    protected String twomass;
    protected float j;
    protected float h;
    protected float k;
    protected String phQual;

    protected boolean isUSNO = false;
    protected boolean isFIRST = false;
    protected boolean isROSAT = false;
    protected boolean isRC3 = false;
    protected boolean isWISE = false;
    protected boolean is2MASS = false;
    
    
    public String getObjID() {
    	return objID;
    }
    public CrossIDControls(ObjectExplorer master)
    {
    	load(master);
    }
    protected void load(ObjectExplorer master)
    {
//        globals = (Globals)Application[Globals.PROPERTY_NAME]; old
//        master = (ObjectExplorer)Page.Master;       old
        objID = master.objID;
        if (master.objID != null && !master.objID.equals(""))
        execQuery();
    }
    
 public String getLinkQuery() {
		return linkQuery;
	}
public String getWise() {
		return wise;
	}
	public float getWmag1() {
		return wmag1;
	}
	public float getWmag2() {
		return wmag2;
	}
	public float getWmag3() {
		return wmag3;
	}
	public float getWmag4() {
		return wmag4;
	}
	public String getWiselink() {
		return wiselink;
	}
public String getTwomass() {
		return twomass;
	}
	public float getH() {
		return h;
	}
	public String getPhQual() {
		return phQual;
	}
public String getHubletype() {
		return hubletype;
	}
public String getRc3() {
		return rc3;
	}
	public String getMagnitude() {
		return magnitude;
	}
	public float getHydrogenIndex() {
		return hydrogenIndex;
	}
public float getExt() {
		return ext;
	}
public String getRosat() {
		return rosat;
	}
	public float getCps() {
		return cps;
	}
	public float getHr1() {
		return hr1;
	}
	public float getHr2() {
		return hr2;
	}
public String getFirst() {
		return first;
	}
	public float getMajor() {
		return major;
	}
	public float getMinor() {
		return minor;
	}
public String getPeakflux() {
		return peakflux;
	}
public float getJ() {
		return j;
	}
	public float getK() {
		return k;
	}
	public boolean isUSNO() {
		return isUSNO;
	}
	public boolean isFIRST() {
		return isFIRST;
	}
	public boolean isROSAT() {
		return isROSAT;
	}
	public boolean isRC3() {
		return isRC3;
	}
	public boolean isWISE() {
		return isWISE;
	}
	public boolean isIs2MASS() {
		return is2MASS;
	}
public String getProperMotion() {
		return properMotion;
	}
	public float getAngle() {
		return angle;
	}
public String getUsno() {
		return usno;
	}
private ExplorerService explorerService = new ExplorerService();
    private void execQuery() { 
        
        //USNO Query
        Map<String,Object> attrs = explorerService.findAttrsFromUSNOByID(objID);
         if (attrs != null && attrs.size()!=0)
         {
            isUSNO = true;
            usno = (String) attrs.get("usno");
            properMotion = (String) attrs.get("properMotion");
            angle = (Float) attrs.get("angle");
         } 
         attrs.clear();
         
         //FIRST Query   
         attrs = explorerService.findAttrsFromFirst(objID);
         if (attrs != null && attrs.size()!=0)
         {
            isFIRST = true;
            first = (String) attrs.get("first");
            peakflux = (String) attrs.get("peakflux");
            major = (Float) attrs.get("major");
            minor = (Float) attrs.get("minor");
         }
         attrs.clear();
         
         //ROSAT Query
         attrs = explorerService.findAttrsFromRosat(objID);
         if (attrs != null && attrs.size()!=0)
         {
        	 isROSAT = true;
        	 rosat = (String) attrs.get("rosat");
        	 cps = (Float) attrs.get("cps");
        	 hr1 = (Float) attrs.get("hr1");
        	 hr2 = (Float) attrs.get("hr2");
        	 ext = (Float) attrs.get("ext");
         }
         attrs.clear();
         
         //RC3
         
         attrs = explorerService.findAttrsFromRC3(objID);
         if (attrs != null && attrs.size()!=0)
         {
             isRC3 = true;
             rc3 = (String) attrs.get("rc3");
             hubletype = (String) attrs.get("hubletype");
             magnitude = (String) attrs.get("magnitude");
             hydrogenIndex = (Float) attrs.get("hydrogenIndex");
         }
         attrs.clear();
         
         //WISE
         linkQuery = ExplorerQueries.wiseLinkCrossID(objID);
         attrs = explorerService.findAttrsFromWISE(objID);
         if (attrs != null && attrs.size()!=0)
         {
        	 isWISE = true;
        	 wise = (String) attrs.get("rc3");
        	 wmag1 = (Float) attrs.get("hubletype");
        	 wmag2 = (Float) attrs.get("magnitude");
        	 wmag3 = (Float) attrs.get("hydrogenIndex");
        	 wmag4 = (Float) attrs.get("hydrogenIndex");
        	 wiselink = (String) attrs.get("hydrogenIndex");
         }
         attrs.clear();

         //TWOMASS
         String aql = ExplorerQueries.TWOMASS(objID);
         attrs = explorerService.findAttrsFromTWOMASS(objID);
         if (attrs != null && attrs.size()!=0)
         {
        	 is2MASS = true;
        	 twomass = (String) attrs.get("twomass");
        	 j = (Float) attrs.get("j");
        	 h = (Float) attrs.get("h");
        	 k = (Float) attrs.get("k");
        	 phQual = (String) attrs.get("phQual");
         }
         attrs.clear();

    }

}
