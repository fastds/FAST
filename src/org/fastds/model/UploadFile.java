package org.fastds.model;

public class UploadFile {
    private String fid;
    private String fileName;
    private String username;
    private String uploadTime;
    private long size;
    private String path;

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public long getSize() {
	return size;
    }

    public void setSize(long size) {
	this.size = size;
    }

    public String getFid() {
	return fid;
    }

    public void setFid(String fid) {
	this.fid = fid;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getUploadTime() {
	return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
	this.uploadTime = uploadTime;
    }

    @Override
    public String toString() {
	return "UploadFile [fid=" + fid + ", fileName=" + fileName
		+ ", username=" + username + ", uploadTime=" + uploadTime
		+ ", size=" + size + ", path=" + path + "]";
    }

}
