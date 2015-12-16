package org.fastds.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.ws.rs.FormParam;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name = "user")
public class User {

    @FormParam(value = "id")
    private int id;

    @FormParam(value = "userName")
    private String userName;

    @FormParam(value = "password")
    private String password;

    @FormParam(value = "email")
    private String email;

    @FormParam(value = "researchArea")
    private String researchArea;

    @FormParam(value = "gender")
    private String gender;

    @FormParam(value = "description")
    private String description;

    @FormParam(value = "cellphone")
    private String cellphone;

    @FormParam(value = "verifyCode")
    private String verifyCode;

    @FormParam(value = "activated")
    private boolean activated;

   
    public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public String getCellphone() {
	return cellphone;
    }

    public String getDescription() {
	return description;
    }

    public String getEmail() {
	return email;
    }

    public String getGender() {
	return gender;
    }

    @Id
    @GeneratedValue
    public int getId() {
	return id;
    }

    public String getPassword() {
	return password;
    }

    public String getResearchArea() {
	return researchArea;
    }

    public String getUserName() {
	return userName;
    }

    public String getVerifyCode() {
	return verifyCode;
    }

   
    public void setCellphone(String cellphone) {
	this.cellphone = cellphone;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public void setGender(String gender) {
	this.gender = gender;
    }

    public void setId(int id) {
	this.id = id;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public void setResearchArea(String researchArea) {
	this.researchArea = researchArea;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public void setVerifyCode(String verifyCode) {
	this.verifyCode = verifyCode;
    }

    @Override
    public String toString() {
	return "User [username=" + userName + ", password=" + password
		+ ", email=" + email + ", researchArea=" + researchArea
		+ ", gender=" + gender + ", description=" + description
		+ ", cellphone=" + cellphone + ", verifyCode=" + verifyCode
		+ ", activated=" + activated + "]";
    }
}
