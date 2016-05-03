package edu.gzu.image;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

//////////////////////////////////等待完善的矩阵类////////////////////////////////////
class PointEq
{
	public double ra,dec;			// the right ascension and declination
	public PointEq(double pRa, double pDec)
	{
		ra	= pRa;
		dec	= pDec;
	}
}
/**
 * Summary description for Coord.
 *  Coord private class carries the transformation of ra/dec to nu/mu and xy. 
 */
public class Coord 
{
	public static String Revision = "$Revision: 1.3 $";


	public double ra,dec;			        // point (ra, dec) in J2000 degrees
	public double mu,nu;			        // point (ra, dec) in J2000 degrees
	public double row, col;			        // the row and column number in the frame
	private float x,y;				        // pixel offsets from upper left corner 
									        // corresponding to (ra,dec), scaled 
	public double scale;			        // image scale 
	private double a,b,c,d,e,f,node,incl;	// affine tranformation of the stripe to ra,dec
	public String info;				        // information about the field for debugging
	public AffineTransform m;
	public static double D2R = Math.PI / 180.0;	// degrees to radians 角度转换为弧度
	public double crval1, crval2, crpix1, crpix2, cdelt1, cdelt2;	// wcs for 2mass


	public Coord()
	{}


	public Coord(double _a,double _b,double _c,double _d,double _e,double _f,
		double _node,double _incl,double _scale, String _info) 
	{
		a = _a;
		b = _b;
		c = _c;
		d = _d;
		e = _e;
		f = _f;
		node = _node;
		incl = _incl;
		scale= _scale;
		info = _info;
		m	 = null;
	}


	/**
	 * The copy method copies the values of the coord parameter into the current coordinate object
	 * 复制coord参数的值到当前coordinate对象中
	 * @param coord 
	 */
	public void copy( Coord coord ) 
	{
		this.a			= coord.a;
		this.b			= coord.b;
		this.c			= coord.c;
		this.d			= coord.d;
		this.e			= coord.e;
		this.f			= coord.f;
		this.node		= coord.node;
		this.incl		= coord.incl;
		this.scale		= coord.scale;
		this.info		= coord.info;
		this.m			= coord.m;
	}


	/**
	 * EqtoScreen goes from equatorial coordinates to xy
	 * Sets ra, dec and consequently xOffset and yOffset of 
	 * Coord according to the astrometric transformation.
	 * 根据天体测量变换，转换赤道坐标为xy集合、ra,dec、和Coord的xOffset和yOffset
	 * @param pRa 赤经，范围：[0°-360]，单位：度
	 * @param pDec 赤纬，范围：[-90,90]，单位：度
	 */
	public Point2D EqToFrame(double pRa, double pDec) 
	{   
		// save the angles 储存角度
		ra	= pRa;
		dec	= pDec;

		// convert angles to radians  角度到弧度的转换
		double inclR = incl*D2R;
		double raR	 = (pRa-node) *D2R;
		double decR	 = pDec*D2R;

		// first go to (mu,nu) 首先变换为(mu,nu)
		double gx	=  Math.cos(raR) * Math.cos(decR);
		double gy	=  Math.sin(raR) * Math.cos(decR);
		double gz	=  Math.sin(decR);

		// reverse rotation by incl around x
		double qx	=  gx;
		double qy	=  gy*Math.cos(inclR) + gz * Math.sin(inclR);            
		double qz	= -gy*Math.sin(inclR) + gz * Math.cos(inclR);

		// compute mu, nu
		mu	=  Math.atan2(qy,qx)/D2R + node;
		nu	=  Math.asin(qz)/D2R;

		// adjust for wraparound of the difference
		double dmu	=  mu - a;
		if (dmu<-180)  dmu += 360;
		if (dmu> 180)  dmu -=360;

		// set up the determinant 建立行列式
		double det	 = b * f - e * c;
		double col   = ( (nu-d)*b - dmu*e )/det;
		double row   = ( dmu*f - (nu-d)*c )/det;

		// compute pixel coordinates 计算像素坐标
		x	= (float)(col/scale);
		y	= (float)(row/scale);
      Point2D p = new Point2D.Float(x,y);
		return p; 
	} 


