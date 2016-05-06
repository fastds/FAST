package edu.gzu.image;

import java.awt.geom.Point2D;

//#define DR2PATCH
///Current version
///ID:          $Id: SdssConstants.cs,v 1.10 2007/07/23 17:10:30 nieto Exp $
///Revision:    $Revision: 1.10 $
///Date:        $Date: 2007/07/23 17:10:30 $
	/// <summary>
	/// The SdssConstants class holds all constants related to SDSS.
	/// </summary>
public class SdssConstants
{
	/// <summary>
	/// Revision from CVS
	/// </summary>
	public static String getRevision()
	{
		return "$Revision: 2.00 $";	
	}
	public final static double FrameHalfDiag = 8.4F;			            // arc mins			      
	public final static double	 plateRadiusArcMin = 89.4;					// arcMinute
	public final double	 plateRadiusDeg	= 1.49;					    // degrees        
	public final static byte	 sflag		    = 1;						// specObject
	public final static byte	 pflag		    = 2;						// photoObject
	public final static byte	 tflag		    = 4;						// targetObject
	public final static byte	 mflag		    = 8;						// maskObject
	public final static byte	 plateflag	    = 16;						// plateObject

 //public const string Twomass = "2Mass";
 //public const string sdss = "sdss";
 //public static string survey = "sdss";
 public static Boolean isSdss = true;


 private static String _DataRelease = "DR9";
   //zoe      ConfigurationManager.AppSettings["DataRelease"].ToUpper();

 private static int _DR = -1;
 private static int _prevVersion = 7; /// Till dr7 some constants were different

 public SdssConstants()
	{
		// TODO: Add constructor logic here
		}

   

     public static float getFrameHeight(){
         if (isSdss)
         {
             return 1489.0F; // For SDSS
     }
     else
     {
         return 1024.0F; // 2mass height

     }
 
 }

 public static float getFrameWidth()
 {
     if (isSdss)
     {

         return 2048.0F; // For SDSS
     }
     else
     {
         return 512.0F; // 2mass 
     }
   
 }

 public static int getPixelsPerDegree()
 {        
         if (isSdss)
         {
             return 9089; // For SDSS
         }
         else
         {
             return 5120; // 2mass
         }            
 }
 
 public static double getPixelsPerArcMin()
 {
         return getPixelsPerDegree() / 60.0;
 }


 public static int getPrevVersion() {
         return _prevVersion;
 }

 public static int getMaxZoom()
 {
         if (isSdss)
         {
             if (getSDR() > getPrevVersion())
                 return 4;
             else
                 return 7;
         }
         else {
             return 1;
         }
 }

 public static int zoom10(int zoom)
 {
     if (getSDR() > getPrevVersion())
     {
         int z10;
         switch (zoom)
         {
             case 0:  z10 =  0; break;
             case 1:  z10 = 50; break;
             case 2:  z10 = 25; break;
             default: z10 = 12; break;
         }
         return z10;
     }
     else
     {
         return (zoom > 3) ? 30 : zoom * 10;
     }

 }

 public static String getOutlineTable()
 {
         if (getSDR() > getPrevVersion())
             return "AtlasOutline";
         else
             return "ObjMask";
 }

 public static int getOutlinePix()
 {
         if (getSDR() > getPrevVersion())
             return 1;
             //return 4;
         else
             return 4;
 }

 public static int getOutlineOff()
 {
         if (getSDR() > getPrevVersion())
             return 1;
         else
             return 0;
 }

 
 
 private static float getClipXLeft()
 {
         if (getSDataRelease().equals("DR1"))
             return 32.0F;                   // Alex: I think it is 32  (was 48) !!!
         else if  (getSDR() > getPrevVersion())//(sDataRelease.Equals("DR10"))
             return 32.0F;
         else
             return 24.0F;
 }

 private static float getClipXRight()
 {
         if (getSDataRelease().equals("DR1"))
             return 32.0F;                   // Alex: I think it is 32  (was 48) !!!
         else if (getSDR() >getPrevVersion())//(sDataRelease.Equals("DR10"))
             return 32.0F;
         else
             return 40.0F;
 }

 private static float getClipYTop()
 {
         //if (sDataRelease.Equals("DR10"))
         //    return 33.0F;

         //else if (sDR > prevVersion)
         //    return 48.0F;

         if (getSDR()>getPrevVersion())
             return 33.0F;
         else
             return 64.0F;
 }
 private static float getClipYBottom()
 {
         if (getSDR() > getPrevVersion())
             return 32.0F;
         else
             return 64.0F;
 }

 /// <summary>
 /// sDataRelease. String version of the DR version, or NULL if invalid
 /// </summary>
 public static String getSDataRelease ()
 {
         if(
             (_DataRelease=="EDR") ||
             (_DataRelease=="DR1") ||
             (_DataRelease=="DR2") ||
             (_DataRelease=="DR3") ||
             (_DataRelease=="DR4") ||
             (_DataRelease=="DR5") ||
             (_DataRelease=="DR6") ||
             (_DataRelease=="DR7") ||
             (_DataRelease=="DR8") ||
             (_DataRelease=="DR9") ||
             (_DataRelease=="DR10") ||
             (_DataRelease == "DR11") ||
             (_DataRelease == "DR12") 
           ) return _DataRelease;
         return null;
 }

 /**
  * sDataRelease. String version of the DR version, or NULL if invalid
  */
 public static int getSDR()
 {
         if (getSDataRelease().equals("EDR"))
             _DR = 0;                
         else
           //zoe  _DR = Integer.Parse(getSDataRelease().Remove(0,2));
        	 _DR=12;
         
         if (
             (_DR == 0) ||
             (_DR == 1) ||
             (_DR == 2) ||
             (_DR == 3) ||
             (_DR == 4) ||
             (_DR == 5) ||
             (_DR == 6) ||
             (_DR == 7) ||
             (_DR == 8) ||
             (_DR == 9) ||
             (_DR == 10) ||
             (_DR == 11) ||
             (_DR == 12)
           ) return _DR;
         return -1;
 }
	/// <summary>
	/// FieldGeometry. Returns the four corners of the field
	/// in unscaled pixel coordinates with optional clipping
	///  </summary>
	public static Point2D.Float[]	FieldGeometry(boolean clip )
	{
		float fX1, fX2, fY1, fY2;
     //-----------------------------------------------------------------
	 // compute the pixel coordinates of corners with optional clipping
     //-----------------------------------------------------------------
     if (!isSdss) clip = false;  // no clipping for twomass images
     int clipit	= (clip?1:0);
     float offY = -0.0F;
     fX1 = (float) (getClipXLeft() * clipit);				
	 fX2	= (float) (getFrameWidth() - getClipXRight() * clipit);							
	 fY1	= (float) (getClipYTop() * clipit +offY);
	 fY2	= (float) (getFrameHeight() - getClipYBottom() * clipit +offY);

		//-----------------------------------
     // create array for the four corners
     //-----------------------------------
		Point2D.Float[] p = new Point2D.Float[4];

     if (getSDR() > getPrevVersion())
     {
         p[0] = new Point2D.Float(fX1, fY2);
         p[1] = new Point2D.Float(fX2, fY2);
         p[2] = new Point2D.Float(fX2, fY1);
         p[3] = new Point2D.Float(fX1, fY1);
     }
     else {
         p[0] = new Point2D.Float(fX1, fY1);
         p[1] = new Point2D.Float(fX2, fY1);
         p[2] = new Point2D.Float(fX2, fY2);
         p[3] = new Point2D.Float(fX1, fY2);
     }
     return p;
	}
}	
