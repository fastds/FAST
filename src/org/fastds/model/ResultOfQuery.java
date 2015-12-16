package org.fastds.model;

public class ResultOfQuery {
	
	private int position;
	private int rows;
	private long dataSize=0;
	public long getDataSize() {
		return dataSize;
	}
	public String getDataSizeStr()
	{
		long size = dataSize;
		if(size==0)
			return "0";
		StringBuilder sb = new StringBuilder();
		if(size/(1024*1024) > 0)
		{
			sb.append(size/(1024*1024)+"M");
			size = size%(1024*1024);
			if(size/1024>0)
			{
				sb.append(size/1024+"K");
				sb.append(size%1024+"B");
				return sb.toString();
			}
			sb.append("0K");
			sb.append(size%1024+"B");
			return sb.toString();
		}
		else if(size/1024 > 0){
			sb.append(size/1024+"K");
			size = size % 1024;
			sb.append(size+"B");
			return sb.toString();
		}
		else
			return sb.append(size+"B").toString();
	}
	public void setDataSize(long dataSize) {
		this.dataSize = dataSize;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	private String showresult;
	
	private int showPage;
	
	public int getShowPage() {
		return showPage;
	}

	public void setShowPage(int showPage) {
		this.showPage = showPage;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getShowresult() {
		return showresult;
	}

	public void setShowresult(String showresult) {
		this.showresult = showresult;
	}

	
	

}
