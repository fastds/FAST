package org.fastds.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

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
    public Integer mjd;
    public Short plate;

    public Integer run ;
    public Short rerun;
    public Short camcol;
    public Short field ;
    
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
        hrefs.Summary  = "summary?"+allID;
        hrefs.PhotoObj = "DisplayResults?name=PhotoObj&"+allID;
        hrefs.PhotoTag = "DisplayResults?name=PhotoTag&" + allID;
        hrefs.Field    = "DisplayResults?name=Field&" + allID;
        hrefs.Frame    = "DisplayResults?name=Frame&" + allID;


        if (globals.getReleaseNumber() >= 8) 
        {
            hrefs.Galaxyzoo = "GalaxyZoo?" + allID;
        }            

       if (globals.getReleaseNumber() > 4)
        {
            hrefs.PhotoZ = "DisplayResults?&name=photoZ&" + allID;
           // hrefs.PhotozRF = "DisplayResults.aspx?&name=photozRF&" + allID;
        } 

        hrefs.Matches = "Matches?"+allID;
        hrefs.Neighbors = "Neighbors?"+allID;
        hrefs.Chart    = "javascript:gotochart(" + ra + "," + dec + ");";
        hrefs.Navigate = "javascript:gotonavi(" + ra + "," + dec + ");";
        hrefs.SaveBook = "javascript:saveBook(\"" + objID + "\");";
        hrefs.ShowBook = "javascript:showNotes();";
        
       if (globals.getDatabase().startsWith("STRIPE"))
        {
                if (run == 106)  run = 100006;
                if (run == 206)  run = 200006;
        } 

        hrefs.FITS = "FitsImg?"+allID;//id=" + id + "&fieldID=" + fieldID + "&spec=" + specID + "&apid=" + apid;
        
        hrefs.NED = "http://nedwww.ipac.caltech.edu/cgi-bin/nph-objsearch?search_type=Near+Position+Search"
                  + "&in_csys=Equatorial&in_equinox=J2000.0&obj_sort=Distance+to+search+center"
                  + "&lon=" + (ra != null?(new BigDecimal(ra).setScale(7, BigDecimal.ROUND_HALF_EVEN).toString()+"d"):"") 
                  + "&lat=" + (dec != null?(new BigDecimal(dec).setScale(7, BigDecimal.ROUND_HALF_EVEN).toString()+"d"):"") + "&radius=1.0";
        									//ROUND_HALF_EVEN:银行家舍入法
        String hmsRA;
            hmsRA = Utilities.hmsPad(ra ==null ? 0:ra).replace(" ", "+");

        String dmsDec;
            if (dec != null && dec >= 0)
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
        hrefs.AllSpec = "AllSpec?"+allID;

        if (specID != null)
        {
            hrefs.SpecObj = "DisplayResults?name=SpecObj&" + allID;
            hrefs.sppLines = "DisplayResults?name=sppLines&" + allID;
            hrefs.sppParams = "DisplayResults?name=sppParams&" + allID;
            hrefs.galSpecLine = "DisplayResults?name=galSpecLine&" + allID;
            hrefs.galSpecIndx = "DisplayResults?name=galSpecIndx&" + allID;
            hrefs.galSpecInfo = "DisplayResults?name=galSpecInfo&" + allID;

            hrefs.Plate = "Plate?&name=Plate&plateID=" + plateID;

            hrefs.Spectrum = "../../get/SpecByID.ashx?ID=" + specID;

            hrefs.SpecFITS = "FitsSpec?&sid=" + specObjID + "&id=" + id + "&spec=" + specID + "&apid=" + apid;
                
           if (globals.getReleaseNumber() >= 8)
            {  
                hrefs.theParameters = "Parameters?"+allID;
                hrefs.stellarMassStarformingPort = "DisplayResults?name=stellarMassStarFormingPort&" + allID;
                hrefs.stellarMassPassivePort = "DisplayResults?name=stellarMassPassivePort&" + allID;
                hrefs.emissionLinesPort = "DisplayResults?name=emissionlinesPort&" + allID;
                hrefs.stellarMassPCAWiscBC03 = "DisplayResults?name=stellarMassPCAWiscBC03&" + allID;
                hrefs.stellarMassPCAWiscM11 = "DisplayResults?name=stellarMassPCAWiscM11&" + allID;
            }  
            if (globals.getReleaseNumber() >= 10)
            {
                hrefs.stellarMassFSPSGranEarlyDust = "DisplayResults?name=stellarMassFSPSGranEarlyDust&" + allID;
                hrefs.stellarMassFSPSGranEarlyNoDust = "DisplayResults?name=stellarMassFSPSGranEarlyNoDust&" + allID;
                hrefs.stellarMassFSPSGranWideDust = "DisplayResults?name=stellarMassFSPSGranWideDust&" + allID;
                hrefs.stellarMassFSPSGranWideNoDust = "DisplayResults?name=stellarMassFSPSGranWideNoDust&" + allID;
             }
         }            
         if (apid != null && !apid.isEmpty())
         {
             hrefs.apogeeStar = "DisplayResults?name=apogeeStar&" + allID;
             hrefs.aspcapStar = "DisplayResults?name=aspcapStar&" + allID;
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
     */
    public String showVTable(ResultSet ds, int w)
    {
    	StringBuilder res = new StringBuilder();
    	
        char c = 't'; String unit = "test";
        res.append("<table cellpadding=2 cellspacing=2 border=0");
        if (w > 0)
            res.append(" width=" + w);
        res.append(">\n");
        ResultSetMetaData meta = null;
        try {
        	meta = ds.getMetaData();
			if (!ds.isAfterLast())
			{
			        for (int k = 1; k <= meta.getColumnCount(); k++)
			        {
			            res.append("<tr align='left' >");
			            res.append("<td  valign='top' class='h'>");
			            res.append("<span ");
			            if (unit != "")
			                res.append("ONMOUSEOVER=\"this.T_ABOVE=true;this.T_WIDTH='100';return escape('<i>unit</i>=" + unit + "')\" ");
			            res.append("></span>");
			            res.append(meta.getColumnName(k) + "</td>");

			            res.append("<td valign='top' class='" + c + "'>");
			            if ("bool".endsWith(meta.getColumnTypeName(k))) {
							res.append(ds.getBoolean(k));
						} else if ("int".startsWith(meta.getColumnTypeName(k))) {
							res.append(ds.getBigDecimal(k));
						} else if ("string".endsWith(meta.getColumnTypeName(k))) {
							String str = ds.getString(k);
							if(str.contains("<"))
								str = str.replace("<", "&lt;");
							if(str.contains(">"))
								str = str.replace(">","&gt;");
							//System.out.println(str);
							res.append(str);
							
						} else if ("datetime".endsWith(meta.getColumnTypeName(k))) {
							
							res.append(ds.getTime(k));
						} else {
							res.append(ds.getDouble(k));
						}
			            res.append("</td>");
			            res.append("</tr>");
			        }
			}
			else {
			    res.append("<tr> <td class='nodatafound'>No data found for this object </td></tr>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        res.append("</table>");
        return res.toString();
    } 

    /**
     * Added new HTable with namevalue pair options
     * 返回要显示的页面元素
     */
    public String showHTable(ResultSet rs, int w, String target)
    {
    	StringBuilder res = new StringBuilder();
    	ResultSetMetaData meta = null;
        char c = 't'; String unit = "test";

        res.append("<table cellpadding=2 cellspacing=2 border=0");
        if (w > 0)
        	res.append(" width=" + w);
        res.append(">\n");

        res.append("<tr>");

        try {
        meta = rs.getMetaData();
        int colCount =  meta.getColumnCount();
			if (!rs.isAfterLast())
			{
			    for (int k = 1; k <= colCount; k++)
			    {
			    	res.append("<td align='middle' class='h'>");
			    	res.append("<span ");
			        if (unit != "")
			        	res.append("ONMOUSEOVER=\"this.T_ABOVE=true;this.T_WIDTH='100';return escape('<i>unit</i>=" + unit + "')\" ");
			        res.append("></span>");
			        res.append(meta.getColumnName(k) + "</td>");
			    }
			    res.append("</tr>");


			    while (!rs.isAfterLast())
			    {
			        res.append("<tr>");

			        for (int k = 1; k <= colCount; k++)
			        {
			            res.append("<td nowrap align='middle' class='" + c + "'>");

			            // think something else if possible for this
			            if (target.equals("AllSpectra") && k == 1)
			            {
			                String u = "<a class='content' target='_top' href='Summary?sid=";
//      old                      res.append(u + rs.GetValue(k) + "'>" + rs.GetValue(k) + "</a></td>");
			                if ("bool".endsWith(meta.getColumnTypeName(k))) {
								res.append(u + rs.getBoolean(meta.getColumnLabel(k))+ "'>" + rs.getBoolean(meta.getColumnLabel(k)) + "</a></td>");
							} else if ("int64".endsWith(meta.getColumnTypeName(k))) {
								res.append(u + rs.getBigDecimal(meta.getColumnLabel(k))+ "'>" + rs.getBigDecimal(meta.getColumnLabel(k)) + "</a></td>");
							} else if ("string".endsWith(meta.getColumnTypeName(k))) {
								String str = u + rs.getString(meta.getColumnTypeName(k))+ "'>" + rs.getString(meta.getColumnLabel(k)) + "</a></td>";
								if(str.contains("<"))
									str = str.replace("<", "&lt;");
								if(str.contains(">"))
									str = str.replace(">","&gt;");
								//System.out.println(str);
								res.append(str);
								
							} else if ("datetime".endsWith(meta.getColumnTypeName(k))) {
								
								res.append(u + rs.getTime(meta.getColumnLabel(k))+ "'>" + rs.getTime(meta.getColumnLabel(k)) + "</a></td>");
							} else {
								res.append(u + rs.getDouble(meta.getColumnLabel(k))+ "'>" + rs.getDouble(meta.getColumnLabel(k)) + "</a></td>");
							}
			            }

			            else if (target.equals("Neighbors") && k == 1)
			            {
			                String u = "<a class='content' target='_top' href='Summary?id=";
//                  old          res.append(u + reader.GetValue(k) + "'>" + reader.GetValue(k) + "</a></td>");
			                if ("bool".endsWith(meta.getColumnTypeName(k))) {
								res.append(u + rs.getBoolean(meta.getColumnLabel(k))+ "'>" + rs.getBoolean(meta.getColumnLabel(k)) + "</a></td>");
							} else if ("int64".endsWith(meta.getColumnTypeName(k))) {
								res.append(u + rs.getBigDecimal(meta.getColumnLabel(k))+ "'>" + rs.getBigDecimal(meta.getColumnLabel(k)) + "</a></td>");
							} else if ("string".endsWith(meta.getColumnTypeName(k))) {
								String str = u + rs.getString(meta.getColumnTypeName(k))+ "'>" + rs.getString(meta.getColumnLabel(k)) + "</a></td>";
								if(str.contains("<"))
									str = str.replace("<", "&lt;");
								if(str.contains(">"))
									str = str.replace(">","&gt;");
								//System.out.println(str);
								res.append(str);
								
							} else if ("datetime".endsWith(meta.getColumnTypeName(k))) {
								
								res.append(u + rs.getTime(meta.getColumnLabel(k))+ "'>" + rs.getTime(meta.getColumnLabel(k)) + "</a></td>");
							} else {
								res.append(u + rs.getDouble(meta.getColumnLabel(k))+ "'>" + rs.getDouble(meta.getColumnLabel(k)) + "</a></td>");
							}
			            }

			            else
			            {
//                            res.append(reader.GetValue(k));
			            	if ("bool".endsWith(meta.getColumnTypeName(k))) {
								res.append(rs.getBoolean(meta.getColumnLabel(k)));
							} else if ("int64".endsWith(meta.getColumnTypeName(k))) {
								res.append(rs.getBigDecimal(meta.getColumnLabel(k)));
							} else if ("string".endsWith(meta.getColumnTypeName(k))) {
								String str = rs.getString(meta.getColumnTypeName(k));
								if(str.contains("<"))
									str = str.replace("<", "&lt;");
								if(str.contains(">"))
									str = str.replace(">","&gt;");
								//System.out.println(str);
								res.append(str);
								
							} else if ("datetime".endsWith(meta.getColumnTypeName(k))) {
								
								res.append(rs.getTime(meta.getColumnLabel(k)));
							} else {
								res.append(rs.getDouble(meta.getColumnLabel(k)));
							}
			            }
			            res.append("</td>");
			        }
			    }
			}
			else {
			    res.append(" <td class='nodatafound'>No data found for this object </td>");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        res.append("</tr>");

        res.append("</table>");
        return res.toString();
    }

    public String getEnUrl() {
    	return enUrl;
    }
    
    public int getTabwidth() {
    	return tabwidth;
    }
}
