package org.fastds.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.fastds.explorehelpers.ExplorerQueries;
import org.fastds.explorehelpers.ObjectInfo;
import org.fastds.model.ApogeeVisit;
import org.fastds.model.HelperFunctions;
import org.w3c.dom.Attr;


import edu.gzu.domain.PhotoObjAll;
import edu.gzu.domain.PhotoTag;
import edu.gzu.image.Functions;
import edu.gzu.utils.Utilities;

public class ExplorerDao {

	public Map<String,Long> getObjIDAndSpecObjIDByCoord(double qra, double qdec) {
		double searchRadius = 0.5 / 60;
        String aql = ExplorerQueries.getPmtsFromEq(qra,qdec,searchRadius);
        System.out.println("ExplorerDao.getObjIDAndSpecObjIDByCoord-->aql:"+aql);
    	ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String,Long> result = new HashMap<String,Long>();
		try {
			rs = exQuery.aqlQuery(aql.toString());
			if(!rs.isAfterLast())
			{
				long objID = rs.getLong("objID");
				long specObjID = rs.getLong("specObjID");
				
				result.put("objID",objID);
				result.put("specObjID", specObjID);
//				objectInfo.objId = reader["objId"] is DBNull  null : Functions.BytesToHex((byte[])reader["objId"]);
//	            objectInfo.specObjId = reader["specObjId"] is DBNull  null : Functions.BytesToHex((byte[])reader["specObjId"]);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public long getApid(double qra, double qdec, double searchRadius) {
		String aql = ExplorerQueries.getApogeeFromEq(qra,qdec,searchRadius);
		System.out.println("ExplorerDao.getApid-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		long apid = -1; //
		try {
			rs = exQuery.aqlQuery(aql.toString());
			apid = rs.getLong("apstar_id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return apid;
	}

	public PhotoTag getPhotoTag(long id) {
		String[] aqls = ExplorerQueries.getPmtsFromPhoto(id);
		System.out.println("ExplorerDao.getPhotoTag-->aql:"+Arrays.toString(aqls));
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		PhotoTag photoTag = new PhotoTag(); //
		try {
			/*
			 * 执行对PhotoObjAll的查询，并将结果赋值
			 */
			rs = exQuery.aqlQuery(aqls[0].toString());
			if(!rs.isAfterLast())
			{
				double ra = rs.getDouble("ra");
				double dec = rs.getDouble("dec");
				int run = rs.getShort("run");
				short rerun = rs.getShort("rerun");
				short camcol = rs.getByte("camcol");
				short field = rs.getShort("field");
				long fieldID = rs.getLong("fieldID");
				long objID = rs.getLong("objID");
				photoTag.setRa(ra);
				photoTag.setDec(dec);
				photoTag.setRun(run);
				photoTag.setRerun(rerun);
				photoTag.setCamcol(camcol);
				photoTag.setField(field);
				photoTag.setFieldID(fieldID);
				photoTag.setObjID(objID);
			}
			rs = null;
			/*
			 * 执行对SpecObjAll的查询，并将结果赋值
			 */
			rs = exQuery.aqlQuery(aqls[1].toString());
			if(!rs.isAfterLast())
			{
				long specObjID = rs.getLong("specObjID");
				photoTag.setSpecObjID(specObjID);
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/*
            if (reader.Read())
            {
               objectInfo.ra = (double)reader["ra"];
               objectInfo.dec = (double)reader["dec"];
               objectInfo.run = rs.getShort("run"];
               objectInfo.rerun = rs.getShort("rerun"];
               objectInfo.camcol = (byte)reader["camcol"];
               objectInfo.field = (short)reader["field"];
               objectInfo.fieldId = reader["fieldId"] is DBNull ? null : Functions.BytesToHex((byte[])reader["fieldId"]);
               objectInfo.objId = reader["objId"] is DBNull ? null : Functions.BytesToHex((byte[])reader["objId"]);
               objectInfo.specObjId = reader["specObjId"] is DBNull ? null : Functions.BytesToHex((byte[])reader["specObjId"]);
        }*/

		return photoTag;
	}

	public Map<String, Object> getAttrsFormSpecObjAllAndPlateX(long specID) {
		 String aql = ExplorerQueries.getPlateFiberFromSpecObj(specID);
		 System.out.println("ExplorerDao.getAttrsFormSpecObjAllAndPlateX-->aql:"+aql);
		 ResultSet rs = null;
		 ExQuery exQuery = new ExQuery();
		 Map<String, Object> res = new HashMap<String, Object>();
		 try {
			rs = exQuery.aqlQuery(aql.toString());
			if(!rs.isAfterLast())
			{
				long plateID = rs.getLong("plateID");
				int mjd = rs.getInt("mjd");
				short fiberID = rs.getShort("fiberID");
				short plate = rs.getShort("plate");
				
				res.put("plateID", plateID);
				res.put("mjd", mjd);
				res.put("fiberID", fiberID);
				res.put("plate", plate);
			}
		 } catch (SQLException e) {
			e.printStackTrace();
		 }/* old
             if (reader.Read())
             {
                objectInfo.plateID = reader["plateId"] is DBNull ? null : Functions.BytesToHex((byte[])reader["plateId"]);
                objectInfo.mjd = (int)reader["mjd"];
                objectInfo.fiberID = (short) reader["fiberId"];
                objectInfo.plate = (short)reader["plate"];
             }*/
		return res;
		
	}

	public PhotoTag getPhotoFromEq(double ra, double dec, double r) {
		String aql = ExplorerQueries.getPhotoFromEq(ra, dec, r);
		System.out.println("ExplorerDao.getPhotoFromEq-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		PhotoTag pt = new PhotoTag();
		 try {
			rs = exQuery.aqlQuery(aql.toString());
			if(!rs.isAfterLast())
			{
				long objID = rs.getLong("objID");
				long specObjID = rs.getLong("specObjID");
				pt.setObjID(objID);
				pt.setSpecObjID(specObjID);
			}
		 } catch (SQLException e) {
			e.printStackTrace();
		 }
		 /*	    if (reader.Read())
        {
           objectInfo.objId = reader["objId"] is DBNull  null : Functions.BytesToHex((byte[])reader["objId"]);
          objectInfo.specObjId = reader["specObjId"] is DBNull  null : Functions.BytesToHex((byte[])reader["specObjId"]);
        }
		  */
		return pt;
	}

	public Map<String, Object> getApogeeStar(String whatdoiget, String sid) {
		String aql = ExplorerQueries.getPmtsFromSpecWithApogeeId(whatdoiget, sid);
		System.out.println("ExplorerDao.getApogeeStar-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String,Object> attrs = new HashMap<String,Object>();
		 try {
			rs = exQuery.aqlQuery(aql.toString());
			if(!rs.isAfterLast())
			{
				long apid = rs.getLong("apid");
				double ra = rs.getDouble("ra");
				double dec = rs.getDouble("dec");
				attrs.put("apid", apid);
				attrs.put("ra", ra);
				attrs.put("dec",dec);
			}
		 } catch (SQLException e) {
			e.printStackTrace();
		 }
//		if (reader.Read())
//        {
//           objectInfo.apid = reader.GetString(0);
//           objectInfo.ra = reader.GetDouble(1);
//           objectInfo.dec = reader.GetDouble(2);
//        }
		return attrs;
	}

	public Map<String, Object> getPmtsFromSpecWithSpecobjID(long sid) {
		String aql = ExplorerQueries.getPmtsFromSpecWithSpecobjID(sid);
		System.out.println("ExplorerDao.getPmtsFromSpecWithSpecobjID-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String,Object> attrs = new HashMap<String,Object>();
		 try {
			rs = exQuery.aqlQuery(aql.toString());
			if(!rs.isAfterLast())
			{
				double ra = rs.getDouble("ra");
				double dec = rs.getDouble("dec");
				long fieldID = rs.getLong("fieldID");
				long objID = rs.getLong("objID");
				long specObjID = rs.getLong("specObjID");
				long plateID = rs.getLong("plateID");
				int mjd = rs.getInt("mjd");
				short fiberID = (short)rs.getInt("fiberID");
				short plate = (short)rs.getInt("plate");
				attrs.put("ra", ra);
				attrs.put("dec",dec);
				attrs.put("fieldID", fieldID);
				attrs.put("objID", objID);
				attrs.put("specObjID",specObjID );
				attrs.put("plateID", plateID);
				attrs.put("mjd", mjd);
				attrs.put("fiberID", fiberID);
				attrs.put("plate", plate);
			}
		 } catch (SQLException e) {
			e.printStackTrace();
		 }
//		 if (reader.Read())
//	        {
//	           objectInfo.ra = (double)reader["ra"];
//	           objectInfo.dec = (double)reader["dec"];
//	           objectInfo.fieldId = reader["fieldId"] is DBNull ? null : Functions.BytesToHex((byte[])reader["fieldId"]);
//	           objectInfo.objId = reader["objId"] is DBNull ? null : Functions.BytesToHex((byte[])reader["objId"]);
//	           objectInfo.specObjId = reader["specObjId"] is DBNull ? null : Functions.BytesToHex((byte[])reader["specObjId"]);
//	           objectInfo.plateId = reader["plateId"] is DBNull ? null : Functions.BytesToHex((byte[])reader["plateId"]);
//	           objectInfo.mjd = (int)reader["mjd"];
//	           objectInfo.fiberId = (short)reader["fiberId"];
//	           objectInfo.plate = (short)reader["plate"];
//	        }

		return attrs;
	}

	public Map<String, Object> getImaging(String objID) {
		String aql = ExplorerQueries.getImagingQuery(objID);
		System.out.println("ExplorerDao.getImaging-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String,Object> attrs = new HashMap<String,Object>();
		/*
                if (reader.HasRows)
                {
                    //photoObjall
                    flag = (String) reader["flags"];
                    ra =  (double) reader["ra"];
                    dec = (double) reader["dec"];
                    run = reader["run"] is DBNull ? -9999 : (short)reader["run"];
                    rerun = reader["rerun"] is DBNull ? -9999 : (short)reader["rerun"];
                    camcol = reader["camcol"] is DBNull ? -9999 : (byte)reader["camcol"];
                    field = reader["field"] is DBNull ? -9999 : (short)reader["field"];
                    fieldID = reader["fieldID"] is DBNull ? " " : Functions.BytesToHex((byte[])reader["fieldID"]);
                    objID = reader["objID"] is DBNull ? null : Functions.BytesToHex((byte[])reader["objID"]);
                    clean = reader["clean"] is DBNull ? -99999 : (int)reader["clean"]; ;
                    otype = reader["clean"] is DBNull ? "" :(String)reader["otype"];

                    ////--- magnitudes
                    u = reader["u"] is DBNull ? -999.99 : (float)reader["u"];
                    g = reader["u"] is DBNull ? -999.99 : (float)reader["g"];
                    r = reader["u"] is DBNull ? -999.99 : (float)reader["r"];
                    i = reader["u"] is DBNull ? -999.99 : (float)reader["i"];
                    z = reader["u"] is DBNull ? -999.99 : (float)reader["z"];

                    ////--- mag errors
                    err_u = reader["err_u"] is DBNull ? -999.99 : (float)reader["err_u"];
                    err_g = reader["err_g"] is DBNull ? -999.99 : (float)reader["err_g"];
                    err_r = reader["err_r"] is DBNull ? -999.99 : (float)reader["err_r"];
                    err_i = reader["err_i"] is DBNull ? -999.99 : (float)reader["err_i"];
                    err_z = reader["err_z"] is DBNull ? -999.99 : (float)reader["err_z"];

                    ////--- PhotoObj
                    mode = reader["mode"] is DBNull ? " - " : (String)reader["mode"];

                    mjdNum = reader["mjdNum"] is DBNull ? -99999 :(int) reader["mjdNum"];
                    if(mjdNum != -99999)
                        mjdDate = HelperFunctions.ConvertFromJulian(mjdNum).ToString("MM/dd/yyyy");

                    otherObs = reader["Other observations"] is DBNull ? -99999 : (int)reader["Other observations"];

                    parentID = reader["parentID"] is DBNull ? -99999 : (long)reader["parentID"];

                    nchild = reader["nChild"] is DBNull ? -99999 : (short)reader["nChild"];

                    extinction_r = reader["extinction_r"] is DBNull ? " - " : (String)reader["extinction_r"];

                    petrorad_r = reader["petrorad_r"] is DBNull ? " - " : (String)reader["petrorad_r"];

                    ////--- PhotoZ, photoZRF
                    photoZ_KD = reader["photoZ_KD"] is DBNull ? " - " : (String)reader["photoZ_KD"];

                    //photoZ_RF = reader["photoZ_KD"] is DBNull ? " - " : (String)reader["photoZ_RF"];

                    galaxyZoo_Morph = reader["photoZ_KD"] is DBNull ? " - " : (String)reader["galaxyZoo_Morph"];
                }
		 * */
		 try {
			rs = exQuery.aqlQuery(aql.toString());
			if(!rs.isAfterLast())
			{
				//photoObjall
				 String flag =  Functions.fPhotoFlagsN(rs.getLong("flags"));
                 double ra =  rs.getFloat("ra");
                 double dec = rs.getFloat("dec");
                 short run = rs.getShort("run") == 0 ? -9999 : rs.getShort("run");
                 short rerun = rs.getShort("rerun") == 0 ? -9999 : rs.getShort("rerun");
                 byte camcol = (byte) (rs.getByte("camcol") == 0 ? -9999 : rs.getByte("camcol"));
                 short field = rs.getShort("field") == 0 ? -9999 : rs.getShort("field");
                 String fieldID = rs.getLong("fieldID") == 0 ? " " : Utilities.longToHex(rs.getLong("field"));
                 String objIDFromDatabase = rs.getLong("objID") == 0 ? null : Utilities.longToHex((rs.getLong("objID")));
                 int clean = rs.getInt("clean") == 0 ? -99999 : rs.getInt("clean"); ;
                 String otype = PhotoObjAll.getTypeName(rs.getInt("otype")) == null ? "" :rs.getInt("otype")+"";

                 //--- magnitudes
                 float u = (float) (rs.getFloat("u") == 0 ? -999.99 : rs.getFloat("u"));
                 float g = (float) (rs.getFloat("g") == 0 ? -999.99 : rs.getFloat("g"));
                 float r = (float) (rs.getFloat("r") == 0 ? -999.99 : rs.getFloat("r"));
                 float i = (float) (rs.getFloat("i") == 0 ? -999.99 : rs.getFloat("i"));
                 float z = (float) (rs.getFloat("z") == 0 ? -999.99 : rs.getFloat("z"));
                 
                 ////--- mag errors
                 float err_u = (float) (rs.getFloat("err_ur") == 0 ? -999.99 : rs.getFloat("err_u"));
                 float err_g = (float) (rs.getFloat("err_g") == 0 ? -999.99 : rs.getFloat("err_g"));
                 float err_r = (float) (rs.getFloat("err_r") == 0 ? -999.99 : rs.getFloat("err_r"));
                 float err_i = (float) (rs.getFloat("err_i") == 0 ? -999.99 : rs.getFloat("err_i"));
                 float  err_z = (float) (rs.getFloat("err_z") == 0 ? -999.99 : rs.getFloat("err_z"));

                 ////--- PhotoObj
                 String temp = Functions.fPhotoModeN(rs.getByte("mode"));
                 String mode = temp == null ? " - " : temp;
                 int mjdNum = rs.getInt("mjdNum") == 0 ? -99999 :(int) rs.getInt("mjdNum");
                 String mjdDate = null;
                 /*
                  
                 if(mjdNum != -99999)
                	 mjdDate = new SimpleDateFormat("MM/dd/yyyy").format(HelperFunctions.ConvertFromJulian(mjdNum).getTime()) ;* old
                  */
                 int otherObs = rs.getInt("Other_observations") == 0 ? -99999 : rs.getInt("Other observations");
                 long parentID = rs.getLong("parentID") == 0 ? -99999 : rs.getLong("parentID");
                 short nchild = (short) (rs.getShort("nChild") == 0 ? -99999 : rs.getShort("nChild"));
                 String extinction_r = new DecimalFormat("####.##").format(rs.getFloat("extinction_r")) ;
                 extinction_r = extinction_r == null ? " - " : extinction_r;
                 
                 String petrorad_r = new DecimalFormat("######.##").format(rs.getFloat("petroRad_r"))+" &plusmn; "+ new DecimalFormat("######.###").format(rs.getFloat("petroRadErr_r"));
                 petrorad_r =  petrorad_r == null ? " - " : petrorad_r;

                 ////--- PhotoZ, photoZRF
                 String photoZ_KD = new DecimalFormat("###.###").format(rs.getFloat("z"))+" &plusmn; "+ new DecimalFormat("###.####").format(rs.getFloat("zErr"));
                 photoZ_KD = photoZ_KD == null ? " - " : photoZ_KD;

                 //photoZ_RF = reader["photoZ_KD") == 0 ? " - " : (String)reader["photoZ_RF"];
                 int galaxyZoo_Morph_Int = rs.getInt("GalaxyZoo_Morph") ;
                 String galaxyZoo_Morph = null;
                 if(galaxyZoo_Morph_Int == 1)
                	 galaxyZoo_Morph = "Spiral";
                 else if(galaxyZoo_Morph_Int ==10)
                	 galaxyZoo_Morph = "Elliptical";
                 else if(galaxyZoo_Morph_Int ==100)
                	 galaxyZoo_Morph = "Uncertain";
                 else 
                	 galaxyZoo_Morph = " - ";
                 galaxyZoo_Morph = galaxyZoo_Morph == null ? " - " : galaxyZoo_Morph;
                 
                 
                 attrs.put("flag", flag);
                 attrs.put("ra", ra);
                 attrs.put("dec", dec);
                 attrs.put("run", run);
                 attrs.put("rerun", rerun);
                 attrs.put("camcol", camcol);
                 attrs.put("field", field);
                 attrs.put("fieldID", fieldID);
                 attrs.put("objID", objIDFromDatabase);
                 attrs.put("clean", clean);
                 attrs.put("otype", otype);   //?????没有这个属性？
                 
                 attrs.put("u", u);
                 attrs.put("g", g);
                 attrs.put("r", r);
                 attrs.put("i", i);
                 attrs.put("z", z);
                 
                 attrs.put("err_u", err_u);
                 attrs.put("err_g", err_g);
                 attrs.put("err_r", err_r);
                 attrs.put("err_i", err_i);
                 attrs.put("err_z", err_z);
                 
                 attrs.put("mode", mode);
                 attrs.put("mjdNum", mjdNum);
                 attrs.put("mjdDate", mjdDate);
                 attrs.put("Other_observations", otherObs);
                 attrs.put("parentID", parentID);
                 attrs.put("nChild", nchild);
                 attrs.put("extinction_r", extinction_r);
                 attrs.put("petrorad_r", petrorad_r);
                 attrs.put("photoZ_KD", photoZ_KD);
                 attrs.put("galaxyZoo_Morph", galaxyZoo_Morph);
			}
		 } catch (SQLException e) {
			e.printStackTrace();
		 }
		return attrs;
	}

	public Map<String, String> getAttrsFromDBColumns() {
		String aql = ExplorerQueries.unitQuery();
		System.out.println("ExplorerDao.getAttrsFromDBColumns-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String,String> attrs = new HashMap<String,String>();
		try {
			rs = exQuery.aqlQuery(aql);
			while (!rs.isAfterLast())
			{
	           String colName = rs.getString("name") == null ? "":rs.getString("name");
	           String colUnit = rs.getString("unit") == null ? "" : rs.getString("unit");
	           
	           attrs.put("name", colName);
	           attrs.put("unit", colUnit);
	          
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return attrs;
	}

	public String getUnitFromDBColumns(String tablename,
			String columname) {
		String aql = ExplorerQueries.getUnit(tablename,columname);
		System.out.println("ExplorerDao.getUnitFromDBColumns-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		String unit = "";
		try {
			rs = exQuery.aqlQuery(aql);
			if (!rs.isAfterLast())
			{
	           unit = rs.getString("unit") ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return unit;
	}

	/*
	 * 未完成
	 */
	public ApogeeVisit getApogeevisitsByID(String id) {
		String aql = ExplorerQueries.apogeevisitsBaseQuery(id);
		System.out.println("ExplorerDao.getApogeevisitsByID-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		ApogeeVisit obj = null;
		/*
		 * while (reader.Read()) // Multiple rows expected
            {
                ApogeeVisit v = new ApogeeVisit();
                v.visit_id = (String)reader["visit_id"];
                v.plate = (String)reader["plate"];
                v.mjd = (long)reader["mjd"];
                v.fiberid = (long)reader["fiberid"];
                v.dateobs = (String)reader["dateobs"];
                v.vrel = (float)reader["vrel"];
                visits.Add(v);
            }
		 */
		try {
			rs = exQuery.aqlQuery(aql);
			while (!rs.isAfterLast())
			{
				obj.setVisit_id(rs.getString("visit_id"));  //??????
				obj.setPlate(rs.getString("plate"));
				obj.setMjd(rs.getLong("mjd"));
				obj.setFiberid(rs.getLong("fiberid"));
				obj.setDateobs(rs.getString("dateobs"));
				obj.setVrel(rs.getFloat("vrel"));
				rs.next();
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public Map<String, Object> getApogeeByAql(String command) {
		System.out.println("ExplorerDao.getApogeeByAql-->aql:"+command);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String,Object> attrs = new HashMap<String,Object>();
		/*
		 * //BASE_QUERY + FIND_NEAREST;
                ra = rs.getFloat("ra") == 0 ?-99.99:(double)rs.getFloat("ra");
                dec = rs.getFloat("dec") == 0 ? -99.99 : (double)rs.getFloat("dec");
                apstar_id = reader["apstar_id"] is DBNull ? "-" : (string)reader["apstar_id"];
                apogee_id = reader["apogee_id"] is DBNull ? "-" : (string)reader["apogee_id"];
                glon = reader["glon"] is DBNull ? -99.99 : (double)reader["glon"];
                glat = reader["glat"] is DBNull ? -99.99 : (double)reader["glat"];
                location_id = reader["location_id"] is DBNull ? -9999 : (long)reader["location_id"];
                commiss = reader["commiss"] is DBNull ? -9999 : (long)reader["commiss"];
                vhelio_avg = reader["vhelio_avg"] is DBNull ? -9 : (float)reader["vhelio_avg"];
                vscatter = reader["vscatter"] is DBNull ? -9 : (float)reader["vscatter"];
                teff = reader["teff"] is DBNull ? -9 : (float)reader["teff"];
                teff_err = reader["teff_err"] is DBNull ? -9 : (float)reader["teff_err"];
                logg = reader["logg"] is DBNull ? -9 : (float)reader["logg"];
                logg_err = reader["logg_err"] is DBNull ? -9 : (float)reader["logg_err"];
                param_m_h = reader["param_m_h"] is DBNull ? -9 : (float)reader["param_m_h"];
                param_m_h_err = reader["param_m_h_err"] is DBNull ? -9 : (float)reader["param_m_h_err"];
                param_alpha_m = reader["param_alpha_m"] is DBNull ? -9 : (float)reader["param_alpha_m"];
                param_alpha_m_err = reader["param_alpha_m_err"] is DBNull ? -9 : (float)reader["param_alpha_m_err"];

                j = reader["j"] is DBNull ? -9 : (float)reader["j"];
                h = reader["h"] is DBNull ? -9 : (float)reader["h"];
                k = reader["k"] is DBNull ? -9 : (float)reader["k"];
                j_err = reader["j_err"] is DBNull ? -9 : (float)reader["j_err"];
                h_err = reader["h_err"] is DBNull ? -9 : (float)reader["h_err"];
                k_err = reader["k_err"] is DBNull ? -9 : (float)reader["k_err"];
                mag_4_5 = reader["mag_4_5"] is DBNull ? null : (float?)reader["mag_4_5"];
                mag_4_5_err = reader["mag_4_5_err"] is DBNull ? null : (float?)reader["mag_4_5_err"];
                src_4_5 = reader["src_4_5"] is DBNull ? "-" : (string)reader["src_4_5"];

                apogeeTarget1N = reader["apogeeTarget1N"] is DBNull ? "-" : (string)reader["apogeeTarget1N"];
                apogeeTarget2N = reader["apogeeTarget2N"] is DBNull ? "-" : (string)reader["apogeeTarget2N"];
                apogeeStarFlagN = reader["apogeeStarFlagN"] is DBNull ? "-" : (string)reader["apogeeStarFlagN"];
                apogeeAspcapFlagN = reader["apogeeAspcapFlagN"] is DBNull ? "-" : (string)reader["apogeeAspcapFlagN"];

                isData = true;

		 * */
		try {
			rs = exQuery.aqlQuery(command);
			if (!rs.isAfterLast())
			{
				//BASE_QUERY + FIND_NEAREST;
                double ra = rs.getFloat("ra") == 0 ?-99.99:(double)rs.getFloat("ra");
                double dec = rs.getFloat("dec") == 0 ? -99.99 : (double)rs.getFloat("dec");
                String apstar_id = rs.getString("apstar_id") == null ? "-" : (String)rs.getString("apstar_id");
                String apogee_id = rs.getString("apogee_id") == null ? "-" : (String)rs.getString("apogee_id");
                double glon = rs.getDouble("glon") == 0 ? -99.99 : (double)rs.getDouble("glon");
                double glat = rs.getDouble("glat") == 0 ? -99.99 : (double)rs.getDouble("glat");
                long location_id = rs.getLong("location_id") == 0 ? -9999 : (long)rs.getLong("location_id");
                long commiss = rs.getLong("commiss") == 0 ? -9999 : (long)rs.getLong("commiss");
                float vhelio_avg = rs.getFloat("vhelio_avg") == 0 ? -9 : (float)rs.getFloat("vhelio_avg");
                float vscatter = rs.getFloat("vscatter") == 0 ? -9 : (float)rs.getFloat("vscatter");
                float teff = rs.getFloat("teff") == 0 ? -9 : (float)rs.getFloat("teff");
                float teff_err = rs.getFloat("teff_err") == 0 ? -9 : (float)rs.getFloat("teff_err");
                float logg = rs.getFloat("logg") == 0 ? -9 : (float)rs.getFloat("logg");
                float logg_err = rs.getFloat("logg_err") == 0 ? -9 : (float)rs.getFloat("logg_err");
                float param_m_h = rs.getFloat("param_m_h") == 0 ? -9 : (float)rs.getFloat("param_m_h");
                float param_m_h_err = rs.getFloat("param_m_h_err") == 0 ? -9 : (float)rs.getFloat("param_m_h_err");
                float param_alpha_m = rs.getFloat("param_alpha_m") == 0 ? -9 : (float)rs.getFloat("param_alpha_m");
                float param_alpha_m_err = rs.getFloat("param_alpha_m_err") == 0 ? -9 : (float)rs.getFloat("param_alpha_m_err");

                float j = rs.getFloat("j") == 0 ? -9 : (float)rs.getFloat("j");
                float h = rs.getFloat("h") == 0 ? -9 : (float)rs.getFloat("h");
                float k = rs.getFloat("k") == 0 ? -9 : (float)rs.getFloat("k");
                float j_err = rs.getFloat("j_err") == 0 ? -9 : (float)rs.getFloat("j_err");
                float h_err = rs.getFloat("h_err") == 0 ? -9 : (float)rs.getFloat("h_err");
                float k_err = rs.getFloat("k_err") == 0 ? -9 : (float)rs.getFloat("k_err");
                float mag_4_5 = rs.getFloat("mag_4_5") == 0 ? null : (float/*?*/)rs.getFloat("mag_4_5");
                float mag_4_5_err = rs.getFloat("mag_4_5_err") == 0 ? null : (float/*?*/)rs.getFloat("mag_4_5_err");
                String src_4_5 = rs.getString("src_4_5") == null ? "-" : (String)rs.getString("src_4_5");

                String apogeeTarget1N = rs.getString("apogeeTarget1N") == null ? "-" : (String)rs.getString("apogeeTarget1N");
                String apogeeTarget2N = rs.getString("apogeeTarget2N") == null ? "-" : (String)rs.getString("apogeeTarget2N");
                String apogeeStarFlagN = rs.getString("apogeeStarFlagN") == null ? "-" : (String)rs.getString("apogeeStarFlagN");
                String apogeeAspcapFlagN = rs.getString("apogeeAspcapFlagN") == null ? "-" : (String)rs.getString("apogeeAspcapFlagN");
                
                
                attrs.put("ra", ra);
                attrs.put("dec", dec);
                attrs.put("apstar_id", apstar_id);
                attrs.put("apogee_id", apogee_id);
                attrs.put("glon", glon);
                attrs.put("glat", glat);
                attrs.put("location_id",location_id );
                attrs.put("commiss", commiss);
                attrs.put("vhelio_avg", vhelio_avg);
                attrs.put("vscatter",vscatter);
                attrs.put("teff",teff);
                attrs.put("teff_err", teff_err);
                attrs.put("param_m_h", param_m_h);
                attrs.put("param_m_h_err",param_m_h_err );
                attrs.put("param_alpha_m",param_alpha_m );
                attrs.put("param_alpha_m_err", param_alpha_m_err);
                attrs.put("j", j);
                attrs.put("h", h);
                attrs.put("k", k);
                attrs.put("j_err",j_err );
                attrs.put("h_err", h_err);
                attrs.put("k_err", k_err);
                attrs.put("mag_4_5", mag_4_5);
                attrs.put("mag_4_5_err", mag_4_5_err);
                attrs.put("src_4_5",src_4_5 );
                attrs.put("apogeeTarget1N", apogeeTarget1N);
                attrs.put("apogeeTarget2N",apogeeTarget2N );
                attrs.put("apogeeStarFlagN",apogeeStarFlagN );
                attrs.put("apogeeAspcapFlagN", apogeeAspcapFlagN);
                
	          
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return attrs;
	}

	public Map<String, Object> getAttrsFromUSNOByID(String objID) {
		String aql = ExplorerQueries.USNO(objID);
		System.out.println("ExplorerDao.getAttrsFromUSNOByID-->aql:"+aql);
		 /*if (reader.Read())
        {
            if (reader.HasRows)
            {
               isUSNO = true;
               usno = reader.GetString(0);
               properMotion = reader.GetString(1);
               angle = reader.GetFloat(2);
            }
        }  old  */
		
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String, Object> attrs = new HashMap<String, Object>();
		try {
			rs = exQuery.aqlQuery(aql);
			if (!rs.isAfterLast())
			{
				String usno = rs.getString("Catalog");
				String properMotion = new DecimalFormat("###.##").format(rs.getFloat(2))+" &plusmn; "+ new DecimalFormat("####.###").format(rs.getFloat(3));
				float angle = rs.getFloat("PM_angle_deg_E");
				
				attrs.put("usno", usno);
				attrs.put("properMotion", properMotion);
				attrs.put("angle", angle);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attrs;
	}

	public Map<String, Object> findAttrsFromFirst(String objID) {
		String aql = ExplorerQueries.FIRST(objID);
		System.out.println("ExplorerDao.findAttrsFromFirst-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String, Object> attrs = new HashMap<String, Object>();
		/*if (reader.Read())
        {
            if (reader.HasRows)
            {
               isFIRST = true;
               first = reader.GetString(0);
               peakflux = reader.GetString(1);
               major = reader.GetFloat(2);
               minor = reader.GetFloat(3);
            }
        }   old */
		try {
			rs = exQuery.aqlQuery(aql);
			if (!rs.isAfterLast())
			{
				String first = rs.getString("Catalog");
				String peakflux = new DecimalFormat("#####.##").format(rs.getDouble(2))+" &plusmn; "+new DecimalFormat("#####.##").format(rs.getDouble(3));
				double major = rs.getDouble("Major_axis_arcsec");
				double minor = rs.getDouble("Minor_axis_arcsec");///float?double?
				
				attrs.put("first", first);
				attrs.put("peakflux", peakflux);
				attrs.put("major", major);
				attrs.put("minor", minor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attrs;
	}

	public Map<String, Object> getAttrsFromRosat(String objID) {
		String aql = ExplorerQueries.ROSAT(objID);
		System.out.println("ExplorerDao.getAttrsFromRosat-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String, Object> attrs = new HashMap<String, Object>();
		/*if (reader.Read())
        {
            if (reader.HasRows)
            {
                isROSAT = true;
                rosat = reader.GetString(0);
                cps = reader.GetFloat(1);
                hr1 = reader.GetFloat(2);
                hr2 = reader.GetFloat(3);
                ext = reader.GetFloat(4);
            }
        }
  old  */
		try {
			rs = exQuery.aqlQuery(aql);
			if (!rs.isAfterLast())
			{
				String rosat = rs.getString("Catalog");
				float cps = rs.getFloat("cps");
				float hr1 = rs.getFloat("hr1");
				float hr2 = rs.getFloat("hr2");
				float ext = rs.getFloat("ext");
				
				attrs.put("rosat", rosat);
				attrs.put("cps", cps);
				attrs.put("hr1", hr1);
				attrs.put("hr2", hr2);
				attrs.put("ext", ext);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attrs;
	}

	public Map<String, Object> getAttrsFromRC3(String objID) {
		String aql = ExplorerQueries.RC3(objID);
		System.out.println("ExplorerDao.getAttrsFromRC3-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String, Object> attrs = new HashMap<String, Object>();
		/*if (reader.Read())
        {
            if (reader.HasRows)
            {
                isRC3 = true;
                rc3 = reader.GetString(0);
                hubletype = reader.GetString(1);
                magnitude = reader.GetString(2);
                hydrogenIndex = reader.GetFloat(3);
            }
        }*/
		try {
			rs = exQuery.aqlQuery(aql);
			if (!rs.isAfterLast())
			{
				String rc3 = rs.getString(1);
				String hubletype = rs.getString(2);
				String magnitude = new DecimalFormat("##.##").format(rs.getFloat(3))+" &plusmn; "+new DecimalFormat("##.###").format(rs.getFloat(4));
				Float hydrogenIndex = rs.getFloat(5);
				
				attrs.put("rc3", rc3);
				attrs.put("hubletype", hubletype);
				attrs.put("magnitude", magnitude);
				attrs.put("hydrogenIndex", hydrogenIndex);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attrs;
	}

	public Map<String, Object> getAttrsFromWISE(String objID) {
		String aql = ExplorerQueries.WISE(objID);
		System.out.println("ExplorerDao.getAttrsFromWISE-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String, Object> attrs = new HashMap<String, Object>();
		/*if (reader.Read())
        {
            if (reader.HasRows)
            {
                isWISE = true;
                wise = reader.GetString(0);
                wmag1 = reader.GetFloat(1);
                wmag2 = reader.GetFloat(2);
                wmag3 = reader.GetFloat(3);
                wmag4 = reader.GetFloat(4);
                wiselink = reader.GetString(5);
            }
        } old */
		try {
			rs = exQuery.aqlQuery(aql);
			if (!rs.isAfterLast())
			{
				String wise = rs.getString(1);
				float wmag1 = rs.getFloat(2);
				float wmag2 = rs.getFloat(3);
				float wmag3 = rs.getFloat(4);
				float wmag4 = rs.getFloat(5);
				String wiselink = rs.getString(6);
				
				attrs.put("wise", wise);
				attrs.put("wmag1", wmag1);
				attrs.put("wmag2", wmag2);
				attrs.put("wmag3", wmag3);
				attrs.put("wmag4", wmag4);
				attrs.put("wiselink", wiselink);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attrs;
	}

	public Map<String, Object> getAttrsFromTWOMASS(String objID) {
		String aql = ExplorerQueries.WISE(objID);
		System.out.println("ExplorerDao.getAttrsFromTWOMASS-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String, Object> attrs = new HashMap<String, Object>();
		/*if (reader.Read())
        {
            if (reader.HasRows)
            {
                is2MASS = true;
                twomass = reader.GetString(0);
                j = reader.GetFloat(1);
                h = reader.GetFloat(2);
                k = reader.GetFloat(3);
                phQual = reader.GetString(4);
            }
        }old */
		try {
			rs = exQuery.aqlQuery(aql);
			if (!rs.isAfterLast())
			{
				String twomass = rs.getString(0);
				float j = rs.getFloat(1);
				float h = rs.getFloat(2);
				float k = rs.getFloat(3);
				String phQual = rs.getString(4);
				
				attrs.put("twomass", twomass);
				attrs.put("j", j);
				attrs.put("h", h);
				attrs.put("k", k);
				attrs.put("phQual", phQual);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attrs;
	}

	public Map<String, Object> getParamsFromTables(String objID) {
		String aql = ExplorerQueries.getObjParamaters(objID);
		System.out.println("ExplorerDao.getParamsFromTables-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String, Object> attrs = new HashMap<String, Object>();
		
		/*if (reader.Read())
        {
            if (reader.HasRows)
            {
                ra = (double)reader["ra"];
                dec = (double)reader["dec"];
                specObjId = reader["specObjId"] is DBNull ? -999999 : (long)(reader["specObjId"]);                       
                clean = (int)reader["clean"];
                survey = reader["survey"] is DBNull ? null:(String)reader["survey"];
                mode = (int)reader["mode"];
                otype = reader["otype"] is DBNull ? null : (String)reader["otype"];
                imageMJD = (int)reader["mjd"];
            }
        }*/
		try {
			rs = exQuery.aqlQuery(aql);
			if (!rs.isAfterLast())
			{
				double ra = rs.getFloat("ra");
				double dec = rs.getFloat("dec");
				long specObjID = rs.getLong("specObjID");
				int clean = rs.getInt("clean");
				String survey = rs.getString("survey");
				int mode = rs.getInt("mode");
				String photypeQual = PhotoObjAll.getTypeName(rs.getInt("otype"));
				int imageMJD = rs.getInt("mjd");
				
				attrs.put("ra", ra);
				attrs.put("dec", dec);
				attrs.put("specObjID", specObjID);
				attrs.put("clean", clean);
				attrs.put("survey", survey);
				attrs.put("mode", mode);
				attrs.put("otype", photypeQual);
				attrs.put("mjd", imageMJD);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attrs;
	}

	public Map<String, Object> getAttrsFromSpecObjAllAndPlateX(String objID,
			String specID) {
		String aql = ExplorerQueries.getSpectroQuery(objID,specID);
		System.out.println("ExplorerDao.getAttrsFromSpecObjAllAndPlateX-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String, Object> attrs = new HashMap<String, Object>();
		
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
		try {
			rs = exQuery.aqlQuery(aql);
			if (!rs.isAfterLast())
			{
				short plate = rs.getShort("plate");
				int mjd = rs.getInt("mjd");
				short fiberid = rs.getShort("fiberid");
				String instrument = rs.getString("instrument");
				String objclass = rs.getString("objclass");
				float redshift_z = rs.getInt("redshift_z");
				float redshift_err = rs.getInt("redshift_err");
				String redshift_flags = rs.getString("redshift_flags");
				String survey = rs.getString("survey");
				long legacy_target1 = rs.getLong("legacy_target1");
				long legacy_target2 = rs.getLong("legacy_target2");
				long special_target1 = rs.getLong("special_target1");
				long boss_target1 = rs.getLong("boss_target1");
				long ancillary_target1 = rs.getLong("ancillary_target1");
				long ancillary_target2 = rs.getLong("ancillary_target2");
				long segue1_target1 = rs.getLong("segue1_target1");
				long segue1_target2 = rs.getLong("segue1_target2");
				long segue2_target1 = rs.getLong("segue2_target1");
				long segue2_target2 = rs.getLong("segue2_target2");
				String programname = rs.getString("programname");
				short primary = rs.getShort("primary");
				int otherspec = rs.getInt("otherspec");
				String sourcetype = rs.getString("sourcetype");
				float veldisp = rs.getInt("veldisp");
				float veldisp_err = rs.getInt("veldisp_err");
				String targeting_flags = rs.getString("targeting_flags");
				//--------------------------------
				if("sdss".equals(survey))
					survey = Functions.fPrimTargetN((int)legacy_target1)+" "+Functions.fPrimTargetN((int)legacy_target2)+ " "+Functions.fSpecialTarget1N(special_target1);
				else if("boss".equals(survey))
					survey = boss_target1+","+ancillary_target1+","+ancillary_target2;
				else if("segue1".equals(survey))
					survey = Functions.fSegue1Target1N((int)segue1_target1)+","+Functions.fSegue1Target2N((int)segue1_target2);
				else if("segue2".equals(survey))
					survey = Functions.fSegue2Target1N((int)segue2_target1)+","+Functions.fSegue2Target2N((int)segue2_target2);
				else
					survey = " No Data ";
				//--------------------------------
				attrs.put("plate", plate);
				attrs.put("mjd", mjd);
				attrs.put("fiberid", fiberid);
				attrs.put("instrument", instrument);
				attrs.put("objclass", objclass);
				attrs.put("redshift_z", redshift_z);
				attrs.put("redshift_err", redshift_err);
				attrs.put("redshift_flags", redshift_flags);
				attrs.put("survey", survey);
				attrs.put("programname", programname);
				attrs.put("primary", primary);
				attrs.put("otherspec", otherspec);
				attrs.put("sourcetype", sourcetype);
				attrs.put("veldisp", veldisp);
				attrs.put("veldisp_err", veldisp_err);
				attrs.put("targeting_flags", targeting_flags);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attrs;
	}

	public Map<String, Object> getApogeeByID(String apid) {
		String aql = ExplorerQueries.getApogee(apid);
		System.out.println("ExplorerDao.getApogeeByID-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String, Object> attrs = new HashMap<String, Object>();
		
		try {
			rs = exQuery.aqlQuery(aql);
			if (!rs.isAfterLast())
			{
				double ra = rs.getFloat("ra");
				double dec = rs.getFloat("dec");
				
				attrs.put("ra", ra);
				attrs.put("dec", dec);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attrs;
	}
	public Map<String, Object> getApogeeByID2(String apid) {
		String aql = ExplorerQueries.getApogee2(apid);
		System.out.println("ExplorerDao.getApogeeByID-->aql:"+aql);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String, Object> attrs = new HashMap<String, Object>();
		
		try {
			rs = exQuery.aqlQuery(aql);
			if (!rs.isAfterLast())
			{
				double ra = rs.getFloat("ra");
				double dec = rs.getFloat("dec");
				
				attrs.put("ra", ra);
				attrs.put("dec", dec);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attrs;
	}

	public Map<String, Object> getObjIDFromPlatefiberMjd(String mjd,
			String plate, String fiber) {
		String aql = ExplorerQueries.getObjIDFromPlatefiberMjd(mjd+"",plate+"",fiber+"");
		System.out.println("ExplorerDao.getObjIDFromPlatefiberMjd-->aql:"+aql);
		 /*if (reader.Read())
         {
            objectInfo.objId = reader["objId"] is DBNull  null : Functions.BytesToHex((byte[])reader["objId"]);
            objectInfo.specObjId = reader["specObjId"] is DBNull  null : Functions.BytesToHex((byte[])reader["specObjId"]);
            objectInfo.ra = (double)reader["ra"];
            objectInfo.dec = (double)reader["dec"];
         }*/
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		Map<String, Object> attrs = new HashMap<String, Object>();
		/* if (reader.Read())
        {
           objectInfo.objId = reader["objId"] is DBNull  null : Functions.BytesToHex((byte[])reader["objId"]);
           objectInfo.specObjId = reader["specObjId"] is DBNull  null : Functions.BytesToHex((byte[])reader["specObjId"]);
           objectInfo.ra = (double)reader["ra"];
           objectInfo.dec = (double)reader["dec"];
        } old */
		try {
			rs = exQuery.aqlQuery(aql);
			if (!rs.isAfterLast())
			{
				long objID = rs.getLong("objID");
				long specObjID = rs.getLong("specObjID");
				double ra = rs.getFloat("objID");
				double dec = rs.getFloat("dec");
				
				attrs.put("objID", objID);
				attrs.put("specObjID", specObjID);
				attrs.put("ra", ra);
				attrs.put("dec", dec);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return attrs;
	}

	public ResultSet getAllSpec1RS(String objID) {
		String aql = ExplorerQueries.AllSpec1(objID);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		try {
			rs = exQuery.aqlQuery(aql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet getAllSpec2RS(String objID) {
		String aql = ExplorerQueries.AllSpec2(objID);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		try {
			rs = exQuery.aqlQuery(aql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet getResultSetFromCmd(String cmd) {
		System.out.println("ExplorerDao.getResultSetFromCmd-->aql:"+cmd);
		ResultSet rs = null;
		ExQuery exQuery = new ExQuery();
		try {
			rs = exQuery.aqlQuery(cmd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

}
