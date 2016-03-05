package org.fastds.model;

public class Globals {
	public final String PROPERTY_NAME = "SkyServer.Globals";

    private int releaseNumber;
    private String connectionString;
    //private String dbType;
    private String database;
    private String wsBaseUrl;
    private String wsSpechome;
    private String wsFilterhome;
    private double eqSearchRadius;
    private String access;
    private String dasUrlBase;
    private double visualRA;
    private double visualDec;
    private double visualScale;
    private long exploreDefault;
    private String sdssUrlBase;
    private String sciserverLink;
    private String siteName;
    private String siteIcon;
    private String logURL;
    private String casJobs;
    private int defaultSpRerun;
    private int skyVersion;
    private String epoHelp;
    private String helpdesk;
    private String smtp;
    private String solarConnectionString;

    private String nDegrees;
    private String nObj;
    private String nStar;
    private String nGalaxy; //count from view galaxy
    private String nQuasar; //spectroscopic
    private String nSpec;
    private String nStarSpec;
    private String nStarNoSpec;
    private String nAsteroids; //unchanged for a while

    private double crossidRadius;
    private int defTimeout;		  // default timeout
    private int qaTimeout;		  // SkyQA.aspx (sdssQA) timeout
    private int sqlTimeout;		  // SQL search page timeout
    private int crossidTimeout;	  // crossid pages timeout
    private int formTimeout;		  // form query (IQS/SQS) timeout
    private int chartTimeout;	  // cutout service (finding chart etc.) timeout
    private int emacsTimeout;	  // special emacs timeout for RHL etc.
    private int rowLimit;		  // row limit on query results
    private int emacsRowLimit;	  // special row limit for RHL etc.
    private int queriesPerMinute;	  // number of queries per minute allowed from a given IP

    private String urlSolarSystemProj;
    private String urlProjRegister;

    private String apogeeSpectrumLink;
    private String apogeeFitsLink;


    public String getSolarConnectionString()
    {
        return solarConnectionString; 
    }

    public String getUrlProjRegister()
    {
          return urlProjRegister; 
    }

    public String getUrlSolarSystemProj()
    {
         return urlSolarSystemProj; 
    }

    public String getWSSpecHome()
    {
          return wsSpechome; 
    }

    public String getWSFilterHome()
    {
          return wsFilterhome; 
    }

    public double getCrossidRadius()
    {
       return crossidRadius; 
    }

    public int getDefTimeout()
    {
         return defTimeout; 
    }

    public int getQaTimeout()
    {
          return qaTimeout; 
    }

    public int getSqlTimeout()
    {
       return sqlTimeout;
    }

    public int getCrossidTimeout()
    {
        return crossidTimeout; 
    }

    public int getFormTimeout()
    {
         return formTimeout;
    }

    public int getChartTimeout()
    {
        return chartTimeout; 
    }

    public int getEmacsTimeout()
    {
         return emacsTimeout;
    }

    public int getEmacsRowLimit()
    {
        return emacsRowLimit; 
    }

    public int getQueriesPerMinute()
    {
         { return queriesPerMinute; }
    }

    public String getNDegrees()
    {
         { return nDegrees; }
    }

    public String getNObj()
    { 
         { return nObj; } 
    }

    public String getNStar()
    {
         { return nStar; }
    }

    public String getNGalaxy()
    {
         { return nGalaxy; }
    }

    public String getNQuasar()
    {
         { return nQuasar; }
    }

    public String getNSpec()
    {
         { return nSpec; }
    }

    public String getNStarSpec()
    {
         { return nStarSpec; }
    }

    public String getNStarNoSpec()
    {
         { return nStarNoSpec; }
    }

    public String getNAsteroids()
    {
         { return nAsteroids; }
    }

    public String getEpoHelp()
    {
         { return epoHelp; }
    }

    /*
    public String DbType
    {
        get { return "BEST"; }
    }
    */

    public String getCasJobs()
    {
          return casJobs; 
    }
    public String getContactUrl()
    {
         return "http://skyserver.sdss3.org/contact/?release=" + getRelease() + "&helpdesk=" + helpdesk + "&smtp=" + smtp + "&epoHelp=" + epoHelp + "&subject=SkyServer+" + getRelease() + "+issue:+";
    }
    public String getLogUrl()
    {
         return logURL; 
    }
    public String getSiteName()
    {
          return siteName; 
    }
    public String getSiteIcon()
    {
          return siteIcon; 
    }
    public String getSdssUrlBase()
    {
          return sdssUrlBase; 
    }
    public String getSciServerLink()
    {
          return sciserverLink; 
    }

    public int getReleaseNumber()
    {
         return releaseNumber; 
    }

    public String getRelease()
    {
         return "DR" + releaseNumber;
    }

    /*
    public String DBType
    {
        get { return dbType; }
    }
    */

    public String getDatabase()
    {
        return database; 
        //get { return DBType + Release; }
        //get { return "BESTTEST"; }
    }

    public String getConnectionString()
    {
         return connectionString + "Initial Catalog=" + getDatabase() + ";"; 
    }

    public String getWSBaseUrl()
    {
          return wsBaseUrl + getRelease() + "/"; 
    }

    public String getWSGetJpegUrl()
    {
          return getWSBaseUrl() + "ImgCutout/getjpeg.aspx"; 
    }

    public String getWSGetImage64()
    {
          return getWSBaseUrl() + "ImgCutout/getImage64.aspx"; 
    }

    public String getWSGetCodecUrl()
    {
         return getWSBaseUrl() + "ImgCutout/getjpegcodec.aspx"; 
    }

