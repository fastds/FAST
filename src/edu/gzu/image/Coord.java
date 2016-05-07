package edu.gzu.image;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

//////////////////////////////////等待完善的矩阵类////////////////////////////////////
/**
 * 与赤经赤纬等价的坐标点对象
 */
class PointEq
{
	public double ra,dec;
	public PointEq(double pRa, double pDec)
	{
		ra	= pRa;
		dec	= pDec;
	}
}
/**
 * Summary description for Coord.
 * Coord  class carries the transformation of ra/dec to nu/mu and xy. 
 * 进行ra/dec 到 nu/mu 和 xy的转换 
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
	public static double D2R = Math.PI / 180.0;	// 角度到弧度的变换

	public Coord()
	{}


	public Coord(double a,double b,double c,double d,double e,double f,
		double node,double incl,double scale, String info) 
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.node = node;
		this.incl = incl;
		this.scale= scale;
		this.info = info;
		this.m	 = null;
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
	 * 根据Frame图像的 像素坐标(x,y)，计算ra,dec
	 * @param x pixels across (scaled for zoom level)
	 * @param y pixels down (scaled for zoom level)
	 */
	public PointEq FrameToEq(float x, float y)  
	{
		// x,y need to be in original pixel scale x,y需要是最初的像素比例x,y
		double col = x;
		double row = y;

		// Great circle coordinates in degrees 
		this.mu = (this.a +  this.b*row  +  this.c*col);
		this.nu = (this.d +  this.e*row  +  this.f*col);

		// convert to radians, then to ra,dec 单位变换为弧度，然后计算ra,dec
		double muR = (mu - node) * D2R;
		double nuR = nu * D2R;
		double xg  = Math.cos(muR) * Math.cos(nuR);
		double yg  = Math.sin(muR) * Math.cos(nuR);
		double zg  = Math.sin(nuR);

		// compute unit vector 计算单位向量
		double inR = incl * D2R;
		double qx  = xg;
		double qy  = yg * Math.cos(inR) - zg*Math.sin(inR);
		double qz  = yg * Math.sin(inR) + zg*Math.cos(inR);

		//  计算ra,dec
		double pRa  = Math.atan2(qy,qx) / D2R  +  node;
		double pDec = Math.asin(qz) / D2R;
		if (pRa>360) pRa -= 360;
		if (pRa<0)   pRa += 360;
		this.ra	= pRa;
		this.dec = pDec;
		return new PointEq(pRa,pDec);
	}


	/**
	 * @param ra frame的中心点
	 * @param dec frame的中心点
	 * @return the number of degrees ,the image must be rotated to make North be "up"
	 */
	public double rotateDegreesToNorth(double ra, double dec)  
	{
		// compute point due North, and center            
      Point2D n = this.EqToFrame(ra, dec+0.1);
      Point2D c = this.EqToFrame(ra,dec);
      double angle = -90 - Math.atan2((double)(n.getY() - c.getY()), (double)(n.getX() - c.getX())) / D2R;
      return angle;
	}


	/**
	 * Converts ra into decimal degrees.
	 */
	  public static double hms2deg(String s)
	  {
	      String[] a = s.split(":");
	      double v = 15.0 * Double.parseDouble(a[0]) + Double.parseDouble(a[1]) / 4.0 + Double.parseDouble(a[2]) / 240.0;
	      return v;
	  }

  /**
   * Converts dec into decimal degrees.
   */
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

} // end of Coord Class

