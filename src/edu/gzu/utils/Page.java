package edu.gzu.utils;

import java.util.List;
import java.util.Map;

import edu.gzu.domain.JpegInfo;

public class Page {
	private int currentPage;//当前页
	private int pageSize;	//每一页记录数
	private int totalRecord;
	private Map<String,String> records;
	private List<JpegInfo> JpegInfo;
	/**
	 * 
	 * @return 总页数
	 */
	public int getTotalPages()
	{
		return totalRecord%pageSize==0?totalRecord/pageSize:totalRecord/pageSize+1;
	}
	/**
	 * 
	 * @return 返回当前页码
	 */
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	/**
	 * 
	 * @return 每页记录数
	 */
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * 
	 * @return 返回总记录数
	 */
	public int getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}
	/**
	 * 
	 * @return 返回当前页的记录
	 */
	public Map<String, String> getRecords() {
		return records;
	}
	public void setRecords(Map<String, String> records) {
		this.records = records;
	}
	/**
	 * @return 返回获取jpeg图像的相关参数信息
	 */
	public List<JpegInfo> getJpegInfo() {
		return JpegInfo;
	}
	public void setJpegInfo(List<JpegInfo> jpegInfo) {
		JpegInfo = jpegInfo;
	}
	

}
