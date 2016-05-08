package edu.gzu.image.test;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

import org.fastds.dbutil.DBConnection;
import org.junit.Test;
import org.scidb.jdbc.IStatementWrapper;

import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi;

import edu.gzu.image.Hex2Image;
import edu.gzu.image.ImgCutout;


public class ImageTest {
	public static void main(String[] args) {
//		test1();
//		test2();
	}
	public static void test1()
	{
		try {
			ImgCutout cutout = new ImgCutout();
			double ra_ = Double.parseDouble("159.815");
			double dec_ = Double.parseDouble("-0.655");
			double scale_ = 0.396127;
			int width_ = 2048;
			int height_ =2048;
			String opt_  = "C";
			String  query_	= "";
			String  imgtype_ = "jpeg";
			String  imgfield_ = "";  
			byte[] buffer = cutout.GetJpeg(ra_, dec_, scale_, width_, height_, opt_, query_, imgtype_, imgfield_);
			FileOutputStream fos = new FileOutputStream("C:\\Users\\zhouyu\\Desktop\\result.jpeg");
			fos.write(buffer,0,buffer.length);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void test2()
	{
		try {
			ImgCutout cutout = new ImgCutout();
			double ra_ = Double.parseDouble("337.138");
			double dec_ = Double.parseDouble("-0.951");
			double scale_ = 0.396127;
			int width_ = 2048;
			int height_ =2048;
			String opt_  = "C";
			String  query_	= "";
			String  imgtype_ = "jpeg";
			String  imgfield_ = "";  
			byte[] buffer = cutout.GetJpeg(ra_, dec_, scale_, width_, height_, opt_, query_, imgtype_, imgfield_);
			FileOutputStream fos = new FileOutputStream("C:\\Users\\zhouyu\\Desktop\\result1.jpeg");
			fos.write(buffer,0,buffer.length);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//	@Test
	public  void testNew()
	{
		
		try {
			String hex = "";
			InputStream is = new Hex2Image().hex2Binary(hex);
			MemoryCacheImageInputStream input = new MemoryCacheImageInputStream(is);
			ImageReader reader = new J2KImageReaderSpi().createReaderInstance();
			reader.setInput(input);
			BufferedImage tile = reader.read(0);
			System.out.println("tile="+tile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	@Test
	public void testDB()
	{
		Connection conn = null;
		ResultSet res = null;
		String sql = "select fieldID from Frame where fieldID<1237645876861337600";
		int i = 0;
		try {
			conn = DBConnection.getConnection();
			Statement st = conn.createStatement();// 27DBConnection.getConnection().createStatement();
			IStatementWrapper staWrapper = st.unwrap(IStatementWrapper.class);
			staWrapper.setAfl(false);
			res = st.executeQuery(sql);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			while(!res.isLast())
			{
				i++;
				System.out.println(res.getLong("fieldID")+"...."+i);
//				System.out.println(res.getString("img")+"...."+i);
				res.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
