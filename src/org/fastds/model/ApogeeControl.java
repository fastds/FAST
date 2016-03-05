package org.fastds.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.service.ExplorerService;

import edu.gzu.image.Functions;

public class ApogeeControl {
	protected Globals globals; 
    protected ObjectExplorer master;

    public final double DEFAULT_RADIUS = 0.5 / 60;

    public float getVhelio_avg() {
		return vhelio_avg;
	}

	public float getVscatter() {
		return vscatter;
	}


	/* TABLE apogeeStar */
    public double ra;
    public double dec;
    public String apstar_id;
    public String apogee_id;


	public double glon;
    public double glat;
    public long location_id;
    public long commiss;
    public float vhelio_avg;          // Avg v<sub>helio</sub> (km/s)
    public float vscatter;            // Scatter in v<sub>helio</sub> (km/s)

    /* TABLE aspcapStar */
    public float teff;                // Best-fit temperature (K)
    public float teff_err;            // Temp error
    public float logg;                // Surface Gravity log<sub>10</sub>(g)
    public float logg_err;            // log(g) error
    public float param_m_h;              // Metallicity [Fe/H]
    public float param_m_h_err;          // Metal error
    public float param_alpha_m;             // [&alpha;/Fe]
    public float param_alpha_m_err;         // [&alpha;/Fe] error

    /* TABLE apogeeObject */
    public float j;                   // 2MASS j
    public float h;                   // 2MASS h
    public float k;                   // 2MASS k
    public float j_err;
    public float h_err;
    public float k_err;
    public float mag_4_5;            // 4.5 micron magnitude
    public float mag_4_5_err;        // 4.5 micron magnitude error
    public String src_4_5;            // 4.5 micron magnitude source

    /* Flags */
    public String apogeeTarget1N;      // APOGEE target flags 1
    public String apogeeTarget2N;      // APOGEE target flags 2
    public String apogeeStarFlagN;     // Star flags
    public String apogeeAspcapFlagN;   // Processing flags (ASPCAP)


    /* TABLE apogeeVisit */
    public String visit_id;
    public long plate;
    public long mjd;
    public long fiberid;
    public String dateobs;
    public float vrel;

    public final String FIND_NEAREST = "join (select top 1 apstar_id from @subselect) s on a.apstar_id = s.apstar_id";
    public final String FIND_APSTAR_ID = "where a.apstar_id = @id";
    public final String FIND_APOGEE_ID = "where a.apogee_id = @id";
    public final String FIND_PLFIB = "join apogeeVisit v on a.apogee_id = v.apogee_id"
								    +"where" 
								    +"v.plate = @plate and" 
								    +"v.mjd = @mjd and "
								    +"v.fiberID = @fiberid";

    protected String apogeeSpecThumbnail;

    protected String apogeeSpecImage;
    protected String spectrumLink;
    protected String fitsLink;

    String[] injection = new String[] { "--", ";", "/*", "*/", "'", "\"" };
    String command;

    protected boolean isData = false;
    ExplorerService explorerService = new ExplorerService();
  
    /* Visits */
    public List<ApogeeVisit> visits = new ArrayList<ApogeeVisit>();

    
    public String getApogeeStarFlagN() {
		return apogeeStarFlagN;
	}

	public String getApogeeAspcapFlagN() {
		return apogeeAspcapFlagN;
	}

	public float getParam_alpha_m() {
		return param_alpha_m;
	}

	public float getParam_alpha_m_err() {
		return param_alpha_m_err;
	}

	public float getLogg() {
		return logg;
	}

	public float getLogg_err() {
		return logg_err;
	}

	public float getParam_m_h() {
		return param_m_h;
	}

	public float getParam_m_h_err() {
		return param_m_h_err;
	}

	public float getTeff() {
		return teff;
	}

	public float getTeff_err() {
		return teff_err;
	}

	public Globals getGlobals() {
		return globals;
	}

	public List<ApogeeVisit> getVisits() {
    	return visits;
    }
    
    public String getFitsLink() {
		return fitsLink;
	}
	public String getSpectrumLink() {
		return spectrumLink;
	}
	public String getApogeeSpecImage() {
		return apogeeSpecImage;
	}
	public boolean isData() {
		return isData;
	}
	public double getRa() {
		return ra;
	}
	public double getDec() {
		return dec;
	}
	public double getGlon() {
		return glon;
	}
	public double getGlat() {
		return glat;
	}
	public String getApogee_id() {
		return apogee_id;
	}
	public ApogeeControl(ObjectExplorer master) throws Exception
    {
    	load(master);
    }
    protected void load(ObjectExplorer master) throws Exception
    {
        globals = new Globals();
        
        if (master.apid != null && !master.apid.equals(""))
        {
            try
            {
                apogeeID(master.apid);
                ReadInfoFromDbReader();
                ReadApogeeLinks();
                ReadVisitsFromDbReader();
            }
            catch (Exception ex) {
                throw ex;
            }
        }
        
    } 

    protected void ReadInfoFromDbReader()  
    {
    	Map<String,Object> attrs = new HashMap<String,Object>();
    	attrs = explorerService.findApogeeByAql(command);
    	if(attrs == null || attrs.size() == 0)
    		isData = true;
    	else
    		isData = false;
           /////////////////////////////////////////
    }

    private void ReadApogeeLinks() {
        String specApogeeLink = globals.getApogeeSpectrumLink() + "?apogee_id=" + apogee_id;
        String doWeNeedC = (commiss == 1) ? "C" : "";

//    old    apogeeSpecImage = globals.getApogeeFitsLink() + "/stars/apo25m/" + location_id + "/plots/apStar" + doWeNeedC + "-r5-" + HttpUtility.UrlEncode(apogee_id) + ".jpg"; ;
        spectrumLink = globals.getApogeeSpectrumLink() + "?locid=" + location_id + "&commiss=" + commiss + "&apogeeid=" + apogee_id;
//   old     fitsLink = globals.getApogeeFitsLink() + "/stars/apo25m/"+location_id+"/apStar" + doWeNeedC + "-r5-" + HttpUtility.UrlEncode(apogee_id) + ".fits";
    }

    protected void ReadVisitsFromDbReader() throws Exception
    {

        for (String s : injection)
        {  
            if (apogee_id != null && apogee_id.indexOf(s) >= 0)
            {
                throw new Exception("Invalid APOGEE ID: " + apogee_id);
            }
        }
        
        ApogeeVisit v = explorerService.findApogeevisitsByID(apogee_id);
        visits.add(v);
    }
    
    public void apogeeRaDec( double ra, double dec, double radius)
    {
        command = ExplorerQueries.getApogeeBaseQuery() + FIND_NEAREST;
        String subselect = Functions.fGetNearestApogeeStarEq(ra, dec, radius);
        command = command.replace("@subselect", subselect);
    }

    public void apogeeID( String id) throws Exception
    {

        if (id.startsWith("apogee")) { command = ExplorerQueries.getApogeeBaseQuery() + FIND_APSTAR_ID; }
        else { command = ExplorerQueries.getApogeeBaseQuery() + FIND_APOGEE_ID; }

        for (String s : injection)
        {
            if (id.indexOf(s) >= 0)
            {
                throw new Exception("Invalid APOGEE ID: " + id);
            }
        }
        command = command.replace("@id", "'"+id+"'");            
    }


    public void apogeePlate(long plate, long mjd, long fiberid)
    {
        command = ExplorerQueries.getApogeeBaseQuery() + FIND_PLFIB;
        command = command.replace("@plate", plate+"");
        command = command.replace("@mjd", mjd+"");
    }

}
