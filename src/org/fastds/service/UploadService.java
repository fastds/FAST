package org.fastds.service;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadService {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void upload(HttpServletRequest request) {
	String savePath = "D://upload/";
	File f1 = new File(savePath);
	if (!f1.exists()) {
	    f1.mkdirs();
	}
	DiskFileItemFactory fac = new DiskFileItemFactory(500 * 1024 * 1024,
		new File("F:/temp"));
	ServletFileUpload upload = new ServletFileUpload(fac);
	upload.setHeaderEncoding("utf-8");
	List fileList = null;
	try {
	    fileList = upload.parseRequest(request);
	} catch (FileUploadException ex) {
	    return;
	}
	Iterator<FileItem> it = fileList.iterator();
	String name = "";
	while (it.hasNext()) {
	    FileItem item = it.next();
	    if (!item.isFormField()) {
		name = item.getName();
		System.out.println(name);
		long size = item.getSize();

		String type = item.getContentType();
		System.out.println(size / 1024 / 1024 + "M:::" + type);
		if (name == null || name.trim().equals("")) {
		    continue;
		}
		File file = null;
		do {
		    file = new File(f1, name);
		} while (file.exists());
		File saveFile = new File(f1, name);
		try {
		    item.write(saveFile);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
    }

}
