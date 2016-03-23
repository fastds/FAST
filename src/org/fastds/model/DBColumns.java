package org.fastds.model;

import java.util.List;

public class DBColumns {
	String tablename ;
	List<String> attrNames ;
	List<String> descriptions;
	public DBColumns(String tablename)
	{
		this.tablename = tablename;
	}
	public List<String> getAttrNames() {
		return attrNames;
	}
	public void setAttrNames(List<String> attrNames) {
		this.attrNames = attrNames;
	}
	public List<String> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(List<String> descriptions) {
		this.descriptions = descriptions;
	}
	public String getTablename() {
		return tablename;
	}
	
	
}
