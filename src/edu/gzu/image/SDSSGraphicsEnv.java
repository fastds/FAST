package edu.gzu.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
class Pen
{
	private Color color;
	private float stroke;
	public Pen(Color c,float i)
	{
		this.color = c;
		this.stroke = i;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color c) {
		this.color = c;
	}
	public float getStroke() {
		return stroke;
	}
	public void setStroke(float stroke) {
		this.stroke = stroke;
	}
	
}

/**
 * SDSSGraphicsEnv defines our own Graphic Enviroment for SDSS
 * 下面这个类定义了我们自己的SDSS的图形环境
 *
 */
	public class SDSSGraphicsEnv
	{	
		public static String Revision = "$Revision: 1.9 $";
		//-----------------------------------
		private boolean debug = false;		
      	private int width, height;		// Image size 图片大小
      	private double imageScale;		// Image scale   图片比例
      	private double ppm;				// image resolution in pixels per arcminute 每弧分的像素点的解析
      	private double ppd;               // pixel resolution in pixels per degree
      	private double pixelScale;		// Pixel scale in arcsec/pixel units 每单元 弧秒/像素 的像素比例
      	//-----------------------------------
      	// define the different projections
      	//-----------------------------------
      	public  IProjection proj;							// the abstract Projection object		
      	private TANProjection tproj;					    // the TAN projection object
      	private STRProjection sproj;					    // the STR projection object
		//-----------------------------------
        private Graphics2D  gc;
		private BufferedImage	  img;
		//-----------------------------------
		// predefine the different colors
		//-----------zoe------------------------
		private Pen queryPen	= new Pen(Color.magenta, 1);
		private Pen photoPen	= new Pen(Color.blue, 1);			
		private Pen specPen		= new Pen(Color.RED, 1);					
		private Pen targetPen	= new Pen(Color.YELLOW, 1);	
		private Pen bboxPen		= new Pen(new Color(255,0,255),0.25F);
		private Pen outlinePen	= new Pen(new Color(0,255,0),0.25F);			
		private Pen fieldPen	= new Pen(Color.GRAY,0.25F);						
		private Pen maskPen		= new Pen(Color.RED,1);			
		private Pen platePen	= new Pen(Color.PINK,1);
		private Pen testPen		= new Pen(Color.CYAN,1);
		private Pen gridPen		= new Pen(Color.GREEN,1);
		private Pen rulerPen	= new Pen(Color.GREEN,2);
		private Pen regionPen   = new Pen(Color.ORANGE, 2.0F);
		// for Apoggee objects
		private Pen apoPen = new Pen(new Color(11,111,11,11), 2);	
		//-----------------------------------
		// label formats
		//-----------------------------------
//  zoe    private SolidBrush drawBrush    = new SolidBrush(Color.WHITE);
//  zoe    private SolidBrush fillBrush    = new SolidBrush(Color.FromArgb(17, 59, 227));
//zoe	private StringFormat drawFormat = new StringFormat();
		private Font font		= new Font("Arial", Font.BOLD,9);
		//-----------------------------------
		// define where the label is printed. 
		// yLabel changes as we write lines.
		//-----------------------------------
		private int xLabel = 20;
		private int yLabel	= 15;
		private StringBuilder debugMessage = new StringBuilder();		
		private String imageType = "";


      /**
       * SDSSGraphicsEnv constructor
       * @param w_
       * @param h_
       * @param isc_
       * @param ppd_
       * @param dbg_
       * @param _imageType
       */
		public SDSSGraphicsEnv ( int w_, int h_, double isc_, double ppd_, boolean dbg_, String _imageType)
		{			
			debug		= dbg_;
			width		= w_;
			height		= h_;
			ppd         = ppd_;
			ppm			= ppd_ /60.0;
			imageScale	= isc_;
			pixelScale  = 3600.0 / ppd_;
          	imageType   = _imageType;

			//---------------------------------------------
            // Extend line ends to fill the whole pixel 
            //---------------------------------------------
//		zoe	outlinePen.StartCap = LineCap.Square;
//	zoe		outlinePen.EndCap	= LineCap.Square;								
			//---------------------------------------------
			// Allocate the drawing canvas 
            //---------------------------------------------
  //zoe        PixelFormat pf  = PixelFormat.Format32bppRgb;		// use colors
			img	= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);	// Make our new image to user's adjusted specs
			gc = (Graphics2D)img.getGraphics();				// Make an graphic obj we can draw on			
		
			//zoe	gc.SmoothingMode = SmoothingMode.AntiAlias;	// set as default SmoothingMode 指定抗锯齿的显示
			gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}


		/**
		 * 初始化cavas中的Project成员
		 * @param ra_ Right ascension in degrees
		 * @param dec_ Declination in degrees
		 * @param ptype_ Astrometric transformation of center Frame
		 */
		public void InitializeProjection(double ra_, double dec_, String ptype_)
		{

          //-------------------------------------
          // 调用 Projection 构造器, 然后转换为抽象接口
          //-------------------------------------
          if (ptype_ == "TAN")
          {
              tproj = new TANProjection(ra_, dec_, ppd, width, height);
              proj = (IProjection)tproj;
          }
          if (ptype_ == "STR")
          {
              sproj = new STRProjection(ra_, dec_, ppd, width, height);
              proj = (IProjection)sproj;
          }        
      }
		//=============================================================
		// Image related functions  图像相关的功能
		//=============================================================
      //@Deoyani Nandrekar
      ///// <summary>
      ///// get image property
      ///// </summary>	
      //public Bitmap image   // 
      //{
      //    get {return this.img;}
      //}

/*zoe
      public ImageFormat getImageFormat(String imgType)
      {
          switch (imgType)
          {
              case "png": return ImageFormat.Png;
              case "gif": return ImageFormat.Gif;
              case "tif": return ImageFormat.Tiff;
              case "bmp": return ImageFormat.Bmp;
              default   : return ImageFormat.Jpeg;
          }
      }*/
