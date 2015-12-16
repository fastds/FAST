package edu.gzu.service;

import java.io.IOException;
import java.io.InputStream;

import edu.gzu.dao.ImageDao;
import edu.gzu.domain.PhotoObjAll;
import edu.gzu.domain.PrimaryObject;
import edu.gzu.image.Hex2Image;

public class ImageService {
	private ImageDao dao = new ImageDao();
	
	public byte[] getSpecObjById(long id) {
		String spec = dao.getSpecObjById(id);
		Hex2Image image = new Hex2Image();
		InputStream is =  image.hex2Binary(spec);
		byte[] buffer = new byte[1024*1024];
		try {
			is.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
	/**
	 * 
	 * @param ra 
	 * @param dec
	 * @param radius
	 * @return 通过PhotoObjAll的id查询出SpecObjAll的id，另见本类getPhotoObjAll(double ra,double dec,double radius)
	 */
	public long getSpecObjID(double ra,double dec,double radius)
	{
		PhotoObjAll poa = getPhotoObjAll(ra, dec, radius);
		if(poa==null)
			return -1;
		long photoObjAllID = poa.getId();
		return dao.getSpecObjID( photoObjAllID);
	}
	/**
	 * 
	 * @param ra
	 * @param dec
	 * @param radius
	 * @return 返回PhotoObjAll对象，Nearest Obj
	 */
	public PhotoObjAll getPhotoObjAll(double ra,double dec,double radius)
	{
		return  dao.getPhotoObjAll(ra, dec, radius);
	}
	public PrimaryObject getPrimaryObject(double ra,
			double dec, double radius) {
		
		return dao.getPrimaryObject(ra,dec,radius);
	}
	
	

}
