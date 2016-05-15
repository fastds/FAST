package edu.gzu.image;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.fastds.dao.ExQuery;



/// <summary>
/// DR1 Cutout returns a JPEG image from the SDSS image archive.
/// The image can be up to 2048x2048 piexls mosaiced（马赛克） from the based data.
/// The image can be zoomed from 100 pixels per degree to 1,000,000 pixels per degree.
/// It is rotated so that north is up.
/// The image optionally contains PhotoObj, SpecObj and Target icons and a label and grid.
/// The image may be inverted.
/// </summary>
/**
 * 从数据库中获取、生成经过剪裁的星体图像的jpeg格式的图片
 * 基本数据中image最大像素到2048x2048
 * 会进行旋转操作使得北方朝上
 * 图像可选包含PhotoObj, SpecObj 和 Target 图表  还有label和grid
 *
 */
  public class ImgCutout 
  {
      //-------------------------------------		
      // Limits of the input parameters 对输入参数的限制
      //-------------------------------------
      final int max_width = 2048;
      final int max_height = 2048;
      final int min_width = 64;
      final int min_height = 64;
      final double max_scale = 2400.0;
      final double min_scale = 0.015;

      //-------------------------------------
      // Input parameters  输入参数
      //-------------------------------------
      private double ra, dec;				 // 标准化 [ra,dec]
      private int width, height;			//   图像大小
      private double scale;					// arcseconds/pixel  角秒/像素

      // private StringBuilder query;	// It can be a SQL SELECT query or  a string like SR(12,20) 
      // which will mark Stars with magnitudes(星等) between 12 and 20 in the R band												
      // The code follows the pattern:
      // objType: S | G | P  --	S for Stars 星体
      //						G for Galaxies 星系
      //						P for Both Stars and Galaxies	 星体和星系																						
      // bad: u | g | r | i | z | a --  will select objects with
      //         					band  BETWEEN low_mag AND high_mag
      // 
      // if band is 'a' then it will look for all the objects 
      // with values between low_mag and high_mag for any band	(compositions of OR)
      // if band and magnitude ranges are not specified then will mark only Stars, 
      // Galaxies, or PhotoPrimary objects 
      //-------------------------------------
      // Derived parameters  派生参数
      //-------------------------------------
      // zoom: the image pyramid level [0..max_zoom]  图像层级
      // imageScale: the additional scaling of   图片比例
      // the image beyond walking the pyramid
      //-------------------------------------
      private int zoom;					    // zoom level 
      private double ppd;					// pixels per degree   每度的像素
      private double imageScale;			// scale of the image  图像的比例
      private String imgtype;               // the image type 图像类型
      private float zoomScale;			   	// reference Coordinate scale
      private float size;					// size of the symbols drawn
      private double radius, fradius;		// radius for field searches  对于field搜索的半径
      private Hashtable<Long,Coord> cTable;	// array of astrometry transformations 天体测量数据转换数组		
      private StringBuilder query;			// It can be a SQL SELECT query or  一个查询语句

      //-------------------------------------
      // Debug support
      //-------------------------------------
      private StringBuilder dbgMsg = new StringBuilder();
      private StringBuilder errMsg = new StringBuilder();
      private boolean debug = true;
      //-------------------------------------
      // Drawing options  绘制选项
      //-------------------------------------
      private boolean
      draw2Mass = false,
      drawApogee = false,  		// 画最高点/远地点
      drawList		= false,
      drawQuery		= false, 
      drawPlate = false,
      drawPhotoObjs = false,
      drawSpecObjs = false,
      drawTargetObjs = false,
      drawGrid = false,
      drawRuler = false,		//绘制标尺
      drawLabel = false,
      drawOutline = false,
      drawBoundingBox = false,
      drawField = false,
      drawMask = false,        
      drawFrames = false,
      invertImage = false, 		//反转图像
      drawRegion = false,
      isRegionList = false,		//是否是区域集合
      fillRegion = false;		//填充区域
      //-------------------------------------
      // region drawing support   对区域绘制的支持
      //-------------------------------------
      private String regionList = "";
      private String regionTypes = "";
      private String viewPort;
      //     Array ridlist;
      private String imgfield = null;
      //-------------------------------------

      //-------------------------------------
      // Graphics canvas   画布
      //-------------------------------------
      public SDSSGraphicsEnv canvas = null;
      //-------------------------------------

      //-------------------------------------
      // DataBase properties   数据库属性
      //-------------------------------------
      private String sConnect = null;
      private String sDataRelease = null;
      private int sDR = -1;  /// Added for releases after dr7


      public Hashtable<Long,Coord> getCTable()
      {
    	  return this.cTable;
      }
      /**
       * Constructor and getting connection strings for databases  
       * 构造器，获取数据库连接字符串
       */
	  public ImgCutout() throws Exception
	  {
	
	      sDataRelease = SdssConstants.getSDataRelease();
	      if (sDataRelease == null)
	          throw new Exception("DataRelease keyword not found or invalid.\n" +
	          "Please check AppSettings in the Web.config file!");
	      sDR = SdssConstants.getSDR();
	
	  }

	  // Revision from CVS
	  public static String Revision = "$Revision: 1.26 $";
	  // Return CVS revision numbers
	  public String[] Revisions()
	  {
	      String[] revs = { "ImgCutout:WebServices " + ImgCutout.Revision, };
	      return revs;
	  }
    ///	optionally draws circles/squares/crosses around photo/spectro/target objects
	/// 可选的针对 photo/spectro/target 对象绘制圆圈、方块、十字
    /**
     * GetJpeg retuns a Jpeg of the image around a point at the specified zoom.
     * It throws an authorization exception if it cannot connect to the database.
     * @param ra_ 基于J2000天球参考坐标系的赤经，范围： [0...360]double类型
	 * @param dec_ 基于J2000天球参考坐标系的赤纬，范围：[-90..90]double类型
	 * @param scale_ 角秒/像素(0.3961267 is native 1:1 for SDSS)角秒，一度的三千六百分之一。<br/>
	 * 					(0.3961267 对应 1:1，默认为0.396126761"/pixel.)，范围：0.015 .. 60.
	 * @param width_ 图像像素宽度，范围：[64 .. 2048]的整数
	 * @param height_ 图像像素高度，范围：[64 .. 2048]的整数
	 * @param opt_ 绘制选项，字符串表示，有如下几个绘制请求：<br/>
	 * 				'P': draws Photo Objects   绘制Photo对象
     *				'S': 绘制具有光谱信息的天体对象
     *				'T': draws Target Objects  绘制目标对象
     *				'G': 绘制单位刻度
     *				'R': 绘制标尺
     *				'L': 绘制信息标签
     *				'B': 绘制包含星体轮廓的最小矩形框
     *				'O': 绘制星体轮廓
     *				'M': 绘制遮罩区
     *				'Q': 绘制光谱观测孔径边界 
     *				'I': 将图像反相
	 * @param query_
	 * @param imgtype_ 图像类型
	 * @param imgfield_
	 * @return JPEG格式图像的字节数组 byte[]
     */
      public byte[] GetJpeg(
          double ra_,						// right ascension in J2000 degrees
          double dec_, 						// declination in J2000 degrees
          double scale_,						// arcsec/pixel (0.3961267 is native 1:1 for SDSS)
          int width_,						// image width  (in pixels)
          int height_,					// image height (in pixels)
          String opt_,						// drawing options
          String query_,
          String imgtype_,
          String imgfield_
          )
      {
          return GetJpegQuery(ra_, dec_, scale_, width_, height_, opt_, query_, imgtype_, imgfield_);
      }


 /**
  * 
  * @param ra_ 基于J2000天球参考坐标系的赤经
  * @param dec_ 基于J2000天球参考坐标系的赤纬
  * @param scale_ 角秒/像素(0.3961267 is native 1:1 for SDSS)角秒，一度的三千六百分之一。(0.3961267 对应 1:1)
  * @param width_ 图像像素宽度
  * @param height_ 图像像素高度
  * @param opt_ 绘制选项
  * @param query_
  * @param imgtype_ 图像类型
  * @param imgfield_
  * @return 图像的字节数组 byte[]
  */
  public byte[] GetJpegQuery(
      double ra_,			// right ascension in J2000 degrees 基于J2000天球参考坐标系的赤经
      double dec_, 			// declination in J2000 degrees 基于J2000天球参考坐标系的赤纬
      double scale_,		// arcsec/pixel 角秒/像素(0.3961267 is native 1:1 for SDSS)角秒，一度的三千六百分之一。(0.3961267 对应 1:1)
      int width_,			// image width  (in pixels)图像像素宽度
      int height_,			// image height (in pixels)图像像素高度
      String opt_,			// drawing options 绘制选项
      String query_,		// mark objects selected by a query, or Region list  通过查询或区域列表选择标记对象
      String imgtype_,
      String imgfield_
      )
  {
      try{

          getImageCutout(ra_, dec_, scale_, width_, height_, opt_, query_, imgtype_, imgfield_);
      }
      catch (Exception e)
      {
    	  e.printStackTrace();
      }
      //返回图像  转到canvas对象对应的类 SdssEvn？？查看
      return (canvas.getBuffer());							// return image
  }

	//获取绘制的图像剪裁
  private void getImageCutout(
      double ra_,					// right ascension in J2000 degrees
      double dec_, 					// declination in J2000 degrees
      double scale_,				// arcsec/pixel (0.3961267 is native 1:1 for SDSS)
      int width_,					// image width  (in pixels)
      int height_,					// image height (in pixels)
      String opt_,					// drawing options
      String query_,				// mark objects selected by a query, or Region list
      String imgtype_,
      String imgfield_
      ) throws Exception {

          try
          {
              //-------------------------------------
              //  验证输入参数的值的范围是否合法
              //-------------------------------------
              validateInput(ra_, dec_, scale_, height_, width_, opt_, query_, imgtype_, imgfield_);
              SdssConstants.isSdss = true;
              setImageScaleAndZoom();
              //---------------------------------------------
              // set SQL search radii（半径范围、半径） for fields and objects
              //---------------------------------------------
              radius = 60.0 * 0.5 * Math.sqrt(width * width + height * height) / ppd;
              fradius = SdssConstants.FrameHalfDiag + radius;//8.4+
              //---------------------------------------------------
              // 初始化canvas，connection，projection
              //---------------------------------------------------
//             if (drawQuery) validateQuery(query_); old
             	canvas = new SDSSGraphicsEnv(width, height, imageScale, ppd, debug, imgtype);
  //zoe            connectToDataBase();

              byte oflag = 0;
              if (drawPhotoObjs) oflag |= SdssConstants.pflag;
              if (drawSpecObjs) oflag |= SdssConstants.sflag;
              if (drawTargetObjs) oflag |= SdssConstants.tflag;
              //为canvas 初始化切面投影成员对象
              canvas.InitializeProjection(ra, dec, "TAN"); ///TAN or STR     
//              canvas.GetViewPort();  ？？？？？？？什么用处？

              imgfield = imgfield_;

              if (drawFrames | drawField) getFrames();
              
//         zoe         if (drawQuery) getQueryObjects(query_);	
				//覆盖选项
//            zoe     OverlayOptions options = new OverlayOptions(SqlConn, canvas, size, ra, dec, radius, zoom, fradius);
              OverlayOptions options = new OverlayOptions(null, canvas, size, ra, dec, radius, zoom, fradius);
             
//                  if (drawApogee) options.getApogeeObjects();
              if (drawField) options.getFields(cTable);
              if (drawPhotoObjs | drawSpecObjs | drawTargetObjs) options.getObjects(drawPhotoObjs, drawSpecObjs, drawTargetObjs);
              if (drawBoundingBox | drawOutline) options.getOutlines(drawBoundingBox, drawOutline, cTable);
              if (drawMask) options.getMasks();
              if (drawLabel) options.getLabel(sDataRelease, scale, imageScale);
              if (drawPlate) options.getPlates();
//                  if (drawList) getListObjects();
              if (drawRuler)
              {
                  canvas.drawRuler();
                  drawGrid = false;
              }
              if (drawGrid)
              {
                  canvas.drawGrid();
                  canvas.drawRuler();
              }
              if (invertImage)
              {
                  canvas.Invert();
              }
          }
          catch (Exception e) { throw e; }
          finally { 
      //zoe  	  disconnectFromDataBase(); 
        	  }
      
  }

   private void setImageScaleAndZoom() {
	   zoom = 0;
       ppd = 3600.0 / scale;
       imageScale = ppd / SdssConstants.getPixelsPerDegree();
       while (zoom < SdssConstants.getMaxZoom() & imageScale <= .5)
       {
           zoom++;							 // go higher in the pyramid
           imageScale *= 2;					 // change the scaling accordingly
       }
       zoomScale = (float)Math.pow(2, zoom);//pow:对数  // set the scale according to the real zoom 根据真实的zoom设置scale
       size = (float)((zoom > 3) ? 6 : 12 * imageScale);
	
}

Coord coord = null;	// coord of current tile  

   /**
    * 从数据库中获取图像数据并将其添加到绘制环境中进行绘制
    */
   private void getFrames() throws Exception
   {
		int zoom10x = SdssConstants.zoom10(zoom);
		System.out.println("ImgCutout.getFrames(): ra:"+ra+",dec:"+dec+",zoom10x:"+zoom10x+",zoom:"+zoom+",fradius:"+fradius+",scale"+scale);
		StringBuilder sQ = new StringBuilder();
		ResultSet rs = null;
	
		try
		{
			   String sql = Functions.fGetNearbyFrameEq(ra, dec, fradius, zoom10x);
			   ExQuery exQuery = new ExQuery();
			   System.out.println("ImgCutout.getFrame()--->sql:"+sql);
			   rs = exQuery.aqlQuery(sql);
			  
			   cTable = new Hashtable<Long,Coord>();
			   if(rs == null || rs.wasNull())
			   {
				   canvas.addDebugMessage("Requested (ra, dec) is outside the SDSS footprint. \n");
				   canvas.drawDebugMessage(width, height);
				   return;
			   }
			   while(!rs.isAfterLast())
			   {
				   String img = rs.getString("img");
				   double a = rs.getFloat("a");
				   double b = rs.getFloat("b");
				   double c = rs.getFloat("c");
				   double d = rs.getFloat("d");
				   double e = rs.getFloat("e");
				   double f = rs.getFloat("f");
				   double node = rs.getFloat("node");
				   double incl = rs.getFloat("incl");
				   long fieldID = rs.getLong("fieldID");
				   String info = Functions.fSDSS(fieldID);
		//		   System.out.println("a:"+a+",b:"+b+",c:"+c+",d:"+d+",e:"+e+",f:"+f+",node:"+node+",incl:"+incl+",info:"+info);
				   coord = new Coord(
						   a,			// a 天体测量转换系数
		                   b,			// b 天体测量转换系数
		                   c,			// c 天体测量转换系数
		                   d,			// d 天体测量转换系数
		                   e,			// e 天体测量转换系数
		                   f,			// f 天体测量转换系数
		                   node,		// node 天体测量转换系数
		                   incl, 		// inclination 倾角，天体测量转换系数
		                   zoomScale,	// zoomScale 缩放比例
		                   info			// info 该天区对象对应的SDSS编号，调试用
		                   );
				   if (debug)
			       {
			           canvas.addDebugMessage("info="+ coord.info+" "+fieldID+"\n");
			       }
				   //通过一系列天体测量转换系数构造仿射变化矩阵
			       coord.m = canvas.getAffineTransform(coord);
			       cTable.put(fieldID, coord);
		
			       if (drawFrames)
			       {
			           //-----------------------------------------
			           // fetch the tile into the memory stream
			           //-----------------------------------------
			    	   InputStream is = new Hex2Image().hex2Binary(img);
			    	   System.out.println("img string length："+img.length());
			    	   BufferedImage tile = ImageIO.read(is);
			    	   System.out.println("tile="+tile);
						
			           if (tile != null)
			           {
			         	  
			               canvas.drawFrame(coord, tile);
		
			           }
			       }
				   rs.next();
			   }
		   
		  }
		  catch (Exception exp)
		  {
			 exp.printStackTrace();
		     showException("Exception in getFrame()",sQ.toString(),exp);
		  }
		  finally {
		    //zoe  disconnectFromDataBaseImage();
			  if(rs!=null)
			  {
				  rs.close();
			  }
		  }
  
  }


      //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
      //%%%%%%%%%%%%%  实用工具      %%%%%%%%%%%%%%%
      //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

	/**
	 *  validateInput(). Validate the range limits the input parameters.
	 */
   /**
    * 检查输入的查询参数的范围，将参数取值控制在合理范围内
    * @param ra_ 赤经，范围：[0°-360]，单位：度
    * @param dec_ 赤纬，范围：[-90,90]，单位：度
    * @param scale_ 缩放比例，范围：[0.015,60.0],单位：arcsec/pixel
    * @param height_ 高度，单位：像素
    * @param width_ 宽度，单位：像素
    * @param opt_ 绘制选项
    * @param query_ 查询语句
    * @param imgtype_ 查询图像类型
    * @param imgfield_ 
    */ 
     private void validateInput(double ra_, double dec_, double scale_,
                                 int height_, int width_, String opt_,
                                 String query_, String imgtype_, String imgfield_)
      {
          //标准化 ra dec
          ra = ra_;
          dec = dec_;
          dec = dec % 180;					// bring dec within the circle
          if (Math.abs(dec) > 90)			// if it is "over the pole",
          {
              dec = (dec - 90) % 180;		// bring int back to the [-90..90] range
              ra += 180;					// and go 1/2 way round the globe
          }
          ra = ra % 360;					// bring ra into [0..360]
          if (ra < 0) ra += 360;

          for (int i = 0; i < opt_.length(); i++)
          {
              char c = opt_.charAt(i);
              switch (c)
              {
                  case 'B': drawBoundingBox = true;
                      break;
                  case 'C': drawFrames = true;
                      break;
                  case 'F': drawField = true;
                      break;
                  case 'G': drawGrid = true;
                      break;
                  case 'I': invertImage = true;
                      break;
                  case 'L': drawLabel = true;
                      break;
                  case 'M': drawMask = true;
                      break;
                  case 'O': drawOutline = true;
                      break;
                  case 'P': drawPhotoObjs = true;
                      break;
                  case 'S': drawSpecObjs = true;
                      break;
                  case 'T': drawTargetObjs = true;
                      break;
                  case 'Q': drawPlate = true;
                      break;
                  case 'X': draw2Mass = true;
                      break;
                  case 'A': drawApogee = true;
                      break;
                  case '?': debug = true;
                      break;
              }
          }


          if (query_.length() > 0) drawQuery = true;

          //--------------------------------
          // clip height, width and ppd
          // 对宽、高、ppd的剪裁
          //--------------------------------
          if(!draw2Mass)
          {
        	  height = Math.max(min_height, Math.min(max_height, height_));
              width = Math.max(min_width, Math.min(max_width, width_));
              scale = Math.max(min_scale, Math.min(max_scale, scale_));
              imgtype = imgtype_;
          }
          
      }
      


    

      //*******************************************************************************************
      // This section is added since DR8 to get Jpeg Images from the cas directly        
      //*******************************************************************************************
 /*  zoe   [WebMethod(BufferResponse = false,
      Description = "Returns the bytes of the Jpeg image for a given pointing"
      + "<br><b>Input 1:</b> run in degrees (double)"
      + "<br><b>Input 2:</b> Dec in degrees (double)"
      + "<br><b>Input 3:</b> Scale, in arcsec/pixel (double)"
      + "<br><b>Input 4:</b> Width in pixels (int)"
      + "<br><b>Output:</b> Image (byte[])")]*/
      /*
      public byte[] GetJpegImg(
          String run,
          String camcol,
          String field,
          String zoom
          )
      {
          byte[] bytes = null;

          try
          {
              string cmdStr = "SELECT img FROM Frame WHERE zoom=" + zoom + " AND run="
                            + run + " AND camCol=" + camcol + " AND field=" + field;

          //    connectToDataBaseImage();
          //    SqlCommand cmd = new SqlCommand(cmdStr, SqlConnImage);
          //   reader = cmd.ExecuteReader();
              while (reader.Read())       // read the next record in the dataset
              {
                  MemoryStream theJpeg = new MemoryStream();
                  Image img_ = Image.FromStream(new MemoryStream((Byte[])reader[0]));
                  //img_.Save(theJpeg, ImageFormat.Jpeg);

                  EncoderParameters jpegParms = new EncoderParameters(1);
                  jpegParms.Param[0] = new EncoderParameter(System.Drawing.Imaging.Encoder.Quality, 95L);
                  img_.Save(theJpeg, GetEncoderInfo("image/jpeg"), jpegParms);
                  		
                  bytes = theJpeg.ToArray();
              }
              if (reader != null) reader.Close();    // close the reader.  
          }
          catch (Exception e)
          {
              throw e;
          }
          finally
          {
              disconnectFromDataBaseImage();
          }
          return bytes;
      }

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
      }*/

      ////// ############################################################################################
      //// this section is to draw query
      private enum QUERYTYPE
      {
          SQL,	// SQL
          FLT ,	// Filter
          LST,	// List of objects
          UKN 
      }

      /// <summary>
      /// getQueryObjects. Mark objects selected by a SQL query.
      /// </summary>
      /*
      private void getQueryObjects(String originalQ)
      {
          try
          {
              //set up the data adapter to get our data...
              DataSet ds = new DataSet();
              SqlDataAdapter da = new SqlDataAdapter(query.ToString(), SqlConn);
              da.Fill(ds, "MarkedObjects");
              if (ds.Tables["MarkedObjects"].Columns["error_message"] != null)
              {
                  throw new Exception("");
              }
              drawTable(ds.Tables["MarkedObjects"]);
          }
          catch (Exception e)
          {
              errMsg.AppendFormat("Wrong mark query. {0} \n\nYour input was:\n{1}", e.Message, originalQ);
          }
      }


      /// <summary>
      /// getListObjects(). Mark objects from a list.
      /// The function processes each line creating a DataTable of objects.
      /// The fisrt line in the list must have the name of the columns below.
      /// The list may include as many columns as desired as far as 
      /// RA and DEC are included.
      /// </summary>
      private void getListObjects()
      {
          // create the table that will hold query results
          DataTable dt = new DataTable("Coordinates");
          try
          {
              string[] querylines = query.ToString().Split(new char[] { '\n' });
              string line = cleanLine(querylines[0]);
              string[] columns = line.Split(new char[] { ' ', '\t', ',', ';' });
              for (int j = 0; j < columns.Length; j++)
                  dt.Columns.Add(columns[j]);
              for (int i = 1; i < querylines.Length; i++)
              {
                  line = cleanLine(querylines[i]);
                  string[] fields = line.Split(new char[] { ' ', '\t' });
                  if (fields.Length != columns.Length)
                  {
                      if (line.CompareTo("") == 0) continue;
                      else
                          throw new Exception("Wrong number of columns in row " + i.ToString());
                  }
                  DataRow newRow;
                  newRow = dt.NewRow();
                  for (int j = 0; j < fields.Length; j++)
                  {
                      newRow[columns[j]] = fields[j];
                  }
                  string sra = Convert.ToString(newRow["ra"]);
                  if (sra.IndexOf(":") != -1)
                      newRow["ra"] = Coord.hms2deg(sra).ToString();
                  string sdec = Convert.ToString(newRow["dec"]);
                  if (sdec.IndexOf(":") != -1)
                      newRow["dec"] = Coord.dms2deg(sdec).ToString();
                  dt.Rows.Add(newRow);
              }
              drawTable(dt);
          }
          catch (Exception e)
          {
              errMsg.AppendFormat("Wrong mark query. {0} \n\nYour input was:\n{1}", e.Message, query.ToString());
          }
      }


      /// <summary>
      /// drawTable(): Mark objects included in a table with a triangle 
      /// </summary>
      private void drawTable(DataTable dt)
      {
          double oRa = 0.0, oDec = 0.0;
          foreach (DataRow dataRow in dt.Rows)
          {
              oRa = Convert.ToDouble(dataRow["ra"]);
              oDec = Convert.ToDouble(dataRow["dec"]);
              canvas.drawQueryObj(oRa, oDec, size);
          }
      }
*/
      /**
       * getQueryType(). Determines the type of marking query. SQL, List of objects or Filter
       */
      private QUERYTYPE getQueryType(String qry)
      {
          if (qry.indexOf("SELECT") != -1) return QUERYTYPE.SQL; //SQL
          if ((qry.indexOf("RA") != -1) && (qry.indexOf("DEC") != -1)) return QUERYTYPE.LST; //List of objects
//       zoe?   if (qry.indexOf(new char[] { 'S', 'G', 'P' }) != -1) return QUERYTYPE.FLT;
          return QUERYTYPE.UKN;
      }
       /**
        * validateQuery(). Filtering queries of the form SR(10,20) are decomposed
        * and the query string is composed.
        */
      private boolean validateQuery(String myq)
      {
    	  boolean correctQuery = true;
          char objType;
          char band;
          double low_mag, high_mag;
          String query_ = myq.toUpperCase();
          QUERYTYPE queryType = getQueryType(query_);
          switch (queryType)
          {
              case SQL:
                  //string server_name = "skyservice";
                  String rows = "500000";
                  //string access = "ImgCutout";

                  /// If the query contains SELECT, the query is considered correct and the validation 
                  /// is postponed to be run by spExecute store procedure.
                  query = new StringBuilder();
                  query.append("EXEC spExecuteSQL '"+myq+"', '"+rows+"' ");
                  //This needs to be clarify
                  // EXEC spExecuteSQL '" + c +"  ', 100000,'" + server_name + "','" + windows_name + "','" + remote_addr + "','" + access + "'";
                  break;
              case LST:
                  query = new StringBuilder(myq);
                  drawQuery = false;
                  drawList = true;
                  break;
              case FLT:
                  try
                  {
                      query_ = query_.replace(" ", ""); //Get rid of the spaces		
                      objType = query_.charAt(0);
                      String t = check_and_set_ObjType(objType);
                      if (query_.length() > 1)
                      {
                          band = query_.charAt(1);
                          int openB = query_.indexOf('(');
                          int closeB = query_.indexOf(')');
                          //it should leave only the numbers
                          StringBuilder magnitudes = new StringBuilder(
                          query_.substring(openB + 1, closeB - openB - 1));
                          String m = magnitudes.toString();
                          String[] mag = m.split(",");
                          low_mag = Double.parseDouble(mag[0]);
                          high_mag = Double.parseDouble(mag[1]);
                          check_and_set_Band(band, t, low_mag, high_mag);
                      }
                  }
                  catch (Exception e)
                  {
                      errMsg.append("Wrong mark query. "+e.getMessage()+"\n\nYour input was:\n"+myq);
                      correctQuery = false;
                  }
                  break;
              default:
                  errMsg.append("Wrong mark query. String in wrong format \n\nYour input was:\n"+myq);
                  correctQuery = false;
                  drawQuery = false;
                  break;
          }
          return correctQuery;
      }

      /**
       * check_and_set_ObjType(). Determines what type of objects are being filtered and builds the proper SQL query.
       * Returns the Table alias to be used by the check_and_set_Band() function.
       */
      private String check_and_set_ObjType(char objType) throws Exception
      {
    	  String table = "", t = "";
          switch (objType)
          {
              case 'P':
                  table = "PhotoPrimary";
                  t = "p";
                  break;
              case 'G':
                  table = "Galaxy";
                  t = "g";
                  break;
              case 'S':
                  table = "Star";
                  t = "s";
                  break;
              default:
                  throw new Exception("Valid object types: P | S | G \n");
          }
          query = new StringBuilder("SELECT ");
          query.append("{0}.ra as ra, "+ t+".dec as dec \n");
          query.append("FROM dbo.fGetNearbyObjEq("+ra+","+dec+","+radius+") n JOIN "+table+" "+t+" \n");
          query.append("ON n.objId = "+t+".objId \n");
          return t;
      }


      /**
       * check_and_set_Band(). Determines what Band is being filtered and builds the proper SQL query.
       */
      private void check_and_set_Band(char band, String t, double low_mag, double high_mag) throws Exception
      {
          switch (band)
          {
              case 'U':
              case 'G':
              case 'R':
              case 'I':
              case 'Z':
                  query.append("WHERE "+t+"."+band+" BETWEEN "+low_mag+" AND "+high_mag+"");
                  break;
              case 'A':
                  query.append("WHERE "+t+".u BETWEEN "+low_mag+" AND "+high_mag+" \n");
                  query.append("OR "+t+".g BETWEEN "+low_mag+" AND "+high_mag+" \n");
                  query.append("OR "+t+".r BETWEEN "+low_mag+" AND "+high_mag+" \n");
                  query.append("OR "+t+".i BETWEEN "+low_mag+" AND "+high_mag+" \n");
                  query.append("OR "+t+".z BETWEEN "+low_mag+" AND "+high_mag+" \n");
                  break;
              default:
                  throw new Exception("Valid band values : A | U | G | R | I | Z \n");
          }
      }



      /**
       * cleanLine(): Replaces separation caracters ';' and ',' for spaces 
       * and then double spaces for only one space to avoid unnecessary
       * columns when building the attributes table.
       */
      private String cleanLine(String ln)
      {
          String line = ln;
          line = line.replace(";", " ");
          line = line.replace(",", " ");
          while (line.indexOf("  ") != -1)
          {
              line = line.replace("  ", " ");
          }
          line = line.trim();
          return line;
      }

      public void showException(String sFunction, String sQuery, Exception e) throws Exception
      {
          StringBuilder msg = new StringBuilder();
          msg.append(""+sFunction+" has failed:\n"+sQuery+"\n");
          msg.append("Exception Message:"+e.getMessage());
          throw new Exception(msg.toString()); //Actual exception
      }
      /*

     //###############################
     // Only for monitoring system 
     //###############################
      [WebMethod(BufferResponse = false, Description = "This is just to check availability of service")]
      public byte[] checkAvailability(
          double ra_,		// right ascension in J2000 degrees
          double dec_, 		// declination in J2000 degrees
          double scale_,	// arcsec/pixel (0.3961267 is native 1:1 for SDSS)
          Int32 width_,		// image width  (in pixels)
          Int32 height_,	// image height (in pixels)
          string opt_,		// drawing options
          string query_,
          string imgtype_,
          string imgfield_
          )
      {

          try
          {

              connectToAllDatabases();
          }
          catch (Exception e)
          {
              throw new Exception(e.Message);
          }
          finally { disconnectAllDatabases(); }

          try{
              
              getImageCutout(ra_, dec_, scale_, width_, height_, opt_, query_, imgtype_, imgfield_);
              return canvas.getBuffer();
          }
          catch (Exception e)
          {
              throw new Exception(e.Message);
          }
          finally { disconnectAllDatabases(); }
          
      }
      */
  }