/*zoe
      private static ImageCodecInfo GetEncoderInfo(String mimeType)
      {
          int j;
          ImageCodecInfo[] encoders;
          encoders = ImageCodecInfo.GetImageEncoders();
          for (j = 0; j < encoders.Length; ++j)
          {
              if (encoders[j].MimeType == mimeType)
                  return encoders[j];
          }
          return null;
      }
*/
		/**
		 * getBuffer. Returns the bytes of the image buffer as a JPEG.
		 * @return Byte[] of the JPEG image
		 */
		public byte[] getBuffer()
		{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try {
				ImageIO.write(img, "jpeg", out);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return out.toByteArray();							    // return image
		}



	 /**
	  * Invert. It will invert each pixel of the image.
	  */
      public void Invert()
      {
          // GDI+ still lies to us - the return format
    	  Rectangle rec = new Rectangle(0,0,img.getWidth(),img.getWidth());
    	  WritableRaster raster = (WritableRaster)img.getData(rec);
    	  //Returns the minimum valid X,Y coordinate of the Raster.
    	  int minX = raster.getMinX();
    	  int minY = raster.getMinY();
    	  //Returns the width,height in pixels
    	  int width = raster.getWidth();
    	  int height = raster.getHeight();
    	  float[] gArray = new float[3];
    	  float[] sArray = new float[3];
    	  for(int x = minX;x<width;x++)
    	  {
    		  for(int y = minY;y<height;y++)
    		  {	
//    			  model.getPixel(x, y, fArray, dataBuffer);
    			  raster.getPixel(x, y, gArray);
    			  for(int n =0;n<gArray.length;n++)
    			  {
    				  sArray[n] = 255-gArray[n];
    			  }
//    			  model.setPixel(x, y, sArray, dataBuffer);
    			  raster.setPixel(x, y, sArray);
    		  }
    	  }
    	  gArray = null;
    	  sArray = null;
    	  img.setData(raster);
         /* BitmapData bmData = img.LockBits(
              new Rectangle2D(0, 0, img.Width, img.Height),
              ImageLockMode.ReadWrite, PixelFormat.Format24bppRgb
              );
          int stride = bmData.Stride;//对象跨距宽度（字节）
          System.IntPtr Scan0 = bmData.Scan0;
          unsafe
          {
              byte* p = (byte*)(void*)Scan0;
              int nOffset = stride - img.Width * 3;
              int nWidth = img.Width * 3;
              for (int y = 0; y < img.Height; ++y)
              {
                  for (int x = 0; x < nWidth; ++x)
                  {
                      p[0] = (byte)(255 - p[0]);
                      ++p;
                  }
                  p += nOffset;
              }
          }
          img.UnlockBits(bmData);*/
      } 
      /**
       * GetViewPort. It will compute the ViewPort from the projection.<br/>
       * 根据投影计算视口,计算投影到屏幕上的图像的中心点和四个角的ra,dec<br/>
       * 
       */
      public String GetViewPort()
      {
          String vs;
          //------------------------------------------------------------
          // construct the viewport 计算四个角以及中心点的(ra,dec)
          //------------------------------------------------------------
          PointEq cc = proj.ScreenToEq(new Point2D.Float(width / 2.0F, height / 2.0F));
          PointEq nw = proj.ScreenToEq(new Point2D.Float(0.0F, 0.0F));
          PointEq ne = proj.ScreenToEq(new Point2D.Float((float)(width), 0.0F));
          PointEq sw = proj.ScreenToEq(new Point2D.Float(0.0F, (float)(height)));
          PointEq se = proj.ScreenToEq(new Point2D.Float((float)(width), (float)(height)));
          //-------------------------------------------------------
          // convert the vertices(至高点) to V3 ，即计算每个点的法向量
          //-------------------------------------------------------
          double[] nw3 = V3.Normal(nw);
          double[] ne3 = V3.Normal(ne);
          double[] sw3 = V3.Normal(sw);
          double[] se3 = V3.Normal(se);
          double[] cen = V3.Normal(cc);

          vs = "REGION CONVEX ";

          //-----------------------------------------
          // compute distance from center
          //-----------------------------------------
          double dist = Math.acos(V3.Dot(cen, nw3));
          //-----------------------------------------
          // is it less than a hemisphere?
          //-----------------------------------------
          if (dist < Math.PI / 2)
          {
              //-----------------------------------------
              // compute the normal vectors 计算法向量
              // corresponding to the great circle edges 与圆弧边相适应
              //-----------------------------------------
              double[] n3 = V3.Normalize(V3.Wedge(nw3, ne3));
              double[] s3 = V3.Normalize(V3.Wedge(se3, sw3));
              double[] w3 = V3.Normalize(V3.Wedge(sw3, nw3));
              double[] e3 = V3.Normalize(V3.Wedge(ne3, se3));

              vs += n3[0] + " " + n3[1] + " " + n3[2]+ " 0.0 ";
              vs += s3[0]+ " " + s3[1]+ " " + s3[2]+ " 0.0 ";
              vs += e3[0] + " " + e3[1]+ " " + e3[2]+ " 0.0 ";
              vs += w3[0] + " " + w3[1] + " " + w3[2] + " 0.0 ";
          }
          else
          {
              PointEq nn = proj.ScreenToEq(new Point2D.Float(width / 2.0f, 0.0F));
              double[] top = V3.Normal(nn);
              //----------------------------------------------------
              // compute distance from center, should be negative
              //----------------------------------------------------
              double cval = V3.Dot(cen, top);
              vs += cen[0] + " " + cen[1]+ " "
                  + cen[2] + " " + cval;////////////////////zoe????
          }

          return vs;
      }

		//=============================================================
		// 高级绘制程序
		//=============================================================
      /**
       * drawImage. Draw the image onto the canvas using the affine 
       * transformation through the Graphics.Transform
       * 同过Graphics2D中的方法，利用反射变换将图像画到画布上
       * @param coord The affine transformation of a Field 仿射变换域
       * @param tile JPEG图像的字节数组
       */
		public void drawFrame(Coord coord, Image tile) 
		{	
			Point2D[] p = SdssConstants.FieldGeometry(true);	
			AffineTransform beforeTS = gc.getTransform();
			gc.transform(coord.m);
//before	gc.DrawImage(tile, new Point2D.Float[] {(Float) p[0], (Float) p[1], (Float) p[3] });
			gc.drawImage(tile,(int) p[0].getX(),(int) p[0].getY(), (int)(p[1].getX()-p[0].getX()),(int)(p[2].getY()-p[1].getY()),null );
			gc.setTransform(beforeTS);
		}


		/**
		 * drawField. Draw the outlines of a field specified by the coord.
		 * @param coord The affine transformation of a Field
		 */
		public void drawField (Coord coord)	
		{
			Point2D[] p = SdssConstants.FieldGeometry(false);
			AffineTransform beforeTS = gc.getTransform();
			gc.transform(coord.m) ;
//zoe		gc.DrawLines(fieldPen, new Point2D[] {p[0],p[1],p[2],p[3],p[0]});
			gc.setColor(fieldPen.getColor());
			Polygon poly = new Polygon();
			for(int i =0;i < p.length; i++)
			{
				poly.addPoint((int)p[i].getX(),(int) p[i].getY());
			}
			
			gc.draw(poly);
			gc.setTransform(beforeTS);//重置为之前的单位矩阵
			if (debug)
			{
			   debugMessage.append("DrawField "+coord.info+"\n");        
			}
		}

		/**
		 * drawRegion. Draw the outlines of a region.
		 * @param hr The struct containing an HTMRegion
		 */
      public void drawRegion(HTMRegion hr, boolean fill)
      {
          if (hr.arc.length == 0)
          {
              debugMessage.append("NULL Region " + hr.rid + "\n");
              return;
          }

          //debugMessage.Append("DrawRegion "+hr.rid.ToString()+" " + fill.ToString()+"\n");

          //----------------------------
          // do an adaptive resolution
          //----------------------------
          int nstep = 100 - 3 * ((int)(pixelScale / 10.0));
          if (nstep < 30) nstep = 30;
          double delta = 2 * Math.PI / (nstep - 1);

          //-------------
          // create path
          //-------------
          GeneralPath path = new GeneralPath();

          //----------------------------------------
          // allocate enough storage for the points
          //----------------------------------------
          int npoints = nstep * hr.arc.length;
          Point2D[] pts = new Point2D.Float[npoints];

          int np = 0;
          try
          {
              int convex = hr.arc[0].cvx;
              int patch = hr.arc[0].patch;
              for (int k = 0; k < hr.arc.length; k++)
              {
                  //---------------------------------------------------
                  // if a new patch or a new convex, draw it right now
                  //---------------------------------------------------
                  if (hr.arc[k].cvx != convex | hr.arc[k].patch != patch)
                  {
                      //drawPatch(pts, np, cl, hr.fill);
                      if (np != 0) addPatch(pts, np, path);
                      convex = hr.arc[k].cvx;
                      patch = hr.arc[k].patch;
                      np = 0;
                  }

                  //-------------------------------------------
                  // if it is not drawable, go to the next arc
                  //-------------------------------------------
                  if (hr.arc[k].draw == 0) continue;

                  double[] r1 = V3.Normal(hr.arc[k].ra1, hr.arc[k].dec1);
                  double[] r2 = V3.Normal(hr.arc[k].ra2, hr.arc[k].dec2);

                  //----------------------------------------------------------
                  // get the equation of the plane, and the circle of the arc
                  //----------------------------------------------------------
                  double cth = hr.arc[k].c;
                  double sth = Math.abs(Math.sin(Math.acos(cth)));
                  double[] n = { hr.arc[k].x, hr.arc[k].y, hr.arc[k].z };
                  PointEq g = V3.ToEq(n);
                  double[] w = V3.West(g.ra, g.dec);
                  double[] u = V3.North(g.ra, g.dec);

                  for (int m = 0; m < 3; m++)
                  {
                      n[m] *= cth;
                      r1[m] -= n[m];
                      r2[m] -= n[m];
                      w[m] *= sth;
                      u[m] *= sth;
                  }

                  //----------------------------------------------------------
                  // compute the two angles defining the two roots on the arc
                  //----------------------------------------------------------
                  double phi1 = Math.atan2(V3.Dot(r1, u), V3.Dot(r1, w));
                  double phi2 = Math.atan2(V3.Dot(r2, u), V3.Dot(r2, w));
                  if (phi2 <= phi1) phi2 += Math.PI * 2.0;

                  //---------------------------------
                  // compute precise number of steps
                  //---------------------------------
                  int steps = (int)Math.floor((phi2 - phi1) / delta);
                  if (steps < 15) steps = 15;

                  //----------------------
                  // do the loop over phi
                  //----------------------
                  double[] r = new double[3];
                  double phi = phi1;
                  double inc = (phi2 - phi1) / (steps - 1);
                  for (int i = 0; i < steps; i++)
                  {
                      for (int m = 0; m < 3; m++)
                      {
                          r[m] = n[m] + Math.cos(phi) * w[m] + Math.sin(phi) * u[m];
                      }
                      pts[np++] = proj.EqToScreen((float)V3.ToEq(r).ra, (float)V3.ToEq(r).dec, 0.0F);
                      phi += inc;
                  }
              }

              if (np != 0) addPatch(pts, np, path);
              Color cl = regionColor(hr.type, hr.mask);
              Pen rpen = new Pen(cl, 0.25F);
//  zoe       SolidBrush oBrush = new SolidBrush(Color.FromArgb(64, cl));
              if (hr.fill > 0)
              {
                  rpen.setColor(Color.RED);
//  zoe    		 oBrush.Color = Color.FromArgb(128, Color.YELLOW);
              }
              if (fill || hr.fill>0 ) 
              {
            	//  zoe   gc.FillPath(oBrush, path);
            	  gc.setColor(Color.YELLOW);//??????????
            	  gc.fill(path);
              }
            	  

              //--------------------------
              // now draw the path itself
              //--------------------------
//    zoe     gc.DrawPath(rpen, path);
              gc.draw(path);
          }
          catch(Exception e) { 
        	  //zoe displayMessage("exception in drawRegion " + hr.rid + "\n");
        	  System.out.println(e);
        	  }
//   zoe       if (debug && false)
//          {
//              debugMessage.append("drawRegion\n");
//          }
      }


      /**
       * addPatch. Insert the outline of a patch given by a polyline into the path.
       * @param pts The array containing the points in screen coordinates
       * @param np The number of points to be drawn
       * @param path The GraphicsPath to be appended to
       */
      public void addPatch(Point2D[] pts, int np, GeneralPath path)
      {
          //-----------------------------------------------
          // copy the points so that we can use a polyline
          //-----------------------------------------------
          Point2D[] pp = new Point2D.Float[np];
          for (int i = 0; i < np; i++) pp[i] = pts[i];

//      zoe    path.CloseFigure();
//   zoe       path.AddLines(pp);
          path.closePath();
          path.moveTo(pp[0].getX(), pp[0].getY());
          for(int i = 1 ; i < np; i++)	path.lineTo(pp[i].getX(),pp[i].getY());
         
      }



      	/**
      	 * drawQueryObj. Draw the position of a Query Object by ra,dec.
      	 * @param oRa The right ascension of the object in degrees
      	 * @param oDec The declination of the object in degrees
      	 * @param size The size of the symbol in screen pixels
      	 */
		public void drawQueryObj(double oRa, double oDec, float size)		
		{

          float b = 0.55F * size;
          Point2D p = proj.EqToScreen(oRa, oDec, size);
          Point2D[] tri = new Point2D.Float[3];
          tri[0] = new Point2D.Float((float)p.getX() - b,(float) p.getY() - b );
          tri[1] = new Point2D.Float((float)p.getX() + b, (float)p.getY() - b);
          tri[2] = new Point2D.Float((float)p.getX() ,(float) p.getY() + b);
          gc.setColor(queryPen.getColor());
		  gc.drawPolygon(new int[]{(int)tri[0].getX(),(int)tri[1].getX(),(int)tri[2].getX()}
			, new int[]{(int)tri[0].getY(),(int)tri[1].getY(),(int)tri[2].getY()}
			, tri.length);
//          gc.DrawPolygon(queryPen, tri);
		}


		/**
		 * drawSpecObj. Draw the position of a SpecObj by ra,dec.
		 * @param oRa The right ascension of the object in degrees
		 * @param oDec The declination of the object in degrees
		 * @param size The size of the symbol in screen pixels
		 */
		public void drawSpecObj	(double oRa, double oDec, float size)		
		{
			Point2D p = proj.EqToScreen(oRa,oDec,0.0F);	
			gc.setColor(specPen.getColor());
			//原始代码没有类型转换
			gc.drawRect((int)(p.getX()-size/2),(int)(p.getY()-size/2),(int) size,(int) size);
//			System.out.println((p.getX()-size/2)+"..."+(p.getY()-size/2)+"..."+size);
		}


		/**
		 * drawPhotoObj. Draw the position of a PhotoObj by ra,dec.
		 * @param oRa The right ascension of the object in degrees
		 * @param oDec The declination of the object in degrees
		 * @param size The size of the symbol in screen pixels
		 */
		public void drawPhotoObj(double oRa, double oDec, float size)
		{				
			System.out.println("drawPhotoObj-------------------");
			Point2D p =  proj.EqToScreen(oRa,oDec, 0.0F);			
//          gc.DrawEllipse(photoPen, p.getX()-size/2, p.getY()-size/2, size, size);
			Ellipse2D.Float ellipse = new Ellipse2D.Float();
			ellipse.setFrame(p.getX()-size/2,p.getY()-size/2,size,size);
//          System.out.println((p.getX()-size/2)+".."+(p.getY()-size/2)+".."+size);
			gc.setColor(photoPen.getColor());
			gc.draw(ellipse);
		}


	/**
	 * drawApojee.
	 * @param oRa The right ascension of the object in degrees
	 * @param oDec The declination of the object in degrees
	 * @param size The size of the symbol in screen pixels
	 */ 
      public void drawApogeeObj(double oRa, double oDec, float size)
      {
          //PointF p = proj.EqToScreen(oRa, oDec, 0.0F);
          
          //gc.DrawEllipse(apoPen, p.X - size / 2, p.Y - size / 2, size, size);
          float b = 0.55F * size;
          Point2D.Float p = (Float) proj.EqToScreen(oRa, oDec, 0.0F);
          Point2D.Float[] tri = new Point2D.Float[3];
          tri[0] = new Point2D.Float((float)p.getX() - b, (float)p.getY() - b);
          tri[1] = new Point2D.Float((float)p.getX() + b, (float)p.getY() - b);
          tri[2] = new Point2D.Float((float)p.getX(), (float)p.getY() + b);
//   zoe       gc.DrawPolygon(apoPen, tri);
          gc.setColor(apoPen.getColor());
          gc.drawPolygon(new int[]{(int)tri[0].getX(),(int)tri[1].getX(),(int)tri[2].getX()}
          				, new int[]{(int)tri[0].getY(),(int)tri[1].getY(),(int)tri[2].getY()}
          				, tri.length);

      }


		/**
		 *  drawTargetObj. Draw the position of a TargetObj by ra,dec.
		 * @param oRa The right ascension of the object in degrees
		 * @param oDec The declination of the object in degrees
		 * @param size The size of the symbol in screen pixels
		 */
		public void drawTargetObj(double oRa, double oDec, float size)
		{
			Point2D.Float p = (Float) proj.EqToScreen(oRa,oDec,0.0F);		// get the center point
			float a = 0.75F*size;
			float b = 0.25F*size;
			gc.setColor(targetPen.getColor());
			gc.drawLine((int)(p.getX()-a),(int)(p.getY()-a),(int)(p.getX()-b),(int)(p.getY()-b));
			gc.drawLine((int)(p.getX()-a),(int)(p.getY()+a),(int)(p.getX()-b),(int)(p.getY()+b));
			gc.drawLine((int)(p.getX()+a),(int)(p.getY()-a),(int)(p.getX()+b),(int)(p.getY()-b));
			gc.drawLine((int)(p.getX()+a),(int)(p.getY()+a),(int)(p.getX()+b),(int)(p.getY()+b));
		}


      /// <summary>
      /// drawTestObj. Draw the position of an Object by row,col.
      ///  </summary>
      /// <param name="oRa">The right ascension of the object in degrees</param>
      /// <param name="oDec">The declination of the object in degrees</param>
      /// <param name="size">The size of the symbol in screen pixels</param>
//      public void drawTestObj(Coord coord, double oRowc, double oColc, float size)
//      {
//          gc.Transform = coord.m;
//          gc.DrawRectangle(bboxPen, (float)oColc - size / 2, (float)oRowc - size / 2,
//              (float)(size), (float)(size));
//          gc.ResetTransform();
//
//      }


		/**????????????????????????
		 * drawPlate. Draw the outline of a Plate centered at ra,dec.
		 * @param pRa The right ascension of the plate center in degrees
		 * @param pDec The declination of the plate center in degrees
		 * @param pRadius The radius of the circle in arcminutes
		 */
		public void drawPlate ( double pRa, double pDec, double pRadius) 
		{   
			System.out.println("SDSSGraphicsEnv.drawPlate("+pRa+","+pDec+","+pRadius+")");
			// the increment for the angle in degrees
			double  inc	= 1.0;
			double[] n = GetV3Normal(pRa,pDec);
			double[] w = GetV3West(pRa,pDec);
			double[] u = GetV3North(pRa,pDec);
			double[] r = new double[3];
			double plateCos = Math.cos(pRadius * Coord.D2R / 60.0);
			double plateSin = Math.sin(pRadius * Coord.D2R / 60.0);
			int steps	= (int)(360/inc) + 1;
			Point2D.Float[] g	= new Point2D.Float[steps];
			inc *= Coord.D2R;
			for (int j = 0; j < steps; j++) 
			{ 
				double phi = (double)(j * inc);				
				double phiCos = Math.cos(phi) * plateSin;
				double phiSin = Math.sin(phi) * plateSin;
				for (int c = 0; c < 3; c++) 
				{
					r[c] = n[c]*plateCos + phiCos*w[c] + phiSin*u[c];					
				}	
				double oDec	= Math.asin(r[2]) / Coord.D2R;
				double oRa	= Math.atan2(r[1], r[0]) / Coord.D2R;
				if (oRa < 0) oRa += 360.0;
				g[j] = (Float) proj.EqToScreen(oRa,oDec,0.0F);
			}				
//	zoe		gc.DrawLines(platePen, g);?????
			
			gc.setColor(platePen.getColor());
			int[] xPoints,yPoints;
			xPoints = new int[g.length];
			yPoints = new int[g.length];
			for(int num = 0; num < g.length; num++)
			{
				xPoints[num] = (int) g[num].getX();
				yPoints[num] = (int) g[num].getY();
			}
			gc.drawPolyline(xPoints, yPoints, g.length);//??????
		}
		
		/**
		 * drawBoundingBox. Will draw the bounding box of a given object.
		 * @param coord The astrometric transformation object of the Frame
		 * @param xmin The x coordinate of the upper left hand corner
		 * @param xmax The x coordinate of the lower right hand corner
		 * @param ymin The y coordinate of the upper left hand corner
		 * @param ymax The y coordinate of the lower right hand corner
		 */ 
		public void drawBoundingBox (Coord coord, double xmin, double xmax, double ymin, double ymax)		
		{			
			System.out.println("SDSSGraphicsEnv:drawBoundingBox()  in......");
			AffineTransform beforeTS = gc.getTransform();
			if(coord!=null)//add by zoe
				gc.transform(coord.m);
//	zoe		gc.DrawRectangle(bboxPen, (float)xmin, (float)ymin, 
//				(float)(xmax-xmin+SdssConstants.OutlinePix), (float)(ymax-ymin+SdssConstants.OutlinePix));		
			gc.setColor(bboxPen.getColor());
			gc.drawRect((int)xmin, (int)ymin, 
					(int)(xmax-xmin+SdssConstants.getOutlinePix()), (int)(ymax-ymin+SdssConstants.getOutlinePix()));		
			gc.setTransform(beforeTS);
			System.out.println("SDSSGraphicsEnv:drawBoundingBox  out ......");
		}


		/**
		 * drawOutline. Draws the outline of the object as given by the compressed span.
		 * @param coord The astrometric transformation object of the Frame
		 * @param span The compressed outline in span format (R. Lupton)
		 */
		public void drawOutline (Coord coord, StringBuilder span)
		{
			AffineTransform beforeTS = gc.getTransform();
			if(coord!=null)//add by zoe
				gc.transform(coord.m);
			String newSpan = span.toString().replace('"', 'k').replace("k", "");
			try
			{								
				ArrayList<Line> l = polyFunk.getPoly(newSpan);
				System.out.println("poly size:"+l.size());
				gc.setColor(outlinePen.getColor());
				for(int i=0; i < l.size();  i++)
				{
//	zoe				gc.drawLine(outlinePen,((Line)l.get(i)).p1,((Line)l.get(i)).p2);						
					gc.drawLine(l.get(i).p1.x, l.get(i).p1.y, l.get(i).p2.x, l.get(i).p2.y);
					System.out.println("line:"+l.get(i).p1.x+","+ l.get(i).p1.y+","+ l.get(i).p2.x+","+l.get(i).p2.y);
				}			
			}
			catch(Exception e) {
				System.out.println("Exception in drawOutline " + newSpan);
				e.printStackTrace();
				}
			
			//if (debug) debugMessage.Append("drawOutline: "+span+"\n");

			//------------------------------------------------
			gc.setTransform(beforeTS);
		}

		/**
		 * drawMask. Draws a selected list of masks over the canvas.
		 * Masks are encoded as spherical polygons, given by their
		 * corners in equatorial coordinates.
		 * @param area The string giving the vertex list
		 */
		public void drawMask(StringBuilder area)
		{
			System.out.println("SDSSGraphicsEnv.drawMask()---run ");
			String[] sp = area.toString().split(" ");
          //newfile.WriteLine("Area :" + area);
          //newfile.WriteLine("SP :" + sp.Length);
			int np = sp.length / 2;
			Point2D.Float[] pts = new Point2D.Float[np-1];
			double oRa, oDec;
			try
			{
              for (int i = 2, j = 0; i < sp.length; i += 2, j++)
              {
                  oRa = Double.parseDouble(sp[i]);
                  oDec = Double.parseDouble(sp[i + 1]);
                  pts[j] = (Float) proj.EqToScreen(oRa, oDec, 0.0F);
                  //newfile.WriteLine("SP RA:" + sp[i] + " :: SP DEC :" + sp[i + 1] + ":: PTS : " + pts[j]);
              }         
//	zoe			gc.DrawPolygon(maskPen,pts);
              gc.setColor(maskPen.getColor());
              int[] xPoints = new int[pts.length];
              int[] yPoints = new int[pts.length];
              for(int i=0;i<pts.length;i++)
              {
            	  xPoints[i] = (int)pts[i].getX();
            	  yPoints[i] = (int)pts[i].getY();
              }
              gc.drawPolygon(xPoints, yPoints, pts.length);
			}
			catch(Exception e) {displayMessage("exception in drawMask " + area);}
		}

      /// <summary>
      /// drawLabel. Draws a label at the next line.
      /// </summary>
      /// <param name="label">The text of the label.</param>
//      public void drawErrorMessage(string label,int width, int height)
//      {
//          gc.ResetTransform();
//          gc.DrawString(label, font, drawBrush, width/2-200 , height/2-40, drawFormat);
//          string[] lines = label.Split(new char[] { '\n' });
//          yLabel += (int)font.SizeInPoints * (lines.Length + 3);
//      }

		/// <summary>
		/// drawLabel. Draws a label at the next line.
		/// </summary>
		/// <param name="label">The text of the label.</param>
		public void drawLabel(String label)
		{
			AffineTransform beforeTS = gc.getTransform();
//	notzoe		gc.transform(coord.m);
//	zoe		gc.ResetTransform();
			gc.setColor(Color.WHITE);
			gc.setFont(font);
			gc.drawString(label, xLabel, yLabel);
//	zoe		gc.DrawString(label, font, drawBrush, xLabel, yLabel, drawFormat);
			String[] lines = label.split("\n",0);
//		zoe	yLabel +=  (int)font.SizeInPoints * (lines.length + 3);
			yLabel +=  (int)font.getSize2D() * (lines.length + 3);
			gc.setTransform(beforeTS);
			
		}

      /// <summary>
      /// drawWarning(): draws a warning message on the canvas
      /// </summary>
      /// <param name="warn"></param>
//      public void drawWarning(String warn)
//      {
//          gc.ResetTransform();
//          int cFudge = 8;            
//          float xc = width / 2;
//          float yc = height / 2;
//          gc.DrawString(warn, font, drawBrush, xc - cFudge, 2 * cFudge, drawFormat);
//      }
    

		/**
		 * drawGrid. Draws the axes and tickmarks on the canvas.
		 */
		public void drawGrid()
		{
			//-------------------------------------------------------------------
			// write the grid with tickmarks 
			// first draw X, Y grid and the N, S, E, W labels on the grid
			int cFudge	= 8;
			float inner	= 0.05F*Math.max(width,height);
			float outer	= 0.125F;
			float xc	= width/2;
			float yc	= height/2;
			gc.setColor(gridPen.getColor());
//		zoe	gc.DrawLine(gridPen, xc, yc-inner, xc, height*outer);				// x axis
//			gc.DrawLine(gridPen, xc, yc+inner, xc, height*(1.0F -outer) );	// x axis
//			gc.DrawLine(gridPen, xc-inner, yc, width*outer, yc);				// y axis
//			gc.DrawLine(gridPen, xc+inner, yc, width*(1.0F-outer), yc);		// y axis
			gc.drawLine((int) xc,(int)( yc-inner), (int)xc, (int)(height*outer));
			gc.drawLine( (int)xc,(int) (yc+inner),(int) xc, (int)(height*(1.0F -outer)));	// x axis
			gc.drawLine( (int)(xc-inner), (int)yc, (int)(width*outer),(int) yc);				// y axis
			gc.drawLine((int)(xc+inner),(int) yc,(int)(width*(1.0F-outer)), (int)yc);		// y axis
			gc.setColor(Color.WHITE);
			gc.setFont(font);
			gc.drawString("N", xc-cFudge, 2*cFudge);
			gc.drawString("S",  xc-cFudge, height-4*cFudge);				
			gc.drawString("E",  2*cFudge, yc-cFudge);		
			gc.drawString("W",  width-4*cFudge, yc-cFudge);
		
		}

		/**
		 * drawRuler(): draw a ruler on the canvas
		 */
		public void drawRuler()
		{
			// first draw X, Y grid and the N, S, E, W labels on the grid
			int cFudge	= 8;						
			float xc	= width/2;
			float yc	= height/2;
			// now draw decide on the tick size
			ScaleItem[] ruler = new ScaleItem []
			{
				new ScaleItem("0.5''", 1.0/120.0),
				new ScaleItem("1''", 1.0/60.0),
				new ScaleItem("2''", 2.0/60.0),
				new ScaleItem("5''", 5.0/60.0),
				new ScaleItem("10''", 1.0/6.0),
				new ScaleItem("20''", 2.0/6.0),
				new ScaleItem("1'", 1.0),
				new ScaleItem("2'", 2.0),
				new ScaleItem("5'", 5.0),
				new ScaleItem("10'", 10.0),
				new ScaleItem("20'", 20.0),
				new ScaleItem("30'", 30.0),
				new ScaleItem("1deg", 60.0),
				new ScaleItem("1.5deg", 90.0),
				new ScaleItem("2deg", 120.0)
			};
			double extent = Math.min(width,height);
			int ticks = 10;
			int i;
			for (i=0; i < ruler.length; i++) 
			{
				ticks = (int)Math.floor(extent / (ruler[i].size*ppm) );
				if ((ticks >= 4) && (ticks < 12) ) 	break;
			}
			if (i == ruler.length) i--;
			String label	= ruler[i].label;
			double tickSize = ruler[i].size * ppm;
            //------------------------
			// draw the tickmarks
            //------------------------
			int pos =  0;
			for (i = -ticks; i <= ticks; i++)
			{
				pos = (int) Math.floor(i*tickSize);
				gc.setColor(gridPen.getColor());
				//原来没有类型抓转换
				gc.drawLine(0,(int)(yc + pos),(int)cFudge,(int)(yc + pos));
				gc.drawLine(width,(int)( yc + pos),width-cFudge,(int)(yc + pos));
				gc.drawLine((int)(xc + pos),0,(int)(xc + pos),cFudge);
				gc.drawLine((int)(xc + pos),height,(int)(xc + pos),height-cFudge);
			}
			//-------------------------------------------------					
			// draw the tick and write the label at the center
            //-------------------------------------------------
			int x1	= xLabel;		
			int x2	= xLabel + (int)tickSize;
			int len	= 4;
			int yy  = yLabel+20;
			gc.setColor(rulerPen.getColor());
			gc.drawLine( x1, yy , x2, yy);
			gc.drawLine( x1, yy - len, x1, yy + len);
			gc.drawLine( x2, yy - len, x2, yy + len);
			xLabel += (int)(0.5*tickSize) - cFudge;
			this.drawLabel(label);
		}


		//=============================================================
		// Debug and printing routines
		//=============================================================
		/**
		 * addDebugMessage. Add a line to the current debug message string.
		 * @param s string to print
		 */
		public void addDebugMessage(String s)
		{
			debugMessage.append(s);
		}


		/**
		 * addDebugMessage. Add a line to the current debug message string.
		 */
		public void drawDebugMessage(int width,int height)
		{
			//drawLabel(debugMessage.ToString());
  //zoe        drawErrorMessage(debugMessage.toString(), width, height);
		}


		/**
		 * displayMessage. Draw the message on the screen.
		 * @param error String to display
		 */
		public void displayMessage(String error)
		{	
		//zoe	gc.ResetTransform();
//	zoe		gc.FillRectangle(new SolidBrush(Color.Black),0,0,width,height);
			gc.setColor(Color.BLACK);
			gc.fillRect(0, 0, width, height);
			gc.setColor(Color.WHITE);
			gc.setFont(new Font("Arial", Font.BOLD, 10));
//	zoe		gc.DrawString(error,new Font("Arial", 10),new SolidBrush(Color.White), 
//				10  ,10 ,new StringFormat());
		}

		//=============================================================
		// Support routines
		//=============================================================
		private class ScaleItem
		{
			public String label;
			public double size;
			public ScaleItem (String strLabel, double vArcminutes) 
			{
				label = strLabel;
				size  = vArcminutes;
			}
		}


		/**
		 * GetAffineTransform. Compute the affine transformation for the
		 * graphic context that maps the pixel coordinates of a Frame to
		 * the world coordinate system of the projection of the canvas.
		 * 为图形上下文计算仿射变换，用以实现Frame的像素坐标到画布上的世界坐标系的投影映射
		 * @param coord The input astrometric transformation of the Frame
		 * @return Matrix for the canvas graphic context
		 */
		public AffineTransform getAffineTransform(Coord coord)
		{
			// compute the corners in both coordinate systems   
			//计算Frame裁剪后的四个角的坐标
			Point2D.Float[] p	= SdssConstants.FieldGeometry(true);
			int len		= p.length;	
			Point2D[] g	= new Point2D.Float[len];
			Coord fc	= new Coord();
			if (SdssConstants.isSdss)
              fc.copy(coord);
			    
			fc.scale	= 1.0;
			
			for(int i=0;i<len;i++)
			{              
              if(SdssConstants.isSdss)//将四个角的像素坐标变换为ra,dec
                  fc.FrameToEq((float)p[i].getX(),(float) p[i].getY());  // SDSS
              
              g[i] = proj.EqToScreen(fc.ra, fc.dec, 0.0F);  
			}
			// build up the terms for solving the best fit affine transformation
			//建立求解最佳拟合仿射变换的项
			float qu=0, qv=0, qux=0, quy=0, qvx=0, qvy=0, 
				  qx=0, qy=0, qxx=0, qyy=0, qxy=0;

			//ImgCutout 通过Frame->Eq->Screen
			for (int i = 0; i < 4; i++)
			{
              qu += g[i].getX() / 4;
              qv += g[i].getY() / 4;
              qux += g[i].getX() * p[i].getX() / 4;
              qvx += g[i].getY() * p[i].getX() / 4;
              quy += g[i].getX() * p[i].getY() / 4;
              qvy += g[i].getY() * p[i].getY() / 4;
              qx += p[i].getX() / 4;
              qy += p[i].getY() / 4;
              qxx += p[i].getX() * p[i].getX() / 4;
              qxy += p[i].getX() * p[i].getY() / 4;
              qyy += p[i].getY() * p[i].getY() / 4;
			}

			// we seek the affine transformation in the form (see Petzold book p. 292)
			//		x' = sx*x + rx * y + dx
			//		y' = ry*x + sy * y + dy;
			double d = det3x3(new double[] {1.0, qx, qy,  qx, qxx, qxy,  qy, qxy, qyy});
			double dx = det3x3(new double[] { qu, qx, qy, qux, qxx, qxy, quy, qxy, qyy})/d;
			double sx = det3x3(new double[] {1.0, qu, qy,  qx, qux, qxy,  qy, quy, qyy})/d;
			double rx = det3x3(new double[] {1.0, qx, qu,  qx, qxx, qux,  qy, qxy, quy})/d;
			double dy = det3x3(new double[] { qv, qx, qy, qvx, qxx, qxy, qvy, qxy, qyy})/d;
			double ry = det3x3(new double[] {1.0, qv, qy,  qx, qvx, qxy,  qy, qvy, qyy})/d;
			double sy = det3x3(new double[] {1.0, qx, qv,  qx, qxx, qvx,  qy, qxy, qvy})/d;
//zoe			if (debug && false)
//			{
//				debugMessage.append("Affine: ["+coord.info+"][ "+sx+", "+ry+",  "+rx+",  "+sy+",  "+dx+",  "+dy+"]\n");        
//				debugMessage.append("  [ W "+(p[2].getX()-p[0].getX())+", H "+(p[2].getY()-p[0].getY())+"]\n");        
//				debugMessage.append("  [ W "+(g[2].getX()-g[0].getX())+", H "+(g[2].getY()-g[0].getY())+"]\n");        
//			}           
			return new AffineTransform(sx,ry,rx,sy,dx,dy);
		}
		
      
		/**
		 * 计算一个3x3矩阵的行列式
		 * @param 包含9个double值的向量, 代表矩阵中的元素
		 * @return 行列式结果
		 */
		private double det3x3(double[] a)
		{
			return (a[0]*a[4]*a[8] + a[1]*a[5]*a[6] + a[2]*a[3]*a[7] 
				- a[0]*a[7]*a[5] - a[3]*a[1]*a[8] - a[6]*a[4]*a[2]);
		}

		/**
		 * GetV3North. Calculate the Cartesian normal vector 
		 * corresponding to the point (ra,dec).
		 * @param ra_ Right ascension in degrees
		 * @param dec_ Declination in degrees
		 * @return Array of 3 doubles, a 3-vector
		 */
		private double[] GetV3Normal(double ra_, double dec_) 
		{
			double[] n = new double[3];	
			n[0] =  Math.cos(dec_ * Coord.D2R) * Math.cos(ra_ * Coord.D2R);
			n[1] =  Math.cos(dec_ * Coord.D2R) * Math.sin(ra_ * Coord.D2R);
			n[2] =  Math.sin(dec_ * Coord.D2R);
			return n;
		}


		/**
		 * GetV3North. Calculate the Cartesian normal vector 
		 * corresponding to the North direction at the point (ra,dec).
		 * @param ra_ Right ascension in degrees
		 * @param dec_ Declination in degrees
		 * @return Array of 3 doubles, a 3-vector
		 */
		private double[] GetV3North(double ra_, double dec_) 
		{
			double[] u = new double[3];	
			u[0] = -Math.sin(dec_ * Coord.D2R) * Math.cos(ra_ * Coord.D2R);
			u[1] = -Math.sin(dec_ * Coord.D2R) * Math.sin(ra_ * Coord.D2R);
			u[2] =  Math.cos(dec_ * Coord.D2R);
			return u;
		}

		/**
		 * GetV3West. Calculate the Cartesian normal vector 
		 *  corresponding to the Est direction at the point (ra,dec).
		 * @param ra_ Right ascension in degrees
		 * @param dec_ Declination in degrees
		 * @return Array of 3 doubles, a 3-vector
		 */
		private double[] GetV3West(double ra_, double dec_) 
		{
			double[] w = new double[3];	
			w[0] = -Math.sin(ra_ * Coord.D2R);
			w[1] =  Math.cos(ra_ * Coord.D2R);
			w[2] =  0;
			return w;
		}


		/**
		 * drawAxis. Debugging, draws the N direction in red, the E in green.
		 * @param ra_ Right ascension of center
		 * @param dec_ Declination of center
		 */ 
		public void drawAxis(double ra_, double dec_)
		{
			Point2D p = proj.EqToScreen(ra_,dec_,0.0F);
			Point2D n = proj.EqToScreen(ra_,dec_+0.1F,0.0F);
			Point2D w = proj.EqToScreen(ra_+0.1F,dec_,0.0F);
			gc.setColor(specPen.getColor());
			gc.drawLine((int)p.getX(), (int)p.getY(), (int)n.getX(), (int)n.getY());
			gc.setColor(outlinePen.getColor());
			gc.drawLine((int)p.getX(), (int)p.getY(), (int)w.getX(), (int)w.getY());
//	zoe		gc.DrawLine(specPen,p,n);
//			gc.DrawLine(outlinePen,p,w);
		}

	/**
	 * regionColor. Returns the drawing color of a given region type.
	 * @param type Region type as a string in upper case
	 * @param isMask 0 if not a mask, 1 if mask
	 * @return Color.
	 */
      public Color regionColor(String type, int isMask)
      {
          //---------------------------
          // set the appropriate color
          //---------------------------
          Color cl;
          if(type.equalsIgnoreCase("CAMCOL"))
        	  cl = Color.white;
          else if(type.equalsIgnoreCase("CHUNK")) cl = Color.ORANGE;
          else if(type.equalsIgnoreCase("HOLE")) cl = new Color(0, 255, 255);
          else if(type.equalsIgnoreCase("TIHOLE")) cl = new Color(128, 128, 255);
          else if(type.equalsIgnoreCase("PLATE"))cl = Color.WHITE;
          else if(type.equalsIgnoreCase("PRIMARY"))cl = Color.GREEN;
          else if(type.equalsIgnoreCase("RUN"))cl = Color.BLUE;
          else if(type.equalsIgnoreCase("SECTOR")) cl = Color.YELLOW;
          else if(type.equalsIgnoreCase("SECTORLET")) cl = Color.YELLOW;
          else if(type.equalsIgnoreCase("SEGMENT"))cl = new Color(128, 255, 128);
          else if(type.equalsIgnoreCase("SKYBOX"))cl = new Color(128, 255, 128);
          else if(type.equalsIgnoreCase("STRIPE"))cl = new Color(0, 255, 255);
          else if(type.equalsIgnoreCase("TIGEOM"))cl = new Color(0, 255, 0);
          else if(type.equalsIgnoreCase("TILE"))cl = new Color(0, 255, 255);
          else if(type.equalsIgnoreCase("TIPRIMARY")) cl =new Color(155, 200, 100);
          else if(type.equalsIgnoreCase("TILEBOX"))cl = new Color(255, 255, 0);
          else if(type.equalsIgnoreCase("WEDGE"))cl = new Color(255, 255, 0);
          else  cl = Color.WHITE;
//  zoe        switch (type)
//          {
//              case "CAMCOL":
//                  cl = Color.WHITE;
//                  break;
//              case "CHUNK":
//                  cl = Color.ORANGE;
//                  break;
//              case "HOLE":
//                  cl = new Color(0, 255, 255);
//                  break;
//              case "TIHOLE":
//                  cl = new Color(128, 128, 255);
//                  break;
//              case "PLATE":
//                  cl = Color.WHITE;
//                  break;
//              case "PRIMARY":
//                  cl = Color.GREEN;
//                  break;
//              case "RUN":
//                  cl = Color.BLUE;
//                  break;
//              case "SECTOR":
//                  cl = Color.YELLOW;
//                  break;
//              case "SECTORLET":
//                  cl = Color.YELLOW;
//                  break;
//              case "SEGMENT":
//                  cl = new Color(128, 255, 128);
//                  break;
//              case "SKYBOX":
//                  cl = new Color(128, 255, 128);
//                  break;
//              case "STAVE":
//                  cl = new Color(0, 255, 255);
//                  break;
//              case "STRIPE":
//                  cl = new Color(0, 255, 0);
//                  break;
//              case "TIGEOM":
//                  cl = new Color(255, 128, 128);
//                  break;
//              case "TILE":
//                  cl = new Color(0, 255, 255);
//                  break;
//              case "TIPRIMARY":
//                  cl =new Color(155, 200, 100);
//                  break;
//              case "TILEBOX":
//                  cl = new Color(255, 255, 0);
//                  break;
//              case "WEDGE":
//                  cl = new Color(255, 255, 0);
//                  break;
//              default:
//                  cl = Color.WHITE;
//                  break;
//          }
          if (isMask > 0) cl = new Color(255, 0, 0);
          return cl;
      }
  }

