package edu.gzu.domain;

import java.util.Arrays;

public class Image {
	private float ra;
	private float dec;
	private float scale;
	public float getScale() {
		return scale;
	}
	public void setScale(float scale) {
		this.scale = scale;
	}
	private int height;
	private int width;
	private String opt;
	private byte[] data;
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public float getRa() {
		return ra;
	}
	public void setRa(float ra) {
		this.ra = ra;
	}
	public float getDec() {
		return dec;
	}
	public void setDec(float dec) {
		this.dec = dec;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	@Override
	public String toString() {
		return "Image [ra=" + ra + ", dec=" + dec + ", scale=" + scale
				+ ", height=" + height + ", width=" + width + ", opt=" + opt
				+ ", data=" + Arrays.toString(data) + "]";
	}
	

}