	/**
	 * FrameToEq goes from Frame x,y coordinates and sets ra and dec
	 * 从Frame x,y坐标开始，设置ra,dec
	 * @param x pixels across (scaled for zoom level)
	 * @param y pixels down (scaled for zoom level)
	 */
	public PointEq FrameToEq(float x, float y)  
	{
		// x,y need to be in original pixel scale x,y需要是最初的像素比例x,y
		double col = x;
		double row = y;

		// Great circle coordinates in degrees
		mu = (a +  b*row  +  c*col);
		nu = (d +  e*row  +  f*col);

		// convert to radians, then to ra,dec
		double muR = (mu - node) * D2R;
		double nuR = nu * D2R;
		double xg  = Math.cos(muR) * Math.cos(nuR);
		double yg  = Math.sin(muR) * Math.cos(nuR);
		double zg  = Math.sin(nuR);

		// compute unit vector
		double inR = incl * D2R;
		double qx  = xg;
		double qy  = yg * Math.cos(inR) - zg*Math.sin(inR);
		double qz  = yg * Math.sin(inR) + zg*Math.cos(inR);

		// compute ra, dec
		double pRa  = Math.atan2(qy,qx) / D2R  +  node;
		double pDec = Math.asin(qz) / D2R;
		if (pRa>360) pRa -= 360;
		if (pRa<0)   pRa += 360;
		ra	= pRa;
		dec	= pDec;
		return new PointEq(pRa,pDec);
	}


  /// <summary>
	/// rotateDegreesToNorth returns the number of degrees 
	/// the image must be rotated to make North be "up"
	/// </summary>
	/// <param name="ra"> center point of the frame</param>
	/// <param name="dec">center point of the frame</param>
	public double rotateDegreesToNorth(double ra, double dec)  
	{
		// compute point due North, and center            
      Point2D n = this.EqToFrame(ra, dec+0.1);
      Point2D c = this.EqToFrame(ra,dec);
      double angle = -90 - Math.atan2((double)(n.getY() - c.getY()), (double)(n.getX() - c.getX())) / D2R;
      return angle;
	}


  /// <summary>
  /// Converts ra into decimal degrees.
  /// </summary>
  public static double hms2deg(String s)
  {
      String[] a = s.split(":");
      double v = 15.0 * Double.parseDouble(a[0]) + Double.parseDouble(a[1]) / 4.0 + Double.parseDouble(a[2]) / 240.0;
      return v;
  }

  /// <summary>
  /// Converts dec into decimal degrees.
  /// </summary>
  public static double dms2deg(String s)
  {
      String[] a = s.split(":");
      double v;
      if (s.lastIndexOf("-") == 0)
          v = -(-1.0 * Double.parseDouble(a[0]) +
        		  Double.parseDouble(a[1]) / 60.0 +
        		  Double.parseDouble(a[2]) / 3600.0);
      else
          v = (Double.parseDouble(a[0]) +
        		  Double.parseDouble(a[1]) / 60.0 +
        		  Double.parseDouble(a[2]) / 3600.0);
      return v;
  }


