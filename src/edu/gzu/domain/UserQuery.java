package edu.gzu.domain;

public class UserQuery {
	private String queryId;
	private String username;
	private String queryString;
	private String queryTime;
	public String getQueryId() {
		return queryId;
	}
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
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
