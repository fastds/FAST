package edu.gzu.image;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

import org.fastds.dao.ExQuery;

import edu.gzu.domain.Obj;

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

    /// <summary>
    /// getFields. Display the outlines of the Fields on the canvas.
    /// Requires getFrames() to be run beforehand.
    /// </summary>
    void getFields(Hashtable<Long,Coord> cTable)
    {
    	System.out.println("getFields------------");
    	System.out.println("ctable:"+cTable.size());
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
    }


    /// <summary>
    /// getObjects(). Mark Photo|Spec|Target objects according to the drawing option string.
    /// </summary>
    void getObjects(boolean drawPhotoObjs ,boolean drawSpecObjs , boolean drawTargetObjs)
    {
        byte flag = 0;
        if (drawPhotoObjs) flag |= SdssConstants.pflag;
        if (drawSpecObjs) flag |= SdssConstants.sflag;
        if (drawTargetObjs) flag |= SdssConstants.tflag;
        /*
         * select * from dbo.fGetObjectsEq(1,195.035290810573,2.56797342011648,0.5975553875436449,0);
         * */
//        StringBuilder sQ = new StringBuilder(" select *");
//        sQ.append(" from dbo.fGetObjectsEq("+flag+","+ra+","+dec+","+radius+","+zoom+")");
        List<Obj> list = Functions.fGetObjectsEq(flag, ra, dec, radius, zoom);
        //    test    System.out.println(sQ.toString());
        //       zoe SqlDataReader reader = null;
        try
        {
//     zoe       SqlCommand cmd = new SqlCommand(sQ.toString(), this.SqlConn);
        	double oRa, oDec;
            byte oFlag;
            System.out.println("listSize:"+list.size());
        	for(int i = 0;i<list.size();i++)
            {
        		Obj obj = list.get(i);
                oRa = obj.getRa();
                oDec = obj.getDec();
                oFlag = (byte)obj.getFlag();
                System.out.println("drawSpecObjs && (oFlag & SdssConstants.sflag) > 0..."+(drawSpecObjs && (oFlag & SdssConstants.sflag) > 0));
                System.out.println("drawPhotoObjs && (oFlag & SdssConstants.pflag) > 0..."+(drawPhotoObjs && (oFlag & SdssConstants.pflag) > 0));
                System.out.println("drawTargetObjs && (oFlag & SdssConstants.tflag) > 0..."+(drawTargetObjs && (oFlag & SdssConstants.tflag) > 0));
            	if (drawSpecObjs && (oFlag & SdssConstants.sflag) > 0)
  	              canvas.drawSpecObj(oRa, oDec, size);
            	if (drawPhotoObjs && (oFlag & SdssConstants.pflag) > 0)
  	              canvas.drawPhotoObj(oRa, oDec, size);
  	          	if (drawTargetObjs && (oFlag & SdssConstants.tflag) > 0)
  	              canvas.drawTargetObj(oRa, oDec, size);
            }
	          
//  zoe..          reader = cmd.ExecuteReader();					// invoke fGetObjectsEq()
//            while (reader.Read())
//            {
//                oRa = Convert.ToDouble(reader[0]);		// get ra
//                oDec = Convert.ToDouble(reader[1]);		// get dec
//                oFlag = Convert.ToByte(reader[2]);		    // get flag												
//                if (drawSpecObjs && (oFlag & SdssConstants.sflag) > 0)
//                    canvas.drawSpecObj(oRa, oDec, size);
//                if (drawPhotoObjs && (oFlag & SdssConstants.pflag) > 0)
//                    canvas.drawPhotoObj(oRa, oDec, size);
                //canvas.drawApogeeObj(oRa,oDec,size);
//                if (drawTargetObjs && (oFlag & SdssConstants.tflag) > 0)
//                    canvas.drawTargetObj(oRa, oDec, size);
//            }
            
        }
        catch (Exception e)
        {
            try {
				showException("getObjects() [Photo|Spec|Target]","aql-->fGetObjecsEq", e);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
//        finally { try { if (reader != null) reader.Close(); } catch (Exception e) { } }
    }


//    /**
//     *  APOGEE objects for 2mass , this needs to be updated once we get final twomass data
//     */
//    void getApogeeObjects()        
//    {  
//        StringBuilder sq1 = new StringBuilder();
//        try
//        {
//            //canvas.drawLabel("Here Are Apgee:"+radius);
//            sq1.append("select ra,dec from dbo.fGetNearbyApogeeStarEq ({0},{1},{2})", ra,dec,radius);                
//            SqlCommand cmd = new SqlCommand(sq1.toString(), SqlConn);
//            double oRa, oDec;
//            SqlDataReader sReader = cmd.ExecuteReader();					// invoke fGetObjectsEq()               
//            while (sReader.Read())
//            {
//                oRa = Convert.ToDouble(sReader[0]);		// get ra
//                oDec = Convert.ToDouble(sReader[1]);		// get dec
//                canvas.drawApogeeObj(oRa, oDec, size);
//            }
//            if (sReader != null) sReader.Close();
//        }
//        catch (Exception e)
//        {
//            showException("getApogee Objects", sq1.toString(), e);
//        }
//    }		old

   

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

//        StringBuilder sQ = new StringBuilder("SELECT \n");
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
        StringBuilder sQ = new StringBuilder();
        sQ.append(" SELECT q.objID AS fieldID, m.rmin, m.rmax ,m.cmin ,m.cmax, m.span ");
        sQ.append(" FROM "+SdssConstants.getOutlineTable()+" AS m JOIN ");
        sQ.append(" (SELECT min(f.objID) AS objID ");
        sQ.append(" FROM "+SdssConstants.getOutlineTable()+" AS o JOIN");
        sQ.append(" ("+subAql+") AS f ");
        sQ.append(" ON f.objID=o.objID GROUP BY o.rmin,o.rmax,o.cmin,o.cmax ) AS q ");
        sQ.append(" ON m.objID=q.objID");
        System.out.println("OverlayOptions:getOutlines()---->aql:"+sQ.toString());
        ResultSet rs = null;
        ExQuery exQuery = new ExQuery();
        
        try
        {
        	rs = exQuery.aqlQuery(sQ.toString());
            Long fieldid;
            StringBuilder span;
            double rmin, rmax, cmin, cmax;
            Coord fc=null;
            
            while (!rs.isAfterLast())							// read the next record in the dataset
            {
//                fieldid = Convert.ToString(reader[0]);
//                rmin = Convert.ToDouble(reader[1]) * SdssConstants.getOutlinePix();
//                rmax = Convert.ToDouble(reader[2]) * SdssConstants.getOutlinePix();
//                cmin = Convert.ToDouble(reader[3]) * SdssConstants.getOutlinePix();
//                cmax = Convert.ToDouble(reader[4]) * SdssConstants.getOutlinePix();
//
//                span = new StringBuilder("\"" + Convert.ToString(reader[5]) + "\"");
//                fc = (Coord)cTable[fieldid];
            	
            	fieldid = (rs.getLong("fieldID") & 0xFFFFFFFFFFFF0000L);
            	rmin = rs.getDouble("rmin");
            	rmax = rs.getDouble("rmax");
	            cmin = rs.getDouble("cmin");
	            cmax = rs.getDouble("cmax");
	            span = new StringBuilder("\"" + rs.getString("span") + "\"");
                fc =  cTable.get(fieldid);
                System.out.println("drawBoundingBox--:"+drawBoundingBox);
                System.out.println("drawOutline--:"+drawOutline);
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
        	System.out.println(e.getMessage());
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
	 	aql.append(" FROM ("+res+") AS f, Mask AS m ");
	 	aql.append(" WHERE (m.type = 2) ");
	 	aql.append(" OR (m.type=0 OR m.type=1 OR m.type=3 AND m.filter=2) ");
	 	aql.append(" OR (m.type = 4 and m.filter = 2 and m.seeing > 1.7 ) AND f.objID = m.maskID ");
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

    /// <summary>
    /// getLabel. Display the standard label on the image.
    /// </summary>
    void getLabel(String sDataRelease, double scale, double imageScale)
    {
    	String zoomRatio = (zoom >= 1) ? ("1:" + (int)(Math.pow(4, zoom))) : ((int)(Math.pow(imageScale, 2.0) + .5) + ":1");
//  zoe  	String theLabel = String.Format("SDSS {0} \nra: {1:N3} dec: {2:N3}\nscale: {3:N4} arcsec/pix\n"
//            + "image zoom: " + zoomRatio, sDataRelease, ra, dec, scale);
       String theLabel = "SciDB "+sDataRelease+" \nra: "+ra+" dec: "+dec+"\nscale: "+scale+" arcsec/pix\n"
             + "image zoom: " + zoomRatio;
    	canvas.drawLabel(theLabel);
    }

    /// <summary>
    /// Assemble a generic message and throw the Exception
    /// </summary>
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
	//16,337.082,-0.945,28.68265860209495+89.4,1
	   Functions.fGetObjectsEq(16,337.082,-0.945,118.08265860209495,0);
}

}
