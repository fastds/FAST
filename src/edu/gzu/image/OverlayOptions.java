package edu.gzu.image;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import org.fastds.dao.ExQuery;
import org.fastds.dao.ExplorerDao;
import org.fastds.service.ExplorerService;

import edu.gzu.domain.Obj;
import edu.gzu.image.sphericalhtm.Pair;

public class OverlayOptions
{
// zoe   private SqlConnection SqlConn = null;
	private Connection SqlConn  = null;
    public SDSSGraphicsEnv canvas = null;
    double ra ;
    double dec;
    float size;
    double radius;
    double fradius;
    int zoom;

    public OverlayOptions(Connection sqlcon, SDSSGraphicsEnv canvas, float size, double ra, double dec, double radius, int zoom, double fradius)
    {
        this.SqlConn = sqlcon;
        this.canvas = canvas;
        this.ra = ra;
        this.dec = dec;
        this.size = size;
        this.radius = radius;
        this.zoom = zoom;
        this.fradius = fradius;
    }

    /**
     * getFields. Display the outlines of the Fields on the canvas.
     * Requires getFrames() to be run beforehand.
     */
    void getFields(Hashtable<Long,Coord> cTable)
    {
    	System.out.println("getFields------in------");
    	System.out.println("ctable size:"+cTable.size());
        try
        {
            for (Long key: cTable.keySet())
            {
                canvas.drawField((Coord)cTable.get(key));
            }
        }
        catch (Exception e)
        {
            try {
				showException("getFields()", "", e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
    }


    /**
     *  根据绘制选项，标记 Photo|Spec|Target 对象
     */
    void getObjects(boolean drawPhotoObjs ,boolean drawSpecObjs , boolean drawTargetObjs)
    {
        byte flag = 0;
        if (drawPhotoObjs) flag |= SdssConstants.pflag;
        if (drawSpecObjs) flag |= SdssConstants.sflag;
        if (drawTargetObjs) flag |= SdssConstants.tflag;
//        StringBuilder sQ = new StringBuilder(" select *");
//        sQ.append(" from dbo.fGetObjectsEq("+flag+","+ra+","+dec+","+radius+","+zoom+")");
        List<Obj> list = Functions.fGetObjectsEq(flag, ra, dec, radius, zoom);
        try
        {
        	double oRa, oDec;
            byte oFlag;
            System.out.println("listSize:"+list.size());
        	for(int i = 0;i<list.size();i++)
            {
//    以前    		Obj obj = list.get(i);
//                oRa = obj.getRa();
//                oDec = obj.getDec();
//                oFlag = (byte)obj.getFlag();
            	if (drawSpecObjs && ((byte)list.get(i).getFlag() & SdssConstants.sflag) > 0)
  	              canvas.drawSpecObj(list.get(i).getRa(), list.get(i).getDec(), size);
            	if (drawPhotoObjs && ((byte)list.get(i).getFlag() & SdssConstants.pflag) > 0)
  	              canvas.drawPhotoObj(list.get(i).getRa(), list.get(i).getDec(), size);
  	          	if (drawTargetObjs && ((byte)list.get(i).getFlag() & SdssConstants.tflag) > 0)
  	              canvas.drawTargetObj(list.get(i).getRa(), list.get(i).getDec(), size);
            }
	          
            
        }
        catch (Exception e)
        {
            try {
				showException("getObjects() [Photo|Spec|Target]","aql-->fGetObjecsEq", e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
    }


    /**
     * getOutlines. Display the bounding boxes and outlines of photoObj.
     * @param drawBoundingBox
     * @param drawOutline
     * @param cTable
     */
    void getOutlines(boolean drawBoundingBox, boolean drawOutline,Hashtable<Long,Coord> cTable)
    {
    	System.out.println("OverlayOptions:getOutlines().........");
        if (!SdssConstants.isSdss) {
//            canvas.drawWarning(" No Outlines/Bounding Boxes available for twomass !");
            return;
        }
        //@Deoyani A workaround for the outlines exception problem
        if (radius > 64)
        {
//            canvas.drawWarning(" No Outlines for this zoom level/scale !");
            return;
        };

//   old     StringBuilder sQ = new StringBuilder("SELECT \n");
//        sQ.append("	(q.objid & 0xFFFFFFFFFFFF0000) as fieldid,\n");
//        sQ.append("	m.rmin, m.rmax, m.cmin, m.cmax, m.span\n from ");
//        sQ.append(SdssConstants.getOutlineTable());
//        sQ.append(" m \n JOIN (select min(f.objid) as objid \n");
//        sQ.append(" from dbo.fGetObjectsEq("+SdssConstants.pflag+", "+ra+", "+dec+", "+radius+", "+zoom+") f JOIN \n	");
//        sQ.append(SdssConstants.getOutlineTable());
//        sQ.append(" o \nwith (nolock)\n");
//        sQ.append(" ON f.objid=o.objid  group by rmin,rmax,cmin,cmax ) q\n");
//        sQ.append(" ON m.objid=q.objid");
        
        String subAql = Functions.fGetObjectsEqStr(SdssConstants.pflag, ra, dec, radius, zoom);
        StringBuilder aqlOne = new StringBuilder();
//        aql.append(" SELECT q.objID , m.rmin, m.rmax ,m.cmin ,m.cmax, m.span ");
//        aql.append(" FROM "+SdssConstants.getOutlineTable()+" AS m JOIN ");
//        aql.append(" (SELECT min(f.objID) AS objID ");
//        aql.append(" FROM "+SdssConstants.getOutlineTable()+" AS o JOIN");
//        aql.append(" ("+subAql+") AS f ");
//        aql.append(" ON f.objID=o.objID GROUP BY o.rmin,o.rmax,o.cmin,o.cmax ) AS q ");
//        aql.append(" ON m.objID=q.objID");
        aqlOne.append(" SELECT min(f.objID) AS objID  FROM AtlasOutline AS o ");
        aqlOne.append(" JOIN ("+subAql+") AS f ");
        aqlOne.append(" ON f.objID=o.objID GROUP BY o.rmin,o.rmax,o.cmin,o.cmax ");
        System.out.println("OverlayOptions:getOutlines()---->aqlOne:"+aqlOne.toString());
        
        ExQuery ex = new ExQuery();
        ResultSet rs = null;
        StringBuilder aqlTwo = new StringBuilder();
        aqlTwo.append("SELECT m.objID, m.rmin, m.rmax ,m.cmin ,m.cmax, m.span ");
        aqlTwo.append(" FROM AtlasOutline AS m WHERE ");
        try {
			rs = ex.aqlQuery(aqlOne.toString());
			while(!rs.isAfterLast())
			{
				
				aqlTwo.append("objID="+rs.getLong("objID")+" OR ");
				rs.next();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
        try
        {
        	System.out.println("OverlayOptions:getOutlines()---->aqlTwo:"+aqlTwo.toString());
        	int index = aqlTwo.lastIndexOf("OR");
        	if(index == -1)
        		return;
        	rs = ex.aqlQuery(aqlTwo.substring(0,index));
            Long fieldid;
            StringBuilder span = null;
            long rmin, rmax, cmin, cmax;
            Coord fc=null;
            while (!rs.isAfterLast())		// read the next record in the dataset
            {
//                fieldid = Convert.ToString(reader[0]);
//                rmin = Convert.ToDouble(reader[1]) * SdssConstants.getOutlinePix();
//                rmax = Convert.ToDouble(reader[2]) * SdssConstants.getOutlinePix();
//                cmin = Convert.ToDouble(reader[3]) * SdssConstants.getOutlinePix();
//                cmax = Convert.ToDouble(reader[4]) * SdssConstants.getOutlinePix();
//
//                span = new StringBuilder("\"" + Convert.ToString(reader[5]) + "\"");
//                fc = (Coord)cTable[fieldid];
            	fieldid = (rs.getLong("objID") & 0xFFFFFFFFFFFF0000L);
            	rmin = rs.getLong("rmin") * SdssConstants.getOutlinePix();
            	rmax = rs.getLong("rmax") * SdssConstants.getOutlinePix();
	            cmin = rs.getLong("cmin") * SdssConstants.getOutlinePix();
	            cmax = rs.getLong("cmax") * SdssConstants.getOutlinePix();
	            span = new StringBuilder("\"" + rs.getString("span") + "\"");
	            System.out.println("fieldid:"+fieldid);
	            System.out.println("rmin:"+rmin);
	            System.out.println("rmax:"+rmax);
	            System.out.println("cmin:"+cmin);
	            System.out.println("cmax:"+cmax);
	            System.out.println("span:"+span);
                fc =  cTable.get(fieldid);
                System.out.println("cTable have?"+cTable.containsKey(fieldid));
                System.out.println("cTable size?"+cTable.size());
                System.out.println("fc::"+fc);
	            if (drawBoundingBox)
                {
                    canvas.drawBoundingBox(fc, cmin, cmax, rmin, rmax);
                }
                if (drawOutline)
                {
                    canvas.drawOutline(fc, span);
                }
                rs.next();
            }
        }
        catch (Exception e)
        {
        	e.printStackTrace();
//            showException("getOutlines()", sQ.toString(), e);
        }
    }
    /**
     * getMasks. Display the typical Masks intersecting with the canvas.
     */
     void getMasks()
    {
//        StringBuilder sQ = new StringBuilder(" select m.area \n");
//        sQ.AppendFormat(
//            " from dbo.fGetObjectsEq({0},{1},{2},{3},{4}) f JOIN Mask m with (nolock)\n",
//            SdssConstants.mflag, ra, dec, fradius, zoom);
//        sQ.Append(" ON f.objid = m.maskid \n WHERE ( \n");
//        sQ.Append(" (m.type = 2) \n");
//        sQ.Append(" or (m.type in (0,1,3) and  m.filter = 2) \n");
//        sQ.Append(" or (m.type = 4 and m.filter = 2 and m.seeing > 1.7 ) )");
//        SqlCommand cmd = new SqlCommand(sQ.toString(), SqlConn);
	 	String res = Functions.fGetObjectsEqStr(SdssConstants.mflag, ra, dec, fradius, zoom);
	 	StringBuilder aql = new StringBuilder();
	 	aql.append(" SELECT m.area ");
	 	aql.append(" FROM ("+res+") AS f JOIN Mask AS m ");
	 	aql.append(" ON f.objID = m.maskID WHERE (m.type = 2) ");
	 	aql.append(" OR ((m.type=0 OR m.type=1 OR m.type=3) AND m.filter=2) ");
	 	aql.append(" OR (m.type = 4 and m.filter = 2 and m.seeing > 1.7 ) ");
	 	System.out.print("drawMask-->aql:"+aql.toString());
	 	ExQuery exQuery = new ExQuery();
	 	ResultSet rs = null;
        try
        {
//   zoe+         SqlDataReader reader = cmd.ExecuteReader();					// invoke fGetObjectsEq
//            while (reader.Read())							// read the next record in the dataset
//            {
//                StringBuilder area = new StringBuilder(Convert.ToString(reader[0]));
//                canvas.drawMask(area);
//            }
//            if (reader != null) reader.Close();				// close the reader.
        	rs = exQuery.aqlQuery(aql.toString());
        	while(!rs.isAfterLast())
        	{
        		StringBuilder area = new StringBuilder(rs.getString("area"));
        		canvas.drawMask(area);
        		rs.next();
        	}
        	
        }
        catch (Exception e)
        {
//            showException("getMasks()", sQ.toString(), e);
        	e.printStackTrace();
        }
        finally{
        	if(rs!=null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
        

    }

     /**
      * getLabel. Display the standard label on the image.
      */
    void getLabel(String sDataRelease, double scale, double imageScale)
    {
    	String zoomRatio = (zoom >= 1) ? ("1:" + (int)(Math.pow(4, zoom))) : ((int)(Math.pow(imageScale, 2.0) + .5) + ":1");
//  zoe  	String theLabel = String.Format("SDSS {0} \nra: {1:N3} dec: {2:N3}\nscale: {3:N4} arcsec/pix\n"
//            + "image zoom: " + zoomRatio, sDataRelease, ra, dec, scale);
       String theLabel = "SciDB "+sDataRelease+" \nra: "+ra+" dec: "+dec+"\nscale: "+scale+" arcsec/pix\n"
             + "image zoom: " + zoomRatio;
    	canvas.drawLabel(theLabel);
    }

    /**
     * Assemble a generic message and throw the Exception
     */
    public void showException(String sFunction, String sQuery, Exception e) throws Exception
    {
        StringBuilder msg = new StringBuilder();
        msg.append(sFunction+" has failed:\n"+sQuery+"\n");
        msg.append("Exception Message:"+ e.getMessage());
        throw new Exception(msg.toString()); //Actual exception
    }


    /**
     * getPlates. Display the outlines of the plates intersecting the canvas.
     */
   void getPlates()
    {
// zoe       StringBuilder sQ = new StringBuilder(" select ra, dec ");
//        sQ.AppendFormat(" from dbo.fGetObjectsEq({0},{1},{2},{3},{4}) ",
//        SdssConstants.plateflag, ra, dec, 2 * radius + SdssConstants.plateRadiusArcMin, zoom);
//        SqlCommand cmd = new SqlCommand(sQ.toString(), SqlConn);
//        try
//        {
//            double oRa, oDec, oRadius;						//					
//            SqlDataReader reader = cmd.ExecuteReader();					// invoke fGetObjectsEq
//            while (reader.Read())							// read the next record in the dataset
//            {
//                oRa = Convert.ToDouble(reader[0]);		// get ra
//                oDec = Convert.ToDouble(reader[1]);		// get dec
//                oRadius = SdssConstants.plateRadiusArcMin;	// plate radius																											
//                canvas.drawPlate(oRa, oDec, oRadius);
//            }
//            if (reader != null) reader.Close();				// close the reader.
//        }
//        catch (Exception e)
//        {
//            showException("getPlates()", sQ.ToString(), e);
	   
//        }
	   System.out.println("zhizhizhi:"+SdssConstants.plateflag+","+ra+","+dec+","+(2 * radius + SdssConstants.plateRadiusArcMin)+","+zoom);
	   List<Obj> objList = Functions.fGetObjectsEq(SdssConstants.plateflag, ra, dec, 2 * radius + SdssConstants.plateRadiusArcMin, zoom);
	   System.out.println("OverlayOptions:getPlates()...run..objList:size():"+objList.size());
	   for(Obj obj : objList)
	   {
		   canvas.drawPlate(obj.getRa(), obj.getDec(), SdssConstants.plateRadiusArcMin);
	   }
    }
   public static void main(String[] args) {
	   /**
	    * HtmIDStart           HtmIDEnd
			-------------------- --------------------
			8796093022208        8796093087743
			8796093239296        8796093243391
			8796093259776        8796093263871
			8796093267968        8796093272063
			12644383719424       12644383784959
			12644383936512       12644383940607
			12644383956992       12644383961087
			12644383965184       12644383969279
			13194139533312       13194139598847
			13194139750400       13194139754495
			13194139770880       13194139774975
			13194139779072       13194139783167
			17042430230528       17042430296063
			17042430447616       17042430451711
			17042430468096       17042430472191
			17042430476288       17042430480383
				    */
	  List<Pair> list = Functions.htmCoverCircleXyz(1, 0, 0, 1);
	  for(Pair p :list)
		  System.out.println(p.getLo()+","+p.getHi());
}

}
