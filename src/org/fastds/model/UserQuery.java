package org.fastds.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="userquery")
public class UserQuery {
	
	private String queryId;
	private String username;
	private String queryString;
	private String queryTime;
	
	@Id
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	@Column
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	@Column
	public String getQueryTime() {
		return queryTime;
	}
	
	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}
	@Override
	public String toString() {
		return "UserQuery [queryId=" + queryId + ", username=" + username
				+ ", queryString=" + queryString + ", queryTime=" + queryTime
				+ "]";
	}
}
