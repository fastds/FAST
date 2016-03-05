package edu.gzu.domain;

public class PhotoTag {
	private double ra ;
	private double dec;
	private short run;
	private short rerun;
	private byte camcol;
	private short field;
	private long fieldID;
	private long specObjID;
	private long objID;
	
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
	public short getRun() {
		return run;
	}
	public void setRun(short run) {
		this.run = run;
	}
	public short getRerun() {
		return rerun;
	}
	public void setRerun(short rerun) {
		this.rerun = rerun;
	}
	public byte getCamcol() {
		return camcol;
	}
	public void setCamcol(byte camcol) {
		this.camcol = camcol;
	}
	public short getField() {
		return field;
	}
	public void setField(short field) {
		this.field = field;
	}
	public long getFieldID() {
		return fieldID;
	}
	public void setFieldID(long fieldID) {
		this.fieldID = fieldID;
	}
	public long getSpecObjID() {
		return specObjID;
	}
	public void setSpecObjID(long specObjID) {
		this.specObjID = specObjID;
	}
	public long getObjID() {
		return objID;
	}
	public void setObjID(long objID) {
		this.objID = objID;
	}

}
