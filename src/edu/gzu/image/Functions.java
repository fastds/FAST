package edu.gzu.image;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.fastds.dao.ExQuery;
import org.fastds.model.View;
import org.junit.Test;

import edu.gzu.domain.Obj;
import edu.gzu.domain.PhotoObjAll;
import edu.gzu.image.sphericalhtm.Cover;
import edu.gzu.image.sphericalhtm.Pair;
import edu.gzu.image.sphericalhtm.Trixel;
import edu.gzu.image.sphericallib.Cartesian;
import edu.gzu.image.sphericallib.Region;
import edu.gzu.utils.Utilities;

public class Functions {
	/*
	 * 求弧度
	 */
	public static double radians(double x)
	{
		return x*(Math.PI/180);
	}
	/**
	 * 由long类型的objID计算六段的SDSS编号
	 * 每一个段的意义和长度为skyversion[5] + rerun[11] + run[16] + camcol[3] + first[1] + field[12] + obj[16]<br>
	 * <samp> select top 5 dbo.fSDSS(objid) as SDSS from PhotoObj</samp>
	 */
	public static String fSDSS(long fieldID)
	{
		String result= 
			skyVersion(fieldID)+"-"+
			run(fieldID) +"-"+
			rerun(fieldID)+"-"+
			camcol(fieldID)+"-"+
			field(fieldID)+"-"+
			obj(fieldID);
		return result;
	}
	/**
	 * Extracts SkyVersion from an SDSS Photo Object ID
	 * The bit-fields and their lengths are: Skyversion[5] Rerun[11] Run[16] Camcol[3] First[1] Field[12] Obj[16]<br/>
	 * <samp> select top 10 objId, dbo.fSkyVersion(objId) as fSkyVersion from Galaxy</samp>
	 */
	public static int skyVersion(long id)
	{
		return (int)(id/(long)Math.pow(2, 59) & 0x0000000F);
	}
	/**
	 * Extracts Run from an SDSS Photo Object ID
	 * The bit-fields and their lengths are: Skyversion[5] Rerun[11] Run[16] Camcol[3] First[1] Field[12] Obj[16]<br/>
	 * <samp> select top 10 objId, dbo.fRun(objId) as fRun from Galaxy</samp>
	 */
	public static int run(long id)
	{
		return (int)(id/(long)Math.pow(2, 32) & 0x0000FFFF);
	}
	/**
	 * Extracts Rerun from an SDSS Photo Object ID
	 * The bit-fields and their lengths are: Skyversion[5] Rerun[11] Run[16] Camcol[3] First[1] Field[12] Obj[16]<br/>
	 * <samp> select top 10 objId, dbo.fRerun(objId) as fRun from Galaxy</samp>
	 */
	public static int rerun(long id)
	{
		return (int)(id/(long)Math.pow(2, 48) & 0x000007FF);
	}
	/**
	 * Extracts Camcol from an SDSS Photo Object ID
	 * The bit-fields and their lengths are: Skyversion[5] Rerun[11] Run[16] Camcol[3] First[1] Field[12] Obj[16]<br/>
	 * <samp>select top 10 objId, dbo.fCamcol(objId) as fCamcol from Galaxy</samp>
	 */
	public static int camcol(long id)
	{
		return (int)(id/(long)Math.pow(2, 29) & 0x00000007);
	}
	/**
	 * -------------------------------------------------------------------------------
	 * --/H Extracts Field from an SDSS Photo Object ID.
	 * --/T The bit-fields and their lengths are: Skyversion[5] Rerun[11] Run[16] Camcol[3] First[1] Field[12] Obj[16]<br>
	 * --/T <samp> select top 10 objId, dbo.fField(objId) as fField from Galaxy</samp>
	 * ------------------------------------------------------------------------------- 
	 */
	public static int field(long id)
	{
		return (int)(id/(long)Math.pow(2, 16) & 0x00000FFF);
	}
	/**
	 * -------------------------------------------------------------------------------
	 * --/H Extracts Obj from an SDSS Photo Object ID.
	 * --/T The bit-fields and their lengths are: Skyversion[5] Rerun[11] Run[16] Camcol[3] First[1] Field[12] Obj[16]<br>
	 * --/T <samp> select top 10 objId, dbo.fObj(objId) as fObj from Galaxy</samp>
	 * -------------------------------------------------------------------------------
	 */
	public static int obj(long id)
	{
		return (int)(id & 0x0000FFFF);
	}
	/**
	 * The covermap of the given Halfspace specified as a circle with the radius given in minutes of arc.
	 * @param x
	 * @param y
	 * @param z
	 * @param radiusArcMin 半径范围，以弧分为单位
	 * @return
	 */
	public static List<Pair> htmCoverCircleXyz(double x, double y, double z, double radiusArcMin)
	{
		System.out.println("Functions.fHtmCoverCircleXyz-----run");
	    double[] num = new double[1];
	    double[] num2 = new double[1];
//	    if (SqlBoolean.op_True(Trixel.Epsilon > (((x * x) + (y * y)) + (z * z))))
	    if(Trixel.Epsilon > (((x * x) + (y * y)) + (z * z)))
	    {
	        x = 0.0;
	        y = 0.0;
	        z = 1.0;
	    }
	    Cartesian.Xyz2Radec( x,  y,  z, /*ref*/ num, /*ref*/ num2);
	    radiusArcMin = Math.max(0.0, Math.min(648000.0, radiusArcMin));
	    Region reg = new Region(num[0], num2[0],  radiusArcMin);
	    reg.Simplify();
	    List<Pair> list = Cover.HidRange(reg);   //有问题
	    List<Pair> list2 = new ArrayList<Pair>();
	    if (list != null)
	    {
	        int count = list.size();
	        for (int i = 0; i < count; i++)
	        {
	            list2.add(new Pair(list.get(i).getLo(), list.get(i).getHi()));
	        }
	    }
	    System.out.println("Functions.fHtmCoverCircleXyz-----finish");
	    return list2;
	}
	/**
	 * 
	 * @param ra 赤经，范围：[0°-360]，单位：度
	 * @param dec 赤纬，范围：[-90,90]，单位：度
	 * @param radius 半径
	 * @return 根据以上参数得到的查询Array PhotoObjAll 中最近物体的objID的AQL语句
	 */
	public static String fGetNearestObjEq(double ra,double dec,double radius)
	{
//		RETURNS @proxtab TABLE (
//	    objID bigint,
//	    run int NOT NULL,
//	    camcol int NOT NULL,
//	    field int NOT NULL,
//	    rerun int NOT NULL,
//	    type int NOT NULL,
//	    mode int NOT NULL,
//	    cx float NOT NULL,
//	    cy float NOT NULL,
//	    cz float NOT NULL,
//	    htmID bigint,
//	    distance float              -- distance in arc minutes
//	  ) 
		double d2r, nx, ny, nz;
		d2r = Math.PI/180.0;
		nx  = Math.cos(dec*d2r)*Math.cos(ra*d2r);
		ny  = Math.cos(dec*d2r)*Math.sin(ra*d2r);
		nz  = Math.sin(dec*d2r);
		
		List<Pair> pair = htmCoverCircleXyz(nx,ny,nz,radius);
        double lim = Math.pow(2*Math.sin(Math.toRadians(radius/120)),2);

        StringBuilder aql = new StringBuilder();
        //select Top 1 
        aql.append("SELECT objID , ra, dec, type, u, g, r, i, z, distance");
//        aql.append("SELECT objID, run, camcol, field, rerun, type, mode, cx, cy, cz, htmID, sqrt(pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2))/"+d2r+"*60 ");
        aql.append(" FROM (SELECT objID, ra, dec, type, u, g, r, i, z, sqrt(pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2))/"+d2r+"*60 AS distance ");
        aql.append(" FROM PhotoObjAll ");
        aql.append(" WHERE htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2) < "+lim+" AND i>0)");
        aql.append(" ORDER BY distance ASC");
        return aql.toString();
	}
	public static String fPhotoTypeN(int value)
	{
		StringBuilder aql = new StringBuilder();
		aql.append("SELECT name FROM PhotoType WHERE value="+value);
		return aql.toString();
	}
	/**
	 * 
	 * @param ra 赤经，范围：[0°-360]，单位：度
	 * @param dec 赤纬，范围：[-90,90]，单位：度
	 * @param radius 半径
	 * @param zoom 缩放程度 [0,12,24,50]
	 * @return 得到Frame相关信息（包含星体图像）的AQL语句
	 */
	public static String fGetNearbyFrameEq(double ra,double dec,double radius,int zoom)
	{
			double nx ,ny ,nz;
			nx  =  (Math.cos(Math.toRadians(dec))*Math.cos(Math.toRadians(ra)));
			ny  =  (Math.cos(Math.toRadians(dec))*Math.sin(Math.toRadians(ra)));
			nz  =  Math.sin(Math.toRadians(dec));
			System.out.println("nx="+nx+",ny="+ny+",nz="+nz+",radius="+radius);
			
			List<Pair> list = htmCoverCircleXyz(nx,ny,nz,radius);
			System.out.println("listSize:"+list.size());
			for(Pair p : list)
			{
				System.out.println(p.getLo()+","+p.getHi());
			}
			
			double lim ;
			lim =  Math.pow(2*Math.sin(Math.toRadians(radius/120)),2);
			System.out.println(lim);
			
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM( ");
			sql.append(" SELECT img,a,b,c,d,e,f,node,incl,ra,dec,fieldID,pow("+nx+"-cx,2)"+"+pow("+ny+"-cy,2)"+"+pow("+nz+"-cz,2) AS orderCol ");
			sql.append(" FROM Frame ");
			sql.append(" WHERE htmID BETWEEN "+list.get(0).getLo()+" AND "+list.get(0).getHi()+" AND zoom="+zoom+" AND pow("+nx+"-cx,2)"+"+pow("+ny+"-cy,2)"+"+pow("+nz+"-cz,2) < "+lim+" )");
			sql.append(" ORDER BY orderCol");
			return sql.toString();
	}
	public static String fGetObjectsEqStr(int flag,double ra, double dec,double radius,double zoom)
	{
			System.out.println("fGetObjectsEqStr run-------------");
		    double nx,ny,nz,rad,mag;
		                
			rad = radius;
	        if (rad > 250)  rad = 250 ;     //-- limit to 4.15 degrees == 250 arcminute radius
	        nx  = Math.cos(radians(dec))*Math.cos(radians(ra));
	        ny  = Math.cos(radians(dec))*Math.sin(radians(ra));
	        nz  = Math.sin(radians(dec));
	        mag =  25 - 1.5* zoom;  ///-- magnitude reduction.
		        
	        List<Pair> pair = htmCoverCircleXyz(nx, ny, nz, rad);
			
	        double lim = Math.pow(2*Math.sin(radians(rad/120)), 2);
			String res = null;
			if ( (flag & 1) > 0 )  //-- specObj
			{
				StringBuilder aql = new StringBuilder();
				aql.append("SELECT ra,dec,specObjID as objID ");
				aql.append(" FROM SpecObjAll ");//old:SpecObj
				aql.append(" WHERE sciencePrimary=1 AND htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)<"+lim);
				System.out.println("Functions:fGetObjectsEq()-specobj:"+aql);
//				ORDER BY power(@nx-cx,2)+power(@ny-cy,2)+power(@nz-cz,2) ASC
				res = aql.toString();
				
			}

	        if ( (flag & 2) > 0 )  //-- photoObj
	        {
				StringBuilder aql = new StringBuilder();
				aql.append("SELECT ra,dec,objID ");
				aql.append(" FROM PhotoPrimary ");
				aql.append(" WHERE htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)<"+lim+" ");
				aql.append(" AND r<="+mag);
				System.out.println("Functions:fGetObjectsEq():photoprimay-->"+aql);
//				ORDER BY power(@nx-cx,2)+power(@ny-cy,2)+power(@nz-cz,2) ASC
				res = aql.toString();
				return res;
	        }

	        if ( (flag & 4) > 0 )  //-- target
	        {
				StringBuilder aql = new StringBuilder();
				aql.append("SELECT ra,dec,targetID AS objID");
				aql.append(" FROM Target ");
				aql.append(" WHERE htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)<"+lim+" ");
//				ORDER BY power(@nx-cx,2)+power(@ny-cy,2)+power(@nz-cz,2) ASC
				System.out.println("Functions:fGetObjectsEq()-target:aql-->"+aql);
				res = aql.toString();
				return res;
	        }
		               

	        if ( (flag & 8) > 0 ) // -- mask
	        {
				StringBuilder aql = new StringBuilder();
				aql.append("SELECT ra,dec,maskID AS objID");
				aql.append(" FROM Mask ");
				aql.append(" WHERE htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)<"+lim+" ");
//					ORDER BY power(@nx-cx,2)+power(@ny-cy,2)+power(@nz-cz,2) ASC
				System.out.println("Functions:fGetObjectsEq():aql-->mask:"+aql);
				res = aql.toString();
				return res;
	        }

	        if ( (flag & 16) > 0 ) //-- plate
	        {
	        	rad = radius + 89.4;   //-- add the tile radius
				StringBuilder aql = new StringBuilder();
				aql.append("SELECT ra,dec,plateID AS objID");
				aql.append(" FROM PlateX ");
				aql.append(" WHERE htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)<"+lim+" ");
//					ORDER BY power(@nx-cx,2)+power(@ny-cy,2)+power(@nz-cz,2) ASC
				System.out.println("Functions:fGetObjectsEq():aql-->palteX"+aql);
				res = aql.toString();
				return res;
	        }

	        if ( (flag & 32) > 0 )  //-- photoPrimary and secondary
	        {
				StringBuilder aql = new StringBuilder();
				aql.append("SELECT ra,dec,objID ");
				aql.append(" FROM PhotoObjAll ");
				aql.append(" WHERE htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)<"+lim+" ");
				aql.append(" AND (mod=1 OR mod=2) ");
//					ORDER BY power(@nx-cx,2)+power(@ny-cy,2)+power(@nz-cz,2) ASC
				System.out.println("Functions:fGetObjectsEq():aql-->PhotoObjAll"+aql);
				res = aql.toString();
				return res;
	        }
	        return res;
	}
	public static List<Obj> fGetObjectsEq(int flag,double ra, double dec,double radius,double zoom)
	{
		System.out.println("fGetObjectsEq run-------------");
	    double nx,ny,nz,rad,mag;
	                
		rad = radius;
        if (rad > 250)  rad = 250 ;     //-- limit to 4.15 degrees == 250 arcminute radius
        nx  = Math.cos(radians(dec))*Math.cos(radians(ra));
        ny  = Math.cos(radians(dec))*Math.sin(radians(ra));
        nz  = Math.sin(radians(dec));
        mag =  25 - 1.5* zoom;  ///-- magnitude reduction.
	        
        List<Pair> pair = htmCoverCircleXyz(nx, ny, nz, rad);
		System.out.println("fGetObjectsEq()--->pair.size()"+pair.size());
        double lim = Math.pow(2*Math.sin(radians(rad/120)), 2);
		List<Obj> objList = new ArrayList<Obj>();
		if ( (flag & 1) > 0 )  //-- specObj
		{
			StringBuilder aql = new StringBuilder();
			aql.append("SELECT ra,dec,specObjID as objID ");
			aql.append(" FROM SpecObjAll ");//old:SpecObj
			aql.append(" WHERE sciencePrimary=1 AND htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)<"+lim);
			System.out.println("Functions:fGetObjectsEq()-specobj:"+aql);
//				ORDER BY power(@nx-cx,2)+power(@ny-cy,2)+power(@nz-cz,2) ASC
			ResultSet rs = null;
			ExQuery exQuery = new ExQuery();
			try {
				rs = exQuery.aqlQuery(aql.toString());
				while(!rs.isAfterLast())
				{
					float oRa = rs.getFloat("ra");
					float oDec = rs.getFloat("dec");
					long objID = rs.getLong("objID");
					Obj obj = new Obj();
					obj.setRa(oRa);
					obj.setDec(oDec);
					obj.setObjID(objID);
					obj.setFlag(flag);
					objList.add(obj);
					rs.next();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	            

        if ( (flag & 2) > 0 )  //-- photoObj
        {
			StringBuilder aql = new StringBuilder();
			aql.append("SELECT ra,dec,objID ");
			aql.append(" FROM PhotoPrimary ");
			aql.append(" WHERE htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)<"+lim+" ");
			aql.append(" AND r<="+mag);
			System.out.println("Functions:fGetObjectsEq():photoprimay-->"+aql);
//				ORDER BY power(@nx-cx,2)+power(@ny-cy,2)+power(@nz-cz,2) ASC
			ResultSet rs = null;
			ExQuery exQuery = new ExQuery();
			try {
				rs = exQuery.aqlQuery(aql.toString());
				
				while(!rs.isAfterLast())
				{
					double oRa = rs.getDouble("ra");
					double oDec = rs.getDouble("dec");
					long objID = rs.getLong("objID");
					Obj obj = new Obj();
					obj.setRa(oRa);
					obj.setDec(oDec);
					obj.setObjID(objID);
					obj.setFlag(2);
					objList.add(obj);
					rs.next();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
        }

        if ( (flag & 4) > 0 )  //-- target
        {
			StringBuilder aql = new StringBuilder();
			aql.append("SELECT ra,dec,targetID ");
			aql.append(" FROM Target ");
			aql.append(" WHERE htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)<"+lim+" ");
//				ORDER BY power(@nx-cx,2)+power(@ny-cy,2)+power(@nz-cz,2) ASC
			ResultSet rs = null;
			ExQuery exQuery = new ExQuery();
			System.out.println("Functions:fGetObjectsEq()-target:aql-->"+aql);
			try {
				rs = exQuery.aqlQuery(aql.toString());
				while(!rs.isAfterLast())
				{
					float oRa = rs.getFloat("ra");
					float oDec = rs.getFloat("dec");
					long objID = rs.getLong("targetID");
					Obj obj = new Obj();
					obj.setRa(oRa);
					obj.setDec(oDec);
					obj.setObjID(objID);
					obj.setFlag(4);
					objList.add(obj);
					rs.next();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
        }
	               

	        if ( (flag & 8) > 0 ) // -- mask
	        {
				StringBuilder aql = new StringBuilder();
				aql.append("SELECT ra,dec,maskID ");
				aql.append(" FROM Mask ");
				aql.append(" WHERE htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)<"+lim+" ");
//					ORDER BY power(@nx-cx,2)+power(@ny-cy,2)+power(@nz-cz,2) ASC
				System.out.println("Functions:fGetObjectsEq():aql-->mask"+aql);
				ResultSet rs = null;
				ExQuery exQuery = new ExQuery();
				try {
					rs = exQuery.aqlQuery(aql.toString());
					while(!rs.isAfterLast())
					{
						float oRa = rs.getFloat("ra");
						float oDec = rs.getFloat("dec");
						long objID = rs.getLong("maskID");
						Obj obj = new Obj();
						obj.setRa(oRa);
						obj.setDec(oDec);
						obj.setObjID(objID);
						obj.setFlag(8);
						objList.add(obj);
						rs.next();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
	        }

	        if ( (flag & 16) > 0 ) //-- plate
	        {
	        	rad = radius + 89.4;   //-- add the tile radius
				StringBuilder aql = new StringBuilder();
				aql.append("SELECT ra,dec,plateID ");
				aql.append(" FROM PlateX ");
				aql.append(" WHERE htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)<"+lim+" ");
//					ORDER BY power(@nx-cx,2)+power(@ny-cy,2)+power(@nz-cz,2) ASC
				System.out.println("Functions:fGetObjectsEq():aql-->palteX:"+aql);
				ResultSet rs = null;
				ExQuery exQuery = new ExQuery();
				try {
					rs = exQuery.aqlQuery(aql.toString());
					while(!rs.isAfterLast())
					{
						double oRa = rs.getDouble("ra");
						double oDec = rs.getDouble("dec");
						long objID = rs.getLong("plateID");
						Obj obj = new Obj();
						obj.setRa(oRa);
						obj.setDec(oDec);
						obj.setObjID(objID);
						obj.setFlag(16);
						objList.add(obj);
						rs.next();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
	        }

	        if ( (flag & 32) > 0 )  //-- photoPrimary and secondary
	        {
				StringBuilder aql = new StringBuilder();
				aql.append("SELECT ra,dec,objID ");
				aql.append(" FROM PhotoObjAll ");
				aql.append(" WHERE htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)<"+lim+" ");
				aql.append(" AND (mod=1 OR mod=2) ");
//					ORDER BY power(@nx-cx,2)+power(@ny-cy,2)+power(@nz-cz,2) ASC
				System.out.println("Functions:fGetObjectsEq():aql-->PhotoObjAll"+aql);
				ResultSet rs = null;
				ExQuery exQuery = new ExQuery();
				try {
					rs = exQuery.aqlQuery(aql.toString());
					while(!rs.isAfterLast())
					{
						float oRa = rs.getFloat("ra");
						float oDec = rs.getFloat("dec");
						long objID = rs.getLong("plateID");
						Obj obj = new Obj();
						obj.setRa(oRa);
						obj.setDec(oDec);
						obj.setObjID(objID);
						obj.setFlag(2);
						objList.add(obj);
						rs.next();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
	        }
		        return objList;
	}
/*--------------------------------以下是explore用到的函数---------------------------------------------*/
	
	public static String fGetNearbyObjAllEq(double ra, double dec, double r)
	{
		double d2r, nx, ny, nz;
		d2r = Math.PI/180.0;
		if (r<0) 
			return null;
		nx  = Math.cos(dec*d2r)*Math.cos(ra*d2r);
		ny  = Math.cos(dec*d2r)*Math.sin(ra*d2r);
		nz  = Math.sin(dec*d2r);
		return fGetNearbyObjAllXYZ(nx,ny,nz,r) ;
	}
	
	
	/**
	 * @param nx 天球坐标对应的向量坐标
	 * @param ny 天球坐标对应的向量坐标
	 * @param nz 天球坐标对应的向量坐标
	 * @param r 搜索半径
	 * @return 对给定坐标的搜索半径范围内的天体的查询AQL语句
	 */
	public static String fGetNearbyObjAllXYZ(double nx ,double ny ,double nz ,double r )
	{
		StringBuilder aql = new StringBuilder();
		
		double lim = Math.pow(2*Math.sin(Math.toRadians(r/120)),2);
		double d2r = Math.PI/180.0;
		List<Pair> pair = htmCoverCircleXyz(nx,ny,nz,r);
		
		aql = aql.append("SELECT * FROM (SELECT objID, run, camcol, field, rerun, "
				+" type, mode, cx, cy, cz, htmID, "
//            --sqrt(pow(@nx-cx,2)+pow(@ny-cy,2)+pow(@nz-cz,2))/@d2r*60 
//            +"2*DEGREES(asin(sqrt(pow(@nx-cx,2)+pow(@ny-cy,2)+pow(@nz-cz,2))/2))*60 "
				+" sqrt(pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2))/"+d2r+"*60 AS distance "
				+" FROM PhotoObjAll AS P "
				+" WHERE (P.htmID BETWEEN "+pair.get(0).getLo()+" AND "+pair.get(0).getHi()+" )"
				+" AND pow("+nx+"-cx,2)+pow("+ny+"-cy,2)+pow("+nz+"-cz,2)< "+lim+") "
				+" ORDER BY distance");
		return aql.toString();
	}
	public static String fGetNearestApogeeStarEq(double qra, double qdec,
			double searchRadius) {
		return null;
	}
	public static String fPhotoModeN(int value)
	{
		String aql = "SELECT name FROM ("+View.getPhotoMode()+") WHERE value='"+ Utilities.longToHex(value)+"'";
		System.out.println("Functions.fPhotoModeN-->aql"+aql);
		String name = null;
		ExQuery eq = new ExQuery();
		ResultSet rs = null;
		try {
			rs = eq.aqlQuery(aql);
			if(!rs.isAfterLast())
			{
				name = rs.getString("name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}
	public static String fPhotoFlagsN(long value)
	{
//		 DECLARE @bit int, @mask bigint, @out varchar(2000);
//		    SET @bit=63;
//		    SET @out ='';
//		    WHILE (@bit>0)
//			BEGIN
//			    SET @bit = @bit-1;
//			    SET @mask = power(cast(2 as bigint),@bit);
//			    SET @out = @out + (CASE 
//				WHEN (@mask & @value)=0 THEN '' 
//				ELSE coalesce((select name from PhotoFlags where value=@mask),'')+' '
//			    	END);
//			END
//		    RETURN @out;
		int bit = 63;
		long mask;
		String out = "";
		while(bit > 0)
		{
			bit--;
			mask = (long) Math.pow(2L, bit);
			if((mask & value) ==0)
				out += "";
			else
			{
				ExQuery eq = new ExQuery();
				String aql = "select name from ("+View.getPhotoFlags()+") where value='"+Utilities.longToHex(mask)+"'";
				
				ResultSet rs = null;
				try {
					rs = eq.aqlQuery(aql);
					String name = null;
					while(!rs.isAfterLast())
					{
						name = rs.getString("name");
						if(name != null)
						{
							out += " ";
							break;
						}
						rs.next();
					}
							
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			
		}
		
		return out;
	}
	public static String fSpecZWarningN(Integer value)
	{
//		 DECLARE @bit int, @mask bigint, @out varchar(2000);
//	    SET @bit=32;
//	    SET @out ='';
//	    IF @value IS NULL
//		RETURN 'NULL';
//	    IF @value = 0
//		RETURN 'OK';
//	    WHILE (@bit>0)
//		BEGIN
//		    SET @bit = @bit-1;
//		    SET @mask = power(cast(2 as bigint),@bit);
//		    SET @out = @out + (CASE 
//			WHEN (@mask & @value)=0 THEN '' 
//			ELSE coalesce((select name from SpecZWarning where value=@mask),'')+' '
//		    	END);
//		END
//	    RETURN @out;
		int bit =32;
		String out = "";
		long mask;
		if(value == null)
			return "NULL";
		if(value.intValue()==0)
			return "OK";
		while(bit>0)
		{
			bit --;
			mask = (long) Math.pow(2L, bit);
			if((mask & value)==0)
				out += "";
			else
			{
				ExQuery eq = new ExQuery();
				String aql = "select name from SpecZWarning where value="+mask;
				ResultSet rs = null;
				try {
					rs = eq.aqlQuery(aql);
					String name = null;
					if(!rs.isAfterLast())
					{
						name = rs.getString("name");
						if(name != null)
						{
							out += name+" ";
							break;
						}
					}
							
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return out;
	}
	public static long fObjID(long objID)
	{
//		-------------------------------------------------------------------------------
//		--/H Match an objID to a PhotoObj and set/unset the first field bit.
//		--
//		--/T Given an objID this function determines whether there is a
//		--/T PhotoObj with a matching (skyversion, run, rerun, camcol, field, 
//		--/T obj) and returns the objID with the first field bit set properly
//		--/T to correspond to that PhotoObj.  It returns 0 if there is
//		--/T no corresponding PhotoObj.  It does not matter whether the
//		--/T first field bit is set or unset in the input objID.
//		-------------------------------------------------------------------------------
//		RETURNS BIGINT
//		AS BEGIN
//		    DECLARE @firstfieldbit bigint;
//		    SET @firstfieldbit = 0x0000000010000000;
//		    SET @objID = @objID & ~@firstfieldbit;
//		    IF (select count_big(*) from PhotoTag WITH (nolock) where objID = @objID) > 0
//		        RETURN @objID
//		    ELSE
//		    BEGIN
//		        SET @objID = @objID + @firstfieldbit;
//		        IF (select count_big(*) from PhotoTag WITH (nolock) where objID = @objID) > 0
//		            RETURN @objID
//		    END
//		    RETURN cast(0 as bigint)
//		END
		long firstfieldbit = 0x0000000010000000;
		objID = objID & ~firstfieldbit;
		ResultSet rs = null;
		StringBuilder aql = new StringBuilder();
		aql.append("SELECT count(*) FROM ( "+View.getPhotoTag()+" ) ");
		aql.append("WHERE objID="+objID);
		ExQuery ex = new ExQuery();
		
		try {
			rs = ex.aqlQuery(aql.toString());
			if(!rs.isAfterLast())
				return objID;
			else 
			{
				objID = objID + firstfieldbit;
				aql = null;
				aql = new StringBuilder();
				aql.append("SELECT count(*) FROM ( "+View.getPhotoTag()+" ) ");
				aql.append("WHERE objID="+objID);
				rs = ex.aqlQuery(aql.toString());
				if(rs.getBigDecimal(1).longValue()>0)
				{
					return objID;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0L;
		
	}
	public static String fIAUFromEq(double ra,double dec)
	{
		return  "SDSS J"+(fHMSbase(ra,1,2)+fDMSbase(dec,1,1)).replace(":","");
	}
	public static String fHMSbase(double ra,double dec)
	{
		return null;
	}
	/**
	 * -------------------------------------------------------------------------------
	 * 	--/H Base function to convert right ascension in degrees to +hh:mm:ss.ss notation.
	 * 	-------------------------------------------------------------------------------
	 * 	--/T @truncate is 0 (default) if decimal digits to be rounded, 1 to be truncated.
	 * 	--/T <br> @precision is the number of decimal digits, default 2.
	 * 	--/T <p><samp> select dbo.fHMSBase(187.5,1,3) </samp> <br>
	 * 	--/T <samp> select dbo.fHMSBase(187.5,default,default) </samp>
	 * 	-------------------------------------------------------------------------------
	 * @param ra
	 * @param truncate
	 * @param precision
	 * @return
	 */
	public static String fHMSbase(double deg,int truncate,int precision)
	{
		String t = "00:00:00.0"; 
		double d =  Math.abs(deg/15.0); 
		int nd = (int) Math.floor(d);
		String q = ltrim(new Integer(nd).toString());
		int np = 0;
//		--
		if (precision < 1) precision = 1;
		if (precision > 10)precision = 10;
		while ( np  < (precision-1))
		{
			t = t+'0';
			np = np + 1;
		}
//		-- degrees
		t  = stuff(t,3-q.length(),q.length(), q);
//		-- minutes
		d  =  (60.0 * (d-nd));
		nd = (int) Math.floor(d);
		q  = new Integer(nd).toString().trim();
		t  = stuff(t,6-q.length(),q.length(), q);
//		-- seconds
		if(truncate != 0) //截断
		{
			String str = new String(60.0 * (d-nd)+"");
			int endIndex = str.indexOf(".");
			d = Double.parseDouble(str.substring(0,endIndex+precision+1));
		}
		else   //四舍五入
			d = new BigDecimal(60.0 * (d-nd)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//	--	SET @d  = 60.0 * (@d-@nd);
		q  = ltrim(str(d,6+precision,precision));
		t = stuff(t,10+precision-q.length(),q.length(), q);
		return t;
	}
	private static String ltrim(String value) {
		int len = value.length();
        int st = 0;

        while ((st < len) && (value.charAt(st) <= ' ')) {
            st++;
        }
        return (st > 0) ? value.substring(st) : value;
			
	}
	/**
	 * -------------------------------------------------------------------------------
		--/H Base function to convert declination in degrees to +dd:mm:ss.ss notation.
		-------------------------------------------------------------------------------
		--/T @truncate is 0 (default) if decimal digits to be rounded, 1 to be truncated.
		--/T <br> @precision is the number of decimal digits, default 2.
		--/T <p><samp> select dbo.fDMSbase(87.5,1,4) </samp> <br>
		--/T <samp> select dbo.fDMSbase(87.5,default,default) </samp>
		-------------------------------------------------------------------------------
	 * @param ra
	 * @param truncate
	 * @param precision
	 * @return
	 */
	public static String fDMSbase(double deg,int truncate,int precision)
	{
		String s = "+";
		String t = "00:00:00.0"; 
		double d =  Math.abs(deg); 
		int nd = (int) Math.floor(d);
		String q = ltrim(new Integer(nd).toString());
		int np = 0;
		
		if(deg<0) s = "-";
		
		if (precision < 1) precision = 1;
		if (precision > 10) precision = 10;
		
		while (np < precision-1)
		{
			t = t+"0";
			np = np + 1;
		}
//		-- degrees
		t  = stuff(t,3-q.length(),q.length(), q);
//		-- minutes
		d  =  (60.0 * (d-nd));
		nd = (int) Math.floor(d);
		q  = ltrim(new Integer(nd).toString().trim());
		t  = stuff(t,6-q.length(),q.length(), q);
//		-- seconds
		if(truncate != 0) //截断
		{
			String str = new String(60.0 * (d-nd)+"");
			int endIndex = str.indexOf(".");
			d = Double.parseDouble(str.substring(0,endIndex+precision+1));
		}
		else   //四舍五入
			d = new BigDecimal(60.0 * (d-nd)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//	--	SET @d  = 60.0 * (@d-@nd);
		q  = ltrim(str(d,6+precision,precision).trim());
		t = stuff(t,10+precision-q.length(),q.length(), q);
		return (s+t);
	}

	private static String str(double num,int length,int decimal)
	{
		StringBuilder precision = new StringBuilder();
		while(decimal-- > 0)
		{
			precision.append("#");
		}
		String result = new DecimalFormat("#."+precision.toString()).format(num);
		if(result.length()>length)
		{
			int diff = result.length() - length;
			if(diff <= 3)
			{
				result = result.substring(0, result.length()-length);
			}
		}
		return result;
	}
	/*
	 * 参数都以index 从1起为标准
	 * 
	 * 如果开始位置或长度值是负数，或者如果开始位置大于第一个字符串的长度，将返回空字符串。如果要删除的长度大于第一个字符串的长度，将删除到第一个字符串中的第一个字符。 
	 * 如果结果值大于返回类型支持的最大值，则产生错误。

示例 
以下示例在第一个字符串 abcdef 中删除从第 2 个位置（字符 b）开始的三个字符，然后在删除的起始位置插入第二个字符串，从而创建并返回一个字符串。

SELECT STUFF('abcdef', 2, 3, 'ijklmn'); 
GO

下面是结果集： 
--------- 
aijklmnef

(1 row(s) affected)
	 */
	/**
	 * @param str 
	 * @param start 个整数值，指定删除和插入的开始位置。如果 start 或 length 为负，则返回空字符串。
	 * 				如果 start 比第一个 character_expression 长，则返回空字符串。start 可以是 bigint 类型。
	 * @param length 一个整数，指定要删除的字符数。如果 length 比第一个 character_expression 长，
	 * 				则最多删除到replacement中的最后一个字符。length 可以是 bigint 类型。
	 * @param replacement
	 */
	private static String stuff(String str, int start, int length, String str2) {
		if(start<0 || str.length()<start)
			return "";
		String temp = str;
		String pre = temp.substring(0,start-1);
		String behind = temp.substring(start-1+length);
		return pre + str2 + behind;
	}
	/**
	 * ---------------------------------------------------
	 * --/H Returns the expanded PrimTarget corresponding to the flag value as a string
	 *	---------------------------------------------------
	 *	--/T the photo and spectro primTarget flags can be shown with Select * from PrimTarget 
	 *	--/T <br>
	 *	--/T Sample call to show the target flags of some photoObjects is
	 *	--/T <samp> 
	 *	--/T <br> select top 10 objId, dbo.fPriTargetN(secTarget) as priTarget
	 *	--/T <br> from photoObj 
	 *	--/T </samp> 
	 *	--/T <br> see also fPrimTarget, fSecTargetN
	 *	-------------------------------------------------------------
	 * @param value
	 */
	public static String fPrimTargetN(int value) {
		/*
		 * RETURNS varchar(1000)
			BEGIN
			    DECLARE @bit int, @mask bigint, @out varchar(2000);
			    SET @bit=32;
			    SET @out ='';
			    WHILE (@bit>0)
				BEGIN
				    SET @bit = @bit-1;
				    SET @mask = power(cast(2 as bigint),@bit);
				    SET @out = @out + (CASE 
					WHEN (@mask & @value)=0 THEN '' 
					ELSE coalesce((select name from PrimTarget where value=@mask),'')+' '
				    	END);
				END
			    RETURN @out;
			END
		 */
		int bit = 32;
		long mask;
		String out = "";
		while(bit > 0)
		{
			bit --;
			mask = (long) Math.pow(2L, bit);
			if((mask & value) == 0)
				out += "";
			else
			{
				ExQuery eq = new ExQuery();
				String aql = "select name from ("+View.getPrimTarget()+") where value='"+Utilities.longToHex(mask)+"'";
				ResultSet rs = null;
				try {
					rs = eq.aqlQuery(aql);
					String name = null;
					if(!rs.isAfterLast())
					{
						name = rs.getString("name");
						if(name != null)
						{
							out += name + " ";
						}
					}
							
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
					
		}
		return out;
		
	}
	/*
	 * ---------------------------------------------------
	 * 	--/H Returns the expanded SpecialTarget1 corresponding to the flag value as a string
	 * 	---------------------------------------------------
	 * 	--/T The spectro specialTarget1 flags can be shown with Select * from SpecialTarget1 
	 * 	--/T <br>
	 * 	--/T Sample call to show the special target flags of some spec objects is
	 * 	--/T <samp> 
	 * 	--/T <br> select top 10 specObjId, dbo.fSpecialTarget1N(specialTarget1) as specialTarget1
	 * 	--/T <br> from specObj 
	 * 	--/T </samp> 
	 * 	--/T <br> see also fSpecialTarget1, fSecTargetN
	 * 	-------------------------------------------------------------
	 */
	public static String fSpecialTarget1N(Long value) {
		int bit = 32;
		long mask;
		String out = "";
		while(bit > 0)
		{
			bit --;
			mask = (long) Math.pow(2L, bit);
			if((mask & value) == 0)
				out += "";
			else
			{
				ExQuery eq = new ExQuery();
				String aql = "select name from SpecialTarget1 where value="+mask;
				ResultSet rs = null;
				try {
					rs = eq.aqlQuery(aql);
					String name = null;
					if(!rs.isAfterLast())
					{
						name = rs.getString("name");
						if(name != null)
						{
							out += name + " ";
						}
					}
							
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
					
		}
		return out;
		
	}
	/**
	 * ---------------------------------------------------
	 * 	--/H Returns the expanded Segue1Target1 corresponding to the flag value as a string
	 * 	---------------------------------------------------
	 * 	--/T The spectro Segue1Target1 flags can be shown with Select * from Segue1Target1 
	 * 	--/T <br>
	 * 	--/T Sample call to show the Segue1 target flags of some spec objects is
	 * 	--/T <samp> 
	 * 	--/T <br> select top 10 specObjId, dbo.fSegue1Target1N(Segue1Target1) as Segue1Target1
	 * 	--/T <br> from specObj 
	 * 	--/T </samp> 
	 * 	--/T <br> see also fSegue1Target1, fSecTargetN
	 * 	-------------------------------------------------------------
	 * @param value
	 */
	public static String fSegue1Target1N(int value) {
		int bit = 32;
		long mask;
		String out = "";
		while(bit > 0)
		{
			bit --;
			mask = (long) Math.pow(2L, bit);
			if((mask & value) == 0)
				out += "";
			else
			{
				ExQuery eq = new ExQuery();
				String aql = "select name from Segue1Target1 where value="+mask;
				ResultSet rs = null;
				try {
					rs = eq.aqlQuery(aql);
					String name = null;
					if(!rs.isAfterLast())
					{
						name = rs.getString("name");
						if(name != null)
						{
							out += name + " ";
						}
					}
							
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
					
		}
		return out;
	}
	public static String fSegue1Target2N(int value) {
		int bit = 32;
		long mask;
		String out = "";
		while(bit > 0)
		{
			bit --;
			mask = (long) Math.pow(2L, bit);
			if((mask & value) == 0)
				out += "";
			else
			{
				ExQuery eq = new ExQuery();
				String aql = "select name from Segue1Target2 where value="+mask;
				ResultSet rs = null;
				try {
					rs = eq.aqlQuery(aql);
					String name = null;
					if(!rs.isAfterLast())
					{
						name = rs.getString("name");
						if(name != null)
						{
							out += name + " ";
						}
					}
							
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
					
		}
		return out;
	}
	/**
	 * ---------------------------------------------------
	 * 	--/H Returns the expanded Segue2Target1 corresponding to the flag value as a string
	 * 	---------------------------------------------------
	 * 	--/T The spectro Segue2Target1 flags can be shown with Select * from Segue2Target1 
	 * 	--/T <br>
	 * 	--/T Sample call to show the SEGUE-2 target flags of some spec objects is
	 * 	--/T <samp> 
	 * 	--/T <br> select top 10 specObjId, dbo.fSegue2Target1N(Segue2Target1) as segue2Target1
	 * 	--/T <br> from specObj 
	 * 	--/T </samp> 
	 * 	--/T <br> see also fSegue2Target1, fSecTargetN
	 * 	-------------------------------------------------------------
	 */
	public static String fSegue2Target1N(int value) {
		
		int bit = 32;
		long mask;
		String out = "";
		while(bit > 0)
		{
			bit --;
			mask = (long) Math.pow(2L, bit);
			if((mask & value) == 0)
				out += "";
			else
			{
				ExQuery eq = new ExQuery();
				String aql = "select name from Segue2Target1 where value="+mask;
				ResultSet rs = null;
				try {
					rs = eq.aqlQuery(aql);
					String name = null;
					if(!rs.isAfterLast())
					{
						name = rs.getString("name");
						if(name != null)
						{
							out += name + " ";
						}
					}
							
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
					
		}
		return out;
	}
	public static String fSegue2Target2N(int value) {
		int bit = 32;
		long mask;
		String out = "";
		while(bit > 0)
		{
			bit --;
			mask = (long) Math.pow(2L, bit);
			if((mask & value) == 0)
				out += "";
			else
			{
				ExQuery eq = new ExQuery();
				String aql = "select name from Segue2Target2 where value="+mask;
				ResultSet rs = null;
				try {
					rs = eq.aqlQuery(aql);
					String name = null;
					if(!rs.isAfterLast())
					{
						name = rs.getString("name");
						if(name != null)
						{
							out += name + " ";
						}
					}
							
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
					
		}
		return out;
		
	}
	/**
	 * -------------------------------------------------
		--/H Get the URL to the FITS file of a corrected frame given the fieldID and band
		-------------------------------------------------
		--/T Combines the value of the DataServer URL from the
		--/T SiteConstants table and builds up the whole URL
		--/T from the fieldId (and a u, g, r, i or z filter)
		--/T <br><samp> select dbo.fGetUrlFitsCFrame(568688299343872,'r')</samp>
		-------------------------------------------------
	 * @param fieldID
	 * @param filter
	 */
	public static String fGetUrlFitsCFrame(String fieldID, String filter) {
		/*
		 * DECLARE @link varchar(256), @run varchar(8), @rerun varchar(8),
		@camcol varchar(8), @field varchar(8), @run6 varchar(10),
		@dbType varchar(32), @release varchar(8);
	SET @link = (select value from SiteConstants where name='DataServerURL');
	SET @release = (select value from SiteConstants where name='Release');
	SET @dbType = (select value from SiteConstants where name='DB Type');
	--SET @link = @link + 'imaging/';
	SET @link = @link + 'sas/dr' + @release + '/boss/photoObj/frames/';
	SELECT  @run = cast(run as varchar(8)), @rerun=cast(rerun as varchar(8)), 
		@camcol=cast(camcol as varchar(8)), @field=cast(field as varchar(8))
	    FROM Field
	    WHERE fieldId=@fieldId;
	IF (@dbType LIKE 'Stripe 82%') AND @run IN ('106','206')
	    BEGIN
	    	-- kludge for coadd runs, which were changed for CAS since the
	    	-- run numbers did not fit in 16 bits (smallint)
	    	SET @run6 = substring(@run,1,1) + '000' + substring(@run,2,2);
		SET @run = @run6;
	    END
	ELSE
	    SET @run6   = substring('000000',1,6-len(@run)) + @run;
	SET @field = substring('0000',1,4-len(@field)) + @field;
	--RETURN 	 @link + @run + '/' + @rerun + '/corr/'+@camcol+'/fpC-'+@run6+'-'+@filter+@camcol+'-'+@field+'.fit.gz';
	RETURN 	 @link + @rerun + '/' + @run + '/' +@camcol+'/frame-'+@filter+'-'+@run6+'-'+@camcol+'-'+@field+'.fits.bz2';
		 */
		String link = "http://dr12.sdss3.org/";
	    String release = "9";
	    String dbType = "DR9 FASTDB";
	    String run = "";
	    String rerun = "";
	    String camcol = "";
	    String field = "";
	    String run6 = "";
	    link += "sas/dr12/boss/photoObj/frames/";
	    String aql = "SELECT run, rerun, camcol, field FROM Field WHERE fieldID="+fieldID;
	    ExQuery ex = new ExQuery();
	    try {
			ResultSet rs = ex.aqlQuery(aql);
			
			if(rs.isAfterLast())
			{
				run = rs.getLong("run")+"";
				rerun = rs.getLong("rerun")+"";
				camcol = rs.getLong("camcol")+"";
				field = rs.getLong("field")+"";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	    run6 = "000000".substring(1, 6-run.length())+run;
	    field = "0000".substring(1, 4-field.length())+field;
		return link + rerun + "/" + run + "/" +camcol+"/frame-"+filter+'-'+run6+'-'+camcol+'-'+field+".fits.bz2";
	}
	/**
	 * -------------------------------------------------
		--/H Get the URL to the FITS file of a binned frame given FieldID and band.
		-------------------------------------------------
		--/T Combines the value of the DataServer URL from the
		--/T SiteConstants table and builds up the whole URL
		--/T from the fieldId (and a u, g, r, i or z filter)
		--/T <br><samp> select dbo.fGetUrlFitsBin(568688299343872,'r')</samp>
		-------------------------------------------------
	 * @param field
	 * @param filter
	 */
	public static String fGetUrlFitsBin(long fieldID,String filter)
	{
		String link = "http://dr12.sdss3.org/";
	    String release = "9";
	    String dbType = "DR9 FASTDB";
	    String run = "";
	    String rerun = "";
	    String camcol = "";
	    String field = "";
	    String run6 = "";
	    link += "sas/dr9/boss/photo/redux/";
	    String aql = "SELECT run, rerun, camcol, field FROM Field WHERE fieldID="+fieldID;
	    
	    ExQuery ex = new ExQuery();
	    try {
			ResultSet rs = ex.aqlQuery(aql);
			
			if(rs.isAfterLast())
			{
				run = rs.getLong("run")+"";
				rerun = rs.getLong("rerun")+"";
				camcol = rs.getLong("camcol")+"";
				field = rs.getLong("field")+"";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	    run6 = "000000".substring(1, 6-run.length())+run;
	    field = "0000".substring(1, 4-field.length())+field;
		return link + rerun + "/" + run + "/objcs/" +camcol+"/fpBIN-"+run6+'-'+filter+camcol+'-'+field+".fits.bz2";
	}
	/**
	 * -------------------------------------------------
		--/H Get the URL to the FITS file of a frame mask given the fieldID and band
		-------------------------------------------------
		--/T Combines the value of the DataServer URL from the
		--/T SiteConstants table and builds up the whole URL
		--/T from the fieldId (and a u, g, r, i or z filter)
		--/T <br><samp> select dbo.fGetUrlFitsMask(568688299343872,'r')</samp>
		-------------------------------------------------
	 * @param fieldID
	 * @param filter
	 * @return
	 */
	public static String fGetUrlFitsMask(long fieldID,String filter)
	{
		String link = "http://dr12.sdss3.org/";
	    String release = "9";
	    String dbType = "DR9 FASTDB";
	    String run = "";
	    String rerun = "";
	    String camcol = "";
	    String field = "";
	    String run6 = "";
	    link += "sas/dr9/boss/photo/redux/";
	    String aql = "SELECT run, rerun, camcol, field FROM Field WHERE fieldID="+fieldID;
	    
	    ExQuery ex = new ExQuery();
	    try {
			ResultSet rs = ex.aqlQuery(aql);
			
			if(rs.isAfterLast())
			{
				run = rs.getLong("run")+"";
				rerun = rs.getLong("rerun")+"";
				camcol = rs.getLong("camcol")+"";
				field = rs.getLong("field")+"";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	    run6 = "000000".substring(1, 6-run.length())+run;
	    field = "0000".substring(1, 4-field.length())+field;
		return link + rerun + "/" + run + "/objcs/" +camcol+"/fpM-"+run6+'-'+filter+camcol+'-'+field+".fits.gz";
	}
	/**
	 * -------------------------------------------------
		--/H Get the URL to the FITS file of all atlas images in a field
		-------------------------------------------------
		--/T Combines the value of the DataServer URL from the
		--/T SiteConstants table and builds up the whole URL
		--/T from the fieldId  
		--/T <br><samp> select dbo.fGetUrlFitsAtlas(568688299343872)</samp>
		-------------------------------------------------
	 */
	public static String fGetUrlFitsAtlas(long fieldID)
	{
		String link = "http://dr12.sdss3.org/";
	    String release = "9";
	    String dbType = "DR9 FASTDB";
	    String run = "";
	    String rerun = "";
	    String camcol = "";
	    String field = "";
	    String run6 = "";
	    link += "sas/dr9/boss/photo/redux/";
	    String aql = "SELECT run, rerun, camcol, field FROM Field WHERE fieldID="+fieldID;
	    
	    ExQuery ex = new ExQuery();
	    try {
			ResultSet rs = ex.aqlQuery(aql);
			
			if(rs.isAfterLast())
			{
				run = rs.getLong("run")+"";
				rerun = rs.getLong("rerun")+"";
				camcol = rs.getLong("camcol")+"";
				field = rs.getLong("field")+"";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	    run6 = "000000".substring(1, 6-run.length())+run;
	    field = "0000".substring(1, 4-field.length())+field;
		return link + rerun + "/" + run + "/objcs/" +camcol+"/fpAtlas-"+run6+'-'+camcol+'-'+field+".fits";
	}
	/**
	 * -------------------------------------------------
		--/H Given a FieldID returns the URL to the FITS file of that field 
		-------------------------------------------------
		--/T Combines the value of the DataServer URL from the
		--/T SiteConstants table and builds up the whole URL 
		--/T <br><samp> select dbo.fGetUrlFitsField(568688299343872)</samp>
		-------------------------------------------------
	 * @param fieldID
	 * @return
	 */
	public static String fGetUrlFitsField(long fieldID)
	{
		String link = "http://dr12.sdss3.org/";
	    String release = "9";
	    String dbType = "DR9 FASTDB";
	    String run = "";
	    String rerun = "";
	    String camcol = "";
	    String field = "";
	    String run6 = "";
	    String skyVersion = fSkyVersion(fieldID)+"";
	    String stripe = null;
	    link += "sas/dr9/boss/photoObj/";
	    String aql = "SELECT f.run, f.rerun, s.stripe, f.camcol, f.field FROM Field AS f, Run AS s WHERE f.fieldID="+fieldID +" AND s.run=f.run";
	    ExQuery ex = new ExQuery();
	    try {
			ResultSet rs = ex.aqlQuery(aql);
			
			if(rs.isAfterLast())
			{
				run = rs.getLong("run")+"";
				rerun = rs.getLong("rerun")+"";
				stripe = rs.getLong("stripe")+"";//??????type??
				camcol = rs.getLong("camcol")+"";
				field = rs.getLong("field")+"";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
	    run6 = "000000".substring(1, 6-run.length())+run;
	    link = link+rerun + "/";
	    if ("0".equals(skyVersion))
	     link = link + "inchunk_target/";
	    else if ("1".equals(skyVersion))
	     link = link + "inchunk_best/";
	    else
	     link = link + run + "/" + camcol + "/";
	    field = "0000".substring(1,4-field.length()) + field;
		if ("15".equals(skyVersion))
	     link = link + rerun + "/calibChunks/";
	    return  link+"photoObj-"+run6+"-"+camcol+"-"+field+".fits";
	}
	public static int fSkyVersion(long objID)
	{
//		RETURN ( cast( ((@ObjID / power(cast(2 as bigint),59)) & 0x0000000F) AS INT));
		return (int)(objID/Math.pow(2, 59)) & 0x0000000F;
	}
	/**
	 * -------------------------------------------------
	 *	--/H Get the URL to the FITS file of the spectrum given the SpecObjID
	 *	-------------------------------------------------
	 *	--/T Combines the value of the DataServer URL from the
	 *	--/T SiteConstants table and builds up the whole URL
	 *	--/T from the specObjId.
	 *	--/T <br><samp> select dbo.fGetUrlFitsSpectrum(75094092974915584)</samp>
	 *	-------------------------------------------------
	 * @param specObjID
	 * @return
	 */
	public static String fGetUrlFitsSpectrum(long specObjID)
	{
		System.out.println("Functions.fGetUrlFitsSpectrum->run");
		String link, plate,mjd, fiber,rerun,release,survey,oplate,ofiber;
		link = "";
		plate = "";
		mjd = "";
		fiber = "";
		rerun = "";
		release = "";
		survey = "";
		oplate = "";
		ofiber = "";
		StringBuilder aql = new StringBuilder();
		aql.append("SELECT p.run2d, p.run1d, p.survey, p.mjd, p.plate, s.fiberID ");
		aql.append(" FROM PlateX AS p JOIN (SELECT * FROM SpecObjAll WHERE specObjID="+specObjID+") AS s ON p.plateID=s.plateID");
		ExQuery ex = new ExQuery();
		ResultSet rs = null;
		try { 
			System.out.println("Functions.fGetUrlFitsSpectrum-->aql1: SELECT value FROM SiteConstants WHERE name='DataServerURL' ");
			rs = ex.aqlQuery(" SELECT value FROM SiteConstants WHERE name='DataServerURL' ");
			if(rs!=null && !rs.isAfterLast())
			{
				link = rs.getString("value");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		try { 
			System.out.println("Functions.fGetUrlFitsSpectrum-->aql2: SELECT value FROM SiteConstants WHERE name='Release' ");
			rs = ex.aqlQuery(" SELECT value FROM SiteConstants WHERE name='Release' ");
			if(rs!=null && !rs.isAfterLast())
			{
				release = rs.getString("value");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		try{
			System.out.println("Functions.fGetUrlFitsSpectrum-->"+aql.toString());
			rs = ex.aqlQuery(aql.toString());
			if(rs!=null && !rs.isAfterLast())
			{
				String run2d = rs.getString("run2d");
				String run1d = rs.getString("run1d");
				if(run2d==null || run2d.isEmpty())
					rerun = run1d;
				else
					rerun = run2d;
				survey = rs.getString("survey");
				mjd = rs.getLong("mjd")+"";
				plate = rs.getLong("plate")+"";
				fiber = rs.getLong("fiberID")+"";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(!"boss".equals(survey))
			survey = "sdss";
		oplate = "0000".substring(0,4-plate.length()) + plate;
		ofiber = "0000".substring(0,4-fiber.length()) + fiber;
		link += "sas/dr" + release + "/" + survey + "/spectro/redux/" +
		rerun + "/spectra/" + oplate + "/spec-" + oplate + "-" + 
		mjd + "-" + ofiber + ".fits";
		return link;
	}
	public static double fDistanceArcMinEq(double ra1, double dec1, double ra2 , double dec2)
	{
		double d2r, nx1, ny1, nz1, nx2, ny2, nz2;
		d2r = Math.PI/180.0;
		nx1 = Math.cos(dec1*d2r)*Math.cos(ra1*d2r);
		ny1 = Math.cos(dec1*d2r)*Math.sin(ra1*d2r);
		nz1 = Math.sin(dec1*d2r);
		nx2 = Math.cos(dec2*d2r)*Math.cos(ra2*d2r);
		ny2 = Math.cos(dec2*d2r)*Math.sin(ra2*d2r);
		nz2 = Math.sin(dec2*d2r);
		
		return ( 2*DEGREES(Math.asin(Math.sqrt(Math.pow(nx1-nx2,2)+Math.pow(ny1-ny2,2)+Math.pow(nz1-nz2,2))/2))*60);
		
	}
	/**
	 * 弧度变角度 :180/π×弧度
	 * 角度转弧度 :π/180×角度
	 */
	private static double DEGREES(double rad) {
		return 180/Math.PI*rad;
	}
	public static void main(String[] args) {
		ExQuery ex = new ExQuery();
		ResultSet rs = null;
		String aql = "SELECT q.objID , m.rmin, m.rmax ,m.cmin ,m.cmax, m.span  FROM AtlasOutline AS m JOIN (SELECT min(f.objID) AS objID  FROM AtlasOutline AS o JOIN (SELECT ra,dec,objID  FROM PhotoPrimary  WHERE htmID BETWEEN 16492674416640 AND 17592186044415 AND pow(0.5371682317808762-cx,2)+pow(0.8434743275522294-cy,2)+pow(0.0011616908888386446-cz,2)<1.740325247409794E-5  AND r<=23.5) AS f  ON f.objID=o.objID GROUP BY o.rmin,o.rmax,o.cmin,o.cmax ) AS q  ON m.objID=q.objID";
		String aql2="SELECT * FROM(  SELECT fieldID,pow(0.537168231780872-cx,2)+pow(0.8434743275522294-cy,2)+pow(0.0011616908888386443-cz,2) AS orderCol  FROM Frame  WHERE htmID BETWEEN 16492674416640 AND 17592186044415 AND zoom=0 AND pow(0.5371682317808762-cx,2)+pow(0.8434743275522294-cy,2)+pow(0.0011616908888386443-cz,2) < 6.793415086819679E-6 ) ORDER BY orderCol";
		String aql3 = "SELECT min(f.objID) AS objID  FROM AtlasOutline AS o JOIN (SELECT ra,dec,objID  FROM PhotoPrimary  WHERE htmID BETWEEN 16492674416640 AND 17592186044415 AND pow(0.5371682317808762-cx,2)+pow(0.8434743275522294-cy,2)+pow(0.0011616908888386446-cz,2)<1.740325247409794E-5  AND r<=23.5) AS f  ON f.objID=o.objID GROUP BY o.rmin,o.rmax,o.cmin,o.cmax ";
		try {
			rs = ex.aqlQuery(aql3);
			String str = "SELECT m.objID, m.rmin, m.rmax ,m.cmin ,m.cmax, m.span  FROM AtlasOutline AS m where ";
			while(!rs.isAfterLast())
			{
				
				str+="objID="+rs.getLong("objID")+" OR ";
				rs.next();
			}
			rs = ex.aqlQuery(str.substring(0,str.lastIndexOf("OR")));
			System.out.println("resultset :"+rs.getLong("objID"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
