package edu.gzu.domain;

public class PhotoTag {
	private Double ra = null;
	private Double dec = null;
	private Integer run= null;
	private Short rerun = null;
	private Short camcol = null;
	private Short field = null;
	private Long fieldID = null;
	private Long specObjID = null;
	private Long objID = null;
	
	public Double getRa() {
		return ra;
	}
	public void setRa(Double ra) {
		this.ra = ra;
	}
	public Double getDec() {
		return dec;
	}
	public void setDec(Double dec) {
		this.dec = dec;
	}
	public Integer getRun() {
		return run;
	}
	public void setRun(Integer run) {
		this.run = run;
	}
	public Short getRerun() {
		return rerun;
	}
	public void setRerun(Short rerun) {
		this.rerun = rerun;
	}
	public Short getCamcol() {
		return camcol;
	}
	public void setCamcol(Short camcol) {
		this.camcol = camcol;
	}
	public Short getField() {
		return field;
	}
	public void setField(Short field) {
		this.field = field;
	}
	public Long getFieldID() {
		return fieldID;
	}
	public void setFieldID(Long fieldID) {
		this.fieldID = fieldID;
	}
	public Long getSpecObjID() {
		return specObjID;
	}
	public void setSpecObjID(Long specObjID) {
		this.specObjID = specObjID;
	}
	public Long getObjID() {
		return objID;
	}
	public void setObjID(Long objID) {
		this.objID = objID;
	}

}
