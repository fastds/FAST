package edu.gzu.domain;

public class User {
	private String username;
	private String password;
	private String email;
	private String researchArea;
	private String gender;
	private String description;
	private String cellphone;
	private String verifyCode;
	private int activated;
	
	public int getActivated() {
		return activated;
	}
	public void setActivated(int activated) {
		this.activated = activated;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getResearchArea() {
		return researchArea;
	}
	public void setResearchArea(String researchArea) {
		this.researchArea = researchArea;
	}
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password
				+ ", email=" + email + ", researchArea=" + researchArea
				+ ", gender=" + gender + ", description=" + description
				+ ", cellphone=" + cellphone + ", verifyCode=" + verifyCode
				+ ", activated=" + activated + "]";
	}

}
