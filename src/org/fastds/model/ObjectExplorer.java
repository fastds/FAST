package org.fastds.model;

import java.math.BigDecimal;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.explorehelpers.ObjectInfo;

import edu.gzu.utils.Utilities;

public class ObjectExplorer {
	protected final String ZERO_ID = "0x0000000000000000";
	protected Globals globals;
    protected HRefs hrefs = new HRefs();
    protected String enUrl;
    protected String url = null;

	protected int tabwidth = 128;        

    public ExplorerQueries exploreQuery;       

    public Long id = null;        
    public Long specID = null;
    public String apid;
    public String objID = null;
    public String specObjID = null;
            
    public Double ra = null;
    public Double dec = null;

    public String plateID = null;
    public String fieldID = null;
    public Short fiberID = null;
    public int mjd;
    public short plate;

    public int run ;
    public short rerun;
    public short camcol;
    public short field ;
    
    public String getApid() {
		return apid;
	}

	public Globals getGlobals() {
		return globals;
	}

	public String getSpecObjID() {
		return specObjID;
	}

	public ObjectExplorer(ObjectInfo objectInfo)
    {
    	load(objectInfo);
    }
    
    protected void load(ObjectInfo objectInfo)
    {
        
       globals = new Globals(); 
        
        exploreQuery = new ExplorerQueries();           
        //Get Values from Session Object  从session对象中获取值
        getSessionIDs(objectInfo);

        url = getURL();
        enUrl = getEnURL(); 
       
        // common query to explorer
        String allID ="id="+id + "&spec=" + specID + "&apid=" + apid+"&field="+fieldID;

        
        // id is the decimal representation; objID is the hex representation.
        hrefs.Summary  = "summary.aspx?"+allID;
        hrefs.PhotoObj = "DisplayResults.aspx?name=PhotoObj&"+allID;
        hrefs.PhotoTag = "DisplayResults.aspx?name=PhotoTag&" + allID;
        hrefs.Field    = "DisplayResults.aspx?name=Field&" + allID;
        hrefs.Frame    = "DisplayResults.aspx?name=Frame&" + allID;


        if (globals.getReleaseNumber() >= 8) 
        {
            hrefs.Galaxyzoo = "galaxyzoo.aspx?" + allID;
        }            

       if (globals.getReleaseNumber() > 4)
        {
            hrefs.PhotoZ = "DisplayResults.aspx?&name=photoZ&" + allID;
           // hrefs.PhotozRF = "DisplayResults.aspx?&name=photozRF&" + allID;
        } 

        hrefs.Matches = "matches.aspx?"+allID;
        hrefs.Neighbors = "neighbors.aspx?"+allID;
        hrefs.Chart    = "javascript:gotochart(" + ra + "," + dec + ");";
        hrefs.Navigate = "javascript:gotonavi(" + ra + "," + dec + ");";
        hrefs.SaveBook = "javascript:saveBook(\"" + objID + "\");";
        hrefs.ShowBook = "javascript:showNotes();";
        
       if (globals.getDatabase().startsWith("STRIPE"))
        {
                if (run == 106)  run = 100006;
                if (run == 206)  run = 200006;
        } 

        hrefs.FITS = "fitsimg.aspx?"+allID;//id=" + id + "&fieldID=" + fieldID + "&spec=" + specID + "&apid=" + apid;
        
        hrefs.NED = "http://nedwww.ipac.caltech.edu/cgi-bin/nph-objsearch?search_type=Near+Position+Search"
                  + "&in_csys=Equatorial&in_equinox=J2000.0&obj_sort=Distance+to+search+center"
                  + "&lon=" + (ra != null?(new BigDecimal(ra).setScale(7, BigDecimal.ROUND_HALF_EVEN).toString()+"d"):"") 
                  + "&lat=" + (dec != null?(new BigDecimal(dec).setScale(7, BigDecimal.ROUND_HALF_EVEN).toString()+"d"):"") + "&radius=1.0";
        									//ROUND_HALF_EVEN:银行家舍入法
        String hmsRA;
            hmsRA = Utilities.hmsPad(ra ==null ? 0:ra).replace(" ", "+");

        String dmsDec;
            if (dec >= 0)
                dmsDec = Utilities.dmsPad(dec ==null ? 0:dec).replace("+", "%2B");
            else
                dmsDec = Utilities.dmsPad(dec ==null ? 0:dec);
            dmsDec = dmsDec.replace(" ", "+");

        hrefs.SIMBAD = "http://simbad.u-strasbg.fr/sim-id.pl?protocol=html&IDent=" + hmsRA + "+" + dmsDec + "&NbIDent=1"
                                + "&Radius=1.0&Radius.unit=arcmin&CooFrame=FK5&CooEpoch=2000&CooEqui=2000"
                                + "&output.max=all&o.catall=on&output.mesdisp=N&Bibyear1=1983&Bibyear2=2005"
                                + "&Frame1=FK5&Frame2=FK4&Frame3=G&Equi1=2000.0&Equi2=1950.0&Equi3=2000.0"
                                + "&Epoch1=2000.0&Epoch2=1950.0&Epoch3=2000.0";

        hrefs.ADS = "http://adsabs.harvard.edu/cgi-bin/nph-abs_connect?db_key=AST&sim_query=YES&aut_xct=NO&aut_logic=OR"
                                + "&obj_logic=OR&author=&object=" + hmsRA + "+" + dmsDec + "&start_mon=&start_year=&end_mon="
                                + "&end_year=&ttl_logic=OR&title=&txt_logic=OR&text=&nr_to_return=100&start_nr=1"
                                + "&start_entry_day=&start_entry_mon=&start_entry_year=&min_score=&jou_pick=ALL"
                                + "&ref_stems=&data_and=ALL&group_and=ALL&sort=SCORE&aut_syn=YES&ttl_syn=YES"
                                + "&txt_syn=YES&aut_wt=1.0&obj_wt=1.0&ttl_wt=0.3&txt_wt=3.0&aut_wgt=YES&obj_wgt=YES"
                                + "&ttl_wgt=YES&txt_wgt=YES&ttl_sco=YES&txt_sco=YES&version=1";

        hrefs.Print = "framePrint();";
        hrefs.AllSpec = "allSpec.aspx?"+allID;

        if (specID != null)
        {
            hrefs.SpecObj = "DisplayResults.aspx?name=SpecObj&" + allID;
            hrefs.sppLines = "DisplayResults.aspx?name=sppLines&" + allID;
            hrefs.sppParams = "DisplayResults.aspx?name=sppParams&" + allID;
            hrefs.galSpecLine = "DisplayResults.aspx?name=galSpecLine&" + allID;
            hrefs.galSpecIndx = "DisplayResults.aspx?name=galSpecIndx&" + allID;
            hrefs.galSpecInfo = "DisplayResults.aspx?name=galSpecInfo&" + allID;

            hrefs.Plate = "plate.aspx?&name=Plate&plateID=" + plateID;

            hrefs.Spectrum = "../../get/SpecByID.ashx?ID=" + specID;

            hrefs.SpecFITS = "fitsspec.aspx?&sid=" + specObjID + "&id=" + id + "&spec=" + specID + "&apid=" + apid;
                
           if (globals.getReleaseNumber() >= 8)
            {  
                hrefs.theParameters = "parameters.aspx?"+allID;
                hrefs.stellarMassStarformingPort = "DisplayResults.aspx?name=stellarMassStarFormingPort&" + allID;
                hrefs.stellarMassPassivePort = "DisplayResults.aspx?name=stellarMassPassivePort&" + allID;
                hrefs.emissionLinesPort = "DisplayResults.aspx?name=emissionlinesPort&" + allID;
                hrefs.stellarMassPCAWiscBC03 = "DisplayResults.aspx?name=stellarMassPCAWiscBC03&" + allID;
                hrefs.stellarMassPCAWiscM11 = "DisplayResults.aspx?name=stellarMassPCAWiscM11&" + allID;
            }  
            if (globals.getReleaseNumber() >= 10)
            {
                hrefs.stellarMassFSPSGranEarlyDust = "DisplayResults.aspx?name=stellarMassFSPSGranEarlyDust&" + allID;
                hrefs.stellarMassFSPSGranEarlyNoDust = "DisplayResults.aspx?name=stellarMassFSPSGranEarlyNoDust&" + allID;
                hrefs.stellarMassFSPSGranWideDust = "DisplayResults.aspx?name=stellarMassFSPSGranWideDust&" + allID;
                hrefs.stellarMassFSPSGranWideNoDust = "DisplayResults.aspx?name=stellarMassFSPSGranWideNoDust&" + allID;
             }
         }            
         if (apid != null && !apid.isEmpty())
         {
             hrefs.apogeeStar = "DisplayResults.aspx?name=apogeeStar&" + allID;
             hrefs.aspcapStar = "DisplayResults.aspx?name=aspcapStar&" + allID;
         }
    }