  //%%%%%%%%%%%%%%%%%%%%%%%  2MASS Section %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//
  /*
  //// WCS for 2mass images
  public void get_theta_phi(double alpha_deg, double delta_deg, double alpha_p_deg, double delta_p_deg, ref double theta, ref double phi)
  {

      double deg2rad = Math.PI / 180.0;
      double alpha = alpha_deg * deg2rad;
      double delta = delta_deg * deg2rad;
      double alpha_p = alpha_p_deg * deg2rad;
      double delta_p = delta_p_deg * deg2rad;

      double phi_p = 180.0 * deg2rad;  //;delta_p
      double cd = Math.Cos(delta);
      double sd = Math.Sin(delta);
      double cdp = Math.Cos(delta_p);
      double sdp = Math.Sin(delta_p);

      phi = phi_p + Math.Atan2(-cd * Math.Sin(alpha - alpha_p), sd * cdp - cd * sdp * Math.Cos(alpha - alpha_p));
      theta = Math.Asin(sd * sdp + cd * cdp * Math.Cos(alpha - alpha_p));

  }
*//*
  public Point2D EqToFrame(double ra, double dec, double ra_ref, double dec_ref, double scale, double x_ref, double y_ref)
  {

      double deg2rad = Math.PI / 180.0;
      double theta = 0.0;
      double phi = 0.0;
      get_theta_phi(ra, dec, ra_ref, dec_ref,  theta,  phi);

      double rtheta = 1.0 / Math.tan(theta) / deg2rad;
      double x = rtheta * Math.sin(phi);
      double y = -rtheta * Math.cos(phi);
      //now  get the actual pixels values  //-1 if first pixel is at 0
      double x_pix = -x / scale + x_ref - 1;
      double y_pix = y / scale + y_ref - 1;
      Point2D p = new Point2D.Float((float)x, (float)y);
      return p;
  }
*/
  //public PointEq FrameToEq(float x, float y, double crpix1, double crpix2, double cdelt1, double cdelt2, double crval1, double crval2)
  //{

  //    double pRa = (x - crpix1 +1) * cdelt1 + crval1;
  //    double pDec = (y - crpix2 +1 ) * cdelt2 + crval2;            
  //    ra = pRa;
  //    dec = pDec;
  //    return new PointEq(pRa, pDec);

  //}
  public PointEq FrameToEq(float x, float y, double crpix1, double crpix2, double cdelt1, double cdelt2, double crval1, double crval2)
  {
      double deg2rad = Math.PI / 180.0;

      double x_int = cdelt1 * (x - crpix1);
      double y_int = cdelt2 * (y - crpix2);

      double phi = Math.atan2(x_int, (-y_int));
      double rtheta = Math.sqrt(x_int * x_int + y_int * y_int);
      double theta = Math.atan(180.0 / Math.PI / rtheta);


      double deltap = crval2 * deg2rad;
      double alphap = crval1 * deg2rad;
      double phip = Math.PI;

      double ct = Math.cos(theta);
      double st = Math.sin(theta);
      double cdp = Math.cos(deltap);
      double sdp = Math.sin(deltap);

      double t1 = st * cdp - ct * sdp * Math.cos(phi - phip);
      double t2 = -Math.cos(theta) * Math.sin(phi - phip);
      ra = alphap + Math.atan(t2 / t1);

      double t = st * sdp + ct * cdp * Math.cos(phi - phip);
      dec = Math.asin(t);

      ra = ra / deg2rad;
      dec = dec / deg2rad;
      return new PointEq(ra, dec);
  }
/*
  public void copy2Mass(Coord coord)
  {
      this.crval1 = coord.crval1;
      this.crval2 = coord.crval2;
      this.crpix1 = coord.crpix1;
      this.crpix2 = coord.crpix2;
      this.cdelt1 = coord.cdelt1;
      this.cdelt2 = coord.cdelt2;
      this.m = coord.m;
  }

  // This Constructor for 2mass images with wcs read from fits files
  public Coord(double _crval1, double _crval2, double _crpix1, double _crpix2, double _cdelt1, double _cdelt2)
  {
      crval1 = _crval1;
      crval2 = _crval2;
      crpix1 = _crpix1;
      crpix2 = _crpix2;
      cdelt1 = _cdelt1;
      cdelt2 = _cdelt2;
      m = null;
  }
*/
 /////%%%%%%%%%%%%%%%%%%%%%%%%%  end 2mass section %%%%%%%%%%%%%%%%%%%%%%%%% ///
  
  } // end of Coord Class

