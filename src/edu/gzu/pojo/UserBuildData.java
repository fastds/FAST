/**
 * @author jamesmarva jamesmarva@163.com
 * 
 */

package edu.gzu.pojo;

public class UserBuildData {

	private String username;
	
	private String buildtime;

	private int ipfirst;
	
	private int ipsecond;
	
	private int ipthird;
	
	private int ipfourth;
	
	private int isbuild;
	
	private int userport;
	
	private int spacestatus;
	
	public int getUserport() {
		return userport;
	}

	public void setUserport(int userport) {
		this.userport = userport;
	}

	public int getSpacestatus() {
		return spacestatus;
	}

	public void setSpacestatus(int spacestatus) {
		this.spacestatus = spacestatus;
	}

	public String getBuildtime() {
		return buildtime;
	}

	public void setBuildtime(String buildtime) {
		this.buildtime = buildtime;
	}

	public int getIsbuild() {
		return isbuild;
	}

	public void setIsbuild(int isbuild) {
		this.isbuild = isbuild;
	}

	public int getIpfirst() {
		return ipfirst;
	}

	public void setIpfirst(int ipfirst) {
		this.ipfirst = ipfirst;
	}

	public int getIpsecond() {
		return ipsecond;
	}

	public void setIpsecond(int ipsecond) {
		this.ipsecond = ipsecond;
	}

	public int getIpthird() {
		return ipthird;
	}

	public void setIpthird(int ipthird) {
		this.ipthird = ipthird;
	}

	public int getIpfourth() {
		return ipfourth;
	}

	public void setIpfourth(int ipfourth) {
		this.ipfourth = ipfourth;
	}
	
	public String getBuildTime() {
		return buildtime;
	}

	public void setBuildTime(String buildTime) {
		this.buildtime = buildTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String toString() {
		return "UserBuildData [username = " + username + ", buildtime = "
				+ buildtime + ", ipfrist = " + ipfirst + ", ipsecond = "
				+ ipsecond + ", ipthird = " + ipthird + ", ipfourth = "
				+ ipfourth + ", isbuild = " + isbuild + ", userport = "
				+ userport + ", spacestatus = " + spacestatus + "]";
	}

}
