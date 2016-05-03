package org.fastds.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.fastds.dao.ImageDao;

import edu.gzu.domain.PhotoObjAll;
import edu.gzu.domain.PrimaryObject;
import edu.gzu.image.Hex2Image;

public class ImageService {
	private ImageDao dao = new ImageDao();
	
	public byte[] getSpecObjById(long id) {
		String spec = dao.getSpecObjById(id);
		Hex2Image image = new Hex2Image();
		ByteArrayInputStream bais = (ByteArrayInputStream) image.hex2Binary(spec);
		byte[] buffer = new byte[1024*1024];
		try {
			bais.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
	/**
	 * 
	 * @param ra 赤经，范围：[0°-360]，单位：度
	 * @param dec 赤纬，范围：[-90,90]，单位：度
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
	 * @param ra 赤经，范围：[0°-360]，单位：度
	 * @param dec 赤纬，范围：[-90,90]，单位：度
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