    private void getSessionIDs(ObjectInfo o) {

//        ObjectInfo o =(ObjectInfo)Session["objectInfo"];   old ---
        objID = o.objID;
        specObjID = o.specObjID;
        apid = o.apid;

        ra = o.ra;
        dec = o.dec;

        plateID =o.plateID;
        fieldID = o.fieldID;
        fiberID = o.fiberID;
        mjd = o.mjd;
        plate = o.plate;

        run = o.run;
        rerun = o.rerun;
        camcol = o.camcol;
        field = o.field;

        id = o.id;
        specID = o.specID;
    }

    public Long getId() {
		return id;
	}

	public String getZERO_ID() {
		return ZERO_ID;
	}

	public HRefs getHrefs() {
		return hrefs;
	}

	public String getUrl() {
		return url;
	}

	public ExplorerQueries getExploreQuery() {
		return exploreQuery;
	}

	public Long getSpecID() {
		return specID;
	}

	public String getObjID() {
		return objID;
	}

	public Double getRa() {
		return ra;
	}

	public Double getDec() {
		return dec;
	}

	public String getPlateID() {
		return plateID;
	}

	public String getFieldID() {
		return fieldID;
	}

	public Short getFiberID() {
		return fiberID;
	}

	public int getMjd() {
		return mjd;
	}

