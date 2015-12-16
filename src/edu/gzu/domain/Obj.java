package edu.gzu.domain;

public class Obj {
	double ra;
	double dec;
	int flag;
	long objID;
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
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public long getObjID() {
		return objID;
	}
	public void setObjID(long objID) {
		this.objID = objID;
	}
	@Override
	public String toString() {
		return "SpecObj [ra=" + ra + ", dec=" + dec + ", flag=" + flag
				+ ", specObjID=" + objID + "]";
	}

}
