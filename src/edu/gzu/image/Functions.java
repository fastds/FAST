package edu.gzu.image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.fastds.dao.ExQuery;
import org.junit.Test;

import edu.gzu.domain.Obj;
import edu.gzu.domain.PhotoObjAll;
import edu.gzu.image.sphericalhtm.Cover;
import edu.gzu.image.sphericalhtm.Pair;
import edu.gzu.image.sphericalhtm.Trixel;
import edu.gzu.image.sphericallib.Cartesian;
import edu.gzu.image.sphericallib.Region;

public class Functions {
	/*
	 * 求弧度
	 */
	public static double radians(double x)
	{
		return x*(Math.PI/180);
	}
	/*
	-------------------------------------------------------------------------------
	--/H Computes the 6-part SDSS numbers from the long objID
	--/T The bit-fields and their lengths are skyversion[5] + rerun[11] + run[16] + camcol[3] + first[1] + field[12] + obj[16]<br>
	--/T <samp> select top 5 dbo.fSDSS(objid) as SDSS from PhotoObj</samp>
	------------------------------------------------------------------------------- 
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
	/*
	-------------------------------------------------------------------------------
	--/H Extracts SkyVersion from an SDSS Photo Object ID
	--/T The bit-fields and their lengths are: Skyversion[5] Rerun[11] Run[16] Camcol[3] First[1] Field[12] Obj[16]<br>
	--/T <samp> select top 10 objId, dbo.fSkyVersion(objId) as fSkyVersion from Galaxy</samp>
	-------------------------------------------------------------------------------
	 */
	public static int skyVersion(long id)
	{
		return (int)(id/(long)Math.pow(2, 59) & 0x0000000F);
	}
	/*
	 -------------------------------------------------------------------------------
	--/H Extracts Run from an SDSS Photo Object ID
	--/T The bit-fields and their lengths are: Skyversion[5] Rerun[11] Run[16] Camcol[3] First[1] Field[12] Obj[16]<br>
	--/T <samp> select top 10 objId, dbo.fRun(objId) as fRun from Galaxy</samp>
	------------------------------------------------------------------------------- 
	 */
	public static int run(long id)
	{
		return (int)(id/(long)Math.pow(2, 32) & 0x0000FFFF);
	}
	/*
	-------------------------------------------------------------------------------
	--/H Extracts Rerun from an SDSS Photo Object ID
	--/T The bit-fields and their lengths are: Skyversion[5] Rerun[11] Run[16] Camcol[3] First[1] Field[12] Obj[16]<br>
	--/T <samp> select top 10 objId, dbo.fRerun(objId) as fRerun from Galaxy</samp>
	------------------------------------------------------------------------------- 
	 */
	public static int rerun(long id)
	{
		return (int)(id/(long)Math.pow(2, 48) & 0x000007FF);
	}
	/*
	-------------------------------------------------------------------------------
	--/H	Extracts Camcol from an SDSS Photo Object ID
	--/T The bit-fields and their lengths are: Skyversion[5] Rerun[11] Run[16] Camcol[3] First[1] Field[12] Obj[16]<br>
	--/T <samp>select top 10 objId, dbo.fCamcol(objId) as fCamcol from Galaxy</samp>
	-------------------------------------------------------------------------------
	 */
	public static int camcol(long id)
	{
		return (int)(id/(long)Math.pow(2, 29) & 0x00000007);
	}
	/*
	-------------------------------------------------------------------------------
	--/H Extracts Field from an SDSS Photo Object ID.
	--/T The bit-fields and their lengths are: Skyversion[5] Rerun[11] Run[16] Camcol[3] First[1] Field[12] Obj[16]<br>
	--/T <samp> select top 10 objId, dbo.fField(objId) as fField from Galaxy</samp>
	------------------------------------------------------------------------------- 
	 */
	public static int field(long id)
	{
		return (int)(id/(long)Math.pow(2, 16) & 0x00000FFF);
	}
	/*
	-------------------------------------------------------------------------------
	--/H Extracts Obj from an SDSS Photo Object ID.
	--/T The bit-fields and their lengths are: Skyversion[5] Rerun[11] Run[16] Camcol[3] First[1] Field[12] Obj[16]<br>
	--/T <samp> select top 10 objId, dbo.fObj(objId) as fObj from Galaxy</samp>
	-------------------------------------------------------------------------------
	 */
	public static int obj(long id)
	{
		return (int)(id & 0x0000FFFF);
	}
	public static List<Pair> fHtmCoverCircleXyz(double x, double y, double z, double radiusArcMin)
	{
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
	    List<Pair> list = Cover.HidRange(reg);
	    List<Pair> list2 = new ArrayList<Pair>();
	    if (list != null)
	    {
	        int count = list.size();
	        for (int i = 0; i < count; i++)
	        {
	            list2.add(new Pair(list.get(i).getLo(), list.get(i).getHi()));
	        }
	    }
	    return list2;
	}
	/**
	 * 
	 * @param ra 赤经
	 * @param dec 赤纬
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
		
		List<Pair> pair = fHtmCoverCircleXyz(nx,ny,nz,radius);
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
	 * param ra 赤经
	 * @param dec 赤纬
	 * @param radius 半径
	 * @param zoom 缩放
	 * @return 得到Frame相关信息（包含星体图像）的AQL语句
	 */
	public static String fGetNearbyFrameEq(double ra,double dec,double radius,int zoom)
	{
			double nx ,ny ,nz;
			nx  =  (Math.cos(Math.toRadians(dec))*Math.cos(Math.toRadians(ra)));
			ny  =  (Math.cos(Math.toRadians(dec))*Math.sin(Math.toRadians(ra)));
			nz  =  Math.sin(Math.toRadians(dec));
			System.out.println("nx="+nx+",ny="+ny+",nz="+nz+",radius="+radius);
			
			List<Pair> list = fHtmCoverCircleXyz(nx,ny,nz,radius);
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
	         nx  =  (Math.cos(radians(dec))*Math.cos(radians(ra)));
	         ny  = Math.cos(radians(dec))*Math.sin(radians(ra));
	         nz  = Math.sin(radians(dec));
	         mag =  25 - 1.5* zoom;  ///-- magnitude reduction.
		        
	         List<Pair> pair = fHtmCoverCircleXyz(nx, ny, nz, rad);
			
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
		System.out.println("flag:"+flag+",flag&1:"+(flag&1)+",flag&2:"+(flag&2)+",flag&4:"+(flag&4)+",flag&8:"+(flag&8)+",flag&16:"+(flag&16)+",flag&32:"+(flag&32));
		    double nx,ny,nz,rad,mag;
		                
			rad = radius;
	        if (rad > 250)  rad = 250 ;     //-- limit to 4.15 degrees == 250 arcminute radius
	         nx  =  (Math.cos(radians(dec))*Math.cos(radians(ra)));
	         ny  = Math.cos(radians(dec))*Math.sin(radians(ra));
	         nz  = Math.sin(radians(dec));
	         mag =  25 - 1.5* zoom;  ///-- magnitude reduction.
		        
	         List<Pair> pair = fHtmCoverCircleXyz(nx, ny, nz, rad);
			
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
					System.out.println("Functions:fGetObjectsEq():aql-->palteX"+aql);
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
	
	
	
	public static String fGetNearbyObjAllXYZ(double nx ,double ny ,double nz ,double r )
	{
		StringBuilder aql = new StringBuilder();
		
		double lim = Math.pow(2*Math.sin(Math.toRadians(r/120)),2);
		double d2r = Math.PI/180.0;
		List<Pair> pair = fHtmCoverCircleXyz(nx,ny,nz,r);
		
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
		// TODO Auto-generated method stub
		return null;
	}
	public static String fPhotoModeN(int value)
	{
		return "SELECT name FROM PhotoMode WHERE value="+ value;
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
				String aql = "select name from PhotoFlags where value="+mask;
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
}