	public short getPlate() {
		return plate;
	}

	public int getRun() {
		return run;
	}

	public short getRerun() {
		return rerun;
	}

	public short getCamcol() {
		return camcol;
	}

	public short getField() {
		return field;
	}

	public String getURL()
    {
    	return "";
        /*String host = Request.ServerVariables["SERVER_NAME"];
        String path = Request.ServerVariables["SCRIPT_NAME"];     

        String root = "http://" + host;
        String s = path;
        String[] q = s.split("/");

        String lang = "";
        for (int i = 0; i < q.length; i++)
        {
            if (i > 0) root += "/";
            root += q[i];
            lang = q[i];
            if ( lang == "en" || lang == "de" || lang == "jp"
              || lang == "hu" || lang == "sp" || lang == "ce" || lang == "pt" 
              || lang == "zh" || lang == "uk" || lang == "ru")
            {
                //depth = q.length - i - 2;
                return root;
            }
        }
        return root;  old */
    }

    public String getEnURL()
    {
    	return "http://skyserver.sdss.org/dr12/en";
        /*String host = Request.ServerVariables["SERVER_NAME"];
        String path = Request.ServerVariables["SCRIPT_NAME"];

        String root = "http://" + host;
        String s = path;
        String[] q = s.split("/");

        String lang = "";
        for (int i = 0; i < q.length; i++)
        {
            if (i > 0) root += "/";
            lang = q[i];
            if (lang == "en" || lang == "de" || lang == "jp"
              || lang == "hu" || lang == "sp" || lang == "ce" || lang == "pt" || lang == "zh" || lang == "uk" || lang == "ru")
            {
                //depth = q.Length - i - 2;
                root += "en";
                return root;
            }
            else
            {
                root += q[i];
            }
        }
        return root; old */
    }

