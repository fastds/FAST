package edu.gzu.domain;

public class PhotoObjAll {
	
	private long id;
	private double ra;
	private double dec;
	private double radius;
	private int type;
	private double u;
	private double g;
	private double r;
	private double i;
	private double z;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public double getU() {
		return u;
	}
	public void setU(double u) {
		this.u = u;
	}
	public double getG() {
		return g;
	}
	public void setG(double g) {
		this.g = g;
	}
	public double getR() {
		return r;
	}
	public void setR(double r) {
		this.r = r;
	}
	public double getI() {
		return i;
	}
	public void setI(double i) {
		this.i = i;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	public String getType() {
		return getTypeName(this.type);
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTypeName(int type)
	{
		switch(type)
		{
			case 0:
				return "UNKNOWN";
			case 1:
				return "COSMIC_RAY";
			case 2:
				return "DEFECT";
			case 3:
				return "GALAXY";
			case 4:
				return "GHOST";
			case 5:
				return "KNOWNOBJ";
			case 6:
				return "STAR";
			case 7:
				return "TRAIL";
			case 8:
				return "SKY";
			case 9:
				return "NOTATYPE";
			default:
				return "NULL";
		}
	}
	@Override
	public String toString() {
		return "PhotoObjAll [id=" + id + ", ra=" + ra + ", dec=" + dec
				+ ", radius=" + radius + ", type=" + type + ", u=" + u + ", g="
				+ g + ", r=" + r + ", i=" + i + ", z=" + z + "]";
	}

}
