package org.fastds.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.fastds.dbutil.DBConnection;
import org.junit.Test;
import org.scidb.jdbc.IStatementWrapper;

import edu.gzu.domain.PhotoObjAll;
import edu.gzu.domain.PrimaryObject;
import edu.gzu.image.Functions;
import edu.gzu.image.SdssConstants;
/*
 * 包含了对表 SpecObjAll PhotoObjAll表的访问
 */
public class ImageDao {
	@Test
	public void getImg(/*Image image*/) 
	{
		Connection conn = null;
		ResultSet res = null;
//		float ra = image.getRa();
//		float dec = image.getDec();
		String sql = "select ra,dec from SpecObjAll";
		try {
			
			conn = DBConnection.getConnection();
			Statement st = conn.createStatement();
			IStatementWrapper staWrapper = st.unwrap(IStatementWrapper.class);
			staWrapper.setAfl(false);
			res = st.executeQuery(sql);	
			int i = 1;
			while(!res.isAfterLast())
			{
				i++;
				float ra = res.getFloat("ra");
				float dec = res.getFloat("dec");
				res.next();
				System.out.println("ra:"+ra+",dec:"+dec+"....."+i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * @param id 数组SpecObj的ID
	 * @return img
	 */
	public String getSpecObjById(long id) {
		ResultSet rs = null;
		String specObjImg = null;
		try {
			String aql = "SELECT img FROM SpecObjAll WHERE specObjID="+id;
			ExQuery exQuery = new ExQuery();
			rs = exQuery.aqlQuery(aql);
			if(rs!=null && (!rs.isAfterLast()))
			{
				return rs.getString("img");
			}
		} catch (Exception e) {
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e1) {
					e.printStackTrace();
				}
			e.printStackTrace();
		}
		
		return specObjImg;
	}
	/**
	 * 
	 * @param objID get specObjID by objID in PhotoObjAll
	 * @return specObjID in array SpecObjAll
	 */
	/**
	 * 
	 * @param ra 赤经
	 * @param dec 赤纬
	 * @param radius 半径
	 * @return PrimaryObject 对象
	 */
	public PrimaryObject getPrimaryObject(double ra, double dec, double radius) {
		
		// build the SQL query string to search for the nearest Primary Object

        String cmd = "SELECT TOP 1 P.objID AS 'objId', ";
        cmd += "  LTRIM(STR(P.ra,10,5))as 'ra', LTRIM(STR(P.dec,8,5)) as 'dec', ";
        cmd += "  dbo.fPhotoTypeN(P.type) as 'type', LTRIM(STR(P.u,6,2)) AS 'u', LTRIM(STR(P.g,6,2)) AS 'g', ";
        cmd += "  LTRIM(STR(P.r,6,2)) AS 'r', LTRIM(STR(P.i,6,2)) AS 'i', LTRIM(STR(P.z,6,2)) AS 'z'";
        cmd += "  FROM dbo.fGetNearestObjEq ("+ra+","+dec+","+radius+") as N, ";
        cmd += "  PhotoObjAll as P";
        cmd += "  WHERE N.objID = P.objID AND P.i>0 ";
        
        ResultSet rs = null;
        PrimaryObject po = new PrimaryObject();
        try {
			ExQuery exQuery = new ExQuery();
			rs = exQuery.aqlQuery(cmd);
			long pObjID = rs.getLong(1);	//objID
			double pRa = rs.getDouble(2);	//ra
			double pdec = rs.getDouble(3);	//dec
			String pType = rs.getString(4);		//type
			double u = rs.getDouble(5);	//u
			double g = rs.getDouble(6);	//g
			double r = rs.getDouble(7);	//r
			double i = rs.getDouble(8);	//i
			double z = rs.getDouble(9);	//z
			po.setDec(pdec);
			po.setG(g);
			po.setI(i);
			po.setObjID(pObjID);
			po.setR(r);
			po.setRa(pRa);
			po.setRadius(radius);
			po.setType(pType);
			po.setU(u);
			po.setZ(z);
			
		} catch (Exception e) {
			try {
				if(rs!=null)rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
        
		return po;
	}
	/**
	 * 
	 * @param photoObjAllID 数组PhotoObjAll的id
	 * @return 数组SpecObjAll的id，如果结果集为空，返回0
	 */
	public long getSpecObjID(long photoObjAllID)
	{
		  StringBuilder aql = new StringBuilder();
	        aql.append(" SELECT s.specObjID ");
	        aql.append(" FROM (SELECT * FROM PhotoObjAll WHERE objID="+photoObjAllID+") AS p JOIN SpecObj AS s ");
	        aql.append(" ON s.bestObjID=p.objID ");
	        System.out.println("ImageDao.getSpecObjID-->"+aql);
	        ResultSet rs = null;
	        long specObjID = -1;
	        try {
				ExQuery exQuery = new ExQuery();
				rs = exQuery.aqlQuery(aql.toString());
				if(rs!=null&&(!rs.isAfterLast()))
					specObjID = rs.getBigDecimal("specObjID").longValue();
			} catch (Exception e) {
				try {
					if(rs!=null)rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
	        
		return specObjID;
	}
	/**
	 * 
	 * @param ra 赤经
	 * @param dec 赤纬
	 * @param radius 半径
	 * @return 返回PhotoObjAll的对象，结果集为空则返回null
	 */
	public PhotoObjAll getPhotoObjAll(double ra,double dec,double radius)
	{
		String aql = Functions.fGetNearestObjEq(ra, dec, radius);
		System.out.println("fGetNearestObjEq--->:"+aql);
        ResultSet rs = null;
        PhotoObjAll photoObjAll = null;
        try {
			ExQuery exQuery = new ExQuery();
			rs = exQuery.aqlQuery(aql);
			DecimalFormat df = new DecimalFormat("#.#####");
			if(rs!=null && (!rs.isAfterLast()))
			{
				photoObjAll = new PhotoObjAll();
				photoObjAll.setId(rs.getLong("objID"));
				photoObjAll.setRa(Double.parseDouble(df.format(rs.getDouble("ra"))));
				photoObjAll.setDec(Double.parseDouble(df.format(rs.getDouble("dec"))));
				photoObjAll.setType(rs.getInt("type"));
				df = new DecimalFormat("#.##");
				photoObjAll.setU(Double.parseDouble(df.format(rs.getDouble("u"))));
				photoObjAll.setG(Double.parseDouble(df.format(rs.getDouble("g"))));
				photoObjAll.setR(Double.parseDouble(df.format(rs.getDouble("r"))));
				photoObjAll.setI(Double.parseDouble(df.format(rs.getDouble("i"))));
				photoObjAll.setZ(Double.parseDouble(df.format(rs.getDouble("z"))));
				return photoObjAll;
			}
		} catch (Exception e) {
			try {
				if(rs!=null)rs.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	        
		return photoObjAll;
	}
	@Test
	public void testGetPhotoObjAll()
	{
		double ra,dec,radius;
		ra=159.815;
		dec=-0.655;
		radius=getRadius(ra, dec, 120, 120, 0.396127).get("radius");
		String aql = Functions.fGetNearestObjEq(ra, dec, radius);
		System.out.println("fGetNearestObjEq--->:"+aql);
		System.out.println(getSpecObjID(1237666660221059165l));
	}
	private Map<String, Double> getRadius(double ra_, double dec_, int width,
			int height, double scale) {
		Map<String, Double> map = new HashMap<String, Double>();
		// Normalize ra and dec
		double ra = ra_;
		double dec = dec_;
		dec = dec % 180; // bring dec within the circle
		if (Math.abs(dec) > 90) // if it is "over the pole",
		{
			dec = (dec - 90) % 180; // bring int back to the [-90..90] range
			ra += 180; // and go 1/2 way round the globe
		}
		ra = ra % 360; // bring ra into [0..360]
		if (ra < 0)
			ra += 360;

		double ppd = 3600.0 / scale;
		double radius = 60.0 * 0.5 * Math.sqrt(width * width + height * height)
				/ ppd;
		double fradius = SdssConstants.FrameHalfDiag + radius;// 8.4+
		map.put("ra", ra);
		map.put("dec", dec);
		map.put("radius", fradius);
		return map;
	}

}
