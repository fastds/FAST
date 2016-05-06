package edu.gzu.image;

import java.awt.geom.Point2D;

///Current version
///ID:          $Id: projection.cs,v 1.2 2003/04/10 21:54:32 szalay Exp $
///Revision:    $Revision: 1.2 $
///Date:        $Date: 2003/04/10 21:54:32 $

/**
 * 为不同的投影定义的接口
 */
interface IProjection
{
	Point2D EqToScreen(double ra_, double dec_, float size_);
	Point2D EqToScreen(PointEq e_, float size_);
	PointEq ScreenToEq(Point2D p_);
}

   
  /// <summary>
  /// CARTProjection
  /// Gives us the definition of the projection to the screen
  /// using a cartesian projection, centered on (ra=180, dec=0).
  /// </summary>
/**
 * Gives us the definition of the projection to the screen
 *  using a cartesian projection, centered on (ra=180, dec=0).
 */
  class CARTProjection implements IProjection
  {
      public static String Revision = "$Revision: 1.2 $";
      private int width, height;					// screen size
      private Point2D cOffset;							// reference point offset
      private double scale;							// image scale
      private double[] n, w, u;						// normal, west, up vectors
      /// <summary>
      /// InitProjection. Initialize the transformation for the projection
      /// to be used on the screen.
      /// </summary>	
      public CARTProjection(double ra_, double dec_, double ppd_, int width_, int height_)
      {
          ra_ = 180.0;
          dec_ = 0.0;
          n = V3.Normal(ra_, dec_);
          w = V3.West(ra_, dec_);/////???????????????????????????????
          u = V3.North(ra_, dec_);

          scale = ppd_ / V3.D2R;
          width = width_;
          height = height_;
          cOffset = new Point2D.Float((float)(width / 2.0), (float)(height / 2.0));
      }

      //------------------------------
      // Coordinate transformations
      //------------------------------

      /// <summary>
      /// EqToScreen. Maps an (ra,dec) coordinate to the Screen.
      /// This is the basic coordinate transformation function
      ///  </summary>
      /// <param name="ra_">Right ascension in degrees, double</param>
      /// <param name="dec_">Declination in degrees, double</param>
      /// <param name="size_">Symbol size in pixels, float</param>
      public Point2D EqToScreen(double ra_, double dec_, float size_)
      {
          double gn = Math.cos(dec_ * V3.D2R);
          if (gn != 0)
          {
              gn = 1.0 / gn;
          }
          Point2D q = new Point2D.Float((float)cOffset.getX() - (float)(scale * (ra_ - 180) * gn),(float) cOffset.getY() - (float)(scale * dec_));
          return q;
      }

      public Point2D EqToScreen(PointEq e, float size_)
      {
          return EqToScreen(e.ra, e.dec, size_);
      }


      /**
       * Maps a screen coordinate to an (ra,dec).
       * This is the inverse coordinate transformation function
       * @param The screen coordinate of the point
       */
      public PointEq ScreenToEq(Point2D p)
      {
          double[] r = new double[3];

          //----------------------------------------
          // first subtract the offset, and rescale  首先减去偏移，并重新调整
          //----------------------------------------
          Point2D q = new Point2D.Float((float)((cOffset.getX() - p.getX()) / scale), (float)((cOffset.getY() - p.getY()) / scale));

          //------------------------------------------------
          // now these are the dot products with w and u
          // compute the dot product with the normal vector
          //------------------------------------------------
          double qdec = 90 * q.getY();
          double qra = 180 + 180 * q.getX() * Math.cos(qdec * V3.D2R);
          PointEq g = new PointEq(qra, qdec);
          return g;
      }
  }

  /**
   * Gives us the definition of the projection to the screen
   * using a tangent plane projection, centered on (ra,dec).
   * 用一个以(ra,dec)为中心点的切面投影定义到屏幕的投影
   */
 class TANProjection implements IProjection
  {
      public static String Revision="$Revision: 1.2 $";
      private int width, height;					// screen size
      private Point2D cOffset;						// reference point offset
      private double scale;							// image scale
      private double[] n, w, u;						// normal, west, up vectors
      /**
       * Initialize the transformation for the projection to be used on the screen.
       */
      public TANProjection(double ra_, double dec_, double ppd_, int width_, int height_)
      {
          n = V3.Normal(ra_, dec_);
          w = V3.West(ra_, dec_);
          u = V3.North(ra_, dec_);

          scale = ppd_ / V3.D2R;
          width = width_;
          height = height_;
          cOffset = new Point2D.Float((float)(width / 2.0), (float)(height / 2.0));
      }

      //--------------
      // 坐标变换
      //--------------

      /**
       * 将(ra,dec)映射到屏幕，这是一个基本的坐标变换功能
       * @param ra_ Right ascension in degrees, double
       * @param dec_ Declination in degrees, double
       * @param size_ Symbol size in pixels, float
       */
      public Point2D EqToScreen(double ra_, double dec_, float size_)
      {
          double[] r = V3.Normal(ra_, dec_);
          double gn = 0;
          double gw = 0;
          double gu = 0;
          for (int i = 0; i < 3; i++)
          {
              gn += r[i] * n[i];
              gw += r[i] * w[i];
              gu += r[i] * u[i];
          }
          Point2D q = new Point2D.Float((float)cOffset.getX() - (float)(scale * gw),(float) cOffset.getY() - (float)(scale * gu));
          return q;
      }

      public Point2D EqToScreen(PointEq e, float size_)
      {
          return EqToScreen(e.ra, e.dec, size_);
      }


      /**
       * Maps a screen coordinate to an (ra,dec).<br/>
       * This is the inverse coordinate transformation function
       * @param p The screen coordinate of the point
       */
      public PointEq ScreenToEq(Point2D p)
      {
          double[] r = new double[3];
          //----------------------------------------
          // first subtract the offset, and rescale
          //----------------------------------------
          Point2D q = new Point2D.Float((float)((cOffset.getX() - p.getX()) / scale), (float)((cOffset.getY() - p.getY()) / scale));
          //------------------------------------------------
          // now these are the dot products with w and u
          // compute the dot product with the normal vector
          //------------------------------------------------
          double gn = 1.0 - q.getX() * q.getX() - q.getY() * q.getY();
          gn = Math.sqrt(gn);
          for (int i = 0; i < 3; i++)
          {
              r[i] = gn * n[i] + q.getX() * w[i] + q.getY() * u[i];
          }
          PointEq g = V3.ToEq(r);
          return g;
      }
  }



  /// <summary>
  /// STRProjection
  /// Gives us the definition of the projection to the screen
  /// using a stereographic projection, centered on (ra,dec).
  /// </summary>
 class STRProjection implements IProjection
  {
      public static String Revision="$Revision: 1.2 $";
      private int width, height;					// screen size
      private Point2D cOffset;							// reference point offset
      private double scale;							// image scale
      private double[] n, w, u;
      /// <summary>
      /// InitProjection. Initialize the transformation for the projection
      /// to be used on the screen.
      /// </summary>	
      public STRProjection(double ra_, double dec_, double ppd_, int width_, int height_)
      {
          n = V3.Normal(ra_, dec_);
          w = V3.West(ra_, dec_);
          u = V3.North(ra_, dec_);

          scale = ppd_ / V3.D2R;
          width = width_;
          height = height_;
          cOffset = new Point2D.Float((float)(width / 2.0), (float)(height / 2.0));
      }

      //-----------------------------
      // Coordinate transformations
      //-----------------------------

      /// <summary>
      /// EqToScreen. Maps an (ra,dec) coordinate to the Screen.
      /// This is the basic coordinate transformation function
      ///  </summary>
      /// <param name="ra_">Right ascension in degrees, double</param>
      /// <param name="dec_">Declination in degrees, double</param>
      /// <param name="size_">Symbol size in pixels, float</param>
      public Point2D EqToScreen(double ra_, double dec_, float size_)
      {
          double[] r = V3.Normal(ra_, dec_);
          double gn = 0;
          double gw = 0;
          double gu = 0;
          for (int i = 0; i < 3; i++)
          {
              gn += r[i] * n[i];
              gw += r[i] * w[i];
              gu += r[i] * u[i];
          }
          double a = 2.0 / (1 + gn);
          Point2D q = new Point2D.Float((float)cOffset.getX() - (float)(scale * a * gw),(float) cOffset.getY() - (float)(scale * a * gu));
          return q;
      }

      public Point2D EqToScreen(PointEq e, float size_)
      {
          return EqToScreen(e.ra, e.dec, size_);
      }


      public PointEq ScreenToEq(Point2D p)
      {
          double[] r = new double[3];

          //----------------------------------------
          // first subtract the offset, and rescale
          //----------------------------------------
          Point2D q = new Point2D.Float((float)((cOffset.getX() - p.getX()) / scale), (float)((cOffset.getY() - p.getY()) / scale));

          //------------------------------------------------
          // now these are the dot products with w and u
          // compute the dot product with the normal vector
          //------------------------------------------------
          double rho = 0.25 * (q.getX() * q.getX() + q.getY() * q.getY());
          for (int i = 0; i < 3; i++)
          {
              r[i] = ((1.0 - rho) * n[i] + q.getX() * w[i] + q.getY() * u[i]) / (1.0 + rho);
          }
          PointEq g = V3.ToEq(r);
          return g;
      }
  }
