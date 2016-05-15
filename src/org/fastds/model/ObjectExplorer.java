package org.fastds.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.fastds.explorehelpers.ObjectInfo;

import edu.gzu.utils.Utilities;

public class ObjectExplorer {
	protected final String ZERO_ID = "0x0000000000000000";
	protected Globals globals;
    protected HRefs hrefs = new HRefs();
    protected String enUrl;
    protected String url = null;

	protected int tabwidth = 128;        


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
		init(objectInfo);
    }
    
    protected void init(ObjectInfo objectInfo)
    {
        
       this.globals = new Globals(); 
        
        //从session对象中获取值
        getSessionIDs(objectInfo);

//        this.url = getURL();
//        this.enUrl = getEnURL(); 
        this.url = "http://skyserver.sdss.org";
        this.enUrl = "http://skyserver.sdss.org/en";
       
        buildURLs();
    }
    /**
     * 构建页面上可以访问的所有链接
     */
   private void buildURLs() {
	   // common query to explorer
       String allID ="id="+id + "&spec=" + specID + "&apid=" + apid+"&field="+fieldID;

       
       // id is the decimal representation; objID is the hex representation.
       hrefs.Summary  = "Summary?"+allID;
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

           hrefs.Spectrum = "/v1/image/SpecById/" + specID;

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
           /*if (globals.getReleaseNumber() >= 10)  DR9 不支持
           {
               hrefs.stellarMassFSPSGranEarlyDust = "DisplayResults?name=stellarMassFSPSGranEarlyDust&" + allID;
               hrefs.stellarMassFSPSGranEarlyNoDust = "DisplayResults?name=stellarMassFSPSGranEarlyNoDust&" + allID;
               hrefs.stellarMassFSPSGranWideDust = "DisplayResults?name=stellarMassFSPSGranWideDust&" + allID;
               hrefs.stellarMassFSPSGranWideNoDust = "DisplayResults?name=stellarMassFSPSGranWideNoDust&" + allID;
            }*/
        }            
        if (apid != null && !apid.isEmpty())
        {
            hrefs.apogeeStar = "DisplayResults?name=apogeeStar&" + allID;
            hrefs.aspcapStar = "DisplayResults?name=aspcapStar&" + allID;
        }
		
	}

 private void getSessionIDs(ObjectInfo o) {

        this.objID = o.objID;
        this.specObjID = o.specObjID;
        this.apid = o.apid;

        this.ra = o.ra;
        this.dec = o.dec;

        this.plateID =o.plateID;
        this.fieldID = o.fieldID;
        this.fiberID = o.fiberID;
        this.mjd = o.mjd;
        this.plate = o.plate;

        this.run = o.run;
        this.rerun = o.rerun;
        this.camcol = o.camcol;
        this.field = o.field;

        this.id = o.id;
        this.specID = o.specID;
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
    	return "http://skyserver.sdss.org";
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
			if (ds!=null && !ds.isAfterLast())
			{
				meta = ds.getMetaData();
			        for (int k = 1; k <= meta.getColumnCount(); k++)
			        {	
//			        	System.out.println("Label:::"+meta.getColumnLabel(k));
//			        	System.out.println("Name:::"+meta.getColumnName(k));
			            res.append("<tr align='left' >");
			            res.append("<td  valign='top' class='h'>");
			            res.append("<span ");
			            if (unit != "")
			                res.append("ONMOUSEOVER=\"this.T_ABOVE=true;this.T_WIDTH='100';return escape('<i>unit</i>=" + unit + "')\" ");
			            res.append("></span>");
			            res.append(meta.getColumnName(k) + "</td>");

			            res.append("<td valign='top' class='" + c + "'>");
			            String typeName = meta.getColumnTypeName(k);
			            if ("bool".endsWith(typeName)) {
							res.append(ds.getBoolean(k));
						} else if (meta.getColumnTypeName(k).startsWith("int")) {
							res.append(ds.getLong(k));
						} else if ("string".endsWith(typeName)) {
							String str = ds.getString(k);
							if("img".equals(meta.getColumnName(k)))
								res.append("image");
							else if(str!=null)
							{
								if(str.contains("<"))
									str = str.replace("<", "&lt;");
								if(str.contains(">"))
									str = str.replace(">","&gt;");
								res.append(str);
							}
							else
								res.append("");
						} else if ("datetime".endsWith(typeName)) {
							
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
			e.printStackTrace();
		}
        res.append("</table>");
        return res.toString();
    } 

    /**
     * Added new HTable with namevalue pair options
     * 返回要显示的页面元素
     */
    public String showHTable(Map<Integer,List<Object>> rs, int w, String target)
    {
    	StringBuilder res = new StringBuilder();
        char c = 't'; String unit = "test";

        res.append("<table cellpadding=2 cellspacing=2 border=0");
        if (w > 0)
        	res.append(" width=" + w);
        res.append(">\n");

        res.append("<tr>");

        if (rs!=null && rs.size()>0)
		{
			List<Object> list =  rs.get(0);
			int colCount =  list.size();
		    for (int k = 0; k < colCount; k++)
		    {
		    	res.append("<td align='middle' class='h'>");
		    	res.append("<span ");
		        if (unit != "")
		        	res.append("ONMOUSEOVER=\"this.T_ABOVE=true;this.T_WIDTH='100';return escape('<i>unit</i>=" + unit + "')\" ");
		        res.append("></span>");
		        res.append(list.get(k) + "</td>");
		    }
		    res.append("</tr>");

		    int rows = 1;
		    while (rows < rs.size())
		    {
		    	list = rs.get(rows++);
		        res.append("<tr>");

		        for (int k = 0; k < colCount; k++)
		        {
		            res.append("<td nowrap align='middle' class='" + c + "'>");

		            // think something else if possible for this
		            if (target.equals("AllSpectra") && k == 0)
		            {
		                String u = "<a class='content' target='_top' href='Summary?sid=";
		                res.append(u + list.get(k)+ "'>" + list.get(k) + "</a></td>");
		            }
		            else if (target.equals("Neighbors") && k == 0)
		            {
		                String u = "<a class='content' target='_top' href='Summary?id=";
		                res.append(u + list.get(k)+ "'>" + list.get(k) + "</a></td>");
		            }
		            else
		            {
		            	res.append(list.get(k));
		            }
		            res.append("</td>");
		        }
		    }
		}
		else {
		    res.append(" <td class='nodatafound'>No data found for this object </td>");
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