    // ***** Functions *****

  

    /**
     * Vertical aligned table  With DataSet
     *//*
    public void showVTable(DataSet ds, int w)
    {
        using (DataTableReader reader = ds.Tables[0].CreateDataReader())
        {
            char c = 't'; String unit = "test";
            Response.Write("<table cellpadding=2 cellspacing=2 border=0");
            if (w > 0)
                Response.Write(" width=" + w);
            Response.Write(">\n");
            if (reader.HasRows)
            {
                if (reader.Read())
                {
                    for (int k = 0; k < reader.FieldCount; k++)
                    {
                        Response.Write("<tr align='left' >");
                        Response.Write("<td  valign='top' class='h'>");
                        Response.Write("<span ");
                        if (unit != "")
                            Response.Write("ONMOUSEOVER=\"this.T_ABOVE=true;this.T_WIDTH='100';return escape('<i>unit</i>=" + unit + "')\" ");
                        Response.Write("></span>");
                        Response.Write(reader.GetName(k) + "</td>");

                        Response.Write("<td valign='top' class='" + c + "'>");
                        Response.Write(reader.GetValue(k));
                        Response.Write("</td>");
                        Response.Write("</tr>");
                    }
                }
            }
            else {
                Response.Write("<tr> <td class='nodatafound'>No data found for this object </td></tr>");
            }
            Response.Write("</table>");
        }
    } old --*/

    /**
     * Added new HTable with namevalue pair options
     */
    /*  old   public void showHTable(DataSet ds, int w, String target)
    {
        using (DataTableReader reader = ds.Tables[0].CreateDataReader())
        {
            char c = 't'; String unit = "test";

            Response.Write("<table cellpadding=2 cellspacing=2 border=0");

            if (w > 0)
                Response.Write(" width=" + w);
            Response.Write(">\n");

            Response.Write("<tr>");

            if (reader.HasRows)
            {
                for (int k = 0; k < reader.FieldCount; k++)
                {
                    Response.Write("<td align='middle' class='h'>");
                    Response.Write("<span ");
                    if (unit != "")
                        Response.Write("ONMOUSEOVER=\"this.T_ABOVE=true;this.T_WIDTH='100';return escape('<i>unit</i>=" + unit + "')\" ");
                    Response.Write("></span>");
                    Response.Write(reader.GetName(k) + "</td>");
                }
                Response.Write("</tr>");


                while (reader.Read())
                {
                    Response.Write("<tr>");

                    for (int k = 0; k < reader.FieldCount; k++)
                    {
                        Response.Write("<td nowrap align='middle' class='" + c + "'>");

                        // think something else if possible for this
                        if (target.Equals("AllSpectra") && k == 0)
                        {
                            String u = "<a class='content' target='_top' href='Summary.aspx?sid=";
                            Response.Write(u + reader.GetValue(k) + "'>" + reader.GetValue(k) + "</a></td>");
                        }

                        else if (target.Equals("Neighbors") && k == 0)
                        {
                            String u = "<a class='content' target='_top' href='Summary.aspx?id=";
                            Response.Write(u + reader.GetValue(k) + "'>" + reader.GetValue(k) + "</a></td>");
                        }

                        else
                        {
                            Response.Write(reader.GetValue(k));
                        }
                        Response.Write("</td>");
                    }
                }
            }
            else {
                Response.Write(" <td class='nodatafound'>No data found for this object </td>");
            }

            Response.Write("</tr>");

            Response.Write("</table>");
        }
    }*/

    public String getEnUrl() {
    	return enUrl;
    }
    
    public int getTabwidth() {
    	return tabwidth;
    }
}