    public double getEqSearchRadius()
    {
         { return eqSearchRadius; }
    }

    public String getAccess()
    {
         return access;
    }

    public String getDasUrlBase()
    {
         return dasUrlBase; 
    }

    public String getDasUrl()
    {
         return getDasUrlBase();
    }

    public double getVisualRA()
    {
        return visualRA;
    }

    public double getVisualDec()
    {
        return visualDec;
    }

    public double getVisualScale()
    {
         return visualScale; 
    }

    public long getExploreDefault()
    {
         return exploreDefault;
    }

    public int getRowLimit()
    {
         return rowLimit; 
    }

    public String getSdssUrl()
    {
         return getSdssUrlBase() + "dr" + getReleaseNumber() + "/"; 
    }

    public int getDefaultSpRerun()
    {
          return defaultSpRerun; 
    }

    public int getSkyVersion()
    {
          return skyVersion; 
    }

    public String getApogeeSpectrumLink()
    {
          return apogeeSpectrumLink; 
    }

    public String getApogeeFitsLink()
    {
          return apogeeFitsLink; 
    }

    public Globals()
    {
// old       Object appSettings = System.Web.Configuration.WebConfigurationManager.AppSettings;
        this.solarConnectionString = ""/*old appSettings["solarConnectionString"]*/;
        this.releaseNumber = Integer.parseInt("9");
        //this.dbType = appSettings["dbType"];
        this.connectionString = ""/* old appSettings["connectionString"]*/;
        this.wsBaseUrl = "http://skyservice.pha.jhu.edu/";
        this.eqSearchRadius = Double.parseDouble("0.75");
        this.access = "public";
        this.dasUrlBase = "http://dr12.sdss3.org/";
        this.visualRA = Double.parseDouble("132.749");
        this.visualDec = Double.parseDouble("11.656");
        this.visualScale = Double.parseDouble("0.79224");
        this.exploreDefault = Long.parseLong("1237668296598749280");
        this.sdssUrlBase = "http://www.sdss.org/";
        this.sciserverLink = "http://www.sciserver.org/"/*appSettings["sciserverLink"]*/ ;

        this.siteName = "";
        this.siteIcon = "../images/flag_us.gif";
        this.logURL = "http://skyserver.sdss.org/log/en/traffic/";
        this.casJobs = "http://skyserver.sdss.org/CasJobs/";
        this.defaultSpRerun = Integer.parseInt("26");
        this.skyVersion = Integer.parseInt("2");
        this.epoHelp = "raddick@pha.jhu.edu";
        this.wsSpechome = "http://www.voservices.net/spectrum/";
        this.wsFilterhome = "http://www.voservices.net/filter/";
        this.urlSolarSystemProj = "http://skyserver.sdss.org/solarsystem/";
        this.urlProjRegister = "http://skyserver.pha.jhu.edu/register/";
        this.apogeeSpectrumLink = "http://dr12.sdss3.org/irSpectrumDetail";
        this.apogeeFitsLink = "http://dr12.sdss3.org/sas/dr12/apogee/spectro/redux/r5";
        this.helpdesk = "helpdesk@sdss3.org";
        this.smtp = "mail.pha.jhu.edu";
        this.database = "BESTDR9";

        this.defTimeout = Integer.parseInt("600");
        this.qaTimeout = Integer.parseInt("3600");
        this.sqlTimeout = Integer.parseInt("600");
        this.crossidTimeout = Integer.parseInt("1800");
        this.crossidRadius = Double.parseDouble( "3.0");
        this.formTimeout = Integer.parseInt("600");
        this.chartTimeout = Integer.parseInt("600");
        this.emacsTimeout = Integer.parseInt("600");
        this.rowLimit = Integer.parseInt("500000");
        this.emacsRowLimit = Integer.parseInt("500000");
        this.queriesPerMinute = Integer.parseInt("60");

        if (releaseNumber == 8)
        {
            //DR7 values still in places
            nDegrees = "14555";
            nObj = "469 million";
            nStar = "261 million";
            nGalaxy = "208 million"; //count from view galaxy
            nQuasar = "130,300"; //spectroscopic
            nSpec = "1,843,200";
            nStarSpec = "600,967";
            nStarNoSpec = "260 million";
            nAsteroids = "200,000"; //unchanged for a while
        }

        // All these settings are now in Web.config
        /*
        if (access == "public")
        {
            defTimeout = 600;
            qaTimeout = 3600;
            sqlTimeout = 600;
            crossidTimeout = 1800;
            crossidRadius = 3.0;
            formTimeout = 600;
            chartTimeout = 600;
            emacsTimeout = 600;
            rowLimit = 500000;
            emacsRowLimit = 500000;
            queriesPerMinute = 60;
        }
        else if (access == "collab")
        {
            defTimeout = 600;
            qaTimeout = 36000;
            sqlTimeout = 600;
            crossidTimeout = 3600;
            crossidRadius = 30.0;
            formTimeout = 3600;
            chartTimeout = 3600;
            emacsTimeout = 36000;
            rowLimit = 500000;
            emacsRowLimit = 50000000;
            queriesPerMinute = 60;
        }
        else
        {
            defTimeout = 600;
            qaTimeout = 18000;
            sqlTimeout = 600;
            crossidTimeout = 3600;
            crossidRadius = 3.0;
            formTimeout = 3600;
            chartTimeout = 3600;
            emacsTimeout = 3600;
            rowLimit = 500000;
            emacsRowLimit = 100000;
            queriesPerMinute = 60;
        }
        */
    }

}
