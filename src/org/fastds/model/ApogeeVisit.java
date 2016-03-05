package org.fastds.model;

public class ApogeeVisit {
	 /* TABLE apogeeVisit */
    public String visit_id;
    public String plate;
    public long mjd;
    public long fiberid;
    public String dateobs;
    public float vrel;

    /* Queries */
    public final String BASE_QUERY = "select  visit_id,  plate, mjd, fiberid, dateobs, vrel"
	+"from apogeeVisit"
	+"where apogee_id = @id"
	+"order by dateobs";

	public String getVisit_id() {
		return visit_id;
	}

	public void setVisit_id(String visit_id) {
		this.visit_id = visit_id;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public long getMjd() {
		return mjd;
	}

	public void setMjd(long mjd) {
		this.mjd = mjd;
	}

	public long getFiberid() {
		return fiberid;
	}

	public void setFiberid(long fiberid) {
		this.fiberid = fiberid;
	}

	public String getDateobs() {
		return dateobs;
	}

	public void setDateobs(String dateobs) {
		this.dateobs = dateobs;
	}

	public float getVrel() {
		return vrel;
	}

	public void setVrel(float vrel) {
		this.vrel = vrel;
	}

	public String getBASE_QUERY() {
		return BASE_QUERY;
	}
    
}

