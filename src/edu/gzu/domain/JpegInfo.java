package edu.gzu.domain;

public class JpegInfo {
	private double ra;
	private double dec;
	private String url;
	private String info;
	public double getRa() {
		return ra;
	}
	public void setRa(double ra) {
		this.ra = ra;
	}
	public double getDec() {
		return dec;
	}
	public void setDec(double dec) {
		this.dec = dec;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getInfo()
	{
		return info;
	}
}
